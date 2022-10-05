package co.kr.board.board.repsoitory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.kr.board.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{
	
	//게시글 목록(페이징)
	public Page<Board> findAll(Pageable pageable);
	
	//게시글 검색
	@Query(
		value="SELECT b FROM Board b WHERE b.boardTitle LIKE %:boardTitle% OR b.boardContents LIKE %:boardContents%",
		countQuery="SELECT COUNT(b.boardId) FROM Board b WHERE b.boardTitle LIKE %:boardTitle% OR b.boardContents LIKE %:boardContents%"
	)
	public Page<Board> findAllSearch(String boardTitle,String boardContents,Pageable pageable);
}
