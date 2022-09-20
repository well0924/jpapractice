package co.kr.board.login.controller;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.loader.plan.build.internal.returns.AbstractEntityReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<Boolean>idcheck(@PathVariable(value="id")String userid)throws Exception{
		
		ResponseEntity<Boolean>entity = null;
		
		Boolean checkreuslt = null;
		
		try {
			checkreuslt = service.checkmemberEmailDuplicate(userid);
			
			if(checkreuslt == true) {
				entity = new ResponseEntity<Boolean>( true ,HttpStatus.OK);
			}else if(checkreuslt ==false) {
				entity = new ResponseEntity<Boolean>( false ,HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<Boolean>(false,HttpStatus.BAD_GATEWAY);
		}
		
		return entity;
	}
	//회원 목록
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
	//회원 가입
	@PostMapping("/memberjoin")
	public ResponseEntity<Integer>memberjoin(@Valid @RequestBody MemberDto.MemberRequestDto dto)throws Exception{
		
		ResponseEntity<Integer>entity = null;
		
		int joinresult = 0;
		
		try {
			joinresult = service.memberjoin(dto);
			
			if(joinresult>0) {
				entity = new ResponseEntity<Integer>(200,HttpStatus.OK);
			}else if(joinresult < 0) {
				entity = new ResponseEntity<Integer>(400,HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<Integer>(500,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}
	
}
