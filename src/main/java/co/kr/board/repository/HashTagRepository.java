package co.kr.board.repository;

import co.kr.board.domain.Dto.HashTagDto;
import co.kr.board.domain.HashTag;
import co.kr.board.repository.QueryDsl.HashTagCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HashTagRepository extends JpaRepository<HashTag,Integer>, QuerydslPredicateExecutor<HashTag>, HashTagCustomRepository {
    //해시태그 조회
    Optional<HashTag> findByHashtagName(String hashtagName);

    //게시글 조회시 관련된 해시태그 목록
    @Query(value="select b.hashtags from Board b where b.id = :boardId")
    Set<HashTag> findHashTagsByArticles(@Param("boardId") Integer boardId);

}
