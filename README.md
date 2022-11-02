# 스프링 jpa 게시판

### 목표
 
  1. jpa에 적응을 하기 위해서 간단한 게시판을 만드는 것을 목표로 한다.


  2. 기능은 간단한 crud기능 및 댓글 그리고 로그인을 기준으로 한다.(추후 추가적인 기능을 할 예정)

  
  3. 기능이 다 구현된 뒤에는 cafe24에 호스팅을 해서 배포를 한다.

  
  4. 추가로 만들기능(구상): 게시물 스크랩 기능/ 첨부파일 기능 /  시큐리티 로그인 방법교체(jwt)/ 게시글 카테고리 기능 / 검색중에 동적 쿼리를 추가하기
  
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

- Back-end: Java11 SpringBoot,Jpa


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

- [x] 검색

- [x] 댓글(목록,작성,삭제)

- [x] 회원 가입

- [x] 로그인(시큐리티)

- [x] 회원 탈퇴,수정기능

- [ ] 파일 첨부 기능 

- [ ] 방문자 수 통계

- [ ] 스크랩 기능

- [ ] 

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

- 댓글 화면 작성


3일차 (2022.09.16)

- 댓글 목록 에러 처리

- 댓글 작성기능 수정중

4일차 (2022.09.17)

- 회원 가입 도메인 작성 

5일차 (2022.9.19)

- 댓글 작성,삭제 기능 완성.

- 시큐리티 설정

6일차 (2022/9.20)

- dto에 유효성검사 테스트

- 회원중복 기능 구현

- 회원중복 기능 데스트(포스트 맨)

- 회원목록 기능 테스트(포스트 맨)

- 회원가입,로그인 페이지 작성

- [api 문서](https://documenter.getpostman.com/view/18344373/2s7YYsc4Ea) 

- 회원가입기능구현


7일차(2022.09.21)

- 회원중복기능 구현

- 회원탈퇴 및 수정기능 테스트(포스트 맨)

- 회원탈퇴기능 구현

- 회원 수정 화면 작성

- 웹 설정 클래스 작성.

- 회원 조회기능 테스트(포스트맨)

- [api 문서](https://documenter.getpostman.com/view/18344373/2s7YYsc4Ea) 

- 회원수정기능구현

- 시큐리티 로그인기능 구현

8일차(2022.09.22)

- 로그인 핸들러 작성

- 어드민 목록 페이지 작성

- 메인 페이지 작성

- 타임리프 권한분기 설정

9일차(2022.09.23)

- 어드민 조회 페이지 작성

- 소셜 로그인 설정 및 코드 작성

- 소셜 로그인 테스트 

10일차(2022/10/05)

- 게시판 글 작성시 유효성 검사 수정하기.

11일차(2022/10/06)

- 게시판 검색기능

12일차(2022/10/07)

- 로그인 실패시 에러메시지

13일차(2022/10/08)

- 회원정보 조회 화면

- 회원 정보 삭제 및 수정기능

14일차(22/10/12)

- 유효성검사에 aop를 사용

- controller부분에 try-catch 부분을 handler클래스 작성

15일차(22/10/19)

- 각 기능 테스트코드 작성(회원부분,글작성부분)

- 컨트롤러 코드 변경(예외처리부분)

16일(22/10/23)

- 에러코드 추가

- 로그인후 글 작성 기능 수정(service,controller부분)

17일차(22/10/24)

- 로그인 지속기능 추가

- 게시판 파일첨부 추가기능 작성중

18일차(22/10/30)

- 테스트 코드작성(댓글부분)
