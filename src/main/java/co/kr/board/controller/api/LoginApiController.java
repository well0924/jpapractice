package co.kr.board.controller.api;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import co.kr.board.config.redis.RedisService;
import co.kr.board.config.security.jwt.CookieUtile;
import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.domain.Dto.LoginDto;
import co.kr.board.domain.Dto.TokenRequest;
import co.kr.board.domain.Dto.TokenResponse;
import co.kr.board.repository.RefreshTokenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
	public Response<Boolean>idcheck(@PathVariable(value="id") String username){
				
		Boolean checkresult = service.checkmemberIdDuplicate(username);
			
		if(checkresult.equals(true)) {//아이디 중복
			return new Response<>(HttpStatus.BAD_REQUEST.value(),true);
		}else{//사용가능한 아이디
			return	new Response<>(HttpStatus.OK.value(),false);
		}
	}
	
	@GetMapping("/emailcheck/{email}")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Boolean>emailcheck(@PathVariable(value="email")@Email String useremail){
		Boolean checkresult = service.checkmemberEmailDuplicate(useremail);
		
		if(checkresult.equals(true)) {//아이디 중복
			return new Response<>(HttpStatus.BAD_REQUEST.value(),false);
		}else {//사용가능한 아이디
			return	new Response<>(HttpStatus.OK.value(),true);
		}
	}
	
	@GetMapping("/list")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<List<MemberDto.MemeberResponseDto>>memberlist(){
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
	public Response<MemberDto.MemeberResponseDto>memberdetail(@PathVariable(value="idx")Integer useridx){
		
		MemberDto.MemeberResponseDto dto = service.getMember(useridx);
				
		return new Response<>(HttpStatus.OK.value(),dto);
	}
	
	@PostMapping("/memberjoin")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Integer>memberjoin(@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
		
		int joinresult = service.memberjoin(dto);

		return new Response<>(HttpStatus.OK.value(),joinresult);
	}
	
	@DeleteMapping("/memberdelete/{idx}/member")
	public Response<String>memberdelete(@PathVariable(value="idx")String username){
		
		service.memberdelete(username);		
		
		return new Response<>(HttpStatus.OK.value(),"delete");
	}
	
	@PutMapping("/memberupdate/{idx}/member")
	public Response<Integer>memberupdate(
			@PathVariable(value="idx")Integer useridx,
			@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult){
				
		int updateresult = service.memberupdate(useridx, dto);
		
		return new Response<>(HttpStatus.OK.value(),updateresult);
	}
	
	@PostMapping("/userfind/{name}/{email}")
	public Response<?>userfindid(
			@PathVariable(value="name")String membername,
			@PathVariable(value="email")String useremail){
		
		String userid = service.findByMembernameAndUseremail(membername, useremail);
		
		return new Response<>(HttpStatus.OK.value(),userid);
	}

	@PutMapping("/passwordchange")
	public Response<Integer>passwordChange(Integer useridx,MemberDto.MemberRequestDto dto){

		int result = service.passwordchange(useridx,dto);

		return new Response<>(HttpStatus.OK.value(),result);
	}

	@PostMapping("/signup")
    public Response <TokenResponse> memberjwtlogin(@RequestBody LoginDto loginDto){
        TokenResponse tokenResponse = service.signin(loginDto);
		return new Response<>(HttpStatus.OK.value(),tokenResponse);
    }

    @PostMapping("/reissue")
    public Response<TokenResponse>jwtreissue(@RequestBody TokenRequest tokenDto){
        TokenResponse tokenResponse = service.reissue(tokenDto);
		return new Response<>(HttpStatus.OK.value(),tokenResponse);
    }
}
