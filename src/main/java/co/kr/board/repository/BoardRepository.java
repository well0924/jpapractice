package co.kr.board.repository;

import co.kr.board.domain.Board;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.repository.QueryDsl.BoardCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BoardRepository extends JpaRepository<Board, Integer>, BoardCustomRepository {

	//게시글 전체갯수
	@Query(value = "select count(*) from Board b")
	Integer ArticleCount();

	//카테고리별 게시글 갯수
	@Query(value = "select count(*) from Board b where b.category.name = :name")
	Integer categoryCount(@Param("name") String categoryName);

	//비밀번호 여부 확인
	@Query(value = "select b.password from Board b where b.id = :id")
	String boardPasswordCheck(@Param("id") Integer boardId);

	//무한 스크롤 test
	@Query(value = "select b from Board b")
	Slice<BoardDto.BoardResponseDto>findAllList(Pageable pageable);
}
