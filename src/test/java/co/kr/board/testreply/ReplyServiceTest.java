package co.kr.board.testreply;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.kr.board.board.domain.Board;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.board.service.BoardService;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.login.domain.Member;
import co.kr.board.login.repository.MemberRepository;
import co.kr.board.login.service.MemberService;
import co.kr.board.reply.domain.Comment;
import co.kr.board.reply.domain.dto.CommentDto;
import co.kr.board.reply.repository.CommentRepository;
import co.kr.board.reply.service.CommentService;

@SpringBootTest
public class ReplyServiceTest {
	
	@Autowired
	CommentRepository commentrepository;
	
	@Autowired
	CommentService commentservice;
	
	@Autowired
	BoardRepository reposi;
	
	@Autowired
	BoardService boardservice;
	
	@Autowired
	MemberRepository memberrepos;
	
	@Autowired
	MemberService memberservice;
	
	private Member member;
	
	private Board board;
	
	@BeforeEach
	public void before() {
		Optional<Member>memberdetail = memberrepos.findById(1);
		
		member = memberdetail.get();
		
		Optional<Board>detail = reposi.findById(5);
		
		board = detail.get();
	}
	
	@Test
	@DisplayName("댓글 목록")
	public void replylist() throws Exception {
		//when
		List<CommentDto.CommentResponseDto>replylist = commentservice.findCommentsBoardId(5);
		//then
		System.out.println("댓글목록:"+replylist);
	}
	
	@Test
	@DisplayName("댓글 작성")
	public void replywrite() throws Exception {
		//given
		CommentDto.CommentRequestDto dto = CommentDto.CommentRequestDto.builder().replyContents("댓글작성!").createdAt(LocalDateTime.now()).build();
		//when
		commentservice.replysave(dto,member,board.getId());
		//then
		assertEquals("댓글작성!",dto.getReplyContents());
		assertEquals("well4149", member.getUsername());
	}
	
	@Test
	@DisplayName("댓글 삭제")
	public void replydelete() throws Exception {
		//when
		commentservice.replydelete(21, member);
		//then
		assertThrows(CustomExceptionHandler.class,()->{
			commentservice.replydelete(21, member);
		});
	}
	
	@Test
	@DisplayName("댓글 수정")
	public void replyUpdate() throws Exception {
		//given
		Optional<Comment>commentdetail = commentrepository.findById(2);
		
		Comment getcomment = commentdetail.get();
		
		CommentDto.CommentRequestDto dto = CommentDto.CommentRequestDto.builder().replyContents("update!!").build();
		
		getcomment.contentsChange(dto.getReplyContents());
		
		//when
		commentservice.replyUpdate(dto, member, getcomment.getId());
		
		//then
		assertEquals(dto.getReplyContents(), getcomment.getReplyContents());
	}

}
