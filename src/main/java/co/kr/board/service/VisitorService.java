package co.kr.board.service;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Member;
import co.kr.board.domain.Visitor;
import co.kr.board.repository.MemberRepository;
import co.kr.board.repository.VisitorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final MemberRepository memberRepository;

    //로그인 중복처리
    public boolean isNotDuplicateLogin(String username) {
        LocalDateTime loginTime = LocalDateTime.now();
        Optional<Visitor> existingLogin = visitorRepository.findByUsernameAndLoginDateTimeAfter(username, loginTime.minusMinutes(1));

        if (existingLogin.isPresent()) {
            // 1분 이내에 로그인한 기록이 있다면 중복 로그인으로 처리
            return false;
        } else {
            // 중복 로그인이 아닌 경우 로그인 기록 저장
            return true;
        }
    }

    //방문자 기록
    public void visitorSave(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null&& authentication.isAuthenticated()){
            String username = authentication.getName();
            Visitor visitor = Visitor
                    .builder()
                    .member(getMember())
                    .username(username)
                    .loginDateTime(LocalDateTime.now())
                    .build();
            visitorRepository.save(visitor);
        }
    }

    //총 들어온 방문자의 수
    @Transactional
    public Integer countTotalVisitors(){
        return Math.toIntExact(visitorRepository.count());
    }

    //오늘 하루동안 들어온 회원 방문자수
    @Transactional
    public List<Visitor> countLoginsForLastDay() {
        // 현재 시간 (UTC로 설정)
        LocalDateTime utcNow = LocalDateTime.now(ZoneId.of("UTC"));
        // 서버의 타임존 설정 (한국 시간대로 설정)
        ZoneId serverTimeZone = ZoneId.of("Asia/Seoul");
        // UTC 시간을 서버의 타임존으로 변환
        ZonedDateTime serverTime = utcNow.atZone(ZoneId.of("UTC")).withZoneSameInstant(serverTimeZone);
        //시작시간 00:00:00
        LocalDateTime startTime = serverTime.toLocalDate().now().atStartOfDay();
        System.out.println("현재시간:"+startTime);
        //종료시간 23:59:00
        LocalDateTime endTime = serverTime.toLocalDate().now().atTime(23,59,0);
        System.out.println("종료시간:"+endTime);
        return visitorRepository.findDistinctUserIdsForBetween(startTime,endTime);
    }

    //어제 들어왔던 회원의 수
    @Transactional
    public List<Visitor> countLoginForYesterDay(){
        // 현재 시간 (UTC로 설정)
        LocalDateTime utcNow = LocalDateTime.now(ZoneId.of("UTC"));
        // 서버의 타임존 설정 (한국 시간대로 설정)
        ZoneId serverTimeZone = ZoneId.of("Asia/Seoul");
        // UTC 시간을 서버의 타임존으로 변환
        ZonedDateTime serverTime = utcNow.atZone(ZoneId.of("UTC")).withZoneSameInstant(serverTimeZone);
        LocalDateTime startOfDay = serverTime.toLocalDate().atStartOfDay(serverTimeZone).toLocalDateTime().minusDays(1);
        LocalDateTime endOfDay = startOfDay.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return visitorRepository.findDistinctUserIdsForBetween(startOfDay,endOfDay);
    }

    //회원의 일주일동안 들어온 방문자수
    public List<Visitor> countLoginForWeekDay(){
        // 현재 시간 (UTC로 설정)
        LocalDateTime utcNow = LocalDateTime.now(ZoneId.of("UTC"));
        // 서버의 타임존 설정 (한국 시간대로 설정)
        ZoneId serverTimeZone = ZoneId.of("Asia/Seoul");
        // UTC 시간을 서버의 타임존으로 변환
        ZonedDateTime serverTime = utcNow.atZone(ZoneId.of("UTC")).withZoneSameInstant(serverTimeZone);

        LocalDateTime startOfDay = serverTime.toLocalDate().atStartOfDay(serverTimeZone).toLocalDateTime().minusWeeks(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime endOfDay = LocalDateTime.now();

        return visitorRepository.findDistinctUserIdsForBetween(startOfDay,endOfDay);
    }

    //회원 인증
    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName().toString();
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
        if(member == null){
            throw new CustomExceptionHandler(ErrorCode.ONLY_USER);
        }
        return member;
    }
}
