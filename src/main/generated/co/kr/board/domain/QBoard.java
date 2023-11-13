package co.kr.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 1623727881L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final QBaseTime _super = new QBaseTime(this);

    public final ListPath<AttachFile, QAttachFile> attachFiles = this.<AttachFile, QAttachFile>createList("attachFiles", AttachFile.class, QAttachFile.class, PathInits.DIRECT2);

    public final StringPath boardAuthor = createString("boardAuthor");

    public final StringPath boardContents = createString("boardContents");

    public final ListPath<Scrap, QScrap> boardScrap = this.<Scrap, QScrap>createList("boardScrap", Scrap.class, QScrap.class, PathInits.DIRECT2);

    public final StringPath boardTitle = createString("boardTitle");

    public final QCategory category;

    public final ListPath<Comment, QComment> commentlist = this.<Comment, QComment>createList("commentlist", Comment.class, QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final SetPath<BoardHashTag, QBoardHashTag> hashtags = this.<BoardHashTag, QBoardHashTag>createSet("hashtags", BoardHashTag.class, QBoardHashTag.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> liked = createNumber("liked", Integer.class);

    public final SetPath<Like, QLike> likes = this.<Like, QLike>createSet("likes", Like.class, QLike.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final NumberPath<Integer> readCount = createNumber("readCount", Integer.class);

    public final QMember writer;

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category"), inits.get("category")) : null;
        this.writer = inits.isInitialized("writer") ? new QMember(forProperty("writer")) : null;
    }

}

