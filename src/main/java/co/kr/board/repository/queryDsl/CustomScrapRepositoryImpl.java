package co.kr.board.repository.queryDsl;

import co.kr.board.domain.*;
import co.kr.board.domain.Dto.ScrapDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CustomScrapRepositoryImpl implements CustomScrapRepository{

    private final JPAQueryFactory jpaQueryFactory;
    QMember qMember;
    QScrap qScrap;
    QBoard qBoard;

    public CustomScrapRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        this.jpaQueryFactory = jpaQueryFactory;
        this.qBoard = QBoard.board;
        this.qMember = QMember.member;
        this.qScrap = QScrap.scrap;
    }

    @Override
    public Page<ScrapDto.ResponseDto> ScrapList(String username,Pageable pageable) {
        JPAQuery<ScrapDto.ResponseDto>list = jpaQueryFactory
                .select(Projections.constructor(ScrapDto.ResponseDto.class,qScrap))
                .from(qScrap)
                .leftJoin(qScrap.board,qBoard)
                .on(qScrap.board.id.eq(qBoard.id))
                .leftJoin(qScrap.member,qMember)
                .on(qScrap.member.id.eq(qMember.id))
                .where(userName(username))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return PageableExecutionUtils.getPage(list.fetch(),pageable,list::fetchCount);
    }

    BooleanBuilder userName(String username){
        return nullSafeBuilder(()->qScrap.member.username.containsIgnoreCase(username));
    }

    //스크랩 정렬
    private List<OrderSpecifier> getAllOrderSpecifiers(Sort sort) {
        List<OrderSpecifier>orders =  new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder<Scrap> orderByExpression =  new PathBuilder<>(Scrap.class,"scrap");
            orders.add(new OrderSpecifier(direction,orderByExpression.get(prop)));
        });

        return orders;
    }

    //BooleanBuilder를 Safe하게 만들기 위해 만든 메소드
    BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (Exception e) {
            return new BooleanBuilder();
        }
    }
}
