package co.kr.board.reply.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.service.CommentService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CommentApiController {
	
	private final CommentService service;
	
	@GetMapping("/reply/{id}")
	public ResponseEntity<List<CommentDto.CommentResponseDto>>replylist(@PathVariable("id")Integer boardId)throws Exception{
		
		List<CommentDto.CommentResponseDto>replylist = null;
		
		try {
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
