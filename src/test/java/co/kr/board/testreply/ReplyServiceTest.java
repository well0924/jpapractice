package co.kr.board.testreply;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import co.kr.board.domain.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import co.kr.board.repository.BoardRepository;
import co.kr.board.service.BoardService;
import co.kr.board.config.exception.dto.ErrorCode;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Member;
import co.kr.board.repository.MemberRepository;
import co.kr.board.service.MemberService;
import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.repository.CommentRepository;
import co.kr.board.service.CommentService;
import org.springframework.boot.test.context.SpringBootTest;

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
		commentservice.replyCreate(dto,member,board.getId());
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
			commentservice.replyCreate(dto,member,board.getId());
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
		Integer result = commentservice.replyCreate(dto, member, board.getId());
		//when
		commentservice.replyDelete(result, member);
		//then
		assertThat(count).isEqualTo(count);
		
	}
	
	@Test
	@DisplayName("댓글 삭제실패 - 회원이 아닌경우")
	public void replyDeleteFail1(){
		//given
		CommentDto.CommentRequestDto dto = CommentDto.CommentRequestDto.builder().replyContents("deleteTest!").createdAt(LocalDateTime.now()).build();
		Integer result = commentservice.replyCreate(dto, member, board.getId());
		member = null;
		//when(회원이 아닌 경우)
		CustomExceptionHandler customExceptionHandler =assertThrows(CustomExceptionHandler.class,()->{
			commentservice.replyDelete(result, member);
		});
		//then
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.ONLY_USER);
	}
	
	@Test
	@DisplayName("댓글 삭제실패 - 회원이 다른 회원인 경우")
	public void replyDeleteFail2(){
		//given
		CommentDto.CommentRequestDto dto = CommentDto.CommentRequestDto.builder().replyContents("deleteTest!").createdAt(LocalDateTime.now()).build();
		Integer result = commentservice.replyCreate(dto, member, board.getId());

		member = memberRepository.findById(2).orElseThrow();
		//when(회원이 아닌 경우)
		//then
		CustomExceptionHandler customExceptionHandler =assertThrows(CustomExceptionHandler.class,()->{
			commentservice.replyDelete(result, member);
		});
		assertEquals(customExceptionHandler.getErrorCode(), ErrorCode.COMMENT_DELETE_DENIED);
	}
}
