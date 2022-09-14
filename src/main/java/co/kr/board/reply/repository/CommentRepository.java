package co.kr.board.reply.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.kr.board.reply.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
