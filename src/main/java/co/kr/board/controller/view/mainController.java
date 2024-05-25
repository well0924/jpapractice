package co.kr.board.controller.view;

import co.kr.board.domain.Const.SearchType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;


@Log4j2
@Controller
@RequestMapping("/page/main")
@AllArgsConstructor
public class mainController {

	private final BoardService boardService;

	@GetMapping("/mainpage")
	public ModelAndView mainPage(
			@RequestParam(required = false,value = "searchVal")String searchVal,
			@RequestParam(required = false,value = "searchType")String searchType,
			@PageableDefault(sort = "id",direction = Sort.Direction.DESC) Pageable pageable){

		ModelAndView mv = new ModelAndView();

		//게시글 목록(전체)
		Page<BoardDto.BoardResponseDto> boardList = boardService.findAllBoards(pageable);
		log.info("list::"+boardList.stream().collect(Collectors.toList()));

		if(searchVal !=null){
			boardList = boardService.findAllSearch(searchVal, String.valueOf(SearchType.toSearch(searchType)),pageable);
		}

		mv.addObject("list",boardList);
		mv.addObject("searchVal", searchVal);

		mv.setViewName("main/mainpage");

		return mv;
	}
}
