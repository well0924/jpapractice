package co.kr.board.reply.domain.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.Board;
import co.kr.board.reply.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class CommentDto {
	
	@Getter
	@Setter
	@ToString
	@Builder
	@AllArgsConstructor
	public static class CommentRequestDto {
		
		private Integer replyId;
		
		private String replyContents;
		
		private Board board;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
		public Comment toEntity() {
			return Comment
					.builder()
					.replyId(replyId)
					.replyContents(replyContents)
					.board(board)
					.createdAt(createdAt)
					.build();
		}
	}
	
	@Getter
	@Builder
	@ToString
	@AllArgsConstructor
	public static class CommentResponseDto{
		
		private Integer replyId;
		
		private String replyContents;
		
		private Integer boardId;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
		public CommentResponseDto(Comment comment) {
			
			this.replyId = comment.getReplyId();
			this.replyContents = comment.getReplyContents();
			this.createdAt = comment.getCreatedAt();
			this.boardId = comment.getBoard().getBoardId();
		
		}
	}
}
