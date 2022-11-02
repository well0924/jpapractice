package co.kr.board.board.domain.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class BoardDto {
	
	@Getter
	@Setter
	@ToString
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class BoardRequestDto{
				
		@NotBlank(message ="제목을 입력해주세요.")
		private String boardTitle;
		
		@NotBlank(message ="내용을 입력해주세요.")
		private String boardContents;
		
		private Integer readCount;
				
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
	}
	
	@Getter
	@ToString
	@RequiredArgsConstructor
	public static class ResponseDto{
		
		private Integer boardId;
				
		private String boardTitle;

		private String boardContents;

		private String boardAuthor;
		
		private Integer readCount;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
				
		@Builder
		public ResponseDto(Board board) {			
			this.boardId = board.getId();
			this.boardTitle = board.getBoardTitle();
			this.boardAuthor = board.getWriter().getUsername();
			this.boardContents = board.getBoardContents();
			this.readCount = board.getReadCount();
			this.createdAt = board.getCreatedAt();
		}		
	}
}
