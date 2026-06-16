# ije-eacct-back : Java 11 → 21 마이그레이션 계획

> 작성일: 2026-06-15
> 목표: 스택 최신화 (Java 21 / Spring Boot 3.x / Gradle 8.x)

---

## 1. 현재 상태 (As-Is)

| 항목 | 현재 버전 | 비고 |
|------|-----------|------|
| Java (sourceCompatibility) | 1.11 | |
| Spring Boot | 2.3.3.RELEASE (2020) | Spring Framework 5.2 기반 |
| Gradle | 6.9 | Java 16까지만 지원 |
| 전체 Java 파일 | 924개 | |
| `javax.*` 사용 파일 | 226개 (491 import) | jakarta 전환 대상 |
| API 문서 | Springfox Swagger 2.9.2 | Boot 3 미지원 |
| Security | `WebSecurityConfigurerAdapter` 상속 | Boot 3에서 제거됨 |
| QueryDSL | 4.2.1 (javax) | jakarta classifier 필요 |
| DB | MariaDB + Oracle(ojdbc8) | |

### ⚠️ 단순 Java 버전 변경이 불가능한 이유
1. **Gradle 6.9** 는 Java 21 빌드/실행 불가 (Java 16까지 지원) → Gradle 8.5+ 필수
2. **Spring Boot 2.3.3** 은 Java 21 런타임 미지원 → Boot 3.x 필수
3. Boot 3.x = Spring Framework 6 = **javax → jakarta 강제 전환 + Springfox 제거**

→ 결론: "Java 버전만 21로 변경" 은 빌드 자체가 실패함. 아래 동반 작업이 필수.

---

## 2. `libs/` 벤더 전용 JAR

`build.gradle`이 `fileTree(dir: 'libs')`로 로컬 JAR을 직접 포함하고 있음.
이 중 **벤더 제공 / 구버전 JAR**이 jakarta·Java 21 환경에서 동작하지 않을 수 있음.

> **✅ 결정 (2026-06-15): SSO 미사용 확정.**
> 현재 운영은 **Login 페이지의 id/pw 폼 로그인만** 사용하며 SSO 로그인은 사용하지 않음.
> 코드 검증 결과 폼 로그인(`/login`)은 Spring Security `AuthenticationManager` + `BCryptPasswordEncoder`로
> 독립 동작하며 SSO 벤더 코드를 호출하지 않음. → **SSO 관련 벤더 JAR은 제거 대상**으로 전환.
> (상세 제거 범위는 §2.1 참조). 이로써 최대 리스크였던 `javax.servlet` 의존 SSO JAR이 사라짐.

각 JAR 클래스파일을 직접 검사한 결과:

| JAR | 위험도 | 판정 / 조치 |
|-----|--------|-------------|
| `RathonSSO_SP_JDK8-4.3.59.jar` | 🟢 제거 | SSO 미사용 → **제거**. (`javax/servlet` 12개 참조 — Boot3 최대 위험원이었으나 제거로 해소) |
| `opAgent.jar` | 🟢 제거 | OnePass SSO 에이전트(`com.saerom.onepass`). `SsoServiceImpl`에서만 사용하나 미연결 → SSO 폐기 시 **제거** |
| `eMateCipher.jar` | 🟢 제거 | SSO 로그인 화면(emate) 관련 암호화. SSO 폐기 시 **제거 검토** (`javax/servlet` 2개 참조) |
| `bcprov-ext-jdk14-145.jar` | 🟡 중간 | BouncyCastle 1.45(2010). 소스 직접 참조 없음. SSO/eMate가 JCE 프로바이더로 **간접 사용 가능** → 제거 전 런타임 확인. 유지 시 최신 `bcprov-jdk18on`으로 교체 |
| `fasoo-jni-3.0.9u.jar` | 🟡 중간 | Fasoo DRM JNI. 소스 참조 없음(`javax` 의존 없음). 파일 다운로드 DRM 사용 여부 런타임 확인 후 유지/제거 |
| `fcwpkg_jni-1.0.8.jar` | 🟡 중간 | JNI 네이티브 모듈. 소스 참조 없음. 사용 여부 확인 후 유지/제거 |
| `ojdbc8-12.2.0.1.jar` | 🟢 낮음 | Java 8 빌드지만 Java 11/17/21에서 동작. `ojdbc11`로 교체 권장 |
| `jdom.jar` | 🟡 중간 | 버전 미상, 최신 jdom2로 교체 검토 |
| 그 외 (httpclient, logback, slf4j, jsoup 등) | 🟢 낮음 | 표준 OSS, Maven 의존성으로 전환 가능 |

