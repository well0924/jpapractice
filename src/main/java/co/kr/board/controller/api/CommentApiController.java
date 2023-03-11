package co.kr.board.controller.api;


import java.util.List;

import javax.validation.Valid;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Member;
import co.kr.board.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.config.Exception.dto.Response;
import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.service.CommentService;
import lombok.AllArgsConstructor;
@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/reply/*")
public class CommentApiController {
	
	private final CommentService service;
	private final MemberRepository memberRepository;
	@GetMapping("/list/{id}")
	public Response<List<CommentDto.CommentResponseDto>>getBoardComments(@PathVariable(value="id")Integer boardId)throws Exception{
		List<CommentDto.CommentResponseDto>list = service.findCommentsBoardId(boardId);
		return new Response<>(HttpStatus.OK.value(),list);
	}
	
	@PostMapping("/write/{id}")
	public Response<?>replyWrite(@PathVariable(value="id")Integer boardId,@Valid @RequestBody CommentDto.CommentRequestDto dto,BindingResult bindingresult){

		Member mebmer = getMember();
		int insertResult = service.replysave(dto, mebmer, boardId);
		
		return new Response<>(HttpStatus.OK.value(),insertResult);
	}
	
	@DeleteMapping("/delete/{id}")
	public Response<?>replyDelete(@PathVariable(value="id")Integer replyId){

		Member mebmer = getMember();
		service.replydelete(replyId,mebmer);

		return new Response<>(HttpStatus.OK.value(),"o.k");
	}
	
	@PutMapping("/update/{id}")
	public Response<?>replyUpdate(@PathVariable(value="id")Integer replyId,CommentDto.CommentRequestDto dto){

		Member mebmer = getMember();
		int updateResult = service.replyUpdate(dto,mebmer, replyId);
		
		return new Response<>(HttpStatus.OK.value(),updateResult);
	}

	private Member getMember(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
		return member;
	}
}