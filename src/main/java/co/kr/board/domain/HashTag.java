package co.kr.board.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@Table(indexes = {
        @Index(columnList = "hashtagName", unique = true),
        @Index(columnList = "createdAt")
})
@ToString
@NoArgsConstructor
public class HashTag extends BaseTime{

    @Id
    @Column(name = "hashtag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Board>articles = new LinkedHashSet<>();

    @Setter
    private String hashtagName;

    public static HashTag hashTag(String hashtagName){
        return new HashTag(hashtagName);
    }

    @Builder
    public HashTag(Integer id, String hashtagName, LocalDateTime createdAt){
        this.id = id;
        this.hashtagName = hashtagName;
        this.getCreatedAt();
    }

    public HashTag(String hashtagName) {
        this.hashtagName = hashtagName;
    }
}
