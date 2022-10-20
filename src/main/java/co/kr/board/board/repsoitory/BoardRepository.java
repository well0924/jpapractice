package co.kr.board.board.repsoitory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.kr.board.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{
	
	//게시글 목록(페이징)
	public Page<Board> findAll(Pageable pageable);
	
	//게시글 검색
	@Query(
		value="SELECT b FROM Board b WHERE b.boardTitle LIKE %:keyword% OR b.boardContents LIKE %:keyword%",
		countQuery="SELECT COUNT(b.id) FROM Board b WHERE b.boardTitle LIKE %:keyword% OR b.boardContents LIKE %:keyword%"
	)
	public Page<Board> findAllSearch(@Param("keyword") String keyword, Pageable pageable);
}
