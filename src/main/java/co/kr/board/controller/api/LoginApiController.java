package co.kr.board.controller.api;

import javax.validation.Valid;

import co.kr.board.config.Email.EmailService;
import co.kr.board.config.security.auth.AuthService;
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
@RequestMapping("/api/login/*")
public class LoginApiController {
	private final AuthService authService;
	private final EmailService emailService;

	private final long COOKIE_EXPIRATION = 7776000; // 90일

	//로그인 ->토큰 발행
	@PostMapping("/signup")
    public ResponseEntity <TokenDto> memberJwtLogin(@Valid @RequestBody LoginDto loginDto){
        TokenDto tokenResponse = authService.login(loginDto);

		HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokenResponse.getRefreshToken())
				.maxAge(COOKIE_EXPIRATION)
				.httpOnly(true)
				.secure(true)
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, httpCookie.toString())
				// AT 저장
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
				.body(tokenResponse);
    }

	//토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?>jwtReissue(@CookieValue(name = "refresh-token") String requestRefreshToken,
								 @RequestHeader("Authorization") String requestAccessToken){
        TokenDto tokenResponse = authService.reissue(requestAccessToken,requestRefreshToken);
		log.info(tokenResponse);
		if (tokenResponse != null) { // 토큰 재발급 성공
			// RT 저장
			ResponseCookie responseCookie = ResponseCookie.from("refresh-token", tokenResponse.getRefreshToken())
					.maxAge(COOKIE_EXPIRATION)
					.httpOnly(true)
					.secure(true)
					.build();
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
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
					.body(tokenResponse);
		}
    }

	//로그아웃
	@PostMapping("/logout")
	public ResponseEntity<?>logout(@RequestHeader("Authorization") String requestAccessToken){

		authService.logout(requestAccessToken);

		ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
				.maxAge(0)
				.path("/")
				.build();

		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body("log-out");
	}

	//회원가입 인증 이메일
	@PostMapping("/sendEmail/{email}")
	public Response<String>sendEmail(@PathVariable(value = "email") String userEmail)throws Exception{
		String epw = emailService.sendSimpleMessage(userEmail);
		return new Response<>(HttpStatus.OK.value(),"인증번호를 보냈습니다.:"+epw);
	}

	//회원 비밀번호 재수정 인증 이메일
	@PostMapping("/temporary-password/{email}")
	public Response<String>sendPwd(@PathVariable(value = "email") String userEmail) throws Exception{
		String tpw = emailService.sendTemporaryPasswordMessage(userEmail);
		return new Response<>(HttpStatus.OK.value(),"인증번호를 보냈습니다.:"+tpw);
	}
}
