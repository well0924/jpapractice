package co.kr.board.testmember;

import co.kr.board.config.security.SecurityConfig;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.LoginDto;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.TokenDto;
import co.kr.board.login.domain.dto.TokenResponse;
import co.kr.board.login.service.MemberService;
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

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("[view]로그인 테스트")
    public void loginPageTest()throws Exception{
        mvc
        .perform(get("/page/login/loginpage"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login/loginpage"))
                .andDo(print());
    }

    @Test
    @DisplayName("[api]로그인 테스트-인증")
    public void loginAuthTest()throws Exception{
        given(memberService.signin(loginDto())).willReturn(new TokenResponse("accessToken","refreshToken"));

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/login/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]로그인 테스트-재발급")
    public void loginAuthReIssueTest(){

    }

    @Test
    @DisplayName("[view]회원가입 테스트")
    public void memberJoinTest()throws Exception{
        mvc.perform(get("/page/login/memberjoin"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login/memberjoin"))
                .andDo(print());
    }

    @Test
    @DisplayName("[api]회원가입 테스트")
    public void memberJoinApiTest()throws Exception{

        mvc.perform(MockMvcRequestBuilders.post("/api/login/memberjoin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[view]아이디찾기 테스트")
    public void userIdFindTest()throws Exception{
        mvc.perform(get("/page/login/finduserid"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("login/userfindid"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]아이디찾기 테스트")
    public void userIdFindApiTest()throws Exception{

        given(memberService.findByMembernameAndUseremail(memberDto().getMembername(),memberDto().getUseremail()))
                .willReturn(memberDto().getUsername());

        mvc.perform(
                post("/api/login/userfind/{name}/{email}", requestDto().getMembername(),requestDto().getUseremail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        then(memberService).should().findByMembernameAndUseremail(memberDto().getMembername(),memberDto().getUseremail());
    }

    @Test
    @DisplayName("[view]비밀번호 재발급")
    @WithMockUser
    public void passwordChangePageTest()throws Exception{
        mvc.perform(get("/page/login/finduserpw")
                        .contentType(MediaType.TEXT_HTML))
                //.andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login/userfindpw"))
                .andDo(print());
    }

    @Test
    @DisplayName("[api]비밀번호 재발급")
    public void passwordChangeApiTest()throws Exception{

    }

    @Test
    @DisplayName("[view]회원 수정페이지")
    @WithMockUser
    public void userUpdatePage()throws Exception{
        given(memberService.getMember(memberDto().getId())).willReturn(responseDto());
        mvc
        .perform(get("/page/login/memberupdate/{id}",memberDto().getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(model().attributeExists("detail"))
        .andDo(print());

        then(memberService).should().getMember(responseDto().getUseridx());
    }

    @Test
    @DisplayName("[api]회원수정")
    public void userUpdateApiTest()throws Exception{

    }
    @Test
    @DisplayName("[view]회원탈퇴")
    @WithMockUser
    public void userDeletePageTest()throws Exception{
        mvc.perform(get("/page/login/memberdelete").contentType(MediaType.TEXT_HTML))
                //.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login/memberdelete"))
                .andDo(print());
    }
    @Test
    @DisplayName("[api]회원탈퇴")
    public void userDeleteApiTest()throws Exception{

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

    private MemberDto.MemeberResponseDto responseDto(){
       return MemberDto.MemeberResponseDto
               .builder()
               .useridx(1)
               .username("well")
               .membername("tester1")
               .password("Qwer4149!")
               .useremail("well123@Test.com")
               .role(Role.ROLE_ADMIN)
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
