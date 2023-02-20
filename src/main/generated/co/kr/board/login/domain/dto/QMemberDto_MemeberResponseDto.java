package co.kr.board.login.domain.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * co.kr.board.login.domain.dto.QMemberDto_MemeberResponseDto is a Querydsl Projection type for MemeberResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberDto_MemeberResponseDto extends ConstructorExpression<MemberDto.MemeberResponseDto> {

    private static final long serialVersionUID = -1682111825L;

    public QMemberDto_MemeberResponseDto(com.querydsl.core.types.Expression<? extends co.kr.board.login.domain.Member> member) {
        super(MemberDto.MemeberResponseDto.class, new Class<?>[]{co.kr.board.login.domain.Member.class}, member);
    }

}

