package co.kr.board.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Scrap extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="useridx",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    @Builder
    public Scrap(Board board,Member member){
        this.board = board;
        this.member = member;
        getCreatedAt();
    }

}
