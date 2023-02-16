package co.kr.board.board.controller;

import javax.validation.Valid;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.login.domain.Member;
import co.kr.board.login.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.service.BoardService;
import co.kr.board.config.Exception.dto.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/board/*")
public class BoardApiController {
	
	private final BoardService service;
	private final MemberRepository memberRepository;

	@GetMapping("/list")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>articleList(@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable){
				
		Page<BoardDto.BoardResponseDto>list = service.findAllPage(pageable);
		
		return new Response<>(HttpStatus.OK.value(),list);
	}

	@GetMapping("/list/search")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>searchList(@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable, @RequestParam(required = false,value = "searchVal")String searchVal){

		Page<BoardDto.BoardResponseDto>list = service.findAllSearch(searchVal,pageable);

		return new Response<>(HttpStatus.OK.value(),list);
	}

	@GetMapping("/detail/{id}")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<BoardDto.BoardResponseDto> detailArticle(@PathVariable(value="id")Integer boardId){

		BoardDto.BoardResponseDto detail = service.getBoard(boardId);

		return new Response<>(HttpStatus.OK.value(),detail);
	}
	@PostMapping(value = "/write",consumes = {"multipart/form-data"})
	@ResponseStatus(code = HttpStatus.CREATED)
	public Response<Integer>writeArticle(
			@RequestPart(value="files", required=false) List<MultipartFile> files,
			@Valid @RequestPart(value = "boardsave") BoardDto.BoardRequestDto dto, BindingResult bindingresult)throws Exception{

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));

		int result = service.boardsave(dto,member,files);

		log.info("title: {},content: {},image:{}",dto.getBoardTitle(),dto.getBoardContents(),files);
		return new Response<>(HttpStatus.OK.value(),result);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Response<?>deleteArticle(@PathVariable(value="id")Integer boardId){

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));

		service.deleteBoard(boardId,member);
				
		return new Response<>(HttpStatus.OK.value(),200);
	}
	
	@PatchMapping("/update/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Response<?>updateArticle(@PathVariable(value="id")Integer boardId,@Valid @RequestBody BoardDto.BoardRequestDto dto, BindingResult bindingresult){

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));

		int result = service.updateBoard(boardId, dto,member);

		return new Response<>(HttpStatus.OK.value(),result);
	}
}
