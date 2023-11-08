package co.kr.board.controller.view;

import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.domain.Dto.ScrapDto;
import co.kr.board.service.BoardService;
import co.kr.board.service.CommentService;
import co.kr.board.service.ScrapService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/page/mypage")
@AllArgsConstructor
public class MyPageController {

    private final ScrapService scrapService;
    private final BoardService boardService;
    private final CommentService commentService;

    //마이페이지 스크랩 목록
    @GetMapping("/list/{username}")
    public ModelAndView myPageList(@PageableDefault(size = 10,direction = Sort.Direction.DESC,sort = "id") Pageable pageable,@PathVariable("username") String username){
        ModelAndView mv = new ModelAndView();
        //스크랩 한 게시글 목록
        Page<ScrapDto.ResponseDto> scrapList = scrapService.scrapList(username,pageable);
        //최근에 작성한 글(5개)
        List<BoardDto.BoardResponseDto> top5 = boardService.findBoardTop5();

        mv.addObject("scrapList",scrapList);
        mv.addObject("top5",top5);
        mv.setViewName("/mypage/mypage");

        return mv;
    }

    //회원이 작성한 글
    @GetMapping("/my-article/{username}")
    public ModelAndView myBoard(@PathVariable("username") String username,@PageableDefault(sort = "id",direction = Sort.Direction.DESC,size = 5) Pageable pageable){
        ModelAndView mv = new ModelAndView();
        //회원이 작성한글
        Page<BoardDto.BoardResponseDto>list = boardService.memberArticle(username,pageable);
        //스크랩 한 게시글 목록
        Page<ScrapDto.ResponseDto> scrapList = scrapService.scrapList(username,pageable);
        //최근에 작성한 글(5개)
        List<BoardDto.BoardResponseDto> top5 = boardService.findBoardTop5();

        mv.addObject("list",list);
        mv.addObject("scrapList",scrapList);
        mv.addObject("top5",top5);

        mv.setViewName("/mypage/myarticle");

        return mv;
    }

    //회원이 작성한 댓글
    @GetMapping("/my-comment/{username}")
    public ModelAndView myComment(@PathVariable("username") String username,@PageableDefault(sort = "id",direction = Sort.Direction.DESC,size = 5) Pageable pageable) throws Exception {
        ModelAndView mv = new ModelAndView();
        //회원이 작성한 댓글
        Page<CommentDto.CommentResponseDto>list = commentService.getMyComment(username,pageable);
        //스크랩 한 게시글 목록
        Page<ScrapDto.ResponseDto> scrapList = scrapService.scrapList(username,pageable);
        //최근에 작성한 글(5개)
        List<BoardDto.BoardResponseDto> top5 = boardService.findBoardTop5();

        mv.addObject("list",list);
        mv.addObject("scrapList",scrapList);
        mv.addObject("top5",top5);

        mv.setViewName("/mypage/mycomment");

        return mv;
    }
}