package co.kr.board.board.repsoitory;


import org.springframework.data.jpa.repository.JpaRepository;

import co.kr.board.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{
	
	
}
