package co.kr.board.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitors")
@Getter
@NoArgsConstructor
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private LocalDateTime loginDateTime;
    @ManyToOne
    @JoinColumn(name = "useridx",nullable = false)
    private Member member;

    @Builder
    public Visitor(Integer id,String username,LocalDateTime loginDateTime,Member member){
        this.id = id;
        this.username = username;
        this.loginDateTime = loginDateTime;
        this.member = member;
    }
}
