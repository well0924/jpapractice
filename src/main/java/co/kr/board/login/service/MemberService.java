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
import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.RefreshToken;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.AuthenticationDto;
import co.kr.board.login.domain.dto.LoginDto;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemberRequestDto;
import co.kr.board.login.domain.dto.MemberDto.MemeberResponseDto;
import co.kr.board.login.domain.dto.TokenDto;
import co.kr.board.login.domain.dto.TokenRequest;
import co.kr.board.login.domain.dto.TokenResponse;
import co.kr.board.login.repository.MemberRepository;
import co.kr.board.login.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {
	
	private final MemberRepository  repository;
	
	private final BCryptPasswordEncoder encoder;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final RefreshTokenRepository refreshTokenRepository;
	
	/*
	 * 회원 목록 
	 * 어드민으로 로그인을 했을 경우 회원 목록출력
	 * 
	 */
	@Transactional
	public List<MemberDto.MemeberResponseDto>findAll()throws Exception{
		
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
		
		Page<Member>memberlist = repository.findAll(pageable);
		
		return memberlist;
	};
	
	/*
	 * 회원 정보 단일 조회
	 * 관리자 페이지 또는 회원 수정 페이지에서 회원 정보조회
	 * @param useridx
	 * @param Exception: 회원이 없는 경우 해당 회원이 없습니다.
	 */
	@Transactional
	public MemberDto.MemeberResponseDto getMember(Integer useridx)throws Exception{
		Optional<Member>memberdetail = Optional
									.ofNullable(
									repository
									.findById(useridx)
									.orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_USER)));
		
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
	
	/*
	 * 로그인 기능 v2
	 */
