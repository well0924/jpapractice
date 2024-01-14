package co.kr.board.domain;

import co.kr.board.domain.Const.NoticeType;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String message;
    private String data;
    private boolean isRead;
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "useridx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public Notification(String message,String data,boolean isRead,NoticeType noticeType,Member member){
        this.message = message;
        this.data = data;
        this.isRead = isRead;
        this.noticeType = noticeType;
        this.member = member;
        this.getCreatedAt();
    }

    public void isReadChange(){
        this.isRead = true;
    }
}
