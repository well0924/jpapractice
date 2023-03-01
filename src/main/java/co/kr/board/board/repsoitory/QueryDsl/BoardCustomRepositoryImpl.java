package co.kr.board.board.repsoitory.QueryDsl;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.QBoard;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.likes.domain.QLike;
import co.kr.board.login.domain.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BoardCustomRepositoryImpl implements BoardCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    private QBoard qBoard;
    private QMember qMember;
    private QLike qLike;

    //생성자 주입
    public BoardCustomRepositoryImpl(EntityManager em){

        this.jpaQueryFactory = new JPAQueryFactory(em);
        qBoard = QBoard.board;
        qMember = QMember.member;
        qLike = QLike.like;
    }

    //게시글 검색
    @Override
    public Page<BoardDto.BoardResponseDto> findByAllSearch(String searchVal, Pageable pageable) {
        List<Board>content = getBoardMemberDtos(searchVal,pageable);

        List<BoardDto.BoardResponseDto>contents = new ArrayList<>();

        long count = getCount(searchVal);

        for(Board article : content){
            BoardDto.BoardResponseDto responseDto = BoardDto.BoardResponseDto
                    .builder()
                    .board(article)
                    .build();
            contents.add(responseDto);
        }
        return new PageImpl<>(contents,pageable,count);
    }
    //글목록 조회
    private List<Board>getBoardMemberDtos(String searchVal, Pageable pageable){

        List<Board>content = jpaQueryFactory
               .select(qBoard)
               .from(qBoard)
               .where(
                       titleCt(searchVal).or(contentCt(searchVal))
               )
               .orderBy(qBoard.id.desc())
               .offset(pageable.getOffset())
               .limit(pageable.getPageSize())
               .fetch();

       return content;
    }
    
    //검색 결과물 갯수
    private Long getCount(String searchVal){

        Long count = jpaQueryFactory
                .select(qBoard.count())
                .from(qBoard)
                .leftJoin(qBoard.writer,qMember)
                .where(titleCt(searchVal).or(contentCt(searchVal)))
                .fetchOne();

        return count;
    }
    //게시글 제목
    BooleanBuilder titleCt(String searchVal) {
        return nullSafeBuilder(() -> qBoard.boardTitle.contains(searchVal));
    }
    //게시글 내용
    BooleanBuilder contentCt(String searchVal) {return nullSafeBuilder(() -> qBoard.boardContents.contains(searchVal));}



    //BooleanBuilder를 Safe하게 만들기 위해 만든 메소드
    BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (Exception e) {
            return new BooleanBuilder();
        }
    }
}
