package co.kr.board.domain.Dto;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto implements Serializable {
	private String accessToken;
    private String refreshToken;
    private Long AccessExpirationTime;
    private Long RefreshExpirationTime;

}
