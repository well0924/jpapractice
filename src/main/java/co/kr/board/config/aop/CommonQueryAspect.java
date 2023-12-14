package co.kr.board.config.aop;

import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.service.BoardService;
import co.kr.board.service.CategoryService;
import co.kr.board.service.HashTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Aspect
@Log4j2
@Component
@RequiredArgsConstructor
public class CommonQueryAspect {
    
    //Controller에서 공통적으로 적용하는 쿼리를 정리
    private final BoardService boardService;
    private final HashTagService hashTagService;
    private final CategoryService categoryService;

    //view 패키지내에 있는 컨트롤러 & ModelAndView 가 있는 모든 부분에 적용
    @Around("execution(* co.kr.board.controller.view.*Controller.*(..)) || execution(* org.springframework.web.servlet.ModelAndView.*(..))")
    public Object commonUiQuery(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Common Aop !");
        ModelAndView mv = new ModelAndView();
        //게시글 갯수
        Integer boardCount = boardService.articleCount();
        //최근에 작성한 글(5개)
        List<BoardDto.BoardResponseDto> top5 = boardService.findBoardTop5();
        //해시태그 목록
        List<String>hashTags = hashTagService.findAllHashTagNames();
        //카테고리 목록
        List<CategoryDto>categoryDtoList = categoryService.categoryList();

        mv.addObject("count",boardCount);
        mv.addObject("top5",top5);
        mv.addObject("hashTag",hashTags);
        mv.addObject("categoryMenu",categoryDtoList);

        log.info("Query::"+proceedingJoinPoint.getSignature().getName());

        return proceedingJoinPoint.proceed();
    }
}
