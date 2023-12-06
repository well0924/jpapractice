package co.kr.board.service;

import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import co.kr.board.config.Redis.CacheKey;
import co.kr.board.domain.*;
import co.kr.board.domain.Const.SearchType;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.BoardDto.BoardResponseDto;
import co.kr.board.repository.*;
import co.kr.board.domain.Dto.AttachDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@AllArgsConstructor
public class BoardService{
	private final CategoryRepository categoryRepository;
	private final BoardRepository repos;
	private final AttachRepository attachRepository;
	private final FileService fileService;
	private final MemberRepository memberRepository;
	private final HashTagRepository hashTagRepository;
	private final FileHandler fileHandler;

	@Transactional(readOnly = true)
	public Page<BoardResponseDto>findAll(Pageable pageable){
		return repos.findAllBoardList(pageable);
	}
	
    /*
	 * 글목록 전체 조회(페이징+카테고리)
	 * @Param Pageable 페이징 객체
	 * @param categoryName 카테고리 명
	*/
	@Transactional(readOnly = true)
	public Page<BoardResponseDto>findAllPage(Pageable pageable, String categoryName){
		return repos.findAllBoardList(categoryName,pageable);
	}

	/*
	 * 페이징 + 검색기능 + 정렬
	 * @Param searchVal : 검색어,
	 * @Param pageable : 페이징 객체
	 * 게시물 목록에서 검색.
	 */
	@Transactional(readOnly = true)
	public Page<BoardResponseDto>findAllSearch(String searchVal,String searchType, Pageable pageable){
		return repos.findByAllSearch(searchVal, SearchType.toSearch(searchType),pageable);
	}

	/*
	 * 글 등록 (파일 첨부)
	 * @Param BoardRequestDto 게시글 요청 dto
	 * @Param Member 회원 객체
	 * 시큐리티 로그인 후 이용
	 * @Valid BindingResult Exception : 게시글 제목, 내용 미작성시 유효성 검사
	*/
	@Transactional
	public Integer boardsave(BoardDto.BoardRequestDto dto ,Integer categoryId,List<MultipartFile>files)throws Exception{

		Member member = getMember();
		//카테고리 적용
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(()->new CustomExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND));

		Board board = Board
				.builder()
				.member(getMember())
				.boardTitle(dto.getBoardTitle())
				.boardAuthor(member.getUsername())
				.boardContents(dto.getBoardContents())
				.password(dto.getPassword())
				.readcount(0)
				.category(category)
				.createdat(dto.getCreatedAt())
				.build();

		//해시태그 저장
		Set<HashTag>hashtags = new HashSet<>();
		//해시태그가 있으면 해시태그의 명칭을 넣기
		for (String hashtagName : dto.getHashTagName()) {

			Optional<HashTag> existingHashTag = hashTagRepository.findByHashtagName(hashtagName);

			log.info(existingHashTag);

			HashTag hashTag = existingHashTag.orElseGet(() -> {
				HashTag newHashTag = HashTag
						.builder()
						.hashtagName(hashtagName)
						.createdAt(LocalDateTime.now())
						.build();
				return newHashTag;
			});
			hashtags.add(hashTag);

			log.info(hashtagName);
		}
		Set<BoardHashTag>boardHashTags = new HashSet<>();
		for(HashTag hashTag:hashtags){
			BoardHashTag boardHashTag = BoardHashTag
					.builder()
					.board(board)
					.hashTag(hashTag)
					.build();
			boardHashTags.add(boardHashTag);
		}
		board.setTag(boardHashTags);

		log.info(board);
		//파일 첨부
		List<AttachFile>fileList = fileHandler.parseFileInfo(files);
		log.info(fileList);
		int InsertResult = repos.save(board).getId();
		//파일 처리
		AttachFile(InsertResult,fileList,board);
		
