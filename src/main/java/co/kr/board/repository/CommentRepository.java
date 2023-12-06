package co.kr.board.repository;

import co.kr.board.repository.QueryDsl.CommentCustomRepository;
import org.springframework.data.repository.CrudRepository;

import co.kr.board.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer>, CommentCustomRepository {

}
