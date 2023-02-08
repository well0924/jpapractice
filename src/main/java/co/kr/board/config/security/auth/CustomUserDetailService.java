package co.kr.board.config.security.auth;

import java.util.Optional;

import co.kr.board.config.redis.CacheKey;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
	@Cacheable(value = CacheKey.USER, key = "#userPk", unless = "#result == null")
	public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
		log.info("userdetailservice-------");
		
		Optional<Member> member = Optional.ofNullable(repository.findByUsername(userPk)
				.orElseThrow(()-> new UsernameNotFoundException("조회된 아이디가 없습니다.")));
		
		Member userdetail = member.get();
		log.info(userPk);
		log.info(userdetail);
		return new CustomUserDetails(userdetail);
	}

}
