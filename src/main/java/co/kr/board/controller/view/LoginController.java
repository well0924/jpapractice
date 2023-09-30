package co.kr.board.controller.view;

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

import co.kr.board.domain.Member;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.service.MemberService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/page/login/*")
public class LoginController {
	
	private final MemberService service;
	
	@GetMapping("/loginpage")
	public ModelAndView loginPage(
			@RequestParam(value="error",required = false) String error, 
			@RequestParam(value="exception",required = false) String exception){
		
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("error", error);
		mv.addObject("exception", exception);
		
		mv.setViewName("login/loginpage");

		return mv;
	}
	
	@GetMapping("/memberjoin")
	public ModelAndView mebmerJoin(){
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login/memberjoin");

		return mv;
	}
	
	@GetMapping("/memberdelete")
	public ModelAndView memberDelete(){
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login/memberdelete");

		return mv;
	}
	
	@GetMapping("/memberupdate/{id}")
	public ModelAndView memberUpdate(@PathVariable("id") Integer userIdx, MemberDto.MemeberResponseDto dto){
		
		ModelAndView mv = new ModelAndView();
		
		dto = service.getMember(userIdx);
		
		mv.addObject("detail", dto);
		mv.setViewName("login/membermodify");
		
		return mv;
	}
	
	@GetMapping("/adminlist")
	public ModelAndView adminList(@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable){
		
		ModelAndView mv = new ModelAndView();
		
		Page<MemberDto.MemeberResponseDto>list= service.findAll(pageable);
				
		mv.addObject("memberlist", list);
		mv.setViewName("admin/index");
		
		return mv;
	}
	
	@GetMapping("/detail/{id}")
	public ModelAndView memberDetail(@PathVariable(value="id")Integer userIdx, MemberDto.MemeberResponseDto dto){
		
		ModelAndView mv = new ModelAndView();
		
		dto = service.getMember(userIdx);
		
		mv.addObject("detail", dto);
		mv.setViewName("login/membermodify");
		
		return mv;
	}
	
	@GetMapping("/finduserid")
	public ModelAndView findUserIdPage(){
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login/userfindid");
		
		return mv;
	}
	
	@GetMapping("/finduserpw")
	public ModelAndView findUserPw(){
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login/userfindpw");
		return mv;
	}
}
