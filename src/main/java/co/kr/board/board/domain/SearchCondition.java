package co.kr.board.board.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchCondition {
    String content;
    SearchType type;

    public SearchCondition(String content, SearchType type) {
        this.content = content;
        this.type = type;
    }
}
