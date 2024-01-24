package co.kr.board.controller.api;

import co.kr.board.domain.Dto.NoticeDto;
import co.kr.board.service.SSeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/notice")
@AllArgsConstructor
public class NoticeController {
    private final SSeService service;

    //알림(구독)
    @GetMapping(value = "/subscribe",produces = MediaType.ALL_VALUE)
    public SseEmitter subscribe( @RequestParam(value = "userName") String userName){
        return service.subscribe(userName);
    }
    
    //알림 목록
    @GetMapping("/list/{username}")
    public ResponseEntity<?>noticeList(@PathVariable("username")String username){
        List<NoticeDto>noticeDtoList = service.noticeList(username);
        return new ResponseEntity<>(noticeDtoList,HttpStatus.OK);
    }

}
