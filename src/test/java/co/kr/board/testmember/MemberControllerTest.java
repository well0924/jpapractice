package co.kr.board.testmember;

import co.kr.board.config.security.SecurityConfig;
import co.kr.board.domain.Member;
import co.kr.board.domain.Role;
import co.kr.board.domain.Dto.LoginDto;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
@DisplayName("MemberController Test")
public class MemberControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private MemberService memberService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("[view]로그인-성공")
    public void loginPageTest()throws Exception{
        mvc
        .perform(get("/page/login/loginpage"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login/loginpage"))
                .andDo(print());
    }

    @Test
    @DisplayName("[view]회원목록-성공")
    @WithMockUser(username = "well",roles = "ADMIN")
    public void memberlistTest()throws Exception{
        mvc.perform(get("/page/login/adminlist")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[view]회원목록-인증이 안된경우-성공")
    public void memberlistTestFail()throws Exception{
        mvc.perform(get("/page/login/list")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("[view]회원단일조회-성공")
    @WithMockUser(roles = "ADMIN",username = "well")
    public void memberDetailTest()throws Exception{
        given(memberService.getMember(1)).willReturn(responseDto());

        mvc.perform(get("/page/login/detail/{id}",1)
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("detail"))
                .andDo(print());

        verify(memberService).getMember(1);
    }

    @Test
    @DisplayName("[view]회원가입-성공")
    public void memberJoinTest()throws Exception{
        mvc.perform(get("/page/login/memberjoin"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login/memberjoin"))
                .andDo(print());
    }

    @Test
    @DisplayName("[view]아이디찾기-성공")
    public void userIdFindTest()throws Exception{
        mvc.perform(get("/page/login/finduserid"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("login/userfindid"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[view]비밀번호 재발급-성공")
    @WithMockUser
    public void passwordChangePageTest()throws Exception{
        mvc.perform(get("/page/login/finduserpw")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login/userfindpw"))
                .andDo(print());
    }

    @Test
    @DisplayName("[view]회원수정-성공")
    @WithMockUser
    public void userUpdatePage()throws Exception{
        given(memberService.getMember(memberDto().getId())).willReturn(responseDto());

        mvc
        .perform(get("/page/login/memberupdate/{id}",memberDto().getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(view().name("login/membermodify"))
        .andExpect(model().attributeExists("detail"))
        .andDo(print());

        then(memberService).should().getMember(responseDto().getUseridx());
    }

    @Test
    @DisplayName("[view]회원탈퇴-성공")
    @WithMockUser
    public void userDeletePageTest()throws Exception{
        mvc.perform(get("/page/login/memberdelete").contentType(MediaType.TEXT_HTML))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login/memberdelete"))
                .andDo(print());
    }

    //회원조회 dto
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
    private MemberDto.MemberRequestDto requestDto(){
        return MemberDto.MemberRequestDto
                .builder()
                .useridx(1)
                .username("well")
                .membername("tester1")
                .password("Qwer4149!")
                .useremail("well123@Test.com")
                .role(Role.ROLE_ADMIN)
                .build();
    }

    private MemberDto.MemberRequestDto updateDto(){
        return MemberDto.MemberRequestDto.builder()
                .password("Qsdvger12%")
                .build();
    }

    private MemberDto.MemeberResponseDto responseDto(){
       return MemberDto.MemeberResponseDto
               .builder()
               .member(memberDto())
               .build();
    }

    //로그인Dto
    private LoginDto loginDto(){
        return LoginDto
                .builder()
                .username("well")
                .password("qwer4149!")
                .build();
    }


}
