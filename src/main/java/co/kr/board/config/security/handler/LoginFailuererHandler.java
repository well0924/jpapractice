package co.kr.board.config.security.handler;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class LoginFailuererHandler extends SimpleUrlAuthenticationFailureHandler{
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String errorMsg="";
		
		if(exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
			errorMsg = "아이디나 비밀번호가 맞지 않습니다.";
		}
		else if(exception instanceof DisabledException) {
			errorMsg = "계정이 비활성화 되었습니다.";
		}
		
		log.info(errorMsg);
		log.info("결과값:"+exception);
		errorMsg = URLEncoder.encode(errorMsg, "UTF-8");
		setDefaultFailureUrl("/page/login/loginpage?error=true&exception="+errorMsg);
		
		super.onAuthenticationFailure(request, response, exception);
	}
}
