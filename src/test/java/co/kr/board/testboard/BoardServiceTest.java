package co.kr.board.testboard;

import co.kr.board.domain.Board;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Member;
import co.kr.board.repository.MemberRepository;
import co.kr.board.service.BoardService;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AbstractSoftAssertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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
        BoardDto.BoardRequestDto dto = new BoardDto.BoardRequestDto();
        boardService.changeSecretBoard(1,dto);
        BoardDto.BoardResponseDto detail = boardService.getBoard(1);

        assertAll(
                ()->assertThat(detail.getPassword()).isNotNull(),
                ()->assertThat(detail.getPassword().length()).isEqualTo(4));
    }

    @Test
    @DisplayName("게시글 비밀글 초기화")
    public void restPassword(){
        BoardDto.BoardResponseDto detail = boardService.getBoard(1);
        BoardDto.BoardRequestDto dto = new BoardDto.BoardRequestDto();
        dto.setPassword(detail.getPassword());
        boardService.passwordReset(1,dto);
        assertThat(detail.getPassword()).isNull();
    }

    @Test
    @DisplayName("해시태그 관련글 검색기능")
    public void hashtagSearchTest(){
        Pageable pageable = Pageable.ofSize(5);
        Page<BoardDto.BoardResponseDto> list = boardService.searchHashtagBoard("spring",pageable);
        System.out.println(list.stream().collect(Collectors.toList()));
    }
}
