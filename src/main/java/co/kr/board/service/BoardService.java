package co.kr.board.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.kr.board.domain.AttachFile;
import co.kr.board.domain.Board;
import co.kr.board.domain.Category;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.repository.CategoryRepository;
import co.kr.board.domain.Dto.AttachDto;
import co.kr.board.repository.AttachRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.board.repository.BoardRepository;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Member;
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
	private final FileHandler fileHandler;

	/*
	 * 글 목록 전체 조회
	 * 카테고리별로 조회하기.
	 */
	@Transactional(readOnly = true)
	public List<BoardDto.BoardResponseDto>findAll(){
	
		List<Board> articlelist= repos.findAll();
		
		List<BoardDto.BoardResponseDto> list = new ArrayList<>();
		
		for(Board article : articlelist) {
			BoardDto.BoardResponseDto boardDto = BoardDto.BoardResponseDto
												.builder()											
												.board(article)
												.build();	
			list.add(boardDto);
		}	
		return list;
	}

    /*
	* 글목록 전체 조회(페이징+카테고리)
	* @Param Pageable
	* 
	*/
	@Transactional(readOnly = true)
	public Page<BoardDto.BoardResponseDto>findAllPage(Pageable pageable, String categoryName){
		Page<Board>boards = repos.findAllByCategoryName(pageable,categoryName);
		return boards.map(board->new BoardDto.BoardResponseDto(board));
	}
	/*
	 * 페이징 + 검색기능
	 * @Param searchVal,
	 * @Param pageable
	 *
	 * 게시물에서 검색.
	 */
	@Transactional(readOnly = true)
	public Page<BoardDto.BoardResponseDto>findAllSearch(String searchVal, Pageable pageable){
		Page<BoardDto.BoardResponseDto>list = repos.findByAllSearch(searchVal,pageable);
		return list;
	}

	/*
	* 글 등록 (파일 첨부)
	* @Param BoardRequestDto 
	* @Param Member
	* 시큐리티 로그인 후 이용
	* @Valid BindingResult Exception : 게시글 제목, 내용 미작성시 유효성 검사
	*/
	@Transactional
	public Integer boardsave(BoardDto.BoardRequestDto dto, Member member ,Integer categoryId,List<MultipartFile>files)throws Exception{
		//회원이 아니면 사용불가
		if(member == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		//카테고리 조회
		Category category = categoryRepository.findById(categoryId).orElseThrow(()->new CustomExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND));

		Board board = Board
				.builder()
				.member(member)
				.boardTitle(dto.getBoardTitle())
				.boardAuthor(member.getUsername())
				.boardContents(dto.getBoardContents())
				.readcount(0)
				.category(category)
				.createdat(dto.getCreatedAt())
				.build();

		int InsertResult = repos.save(board).getId();

		List<AttachFile>fileList = fileHandler.parseFileInfo(files);

		log.info(fileList);

		//파일이 없는 경우
		if(fileList == null || fileList.size() == 0){
			return InsertResult;
		}

		//첨부파일이 있는경우
		if(!fileList.isEmpty()){
			for(AttachFile attachFile : fileList){
				//파일 저장
				board.addAttach(attachRepository.save(attachFile));
			}
		}
		return InsertResult;
	}
	
    /*
    * 글 목록 단일 조회
    * @Param boardId
    * @Exception :게시글이 존재하지 않음.(NOT_BOARDDETAIL)
    */
	@Transactional
	public BoardDto.BoardResponseDto getBoard(Integer boardId){
		
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL)));

		//글 조회
		Board board = articlelist.get();
				
		//게시글 조회수 증가->중복 증가 방지필요
		board.countUp();		
		
		return BoardDto.BoardResponseDto
			   .builder()
			   .board(board)
			   .build();
	}
	
    /*
	* 게시글 삭제 (파일 삭제 포함)
	* @Param boardId
	* @Param Member
	* @Exception : 회원글이 존재하지 않은 경우 NOT_BOARDDETAIL
	* @Exception : 글작성자와 로그인한 유저의 아이디가 일치하지 않으면 NOT_USER
	*/
	@Transactional
	public void deleteBoard(Integer boardId , Member member)throws Exception{
		
		if(member == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		//글 조회
		Optional<Board> board = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL)));
		
		String boardAuthor = board.get().getBoardAuthor();
		String loginUser = member.getUsername();
		
		//글 작성자와 로그인한 유저의 아이디가 동일하지 않으면 Exception
		if(!boardAuthor.equals(loginUser)) {
			throw new CustomExceptionHandler(ErrorCode.BOARD_DELETE_DENIED);
		}

		//첨부 파일 조회
		List<AttachDto>list = fileService.filelist(boardId);

		for(int i = 0; i<list.size();i++){

			String filePath = list.get(i).getFilePath();
			File file = new File(filePath);

			//파일 경로가 존재를 하면 해당 위치의 파일을 삭제한다.
			if(file.exists()){
				file.delete();
			}
		}
		//게시물 삭제
		repos.deleteById(board.get().getId());
	}
	
    /*
	* 글 수정 기능 (파일 첨부)
	* @Param BoardRequestDto 
	* @Param boardId
	* @Param Member
	* @Exception : 로그인을 하지 않은경우 ONLY_USER
	* @Exception : 게시글이 존재하지 않습니다. NOT_BOARDDETAIL 
	* @Exception : 글작성자와 로그인한 유저의 아이디가 일치하지 않으면 BOARD_EDITE_DENIED
	*/
	@Transactional
	public Integer updateBoard(Integer boardId,BoardDto.BoardRequestDto dto,Member member,List<MultipartFile>files)throws Exception{
		//로그인 여부
		if(member == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		//글조회
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL)));
		
		Board boardDetail = articlelist.get();
		
		String boardAuthor = boardDetail.getBoardAuthor();
		String loginUser = member.getUsername();
		//글 수정
		boardDetail.updateBoard(dto);
		//회원인지 아닌지 여부
		if(!boardAuthor.equals(loginUser)) {
			throw new CustomExceptionHandler(ErrorCode.BOARD_EDITE_DENIED);
		}

		int updateResult = boardDetail.getId();

		//파일 조회를 하고 해당파일을 수정할 수 있으면 post를 한다.
		List<AttachFile>fileList = fileHandler.parseFileInfo(files);

		if(fileList == null || fileList.size() ==0){
			return updateResult;
		}
		//파일이 있는경우
		if(!fileList.isEmpty()){
			//저장된 파일경로에 있는 파일 삭제
			for (int i=0;i<fileList.size();i++) {
				String filePath = fileList.get(i).getFilePath();
				File file = new File(filePath);
				//파일 경로가 존재를 하면 해당 위치의 파일을 삭제한다.
				if(file.exists()){
					file.delete();
				}
			}
			//새로 파일을 저장한다.
			for(AttachFile attachFile : fileList){
				//파일 저장
				boardDetail.addAttach(attachRepository.save(attachFile));
			}
		}
		return updateResult;
	}
}
