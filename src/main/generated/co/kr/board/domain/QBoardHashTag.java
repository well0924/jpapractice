package co.kr.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardHashTag is a Querydsl query type for BoardHashTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardHashTag extends EntityPathBase<BoardHashTag> {

    private static final long serialVersionUID = 643657827L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardHashTag boardHashTag = new QBoardHashTag("boardHashTag");

    public final QBoard board;

    public final QHashTag hashTag;

    public final QBoardTagId id;

    public QBoardHashTag(String variable) {
        this(BoardHashTag.class, forVariable(variable), INITS);
    }

    public QBoardHashTag(Path<? extends BoardHashTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardHashTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardHashTag(PathMetadata metadata, PathInits inits) {
        this(BoardHashTag.class, metadata, inits);
    }

    public QBoardHashTag(Class<? extends BoardHashTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.hashTag = inits.isInitialized("hashTag") ? new QHashTag(forProperty("hashTag")) : null;
        this.id = inits.isInitialized("id") ? new QBoardTagId(forProperty("id")) : null;
    }

}

