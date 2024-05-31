# JPA Blog

### 프로젝트 설명

Jpa Blog 는 JPA를 사용해서 

JPA를 처음으로 사용하는데 적응 및 공부를 위해서 웹개발에 있어서 가장 보편적인 블로그를 만들기로 했습니다. 

스프링부트를 사용해서 개발에서 배포까지의 개발의 한 사이클을 경험을 하는 것을 목표로 했고, 

무조건적으로 책과 강의를 따라하여 만드는 것이 아닌 내가 직접 기능에 대한 구현방법을 고민하고, 

여러 자료를 찾아보며 적용하도록 노력했습니다.

### 목표
 
  1. **JPA에 적응을 하기 위해서 블로그를 만드는 것을 목표로 한다.** 


  2. **기능이 다 구현된 뒤에는 CI/CD를 구축한다.**


  3. **TDD를 배우기 위해서 각 레이어마다 테스트 코드를 작성을 하고 테스트 커버리지를 80%로 한다.**


  4. **기능을 단순히 만드는 것에 집착하지 않고 관련된 기술에 대해서 공부하기.**


  5. **Redis를 활용해서 캐시를 사용해서 조회속도를 향상시키기 위해서 디비의 접근을 줄이기.**

### 개발 환경

- Back-end: JAVA17, SpringBoot 2.6.6,Spring data JPA , Redis , Spring Security , QueryDSL , JWT


- DB: MariaDB


- Build: Gradle


- Front-end: TheymLeaf(추후 SPA 변경해보기.),JQuery 


- TestTool: Postman, Mockito, Junit5


- Deploy: Aws ,Github-Actions

### 주요 기능

| 관리자 페이지 | 설명                                            |
|---------|-----------------------------------------------|
| 회원관리    | 블로그에 가입한 회원을  관리할 수 있습니다. (회원등급 변경기능)         |
| 게시글 관리  | 회원이 작성한 게시글을 관리할 수 있습니다. (비밀글 전환 및 게시글 선택 삭제) |
| 댓글 관리   | 회원이 작성한 댓글을 관리할 수 있습니다. (댓글 선택 삭제)            |
| 카테고리 관리 | 만들어 놓은 카테고리를 관리할 수 있습니다. (카테고리 추가 및 삭제)       |
| 방문자 관리  | 로그인을 한 회원의 방문을 통계로 관리할 수 있습니다.                |

| 회원 페이지      | 설명                                               |
|-------------|--------------------------------------------------|
| 회원가입 및 로그인  | 회원 가입 및 로그인을 할 수 있습니다.                           |
| 게시글 작성      | 게시글을 작성 및 비밀글을 작성할 수 있고 파일첨부 및 해시태그를 작성할 수 있습니다. |
| 게시글 검색 및 정렬 | 작성한 게시글을 검색할 수 있고,조회수,좋아요,작성일순으로 정렬을 할 수 있습니다.   |
| 댓글 작성 및 삭제  | 작성한 게시글에 댓글을 작성 및 삭제를 할 수 있습니다.                  |
| 게시글 좋아요     | 작성한 게시글에 좋아요를 추가,삭제를 할 수 있습니다.                   |
| 게시글 스크랩     | 타회원이 작성한 게시글을 스크랩을 할 수 있습니다.                     |
| 마이 페이지      | 회원이 작성한 글,댓글 및 스크랩을 한 게시글을 확인할 수 있습니다.           |
| 실시간 알림      | 타회원이 좋아요,댓글을 작성하면 실시간 알림으로 확인을 할 수 있습니다.         |

### 아키텍쳐 구조

![jpaBlog-Arc](https://github.com/well0924/jpapractice/assets/89343159/a5d6ebe6-3644-4417-ada8-8eac46058204)

### Api 명세서

[api 명세서](https://documenter.getpostman.com/view/18344373/2s93JqRQ1U)

### ERD

![jpaBlog-ERD](https://github.com/well0924/jpapractice/assets/89343159/9bb94c5a-b603-41a7-9d8f-58f979655188)


### 트러블 슈팅 및 구현 

1.[Jwt을 사용한 로그인 구현](https://codingweb.tistory.com/185)

2.[Redis Global Cache로 조회성능 향상시키기](https://codingweb.tistory.com/186)

3.[JPQL에서 QueryDSL을 활용한 동적쿼리 적용](https://codingweb.tistory.com/187)

4.[게시글 조회수에서 발생한 동시성 제어](https://codingweb.tistory.com/188)

5.[SSE를 활용해서 실시간 댓글알림기능 구현하기](https://codingweb.tistory.com/190)

6.[AOP를 활용해서 공통로직에 적용되는 코드 리팩토링](https://codingweb.tistory.com/189)

### 주요 로직

Jwt 로그인 로직

![Jwt 로그인 순서 drawio](https://github.com/well0924/jpapractice/assets/89343159/4ed2d159-0afd-48ea-a77b-6f578b810e4d)


Redis 캐시 로직

![Redis 캐시적용 drawio](https://github.com/well0924/jpapractice/assets/89343159/00bbb763-0385-42dc-bb6d-cf24d5ec10a4)
