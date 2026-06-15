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

## 6. 다음 액션
1. [ ] **(선행)** §2.1에 따라 SSO(RathonSSO/OnePass) 코드·설정·JAR 제거 (운영 로그로 `/login/sso`·`/dispatch/` 호출 0 확인 후)
2. [ ] fasoo/fcwpkg(네이티브) 및 bcprov(JCE) 런타임 사용 여부 확인 → 미사용분 제거
3. [ ] 1단계(Gradle 8.5 + QueryDSL 플러그인 교체) PoC 브랜치 진행
4. [ ] OpenRewrite/IntelliJ 기반 jakarta 전환 자동화 검증
5. [ ] springdoc, Security 6 샘플 전환으로 공수 실측
