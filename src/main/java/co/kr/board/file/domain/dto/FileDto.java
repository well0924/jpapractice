package co.kr.board.file.domain.dto;

import groovy.transform.ToString;
import lombok.Getter;

public class FileDto {
	
	@Getter
	@ToString
	public static class FileResponse{
		private String fileName;
		private String storedFileName;
	}
}
