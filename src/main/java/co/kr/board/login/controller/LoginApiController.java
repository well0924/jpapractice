package co.kr.board.login.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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

import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemeberResponseDto;
import co.kr.board.login.domain.dto.Response;
import co.kr.board.login.service.MemberService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/login/*")
public class LoginApiController {
	
	private final MemberService service;
	
	@GetMapping("/logincheck/{id}")
	public ResponseEntity<Boolean>idcheck(@PathVariable(value="id")String userid)throws Exception{
		
		ResponseEntity<Boolean>entity = null;
		
		Boolean checkreuslt = null;
		
		try {
			checkreuslt = service.checkmemberEmailDuplicate(userid);
			
			if(checkreuslt == true) {//아이디 중복
				entity = new ResponseEntity<Boolean>( false ,HttpStatus.BAD_REQUEST);
			}else if(checkreuslt ==false) {//사용가능한 아이디
				entity = new ResponseEntity<Boolean>( true ,HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<Boolean>(false,HttpStatus.BAD_GATEWAY);
		}
		
		return entity;
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<MemberDto.MemeberResponseDto>>memberlist()throws Exception{
		ResponseEntity<List<MemberDto.MemeberResponseDto>>entity = null;
		
		List<MemberDto.MemeberResponseDto>list =null;
		
		try {
			list = service.findAll();
			
			if(list != null) {
				entity = new ResponseEntity<List<MemeberResponseDto>>(list,HttpStatus.OK);
			}else if(list == null){
				entity = new ResponseEntity<List<MemeberResponseDto>>(list,HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<List<MemeberResponseDto>>(list,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return new Response<Integer>(HttpStatus.OK.value(),1);
	}
	
	//회원탈퇴
	@DeleteMapping("/memberdelete/{idx}/member")
	public ResponseEntity<Integer>memberdelete()throws Exception{
		
		ResponseEntity<Integer>entity = null;
		
		int deleteresult = 0;
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return entity;
	}
	
	//회원수정
	@PutMapping("/memberupdate/{idx}/member")
	public ResponseEntity<Integer>memberupdate(@Valid @RequestBody MemberDto.MemberRequestDto dto)throws Exception{
		
		ResponseEntity<Integer>entity = null;
		
		int updateresult = 0;
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
}