> **참고:** SSO 미사용 결정으로 당초 🔴 5종이던 통제 불가 벤더 리스크가 사실상 해소됨.
> 남은 확인 대상은 fasoo/fcwpkg(네이티브 런타임 사용 여부)와 bcprov(JCE 간접 사용 여부)뿐.

---

## 2.1 RathonSSO(및 SSO 일체) 제거 범위

**전제:** SSO 로그인 미사용, id/pw 폼 로그인만 사용 → SSO 코드는 실행되지 않는 죽은 코드.
폼 로그인은 RathonSSO와 무관하게 독립 동작함이 코드로 확인됨(`UserServiceImpl.login`).

### 백엔드 — 삭제
| 대상 | 조치 |
|------|------|
| `core/config/SPListener.java` | 파일 전체 삭제 (Rathon SP 디스패처/리스너 등록) |
| `resources/rathon_sso_sp_product.properties` | 삭제 |
| `resources/rathon_sso_sp_dev.properties` | 삭제 |
| `libs/RathonSSO_SP_JDK8-4.3.59.jar` | 삭제 |

### 백엔드 — 수정
| 파일 | 조치 |
|------|------|
| `ijeas/security/LoginController.java` | L23 `import ...rathontech...Env` 삭제 / L47~52 `/login/sso` 메서드 삭제(`Env.DEFAULT_SESSION_USERID` 의존) / 미사용 시 L36 `ssoService` 필드 정리 |
| `core/config/SecurityConfig.java` | L26 `import static ...IDPM_DOMAIN_CONTEXT` 삭제 / L76 `/login/ssoRathon`·L77~78 `/dispatch/**`·L79 `IDPM_DOMAIN_CONTEXT` antMatchers 삭제 |

### SSO 전체 폐기 시 추가 정리(선택)
| 대상 | 조치 |
|------|------|
| `core/security/sso/SsoServiceImpl.java`, `SsoService.java` | OnePass/OpAgent(미연결) 제거 |
| `libs/opAgent.jar`, `libs/eMateCipher.jar` | 제거 |
| `application-*.properties`의 `opagent.*` (각 약 13~17줄) | 제거 |

### 프론트엔드(`ije-eacct-front`) — 수정
| 파일 | 조치 |
|------|------|
| `src/components/Login.vue` | `created()` 내 `rathonsso.init()`/`requestAuthentication()` 블록(약 L281~298) 삭제 |
| `.env.development` / `.env.local` / `.env.staging` / `.env.production` | `VUE_APP_SSO_JS=rathonsso-*.js` 줄 삭제 |

### 제거 전 확인
- 운영 액세스 로그에서 `/login/sso`, `/dispatch/` 호출 빈도가 0인지 확인(외부 진입/북마크 경로 잔존 여부).

---

## 3. 권장 전략: 단계적 업그레이드 (Big-bang 비권장)

924개 파일 + 벤더 종속성 때문에 한 번에 21+Boot3로 가는 것은 위험.
**중간 기착지(Java 17 / Boot 2.7)** 를 두어 리스크를 분산.

