package co.kr.board.repository;


import co.kr.board.domain.Board;
import co.kr.board.domain.Dto.BoardNextPrevious;
import co.kr.board.repository.QueryDsl.BoardCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer>, BoardCustomRepository {

	//게시글 목록(페이징+카테고리)->querydsl로 변경하기.
	Page<Board> findAllByCategoryName(Pageable pageable,String categoryName);

	//게시글 전체갯수
	@Query(value = "select count(*) from Board b")
	Integer ArticleCount();
}
