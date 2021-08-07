# REST_API_SpringBoot
 
### HTTP Method

- Create --> POST
- Retrieve --> GET
- Update --> PUT
- Delete --> DELETE

-------------------------------------------------------

Group id --> 회사의 이름

Artifact id --> 프로젝트의 이름

### maven
- clean : 빌드, 패키징 된 삭제
- compile : 프로젝트 컴파일, 자바 클래스파일 생성
- package : 컴파일 -> Jar or War
- install : 컴파일 -> 로컬서버에 배포

-------------------------------------------------------
@Component -> 스캔해서 등록하고 싶은 것들

@Repository -> DB에 접근하는 코드 (DAO 명시) : 퍼시스턴스 레이어, 영속성을 가지는 속성(파일, 데이터베이스)

@Service -> 비즈니스 로직과 관련된 코드 : 서비스 레이어, 비지니스 로직을 가진 클래스에 사용한다

@Controller -> 프리젠테이션 레이어, 웹 어플리케이션에서 웹 요청과 응답을 처리하는 클래스에 사용한다.

HATEOAS(Hypermedia As the Engine Of Application State)
-> 현재 리소스와 연관된(호출 가능한) 자원 상태 정보를 제공

-------------------------------------------------------

### REST API 성숙도 모델

- 자원 : Resource (URI)
- 행위 : Http Method (GET, POST 등)
- 표현 : 자원의 형태 - JSON, XML 등

##### Level 0 : 1 URI, 1 HTTP method

-> 하나의 POST Method로 함수이름과 인자를 넘김

-> 전달되는 서로 다른 매개변수를 통해서 하나의 endpoint로 여러 동작을 하게 된다.

-> 매개변수를 Body 로 전달하기 위해 HTTP method 는 POST 가 된다.

##### Level 1 : N URI, 2 HTTP method

-> Resource 별로 고유한 URI를 사용해서 식별하는 것이다.

-> URI에 /create, /update 등 표현

##### Level 2 : N URI, 4 HTTP method
-> URI 에 action 이 담기지 않는다. 

-> 멱등성(같은 결과를 출력하는 것)을 보장하는 GET 에는 Cache 가 적용되며

-> Response 에 HTTP Status Code 가 의미있게 반환된다.

##### Level 3 : Hypermedia As Engine of Application State

-> API 서비스의 모든 endpoint 를 최초 진입점이 되는 URI 를 통해 Hypertext Link 형태로 제공

-> 어떤 request 의 다음 request 에 필요한 endpoint 를 제공한다는 것

##### Level 4 : API Versionning (어떤 단계에서도 적용 가능)
-> URI 버전 방식

-> Request Param 버전 방식

-> Header 버전 방식

-> Media Type 버전 방식

@GetMapping(path = "/users/{id}/v1")  // -> URI 활용

@GetMapping(value="/users/{id}/", params = "version=1") -> param 활용

@GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1") -> header 활용

@GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json") // media type versioning

-------------------------------------------------------

http://localhost:8088/v2/api-docs

http://localhost:8088/swagger-ui.html

http://localhost:8088/actuator
