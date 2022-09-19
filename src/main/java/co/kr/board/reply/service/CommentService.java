package co.kr.board.reply.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import co.kr.board.board.domain.Board;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.reply.domain.Comment;
import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.domain.dto.CommentDto.CommentResponseDto;
import co.kr.board.reply.repository.CommentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
	
	private final CommentRepository repository;
	
	private final BoardRepository boardrepository;
	
	@Transactional
	public List<CommentResponseDto> findCommentsBoardId(@Param("id") Integer id)throws Exception{
		
		Optional<Board> board = boardrepository.findById(id);
		
		Board bb = board.get();
		
		List<Comment>comment = repository.findCommentsBoardId(id);
		List<CommentDto.CommentResponseDto> list = new ArrayList<>();
		
		for(Comment co : comment) {
			CommentDto.CommentResponseDto dto = CommentDto
					.CommentResponseDto
					.builder()
					.boardId(bb.getBoardId())
					.replyId(co.getReplyId())
					.replyContents(co.getReplyContents())
					.replyWriter(co.getReplyWriter())
					.createdAt(co.getCreatedAt())
					.build();
			
			list.add(dto);
		}
		return list;
	};
	
	@Transactional
	public Integer replysave(CommentDto.CommentRequestDto dto)throws Exception{
		
		Comment reply = dtoToEntity(dto);
		
		repository.save(reply);
		
		return reply.getReplyId();
	}
	
	@Transactional
	public void replydelete(Integer replyId)throws Exception{	
		
		repository.deleteById(replyId);
	}
	
	//entity => dto
	public Comment dtoToEntity(CommentDto.CommentRequestDto dto){
		
		Board board = Board.builder().boardId(dto.getBoardId()).build();
		
		Comment comment = Comment
				.builder()
				.replyId(dto.getReplyId())
				.replyWriter(dto.getReplyWriter())
				.replyContents(dto.getReplyContents())
				.createdAt(dto.getCreatedAt().now())
				.board(board)
				.build();
		
		return comment;
	}
	
	//dto => entity
	public CommentDto.CommentResponseDto entityToDto(Comment comment){
		
		CommentDto.CommentResponseDto commentlist = CommentDto.CommentResponseDto
													.builder()
													.replyId(comment.getReplyId())
													.replyContents(comment.getReplyContents())
													.replyWriter(comment.getReplyWriter())
													.createdAt(comment.getCreatedAt())
													.build();	
		return commentlist;
	}
	
	
}
