package co.kr.board.likes.controller;

import co.kr.board.likes.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LikeController {
    private final LikeService likeService;

    //좋아요+1
    //좋아요-1
    //좋아요 중복체크

}
