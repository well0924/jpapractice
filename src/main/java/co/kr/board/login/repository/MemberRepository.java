package co.kr.board.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.kr.board.login.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	
	Optional<Member>findByUserid(String userid);
	
	
}
