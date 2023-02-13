package co.kr.board.file.repository;

import co.kr.board.file.domain.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachRepository extends JpaRepository<AttachFile,Long> {
}
