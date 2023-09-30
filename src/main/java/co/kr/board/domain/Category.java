package co.kr.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    //카테고리의 경우에는 self join을 사용한다.
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50,nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    @Builder
    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }
}
