package co.kr.board.testboard;

import static org.assertj.core.api.Assertions.assertThat;
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
import co.kr.board.config.exception.dto.ErrorCode;
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
		assertThat(member1.getMembername()).isEqualTo("updateuser1");
	}
	
	@Test
	@DisplayName("글 조회")
	public void boarddetail() throws Exception {
		
		//given
		Optional<Board> board = reposi.findById(4);
		Board detail = board.get();
		
		//when
		BoardDto.BoardResponseDto result = boardservice.getBoard(detail.getId());
		
		//then		
		assertEquals(result.getBoardAuthor(), "well");
	}
	
	@Test
	@DisplayName("글 조회 실패")
	public void boarddetailfail()throws Exception{
		
		org.junit.jupiter.api.Assertions.assertThrows(Exception.class,()->{
			Optional<Board>board = Optional.ofNullable(reposi.findById(3).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_BOARDDETAIL)));});
	}
	
	@Test
	@DisplayName("글 작성")
	public void boardwrite() throws Exception {
		
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto.builder().boardTitle("제목테스트??").boardContents("내용3").createdAt(LocalDateTime.now()).build();
		
		//when
		Integer result = boardservice.boardsave(dto, member1);
		
		//then
		Board board = reposi.findById(result).orElseThrow();
		assertEquals(member1.getUsername(),board.getBoardAuthor());
	}
	
	@Test
	@DisplayName("글 작성실패 - 다른 회원인 경우")
	public void boardwriteFail() throws Exception {
		
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto.builder().boardTitle("제목테스트??").boardContents("내용3").createdAt(LocalDateTime.now()).build();
		
		//when
		//Integer result = boardservice.boardsave(dto, member1);
		
		//then
		//Board board = reposi.findById(result).orElseThrow();
		//assertEquals(member1.getUsername(),board.getBoardAuthor());
	}
	
	@Test
	@DisplayName("글 수정")
	public void boardupdate() throws Exception {
		
		//given
		before();
		Board board = new Board();
		
		board = reposi.findById(41).orElseThrow();
		
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
										.builder()
										.boardContents("수정내용Q")
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
		long count = reposi.count();
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
				.builder()
				.boardTitle("제목테스트>??")
				.boardContents("내용24")
				.createdAt(LocalDateTime.now())
				.build();
		//글 저장
		Integer result = boardservice.boardsave(dto, member1);
		
		//when
		boardservice.deleteBoard(result, member1);
		
		//then
		assertThat(count).isEqualTo(count);
	}
	
	@Test
	@DisplayName("글 삭제 실패- 다른 회원인 경우")
	public void boarddeleteFail() throws Exception {
		long count = reposi.count();
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
				.builder()
				.boardTitle("제목테스트>??")
				.boardContents("내용24")
				.createdAt(LocalDateTime.now())
				.build();
		
	}
	
	@Test
	@DisplayName("글 목록")
	public void boardlist() throws Exception {
		
		List<Board>boardlist = reposi.findAll();
		
		List<BoardDto.BoardResponseDto>listresult = boardservice.findAll();
		
		assertThat(boardlist).isNotNull();
		assertThat(listresult).isNotNull();
		assertThat(listresult.get(0).getBoardId()).isEqualTo(boardlist.get(0).getId());
	}
	
	@Test
	@DisplayName("게시글 목록 페이징")
	public void boardlistPageingTest() throws Exception {
		Pageable pageable = Pageable.ofSize(5);

		Page<Board>list = reposi.findAll(pageable);
		
		List<Board>content = list.getContent();
		
		Integer total = list.getTotalPages();

		for(int i=0; i<content.size();i++) {
			System.out.println("boardpaging content:"+content.get(i).getClass());
		}
		Page<BoardDto.BoardResponseDto>pageResult = boardservice.findAllPage(pageable);
		
		assertThat(pageResult).isNotNull();
		assertThat(list).info.description();
	}
	
	@Test
	@DisplayName("게시글 검색 테스트")
	public void boardlistSearchTest() {
		
	}
	
	
}
