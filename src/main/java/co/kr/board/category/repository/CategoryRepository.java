package co.kr.board.category.repository;

import co.kr.board.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer>{
    @Query("select c " +
            "from " +
            "Category c " +
            "left join c.parent p " +
            "order by " +
            "p.id asc nulls first, c.id asc")
    List<Category> findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
}
