package co.kr.board.visitorTest;

import co.kr.board.Config.TestQueryDslConfig;
import co.kr.board.domain.Visitor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@DataJpaTest
@Import({TestQueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VisitorRepositoryTest {
    @Autowired
    private co.kr.board.repository.VisitorRepository visitorRepository;

    @Test
    @DisplayName("일일 방문자 집계")
    public void totalVisitorTest(){
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
        List<Visitor> list = visitorRepository.findDistinctByUsernameForBetween(startTime,endTime);
        System.out.println("result::"+list);
    }
}
