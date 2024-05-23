package co.kr.board.controller.api;

import co.kr.board.config.exception.dto.Response;
import co.kr.board.service.HashTagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hashtag")
public class HashTagApiController {

    private final HashTagService hashTagService;

    //해시 태그 전체 목록
    @GetMapping("/list")
    public Response<?>hashTagList(){
        List<String> list = hashTagService.findAllHashTagNames();
        return new Response<>(HttpStatus.OK.value(),list);
    }


}
