package co.kr.board.board.domain.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.BaseTime;
import co.kr.board.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardRequestDto extends BaseTime{
	
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
				.readCount(readCount)
				.createdAt(createdAt)
				.build();
	}
	
}
