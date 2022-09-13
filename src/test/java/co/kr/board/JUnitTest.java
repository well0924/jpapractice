package co.kr.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.kr.board.board.domain.Board;
import co.kr.board.board.repsoitory.BoardRepository;

@SpringBootTest
public class JUnitTest {
	
	@Autowired
	private BoardRepository reposi;
	
	//글 목록 조회 테스트
	@Test
	public void getList()throws Exception{
		
		List<Board>getlist = reposi.findAll();
		
		System.out.println(getlist.toString());
	}
}
