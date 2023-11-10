package co.kr.board.domain.Dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * co.kr.board.domain.Dto.QScrapDto_ResponseDto is a Querydsl Projection type for ResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QScrapDto_ResponseDto extends ConstructorExpression<ScrapDto.ResponseDto> {

    private static final long serialVersionUID = 182281194L;

    public QScrapDto_ResponseDto(com.querydsl.core.types.Expression<? extends co.kr.board.domain.Scrap> scrap) {
        super(ScrapDto.ResponseDto.class, new Class<?>[]{co.kr.board.domain.Scrap.class}, scrap);
    }

}

