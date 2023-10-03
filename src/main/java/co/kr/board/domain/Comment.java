package co.kr.board.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name="reply",indexes = {
		@Index(columnList = "reply_writer"),
		@Index(columnList = "reply_contents")
})
@RequiredArgsConstructor
public class Comment implements Serializable {
	
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
