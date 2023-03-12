package co.kr.board.repository;

import co.kr.board.domain.Board;
import co.kr.board.domain.Like;
import co.kr.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    //좋아요 중복여부
    Optional<Like>findByMemberAndBoard(Member member, Board board);
}
