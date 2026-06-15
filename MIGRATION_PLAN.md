# 일진전기 전자전표(eAcct) 버전 업그레이드 마이그레이션 계획서

> 작성일: 2026-06-15 · 대상: ije-eacct-back(Spring Boot), ije-eacct-front(Vue)
> 목표 스택: **Java 21 (LTS) + Spring Boot 4.0 / Vue 3.5 + Vite**

---

## 1. 목표 버전 및 선정 근거

| 구분 | 현재(AS-IS) | 목표(TO-BE) | 비고 |
| --- | --- | --- | --- |
| Java | 11 | **21 (LTS)** | SB 4.0은 Java 17~25 지원. 21은 LTS로 수명이 길고 가장 안정적 |
| Spring Boot | 2.3.3.RELEASE | **4.0.x** (최신 4.0.7) | 3.5는 OSS 지원이 2026-06-30 종료 예정이라 신규 시작용으로 부적합 |
| Spring Framework | 5.2 | 7.x | SB 4.0에 종속 |
| 빌드 | Gradle 6.9 | **Gradle 8.10+ / 9.x** | Java 21·SB4 빌드 요건 |
| Node.js | 18.20.8 | **22 LTS 또는 24 LTS** | 18은 2025-04 EOL. Vite/최신 툴체인 요건 |
| Vue | 2.6.12 | **3.5.x** | Vue 2는 2023-12 EOL |
| 번들러 | vue-cli 3 (webpack 4) | **Vite 5/6** | vue-cli는 유지보수 종료 |
| 상태관리 | Vuex 3 | **Pinia** (또는 Vuex 4) | Vue 3 공식 권장은 Pinia |

> ⚠️ **중요 — 지원 종료 일정**: 조사 시점(2026-06) 기준 Spring Boot 3.5의 무상(OSS) 지원은 **2026-06-30**에 종료됩니다. 3.4 이하는 이미 OSS 지원이 끝났습니다. 따라서 "지금부터 새로 시작해서 오래 쓸" 신규 프로젝트라면 2025-11 출시된 **4.0 + Java 21 LTS** 조합이 가장 합리적입니다. (확정 답변 반영)

---

## 2. 전체 전략 — "신규 스켈레톤 + 레이어별 이전 + 자동화"

신규 폴더에서 작업하기로 한 방향은 적절합니다. 다만 **업무 로직을 처음부터 다시 짜는 재작성(rewrite)이 아니라**, 새 스켈레톤을 만든 뒤 기존 코드를 레이어 단위로 옮기면서 자동화 도구로 일괄 변환하는 방식을 권장합니다. 백엔드와 프론트의 난이도가 크게 다르므로 접근을 분리합니다.

- **백엔드**: 변경의 대부분(`javax`→`jakarta`, Security DSL, 설정 프로퍼티)이 **기계적**입니다. OpenRewrite로 자동 변환 후 사람이 검수하는 **빅뱅에 가까운 일괄 이전**이 효율적입니다.
- **프론트**: Buefy·Element-UI·ag-grid 등 핵심 UI 라이브러리가 Vue 3를 지원하지 않아 **사실상 UI 레이어 재작성**에 가깝습니다. 공통 인프라(라우터·스토어·i18n·HTTP)를 먼저 올린 뒤 **화면 단위로 점진 이전**하는 방식을 권장합니다.

```
권장 순서:  ① 백엔드 업그레이드(안정화) → ② 프론트 인프라 전환 → ③ 화면 점진 이전
                     │                            │
                  OpenRewrite                 Vue 2.7 경유(선택) → Vue 3
```

### 2.1 빅뱅 vs 점진(Strangler) 판단
- 백엔드 1,140개 Java 파일은 변경 성격이 균질(import·API 시그니처)하여 **한 번에 변환하고 통합 테스트로 검증**하는 편이 낫습니다.
- 프론트 277개 .vue 파일은 화면별로 의존 라이브러리가 달라 **한 화면씩 새 셸로 옮기며 동작 검증**(Strangler)하는 편이 리스크가 낮습니다.

---

## 3. 백엔드 마이그레이션 (Java 11 → 21, Spring Boot 2.3 → 4.0)

### 3.1 핵심 호환성 이슈 (코드 스캔 결과 기반)

