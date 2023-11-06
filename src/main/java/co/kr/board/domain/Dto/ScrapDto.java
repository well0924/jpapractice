package co.kr.board.domain.Dto;

import co.kr.board.domain.Scrap;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

public class ScrapDto {

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto{
        private Integer boardId;
        private Integer categoryId;
        private String categoryName;
        private String boardTitle;
        private String boardContents;
        private String boardAuthor;
        private Integer readCount;
        private Integer liked;
        private String password;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;

        @Builder
        @QueryProjection
        public ResponseDto(Scrap scrap){
            this.boardId = scrap.getBoard().getId();
            this.categoryId = scrap.getBoard().getCategory().getId();
            this.categoryName = scrap.getBoard().getCategory().getName();
            this.boardTitle = scrap.getBoard().getBoardTitle();
            this.boardContents = scrap.getBoard().getBoardContents();
            this.boardAuthor = scrap.getMember().getUsername();
            this.readCount = scrap.getBoard().getReadCount();
            this.password = scrap.getBoard().getPassword();
            this.liked = scrap.getBoard().getLiked();
            this.createdAt = scrap.getBoard().getCreatedAt();
        }
    }
}