```
[1단계]  Gradle 6.9 → 8.5+        (Java 17 호환 빌드 환경 확보)
[2단계]  Java 11 → 17             (런타임/컴파일만, 코드 변경 최소)
[3단계]  Spring Boot 2.3 → 2.7    (deprecated API 정리, Boot3 준비)
[4단계]  Spring Boot 2.7 → 3.x    (javax→jakarta, Springfox→springdoc) ← 최대 작업
[5단계]  Java 17 → 21             (최종 런타임 전환)
```

---

## 4. 단계별 상세

### 1단계 — Gradle 8.5+ 업그레이드
- `gradle-wrapper.properties` distributionUrl 변경
- `com.ewerk.gradle.plugins.querydsl` 플러그인은 Gradle 8과 비호환 → 제거 후
  `annotationProcessor` 방식 QueryDSL 설정으로 교체
- `compile`, `testCompile`, `runtime` 등 **제거된 configuration** → `implementation`,
  `testImplementation`, `runtimeOnly`로 변경 (build.gradle 56, 77, 127행 등)
- `pathingJar`의 `configurations.runtime` → `runtimeClasspath`

### 2단계 — Java 17
- `sourceCompatibility = '17'` (또는 `java { toolchain }` 방식)
- build.gradle 112~123행의 `--add-opens jdk.compiler/...` 옵션 점검
  (Lombok/QueryDSL 버전업으로 상당수 불필요해질 수 있음)
- 컴파일 후 deprecation 경고 정리

### 3단계 — Spring Boot 2.7
- Boot 2.7로 올려 `WebSecurityConfigurerAdapter` 등 deprecated 경고를 미리 노출
- `SecurityConfig`를 **`SecurityFilterChain` Bean 방식**으로 사전 리팩터링 가능
  (2.7에서도 신방식 지원 → Boot3 전환 부담 분산)

### 4단계 — Spring Boot 3.x (핵심·최대 공수)
- **javax → jakarta 전환: 226개 파일 / 491 import**
  - `javax.persistence`, `javax.validation`, `javax.servlet`, `javax.annotation`,
    `javax.transaction`, `javax.mail` 등 → `jakarta.*`
  - IntelliJ "Migrate to Jakarta EE 9" 또는 OpenRewrite 레시피
    (`org.openrewrite.java.migrate.jakarta`) 활용 권장
- **Springfox 2.9.2 완전 제거 → springdoc-openapi 2.x로 교체**
  - `SwaggerConfig.java` 재작성 (`Docket` → `OpenAPI` Bean)
  - 컨트롤러의 `@Api`, `@ApiOperation` → `@Tag`, `@Operation`
- **QueryDSL** → `querydsl-jpa:5.x:jakarta` + `querydsl-apt:5.x:jakarta`
- **Spring Security 6**
  - `SecurityConfig`: `WebSecurityConfigurerAdapter` 상속 제거 →
    `SecurityFilterChain` + `WebSecurityCustomizer` Bean
  - `antMatchers()` → `requestMatchers()`
  - `authorizeRequests()` → `authorizeHttpRequests()`
  - `configure(AuthenticationManagerBuilder)` → `AuthenticationManager`/`UserDetailsService` Bean
- **`javax.xml.bind:jaxb-api`** (build.gradle 74행) → `jakarta.xml.bind:jakarta.xml.bind-api`
- 기타 의존성 Boot 3 호환 버전으로 일괄 상향
  (firebase-admin 6.8.1 → 9.x, java-jwt 3.10.3 → 4.x, spring-boot-admin 2.2.1 → 3.x,
   p6spy-spring-boot-starter, modelmapper 등)
- `application.yml`/`properties` 의 변경된 설정 키 점검

> **0단계(선행 권장) — SSO 코드 제거:** §2.1에 따라 RathonSSO(및 미연결 OnePass) 관련
> 코드/설정/JAR을 먼저 제거하면, Boot3 전환 시 `javax.servlet` 의존 SSO JAR 문제가 원천 제거됨.
> 폼 로그인만 남으므로 4단계 Security 6 재작성 범위도 단순해짐.

