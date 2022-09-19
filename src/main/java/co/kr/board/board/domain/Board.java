package co.kr.board.board.domain;


import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import co.kr.board.reply.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder
@Table(name="board")
@AllArgsConstructor
@RequiredArgsConstructor
public class Board extends BaseTime{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="board_id")
	private Integer boardId;
	
	@Column(name = "board_title",nullable = false)
	@NotBlank(message="게시글 제목을 입력해주세요.")
	private String boardTitle;
	
	@Column(name = "board_contents",nullable = false)
	@NotBlank(message = "게시글 내용을 입력해주세요.")
	private String boardContents;
	
	@Column(name = "board_author",nullable = false)
	@NotBlank(message = "게시글 작성자를 입력해주세요.")
	private String boardAuthor;
	
	@Column(name = "read_count",nullable = true)
	private Integer readCount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	//회원
	
	
	//댓글
	@JsonIgnore
	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
	private List<Comment>commentlist;
	
	
	//게시글 수정
	public void update(String boardTitle,String boardContents,String boardAuthor,Integer readCount) {	
		this.boardTitle = boardTitle;
		this.boardContents = boardContents;
		this.boardAuthor = boardAuthor;
		this.readCount = readCount;
	}
	
	//게시글 조회수 증가
	public void countUp() {
		this.readCount ++;
	}
	
}
