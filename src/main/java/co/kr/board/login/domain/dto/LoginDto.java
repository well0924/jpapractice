package co.kr.board.login.domain.dto;

import javax.validation.constraints.Size;

import co.kr.board.login.domain.Member;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
	
	@NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
    
    
    public Member toEntity() {
    	Member build = Member
    			.builder()
    			.username(username)
    			.password(password)
    			.build();
    	
    	return build;
    }
}
