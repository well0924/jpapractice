package co.kr.board.reply.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.kr.board.board.domain.BaseTime;
import co.kr.board.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name="reply")
@Builder
@AllArgsConstructor
public class Comment extends BaseTime{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer replyId;
	
	@Column(name="reply_contents", nullable = false)
	private String replyContents;
	
	@ManyToOne
	@JoinColumn(name="board_id")
	private Board board;
	
	
}
