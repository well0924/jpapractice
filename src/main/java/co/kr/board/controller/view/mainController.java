package co.kr.board.controller.view;

import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Log4j2
@Controller
@RequestMapping("/page/main/*")
@AllArgsConstructor
public class mainController {

	private final BoardService boardService;

	@GetMapping("/mainpage")
	public ModelAndView mainPage(@PageableDefault(sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
		ModelAndView mv = new ModelAndView();
		//게시글 목록(전체)
		Page<BoardDto.BoardResponseDto> boardList = boardService.findAll(pageable);

		mv.addObject("list",boardList);

		mv.setViewName("main/mainpage");

		return mv;
	}
}
