package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Member;
import co.kr.board.domain.Dto.MemberDto;
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

import static co.kr.board.domain.QMember.member;

public class MemberCustomRepositoryImpl implements MemberCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    //생성자 주입
    public MemberCustomRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    //회원 이름 검색
    @Override
    public Page<MemberDto.MemeberResponseDto> findByAllSearch(String searchVal, Pageable pageable) {

        List<Member>content = getMemberDto(searchVal,pageable);
        List<MemberDto.MemeberResponseDto>contents = new ArrayList<>();

        long count = getCount(searchVal);

        for(Member memberlist : content){
            MemberDto.MemeberResponseDto result = MemberDto.MemeberResponseDto.builder()
                    .member(memberlist)
                    .build();
            contents.add(result);
        }
        return new PageImpl<>(contents,pageable,count);
    }

    private List<Member> getMemberDto(String searchVal, Pageable pageable){
        List<Member>content = jpaQueryFactory
                .select(member)
                .from(member)
                .where(userNameCt(searchVal))
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    //검색시 결과수
    private Long getCount(String searchVal){
        Long count = jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(userNameCt(searchVal))
                .fetchOne();
        return count;
    }

    //회원 아이디명
    BooleanBuilder userNameCt(String searchVal) {
        return nullSafeBuilder(() -> member.username.contains(searchVal));
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
