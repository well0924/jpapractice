package co.kr.board.likes.service;

import co.kr.board.likes.repository.LikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepository repository;

    //댓글 +1
    //댓글 -1
    //댓글 중복확인기능
}
