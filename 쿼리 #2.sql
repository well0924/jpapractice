SELECT * FROM member;
SELECT * FROM board;
SELECT * FROM reply;

INSERT INTO board(board_id,board_author,board_contents,board_title,read_count,created_at,useridx)
VALUES('well4149','test','testtitle',1,NOW(),1);
