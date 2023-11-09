package co.kr.board.controller.view;

import co.kr.board.domain.Board;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.repository.BoardRepository;
import co.kr.board.service.BoardService;
import co.kr.board.service.CategoryService;
import co.kr.board.service.HashTagService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/page/main/*")
@AllArgsConstructor
public class mainController {

	private final BoardService boardService;
	private final BoardRepository boardRepository;
	private final CategoryService categoryService;
	private final HashTagService hashTagService;

	@GetMapping("/mainpage")
	public ModelAndView mainPage()throws Exception{
		ModelAndView mv = new ModelAndView();

		//게시글 전체갯수.
		Integer boardCount = boardService.articleCount();
		//최근에 작성한 글(5개)
		List<BoardDto.BoardResponseDto>top5 = boardService.findBoardTop5();
		//카테고리 목록
		List<CategoryDto>categoryDtoList = categoryService.categoryList();
		//게시글 목록(전체)
		List<Board>boardList = boardRepository.findAll();
		//해시태그 목록
		List<String>hashTags = hashTagService.findAllHashTagNames();

		mv.addObject("top5",top5);
		mv.addObject("count",boardCount);
		mv.addObject("categoryMenu",categoryDtoList);
		mv.addObject("list",boardList);
		mv.addObject("hashTag",hashTags);

		log.info(boardList.size());
		log.info(categoryDtoList);
		log.info(hashTags);
		mv.setViewName("main/mainpage");

		return mv;
	}
}
