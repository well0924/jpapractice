package co.kr.board.testboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.board.service.BoardService;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.login.domain.Member;
import co.kr.board.login.repository.MemberRepository;

@SpringBootTest
class BoardServiceTest {
	
	@Autowired
	BoardRepository reposi;
	
	@Autowired
	BoardService boardservice;
	
	@Autowired
	MemberRepository memberrepos;
	
	private Member member1;
		
	@BeforeEach
	public void before() {
		//admin 내용
		Optional<Member>detail = memberrepos.findById(1);
		
		member1 = detail.get();
		
	}
	
	@Test
	@DisplayName("글 조회")
	public void boarddetail() throws Exception {
		
		//given
		Optional<Board> board = reposi.findById(4);
		Board detail = board.get();
		
		//when
		BoardDto.ResponseDto result = boardservice.getBoard(detail.getId());
		
		//then		
		assertEquals(result.getBoardAuthor(), detail.getBoardAuthor());
	}
	
	@Test
	@DisplayName("글 조회 실패")
	public void boarddetailfail()throws Exception{
		//given
		Optional<Board>board = reposi.findById(3);
		Board detail = board.get();
		
		//when
		BoardDto.ResponseDto result = boardservice.getBoard(detail.getId());
		
		//then
		org.junit.jupiter.api.Assertions.assertThrows(CustomExceptionHandler.class,()->{
			boardservice.getBoard(result.getBoardId());
		});
	}
	
	@Test
	@DisplayName("글 작성")
	public void boardwrite() throws Exception {
		
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto.builder().boardTitle("제목테스트??").boardContents("내용2").createdAt(LocalDateTime.now()).build();
		
		//when
		Integer result = boardservice.boardsave(dto, member1);
		
		//then
		Board board = reposi.findById(result).orElseThrow();
		assertEquals(member1.getUsername(),board.getBoardAuthor());
	}
	
	@Test
	@DisplayName("글 수정")
	public void boardupdate() throws Exception {
	
		//given
		Board board = new Board();
		
		board = reposi.findById(41).orElseThrow();
		
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
										.builder()
										.boardContents("수정내용")
										.boardTitle("수정제목")
										.createdAt(LocalDateTime.now())
										.build();
		
		//when
		board.updateBoard(dto);
		Integer result = boardservice.updateBoard(board.getId(), dto, member1);
		
		//then
		board = reposi.findById(result).orElseThrow();
		
		assertEquals(dto.getBoardContents(), board.getBoardContents());
		assertEquals(dto.getBoardTitle(),board.getBoardTitle());
		assertEquals(member1.getUsername(), board.getBoardAuthor());
	}
	
	@Test
	@DisplayName("글 삭제")
	public void boarddelete() throws Exception {
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto.builder().boardTitle("제목테스트??").boardContents("내용2").createdAt(LocalDateTime.now()).build();
		
		Integer result = boardservice.boardsave(dto, member1);
		
		//when
		boardservice.deleteBoard(result, member1);
		
		//then
		org.junit.jupiter.api.Assertions.assertThrows(CustomExceptionHandler.class,()->{
			boardservice.deleteBoard(result, member1);
		});
	}
	
	@Test
	@DisplayName("글 목록")
	public void boardlist() {
		
		List<Board>boardlist = reposi.findAll();
		
		Pageable pageable = Pageable.ofSize(5);

		Page<Board>list = reposi.findAll(pageable);
		
		List<Board>content = list.getContent();
		
		Integer total = list.getTotalPages();

		for(int i=0; i<content.size();i++) {
			System.out.println("boardpaging content:"+content.get(i).getClass());
		}
		
		System.out.println(total);
		System.out.println(boardlist);
	}

}
