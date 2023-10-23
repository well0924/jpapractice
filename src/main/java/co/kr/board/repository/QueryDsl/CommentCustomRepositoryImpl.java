package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.*;
import co.kr.board.domain.Dto.CommentDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentCustomRepositoryImpl implements CommentCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    QMember qMember;

    QComment qComment;

    QCategory qCategory;

    public CommentCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.qMember = QMember.member;
        this.qComment = QComment.comment;
        this.qCategory = QCategory.category;
    }

    //댓글 목록(어드민)
    @Override
    public Page<CommentDto.CommentResponseDto> findCommentList(Pageable pageable){
        JPAQuery<CommentDto.CommentResponseDto> list = jpaQueryFactory
                .select(Projections.constructor(CommentDto.CommentResponseDto.class,qComment))
                .from(qComment)
                .orderBy(getAllOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return PageableExecutionUtils.getPage(list.fetch(),pageable,list::fetchCount);
    }

    //댓글 목록
    @Override
    public List<CommentDto.CommentResponseDto> findCommnentList(Integer boardId) {
        List<Comment>commentList = jpaQueryFactory
                .select(qComment)
                .from(qComment)
                .where(qComment.board.id.eq(boardId))
                .fetch();
        return commentList.stream().map(CommentDto.CommentResponseDto::new).collect(Collectors.toList());
    }

    //최근에 작성한 댓글 5개
    @Override
    public List<CommentDto.CommentResponseDto> findTop5ByOrderByReplyIdCreatedAtDesc() {
        List<Comment> list = jpaQueryFactory
                .select(qComment)
                .from(qComment)
                .orderBy(qComment.id.desc(),qComment.createdAt.desc())
                .limit(5)
                .fetch();
        return list.stream().map(CommentDto.CommentResponseDto::new).collect(Collectors.toList());
    }

    //댓글 정렬
    private List<OrderSpecifier> getAllOrderSpecifiers(Sort sort) {
        List<OrderSpecifier>orders =  new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            String prop = order.getProperty();
            PathBuilder<Comment> orderByExpression =  new PathBuilder<>(Comment.class,"comment");

            orders.add(new OrderSpecifier(direction,orderByExpression.get(prop)));
        });

        return orders;
    }
}
