package co.kr.board.testreply;

import co.kr.board.CustomSecurity.TestCustomUserDetailsService;
import co.kr.board.board.domain.Board;
import co.kr.board.config.security.SecurityConfig;
import co.kr.board.config.security.auth.CustomUserDetails;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.repository.MemberRepository;
import co.kr.board.reply.domain.Comment;
import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.repository.CommentRepository;
import co.kr.board.reply.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
@DisplayName("ReplyController Test")
public class ReplyApiControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    CommentService commentService;
    @MockBean
    CommentRepository commentRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MemberRepository memberRepository;
    private CustomUserDetails customUserDetails;
    private final TestCustomUserDetailsService testCustomUserDetailsService = new TestCustomUserDetailsService();

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        memberRepository.save(memberDto());

        customUserDetails = (CustomUserDetails)testCustomUserDetailsService.loadUserByUsername(memberDto().getUsername());
    }

    @Test
    @DisplayName("[api]댓글목록-성공")
    @WithMockUser(username = "well",roles = "ADMIN")
    public void replylistTest()throws Exception{
        List<CommentDto.CommentResponseDto>commentResponseDtoList = Arrays.asList(responseDto());

        when(commentService.findCommentsBoardId(responseDto().getBoardId())).thenReturn(commentResponseDtoList);

        mvc.perform(get("/api/reply/list/{id}",comment().getBoard().getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(commentService).findCommentsBoardId(any());
    }

    @Test
    @DisplayName("[api]댓글작성-성공")
    public void replyWriteTest()throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .post("/api/reply/write/{id}",comment().getBoard().getId())
                        .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(requestDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]댓글작성-성공-인증이 안된경우-응답코드 401")
    public void replyWriteTestFail()throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/reply/write/{id}",comment().getBoard().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto())))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]댓글수정- 성공")
    public void replyUpdateTest()throws Exception{
        given(commentRepository.findById(1)).willReturn(Optional.of(comment()));

        mvc.perform(MockMvcRequestBuilders.put("/api/reply/update/{id}",1)
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("[api]댓글수정- 응답40xs: user가 인증이 안된경우")
    //@WithMockUser
    public void replyUpdateTestFail()throws Exception{
        given(commentRepository.findById(1)).willReturn(Optional.of(comment()));

        mvc.perform(MockMvcRequestBuilders.put("/api/reply/update/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto())))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]댓글삭제-성공")
    public void replyDeleteTest()throws Exception{
        int replyId = 1;

        given(commentRepository.findById(eq(replyId))).willReturn(Optional.of(comment()));

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/reply/delete/{id}",replyId)
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andDo(print());

    }
    //댓글요청dto
    private CommentDto.CommentRequestDto requestDto(){
        return CommentDto.CommentRequestDto
                .builder()
                .replyContents("test")
                //.createdAt(LocalDateTime.of(2023,1,10,0,0))
                .build();
    }
    //댓글응답
    private static CommentDto.CommentResponseDto responseDto(){
        return CommentDto.CommentResponseDto
                .builder()
                .replyId(1)
                .boardId(1)
                .replyContents("tester")
                .replyWriter("well")
                .build();
    }
    //댓글객체
    private static Comment comment(){
        return Comment.builder()
                .replyWriter("well")
                .replyContents("tester")
                .member(memberDto())
                .board(board())
                .build();
    }
    //회원 dto
    private static Member memberDto(){
        return Member.builder()
                .id(1)
                .username("well4149")
                .membername("tester1")
                .password("$2a$12$XcIiB0doaPMx0AoRv0G0f.enty5bjsZADwrmw7SmgNZuI4yQVmRSu")
                .useremail("well123@Test.com")
                .role(Role.ROLE_ADMIN)
                .createdAt(LocalDateTime.now())
                .build();
    }

    //게시글 객체
    private static Board board(){
        return Board.builder().member(memberDto())
                .boardId(1)
                .boardTitle("title")
                .boardAuthor(memberDto().getUsername())
                .readcount(0)
                .member(memberDto())
                .boardContents("contetns").build();
    }
}
