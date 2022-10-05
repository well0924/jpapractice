package co.kr.board.board.controller;


import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import co.kr.board.config.Response;
import co.kr.board.config.aop.ValidationCheck;
import co.kr.board.login.domain.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/board/*")
public class BoardRestController {
	
	private final BoardService service;
	
	private final ValidationCheck check;
	
	@GetMapping("/list")
	public Response<Page<Board>>articlelist(
			@PageableDefault(sort="boardId",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
				
		Page<Board>list =null;
		
		try {
			
			list = service.findAll(pageable);
			
			if(list != null) {			
			
				new Response<Page<Board>>(HttpStatus.OK.value(),list);
			
			}else {
				
				new Response<Page<Board>>(HttpStatus.BAD_REQUEST.value(),list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			new Response<Page<Board>>(HttpStatus.INTERNAL_SERVER_ERROR.value(),list);
		}
		return new Response<Page<Board>>(HttpStatus.OK.value(),list);
	}
	
	//페이징 및 검색
	@GetMapping("/list")
	public Response<Page<Board>>findAllSearch(
			@RequestParam String boardTitle, 
			@RequestParam String boardContents,
			@PageableDefault(sort="boardId",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
		
		Page<Board>boardlist;
		
		return null;
	}
	
	@PostMapping("/write")
	public Response<?>writeproc(@Valid @RequestBody BoardDto.BoardRequestDto dto,
			MemberDto.MemeberResponseDto user,
			BindingResult bindingresult)throws Exception{
		
		int result = 0;
		
		if(bindingresult.hasErrors()) {
			Map<String, String> validatorResult = check.validateHandling(bindingresult);
			log.info("result:"+validatorResult);
			return new Response<>(HttpStatus.BAD_REQUEST.value(),validatorResult);
		}
		
		try {
			
				result = service.boardsave(dto,user.getUsername());
				
				log.info("결과값:"+result);
				
				if(result > 0) {					
				
					return new Response<Integer>(HttpStatus.OK.value(),200);
				
				}else if(result <0) {
					return new Response<Integer>(HttpStatus.BAD_REQUEST.value(),400);
				}
				
		} catch (Exception e) {
			
			e.printStackTrace();
			
			return new Response<Integer>(HttpStatus.INTERNAL_SERVER_ERROR.value(),500);
		}
		return new Response<Integer>(HttpStatus.OK.value(),result);
	}
	
	@GetMapping("/detail/{id}")
	public Response<BoardDto.BoardResponseDto> detailarticle(@PathVariable(value="id",required = true)Integer boardId)throws Exception{
				
		BoardDto.BoardResponseDto detail = null;
		
		try {	   
			detail = service.getBoard(boardId);
			
			if(detail != null) {
	
				new Response<BoardDto.BoardResponseDto>(HttpStatus.OK.value(),detail);
			
			}		
		} catch (Exception e) {
			
			e.printStackTrace();
			
			new Response<BoardDto.BoardResponseDto>(HttpStatus.INTERNAL_SERVER_ERROR.value(),detail);
		}
		
		return new Response<BoardDto.BoardResponseDto>(HttpStatus.OK.value(),detail);
	}
	
	@DeleteMapping("/delete/{id}")
	public Response<Integer>deletearticle(@PathVariable(value="id")Integer boardId)throws Exception{
				
		try {
			service.deleteBoard(boardId);
	
			new Response<Integer>(HttpStatus.OK.value(),200);
		
		} catch (Exception e) {		
		
			e.printStackTrace();
			
			new Response<Integer>(HttpStatus.INTERNAL_SERVER_ERROR.value(),500);
		
		}
		
		return new Response<Integer>(HttpStatus.OK.value(),200);
	}
	
	@PutMapping("/update/{id}")
	public Response<?>updatearticle(
			@PathVariable(value="id")Integer boardId,
			@Valid @RequestBody BoardDto.BoardRequestDto dto,BindingResult bindingresult)throws Exception{
		
		if(bindingresult.hasErrors()) {
			Map<String, String> validatorResult = check.validateHandling(bindingresult);
			log.info("result:"+validatorResult);
			return new Response<>(HttpStatus.BAD_REQUEST.value(),validatorResult);
		}
		
		int result = 0;
		
		try {
			
			result = service.updateBoard(boardId, dto);
			
			if(result > 0) {			

				new Response<Integer>(HttpStatus.OK.value(),result);
			
			}else {
			
				new Response<Integer>(HttpStatus.BAD_REQUEST.value(),result);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			
			new Response<Integer>(HttpStatus.INTERNAL_SERVER_ERROR.value(),result);
		}
		
		return new Response<Integer>(HttpStatus.OK.value(),result);
	}
}
