package co.kr.board.testmember;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import co.kr.board.config.exception.dto.ErrorCode;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemberRequestDto;
import co.kr.board.login.repository.MemberRepository;
import co.kr.board.login.service.MemberService;

@SpringBootTest
public class MemberServiceTest {
	
	@Autowired
	MemberRepository memberrepos;
	
	@Autowired
	MemberService memberservice;
	
	@Autowired
	BCryptPasswordEncoder encode;
	
	@Test
	@DisplayName("회원가입 테스트")
	public void memberjointest() throws Exception {
		//given
		Member member1 = new Member();

		member1.setUsername("sleep");
		member1.setPassword(encode.encode("qwer4149@"));
		member1.setRole(Role.USER);
		member1.setMembername("user2");
		member1.setUseremail("springboot0924@gmail.com");
		member1.setCreatedAt(LocalDateTime.now());
		
		MemberDto.MemberRequestDto dto = MemberRequestDto
				.builder()
				.username(member1.getUsername())
				.password(member1.getPassword())
				.role(member1.getRole())
				.membername(member1.getMembername())
				.useremail(member1.getUseremail())
				.createdAt(member1.getCreatedAt())
				.build();
		//when
		Integer result = memberservice.memberjoin(dto);
		
		//then
		Member membername = memberrepos.findByUseremail(dto.getUseremail()).orElse(null);
		assertThat(membername.getUsername()).isEqualTo("sleep");
		
	}
	
	@Test
	@DisplayName("회원아이디 중복")
	public void idcheck() throws Exception {
		//given
		Member member = memberrepos.findById(1).orElseThrow();
		String userid = member.getUsername();
		
		Member member1 = new Member();

		member1.setUsername("well");
		member1.setPassword(encode.encode("qwer4149@"));
		member1.setRole(Role.USER);
		member1.setMembername("user3");
		member1.setUseremail("springboot0924@gmail.com");
		member1.setCreatedAt(LocalDateTime.now());
		
		MemberDto.MemberRequestDto dto = MemberRequestDto
				.builder()
				.username(member1.getUsername())
				.password(member1.getPassword())
				.role(member1.getRole())
				.membername(member1.getMembername())
				.useremail(member1.getUseremail())
				.createdAt(member1.getCreatedAt())
				.build();
		//when
		Integer result = memberservice.memberjoin(dto);

		//when
		Boolean duplicatedresult = memberservice.checkmemberIdDuplicate(userid);
		
		assertThat(duplicatedresult);
		assertEquals(dto.getUsername(), member.getUsername());
	}
	
	@Test
	@DisplayName("회원 조회")
	public void memberdetailtest() throws Exception {
		//when
		Member member1 = new Member();
		Optional<Member>detail = memberrepos.findById(1);
		member1 = detail.get();
		
		//when
		MemberDto.MemeberResponseDto result = memberservice.getMember(member1.getId());
		
		//then
		assertEquals(member1.getMembername(),result.getMembername());
	}
	
	@Test
	@DisplayName("회원목록")
	public void memberlist() throws Exception {

		Pageable pageable = Pageable.ofSize(5);

		Page<Member>list = memberrepos.findAll(pageable);
		
		List<Member>content = list.getContent();
		
		Integer total = list.getTotalPages();
		
		List<MemberDto.MemeberResponseDto>result=memberservice.findAll();
		
		//then
		System.out.println("paging content:"+content);
		System.out.println("paging total:"+total);
		assertThat(result);
	}
	
	@Test
	@DisplayName("회원 수정")
	public void memberupdate() throws Exception {
		//given
		Member member1 = new Member();
		
		Optional<Member>detail = memberrepos.findById(1);
		
		member1 = detail.get();
		
		String username = "well";
		String password = "qwer4149!";
		String membername= "updateuser1";
		String useremail = "well4149@naver.com";
		
		MemberDto.MemberRequestDto dto = MemberRequestDto
				.builder()
				.username(username)
				.password(password)
				.membername(membername)
				.useremail(useremail)
				.build();
		
		//when
		Integer result = memberservice.memberupdate(member1.getId(),dto);
		
		//then
		member1 = memberrepos.findById(result).orElseThrow();
		
		assertEquals(dto.getMembername(),member1.getMembername());
		assertEquals(dto.getUsername(), member1.getUsername());
		assertEquals(dto.getUseremail(),member1.getUseremail());
		
	}
	
	@Test
	@DisplayName("회원수정 실패-없는 회원번호")
	public void memberupdatefail() {
		MemberDto.MemberRequestDto dto = MemberRequestDto
				.builder()
				.username("scssc")
				.password("Scscscsc")
				.membername("sssss")
				.useremail("acacacd")
				.build();
		
		try {
			memberservice.memberupdate(10,dto);
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("회원 삭제")
	public void memberdelete() throws Exception {
		//given
		Member detail = memberrepos.findById(1).orElseThrow();
		
		//when
		//memberservice.memberdelete(detail.getUsername());
		
		//then
//		assertThrows(CustomExceptionHandler.class,()->{
//			memberservice.memberdelete(detail.getUsername());
//		});
	}
	
	@Test
	@DisplayName("회원 아이디 찾기")
	public void finduserid() throws Exception {
		//given
		String membername="user1";
		String useremail = "rayman0924@naver.com";
		Optional<Member>detail = Optional.ofNullable(memberrepos.findByMembernameAndUseremail(membername, useremail));
		Member member = detail.get();
		
		//when
		memberservice.findByMembernameAndUseremail(membername, useremail);
		
		//then
		String userid = (String)member.getUsername();
		assertEquals("well",userid);
		
	}
	
	@Test
	@DisplayName("비밀번호 재설정")
	public void passwordreset() {
		
	}
	
}
