package co.kr.board.controller.api;

import co.kr.board.config.Exception.dto.Response;
import co.kr.board.service.VisitorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visitor")
@AllArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    //방문자 목록(당일)
    @GetMapping("/day-count")
    public Response<?>visitorDayList(){
        Integer dayCount = visitorService.countLoginsForLastDay();
        return new Response<>(HttpStatus.OK.value(),dayCount);
    }

    //방문자 목록(일주일)
    @GetMapping("/week-count")
    public Response<?>visitorWeekList(){
        Integer weekCount = visitorService.countLoginForWeekDay();
        return new Response<>(HttpStatus.OK.value(),weekCount);
    }

    //방문자 목록(한달)

}
