package co.kr.board.board.domain;

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

    private static final long serialVersionUID = -653239471L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final QBaseTime _super = new QBaseTime(this);

    public final ListPath<co.kr.board.file.domain.AttachFile, co.kr.board.file.domain.QAttachFile> attachFiles = this.<co.kr.board.file.domain.AttachFile, co.kr.board.file.domain.QAttachFile>createList("attachFiles", co.kr.board.file.domain.AttachFile.class, co.kr.board.file.domain.QAttachFile.class, PathInits.DIRECT2);

    public final StringPath boardAuthor = createString("boardAuthor");

    public final StringPath boardContents = createString("boardContents");

    public final StringPath boardTitle = createString("boardTitle");

    public final co.kr.board.category.domain.QCategory category;

    public final ListPath<co.kr.board.reply.domain.Comment, co.kr.board.reply.domain.QComment> commentlist = this.<co.kr.board.reply.domain.Comment, co.kr.board.reply.domain.QComment>createList("commentlist", co.kr.board.reply.domain.Comment.class, co.kr.board.reply.domain.QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> liked = createNumber("liked", Integer.class);

    public final SetPath<co.kr.board.likes.domain.Like, co.kr.board.likes.domain.QLike> likes = this.<co.kr.board.likes.domain.Like, co.kr.board.likes.domain.QLike>createSet("likes", co.kr.board.likes.domain.Like.class, co.kr.board.likes.domain.QLike.class, PathInits.DIRECT2);

    public final NumberPath<Integer> readCount = createNumber("readCount", Integer.class);

    public final co.kr.board.login.domain.QMember writer;

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
        this.category = inits.isInitialized("category") ? new co.kr.board.category.domain.QCategory(forProperty("category"), inits.get("category")) : null;
        this.writer = inits.isInitialized("writer") ? new co.kr.board.login.domain.QMember(forProperty("writer")) : null;
    }

}

