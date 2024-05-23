package co.kr.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardTagId is a Querydsl query type for BoardTagId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBoardTagId extends BeanPath<BoardTagId> {

    private static final long serialVersionUID = -694404916L;

    public static final QBoardTagId boardTagId = new QBoardTagId("boardTagId");

    public final NumberPath<Integer> boardId = createNumber("boardId", Integer.class);

    public final NumberPath<Integer> hashTagId = createNumber("hashTagId", Integer.class);

    public QBoardTagId(String variable) {
        super(BoardTagId.class, forVariable(variable));
    }

    public QBoardTagId(Path<? extends BoardTagId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardTagId(PathMetadata metadata) {
        super(BoardTagId.class, metadata);
    }

}

