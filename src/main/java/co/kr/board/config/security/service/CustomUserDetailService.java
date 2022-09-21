package co.kr.board.config.security.service;

import java.util.Optional;

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
		
		Optional<Member> member = Optional.ofNullable(repository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("조회된 아이디가 없습니다.")));
		
		Member userdetail = member.get();
		
		log.info(member.toString());
		log.info(userdetail.getRole());
		
		return new CustomUserDetails(userdetail);
	}

}
