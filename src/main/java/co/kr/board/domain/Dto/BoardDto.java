package co.kr.board.domain.Dto;

import co.kr.board.domain.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

public class BoardDto {
    //게시물 작성 dto
    @Getter
    @Setter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class BoardRequestDto{
        @NotBlank(message ="제목을 입력해주세요.")
        private String boardTitle;
        @NotBlank(message ="내용을 입력해주세요.")
        private String boardContents;
        private Integer readCount;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;
    }

    //게시물 응답 dto
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardResponseDto implements Serializable {
        private Integer boardId;
        private Integer categoryId;
        private String boardTitle;
        private String boardContents;
        private String boardAuthor;
        private Integer readCount;
        private Integer liked;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;

        @Builder
        @QueryProjection
        public BoardResponseDto(Board board) {
            this.boardId = board.getId();
            this.boardTitle = board.getBoardTitle();
            this.boardAuthor = board.getWriter().getUsername();
            this.boardContents = board.getBoardContents();
            this.readCount = board.getReadCount();
            this.liked = board.getLiked();
            this.categoryId = board.getCategory().getId();
            this.createdAt = board.getCreatedAt();
        }
    }
}
