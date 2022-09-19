package co.kr.board.login.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import co.kr.board.board.domain.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name="member")
@AllArgsConstructor
public class Member extends BaseTime{
	
	@Id
	@Column(name="user_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer useridx;
	
	@Column(nullable = false)
	private String userid;
	
	@Column(nullable = false , length=100)
	private String password;
	
	@Column(nullable = false,length = 50)
	private String membername;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
}
