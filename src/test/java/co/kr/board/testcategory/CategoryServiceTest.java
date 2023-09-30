package co.kr.board.testcategory;

import co.kr.board.domain.Dto.CategoryCreateRequest;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.repository.CategoryRepository;
import co.kr.board.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static co.kr.board.testcategory.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static co.kr.board.testcategory.CategoryFactory.*;
@DisplayName("category service Test")
@SpringBootTest
public class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryService categoryService;
    @Test
    @DisplayName("카테고리 목록 테스트")
    public void categoryListTest(){
        //given
        given(categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc())
                .willReturn(List.of(createCategoryWithName("name1"),createCategoryWithName("name2")));
        //when
        List<CategoryDto>list = categoryService.categoryList();
        //then
        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getName()).isEqualTo("name1");
        assertThat(list.get(1).getName()).isEqualTo("name2");
    }

    @Test
    @DisplayName("카테고리 생성 테스트")
    public void categoryCreateTest(){
        CategoryCreateRequest req = createCategoryCreateRequest();

        categoryService.create(req);

        Mockito.verify(categoryRepository).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    public void categoryDeleteTest(){
        categoryCreateTest();

        given(categoryRepository.findById(anyInt())).willReturn(Optional.of(createCategory()));

        categoryService.delete(anyInt());

        Mockito.verify(categoryRepository).deleteById(anyInt());
    }

}
