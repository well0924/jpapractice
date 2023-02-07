package co.kr.board.board.repsoitory;


import co.kr.board.board.repsoitory.QueryDsl.BoardCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import co.kr.board.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>, BoardCustomRepository {
	
	//게시글 목록(페이징)
	Page<Board> findAll(Pageable pageable);
}
