package co.kr.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import co.kr.board.domain.Board;
import co.kr.board.domain.Const.NoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import co.kr.board.repository.BoardRepository;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Member;
import co.kr.board.domain.Comment;
import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommentService {
	
	private final CommentRepository repository;
	
	private final BoardRepository boardrepository;

	private final SSeService sSeService;

	/**
	  * 댓글 목록 (페이징)
	  * @param pageable : 페이징 객체
	  * 어드민 페이지에서 댓글 목록을 페이징으로 출력
	 **/
	@Transactional(readOnly = true)
	public Page<CommentDto.CommentResponseDto>findCommentList(Pageable pageable)throws Exception{
		return repository.findCommentList(pageable);
	}

	/**
	  * 댓글 목록
	  * @param id : 게시글 번호
	  * 게시글 조회화면에서 댓글 목록 출력 
	 **/
	@Transactional(readOnly = true)
	public List<CommentDto.CommentResponseDto> findCommentsBoardId(@Param("id") Integer id)throws Exception{
		Optional<Board> detail = boardrepository.findById(id);
		List<CommentDto.CommentResponseDto> list = new ArrayList<>();
		if(detail.isPresent()){
			list = repository.findCommnentList(id);
		}
		return list;
	}
	
	/**
	  * 댓글 추가하기.
	  * @param dto : 댓글 요청 Dto
	  * @param boardId : 게시글 번호
	  * @param principal : 회원 객체
	  * @exception CustomExceptionHandler : 댓글사용시 로그인을 하지 않은 경우 ONLY_USER
	  * @exception CustomExceptionHandler : 게시판글 조회시 글이 없는 경우에는 NOT_BOARD_DETAIL
	 **/
	@Transactional
	public Integer replyCreate(CommentDto.CommentRequestDto dto,Member principal,Integer boardId){
		
		//유저가 아니면 사용불가
		if(principal == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		
		//게시판에서 글 조회 -> 글이 없으면 Exception
		Board board = boardrepository.findById(boardId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL));

		Comment reply = Comment.builder()
				.board(board)
				.member(principal)
				.replyWriter(principal.getUsername())
				.replyContents(dto.getReplyContents())
				.createdAt(dto.getCreatedAt())
				.build();

		repository.save(reply);

		board.getCommentlist().add(reply);
		Member writer = board.getWriter();
		//댓글 알림
		sSeService.send(writer, NoticeType.REPLY,"게시글에 댓글이 달렸습니다.",String.valueOf(board.getId()));

		return reply.getId();
	}
	
	/**
	  * 댓글 삭제 
	  * @param replyId : 댓글 번호
	  * @param principal : 회원 인증객체
	  * @exception CustomExceptionHandler : ONLY_USER (회원만 사용가능)                     
	 **/
	@Transactional
	public void replyDelete(Integer replyId,Member principal){
		
		if(principal == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		
		Comment comment = repository.findById(replyId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_FOUND));
		
		String userid= principal.getUsername();
		String replyWriter= comment.getReplyWriter();
	
		if(!userid.equals(replyWriter)) {
			throw new CustomExceptionHandler(ErrorCode.COMMENT_DELETE_DENIED);
		}

		repository.deleteById(replyId);
	}

	/**
	  * 최근에 작성한 댓글 5개 출력하기.
	 **/
	@Transactional(readOnly = true)
	public List<CommentDto.CommentResponseDto>commentTop5() throws Exception {
		return repository.findTop5ByOrderByReplyIdCreatedAtDesc();
	}
	
	/**
	  * 댓글 선택 삭제
	  * @param commentId: 선택된 댓글 번호(List)
	 **/
	public void commentSelectDelete(List<Integer>commentId){
		IntStream.range(0,commentId.size())
				.mapToObj(i->commentId)
				.forEach(repository::deleteAllById);
	}

	/**
	  * 회원이 작성한 댓글 목록
	  * @param username : 회원 아이디
	  * @param pageable : 페이징 객체                    
	 **/
	@Transactional(readOnly = true)
	public Page<CommentDto.CommentResponseDto>getMyComment(String username,Pageable pageable) throws Exception {
		return repository.getMyComment(username,pageable);
	}
}
