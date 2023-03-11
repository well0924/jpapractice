package co.kr.board.domain.Dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AttachDto implements Serializable {
    private String originFileName;
    private String filePath;
    private Long fileSize;
    private Integer boardId;

    @Builder
    public AttachDto(String originFileName,String filePath,Long fileSize,Integer boardId){
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.boardId = boardId;
    }
}
