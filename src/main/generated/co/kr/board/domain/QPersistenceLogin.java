package co.kr.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersistenceLogin is a Querydsl query type for PersistenceLogin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersistenceLogin extends EntityPathBase<PersistenceLogin> {

    private static final long serialVersionUID = 1579480071L;

    public static final QPersistenceLogin persistenceLogin = new QPersistenceLogin("persistenceLogin");

    public final DateTimePath<java.time.LocalDateTime> lastUsed = createDateTime("lastUsed", java.time.LocalDateTime.class);

    public final StringPath series = createString("series");

    public final StringPath token = createString("token");

    public final StringPath username = createString("username");

    public QPersistenceLogin(String variable) {
        super(PersistenceLogin.class, forVariable(variable));
    }

    public QPersistenceLogin(Path<? extends PersistenceLogin> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersistenceLogin(PathMetadata metadata) {
        super(PersistenceLogin.class, metadata);
    }

}

