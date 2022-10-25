package co.kr.board.file.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import co.kr.board.file.domain.Files;
import groovy.transform.ToString;
import lombok.Getter;

public class FileDto {
	
	@Getter
	@ToString
	public static class FileResponse{
		private String fileName;
	}
	
	public static FileDto.FileResponse entityToDtoResponse(Files files){	
		FileDto.FileResponse response = new FileDto.FileResponse();
		response.fileName = files.getFileName();
		return response;
	}
	
	public static List<FileDto.FileResponse>entityListToDtoList(List<Files>filelist){
		return filelist.stream()
				.map(file -> FileDto.entityToDtoResponse(file))
				.collect(Collectors.toList());
	}
}
