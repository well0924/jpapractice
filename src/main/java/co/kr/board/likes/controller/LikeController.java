package co.kr.board.likes.controller;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.dto.Response;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.likes.service.LikeService;
import co.kr.board.login.domain.Member;
import co.kr.board.login.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/like/*")
public class LikeController {
    private final LikeService likeService;
    private final MemberRepository memberRepository;
    
    //좋아요기능
    @PostMapping("/{id}")
    public Response likeBoard(@PathVariable("id")Integer boardId,Member member){
        member = getMember();
        String likeResult =likeService.updateLikeOfBoard(boardId,member);
        return new Response(HttpStatus.OK.value(),likeResult);
    }
    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
        return member;
    }
}
