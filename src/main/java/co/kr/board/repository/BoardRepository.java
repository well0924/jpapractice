package co.kr.board.repository;


import co.kr.board.domain.Board;
import co.kr.board.repository.QueryDsl.BoardCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer>, BoardCustomRepository {
	
	//게시글 목록(페이징)
	Page<Board> findAllByCategoryId(Pageable pageable,Integer categoryId);
	Page<Board> findAllByCategoryName(Pageable pageable,String categoryName);
}
