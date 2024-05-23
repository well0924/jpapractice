package co.kr.board.testmember;

import co.kr.board.config.TestQueryDslConfig;
import co.kr.board.domain.Const.Role;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestQueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 아이디 찾기")
    public void searchMemberId(){
        String memberName = "tester1";
        String userEmail = "well123@Test.com";
        Optional<MemberDto.MemeberResponseDto>result =
                memberRepository.findByMemberNameAndUserEmail(memberName,userEmail);

        String resultId = result.get().getUsername();
        System.out.println(resultId);
        assertThat(result.get().getRole()).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    @DisplayName("회원 단일 조회")
    public void memberDetail(){
        Optional<MemberDto.MemeberResponseDto>result = memberRepository.findByMemberDetail(1);
        System.out.println(result.get());
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("회원 검색기능")
    public void memberSearchTest(){
        String keyword = "well4149";
        Pageable pageable  = Pageable.ofSize(5);
        Page<MemberDto.MemeberResponseDto>list = memberRepository.findByAllSearch(keyword, pageable);
        System.out.println(list.toList());
    }

    @Test
    @DisplayName("회원 아이디 중복 테스트")
    public void existsByIdTest(){
        String userId = "well4149";
        boolean isId = memberRepository.existsByUsername(userId);
        System.out.println("result::"+isId);
        assertThat(isId).isTrue();
    }

    @Test
    @DisplayName("회원 아이디 중복 테스트")
    public void existsByEmailTest(){
        String userEmail = "well123@Test.com";
        boolean isId = memberRepository.existsByUseremail(userEmail);
        System.out.println("result::"+isId);
        assertThat(isId).isTrue();
    }
}
