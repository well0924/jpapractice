package co.kr.board.board.domain;


import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.login.domain.Member;
import co.kr.board.reply.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString(callSuper = true)
@Table(name="board")
@RequiredArgsConstructor
public class Board extends BaseTime{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="board_id")
	private Integer id;
	
	@Column(name = "board_title")
	private String boardTitle;
	
	@Column(name = "board_contents")
	private String boardContents;
	
	//회원
	@ManyToOne(fetch =FetchType.EAGER)
	@JoinColumn(name="useridx")
	private Member writer;
	
	@Column(name = "board_author")
	private String boardAuthor;
	
	@Column(name = "read_count")
	private Integer readCount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	//댓글
	@ToString.Exclude
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	private List<Comment>commentlist;

	@Builder
	public Board(Integer boardId,String boardTitle,String boardContents,String boardAuthor,Integer readcount,LocalDateTime createdat,Member member) {
		this.id = boardId;
		this.boardTitle = boardTitle;
		this.boardContents = boardContents;
		this.boardAuthor = member.getUsername();
		this.readCount = readcount;
		this.createdAt = LocalDateTime.now();
		this.writer = member;
	}
	
	//게시글 조회수 증가
	public void countUp() {
		this.readCount ++;
	}
	
	//게시글 수정
	public void updateBoard(BoardDto.BoardRequestDto dto) {
		this.boardTitle = dto.getBoardTitle();
		this.boardContents =dto.getBoardContents();
		this.createdAt = LocalDateTime.now();
	}
}
