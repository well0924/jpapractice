package co.kr.board.domain;

import co.kr.board.domain.Const.NoticeType;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String message;
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "useridx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public Notification(String message,boolean isRead,NoticeType noticeType,Member member){
        this.message = message;
        this.isRead = isRead;
        this.noticeType = noticeType;
        this.member = member;
        this.getCreatedAt();
    }
}
