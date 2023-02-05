package co.kr.board.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.config.exception.dto.ErrorCode;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.login.domain.Member;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardService {
	
	private final BoardRepository repos;		

	/*
	 * 글 목록 전체 조횐 
	 * 
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
	* 글목록 전체 조회(페이징)
	* @Param Pageable
	* 
	*/
	@Transactional(readOnly = true)
	public Page<BoardDto.BoardResponseDto> findAllPage(Pageable pageable) {
		
		Page<Board> articlelist = repos.findAll(pageable);
				
		return articlelist.map(board ->new BoardDto.BoardResponseDto(board));
	}
	
	/*
	 * 페이징 + 검색기능
	 * @Param keyword
	 * @Param pageable
	 */
	@Transactional(readOnly = true)
	public Page<BoardDto.BoardResponseDto>findAllSearch(String keyword,Pageable pageable){
		
		Page<Board>allSearch = repos.findAllSearch(keyword, pageable);

 		return allSearch.map(board -> new BoardDto.BoardResponseDto(board));
	}
	
    /*
	* 글 등록 
	* @Param BoardRequestDto 
	* @Param Member
	* 시큐리티 로그인 후 이용
	* @Valid BindingResult Exception : 게시글 제목, 내용 미작성시 유효성 검사
	*/
	@Transactional
	public Integer boardsave(BoardDto.BoardRequestDto dto, Member member){
		
		//회원이 아니면 사용불가
		if(member == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		
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
	public BoardDto.BoardResponseDto getBoard(Integer boardId){
		
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARDDETAIL)));
		
		//글 조회
		Board board = articlelist.get();
				
		//게시글 조회수 증가
		board.countUp();		
		
		return BoardDto.BoardResponseDto
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
	public void deleteBoard(Integer boardId , Member member){
		
		if(member == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		
		Optional<Board> board = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARDDETAIL)));
		
		String boardAuthor = board.get().getBoardAuthor();

		String loginUser = member.getUsername();
		
		//글 작성자와 로그인한 유저의 아이디가 동일하지 않으면 Exception
		if(!boardAuthor.equals(loginUser)) {
			throw new CustomExceptionHandler(ErrorCode.BOARD_DELETE_DENIED);
		}
		
		repos.deleteById(board.get().getId());
	}
	
    /*
	* 글 수정 기능 
	* @Param BoardRequestDto 
	* @Param boardId
	* @Param Member
	* @Exception : 로그인을 하지 않은경우 ONLY_USER
	* @Exception : 게시글이 존재하지 않습니다. NOT_BOARDDETAIL 
	* @Exception : 글작성자와 로그인한 유저의 아이디가 일치하지 않으면 BOARD_EDITE_DENIED
	*/
	@Transactional
	public Integer updateBoard(Integer boardId, BoardDto.BoardRequestDto dto, Member member){
		
		if(member == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARDDETAIL)));
		
		Board result = articlelist.get();
		
		String boardAuthor = result.getBoardAuthor();
		String loginUser = member.getUsername();
		
		result.updateBoard(dto);
		
		if(!boardAuthor.equals(loginUser)) {
			throw new CustomExceptionHandler(ErrorCode.BOARD_EDITE_DENIED);
		}

		return result.getId();
	}
}
