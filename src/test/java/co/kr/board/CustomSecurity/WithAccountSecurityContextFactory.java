package co.kr.board.CustomSecurity;

import co.kr.board.config.security.service.CustomUserDetailService;
import co.kr.board.config.security.vo.CustomUserDetails;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.service.MemberService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDateTime;

public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {
    private final MemberService memberService;
    private final CustomUserDetailService userDetailsService;
    public WithAccountSecurityContextFactory(MemberService memberService,CustomUserDetailService userDetailsService){
        this.memberService = memberService;
        this.userDetailsService = userDetailsService;
    }
    @Override
    public SecurityContext createSecurityContext(WithAccount annotation){
        String username = annotation.value();

        MemberDto.MemberRequestDto memberRequestDto = MemberDto.MemberRequestDto
                .builder()
                .useridx(1)
                .username(username)
                .password("qwer4149!!")
                .membername("test1")
                .useremail("testUser1@Test.com")
                .role(Role.ROLE_ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            memberService.memberjoin(memberRequestDto);
        }catch (Exception e){
            e.printStackTrace();
        }

        CustomUserDetails userDetails = new CustomUserDetails(new Member(1,"well","qwer4149!","test1","well4149@test.com",Role.ROLE_ADMIN,LocalDateTime.now()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
