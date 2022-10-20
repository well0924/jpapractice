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

import co.kr.board.board.domain.Board;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.MemberDto;
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

		System.out.println("memberdetail:"+detail.get().toString());		

		assertThat(member1);
	}
	
	@Test
	@DisplayName("회원목록")
	public void memberlist() {

		Pageable pageable = Pageable.ofSize(5);

		Page<Member>list = memberrepos.findAll(pageable);
		
		List<Member>content = list.getContent();
		
		Integer total = list.getTotalPages();

		System.out.println("paging content:"+content);
		System.out.println("paging total:"+total);

		assertEquals(1,content.size());
		assertEquals(1,total);
		
	}
	
	@Test
	@DisplayName("회원 수정")
	public void memberupdate() {
		
		Member member1 = new Member();
		
		Optional<Member>detail = memberrepos.findById(1);
		
		member1 = detail.get();

		System.out.println("회원조회값:"+member1.toString());
		
		String username = "well";
		String password = "qwer4149!";
		String membername= "updateuser1";
		String useremail = "well4149@naver.com";
		
		member1.memberupdate(username,password,membername,useremail);
		System.out.println("회원수정값:"+member1);		
	}
	
	@Test
	@DisplayName("회원 삭제")
	public void memberdelete() {
		//memberrepos.deleteById(1);
	}
	
	@Test
	@DisplayName("회원 아이디 찾기")
	public MemberDto.MemeberResponseDto finduserid() {
		
		String membername="admin";
		String useremail = "well414965@gmail.com";
		
		Member member = null;
		
		member = memberrepos.findByUserId(membername, useremail);
		
		MemberDto.MemeberResponseDto response = MemberDto.MemeberResponseDto.builder().username(member.getUsername()).build();
		
		assertEquals("well4149",member.getUsername());
		
		return response;
	}
	
	@Test
	@DisplayName("글 목록")
	public void boardlist() {
		
		List<Board>boardlist = reposi.findAll();
		
		assertThat(boardlist);
	}
	
	@Test
	@DisplayName("글 조회")
	public void boarddetail() {
		Optional<Board> board = reposi.findById(1);
		Board detail = board.get();
		
		assertEquals("well4149", detail.getBoardAuthor());
	}
	
	@Test
	@DisplayName("글 작성")
	public void boardwrite() {
		
		Member member1 = new Member();
		
		Optional<Member>detail = memberrepos.findById(1);
		
		member1 = detail.get();

		Board board = new Board();

		board.setBoardTitle("제목");
		board.setBoardContents("내용");
		//board.setBoardAuthor("작성자");
		board.setReadCount(0);
		board.setCreatedAt(LocalDateTime.now());
		board.setWriter(member1);
		board.changeUser(member1);
		System.out.println("들어감?"+board.getWriter());
	//	reposi.save(board);
	}
	
	@Test
	@DisplayName("글 수정")
	public void boardupdate() {
		
	}
	
	@Test
	@DisplayName("글 삭제")
	public void boarddelete() {
	//	reposi.deleteById(1);
	}
}
