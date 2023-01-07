package co.kr.board.config.security;

import javax.sql.DataSource;

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

import co.kr.board.config.security.jwt.JwtAccessDeniedHandler;
import co.kr.board.config.security.jwt.JwtAuthenticationEntryPoint;
import co.kr.board.config.security.jwt.JwtAuthenticationFilter;
import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.config.security.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CustomUserDetailService service;
	
	private final DataSource dataSource;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	
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
	public void configure(WebSecurity web) throws Exception {
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
		.httpBasic().disable()
		.authorizeRequests()
		.antMatchers("/api/login/signup").permitAll()
		.antMatchers("/page/admin/list","/api/login/list").hasRole("ADMIN")
		.antMatchers("/page/board/list","/api/board/**","/api/reply/**").hasAnyRole("ADMIN","USER")
		.antMatchers("/page/main/mainpage","/page/login/loginpage","/page/login/memberjoin","/page/login/finduserid","/api/login/**").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
		.invalidateHttpSession(true)
		.clearAuthentication(true)
		.and()
		.exceptionHandling()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http
		.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
		.exceptionHandling()
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.accessDeniedHandler(jwtAccessDeniedHandler);
	}
	
	@Bean
    public PersistentTokenRepository tokenRepository() {
		// JDBC 기반의 tokenRepository 구현체
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); // dataSource 주입
        return jdbcTokenRepository;
    }
}
