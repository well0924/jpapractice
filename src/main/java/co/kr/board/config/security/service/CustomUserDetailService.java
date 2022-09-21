package co.kr.board.config.security.service;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import co.kr.board.config.security.vo.CustomUserDetails;
import co.kr.board.config.security.vo.SessionDto;
import co.kr.board.login.domain.Member;
import co.kr.board.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{
	
	private final MemberRepository repository;
	
	private final HttpSession session;

	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
		log.info("userservice.");
		Member member = repository
				.findByUserid(userid)
				.orElseThrow(()->new UsernameNotFoundException("해당 사용자가 존재하지 않습니다."+userid));
		
		session.setAttribute("user", new SessionDto(member));
		
		log.info("serviceResult:"+member);
		
		return new CustomUserDetails(member);
	}
	
	
}
