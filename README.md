# 스프링 jpa 게시판

### 목표
 
  1. jpa에 적응을 하기 위해서 간단한 게시판을 만드는 것을 목표로 한다.


  2. 기능은 간단한 crud기능 및 댓글 그리고 로그인을 기준으로 한다.(추후 추가적인 기능을 할 예정)

  
  3. 기능이 다 구현된 뒤에는 aws를 사용해서 배포를 한다.

  
### 요구사항

1.게시판
	
	목록 페이지: 게시글 페이징,검색 기능을 할 수 있다.
	
	글 작성 페이지: 작성자,내용,제목을 입력하지 않는 경우 경고가 나오도록 유효성 처리
	
	글 수정 페이지: 글 수정과 삭제를 할 수 있게 하는 페이지
	
	화면을 랜더링하기 전 postman으로 데이터 검증

2.회원가입 & 로그인 
	
	2-1.회원 중복기능: 아이디 입력시 중복여부 확인하기.
	             
	2-2.유효성 검사:  아이디는 최소 8자 이상,알파벳 대소문자(a~z,A~Z),숫자(0~9)를 조합한 아이디
	             
	   	        비밀번호는 최소 8자 이상 알파벳 대소문자,숫자를 포함 
	             
	       		이메일의 경우에는 이메일여부 확인하기.
	             
	2-3.회원가입 기능: 비밀번호를 암호화 해주는 기능
	   
	2-4.로그인 기능(스프링 시큐리티): 로그인 실패시 실패문구 보여주기.
	       
	                               아이디 및 비밀번호 미입력시 유효성 검사 
	          
	                               로그인이 된 경우 게시판 메인 페이지로 이동   
    									 
    							   어드민: 어드민 목록페이지
    									 
    							   유저: 일반 게시판 목록 페이지.
   
    2-5.소셜로그인(카카오):소셜 로그인으로 로그인하는 기능.
	   
3.댓글
	
	3-1.댓글 목록: 게시물에 댓글목록을 보여주기.(수정 및 삭제 버튼은 로그인 한 사람 보여주기)
	
	3-2.댓글 작성: 댓글은 로그인한 회원만 가능
	
	3-3.댓글 유효성검사: 작성시 유효성 검사 필요
	
	3-4.댓글 삭제: 삭제 버튼을 누른경우 댓글 삭제.

				
### 개발 환경

- Back-end: Java11, SpringBoot, Jpa , Jwt, Redis


- DB: MariaDB


- Build: Gradle


- Front-end: Theymleaf,Html,CSS,JQuery


- TestTool: postman


### 개발 기간

2022.09.13~진행중

### 기능

- [x] 게시물 목록

- [x] 글 작성

- [x] 글 삭제

- [x] 글 수정

- [x] 조회수 증가

- [x] 페이징

- [x] 검색(QueryDsl적용해서 검색 추가하기.->변경 완료)

- [x] 댓글(목록,작성,삭제)

- [x] 회원 가입(아이디중복,이메일중복)

- [x] 아이디 찾기

- [x] 로그인(시큐리티+jwt)

- [x] 회원 탈퇴,수정기능

- [x] Redis를 활용한 RefreshToken 관리

- [x] Redis를 활용한 cache 처리.

- [ ] 파일 첨부 기능

- [ ] 좋아요 기능

- [ ] 게시물 카테고리 기능

- [ ] 어드민 페이지 배치(휴먼회원 전환)

- [ ] 카카오 소셜 로그인 기능



