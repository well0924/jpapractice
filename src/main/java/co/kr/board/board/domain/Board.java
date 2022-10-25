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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.file.domain.Files;
import co.kr.board.login.domain.Member;
import co.kr.board.reply.domain.Comment;
import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@ToString
@Builder
@Table(name="board")
@AllArgsConstructor
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
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name="useridx")
	private Member writer;
	
	@Column(name = "board_author")
	private String boardAuthor;
	
	@Column(name = "read_count")
	private Integer readCount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	//댓글
	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@OrderBy(value="reply_id")
	private List<Comment>commentlist;
	
	//첨부파일
	@OneToMany(mappedBy = "board",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<Files>filelist;
	
	@Builder
	public Board(String boardTitle,String boardContents,String boardAuthor,Integer readcount,LocalDateTime createdat,Member member) {
		
		this.boardTitle = boardTitle;
		this.boardContents = boardContents;
		this.boardAuthor = member.getUsername();
		this.readCount = readcount;
		this.createdAt = LocalDateTime.now();
		this.writer = member;
	}
	
	//댓글 넣기
	public void writeCommnet(Comment comment) {
		this.commentlist.add(comment);
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
