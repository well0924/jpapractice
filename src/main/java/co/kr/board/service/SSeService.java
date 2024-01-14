package co.kr.board.service;

import co.kr.board.domain.Const.NoticeType;
import co.kr.board.domain.Dto.NoticeDto;
import co.kr.board.domain.Member;
import co.kr.board.domain.Notification;
import co.kr.board.repository.EmitterRepositoryImpl;
import co.kr.board.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Log4j2
@Service
@AllArgsConstructor
public class SSeService {
    private final EmitterRepositoryImpl emitterRepository;
    private final NotificationRepository notificationRepository;
    private static final Long DEFAULT_TIMEOUT = 60 * 1000L;

    /*
     * 구독 알림
     * @param memberId : 회원 번호
     */
    public SseEmitter subscribe(Integer memberId){
        String emitterId = makeTimeIncludeId(memberId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(memberId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");

        return emitter;
    }


    private String makeTimeIncludeId(Integer memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }


    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter
                    .event()
                    .id(eventId)
                    .name("open")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }
    
    //알림전송
    public void send(Member receiver, NoticeType notificationType, String content, String data) {
        log.info("send??");
        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, data));

        String receiverId = String.valueOf(receiver.getId());

        log.info(receiverId);

        String eventId = receiverId + "_" + System.currentTimeMillis();

        log.info(eventId);

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        log.info(emitters);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NoticeDto.create(notification));
                }
        );
        log.info("????:"+emitters.get(receiverId));
    }

    private Notification createNotification(Member member,NoticeType noticeType,String message,String data){
        return Notification
                .builder()
                .member(member)
                .message(message)
                .noticeType(noticeType)
                .isRead(false)
                .data(data)
                .build();
    }
}
