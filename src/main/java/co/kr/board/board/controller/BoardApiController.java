package co.kr.board.board.controller;



import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.service.BoardService;
import co.kr.board.config.exception.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/board/*")
public class BoardApiController {
	
	private final BoardService service;
	
	//페이징
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/list")
	public Response<Page<Board>>articlelist(
			@PageableDefault(sort="boardId",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
				
		Page<Board>list =null;
				
		list = service.findAll(pageable);
		
		if(list != null) {			
		
			new Response<Page<Board>>(HttpStatus.OK.value(),list);
		
		}else {
			
			new Response<Page<Board>>(HttpStatus.BAD_REQUEST.value(),list);
		}
		
		return new Response<Page<Board>>(HttpStatus.OK.value(),list);
	}
	
	@GetMapping("/list/search")
	public Response<Page<BoardDto.BoardResponseDto>>searchlist(
			@RequestParam String keyword,
			@PageableDefault(sort="boardId",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
		
		Page<BoardDto.BoardResponseDto>list = null;
		
		
		list = service.findAllSearch(keyword, pageable);
			
		if(list != null) {
			return new Response<>(HttpStatus.OK.value(),list);
		}else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(),list);
		}
		
	}
	
	@PostMapping("/write")
	public Response<?>writeproc(@Valid @RequestBody BoardDto.BoardRequestDto dto,
			BindingResult bindingresult)throws Exception{
		
		int result = 0;
		
		result = service.boardsave(dto);
								
		if(result > 0) {					
				
			return new Response<Integer>(HttpStatus.OK.value(),200);
				
		}else if(result <0) {
					
			return new Response<Integer>(HttpStatus.BAD_REQUEST.value(),400);
		}
	
		return new Response<Integer>(HttpStatus.OK.value(),result);
	}
	
	@GetMapping("/detail/{id}")
	public Response<BoardDto.BoardResponseDto> detailarticle(@PathVariable(value="id",required = true)Integer boardId)throws Exception{
				
		BoardDto.BoardResponseDto detail = null;
		   
		detail = service.getBoard(boardId);
			
		if(detail != null) {
	
		new Response<BoardDto.BoardResponseDto>(HttpStatus.OK.value(),detail);
			
		}		
		
		return new Response<BoardDto.BoardResponseDto>(HttpStatus.OK.value(),detail);
	}
	
	@DeleteMapping("/delete/{id}")
	public Response<Integer>deletearticle(@PathVariable(value="id")Integer boardId)throws Exception{
				
		service.deleteBoard(boardId);
	
		new Response<Integer>(HttpStatus.OK.value(),200);
				
		return new Response<Integer>(HttpStatus.OK.value(),200);
	}
	
	@PutMapping("/update/{id}")
	public Response<?>updatearticle(
			@PathVariable(value="id")Integer boardId,
			@Valid @RequestBody BoardDto.BoardRequestDto dto,BindingResult bindingresult)throws Exception{
		
		int result = 0;
					
		result = service.updateBoard(boardId, dto);
		
		if(result > 0) {			

			new Response<Integer>(HttpStatus.OK.value(),result);
		
		}else {
		
			new Response<Integer>(HttpStatus.BAD_REQUEST.value(),result);
		}
		
		return new Response<Integer>(HttpStatus.OK.value(),result);
	}
}
