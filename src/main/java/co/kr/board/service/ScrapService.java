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

    /*
      * 스크랩 중복여부
      * 스크랩 버튼을 눌렀을 경우 게시글이 스크랩이 되었는지 아닌지를 확인하는 기능
      * @param boardId : 게시글 번호
     */
    @Transactional(readOnly = true)
    public boolean ScrapDuplicated(Integer boardId){
        return scrapRepository.findByBoardAndMember(getBoard(boardId),getMember()).isPresent();
    }

    /*
      * 스크랩 목록
      * 마이페이지에서 게시글을 스크랩한 목록을 보여주는 기능
      * @param username : 회원 아이디
      * @param pageable : 페이징 객체
      * @return : Page<ScrapDto.ResponseDto>
    */
    @Transactional(readOnly = true)
    public Page<ScrapDto.ResponseDto>scrapList(String username,Pageable pageable){
        Page<ScrapDto.ResponseDto>list =scrapRepository.ScrapList(username,pageable);
        return list;
    }

    /*
      * 스크랩 추가
      * 스크랩 중복 결과를 거친 후 중복이 없는 경우에 스크랩을 추가.
      * @param boardId : 게시글 번호
      * @return : 스크랩 완료
     */
    @Transactional
    public String scrapAdd(Integer boardId){
        scrapRepository.save(Scrap.builder().board(getBoard(boardId)).member(getMember()).build());
        return scrapAddMessage;
    }

    /*
      * 스크랩 삭제
      * 마이페이지에서 스크랩 목록에서 스크랩을 삭제하는 기능
      * @param boardId : 게시글 번호
      * @return : 스크랩 취소 처리 완료
     */
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
