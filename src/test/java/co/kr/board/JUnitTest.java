package co.kr.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import co.kr.board.board.domain.Board;
import co.kr.board.board.repsoitory.BoardRepository;

@SpringBootTest
public class JUnitTest {
	
	@Autowired
	private BoardRepository reposi;
	
	//글 목록 조회 테스트
	@Test
	public void getList()throws Exception{
	
	}
}
