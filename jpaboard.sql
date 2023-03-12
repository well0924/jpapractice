SELECT * FROM board;

#insert test
INSERT INTO board(board_author,board_contents,board_title, created_at,read_count)VALUES('작성자','내용','제목',NOW(),0);

SELECT * FROM reply;

SELECT * FROM member;

INSERT INTO reply(reply_contents,board_id,created_at)VALUES('test!!!',1,NOW());
