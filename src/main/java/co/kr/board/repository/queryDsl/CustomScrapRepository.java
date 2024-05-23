package co.kr.board.repository.queryDsl;

import co.kr.board.domain.Dto.ScrapDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomScrapRepository {
    //스크랩 목록
    Page<ScrapDto.ResponseDto>ScrapList(String username,Pageable pageable);
}
