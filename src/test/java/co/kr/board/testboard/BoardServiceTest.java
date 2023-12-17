package co.kr.board.testboard;

import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.repository.MemberRepository;
import co.kr.board.service.BoardService;
import co.kr.board.service.VisitorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureDataJpa
public class BoardServiceTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("게시글 비밀글로 전환")
    public void changeBoard(){
        boardService.changeSecretBoard(1);
        BoardDto.BoardResponseDto detail = boardService.getBoard(1);

        assertAll(
                ()->assertThat(detail.getPassword()).isNotNull(),
                ()->assertThat(detail.getPassword().length()).isEqualTo(4));
    }

    @Test
    @DisplayName("해시태그 관련글 검색기능")
    public void hashtagSearchTest(){
        Pageable pageable = Pageable.ofSize(5);
        Page<BoardDto.BoardResponseDto> list = boardService.findHashTagRelatedBoardList("spring",pageable);
        System.out.println(list.stream().collect(Collectors.toList()));
    }

    @Test
    @DisplayName("비관적 락을 사용해서 게시글 조회수 동시성 제어 테스트")
    public void test()throws Exception{

        ExecutorService ex = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(100);
        int result = 0;
        for (int i=0;i<100;i++){
            ex.execute(()->{
                boardService.getBoard(382);
                latch.countDown();
            });
        }
        latch.await();
        result = boardService.getBoard(382).getReadCount();

        assertThat(result).isEqualTo(99);
    }

    @Test
    @DisplayName("회원이 작성한 글")
    public void memberArticle(){
        Pageable pageable = PageRequest.of(0,20);
        String username = "well";

        Page<BoardDto.BoardResponseDto>memberArticle = boardService.memberArticle(username,pageable);

        System.out.println(memberArticle.toList());
        System.out.println(memberArticle.stream().count());

        assertThat(memberArticle).isNotNull();
    }

    @Test
    @DisplayName("최근에 작성한 글")
    public void findBoardTop5Test(){
        List<BoardDto.BoardResponseDto>list = boardService.findBoardTop5();

        System.out.println("size::"+list.size());
        System.out.println("result::"+list);
        assertThat(list).isNotNull();
    }
}
