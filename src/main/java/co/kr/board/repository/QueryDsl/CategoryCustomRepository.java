package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Dto.CategoryDto;

import java.util.List;

public interface CategoryCustomRepository {
    List<CategoryDto>categoryList();
}
