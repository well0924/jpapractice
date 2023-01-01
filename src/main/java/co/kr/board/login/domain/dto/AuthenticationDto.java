package co.kr.board.login.domain.dto;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDto {
	
	private Integer id;
	
	private String username;
	
	private String password;
	
	private String membername;
	
	private String useremail;
	
}
