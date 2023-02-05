package co.kr.board.board.domain;

import lombok.Getter;

@Getter
public enum SearchType {
    TITLE("글 제목"),CONTENTS("글 내용"),TITCONTENTS("제목 + 내용");

    String description;

    SearchType(String description) {
        this.description = description;
    }
}
