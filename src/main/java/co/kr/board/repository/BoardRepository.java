package co.kr.board.repository;

import co.kr.board.domain.Board;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.repository.QueryDsl.BoardCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Integer>, BoardCustomRepository {

	//게시글 단일 조회
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value = "select b from Board b where b.id = :boardId")
	Optional<Board>findByboardId(@Param("boardId") Integer boardId);
	//조회수 증가
	@Modifying
	@Transactional
	@Query(value = "update Board b set b.readCount = b.readCount+1 where b.id = :id")
	void updateByReadCount(@Param("id") Integer boardId);

	//게시글 전체갯수
	@Query(value = "select count(*) from Board b")
	Integer ArticleCount();

	//카테고리별 게시글 갯수
	@Query(value = "select count(*) from Board b where b.category.name = :name")
	Integer categoryCount(@Param("name") String categoryName);

	//게시글 선택삭제
	@Transactional
	@Modifying
	@Query(value = "delete from Board b where b.id in :boardId")
	void deleteAllById(@Param("boardId") List<Integer>boardId);

	//비밀글 확인
	@Query(value = "select b from Board b where b.id = :id and b.password = :pw")
	BoardDto.BoardResponseDto passwordCheck(@Param("pw") String password,@Param("id") Integer boardId);

	//비밀번호 여부 확인
	@Query(value = "select b.password from Board b where b.id = :id")
	String boardPasswordCheck(@Param("id") Integer boardId);

}
