package co.kr.board.CustomSecurity;

import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.login.domain.dto.LoginDto;
import co.kr.board.login.domain.dto.TokenResponse;
import co.kr.board.login.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @SpyBean
    JwtTokenProvider jwtTokenProvider;
    @MockBean
    private MemberService memberService;
    ObjectMapper objectMapper = new ObjectMapper();
    @Value("${jwt.secret}")
    private String secretKey;
    private long tokenValidTime = 30 * 60 * 1000L;
    private long refreshtokenValidTime = 1000 * 60 * 60 * 24 * 7;
    long now = (new Date()).getTime();
    Date accessTime = new Date(now+tokenValidTime);
    Date refreshTime =new Date(now+refreshtokenValidTime);
    long accessTimes = accessTime.getTime();
    long refreshTimes = refreshTime.getTime();

    @AfterEach
    public void clear(){
        Mockito.reset(jwtTokenProvider);
    }

    private String createToken(String userName, List<String> roles, Date now, int expireMin, String secretKey) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("roles", roles);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessTime)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String accessToken(){
        return createToken("well4149", Collections.singletonList("ROLE_ADMIN"),accessTime,10,secretKey);
    }

    public String refreshToken(){
        return createToken("well4149",Collections.singletonList("ROLE_ADMIN"),refreshTime,10,secretKey);
    }
    @Test
    //@Disabled
    @DisplayName("[api]로그인 인증")
    public void loginAuthTest()throws Exception{
        String accessToken = accessToken();
        String refreshToken = refreshToken();

        TokenResponse tokenResponse= ResultToken();

        given(memberService.signin(loginDto())).willReturn(ResultToken());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/login/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(loginDto())))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(accessToken,tokenResponse.getAccessToken());
        //verify(memberService).signin(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("[api]로그인 테스트-재발급")
    public void loginAuthReIssueTest(){

    }

    @Test
    @DisplayName("토큰 유효성 검사")
    public void TokenValidationTest()throws Exception{

    }

    //로그인Dto
    private LoginDto loginDto(){
        return LoginDto
                .builder()
                .username("well4149")
                .password("qwer4149!!")
                .build();
    }

    TokenResponse ResultToken() {
        return TokenResponse.builder()
                .accessToken(accessToken())
                .refreshToken(refreshToken())
                .build();
    }
}
