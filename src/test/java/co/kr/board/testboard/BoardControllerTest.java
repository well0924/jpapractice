package co.kr.board.testboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.kr.board.config.security.SecurityConfig;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class BoardControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	private ObjectMapper objectMapper;
	
	//@WithMockUser
	@DisplayName("게시판 목록 화면- 목록을 화면에 출력")
	@Test
	public void cotrollerViewTest()throws Exception{
		given("").willReturn("");
		mockMvc
		.perform(get("/page/board/list")
		.contentType(MediaType.TEXT_HTML)
		.content(""))
		.andExpect(view().name("board/boardlist"))
		.andExpect(status().isOk())
		.andDo(print());
	}
	
	@WithMockUser
	@DisplayName("게시글 조회 화면-단일글을 출력한다.")
	@Test
	public void controllerDetailViewTest()throws Exception{
		
		Integer boardId = 4;
		
		mockMvc.perform(get("/page/board/detail/"+boardId))
		.andExpect(status().isOk())
		.andDo(print());
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
		
		Integer boardId =4;
		
		mockMvc
		.perform(get("/page/board/modify/"+boardId))
		.andExpect(status().isOk())
		.andDo(print());
	}


	@WithMockUser
	@DisplayName("게시글 수정화면-특정게시글을 수정을 한다.")
	@Test
	public void controllerPostEditeProcTest()throws Exception{

		Integer boardId =4;

		mockMvc
				.perform(get("/page/board/modify/"+boardId))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@WithMockUser
	@Test
	@DisplayName("메인화면")
	public void controllerMainPageViewTest()throws Exception{
		mockMvc
		.perform(get("/page/main/mainpage"))
		.andExpect(view().name("main/mainpage"))
		.andExpect(status().isOk())
		.andDo(print());
	}
}
