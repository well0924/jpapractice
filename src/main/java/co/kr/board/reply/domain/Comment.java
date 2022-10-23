package co.kr.board.reply.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.Board;
import co.kr.board.login.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Builder
@Table(name="reply")
@RequiredArgsConstructor
@AllArgsConstructor
public class Comment{
	
	@Id
	@Column(name="reply_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
		
	@Column(name="reply_writer",nullable = false)
	private String replyWriter;
	
	@Column(name="reply_contents", nullable = false)
	private String replyContents;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	//게시글
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id")
	private Board board;
	
	//회원
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="useridx")
	private Member member;
	
	@Builder
	public Comment(Member member,Board board,String replyWriter,String replyContents,LocalDateTime createdAt) {
		this.board = board;
		this.member = member;
		this.replyContents = replyContents;
		this.replyWriter = member.getUsername();
		this.createdAt = LocalDateTime.now();
	}
	
	//댓글 수정
	public void contentsChange(String replyContents) {
		this.replyContents = replyContents;
	}
}