| 이슈 | 현재 코드 영향 | 조치 |
| --- | --- | --- |
| **Jakarta 네임스페이스** | `javax.persistence.*` **452곳**, `javax.servlet.http` 18곳, `javax.validation` 3곳, `javax.transaction`·`javax.annotation`·`javax.xml.bind` 등 | 전부 `jakarta.*`로 변경 (Servlet 6 / Jakarta EE 10). **OpenRewrite로 자동화** |
| **Spring Security 5 → 6/7** | `SecurityConfig.java`의 `WebSecurityConfigurerAdapter` 상속, `antMatchers` 13곳, `authorizeRequests` | `WebSecurityConfigurerAdapter` 제거됨 → `SecurityFilterChain` Bean 방식. `antMatchers`→`requestMatchers`, `authorizeRequests`→`authorizeHttpRequests` |
| **Springfox(사망) 제거** | `SwaggerConfig.java`, `springfox-swagger2/ui 2.9.2` | Springfox는 SB 3+ 미지원. **springdoc-openapi v2**(`springdoc-openapi-starter-webmvc-ui`)로 교체. `Docket`→OpenAPI 어노테이션 |
| **QueryDSL** | `querydsl-jpa/apt/core 4.2.1` + `com.ewerk` Gradle 플러그인 | 5.x + **jakarta 분류자** 필요(`querydsl-jpa:5.x:jakarta`). ewerk 플러그인은 SB3+와 충돌 잦음 → 순수 `annotationProcessor` 방식 권장. (정체된 본가 대신 OpenFeign 포크 검토 가능) |
| **Gradle 설정 문법** | `compile`, `testCompile`, `runtime`, `compile fileTree(...)` 사용 | Gradle 7+에서 제거됨 → `implementation`/`testImplementation`/`runtimeOnly`로 변경. `--add-opens` JVM 인자 블록은 재검토 |
| **Hibernate 5 → 6** | JPA 엔티티·HQL·네이밍 전략 | HQL 문법 일부 변경, `GenerationType` 기본 동작·시퀀스 변경, 네이밍 전략 차이 → 쿼리·DDL 회귀 테스트 필수 |
| **설정 프로퍼티 변경** | `application.yml`/properties | SB 3에서 다수 프로퍼티 키 변경(예: `spring.redis`→`spring.data.redis`). `spring-boot-properties-migrator` 일시 적용으로 점검 |
| **Spring Boot Admin** | `spring-boot-admin-starter-client 2.2.1` | SB 4와 호환되는 3.x 계열로 상향 |
| **부가 라이브러리 버전** | java-jwt 3.10, poi 4.1.2, gson 2.8.5, guava 27, modelmapper 2.3, firebase-admin 6.8.1, p6spy 1.7.1 | 각 SB4/Java21 호환 최신 안정버전으로 상향. firebase-admin은 9.x 권장 |
| **log4jdbc-log4j2** | DB 로깅 | Java 21에서 동작 불안정 가능 → p6spy로 일원화 검토 |
| **JUnit Vintage** | `junit 4.13` 잔존 | JUnit 5(Jupiter)로 통일 권장 |

### 3.2 권장 절차
1. **신규 스켈레톤 생성**: Spring Initializr에서 SB 4.0 / Java 21 / Gradle 프로젝트 생성 → 기존 패키지 구조(`com.iljin.apiServer...`) 복사.
2. **OpenRewrite 적용**: `rewrite-spring`의 Spring Boot 3.x 업그레이드 레시피로 `javax`→`jakarta`, Security, 프로퍼티 등을 1차 자동 변환. (4.0 전용 레시피는 적용 후 잔여분 수동 보정)
3. **빌드 통과까지 컴파일 오류 해소**: QueryDSL Q클래스 생성 경로, Security 설정, Swagger 교체를 우선 처리.
4. **런타임 기동 + 통합 테스트**: 인증/권한, 전표 CRUD, 파일, 메일, 스케줄러, WebSocket, ERP 뷰 등 도메인별 회귀 테스트.
5. **성능·로그 점검**: Hibernate 6 쿼리 플랜, 커넥션풀, 세션(JDBC) 동작 확인.

---

## 4. 프론트 마이그레이션 (Vue 2.6 → 3.5, vue-cli → Vite)

### 4.1 핵심 호환성 이슈 (코드 스캔 결과 기반)

