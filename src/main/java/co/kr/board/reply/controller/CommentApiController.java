package co.kr.board.reply.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.config.exception.dto.Response;
import co.kr.board.config.security.vo.CustomUserDetails;
import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.service.CommentService;
import lombok.AllArgsConstructor;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/reply/*")
public class CommentApiController {
	
	private final CommentService service;
	
	@GetMapping("/list/{id}")
	public Response<List<CommentDto.CommentResponseDto>>getBoardComments(@PathVariable(value="id")Integer boardId)throws Exception{
		
		List<CommentDto.CommentResponseDto>list = service.findCommentsBoardId(boardId);
	
		return new Response<>(HttpStatus.OK.value(),list);
	}
	
	@PostMapping("/write/{id}")
	public Response<?>replyWrite(
			@PathVariable(value="id")Integer boardId,
			@Valid @RequestBody CommentDto.CommentRequestDto dto,
			BindingResult bindingresult,
			@AuthenticationPrincipal CustomUserDetails user)throws Exception{
		
		int insertResult = service.replysave(dto, user.getMember(), boardId);
		
		return new Response<>(HttpStatus.OK.value(),insertResult);
	}
	
	@DeleteMapping("/delete/{id}")
	public Response<?>replyDelete(
			@PathVariable(value="id")Integer replyId,
			@AuthenticationPrincipal CustomUserDetails user)throws Exception{
		
		service.replydelete(replyId,user.getMember());
		
		return new Response<>(HttpStatus.OK.value(),"o.k");
	}
	
	@PutMapping("/update/{id}")
	public Response<?>replyUpdate(
			@PathVariable(value="id")Integer replyId,
			CommentDto.CommentRequestDto dto,
			@AuthenticationPrincipal CustomUserDetails user)throws Exception{
		
		int updateResult = service.replyUpdate(dto,user.getMember(), replyId);
		
		return new Response<>(HttpStatus.OK.value(),updateResult);
	}
}
