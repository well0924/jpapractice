package co.kr.board.file.domain;

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
import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "files")
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Files {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="file_id")
	private Integer id;
	
	//파일 경로
	@Column(name="file_path")
	private String filePath;
	
	//파일명
	@Column(name = "file_name")
	private String fileName;
	
	//저장된 파일명
	@Column(name="stored_name")
	private String storedFileName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id")
	private Board board;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdat;
	
	public void setBoard(Board board) {
		this.board = board;
		//파일이 있는 경우
		if(board.getFilelist().contains(this)) {
			board.getFilelist().add(this);
		}
	}
}
