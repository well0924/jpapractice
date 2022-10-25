package co.kr.board.file.domain;

import java.time.LocalDateTime;
import java.util.UUID;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
	
	@Column(name = "file_name",columnDefinition = "원본파일명")
	private String fileName;
	
	@Column(name="stored_name",columnDefinition = "저장파일명")
	private String storedFileName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id")
	private Board board;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdat;
	
	@Builder
	public Files(Board board,String fileName,String storedFileName,LocalDateTime createdAt) {
		this.board = board;
		this.fileName = fileName;
		this.storedFileName = storedFileName;
		this.createdat = createdAt;
	}
	
}
