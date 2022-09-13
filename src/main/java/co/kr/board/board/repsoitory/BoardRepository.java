package co.kr.board.board.repsoitory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import co.kr.board.board.domain.Board;

public interface BoardRepository extends PagingAndSortingRepository<Board, Integer>{
	
	//게시글 목록(페이징)
	public Page<Board> findAll(Pageable pageable);
}
