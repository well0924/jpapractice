package co.kr.board.reply.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.kr.board.reply.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer>{
	
	@Query("select c from Comment c where c.board.boardId = :id")
	List<Comment>findCommentsBoardId(@Param("id") Integer boardId)throws Exception;
}
