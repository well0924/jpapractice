package co.kr.board.board.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BoardFileDto {
	
	@NotBlank(message ="제목을 입력해주세요.")
	private String boardTitle;
	
	@NotBlank(message ="내용을 입력해주세요.")
	private String boardContents;
	
	private Integer readCount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	private List<MultipartFile>files;
}
