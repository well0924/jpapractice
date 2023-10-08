package co.kr.board.controller.view;

import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/page/member")
@AllArgsConstructor
public class MemberController {
    private final MemberService service;

    //회원 가입
    @GetMapping("/memberjoin")
    public ModelAndView mebmerJoin(){

        ModelAndView mv = new ModelAndView();

        mv.setViewName("login/memberjoin");

        return mv;
    }

    //회원 탈퇴
    @GetMapping("/memberdelete")
    public ModelAndView memberDelete(){

        ModelAndView mv = new ModelAndView();

        mv.setViewName("login/memberdelete");

        return mv;
    }

    //회원 수정
    @GetMapping("/memberupdate/{id}")
    public ModelAndView memberUpdate(@PathVariable("id") Integer userIdx, MemberDto.MemeberResponseDto dto){

        ModelAndView mv = new ModelAndView();

        dto = service.getMember(userIdx);

        mv.addObject("detail", dto);
        mv.setViewName("login/membermodify");

        return mv;
    }

    //어드민 페이지
    @GetMapping("/adminlist")
    public ModelAndView adminList(@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5) Pageable pageable){

        ModelAndView mv = new ModelAndView();

        Page<MemberDto.MemeberResponseDto> list= service.findAll(pageable);

        mv.addObject("memberlist", list);
        mv.setViewName("admin/adminList");

        return mv;
    }

    //회원 조회
    @GetMapping("/detail/{id}")
    public ModelAndView memberDetail(@PathVariable(value="id")Integer userIdx, MemberDto.MemeberResponseDto dto){

        ModelAndView mv = new ModelAndView();

        dto = service.getMember(userIdx);

        mv.addObject("detail", dto);
        mv.setViewName("login/membermodify");

        return mv;
    }

}
