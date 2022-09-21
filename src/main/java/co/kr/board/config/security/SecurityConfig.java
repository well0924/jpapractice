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

import co.kr.board.config.security.service.CustomUserDetailService;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CustomUserDetailService service;
	
	@Bean    
	public BCryptPasswordEncoder encoder() {   
		return new BCryptPasswordEncoder();    
	}
	
	@Override    
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {        
		 auth
		 .userDetailsService(service)
		 .passwordEncoder(encoder());    
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		 web                
		 .ignoring()
		 .antMatchers( "/css/**", "/js/**", "/img/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/**").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/page/login/loginpage")
		.usernameParameter("username")
		.passwordParameter("password")
		.loginProcessingUrl("/loginProc").permitAll()
		.defaultSuccessUrl("/page/board/list")
		.disable()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
		.invalidateHttpSession(true)
		.clearAuthentication(true);
	}	
	
}
