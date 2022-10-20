package co.kr.board.login.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemeberResponseDto;
import co.kr.board.login.repository.MemberRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {
	
	private final MemberRepository  repository;
	
	private final BCryptPasswordEncoder encoder;
	
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
									.orElseThrow(()->new IllegalArgumentException("해당 회원이 없습니다.")));
		
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
	 * 회원가입 기능
	 * @Param MemberDto.MemberRequestDto
	 * 
	 */
	@Transactional
	public Integer memberjoin(MemberDto.MemberRequestDto dto)throws Exception{
		//비밀번호 암호화
		dto.setPassword(encoder.encode(dto.getPassword()));
		
		Member member = dtoToEntity(dto); 
		repository.save(member);
		
		return member.getId();
	}
	
	/*
	 * 회원삭제(탈퇴)
	 * @Param username 
	 * 회원 삭제 (로그인이 필요함) 
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
		
		Optional<Member>memberdetail = Optional.ofNullable(repository.findById(useridx).orElseThrow(()-> new IllegalArgumentException("조회된 회원이 없습니다.")));
		
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
	 * 회원가입 페이지에서 아이디 중복확인
	 */
	@Transactional
	public Boolean checkmemberIdDuplicate(String username)throws Exception{
		return repository.existsByUsername(username);
	}
	
	/*
	 * 회원아이디 찾기
	 * @Param membername
	 * @Param useremail
	 * 로그인 페이지에서 회원이름및 이메일을 입력을 하면 회원아이디를 찾는 기능
	 */
	@Transactional
	public MemberDto.MemeberResponseDto findByUserId(@Param("name")String membername,@Param("email")String useremail)throws Exception{
		Member member = repository.findByUserId(membername, useremail);
		
		return MemberDto.MemeberResponseDto
				.builder()
				.membername(member.getMembername())
				.build();
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
				.role(Role.ADMIN)
				.createdAt(LocalDateTime.now())
				.build();
		
		return member;
	}
	
	//Entity 에서 Dto로 변환
	public MemberDto.MemeberResponseDto entityToDto(Member member){
		MemberDto.MemeberResponseDto memberlist = MemberDto.MemeberResponseDto
												.builder()
												.useridx(member.getId())
												.username(member.getUsername())
												.password(member.getPassword())
												.membername(member.getMembername())
												.useremail(member.getUseremail())
												.role(member.getRole())
												.createdAt(LocalDateTime.now())
												.build();
		return memberlist;
	}
	
}
