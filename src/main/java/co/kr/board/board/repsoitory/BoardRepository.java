package co.kr.board.board.repsoitory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import co.kr.board.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{
	
	//게시글 목록(페이징)
	public Page<Board> findAll(Pageable pageable);
	
	//게시글 검색
	public Page<Board> findByboardTitleContaining(String boardTitle,Pageable pageable);
}
