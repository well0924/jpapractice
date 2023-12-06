package co.kr.board.repository;

import java.util.Optional;

import co.kr.board.repository.QueryDsl.MemberCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import co.kr.board.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>, MemberCustomRepository {
	//시큐리티 회원 조회 o.k
	Optional<Member>findByUsername(String username);
	//아이디 중복여부 o.k
 	Boolean existsByUsername(String userename);
 	//이메일 중복여부 o.k
 	Boolean existsByUseremail(String useremail);
	//회원목록(페이징) o.k
	Page<Member>findAll(Pageable pageable);
}