| 이슈 | 현재 코드 영향 | 조치 |
| --- | --- | --- |
| **전역 API 변경** | `new Vue(...)` 패턴 **25개 파일**(main.js + EventBus 다수) | `new Vue()`→`createApp()`. EventBus용 `new Vue()`는 **mitt** 등 경량 이벤트 버스로 대체 |
| **Filters 제거** | `Vue.filter`/`filters:` 4개 파일 | Vue 3에서 필터 제거 → computed/메서드/전역 프로퍼티로 변환 |
| **Vue.prototype** | 17곳 | `app.config.globalProperties.*`로 변경 |
| **`.sync` / `$listeners`** | 26곳 | `.sync`→`v-model:prop`. `$listeners`는 `$attrs`로 통합 |
| **번들러** | vue-cli 3 + webpack 4, `NODE_OPTIONS=--openssl-legacy-provider`, `runtimeCompiler:true`, ts-loader | **Vite**로 전환. legacy-provider 플래그 불필요. vue.config.js→vite.config.ts, alias `@` 재설정 |
| **클래스 컴포넌트** | `vue-class-component 7`, `vue-property-decorator 9` | Vue 3에서 지원 약함 → **Composition API(`<script setup>`)** 이전 권장. 클래스 유지 시 `vue-facing-decorator` |
| **라우터/스토어/i18n** | vue-router 3, Vuex 3, vue-i18n 8 | router 4(`createRouter`+history API), **Pinia**(또는 Vuex 4), vue-i18n 9 |

### 4.2 UI·유틸 라이브러리 교체 매핑

| 현재 | 상태 | 권장 대체 |
| --- | --- | --- |
| buefy 0.7 + bulma 0.7 | **Vue 3 미지원** | **Oruga UI** + Bulma 테마 (Buefy 후속) |
| element-ui 2.15 | **Vue 3 미지원** | **Element Plus** |
| ag-grid-community/enterprise/vue 25 | Vue3 래퍼 변경 | ag-grid **31+** + `ag-grid-vue3` (API 대거 변경, 별도 검증 필요) |
| ag-charts-vue 9 | 래퍼 변경 | `ag-charts-vue3` |
| vuex-persistedstate | Pinia 전환 시 | `pinia-plugin-persistedstate` |
| axios 0.18 | 구버전 | axios **1.x** (인터셉터/에러 처리 일부 변경) |
| date-fns 1.30 / moment | 노후 | date-fns **3.x** 또는 dayjs로 통일 |
| sweetalert2 7 + vue-sweetalert2 | 구버전 | sweetalert2 11 + 호환 래퍼 |
| vuejs-datepicker | Vue3 미지원 | `@vuepic/vue-datepicker` 등 |
| vue-the-mask | Vue3 미지원 | `maska` |
| vue-loading-overlay 3 | Vue3 미지원 버전 | vue-loading-overlay **6.x** |
| vue-pdf 4 | Vue3 미지원 | `vue-pdf-embed` |
| swiper 4 | 구버전 | swiper **11** (Vue 컴포넌트 방식 변경) |
| jquery / jquery-ui | 잔존 | 제거 목표. 불가 시 모듈 격리 |
| ESLint airbnb(레거시 config) | Flat config 전환 | eslint flat config + `eslint-plugin-vue` 9 |

### 4.3 권장 절차 (점진 이전)
1. **신규 셸 생성**: `npm create vue@latest`로 Vite + Vue 3 + (TS 선택) + Pinia + Router 스캐폴딩.
2. **공통 인프라 이식**: HTTP 래퍼(axios), 라우터, 스토어, i18n, 전역 스타일/테마, 인증 가드를 먼저 올림.
3. **(선택) Vue 2.7 경유 전략**: 기존 코드를 Vue 2.7로 먼저 올려 **Composition API/`<script setup>`로 로직을 이전**해두면, Vue 3 점프 시 리스크가 크게 줄어듭니다. 시간 여유가 있으면 권장.
4. **`@vue/compat`(마이그레이션 빌드)**: 초기에 호환 모드로 띄워 deprecation 경고를 잡으며 화면을 하나씩 정상화.
5. **화면 단위 이전**: 의존 UI 라이브러리가 적은 화면부터 → 표(ag-grid)·복합 폼 화면은 후반에. 화면별로 동작·스타일 회귀 확인.

---

## 5. 단계별 로드맵 (제안)

