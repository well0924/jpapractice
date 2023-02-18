package co.kr.board.file.domain;

import co.kr.board.board.domain.BaseTime;
import co.kr.board.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name="file")
@NoArgsConstructor
public class AttachFile extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String originFileName;
    @Column(nullable = false)
    private String filePath;  // 파일 저장 경로
    private Long fileSize;
    //게시판
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @Builder
    public AttachFile(String originFileName, String filePath, Long fileSize){
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
    //첨부파일 관련 메서드
    public void setBoard(Board board){
        this.board = board;
        // 게시글에 현재 파일이 존재하지 않는다면
        if(!board.getAttachFiles().contains(this)){
            // 파일 추가
            board.getAttachFiles().add(this);
        }
    }
}
