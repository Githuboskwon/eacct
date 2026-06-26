# 프론트엔드 웹 보안 취약점 점검 보고서

- **대상**: `ije-eacct-front/` (Vue.js)
- **점검일**: 2026-06-26
- **점검 범위**: 프론트엔드 소스(`src/`), 환경설정(`.env.*`, `vue.config.js`), 정적 리소스(`public/index.html`)
- **점검 방식**: 정적 코드 분석(소스 코드 수정 없음)

---

## 요약 (심각도별)

| # | 심각도 | 항목 | 위치 |
|---|--------|------|------|
| F-01 | 🔴 높음 | 운영 포함 전 구간 HTTP 평문 통신 (TLS 없음) | `.env.*`, `index.html` |
| F-02 | 🔴 높음 | 인증 토큰/권한/loginInfo를 비-HttpOnly 쿠키 + localStorage 평문 저장 | `Login.vue`, `store.js` |
| F-03 | 🔴 높음 | `index.html`에서 외부 jQuery를 HTTP로 로드 (MITM 코드 주입) | `public/index.html` |
| F-04 | 🔴 높음 | `eval()` 다수 사용 | `BudgetMng.vue` 등 |
| F-05 | 🔴 높음 | `innerHTML` / `document.write`에 서버 URL 직접 삽입 (DOM XSS) | 첨부 팝업 다수 |
| F-06 | 🔴 높음 | OAuth `state` 값이 고정 상수 (CSRF 방어 무력화) | `Login.vue` |
| F-07 | 🟠 중간 | CSP / X-Frame-Options 등 보안 헤더 전무 (클릭재킹) | `index.html`, `vue.config.js` |
| F-08 | 🟠 중간 | `postMessage` 송신 시 `targetOrigin: '*'` 사용 | `Login.vue` |
| F-09 | 🟠 중간 | 인증 토큰을 `console.log`로 출력 (운영 빌드 포함) | `Login.vue` 등 |
| F-10 | 🟠 중간 | 검증 없는 서버 URL을 `window.open`/`location`으로 오픈 (open redirect) | 첨부 팝업 다수 |
| F-11 | 🟠 중간 | 하드코딩된 키/식별자, 내부 IP·DMZ·도메인 노출 | `main.js`, `Login.vue`, `.env.*` |
| F-12 | 🟢 낮음 | 인터셉터 `config.data` 미가드, 권한코드 로그 등 | `main.js` 등 |

> **근본 원인**: ① 전 구간 **HTTP 평문 통신** ② **JS에서 접근 가능한 저장소(쿠키/localStorage)에 토큰 보관**. 이 둘이 XSS(`eval`/`innerHTML`) 및 MITM(HTTP CDN)과 결합되면 자격증명 탈취가 현실적인 공격 경로가 됩니다.

---

## 상세 내역

### F-01 🔴 전 구간 HTTP 평문 통신 (운영 포함)

모든 백엔드 API URL이 `http://`로 설정되어 있어 토큰·자격증명·전표 데이터가 평문으로 전송됩니다.

`.env.production`
```
VUE_APP_API_URL=http://10.12.21.212:19500
VUE_APP_MO_URL=http://10.12.21.212:19051
```
`.env.development`
```
VUE_APP_API_URL=http://10.12.21.212:19500
VUE_APP_MO_URL=http://10.12.21.212:19051
```

- 운영 빌드도 TLS 미적용 → 네트워크 도청 시 세션 토큰/비밀번호 즉시 탈취.
- 주석에 내부 IP(`197.200.x`), DMZ IP(`168.126.74.x`), 내부 도메인(`eleceas.iljin.co.kr`)이 함께 노출.

**권장**: 운영 구간 전체 HTTPS 전환, HSTS 적용, `.env` 내 내부 인프라 정보 제거.

---

### F-02 🔴 인증 토큰을 JS 접근 가능 저장소에 평문 저장

`src/components/Login.vue:168-171`
```js
this.$store.commit('login', loginInfo);
this.$cookie.set('loginInfo', JSON.stringify(loginInfo));   // HttpOnly/Secure/SameSite 없음
this.$cookie.set('sessionAlive', true);
this.$http.defaults.headers['x-auth-token'] = loginInfo.token;
```
`src/store.js:16`
```js
plugins: [createPersistedState()],   // 기본 저장소 localStorage('vuex' 키), loginInfo 전체 영속화
```

