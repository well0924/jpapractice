package co.kr.board.CustomSecurity;

import co.kr.board.config.security.auth.CustomUserDetails;
import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.domain.Dto.TokenDto;
import co.kr.board.domain.Member;
import co.kr.board.domain.Role;
import co.kr.board.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@DisplayName("jwt token test")
@SpringBootTest
@AutoConfigureMockMvc
public class TokenTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    BCryptPasswordEncoder encode;
    private CustomUserDetails customUserDetails;
    private final TestCustomUserDetailsService customUserDetailsService= new TestCustomUserDetailsService();
    @Value("${jwt.secret}")
    private String secretKey;

    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Test
    @DisplayName("토큰 생성")
    public void tokenGenerate(){
        TokenDto result = jwtTokenProvider.createTokenDto(memberDto().getUsername(),memberDto().getRole());

        String accessToken= result.getAccessToken();
        String refreshToken = result.getRefreshToken();

        String userPk=jwtTokenProvider.getUserPK(accessToken);
        String userpk = jwtTokenProvider.getUserPK(refreshToken);
        System.out.println(accessToken);
        assertThat(userPk).isEqualTo(userpk);
    }
    @Test
    @DisplayName("토큰 유효성 검사-유효기간이 지난 토큰검증")
    public void tokenValidationTest(){
        String accessToken ="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3ZWxsNDE0OSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImV4cCI6MTY3ODg3MzI1NH0.dmM_EwSJa7DOrObMa2LlNfLSE5zS_kacpKizxvl9afw";

        try {
            boolean result=jwtTokenProvider.validateToken(accessToken);
        }catch (ExpiredJwtException e){
            e.getMessage();
            assertThat("만료된 JWT 토큰입니다.").isEqualTo(e.getMessage());
        }
    }

    @Test
    @DisplayName("토큰 유효성 검사-잘못된 토큰서명")
    public void tokenValidationTestFail(){
        String vaildToken = "??????sfsdffdfdfdf";
        try {
            jwtTokenProvider.validateToken(vaildToken);
        }catch (IllegalArgumentException e){
            e.getMessage();
            assertThat("잘못된 JWT 서명입니다.").isEqualTo(e.getMessage());
        };
    }
    private Member memberDto(){
        return Member
                .builder()
                .id(1)
                .username("well4149")
                .password(encode.encode("qwer4149!!"))
                .membername("tester1")
                .useremail("well4149@naver.com")
                .role(Role.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
