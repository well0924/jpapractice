package co.kr.board.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/page/login")
public class LoginController {

	//로그인 페이지
	@GetMapping("/login-page")
	public ModelAndView loginPage(
			@RequestParam(value="error",required = false) String error, 
			@RequestParam(value="exception",required = false) String exception){
		
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("error", error);
		mv.addObject("exception", exception);
		
		mv.setViewName("login/loginpage");

		return mv;
	}

	//회원 아이디 찾기
	@GetMapping("/find-userid")
	public ModelAndView findUserIdPage(){
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login/userfindid");
		
		return mv;
	}

	//회원 비밀번호 재설정
	@GetMapping("/find-user-pwd")
	public ModelAndView findUserPw(){
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login/userfindpw");
		return mv;
	}
}
