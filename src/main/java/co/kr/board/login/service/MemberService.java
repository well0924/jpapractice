package co.kr.board.login.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.repository.MemberRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {
	
	private final MemberRepository  repository;
	
	private final BCryptPasswordEncoder encoder;
	
	//회원가입
	@Transactional
	public int memberjoin(MemberDto.MemberRequestDto dto)throws Exception{
		//비밀번호 암호화
		dto.setPassword(encoder.encode(dto.getPassword()));
		
		Member member = dtoToEntity(dto);
		
		repository.save(member);
		
		return member.getUseridx();
	}
	
	//아이디 중복기능
	@Transactional
	public Boolean checkmemberEmailDuplicate(String userid)throws Exception{
		return repository.existsByUserid(userid);
	}
	
	//dto->entity
	public Member dtoToEntity(MemberDto.MemberRequestDto dto) {
		
		Member member = Member
				.builder()
				.userid(dto.getUserid())
				.useridx(dto.getUseridx())
				.membername(dto.getMembername())
				.password(dto.getPassword())
				.useremail(dto.getUseremail()).role(dto.getRole().USER)
				.createdAt(dto.getCreatedAt().now())
				.build();
		
		return member;
	}	
	
	//entity->dto
	public MemberDto.MemeberResponseDto entityToDto(Member member){
		
		MemberDto.MemeberResponseDto memberlist = MemberDto.MemeberResponseDto
													.builder()
													.useridx(member.getUseridx())
													.userid(member.getUserid())
													.membername(member.getMembername())
													.password(member.getPassword())
													.useremail(member.getUseremail())
													.createdAt(member.getCreatedAt())
													.build();
		return memberlist;
	}
}
