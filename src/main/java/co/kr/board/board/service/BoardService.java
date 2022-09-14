package co.kr.board.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.repsoitory.BoardRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardService {
	
	private final BoardRepository repos;
	
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
	public Page<Board> findAll(Pageable pageable) throws Exception{
		
		Page<Board> articlelist= repos.findAll(pageable);
		
		return articlelist;
	}
	
	
	//검색
	@Transactional
	public Page<Board> findByboardTitleContaining(String keyword,Pageable pageable){
		
		Page<Board>articlelist = repos.findByboardTitleContaining(keyword, pageable);
		
		return articlelist;
	};
	
	@Transactional
	public Integer boardsave(BoardDto.BoardRequestDto dto)throws Exception{
		return repos.save(dto.toEntity()).getBoardId();
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
				
		articlelist.ifPresent(t->{
			
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
	
}
