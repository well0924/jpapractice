package co.kr.board.controller.api;

import co.kr.board.config.Exception.dto.Response;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.HashTagDto;
import co.kr.board.service.BoardService;
import co.kr.board.service.HashTagService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hashtag")
public class HashTagApiController {

    private final HashTagService hashTagService;

    private final BoardService boardService;

    //해시 태그 전체 목록
    @GetMapping("/list")
    public Response<?>hashTagList(){
        List<String> list = hashTagService.findAllHashTagNames();
        return new Response<>(HttpStatus.OK.value(),list);
    }


}
