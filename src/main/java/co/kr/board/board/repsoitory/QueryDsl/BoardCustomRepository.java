package co.kr.board.board.repsoitory.QueryDsl;

import co.kr.board.board.domain.SearchCondition;
import co.kr.board.board.domain.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    public Page<BoardDto.BoardResponseDto> findByAllSearch(SearchCondition searchCondition, Pageable pageable);
}
