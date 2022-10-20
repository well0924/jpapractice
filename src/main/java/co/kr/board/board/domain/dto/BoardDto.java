package co.kr.board.board.domain.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class BoardDto {
	
	@Getter
	@Setter
	@ToString
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BoardRequestDto{
		
		private Integer boardId;
				
		@NotBlank(message ="제목을 입력해주세요.")
		private String boardTitle;
		
		@NotBlank(message ="내용을 입력해주세요.")
		private String boardContents;
		
		@NotBlank(message ="작성자를 입력해주세요.")
		private String boardAuthor;
		
		private Integer readCount;
				
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
	}
	
	@Getter
	@Builder
	@NoArgsConstructor
	@ToString
	public static class BoardResponseDto{
		
		private Integer boardId;
		
		private Integer useridx;
		
		private String boardTitle;

		private String boardContents;

		private String boardAuthor;

		private Integer readCount;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
		@Builder
		public BoardResponseDto(Integer boardId,Integer useridx,String boardTitle,String boardContents,String boardAuthor,Integer readCount,LocalDateTime createdAt) {
			this.boardId = boardId;
			this.useridx = useridx;
			this.boardTitle = boardTitle;
			this.boardContents = boardContents;
			this.boardAuthor = boardAuthor;
			this.readCount = readCount;
			this.createdAt = createdAt;			
		}
		
	}
}
