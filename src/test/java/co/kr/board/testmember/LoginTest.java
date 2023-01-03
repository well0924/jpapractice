package co.kr.board.testmember;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.login.domain.Role;

@SpringBootTest
public class LoginTest {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Test
	@DisplayName("토큰 생성 테스트")
	public void createToken() {
		String username = "well";
		String password = "qwer4149!";
				
		String result =jwtTokenProvider.createToken(username, Role.ADMIN);
		
		System.out.println(result);
	}
}
