package co.kr.board.controller.api;


import java.util.List;

import javax.validation.Valid;

import co.kr.board.config.exception.dto.ErrorCode;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Member;
import co.kr.board.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import co.kr.board.config.exception.dto.Response;
import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.service.CommentService;
import lombok.AllArgsConstructor;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/reply")
public class CommentApiController {
	
	private final CommentService service;

	private final MemberRepository memberRepository;

	//목록(어드민)
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/")
	public Response<Page<CommentDto.CommentResponseDto>>listComment(@PageableDefault(sort="id",direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
		Page<CommentDto.CommentResponseDto> list = service.findCommentList(pageable);
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//댓글목록(게시글)
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/{id}")
	public Response<List<CommentDto.CommentResponseDto>>findComment(@PathVariable(value="id")Integer boardId)throws Exception{
		List<CommentDto.CommentResponseDto>list = service.findCommentsBoardId(boardId);
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//댓글 작성
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Response<?>createComment(@PathVariable(value="id")Integer boardId,
								 @Valid @RequestBody CommentDto.CommentRequestDto dto){

		Member member = getMember();
		int insertResult = service.createComment(dto, member, boardId);
		
		return new Response<>(HttpStatus.CREATED.value(),insertResult);
	}

	//댓글 삭제
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Response<?>deleteComment(@PathVariable(value="id")Integer replyId){

		Member member = getMember();
		service.deleteComment(replyId,member);

		return new Response<>(HttpStatus.NO_CONTENT.value(),"o.k");
	}
	
	//댓글 선택 삭제
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/select-delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Response<?>selectDeleteComments(@RequestBody List<Integer>commentId){
		service.selectDeleteComment(commentId);
		return new Response<>(HttpStatus.NO_CONTENT.value(), null);
	}

	//회원이 작성한 댓글
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/my-comment/{username}")
	@ResponseStatus(HttpStatus.OK)
	public Response<Page<CommentDto.CommentResponseDto>>getCommentByUserName(@PathVariable("username")String username,@PageableDefault(size = 5,sort = "id",direction = Sort.Direction.DESC) Pageable pageable)throws Exception{
		Page<CommentDto.CommentResponseDto>commentList = service.getMyComment(username,pageable);
		return new Response<>(HttpStatus.OK.value(),commentList);
	}

	//회원 인증
	private Member getMember(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
	}
}