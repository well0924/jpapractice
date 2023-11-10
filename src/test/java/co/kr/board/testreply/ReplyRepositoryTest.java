package co.kr.board.testreply;

import co.kr.board.Config.TestQueryDslConfig;
import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.repository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReplyRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("최근에 작성한 댓글5개")
    public void CommentTop5Test() throws Exception {
        List<CommentDto.CommentResponseDto>list = commentRepository.findTop5ByOrderByReplyIdCreatedAtDesc();
        System.out.println(list);
        Assertions.assertThat(list).isNotNull();
    }
}
