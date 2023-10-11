package co.kr.board.testboard;

import co.kr.board.Config.TestQueryDslConfig;
import co.kr.board.domain.Board;
import co.kr.board.domain.Category;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.repository.BoardRepository;
import co.kr.board.repository.CategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestQueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired // 원래는 @PersistenceContext 을 많이 썼는데, 이제는 오토와이어도 잘됩니다.
    private EntityManager em;

    private JPAQueryFactory queryFactory;

    @BeforeEach // 기본적으로 테스트가 시작하기 전에 실행하는 함수
    void createTest() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("게시글 목록(카테고리 및 페이징)")
    public void boardListTest(){
        Category category = categoryRepository.findById(1).orElseThrow();
        String categoryName = category.getName();
        Pageable pageable  = Pageable.ofSize(5);
        Page<Board> list = boardRepository.findAllByCategoryName(pageable,categoryName);
        BoardDto.BoardResponseDto result = new BoardDto.BoardResponseDto();

        System.out.println("목록::"+list.get().map(board->new BoardDto.BoardResponseDto(board)).collect(Collectors.toList()));
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("최근에 작성한 게시글 5개")
    public void findTop5BoardListTest(){
        List<BoardDto.BoardResponseDto>list = boardRepository.findTop5ByOrderByBoardIdDescCreatedAtDesc();

        System.out.println(list);
        assertThat(list).isNotNull();
        assertThat(list).hasSize(5);
    }

    @Test
    @DisplayName("게시글 이전글/다음글 출력")
    public void findNextPreviousBoard(){
        List<BoardDto.BoardResponseDto>result = boardRepository.findNextPrevioustBoard(386);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }
}