//	@Transactional
//	public AuthenticationDto loginService(LoginDto logindto)throws Exception{
//		Member loginEntity = logindto.toEntity();
//		
//		Member member = repository.findByUsername(logindto.getUsername()).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_FOUND));
//		
//		if(!encoder.matches(loginEntity.getPassword(), member.getPassword())) {
//			throw new Exception("Passwords do not match");
//		}
//		
//		AuthenticationDto authen = new AuthenticationDto();
//		
//		authen.setId(member.getId());
//		authen.setMembername(member.getMembername());
//		authen.setPassword(member.getPassword());
//		authen.setUseremail(member.getUseremail());
//		authen.setUsername(member.getUsername());
//		authen.setRole(member.getRole());
//		return authen;
//	}
	
    @Transactional
    public TokenResponse signin(LoginDto dto)throws Exception{
        //회원 정보 조회
        Optional<Member> memberAccount = repository.findByUsername(dto.getUsername());
        Member memberdetail = memberAccount.get();
        //비밀번호 유효성 검사
        passwordvalidation(memberdetail,dto);
        
        //token 발행
        TokenDto tokenDto=jwtTokenProvider.createTokenDto(memberdetail.getUsername(),memberdetail.getRole());
        
        //refresh토큰 발행
        RefreshToken refreshToken = RefreshToken
                .builder()
                .key(memberdetail.getUsername())
                .value(tokenDto.getRefreshToken())
                .build();

        //refresh토큰 저장
        refreshTokenRepository.save(refreshToken);

        TokenResponse tokenResponse = TokenResponse
                .builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .expirationTime(tokenDto.getExpirationTime())
                .build();

        return tokenResponse;
    }

    //토큰 재발급
    @Transactional
    public TokenResponse reissue(TokenRequest request)throws Exception{
        //토큰 유효성 검사
       if(jwtTokenProvider.validateToken(request.getRefreshToken())){
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
        //발행 토큰에서 유저정보 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(request.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository
                .findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        //Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(request.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
        //권한 가져오기 및 토큰 생성
        String authority = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        TokenDto tokenDto = jwtTokenProvider.createTokenDto(authentication.getName(),Role.valueOf(authority));
        //토큰 업데이트
        RefreshToken  newrefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());

        //토큰 저장
        refreshTokenRepository.save(newrefreshToken);
        TokenResponse tokenResponse = new TokenResponse(tokenDto.getAccessToken(),tokenDto.getRefreshToken(), tokenDto.getExpirationTime());
        return tokenResponse;
    }

    private void validatedSignUpInfo(MemberRequestDto dto){
        //회원 가입시 이메일이 같으면 Exception
        boolean idduplicate = repository.existsByUsername(dto.getUsername());
        boolean emailduplicate = repository.existsByUseremail(dto.getUseremail());

        if(idduplicate){
            throw new CustomExceptionHandler(ErrorCode.USERID_DUPLICATE);
        }
        //회원 가입시 아이디가 같으면 Exception
        if(emailduplicate){
            throw new CustomExceptionHandler(ErrorCode.USEREMAIL_DUPLICATE);
        }
    }

    private void passwordvalidation(Member memberAccount,LoginDto dto){
        if(!encoder.matches(dto.getPassword(),memberAccount.getPassword())){
            throw new CustomExceptionHandler(ErrorCode.NOT_PASSWORD_MATCH);
        }
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
		
		//아이디가 중복된 경우 
		if(checkmemberIdDuplicate(dto.getUsername())) {
			throw new CustomExceptionHandler(ErrorCode.USERID_DUPLICATE);
		}
		//이메일이 중복된 경우
		if(checkmemberEmailDuplicate(dto.getUseremail())) {
			throw new CustomExceptionHandler(ErrorCode.USEREMAIL_DUPLICATE);
		}
		
		return member.getId();
	}
	
	/*
	 * 회원삭제(탈퇴)
	 * @Param username 
	 * 회원 삭제 
	 */
	@Transactional
	public void memberdelete(String username)throws Exception{
		 repository.deleteByUsername(username);
	}
	
	/*
	 * 회원수정 
	 * @Param useridx
	 * @Param MemberDto.MemberRequestDto
	 * 회원수정을 하는 기능 (시큐리티 로그인이 필요함)
	 */
	@Transactional
	public Integer memberupdate(Integer useridx,MemberDto.MemberRequestDto dto)throws Exception{
		
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
	public Boolean checkmemberIdDuplicate(String username)throws Exception{
		
		Boolean result = repository.existsByUsername(username);
		
		return result;
	}
	
	/*
	 * 회원이메일 중복 확인 
	 * @Param username
	 * @Exception: 이메일이 중복이되면 USEREMAIL_DUPLICATE
	 * 회원가입 페이지에서 이메일 중복확인
	 */
	@Transactional
	public Boolean checkmemberEmailDuplicate(String useremail)throws Exception{
		
		Boolean result = repository.existsByUseremail(useremail);
		
		return result;
	}
	
	/*
	 * 회원아이디 찾기
	 * @Param membername
	 * @Param useremail
	 * @Exception: 회원아이디가 없는 경우
	 * 로그인 페이지에서 회원이름및 이메일을 입력을 하면 회원아이디를 찾는 기능
	 */
	@Transactional
	public String findByMembernameAndUseremail(String membername, String useremail)throws Exception{
		Optional<Member> member = repository.findByUseremail(useremail);
		
		Member detail = member.get();
		
		String userid = detail.getUsername();
		
		return userid;
	}
	
	/*
	 * 회원 비밀번호 재수정 
	 * @param 
	 * @param 
	 */
	public MemberDto.MemeberResponseDto passwordchange(Integer useridx,MemberDto.MemberRequestDto dto)throws Exception{
		Optional<Member>memberdetail = Optional.ofNullable(repository.findById(useridx).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_USER)));
		return null;
	}
	
	//Dto에서 Entity 로 변환
	public Member dtoToEntity(MemberDto.MemberRequestDto dto) {
		
		dto.getCreatedAt();
		dto.getRole();
		
		Member member = Member
				.builder()
				.id(dto.getUseridx())
				.username(dto.getUsername())
				.password(dto.getPassword())
				.membername(dto.getMembername())
				.useremail(dto.getUseremail())
				.role(Role.USER)
				.createdAt(LocalDateTime.now())
				.build();
		
		return member;
	}
	
	//Entity 에서 Dto로 변환
//	public MemberDto.MemeberResponseDto entityToDto(Member member){
//		
//		MemberDto.MemeberResponseDto memberlist = MemberDto.MemeberResponseDto
//												.builder()
//												.useridx(member.getId())
//												.username(member.getUsername())
//												.password(member.getPassword())
//												.membername(member.getMembername())
//												.useremail(member.getUseremail())
//												.role(member.getRole())
//												.createdAt(LocalDateTime.now())
//												.build();
//		
//		return memberlist;
//	}
	
}
