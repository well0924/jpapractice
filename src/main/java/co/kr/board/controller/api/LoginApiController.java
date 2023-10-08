package co.kr.board.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import co.kr.board.config.Email.EmailService;
import co.kr.board.domain.Dto.LoginDto;
import co.kr.board.domain.Dto.TokenRequest;
import co.kr.board.domain.Dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.kr.board.config.Exception.dto.Response;
import co.kr.board.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/login/*")
public class LoginApiController {

	private final MemberService service;

	private final EmailService emailService;

	//로그인
	@PostMapping("/signup")
    public Response <TokenResponse> memberJwtLogin(@Valid @RequestBody LoginDto loginDto){
        TokenResponse tokenResponse = service.signin(loginDto);
		return new Response<>(HttpStatus.OK.value(),tokenResponse);
    }

	//토큰 재발급
    @PostMapping("/reissue")
    public Response<TokenResponse>jwtReissue(@Valid @RequestBody TokenRequest tokenDto){
        TokenResponse tokenResponse = service.reissue(tokenDto);
		return new Response<>(HttpStatus.OK.value(),tokenResponse);
    }

	//로그아웃
	@PostMapping("/logout")
	public ResponseEntity<?>logout(HttpServletRequest servletRequest){
		service.logout();
		return ResponseEntity.ok().build();
	}
	
	//회원가입 인증 이메일
	@PostMapping("/sendEmail/{email}")
	public Response<String>sendEmail(@PathVariable(value = "email") String userEmail)throws Exception{
		String epw =emailService.sendSimpleMessage(userEmail);
		return new Response<>(HttpStatus.OK.value(),"인증번호를 보냈습니다.");
	}

	//회원 비밀번호 재수정 인증 이메일
	@PostMapping("/temporary-password/{email}")
	public Response<String>sendPwd(@PathVariable(value = "email") String userEmail) throws Exception{
		String tpw = emailService.sendTemporaryPasswordMessage(userEmail);
		return new Response<>(HttpStatus.OK.value(),"인증번호를 보냈습니다.");
	}
}
