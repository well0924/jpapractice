package co.kr.board.service;

import co.kr.board.domain.Const.NoticeType;
import co.kr.board.domain.Dto.NoticeDto;
import co.kr.board.domain.Member;
import co.kr.board.domain.Notification;
import co.kr.board.repository.EmitterRepository;
import co.kr.board.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Integer memberId, String lastEventId) {
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(memberId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }

    public void send(Member member, NoticeType noticeType,String message){
        Notification notification = Notification
                .builder()
                .member(member)
                .message(message)
                .noticeType(noticeType)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        String receiverId = String.valueOf(member.getId());
        String eventId = makeTimeIncludeId(member.getId());

        Map<String,SseEmitter>emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);

        emitters.forEach((key,emitter)->{
            emitterRepository.saveEventCache(key,notification);
            sendNotification(emitter,eventId,key,NoticeDto.builder().notification(notification).build());
        });
    }

    private String makeTimeIncludeId(Integer memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Integer memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }
}
