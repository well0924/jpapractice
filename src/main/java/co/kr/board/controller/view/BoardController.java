package co.kr.board.controller.view;

import javax.validation.Valid;

import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.domain.Const.SearchType;
import co.kr.board.domain.Dto.AttachDto;
import co.kr.board.service.CategoryService;
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
	private final CategoryService categoryService;

	//게시글 목록
	@GetMapping("/list/{cname}")
	public ModelAndView pageList(
			@PathVariable(value = "cname") String categoryName,
			@RequestParam(required = false,value = "searchVal") String searchVal,
			@RequestParam(required = false,value = "searchType")String searchType,
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable){
		
		ModelAndView mv = new ModelAndView();

		//페이징 기능
		Page<BoardDto.BoardResponseDto> list = service.findAllPage(pageable,categoryName);
		//최근에 작성한 글(5개)
		List<BoardDto.BoardResponseDto>top5 = service.findBoardTop5();
		//카테고리 목록
		List<CategoryDto>categoryDtoList = categoryService.categoryList();
		//게시글 갯수 & 카테고리 갯수
		Integer boardCount = service.articleCount();
		Integer categoryCount = service.categoryCount(categoryName);

		//검색어가 있으면 검색.
		if(searchVal!=null){
			list= service.findAllSearch(searchVal, String.valueOf(SearchType.toSearch(searchType)),pageable);
		}

		mv.addObject("list", list);
		mv.addObject("cname",categoryName);
		mv.addObject("count",boardCount);
		mv.addObject("categoryCount",categoryCount);
		mv.addObject("searchVal", searchVal);
		mv.addObject("previous", pageable.previousOrFirst().getPageNumber());
		mv.addObject("next", pageable.next().getPageNumber());
		mv.addObject("hasNext", list.hasNext());        
		mv.addObject("hasPrev", list.hasPrevious());
		mv.addObject("top5",top5);
		mv.addObject("categoryMenu",categoryDtoList);

		mv.setViewName("board/boardlist");
		
		return mv;
	}
	
	@GetMapping("/detail/{id}")
	public ModelAndView detailPage(@PathVariable(value="id")Integer boardId, BoardDto.BoardResponseDto dto)throws Exception{

		ModelAndView mv = new ModelAndView();

		BoardDto.BoardResponseDto board = service.getBoard(boardId);
		//파일 첨부목록
		List<AttachDto> fileList = fileService.filelist(boardId);
		//게시글 이전글/다음글
		List<BoardDto.BoardResponseDto>nextPrevious = service.articleNextPreviousBoard(boardId);
		//최근에 작성한 글(5개)
		List<BoardDto.BoardResponseDto>top5 = service.findBoardTop5();
		//카테고리 목록
		List<CategoryDto>categoryDtoList = categoryService.categoryList();

		//게시글 갯수 & 카테고리 갯수
		Integer boardCount = service.articleCount();

		mv.addObject("count",boardCount);
		mv.addObject("nextPrevious",nextPrevious);
		mv.addObject("fileList",fileList);
		mv.addObject("detail", board);
		mv.addObject("top5",top5);
		mv.addObject("categoryMenu",categoryDtoList);

		mv.setViewName("board/detailpage");
		
		return mv;
	}

	@GetMapping("/write")
	public ModelAndView writePage(@Valid @ModelAttribute BoardDto.BoardRequestDto dto,BindingResult binding,
								  @AuthenticationPrincipal CustomUserDetails user){

		ModelAndView mv = new ModelAndView();
		//게시글 갯수
		Integer boardCount = service.articleCount();
		//최근에 작성한 글(5개)
		List<BoardDto.BoardResponseDto>top5 = service.findBoardTop5();
		//카테고리 목록
		List<CategoryDto>categoryDtoList = categoryService.categoryList();

		mv.addObject("count",boardCount);
		mv.addObject("top5",top5);
		mv.addObject("categoryMenu",categoryDtoList);
		mv.setViewName("board/writeboard");

		return mv;
	}

	@GetMapping("/modify/{id}")
	public ModelAndView modifyPage(@PathVariable(value="id")Integer boardId)throws Exception{
		ModelAndView mv = new ModelAndView();
		//게시글 조회
		BoardDto.BoardResponseDto board = service.getBoard(boardId);
		//파일 첨부목록
		List<AttachDto> fileList = fileService.filelist(boardId);
		//게시글 갯수
		Integer boardCount = service.articleCount();
		//최근에 작성한 글(5개)
		List<BoardDto.BoardResponseDto>top5 = service.findBoardTop5();
		//카테고리 목록
		List<CategoryDto>categoryDtoList = categoryService.categoryList();

		mv.addObject("count",boardCount);
		mv.addObject("fileList",fileList);
		mv.addObject("modify", board);
		mv.addObject("top5",top5);
		mv.addObject("categoryMenu",categoryDtoList);

		mv.setViewName("board/modifyboard");
		
		return mv;
	}
	
	//비밀글 화면
	@GetMapping("/passwordcheck")
	public ModelAndView passwordCheckPage(Integer boardId){
		ModelAndView mv = new ModelAndView();

		BoardDto.BoardResponseDto dto = service.getBoard(boardId);

		mv.addObject("detail",dto);

		mv.setViewName("/board/passwordCheck");
		return mv;
	}
}
