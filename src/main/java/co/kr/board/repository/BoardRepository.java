package co.kr.board.repository;


import co.kr.board.domain.Board;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.BoardNextPrevious;
import co.kr.board.domain.Dto.CommentDto;
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

	//게시글 이전글/다음글
	@Transactional
	@Query(nativeQuery = true,
			value = "select " +
					"b.board_id as id, b.board_title as boardTitle from board b where board_id " +
					"in((select board_id as id from board where board_id < :id order by board_id desc limit 1)," +
					"(select board_id as id from board where board_id > :id order by board_id limit 1)) " +
					"order by board_id desc")
	List<BoardNextPrevious> findPreviousNextBoard(@Param("id") Integer id);
}
