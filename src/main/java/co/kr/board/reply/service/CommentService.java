package co.kr.board.reply.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.kr.board.reply.domain.Comment;
import co.kr.board.reply.repository.CommentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
	
	private final CommentRepository repository;
	
	public List<Comment>getCommentList(Integer boardId)throws Exception{
		return repository.getCommentList();
	}
}
