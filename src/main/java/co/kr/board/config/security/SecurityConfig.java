package co.kr.board.config.security;

import co.kr.board.config.security.jwt.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import co.kr.board.config.security.auth.CustomUserDetailService;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Log4j2
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class SecurityConfig{
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailService customUserDetailService;

	//비밀번호 암호화
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration)throws Exception{
		return authConfiguration.getAuthenticationManager();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web
				.httpFirewall(defaultFireWell())
				.ignoring()
				.antMatchers("/images/**", "/js/**","/font/**", "/webfonts/**","/istatic/**",
						"/main/**", "/webjars/**", "/dist/**", "/plugins/**", "/css/**","/favicon.ico","/h2-console/**","/css/**",
						"/vendor/**","/scss/**","/**");
	}

	@Bean
	public SecurityFilterChain cofigure(HttpSecurity http)throws Exception{

		log.info("security config!!");
		http
				.cors().disable()
				.csrf().disable()//rest Api 를 사용하므로 csrf는 차단.
				.httpBasic().disable()// httpBasic 방식은 Authorization에 ID,PW를 들고 다니는 방식이다. <-> Bearer 방식 (토큰을 들고다니는 방식)
				.formLogin().disable()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)//로그인에 사용되는 filter
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)//jwt를 사용하기 위해서 session을 사용하지 않는다.
				.and()
				.authorizeRequests()//요청 url에 따라서 권한설정하기.
				.antMatchers("/api/member/*").permitAll()
				.antMatchers(HttpMethod.GET,"/api/member/list/search").permitAll()
				.antMatchers(HttpMethod.GET,"/api/member/list").access("hasRole('ROLE_ADMIN')")
				.antMatchers(HttpMethod.GET,"/page/member/board/list").access("hasRole('ROLE_ADMIN')")
				.antMatchers(HttpMethod.GET,"/page/member/comment/list").access("hasRole('ROLE_ADMIN')")
				.antMatchers(HttpMethod.GET,"/api/member/detailmember/{id}/member").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
				.antMatchers("/api/login/*").permitAll()
				.antMatchers("/api/reply/*").permitAll()
				.antMatchers("/api/board/*").access("hasRole('ROLE_USER') or hasRole('ROLE_USER')")
				.antMatchers("/page/**").permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.exceptionHandling()//jwt로 로그인을 했을시 실패를 한 경우 handling
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler);

		return http.build();
	}

	@Bean
	public HttpFirewall defaultFireWell(){
		return new DefaultHttpFirewall();
	}
}
