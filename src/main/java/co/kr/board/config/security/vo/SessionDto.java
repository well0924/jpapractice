package co.kr.board.config.security.vo;

import java.io.Serializable;

import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import lombok.Getter;

@Getter
public class SessionDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer useridx;
	private String userid;
	private String password;
	private String membername;
	private String useremail;
	private Role role;
	
	public SessionDto(Member member) {
		this.useridx = member.getUseridx();
		this.userid = member.getUserid();
		this.password =member.getPassword();
		this.membername = member.getMembername();
		this.useremail = member.getUseremail();
		this.role = member.getRole();	
	}

}
