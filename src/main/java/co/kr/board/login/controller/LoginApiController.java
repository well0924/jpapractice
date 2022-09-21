package co.kr.board.login.controller;

import java.util.List;
import java.util.Map;

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

import co.kr.board.config.Response;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemeberResponseDto;
import co.kr.board.login.service.MemberService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/login/*")
public class LoginApiController {
	
	private final MemberService service;
	
	@GetMapping("/logincheck/{id}")
	public Response<Boolean>idcheck(@PathVariable(value="id")String username)throws Exception{
				
		Boolean checkreuslt = null;
		
		try {
			checkreuslt = service.checkmemberEmailDuplicate(username);
			
			if(checkreuslt == true) {//아이디 중복
				new Response<Boolean>(HttpStatus.BAD_REQUEST.value(),false);
			}else if(checkreuslt ==false) {//사용가능한 아이디
				new Response<Boolean>(HttpStatus.OK.value(),true);
			}
		} catch (Exception e) {
			e.printStackTrace();
				new Response<Boolean>(HttpStatus.BAD_GATEWAY.value(),false);
		}
		
		return new Response<Boolean>(HttpStatus.OK.value(),true);
	}
	
	@GetMapping("/list")
	public Response<List<MemberDto.MemeberResponseDto>>memberlist()throws Exception{
		
		List<MemberDto.MemeberResponseDto>list =null;
		
		try {
			list = service.findAll();
			
			if(list != null) {
				new Response<List<MemeberResponseDto>>(HttpStatus.OK.value(),list);
			}else if(list == null){
				new Response<List<MemeberResponseDto>>(HttpStatus.BAD_REQUEST.value(),list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			new Response<List<MemeberResponseDto>>(HttpStatus.INTERNAL_SERVER_ERROR.value(),list);
		}
		return new Response<List<MemeberResponseDto>>(HttpStatus.OK.value(),list);
	}
	
	@GetMapping("/detailmember/{idx}/member")
	public Response<MemberDto.MemeberResponseDto>memberdetail(@PathVariable("idx")Integer useridx)throws Exception{
		
		MemberDto.MemeberResponseDto dto = null;
		
		try {
			dto =service.getMember(useridx);
			
			if(dto != null) {
				new Response<MemberDto.MemeberResponseDto>(HttpStatus.OK.value(),dto);
			}else if(dto == null) {
				new Response<MemberDto.MemeberResponseDto>(HttpStatus.BAD_REQUEST.value(),dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			new Response<MemberDto.MemeberResponseDto>(HttpStatus.INTERNAL_SERVER_ERROR.value(),dto);
		}
		
		return new Response<MemberDto.MemeberResponseDto>(HttpStatus.OK.value(),dto);
	}
		
	@PostMapping("/memberjoin")
	public Response<?>memberjoin(@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
		
		int joinresult = 0;
		
		//유효성 검사
		if(bindingresult.hasErrors()) {
			Map<String, String> validatorResult = service.validateHandling(bindingresult);
			return new Response<>(HttpStatus.BAD_REQUEST.value(),validatorResult);
		}
		
		//회원가입
		try {
			joinresult = service.memberjoin(dto);
			
			if(joinresult >0) {		
				new Response<Integer>(HttpStatus.OK.value(),200);
			}else if(joinresult < 0) {
				new Response<Integer>(HttpStatus.BAD_REQUEST.value(),400);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return new Response<Integer>(HttpStatus.OK.value(),200);
	}
	
	@DeleteMapping("/memberdelete/{idx}/member")
	public Response<String>memberdelete(@PathVariable("idx")String username)throws Exception{
				
		try {		
			service.memberdelete(username);
			
			new Response<>(HttpStatus.OK.value(),"delete");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Response<>(HttpStatus.OK.value(),"delete");
	}
	
	@PutMapping("/memberupdate/{idx}/member")
	public Response<?>memberupdate(@PathVariable("idx")Integer useridx,@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
				
		int updateresult = 0;
		
		//유효성 검사
		if(bindingresult.hasErrors()) {
			Map<String, String> validatorResult = service.validateHandling(bindingresult);
			return new Response<>(HttpStatus.BAD_REQUEST.value(),validatorResult);
		}
		
		try {
			updateresult = service.memberupdate(useridx, dto);
			
			if(updateresult>0) {			
				new Response<Integer>(HttpStatus.OK.value(),200);
			}else if(updateresult < 0) {
				new Response<Integer>(HttpStatus.BAD_REQUEST.value(),400);
			}
		} catch (Exception e) {
			e.printStackTrace();
			new Response<Integer>(HttpStatus.INTERNAL_SERVER_ERROR.value(),500);
		}
		return new Response<Integer>(HttpStatus.OK.value(),200);
	}
}
