package co.kr.board.config.Security.auth;

import java.util.Optional;

import co.kr.board.config.Redis.CacheKey;
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
	//캐시 설정
	@Cacheable(key ="#userPk",value = CacheKey.USER,unless = "#result == null")
	public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
		log.info("userDetailService");
		Optional<Member> member = Optional
				.ofNullable(repository
						.findByUsername(userPk)
						.orElseThrow(()-> new UsernameNotFoundException("조회된 아이디가 없습니다.")));

		if(member.isPresent()){
			CustomUserDetails customUserDetails = new CustomUserDetails(member.get());
			return customUserDetails;
		}
		return null;
	}

}
