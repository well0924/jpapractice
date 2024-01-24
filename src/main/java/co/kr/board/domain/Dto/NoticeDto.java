package co.kr.board.domain.Dto;

import co.kr.board.domain.Const.NoticeType;
import co.kr.board.domain.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class NoticeDto {
    private Integer id;
    private String data;
    private String messeage;
    private boolean isRead;
    private NoticeType noticeType;
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdTime;

    @Builder
    public  NoticeDto(Notification notification){
        this.id = notification.getId();
        this.data = notification.getData();
        this.messeage = notification.getMessage();
        this.noticeType = notification.getNoticeType();
        this.isRead = notification.isRead();
        this.username = notification.getMember().getUsername();
        this.createdTime = notification.getCreatedAt();
    }

    public static NoticeDto create(Notification notification){
        return NoticeDto
                .builder()
                .notification(notification)
                .build();
    }
}
