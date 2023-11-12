package co.kr.board.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "boardhastag")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardHashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;

    @Builder
    public BoardHashTag(Board board,HashTag hashTag){
        this.board =board;
        this.hashTag =hashTag;
    }
}
