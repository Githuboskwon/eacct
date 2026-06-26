# 백엔드 웹 보안 취약점 점검 보고서

- **대상**: `ije-eacct-back/` (Spring Boot 3.3.5, Spring Security, JPA/QueryDSL, Oracle)
- **점검일**: 2026-06-26
- **점검 범위**: 인증/인가(`core/security`, `ijeas/security`), 보안설정(`SecurityConfig`), 설정파일(`application-*.properties`), SQL 인젝션(네이티브 쿼리/QueryDSL), 파일 업로드·다운로드, 암호화 유틸
- **점검 방식**: 정적 코드 분석(소스 코드 수정 없음)

---

## 요약 (심각도별)

| # | 심각도 | 항목 | 위치 |
|---|--------|------|------|
| B-01 | 🔴 치명 | 인증 우회 — `token` 파라미터만 있으면 비밀번호 검증 생략 | `UserServiceImpl.java:178` |
| B-02 | 🔴 치명 | 사용자/권한 관리 API 전체가 `permitAll` (비인증 접근) | `SecurityConfig.java:70` |
| B-03 | 🔴 치명 | DB·메일·스마트빌·Admin 자격증명 평문 하드코딩 (운영 포함) | `application-*.properties` |
| B-04 | 🔴 높음 | Actuator 전체 엔드포인트 노출 + `permitAll` | `application.properties:56`, `SecurityConfig.java:68` |
| B-05 | 🔴 높음 | AES 대칭키 소스 하드코딩 | `AES_Encryption.java:15` |
| B-06 | 🔴 높음 | SQL 인젝션 — 네이티브 쿼리 문자열 결합 다수 | `SlipRepositoryCustomImpl.java:109` 등 |
| B-07 | 🔴 높음 | 권한 체크(@PreAuthorize) 전무 — 로그인만 하면 전 API 접근 | 전체 컨트롤러 |
| B-08 | 🔴 높음 | 파일 다운로드 IDOR + `download2` permitAll | `FileController.java:76`, `SecurityConfig.java:71` |
| B-09 | 🟠 중간 | 업로드 확장자/MIME 화이트리스트 부재 (웹쉘 업로드) | `FileServiceImpl.java:64` |
| B-10 | 🟠 중간 | Path Traversal — 다운로드 시 경로 경계 검증 누락 | `FileServiceImpl.java:105` |
| B-11 | 🟠 중간 | 세션 타임아웃 1년, 세션 고정 방어 미설정 | `SecurityConfig.java:30` |
| B-12 | 🟠 중간 | 계정 잠금/브루트포스 방어 전무, 비활성 계정도 로그인 | `MemberService.java:39` |
| B-13 | 🟠 중간 | CSRF 비활성 + 세션쿠키 사용 / CORS `@CrossOrigin` 와일드카드 | `SecurityConfig.java:64` |
| B-14 | 🟠 중간 | OAuth 토큰을 URL 쿼리스트링으로 전달 / state 미검증 | `OAuthServiceImpl.java:274` |
| B-15 | 🟢 낮음 | `show-sql`, `printStackTrace`, 임시 비번 평문 메일 등 | 다수 |

> **가장 시급**: B-01(인증 우회) + B-02(관리 API permitAll)는 **공격자가 로그인 없이도 사용자 생성·권한 변경·전사 비밀번호 초기화**를 할 수 있는 조합입니다. 즉시 조치가 필요합니다.

---

## 상세 내역

### B-01 🔴 치명 — 인증 우회 (`token` 분기로 비밀번호 검증 생략)