### 5단계 — Java 21
- toolchain을 21로 상향, 전체 회귀 테스트
- (잔존 시) 네이티브 JNI `fasoo-jni`, `fcwpkg_jni` 동작 검증 — 미사용이면 제거

---

## 5. 작업량 / 리스크 요약

| 영역 | 규모 | 난이도 |
|------|------|--------|
| Gradle/빌드 설정 | build.gradle 전반 | 중 |
| javax → jakarta | 226개 파일 | 중 (도구 자동화 가능, 검증 필요) |
| Springfox → springdoc | SwaggerConfig + 컨트롤러 다수 | 중~상 |
| Spring Security 6 | SecurityConfig 재작성 (SSO 제거로 단순화) | 중~상 |
| SSO 코드 제거 | 백엔드 3파일 수정 + 2파일/JAR 삭제, 프론트 소폭 | 하 |
| 벤더 JAR 호환성 | fasoo/fcwpkg/bcprov 런타임 확인 | 중 (SSO 제거로 🔴→🟡 완화) |
| 회귀 테스트 | 전체 | 상 |

**예상 기간:** SSO 미사용 확정으로 통제 불가 벤더 리스크 해소 → **수 주(2~4주)** 규모로 산정 가능.
주 공수는 javax→jakarta(226파일), Springfox→springdoc, Security 6 재작성에 집중됨.

---

## 6.5 실행 결과 (2026-06-15, 브랜치 `migration/java21-boot3`)

Java 21 설치(Temurin 21.0.7) 후 **단계 통합 진행** — Java 21만 설치되어 Boot 2.x 중간 기착이 불가하므로 Boot 3 직행.

**완료 (clean build -x test = BUILD SUCCESSFUL, `eacct-back.jar` 생성):**
- Gradle 6.9 → **8.10.2** (wrapper)
- Spring Boot 2.3.3 → **3.3.5**, Java 11 → **21** (toolchain)
- `javax.*` → `jakarta.*` 정밀 치환 (persistence 452/servlet 17/validation/transaction/mail/annotation), JDK 패키지(crypto/sql/net/xml.parsers) 보존
- QueryDSL 4.2.1 → **5.0.0:jakarta** (ewerk 플러그인 제거, 표준 annotationProcessor)
- Springfox 2.9.2 → **springdoc-openapi 2.6.0** (`SwaggerConfig` OpenAPI/GroupedOpenApi 재작성)
- Spring Security 6: `SecurityConfig` → `SecurityFilterChain`+`DaoAuthenticationProvider`+`AuthenticationManager`, `@EnableMethodSecurity`
- **qlrm 3.0.1(javax) → 4.0.1(jakarta)** ← 100개 컴파일 오류 원인이었음
- firebase-admin 6.8.1 → **9.2.0** (`Notification` 빌더 패턴 대응)
- 기타: java-jwt 4.4, modelmapper 3.2, guava 33, poi 5.2.5, commons-beanutils 1.9.4, jakarta.xml.bind 4.0 + jaxb-runtime, thumbnailator(maven 이전), spring-boot-admin-client 3.3.6
- Hibernate 6: `ProcedureParameterImpl.enablePassingNulls` 제거(런타임 검증 TODO)
- `libs/` 정리 → 벤더/드라이버 6종만 유지 (eMateCipher, fasoo-jni, fcwpkg_jni, ojdbc8, bcprov, jdom). 구버전 logback/slf4j 등 13종 제거(Boot BOM 충돌 해소)
- Boot 3 설정 키: `initialization-mode`→`sql.init.mode`, `datasource.platform`→`sql.init.platform`, `profiles.include`(프로파일별 금지)→`profiles.group`
- `orm.xml` 네임스페이스 javax(jcp.org 2.2) → **jakarta 3.0**

