package co.kr.board.login.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.board.domain.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Builder
@ToString
@Table(name="member")
@AllArgsConstructor
@RequiredArgsConstructor
public class Member extends BaseTime{
	
	@Id
	@Column(name="user_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer useridx;
	
	@Column(name="userid",nullable = false)
	private String username;

	@Column(nullable = false , length=100)
	private String password;
	
	@Column(nullable = false,length = 50)
	private String membername;
	
	@Column(nullable = false,length = 100)
	private String useremail;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	
	//회원정보 수정.
	public void memberupdate(String username, String password,String membername,String useremail) {
		this.username = username;
		this.password = password;
		this.membername = membername;
		this.useremail = useremail;
	}
	
	@Builder
	public Member(String username,String password,Role role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
}
