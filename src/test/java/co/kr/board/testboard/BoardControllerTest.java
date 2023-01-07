package co.kr.board.testboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.service.BoardService;
import co.kr.board.login.domain.Member;
import co.kr.board.login.repository.MemberRepository;
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
import co.kr.board.config.security.SecurityConfig;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


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
	@MockBean
	private MemberRepository memberRepository;

	@DisplayName("게시판 목록 화면- 목록을 화면에 출력")
	@Test
	@WithMockUser
	public void cotrollerViewTest()throws Exception{
		//given
		given(boardService.findAllPage(any(Pageable.class))).willReturn(Page.empty());
		//when
		mockMvc
		.perform(get("/page/board/list")
		.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(view().name("board/boardlist"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("list"))
		.andDo(print());

	}
	
	@WithMockUser(username = "well",authorities = "ROLE_ADMIN")
	@DisplayName("[api] 게시글 조회")
	@Test
	public void controllerDetailViewTest()throws Exception{
		int boardId = 4;
		mockMvc.perform(get("/api/board/detail/{boardId}",boardId))
		.andExpect(status().isOk())
		.andDo(print());

		verify(boardService).getBoard(boardId);
	}
	
	@WithMockUser
	@DisplayName("게시글 작성화면")
	@Test
	public void controllerPostViewTest()throws Exception{
		
		mockMvc
		.perform(get("/page/board/write"))
		.andExpect(view().name("board/writeboard"))
		.andExpect(status().isOk())
		.andDo(print());
	}

	@WithMockUser
	@DisplayName("게시글 작성화면-게시글을 작성")
	@Test
	public void controllerPostViewProcTest()throws Exception{

		mockMvc
				.perform(get("/page/board/write"))
				.andExpect(view().name("board/writeboard"))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@WithMockUser
	@DisplayName("게시글 수정화면")
	@Test
	public void controllerPostEditeViewTest()throws Exception{
		
		int boardId =4;
		
		mockMvc
		.perform(get("/page/board/modify/"+boardId))
		.andExpect(status().isOk())
		.andDo(print());
	}


	@WithMockUser
	@DisplayName("게시글 수정화면-특정게시글을 수정을 한다.")
	@Test
	public void controllerPostEditeProcTest()throws Exception{

		int boardId =4;

		mockMvc
				.perform(get("/page/board/modify/"+boardId))
				.andExpect(status().isOk())
				.andDo(print());
	}

}
