package co.kr.board;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.board.service.BoardService;

@SpringBootTest
class JpaboardpracticeApplicationTests {
	
	@Autowired
	BoardRepository reposi;
	
	@Autowired
	BoardService service;

}
