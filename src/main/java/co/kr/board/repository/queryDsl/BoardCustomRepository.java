package co.kr.board.repository.queryDsl;

import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Const.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardCustomRepository {

    //게시글 목록(전체)
    Page<BoardDto.BoardResponseDto>findAllBoardList(Pageable pageable);

    //게시글 목록(카테고리 및 정렬 기능)
    Page<BoardDto.BoardResponseDto>findAllBoardList(String categoryName,Pageable pageable);

    //게시글 검색(카테고리 ,검색 ,정렬)
    Page<BoardDto.BoardResponseDto> findByAllSearch(String searchVal, SearchType searchType , Pageable pageable);

    //내가 작성한 글 목록
    Page<BoardDto.BoardResponseDto>findByAllContents(String username,Pageable pageable);

    //최근에 작성한 글(5개)
    List<BoardDto.BoardResponseDto>findTop5ByOrderByBoardIdDescCreatedAtDesc();

    //게시글 이전글/다음글
    List<BoardDto.BoardResponseDto>findNextPreviousBoard(Integer id);

    //해시태그에 관련된 게시글 목록
    Page<BoardDto.BoardResponseDto>findAllHashTagWithBoard(String tagName,Pageable pageable);

    //게시글 선택삭제
    void deleteAllByBoard(List<Integer>boardId);
    
    //게시글 단일 조회
    Optional<BoardDto.BoardResponseDto>findByBoardDetail(Integer boardId);

    //조회수 증가
    void updateReadCount(Integer boardId);

    //비밀글 확인
    BoardDto.BoardResponseDto passwordCheck(String password,Integer boardId);
}
