package co.kr.board.file.repository;

import co.kr.board.file.domain.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttachRepository extends JpaRepository<AttachFile,Integer> {
    //파일 목록
    @Query("select a from AttachFile a where a.board.id = :id")
    List<AttachFile>findAttachFilesBoardId(@Param("id")Integer boardId)throws Exception;
}
