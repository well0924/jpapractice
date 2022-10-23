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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "files")
@Entity
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
	public Files(Board board,String fileName,String storedFileName) {
		this.fileName = fileName;
		this.storedFileName = storedNameCreate(extractExtension(fileName));
	}
	
	public void initBoard(Board board) {
		if(board == null) {
			this.board = board;
		}
	}
	
	
	//파일 저장시 확장자만들기.
	public String storedNameCreate(String extension) {
		
		return UUID.randomUUID().toString()+"."+extension;
	}
	
	private String extractExtension(String fileName) {
		
		String ext = "";
		
		try {
			ext = fileName.substring(fileName.lastIndexOf(".")+1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ext;
	}
}
