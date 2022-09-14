package co.kr.board.reply.service;

import org.springframework.stereotype.Service;

import co.kr.board.reply.repository.CommentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
	
	private final CommentRepository repository;
	
	
}
