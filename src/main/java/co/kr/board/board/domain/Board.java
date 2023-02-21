package co.kr.board.board.domain;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import co.kr.board.file.domain.AttachFile;
import co.kr.board.likes.domain.Like;
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
public class Board extends BaseTime implements Serializable{
	//redis에 객체를 저장을 하면 내부적으로 직렬화가 되어서 저장이 된다.
	//entity 객체중 lazy로 로딩이 되는 경우에는 @Proxy(lazy = false)를 선언해줘야 한다.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="board_id")
	private Integer id;
	
	@Column(name = "board_title")
	private String boardTitle;
	
	@Column(name = "board_contents")
	private String boardContents;
	
	@Column(name = "board_author")
	private String boardAuthor;
	
	@Column(name = "read_count")
	private Integer readCount;
	@Column(nullable = false)
	private Integer liked;//추천수
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;

	//회원
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name="useridx")
	private Member writer;

	//댓글
	@ToString.Exclude
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	private List<Comment>commentlist = new ArrayList<>();

	//파일첨부(게시글을 삭제하면 파일도 삭제)
	//orphanRemoval을 true로 설정을 하면 게시글을 삭제시 파일도 같이 삭제
	//orphanRemoval과 CasecadeType.REMOVE 차이점 알아보기.
	@ToString.Exclude
	@OneToMany(mappedBy = "board",cascade = {CascadeType.ALL},orphanRemoval = true)
	private List<AttachFile>attachFiles = new ArrayList<>();

	//좋아요
	@OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
	private Set<Like> likes = new HashSet<>();
	
	@Builder
	public Board(Integer boardId,String boardTitle,String boardContents,String boardAuthor,Integer readcount,LocalDateTime createdat,Member member) {
		this.id = boardId;
		this.boardTitle = boardTitle;
		this.boardContents = boardContents;
		this.boardAuthor = member.getUsername();
		this.readCount = readcount;
		this.liked = 0;
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

	//첨부 파일 추가
	public void addAttach(AttachFile attachFile){
		this.attachFiles.add(attachFile);
		if(attachFile.getBoard()!=this){
			attachFile.setBoard(this);
		}
	}

	//좋아요 추가
	public void increaseLikeCount(){
		this.liked +=1;
	}
	//좋아요 취소
	public void decreaseLikeCount(){
		this.liked -=1;
	}
}
