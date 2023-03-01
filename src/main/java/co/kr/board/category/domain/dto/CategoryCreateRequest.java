package co.kr.board.category.domain.dto;

import co.kr.board.category.domain.Category;
import co.kr.board.category.repository.CategoryRepository;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import lombok.*;

import java.util.Arrays;
import java.util.Optional;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest {
    private String name;
    private Integer parentId;

}
