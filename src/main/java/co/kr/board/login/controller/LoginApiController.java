package co.kr.board.login.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.config.exception.Response;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemeberResponseDto;
import co.kr.board.login.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/login/*")
public class LoginApiController {
	
	private final MemberService service;
	
//	private final ValidationCheck check;
	
	
	@GetMapping("/logincheck/{id}")
	public Response<?>idcheck(@PathVariable(value="id",required = true)String username)throws Exception{
				
		Boolean checkreuslt = null;
				
		checkreuslt = service.checkmemberEmailDuplicate(username);
			
		if(checkreuslt == true) {//아이디 중복
			log.info("결과값:"+checkreuslt);
			return new Response<Boolean>(HttpStatus.BAD_REQUEST.value(),false);
			
		}else if(checkreuslt ==false) {//사용가능한 아이디
			
			return	new Response<Boolean>(HttpStatus.OK.value(),true);
			
		}
		
		return new Response<Boolean>(HttpStatus.OK.value(),true);
	}
	
	@GetMapping("/list")
	public Response<List<MemberDto.MemeberResponseDto>>memberlist()throws Exception{
		
		List<MemberDto.MemeberResponseDto>list =null;
		
		
		list = service.findAll();
			
		if(list == null){
				
			return new Response<List<MemeberResponseDto>>(HttpStatus.BAD_REQUEST.value(),list);
			
		}
		
		
		return new Response<List<MemeberResponseDto>>(HttpStatus.OK.value(),list);
	}
	
	@GetMapping("/detailmember/{idx}/member")
	public Response<MemberDto.MemeberResponseDto>memberdetail(@PathVariable(value="idx",required = true)Integer useridx)throws Exception{
		
		MemberDto.MemeberResponseDto dto = null;
		
			
		dto =service.getMember(useridx);
			
		if(dto != null) {
				
			new Response<MemberDto.MemeberResponseDto>(HttpStatus.OK.value(),dto);
			
		}else if(dto == null) {
				
			new Response<MemberDto.MemeberResponseDto>(HttpStatus.BAD_REQUEST.value(),dto);
			
		}
		
		return new Response<MemberDto.MemeberResponseDto>(HttpStatus.OK.value(),dto);
	}
		
	@PostMapping("/memberjoin")
	public Response<?>memberjoin(@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
		
		int joinresult = 0;
		
		//유효성 검사
//		if(bindingresult.hasErrors()) {
//			
//			Map<String, String> validatorResult = check.validateHandling(bindingresult);
//			log.info("result:"+validatorResult);
//			return new Response<>(HttpStatus.BAD_REQUEST.value(),validatorResult);
//		}
		
			
			joinresult = service.memberjoin(dto);
			
			if(joinresult > 0) {	
				
				return	new Response<Integer>(HttpStatus.OK.value(),joinresult);
			
			}else if(joinresult < 0) {
				
				return	new Response<Integer>(HttpStatus.BAD_GATEWAY.value(),joinresult);
			
			}
			
		return new Response<Integer>(HttpStatus.OK.value(),joinresult);
	}
	
	@DeleteMapping("/memberdelete/{idx}/member")
	public Response<String>memberdelete(@PathVariable(value="idx")String username)throws Exception{
		
		service.memberdelete(username);		
		
		return new Response<>(HttpStatus.OK.value(),"delete");
	}
	
	@PutMapping("/memberupdate/{idx}/member")
	public Response<?>memberupdate(
			@PathVariable(value="idx")Integer useridx,
			@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
				
		int updateresult = 0;
		
		//유효성 검사
//		if(bindingresult.hasErrors()) {
//			
//			Map<String, String> validatorResult = check.validateHandling(bindingresult);
//			log.info("result:"+validatorResult);
//			return new Response<>(HttpStatus.BAD_REQUEST.value(),validatorResult);
//		}
		
			updateresult = service.memberupdate(useridx, dto);
			
			if(updateresult>0) {			
			return	new Response<Integer>(HttpStatus.OK.value(),200);
			
			}else if(updateresult < 0) {
			return	new Response<Integer>(HttpStatus.BAD_REQUEST.value(), 400);
			
			}
		
		return new Response<Integer>(HttpStatus.OK.value(),200);
	}
}
