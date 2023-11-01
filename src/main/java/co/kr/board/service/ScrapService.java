package co.kr.board.service;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Board;
import co.kr.board.domain.Dto.ScrapDto;
import co.kr.board.domain.Member;
import co.kr.board.domain.Scrap;
import co.kr.board.repository.BoardRepository;
import co.kr.board.repository.MemberRepository;
import co.kr.board.repository.ScrapRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@AllArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final String scrapAddMessage ="스크랩 완료";
    private final String scrapCancelMessage ="스크랩 취소 처리 완료";


    //스크랩 중복여부
    @Transactional(readOnly = true)
    public boolean ScrapDuplicated(Integer boardId){
        return scrapRepository.findByBoardAndMember(getBoard(boardId),getMember()).isPresent();
    }

    //스크랩 목록
    @Transactional(readOnly = true)
    public Page<ScrapDto.ResponseDto>scrapList(Pageable pageable){
        Page<Scrap>list =scrapRepository.findAll(pageable);
        return list.map(scrap -> new ScrapDto.ResponseDto(scrap));
    }

    //스크랩 추가
    @Transactional
    public String scrapAdd(Integer boardId){
        scrapRepository.save(Scrap.builder().board(getBoard(boardId)).member(getMember()).build());
        return scrapAddMessage;
    }

    //스크랩 삭제
    @Transactional
    public String scrapCancel(Integer boardId){
        Scrap scrap = scrapRepository.findByBoardAndMember(getBoard(boardId),getMember()).orElseThrow();
        scrapRepository.delete(scrap);
        return scrapCancelMessage;
    }

    /*
     * 회원 인증
     */
    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName().toString();
        log.info(username);
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
        return member;
    }
    
    /*
     * 게시글 조회
     */
    private Board getBoard(Integer boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL));
        return board;
    }
}
