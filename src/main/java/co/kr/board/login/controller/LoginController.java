package co.kr.board.login.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.service.MemberService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/page/login/*")
public class LoginController {
	
	private final MemberService service;
	
	@GetMapping("/loginpage")
	public ModelAndView loginpage(
			@RequestParam(value="error",required = false) String error, 
			@RequestParam(value="exception",required = false) String exception)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("error", error);
		mv.addObject("exception", exception);
		
		mv.setViewName("login/loginpage");

		return mv;
	}
	
	@GetMapping("/memberjoin")
	public ModelAndView mebmerjoin()throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login/memberjoin");

		return mv;
	}
	
	@GetMapping("/memberdelete")
	public ModelAndView memberdelete()throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login/memberdelete");

		return mv;
	}
	
	@GetMapping("/memberupdate/{id}")
	public ModelAndView memberupdate(@PathVariable("id") Integer useridx, MemberDto.MemeberResponseDto dto)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		dto = service.getMember(useridx);
		
		mv.addObject("detail", dto);
		mv.setViewName("login/membermodify");
		
		return mv;
	}
	
	@GetMapping("/adminlist")
	public ModelAndView adminlist(@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		Page<Member>list= null;
			
		list = service.findAll(pageable);
				
		mv.addObject("memberlist", list);
		
		mv.setViewName("admin/adminlist");
		
		return mv;
	}
	
	@GetMapping("/detail/{id}")
	public ModelAndView memberdetail(@PathVariable(value="id",required = true)Integer useridx, MemberDto.MemeberResponseDto dto)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		dto = service.getMember(useridx);
		
		mv.addObject("detail", dto);
		mv.setViewName("/login/membermodify");
		
		return mv;
	}
}
