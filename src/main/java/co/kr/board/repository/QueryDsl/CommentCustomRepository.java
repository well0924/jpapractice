package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentCustomRepository {
    //댓글 목록(관리자 페이지)
    Page<CommentDto.CommentResponseDto> findCommentList(Pageable pageable)throws Exception;
    //댓글 목록
    List<CommentDto.CommentResponseDto> findCommnentList(Integer boardId)throws Exception;
    //최근에 작성한 댓글
    List<CommentDto.CommentResponseDto> findTop5ByOrderByReplyIdCreatedAtDesc()throws Exception;
    //회원이 작성한 댓글
    Page<CommentDto.CommentResponseDto> getMyComment(String username,Pageable pageable)throws Exception;
    //댓글 선택삭제
    void deleteAllById(List<Integer> commentId);
}
