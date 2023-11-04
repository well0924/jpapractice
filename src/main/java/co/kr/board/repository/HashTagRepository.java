package co.kr.board.repository;

import co.kr.board.domain.HashTag;
import co.kr.board.repository.QueryDsl.HashTagCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HashTagRepository extends JpaRepository<HashTag,Integer>, QuerydslPredicateExecutor<HashTag>, HashTagCustomRepository {
    //해시태그 조회
    Optional<HashTag> findByHashtagName(String hashtagName);
    //해시태그 목록
    List<HashTag> findByHashtagNameIn(Set<String> hashtagNames);

}
