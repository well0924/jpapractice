package co.kr.board.file.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AttachDto {
    private String originFileName;
    private String filePath;
    private Long fileSize;

    @Builder
    public AttachDto(String originFileName,String filePath,Long fileSize){
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
