package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.*;
import co.kr.board.domain.Dto.BoardDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BoardCustomRepositoryImpl implements BoardCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    QBoard qBoard;
    QMember qMember;
    QLike qLike;
    QCategory qCategory;

    //생성자 주입
    public BoardCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
        qBoard = QBoard.board;
        qMember = QMember.member;
        qLike = QLike.like;
        qCategory = QCategory.category;
    }
    
    //게시글 카테고리 목록
    @Override
    public Page<BoardDto.BoardResponseDto> findAllBoardList(String categoryName, Pageable pageable) {
        List<BoardDto.BoardResponseDto>boardList = jpaQueryFactory
                .select(Projections.constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember)
                .on(qBoard.id.eq(qBoard.writer.id))
                .leftJoin(qBoard.category,qCategory)
                .on(qCategory.id.eq(qBoard.category.id))
                .leftJoin(qBoard.likes,qLike)
                .on(qLike.board.id.eq(qBoard.id))
                .where(categoryName(categoryName))//카테고리명
                .orderBy(getAllOrderSpecifiers(pageable.getSort())
                        .toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        //게시물 갯수
        Long listCount = jpaQueryFactory
                .select(qBoard.count())
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember)
                .on(qBoard.id.eq(qBoard.writer.id))
                .leftJoin(qBoard.category,qCategory)
                .on(qCategory.id.eq(qBoard.category.id))
                .leftJoin(qBoard.likes,qLike)
                .on(qLike.board.id.eq(qBoard.id))
                .where(categoryName(categoryName))//카테고리
                .orderBy(getAllOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .fetchOne();

        return new PageImpl<>(boardList,pageable,listCount);
    }

    //게시글 목록 검색 + 정렬 + 페이징
    @Override
    public Page<BoardDto.BoardResponseDto> findByAllSearch(String searchVal, SearchType searchType, Pageable pageable) {
        JPQLQuery<BoardDto.BoardResponseDto>list = jpaQueryFactory
                .select(Projections.constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember)
                .on(qBoard.id.eq(qBoard.writer.id))
                .leftJoin(qBoard.category,qCategory)
                .on(qCategory.id.eq(qBoard.category.id))
                .distinct();

        JPQLQuery<BoardDto.BoardResponseDto>middleQuery = switch (searchType){
            //제목
            case TITLE -> list.where(titleCt(searchVal));
            //작성자
            case AUTHOR -> list.where(authorCt(searchVal));
            //내용
            case CONTENTS -> list.where(contentCt(searchVal));
            //ALL
            default ->list.where(titleCt(searchVal).or(authorCt(searchVal)).or(contentCt(searchVal)));
        };

        return PageableExecutionUtils
                .getPage(middleQuery
                        .orderBy(getAllOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                        pageable,
                        middleQuery::fetchCount);
    }

    //내가 작성한 글 o.k
    @Override
    public Page<BoardDto.BoardResponseDto> findByAllContents(String username, Pageable pageable) {

        List<Board>myarticle = getMyArticle(username,pageable);
        long count = getMyArticleCount(username);

        List<BoardDto.BoardResponseDto>list = myarticle
                .stream()
                .map(BoardDto.BoardResponseDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(list,pageable,count);
    }
    
    //최근에 작성한 글(5개) o.k
    @Override
    public List<BoardDto.BoardResponseDto> findTop5ByOrderByBoardIdDescCreatedAtDesc() {

        List<Board>boardList = jpaQueryFactory
                .select(qBoard)
                .from(qBoard)
                .orderBy(qBoard.createdAt.desc(),qBoard.id.desc())
                .limit(5)
                .fetch();

        return boardList.stream().map(BoardDto.BoardResponseDto::new).collect(Collectors.toList());
    }
    
    //게시글 이전글/다음글 조회 o.k
    @Override
    public List<BoardDto.BoardResponseDto> findNextPrevioustBoard(Integer id) {
        List<Board>boardResponseDtos = jpaQueryFactory
                .select(qBoard)
                .from(qBoard)
                .where( qBoard.id
                        .in(findNextBoard(id),
                            findPreviousBoard(id)
                        )
                )
                .orderBy(qBoard.id.desc())
                .fetch();

        return boardResponseDtos.stream().map(BoardDto.BoardResponseDto::new).collect(Collectors.toList());
    }

    //게시글 다음글
    private Integer findNextBoard(Integer boardId){

        return jpaQueryFactory
                .select(qBoard.id)
                .from(qBoard)
                .where(qBoard.id.lt(boardId))
                .orderBy(qBoard.id.desc())
                .limit(1)
                .fetchOne();
    }

    //게시글 이전글 
    private Integer findPreviousBoard(Integer boardId){
        return jpaQueryFactory
                .select(qBoard.id)
                .from(qBoard)
                .where(qBoard.id.goe(boardId))
                .orderBy(qBoard.id.asc())
                .limit(1)
                .fetchOne();
    }

    //회원이 작성한 글 목록
    private List<Board>getMyArticle(String username,Pageable pageable){
        return jpaQueryFactory
                .select(qBoard)
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember)
                .where(qMember.username.eq(username))
                .orderBy(qBoard.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    //회원이 작성한 글 개수
    private Long getMyArticleCount(String username){
        return jpaQueryFactory
                .select(qBoard.count())
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember)
                .where(qMember.username.eq(username))
                .fetchOne();
    }

    //카테고리 이름
    BooleanBuilder categoryName(String categoryName){
        return nullSafeBuilder(()->qCategory.name.containsIgnoreCase(categoryName));
    }
    //게시글 제목
    BooleanBuilder titleCt(String searchVal) {return nullSafeBuilder(() -> qBoard.boardTitle.contains(searchVal));}

    //게시글 내용
    BooleanBuilder contentCt(String searchVal) {return nullSafeBuilder(() -> qBoard.boardContents.contains(searchVal));}

    //게시글 작성자
    BooleanBuilder authorCt(String searchVal){
        return nullSafeBuilder(()->qBoard.boardAuthor.containsIgnoreCase(searchVal));
    }
    
    //게시글 정렬
    private List<OrderSpecifier> getAllOrderSpecifiers(Sort sort) {
        List<OrderSpecifier>orders =  new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            String prop = order.getProperty();

            PathBuilder<Board> orderByExpression =  new PathBuilder<>(Board.class,"board");

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
