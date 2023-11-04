package co.kr.board.repository.QueryDsl;

import java.util.List;

public interface HashTagCustomRepository {
    List<String> findAllHashtagNames();
}
