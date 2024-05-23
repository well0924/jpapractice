package co.kr.board.repository.queryDsl;

import java.util.List;

public interface HashTagCustomRepository {
    List<String> findAllHashtagNames();
}
