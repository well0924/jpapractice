package co.kr.board.repository.queryDsl;

import co.kr.board.domain.Dto.CategoryDto;

import java.util.List;

public interface CategoryCustomRepository {
    List<CategoryDto>categoryList();
}
