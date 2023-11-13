SELECT * FROM member;
INSERT INTO jpaboard.member (id, created_at, membername, password, role, useremail, userid) VALUES (1, '2023-11-05 12:01:49', 'tester1', '$2a$12$XcIiB0doaPMx0AoRv0G0f.enty5bjsZADwrmw7SmgNZuI4yQVmRSu', 'ROLE_ADMIN', 'well123@Test.com', 'well4149');
INSERT INTO jpaboard.member (id, created_at, membername, password, role, useremail, userid) VALUES (2, '2022-10-23 14:10:15', 'updateuser1', '$2a$10$XXpimye2qVTj7fCbiM4Uge5Uanx9.DVQtOmta2CzxseTfZ6T3Te8.', 'ROLE_USER', 'well4149@naver.com', 'well322');
INSERT INTO jpaboard.member (id, created_at, membername, password, role, useremail, userid) VALUES (274, '2023-03-18 21:13:57', 'user2', '$2a$10$fop70cAwVrKTyT87lfMO4eNGtrT0dP4lHbWmzUlTJuZ2ef2ZATfoO', 'ROLE_USER', 'springboot0924@gmail.com', 'sleep');

SELECT * FROM category;
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (1, 'board', null);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (2, 'freeboard', 1);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (4, 'noticeboard', 1);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (5, 'java', null);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (6, 'CS', null);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (7, '데이터 베이스', 6);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (8, '알고리즘', 6);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (9, '운영체제', 6);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (10, '네트워크', 6);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (11, '웹개발', null);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (12, 'Spring', 11);
INSERT INTO jpaboard.category (category_id, name, parent_id) VALUES (13, 'Web', 11);

