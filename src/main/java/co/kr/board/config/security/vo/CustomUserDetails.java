package co.kr.board.config.security.vo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import co.kr.board.login.domain.Member;

public class CustomUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;

	private Member member;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 
		Collection<GrantedAuthority> collectors = new ArrayList<>();         
		 
		collectors.add(() -> "ROLE_"+member.getRole());         
		 
		return collectors;
	}
	
	public CustomUserDetails(Member member) {
		this.member = member;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return member.getPassword();
	}

	@Override
	public String getUsername() {

		return member.getUserid();
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}
}
