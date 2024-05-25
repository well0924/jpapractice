package co.kr.board.controller.api;

import co.kr.board.config.exception.dto.Response;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@Log4j2
@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
public class MemberApiController {

    private final MemberService service;

    @GetMapping("/login-check/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Boolean>checkUsernameDuplicate(@PathVariable(value="username") String username){

        Boolean checkResult = service.isUsernameDuplicate(username);

        if(checkResult.equals(true)) {//아이디 중복
            return new Response<>(HttpStatus.BAD_REQUEST.value(),true);
        }else{//사용가능한 아이디
            return	new Response<>(HttpStatus.OK.value(),false);
        }
    }

    @GetMapping("/email-check/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Boolean>checkEmailDuplicate(@PathVariable(value="email")@Email String email){
        Boolean checkResult = service.isEmailDuplicate(email);

        if(checkResult.equals(true)) {//아이디 중복
            return new Response<>(HttpStatus.BAD_REQUEST.value(),false);
        }else {//사용가능한 아이디
            return	new Response<>(HttpStatus.OK.value(),true);
        }
    }
    //페이징
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Response<Page<MemberDto.MemeberResponseDto>>getAllMembers(@PageableDefault(sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        Page<MemberDto.MemeberResponseDto>list= service.getAllMembers(pageable);
        return new Response<>(HttpStatus.OK.value(),list);
    }
    //검색
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Response<Page<MemberDto.MemeberResponseDto>>searchMembers(
            @RequestParam(value = "searchVal",required = false) String searchVal,
            @PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable){

        Page<MemberDto.MemeberResponseDto>list = service.searchMembers(searchVal,pageable);

        return new Response<>(HttpStatus.OK.value(),list);
    }
    //회원 조회
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<MemberDto.MemeberResponseDto>getMemberById(@PathVariable(value="id")Integer memberId){

        MemberDto.MemeberResponseDto dto = service.getMemberById(memberId);

        return new Response<>(HttpStatus.OK.value(),dto);
    }

    //회원 가입
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Integer>createMember(@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult){
        int joinResult = service.createMember(dto);
        return new Response<>(HttpStatus.CREATED.value(),joinResult);
    }
    //회원 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Response<String>deleteMember(@PathVariable(value="id")Integer userId){
        service.deleteMember(userId);
        return new Response<>(HttpStatus.NO_CONTENT.value(),"delete");
    }
    //회원 수정
    @PutMapping("/{id}")
    public Response<Integer>modifyMember(
            @PathVariable(value="id")Integer userId,
            @Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult){
        int updateResult = service.updateMember(userId, dto);
        return new Response<>(HttpStatus.OK.value(),updateResult);
    }
    //회원 아이디 찾기
    @PostMapping("/user/{name}/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?>findUserId(
            @PathVariable(value="name")String memberName,
            @PathVariable(value="email")String userEmail){
        String userid = service.findByUserId(memberName, userEmail);
        return new Response<>(HttpStatus.OK.value(),userid);
    }
    //비밀번호 변경
    @PutMapping("/password-change/{name}")
    public Response<Integer>changeUserPassword(@PathVariable(value = "name")String username,
                                           @RequestBody MemberDto.MemberRequestDto dto){
        int result = service.changeUserPassword(username,dto);
        log.info(result);
        return new Response<>(HttpStatus.OK.value(),result);
    }
}
