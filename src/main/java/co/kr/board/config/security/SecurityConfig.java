package co.kr.board.config.security;

import co.kr.board.config.security.jwt.*;
import lombok.AllArgsConstructor;
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
import co.kr.board.config.security.auth.CustomUserDetailService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final CustomUserDetailService service;
	private final JwtTokenProvider jwtTokenProvider;

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
				.antMatchers("/**")
		.antMatchers("/favicon.ico")
		.antMatchers("/webjars/**", "/dist/**", "/plugins/**", "/css/**", "/user/**","/swagger-resources/**")
		.antMatchers("/images/**", "/js/**", "/font/**", "/webfonts/**", "/main/**", "/swagger-ui/**", "/v2/**");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()//rest Api 를 사용하므로 csrf는 차단.
		.httpBasic().disable()// httpBasic 방식은 Authorization에 ID,PW를 들고 다니는 방식이다. <-> Bearer 방식 (토큰을 들고다니는 방식)
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)//jwt를 사용하기 위해서 session을 사용하지 않는다.
		.and()
		.authorizeRequests()//요청 url에 따라서 권한설정하기.
		.antMatchers("/api/login/signup").permitAll()
		.antMatchers("/page/admin/list","/api/login/list").hasRole("ADMIN")
		.antMatchers("/page/board/list","/api/board/**","/api/reply/**").hasAnyRole("ADMIN","USER")
		.antMatchers("/page/main/mainpage","/page/login/loginpage","/page/login/memberjoin","/page/login/finduserid","/api/login/**").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling()//jwt로 로그인을 했을시 실패를 한 경우 handling
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.accessDeniedHandler(jwtAccessDeniedHandler)
		.and()
		.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);//로그인에 사용되는 filter
	}
}
