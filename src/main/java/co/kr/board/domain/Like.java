package co.kr.board.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "likes",indexes = {
        @Index(columnList = "id"),
        @Index(columnList = "likeStatus")
})
@Getter
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id",nullable = false)
    private Board board;

    //회원
    //회원이 삭제가 되면 연관관계도 삭제됨.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="useridx",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    //좋아요를 눌렀는지 확인
    private boolean likeStatus;

    public Like(Board board,Member member){
        this.board = board;
        this.member = member;
        this.likeStatus = true;
    }
}
