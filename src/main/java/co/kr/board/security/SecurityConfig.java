package co.kr.board.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean    
	public BCryptPasswordEncoder encoder() {   
		return new BCryptPasswordEncoder();    
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
		.and()
		.formLogin()
		.loginPage("/page/login/loginpage")
		.disable()
		.logout()
		.logoutSuccessUrl("/")                
		.invalidateHttpSession(true);
	}
	
	
}
