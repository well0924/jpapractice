package co.kr.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVisitor is a Querydsl query type for Visitor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVisitor extends EntityPathBase<Visitor> {

    private static final long serialVersionUID = 1744168913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVisitor visitor = new QVisitor("visitor");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> loginDateTime = createDateTime("loginDateTime", java.time.LocalDateTime.class);

    public final QMember member;

    public final StringPath username = createString("username");

    public QVisitor(String variable) {
        this(Visitor.class, forVariable(variable), INITS);
    }

    public QVisitor(Path<? extends Visitor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVisitor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVisitor(PathMetadata metadata, PathInits inits) {
        this(Visitor.class, metadata, inits);
    }

    public QVisitor(Class<? extends Visitor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

