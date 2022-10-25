package co.kr.board.file.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import co.kr.board.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	private FileRepository repository;
		
	@Value("${server.file.upload}")
	private String uploadPath;
	
	//파일 저장
	
}
