package co.kr.board.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.config.exception.dto.ErrorCode;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.file.service.FileService;
import co.kr.board.login.domain.Member;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardService {
	
	private final BoardRepository repos;		
	
	private final FileService fileservice;
	
	/*
	 * 글 목록 전체 조횐 
	 * 
	 */
	@Transactional
	public List<BoardDto.ResponseDto>findAll() throws Exception{
	
		List<Board> articlelist= (List<Board>)repos.findAll();
		
		List<BoardDto.ResponseDto> list = new ArrayList<>();
		
		for(Board article : articlelist) {
			BoardDto.ResponseDto boardDto = BoardDto.ResponseDto
												.builder()
												.board(article)
												.build();	
			list.add(boardDto);
		}	
		return list;
	}
	
	
    /*
	* 글목록 전체 조회(페이징)
	* @Param Pageable
	* 
	*/
	@Transactional
	public Page<BoardDto.ResponseDto> findAll(Pageable pageable) throws Exception{
		
		Page<Board> articlelist = repos.findAll(pageable);
				
		Page<BoardDto.ResponseDto> list = articlelist.map(board ->new BoardDto.ResponseDto(board));
		
		return list;
	}
	
	/*
	 * 페이징 + 검색기능
	 * @Param keyword
	 * @Param pageable
	 */
	@Transactional
	public Page<BoardDto.ResponseDto>findAllSearch(String keyword,Pageable pageable)throws Exception{
		
		Page<Board>allSearch = repos.findAllSearch(keyword, pageable);
				
		Page<BoardDto.ResponseDto>list = allSearch.map(
					board -> new BoardDto.ResponseDto(board));
 		return list;
	}
	
    /*
	* 글 등록 
	* @Param BoardRequestDto 
	* @Param Member
	* 시큐리티 로그인 후 이용
	* @Valid BindingResult Exception : 게시글 제목, 내용 미작성시 유효성 검사
	*/
	@Transactional
	public Integer boardsave(BoardDto.BoardRequestDto dto, Member member,List<MultipartFile>multiparts)throws Exception{
		
		Board board = Board
				.builder()
				.member(member)
				.boardTitle(dto.getBoardTitle())
				.boardAuthor(member.getUsername())
				.boardContents(dto.getBoardContents())
				.readcount(0)
				.createdat(dto.getCreatedAt())
				.build();
		
		repos.save(board);
		
		return board.getId();
	}
	
    /*
    * 글 목록 단일 조횐 
    * @Param boardId
    * @Exception :게시글이 존재하지 않음.(NOT_BOARDDETAIL)
    */
	@Transactional
	public BoardDto.ResponseDto getBoard(Integer boardId)throws Exception{
		
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARDDETAIL)));
		
		//글 조회
		Board board = articlelist.get();
				
		//게시글 조회수 증가
		board.countUp();		
		
		return BoardDto.ResponseDto
			   .builder()
			   .board(board)
			   .build();
	}
	
    /*
	* 게시글 삭제
	* @Param boardId
	* @Param Member
	* @Exception : 회원글이 존재하지 않은 경우 NOT_BOARDDETAIL
	* @Exception : 글작성자와 로그인한 유저의 아이디가 일치하지 않으면 NOT_USER
	*/
	@Transactional
	public void deleteBoard(Integer boardId , Member member)throws Exception{
		
		Optional<Board> board = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARDDETAIL)));
		
		String boardAuthor = board.get().getBoardAuthor();
		String loginUser = member.getUsername();
		
		//글 작성자와 로그인한 유저의 아이디가 동일하지 않으면 Exception
		if(!boardAuthor.equals(loginUser)) {
			throw new CustomExceptionHandler(ErrorCode.NOT_USER);
		}
		
		repos.deleteById(board.get().getId());
	}
	
    /*
	* 글 수정 기능 
	* @Param BoardRequestDto 
	* @Param boardId
	* @Param Member
	* @Exception : 게시글이 존재하지 않습니다. NOT_BOARDDETAIL 
	* @Exception : 글작성자와 로그인한 유저의 아이디가 일치하지 않으면 NOT_USER
	*/
	@Transactional
	public Integer updateBoard(
			Integer boardId, 
			BoardDto.BoardRequestDto dto,
			Member member)throws Exception{
		
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARDDETAIL)));
		Board result = articlelist.get();
		
		String boardAuthor = result.getBoardAuthor();
		String loginUser = member.getUsername();
		
		if(!boardAuthor.equals(loginUser)) {
			throw new CustomExceptionHandler(ErrorCode.NOT_USER);
		}
		
		articlelist.ifPresent(t -> {
			
			if(dto.getBoardTitle() != null) {
				t.setBoardTitle(dto.getBoardTitle());
			}
			if(dto.getBoardContents() != null) {
				t.setBoardContents(dto.getBoardContents());
			}
			if(dto.getReadCount() !=null) {
				t.setReadCount(dto.getReadCount());
			}
			if(dto.getCreatedAt() != null) {
				t.setCreatedAt(dto.getCreatedAt());
			}
			
			this.repos.save(t);
		});
		
		return boardId;
	}
}
