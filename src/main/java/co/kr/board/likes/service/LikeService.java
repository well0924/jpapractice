package co.kr.board.likes.service;

import co.kr.board.board.domain.Board;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.likes.domain.Like;
import co.kr.board.likes.repository.LikeRepository;
import co.kr.board.login.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepository repository;
    private final BoardRepository boardRepository;

    @Transactional
    public String updateLikeOfBoard(Integer boardId,Member member){
        Board board = boardRepository.findById(boardId).orElseThrow(()->{throw new CustomExceptionHandler(ErrorCode.NOT_FOUND);});
        //좋아요 중복체크 
        if(!hasLikeBoard(board,member)){
            board.increaseLikeCount();
            return createLikeBoard(board,member);
        }
        //중복이 되는 경우에는 감소
        board.decreaseLikeCount();
        return removeLikeBoard(board,member);
    }

    //좋아요+1
    public String createLikeBoard(Board board,Member member){
        Like like = new Like(board,member);
        repository.save(like);
        return "좋아요 처리 완료";
    }

    //좋아요-1
    public String removeLikeBoard(Board board,Member member){
        Like likeBoard = repository.findByMemberAndBoard(member,board).orElseThrow(()->{throw new CustomExceptionHandler(ErrorCode.LIKE_NOT_FOUND);});
        repository.delete(likeBoard);
        return "좋아요 취소 처리 완료";
    }

    //좋아요 중복처리
    public boolean hasLikeBoard(Board board,Member member){
        return repository.findByMemberAndBoard(member,board).isPresent();
    }
}
