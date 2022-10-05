package co.kr.board.login.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public ModelAndView loginpage()throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		
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
		
		try {
		
			dto = service.getMember(useridx);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.addObject("detail", dto);
		mv.setViewName("login/membermodify");
		
		return mv;
	}
	
	@GetMapping("/adminlist")
	public ModelAndView adminlist(@PageableDefault(sort="useridx",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		Page<Member>list= null;
		
		try {
			
			list = service.findAll(pageable);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.addObject("memberlist", list);
		mv.setViewName("admin/adminlist");
		
		return mv;
	}
}
