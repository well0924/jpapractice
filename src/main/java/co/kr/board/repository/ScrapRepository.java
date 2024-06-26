package co.kr.board.repository;

import co.kr.board.domain.Board;
import co.kr.board.domain.Member;
import co.kr.board.domain.Scrap;
import co.kr.board.repository.queryDsl.CustomScrapRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap,Long>, CustomScrapRepository {
    //스크랩 중복 여부
    Optional<Scrap> findByBoardAndMember(Board board, Member member);
}
