package co.kr.board.board.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.service.BoardService;
import co.kr.board.config.exception.dto.Response;
import co.kr.board.config.security.vo.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/board/*")
public class BoardApiController {
	
	private final BoardService service;
		
	//페이징
	@GetMapping("/list")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>articlelist(
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable){
				
		Page<BoardDto.BoardResponseDto>list = service.findAllPage(pageable);
		
		return new Response<>(HttpStatus.OK.value(),list);
	}
	
	//페이징+검색.
	@GetMapping("/list/search")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>searchlist(
			@RequestParam(required = false) String keyword,
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable){
		
		Page<BoardDto.BoardResponseDto>list = service.findAllSearch(keyword, pageable);
			
		return new Response<>(HttpStatus.OK.value(),list);	
	}
	
	//작성
	@PostMapping("/write")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Response<Integer>writeproc(
			@Valid @RequestBody BoardDto.BoardRequestDto dto,
			BindingResult bindingresult,			
			@AuthenticationPrincipal CustomUserDetails user){

		int result = service.boardsave(dto,user.getMember());
		
		return new Response<>(HttpStatus.OK.value(),result);
	}
	
	//단일 조회
	@GetMapping("/detail/{id}")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<BoardDto.BoardResponseDto> detailarticle(@PathVariable(value="id")Integer boardId){
				
		BoardDto.BoardResponseDto detail = service.getBoard(boardId);
					
		return new Response<>(HttpStatus.OK.value(),detail);
	}
	
	//삭제
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Response<?>deletearticle(@PathVariable(value="id")Integer boardId,@AuthenticationPrincipal CustomUserDetails user){
		
		service.deleteBoard(boardId,user.getMember());
				
		return new Response<>(HttpStatus.OK.value(),200);
	}
	
	//수정
	@PatchMapping("/update/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Response<?>updatearticle(
			@PathVariable(value="id")Integer boardId,
			@Valid @RequestBody BoardDto.BoardRequestDto dto,
			BindingResult bindingresult,
			@AuthenticationPrincipal CustomUserDetails user){
		
		int result = service.updateBoard(boardId, dto,user.getMember());

		return new Response<>(HttpStatus.OK.value(),result);
	}
}
