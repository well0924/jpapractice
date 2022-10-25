package co.kr.board.board.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.service.BoardService;
import co.kr.board.config.exception.dto.Response;
import co.kr.board.config.security.vo.CustomUserDetails;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/board/*")
public class BoardApiController {
	
	private final BoardService service;
		
	//페이징
	@GetMapping("/list")
	public Response<Page<BoardDto.ResponseDto>>articlelist(
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
				
		Page<BoardDto.ResponseDto>list =null;
				
		list = service.findAll(pageable);
		
		return new Response<Page<BoardDto.ResponseDto>>(HttpStatus.OK.value(),list);
	}
	
	//페이징+검색.
	@GetMapping("/list/search")
	public Response<Page<BoardDto.ResponseDto>>searchlist(
			@RequestParam(required = false) String keyword,
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
		
		Page<BoardDto.ResponseDto>list = null;
		
		list = service.findAllSearch(keyword, pageable);
			
		return new Response<>(HttpStatus.OK.value(),list);	
	}
	
	//작성
	@PostMapping("/write")
	public Response<Integer>writeproc(
			@Valid @RequestPart(name="jsonData") BoardDto.BoardRequestDto dto,
			BindingResult bindingresult,
			@AuthenticationPrincipal CustomUserDetails user)throws Exception{
		
		int result = 0;

		result = service.boardsave(dto,user.getMember());				

		return new Response<Integer>(HttpStatus.OK.value(),result);
	}
	
	//단일 조회
	@GetMapping("/detail/{id}")
	public Response<BoardDto.ResponseDto> detailarticle(@PathVariable(value="id",required = true)Integer boardId)throws Exception{
				
		BoardDto.ResponseDto detail = null;
		   
		detail = service.getBoard(boardId);
					
		return new Response<BoardDto.ResponseDto>(HttpStatus.OK.value(),detail);
	}
	
	//삭제
	@DeleteMapping("/delete/{id}")
	public Response<?>deletearticle(@PathVariable(value="id",required = true)Integer boardId,@AuthenticationPrincipal CustomUserDetails user)throws Exception{
		
		service.deleteBoard(boardId,user.getMember());
				
		return new Response<Integer>(HttpStatus.OK.value(),200);
	}
	
	//수정
	@PutMapping("/update/{id}")
	public Response<Integer>updatearticle(
			@PathVariable(value="id",required = true)Integer boardId, 
			@Valid BoardDto.BoardRequestDto dto,
			BindingResult bindingresult,
			@AuthenticationPrincipal CustomUserDetails user,
			@RequestPart(value="updatefilelist") List<MultipartFile>filelist,
			@RequestPart(value="removefilelist") List<String>removefilelist)throws Exception{
		
		int result = 0;
					
		result = service.updateBoard(boardId, dto,user.getMember(),filelist,removefilelist);
				
		return new Response<Integer>(HttpStatus.OK.value(),result);
	}
}
