package co.kr.board.testmember;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import co.kr.board.config.redis.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.domain.Member;
import co.kr.board.domain.Role;
import co.kr.board.domain.Dto.LoginDto;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.domain.Dto.MemberDto.MemberRequestDto;
import co.kr.board.domain.Dto.TokenDto;
import co.kr.board.repository.MemberRepository;
import co.kr.board.service.MemberService;

@SpringBootTest
public class MemberServiceTest {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	MemberService memberservice;
	@Autowired
	BCryptPasswordEncoder encode;
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	private RedisService redisService;
	@Autowired
	private RedisTemplate redisTemplate;

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
		Member membername = memberRepository.findById(result).orElseThrow();
		assertThat(membername.getUsername()).isEqualTo("sleep");
		
	}
	
	@Test
	@DisplayName("회원아이디 중복")
	public void idcheck(){
		//given
		Member member = memberRepository.findById(1).orElseThrow();
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
		Member member = memberRepository.findById(1).orElseThrow();
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
		Optional<Member>detail = memberRepository.findById(1);
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

		Page<Member>list = memberRepository.findAll(pageable);
		
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
		Optional<Member>detail = memberRepository.findById(2);
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
		member1 = memberRepository.findById(result).orElseThrow();
		
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
		Optional<Member>member = memberRepository.findById(result);

		Member detail = member.get();
		String username = detail.getUsername();
		
		//when
		memberservice.memberdelete(username);
		//then
		assertThrows(CustomExceptionHandler.class,()->{
			 Optional<Member>member1 = Optional
					 .ofNullable(
							 memberRepository
									 .findById(result)
									 .orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_USER)));
		});
	}
	
	@Test
	@DisplayName("회원 아이디 찾기")
	public void finduserid(){
		//given
		String membername="updateuser1";
		String useremail = "well4149@naver.com";
		Optional<Member>detail = memberRepository.findByMembernameAndUseremail(membername, useremail);

		Member member = detail.get();
		
		//when
		memberservice.findByMembernameAndUseremail(membername, useremail);
		
		//then
		String userid = member.getUsername();
		assertEquals("well322",userid);
		
	}

	@Test
	@DisplayName("jwt 토큰 발급 테스트")
	public void jwtTokenGenerateTest(){
		
		LoginDto dto = loginDto();
		//회원조회
		Optional<Member>memberAccount = memberRepository.findByUsername(dto.getUsername());

		Member memberdetail = memberAccount.get();
		//비밀번호 매칭
		memberservice.passwordvalidation(memberdetail, dto);
		//토큰을 발행
		TokenDto tokenDto = jwtTokenProvider.createTokenDto(memberdetail.getUsername(), memberdetail.getRole());
		//토큰값을 decode를 해서 name과 일치하는지 보기.
		String userpk = jwtTokenProvider.getUserPK(tokenDto.getAccessToken());
		
		assertEquals(userpk, memberdetail.getUsername());
	}

	@Test
	@DisplayName("jwt 토큰 재발급 테스트")
	public void jwtTokenReissueTest() {
		LoginDto dto = loginDto();
		//회원조회
		Optional<Member>memberAccount = memberRepository.findByUsername(dto.getUsername());

		Member memberDetail = memberAccount.get();
		//비밀번호 매칭
		memberservice.passwordvalidation(memberDetail, dto);
		//토큰을 발행
		TokenDto tokenDto = jwtTokenProvider.createTokenDto(memberDetail.getUsername(), memberDetail.getRole());

		//토큰에서 유저정보 가져오기
		Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getRefreshToken());

		//권한 가져오기
		String authority = authentication
				.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		//토큰 refresh
		redisService.checkRefreshToken(authentication.getName(),tokenDto.getRefreshToken());

		String result = jwtTokenProvider.getUserPK(tokenDto.getRefreshToken());

		tokenDto = jwtTokenProvider.createTokenDto(authentication.getName(),Role.valueOf(authority));

		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(result,tokenDto.getRefreshToken());

		assertEquals(valueOperations.get(result),tokenDto.getRefreshToken());
	}
	@Test
	@DisplayName("비밀번호 재수정 테스트")
	public void passwordChangeTest()throws Exception{
		//given (회원가입후 조회)
		MemberDto.MemberRequestDto memberRequestDto = dto();
		memberservice.memberjoin(memberRequestDto);
		Optional<Member>member = Optional.ofNullable(memberRepository.findByUsername(memberRequestDto.getUsername()).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_USER)));
		memberservice.getMember(member.get().getId());

		//when (비밀번호 수정)
		memberRequestDto = MemberDto.MemberRequestDto.builder().password("4567").build();
		memberservice.passwordchange(member.get().getId(),memberRequestDto);

		//then
		assertThat(encode.matches("4567",member.get().getPassword()));
		memberservice.memberdelete(member.get().getUsername());
	}
	
	//회원가입 dto
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

	private LoginDto loginDto(){
		return LoginDto
				.builder()
				.username("well322")
				.password("qwer4149!")
				.build();
	}
}
