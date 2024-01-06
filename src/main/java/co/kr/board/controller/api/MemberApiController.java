package co.kr.board.controller.api;

import co.kr.board.config.Exception.dto.Response;
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

    @GetMapping("/logincheck/{id}")
    @ResponseStatus(code= HttpStatus.OK)
    public Response<Boolean> idCheck(@PathVariable(value="id") String username){

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
    //페이징
    @GetMapping("/list")
    @ResponseStatus(code=HttpStatus.OK)
    public Response<Page<MemberDto.MemeberResponseDto>>memberList(@PageableDefault(sort = "id",direction = Sort.Direction.DESC,size = 10) Pageable pageable){
        Page<MemberDto.MemeberResponseDto>list= service.findAll(pageable);
        return new Response<>(HttpStatus.OK.value(),list);
    }
    //검색
    @GetMapping("/list/search")
    @ResponseStatus(code=HttpStatus.OK)
    public Response<Page<MemberDto.MemeberResponseDto>>memberSearchList(
            @RequestParam(value = "searchVal",required = false) String searchVal,
            @PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable){

        Page<MemberDto.MemeberResponseDto>list = service.findByAll(searchVal,pageable);

        return new Response<>(HttpStatus.OK.value(),list);
    }
    //회원 조회
    @GetMapping("/detailmember/{idx}/member")
    @ResponseStatus(code=HttpStatus.OK)
    public Response<MemberDto.MemeberResponseDto>memberDetail(@PathVariable(value="idx")Integer useridx){

        MemberDto.MemeberResponseDto dto = service.getMember(useridx);

        return new Response<>(HttpStatus.OK.value(),dto);
    }
    //회원 가입
    @PostMapping("/memberjoin")
    @ResponseStatus(code=HttpStatus.OK)
    public Response<Integer>memberJoin(@Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult)throws Exception{
        int joinresult = service.memberjoin(dto);
        return new Response<>(HttpStatus.OK.value(),joinresult);
    }
    //회원 삭제
    @DeleteMapping("/memberdelete/{idx}/member")
    public Response<String>memberDelete(@PathVariable(value="idx")Integer userIdx){
        service.memberdelete(userIdx);
        return new Response<>(HttpStatus.OK.value(),"delete");
    }
    //회원 수정
    @PutMapping("/memberupdate/{idx}/member")
    public Response<Integer>memberUpdate(
            @PathVariable(value="idx")Integer useridx,
            @Valid @RequestBody MemberDto.MemberRequestDto dto, BindingResult bindingresult){
        int updateresult = service.memberupdate(useridx, dto);
        return new Response<>(HttpStatus.OK.value(),updateresult);
    }
    //회원 아이디 찾기
    @PostMapping("/userfind/{name}/{email}")
    public Response<?>userFindId(
            @PathVariable(value="name")String membername,
            @PathVariable(value="email")String useremail){
        String userid = service.findByMembernameAndUseremail(membername, useremail);
        return new Response<>(HttpStatus.OK.value(),userid);
    }
    //비밀번호 변경
    @PutMapping("/password-change/{name}")
    public Response<Integer>passwordChange(@PathVariable(value = "name")String username,
                                           @RequestBody MemberDto.MemberRequestDto dto){
        int result = service.passwordchange(username,dto);
        log.info(result);
        return new Response<>(HttpStatus.OK.value(),result);
    }
}
