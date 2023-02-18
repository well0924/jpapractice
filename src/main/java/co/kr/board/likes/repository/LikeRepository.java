package co.kr.board.likes.repository;

import co.kr.board.board.domain.Board;
import co.kr.board.likes.domain.Like;
import co.kr.board.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {

    Optional<Like>findByMemberAndBoard(Member member, Board board);
}
