package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
    //회원 목록 검색
    Page<MemberDto.MemeberResponseDto> findByAllSearch(String searchVal, Pageable pageable);
}
