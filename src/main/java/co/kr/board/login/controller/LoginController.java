package co.kr.board.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import co.kr.board.login.service.MemberService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/page/login/")
public class LoginController {
	
	private final MemberService service;
	
	@GetMapping("/loginpage")
	public ModelAndView loginpage()throws Exception{
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login/loginpage");
		return mv;
	}
}
