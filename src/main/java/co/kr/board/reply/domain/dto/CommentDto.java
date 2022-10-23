package co.kr.board.reply.domain.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class CommentDto {
	
	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	public static class CommentRequestDto {
								
		@NotBlank(message = "내용을 입력해 주세요.")
		private String replyContents;
						
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
		@Builder
		public CommentRequestDto(String replyContents,LocalDateTime createdAt){
			this.replyContents = replyContents;
			this.createdAt = createdAt;
		}
	}
	
	@Getter
	@Builder
	@ToString
	@RequiredArgsConstructor
	public static class CommentResponseDto{
		
		private Integer replyId;
		
		private Integer boardId;
		
		private String replyContents;
		
		private String replyWriter;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
		@Builder
		public CommentResponseDto(Integer replyId,Integer boardId,String replyWriter,String replyContents,LocalDateTime createdAt){
			this.replyId = replyId;
			this.boardId = boardId;
			this.replyWriter = replyWriter;
			this.replyContents = replyContents;
			this.createdAt = createdAt;
		}
	}
}