**Hibernate 6 엔티티 매핑 수정 (기동 검증 중 발견, 2026-06-15):**
Hibernate 6는 `@Id` 속성 집합과 `@IdClass` 필드 집합의 정확한 일치를 강제(5는 묵인). 불일치 3건 수정:
- `ApPaymentsItem` ↔ `ApPaymentsItemKey`: 키에 `aeHeaderId`(Long) **추가** (엔티티 @Id에 있었음)
- `CodeDetail`: 엔티티에 `@Id detailCd` **추가** (키에 있었음 / 부모 codeHeader 조인 구조상 PK 구성요소 확실)
- `Task`: 엔티티에 `@Id compCd` **추가** (키에 있었음)
- → ⚠️ **위 3개는 실제 DB PRIMARY KEY와 대조 검증 권장** (특히 ApPaymentsItem/Task). 데이터 무결성 안전 방향(식별자 충돌 회피)으로 컬럼을 PK에 포함시킴.

**Hibernate 6 HQL 검증 강화 (기동 중 발견):**
- `SlipHeaderRepository.deleteByMonth`의 `@Query`: `UPDATE FROM SlipHeader b ...` → `UPDATE SlipHeader b ...` (HQL UPDATE 구문에 FROM 불가). 전체 237개 `@Query` 스캔 결과 동일 패턴은 이 1건뿐, 나머지 UPDATE/DELETE JPQL은 정상.
- `CardUseListRepository.resetByCompCdAndApprovalGroupId`: BigDecimal 필드에 빈 문자열 대입(`c.slipHeaderId = '', c.slipLineId = ''`) → `= null` (Hibernate 6 타입 검사 강화). 전체 `''` 리터럴 스캔 결과 숫자 필드 대입은 이 1건뿐(나머지는 String 컨텍스트).
- `MoBoardRepository.findByIdContainsAndCompCdContainsAndLoginId` → `findByIdAndCompCdAndLoginId` (+ 호출부): `Contains`가 `Long id`에 LIKE 생성 → Hibernate 6가 숫자에 LIKE 거부. PK 단건 조회이므로 정확 매칭으로 변경. 전체 파생쿼리 LIKE 계열 스캔 결과 나머지(loginId/validationFlag)는 String이라 정상.
- `AuthorityRepository.getMenuByAuthority`의 JPQL: `LTRIM(LENGTH(...))` → `CAST(LENGTH(...) AS string)` (Hibernate 6는 문자함수 LTRIM에 Integer 인자 거부 — 암묵적 숫자→문자 변환 불가).
- `AuthorityRepository.getMenuByAuthority`: `menu.menuNo <> 0` → `<> '0'` (menuNo는 String, 정수 리터럴과 비교 불가 — Hibernate 6 타입 검사).

**전체 JPQL 일괄 검증 도구 도입 (한 번에 모두 검출):**
- `src/test/java/com/iljin/apiServer/HqlQueryValidationTest.java` 추가 — DB 없이 OracleDialect로 오프라인 SessionFactory를 빌드(@Entity 145개)하고, 모든 Repository의 비-native `@Query`를 Hibernate 6 HQL 의미분석기로 검증.
- 실행: `gradlew test --tests com.iljin.apiServer.HqlQueryValidationTest`
- 결과: **JPQL @Query 67개 전부 통과(invalid=0)** → startup 시 하나씩 터지던 쿼리 오류를 사전에 일괄 제거.
- (커버리지 외: native @Query 35개는 raw SQL이라 startup HQL 검증 대상 아님 / 파생쿼리는 별도 키워드 스캔으로 점검, 위 MoBoard 1건만 수정.)
- 부수: 레거시 JUnit4 테스트 2종(`TaxDtiNtsMainQdslRepositoryTest`, `CodeQdslRepositoryTest`)을 테스트 컴파일에서 제외(build.gradle) — JUnit5 마이그레이션/정리 대상.

