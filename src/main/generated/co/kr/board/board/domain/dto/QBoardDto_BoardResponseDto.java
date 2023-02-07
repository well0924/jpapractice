package co.kr.board.board.domain.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * co.kr.board.board.domain.dto.QBoardDto_BoardResponseDto is a Querydsl Projection type for BoardResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBoardDto_BoardResponseDto extends ConstructorExpression<BoardDto.BoardResponseDto> {

    private static final long serialVersionUID = 1367268233L;

    public QBoardDto_BoardResponseDto(com.querydsl.core.types.Expression<? extends co.kr.board.board.domain.Board> board) {
        super(BoardDto.BoardResponseDto.class, new Class<?>[]{co.kr.board.board.domain.Board.class}, board);
    }

}