SELECT * FROM board;
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (1, '2023-11-01 18:18:10', 'well4149', 'test', 'test titlte', 67, 1, 1, 1, 'qwer4149!');
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (4, '2023-10-08 16:25:57', 'well4149', '수정내용Q', '수정제목', 45, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (5, '2022-10-22 00:06:52', 'well4149', '트트', '테스트', 62, 1, -3, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (12, '2022-10-25 10:28:37', 'well', 'test', 'test', 5, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (19, '2022-10-27 10:41:03', 'well', 'dfwf', 'tre(test)', 20, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (329, '2023-02-05 14:57:06', 'well4149', '내용24', '제목테스트>??', 1, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (331, '2023-02-05 14:57:07', 'well4149', '내용3', '제목테스트??', 1, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (332, '2023-02-07 23:11:40', 'well4149', '내용24', '제목테스트>??', 3, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (334, '2023-02-07 23:11:40', 'well4149', '내용3', '제목테스트??', 38, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (335, '2023-02-13 23:26:12', 'well4149', '내용', 'test tiele', 1, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (369, '2023-02-18 10:47:48', 'well4149', '내용1', 'test', 2, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (382, '2023-03-05 21:18:25', 'well4149', '내용1', 'test', 18, 1, 1, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (383, '2023-03-05 21:24:02', 'well4149', 'upload test', 'springboot test', 16, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (386, '2023-03-12 20:57:57', 'well4149', 'upload test', 'springboot test', 17, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (387, '2023-03-13 21:13:03', 'well4149', '글작성 테스트', '글작성', 16, 1, 0, 2, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (388, '2023-03-15 00:44:40', 'well4149', '내용24', '제목테스트>??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (390, '2023-03-15 00:48:11', 'well4149', '내용3', '제목테스트??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (391, '2023-03-15 00:51:18', 'well4149', '내용24', '제목테스트>??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (393, '2023-03-15 00:51:18', 'well4149', '내용3', '제목테스트??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (394, '2023-03-15 00:52:59', 'well4149', '내용24', '제목테스트>??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (396, '2023-03-15 00:52:59', 'well4149', '내용3', '제목테스트??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (424, '2023-05-22 00:12:36', 'well4149', '내용24', '제목테스트>??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (426, '2023-05-22 00:12:36', 'well4149', '내용3', '제목테스트??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (427, '2023-05-22 00:14:54', 'well4149', '내용24', '제목테스트>??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (429, '2023-05-22 00:14:55', 'well4149', '내용3', '제목테스트??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (430, '2023-10-01 14:46:21', 'well4149', '내용24', '제목테스트>??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (432, '2023-10-01 14:46:21', 'well4149', '내용3', '제목테스트??', 0, 1, 0, 1, null);
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (433, '2023-11-05 00:02:41', 'well4149', 'test입니다.', 'test', 0, 1, 0, 4, '1234');
INSERT INTO jpaboard.board (board_id, created_at, board_author, board_contents, board_title, read_count, useridx, liked, category_id, board_pw) VALUES (435, '2023-11-08 21:34:17', 'well4149', 'test', 'hashtag test', 11, 1, 0, 2, '1234');

SELECT * FROM file;
INSERT INTO jpaboard.file (id, created_at, file_path, file_size, origin_file_name, board_id) VALUES (31, '2023-03-05 21:18:25', 'C:\\upload\\\\825443066852500..png', 230767, '스크린샷(29).png', 382);
INSERT INTO jpaboard.file (id, created_at, file_path, file_size, origin_file_name, board_id) VALUES (32, '2023-03-05 21:18:25', 'C:\\upload\\\\825443073357900..png', 230776, '스크린샷(30).png', 382);
INSERT INTO jpaboard.file (id, created_at, file_path, file_size, origin_file_name, board_id) VALUES (33, '2023-03-05 21:24:02', 'C:\\upload\\\\825780411288900.txt', 404, 'jpa 공부기록.txt', 383);
INSERT INTO jpaboard.file (id, created_at, file_path, file_size, origin_file_name, board_id) VALUES (34, '2023-03-05 21:24:02', 'C:\\upload\\\\825780413142500.txt', 2293, 'jpa 블로그.txt', 383);
INSERT INTO jpaboard.file (id, created_at, file_path, file_size, origin_file_name, board_id) VALUES (35, '2023-03-05 21:24:02', 'C:\\upload\\\\825780414571600.txt', 1050, 'ㄴㅇㄴ.txt', 383);

SELECT * FROM reply;
INSERT INTO reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (2, '2022-10-23 14:19:31', 'update!!', 'well', 5, 1);
INSERT INTO reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (5, '2022-10-24 01:24:20', 'test!', 'well4149', 5, 2);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (11, '2022-10-27 10:41:25', 'sd', 'well4149', 12, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (326, '2023-02-05 11:03:22', 'ssss', 'well4149', 12, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (329, '2023-02-05 14:57:14', '댓글작성!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (330, '2023-02-05 14:57:14', 'update!!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (331, '2023-02-05 14:57:14', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (332, '2023-02-05 14:57:14', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (334, '2023-02-07 23:11:51', '댓글작성!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (335, '2023-02-07 23:11:51', 'update!!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (336, '2023-02-07 23:11:51', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (337, '2023-02-07 23:11:51', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (340, '2023-02-18 10:16:29', '댓글작성!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (341, '2023-02-18 10:16:29', 'update!!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (342, '2023-02-18 10:16:29', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (343, '2023-02-18 10:16:29', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (345, '2023-02-20 23:30:14', '댓글작성!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (346, '2023-02-20 23:30:14', 'update!!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (347, '2023-02-20 23:30:14', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (348, '2023-02-20 23:30:14', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (350, '2023-03-09 23:57:23', '댓글작성!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (351, '2023-03-09 23:57:24', 'update!!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (352, '2023-03-09 23:57:24', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (353, '2023-03-09 23:57:24', 'deleteTest!', 'well4149', 5, 1);
INSERT INTO jpaboard.reply (reply_id, created_at, reply_contents, reply_writer, board_id, useridx) VALUES (355, '2023-03-11 17:35:08', '댓글작성!', 'well4149', 5, 1);

SELECT * FROM likes;

INSERT INTO jpaboard.likes (id, board_id, useridx, like_status) VALUES (57, 5, 2, true);
INSERT INTO jpaboard.likes (id, board_id, useridx, like_status) VALUES (58, 5, 274, true);
INSERT INTO jpaboard.likes (id, board_id, useridx, like_status) VALUES (65, 382, 1, true);
INSERT INTO jpaboard.likes (id, board_id, useridx, like_status) VALUES (88, 1, 1, TRUE);

SELECT *  FROM scrap;

INSERT INTO jpaboard.scrap (id, created_at, useridx, board_id) VALUES (5, '2023-11-02 00:21:18', 1, 1);

SELECT * FROM visitors;
INSERT INTO jpaboard.visitors (id, login_date_time, username, useridx) VALUES (1, '2023-11-06 21:21:28', 'well4149', 1);
INSERT INTO jpaboard.visitors (id, login_date_time, username, useridx) VALUES (5, '2023-11-07 01:02:49', 'well4149', 1);

SELECT * FROM hash_tag;
INSERT INTO jpaboard.hash_tag (hashtag_id, created_at, hashtag_name) VALUES (1, null, 'spring');
INSERT INTO jpaboard.hash_tag (hashtag_id, created_at, hashtag_name) VALUES (2, null, 'jquery');
INSERT INTO jpaboard.hash_tag (hashtag_id, created_at, hashtag_name) VALUES (3, null, 'java');
INSERT INTO jpaboard.hash_tag (hashtag_id, created_at, hashtag_name) VALUES (4, null, 'jpa');

SELECT * FROM article_hashtag;

