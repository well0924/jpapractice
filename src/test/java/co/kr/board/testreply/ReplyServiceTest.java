package co.kr.board.testreply;

import static org.assertj.core.api.Assertions.assertThat;
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
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
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
	BoardRepository repository;
	
	@Autowired
	BoardService boardservice;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	MemberService memberservice;
	
	private Member member;
	
	private Board board;
	
	@BeforeEach
	public void before() {
		Optional<Member> memberDetail = memberRepository.findById(1);
		
		member = memberDetail.get();
		
		Optional<Board>detail = repository.findById(5);
		
		board = detail.get();
	}
	
	@Test
	@DisplayName("댓글 목록")
	public void replyList() throws Exception {
		//when
		List<CommentDto.CommentResponseDto>replylist = commentservice.findCommentsBoardId(5);
		//then
		System.out.println("댓글목록:"+replylist);
	}
	
	@Test
	@DisplayName("댓글 작성")
	public void replyWrite(){
		//given
		CommentDto.CommentRequestDto dto = CommentDto.CommentRequestDto.builder().replyContents("댓글작성!").createdAt(LocalDateTime.now()).build();
		//when
		commentservice.replysave(dto,member,board.getId());
		//then
		assertEquals("댓글작성!",dto.getReplyContents());
	}
	
	@Test
	@DisplayName("댓글 작성실패 - 회원이 아닌경우")
	public void replyWriteFail3(){
		//given
		CommentDto.CommentRequestDto dto = CommentDto
				.CommentRequestDto
				.builder()
				.replyContents("eot")
				.createdAt(LocalDateTime.now()).build();
		member = null;
		//when
		CustomExceptionHandler customExceptionHandler =assertThrows(CustomExceptionHandler.class,()->{
			commentservice.replysave(dto,member,board.getId());
		});
		//then
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.ONLY_USER);
	}
	
	@Test
	@DisplayName("댓글 삭제")
	public void replyDelete(){
		//given
		Long count = commentrepository.count();
		CommentDto.CommentRequestDto dto = CommentDto
				.CommentRequestDto.builder()
				.replyContents("deleteTest!")
				.createdAt(LocalDateTime.now()).build();
		Integer result = commentservice.replysave(dto, member, board.getId());
		//when
		commentservice.replydelete(result, member);
		//then
		assertThat(count).isEqualTo(count);
		
	}
	
	@Test
	@DisplayName("댓글 삭제실패 - 회원이 아닌경우")
	public void replyDeleteFail1(){
		//given
		CommentDto.CommentRequestDto dto = CommentDto.CommentRequestDto.builder().replyContents("deleteTest!").createdAt(LocalDateTime.now()).build();
		Integer result = commentservice.replysave(dto, member, board.getId());
		member = null;
		//when(회원이 아닌 경우)
		CustomExceptionHandler customExceptionHandler =assertThrows(CustomExceptionHandler.class,()->{
			commentservice.replydelete(result, member);
		});
		//then
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.ONLY_USER);
	}
	
	@Test
	@DisplayName("댓글 삭제실패 - 회원이 다른 회원인 경우")
	public void replyDeleteFail2(){
		//given
		CommentDto.CommentRequestDto dto = CommentDto.CommentRequestDto.builder().replyContents("deleteTest!").createdAt(LocalDateTime.now()).build();
		Integer result = commentservice.replysave(dto, member, board.getId());
		member = memberRepository.findById(2).orElseThrow();
		//when(회원이 아닌 경우)
		
		//then
		CustomExceptionHandler customExceptionHandler =assertThrows(CustomExceptionHandler.class,()->{
			commentservice.replydelete(result, member);
		});
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.COMMENT_DELETE_DENIED);
	}
	
	@Test
	@DisplayName("댓글 수정")
	public void replyUpdate(){
		//given
		CommentDto.CommentRequestDto dto = CommentDto
				.CommentRequestDto.builder()
				.replyContents("deleteTest!")
				.createdAt(LocalDateTime.now()).build();
		Integer result = commentservice.replysave(dto, member, board.getId());

		Optional<Comment> commentDetail = commentrepository.findById(result);
		Comment getcomment = commentDetail.get();

		dto = CommentDto.CommentRequestDto.builder().replyContents("update!!").build();

		getcomment.contentsChange(dto.getReplyContents());
		
		//when
		commentservice.replyUpdate(dto, member, getcomment.getId());
		
		//then
		assertEquals(dto.getReplyContents(), getcomment.getReplyContents());
	}
	
	@Test
	@DisplayName("댓글 수정실패 - 회원이 아닌경우")
	public void replyUpdateFail1(){
		//given
		Optional<Comment> commentDetail = commentrepository.findById(2);
		
		Comment getcomment = commentDetail.get();
		
		CommentDto.CommentRequestDto dto = CommentDto.CommentRequestDto.builder().replyContents("update!!").build();
		
		getcomment.contentsChange(dto.getReplyContents());
		member = null;
		//when
		CustomExceptionHandler customExceptionHandler =assertThrows(CustomExceptionHandler.class,()->{
			commentservice.replyUpdate(dto, member, getcomment.getId());
		});
		
		//then
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.ONLY_USER);
	}
	
	@Test
	@DisplayName("댓글 수정실패 - 회원이 다른 경우")
	public void replyUpdateFail2(){
		//given
		Optional<Comment> commentDetail = commentrepository.findById(2);
		
		Comment getcomment = commentDetail.get();
		
		CommentDto.CommentRequestDto dto = CommentDto.CommentRequestDto.builder().replyContents("update!!").build();
		
		getcomment.contentsChange(dto.getReplyContents());
		
		member = memberRepository.findById(2).orElseThrow();
		
		//when
		CustomExceptionHandler customExceptionHandler =assertThrows(CustomExceptionHandler.class,()->{
			commentservice.replyUpdate(dto, member, getcomment.getId());
		});

		//then
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.COMMENT_EDITE_DENINED);
	}
}
