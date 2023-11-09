package co.kr.board.domain.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class HashTagDto {
    private Integer id;
    private Integer boardId;
    private String hashtagName;
}
