package co.kr.board.board.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.service.BoardService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/page/board/*")
public class BoardController {
	
	private final BoardService service;
	
	@GetMapping("/list")
	public ModelAndView pagelist(
			@PageableDefault(sort="boardId",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		Page<Board> list =null;

		try {
		
			list = service.findAll(pageable);
		
		} catch (Exception e) {
			e.printStackTrace();		
		}
		
		mv.addObject("list", list);
		mv.addObject("previous", pageable.previousOrFirst().getPageNumber());
		mv.addObject("next", pageable.next().getPageNumber());
		mv.addObject("hasNext", list.hasNext());
		mv.addObject("hasPrev", list.hasPrevious());
		
		mv.setViewName("board/boardlist");
		
		return mv;
	}
	
	@GetMapping("/search")
	public ModelAndView search(Pageable pageable,String keyword)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		Page<Board>searchList = service.findByboardTitleContaining(keyword, pageable);
		
		
		mv.addObject("searchList", searchList);
		mv.addObject("keyword", keyword);        
		mv.addObject("previous", pageable.previousOrFirst().getPageNumber());        
		mv.addObject("next", pageable.next().getPageNumber());        
		mv.addObject("hasNext", searchList.hasNext());        
		mv.addObject("hasPrev", searchList.hasPrevious());
		
		mv.setViewName("board/boardlist");
		
		return mv;
	}
	
	@GetMapping("/detail/{id}")
	public ModelAndView detailpage(@PathVariable(value="id")Integer boardId,BoardDto.BoardResponseDto dto)throws Exception{
	
		ModelAndView mv = new ModelAndView();
		
		try {
			dto = service.getBoard(boardId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.addObject("detail", dto);
		mv.setViewName("board/detailpage");
		
		return mv;
	}
	
	@GetMapping("/write")
	public ModelAndView writepage(@Valid @ModelAttribute BoardDto.BoardRequestDto dto,BindingResult binding)throws Exception{
	
		ModelAndView mv = new ModelAndView();
		
		if(binding.hasErrors()) {//유효성 검사에 문제가 있는 경우
			mv.setViewName("board/writeboard");
		}
		
		
		mv.setViewName("board/writeboard");
	
		return mv;
	}
	
	@GetMapping("/modify/{id}")
	public ModelAndView modifypage(@PathVariable(value="id")Integer boardId, BoardDto.BoardResponseDto dto)throws Exception{
		
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
