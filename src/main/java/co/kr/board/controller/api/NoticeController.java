package co.kr.board.controller.api;

import co.kr.board.config.Security.auth.CustomUserDetails;
import co.kr.board.service.SSeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Log4j2
@RestController
@RequestMapping("/api/notice")
@AllArgsConstructor
public class NoticeController {
    private final SSeService service;

    //알림
    @GetMapping(value = "/subscribe",produces = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return service.subscribe(customUserDetails.getMember().getId());
    }

}
