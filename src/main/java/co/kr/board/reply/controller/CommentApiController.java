package co.kr.board.reply.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.config.Response;
import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.service.CommentService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reply/*")
public class CommentApiController {
	
	private final CommentService service;
	
	@GetMapping("/list/{id}")
	public Response<List<CommentDto.CommentResponseDto>>getBoardComments(@PathVariable(value="id")Integer boardId)throws Exception{
		
		List<CommentDto.CommentResponseDto>list = null;

		try {
			list= service.findCommentsBoardId(boardId);
			
			if(list != null) {
				 new Response<List<CommentDto.CommentResponseDto>>(HttpStatus.OK.value(),list);
			}else {
				 new Response<List<CommentDto.CommentResponseDto>>(HttpStatus.BAD_REQUEST.value(),list);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			new Response<List<CommentDto.CommentResponseDto>>(HttpStatus.INTERNAL_SERVER_ERROR.value(),list);
		}
		return new Response<List<CommentDto.CommentResponseDto>>(HttpStatus.OK.value(),list);
	}
	
	@PostMapping("/write/{boardid}")
	public Response<Integer>replywrite( @PathVariable("boardid")Integer boardId,@Valid @RequestBody CommentDto.CommentRequestDto dto)throws Exception{
		
		int insertresult = 0;
		
		try {		
			insertresult = service.replysave(dto);
			
			if(insertresult > 0) {							
				 new Response<Integer>(HttpStatus.OK.value(),200);
			}else if(insertresult < 0) {
				 new Response<Integer>(HttpStatus.BAD_REQUEST.value(),400);
			}
		} catch (Exception e) {
			e.printStackTrace();
			new Response<Integer>(HttpStatus.INTERNAL_SERVER_ERROR.value(),500);
		}
		return new Response<Integer>(HttpStatus.OK.value(),200);
	}
	
	@DeleteMapping("/delete/{replyid}")
	public Response<String>replydelete(@PathVariable("replyid")Integer replyId)throws Exception{
						
		try {
			service.replydelete(replyId);
			new Response<String>(HttpStatus.OK.value(),"o.k");			
		} catch (Exception e) {
			e.printStackTrace();
			new Response<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(),"500");
		}
		return new Response<String>(HttpStatus.OK.value(),"o.k");
	}
}
