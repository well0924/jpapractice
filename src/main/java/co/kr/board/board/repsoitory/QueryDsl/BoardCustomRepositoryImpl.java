package co.kr.board.board.repsoitory.QueryDsl;

import static co.kr.board.board.domain.QBoard.board;

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.SearchCondition;
import co.kr.board.board.domain.SearchType;
import co.kr.board.board.domain.dto.BoardDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
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

    public BoardCustomRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardDto.BoardResponseDto> findByAllSearch(SearchCondition searchCondition, Pageable pageable) {
        QueryResults<Board>results = jpaQueryFactory
                .select(board)
                .from(board)
                .where(isSearchable(searchCondition.getType(),searchCondition.getContent()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                //.orderBy(sort(pageable))
                .fetchResults();

        List<BoardDto.BoardResponseDto> content = new ArrayList<>();

        for(Board board : results.getResults()){
            BoardDto.BoardResponseDto responseDto = BoardDto
                    .BoardResponseDto
                    .builder()
                    .board(board)
                    .build();
            content.add(responseDto);
        }

        long total = results.getTotal();

        return new PageImpl<>(content,pageable,total);
    }
    //검색 조건
    BooleanBuilder isSearchable(SearchType sType, String content) {
        if (sType == SearchType.TITLE) {//제목
            return titleCt(content);
        }
        else if(sType == SearchType.CONTENTS) {//내용
            return contentCt(content);
        }
        else {//제목 + 내용
            return titleCt(content).or(contentCt(content));
        }
    }

    BooleanBuilder titleCt(String content) {
        return nullSafeBuilder(() -> board.boardTitle.contains(content));
    }
    BooleanBuilder contentCt(String content) {
        return nullSafeBuilder(() -> board.boardContents.contains(content));
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
