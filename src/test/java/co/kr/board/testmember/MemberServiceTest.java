package co.kr.board.testmember;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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

import co.kr.board.config.exception.dto.ErrorCode;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.LoginDto;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemberRequestDto;
import co.kr.board.login.domain.dto.TokenDto;
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
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Test
	@DisplayName("회원가입 테스트")
	public void memberjointest() throws Exception {
		//given
		Member member1 = new Member();

		member1.setUsername("sleep");
		member1.setPassword(encode.encode("qwer4149@"));
		member1.setRole(Role.ROLE_USER);
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
		System.out.println(result);
		//then
		Member membername = memberrepos.findById(result).orElseThrow();
		assertThat(membername.getUsername()).isEqualTo("sleep");
		
	}
	
	@Test
	@DisplayName("회원아이디 중복")
	public void idcheck(){
		//given
		Member member = memberrepos.findById(1).orElseThrow();
		String userid = member.getUsername();

		//when
		Boolean duplicatedresult = memberservice.checkmemberIdDuplicate(userid);
		
		//then
		assertEquals(duplicatedresult,true);
	}
	
	@Test
	@DisplayName("이메일 중복체크")
	public void emailduplicatedTest(){
		//given
		Member member = memberrepos.findById(1).orElseThrow();
		String userEmail = member.getUseremail();
		//when
		Boolean duplicatedresult = memberservice.checkmemberEmailDuplicate(userEmail);
		//then
		assertEquals(duplicatedresult,true);
	}
	
	
	@Test
	@DisplayName("회원 조회")
	public void memberdetailtest(){
		//when
		Optional<Member>detail = memberrepos.findById(1);
		Member member1 = detail.get();

		//when
		MemberDto.MemeberResponseDto result = memberservice.getMember(member1.getId());
		
		//then
		assertEquals(member1.getMembername(),result.getMembername());
	}
	
	@Test
	@DisplayName("회원목록")
	public void memberlist(){

		Pageable pageable = Pageable.ofSize(5);

		Page<Member>list = memberrepos.findAll(pageable);
		
		List<Member>content = list.getContent();
		
		int total = list.getTotalPages();
		
		List<MemberDto.MemeberResponseDto>result=memberservice.findAll();
		
		//then
		System.out.println("paging content:"+content);
		System.out.println("paging total:"+total);
		
		assertThat(result).isNotNull();
	}
	
	@Test
	@DisplayName("회원 수정")
	public void memberupdate(){
		//given
		Optional<Member>detail = memberrepos.findById(2);
		Member member1 = detail.get();
		
		String username = "well322";
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
	@DisplayName("회원 탈퇴")
	public void memberdelete() throws Exception {
		//given
		MemberDto.MemberRequestDto dto = dto();
		Integer result = memberservice.memberjoin(dto);
		Optional<Member>member = memberrepos.findById(result);

		Member detail = member.get();
		String username = detail.getUsername();
		
		//when
		memberservice.memberdelete(username);
		//then
		assertThrows(CustomExceptionHandler.class,()->{
			 Optional<Member>member1 = Optional
					 .ofNullable(
							 memberrepos
									 .findById(result)
									 .orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_USER)));
		});
	}
	
	@Test
	@DisplayName("회원 아이디 찾기")
	public void finduserid(){
		//given
		String membername="updateuser12";
		String useremail = "well84149@naver.com";
		Optional<Member>detail = memberrepos.findByMembernameAndUseremail(membername, useremail);

		Member member = detail.get();
		
		//when
		memberservice.findByMembernameAndUseremail(membername, useremail);
		
		//then
		String userid = member.getUsername();
		assertEquals("well",userid);
		
	}

	@Test
	@DisplayName("jwt 토큰 발급 테스트")
	public void jwtTokenGenerateTest(){
		
		LoginDto dto = LoginDto
				.builder()
				.username("well")
				.password("qwer4149!")
				.build();
		//회원조회
		Optional<Member>memberAccount = memberrepos.findByUsername(dto.getUsername());

		Member memberdetail = memberAccount.get();
		//비밀번호 매칭
		memberservice.passwordvalidation(memberdetail, dto);
		//토믄을 발행
		TokenDto tokenDto = jwtTokenProvider.createTokenDto(memberdetail.getUsername(), memberdetail.getRole());
		//토큰값을 decode를 해서 name과 일치하는지 보기.
		String userpk = jwtTokenProvider.getUserPK(tokenDto.getAccessToken());
		
		assertEquals(userpk, memberdetail.getUsername());
	}
	
	@Test
	@DisplayName("jwt 토큰 재발급 테스트")
	public void jwtTokenReissueTest() {

	}

	private MemberDto.MemberRequestDto dto(){
		Member detail =Member
				.builder()
				.membername("test member")
				.password(encode.encode("1234"))
				.username("test id")
				.useremail("well4149@Test.com")
				.role(Role.ROLE_USER)
				.createdAt(LocalDateTime.now())
				.build();

		return MemberRequestDto
				.builder()
				.username(detail.getUsername())
				.password(detail.getPassword())
				.role(detail.getRole())
				.membername(detail.getMembername())
				.useremail(detail.getUseremail())
				.createdAt(detail.getCreatedAt())
				.build();
	}
}
