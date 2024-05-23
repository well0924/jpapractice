package co.kr.board.repository;

import co.kr.board.repository.queryDsl.CommentCustomRepository;
import org.springframework.data.repository.CrudRepository;

import co.kr.board.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer>, CommentCustomRepository {

}
