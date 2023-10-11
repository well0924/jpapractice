package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.*;
import co.kr.board.domain.Dto.BoardDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    @Override
    public Page<BoardDto.BoardResponseDto> findAllBoardList(String categoryName, Pageable pageable) {
        List<Board>boardList = jpaQueryFactory
                .select(qBoard)
                .from(qBoard)
                .fetch();

        return null;
    }

    //게시글 검색 + 정렬
    @Override
    public Page<BoardDto.BoardResponseDto> findByAllSearch(String searchVal, Pageable pageable) {
        List<Board>content = getBoardMemberDtos(searchVal,pageable);

        List<BoardDto.BoardResponseDto>contents = content
                                                .stream()
                                                .map(BoardDto.BoardResponseDto::new)
                                                .collect(Collectors.toList());

        long count = getCount(searchVal,pageable);

        return new PageImpl<>(contents,pageable,count);
    }

    //내가 작성한 글
    @Override
    public Page<BoardDto.BoardResponseDto> findByAllContents(String username, Pageable pageable) {

        List<Board>myarticle = getMyArticle(username,pageable);
        long count = getMyArticleCount(username);

        List<BoardDto.BoardResponseDto>list = myarticle
                .stream()
                .map(board->new BoardDto.BoardResponseDto(board))
                .collect(Collectors.toList());

        return new PageImpl<>(list,pageable,count);
    }
    
    //최근에 작성한 글(5개)
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
    
    //게시글 이전글/다음글 조회
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

    //내가 작성한 글 목록
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

    //글목록 조회+정렬
    private List<Board>getBoardMemberDtos(String searchVal, Pageable pageable){
        return jpaQueryFactory
                .select(qBoard)
                .from(qBoard)
                .leftJoin(qBoard.writer, qMember)
                .where(titleCt(searchVal).or(contentCt(searchVal)))
                .orderBy(getAllOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    //검색 결과물 갯수
    private Long getCount(String searchVal,Pageable pageable){
        return jpaQueryFactory
                .select(qBoard.count())
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember)
                .where(titleCt(searchVal).or(contentCt(searchVal)))
                .orderBy(getAllOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchOne();
    }

    //게시글 제목
    BooleanBuilder titleCt(String searchVal) {return nullSafeBuilder(() -> qBoard.boardTitle.contains(searchVal));}

    //게시글 내용
    BooleanBuilder contentCt(String searchVal) {return nullSafeBuilder(() -> qBoard.boardContents.contains(searchVal));}
    
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