`core/security/user/UserServiceImpl.java:178-192`
```java
UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginId, loginPw);
if(loginToken != null && !loginToken.equals("")) {
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    token = new UsernamePasswordAuthenticationToken(loginId, null, grantedAuthorities);  // 인증된 토큰 생성
} else {
    token = (UsernamePasswordAuthenticationToken) authenticationManager.authenticate(token);
}
```
요청 본문의 `token` 값이 **비어있지만 않으면**(임의 문자열 허용) `authenticationManager.authenticate()`를 건너뛰고 **인증된 토큰**을 만듭니다. `token`은 어떤 저장값과도 대조되지 않습니다(`UserDto.token`은 단순 String). `/login`은 `permitAll`이므로:
```
POST /login  {"loginId":"admin","token":"x","compCd":"..."}  → 비밀번호 없이 로그인 성공
```
유효한 `loginId`만 알면 누구나 해당 계정으로 인증됩니다.

**권장**: `token` 분기 제거. OAuth 연동이 필요하면 서버가 발급·저장한 **일회성 토큰을 서버측에서 검증**하도록 변경.

---

### B-02 🔴 치명 — 사용자/권한 관리 API 전체 `permitAll`

`core/config/SecurityConfig.java:70`
```java
.requestMatchers("/api/v1/user*/**").permitAll()
```
`/api/v1/user`로 시작하는 모든 경로가 **인증 없이** 공개됩니다. `UserController`의 다음이 비인증 호출 가능:

- `GET /api/v1/user/list` — 전 사용자 목록(loginId·이름·권한 노출)
- `POST /api/v1/user/add`, `DELETE /api/v1/user/{loginId}`, `PUT /api/v1/user/update` — **비인증 사용자 생성/삭제/권한 변경**
- `POST /api/v1/user/reset-pw`, `GET /api/v1/user/init-all-passwords` — **비인증 임의 비밀번호 초기화 / 전사 일괄 초기화**
- `GET /api/v1/user/login-id/{loginId}` — 임의 사용자 엔티티 조회(BCrypt 해시 포함 가능)

B-01과 무관하게, **로그인 자체가 불필요**하여 더 심각합니다.

**권장**: 해당 매처 제거하고 관리 엔드포인트에 관리자 역할 강제(`@PreAuthorize("hasRole('ADMIN')")`). 로그인에 꼭 필요한 단일 경로만 좁게 허용.

---

### B-03 🔴 치명 — 자격증명 평문 하드코딩 (운영 포함)

`application-production.properties`
```
spring.datasource.url=jdbc:oracle:thin:@197.200.11.77:1521/PROD
spring.datasource.username=CBOEA
spring.datasource.password=cboea                 # 사용자명 소문자 = 매우 취약
...
spring.boot.admin.client.username=admin
spring.boot.admin.client.password=admin          # admin/admin
etax.smartbill.id=ije12486
etax.smartbill.pwd=a1234567                       # 스마트빌(전자세금계산서) 평문
spring.mail.host=197.200.11.57                    # 내부 메일서버
```
- 운영 DB 비밀번호가 사용자명 소문자(`cboea`)로 매우 약하며 **저장소에 평문 커밋**.
- Spring Boot Admin 자격증명 `admin/admin`.
- 외부 연동(스마트빌) 자격증명 평문 노출.
- 모든 프로파일(`dev/linux/win/production`)에 동일 패턴.

**권장**: 모든 자격증명을 환경변수/Vault/암호화 설정(Jasypt 등)으로 외부화하고 저장소에서 제거. DB 비밀번호 강화 및 즉시 로테이션. Git 히스토리에서 제거 검토.

---

### B-04 🔴 높음 — Actuator 전체 노출 + `permitAll`

`application.properties` 계열
```
management.endpoints.web.exposure.include=*       # 모든 actuator 엔드포인트 공개
```
`SecurityConfig.java:68`
```java
.requestMatchers("/actuator/**").permitAll()
```
`/actuator/env`, `/actuator/heapdump`, `/actuator/threaddump`, `/actuator/mappings` 등이 **비인증** 노출됩니다. `env`/`configprops`로 B-03의 DB 비밀번호·시크릿이 그대로 유출될 수 있습니다.

**권장**: `management.endpoints.web.exposure.include=health,info`로 최소화, actuator는 인증·내부망 한정, heapdump 비활성.

---

### B-05 🔴 높음 — AES 대칭키 소스 하드코딩

