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
	
	@Column(name="file_path")
	private String filePath;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name="stored_name")
	private String storedFileName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id")
	private Board board;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdat;
	
	public Files(String fileName) {
		this.fileName = fileName;
		this.storedFileName = generateStoredName(extractExtension(fileName));
	}
	
	public void initBoard(Board board) {
		if(this.board == null) {
			this.board = board;
		}
	}
	
	private String generateStoredName(String extension) {
		return UUID.randomUUID().toString()+"."+extension;
	}
	
	private String extractExtension(String fileName) {
		String ext = "";
		ext = fileName.substring(fileName.lastIndexOf(".")+1);			
		return ext;
	}
}
