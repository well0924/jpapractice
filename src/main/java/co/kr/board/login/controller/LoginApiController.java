package co.kr.board.login.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import co.kr.board.login.domain.dto.TokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import co.kr.board.config.exception.dto.Response;
import co.kr.board.login.domain.dto.LoginDto;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.TokenResponse;
import co.kr.board.login.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/login/*")
public class LoginApiController {
	
	private final MemberService service;

	@GetMapping("/logincheck/{id}")
	public Response<Boolean>idcheck(@PathVariable(value="id") String username){
				
		Boolean checkresult = service.checkmemberIdDuplicate(username);
			
		if(checkresult.equals(true)) {//아이디 중복
			return new Response<>(HttpStatus.BAD_REQUEST.value(),true);
			
		}else{//사용가능한 아이디
			return	new Response<>(HttpStatus.OK.value(),false);
		}
	}
	
	@GetMapping("/emailcheck/{email}")
	public Response<Boolean>emailcheck(@PathVariable(value="email")@Email String useremail){
		Boolean checkresult = service.checkmemberEmailDuplicate(useremail);
		
		if(checkresult.equals(true)) {//아이디 중복
			return new Response<>(HttpStatus.BAD_REQUEST.value(),false);
			
		}else {//사용가능한 아이디
			return	new Response<>(HttpStatus.OK.value(),true);
		}
	}
	
	@GetMapping("/list")
	public Response<List<MemberDto.MemeberResponseDto>>memberlist(){
		
		List<MemberDto.MemeberResponseDto>list = service.findAll();
		
		return new Response<>(HttpStatus.OK.value(),list);
	}
	
	@GetMapping("/detailmember/{idx}/member")
	public Response<MemberDto.MemeberResponseDto>memberdetail(@PathVariable(value="idx")Integer useridx){
		
		MemberDto.MemeberResponseDto dto = service.getMember(useridx);
				
		return new Response<>(HttpStatus.OK.value(),dto);
	}
	
	@PostMapping("/memberjoin")
	public Response<Integer>memberjoin(@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
		
		int joinresult = service.memberjoin(dto);

		return new Response<>(HttpStatus.OK.value(),joinresult);
	}
	
	@DeleteMapping("/memberdelete/{idx}/member")
	public Response<String>memberdelete(@PathVariable(value="idx")String username){
		
		service.memberdelete(username);		
		
		return new Response<>(HttpStatus.OK.value(),"delete");
	}
	
	@PutMapping("/memberupdate/{idx}/member")
	public Response<Integer>memberupdate(
			@PathVariable(value="idx")Integer useridx,
			@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult){
				
		int updateresult = service.memberupdate(useridx, dto);
		
		return new Response<>(HttpStatus.OK.value(),updateresult);
	}
	
	@PostMapping("/userfind/{name}/{email}")
	public Response<?>userfindid(
			@PathVariable(value="name")String membername,
			@PathVariable(value="email")String useremail){
		
		String userid = service.findByMembernameAndUseremail(membername, useremail);
		
		return new Response<>(HttpStatus.OK.value(),userid);
	}

	@PutMapping("/passwordchange")
	public Response<Integer>passwordChange(Integer useridx,MemberDto.MemberRequestDto dto){
		int result = service.passwordchange(useridx,dto);
		return new Response<>(HttpStatus.OK.value(),result);
	}

	//jwt 로그인 인증
	@PostMapping("/signup")
    public Response <TokenResponse> memberjwtlogin(@RequestBody LoginDto loginDto){
        TokenResponse tokenResponse = service.signin(loginDto);
		log.info("accessToken:"+tokenResponse.getAccessToken());
		log.info("refreshToken:"+tokenResponse.getRefreshToken());
        return new Response<>(HttpStatus.OK.value(),tokenResponse);
    }
    
	//토큰 재발행
    @PostMapping("/reissue")
    public Response<TokenResponse>jwtreissue(@RequestBody TokenRequest tokenDto){
        TokenResponse tokenResponse = service.reissue(tokenDto);
        return new Response<>(HttpStatus.OK.value(),tokenResponse);
    }
    
    //로그아웃

}
