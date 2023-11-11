package co.kr.board.domain.Dto;

import co.kr.board.domain.Category;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {
    private Integer id;
    private String name;
    private List<CategoryDto> children;

    public static List<CategoryDto> toDtoList(List<Category> categories) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                categories,
                c -> new CategoryDto(c.getId(), c.getName(), new ArrayList<>()),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }
}
