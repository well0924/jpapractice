package co.kr.board.service;

import co.kr.board.domain.Category;
import co.kr.board.domain.Dto.CategoryCreateRequest;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.repository.CategoryRepository;
import co.kr.board.config.exception.dto.ErrorCode;
import co.kr.board.config.exception.handler.CustomExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    //카테고리 목록
    public List<CategoryDto> categoryList() {
        List<CategoryDto> list = categoryRepository.categoryList();
        return list;
    }

    //카테고리 등록
    @Transactional
    public void create(CategoryCreateRequest req) {
        Category parent = Optional.ofNullable(req.getParentId())
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(()->new CustomExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND)))
                .orElse(null);

        categoryRepository.save(new Category(req.getName(),parent));
    }

    //카테고리 삭제
    @Transactional
    public void delete(Integer id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(()->new CustomExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND));
        log.info("category::"+category);
        if(category!=null){
            categoryRepository.deleteById(id);
        }
    }
}
