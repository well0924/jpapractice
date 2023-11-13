package co.kr.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHashTag is a Querydsl query type for HashTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHashTag extends EntityPathBase<HashTag> {

    private static final long serialVersionUID = 1974925071L;

    public static final QHashTag hashTag = new QHashTag("hashTag");

    public final QBaseTime _super = new QBaseTime(this);

    public final SetPath<BoardHashTag, QBoardHashTag> articles = this.<BoardHashTag, QBoardHashTag>createSet("articles", BoardHashTag.class, QBoardHashTag.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath hashtagName = createString("hashtagName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QHashTag(String variable) {
        super(HashTag.class, forVariable(variable));
    }

    public QHashTag(Path<? extends HashTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHashTag(PathMetadata metadata) {
        super(HashTag.class, metadata);
    }

}

