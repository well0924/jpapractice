package co.kr.board.repository.queryDsl;

import co.kr.board.domain.Dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberCustomRepository {
    //회원 목록 검색
    Page<MemberDto.MemeberResponseDto> findByAllSearch(String searchVal, Pageable pageable);
    //회원 단일 조회하기.
    Optional<MemberDto.MemeberResponseDto>findByMemberDetail(Integer userid);
    //회원 아이디 찾기
    Optional<MemberDto.MemeberResponseDto>findByMemberNameAndUserEmail(String memberName,String userEmail);
    //아이디 중복여부 o.k
    Boolean existsByUsername(String userename);
    //이메일 중복여부 o.k
    Boolean existsByUseremail(String useremail);
}
