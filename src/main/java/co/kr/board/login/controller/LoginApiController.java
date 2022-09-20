package co.kr.board.login.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	//회원 가입
	
	
}
