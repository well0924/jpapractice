package co.kr.board.domain.Dto;

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
