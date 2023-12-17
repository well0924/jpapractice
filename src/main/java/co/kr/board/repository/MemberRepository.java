package co.kr.board.repository;

import java.util.Optional;

import co.kr.board.repository.QueryDsl.MemberCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import co.kr.board.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>, MemberCustomRepository {
	//시큐리티 회원 조회 o.k
	Optional<Member>findByUsername(String username);
}
