package co.kr.board.controller.api;

import co.kr.board.config.Exception.dto.Response;
import co.kr.board.domain.Visitor;
import co.kr.board.service.VisitorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitor")
@AllArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    //방문자 목록(당일)
    @GetMapping("/day-count")
    public Response<?>visitorDayList(){
        Map<String,Object>response = new HashMap<>();
        List<LocalDateTime>labels = new ArrayList<>();
        List<Integer>visitors = new ArrayList<>();

        List<Visitor> dayCount = visitorService.countLoginsForLastDay();

        for(Visitor visitor : dayCount){
            labels.add(visitor.getLoginDateTime());
            visitors.add(visitor.getId());
        }

        response.put("labels",labels);
        response.put("visitors",visitors);

        return new Response<>(HttpStatus.OK.value(),response);
    }

    //방문자 목록(일주일)
    @GetMapping("/week-count")
    public Response<?>visitorWeekList(){
        Map<String,Object>response = new HashMap<>();
        List<LocalDateTime>labels = new ArrayList<>();
        List<Integer>visitors = new ArrayList<>();
        List<Visitor> weekCount = visitorService.countLoginForWeekDay();

        for(Visitor visitor : weekCount){
            labels.add(visitor.getLoginDateTime());
            visitors.add(visitor.getId());
        }

        response.put("labels",labels);
        response.put("visitors",visitors);
        return new Response<>(HttpStatus.OK.value(),response);
    }

    //방문자 목록(한달)

}
