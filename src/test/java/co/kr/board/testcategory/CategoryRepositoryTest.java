package co.kr.board.testcategory;


import co.kr.board.Config.TestQueryDslConfig;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.repository.CategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.lang.annotation.Documented;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestQueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired // 원래는 @PersistenceContext 을 많이 썼는데, 이제는 오토와이어도 잘됩니다.
    private EntityManager em;

    private JPAQueryFactory queryFactory;

    // 기본적으로 테스트가 시작하기 전에 실행하는 함수
    @BeforeEach
    void createTest() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("카테고리 목록")
    public void categoryList(){
        List<CategoryDto>list = categoryRepository.categoryList();
        assertThat(list).isNotNull();
        System.out.println(list);
    }
}
