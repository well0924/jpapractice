package co.kr.board.likes.domain;

import co.kr.board.board.domain.Board;
import co.kr.board.login.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    //회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="useridx")
    private Member member;

    public Like(Board board,Member member){
        this.board = board;
        this.member = member;
    }
}
