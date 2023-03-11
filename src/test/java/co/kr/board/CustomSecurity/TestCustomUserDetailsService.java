package co.kr.board.CustomSecurity;

import co.kr.board.config.security.auth.CustomUserDetails;
import co.kr.board.domain.Member;
import co.kr.board.domain.Role;
import co.kr.board.repository.MemberRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.LocalDateTime;

@Profile("tester")
public class TestCustomUserDetailsService implements UserDetailsService {

    private MemberRepository repository;
    private Member getUser(){
        return Member
                .builder()
                .id(1)
                .username("well4149")
                .membername("tester123")
                .password("$2a$12$XcIiB0doaPMx0AoRv0G0f.enty5bjsZADwrmw7SmgNZuI4yQVmRSu")
                //qwer4149!!
                .useremail("well4149@Test.com")
                .role(Role.ROLE_ADMIN)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("well4149")) {
            return new CustomUserDetails(getUser());
        }
        return null;
    }
}
