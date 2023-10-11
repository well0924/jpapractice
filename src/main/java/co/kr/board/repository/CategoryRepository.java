package co.kr.board.repository;

import co.kr.board.domain.Category;
import co.kr.board.repository.QueryDsl.CategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer>, CategoryCustomRepository {

/*
    @Query("select c " +
            "from " +
            "Category c " +
            "left join c.parent p " +
            "order by " +
            "p.id asc nulls first, c.id asc")
    List<Category> findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
*/
    Category findByName(String categoryName);
}
