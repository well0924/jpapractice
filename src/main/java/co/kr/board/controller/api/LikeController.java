package co.kr.board.controller.api;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.dto.Response;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.config.redis.CacheKey;
import co.kr.board.domain.Board;
import co.kr.board.repository.BoardRepository;
import co.kr.board.service.LikeService;
import co.kr.board.domain.Member;
import co.kr.board.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@RestController
@AllArgsConstructor
@RequestMapping("/api/like/*")
public class LikeController {
    private final BoardRepository boardRepository;
    private final LikeService likeService;
    private final MemberRepository memberRepository;
    
    //좋아요+1기능
    @PostMapping("/plus/{id}")
    public Response<String> likeBoard(@PathVariable(value = "id")Integer boardId){
        Member member = getMember();
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL));
        String likeResult =likeService.createLikeBoard(board, member);
        return new Response<>(HttpStatus.OK.value(),likeResult);
    }
    //좋아요-1기능
    @PostMapping("/minus/{id}")
    public Response<String> minusBoard(@PathVariable(value = "id")Integer boardId){
        Member member = getMember();
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL));
        String likeResult = likeService.removeLikeBoard(board,member);
        return new Response<>(HttpStatus.OK.value(),likeResult);
    }
    //좋아요 중복기능
    @GetMapping("/duplicated/{id}")
    @Caching(cacheable = {@Cacheable(value = CacheKey.LIKE,key = "#boardId",unless = "#result == null")},evict = {@CacheEvict(value = CacheKey.LIKE,key = "#boardId")})
    public Response<Boolean> likeDuplicated(@PathVariable(value = "id") Integer boardId){
        Member member = getMember();
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL));
        boolean result = likeService.hasLikeBoard(board, member);

        if(result == true){
            minusBoard(boardId);
        }else{
            likeBoard(boardId);
        }
        return new Response<>(HttpStatus.OK.value(),result);
    }
    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
        return member;
    }
}
