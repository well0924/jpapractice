package co.kr.board.testboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import co.kr.board.config.Security.SecurityConfig;
import co.kr.board.domain.Board;
import co.kr.board.domain.Category;
import co.kr.board.domain.Const.Role;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.service.BoardService;
import co.kr.board.domain.Member;
import co.kr.board.domain.Const.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import co.kr.board.config.Security.SecurityConfig;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class BoardControllerTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	private BoardService boardService;

	@DisplayName("[view]게시판 목록 화면-성공")
	@Test
	public void CotrollerViewTest()throws Exception{
		//given
		given(boardService.findAllPage(any(Pageable.class),eq(categoryDto().getName()))).willReturn(Page.empty());
		//when&then
		mockMvc
		.perform(get("/page/board/list/"+categoryDto().getName())
		.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(view().name("board/boardlist"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("list"))
		.andDo(print());

	}

	@Test
	@DisplayName("[view]게시글 단일 조회-성공")
	public void controllerDetailViewTest()throws Exception{
		int boardId = 4;
		given(boardService.getBoard(boardId)).willReturn(boardResponseDto());

		mockMvc.perform(get("/page/board/detail/{id}",boardId))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("board/detailpage"))
				.andExpect(model().attributeExists("detail"))
				.andDo(print());

		then(boardService).should().getBoard(boardId);
	}
	
	@WithMockUser
	@DisplayName("[view]게시글 작성화면-성공")
	@Test
	public void controllerPostViewTest()throws Exception{
		
		mockMvc
		.perform(get("/page/board/write"))
		.andExpect(view().name("board/writeboard"))
		.andExpect(status().isOk())
		.andDo(print());
	}

	@Test
	@WithMockUser
	@DisplayName("[view]게시물 수정-성공")
	public void boardUpdateViewTest()throws Exception{
		int boardId =4;

		given(boardService.getBoard(boardId)).willReturn(boardResponseDto());

		mockMvc.perform(get("/page/board/modify/{id}",boardId))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(model().attributeExists("modify"))
				.andDo(print());

		then(boardService).should().getBoard(boardId);
	}

	//게시물 요청 dto
	private BoardDto.BoardRequestDto boardRequestDto(){
		return BoardDto.BoardRequestDto
				.builder()
				.build();
	}
	//게시물 응답 dto
	private BoardDto.BoardResponseDto  boardResponseDto(){

		Board board = Board.builder()
				.boardId(4)
				.boardAuthor("well")
				.boardContents("test")
				.boardTitle("testTitle")
				.readcount(0)
				.category(categoryDto())
				.member(memberDto())
				.createdat(LocalDateTime.now()).build();

		return BoardDto.BoardResponseDto
				.builder()
				.board(board)
				.build();
	}
	//회원 dto
	private Member memberDto(){
		return Member.builder()
				.id(1)
				.username("well")
				.membername("tester1")
				.password("$2a$10$NtPkdBqddj6ZYmbUpTS9Ve9T2WU4EUVUhN3uAFxzKUzecFxmGLy4W")
				.useremail("well123@Test.com")
				.role(Role.ROLE_ADMIN)
				.createdAt(LocalDateTime.now())
				.build();
	}

	private Category categoryDto(){
		return Category
				.builder()
				.id(1)
				.name("freeboard")
				.parent(null)
				.build();
	}
}