| 단계 | 내용 | 산출물/게이트 |
| --- | --- | --- |
| **0. 준비** | 신규 레포/폴더 생성, 빌드 파이프라인·테스트 환경 구성, 회귀 테스트 시나리오 정의 | 테스트 체크리스트 |
| **1. 백엔드 변환** | 스켈레톤+OpenRewrite, Security/Swagger/QueryDSL 정비, 빌드 통과 | `./gradlew build` 성공 |
| **2. 백엔드 검증** | 도메인별 통합 테스트, Hibernate 6 쿼리 회귀, 프로퍼티 점검 | API 회귀 통과 |
| **3. 프론트 인프라** | Vite+Vue3 셸, 라우터/스토어/i18n/HTTP/인증 이식 | 로그인~메인 동작 |
| **4. 프론트 화면 이전** | 화면 배치별 이전 + UI 라이브러리 교체 | 화면별 QA 통과 |
| **5. 통합·안정화** | E2E, 성능, 보안 점검, 운영 배포 스크립트 정비 | UAT 통과 |

---

## 6. 리스크 및 유의사항

- **프론트가 진짜 병목**: Buefy/Element-UI/ag-grid 동시 교체는 UI/UX 회귀가 크므로, **디자인·동작 검증 인력과 시간**을 백엔드보다 더 확보해야 합니다.
- **ag-grid 25 → 31+**: 컬럼/이벤트/렌더러 API 변경 폭이 커서 전표 그리드 화면은 별도 PoC로 먼저 검증 권장.
- **Hibernate 6 회귀**: 자동 변환으로 잡히지 않는 쿼리 결과/정렬/널 처리 차이가 운영에서 드러날 수 있음 → 데이터 기반 회귀 테스트 필수.
- **인증/보안 화이트리스트**: Security DSL 재작성 시 공개 경로(`antMatchers` 13곳) 누락 시 장애 직결 → 경로 매핑 표로 관리.
- **외부 연동**: Firebase(푸시), 메일, OAuth2 클라이언트, WebSocket, ERP 뷰는 라이브러리 상향 후 반드시 개별 검증.
- **병행 운영**: 신규 폴더에서 작업하는 동안 기존 운영 코드의 변경분을 주기적으로 동기화하는 정책 필요(브랜치 전략·동결 시점 합의).
- **롤백 계획**: 단계별 게이트마다 롤백 가능한 배포 단위 유지.

---

## 7. 즉시 시작 체크리스트

**백엔드**
- [ ] SB 4.0 / Java 21 / Gradle 8.10+ 스켈레톤 생성
- [ ] `compile/testCompile/runtime` → `implementation/...` 치환, `libs` flatDir 의존성 정리
- [ ] OpenRewrite Spring Boot 업그레이드 레시피 실행 (`javax`→`jakarta` 등)
- [ ] `SecurityConfig` → `SecurityFilterChain` 재작성, `requestMatchers` 전환
- [ ] Springfox → springdoc-openapi v2 교체
- [ ] QueryDSL 5.x(jakarta) + annotationProcessor 설정, Q클래스 생성 확인
- [ ] firebase-admin·poi·gson·guava·java-jwt·modelmapper·SBA 클라이언트 상향
- [ ] `spring-boot-properties-migrator`로 프로퍼티 점검 후 제거

**프론트**
- [ ] Vite + Vue 3 + Pinia + Router 스켈레톤 생성 (`npm create vue@latest`)
- [ ] Node 22/24 LTS로 전환 (volta 핀 변경), `--openssl-legacy-provider` 제거
- [ ] HTTP/라우터/스토어/i18n/인증 인프라 이식
- [ ] EventBus(`new Vue()`) → mitt, filters/`Vue.prototype`/`.sync` 변환
- [ ] Buefy→Oruga, Element-UI→Element Plus, ag-grid→31+ 교체 PoC
- [ ] axios 1.x, date 라이브러리, sweetalert2 등 부가 라이브러리 상향
- [ ] 화면 배치 우선순위 확정 후 점진 이전 시작

---

### 참고 (지원 일정, 2026-06 조사 기준)
- Spring Boot 4.0: 2025-11-30 출시, 최신 4.0.7. Java 17~25 지원. / 3.5 OSS 지원 2026-06-30 종료.
- Java: 21·25가 LTS. 21이 수명·안정성 균형상 권장.
- Node.js: 18 EOL(2025-04). 22·24가 현행 LTS.
- Vue 2: 2023-12 EOL. Vue 3.5가 현행.

출처: endoflife.date (Spring Boot, Node.js, Java distributions) — 2026-06 조회. 코드 영향 수치는 현재 레포(`ije-eacct-back`, `ije-eacct-front`) 정적 스캔 결과.
