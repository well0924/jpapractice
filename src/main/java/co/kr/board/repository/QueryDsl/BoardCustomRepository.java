package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
}
