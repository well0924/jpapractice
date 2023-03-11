package co.kr.board.domain.Dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * co.kr.board.domain.Dto.QBoardDto_BoardResponseDto is a Querydsl Projection type for BoardResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBoardDto_BoardResponseDto extends ConstructorExpression<BoardDto.BoardResponseDto> {

    private static final long serialVersionUID = -2020055263L;

    public QBoardDto_BoardResponseDto(com.querydsl.core.types.Expression<? extends co.kr.board.domain.Board> board) {
        super(BoardDto.BoardResponseDto.class, new Class<?>[]{co.kr.board.domain.Board.class}, board);
    }

}

