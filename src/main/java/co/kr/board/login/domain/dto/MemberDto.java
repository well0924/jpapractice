package co.kr.board.login.domain.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.login.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class MemberDto {
	
	@Getter
	@Setter
	@ToString
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberRequestDto{
		
		private Integer useridx;
		private String userid;
		private String password;
		private String membername;
		private String useremail;
		private Role role;
		
		@JsonFormat(pattern = "yyyy-mm-dd HH:mm")
		private LocalDateTime createdAt;
		
	}
	
	@Getter
	@Builder
	@ToString
	@AllArgsConstructor
	public static class MemeberResponseDto{
		
		private Integer useridx;
		private String userid;
		private String password;
		private String membername;
		private String useremail;

		@JsonFormat(pattern = "yyyy-mm-dd HH:mm")
		private LocalDateTime createdAt;
		
	}
}
