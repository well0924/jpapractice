package co.kr.board.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.domain.dto.BoardDto.BoardResponseDto;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.login.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@AllArgsConstructor
public class BoardService {
	
	private final BoardRepository repos;		
	
	private final MemberService service;
	
	@Transactional
	public List<BoardDto.BoardResponseDto>findAll() throws Exception{
	
		List<Board> articlelist= (List<Board>)repos.findAll();
		
		List<BoardDto.BoardResponseDto> list = new ArrayList<>();
		
		for(Board article : articlelist) {
			
			BoardDto.BoardResponseDto boardDto = BoardDto.BoardResponseDto
												.builder()
												.boardId(article.getBoardId())
												.boardTitle(article.getBoardTitle())
												.boardContents(article.getBoardContents())
												.boardAuthor(article.getBoardAuthor())
												.readCount(article.getReadCount())
												.createdAt(article.getCreatedAt())
												.build();
			
			list.add(boardDto);
		}
		
		return list;
	}
	
	@Transactional
	public Page<BoardDto.BoardResponseDto> findAll(Pageable pageable) throws Exception{
		
		Page<Board> articlelist = repos.findAll(pageable);
		
		Page<BoardDto.BoardResponseDto> list = articlelist.map(board ->new BoardResponseDto(
				board.getBoardId(),
				board.getBoardAuthor(),
				board.getBoardContents(),
				board.getBoardTitle(),
				board.getReadCount(),
				board.getCreatedAt()));
		
		return list;
	}
	
	//페이징 + 검색기능
	@Transactional
	public Page<BoardDto.BoardResponseDto>findAllSearch(String keyword,Pageable pageable)throws Exception{
		
		Page<Board>allSearch = repos.findAllSearch(keyword, pageable);
		
		Page<BoardDto.BoardResponseDto>list = allSearch.map(
					board -> new BoardResponseDto(
							board.getBoardId(),
							board.getBoardTitle(),
							board.getBoardAuthor(),
							board.getBoardContents(),
							board.getReadCount(),
							board.getCreatedAt()
							));
 		return list;
	}
	
	@Transactional
	public Integer boardsave(BoardDto.BoardRequestDto dto)throws Exception{
		
		Board board = EntityTodto(dto);
		
		repos.save(board);
		
		return board.getBoardId();
	}
	
	@Transactional
	public BoardDto.BoardResponseDto getBoard(Integer boardId)throws Exception{
		
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다.")));
		
		//글 조회
		Board board = articlelist.get();
		
		//게시글 조회수 증가
		board.countUp();		
		
		return BoardDto.BoardResponseDto
			   .builder()
			   .boardId(board.getBoardId())
			   .boardTitle(board.getBoardTitle())
			   .boardAuthor(board.getBoardAuthor())
			   .boardContents(board.getBoardContents())
		       .readCount(board.getReadCount())
			   .createdAt(board.getCreatedAt())
			   .build();
	}
	
	@Transactional
	public void deleteBoard(Integer boardId)throws Exception{
		
		repos.deleteById(boardId);
	}
	
	@Transactional
	public Integer updateBoard(Integer boardId, BoardDto.BoardRequestDto dto)throws Exception{
		
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다.")));
				
		articlelist.ifPresent(t -> {
			
			if(dto.getBoardTitle() != null) {
				t.setBoardTitle(dto.getBoardTitle());
			}
			if(dto.getBoardAuthor() != null) {
				t.setBoardAuthor(dto.getBoardAuthor());
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
	
	//Entity 에서 dto로 변경
	public Board EntityTodto(BoardDto.BoardRequestDto dto) throws Exception {
				
		Board board = Board
				.builder()
				.boardId(dto.getBoardId())
				.boardTitle(dto.getBoardTitle())
				.boardAuthor(dto.getBoardAuthor())
				.boardContents(dto.getBoardContents())
				.readCount(0)
				.writer(dto.getMember())
				.createdAt(LocalDateTime.now())
				.build();
				
		return board;
	}
}
