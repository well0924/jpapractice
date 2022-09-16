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
import com.fasterxml.jackson.annotation.JsonIgnore;

import co.kr.board.board.domain.BaseTime;
import co.kr.board.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="reply")
@NoArgsConstructor
public class Comment extends BaseTime{
	
	@Id
	@Column(name="reply_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer replyId;
		
	@Column(name="reply_writer",nullable = false)
	private String replyWriter;
	
	@Column(name="reply_contents", nullable = false)
	private String replyContents;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="board_id")
	private Board board;
	
	@Builder
	public Comment(Integer replyId,String replyWriter,String replyContents, LocalDateTime createdAt,Board board) {
		
		this.replyId = replyId;
		this.replyWriter = replyWriter;
		this.replyContents = replyContents;
		this.createdAt = createdAt;
		this.board = board;
	}
	
	public void changeBoard(Board board) {
		this.board = board;
	}
}
