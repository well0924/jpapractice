package co.kr.board.controller.api;


import java.util.List;

import javax.validation.Valid;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import co.kr.board.config.Exception.dto.Response;
import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.service.CommentService;
import lombok.AllArgsConstructor;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/reply/*")
public class CommentApiController {
	
	private final CommentService service;
	private final MemberRepository memberRepository;

	//목록(어드민)
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/list")
	public Response<Page<CommentDto.CommentResponseDto>>commentList(@PageableDefault(sort="id",direction = Sort.Direction.DESC,size =10) Pageable pageable) throws Exception {
		Page<CommentDto.CommentResponseDto> list = service.findCommentList(pageable);
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//댓글목록(게시글)
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/list/{id}")
	public Response<List<CommentDto.CommentResponseDto>>getBoardComments(@PathVariable(value="id")Integer boardId)throws Exception{
		List<CommentDto.CommentResponseDto>list = service.findCommentsBoardId(boardId);
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//댓글 작성
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/write/{id}")
	public Response<?>replyWrite(@PathVariable(value="id")Integer boardId,
								 @Valid @RequestBody CommentDto.CommentRequestDto dto,
								 BindingResult bindingresult){

		Member member = getMember();
		int insertResult = service.replysave(dto, member, boardId);
		
		return new Response<>(HttpStatus.OK.value(),insertResult);
	}

	//댓글 삭제
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@DeleteMapping("/delete/{id}")
	public Response<?>replyDelete(@PathVariable(value="id")Integer replyId){

		Member mebmer = getMember();
		service.replydelete(replyId,mebmer);

		return new Response<>(HttpStatus.OK.value(),"o.k");
	}
	
	//댓글 선택 삭제
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/select-delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Response<?>commentSelectDelete(@RequestBody List<Integer>commentId){
		service.commentSelectDelete(commentId);
		return new Response<>(HttpStatus.NO_CONTENT.value(), null);
	}

	//회원이 작성한 댓글
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/my-comment/{username}")
	public Response<Page<CommentDto.CommentResponseDto>>getMyComment(@PathVariable("username")String username,@PageableDefault(size = 5,sort = "id",direction = Sort.Direction.DESC) Pageable pageable)throws Exception{
		Page<CommentDto.CommentResponseDto>commentList = service.getMyComment(username,pageable);
		return new Response<>(HttpStatus.OK.value(),commentList);
	}

	//회원 인증
	private Member getMember(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName().toString();
		return memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
	}
}