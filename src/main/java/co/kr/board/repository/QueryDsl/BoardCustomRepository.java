package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardCustomRepository {
    //게시글 검색
    Page<BoardDto.BoardResponseDto> findByAllSearch(String searchVal, Pageable pageable);

    //내가 작성한 글 목록
    Page<BoardDto.BoardResponseDto>findByAllContents(String username,Pageable pageable);

    //최근에 작성한 글
    List<BoardDto.BoardResponseDto>findTop5ByOrderByBoardIdDescCreatedAtDesc();
}
