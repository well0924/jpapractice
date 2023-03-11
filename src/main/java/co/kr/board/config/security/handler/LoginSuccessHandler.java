package co.kr.board.config.security.handler;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import co.kr.board.domain.Role;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler{
	
	private final RequestCache requestCache = new HttpSessionRequestCache();
	private final RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();

	private static final String DEFAULT_URL= "/page/main/mainpage";
	private static final String ADMIN_URL="/page/login/adminlist";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		log.info("successhandler.");
		
		try {
			loginPrevPage(request,response,authentication);
			
			redirectStrategy(request, response, authentication);
			
			clearAuthenticationAttributes(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		if(session !=null) {
	
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		
		}
	}
	
	private void redirectStrategy(HttpServletRequest request,HttpServletResponse response,Authentication authentication)throws Exception {
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		log.info(savedRequest);
			
		Set<String>roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
			
			if(roles.contains(Role.ROLE_ADMIN.getValue())) {
				
				redirectStratgy.sendRedirect(request, response,ADMIN_URL);
			
			}else if(roles.contains(Role.ROLE_USER.getValue())) {
				
				redirectStratgy.sendRedirect(request, response,DEFAULT_URL);
			
		}
		
	}
	
	private void loginPrevPage(HttpServletRequest request,HttpServletResponse response,Authentication authentication)throws Exception{
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		log.info(savedRequest);
		
		String prevPage = (String) request.getSession().getAttribute("prevPage");
        
		if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
        }
	}
}