`core/security/aes/AES_Encryption.java:15`
```java
static String key = "12345678901234567890123456789012";
```
인증서 비밀번호 암복호화(`CertificateServiceImpl.java:82,94`, `CertificateController.java:46`)에 사용됩니다. 소스/저장소 접근만으로 모든 인증서 비밀번호를 복호화할 수 있고 키 로테이션이 불가합니다.

추가로 `AES/CBC/NoPadding` + 직접 구현한 비표준 패딩(`AES_Encryption.java:28,36`)으로 무결성 보호(MAC)가 없어 패딩 오라클/비트 플리핑에 노출됩니다(블록 크기를 128**바이트**로 다루는 구현 오류 존재).

**권장**: 키를 환경변수/Vault로 외부화, **AES-GCM**(인증 암호화)로 전환, 표준 라이브러리 패딩 사용.

---

### B-06 🔴 높음 — SQL 인젝션 (네이티브 쿼리 문자열 결합)

같은 메서드에서 `setParameter` 바인딩을 쓰면서도 일부 사용자 입력 필드만 따옴표로 감싸 직접 결합한 지점들입니다(`@RequestBody` DTO의 String 필드 → 실제 도달 가능).

- `ijeas/slip/SlipRepositoryCustomImpl.java:109,141` — `wrtDeptCd`/`slipStatCd`를 `split(",")` 후 `IN ('"+v+"')` 결합
  ```java
  sb.append("'" + depts[i] + "'");
  ```
- `ijeas/costBudget/CostBudgetRepositoryCustomImpl.java:1309,1313,1333` — `acct_code`, `search_dept_code` 를 `= '...'` 결합. 저장/프로시저 경로(`:2751,2921` `slip_no` 등)도 동일
- `ijeas/ims/pjtPersonnelPlan/PjtPersonnelPlanRepositoryCustomImpl.java:179,180,210,211,356,357,386,387` — `projectManageNo`, `degree`
- `ijeas/ims/pjtProcessRatePlan/PjtProcessRatePlanRepositoryCustomImpl.java:58,59,114,115`
- `ijeas/ims/pjtProgressChart/PjtProgressChartRepositoryCustomImpl.java:216,220,224,228` — `projectName`, `projectGubun`, `countryCode`, `countryName`
- `ijeas/es/gl/GlRepositoryCustomImpl.java:86` — `INTEGRATION_VENDOR_NUM = '"+vendorCd+"'`
- (중간) `ijeas/slip/etax/SalesTaxInvoiceRepositoryImpl.java:299` — `dtiStatus` 분기 외 값 결합

**안전 확인**: 18개 파일의 `@Query(nativeQuery=true)`는 전부 `:param`/`?n` 바인딩, `Expressions.stringTemplate`은 컬럼 경로/상수 포맷만 사용, 정렬·컬럼명 동적 결합은 없음. 숫자 연산으로 만든 `period_m*`/인덱스 결합은 주입 불가.

**권장**: 위 지점 모두 `setParameter` 바인딩으로 전환. `IN` 다건은 컬렉션 바인딩(`:list`) 사용.

---

### B-07 🔴 높음 — 메서드/URL 수준 권한 체크 전무

- 전체 코드베이스에 `@PreAuthorize`/`@Secured`/`hasRole`/`@RolesAllowed` **0건**. `@EnableMethodSecurity(prePostEnabled=true)`(`SecurityConfig.java:31`)는 켜져 있으나 미사용.
- 유일한 인가 정책은 `anyRequest().authenticated()`. 즉 **로그인(또는 B-01 우회)만 하면 역할 구분 없이 모든 민감 API 접근 가능** — 권한관리(`RoleController`), 사원관리(`EmployeeController`), 코드관리(`CodeController`), 결재선 등.

**권장**: 관리/설정 도메인 컨트롤러에 역할 기반 `@PreAuthorize` 도입. 최소 권한 원칙 적용.

---

### B-08 🔴 높음 — 파일 다운로드 IDOR + `download2` permitAll

