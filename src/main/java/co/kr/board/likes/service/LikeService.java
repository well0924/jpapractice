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

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepository repository;
    private final BoardRepository boardRepository;

    //좋아요 +1
    public boolean addLike(Member member,Board board){
        //중복 체크후 좋아요 +1
        if(isNotAlreadyLike(member,board)){
            repository.save(new Like(board,member));
            return true;
        }
        //중복이 되면 작동x
        return false;
    }
    //좋아요 -1
    public void minusLike(Member member,Integer boardId){
        //게시글 조회
        Board detailBoard = boardRepository.findById(boardId).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
        
        //좋아요 -1
        Like like = repository.findByMemberAndBoard(member,detailBoard).orElseThrow();
        repository.delete(like);
    }
    //댓글 중복확인기능
    private boolean isNotAlreadyLike(Member member, Board board){
        return repository.findByMemberAndBoard(member,board).isEmpty();
    }
}
