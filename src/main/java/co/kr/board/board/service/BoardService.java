package co.kr.board.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardRequestDto;
import co.kr.board.board.domain.dto.BoardResponseDto;
import co.kr.board.board.repsoitory.BoardRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardService {
	
	private final BoardRepository repos;
	
	//글 목록o.k
	@Transactional
	public List<BoardResponseDto>findAll() throws Exception{
	
		List<Board> articlelist= (List<Board>)repos.findAll();
		
		List<BoardResponseDto> list = new ArrayList<>();
		
		for(Board article : articlelist) {
			
			BoardResponseDto boardDto = BoardResponseDto
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
	
	//글작성o.k
	@Transactional
	public Integer boardsave(BoardRequestDto dto)throws Exception{
		return repos.save(dto.toEntity()).getBoardId();
	}
	
	//글조회o.k
	@Transactional
	public BoardResponseDto getBoard(Integer boardId)throws Exception{
		
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다.")));
		
		//글 조회
		Board board = articlelist.get();
		
		//게시글 조회수 증가
		board.countUp();		
		
		return BoardResponseDto
				.builder()
				.boardId(board.getBoardId())
				.boardTitle(board.getBoardTitle())
				.boardAuthor(board.getBoardAuthor())
				.boardContents(board.getBoardContents())
				.readCount(board.getReadCount())
				.createdAt(board.getCreatedAt())
				.build();
	}
	
	//글 삭제o.k
	@Transactional
	public void deleteBoard(Integer boardId)throws Exception{
		
		repos.deleteById(boardId);
	}
	
	//글 수정o.k
	@Transactional
	public Integer updateBoard(Integer boardId, BoardRequestDto dto)throws Exception{
		
		Optional<Board>articlelist = Optional.ofNullable(repos.findById(boardId).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다.")));
		
		Board board = articlelist.get();
		
		board.update(dto.getBoardTitle(),dto.getBoardContents(), dto.getBoardAuthor(),dto.getReadCount());
		
		return boardId;
	}
}
