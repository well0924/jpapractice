package co.kr.board.domain.Dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * co.kr.board.domain.Dto.QCommentDto_CommentResponseDto is a Querydsl Projection type for CommentResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommentDto_CommentResponseDto extends ConstructorExpression<CommentDto.CommentResponseDto> {

    private static final long serialVersionUID = 1814472065L;

    public QCommentDto_CommentResponseDto(com.querydsl.core.types.Expression<? extends co.kr.board.domain.Comment> comment) {
        super(CommentDto.CommentResponseDto.class, new Class<?>[]{co.kr.board.domain.Comment.class}, comment);
    }

}

