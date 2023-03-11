package co.kr.board.service;

import co.kr.board.domain.Category;
import co.kr.board.domain.Dto.CategoryCreateRequest;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.repository.CategoryRepository;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    //카테고리 목록
    public List<CategoryDto> categoryList() {
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        return CategoryDto.toDtoList(categories);
    }

    //카테고리 등록
    @Transactional
    public void create(CategoryCreateRequest req) {
        Category parent = Optional.ofNullable(req.getParentId())
                .map(id -> categoryRepository.findById(id).orElseThrow(()->new CustomExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND)))
                .orElse(null);

        categoryRepository.save(new Category(req.getName(),parent));
    }

    //카테고리 삭제
    @Transactional
    public void delete(Integer id) {
        if(notExistsCategory(id)){
            throw new CustomExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }

    //카테고리 확인여부
    private boolean notExistsCategory(Integer id) {
        return !categoryRepository.existsById(id);
    }

}