**Spring Security 6 인증 판정 변경 (로그인 후 403 수정):**
- 증상: 로그인 성공 후 보호 API 호출이 403. 원인: `UserServiceImpl.login()` 폼 로그인 경로가 `authenticate()` 결과를 버리고 `isAuthenticated()=false`인 2-arg `UsernamePasswordAuthenticationToken`을 세션 SecurityContext에 저장.
- Security 5의 `authenticated()`는 "익명만 아니면 통과"였으나, **Security 6의 `AuthenticatedAuthorizationManager`는 `isAuthenticated()==true`를 요구** → 미인증 토큰 거부.
- 수정: `token = (UsernamePasswordAuthenticationToken) authenticationManager.authenticate(token);` — 인증된 Authentication(권한 포함)을 세션에 저장. (id/pw 폼 로그인 경로만 해당; 토큰 분기는 3-arg라 영향 없음)

**Hibernate 6 점(.) 포함 @Table 이름 따옴표 처리 (ORA-00942 수정):**
- 증상: `slipLst` 등에서 `ORA-00942 table or view does not exist`. 생성 SQL이 `from "APPS.PO_HEADERS_ALL"`처럼 **테이블명 전체를 따옴표** 처리.
- 원인: `@Table(name="APPS.PO_HEADERS_ALL")`의 점을 Hibernate 5는 스키마 구분자로 봤으나, Hibernate 6는 식별자 내 특수문자로 보고 통째로 quoting.
- 수정: 점 포함 10개 엔티티를 `@Table(name="TABLE", schema="SCHEMA")`로 분리 (APPS/CBOTAX/CBOSLIP 스키마의 Oracle EBS 테이블/뷰). native @Query의 직접 SQL은 영향 없음.

**Hibernate 6 native query CHAR→Character 결과 타입 변화 (qlrm 매핑 깨짐, 전역 수정):**
- 증상: `eSlipSubmit` 등에서 `No constructor taking: ... java.lang.Character ...` (qlrm `JpaResultMapper`가 결과 타입에 맞는 DTO 생성자를 못 찾음).
- 원인: Hibernate 6는 native query의 CHAR(1) 컬럼·단일문자 리터럴('Y')을 `java.lang.Character`로 반환(5는 String). qlrm 생성자 타입 매칭이 광범위(154곳/27파일)하게 깨짐.
- **전역 수정**: qlrm `JpaResultMapper`를 자체 유틸 `core/util/ResultMapperUtil`로 교체 (154곳: `new JpaResultMapper().list/uniqueResult(` → `ResultMapperUtil.list/uniqueResult(`). 이 유틸은 native 결과(Object[])의 `Character` 값을 `String`으로 보정한 뒤 인자 개수/타입이 맞는 생성자를 찾아 인스턴스화한다. **반환된 실제 값에 작용하므로 Hibernate 타입 해석/Dialect 동작과 무관하게 보장**.
  - (TypeContributor로 JDBC CHAR→String 전역 매핑도 시도했으나 native 스칼라 해석 경로에 적용되지 않아 제거, ResultMapperUtil 방식으로 확정.)
- 더불어 DTO에 남아있던 `Character` 필드·생성자 파라미터(총 11곳: ErpSlipSubmitDto 3, SalesTaxInvoiceDto·SlipDetailDto·SlipHeaderDto·MenuAuthDto·EmployeeDto·TrxDto·CostBudgetDto 등)를 String으로 통일 (대입부가 모두 `String.valueOf(...)`라 안전).
- → qlrm 기반 154개 네이티브 매핑이 일괄 정상화. (참고: TIMESTAMP→`java.sql.Timestamp`, NUMBER→`BigDecimal` 등 다른 native 타입 변화로 또 "No constructor taking"이 나오면 ResultMapperUtil.normalize 에 동일하게 보정 추가 가능)

