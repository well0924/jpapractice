package co.kr.board.login.domain.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
	private String accessToken;
    private String refreshToken;
    private Long ExpirationTime;

}
