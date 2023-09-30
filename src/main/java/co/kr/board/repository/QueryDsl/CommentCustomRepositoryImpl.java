package co.kr.board.repository.QueryDsl;

import co.kr.board.domain.Comment;
import co.kr.board.domain.Dto.CommentDto;
import co.kr.board.domain.QCategory;
import co.kr.board.domain.QComment;
import co.kr.board.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CommentCustomRepositoryImpl implements CommentCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    QMember qMember;

    QComment qComment;

    QCategory qCategory;

    public CommentCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.qMember = QMember.member;
        this.qComment = QComment.comment;
        this.qCategory = QCategory.category;
    }

    //최근에 작성한 댓글 5개
    @Override
    public List<CommentDto.CommentResponseDto> findTop5ByOrderByReplyIdCreatedAtDesc() {
        List<Comment> list = jpaQueryFactory
                .select(qComment)
                .from(qComment)
                .orderBy(qComment.id.desc(),qComment.createdAt.desc())
                .limit(5)
                .fetch();

        return list.stream().map(comment->new CommentDto.CommentResponseDto(comment)).collect(Collectors.toList());
    }

}
