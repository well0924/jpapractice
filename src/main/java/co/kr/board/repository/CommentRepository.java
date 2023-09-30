package co.kr.board.repository;

import java.util.List;

import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.repository.QueryDsl.CommentCustomRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.kr.board.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer>, CommentCustomRepository {

	//댓글 목록
	@Query("select c from Comment c where c.board.id = :id")
	List<Comment>findCommentsBoardId(@Param("id") Integer boardId)throws Exception;

	//최근에 작성한 댓글
	List<CommentDto.CommentResponseDto>findTop5ByOrderByReplyIdCreatedAtDesc();
	
}
