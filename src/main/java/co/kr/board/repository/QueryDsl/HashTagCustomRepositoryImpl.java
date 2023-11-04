package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.HashTag;
import co.kr.board.domain.QHashTag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class HashTagCustomRepositoryImpl extends QuerydslRepositorySupport implements HashTagCustomRepository{

    public HashTagCustomRepositoryImpl() {
        super(HashTag.class);
    }

    @Override
    public List<String> findAllHashtagNames() {
        QHashTag hashTag = QHashTag.hashTag;

        return from(hashTag)
                .select(hashTag.hashtagName)
                .fetch();
    }
}
