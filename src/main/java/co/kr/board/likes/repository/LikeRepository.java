package co.kr.board.likes.repository;

import co.kr.board.likes.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
}
