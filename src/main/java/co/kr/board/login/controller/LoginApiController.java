package co.kr.board.login.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.config.exception.dto.Response;
import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.dto.LoginDto;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemeberResponseDto;
import co.kr.board.login.domain.dto.AuthenticationDto;
import co.kr.board.login.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/login/*")
public class LoginApiController {
	
	private final MemberService service;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@GetMapping("/logincheck/{id}")
	public Response<Boolean>idcheck(@PathVariable(value="id",required = true) String username)throws Exception{
				
		Boolean checkresult = null;
				
		checkresult = service.checkmemberIdDuplicate(username);
			
		if(checkresult == true) {//아이디 중복
			log.info("결과값:"+checkresult);
			return new Response<Boolean>(HttpStatus.BAD_REQUEST.value(),false);
			
		}else if(checkresult ==false) {//사용가능한 아이디
			log.info("사용결과?:"+checkresult);
			return	new Response<Boolean>(HttpStatus.OK.value(),true);
			
		}
		
		return new Response<Boolean>(HttpStatus.OK.value(),true);
	}
	
	@GetMapping("/emailcheck/{email}")
	public Response<Boolean>emailcheck(@PathVariable(value="email",required = true)@Email String useremail)throws Exception{
		Boolean checkresult = null;
		
		checkresult = service.checkmemberEmailDuplicate(useremail);
		
		if(checkresult == true) {//아이디 중복
			log.info("결과값:"+checkresult);
			return new Response<Boolean>(HttpStatus.BAD_REQUEST.value(),false);
			
		}else if(checkresult ==false) {//사용가능한 아이디
			log.info("결과값:"+checkresult);
			return	new Response<Boolean>(HttpStatus.OK.value(),true);
			
		}
		return new Response<Boolean>(HttpStatus.OK.value(),true);
	}
	
	@GetMapping("/list")
	public Response<List<MemberDto.MemeberResponseDto>>memberlist()throws Exception{
		
		List<MemberDto.MemeberResponseDto>list =null;
		
		list = service.findAll();		
		
		return new Response<List<MemeberResponseDto>>(HttpStatus.OK.value(),list);
	}
	
	@GetMapping("/detailmember/{idx}/member")
	public Response<MemberDto.MemeberResponseDto>memberdetail(@PathVariable(value="idx",required = true)Integer useridx)throws Exception{
		
		MemberDto.MemeberResponseDto dto = null;
		
		dto =service.getMember(useridx);
				
		return new Response<MemberDto.MemeberResponseDto>(HttpStatus.OK.value(),dto);
	}
		
	@PostMapping("/memberjoin")
	public Response<Integer>memberjoin(@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
		
		int joinresult = 0;
		
		joinresult = service.memberjoin(dto);
					
		return new Response<Integer>(HttpStatus.OK.value(),joinresult);
	}
	
	@DeleteMapping("/memberdelete/{idx}/member")
	public Response<String>memberdelete(@PathVariable(value="idx")String username)throws Exception{
		
		service.memberdelete(username);		
		
		return new Response<>(HttpStatus.OK.value(),"delete");
	}
	
	@PutMapping("/memberupdate/{idx}/member")
	public Response<Integer>memberupdate(
			@PathVariable(value="idx")Integer useridx,
			@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
				
		int updateresult = 0;
		
		updateresult = service.memberupdate(useridx, dto);
		
		return new Response<Integer>(HttpStatus.OK.value(),updateresult);
	}
	
	@PostMapping("/userfind/{name}/{email}")
	public Response<?>userfindid(
			@PathVariable(value="name",required = true)String membername,
			@PathVariable(value="email",required = true)String useremail)throws Exception{
		
		String userid = service.findByMembernameAndUseremail(membername, useremail);
		
		return new Response<>(HttpStatus.OK.value(),userid);
	}
	
	
//	@PostMapping("/signup")
//	public ResponseEntity<String> userlogin(@RequestBody LoginDto logindto)throws Exception{
//		
//		Member member = service.findByUsername(logindto.getUsername());
//		
//		return new ResponseEntity<>(jwtTokenProvider.createToken(logindto.getUsername(),member.getRole()),HttpStatus.OK);
////		return ResponseEntity.ok()
////				.header("X-AUTH-TOKEN",jwtTokenProvider
////						.createToken(logindto.getUsername(),member.getRole())).body(AuthenticationDto);
//	}
	
	@PostMapping("/signup")
	public ResponseEntity<AuthenticationDto> userlogin(@RequestBody LoginDto logindto)throws Exception{
		
		Member member = service.findByUsername(logindto.getUsername());
		AuthenticationDto authen = service.loginService(logindto);
		
		return ResponseEntity.ok()
				.header("X-AUTH-TOKEN",jwtTokenProvider
						.createToken(logindto.getUsername(),member.getRole())).body(authen);
	}

}
