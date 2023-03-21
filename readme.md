# ****스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술****

# 출처

- 출처 : [https://www.inflearn.com/course/스프링-입문-스프링부트/dashboard](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%9E%85%EB%AC%B8-%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8/dashboard)

# 학습내용 정리(강의를 듣고 정리한 내용입니다.)
## 프로젝트 환경 설정

- [https://start.spring.io](https://start.spring.io/) 스타터를 통해 쉽게 스프링 부트 사용이 가능하다.
- 스프링 부트의 주요 라이브러리로는
    - spring-boot-starter-web : 톰캣 웹서버와 스플잉 웹 MVC 담당한다.
    - pring-boot-starter-thymeleaf: 타임리프 템플릿 엔진이다.
    - spring-boot-starter-test : junit, mockito, assertj 등의 테스트 라이브러리가 있다.
- static/index.html을 통해 인덱스 페이지 설정 가능하다.
- 컨트롤러에서 @GetMapping(”hello”) 어노테이션을 통해 url통해 호출 할 수 있다.
- 컨트롤러에서 model.addAttribute를 통해 view단으로 데이터를 넘길 수 있고 뷰단에서는 ${name}을 통해 호출할 수 있다.
- 동작 순서
- 
    
    ![image](https://user-images.githubusercontent.com/76714485/226666290-74543c91-a6f8-4734-9b4b-37eb3b6e1368.png)

    
    - 웹 브라우저에서 특정 url로 호출한다.
    - 톰캣 서버를 거쳐서 스프링 컨테이너 내의 controller로 매핑된다.
    - 그후 리턴된 값으로 템플릿에서  html로 데이터를 넘겨준다.
    - 해당 html을 톰캣 서버를 통해 웹브라우저로 넘겨준다.

## 스프링 웹 개발 기초

- 스프링은 static 폴더에서 정적 컨텐츠를 제공하며, url호출 시 매칭되는 컨트롤러가 없으면 정적 컨텐츠를 호출한다.
- MVC → Model, View, Controller
- 주로 View는 화면 관련 로직, Controller에서는 비즈니스로직과 서버관련, Model을 통해서 전달하는 담당을 한다.
- @RequestParam(”name”)을 통해서 url을 통해 파라미터를 전달 받을 수 있다.

```java
@GetMappint("hello-api")
@ResponseBody
public Hello helloApi(@RequstParam("name") String name){
}
```

- 위의 코드처럼 @ResponseBody어노테이션을 작성하면 HTTP의 BODY에 문자 및 JSON을 반환 가능하다.
- @ResponseBody 시 기존 컨트롤러에서 리턴 시 viewResolver로 반환하는게 아닌 HttpMessageConverter작동하여 문자 및 객체는JSON으로 반환한다.

## 회원관리 예제를 통한 백엔드 개발

- 일반적인 웹 애플리케이션 계층구조는
    - 
    
    ![image](https://user-images.githubusercontent.com/76714485/226666357-c34c0f3c-7bf2-4417-8ed5-7203636d96fe.png)
    
    - 컨트롤러 → 서비스 → 리포지토리 → 디비 구조이다.
    - 컨트롤러는 웹 MVC의 컨트롤러 역할을 한다.
    - 서비스는 핵심 비즈니스 로직을 구현한다.
    - 리포지토리는 데이터베이스에 접근하거나 객체를 DB에 저장하고 관리한다.
    - 도메인은 일반적으로 사용하는 비즈니스 도메인이다.
- 서비스가 바로 리포지토리에 접근하는것 보단 중간에 인터페이스를 두어 느슨한 결함을 하면 db변경에 유리하다.
- 테스트 진행시 @BeforeEach와 @AfterEach 를 사용하면 각 테스트 시작, 종료 시 해당 기능을 사용한다.
- 테스트 진행시 try-catch 대신 assertThrows를 사용할 수 있다.
- 서비스 테스트시 @beforeEach에 리포지토리를 clear하는 과정에서
    - 서비스 코드
        
        ```java
        public class MemberService{
         private final MemberRepository memberRepostiroey = new MemoryMemberRepository();
         ...
        }
        ```
        
    - 테스트 코드
        
        ```java
        public class MemberServiceTest{
         MemberService memberService = new MemberService();
         MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        
         @AfterEach
         public void afterEach(){
          repository.clearStore();
         }
        ...
        }
        ```
        
    - 위의 코드처럼 구성하면 테스트하는 MemoryMemberRepository객체와 clear하는 객체가 다른 객체가 된다.
    - 서비스 코드
        
        ```java
        public class MemberService{
         private final MemberRepository memberRepostiroey;
         public MemberService(MemberRepository memberRepository){
          this.memberRepository =memberRepository;
         }
         ...
        }
        ```
        
    - 테스트 코드
        
        ```java
        public class MemberServiceTest{
         MemberService memberService;
         MemoryMemberRepository memberRepository;
        
         @BeforeEach
         public void beforeEach(){
          memberRepository = new MemoryMemberRepository();
          memberService = new MemberService();
         }
        ...
        }
        ```
        
    - 이런식으로 DI를 해주어서 각 테스트마다 각각의 리포지토리 생성 및 초기화가 가능하다.

## 스프링 빈과 의존관계

- 생성자에 @AutoWired를 통해서 스프링 컨테이너에 등록된 해당 빈을 찾아서 주입가능하다.
- 빈 등록 방법으로는
    - @Component혹은 해당 어노테이션을 포함한 @Controller, @Service, @Repository를 사용하면된다.
    - SpringConfig(@Configuration)에서 @Bean어노테이션을 통해 직접 빈을 등록 가능하다.
    - 주로 정형화된 코드는 컴포넌트 스캔, 상황에따라 변경가능한 것은 스프링 빈으로 등록한다.

## 스프링 DB 접근 기술

- H2 데이터베이스 → 개발이나 테스트 용도로 가볍고 편리한 DB이다.
- 개방 폐쇄 원칙(OCP)을 통해 JDBC로 변경 가능하다.
- @Transactional을 통해 테스트 시작전 트랜잭션을 시작하고, 테스트 완료 후에 롤백이 가능하다.
- 스프링 JdbcTemplate사용 시 순수 JDBC보다 반복코드를 제거해준다.
- JPA
    - 기본적인 SQL도 JPA가 직접 만들어서 실행해준다.
    - SQL과 데이터 중심의 설계에서 객체 중심의 설계로 패러다임을 전환할 수 있다.

### AOP

- AOP적용 시 공통 관심 사항과 핵심 관심사항을 분리할 수 있다.
    - 함수들의 시간을 재는 기능을 추가시 각 함수에 시간측정 기능을 추가하면 여러 문제점이 많다.
    - 이때 AOP를 적용하면 한번에 관리할 수 있다.
- AOP의 의존관계는
    - 기존에 A→B의 의존관계였다하면
    - A → 프록시 -> B와 같은 관계를 띈다.
