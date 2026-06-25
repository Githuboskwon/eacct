# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

일진전자 전자경비결재 시스템(eAcct). Vue.js 프론트엔드 + Spring Boot 백엔드로 구성된 전표·승인·예산 관리 웹 애플리케이션.

---

## 빌드 및 실행 명령어

### 프론트엔드 (`ije-eacct-front/`)

```bash
npm install                         # 의존성 설치
npm run serve                       # 개발 서버 (기본 포트 19050, .env 기준)
npm run serve:local                 # 로컬 모드
npm run build                       # 운영 빌드 → dist/
npm run build:development           # 개발 빌드
npm run lint                        # ESLint
npm run test:unit                   # 단위 테스트
```

### 백엔드 (`ije-eacct-back/`)

```bash
./gradlew build                     # 전체 빌드 (테스트 포함)
./gradlew build -x test             # 테스트 제외 빌드
./gradlew bootRun                   # 개발 서버 실행 (기본 프로파일: dev)
./gradlew test                      # 전체 테스트 실행
./gradlew test --tests "패키지.클래스명" # 단일 테스트 클래스 실행
```

> JAR 아티팩트명: `eacct-back.jar` (`build/libs/`)

### 실행 프로파일

| 프로파일 | 설명 |
|----------|------|
| `dev` | 개발 (Oracle TEST DB, 포트 19500) |
| `linux` | 리눅스 배포 (Oracle MATRIX DB, 포트 19500) |
| `production` | 운영 (Oracle PROD DB, 포트 19500) |
| `win` | Windows 로컬 |

```bash
java -jar eacct-back.jar --spring.profiles.active=production
```

---

## 아키텍처 개요

### 전체 구성

```
프론트엔드 (Vue 2, 포트 19050)
    ↓ HTTP (axios, VUE_APP_API_URL)
백엔드 API (Spring Boot 3.3.5, 포트 19500)
    ↓ JPA / QueryDSL
Oracle DB
```

- 세션: Spring Session JDBC (Oracle 테이블 저장, 쿠키 기반)
- SSO: Saerom OnePass (`rathon_sso_sp_*.properties` + `libs/*.jar`)
- 파일 업로드 경로: `/data/apps/ije-eacct/uploads` (Linux 운영)

---

### 백엔드 패키지 구조 (`com.iljin.apiServer`)

```
core/           공통 인프라 (업무 도메인과 무관)
  aop/          LogAspect (Controller 진입/반환 로깅)
  audit/        BaseEntity, BaseTimeEntity (JPA Auditing)
  config/       Security·JPA·WebMvc·Swagger·스케줄·파일저장 설정
  security/     인증·인가 (MemberService, UserRepository, SSO, AES, OAuth)
  files/        파일 업로드·다운로드 서비스
  mail/         메일 발송
  mPush/        Firebase 모바일 푸시
  scheduled/    스케줄 배치 작업
  util/         Util, Pair, Error, XmlParser 등

ijeas/          업무 도메인
  slip/         전표 (헤더·상세, 세금계산서, ERP 연동, 선급금 등)
  slipCommon/   전표 공통 (계정과목 계층, 보조부문 등)
  approval/     전자결재 (결재선, 결재 규칙, 위임)
  system/       코드·메뉴·권한·사원·거래처·비용센터 등 시스템 관리
  bond/         채권·채무
  card/         법인카드
  costBudget/   예산 관리
  es/           전자세금계산서
  file/         첨부파일 관리
  mail/         결재 메일 발송
  mobile/       모바일 API
  sm/           스마트빌 연동
  security/     로그인·권한 관리 API
```

**Controller 패턴**: `@RestController` + `@RequestMapping("/api/{도메인}")`, `@CrossOrigin` 적용.  
공개 URL 패턴: `/login`, `/logout`, `/api/v1/user*/**`, `/api/v1/download2*/**`.

**Entity 규칙**:
- 등록자·수정자 추적 필요 시 `BaseEntity` 상속 (`regId`, `chgId`, `regDtm`, `chgDtm`)
- 일시만 필요 시 `BaseTimeEntity` 상속
- M:N 연관관계 금지, 단방향·양방향 명시 필수
- 쿼리는 QueryDSL 우선, 복잡한 경우 Native Query 또는 JPQL 허용

---

### 프론트엔드 구조 (`src/`)

```
views/          라우트 진입점 (wrapper 역할, 기능 로직 최소화)
components/     실제 기능 컴포넌트
  agGrid/       ag-Grid 관련 공통 컴포넌트
mixin/          공통 믹스인
  common.js     공통 유틸 (API 호출, 알림 등)
  slip.js       전표 처리 공통 로직
  slip-basic.js 전표 기본 믹스인
  slip-table.js 전표 테이블 믹스인
libs/           유틸리티
  eventBus.js   mitt 기반 이벤트 버스 (Vue 3 마이그레이션 대응)
store.js        Vuex 전역 상태 (loginInfo, openDate, searchForm, locale, isLoading)
router.js       Vue Router (history 모드)
i18n.js         다국어 (ko/en)
```

**주요 UI 라이브러리**: Element UI + Buefy (컴포넌트), ag-Grid Enterprise 25 (데이터 그리드)

**Vuex 주요 상태**:
- `loginInfo`: 사용자 정보, 권한(`authorities`), 메뉴 목록
- `openDate`: 회계 기준 월 (현재월 기준 -3개월 ~ 익월)
- `isLoading`: 전역 로딩 인디케이터

**환경변수 (`.env.*`)**:
- `VUE_APP_API_URL`: 백엔드 API 베이스 URL
- `VUE_APP_MO_URL`: 모바일 API URL
- `VUE_APP_COMP_CODE`: 회사 코드
- `VUE_APP_PORT`: 개발 서버 포트 (기본 19050)

---

## 주의사항

- **trailing slash**: 일부 프론트 API 호출이 끝 슬래시(`/api/erp/slip/fund/list/`)를 사용함. `WebMvcConfig`에서 전역 허용 처리되어 있음.
- **ag-Grid 라이선스**: `main.js`에 Enterprise 라이선스 키 하드코딩됨 (만료된 평가 라이선스).
- **SSO**: `rathon_sso_sp_*.properties` 및 `libs/` 내 Saerom OnePass JAR 필요. 미설정 시 SSO 인증 불가.
- **QueryDSL Q타입**: `./gradlew compileJava` 실행 후 `build/generated/sources/` 에 생성됨.
- **테스트 제외**: `TaxDtiNtsMainQdslRepositoryTest`, `CodeQdslRepositoryTest` 는 JUnit4 레거시로 빌드에서 제외됨.
- **Vue Router history 모드**: Nginx 또는 웹서버에서 `try_files $uri $uri/ /index.html` 설정 필수.
