package co.kr.board.service;

import co.kr.board.domain.Board;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Like;
import co.kr.board.repository.LikeRepository;
import co.kr.board.domain.Member;
import co.kr.board.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepository repository;
    private final MemberRepository memberRepository;
    private final String likeMessage ="좋아요 처리 완료";
    private final String likeCancelMessage ="좋아요 취소 처리 완료";

    /*
    * 좋아요+1
    * @param Board
    * @param Member
    * 게시글조회에서 좋아요 +1기능
    */
    @Transactional
    public String createLikeBoard(Board board){
        Member member =getMember();
        //좋아요 증가
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
    public String removeLikeBoard(Board board){
        Member member =getMember();
        Like likeBoard = repository.findByMemberAndBoard(member,board).orElseThrow(()->{throw new CustomExceptionHandler(ErrorCode.LIKE_NOT_FOUND);});
        //좋아요 감소기능
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
    public boolean hasLikeBoard(Board board){
        Member member =getMember();
        return repository.findByMemberAndBoard(member,board).isPresent();
    }

    /*
     * 좋아요 갯수
     */
    @Transactional
    public Integer LikeCount(Integer boardId){
        return repository.findByBoardId(boardId);
    }

    /*
     * 회원 인증
     */
    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getName().toString();
        log.info(username);
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
        return member;
    }
}
