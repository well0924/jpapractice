package co.kr.board.controller.view;

import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.repository.BoardRepository;
import co.kr.board.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Controller
@AllArgsConstructor
public class TestController {
    private final BoardService boardService;

    @GetMapping("/test")
    public ModelAndView infinityScrollTest(){
        ModelAndView mv = new ModelAndView();

        mv.setViewName("/main/scrollTest");
        return mv;
    }

    @GetMapping("/scroll")
    @ResponseBody
    public ResponseEntity<Slice<BoardDto.BoardResponseDto>>list(
                                 @RequestParam(required = false,value = "page") Integer page,
                                 @RequestParam(required = false,value = "size") Integer size){
        Pageable pageable = PageRequest.of(page,size);
        log.info(page);
        log.info(size);
        log.info(pageable);
        Slice<BoardDto.BoardResponseDto>scrollList = boardService.list(pageable);
        log.info(scrollList);
        return new ResponseEntity<>(scrollList,HttpStatus.OK);
    }
}
