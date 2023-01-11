package co.kr.board.CustomSecurity;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.board.service.BoardService;
import co.kr.board.config.security.vo.CustomUserDetails;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSecurityCustomTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    private CustomUserDetails customUserDetails;
    private final TestCustomUserDetailsService testCustomUserDetailsService = new TestCustomUserDetailsService();
    private MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        memberRepository.save(getUser());

        customUserDetails = (CustomUserDetails)testCustomUserDetailsService.loadUserByUsername(getUser().getUsername());
    }

    @Test
    @DisplayName("글 작성테스트")
    public void given_whenBoardSave_thenReturnBoardId()throws Exception{

        mvc.perform(post("/api/board/write")
                        .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(
                       objectMapper.writeValueAsString(requestDto())
                ))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    private Board getBoard(){
        return Board
                .builder()
                .boardId(1)
                .boardTitle("제목")
                .boardAuthor(getUser().getUsername())
                .boardContents("내용임....")
                .readcount(1)
                .member(getUser())
                .createdat(LocalDateTime.now())
                .build();
    }
    private Member getUser(){
        return Member.builder()
                .id(1)
                .username("well4149")
                .membername("tester1")
                .password("qwer4149!!")
                .useremail("well4149!@Test.com")
                .role(Role.ROLE_ADMIN)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private BoardDto.BoardRequestDto requestDto(){
        return BoardDto.BoardRequestDto
                .builder()
                .boardTitle("테스트")
                .boardContents("sodyd")
                .readCount(1)
                .build();
    }
}