**Hibernate 6 native query NUMBER→Long/BigInteger 결과 타입 변화 (ResultMapperUtil 숫자 coercion, 전역, accrualSlip):**
- 증상: `accrualSlip`에서 거래처 선택 후 `/api/gl/term/list` 호출 시 `No constructor taking: java.lang.String java.lang.String java.lang.Long java.lang.String java.lang.String java.lang.String` (500).
- 원인: 해당 native query 3번째 컬럼 `TERM_ID`(Oracle `NUMBER`, 스케일 없음)를 Hibernate 6는 `java.lang.Long`으로 반환(5는 `BigDecimal`). 대상 DTO `ErpGlTermsDto`의 6-인자 생성자는 해당 자리를 `BigDecimal`로 선언 → `BigDecimal.isAssignableFrom(Long)=false`로 매칭 실패. (위 Character와 동일 계열의 native 결과 타입 변화)
- **전역 수정**: `ResultMapperUtil.map`을 2-pass로 변경. 1차는 기존대로 타입이 그대로 호환되는 생성자만 매칭(기존 동작 보존), 1차 실패 시 2차에서 **숫자 타입 간 변환(coercion)**을 허용해 매칭(`coerceNumber`: Long/BigInteger/Integer/BigDecimal/Double/Float/Short/Byte 상호 변환). 반환된 실제 값에 작용하므로 NUMBER가 Long/BigInteger/BigDecimal 어느 것으로 와도 DTO 생성자 선언 타입에 맞춰 변환됨.
- → 이 패턴(native NUMBER 컬럼을 BigDecimal/Integer 파라미터로 받는 모든 native 매핑)이 일괄 해소. 단건 패치 없이 ResultMapperUtil 한 곳으로 전 native 쿼리 보호.

