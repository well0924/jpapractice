package co.kr.board.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.kr.board.file.domain.Files;

public interface FileRepository extends JpaRepository<Files, Integer>{
	
	public void deleteByFileName(String fileName);
}
