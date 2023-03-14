package co.kr.board.like;

import co.kr.board.domain.Board;
import co.kr.board.domain.Like;
import co.kr.board.domain.Member;
import co.kr.board.repository.BoardRepository;
import co.kr.board.repository.LikeRepository;
import co.kr.board.repository.MemberRepository;
import co.kr.board.service.LikeService;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("좋아요 서비스 테스트")
@SpringBootTest
public class LikeServiceTest {
    @Autowired
    private LikeService likeService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    private Board board;
    private Member member;
    private Like like;

    @BeforeEach
    public void member1(){
        Optional<Member> detail1 = memberRepository.findById(1);
        member = detail1.get();
    }
    @BeforeEach
    public void board1(){
        Optional<Board>detail = boardRepository.findById(4);
        board = detail.get();
    }
    @BeforeEach
    public void like(){
        like = new Like(board,member);
    }

    @DisplayName("좋아요 중복체크")
    @Test
    public void likeDuplicatedTest(){
        member1();
        board1();
        like();

        Boolean duplidatedresult = likeService.hasLikeBoard(board,member);

        Assumptions.assumingThat(duplidatedresult.equals(true),()->{
            likeMinusTest();
        });

        Assumptions.assumingThat(duplidatedresult.equals(false),()->{
            likePlusTest();
        });
    }
    @DisplayName("좋아요 -1")
    public void likeMinusTest(){
        String likeResult = likeService.removeLikeBoard(board,member);
        assertEquals(likeResult,"좋아요 취소 처리 완료");
    }
    @DisplayName("좋아요 +1")
    public void likePlusTest(){
        String likeResult = likeService.createLikeBoard(board,member);
        assertEquals(likeResult,"좋아요 처리 완료");
    }
}
