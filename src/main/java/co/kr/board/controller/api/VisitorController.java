package co.kr.board.controller.api;

import co.kr.board.config.exception.dto.Response;
import co.kr.board.service.VisitorService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/visitor")
@AllArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    //방문자 목록(당일)
    @GetMapping("/day-count")
    public Response<?>visitorDayList(){
        Map<String,Object>response = new HashMap<>();
        Integer dayCount = visitorService.countLoginsForLastDay();
        response.put("today",dayCount);
        return new Response<>(HttpStatus.OK.value(),response);
    }

    //어제 방문자
    @GetMapping("/yesterday-count")
    public Response<?>yesterDayVisitor(){
        Map<String,Object>response = new HashMap<>();
        Integer yesterDayCount = visitorService.countLoginForYesterDay();
        response.put("yesterday",yesterDayCount);
        return new Response<>(HttpStatus.OK.value(),response);
    }

    //방문자 목록(일주일)
    @GetMapping("/week-count")
    public Response<?>visitorWeekList(){
        Map<String,Object>response = new HashMap<>();
        //방문일자
        List<LocalDateTime>labels = new ArrayList<>();
        //당일 들어온 회원들의 수
        List<Integer>visitors = visitorService.countLoginForWeekDayCount();
        // 현재 시간 (UTC로 설정)
        LocalDateTime utcNow = LocalDateTime.now(ZoneId.of("UTC"));
        // 서버의 타임존 설정 (한국 시간대로 설정)
        ZoneId serverTimeZone = ZoneId.of("Asia/Seoul");
        // UTC 시간을 서버의 타임존으로 변환
        ZonedDateTime serverTime = utcNow.atZone(ZoneId.of("UTC")).withZoneSameInstant(serverTimeZone);

        LocalDateTime startOfWeek = serverTime.toLocalDate().atStartOfDay(serverTimeZone).minusWeeks(1).toLocalDateTime();

        for(int i=0;i<=7;i++){
            LocalDateTime startOfDay = startOfWeek.plusDays(i);
            labels.add(startOfDay);
        }

        response.put("labels",labels);
        response.put("DayCount",visitors);

        return new Response<>(HttpStatus.OK.value(),response);
    }

    //전체 방문자
    @GetMapping("/total-count")
    public Response<?>totalCount(){
        Integer count = visitorService.countTotalVisitors();
        return new Response<>(HttpStatus.OK.value(),count);
    }
}
