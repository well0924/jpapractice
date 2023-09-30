package co.kr.board.testboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import co.kr.board.domain.Board;
import co.kr.board.domain.Category;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.repository.CategoryRepository;
import co.kr.board.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import co.kr.board.repository.BoardRepository;
import co.kr.board.service.BoardService;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Member;
import co.kr.board.repository.MemberRepository;

@SpringBootTest
class BoardServiceTest {
	
	@Autowired
	BoardRepository reposi;
	
	@Autowired
	BoardService boardservice;
	
	@Autowired
	MemberRepository memberrepos;
	@Autowired
	CategoryRepository categoryRepository;
	private Member member1;
	@Autowired
	private CommentRepository commentRepository;

	@BeforeEach
	public void before() {
		//admin 내용
		Optional<Member>detail = memberrepos.findById(1);
		member1 = detail.get();
	}
	@Test
	@DisplayName("글 단일조회-성공")
	public void boarddetail(){

		//given
		Optional<Board> board = reposi.findById(4);
		Board detail = board.get();

		//when
		BoardDto.BoardResponseDto result = boardservice.getBoard(detail.getId());

		//then
		assertEquals(result.getBoardAuthor(), "well4149");
	}
	@Test
	@DisplayName("글 단일조회 실패-성공")
	public void boarddetailfail(){
		
		org.junit.jupiter.api.Assertions.assertThrows(Exception.class,()->{
			Optional<Board>board = Optional.ofNullable(reposi.findById(3).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL)));});
	}
	
	@Test
	@DisplayName("글 작성-성공")
	public void boardwrite()throws Exception{
		
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
				.builder()
				.boardTitle("제목테스트??")
				.boardContents("내용3")
				.createdAt(LocalDateTime.now())
				.build();
		
		//when
		Integer result = boardservice.boardsave(dto, member1,1,null);
		
		//then
		Board board = reposi.findById(result).orElseThrow();
		assertEquals(member1.getUsername(),board.getBoardAuthor());
	}
	
	@Test
	@DisplayName("글작성실패 -회원이 아닌경우")
	public void boardwritefail(){
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
				.builder()
				.boardTitle("제목테스트fail??")
				.boardContents("내용3fail")
				.createdAt(LocalDateTime.now())
				.build();

		member1 = null;

		//when
		CustomExceptionHandler customExceptionHandler = assertThrows(CustomExceptionHandler.class,()->{
			boardservice.boardsave(dto, member1,1,null);
		});

		//then
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.ONLY_USER);
	}
	
	@Test
	@DisplayName("글 수정")
	public void boardupdate()throws Exception{
		
		//given
		before();
		Board board = reposi.findById(4).orElseThrow();
		
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
										.builder()
										.boardContents("수정내용Q")
										.boardTitle("수정제목")
										.createdAt(LocalDateTime.now())
										.build();
		
		//when
		board.updateBoard(dto);
		Integer result = boardservice.updateBoard(board.getId(), dto, member1,null);
		
		//then
		board = reposi.findById(result).orElseThrow();
		
		assertEquals(dto.getBoardContents(), board.getBoardContents());
		assertEquals(dto.getBoardTitle(),board.getBoardTitle());
		assertEquals(member1.getUsername(), board.getBoardAuthor());
	}
	
	@Test
	@DisplayName("글 수정실패 - 회원이 아닌경우")
	public void boardupdatefail1() {
		Board board = reposi.findById(4).orElseThrow();
		
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
										.builder()
										.boardContents("수정내용Q")
										.boardTitle("수정제목")
										.createdAt(LocalDateTime.now())
										.build();
		
		//when
		board.updateBoard(dto);
		Integer boardid= board.getId();
		member1 = null;
		
		CustomExceptionHandler customExceptionHandler = assertThrows(CustomExceptionHandler.class,()->{
			boardservice.updateBoard(boardid, dto, member1,null);
		});
		
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.ONLY_USER);
	}
	
	@Test
	@DisplayName("글 수정실패-아이디와 작성자가 다른경우")
	public void boardupdatefail2() {
		Board board = reposi.findById(4).orElseThrow();
		
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
										.builder()
										.boardContents("수정내용Q")
										.boardTitle("수정제목")
										.createdAt(LocalDateTime.now())
										.build();
		
		//when
		board.updateBoard(dto);
		Integer boardid= board.getId();
		member1 = memberrepos.findById(2).orElseThrow();
		
		CustomExceptionHandler customExceptionHandler = assertThrows(CustomExceptionHandler.class,()->{
			boardservice.updateBoard(boardid, dto, member1,null);
		});
		
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.BOARD_EDITE_DENIED);
	}
	
	@Test
	@DisplayName("글 삭제")
	public void boarddelete() throws Exception{
		long count = reposi.count();
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
				.builder()
				.boardTitle("제목테스트>??")
				.boardContents("내용24")
				.createdAt(LocalDateTime.now())
				.build();
		//글 저장
		Integer result = boardservice.boardsave(dto, member1,1,null);
		
		//when
		boardservice.deleteBoard(result, member1);
		
		//then
		assertThat(count).isEqualTo(count);
	}
	
	@Test
	@DisplayName("글 삭제 실패- 회원이 아닌경우-성공")
	public void boarddeleteFail() throws Exception{
		//given
		BoardDto.BoardRequestDto dto = BoardDto.BoardRequestDto
				.builder()
				.boardTitle("제목테스트>??")
				.boardContents("내용24")
				.createdAt(LocalDateTime.now())
				.build();
		
		Integer result = boardservice.boardsave(dto, member1,1,null);
		
		member1 = null;
		
		//when
		CustomExceptionHandler customExceptionHandler = assertThrows(CustomExceptionHandler.class,()->{
			boardservice.deleteBoard(result, member1);
		});
		//then
		assertEquals(customExceptionHandler.getErrorCode(),ErrorCode.ONLY_USER);
	}
	
	@Test
	@DisplayName("글 목록-성공")
	public void boardlist() {
		
		List<Board>boardlist = reposi.findAll();
		
		List<BoardDto.BoardResponseDto>listresult = boardservice.findAll();
		
		assertThat(boardlist).isNotNull();
		assertThat(listresult).isNotNull();
		assertThat(listresult.get(0).getBoardId()).isEqualTo(boardlist.get(0).getId());
	}
	
	@Test
	@DisplayName("게시글 목록 페이징-성공")
	public void boardlistPageingTest() {
		Pageable pageable = Pageable.ofSize(5);

		Page<Board>list = reposi.findAll(pageable);
		
		List<Board>content = list.getContent();
		
		Integer total = list.getTotalPages();

		for(int i=0; i<content.size();i++) {
			System.out.println("boardpaging content:"+content.get(i).getClass());
		}
		assertThat(list).info.description();
	}

	//카테고리 + 페이징
	@Test
	@DisplayName("게시글 카테고리 + 페이징")
	public void boardListPagingCategoryTest(){
		Category category = categoryRepository.findById(1).orElseThrow(()->new CustomExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND));
		String categoryName = category.getName();
		Pageable pageable = Pageable.ofSize(5);

		Page<BoardDto.BoardResponseDto>result = boardservice.findAllPage(pageable,categoryName);

		assertThat(result.get().findAny().get().getCategoryId()).isEqualTo(1);
	}
	//글검색
	@Test
	@DisplayName("게시글 카테고리 + 페이징+검색")
	public void boardListPagingCategorySearchTest(){
		String keyword = "test";
		Pageable pageable = Pageable.ofSize(5);

		Page<BoardDto.BoardResponseDto>result = boardservice.findAllSearch(keyword,pageable);
		assertThat(result.get().findAny().get());
		assertThat(result.get().findAny().get().getBoardAuthor()).isEqualTo("well4149");
	}
}
