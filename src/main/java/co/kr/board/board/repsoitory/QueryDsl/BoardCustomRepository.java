package co.kr.board.board.repsoitory.QueryDsl;

import co.kr.board.board.domain.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    //게시글 검색
    Page<BoardDto.BoardResponseDto> findByAllSearch(String searchVal, Pageable pageable);
}
