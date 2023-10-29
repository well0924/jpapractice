package co.kr.board.domain.Const;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SearchType {
    //제목
    TITLE,
    //회원 아이디
    ID,
    //작성자
    AUTHOR,
    CONTENTS,
    //회원 이름
    MEMBERNAME,
    EMAIL,
    ALL;

    public static SearchType toSearch(String searchType){
        return switch (searchType){
            case "boardTitle"->TITLE;
            case "id"->ID;
            case "memberName"->MEMBERNAME;
            case "boardAuthor"->AUTHOR;
            case "memberEmail"->EMAIL;
            case "boardContents"->CONTENTS;
            default -> ALL;
        };
    }
}
