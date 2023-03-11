package co.kr.board.domain.Dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * co.kr.board.domain.Dto.QMemberDto_MemeberResponseDto is a Querydsl Projection type for MemeberResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberDto_MemeberResponseDto extends ConstructorExpression<MemberDto.MemeberResponseDto> {

    private static final long serialVersionUID = -1661620588L;

    public QMemberDto_MemeberResponseDto(com.querydsl.core.types.Expression<? extends co.kr.board.domain.Member> member) {
        super(MemberDto.MemeberResponseDto.class, new Class<?>[]{co.kr.board.domain.Member.class}, member);
    }

}

