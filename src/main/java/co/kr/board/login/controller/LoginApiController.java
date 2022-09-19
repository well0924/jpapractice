package co.kr.board.login.controller;

import org.springframework.web.bind.annotation.RestController;

import co.kr.board.login.service.MemberService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class LoginApiController {
	
	private final MemberService service;
	
	
}
