package co.kr.board.controller.api;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.dto.Response;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.config.redis.CacheKey;
import co.kr.board.domain.Board;
import co.kr.board.repository.BoardRepository;
import co.kr.board.service.LikeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/like/*")
public class LikeController {
    private final BoardRepository boardRepository;
    private final LikeService likeService;

    //좋아요+1기능
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/plus/{id}")
    @Cacheable(value = CacheKey.LIKE,key = "#boardId",unless = "#result == null")
    public Response<String> likeBoard(@PathVariable(value = "id")Integer boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL));
        String likeResult =likeService.createLikeBoard(board);
        return new Response<>(HttpStatus.OK.value(),likeResult);
    }

    //좋아요-1기능
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @CacheEvict(value = CacheKey.LIKE,key = "#boardId")
    @PostMapping("/minus/{id}")
    public Response<String> minusBoard(@PathVariable(value = "id")Integer boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL));
        String likeResult = likeService.removeLikeBoard(board);
        return new Response<>(HttpStatus.OK.value(),likeResult);
    }

    //좋아요 중복기능
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/duplicated/{id}")
    public Response<Boolean> likeDuplicated(@PathVariable(value = "id") Integer boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL));
        boolean result = likeService.hasLikeBoard(board);
        log.info("result::"+result);
        if(result == true){
            minusBoard(boardId);
        }else{
            likeBoard(boardId);
        }
        return new Response<>(HttpStatus.OK.value(),result);
    }

}
