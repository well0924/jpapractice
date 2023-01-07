package co.kr.board.testmember;

import co.kr.board.config.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
@DisplayName("MemberControllerView Test")
public class MemberControllerTest {
    @Autowired
    private MockMvc mvc;
    
    @Test
    @DisplayName("로그인화면 테스트")
    public void loginpagetest(){
        
    }
}
