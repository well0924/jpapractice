package co.kr.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    private Set<Board>articles = new LinkedHashSet<>();

    @Setter
    private String hashtagName;


}
