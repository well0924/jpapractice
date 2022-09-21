package co.kr.board.config.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.kr.board.config.security.vo.CustomUserDetails;
import co.kr.board.login.domain.Member;
import co.kr.board.login.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService{
	
	private final MemberRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("userdetailservice-------");
		Member member = repository.findByUsername(username);
		
		if(member == null) {
			new UsernameNotFoundException("조회된 회원이 없습니다.");
		}
		log.info(member.toString());
		log.info(member.getRole());
		return new CustomUserDetails(member);
	}

}
