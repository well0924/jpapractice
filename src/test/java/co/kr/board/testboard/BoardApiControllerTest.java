package co.kr.board.testboard;

import co.kr.board.CustomSecurity.TestCustomUserDetailsService;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.config.security.jwt.JwtTokenProvider;
import co.kr.board.domain.Board;
import co.kr.board.domain.Category;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.domain.Dto.MemberDto;
import co.kr.board.domain.Dto.TokenDto;
import co.kr.board.repository.CategoryRepository;
import co.kr.board.repository.RefreshTokenRepository;
import co.kr.board.service.BoardService;
import co.kr.board.config.security.auth.CustomUserDetails;
import co.kr.board.domain.Member;
import co.kr.board.domain.Role;
import co.kr.board.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private BoardService boardService;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    private CustomUserDetails customUserDetails;
    private final TestCustomUserDetailsService testCustomUserDetailsService = new TestCustomUserDetailsService();
    private Category category;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        memberRepository.save(memberDto());
        customUserDetails = (CustomUserDetails)testCustomUserDetailsService.loadUserByUsername(memberDto().getUsername());
        System.out.println(customUserDetails);
        category = categoryRepository.findByName(categoryDto().getName());
    }

    @DisplayName("[api]게시글 목록(페이징+검색)-성공")
    @Test
    public void controllerApiBoardListPagingSearchTest()throws Exception{
        String keyword = "test";
        String accessToken = tokenDto().getAccessToken();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(),"",customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(memberDto()));
        given(boardService.findAllSearch(eq(keyword),any(Pageable.class))).willReturn(Page.empty());


        mockMvc.perform(
                get("/api/board/list/search")
                        .param("searchVal",keyword)
                        .header("X-AUTH-TOKEN",accessToken))
                .andExpect(status().isOk())
                .andDo(print());

        then(boardService.findAllSearch(eq(keyword),any(Pageable.class)));
    }

    @DisplayName("[api] 게시글 단일조회-성공")
    @Test
    public void controllerDetailApiTest()throws Exception{

        int boardId = 4;
        String accessToken = tokenDto().getAccessToken();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(),"",customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(memberDto()));
        given(boardService.getBoard(boardId)).willReturn(boardResponseDto());

        mockMvc
                .perform(get("/api/board/detail/{boardId}",boardId)
                .header("X-AUTH-TOKEN",accessToken))
                .andExpect(status().isOk())
                .andDo(print());

        verify(boardService).getBoard(boardId);
    }

    @DisplayName("[api]게시글 작성-성공")
    @Test
    public void controllerPostViewProcTest()throws Exception{

        String accessToken = tokenDto().getAccessToken();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(),"",customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(memberDto()));

        mockMvc.perform(multipart("/api/board/save")
                        .part())
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]게시물 수정")
    public void boardUpdateApiTest()throws Exception{
        String accessToken = tokenDto().getAccessToken();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(),"",customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(memberDto()));

        mockMvc.perform(patch("/api/board/update/{id}",1)
                .with(user(customUserDetails))
                .header("X-AUTH-TOKEN",accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8).content(objectMapper.writeValueAsString(boardRequestDto())))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("[api]게시물 삭제-성공")
    public void boardDeleteApiTest()throws Exception{
        String accessToken = tokenDto().getAccessToken();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(),"",customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(memberDto()));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/board/delete/{id}",1)
                        .header("X-AUTH-TOKEN", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //게시물 요청 dto
    private BoardDto.BoardRequestDto boardRequestDto(){
        return BoardDto.BoardRequestDto
                .builder()
                .boardTitle("테스트")
                .boardContents("sosdddfdfd")
                .createdAt(LocalDateTime.now())
                .readCount(1)
                .build();
    }
    //게시물 응답 dto
    private BoardDto.BoardResponseDto  boardResponseDto(){

        Board board = Board.builder()
                .boardId(4)
                .boardAuthor("well")
                .boardContents("test")
                .boardTitle("testTitle")
                .readcount(0)
                .member(memberDto())
                .category(categoryDto())
                .createdat(LocalDateTime.now()).build();

        return BoardDto.BoardResponseDto
                .builder()
                .board(board)
                .build();
    }
    //회원 dto
    private Member memberDto(){
        return Member.builder()
                .id(1)
                .username("well4149")
                .membername("tester1")
                .password("$2a$12$XcIiB0doaPMx0AoRv0G0f.enty5bjsZADwrmw7SmgNZuI4yQVmRSu")
                .useremail("well123@Test.com")
                .role(Role.ROLE_ADMIN)
                .createdAt(LocalDateTime.now())
                .build();
    }
    //카테고리 dto
    private Category categoryDto(){
        return Category.builder()
                .id(1)
                .name("freeboard")
                .parent(null)
                .build();
    }

    private TokenDto tokenDto(){
        TokenDto result = jwtTokenProvider.createTokenDto(memberDto().getUsername(),Role.ROLE_ADMIN);
        String accessToken = result.getAccessToken();
        String refreshToken = result.getRefreshToken();

        return TokenDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
