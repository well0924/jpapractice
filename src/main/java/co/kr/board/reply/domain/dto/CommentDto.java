package co.kr.board.reply.domain.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.Board;
import co.kr.board.login.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class CommentDto {
	
	@Getter
	@Setter
	@ToString
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CommentRequestDto {
		
		private Integer replyId;
		
		private Integer boardId;
				
		@NotBlank(message = "내용을 입력해 주세요.")
		private String replyContents;
		
		@NotBlank(message = "작성자를 입력해 주세요")
		private String replyWriter;
		
		private Board board;
		
		private Member member;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
	}
	
	@Getter
	@Builder
	@ToString
	@AllArgsConstructor
	public static class CommentResponseDto{
		
		private Integer replyId;
		
		private Integer boardId;
		
		private String replyContents;
		
		private String replyWriter;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
	}
}
