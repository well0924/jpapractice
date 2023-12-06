package co.kr.board.testmember;

import co.kr.board.Config.TestQueryDslConfig;
import co.kr.board.domain.Const.Role;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.domain.Visitor;
import co.kr.board.repository.MemberRepository;
import co.kr.board.repository.VisitorRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({TestQueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void test1(){
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
        List<Visitor>list = visitorRepository.findDistinctByUsernameForBetween(startTime,endTime);
        System.out.println("result::"+list);
    }

    @Test
    @DisplayName("회원 아이디 찾기")
    public void searchMemberId(){
        String memberName = "tester1";
        String userEmail = "well123@Test.com";
        Optional<MemberDto.MemeberResponseDto>result =
                memberRepository.findByMemberNameAndUserEmail(memberName,userEmail);

        String resultId = result.get().getUsername();
        System.out.println(resultId);
        assertThat(result.get().getRole()).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    @DisplayName("회원 단일 조회")
    public void memberDetail(){
        Optional<MemberDto.MemeberResponseDto>result = memberRepository.findByMemberDetail(1);
        System.out.println(result.get());
        assertThat(result).isNotEmpty();
    }
}
