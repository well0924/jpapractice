package co.kr.board.domain;

import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Data
@Entity
@Table(name = "boardhastag")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardHashTag implements Persistable<BoardTagId> {
    @EmbeddedId
    BoardTagId id;

    @MapsId("boardId")
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @MapsId("hashTagId")
    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;

    public void setParent(Board board){
        this.board =board;
        this.id = new BoardTagId(board.getId(),hashTag.getId());
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
