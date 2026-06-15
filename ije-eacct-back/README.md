# ILJIN Framork Back-end (API App)  
## Pre Required
- Git  
- Java 11+  

## packages (com.iljin.apiServer)
### core
> 주요 서비스 및 메서드 등 백엔 핵심 로직 구현.

#### aop
> Aspect 

#### config
> configurations

#### files
> file upload service

#### mail
> mail service

#### mPush
> mobile push service

#### schedule
> schedule job service

#### security
> application security settings

#### util
> key/value Pair, Util, Error, ...

### template
>for Basic APIs  
> like login, user 

## api 작성 예시
- 전체 사원 조회: GET /api/v1/user  
- 특정 사원 조회: GET /api/v1/user/id/1  
- 새로운 사원 생성: POST /api/v1/user/370372  
- 사원 정보 변경: PUT /api/v1/user/370372  
- 사원 정보 삭제: DELETE /api/v1/user/370372  

# Entity 개발 가이드 

## 권장사항
1. 모든 Entity에 등록자(regId), 등록일자(regDtm), 수정자(chgId), 수정일자(chgDtm) 필수 추가
2. 등록자, 등록일자, 수정자, 수정일자 필요시 BaseEntity를 상속 받아 사용
3. 등록일자, 수정일자 필요시 BaseTimeEntity를 상속 받아 사용

## 필수사항
1. 엔티티간에 연관관계 (단방향, 양방향) 표현 명시 
2. N:1, 1:N, 1:1은 지향하나, M:M(Many to Many)은 절대 사용하지 말 것

# Query개발 가이드
1. 가급적 Querydsl 사용을 지향하나 복잡한 비지니스 로직이 있을 경우 Custom Query 또는 JPQL사용
2. Q-Type, @QueryProjection을 활용

## API Documentation
### Spring REST Docs
```src/docs/asciidoc/api-guide.adoc```  
: Asciidoc 문법으로 작성할 수 있습니다.   
: test, bootJar 실행시 해당 테스트에 의해 adoc 문서가 build/generated-snippets 에 생성됩니다.


 