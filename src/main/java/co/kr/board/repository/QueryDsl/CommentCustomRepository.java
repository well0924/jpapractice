package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Dto.CommentDto;

import java.util.List;

public interface CommentCustomRepository {
    //최근에 작성한 댓글
    List<CommentDto.CommentResponseDto> findTop5ByOrderByReplyIdCreatedAtDesc();
}
