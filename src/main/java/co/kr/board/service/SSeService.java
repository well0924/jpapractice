package co.kr.board.service;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Const.NoticeType;
import co.kr.board.domain.Dto.NoticeDto;
import co.kr.board.domain.Member;
import co.kr.board.domain.Notification;
import co.kr.board.repository.EmitterRepositoryImpl;
import co.kr.board.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class SSeService {
    private final EmitterRepositoryImpl emitterRepository;
    private final NotificationRepository notificationRepository;
    private static final Long DEFAULT_TIMEOUT = 60 * 1000L;

    /**
     * 구독 알림
     * @param userName : 회원 아이디
     **/
    public SseEmitter subscribe(String userName, HttpServletResponse response){
        String emitterId = makeTimeIncludeId(userName);
        response.setHeader("X-Accel-Buffering", "no");//Nginx의 프록시에서 필요설정 불필요한 버퍼링방지
        log.info(emitterId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        log.info(emitter);
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(userName);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userName + "]");

        return emitter;
    }


    private String makeTimeIncludeId(String memberId) {
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

        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, data));

        String receiverId = String.valueOf(receiver.getUsername());

        log.info(receiverId);

        String eventId = receiverId + "_" + System.currentTimeMillis();

        log.info(eventId);

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);

        log.info(emitters);

        emitters.forEach(
                (key, object) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(object, receiverId, key, NoticeDto.create(notification));
                }
        );
    }
    
    //알림 목록
    @Transactional(readOnly = true)
    public List<NoticeDto>noticeList(String username){
        return notificationRepository.findAllByNotification(username);
    }
    
    //알림 개별 조회
    @Transactional(readOnly = true)
    public NoticeDto getNotice(String username,Integer noticeId){
        Optional<NoticeDto>detail = notificationRepository.findByUsername(username,noticeId);
        //조회시 읽은여부 true로 전환하기.
        if(detail.isPresent()){
            Notification notification = Notification
                    .builder()
                    .noticeType(detail.get().getNoticeType())
                    .data(detail.get().getData())
                    .message(detail.get().getMesseage())
                    .noticeType(detail.get().getNoticeType())
                    .isRead(true)
                    .build();
            notificationRepository.save(notification);
        }
        return detail.orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOTICE_NOT_FOUND));
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
