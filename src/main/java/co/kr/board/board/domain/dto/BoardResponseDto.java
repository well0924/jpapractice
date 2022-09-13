package co.kr.board.board.domain.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BoardResponseDto {
	
	private Integer boardId;

	private String boardTitle;

	private String boardContents;

	private String boardAuthor;

	private Integer readCount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	@Builder
	public BoardResponseDto(Integer boardId,String boardTitle,String boardContents,String boardAuthor,Integer readCount,LocalDateTime createdAt) {
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
				.readCount(readCount)
				.createdAt(createdAt)
				.build();
	}
}
