package co.kr.board.login.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.kr.board.config.exception.dto.ErrorCode;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.config.redis.RedisService;
import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.RefreshToken;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.LoginDto;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemeberResponseDto;
import co.kr.board.login.domain.dto.TokenDto;
import co.kr.board.login.domain.dto.TokenRequest;
import co.kr.board.login.domain.dto.TokenResponse;
import co.kr.board.login.repository.MemberRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {
	private final MemberRepository  repository;
	
	private final BCryptPasswordEncoder encoder;
	
	private final JwtTokenProvider jwtTokenProvider;

	private final RedisService redisService;
	
	/*
	 * 회원 목록 
	 * 어드민으로 로그인을 했을 경우 회원 목록출력
	 * 
	 */
	@Transactional
	public List<MemberDto.MemeberResponseDto>findAll(){
		
		List<Member>memberlist = repository.findAll();

		List<MemberDto.MemeberResponseDto> list = new ArrayList<>();
		
		for(Member member : memberlist) {
			MemeberResponseDto dto = MemberDto.MemeberResponseDto
									.builder()
									.useridx(member.getId())
									.username(member.getUsername())
									.membername(member.getMembername())
									.password(member.getPassword())
									.useremail(member.getUseremail())
									.createdAt(member.getCreatedAt())
									.role(member.getRole())
									.build();
			
			list.add(dto);
		}
		return list;
	}
	
	/*
	 * 회원 목록(페이징)
	 * 
	 * 회원 목록 페이징 적용
	 */
	@Transactional
	public Page<Member>findAll(Pageable pageable){
		return repository.findAll(pageable);
	}
	
	/*
	 * 회원 정보 단일 조회
	 * 관리자 페이지 또는 회원 수정 페이지에서 회원 정보조회
	 * @param useridx
	 * @param Exception: 회원이 없는 경우 해당 회원이 없습니다.
	 */
	@Transactional
	public MemberDto.MemeberResponseDto getMember(Integer useridx){
		Optional<Member>memberdetail = Optional
									.ofNullable(
									repository
									.findById(useridx)
									.orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_USER)));

		if(!memberdetail.isPresent()){
			throw new CustomExceptionHandler(ErrorCode.NOT_USER);
		}

		Member member = memberdetail.get();

		return MemberDto.MemeberResponseDto
				.builder()
				.useridx(useridx)
				.username(member.getUsername())
				.password(member.getPassword())
				.membername(member.getMembername())
				.useremail(member.getUseremail())
				.role(member.getRole())
				.createdAt(member.getCreatedAt())
				.build();
	}
	
	//로그인 인증
    @Transactional
    public TokenResponse signin(LoginDto dto){
        //회원 정보 조회
        Optional<Member> memberAccount = repository.findByUsername(dto.getUsername());

		if(!memberAccount.isPresent()){
			throw new CustomExceptionHandler(ErrorCode.NOT_USER);
		}

        Member memberDetail = memberAccount.get();

		//비밀번호 유효성 검사
        passwordvalidation(memberDetail,dto);
        
        //token 발행
        TokenDto tokenDto=jwtTokenProvider.createTokenDto(memberDetail.getUsername(),memberDetail.getRole());
        
        //refresh토큰 발행
        RefreshToken refreshToken = RefreshToken
                .builder()
                .key(memberDetail.getUsername())
                .value(tokenDto.getRefreshToken())
                .build();

        //redis로 refresh토큰 저장
        redisService.setValues(memberDetail.getUsername(), refreshToken.getValue());

        return TokenResponse
				.builder()
				.accessToken(tokenDto.getAccessToken())
				.refreshToken(tokenDto.getRefreshToken())
				.build();
    }

    //토큰 재발급
    @Transactional
    public TokenResponse reissue(TokenRequest request){

    	//발행 토큰에서 유저정보 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(request.getRefreshToken());

        //Refresh Token 일치하는지 검사
        if (!authentication.getName().equals(request.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
        
        //권한 가져오기 및 토큰 생성
        String authority = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //redis에 refresh 토큰저장
        redisService.checkRefreshToken(authentication.getName(), request.getRefreshToken());
        //토큰 재생성
        TokenDto tokenDto = jwtTokenProvider.createTokenDto(authentication.getName(),Role.valueOf(authority));
        //토큰 발급
        return TokenResponse
				.builder()
				.accessToken(tokenDto.getAccessToken())
				.refreshToken(tokenDto.getRefreshToken())
				.build();
    }
	
	/*
	 * 회원가입 기능
	 * @Param MemberDto.MemberRequestDto
	 * @Exception:아이디가 중복이 되면 USERID_DUPLICATE
	 * @Exception:이메일이 중복이 되면 USEREMAIL_DUPLICATE 
	 */
	@Transactional
	public Integer memberjoin(MemberDto.MemberRequestDto dto)throws Exception{
		//비밀번호 암호화
		dto.setPassword(encoder.encode(dto.getPassword()));
		
		Member member = dtoToEntity(dto); 
		repository.save(member);
		//회원가입 유효성 검사(아이디 중복 & 이메일 중복)
		//validatedSignUpInfo(dto);
		
		return member.getId();
	}
	
	/*
	 * 회원삭제(탈퇴)
	 * @Param username 
	 * 회원 삭제 
	 */
	@Transactional
	public void memberdelete(String username){
		 repository.deleteByUsername(username);
	}
	
	/*
	 * 회원수정 
	 * @Param useridx
	 * @Param MemberDto.MemberRequestDto
	 * 회원수정을 하는 기능 (시큐리티 로그인이 필요함)
	 */
	@Transactional
	public Integer memberupdate(Integer useridx,MemberDto.MemberRequestDto dto){
		
		Optional<Member>memberdetail = Optional.ofNullable(repository.findById(useridx).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_USER)));
		
		memberdetail.ifPresent(member -> {
			
			if(dto.getUsername() !=null) {
				member.setUsername(dto.getUsername());
			}
			
			if(dto.getMembername() !=null) {
				member.setMembername(dto.getMembername());
			}
			
			if(dto.getPassword()!=null) {
				member.setPassword(encoder.encode(dto.getPassword()));
			}
			if(dto.getUseremail()!=null) {
				member.setUseremail(dto.getUseremail());
			}
			
			this.repository.save(member);
		});
		
		return useridx;
	}
	
	/*
	 * 회원아이디 중복 확인 
	 * @Param username
	 * @Exception: 아이디가 중복이 되면 USERID_DUPLICATE
	 * 회원가입 페이지에서 아이디 중복확인
	 */
	@Transactional
	public Boolean checkmemberIdDuplicate(String username){
		return repository.existsByUsername(username);
	}
	
	/*
	 * 회원이메일 중복 확인 
	 * @Param username
	 * @Exception: 이메일이 중복이되면 USEREMAIL_DUPLICATE
	 * 회원가입 페이지에서 이메일 중복확인
	 */
	@Transactional
	public Boolean checkmemberEmailDuplicate(String useremail){
		return repository.existsByUseremail(useremail);
	}
	
	/*
	 * 회원아이디 찾기
	 * @Param membername
	 * @Param useremail
	 * @Exception: 회원아이디가 없는 경우
	 * 로그인 페이지에서 회원이름및 이메일을 입력을 하면 회원아이디를 찾는 기능
	 */
	@Transactional
	public String findByMembernameAndUseremail(String membername, String useremail){
		Optional<Member> member = repository.findByMembernameAndUseremail(membername, useremail);
		
		Member detail = member.get();

		return detail.getUsername();
	}
	
	/*
	 * 회원 비밀번호 재수정 
	 * @param useridx(회원번호)
	 * @param MemberRequestDto
	 * @Exception NOT_USER(회원이 존재하지 않습니다)
	 */
	public Integer passwordchange(Integer useridx,MemberDto.MemberRequestDto dto){
		//회원조회
		Optional<Member>memberDetail = Optional.ofNullable(repository.findById(useridx).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_USER)));
		memberDetail.ifPresent(member ->{
			if(dto.getPassword()!= null) {
				member.setPassword(encoder.encode(dto.getPassword()));
			}
			repository.save(member);
		});

		return useridx;
	}
	
	//Dto에서 Entity 로 변환
	public Member dtoToEntity(MemberDto.MemberRequestDto dto) {
		
		return Member
				.builder()
				.id(dto.getUseridx())
				.username(dto.getUsername())
				.password(dto.getPassword())
				.membername(dto.getMembername())
				.useremail(dto.getUseremail())
				.role(Role.ROLE_USER)
				.createdAt(LocalDateTime.now())
				.build();
	}
	
	//비밀번호 유효성 검사
    public void passwordvalidation(Member memberAccount,LoginDto dto){
        if(!encoder.matches(dto.getPassword(),memberAccount.getPassword())){
            throw new CustomExceptionHandler(ErrorCode.NOT_PASSWORD_MATCH);
        }
    }

}
