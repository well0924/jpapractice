package co.kr.board.repository;

import co.kr.board.domain.Category;
import co.kr.board.repository.queryDsl.CategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer>, CategoryCustomRepository {
}
