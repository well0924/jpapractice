package co.kr.board.repository;

import co.kr.board.domain.Board;
import co.kr.board.domain.Like;
import co.kr.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    //좋아요 중복여부
    Optional<Like>findByMemberAndBoard(Member member, Board board);

    //게시글 좋아요 갯수
    @Query(value = "select count (*) from Like l where l.board.id = :id")
    Integer findByBoardId(@Param("id") Integer boardId);
}
