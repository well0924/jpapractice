package co.kr.board.config.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginFailuererHandler implements AuthenticationFailureHandler{
	
	private final String failUrl = "/login?error=true";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String errorMessage = null;
		
		if(exception instanceof BadCredentialsException || 
				exception instanceof InternalAuthenticationServiceException) {
			
			errorMessage = "아이디나 비밀번호가 맞지 않습니다.";
		}
		else if(exception instanceof DisabledException) {
			errorMessage = "계정이 비활성화 되었습니다.";
		}
		
		request.setAttribute("errorMessage", errorMessage);
		request.getRequestDispatcher(failUrl).forward(request, response);
	}
}
