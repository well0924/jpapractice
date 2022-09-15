package co.kr.board.reply.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.reply.domain.Comment;
import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.domain.dto.CommentDto.CommentResponseDto;
import co.kr.board.reply.service.CommentService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reply/*")
public class CommentApiController {
	
	private final CommentService service;
	
	@GetMapping("/list/{id}")
	public ResponseEntity<List<CommentDto.CommentResponseDto>>getBoardComments(@PathVariable(value="id")Integer boardId)throws Exception{
		ResponseEntity<List<CommentDto.CommentResponseDto>>entity = null;
		
		List<CommentDto.CommentResponseDto>list = null;
		try {
			list= service.findCommentsBoardId(boardId);
			
			if(list != null) {
				entity = new ResponseEntity<List<CommentDto.CommentResponseDto>>(list,HttpStatus.OK);
			}else {
				entity = new ResponseEntity<List<CommentDto.CommentResponseDto>>(list,HttpStatus.BAD_REQUEST);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
	
	@PostMapping("/write")
	public ResponseEntity<Integer>replywrite(@Valid @RequestBody Comment dto)throws Exception{
		
		ResponseEntity<Integer>entity = null;
		
		int insertresult = 0;
		
		try {
			insertresult = service.replysave(dto);
			
			if(insertresult > 0) {
				entity = new ResponseEntity<Integer>(200,HttpStatus.OK);
			}else {
				entity = new ResponseEntity<Integer>(400,HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<Integer>(500,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}
	
	@DeleteMapping("/delete/{replyid}")
	public ResponseEntity<String>replydelete(@PathVariable("replyid")Integer replyId)throws Exception{
		
		ResponseEntity<String>entity = null;
				
		try {
			
			service.replydelete(replyId);
			
			entity = new ResponseEntity<String>("o.k",HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
}
