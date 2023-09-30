package co.kr.board.controller.view;

import javax.validation.Valid;

import co.kr.board.domain.Category;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.repository.CategoryRepository;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Dto.AttachDto;
import co.kr.board.service.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import co.kr.board.service.BoardService;
import co.kr.board.config.security.auth.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@Controller
@AllArgsConstructor
@RequestMapping("/page/board")
public class BoardController {
	private final BoardService service;	
	private final FileService fileService;
	private final CategoryRepository categoryRepository;

	@GetMapping("/list/{cname}")
	public ModelAndView pageList(
			@PathVariable(value = "cname",required = false) String categoryName,
			@RequestParam(required = false,value = "searchVal") String searchVal,
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable){
		
		ModelAndView mv = new ModelAndView();

		Category category = categoryRepository.findByName(categoryName);
		Page<BoardDto.BoardResponseDto> list =null;
		//페이징 기능
		list= service.findAllPage(pageable,categoryName);

		mv.addObject("list", list);
		mv.addObject("category",category);
		mv.addObject("searchVal", searchVal);
		mv.addObject("previous", pageable.previousOrFirst().getPageNumber());
		mv.addObject("next", pageable.next().getPageNumber());
		mv.addObject("hasNext", list.hasNext());        
		mv.addObject("hasPrev", list.hasPrevious());

		mv.setViewName("board/boardlist");
		
		return mv;
	}
	
	@GetMapping("/detail/{id}")
	public ModelAndView detailPage(@PathVariable(value="id")Integer boardId, BoardDto.BoardResponseDto dto)throws Exception{
	
		ModelAndView mv = new ModelAndView();
		BoardDto.BoardResponseDto board = service.getBoard(boardId);
		//파일 첨부목록
		List<AttachDto> fileList = fileService.filelist(boardId);
		
		log.info(fileList);

		mv.addObject("fileList",fileList);
		mv.addObject("detail", board);
		mv.setViewName("board/detailpage");
		
		return mv;
	}
	
	@GetMapping("/write")
	public ModelAndView writePage(@Valid @ModelAttribute BoardDto.BoardRequestDto dto,BindingResult binding,@AuthenticationPrincipal CustomUserDetails user){
	
		ModelAndView mv = new ModelAndView();	
		
		mv.setViewName("board/writeboard");
	
		return mv;
	}
	
	@GetMapping("/modify/{id}")
	public ModelAndView modifyPage(@PathVariable(value="id")Integer boardId, BoardDto.BoardResponseDto dto)throws Exception{
		
		ModelAndView mv = new ModelAndView();
		BoardDto.BoardResponseDto board = service.getBoard(boardId);
		//파일 첨부목록
		List<AttachDto> fileList = fileService.filelist(boardId);

		log.info(fileList);

		mv.addObject("fileList",fileList);

		mv.addObject("modify", dto);
		mv.setViewName("board/modifyboard");
		
		return mv;
	}
	
	
}