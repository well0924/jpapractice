package co.kr.board.repository.queryDsl;

import co.kr.board.domain.*;
import co.kr.board.domain.Const.SearchType;
import co.kr.board.domain.Dto.BoardDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log4j2
public class BoardCustomRepositoryImpl implements BoardCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    private final QBoard qBoard;
    private final QMember qMember;
    private final QLike qLike;
    private final QCategory qCategory;
    private final QBoardHashTag qBoardHashTag;
    private final QHashTag qHashTag;

    //생성자 주입
    public BoardCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
        qBoard = QBoard.board;
        qMember = QMember.member;
        qLike = QLike.like;
        qCategory = QCategory.category;
        qHashTag = QHashTag.hashTag;
        qBoardHashTag = QBoardHashTag.boardHashTag;
    }

    //게시글 목록(게시글 관리페이지)
    @Override
    public Page<BoardDto.BoardResponseDto> findAllBoardList(Pageable pageable) {
        JPQLQuery<BoardDto.BoardResponseDto>list = jpaQueryFactory
                .select(Projections.constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoard)
                .orderBy(getAllOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return PageableExecutionUtils.getPage(list.fetch(),pageable,list::fetchCount);
    }

    //게시글 카테고리 목록(카테고리 및 정렬)
    @Override
    public Page<BoardDto.BoardResponseDto> findAllBoardList(String categoryName, Pageable pageable) {
        JPQLQuery<BoardDto.BoardResponseDto>boardList = jpaQueryFactory
                .select(Projections.constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember).fetchJoin()
                .leftJoin(qBoard.category,qCategory)
                .leftJoin(qBoard.likes,qLike)
                .leftJoin(qBoard.hashtags,qBoardHashTag)
                .distinct()
                .where(categoryName(categoryName))//카테고리명
                .orderBy(getAllOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return PageableExecutionUtils.getPage(boardList.fetch(),pageable,boardList::fetchCount);
    }

    //게시글 목록 검색 + 정렬 + 페이징
    @Override
    public Page<BoardDto.BoardResponseDto> findByAllSearch(String searchVal, SearchType searchType, Pageable pageable) {
        JPQLQuery<BoardDto.BoardResponseDto>list = jpaQueryFactory
                .select(Projections.constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember).fetchJoin()
                .leftJoin(qBoard.category,qCategory)
                .leftJoin(qBoard.likes,qLike)
                .leftJoin(qBoard.hashtags,qBoardHashTag)
                .distinct();

        JPQLQuery<BoardDto.BoardResponseDto>middleQuery = switch (searchType){
            //제목
            case TITLE -> list.where(titleCt(searchVal));
            //작성자
            case AUTHOR -> list.where(authorCt(searchVal));
            //내용
            case CONTENTS -> list.where(contentCt(searchVal));
            //ALL
            case ALL ->list.where(titleCt(searchVal).or(authorCt(searchVal)).or(contentCt(searchVal)));

            default -> throw new IllegalStateException("Unexpected value: " + searchType);
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

        JPQLQuery<BoardDto.BoardResponseDto>result = jpaQueryFactory
                .select(Projections.constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember).fetchJoin()
                .leftJoin(qBoard.category,qCategory).fetchJoin()
                .leftJoin(qBoard.likes,qLike)
                .distinct()
                .where(qBoard.boardAuthor.eq(username))
                .orderBy(qBoard.id.desc());

        return PageableExecutionUtils
                .getPage(result.fetch(),pageable,result::fetchCount);
    }
    
    //최근에 작성한 글(5개) o.k
    @Override
    public List<BoardDto.BoardResponseDto> findTop5ByOrderByBoardIdDescCreatedAtDesc() {
        JPQLQuery<BoardDto.BoardResponseDto>result = jpaQueryFactory
                .select(Projections.constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoard)
                .leftJoin(qBoard.category,qCategory).fetchJoin()
                .leftJoin(qBoard.likes,qLike)
                .distinct()
                .orderBy(qBoard.createdAt.desc(),qBoard.id.desc())
                .limit(5);
        return result.fetch();
    }
    
    //게시글 이전글/다음글 조회 o.k
    @Override
    public List<BoardDto.BoardResponseDto> findNextPreviousBoard(Integer id) {
        List<Board>boardResponseDtos = jpaQueryFactory
                .select(qBoard)
                .from(qBoard)
                .where(qBoard.id.in(findNextBoard(id), findPreviousBoard(id)))
                .orderBy(qBoard.id.desc())
                .fetch();

        return boardResponseDtos.stream().map(BoardDto.BoardResponseDto::new).collect(Collectors.toList());
    }

    //해시태그 관련 게시글 o.k
    @Override
    public Page<BoardDto.BoardResponseDto> findAllHashTagWithBoard(String tagName, Pageable pageable) {

        JPQLQuery<BoardDto.BoardResponseDto>list = jpaQueryFactory
                .select(Projections.constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoardHashTag)
                .innerJoin(qBoardHashTag.board,qBoard)
                .on(qBoardHashTag.board.id.eq(qBoard.id))
                .innerJoin(qBoardHashTag.hashTag,qHashTag)
                .on(qBoardHashTag.hashTag.id.eq(qHashTag.id))
                .where(qBoardHashTag.hashTag.hashtagName.eq(tagName))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return PageableExecutionUtils.getPage(list.fetch(),pageable,list::fetchCount);
    }
    
    //게시글 선택삭제
    @Modifying
    @Transactional
    @Override
    public void deleteAllByBoard(List<Integer> boardId) {
        jpaQueryFactory
                .delete(qBoard)
                .where(qBoard.id.in(boardId))
                .execute();
    }

    //게시글 조회(비관적 락 사용) o.k
    @Override
    public Optional<BoardDto.BoardResponseDto> findByBoardDetail(Integer boardId) {
        BoardDto.BoardResponseDto result = jpaQueryFactory
                .select(Projections
                        .constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoard)
                .where(qBoard.id.eq(boardId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
        return Optional.ofNullable(result);
    }

    //게시글 조회수 증가 o.k
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateReadCount(Integer boardId) {
        jpaQueryFactory.update(qBoard)
                .set(qBoard.readCount,qBoard.readCount.add(1))
                .where(qBoard.id.eq(boardId))
                .execute();
    }

    //게시글 비밀번호 확인 o.k
    @Override
    public BoardDto.BoardResponseDto passwordCheck(String password,Integer boardId) {
        JPAQuery<BoardDto.BoardResponseDto> result = jpaQueryFactory
                .select(Projections
                .constructor(BoardDto.BoardResponseDto.class,qBoard))
                .from(qBoard)
                .where(qBoard.id.eq(boardId)
                        .and(qBoard.password.eq(password)));
        return result.fetchOne();
    }

    //게시글 다음글 o.k
    private Integer findNextBoard(Integer boardId){

        return jpaQueryFactory
                .select(qBoard.id)
                .from(qBoard)
                .where(qBoard.id.lt(boardId))
                .orderBy(qBoard.id.desc())
                .limit(1)
                .fetchOne();
    }

    //게시글 이전글 o.k
    private Integer findPreviousBoard(Integer boardId){
        return jpaQueryFactory
                .select(qBoard.id)
                .from(qBoard)
                .where(qBoard.id.gt(boardId))
                .orderBy(qBoard.id.asc())
                .limit(1)
                .fetchOne();
    }

    //카테고리 이름
    BooleanBuilder categoryName(String categoryName){return nullSafeBuilder(()->qBoard.category.name.containsIgnoreCase(categoryName));}

    //게시글 제목
    BooleanBuilder titleCt(String searchVal) {return nullSafeBuilder(() -> qBoard.boardTitle.contains(searchVal));}

    //게시글 내용
    BooleanBuilder contentCt(String searchVal) {return nullSafeBuilder(() -> qBoard.boardContents.contains(searchVal));}

    //게시글 작성자
    BooleanBuilder authorCt(String searchVal){return nullSafeBuilder(()->qBoard.boardAuthor.containsIgnoreCase(searchVal));}
    
    //게시글 정렬
    private List<OrderSpecifier> getAllOrderSpecifiers(Sort sort) {
        List<OrderSpecifier>orders =  new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            log.info(prop);
            PathBuilder<Board> orderByExpression =  new PathBuilder<>(Board.class,"board");
            log.info(direction);
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
