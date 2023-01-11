package co.kr.board.maincontroller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MainControllerTest {
    @Autowired
    MockMvc mvc;
    
    @Test
    @DisplayName("메인 페이지화면 조회")
    public void mainpagetest()throws Exception{
        mvc
        .perform(get("/page/main/mainpage").contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("main/mainpage"))
        .andExpect(status().isOk())
        .andDo(print());
    }
}
