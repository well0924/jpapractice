package co.kr.board.board.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.BaseTime;
import co.kr.board.board.domain.Board;
import co.kr.board.reply.domain.dto.CommentDto;
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
	public static class BoardRequestDto extends BaseTime{
		
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
		
		
		@Builder
		public BoardRequestDto(Integer boardId,String boardTitle,String boardContents,String boardAuthor,Integer readCount,LocalDateTime createdAt) {
			
			this.boardId = boardId;
			this.boardTitle = boardTitle;
			this.boardContents = boardContents;
			this.boardAuthor = boardAuthor;
			this.readCount = readCount;
			this.createdAt = createdAt;	
		
		}
		
		//entity변환
		public Board toEntity() {
			return Board.builder()
					.boardId(boardId)
					.boardTitle(boardTitle)
					.boardContents(boardContents)
					.boardAuthor(boardAuthor)
					.readCount(0)
					.createdAt(LocalDateTime.now())
					.build();
		}
	}
	
	@Getter
	@Builder
	@NoArgsConstructor
	@ToString
	public static class BoardResponseDto{
		
		private Integer boardId;

		private String boardTitle;

		private String boardContents;

		private String boardAuthor;

		private Integer readCount;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
		private List<CommentDto.CommentResponseDto> comments;
		
		@Builder
		public BoardResponseDto(Integer boardId,String boardTitle,String boardContents,String boardAuthor,Integer readCount,LocalDateTime createdAt) {
			this.boardId = boardId;
			this.boardTitle = boardTitle;
			this.boardContents = boardContents;
			this.boardAuthor = boardAuthor;
			this.readCount = readCount;
			this.createdAt = createdAt;	
		}
		
	}
}
