package co.kr.board.repository;

import co.kr.board.domain.Board;
import co.kr.board.repository.QueryDsl.BoardCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Integer>, BoardCustomRepository {

	//게시글 전체갯수
	@Query(value = "select count(*) from Board b")
	Integer ArticleCount();
	//카테고리별 게시글 갯수
	@Query(value = "select count(*) from Board b where b.category.name = :name")
	Integer categoryCount(@Param("name") String categoryName);
	//게시글 선택삭제
	@Transactional
	@Modifying
	@Query(value = "delete from Board b where b.id in :boardId")
	void deleteAllById(List<String>boardId);
}
