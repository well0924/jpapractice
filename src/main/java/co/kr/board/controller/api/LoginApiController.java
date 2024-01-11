package co.kr.board.controller.api;

import javax.validation.Valid;

import co.kr.board.config.Email.EmailService;
import co.kr.board.config.Security.auth.AuthService;
import co.kr.board.domain.Dto.LoginDto;
import co.kr.board.domain.Dto.TokenDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import co.kr.board.config.Exception.dto.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/login")
public class LoginApiController {
	private final AuthService authService;
	private final EmailService emailService;
	private final long COOKIE_EXPIRATION = 1209600;

	//로그인 ->토큰 발행
	@PostMapping("/signup")
    public ResponseEntity <TokenDto> memberJwtLogin(@Valid @RequestBody LoginDto loginDto){

		TokenDto tokenResponse = authService.login(loginDto);

		// RT 쿠키에 저장하기.
		HttpCookie RtHttpCookie = ResponseCookie.from("refresh-token", tokenResponse.getRefreshToken())
				.maxAge(COOKIE_EXPIRATION)
				.path("/")
				.secure(true)
				.build();

		log.info("쿠키저장??::"+RtHttpCookie);

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, RtHttpCookie.toString())
				// AT 저장
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
				.body(tokenResponse);
    }

	//토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?>jwtReissue(@CookieValue(name = "refresh-token",required = false) String requestRefreshToken,
								 @RequestHeader("Authorization") String requestAccessToken){
        TokenDto tokenResponse = authService.reissue(requestAccessToken,requestRefreshToken);
		log.info(requestRefreshToken);
		if (tokenResponse != null) { // 토큰 재발급 성공
			// RT 저장
			ResponseCookie responseCookie = ResponseCookie.from("refresh-token", tokenResponse.getRefreshToken())
					.maxAge(COOKIE_EXPIRATION)
					.secure(true)
					.path("/")
					.build();
			log.info("쿠키값 저장::"+responseCookie);
			return ResponseEntity
					.status(HttpStatus.OK)
					.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
					// AT 저장
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
					.body(tokenResponse);

		} else { // Refresh Token 탈취 가능성
			// Cookie 삭제 후 재로그인 유도
			ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
					.maxAge(0)
					.path("/")
					.build();
			log.info("401인경우:"+responseCookie);
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
					.build();
		}
    }

	//로그아웃
	@PostMapping("/logout")
	public ResponseEntity<?>logout(@RequestHeader("Authorization") String requestAccessToken){

		authService.logout(requestAccessToken);

		ResponseCookie responseCookie = ResponseCookie.from("refresh-token", null)
				.maxAge(0)
				.path("/")
				.build();
		log.info(responseCookie);
		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body("log-out");
	}

	//회원 비밀번호 재수정 인증 이메일
	@PostMapping("/temporary-password/{email}")
	public Response<String>sendPwd(@PathVariable(value = "email") String userEmail) throws Exception{
		String tpw = emailService.sendTemporaryPasswordMessage(userEmail);
		return new Response<>(HttpStatus.OK.value(),"인증번호를 보냈습니다.");
	}
}
