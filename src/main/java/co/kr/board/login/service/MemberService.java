package co.kr.board.login.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemeberResponseDto;
import co.kr.board.login.repository.MemberRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {
	
	private final MemberRepository  repository;
	
	private final BCryptPasswordEncoder encoder;
	
	@Transactional
	public List<MemberDto.MemeberResponseDto>findAll()throws Exception{
		List<Member>memberlist = repository.findAll();

		List<MemberDto.MemeberResponseDto> list = new ArrayList<>();
		
		for(Member member : memberlist) {
			MemeberResponseDto dto = MemberDto.MemeberResponseDto
									.builder()
									.useridx(member.getUseridx())
									.userid(member.getUserid())
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
	
	//회원가입
	@Transactional
	public Integer memberjoin(MemberDto.MemberRequestDto dto)throws Exception{
		//비밀번호 암호화
		dto.setPassword(encoder.encode(dto.getPassword()));
		
		Member member = dtoToEntity(dto);
		
		repository.save(member);
		
		return member.getUseridx();
	}
	
	@Transactional
	public Boolean checkmemberEmailDuplicate(String userid)throws Exception{
		return repository.existsByUserid(userid);
	}
	
	
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
	
	
	// 회원가입 시, 유효성 체크
	@Transactional
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

}
