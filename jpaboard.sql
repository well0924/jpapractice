SELECT * FROM board;

#insert test
INSERT INTO board(board_author,board_contents,board_title, created_at,read_count)
VALUES('작성자','내용','제목',NOW(),0);

SELECT * FROM board WHERE board_id = 10;

UPDATE board SET board_title = 'update title',board_contents='update_contents', board_author='??' WHERE board_id = 10;


SELECT * FROM reply;

INSERT INTO reply(reply_contents,board_id,created_at)VALUES('test!!!',1,NOW());

SELECT * FROM reply WHERE board_id = 22;