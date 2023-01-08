package co.kr.board.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/page/main/*")
public class mainController {
	
	@GetMapping("/mainpage")
	public ModelAndView mainPage()throws Exception{
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("main/mainpage");
		return mv;
	}
}
