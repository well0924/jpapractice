package co.kr.board.login.domain.dto;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    
	private String accessToken;
    private String refreshToken;
    private Long ExpirationTime;
}
