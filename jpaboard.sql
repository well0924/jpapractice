SELECT * FROM board;

INSERT INTO board(board_author,board_contents,board_title, created_at,read_count)
VALUES('작성자','내용','제목',NOW(),0);