**Java 21 JPMS + Jackson: @RequestBody 역직렬화 실패 (Content-Type not supported, 전역):**
- 증상: 여러 화면(refLst 및 /api/appr/* 승인 목록 등)에서 `Content-Type 'application/json;charset=UTF-8' is not supported`.
- 원인: `ApprovalHeaderDto`에 `DateTimeFormatter` **인스턴스 필드**가 있어 Jackson이 deserializer 생성 시 `java.time.format` 내부에 리플렉션 접근 → **Java 21 JPMS가 차단(InaccessibleObjectException)** → `canDeserialize=false` → 컨버터 `canRead=false` → `HttpMediaTypeNotSupportedException`. 해당 DTO를 쓰는 승인 관련 7개 엔드포인트가 모두 실패.
- 수정: `ApprovalHeaderDto.formatter`를 `private static final`로 변경 (Jackson은 static 필드 무시).
- 검증 도구 추가: `JacksonCanReadTest` — 전 컨트롤러의 `@RequestBody` 타입(70개)을 스캔해 Jackson `canRead`를 일괄 검사. **전체 통과(실패 0)** 확인.

**Spring 6 trailing slash 매칭 비활성화 (404 / No static resource, 전역):**
- 증상: `fundSlipLst` 등에서 `POST /api/erp/slip/fund/list/`(끝 슬래시) → 404 `No static resource ...`.
- 원인: Spring 6는 trailing slash 매칭을 기본 비활성화(Boot 2.x/Spring 5는 허용). 프론트가 일부 API를 끝 슬래시로 호출해 매칭 실패.
- 수정: `WebMvcConfig implements WebMvcConfigurer`에서 `configurePathMatch` → `setUseTrailingSlashMatch(true)` 전역 복원 (Spring 6.4에서 deprecated이나 동작).

**Oracle 페이징 + 중복 컬럼명 ORA-00918 / count ORA-00937 (cardInfoMng 등):**
- 증상1: `cardInfoMng` 목록에서 `ORA-00918 column ambiguously defined`. 코드테이블 4중 조인으로 `detail_nm`을 별칭 없이 4번 select → Oracle OFFSET/FETCH 페이징 내부 래핑에서 중복 컬럼명 모호 (Hibernate 6가 select 컬럼 별칭 누락 — 알려진 이슈).
  - 조치: Hibernate 6.6.4 상향을 시도했으나 SQL 동일(미해결) → **되돌림**(6.4.4). 대신 중복되는 `detailNm` select 컬럼을 `.trim()`으로 감싸 SQL에서 고유 표현식(`trim(cd1_0.detail_nm)`)이 되게 함 → bare 컬럼 중복 제거로 Oracle 페이징 래핑 시 모호성 해소. (이 다중 코드조인+페이징 패턴은 `CardQdslRepositoryImpl` 1개 파일에만 존재 — 전수 grep 확인)
- 증상2: `/api/card/count` 500 — count 쿼리에 `.orderBy(...)`가 있어 집계+정렬 충돌(ORA-00937 류). count 메서드의 `.orderBy()` 제거.
- 부트스트랩 sanity: 6.6.4에서 엔티티 145개 + JPQL 67개 검증 통과(HqlQueryValidationTest).

**Hibernate 6 select 별칭 중복 금지 (AliasCollisionException, confirmMng):**
- 증상: `confirmMng`에서 `AliasCollisionException: Duplicate alias 'confirmUserNm'`.
- 원인: `ConfirmQdslRepositoryImpl`의 한 select에서 `costCenter.deptNm.as("confirmUserNm")`와 `employee1.empNm.as("confirmUserNm")` 두 컬럼이 같은 별칭 사용(Hibernate 5는 허용, 6은 금지).
- 수정: 앞 컬럼 별칭을 `deptNm`으로 변경(QConfirmDto는 위치 기반이라 매핑 영향 없음). 전 QueryDSL select 블록 정밀 스캔 결과 동일 패턴 추가 없음(0건).

**⚠️ 남은 검증 (실제 DB 환경 필요 — 사내에서 수행):**
1. `./gradlew bootRun` 실제 기동 (Oracle 연결) — 컨텍스트 로딩/매핑 검증 (다음 엔티티에서 추가 매핑 에러 시 동일 패턴으로 대응)
2. Hibernate 6 동작 검증: 네이티브 쿼리/`@NamedStoredProcedureQuery`(APPS.CBO_SP_SLIP_PKG.*), null 파라미터 전달(위 enablePassingNulls 제거분)
3. id/pw 로그인 E2E, 파일 다운로드 시 Fasoo/eMate 벤더 jar 런타임 동작
4. bcprov(JCE) 간접 사용 여부 최종 확인
5. 프론트 빌드/연동 점검

## 6. 다음 액션
1. [x] **(완료 2026-06-15)** §2.1에 따라 SSO(RathonSSO/OnePass) 코드·설정·JAR 제거 완료
   - 백엔드 삭제: `SPListener.java`, `SsoServiceImpl.java`, `SsoService.java`, `rathon_sso_sp_*.properties`(2), `libs/RathonSSO_SP_JDK8-4.3.59.jar`, `libs/opAgent.jar`
   - 백엔드 수정: `LoginController`(`/login/sso` 메서드·`ssoService` 필드·import 제거), `SecurityConfig`(SSO matcher·import 제거), `UserService`/`UserServiceImpl`(`ssoLogin` 제거)
   - 프론트 수정: `Login.vue`(created `rathonsso` 블록·`ssoLogin` 메서드·beforeMount 호출 제거), `.env.*`(4) `VUE_APP_SSO_JS` 제거
   - orphan 설정 정리 완료: `application-*.properties`의 `opagent.*`(13줄×4파일) 및 `RTSSO_SP_CONFIG`(dev/production) 제거
   - ⚠️ 잔여: 빌드/로그인 회귀 테스트 미수행 / `eMateCipher.jar`·`bcprov`는 보존(런타임 사용 여부 별도 확인)
2. [ ] fasoo/fcwpkg(네이티브) 및 bcprov(JCE)·eMateCipher 런타임 사용 여부 확인 → 미사용분 제거
3. [ ] 1단계(Gradle 8.5 + QueryDSL 플러그인 교체) PoC 브랜치 진행
4. [ ] OpenRewrite/IntelliJ 기반 jakarta 전환 자동화 검증
5. [ ] springdoc, Security 6 샘플 전환으로 공수 실측
