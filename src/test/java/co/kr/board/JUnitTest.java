package co.kr.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.repository.MemberRepository;

@SpringBootTest
public class JUnitTest {
	
	@Autowired
	BoardRepository reposi;
	
	@Autowired
	MemberRepository memberrepos;
	
	@Autowired
	BCryptPasswordEncoder encode;
	
	@Test
	@DisplayName("회원가입 테스트")
	public void onetomanytest() {
		
		Member member1 = new Member();
		member1.setUsername("well");
		member1.setPassword(encode.encode("qwer4149@"));
		member1.setRole(Role.USER);
		member1.setMembername("user1");
		member1.setUseremail("rayman0924@naver.com");
		member1.setCreatedAt(LocalDateTime.now());
		
//		memberrepos.save(member1);	
	}
	
	@Test
	@DisplayName("회원 조회")
	public void memberdetailtest() {
		Member member1 = new Member();
		
		Optional<Member>detail = memberrepos.findById(1);
		
		member1 = detail.get();
				
		assertThat(member1);
	}
	
	@Test
	@DisplayName("회원목록")
	public void memberlist() {
		Pageable pageable = Pageable.ofSize(5);
		Page<Member>list = memberrepos.findAll(pageable);
		
		List<Member>content = list.getContent();
		
		Integer total = list.getTotalPages();
		
		assertEquals(2,content.size());
		assertEquals(1,total);
		
	}
	
	@Test
	@DisplayName("회원 수정")
	public void memberupdate() {
		
	}
	
	@Test
	@DisplayName("회원 삭제")
	public void memberdelete() {
		
	}
	
	@Test
	@DisplayName("글 목록")
	public void boardlist() {
		
	}
	
	@Test
	@DisplayName("글 조회")
	public void boarddetail() {
		
	}
	
	@Test
	@DisplayName("글 작성")
	public void boardwrite() {
		
	}
}
