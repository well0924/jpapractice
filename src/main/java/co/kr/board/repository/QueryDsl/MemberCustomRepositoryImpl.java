package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Member;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.domain.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static co.kr.board.domain.QMember.member;

public class MemberCustomRepositoryImpl implements MemberCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    QMember qMember;

    //생성자 주입
    public MemberCustomRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
        qMember = QMember.member;
    }

    //회원 이름 검색
    @Override
    public Page<MemberDto.MemeberResponseDto> findByAllSearch(String searchVal, Pageable pageable) {

        List<Member>content = getMemberDto(searchVal,pageable);

        long count = getCount(searchVal);

        return new PageImpl<>(content.stream().map(MemberDto.MemeberResponseDto::new).collect(Collectors.toList()),pageable,count);
    }

    //회원 단일 조회하기.
    @Override
    public Optional<MemberDto.MemeberResponseDto> findByMemberDetail(Integer useridx) {
        MemberDto.MemeberResponseDto result = jpaQueryFactory
                .select(Projections.constructor(MemberDto.MemeberResponseDto.class,qMember))
                .from(qMember)
                .where(qMember.id.eq(useridx))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    //회원 아이디 찾기.
    @Override
    public Optional<MemberDto.MemeberResponseDto> findByMemberNameAndUserEmail(String memberName, String userEmail) {
        MemberDto.MemeberResponseDto result = jpaQueryFactory
                .select(Projections
                        .constructor(MemberDto.MemeberResponseDto.class,qMember))
                .from(qMember)
                .where(qMember.membername.eq(memberName)
                        .and(qMember.useremail.eq(userEmail)))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    //회원 목록
    private List<Member> getMemberDto(String searchVal, Pageable pageable){
        return jpaQueryFactory
                .select(member)
                .from(member)
                .where(userNameCt(searchVal).or(userEmailCt(searchVal)))
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    //검색시 결과수
    private Long getCount(String searchVal){
        return jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(userNameCt(searchVal).or(userEmailCt(searchVal)))
                .orderBy(member.id.desc())
                .fetchOne();
    }

    //회원 아이디명
    BooleanBuilder userNameCt(String searchVal) {
        return nullSafeBuilder(() -> member.username.contains(searchVal));
    }

    //회원 이메일
    BooleanBuilder userEmailCt(String searchVal){
        return nullSafeBuilder(()->member.useremail.contains(searchVal));
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
