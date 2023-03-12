package co.kr.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import co.kr.board.domain.Board;
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

@Service
@AllArgsConstructor
public class CommentService {
	
	private final CommentRepository repository;
	
	private final BoardRepository boardrepository;
	
	/*
	 * 댓글 목록
	 * @Param id
	 * 게시글 조회화면에서 댓글 목록 출력 
	 */
	@Transactional
	public List<CommentDto.CommentResponseDto> findCommentsBoardId(@Param("id") Integer id)throws Exception{
		
		Optional<Board> detail = boardrepository.findById(id);
		
		Board board = detail.get();
		
		List<Comment>comment = repository.findCommentsBoardId(id);
		List<CommentDto.CommentResponseDto> list = new ArrayList<>();
		
		for(Comment co : comment) {
			CommentDto.CommentResponseDto dto = CommentDto
					.CommentResponseDto
					.builder()
					.comment(co)
					.build();
			
			list.add(dto);
		}
		return list;
	}
	
	/*
	 * 댓글 추가하기.
	 * @Param CommentRequestDto
	 * @Param CustomUserDetails
	 * @Exception : 댓글사용시 로그인을 하지 않은 경우 ONLY_USER
	 * @Exception : 게시판글 조회시 글이 없는 경우에는 NOT_BOARDDETAIL 
	 */
	@Transactional
	public Integer replysave(CommentDto.CommentRequestDto dto,Member principal,Integer boardId){
		
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
		
		return reply.getId();
	}
	
	/*
	 * 댓글 삭제 
	 * @Param replyId
	 */
	@Transactional
	public void replydelete(Integer replyId,Member principal){
		
		if(principal == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		
		Comment comment = repository.findById(replyId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_FOUND));
		
		String userid= principal.getUsername();
		String replywriter= comment.getReplyWriter();
	
		if(!userid.equals(replywriter)) {
			throw new CustomExceptionHandler(ErrorCode.COMMENT_DELETE_DENIED);
		}
		repository.deleteById(replyId);
	}
	
	/*
	 * 댓글 수정 기능
	 * @Param CommentRequestDto 
	 * @Param Member
	 * @Param replyId
	 * 
	 * @Exception:로그인을 안한 경우 ONLY_USER
	 * @Exception:수정시 로그인한 아이디와 댓글 작성자가 일치하지 않은 경우 COMMENT_EDITE_DENINED
	 */
	@Transactional
	public Integer replyUpdate(CommentDto.CommentRequestDto dto,Member principal,Integer replyId){
		
		if(principal == null) {
			throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
		}
		
		Comment comment = repository.findById(replyId).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_FOUND));
		
		String userid= principal.getUsername();
		
		String replywriter= comment.getReplyWriter();
		
		if(!userid.equals(replywriter)) {
			throw new CustomExceptionHandler(ErrorCode.COMMENT_EDITE_DENIED);
		}
		
		comment.contentsChange(dto.getReplyContents());
		
		return comment.getId();
	}
}
