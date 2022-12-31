package co.kr.board.board.domain.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
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
	
	//게시물 작성 dto
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class BoardRequestDto{
		
		@NotBlank(message ="제목을 입력해주세요.")
		private String boardTitle;
		
		@NotBlank(message ="내용을 입력해주세요.")
		private String boardContents;
		
		private Integer readCount;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
	}
	
	
	//파일 첨부용 dto
	
	//게시물 응답 dto
	@Getter
	public static class BoardResponseDto{
		
		private Integer boardId;
				
		private String boardTitle;

		private String boardContents;

		private String boardAuthor;
		
		private Integer readCount;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
				
		@Builder
		public BoardResponseDto(Board board) {			
			this.boardId = board.getId();
			this.boardTitle = board.getBoardTitle();
			this.boardAuthor = board.getWriter().getUsername();
			this.boardContents = board.getBoardContents();
			this.readCount = board.getReadCount();
			this.createdAt = board.getCreatedAt();
		}		
	}
	
	
}
