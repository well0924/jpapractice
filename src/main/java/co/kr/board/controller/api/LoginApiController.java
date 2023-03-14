package co.kr.board.controller.api;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import co.kr.board.domain.Dto.LoginDto;
import co.kr.board.domain.Dto.TokenRequest;
import co.kr.board.domain.Dto.TokenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import co.kr.board.config.Exception.dto.Response;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/login/*")
public class LoginApiController {
	private final MemberService service;

	@GetMapping("/logincheck/{id}")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Boolean>idCheck(@PathVariable(value="id") String username){
				
		Boolean checkresult = service.checkmemberIdDuplicate(username);
			
		if(checkresult.equals(true)) {//아이디 중복
			return new Response<>(HttpStatus.BAD_REQUEST.value(),true);
		}else{//사용가능한 아이디
			return	new Response<>(HttpStatus.OK.value(),false);
		}
	}
	
	@GetMapping("/emailcheck/{email}")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Boolean>emailCheck(@PathVariable(value="email")@Email String useremail){
		Boolean checkresult = service.checkmemberEmailDuplicate(useremail);
		
		if(checkresult.equals(true)) {//아이디 중복
			return new Response<>(HttpStatus.BAD_REQUEST.value(),false);
		}else {//사용가능한 아이디
			return	new Response<>(HttpStatus.OK.value(),true);
		}
	}
	
	@GetMapping("/list")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<List<MemberDto.MemeberResponseDto>>memberList(){
		List<MemberDto.MemeberResponseDto>list = service.findAll();
		
		return new Response<>(HttpStatus.OK.value(),list);
	}
	@GetMapping("/list/search")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Page<MemberDto.MemeberResponseDto>>memberSearchList(
			@RequestParam(value = "searchVal",required = false) String searchVal,
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable){

		Page<MemberDto.MemeberResponseDto>list = service.findByAll(searchVal,pageable);

		return new Response<>(HttpStatus.OK.value(),list);
	}
	@GetMapping("/detailmember/{idx}/member")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<MemberDto.MemeberResponseDto>memberDetail(@PathVariable(value="idx")Integer useridx){
		
		MemberDto.MemeberResponseDto dto = service.getMember(useridx);
				
		return new Response<>(HttpStatus.OK.value(),dto);
	}
	
	@PostMapping("/memberjoin")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Integer>memberJoin(@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
		int joinresult = service.memberjoin(dto);
		return new Response<>(HttpStatus.OK.value(),joinresult);
	}
	
	@DeleteMapping("/memberdelete/{idx}/member")
	public Response<String>memberDelete(@PathVariable(value="idx")String username){
		service.memberdelete(username);
		return new Response<>(HttpStatus.OK.value(),"delete");
	}
	
	@PutMapping("/memberupdate/{idx}/member")
	public Response<Integer>memberUpdate(
			@PathVariable(value="idx")Integer useridx,
			@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult){
		int updateresult = service.memberupdate(useridx, dto);
		return new Response<>(HttpStatus.OK.value(),updateresult);
	}
	
	@PostMapping("/userfind/{name}/{email}")
	public Response<?>userFindId(
			@PathVariable(value="name")String membername,
			@PathVariable(value="email")String useremail){
		String userid = service.findByMembernameAndUseremail(membername, useremail);
		return new Response<>(HttpStatus.OK.value(),userid);
	}

	@PutMapping("/passwordchange/{name}")
	public Response<Integer>passwordChange(@PathVariable(value = "name")String username,
										   @RequestBody MemberDto.MemberRequestDto dto){
		int result = service.passwordchange(username,dto);
		return new Response<>(HttpStatus.OK.value(),result);
	}

	@PostMapping("/signup")
    public Response <TokenResponse> memberJwtLogin(@Valid @RequestBody LoginDto loginDto){
        TokenResponse tokenResponse = service.signin(loginDto);
		return new Response<>(HttpStatus.OK.value(),tokenResponse);
    }

    @PostMapping("/reissue")
    public Response<TokenResponse>jwtReissue(@Valid @RequestBody TokenRequest tokenDto){
        TokenResponse tokenResponse = service.reissue(tokenDto);
		return new Response<>(HttpStatus.OK.value(),tokenResponse);
    }
}
