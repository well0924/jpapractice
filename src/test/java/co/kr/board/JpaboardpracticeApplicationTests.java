package co.kr.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import co.kr.board.board.domain.Board;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.board.service.BoardService;

@SpringBootTest
class JpaboardpracticeApplicationTests {
	
	@Autowired
	BoardRepository reposi;
	
	@Autowired
	BoardService service;
	
	//글 목록 조회 테스트
		@Test
		public void getList()throws Exception{
			
			
		}
	
}
