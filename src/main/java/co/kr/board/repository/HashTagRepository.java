package co.kr.board.repository;

import co.kr.board.domain.HashTag;
import co.kr.board.repository.QueryDsl.HashTagCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag,Integer>, QuerydslPredicateExecutor<HashTag>, HashTagCustomRepository {
    //해시태그 조회
    Optional<HashTag> findByHashtagName(String hashtagName);
}
