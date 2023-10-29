package co.kr.board.domain.Dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import co.kr.board.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.board.domain.Const.Role;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

public class MemberDto {
	
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberRequestDto{
		
		private Integer useridx;
		
		@NotBlank(message = "아이디는 필수 입력값입니다.")
		@Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
		private String username;
		
		@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,50}$",
				message = "비밀번호는 8~50자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
		private String password;
		
		@NotBlank(message="회원이름을 입력해주세요.")
		@Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
		private String membername;
		
		@NotBlank(message="이메일을 입력해주세요.")
		@Email
		private String useremail;
		
		private Role role;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime createdAt;
		
	}
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberPasswordChangeDto implements Serializable{
		private Integer useridx;
		private String password;
	}
	@Getter
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MemeberResponseDto implements Serializable {
		
		private Integer useridx;
		private String username;
		private String password;
		private String membername;
		private String useremail;
		private Role role;
		@JsonFormat(pattern = "yyyy-mm-dd HH:mm")
		private LocalDateTime createdAt;

		@Builder
		@QueryProjection
		public MemeberResponseDto(Member member){
			this.useridx = member.getId();
			this.membername = member.getMembername();
			this.username = member.getUsername();
			this.role = member.getRole();
			this.password = member.getPassword();
			this.useremail = member.getUseremail();
			this.createdAt = member.getCreatedAt();
		}
	}
}