		return InsertResult;
	}
	
    /*
     * 글 목록 단일 조회
     * @Param boardId
     * @Exception :게시글이 존재하지 않음.(NOT_BOARD_DETAIL)
    */
	@Transactional
	@Cacheable(value = CacheKey.BOARD,key = "#boardId",unless = "#result == null")
	public BoardResponseDto getBoard(Integer boardId){
		//글 조회
		Optional<BoardResponseDto>boardDetail = Optional
				.ofNullable(repos.findByBoardDetail(boardId)
				.orElseThrow(() -> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL)));

		if(boardDetail.isPresent()){
			updateReadCount(boardId);
		}

		return boardDetail.get();
	}

	/*
	 * 게시글 조회수 증가.
	 * @Param boardId 게시글 번호
	 */
	public void updateReadCount(Integer boardId){
		repos.updateReadCount(boardId);
	}

	/*
	 * 게시글 삭제 (파일 삭제 포함)
	 * @Param boardId 게시물 번호
	 * @Param Member 회원 객체
	 * @Exception : 회원글이 존재하지 않은 경우 NOT_BOARD_DETAIL
	 * @Exception : 글작성자와 로그인한 유저의 아이디가 일치하지 않으면 NOT_USER
	*/
	@Transactional
	@CacheEvict(value = CacheKey.BOARD,key = "#boardId")
	public void deleteBoard(Integer boardId)throws Exception{

		Member member = getMember();

		Board board = validateMember(boardId,member);

		List<AttachDto>list = fileService.filelist(boardId);

		for(int i = 0; i<list.size();i++){

			String filePath = list.get(i).getFilePath();
			File file = new File(filePath);

			if(file.exists()){
				file.delete();
			}
		}
		//게시글 삭제
		repos.deleteById(board.getId());
	}
	
    /*
	 * 글 수정 기능 (파일 첨부)
	 * @Param BoardRequestDto 게시물 요청 dto
	 * @Param boardId 게시물 번호
	 * @Param Member 회원 객체
	 * @Exception : 로그인을 하지 않은경우 ONLY_USER
	 * @Exception : 게시글이 존재하지 않습니다. NOT_BOARD_DETAIL
	 * @Exception : 글작성자와 로그인한 유저의 아이디가 일치하지 않으면 BOARD_EDITE_DENIED
	*/
	@Transactional
	public Integer updateBoard(Integer boardId,BoardDto.BoardRequestDto dto,List<MultipartFile>files)throws Exception{

		Member member = getMember();

		Board boardDetail =	validateMember(boardId,member);

		//해시태그 저장
		Set<HashTag>hashtags = new HashSet<>();
		//해시태그가 있으면 해시태그의 명칭을 넣기
		for (String hashtagName : dto.getHashTagName()) {

			Optional<HashTag> existingHashTag = hashTagRepository.findByHashtagName(hashtagName);

			log.info(existingHashTag);

			HashTag hashTag = existingHashTag.orElseGet(() -> {
				HashTag newHashTag = HashTag
						.builder()
						.hashtagName(hashtagName)
						.createdAt(LocalDateTime.now())
						.build();
				return newHashTag;
			});
			hashtags.add(hashTag);

			log.info(hashtagName);
		}

		Set<BoardHashTag>boardHashTags = new HashSet<>();

		for(HashTag hashTag:hashtags){
			BoardHashTag boardHashTag = BoardHashTag
					.builder()
					.board(boardDetail)
					.hashTag(hashTag)
					.build();
			boardHashTags.add(boardHashTag);
		}

		boardDetail.setTag(boardHashTags);

		//게시글 수정
		boardDetail.updateBoard(dto);
		
		int updateResult = boardDetail.getId();
		
		List<AttachFile>fileList = fileHandler.parseFileInfo(files);

		AttachFile(updateResult,fileList,boardDetail);

		return updateResult;
	}

	/*
	 * 회원이 작성한 글목록
	 * @Param String username 회원의 아이디
	 * @Param Pageable 페이징 객체
	 * @return list
	 */
	@Transactional
	public Page<BoardResponseDto>memberArticle(String username, Pageable pageable){
		return repos.findByAllContents(username,pageable);
	}

	/*
	 * 최근에 작성한 글(5개)
	 */
	@Transactional(readOnly = true)
	public List<BoardResponseDto>findBoardTop5(){
		return repos.findTop5ByOrderByBoardIdDescCreatedAtDesc();
	}

	/*
	 * 게시글 전체 갯수
	 */
	@Transactional(readOnly = true)
	public Integer articleCount(){
		return repos.ArticleCount();
	}

	/*
	 * 게시글 카테고리별 갯수
	 */
	@Transactional(readOnly = true)
	public Integer categoryCount(String categoryName){
		return repos.categoryCount(categoryName);
	}

	/*
	  * 게시글 이전글/다음글 가져오기.
	  * @param boardId 게시글 번호
	 */
	@Transactional(readOnly = true)
	public List<BoardResponseDto>articleNextPreviousBoard(Integer boardId){
		return repos.findNextPreviousBoard(boardId);
	}
	
	/*
	 * 게시글 선택 삭제
	 * 게시글 관리자 페이지에서 게시글을 선택삭제 할 수 있는 기능
	 * @param boardId 게시글 번호들
	 */
	@Transactional
	public void boardSelectDelete(List<Integer>boardId){
		IntStream.range(0,boardId.size())
				.mapToObj(i->boardId)
				.forEach(repos::deleteAllByBoard);
	}

	/*
	 * 게시글 비밀번호 확인
     */
	@Transactional(readOnly = true)
	public BoardDto.BoardResponseDto passwordCheck(String password, Integer boardId){
		return repos.passwordCheck(password,boardId);
	}

	/*
	 * 비밀글 전환
	 * 게시글 관리자 페이지에서 공개글을 비밀글로 변환하는 기능
	 * @Param id 게시글 번호
	 */
	public void changeSecretBoard(Integer id){
		//랜덤으로 4자리 비밀번호를 발급
		String randomPassword = getRandomPassword(4);
		Optional<Board>detail = Optional
				.ofNullable(repos
						.findById(id)
						.orElseThrow(() -> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL)));

		if(detail.get().getPassword()==null||detail.get().getPassword()==""){
			detail.get().setPassword(randomPassword);
			repos.save(detail.get());
		}
	}

	/*
	 * 비밀번호 초기화
	 * 게시글 관리자 페이지에서 비밀번호를 초기화하는 기능
	 * @param boardId 게시글 번호
 	 */
	public void passwordReset(Integer boardId){
		Optional<Board> board = Optional
				.ofNullable(repos
						.findById(boardId)
						.orElseThrow(() -> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL)));

		//비밀번호가 있으면 비밀번호를 초기화
		if(board.get().getPassword()!=null){
			board.get().setPassword(null);
			repos.save(board.get());
			log.info("초기화 값::"+board.get().getPassword());
		}
	}

	/*
	* 비밀번호 조회여부
	* 게시글 조회시 비밀번호가 있는 경우 비밀번호 확인 페이지에서 비밀번호 확인하는 기능
	* @Param boardId 게시글 번호 
	*/
	@Transactional(readOnly = true)
	public String checkPassword(Integer boardId){
		return repos.boardPasswordCheck(boardId);
	}


	//랜덤 비밀번호 발급(SecuredRandom)
	public String getRandomPassword(int size) {
		char[] charSet = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&' };

		StringBuffer sb = new StringBuffer();
		SecureRandom sr = new SecureRandom();

		sr.setSeed(new Date().getTime());

		int idx = 0;
		int len = charSet.length;
		for (int i=0; i<size; i++) {
			idx = sr.nextInt(len);// 강력한 난수를 발생시키기 위해 SecureRandom 을 사용한다.
			sb.append(charSet[idx]);
		}
		return sb.toString();
	}

	/*
	 * 회원 정보 가져오기.
	 */
	private Member getMember(){

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName().toString();
		log.info(username);
		Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));

		if(member == null){
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		return member;
	}

	/*
	  * 회원 정보 일치 확인
	  * @param boardId 게시물 번호
	  * @param Member 회원객체
	  * @Exception : 로그인을 하지 않은경우 NOT_BOARD_DETAIL
	  * @Exception : 로그인을 하지 않은경우 NOT_USER
	 */
	public Board validateMember(Integer boardId,Member member){

		Board board = repos.findById(boardId).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL));

		String boardWriter = board.getBoardAuthor();
		String loginUser = member.getUsername();

		if(!boardWriter.equals(loginUser)){
			throw new CustomExceptionHandler(ErrorCode.NOT_USER);
		}
		return board;
	}

	/*
	  * 파일 첨부 부분(작성,수정)
	 */
	public int AttachFile(int result, List<AttachFile>files, Board board){

		if(files == null || files.size() ==0){
			return result;
		}

		if(!files.isEmpty()){
			for (int i=0;i<files.size();i++) {
				String filePath = files.get(i).getFilePath();
				File file = new File(filePath);
				if(file.exists()){
					file.delete();
				}
			}
			for(AttachFile attachFile : files){
				board.addAttach(attachRepository.save(attachFile));
			}
		}
		return result;
	}

}
