package co.kr.board.controller.api;

import co.kr.board.config.Security.auth.CustomUserDetails;
import co.kr.board.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notice")
@AllArgsConstructor
public class NoticeController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe",produces = "text/event-stream")
    public ResponseEntity<SseEmitter>subscribe(@AuthenticationPrincipal CustomUserDetails memberDetails,
                                               @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        return new ResponseEntity<>(notificationService.subscribe(memberDetails.getMember().getId(),lastEventId),HttpStatus.OK);
    }
}
