package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.*;
import co.kr.board.domain.Dto.CategoryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

public class CategoryCustomRepositoryImpl implements CategoryCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    QCategory qCategory;

    public CategoryCustomRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
        qCategory = QCategory.category;
    }

    //카테고리 목록
    @Override
    public List<CategoryDto> categoryList() {
        List<Category>list = jpaQueryFactory
                .select(qCategory)
                .from(qCategory)
                .leftJoin(qCategory.parent)
                .orderBy(qCategory.parent.id.asc().nullsFirst(),qCategory.id.asc())
                .fetch();
        return CategoryDto.toDtoList(list);
    }
}
