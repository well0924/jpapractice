package co.kr.board.login.repository;



import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import co.kr.board.login.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	//시큐리티 회원 조회
	public Optional<Member>findByUsername(String username);
	//oauth 조회
	public Optional<Member>findByUseremail(String useremail);
	//아이디 중복여부
	public Boolean existsByUsername(String userename);
	//회원 삭제
	public void deleteByUsername(String username);
	//회원목록(페이징)
	public Page<Member>findAll(Pageable pageable);
}