- `vue-cookie`로 설정한 쿠키는 `HttpOnly`/`Secure`/`SameSite` 미설정 → XSS로 즉시 탈취 가능.
- Vuex 전체 상태(`token`, `authorities`, `loginPw` 필드 포함)가 localStorage에 평문 저장됨(`Login.vue:331`의 `localStorage.removeItem('vuex')`로 영속 사실 확인).

**권장**: 세션 토큰은 `HttpOnly`+`Secure`+`SameSite=Strict` 쿠키로 서버가 설정(JS 접근 차단). 클라이언트 영속 스토리지에서 토큰/권한 제거. `loginPw` 필드를 영속 상태에서 제거.

---

### F-03 🔴 외부 jQuery를 HTTP로 로드 (MITM 코드 주입)

`public/index.html:46-47`
```html
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
```

- `http://` CDN → 중간자 공격 시 임의 JS 주입 가능.
- `jquery-latest.min.js`는 **버전 고정 안 됨** + SRI(`integrity`) 해시 없음.
- 앱이 HTTP로 운영(F-01)되어 mixed-content 차단도 적용되지 않음.

**권장**: 로컬 번들로 포함하거나 최소한 `https://` + 버전 고정 + SRI 적용.

---

### F-04 🔴 `eval()` 다수 사용

- `src/views/BudgetMng.vue:1407,1408,1421,1430,1431,1432`
  ```js
  const rowdata1 = eval("this.planConfirm.actual" + this.addLeadingZero(month) + "ApprYn");
  ```
- `src/views/ProjectConsLst.vue:359,360,464`
- `src/views/ProjectSuperLst.vue:501,502`

현재 인자는 내부 계산값이라 직접적 외부 주입은 보이지 않으나, `eval` 사용은 CSP 적용을 불가능하게 만들고(F-07) 데이터 흐름 변경 시 XSS/RCE로 확대될 수 있는 고위험 안티패턴입니다.

**권장**: 브래킷 표기로 대체 — `this.planConfirm['actual' + m + 'ApprYn']`, `list[i]['M' + idx]`.

---

### F-05 🔴 `innerHTML` / `document.write`에 서버 데이터 직접 삽입 (DOM XSS)

- `src/components/EvidAtchPopModeless.vue:142`, `EvidAtchPopGroupware.vue:113`, `CertAtchPop.vue:88`
  ```js
  this.windowRef.document.body.innerHTML += "<img src='" + this.sel[0].downloadUrl + "' style='height: 100%; width: 100%;'>";
  ```
  `downloadUrl`이 따옴표/태그를 포함하도록 조작되면 새 창 컨텍스트에서 스크립트 실행 가능.
- `src/components/ElecTaxPopup.vue:99-101` — `pop.document.write(printDiv.innerHTML)`
- `src/components/accrualSlip/Print/Main.vue:296-297` — 출력용 `document.write`
- `src/mixin/slip.js:174`, `src/components/DhxGrid.vue:532` — 그리드 페이저 `innerHTML`(내부 생성 HTML)

**권장**: `createElement` + `img.src = url` + 텍스트 노드 방식으로 DOM 구성, URL 화이트리스트 검증.

---

### F-06 🔴 OAuth `state` 고정 상수 (CSRF)

`src/components/Login.vue:109-110`
```js
//  FIXME state value random string 으로 변경
state: '2014dlfwls',
```
OAuth `state`가 고정 문자열이라 OAuth 로그인 CSRF 방어가 사실상 없습니다(코드 주석에도 FIXME 명시). 현재 소셜/SSO 로그인이 비활성으로 보이나 코드가 잔존합니다.

**권장**: 매 요청 암호학적 난수 `state` 생성·세션 저장·콜백 검증. 미사용 코드면 완전 제거.

---

### F-07 🟠 CSP / X-Frame-Options 등 보안 헤더 전무

- `vue.config.js` — `devServer`에 보안 헤더 설정 없음.
- `public/index.html` — CSP/X-Frame-Options용 `<meta http-equiv>` 없음(`X-UA-Compatible`만 존재).

결과: 클릭재킹(iframe 삽입) 방어 없음, 인라인/외부 스크립트 제한 없음. F-03·F-04와 결합 시 위험.

**권장**: 운영 웹서버(Nginx)에서 `Content-Security-Policy`, `X-Frame-Options: DENY`, `X-Content-Type-Options: nosniff`, `Referrer-Policy` 설정.

---

### F-08 🟠 `postMessage` 송신 `targetOrigin: '*'`