`SecurityConfig.java:71`
```java
.requestMatchers("/api/v1/download2*/**", "/home", "/login/**").permitAll()
```
`FileController.downloadFile2`(`:76`)는 `fileName`(UUID) 또는 `id`(PK)로 **인증·소유자·compCd·문서권한 검증 없이** 임의 파일 다운로드. `id`는 단순 PK라 열거 가능. 인증이 필요한 다른 다운로드 엔드포인트(`downloadJiniFile`, `downloadBeforeFile`)도 compCd만 맞추고 사용자/문서 단위 인가는 없어 **IDOR 공통**.

**권장**: `download2` permitAll 재검토, 모든 다운로드에 로그인 사용자-파일 소유/문서 권한 검증 추가.

---

### B-09 🟠 중간 — 업로드 확장자/MIME 화이트리스트 부재 (웹쉘)

`core/files/FileServiceImpl.java:64-85`, `ijeas/sm/evid/UFileServiceImpl.java:120-150`, `ijeas/sm/jiniEvid/UJiniFileServiceImpl.java:111-142`
- 허용 확장자/MIME 화이트리스트나 `.jsp/.html/.svg` 차단 없음. 확장자 비교는 썸네일 제외 용도뿐.
- 저장명은 UUID로 난수화되나(`storedName = uuid + ext`) **원본 확장자를 그대로 보존** → 화이트리스트 부재와 결합 시 `uuid.jsp`/`uuid.html` 디스크 저장. 저장경로(`./app/uploads`)가 웹서버 설정에 따라 노출되면 실행 위험.
- `ext = fileName.substring(fileName.lastIndexOf("."))` — 확장자 없으면 예외 가능.

**권장**: 업로드 시 확장자/MIME 화이트리스트 강제, 실행 확장자 차단, 저장 확장자도 정규화.

---

### B-10 🟠 중간 — Path Traversal (다운로드 경로 경계 검증 누락)

`core/files/FileServiceImpl.java:105-151`
```java
final Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
// 결과가 fileStorageLocation 하위인지(startsWith) 검증 없음
```
`loadFileAsResource`/`loadJiniFileAsResource`/`loadOldFileAsResource` 모두 `normalize()` 후 경계 검증이 없어, `..`/절대경로 입력 시 저장소 밖 파일 접근 가능. 업로드 측 `contains("..")` 차단은 다운로드 입력에는 미적용. `UJiniFileServiceImpl.java:200,223`는 DB 경로를 문자열 결합.

**권장**: `filePath.startsWith(fileStorageLocation)` 검증 추가, 모든 경로 조립을 `resolve().normalize()` + 경계 체크로 통일.

---

### B-11 🟠 중간 — 세션 타임아웃 1년 / 세션 고정 방어 미설정

`SecurityConfig.java:30`
```java
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 31536000)  // 365일
```
- 비활성 세션이 사실상 무기한 → 탈취 세션 장기 유효(`application.properties`의 `session.timeout=86400`과도 불일치).
- 로그인 시 Spring Security 폼 필터를 거치지 않고 수동으로 SecurityContext를 주입(`UserServiceImpl.java:276`)하여 **세션 ID 재발급(session fixation 보호)이 없음**. `SessionCreationPolicy.NEVER`와 맞물림.

**권장**: 타임아웃 현실화(30분~수시간), 로그인 성공 시 세션 ID 재발급.

---

### B-12 🟠 중간 — 계정 잠금/브루트포스 방어 전무

`core/security/user/MemberService.java:39-42`
```java
member.setEnabled(true);
member.setAccountNonExpired(true);
member.setAccountNonLocked(true);
member.setCredentialsNonExpired(true);
```
모든 계정을 무조건 활성/비잠금 처리 → `enableFlag`(비활성 계정)도 로그인 가능, 실패 횟수 추적/잠금 없음. 로그인 실패는 `LoginHistory` 기록만(`UserServiceImpl.java:239`).

