package co.kr.board.config.security.auth;

import java.util.Optional;

import co.kr.board.config.redis.CacheKey;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.kr.board.domain.Member;
import co.kr.board.repository.MemberRepository;
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
		Optional<Member> member = Optional.ofNullable(repository.findByUsername(userPk).orElseThrow(()-> new UsernameNotFoundException("조회된 아이디가 없습니다.")));
		Member userdetail = member.get();
		return new CustomUserDetails(userdetail);
	}

}
