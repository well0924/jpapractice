package co.kr.board.repository;

import co.kr.board.domain.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttachRepository extends JpaRepository<AttachFile,Integer> {
    //파일 목록
    @Query("select a from AttachFile a where a.board.id = :id")
    List<AttachFile>findAttachFilesBoardId(@Param("id")Integer boardId)throws Exception;

    //파일 조회
    Optional<AttachFile>findAttachFileByOriginFileName(String originFileName);
}
