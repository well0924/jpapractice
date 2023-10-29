package co.kr.board.testboard;

import co.kr.board.Config.TestQueryDslConfig;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Const.SearchType;
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
        List<BoardDto.BoardResponseDto>result = boardRepository.findNextPreviousBoard(386);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("게시글 목록")
    public void boardListQueryDslTest(){
        Pageable pageable  = Pageable.ofSize(5);
        String categoryName = "";
        Page<BoardDto.BoardResponseDto>list = boardRepository.findAllBoardList(categoryName,pageable);
        System.out.println("result::"+list.get().collect(Collectors.toList()));
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("게시글 검색")
    public void boardSearchTest(){
        Pageable pageable = Pageable.ofSize(10);
        String keyword = "well4149";
        Page<BoardDto.BoardResponseDto>list = boardRepository.findByAllSearch(keyword, SearchType.toSearch("boardAuthor"),pageable);
        System.out.println(list.toList());
        assertThat(list);
        System.out.println("size::"+list.stream().count());
    }

    @Test
    @DisplayName("게시글 전체 목록")
    public void adminBoardList(){
        Pageable pageable = Pageable.ofSize(5);
        Page<BoardDto.BoardResponseDto>list = boardRepository.findAllBoardList(pageable);
        System.out.println(list.toList());
        assertThat(list).isNotNull();
    }
}
