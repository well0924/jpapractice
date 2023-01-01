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

import co.kr.board.config.security.SecurityConfig;


@SpringBootTest
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class BoardControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@WithMockUser
	@DisplayName("게시판 목록 화면")
	@Test
	public void cotrollerViewTest()throws Exception{
		
		mockMvc
		.perform(get("/page/board/list")
		.contentType(MediaType.TEXT_HTML))
		.andExpect(view().name("board/boardlist"))
		.andExpect(status().isOk())
		.andDo(print());
	}
	
	@WithMockUser
	@DisplayName("게시글 조회 화면")
	@Test
	public void controllerDetailViewTest()throws Exception{
		
		Integer boardId = 10;
		
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
	@DisplayName("게시글 수정화면")
	@Test
	public void controllerPostEditeViewTest()throws Exception{
		
		Integer boardId =10;
		
		mockMvc
		.perform(get("/page/board/modify/"+boardId))
		.andExpect(status().isOk())
		.andDo(print());
	}
}
