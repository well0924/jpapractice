package co.kr.board.reply.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.BaseTime;
import co.kr.board.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name="reply")
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTime{
	
	@Id
	@Column(name="reply_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer replyId;
	
	@Column(name="reply_contents", nullable = false)
	private String replyContents;
	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name="board_id")
	private Board board;
	
}
