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
@AllArgsConstructor
public class MemberService {
	private final MemberRepository  repository;
	private final BCryptPasswordEncoder encoder;
	
	/**
	  * 회원 목록(페이징)
	  * 회원 목록 페이징 적용
	 **/
	@Transactional
	public Page<MemberDto.MemeberResponseDto>findAll(Pageable pageable){
		Page<Member>members = repository.findAll(pageable);

		if(members.isEmpty()) {
			new CustomExceptionHandler(ErrorCode.NOT_USER);
		}

		return members.map(member -> new MemeberResponseDto(member));
	}

	/**
	 * 회원 목록(페이징 + 검색)
	 * 어드민 페이지에서 회원아이디를 검색하는 기능
	 * @param searchVal : 회원검색어
	 * @param pageable :페이징 객체                    
	**/
	@Transactional(readOnly = true)
	public Page<MemberDto.MemeberResponseDto>findByAll(String searchVal,Pageable pageable){
		return repository.findByAllSearch(searchVal,pageable);
	}

	/**
	  * 회원 정보 단일 조회
	  * 관리자 페이지 또는 회원 수정 페이지에서 회원 정보조회
	  * @param userId : 회원번호
	  * @exception CustomExceptionHandler : 회원이 없는 경우 해당 회원이 없습니다.
	 **/
	@Transactional(readOnly = true)
	public MemberDto.MemeberResponseDto getMember(Integer userId){
		Optional<MemberDto.MemeberResponseDto>result = repository
				.findByMemberDetail(userId);

		if(!result.isPresent()){
			throw new CustomExceptionHandler(ErrorCode.NOT_USER);
		}

		return result.get();
	}

	@Transactional(readOnly = true)
	public MemberDto.MemeberResponseDto memberMyPage(String userId){
		Member member = repository.findByUsername(userId)
				.orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_USER));
		return new MemeberResponseDto(member);
	}

	/*
	 * 회원가입 기능
	 * @Param MemberDto.MemberRequestDto
	 * @Exception:아이디가 중복이 되면 USERID_DUPLICATE
	 * @Exception:이메일이 중복이 되면 USEREMAIL_DUPLICATE 
	 */
	@Transactional
	public Integer memberjoin(MemberDto.MemberRequestDto dto){
		//비밀번호 암호화
		dto.setPassword(encoder.encode(dto.getPassword()));

		Member member = dtoToEntity(dto);
		repository.save(member);

		return member.getId();
	}

	/*
	 * 회원삭제(탈퇴)
	 * @Param username
	 * 회원 삭제
	 */
	@Transactional
	public void memberdelete(Integer userIdx){
		 repository.deleteById(userIdx);
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
	@Transactional(readOnly = true)
	public Boolean checkmemberIdDuplicate(String username){
		return repository.existsByUsername(username);
	}

	/*
	 * 회원이메일 중복 확인
	 * @Param username
	 * @Exception: 이메일이 중복이되면 USEREMAIL_DUPLICATE
	 * 회원가입 페이지에서 이메일 중복확인
	 */
	@Transactional(readOnly = true)
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

		Optional<MemberDto.MemeberResponseDto> member = Optional
				.ofNullable(repository
				.findByMemberNameAndUserEmail(membername, useremail)
				.orElseThrow(() -> new CustomExceptionHandler(ErrorCode.NOT_USER)));

		MemberDto.MemeberResponseDto result = member.get();

		return result.getUsername();
	}

	/*
	 * 회원 비밀번호 재수정
	 * @param useridx(회원번호)
	 * @param MemberRequestDto
	 * @Exception NOT_USER(회원이 존재하지 않습니다)
	 */
	@Transactional
	public Integer passwordchange(String username,MemberDto.MemberRequestDto dto){
		//회원조회
		Optional<Member>memberDetail = Optional.ofNullable(repository.findByUsername(username).orElseThrow(()-> new CustomExceptionHandler(ErrorCode.NOT_USER)));
		memberDetail.ifPresent(member ->{
			if(dto.getPassword()!= null) {
				member.setPassword(encoder.encode(dto.getPassword()));
			}
			repository.save(member);
		});

		return memberDetail.get().getId();
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
}
