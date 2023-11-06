package co.kr.board.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import co.kr.board.domain.Const.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import org.hibernate.annotations.Proxy;

@Getter
@Setter
@Entity
@ToString
@Builder
@Table(name="member",indexes = {
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

	@Column(name="userid",nullable = false)
	private String username;

	@Column(nullable = false,length = 500)
	private String password;

	@Column(length = 200)
	private String membername;

	@Column(nullable = false,length = 200)
	private String useremail;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;

	//방문자 (양방향)
	@ToString.Exclude
	@OneToMany(mappedBy = "member",cascade = {CascadeType.ALL},orphanRemoval = true,fetch = FetchType.LAZY)
	private List<Visitor>visitors = new ArrayList<>();
}