`src/components/Login.vue:288-291, 304-307, 311-314`
```js
window.parent.postMessage({ message: 'pcLogin', value: ret }, '*');
```
임의 부모 프레임이 로그인 상태 결과를 수신할 수 있습니다.

수신 리스너(`Login.vue:281-319`)는 `event.origin === process.env.VUE_APP_MO_URL` 검증이 있어 기본 방어는 존재하나, 검증 통과 시 메시지의 `loginId/loginPw`로 자동 로그인을 수행하므로 origin 스푸핑 시 자격증명 주입 경로가 됩니다.

**권장**: `targetOrigin`을 명시적 origin으로 지정, 수신 origin 검증을 HTTPS 정확 일치로 강화.

---

### F-09 🟠 토큰/민감정보 `console.log` 출력

`src/components/Login.vue:333`
```js
console.log("this.token : " + this.token);
```
- 추가: `accrualSlip/Approval/Main.vue:528`, `Print/Main.vue:615` — `loginInfo.authorities[0].roleCd` 출력. `Login.vue:274` 사용자명 등.
- `main.js:178`의 `Vue.config.silent=true`(production)는 **Vue 경고만** 끌 뿐 `console.log`는 운영에서도 출력됨.

**권장**: 운영 빌드에서 console 제거(빌드 플러그인 `drop_console`), 민감정보 로깅 금지.

---

### F-10 🟠 검증 없는 서버 URL을 `window.open`/location으로 오픈

- `src/components/EvidAtchPopModeless.vue:138` `window.open(this.sel[0].downloadUrl, ...)`
- `CertAtchPop.vue:84`, `EvidAtchPopGroupware.vue:109`, `JiniAtchPop.vue:64,66`, `JiniAtchBatchPop.vue:62,65`

URL 출처가 백엔드라 위험도는 중간이나, 값 조작 시 임의 사이트 유도(open redirect/피싱) 가능.

**권장**: 오픈 전 URL 스킴/호스트 화이트리스트 검증.

---

### F-11 🟠 하드코딩된 키/식별자 및 인프라 정보 노출

- `src/main.js:26` — ag-Grid Enterprise 라이선스 키(만료, 2022-03-03). 번들 노출 + 라이선스 정책 위반 소지.
- `src/components/Login.vue:102,107`, `public/index.html:8` — Google/Naver OAuth `client_id`(공개 식별자라 단독 위험은 낮음).
- `.env.*` — 내부 IP(`10.12.21.x`, `197.200.x`), DMZ IP(`168.126.74.x`), 내부 도메인 노출.

> AES/암호화 키, API 비밀키, client_secret, 비밀번호는 소스 전체 검색 결과 **발견되지 않음**(양호).

**권장**: 라이선스 키 갱신/서버측 관리, `.env`의 내부 인프라 주석 제거.

---

### F-12 🟢 기타 (낮음)

- `src/main.js:238` — loginInfo 부재 시 `config.data.compCd` 무조건 대입 → GET 요청에서 `config.data` undefined 예외 가능(안정성).
- `src/views/HrSlipReg.vue:67` — 고정 경로 `location.href`(사용자 입력 없음, 정보용).
- 다수 디버그 `console.log`(`main.js:210` 등).

---

## 권장 조치 우선순위

1. **(즉시) HTTPS 전면 전환** — F-01, F-03 동시 해소. HSTS·Secure 쿠키 전제 조건.
2. **(즉시) 토큰 저장 방식 변경** — F-02. HttpOnly+Secure+SameSite 쿠키로 서버 발급, localStorage/JS-쿠키에서 토큰 제거.
3. **(높음) XSS 표면 제거** — F-04(`eval`→브래킷 표기), F-05(`innerHTML`→DOM API).
4. **(높음) OAuth state 난수화 또는 미사용 코드 제거** — F-06.
5. **(중간) 보안 헤더(CSP 등) 적용** — F-07. 위 1~3 완료 후 CSP가 실효성 확보.
6. **(중간) postMessage origin 강화, console 정리, URL 검증** — F-08~F-10.

---

## 참고

- 본 보고서는 **프론트엔드** 정적 분석 결과입니다. 백엔드(Spring Boot) 측 인증/인가, SQL 인젝션, 파일 업로드, 세션 관리 등은 별도 점검이 필요합니다.
- 라인 번호는 점검일 기준이며, 코드 변경 시 달라질 수 있습니다.
