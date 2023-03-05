package co.kr.board.testmember;

import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("MemberApiController Test")
public class MemberApiControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private MemberService memberService;
    ObjectMapper objectMapper = new ObjectMapper();
    //@MockBean
    //JwtTokenProvider jwtTokenProvider;


    @Test
    @DisplayName("[api]회원목록-성공")
    @WithMockUser(username = "well",roles = "ADMIN")
    public void memberListApiTest()throws Exception{
        List<MemberDto.MemeberResponseDto> list = Arrays.asList(responseDto());

        when(memberService.findAll()).thenReturn(list);

        mvc.perform(get("/api/login/list").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).findAll();
    }
    
    @Test
    @DisplayName("[api]회원조회-성공")
    @WithMockUser
    public void memberDetailTest()throws Exception{
        given(memberService.getMember(any())).willReturn(responseDto());

        mvc.perform(get("/api/login/detailmember/{id}/member",requestDto().getUseridx())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).getMember(any());
    }
    
    @Test
    @DisplayName("[api]회원가입-성공")
    public void memberJoinApiTest()throws Exception{

        given(memberService.memberjoin(any())).willReturn(responseDto().getUseridx());

        mvc.perform(MockMvcRequestBuilders.post("/api/login/memberjoin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto()))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());

        verify(memberService).memberjoin(refEq(requestDto()));
    }


    @Test
    @DisplayName("[api]아이디찾기-성공")
    public void userIdFindApiTest()throws Exception{

        given(memberService.findByMembernameAndUseremail(memberDto().getMembername(),memberDto().getUseremail()))
                .willReturn(memberDto().getUsername());

        mvc.perform(post("/api/login/userfind/{name}/{email}", requestDto().getMembername(),requestDto().getUseremail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());

        then(memberService).should().findByMembernameAndUseremail(memberDto().getMembername(),memberDto().getUseremail());
    }

    @Test
    @DisplayName("[api]아이디 중복확인(중복)-성공")
    public void givenDuplicateUserId_whenCheckMemberIdDuplicate_thenDuplicatedUserId()throws Exception{
        given(memberService.checkmemberIdDuplicate(requestDto().getUsername())).willReturn(true);

        mvc.perform(get("/api/login/logincheck/{id}",requestDto().getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).checkmemberIdDuplicate(any());
    }

    @Test
    @DisplayName("[api]아이디 중복확인(사용가능)-성공")
    public void givenUserId_whenCheckMemberIdDuplicate_thenNotDuplicateUserId()throws Exception{
        given(memberService.checkmemberIdDuplicate("well23")).willReturn(false);

        mvc.perform(get("/api/login/logincheck/{id}","well123")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).checkmemberIdDuplicate(any());
    }
    @Test
    @DisplayName("[api]이메일 중복확인(중복)-성공")
    public void givenDuplicateUserEmail_whenCheckMemberEmailDuplicate_thenDuplicateUserEmail()throws Exception{
        given(memberService.checkmemberEmailDuplicate(requestDto().getUseremail())).willReturn(true);

        mvc.perform(get("/api/login/emailcheck/{email}",requestDto().getUseremail())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).checkmemberEmailDuplicate(any());
    }

    @Test
    @DisplayName("[api]이메일 중복확인(사용가능)-성공")
    public void givenUserEmail_whenCheckMemberEmailDuplicate_thenUserEmail()throws Exception{
        given(memberService.checkmemberEmailDuplicate("well41491@test.com")).willReturn(false);

        mvc.perform(get("/api/login/emailcheck/{email}","well41491@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).checkmemberEmailDuplicate(any());
    }

    @Test
    @DisplayName("[api]비밀번호 재발급-성공")
    @WithMockUser
    public void passwordChangeApiTest()throws Exception{

        given(memberService.getMember(1)).willReturn(responseDto());

        mvc.perform(MockMvcRequestBuilders.put("/api/login/passwordchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto()))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    @DisplayName("[api]회원수정-성공")
    public void userUpdateApiTest()throws Exception{
        given(memberService.memberupdate(any(),any())).willReturn(responseDto().getUseridx());

        mvc.perform(
                MockMvcRequestBuilders
                .put("/api/login/memberupdate/{id}/member",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto())))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).memberupdate(any(),any());
    }

    @Test
    @DisplayName("[api]회원탈퇴-성공")
    @WithMockUser
    public void userDeleteApiTest()throws Exception{

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/login/memberdelete/{idx}/member",requestDto().getUseridx())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).memberdelete(any());
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

    //회원 요청 dto
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

}
