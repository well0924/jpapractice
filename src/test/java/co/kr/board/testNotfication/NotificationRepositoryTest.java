package co.kr.board.testNotfication;

import co.kr.board.config.TestQueryDslConfig;
import co.kr.board.domain.Board;
import co.kr.board.domain.Const.NoticeType;
import co.kr.board.domain.Dto.NoticeDto;
import co.kr.board.domain.Member;
import co.kr.board.domain.Notification;
import co.kr.board.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestQueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotificationRepositoryTest {

    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    private final Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;

    @Test
    @DisplayName("새로운 Emitter를 추가한다.")
    public void save() {
        //given
        Long memberId = 1L;
        String emitterId =  memberId + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        //when, then
        Assertions.assertDoesNotThrow(() -> emitterRepository.save(emitterId, sseEmitter));
        System.out.println(sseEmitter);
    }

    @Test
    @DisplayName("수신한 이벤트를 캐시에 저장한다.")
    public void saveEventCache() {
        //given
        Member member = memberRepository.findById(1).orElseThrow();
        Board board = boardRepository.findById(1).orElseThrow();
        Integer memberId = member.getId();
        String eventCacheId =  memberId + "_" + System.currentTimeMillis();
        Notification notification = new Notification(board.getBoardAuthor()+"에 댓글이 작성되었습니다.","boardId"+board.getId(),false,NoticeType.REPLY,member);

        //when, then
        Assertions.assertDoesNotThrow(() -> emitterRepository.saveEventCache(eventCacheId, notification));
    }

    @Test
    @DisplayName("어떤 회원이 접속한 모든 Emitter를 찾는다")
    public void findAllEmitterStartWithByMemberId() throws Exception {
        //given
        Integer memberId = 1;
        String emitterId1 = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(emitterId1, new SseEmitter(DEFAULT_TIMEOUT));

        Thread.sleep(100);
        String emitterId2 = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(emitterId2, new SseEmitter(DEFAULT_TIMEOUT));

        Thread.sleep(100);
        String emitterId3 = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(emitterId3, new SseEmitter(DEFAULT_TIMEOUT));


        //when
        Map<String, SseEmitter> ActualResult = emitterRepository.findAllEmitterStartWithByMemberId(String.valueOf(memberId));

        System.out.println(ActualResult);
        System.out.println(ActualResult.get(emitterId1).toString());
        System.out.println(ActualResult.get(emitterId2).toString());
        System.out.println(ActualResult.get(emitterId3).toString());

        //then
        Assertions.assertEquals(3, ActualResult.size());
    }

    @Test
    @DisplayName("어떤 회원에게 수신된 이벤트를 캐시에서 모두 찾는다.")
    public void findAllEventCacheStartWithByMemberId() throws Exception {
        //given
        Member member = memberRepository.findById(1).orElseThrow();
        Board board = boardRepository.findById(1).orElseThrow();

        Integer memberId = member.getId();

        String eventCacheId1 =  memberId + "_" + System.currentTimeMillis();

        Notification notification1 = new Notification(board.getBoardAuthor()+"에 댓글이 작성되었습니다.","boardId"+board.getId(),false,NoticeType.REPLY,member);
        emitterRepository.saveEventCache(eventCacheId1, notification1);

        Thread.sleep(100);
        String eventCacheId2 =  memberId + "_" + System.currentTimeMillis();
        Notification notification2 = new Notification(board.getBoardAuthor()+"에 댓글이 작성되었습니다.","boardId"+board.getId(),false,NoticeType.REPLY,member);
        emitterRepository.saveEventCache(eventCacheId2, notification2);

        Thread.sleep(100);
        String eventCacheId3 =  memberId + "_" + System.currentTimeMillis();
        Notification notification3 = new Notification(board.getBoardAuthor()+"에 댓글이 작성되었습니다.","boardId"+board.getId(),false,NoticeType.REPLY,member);
        emitterRepository.saveEventCache(eventCacheId3, notification3);

        //when
        Map<String, Object> ActualResult = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
        System.out.println("??:"+ActualResult.get(eventCacheId1).toString());
        System.out.println("??:"+ActualResult.get(eventCacheId2).toString());
        System.out.println("??:"+ActualResult.get(eventCacheId3).toString());
        //then
        Assertions.assertEquals(3, ActualResult.size());
    }

    @Test
    @DisplayName("ID를 통해 Emitter를 Repository에서 제거한다.")
    public void deleteById(){
        //given
        Long memberId = 1L;
        String emitterId =  memberId + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        //when
        emitterRepository.save(emitterId, sseEmitter);
        emitterRepository.deleteById(emitterId);

        //then
        Assertions.assertEquals(0, emitterRepository.findAllEmitterStartWithByMemberId(emitterId).size());
    }

    @Test
    @DisplayName("저장된 모든 Emitter를 제거한다.")
    public void deleteAllEmitterStartWithId() throws Exception {
        //given
        Long memberId = 1L;
        String emitterId1 = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(emitterId1, new SseEmitter(DEFAULT_TIMEOUT));

        Thread.sleep(100);
        String emitterId2 = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(emitterId2, new SseEmitter(DEFAULT_TIMEOUT));

        //when
        emitterRepository.deleteAllEmitterStartWithId(String.valueOf(memberId));

        //then
        Assertions.assertEquals(0, emitterRepository.findAllEmitterStartWithByMemberId(String.valueOf(memberId)).size());
    }

    @Test
    @DisplayName("수신한 이벤트를 캐시에 저장한다.")
    public void deleteAllEventCacheStartWithId() throws Exception {
        //given
        Long memberId = 1L;
        Board board = boardRepository.findById(1).orElseThrow();
        String eventCacheId1 =  memberId + "_" + System.currentTimeMillis();
        Notification notification1 = new Notification(board.getBoardAuthor()+"에 댓글이 작성되었습니다.","boardId"+board.getId(),false,NoticeType.REPLY,new Member());
        emitterRepository.saveEventCache(eventCacheId1, notification1);

        Thread.sleep(100);
        String eventCacheId2 =  memberId + "_" + System.currentTimeMillis();
        Notification notification2 = new Notification(board.getBoardAuthor()+"에 댓글이 작성되었습니다.","boardId"+board.getId(),false,NoticeType.REPLY,new Member());
        emitterRepository.saveEventCache(eventCacheId2, notification2);

        //when
        emitterRepository.deleteAllEventCacheStartWithId(String.valueOf(memberId));

        //then
        Assertions.assertEquals(0, emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId)).size());
    }

    @Test
    @DisplayName("회원 이름을 기반으로 알림목록을 출력하기.")
    public void notificationListTest(){
        String username= "well4149";

        List<NoticeDto>list = notificationRepository.findAllByNotification(username);

        System.out.println(list);

        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("회원 알림 조회")
    public void NotificationGetTest(){
        String username = "well4149";
        Optional<NoticeDto>detail = notificationRepository.findByUsername(username,91);
        System.out.println(detail.get());
    }
}
