package co.kr.board.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Getter
@Entity
@Builder
@Table(name="member",indexes = {
		@Index(columnList = "id"),
		@Index(columnList = "userid"),
		@Index(columnList = "membername"),
		@Index(columnList = "useremail")
})
@Proxy(lazy = false)
@AllArgsConstructor
@RequiredArgsConstructor
public class Member extends BaseTime implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Setter
	@Column(name="userid",nullable = false)
	private String username;

	@Setter
	@Column(nullable = false,length = 500)
	private String password;

	@Setter
	@Column(length = 200)
	private String membername;

	@Setter
	@Column(nullable = false,length = 200)
	private String useremail;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
}
