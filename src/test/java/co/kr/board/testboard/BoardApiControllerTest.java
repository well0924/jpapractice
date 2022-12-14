package co.kr.board.testboard;

import co.kr.board.CustomSecurity.TestCustomUserDetailsService;
import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.service.BoardService;
import co.kr.board.config.security.vo.CustomUserDetails;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private BoardService boardService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private MemberRepository memberRepository;
    private CustomUserDetails customUserDetails;
    private final TestCustomUserDetailsService testCustomUserDetailsService = new TestCustomUserDetailsService();

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        memberRepository.save(memberDto());

        customUserDetails = (CustomUserDetails)testCustomUserDetailsService.loadUserByUsername(memberDto().getUsername());
    }

    @DisplayName("[api]????????? ??????(?????????+??????)")
    @Test
    @Disabled
    public void controllerApiBoardListPagingSearchTest()throws Exception{
        String keyword = "test";

        given(boardService.findAllSearch(eq(keyword),any(Pageable.class))).willReturn(Page.empty());

        mockMvc.perform(get("/api/board/list/search"))
                .andExpect(status().isOk())
                .andDo(print());

        //then(boardService.findAllSearch(eq(keyword),any(Pageable.class))).should().get();
        verify(boardService).findAllSearch(eq(keyword),any(Pageable.class)).get();
    }

    @WithMockUser(username = "well",authorities = "ROLE_ADMIN")
    @DisplayName("[api] ????????? ????????????-??????")
    @Test
    public void controllerDetailApiTest()throws Exception{

        int boardId = 4;

        given(boardService.getBoard(boardId)).willReturn(boardResponseDto());

        mockMvc
                .perform(get("/api/board/detail/{boardId}",boardId))
                .andExpect(status().isOk())
                .andDo(print());

        verify(boardService).getBoard(boardId);
    }

    @DisplayName("[api]????????? ??????-??????")
    @Test
    public void controllerPostViewProcTest()throws Exception{
        mockMvc.perform(post("/api/board/write")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(boardRequestDto())))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]????????? ??????")
    public void boardUpdateApiTest()throws Exception{
        mockMvc.perform(patch("/api/board/update/{id}",1)
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8).content(objectMapper.writeValueAsString(boardRequestDto())))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]????????? ??????-??????")
    public void boardDeleteApiTest()throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/board/delete/{id}",1)
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //????????? ?????? dto
    private BoardDto.BoardRequestDto boardRequestDto(){
        return BoardDto.BoardRequestDto
                .builder()
                .boardTitle("?????????")
                .boardContents("sosdddfdfd")
                .createdAt(LocalDateTime.now())
                .readCount(1)
                .build();
    }
    //????????? ?????? dto
    private BoardDto.BoardResponseDto  boardResponseDto(){

        Board board = Board.builder()
                .boardId(4)
                .boardAuthor("well")
                .boardContents("test")
                .boardTitle("testTitle")
                .readcount(0)
                .member(memberDto())
                .createdat(LocalDateTime.now()).build();

        return BoardDto.BoardResponseDto
                .builder()
                .board(board)
                .build();
    }
    //?????? dto
    private Member memberDto(){
        return Member.builder()
                .id(1)
                .username("well4149")
                .membername("tester1")
                .password("qwer4149!!!")
                .useremail("well123@Test.com")
                .role(Role.ROLE_ADMIN)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
