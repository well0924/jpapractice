package co.kr.board.domain.Dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import co.kr.board.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class CommentDto {
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class CommentRequestDto {
								
		@NotBlank(message = "내용을 입력해 주세요.")
		private String replyContents;
						
		//@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
		@Builder
		public CommentRequestDto(String replyContents,LocalDateTime createdAt){
			this.replyContents = replyContents;
			this.createdAt = createdAt;
		}
	}
	
	@Getter
	@ToString
	public static class CommentResponseDto{
		
		private Integer replyId;
		
		private Integer boardId;
		
		private String replyContents;
		
		private String replyWriter;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
		@Builder
		@QueryProjection
		public CommentResponseDto(Comment comment){
			this.replyId = comment.getId();
			this.boardId = comment.getBoard().getId();
			this.replyWriter = comment.getMember().getUsername();
			this.replyContents = comment.getReplyContents();
			this.createdAt = comment.getCreatedAt();
		}
	}
}
