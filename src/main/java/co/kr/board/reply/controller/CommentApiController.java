package co.kr.board.reply.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.reply.domain.Comment;
import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.service.CommentService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reply/*")
public class CommentApiController {
	
	private final CommentService service;
	
	@GetMapping("/list/{id}")
	public ResponseEntity<List<Comment>>replylist(@PathVariable("id")Integer boardId)throws Exception{
		
		ResponseEntity<List<Comment>> entity = null;
		
		List<Comment>replylist = null;
		
		try {
			replylist = service.findAll(boardId);
			
			if(replylist != null) {
				entity = new ResponseEntity<List<Comment>>(replylist,HttpStatus.OK);
			}else {
				entity = new ResponseEntity<List<Comment>>(replylist,HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<List<Comment>>(replylist, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}
}
