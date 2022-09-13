package co.kr.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardRequestDto;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.board.service.BoardService;

@SpringBootTest
class JpaboardpracticeApplicationTests {
	
	@Autowired
	BoardRepository repository;
	
	@Autowired
	BoardService service;
	
	
	@org.junit.jupiter.api.Test
	public void savetest(BoardRequestDto param) {
		
		Board result = repository.findById((int)8).get();
		
		assertThat(result.getBoardTitle()).isEqualTo("tester");
		assertThat(result.getBoardAuthor()).isEqualTo("작성자");
		assertThat(result.getBoardContents()).isEqualTo("내용!");
		
	}
	
}
