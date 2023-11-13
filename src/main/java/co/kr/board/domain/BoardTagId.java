package co.kr.board.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class BoardTagId implements Serializable {
    @Column(name = "board_id")
    private Integer boardId;
    @Column(name = "hashtag_id")
    private Integer hashTagId;

    public BoardTagId(Integer boardId,Integer hashTagId){
        this.boardId = boardId;
        this.hashTagId = hashTagId;
    }
}
