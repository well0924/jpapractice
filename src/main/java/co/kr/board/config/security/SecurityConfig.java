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

import co.kr.board.config.security.handler.LoginFailuererHandler;
import co.kr.board.config.security.handler.LoginSuccessHandler;
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
		.antMatchers("/images/**", "/JS/**", "/font", "/webfonts/**", "/main/**", "/swagger-ui/**", "/v2/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.httpBasic().disable()
		.authorizeRequests()
		.antMatchers("/page/admin/list").hasRole("ADMIN")
		.antMatchers("/page/board/list","/api/board/**","/api/reply/**").hasAnyRole("ADMIN","USER")
		.antMatchers("/page/main/mainpage","/page/login/loginpage","/page/login/memberjoin","/api/login/**").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/page/login/loginpage")//시큐리티에 적용되는 로그인페이지가 아닌 커스텀페이지로 이동
		.loginProcessingUrl("/loginProc").permitAll()//로그인은 전부 허용
		.failureHandler(new LoginFailuererHandler())//로그인에 실패를 하면 fail handler
		.successHandler(new LoginSuccessHandler())//로그인에 성공을 하면 success handler
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
