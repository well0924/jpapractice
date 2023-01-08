package co.kr.board.testreply;

import co.kr.board.board.domain.Board;
import co.kr.board.config.security.SecurityConfig;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.reply.domain.Comment;
import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
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

import java.time.LocalDateTime;
import java.util.List;

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
    @MockBean
    private CommentService commentService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("[api]댓글목록")
    @WithMockUser
    public void replylistTest()throws Exception{
        mvc.perform(get("/api/reply/list/{id}",comment().getBoard().getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(commentService).findCommentsBoardId(board().getId());
    }

    @Test
    @DisplayName("[api]댓글목록-실패")
    //@WithMockUser
    public void replylistTestFail()throws Exception{
        mvc.perform(get("/api/reply/list/{id}",comment().getBoard().getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        //verify(commentService).findCommentsBoardId(board().getId());
    }

    @Test
    @DisplayName("[api]댓글작성")
    //@WithMockUser(username = "well",authorities = "ROLE_ADMIN")
    public void replyWriteTest()throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/reply/write/{id}",comment().getBoard().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]댓글작성-인증이 안된경우")
    public void replyWriteTestFail()throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/reply/write/{id}",comment().getBoard().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto())))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]댓글수정")
    @WithMockUser
    @Disabled
    public void replyUpdateTest()throws Exception{

        mvc.perform(MockMvcRequestBuilders.put("/api/reply/update/{id}",comment().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto())))
                //.andExpect(status().isOk())
                .andDo(print());

        verify(commentService).replyUpdate(requestDto(),memberDto(), responseDto().getReplyId());
    }

    @Test
    @DisplayName("[api]댓글삭제")
    public void replyDeleteTest()throws Exception{

    }

    private CommentDto.CommentRequestDto requestDto(){
        return CommentDto.CommentRequestDto
                .builder()
                .replyContents("test")
                .build();
    }

    private CommentDto.CommentResponseDto responseDto(){
        return CommentDto.CommentResponseDto
                .builder()
                .replyId(1)
                .boardId(1)
                .replyContents("tester")
                .replyWriter("well")
                .build();
    }

    private Comment comment(){
        return Comment.builder()
                .replyWriter("well")
                .replyContents("tester")
                .member(memberDto())
                .board(board())
                .build();
    }

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

    private Board board(){
        return Board.builder().member(memberDto())
                .boardId(1)
                .boardTitle("title")
                .boardAuthor(memberDto().getUsername())
                .readcount(0)
                .member(memberDto())
                .boardContents("contetns").build();
    }
}
