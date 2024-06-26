package co.kr.board.controller.api;

import co.kr.board.config.exception.dto.Response;
import co.kr.board.domain.Dto.ScrapDto;
import co.kr.board.service.ScrapService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/scrap")
@AllArgsConstructor
public class ScrapApiController {

    private final ScrapService scrapService;

    //회원이 스크랩한 목록
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/{username}")
    public Response<?>listScrap(@PathVariable("username") String username,@PageableDefault(size = 5,direction = Sort.Direction.DESC,sort = "id") Pageable pageable){
        Page<ScrapDto.ResponseDto>scrapList = scrapService.scrapList(username,pageable);
        return new Response<>(HttpStatus.OK.value(),scrapList);
    }

    //스크랩 여부
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/duplicate/{id}")
    public Response<?>hasScrap(@PathVariable("id") Integer boardId){
        boolean duplicatedResult = scrapService.ScrapDuplicated(boardId);
        log.info("중복결과::"+duplicatedResult);
        if(duplicatedResult != true){
            createScrap(boardId);
        }else{
            deleteScrap(boardId);
        }
        return new Response<>(HttpStatus.OK.value(),duplicatedResult);
    }

    //스크랩 추가
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PostMapping("/add/{id}")
    public Response<?>createScrap(@PathVariable("id")Integer boardId){
        String result = scrapService.scrapAdd(boardId);
        return new Response<>(HttpStatus.OK.value(),result);
    }

    //스크랩 삭제
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @DeleteMapping("/delete/{id}")
    public Response<?>deleteScrap(@PathVariable("id")Integer boardId){
        String result = scrapService.scrapCancel(boardId);
        return new Response<>(HttpStatus.OK.value(), result);
    }
}
