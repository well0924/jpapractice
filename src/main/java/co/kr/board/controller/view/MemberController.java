package co.kr.board.controller.view;

import co.kr.board.domain.Board;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.domain.SearchType;
import co.kr.board.service.BoardService;
import co.kr.board.service.CommentService;
import co.kr.board.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/page/member")
@AllArgsConstructor
public class MemberController {
    private final MemberService service;

    private final BoardService boardService;

    private final CommentService commentService;

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
    public ModelAndView adminList(@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5) Pageable pageable)throws Exception{

        ModelAndView mv = new ModelAndView();
        //회원 목록
        Page<MemberDto.MemeberResponseDto> list= service.findAll(pageable);
        //최근에 작성한 게시글
        List<BoardDto.BoardResponseDto>articleList = boardService.findBoardTop5();
        //최근에 작성한 댓글
        List<CommentDto.CommentResponseDto>commentList = commentService.commentTop5();

        mv.addObject("memberlist", list);
        mv.addObject("top5",articleList);
        mv.addObject("comment",commentList);

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

    //게시글 관리 페이지(관리자)
    @GetMapping("/board/list")
    public ModelAndView memberBoardList(@RequestParam(required = false,value = "searchVal") String searchVal,
                                        @RequestParam (required = false,value = "searchType") String searchType,
                                        @PageableDefault(sort="id",direction = Sort.Direction.DESC,size=10) Pageable pageable){
        ModelAndView mv = new ModelAndView();

        Page<BoardDto.BoardResponseDto>list = boardService.findAll(pageable);
        Page<BoardDto.BoardResponseDto>search =null;

        //검색어가 있는 경우
        if(searchVal!=null){
            search = boardService.findAllSearch(searchVal,searchType,pageable);
        }

        mv.addObject("search",search);
        mv.addObject("list",list);

        mv.setViewName("/admin/boardManagerList");

        return mv;
    }

    //댓글 관리 페이지(관리자)
    @GetMapping("/comment/list")
    public ModelAndView memberCommentList(
            @PageableDefault(sort = "id",direction = Sort.Direction.DESC,size = 10) Pageable pageable)throws Exception{

        ModelAndView mv = new ModelAndView();

        Page<CommentDto.CommentResponseDto>list = commentService.findCommentList(pageable);

        mv.addObject("list",list);
        mv.setViewName("/admin/commentManagerList");

        return mv;
    }

    //카테고리 관리 페이지(관리자)
    @GetMapping("/category/list")
    public ModelAndView memberCategoryList(){
        ModelAndView mv = new ModelAndView();

        return mv;
    }
}
