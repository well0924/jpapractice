package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    //게시글 검색
    Page<BoardDto.BoardResponseDto> findByAllSearch(String searchVal, Pageable pageable);
}
