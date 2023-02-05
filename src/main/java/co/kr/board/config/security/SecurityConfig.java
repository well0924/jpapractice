package co.kr.board.config.security;

import javax.sql.DataSource;

import co.kr.board.config.security.jwt.*;
import co.kr.board.login.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import co.kr.board.config.security.auth.CustomUserDetailService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final CustomUserDetailService service;
	private final DataSource dataSource;
	private final CorsConfig corsConfig;
	//비밀번호 암호화
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(encoder());
	}
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Override
	public void configure(WebSecurity web) {
		web
		.ignoring()
		.antMatchers("/favicon.ico")
		.antMatchers("/webjars/**", "/dist/**", "/plugins/**", "/css/**", "/user/**","/swagger-resources/**")
		.antMatchers("/images/**", "/js/**", "/font/**", "/webfonts/**", "/main/**", "/swagger-ui/**", "/v2/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.csrf().disable()
		.httpBasic().disable()// httpBasic 방식은 Authorization에 ID,PW를 들고 다니는 방식이다. <-> Bearer 방식 (토큰을 들고다니는 방식)
		.headers()
		.frameOptions()
		.sameOrigin()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers("/favicon.ico").permitAll()
		.antMatchers("/api/login/signup").permitAll()
		.antMatchers("/page/admin/list","/api/login/list").hasRole("ADMIN")
		.antMatchers("/page/board/list","/api/board/**","/api/reply/**").hasAnyRole("ADMIN","USER")
		.antMatchers("/page/main/mainpage","/page/login/loginpage","/page/login/memberjoin","/page/login/finduserid","/api/login/**").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.accessDeniedHandler(jwtAccessDeniedHandler)
		.and()
		.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
	}
	@Bean
    public PersistentTokenRepository tokenRepository() {
		// JDBC 기반의 tokenRepository 구현체
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); // dataSource 주입
        return jdbcTokenRepository;
    }
}
