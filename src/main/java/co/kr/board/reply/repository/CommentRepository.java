package co.kr.board.reply.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.kr.board.reply.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

	@Query("select c from Comment c where c.board_id = :boardId")
	public List<Comment> getCommentList()throws Exception;
}
