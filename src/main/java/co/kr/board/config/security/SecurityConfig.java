package co.kr.board.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import co.kr.board.config.handler.LoginFailuererHandler;
import co.kr.board.config.handler.LoginSuccessHandler;
import co.kr.board.config.security.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CustomUserDetailService service;
	
	public SecurityConfig(CustomUserDetailService service) {
		this.service = service;
	}
	
	//비밀번호 암호화
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(encoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring()
		.antMatchers("/webjars/**", "/dist/**", "/plugins/**", "/css/**", "/user/**","/swagger-resources/**")
		.antMatchers("/images/**", "/JS/**", "/font", "/webfonts/**", "/main/**", "/swagger-ui/**", "/v2/**","/page/board/list");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/api/reply/write/*","/api/reply/delete/*").hasAnyRole("ADMIN","USER")
		.antMatchers("/api/board/write","/api/board/delete/*","/api/board/modify/*").hasAnyRole("ADMIN","USER")
		.antMatchers("/api/reply/list/*").permitAll()
		.antMatchers("/page/**").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/page/login/loginpage")
		.loginProcessingUrl("/loginProc").permitAll()
		.successHandler(new LoginSuccessHandler())
		.failureHandler(new LoginFailuererHandler())
		.and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
		.invalidateHttpSession(true)
		.clearAuthentication(true)
		.and()
		.sessionManagement()
        .maximumSessions(1) //세션 최대 허용 수 
        .maxSessionsPreventsLogin(true);//중복아이디 세션 차단 
	}
	
	
}
