package co.kr.board.like;

import co.kr.board.domain.Like;
import co.kr.board.service.LikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@DisplayName("좋아요 컨트롤러 테스트")
@SpringBootTest
@AutoConfigureMockMvc
public class LikeApiControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private LikeService likeService;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();



    @DisplayName("좋아요 +1 테스트-성공")
    @Test
    public void likePlusControllerTest()throws Exception{
        mvc.perform(post("/api/like/plus/"+4))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("좋아요 -1 테스트")
    @Test
    public void likeMinusControllerTest(){

    }

    @DisplayName("좋아요 중복 테스트")
    @Test
    public void likeDuplicatedControllerTest(){

    }

    private Like likeDto(){
        return null;
    }
}
