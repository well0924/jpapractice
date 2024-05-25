package co.kr.board.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.kr.board.config.exception.dto.ErrorCode;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Member;
import co.kr.board.domain.Const.Role;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.domain.Dto.MemberDto.MemeberResponseDto;
import co.kr.board.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class MemberService {

	private final MemberRepository  repository;

	private final BCryptPasswordEncoder encoder;
	
	/**
	 * 회원 목록(페이징)
	 * @param pageable : 페이징에 필요한 페이징 객체
	 * @exception CustomExceptionHandler : 회원목록에 회원이 없는 경우 NOT_USER
	 * @return 회원목록에 필요한 객체
	 **/
	@Transactional(readOnly = true)
	public Page<MemberDto.MemeberResponseDto>getAllMembers(Pageable pageable){
		Page<Member>members = repository.findAll(pageable);

		if(members.isEmpty()) {
			throw new CustomExceptionHandler(ErrorCode.NOT_USER);
		}

		return members.map(MemeberResponseDto::new);
	}

	/**
	 * 회원 목록(페이징 + 검색)
	 * 어드민 페이지에서 회원아이디를 검색하는 기능
	 * @param searchVal : 회원검색어
	 * @param pageable :페이징 객체
	 * @return 검색된 회원의 결과 객체를 반환
	 **/
	@Transactional(readOnly = true)
	public Page<MemberDto.MemeberResponseDto>searchMembers(String searchVal,Pageable pageable){
		return repository.findByAllSearch(searchVal,pageable);
	}

	/**
	 * 회원 정보 단일 조회
	 * 관리자 페이지 또는 회원 수정 페이지에서 회원 정보조회
	 * @param userId : 회원번호
	 * @exception CustomExceptionHandler : 회원이 없는 경우 해당 회원이 없습니다.
	 * @return result : 조회된 회원객체
     **/
	@Transactional(readOnly = true)
	public MemberDto.MemeberResponseDto getMemberById(Integer userId){
		Optional<MemberDto.MemeberResponseDto>result = repository
				.findByMemberDetail(userId);

        if (result.isPresent()) {
            return result.get();
        }
        throw new CustomExceptionHandler(ErrorCode.NOT_USER);
    }

	/**
	 * 회원 정보 단일조회(마이 페이지)
	 * @param userId : 회원 아이디
	 * @exception CustomExceptionHandler : 회원이 없는 경우 해당 회원이 없습니다.
	 * @return 단일 조회된 회원 객체
	 **/
	@Transactional(readOnly = true)
	public MemberDto.MemeberResponseDto getMemberDetails(String userId){
		Member member = repository.findByUsername(userId)
				.orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_USER));
		return new MemeberResponseDto(member);
	}

	/**
	 * 회원가입 기능
	 * @param dto :회원 관련 Dto
	 * @exception CustomExceptionHandler : 아이디가 중복이 되면 USERID_DUPLICATE
	 * @exception CustomExceptionHandler : 이메일이 중복이 되면 USER_EMAIL_DUPLICATE
	 * @return id : 회원의 번호
	 **/
	public Integer createMember(MemberDto.MemberRequestDto dto){
		//비밀번호 암호화
		dto.setPassword(encoder.encode(dto.getPassword()));

		Member member = dtoToEntity(dto);
		//회원 아이디 중복여부
		if(isUsernameDuplicate(dto.getUsername())){
			throw new CustomExceptionHandler(ErrorCode.USERID_DUPLICATE);
		}
		//회원 이메일 중복여부
		if(isEmailDuplicate(dto.getUseremail())){
			throw new CustomExceptionHandler(ErrorCode.USER_EMAIL_DUPLICATE);
		}
		repository.save(member);

		return member.getId();
	}

	/**
	 * 회원삭제(탈퇴)
	 * @param userId :회원 번호
	 **/
	public void deleteMember(Integer userId){
		 repository.deleteById(userId);
	}

	/**
	 * 회원수정(시큐리티 로그인이 필요함)
	 * @param userId : 회원번호
	 * @param dto : 회원 dto
	 * @exception CustomExceptionHandler : 회원을 조회했을때 회원이 없으면 NOT_USER
	 * @return userId : 회원번호
	 **/
	public Integer updateMember(Integer userId,MemberDto.MemberRequestDto dto){

		Optional<Member>memberDetail = Optional.ofNullable(repository.findById(userId)
				.orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_USER)));

		memberDetail.ifPresent(member -> {

			if(dto.getUsername() !=null) member.setUsername(dto.getUsername());

			if(dto.getMembername() !=null) member.setMembername(dto.getMembername());

			if(dto.getPassword()!=null) member.setPassword(encoder.encode(dto.getPassword()));
			
			if(dto.getUseremail()!=null) member.setUseremail(dto.getUseremail());
			
			this.repository.save(member);
		});

		return userId;
	}

	/**
	 * 회원아이디 중복 확인
	 * @param username : 회원 아이디
	 * @exception CustomExceptionHandler: 아이디가 중복이 되면 USERID_DUPLICATE
	 * @return true : 아이디가 중복이 된 경우 (아닌 경우에는 false)
	 **/
	@Transactional(readOnly = true)
	public Boolean isUsernameDuplicate(String username){
		return repository.existsByUsername(username);
	}

	/**
	 * 회원이메일 중복 확인
	 * @param email : 회원의 이메일
	 * @exception CustomExceptionHandler: 이메일이 중복이되면 USER_EMAIL_DUPLICATE
	 * @return true : 이메일이 중복된 경우 (아닌 경우에는 false)
	 **/
	@Transactional(readOnly = true)
	public Boolean isEmailDuplicate(String email){
		return repository.existsByUseremail(email);
	}

	/**
	 * 회원아이디 찾기
	 * 로그인 페이지에서 회원이름및 이메일을 입력을 하면 회원아이디를 찾는 기능
	 * @param memberName : 회원의 이름
	 * @param userEmail : 회원의 이메일
	 * @exception CustomExceptionHandler: 회원아이디가 없는 경우 NOT_USER
	 * @return userName : 회원의 이름
	 **/
	public String findByUserId(String memberName, String userEmail){

		Optional<MemberDto.MemeberResponseDto> member = Optional
				.ofNullable(repository
				.findByMemberNameAndUserEmail(memberName, userEmail)
				.orElseThrow(() -> new CustomExceptionHandler(ErrorCode.NOT_USER)));

		MemberDto.MemeberResponseDto result = member.orElseThrow();

		return result.getUsername();
	}

	/**
	 * 회원 비밀번호 재수정
	 * @param username : 회원의 이름
	 * @param dto : 수정에 사용되는 dto
	 * @Exception NOT_USER(회원이 존재하지 않습니다)
	 * @return id : 비밀번호가 재수정된 회원의 번호
	 **/
	public Integer changeUserPassword(String username,MemberDto.MemberRequestDto dto){
		//회원조회
		Optional<Member>memberDetail = Optional.ofNullable(repository.findByUsername(username)
				.orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_USER)));

		memberDetail.ifPresent(member ->{
			if(dto.getPassword()!= null) member.setPassword(encoder.encode(dto.getPassword()));
			repository.save(member);
		});
		return memberDetail.orElseThrow().getId();
	}

	//Dto 에서 Entity 로 변환
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
}
