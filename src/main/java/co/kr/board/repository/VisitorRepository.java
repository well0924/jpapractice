package co.kr.board.repository;

import co.kr.board.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor,Integer> {
    //로그인 중복 방지
    Optional<Visitor>findByUsernameAndLoginDateTimeAfter(String username, LocalDateTime loginTime);
    //방문자 수
    @Query(value = "select distinct v.member.id from Visitor v where v.loginDateTime between :startTime and :endTime")
    Integer findDistinctUserIdsForBetween(@Param("startTime")LocalDateTime startTime,@Param("endTime")LocalDateTime endTime);
}
