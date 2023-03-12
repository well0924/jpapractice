package co.kr.board.service;

import co.kr.board.config.redis.CacheKey;
import co.kr.board.domain.Board;
import co.kr.board.repository.BoardRepository;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Like;
import co.kr.board.repository.LikeRepository;
import co.kr.board.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepository repository;
    private final String likeMessage ="좋아요 처리 완료";
    private final String likeCancelMessage ="좋아요 취소 처리 완료";

    /*
    * 좋아요+1
    * @param Board
    * @param Member
    * 게시글조회에서 좋아요 +1기능
    */
    @Transactional
    @Cacheable(value = CacheKey.LIKE,key = "#likeMessage",unless = "#result == null")
    public String createLikeBoard(Board board,Member member){
        board.increaseLikeCount();
        Like like = new Like(board,member);
        repository.save(like);
        return likeMessage;
    }

    /*
     * 좋아요-1
     * @param Board
     * @param Member
     * 게시글 조회에서 좋아요 -1
     */
    @Transactional
    @Cacheable(value = CacheKey.LIKE,key = "#likeCancelMessage",unless = "#result == null")
    public String removeLikeBoard(Board board,Member member){
        Like likeBoard = repository.findByMemberAndBoard(member,board).orElseThrow(()->{throw new CustomExceptionHandler(ErrorCode.LIKE_NOT_FOUND);});
        board.decreaseLikeCount();
        repository.delete(likeBoard);
        return likeCancelMessage;
    }

    /*
    * 좋아요 중복처리기능
    * @param Board
    * @param Member
    *
    */
    @Transactional
    public boolean hasLikeBoard(Board board,Member member){
        return repository.findByMemberAndBoard(member,board).isPresent();
    }
}
