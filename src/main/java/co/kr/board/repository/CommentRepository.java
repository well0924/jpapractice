package co.kr.board.repository;

import co.kr.board.repository.QueryDsl.CommentCustomRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import co.kr.board.domain.Comment;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer>, CommentCustomRepository {

    //댓글 선택 삭제
    @Modifying
    @Transactional
    @Query(value = "delete from Comment c where c.id in :id")
    void deleteAllById(@Param("id")List<Integer> commentId);
}
