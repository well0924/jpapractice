package co.kr.board.testmember;

import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MemberApiControllerTest {
    @Autowired
    MockMvc mvc;
    @Mock
    Member member;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원가입")
    public void memberjoinTest()throws Exception{
        //given
        MemberDto.MemberRequestDto dto = MemberDto.MemberRequestDto
                .builder()
                .username("test1")
                .password("qwer4148!")
                .membername("testuser1")
                .useremail("testuser2@test.com")
                .role(Role.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build();

        String result = "";
        //when&then
        mvc.perform(
                post("/api/login/memberjoin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(result));
    }
}
