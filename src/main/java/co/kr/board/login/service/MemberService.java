package co.kr.board.login.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.kr.board.login.domain.Member;
import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.MemberDto;
import co.kr.board.login.domain.dto.MemberDto.MemeberResponseDto;
import co.kr.board.login.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
	
	@Transactional
	public Page<Member>findAll(Pageable pageable){
		
		Page<Member>memberlist = repository.findAll(pageable);
		
		return memberlist;
	};
	
	@Transactional
	public MemberDto.MemeberResponseDto getMember(Integer useridx)throws Exception{
		Optional<Member>memberdetail = Optional.ofNullable(repository.findById(useridx).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다.")));
		
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
	
	@Transactional
	public Integer memberjoin(MemberDto.MemberRequestDto dto)throws Exception{
		//비밀번호 암호화
		dto.setPassword(encoder.encode(dto.getPassword()));
		
		Member member = dtoToEntity(dto); 
		log.info("들어왔는가?:"+member);
		repository.save(member);
		
		return member.getUseridx();
	}
	
	@Transactional
	public void memberdelete(String username)throws Exception{
		 repository.deleteByUsername(username);
	}
	
	//회원정보수정
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
	
	@Transactional
	public Boolean checkmemberEmailDuplicate(String username)throws Exception{
		return repository.existsByUsername(username);
	}
	
	public Member dtoToEntity(MemberDto.MemberRequestDto dto) {
		
		dto.getCreatedAt();
		dto.getRole();
		
		Member member = Member
				.builder()
				.useridx(dto.getUseridx())
				.username(dto.getUsername())
				.password(dto.getPassword())
				.membername(dto.getMembername())
				.useremail(dto.getUseremail())
				.role(Role.ADMIN)
				.createdAt(LocalDateTime.now())
				.build();
		
		return member;
	}
	
	public MemberDto.MemeberResponseDto entityToDto(Member member){
		MemberDto.MemeberResponseDto memberlist = MemberDto.MemeberResponseDto
												.builder()
												.useridx(member.getUseridx())
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
