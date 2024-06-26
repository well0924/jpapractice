package co.kr.board.testboard;

import co.kr.board.config.TestQueryDslConfig;
import co.kr.board.domain.Board;
import co.kr.board.domain.Category;
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
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
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

    @PersistenceContext
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
        List<BoardDto.BoardResponseDto>result = boardRepository.findNextPreviousBoard(382);
        System.out.println("목록::"+result);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("게시글 목록-카테고리 및 정렬")
    public void boardListQueryDslTest(){
        //given
        Pageable pageable  = Pageable.ofSize(5);
        Category category = categoryRepository.findById(1).get();
        String categoryName = category.getName();
        //when
        Page<BoardDto.BoardResponseDto>list = boardRepository.findAllBoardList(categoryName,pageable);
        System.out.println("result::"+list.get().collect(Collectors.toList()));
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("게시글 검색 - 게시글 내용")
    public void boardSearchContentsTest(){
        Pageable pageable = Pageable.ofSize(10);
        String keyword = "트트";
        Page<BoardDto.BoardResponseDto>list = boardRepository.findByAllSearch(keyword, SearchType.toSearch("boardContents"),pageable);
        System.out.println(list.toList());
        assertThat(list).isNotNull();
        System.out.println("size::"+list.stream().count());
    }

    @Test
    @DisplayName("게시글 검색 - 게시글 제목")
    public void boardSearchTitleTest(){
        Pageable pageable = Pageable.ofSize(10);
        String keyword = "수정제목";
        Page<BoardDto.BoardResponseDto>list = boardRepository.findByAllSearch(keyword, SearchType.toSearch("boardTitle"),pageable);
        System.out.println(list.toList());
        assertThat(list.toList().get(0).getBoardTitle()).isEqualTo(keyword);
        System.out.println("size::"+list.stream().count());
    }

    @Test
    @DisplayName("게시글 검색 - 전체")
    public void boardSearchALLTest(){
        Pageable pageable = Pageable.ofSize(10);
        String keyword = "upload test";
        Page<BoardDto.BoardResponseDto>list = boardRepository.findByAllSearch(keyword, SearchType.ALL,pageable);
        System.out.println(list.toList());
        System.out.println("size::"+list.stream().count());
    }

    @Test
    @DisplayName("게시글 전체 목록(게시글 관리 페이지)")
    public void adminBoardList(){
        Pageable pageable = Pageable.ofSize(5);
        Page<BoardDto.BoardResponseDto>list = boardRepository.findAllBoardList(pageable);
        System.out.println(list.toList());
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("게시글 비밀번호 여부")
    public void passwordCheck(){
        Optional<Board>detail = boardRepository.findById(1);
        String result = boardRepository.boardPasswordCheck(detail.get().getId());
        System.out.println("결과값::"+result);
    }

    @Test
    @DisplayName("관련 해시태그 게시글 목록이 나오는가?")
    public void hashtest(){
        Pageable pageable = Pageable.ofSize(5);
        Page<BoardDto.BoardResponseDto>list = boardRepository.findAllHashTagWithBoard("spring",pageable);
        System.out.println(list.toList());
        assertThat(list).isNotNull();
    }


    @Test
    @DisplayName("작성자가 작성한 게시글")
    public void findUserBoardContents(){
        Pageable pageable = Pageable.ofSize(5);
        String username = "well4149";
        Page<BoardDto.BoardResponseDto>list = boardRepository.findByAllContents(username,pageable);
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("비밀번호 확인 테스트")
    public void boardPasswordCheckTest(){
        BoardDto.BoardResponseDto result1 = boardRepository.passwordCheck("1234",433);
        System.out.println(result1);
        String result=boardRepository.boardPasswordCheck(433);
        System.out.println(result);
    }


}

