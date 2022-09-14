package co.kr.board.reply.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import co.kr.board.board.domain.Board;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.reply.domain.Comment;
import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.repository.CommentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
	
	private final CommentRepository repository;
	
	private final BoardRepository boardrepository;
	
	@Transactional
	public List<CommentDto.CommentResponseDto>findAll(Integer boardId)throws Exception{
		
		List<Comment>list = repository.findAll();
		
		List<CommentDto.CommentResponseDto> replylist = new ArrayList<>();
		
		for(Comment reply : list) {
			CommentDto.CommentResponseDto commentdto = CommentDto.CommentResponseDto
													   .builder()
													   .replyId(reply.getReplyId())
													   .replyContents(reply.getReplyContents())
													   .boardId(reply.getBoard().getBoardId())
													   .createdAt(reply.getCreatedAt())
													   .build();
			
			replylist.add(commentdto);
		}
		
		return replylist;
	}
	
	@Transactional
	public Integer replysave(CommentDto.CommentRequestDto dto)throws Exception{
		Optional<Board>detailBoard = boardrepository.findById(dto.getBoardId());
		
		dto.setBoard(detailBoard.get());
		
		return repository.save(dto.toEntity()).getReplyId();
	}
	
	@Transactional
	public void replydelete(Integer replyId)throws Exception{	
		
		repository.deleteById(replyId);
	}
}
