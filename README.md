# 스프링 jpa 게시판

### 목표
 
  1. jpa에 적응을 하기 위해서 간단한 게시판을 만드는 것을 목표로 한다.


  2. 기능은 간단한 crud기능 및 댓글 그리고 로그인을 기준으로 한다.

### 요구사항

1 게시판
	
	목록 페이지: 게시글 페이징,검색 기능을 할 수 있다.
	
	글 작성 페이지: 작성자,내용,제목을 입력하지 않는 경우 경고가 나오도록 유효성 처리
	
	글 수정 페이지: 글 수정과 삭제를 할 수 있게 하는 페이지
	
	화면을 랜더링하기 전 postman으로 데이터 검증

2.회원가입 & 로그인
	
	회원가입 페이지: 아이디 입력시 중복여부를 체크할 수 있는 기능
	             
	                각 입력칸에 유효성 검사처리.
	             
	                아이디는 최소 8자 이상,알파벳 대소문자(a~z,A~Z),숫자(0~9)를 조합한 아이디
	             
	                비밀번호는 최소 8자 이상 알파벳 대소문자,숫자를 포함 
	             
	                이메일의 경우에는 이메일여부 확인하기.
	            
	
	로그인 페이지: 아이디 및 비밀번호를 미입력 또는 일치하지 않는경우 유효성 검사
	          
	             로그인이 된 경우 게시판 메인 페이지로 이동

3.댓글

	댓글 검사: 댓글의 경우에는 로그인을 한 사람에게 보이게 하기.
	             
	          댓글의 작성자 및 내용이 빈칸인 경우 유효성 검사
	             	
	          댓글 수정 및 삭제 버튼의 경우 로그인이된 사람만 보이게 하기             	  		    

				
### 개발 환경

- Back-end: Java11 SpringBoot,Jpa


- DB: MariaDB


- Build: Gradle


- Front-end: Theymleaf,Html,CSS,JQuery


- TestTool: postman


### 개발 기간

2022.09.13~진행중

### 기능

- [x] 글목록

- [x] 글 작성

- [x] 글 삭제

- [x] 글 수정

- [x] 조회수 증가

- [x] 페이징

- [ ] 검색

- [ ] 댓글

- [ ] 회원 가입

- [ ] 로그인(시큐리티)

### 개발 기록

1일차 (2022.09.13)

- 개발 환경 설정 및 controller,service,repository,entity 작성


- 기본적인 crud기능 포스트맨으로 테스트 


- [api 문서](https://documenter.getpostman.com/view/18344373/2s7YYsc4Ea)


- 화면 렌더링 작업(목록 화면)


2일차 (2022.09.14)

- 화면 랜더링 작업(등록,수정화면,조회화면)


- 등록 화면 유효성 검사 및 등록,수정,삭제 기능 


- 댓글 기능(목록,작성,삭제) 포스트맨으로 테스트


- [api 문서](https://documenter.getpostman.com/view/18344373/2s7YYsc4Ea)
