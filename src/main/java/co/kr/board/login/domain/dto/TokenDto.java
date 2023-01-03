package co.kr.board.login.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
	private String accessToken;
    private String refreshToken;
    private Long ExpirationTime;
}
