package co.kr.board.board.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import co.kr.board.board.domain.dto.BoardResponseDto;
import co.kr.board.board.service.BoardService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/page/board/*")
public class BoardController {
	
	private final BoardService service;
	
	@GetMapping("/list")
	public ModelAndView pagelist(@PageableDefault(sort="boardId",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		List<BoardResponseDto> list =null;

		try {
		
			list = service.findAll();
		
		} catch (Exception e) {
			e.printStackTrace();		
		}
		
		mv.addObject("list", list);
		mv.setViewName("board/boardlist");
		
		return mv;
	}
	
	
	@GetMapping("/detail/{id}")
	public ModelAndView detailpage(@PathVariable(value="id")Integer boardId,BoardResponseDto dto)throws Exception{
	
		ModelAndView mv = new ModelAndView();
		
		try {
			dto = service.getBoard(boardId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.addObject("detail", dto);
		mv.setViewName("board/detail");
		
		return mv;
	}
	
	@GetMapping("/write")
	public ModelAndView writepage()throws Exception{
	
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("board/writeboard");
	
		return mv;
	}
	
	@GetMapping("/modify/{id}")
	public ModelAndView modifypage(@PathVariable(value="id")Integer boardId,BoardResponseDto dto)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		try {
			dto = service.getBoard(boardId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.addObject("modify", dto);
		mv.setViewName("board/modifyboard");
		
		return mv;
	}
	
}