**권장**: 실패 횟수 기반 잠금, `enableFlag` 반영, (선택) CAPTCHA/지연.

> 양호: 비밀번호는 BCrypt 사용(`SecurityConfig.java:57`, `encode/matches`), MD5/SHA1/평문 비교 없음.

---

### B-13 🟠 중간 — CSRF 비활성 + 세션쿠키 / CORS 와일드카드

- `SecurityConfig.java:64` `csrf.disable()` — 쿠키 세션 기반(`CookieHttpSessionIdResolver`)인데 CSRF 비활성. SPA+토큰 의도로 보이나 세션 쿠키를 쓰므로 CSRF 노출.
- 다수 컨트롤러에 인자 없는 `@CrossOrigin`(예: `UserController.java:14`, `AuthorityController.java:14` 등) → 기본값이 모든 Origin 허용. 자격증명 동반 요청과 결합 시 CORS 정책 약화.
- `SecurityConfig.java:78` `frameOptions.disable()` + `ALLOW-FROM` (구식·미지원 헤더) → 클릭재킹 방어 사실상 없음.

**권장**: 세션 기반이면 CSRF 토큰 적용 또는 `SameSite` 쿠키, `@CrossOrigin`에 허용 Origin 명시, `X-Frame-Options: DENY` 또는 CSP `frame-ancestors`.

---

### B-14 🟠 중간 — OAuth 토큰 URL 노출 / state 미검증

`core/security/oauth/OAuthServiceImpl.java:274,312`
```java
URI redirectUri = new URI(frontAddress + "/login?loginId=" + ... + "&token=" + authToken.getToken());
```
`loginId`/`token`이 리다이렉트 쿼리스트링에 노출(브라우저 히스토리·Referer·로그에 잔존)되고, 이 `token`이 B-01의 검증 없는 인증 분기로 들어갑니다. Naver `state` CSRF 검증 로직 부재, 예외 시 `printStackTrace`(`:97,137`).

**권장**: 토큰을 쿼리스트링 대신 본문/HttpOnly 쿠키로 전달, `state` 검증, 토큰 issuer/유효성 검증.

---

### B-15 🟢 낮음 — 기타

- `spring.jpa.show-sql=true` + `format_sql`/`use_sql_comments`(`application-production.properties:10-12`) — 운영에서 SQL 로그 노출.
- `e.printStackTrace()`/`System.out.println` 다수(`UserServiceImpl.java:323,378`, `OAuthServiceImpl.java:97`) — 내부정보 노출, 로깅 일관성 부재.
- 임시 비밀번호를 평문 메일로 발송(`UserServiceImpl.java:308`).
- Swagger UI가 `permitAll`(`SecurityConfig.java:72`) — 운영에서 API 스펙 노출.

---

## 권장 조치 우선순위

1. **(즉시/치명) B-01 인증 우회 분기 제거**, **B-02 `/api/v1/user*/**` permitAll 제거** — 비인증 계정 탈취·전사 비번 초기화 차단.
2. **(즉시) B-03 자격증명 외부화 + 로테이션**, **B-04 Actuator 노출 최소화** — 시크릿 유출 차단.
3. **(높음) B-06 SQL 인젝션 바인딩 전환**, **B-05 AES 키 외부화/GCM 전환**.
4. **(높음) B-07 관리 API 역할 기반 인가(@PreAuthorize)**, **B-08 다운로드 인가/IDOR 차단**.
5. **(중간) B-09~B-14** 업로드 화이트리스트·경로 검증·세션 정책·CSRF/CORS·OAuth 보강.

---

## 참고

- 본 보고서는 백엔드 정적 분석 결과입니다. 프론트엔드 점검은 `ije-eacct-front/SECURITY_AUDIT_FRONTEND.md` 참조.
- 라인 번호는 점검일 기준이며 코드 변경 시 달라질 수 있습니다.
- **B-01 + B-02 조합은 원격 비인증 권한 탈취가 가능한 수준**이므로 다른 항목보다 먼저 조치할 것을 강력히 권고합니다.
