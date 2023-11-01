package co.kr.board.domain;

import co.kr.board.domain.Dto.BoardDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Entity
@ToString(callSuper = true)
@Table(name="board",indexes = {
        @Index(columnList = "board_id"),
        @Index(columnList = "board_title"),
        @Index(columnList = "board_contents")
})
@RequiredArgsConstructor
public class Board extends BaseTime implements Serializable {
    //redis 에 객체를 저장을 하면 내부적으로 직렬화가 되어서 저장이 된다.
    //entity 객체중 lazy 로 로딩이 되는 경우에는 @Proxy(lazy = false)를 선언해줘야 한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_id",nullable = false)
    private Integer id;
    @Column(name = "board_title",nullable = false)
    private String boardTitle;
    @Column(name = "board_contents",nullable = false)
    @Lob
    private String boardContents;
    @Column(name = "board_author",nullable = false)
    private String boardAuthor;
    @Column(name = "read_count",nullable = false)
    private Integer readCount;
    @Column(nullable = false)
    private Integer liked;//추천수
    @Column(name = "board_pw",length = 10)
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    //회원
    @ManyToOne(fetch =FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="useridx")
    @ToString.Exclude
    private Member writer;

    //댓글
    @ToString.Exclude
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    private final List<Comment> commentlist = new ArrayList<>();

    //파일첨부(게시글을 삭제하면 파일도 삭제)
    //orphanRemoval 를 true 로 설정을 하면 게시글을 삭제시 파일도 같이 삭제
    //orphanRemoval 과 CaseCadeType.REMOVE 차이점 알아보기.
    @ToString.Exclude
    @OneToMany(mappedBy = "board",cascade = {CascadeType.ALL},orphanRemoval = true,fetch = FetchType.LAZY)
    private final List<AttachFile>attachFiles = new ArrayList<>();

    //좋아요
    @ToString.Exclude
    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private final Set<Like> likes = new HashSet<>();

    //카테고리
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    //해시태그
    @JoinTable(
            name = "article_hashtag",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private Set<HashTag> hashtags = new LinkedHashSet<>();

    //스크랩
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Scrap>boardScrap = new ArrayList<>();

    @Builder
    public Board(Integer boardId,String boardTitle,String boardAuthor,String boardContents,Integer readcount,String password,Category category,LocalDateTime createdat,Member member) {
        this.id = boardId;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardAuthor = member.getUsername();
        this.readCount = readcount;
        this.category = category;
        this.liked = likes.size();
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.writer = member;
    }

    //게시글 조회수 증가
    public void countUp() {
        this.readCount ++;
    }

    //게시글 수정
    public void updateBoard(BoardDto.BoardRequestDto dto) {
        this.boardTitle = dto.getBoardTitle();
        this.boardContents =dto.getBoardContents();
        this.password = dto.getPassword();
        this.createdAt = LocalDateTime.now();
    }
    
    //비밀번호 수정
    public void passwordChange(BoardDto.BoardRequestDto dto){
        this.password = dto.getPassword();
    }

    //첨부 파일 추가
    public void addAttach(AttachFile attachFile){
        this.attachFiles.add(attachFile);
        if(attachFile.getBoard()!=this){
            attachFile.setBoard(this);
        }
    }
    //좋아요 추가
    public void increaseLikeCount(){
        this.liked +=1;
    }
    //좋아요 취소
    public void decreaseLikeCount(){
        this.liked -=1;
    }

    //해시태그 추가
    public void addHashTag(HashTag hashTag){
        this.getHashtags().add(hashTag);
    }
    public void addHashTags(Collection<HashTag>hashTags){
        this.getHashtags().addAll(hashTags);
    }
    //해시태그 삭제
    public void clearHashTag(){
        this.getHashtags().clear();
    }
}
