# ije-eacct-front : Vue 2 → Vue 3 마이그레이션 계획

> 작성일: 2026-06-16
> 목표: 프론트엔드 스택 최신화 (Vue 3 / 빌드 툴체인 현대화)
> 참고: 백엔드는 `ije-eacct-back/MIGRATION_PLAN_JAVA21.md`로 Java 21 / Boot 3 전환 완료

---

## 1. 현재 상태 (As-Is)

| 항목 | 현재 버전 | 비고 |
|------|-----------|------|
| Vue | ~~2.6.12~~ → **2.7.16** | (2026-06-17 브리지 완료 §12) Vue 2 최종, Vue 3 도약판 |
| @vue/cli-service | ~~3.12.1~~ → **5.0.8** | (2026-06-17 교체 완료 §11.6) webpack5 |
| webpack | 4.46.0 | EOL, `--openssl-legacy-provider` 우회 필요 |
| Node | 18.20.8 (Volta 고정) | Node 20+ 빌드 깨짐 |
| Vue Router | 3.3.4 | Vue 3는 v4 필요 |
| Vuex | 3.5.1 | Vue 3는 v4 / Pinia 필요 |
| vue-i18n | 8.22.2 | Vue 3는 v9 필요 |
| 전체 `.vue` 파일 | 277개 | |
| 전체 `.js` 파일 | 39개 | TypeScript 미사용(jsconfig만 존재) |

### ⚠️ 단순 Vue 버전 변경이 불가능한 이유
1. **vue-cli 3 / webpack 4** 는 Vue 3 SFC 컴파일 미지원 → 빌드 툴체인(vue-cli 5 또는 Vite) 교체 필수
2. **element-ui / buefy** 는 Vue 2 전용 → Vue 3에서 동작 불가, 교체 필수
3. **ag-grid-vue v25** 는 Vue 2 바인딩 → ag-grid-vue3 필요
4. Vue 3 Breaking Change(필터 제거, 이벤트 버스 패턴 변경, v-model 시맨틱 변경 등) 코드 수정 동반

→ 결론: "Vue 버전만 3으로 변경" 은 빌드·런타임 모두 실패. 아래 동반 작업이 필수.

---

## 2. Vue 3 비호환 / 교체 대상 라이브러리

`package.json` 의존성을 Vue 3 호환성 기준으로 분류하고, `src` 전수 검색으로 실사용량을 확인했다.

### 🔴 교체 필수 (Vue 3 비호환 UI 라이브러리 — 최대 공수)
| 라이브러리 | 현재 | 사용량 | 조치 / 대체재 |
|-----------|------|--------|---------------|
| `element-ui` | 2.15.14 | **99개 파일 / ~773곳** (`<el-*>`) | → **element-plus** 2.x. 컴포넌트명·import 일괄 치환 |
| `ag-grid-vue` + community/enterprise | 25.1.0 | **155개 파일 / ~177곳** (`<ag-grid-vue>`) | → **ag-grid-vue3** + ag-grid v31+, 라이선스 키 매니저 재설정 |
| **DHTMLX Grid** (`DhxGrid.vue` + 전역 `/js/dhtmlx.js`) | dhtmlxSuite v4 세대(상용) | **활성 ~26개 파일** (목록 화면은 ag-grid 이관 완료) | 셀-내부 컴포넌트 렌더링이 `Vue.extend/$mount`에 의존 → Vue 3에서 **동작 불가, 재작성 필수** (§7.1 참조) |
| `buefy` | 0.7.10 | 54개 파일 / ~126곳 (`<b-*>`) | Vue 3 직접 대체 없음 → element-plus/PrimeVue로 흡수 |

### 🟡 코어 플러그인 업그레이드 (API 대부분 호환)
| 라이브러리 | 현재 → 목표 | 설정 파일 |
|-----------|------------|----------|
| `vue-router` | 3 → 4 | `src/router.js` |
| `vuex` (+`vuex-persistedstate`) | 3 → 4 (또는 Pinia) | `src/store.js` |
| `vue-i18n` | 8 → 9 | `src/i18n.js` |
| `vue-sweetalert2` | 1.6 → 5.x (또는 sweetalert2 직접) | `src/main.js` |
| `vue-cookie` | → 유지/js-cookie | `src/main.js` (인터셉터) |
| `vue-momentjs` / `moment` | → day.js (선택) | `src/main.js`, `src/store.js` |
| `axios` | 0.18.1 → 1.x | `src/main.js` (`$http`) — 보안상 상향 권장 |

### 🟢 미사용 — 제거 가능 (package.json에 있으나 `src` 사용 0건)
> ⚠️ **(2026-06-17 전수 재검증으로 정정) — 최초 10종 후보 중 실제 제거 가능은 5종뿐이었음.**
> 나머지 5종은 (a) 실사용 또는 (b) 그리드 바인딩의 미선언 전이의존이라 **유지 필수.**

**✅ 실제 제거 완료 (5종, 브랜치 `chore/phase0-cleanup`):**
`vuejs-datepicker`, `vue-upload-component`, `vue-owl-carousel`, `vue-swiper`,
`vue-the-mask`(전역 등록만 됨 → `main.js` import+`Vue.component('TheMask')` 2줄도 제거)

**❌ 유지 필수 (제거 후 빌드 깨짐 확인 → 정정):**
- `ag-charts-vue` — `MyMain.vue`(활성 메인)에서 도넛차트 `<ag-charts-vue>` 실제 렌더링
- `vue-pdf` — `PdfViewer.vue` → 증빙첨부 팝업 4종(`EvidAtchPop`/`EvidAtchBatchPop`/`EvidAtchPopGroupware`/`EvidAtchPopModeless`)에서 사용
- `vue-loading-overlay` — `App.vue` + `ApprBundleSubm(/Slip/Temp)` 4파일 실사용
- `vue-property-decorator`·`vue-class-component` — **`ag-grid-vue`/`ag-charts-vue`(v25)가 `dependencies:{}`로 선언했지만 lib 코드에서 import** → 루트 hoist에 암묵 의존. 직접 제거 시 `Module not found` 빌드 실패. ag-grid-vue3 업그레이드(3단계) 전까지 유지

### ⚠️ 별도 검토 필요
| 항목 | 내용 |
|------|------|
| **jQuery / jquery-ui** | `DhxGrid.vue`, `NumberInput.vue`, `main.js`에서 **225곳 이상** DOM 직접 조작. Vue 3 반응성과 충돌 가능 → 점진적 제거 대상(부담 큼) |
| **SSO 잔존** | `.env*`에 `VUE_APP_SSO_URL` 잔존. 백엔드는 이미 SSO 제거 완료 → 프론트 SSO 호출/설정 정리 필요 (백엔드 문서 §2.1과 연계) |

---

## 3. Vue 2 → 3 API 패턴 변경 (코드 수정 대상)

| 패턴 | 규모 | 변경 내용 |
|------|------|----------|
| 앱 부트스트랩 | `src/main.js` 1곳 | `new Vue({...}).$mount()` → `createApp(App).use(...).mount()` |
| `Vue.filter` | 정의 3개 + 사용 ~17곳 | 제거됨 → 메서드/`globalProperties.$filters`/computed로 |
| `$bus` 이벤트 버스(`new Vue()`) | ~16개 파일 | `new Vue()` 불가 → **mitt** / provide-inject / Pinia로 대체 |
| `.native` modifier | ~28개 파일 / 48곳 | 제거 (Vue 3에서 기본 동작) |
| `Vue.prototype.*` | ~14개 ($http, $loading, $alert, $bus, $numeral 등) | `app.config.globalProperties.*` |
| `Vue.use/component/directive` | 37 + 6 + 1 | `app.use/component/directive` |
| `beforeDestroy`/`destroyed` | 26개 파일 | `beforeUnmount`/`unmounted` |
| `v-model` (커스텀 컴포넌트) | ~244곳 | `value`/`input` → `modelValue`/`update:modelValue` (컴포넌트 계약 검토) |
| 커스텀 디렉티브 `v-focus` | 1곳 (`main.js`) | `inserted` 훅 → `mounted` |

> 클래스 기반 컴포넌트(vue-class-component) / `slot-scope` 구문 사용은 0건 → 해당 마이그레이션 불필요.

---

## 4. 빌드 / 툴체인 현대화

| 항목 | 현재 | 조치 |
|------|------|------|
| 빌드 도구 | vue-cli 3 + webpack 4 | **vue-cli 5(webpack 5)** 또는 **Vite**로 교체 (`--openssl-legacy-provider` 우회 제거) |
| `vue.config.js` | 미사용 ts-loader 규칙 포함 | dead code 정리, Vue 3 빌드 설정으로 재작성 |
| ESLint | `babel-eslint`(deprecated) + airbnb v3 | `@babel/eslint-parser` + 최신 eslint-plugin-vue(Vue 3 룰) |
| SCSS | sass 1.49 / sass-loader 10 | 최신 dart-sass / sass-loader로 상향 (CI에 `sass-migrator division` 흔적 있음) |
| GitLab CI | webpack 4 dist 출력 전제 | Vite 채택 시 빌드 산출물 구조 변경 → `.gitlab-ci.yml` 빌드/배포 스텝 수정 |
| 환경변수 | `.env*` (`VUE_APP_*`) | Vite 채택 시 `VITE_*` 프리픽스 규칙으로 변경 필요 / vue-cli 5 유지 시 그대로 |

---

## 5. 권장 전략: 단계적 전환

277개 SFC + 대형 UI 라이브러리 3종(element-ui/ag-grid/buefy) 교체가 핵심 공수.
Big-bang은 위험하므로 **빌드 안정화 → 코어 → UI 라이브러리 → 패턴 정리** 순으로 분산.

```
[선행]  DHTMLX → ag-grid 통합 (現 Vue 2 위에서)      ← Vue 3 최대 블로커 사전 제거 (§8)
[0단계] 미사용 의존성 제거 + dead import + SSO 잔존 정리  (노이즈 제거, 저위험)
[1단계] 빌드 툴체인 교체 (vue-cli 5 or Vite)        (Vue 3 컴파일 환경 확보)
[2단계] Vue 3 + 코어 플러그인 (router4/vuex4/i18n9) (부트스트랩·라우터·스토어)
[3단계] UI 라이브러리 교체 (element-plus/ag-grid-vue3/buefy 흡수) ← 최대 작업
[4단계] API 패턴 정리 (filter/$bus/.native/v-model/lifecycle)
[5단계] jQuery 제거 + 회귀 테스트
```
> **[선행] 단계가 핵심 차별점:** DHTMLX를 Vue 2 상태에서 ag-grid로 먼저 통합하면(§8),
> Vue 3 비호환 그리드 블로커가 사라져 3단계는 `ag-grid-vue → ag-grid-vue3` 단순 업그레이드로 축소된다.

---

## 6. 작업량 / 리스크 요약

| 영역 | 규모 | 난이도 |
|------|------|--------|
| 빌드 툴체인 교체 | vue.config/CI/babel | 중 |
| element-ui → element-plus | 99파일/773곳 | 상 (도구 일부 자동화, 검증 필수) |
| **DHTMLX 그리드 재작성/이관** | **활성 ~26파일** (전표/결재/검색) | **최상 (셀 렌더링 전면 재작성, §7.1)** |
| ag-grid v25 → vue3/v31 | 145파일 | 상 (API/라이선스 변경, §7.2) |
| buefy 흡수 | 54파일/126곳 | 중~상 (직접 대체재 없음) |
| Vue 코어(router/vuex/i18n/부트스트랩) | 설정 4파일 | 중 |
| API 패턴($bus/v-model/filter/lifecycle) | ~수십~수백 곳 | 중~상 |
| jQuery 제거 | 3파일/225곳 | 상 (반응성 충돌 검토) |
| 회귀 테스트 | 전체 277 SFC | 상 |

**예상 기간:** UI 라이브러리 3종 교체와 jQuery 제거가 주 공수 → **수 주~수 개월** 규모.
백엔드 대비 자동화 여지가 적고(템플릿 수기 검증) 화면 단위 회귀 테스트 비중이 큼.

---

## 7. ⚠️ 그리드 마이그레이션 리스크 (이 프로젝트 최대 난관)

> **핵심 경고:** 본 프로젝트는 그리드를 **2종** 사용하며, 둘 다 Vue 3에서 **"버전업만으로는 동작하지 않는다."**
> 특히 DHTMLX 그리드는 **재작성 없이는 화면에서 그리드를 아예 쓸 수 없게 될 수 있다.**
> 그리드는 거의 모든 목록/입력 화면의 핵심이므로, **마이그레이션 성패는 사실상 이 그리드 2종 처리에 달려 있다.**

### 7.1 DHTMLX Grid (`src/components/DhxGrid.vue`) — 🔴 최고 위험 / 동작 불가

- **로드 방식:** `public/index.html`에서 전역 스크립트 `/js/dhtmlx.js`(상용 dhtmlxSuite, `dhxgrid_material` 이미지 경로 → v4 세대)로 로드. `DhxGrid.vue`가 `dhtmlXGridObject` 전역 객체를 래핑.
- **사용 규모 (정정):** **활성 ~26개 파일.** 다수 **목록(조회) 화면은 이미 ag-grid로 이관되어 `<dhx-grid>`가 주석 처리**된 상태(약 35개 화면). 단, 아래 **전표 입력/조회·결재 설정·권한·검색 팝업**은 여전히 활성 사용 중이므로 제거 상태가 아님.

  **(A) `<dhx-grid>` 컴포넌트로 활성 사용 (19파일):**
  - 전표(핵심): `slip/GridED.vue`(입력 그리드×2), `slip/GridRO.vue`(조회 그리드×2), `SlipGr.vue`(전표 그룹), `SlipCrdLstModal.vue`(카드내역 모달)
  - 결재 설정: `views/ApprLineSet.vue`(그리드×3), `views/ApprRuleSet.vue`, `views/ApprMndSet.vue`
  - 권한: `AuthMngUser.vue`, `AuthMngMenu.vue`
  - 검색/입력: `Cctr_new.vue`, `Account_new.vue`, `Emp_new.vue`, `Vendor_new.vue`, `IO_new.vue`, `ErpAccount.vue`, `Prepay.vue`(선급), `BdgReq.vue`(예산요청), `JiniAtchPop.vue`, `JiniAtchBatchPop.vue`

  **(B) `new dhtmlXGridObject(...)` 직접 호출 — DhxGrid 래퍼 미경유 (7파일):**
  - `Cctr.vue`, `Account.vue`, `Emp.vue`, `ErpAccountPop.vue`, `Expend.vue`, `Product.vue`, `Vendor.vue` (검색/선택용 그리드)

  **(C) `DhxGrid` import만 존재 → 16개 전부 잔존(dead) import로 확정 (제거 가능):**
  - 16개 파일을 전수 확인한 결과 **자기 템플릿에서 `<dhx-grid>`를 직접 렌더링하는 파일은 0개.** 모두 `import` + `components` 등록만 남은 dead code → 제거 가능.
  - 대상: `ApprDtl/ApprErpDtl/ApprBundleDtl/ApprSubm/ApprBundleSubm(Slip|Temp).vue`, `MyMain(2|_pub).vue`, `Slip{Detail,Bulk,Gl,Fund,Bond,Collection}DetailModal.vue`
  - 이들이 실제 렌더링하는 것: 대부분 **ag-grid**(Slip*DetailModal 6종, ApprBundleSubm 계열), **ag-charts/dhtmlXChart**(MyMain 계열).
  - ⚠️ **화면 단위 주의 — 간접 DHTMLX 2건:** `ApprDtl.vue`(`<component :is>`로 `SlipGr`/`BdgReq` 렌더)와 `ApprSubm.vue`(`<appr-dtl>` 경유)는 **자기 import를 지워도 자식(A그룹)이 DHTMLX를 쓰므로 화면에는 DHTMLX가 그대로 남음.**

  > 즉 **목록 화면은 ag-grid 이관 완료**, **전표/결재/권한/검색 영역은 DHTMLX 잔존**(A그룹 19 + B그룹 7 = **활성 26개**) → 특히 `GridED`/`GridRO`는 전표 작성의 핵심이라 마이그레이션 이슈가 유효함. C그룹 16개의 DhxGrid import는 정리 대상(dead).

- **별도 의존 — dhtmlXChart (그리드 아님):** `MyMain2.vue`/`MyMain_pub.vue`는 메인 화면 차트를 `dhtmlXChart`(순수 JS, DHTMLX Suite의 차트 모듈)로 그림. 그리드와 무관하지만 같은 상용 DHTMLX Suite 의존이므로 마이그레이션 시 차트 라이브러리(ag-charts 등)로의 교체를 함께 검토.

- **Vue 3에서 깨지는 결정적 지점 — 셀 내부 Vue 컴포넌트 렌더링:**
  ```js
  // DhxGrid.vue (약 228~238행)
  var Component = Vue.extend(c.component)
  this.componentMap[cell_id] = new Component({ propsData, parent: this, mixins }).$mount(...)
  ```
  → **`Vue.extend`, `new Component({propsData, parent}).$mount()` 는 Vue 3에서 전부 제거됨.**
  → 그리드 셀 안에 들어가는 입력/버튼/체크박스 등 모든 커스텀 컴포넌트 렌더링 경로가 끊김 → **렌더링 로직 전면 재작성 필요**(`createApp()`/`h()` 기반).
- **그 외 Vue 2 의존:** `beforeDestroy()`(984행), `value` prop 기반 v-model, jQuery DOM 조작 다수(`$(...).empty()/.append()/.css()` 등).
- **그리드 코어 자체:** `dhtmlXGridObject`는 프레임워크 비종속 바닐라 JS라 스크립트 "로드"는 되지만, **Vue 컴포넌트 ↔ 셀 브릿지가 끊겨 실사용 불가.** 또한 v4 세대 상용 라이브러리라 라이선스/유지보수 자체가 별도 리스크.
- **선택지:**
  1. `DhxGrid.vue`의 셀 렌더링을 Vue 3 방식으로 재작성(난이도 상, dhtmlx 유지)
  2. DHTMLX 사용 ~58화면을 **ag-grid 또는 다른 그리드로 통합 이관**(장기적으로는 그리드 일원화 권장)

### 7.2 ag-grid-vue v25 → ag-grid-vue3 — 🔴 높은 위험 (주력 그리드)

- **사용 규모:** **~145개 파일**의 주력 그리드. `src/components/agGrid/` 아래 자체 셀 렌더러 다수
  (`select-cell-renderer.js`, `checkbox-cell-renderer.js`, `numberinput-cell-renderer.js`,
  `el-datepicker-cell-renderer.js`, `input-cell-renderer.js` 등).
- **교체:** `ag-grid-vue`(Vue2용) → **`ag-grid-vue3`**, ag-grid `25` → **`31+`**.
- **v25 → v31 주요 Breaking Change:**
  - `frameworkComponents` → `components`
  - `columnApi` 가 `gridApi` 로 통합(v31)
  - 셀 렌더러 컴포넌트 모델 변경, Vue 3 반응형 렌더러용 `reactiveCustomComponents` 플래그 필요
  - `LicenseManager` import 경로/엔터프라이즈 라이선스 재설정
- 커스텀 셀 렌더러 일부가 `Vue.extend`/`$mount` 패턴을 혼용 → 함께 재작성 대상.

### 7.3 그리드 리스크 요약

| 그리드 | 사용 규모 | Vue 3 직접 호환 | 핵심 작업 | 미처리 시 영향 |
|--------|----------|-----------------|-----------|----------------|
| **DHTMLX (`DhxGrid.vue`)** | 활성 ~26 파일 (목록은 ag-grid 이관 완료) | ❌ 동작 불가 | `Vue.extend/$mount` 셀 렌더링 전면 재작성 + jQuery 정리 (또는 타 그리드로 이관) | 전표 입력/조회 등 핵심 화면 그리드 사용 불가 |
| **ag-grid-vue v25** | ~145 파일 | ❌ 패키지·API 변경 | ag-grid-vue3 + v31 마이그레이션, 셀 렌더러 재작성 | 목록/입력 화면 대부분 영향 |

> **결론:** 그리드는 "문제없이 넘어가는" 영역이 아니라 **마이그레이션의 최대 난관이자 선결 과제**다.
> 1단계(빌드)·2단계(코어) 진행 전에 **그리드 2종에 대한 PoC로 실현 가능성과 공수를 먼저 검증**할 것을 강력히 권장한다.

---

## 8. ✅ 선행 전략: DHTMLX → ag-grid 통합 (Vue 3 전환 前)

> **권장:** Vue 3 전환에 착수하기 **전에**, 현재 Vue 2 스택 위에서 잔존 DHTMLX를 ag-grid로 먼저 통합한다.
> ag-grid v25는 **이미 Vue 2에서 정상 동작**하므로 지금 바로 진행 가능하며, Vue 3 최대 블로커
> (DhxGrid의 `Vue.extend/$mount` 셀 렌더링)를 사전에 제거해 이후 전환 리스크를 크게 낮춘다.

### 8.1 왜 먼저 하는가
- ag-grid v25가 현 Vue 2에서 동작 → **Vue 3 없이 지금 착수 가능**
- DHTMLX 셀 렌더링(Vue 3 비호환)을 미리 제거 → **이후 Vue 3 전환 단순화**
- 그리드를 **ag-grid 한 종으로 일원화** → 이후 ag-grid-vue3 업그레이드만 관리
- 화면 단위 **점진적 전환** 가능(한 번에 안 바꿔도 됨)

### 8.2 이미 갖춰진 기반 (전환이 쉬운 이유)
- **검색/선택 팝업은 ag-grid 대체본(`_Ag.vue`) 10종이 이미 존재·운영 사용 중**
  (`Emp_Ag` 37파일, `Cctr_Ag` 21파일, `Vendor_Ag` 12파일, `Account_Ag`/`Product_Ag`/`Expend_Ag`/`ErpAccount_Ag`,
   ag 전용 `ErpCctr_Ag`/`ErpAccountSub_Ag`/`ExpPrice_Ag`)
- **편집형 셀 에디터/렌더러가 `src/components/agGrid/`에 이미 구비**
  (`select`/`checkbox`/`numberinput`/`input`/`maskedit`/`el-datepicker`/`button` cell-renderer, `AgGridCheckbox/ScanAttach/SearchBtn`)
- `GridED.vue`는 이미 한 화면에서 ag-grid(`Account_Ag`/`Cctr_Ag`/`Product_Ag`)와 DHTMLX **혼용 검증됨**
- `EstimateReg.vue` 등 **ag-grid 편집형 그리드 구현 사례** 존재

### 8.3 화면별 난이도 / 전환 순서

| 단계 | 대상 | 난이도 | 작업 성격 |
|------|------|--------|-----------|
| **1차 (마무리성)** | 검색/선택 팝업의 DHTMLX 잔존 경로를 기존 `_Ag` 대체본으로 통일 — `GridRO`의 `Account`/`Cctr`/`Product`, `DelegateMng`의 `Cctr`, B그룹 직접호출 7종(`Cctr/Account/Emp/ErpAccountPop/Expend/Product/Vendor`) | **하** | 신규개발 아님, **대체본 연결·경로 정리** |
| **1.5차 (정리)** | C그룹 16파일 dead `DhxGrid` import 제거 (§7.1 C) | 하 | 코드 청소 |
| **2차 (목록형)** | 결재/권한 설정 — `ApprLineSet`(그리드3), `ApprRuleSet`, `ApprMndSet`, `AuthMngUser`, `AuthMngMenu` | 중 | 대부분 목록형, ag-grid 표준 패턴 |
| **3차 (편집형·핵심)** | 전표 편집 그리드 — `slip/GridED`(입력), `slip/GridRO`(조회), `SlipGr`, `BdgReq`, `SlipCrdLstModal`, `Prepay`, `JiniAtchPop`/`JiniAtchBatchPop`, `ErpAccount` | **상** | 인라인 편집/셀잠금/페이징/키보드 재구현 |

### 8.4 3차(편집형 전표)의 핵심 난관 — 사전 설계 필요
1. **셀 동적 잠금**: DhxGrid `lockCell()`/`isCellLocked()` → ag-grid `colDef.editable` 함수 + `cellStyle`로 재구현
2. **키보드 네비게이션**: Tab이 "다음 **편집가능** 셀만" 이동하도록 ag-grid `tabToNextCell` 커스터마이징, Enter/Esc/Ctrl+C 동작 매핑
3. **가상 페이징**: DHTMLX virtual paging → ag-grid 페이징 API(`paginationPageSize` 등) 또는 클라이언트 모델로 재작성
4. **셀 내부 Vue 컴포넌트**: DhxGrid `component:` 옵션 → ag-grid `cellRenderer`/`cellEditor`(기존 `agGrid/` 렌더러 재사용)

> **공수 추정:** 1차·1.5차·2차는 비교적 짧게(경로 정리 위주), **3차 편집형 전표가 본 작업** — 핵심 구현 2~3주 + 테스트 1~2주.

### 8.5 권장 진행
1. **3차 대상 1화면 PoC**(예: `GridED` 단일 전표) — 셀잠금/키보드/페이징/인라인 컴포넌트 4대 난관 실현성·공수 실측
2. PoC 통과 시 1차 → 2차 → 3차 순으로 화면 단위 점진 전환 + 화면별 회귀 테스트
3. 전환 완료 후 `DhxGrid.vue`·전역 `/js/dhtmlx.js`·관련 SCSS 제거 → **DHTMLX Suite 의존 종료**(차트 `dhtmlXChart`는 별도, §7.1)

### 8.6 진행 현황 (2026-06-16, 1차 착수)

**검색 팝업 전환 실측 결과 — 1차는 사실상 거의 완료 상태였음:**
DHTMLX 검색 원본(`Emp/Cctr/Account/...`)의 **활성(비주석) import**를 전수 조사한 결과,
대부분 화면은 이미 `_Ag` 버전으로 전환됐고 잔존분은 **(a) 죽은 import** 또는 **(b) 전표 그리드 내부 활성 사용**뿐이었다.

| 파일 | 상태 | 조치 |
|------|------|------|
| `components/ApprMndPop.vue` | 비-전표 화면 중 **유일한 활성 DHTMLX 사원조회** | ✅ **전환 완료** — `Emp.vue`→`Emp_Ag.vue`, 구형 `<b-modal @receive>`+`<emp>` → 표준 `$modal.open({component, events:{close}})` |
| `components/ExchangeRateMng.vue` | 죽은 `import Emp` | ✅ 제거(import·components 등록·`showEmpModal`) |
| `views/EbillSlipRcvLst.vue` | 죽은 `import Emp` | ✅ 제거 |
| `views/GridComponent.vue`(데모 라우트) | 죽은 `import Emp` | ✅ 제거 |
| `views/DelegateMng.vue` | 죽은 `import Cctr` + 죽은 `popCctr`(2개)·`receiveCctr`·`initCctr`·`showCctrModal` (실사용 검색은 이미 `Emp_Ag`) | ✅ 제거 |
| `components/PopupGrid.vue` | `<account>`/`<cctr>`(DHTMLX) 사용하나 **어디서도 import 안 되는 완전 죽은 파일** | ✅ **삭제 완료**(2026-06-16) |
| `slip/GridED.vue`, `slip/GridRO.vue`, `SlipCrdLstModal.vue` | **활성 DHTMLX 검색(`Emp/Account/Cctr/Product/ErpAccount(Pop)`)** | ⏭ **3차(편집형 전표)** 작업 시 그리드와 함께 전환 |

> 검증: 변경 5개 파일 **ESLint 통과(에러 0)**. ⚠️ 런타임 클릭 테스트(`ApprMndPop` 위임자/수임자 검색→선택)는 dev 기동 후 별도 확인 필요.
> 시사점: **1차(검색 팝업)는 잔여 정리 수준**이며, 실질 공수는 예상대로 **3차 편집형 전표**에 집중됨. `PopupGrid` 등 죽은 파일/잔존 DHTMLX `_new` 변형은 별도 정리.

### 8.7 B그룹(결재/권한 설정) 전환 현황 (2026-06-16)

**실제 활성 화면 3개 전환 완료. 권한관리 2개는 죽은 구버전을 잘못 건드린 것으로 판명 → 삭제하고 활성본(이미 ag-grid) 확인·정리.**

| # | 화면 | 유형 | 상태 |
|---|------|------|------|
| 1 | `views/ApprRuleSet.vue` (전결 규정, `/apprRuleSet`) | 읽기전용 + 더블클릭 팝업 + 금액 포맷 | ✅ 완료 (활성) |
| 2 | `views/ApprMndSet.vue` (결재 위임현황, `/apprMndSet`) | 읽기전용 + 날짜 포맷 + 더블클릭 | ✅ 완료 (활성) |
| 3 | `components/ApprLineSet.vue` (결재선 지정, `/apprLineMng` 및 전표 상신 플로우 다수) | 그리드 3개 + 행 추가/삭제/순서이동 | ✅ 완료 (활성) — `gridEmp`/`gridLine`/`gridRef` 전환, DHTMLX 인스턴스 API(`getSelectedRowId`/`getRowIndex`/`selectRowById`) → ag-grid `getSelectedNodes()`/`getDisplayedRowAtIndex().setSelected()`. **좌측 부서 트리(`dhtmlXTreeView`)는 그리드가 아니므로 유지** |
| — | ~~`components/AuthMngUser.vue` (권한별 사용자)~~ | — | ⚠️ **죽은 구버전 파일(importer 0건)** → **삭제.** 권한관리(`/authMng`)가 실제 쓰는 것은 `AuthMngUser2.vue`이며 **이미 ag-grid**(DHTMLX 잔재 없음) |
| — | ~~`components/AuthMngMenu.vue` (권한별 메뉴)~~ | — | ⚠️ **죽은 구버전 파일(importer 0건)** → **삭제.** 실제 사용본 `AuthMngMenu2.vue`는 **이미 ag-grid**(체크박스 `ap_checkbox-cell-renderer`). 활성본의 죽은 `DhxGrid`/`GridCheckbox` import만 정리 |

> **교훈:** `Account.vue` ↔ `Account_Ag.vue`처럼 "구버전(죽음)/신버전(활성)"이 공존하는 파일은, 전환 전 **import(사용처) 존재 여부를 먼저 확인**할 것. AuthMngUser/AuthMngMenu는 `<dhx-grid>` 태그가 살아있어 활성처럼 보였으나 어디서도 import되지 않는 죽은 파일이었음.

**확립된 변환 패턴 (B그룹 공통):**
- `<dhx-grid ref v-model="data" :config="config" @constructGridSuccessful>` → `<ag-grid-vue :columnDefs :rowData="data" :gridOptions :defaultColDef [:frameworkComponents] @grid-ready @rowDoubleClicked>`
- `config.columns`(`type: ro/ron/ed/ch/cntr`) → `columnDefs`(`hide:true` / `editable:true` / `cellRenderer:'checkboxRenderer'` / `valueGetter`(No.) / `valueFormatter`(날짜·금액))
- `constructGridSuccessful`의 `setColumnHidden`·`setNumberFormat`·`attachEvent('onRowDblClicked')` → `hide:true`·`valueFormatter`·`@rowDoubleClicked="rowDoubleClick(params)"`(셀 인덱스 `grid.cells(rId,n)` → `params.data.field`)
- 체크박스: `type:'ch'` → `checkbox-cell-renderer` + `cellRendererParams:{trueValue,falseValue}` (boolean 유지 시 `trueValue:true`)
- 엑셀: `downloadExcel(this.$refs.grid)` → `this.gridOptions.api.exportDataAsExcel({fileName})`
- DHTMLX 페이징 잔재(`config.queryPage`) 제거

> 검증: 변경 파일 **ESLint 통과**(단, `AuthMngMenu2.vue`의 `no-inner-declarations` 2건은 기존 트리생성 코드의 사전 존재 경고로 본 작업과 무관).
> ⚠️ 런타임 확인 필요: `ApprRuleSet`/`ApprMndSet`(조회·더블클릭·엑셀), `ApprLineSet`(`/apprLineMng`: 부서트리→임직원목록→추가/삭제/순서이동/적용).
> 커밋: `refactor(front): DHTMLX → ag-grid ...`(B그룹) + `refactor(front): 죽은 AuthMngUser/AuthMngMenu 삭제 + AuthMngMenu2 정리`.

### 8.8 편집형 전표 `GridED` ag-grid PoC 결과 (2026-06-16)

> **목적:** 운영 `GridED.vue`(약 3,658줄, 슬립유형별 config 4종 `def/E2/E6/E1`)를 통째로 건드리지 않고, **4대 난관의 실현성·공수만 실측.**
> **산출물:** 독립 실증 컴포넌트 `src/components/poc/GridEDPoc.vue` (라우트 `/gridEdPoc`, 운영 영향 0). 슬립 기본유형(config_def) 핵심 패턴을 ag-grid로 재현.

**4대 난관 실현성 — 전부 ✅ 검증됨:**

| 난관 | DHTMLX(현행) | ag-grid 대응 | 결과 |
|------|--------------|--------------|------|
| ① 셀 내부 Vue 컴포넌트 | `config.columns[].component:{template,methods}` (검색버튼·GridSelect·예산표시) | `cellRenderer`(frameworkComponent) / `cellEditor`. 검색버튼은 `Vue.extend` 렌더러에서 `$modal.open(...)` 호출 → `params.node.setDataValue`로 주입 | ✅ (프로젝트 기존 `agGrid/*-cell-renderer.js`가 동일 패턴 사용 — 이미 검증된 방식) |
| ② 셀/행 잠금 | `grid.lockRow()` + `setColumnClassName('bg-lightpink')` (afterRefreshData에서 대변/세액 행 잠금) | `colDef.editable=(params)=>!isLocked(row)` + `cellStyle=(params)=>음영` | ✅ |
| ③ 키보드 Tab 네비 | DHTMLX 편집셀 Tab 이동 | `gridOptions.tabToNextCell`로 **편집 가능 셀만** 점프 구현 | ✅ |
| ④ 인라인 편집 + 포맷 + 연동계산 | `type:ed/edn`, `onEditCell`/`onCellChanged`의 합계·검색 로직 | `editable` + `valueParser`/`valueFormatter`(천단위) + `@cell-value-changed`(차/대변 합계 자동) + `agSelectCellEditor`(셀렉트) | ✅ |

> **참고:** GridED의 슬립 그리드는 **페이징을 쓰지 않음**(`height` 고정, `enablePaging` 없음) → §8.4의 "가상 페이징" 난관은 GridED에는 **해당 없음**.

**결론 — 기술적 전환 가능(난관 모두 해소). 단, 공수는 "패턴"이 아니라 "분량"에서 발생:**
- 4대 난관은 PoC로 해소됐고, 검색 컴포넌트(`Cctr_Ag`/`Account_Ag`/`Product_Ag`)·셀 렌더러(`agGrid/*`)도 이미 존재.
- 실제 공수 = **config 4종(def/E2/E6/E1) × 각 15~20컬럼 + 슬립유형별 `onEditCell`/`onCellChanged`/`afterRefreshData` 비즈니스 로직 포팅**. 화면 의존성(예산조회·계정애드온·유가계산·세액계산 등)이 많아 **화면 단위 신중 전환 + 회귀 테스트** 필요.
- 추정: `GridED`(+`GridRO`) 본전환 **2~3주 + 슬립유형별 E2E 1~2주** (기존 §8.4 추정 유지).

**권장 전환 순서(전표 영역):** 가장 단순한 config부터 — 읽기전용 `GridRO` → `config_def`/`config_E6` → `config_E2`(보조계정·예산연동) → `config_E1`(법인카드, 2그리드). 각 단계 후 해당 슬립유형 E2E.

### 8.9 `GridRO`(읽기전용 전표 조회) ag-grid 전환 (2026-06-16) — 첫 A그룹 실전

- `components/slip/GridRO.vue`를 `<dhx-grid>` → `<ag-grid-vue>`로 전환(읽기전용 6 config: def/E5/E6/E2/E1/E1_2).
- **합계 푸터**(`attachHeader #stat_total`) → ag-grid `pinnedBottomRowData`(def/E5: 차·대변 합, E6: 사용금액 합).
- **셀 내부 컴포넌트** → frameworkComponent + `gridOptions.context`:
  - 카드정보 버튼(`crdInfoBtn`) → `openCrdInfo()`로 `CardInfoDetailPop` 오픈
  - 스캔증빙(`scanCtCell`) → 파일수 조회 + `openEvidence()`로 증빙 팝업 `window.open`(IE 분기/상태별 readonly 로직 부모 메서드로 이관)
  - 유종/유가·날짜·금액 → `valueGetter`/`valueFormatter`(천단위·소수점·YYYY-MM-DD)
  - 숨김컬럼 `setColumnHidden` → `hide:true`, `attribute10`(증빙유형) 매핑은 `mapAttribute10()`로 이관
- vatYn(GridSelect)은 원본에서 `hide:true`였으므로 컬럼 생략.
- 검증: ESLint 통과. ⚠️ **머지 전 슬립유형별(E1/E2/E5/E6/일반) 런타임 검증 필수**(합계 푸터·카드정보·스캔증빙 팝업·금액/날짜 포맷). 별도 브랜치 `poc/grided-aggrid`.

### 8.10 `GridED`(편집형 전표) 본전환 착수 — 증분 1 (2026-06-16)

> `GridED.vue`(3,658줄, config 5종 def/E2/E6/E1/E1_2)는 자동검색·보조계정 캐스케이드·예산잔액·유가·세액 계산·합계 푸터(DHTMLX 헤더 훅)·`$refs.grid.instance`/`$refs.grid.data` 전반 의존 + mixin(`add_row`)으로 GridRO보다 훨씬 깊게 결합. **단일 세션 일괄 변환은 핵심 전표작성 화면에 치명적 — 증분 + 슬립유형별 런타임 검증 필수.**

- **신규 `components/slip/GridED_Ag.vue` 생성**(`Account.vue`↔`Account_Ag.vue` 관례). 운영 `GridED.vue`는 미변경, `slip-basic.js` 미연결 → **운영 무영향**.
- **증분 1 = `config_def`(일반전표/기타) 완성:**
  - columnDefs(부서/계정/제품 검색버튼 cellRenderer + 계정명/제품/차변금액/적요 인라인 편집)
  - 자동검색(`@cell-editing-stopped` → `findAccount`/`findProduct`, 단건 자동·다건 팝업), `@cell-value-changed`로 차변=acctAmt 동기
  - 셀 잠금(C_ITEM/D_TAX → `editable(fn)`+`cellStyle`), 합계 푸터(`pinnedBottomRowData` 차/대변), 행추가·삭제(mixin `add_row`/splice), 초기화(`reset_rows`), 엑셀(`exportDataAsExcel`), Tab 순환(PoC 패턴)
- **증분 2 = `config_E6`(출장/교통비) 완성:** 교통비유형/유종 **select 셀(코드→명, `select-cell-renderer` 재사용)**, 사용일자/출발·도착지/출장목적 편집, 유류대(tpsTypeCd='10') 시 **거리·유가표로 사용금액 자동계산**(`computeOil`, 월별 `/api/oilPrice/list` 캐시), 편집 잠금규칙(유류대=거리편집/그외=금액편집), 사용일자 변경 시 지급예정일·대변행 동기(`syncUseDt`), 금액 변경 시 공급가/세액 재계산(`recalcE6Totals`), 합계 푸터(사용금액 합).
- **증분 3 = `config_E2`/`E5`(개인비용외) 완성:** 부서/계정/보조계정/개발프로젝트 검색버튼 + 자동검색, **계정 선택 시 보조계정 자동조회 캐스케이드**(1건 자동·다건 팝업 `openAccountSub`), **잔여예산 표시**(`/api/budget/remain` 캐시 `loadBudget`, `budgetKey`로 valueGetter), 관리항목(attribute1~15) 팝업, 부서변경 시 계정/보조계정/항목 초기화, 금액 변경 시 총액/원화금액 재계산(`recalcE2Totals`, JPY 보정). 합계 푸터 없음(원본과 동일).
- **증분 4 = `config_E1`+`config_E1_2`(법인카드, 2그리드) 완성:** 메인(법인카드지급)+현금지급 2개 ag-grid, 세금코드/교통비유형/유종 **select**, 부서/항목(계정)/제품 검색버튼(ErpCctr/ErpAccount/Product), **카드정보 팝업**(`CardInfoDetailPop`)·**스캔증빙 팝업**(`EvidAtchPop` + 파일수 조회), 유류대 자동계산(`computeOilE1`, eaSlipDt 월 유가), 금액/거리 변경 시 총액 재계산(`recalcE1Totals`=메인+현금 합), **법인카드내역 불러오기**(`SlipCrdLstModal` → `add_row` 일괄), 현금지급 행추가/삭제(`add_row_sub`/splice). 합계 푸터 없음.
- **✅ 전 슬립유형(def/E6/E2/E5/E1) 증분 완료.** 남은 단계 = **`SlipBase.vue`의 `GridED` → `GridED_Ag` 교체(또는 `slip-basic.js` gridType 스위치)** 후 **슬립유형별 런타임 검증**(작성/조회·검색·금액계산·증빙·카드내역·저장).
- 검증: ESLint 통과. ⚠️ 슬립 컨텍스트(value.slipDetails) 필요해 단독 라우트 검증 불가 → **`slip-basic.js` 연결 후 슬립유형별 런타임 검증**(증분 완료 시점).

---

## 9. 다음 액션
1. [x] **(완료 2026-06-16)** 1차 PoC — `ApprMndPop.vue` DHTMLX 사원조회 → `Emp_Ag` 전환 + 검색팝업 죽은 import 정리(4파일) (§8.6)
2. [x] **(완료 2026-06-16)** B그룹 활성 3개 전환 — `ApprRuleSet`/`ApprMndSet`/`ApprLineSet` (§8.7)
3. [x] **(완료 2026-06-16)** 죽은 구버전 `AuthMngUser.vue`/`AuthMngMenu.vue` 삭제 + 활성본 `AuthMngMenu2.vue` 죽은 import 정리 (권한관리 활성본은 기전환됨) (§8.7)
4. [x] **(완료 2026-06-16)** 런타임 확인 — `/apprRuleSet`·`/apprMndSet`·`/apprLineMng` 정상 동작 확인 (※ `ApprMndPop` 사원검색 팝업은 `/apprMndSet` 신규/수정 흐름에서 추가 확인 권장)
5. [x] **(완료 2026-06-16)** 저위험 정리 — 죽은 `DhxGrid` import 27개 파일 일괄 제거 + 죽은 파일 `PopupGrid.vue` 삭제 (그리드 동작 변화 없음, DhxGrid no-undef/구문오류 0)
6. [x] **(완료 2026-06-16)** 편집형 전표 `GridED` ag-grid PoC — 4대 난관 전부 검증(독립 컴포넌트 `poc/GridEDPoc.vue`, `/gridEdPoc`). 기술적 전환 가능 확인, 공수는 config 4종 분량에서 발생 (§8.8)
7. [중단] **(보류 2026-06-17, §10 참조)** A그룹(`GridED`/`GridRO`/`SlipCrdLstModal`/`BdgReq`) — `GridED_Ag.vue` 증분1~4 완성·`SlipBase` 연결(PR #4)까지 했으나, **해당 경로(`/exctpExpense`)가 운영 네비게이션에서 도달 불가한 레거시로 확인됨** → 전환 중단. PR #4는 죽은 경로라 영향 없어 유지, **PR #7(SlipCrdLstModal) 보류/CLOSE**.
8. [~] **(진행 2026-06-16)** 잔여 그리드 정리/전환 (브랜치 `refactor/dhtmlx-remaining-grids`):
   - 죽은 파일 삭제: `Account.vue`/`Expend.vue`/`Cctr.vue`/`Vendor.vue` (참조 0 확정). **`Account_new.vue`는 유지** — `SearchAccount.vue`가 상대경로 `./Account_new.vue`로 사용(삭제했다가 빌드 깨져 복원).
   - `SlipGr.vue`(전표 그룹, 읽기전용) ag-grid 전환 완료
   - ⚠️ 감사 주의: import 경로가 **(a) `'`/`"` 따옴표 (b) `.vue` 유무 (c) 별칭 `@/components/` vs 상대경로 `./`** 로 혼재 → **세 가지 모두 매칭**해야 안전(예: `JiniAtchBatchPop`/`Cctr`는 쌍따옴표 alias라 live였고, `Account_new`는 `./` 상대경로라 누락됨). **삭제 후 `npm run build`로 컴파일 검증 필수.**
   - **`_Ag` 호출처 교체 → 원본 삭제:** `HrExpendPop`의 `Cctr`→`Cctr_Ag` 교체 후 **`Cctr.vue` 삭제**, `Vendor.vue` 삭제(`CardInfo`가 이미 `Vendor_Ag` 사용). (드롭인: props `param`, close `deptCd/deptNm` 확인)
   - 잔여(live, 전환 필요): `SlipCrdLstModal`/`BdgReq`/`Prepay`/`JiniAtchPop`/`JiniAtchBatchPop`/`Cctr_new`/`Emp_new`/`IO_new`/`Vendor_new`/`ErpAccountPop`.
   - ⚠️ `ErpAccount`/`Emp`/`Product` 원본은 **운영 `GridED.vue`·`SlipCrdLstModal`**(아직 DHTMLX, 전환 예정)이 사용 중 → 그 파일 전환 시 함께 `_Ag`로 교체. (`GridED_Ag`의 `ErpAccount`→`ErpAccount_Ag`는 emit 필드 검증 후 교체 TODO)
8. [x] **(완료 2026-06-17)** 0단계 잔여: 미사용 의존성 제거(후보 10종 전수 재검증 → 실제 제거 **5종**, 5종은 실사용/미선언 전이의존이라 유지) + `.env*` 4파일 `VUE_APP_SSO_URL` 제거(src 참조 0 확인). 빌드 통과. 브랜치 `chore/phase0-cleanup` (§2 🟢)
9. [x] **(완료 2026-06-17)** 1단계: 빌드 환경 결정·교체 — **vue-cli 5(webpack5)** 적용, build/serve openssl 플래그 없이 통과 (§11.6, 브랜치 `migration/vue-cli5`)
10. [x] **(완료 2026-06-17)** 2단계 도약판: **Vue 2.6 → 2.7.16** 브리지(`vue-template-compiler` 제거, 라이브러리 무변경), build/serve 통과 (§12, 브랜치 `migration/vue2.7`)
11. [ ] 2단계 본체: `@vue/compat` + router4/vuex4/i18n9 + `createApp` (UI 라이브러리 교체와 묶어 진행, §12.4)
12. [x] **(완료 2026-06-17)** element-plus 자동 치환 도구(gogocode) 검증 — 인벤토리·자동화율·전략 실측. gogocode 통짜 적용 비권장(재포맷 노이즈) → 변환종류별 스코프 코드모드 권장 (§13, 브랜치 `poc/element-plus`)
13. [x] **(완료 2026-06-17, §14.1.1 정정)** buefy PoC — 템플릿은 얕으나(`b-select` 108→native·완료 / `b-modal` 6) **`$modal`이 buefy이며 469곳/111파일 사용**(accrualSlip 포함)으로 판명 → "단독 즉시 제거" 결론 철회 (§14)
14. [x] **(완료 2026-06-17)** `b-select`→native `<select>` 51파일 변환(buefy 비의존 cleanup, 빌드통과) — 브랜치 `refactor/remove-buefy`
15. [ ] **(buefy 제거 선결)** `$modal.open` 드롭인 커스텀 플러그인 설계·구현(469곳 무수정) → 이후 b-modal 6 재배선 + `Vue.use(Buefy)` 교체 + buefy npm 제거
16. [x] **(완료 2026-06-17)** `$bus`(new Vue() 이벤트버스) → **mitt** 전환 — 전역 prototype(accrualSlip 통신) + 로컬버스 18선언/17파일. `$on/$emit`→`on/emit`, 빌드통과 (§15, 브랜치 `refactor/bus-to-mitt`)
17. [ ] element-ui/ag-grid 정리 후 Vue 3 빅뱅 브랜치 착수(@vue/compat + 코어 플러그인 + UI 교체)

---

## 10. 🔁 전략 전환: 레거시 GridED 경로 중단 → 활성 화면 우선 (2026-06-17)

### 10.1 발견 — `GridED_Ag` / `SlipCrdLstModal` / `BdgReq`는 죽은 경로
런타임/코드 추적으로 다음 확인:

| 경로 | 실제 동작 | GridED_Ag 사용 |
|------|----------|----------------|
| 전표관리 `/slipMng` 더블클릭 | `ApprovalModal`(신규 accrualSlip 결재 모달) | ❌ |
| 전표내역조회 `/slipLst` 더블클릭 | `/accrualSlip/{slipNo}` 라우팅(신규) | ❌ |
| `/slipLst` "개인비용외/전자세금계산서/수기세금계산서" 버튼 | **블록 전체 주석 처리** (SlipLst.vue 56~60) | ❌ (진입점 제거) |
| 활성 메인 `MyMain.vue` | `/exctpExpense` **미참조** (구버전 MyMain2/MyMain_pub에만 잔존) | ❌ |

→ `/exctpExpense`(`SlipBase`→`GridED_Ag`) 및 그 안의 **`SlipCrdLstModal`(법인카드내역 버튼, config_E1)·`BdgReq`** 는 메뉴·버튼·대시보드 어디로도 도달 불가. **직접 URL 입력으로만 접근**. 운영 전표 작성/조회/결재는 전부 신규 **`/accrualSlip`** 서브시스템(이미 ag-grid)으로 이전됨. (신규 법인카드 팝업도 `accrualSlip/Modals/CorpCardHstModal.vue`로 별도 존재 = 이미 ag-grid)

### 10.2 결정 (사용자 승인: 선택지 1)
- **레거시 GridED 경로 전환 작업 중단(보류):** `GridED_Ag`/`GridRO`/`SlipCrdLstModal`/`BdgReq`.
- 이미 머지된 **PR #4**(`GridED_Ag` 연결)는 죽은 경로라 운영 영향 없음 → 롤백 불필요, 유지. **PR #7(`SlipCrdLstModal`) 보류/CLOSE.**
- 공수를 **실제 메뉴로 도달 가능한 활성 DHTMLX 그리드 화면**으로 이동.

### 10.3 ⚠️ 정정 (2026-06-17) — 활성 화면 DHTMLX 그리드 전환은 **이미 완료** 상태

최초 `<dhx-grid>` grep이 **주석 처리된 라인까지 카운트**하여 "활성 14개 DHTMLX 그리드"로 오판했음. **주석 제외 활성 기준**으로 재검증한 정확한 실태:

| 구분 | 실태 |
|------|------|
| ERP전표/결재/법인카드 **목록 14개**(PaySlipList, PurSlipList, ApprPendLst, ApprCompLst, ApprReqLst, CardUseLst, CardInfoMng, GLSlipList, FundSlipLst, CollectionSlipLst, EtcSalesSlipLst, ForeignSlipLst, ExportSlipLst, ESlipSubmit) | **이미 `<ag-grid-vue>` 전환 완료**. `<dhx-grid>`는 전부 주석 잔재 |
| 주석 아닌 **활성 `<dhx-grid>`** | **8개 파일뿐**: `Account_new`/`Emp_new`/`Cctr_new`/`Vendor_new`/`IO_new`/`ErpAccount`(검색팝업) + `BdgReq` + `GridED`(죽은 원본) |

**활성 `<dhx-grid>` 8개 = 전부 레거시 죽은 경로:**
- `GridED.vue`: SlipBase가 `GridED_Ag`로 대체 → 죽은 원본
- `BdgReq`/검색팝업(`_new`): `ApprDtl`→`slip-table` 레거시 체인에서만 사용. **활성 결재화면(`ApprPendLst` 등)은 신규 `ApprovalModal`(`accrualSlip/Approval/Main.vue`) + `Emp_Ag`/`Cctr_Ag`(ag-grid) 사용** → 레거시 검색팝업은 죽은 경로. (신규 accrualSlip는 자체 ag-grid 모달 `accrualSlip/GridModal/AccountModal` 등 사용)

→ **결론: 활성 화면의 DHTMLX→ag-grid 그리드 전환은 사실상 완료. 추가 변환할 활성 그리드 없음.**

### 10.3.1 실제 남은 DHTMLX 작업 (그리드 아님)
1. **`<dhx-calendar>` 날짜 위젯 → `el-date-picker`** — 활성 화면 잔존(예: `CardInfoMng`, `CertificateMng`, `ProjectLst`, `ExpPriceReg/List`, `EstimateReg/List` 등). 소규모·기계적 치환(PaySlipList가 이미 이 패턴으로 전환 완료).
2. **죽은 코드 정리** — 14개 기전환 화면의 주석 `<dhx-grid>` + 죽은 `DhxGrid`/`DhxCalendar` import 제거. 죽은 레거시 파일(`GridED.vue`, `ApprDtl` 체인, `_new` 검색팝업, `BdgReq`, `slip-table` 서브시스템) 참조 0 확정 후 일괄 삭제 대상.

### 10.4 다음 액션
1. [ ] PR #7(`SlipCrdLstModal`) CLOSE — *gh 실행 대기(분류기 일시 장애)*
2. [ ] (선택) `<dhx-calendar>`→`el-date-picker` 치환 — 활성 화면 우선
3. [ ] (선택) 죽은 레거시 슬립/검색팝업/`DhxGrid` 잔재 정리 후 삭제(참조 0 확정 필수, `npm run build` 검증)
4. [x] **(완료 2026-06-17)** 그리드 전환이 사실상 끝나 **Vue 3 선행 1단계(빌드 툴체인)로 이동** → vue-cli 5 교체 완료 (§11.6)

---

## 11. 빌드 툴체인 결정 (1단계) — 현황 분석 + 권고 (2026-06-17)

### 11.1 현재 빌드 설정 실측
| 파일 | 핵심 내용 | 이관 시 영향 |
|------|----------|-------------|
| `package.json` scripts | `set NODE_OPTIONS=--openssl-legacy-provider && vue-cli-service build` | webpack4+Node18 OpenSSL 우회 → 툴 교체 시 제거 가능 |
| `vue.config.js` | `runtimeCompiler: true`, `chainWebpack`(alias `@`), 커스텀 `postcss-loader`+`sass-loader` 규칙, `HtmlWebpackPlugin`, `ts-loader` 규칙(**TS 파일 0 = 죽은 규칙**) | ⚠️ **`configureWebpack` 키 중복**(객체가 함수 덮어씀 → 해시 파일명 설정 사문화). 재작성 필요 |
| `babel.config.js` | `@babel/preset-env`(esmodules, core-js 3) | 유지 가능(vue-cli5) / Vite는 esbuild라 babel 최소화 |
| `.gitlab-ci.yml` | `sass-migrator division` → `build:staging` → `dist` → nginx | 산출물 `dist` 동일하면 변경 최소 |
| `.env` | `VUE_APP_*` 컨벤션 | vue-cli5 유지 / **Vite는 `VITE_*`로 일괄 rename 필요** |
| 코드 | jQuery/jquery-ui 전역 225곳, DHTMLX 전역 스크립트, `Vue.extend({template})` 다수 | `runtimeCompiler` 필요 → Vite는 `vue.esm-bundler` alias 명시 필요 |

### 11.2 vue-cli 5 (webpack 5) vs Vite 비교
| 기준 | vue-cli 5 | Vite |
|------|-----------|------|
| 마이그레이션 위험 | **낮음** — `vue.config.js`(chainWebpack/configureWebpack) 구조·`VUE_APP_*`·webpack 멘탈모델 그대로 | 높음 — config 패러다임 변경(no chainWebpack), env rename, CJS config 변환 |
| Vue 2 유지 가능 | ✅ Vue 2.6/2.7 그대로 빌드 → **빌드 툴만 먼저 교체 후 Vue 3은 별도 단계** | △ `@vitejs/plugin-vue2` 필요(과도기) |
| `runtimeCompiler` | 옵션 그대로 지원 | alias 수동 설정 필요 |
| jQuery/DHTMLX 전역 | 기존 방식 유지 | ProvidePlugin 대체(`@rollup/plugin-inject`) 등 추가 작업 |
| 빌드/HMR 속도 | 보통(webpack5) | 빠름 |
| 장기성 | 유지보수 모드(신규 개발 적음) | 생태계 표준·활발 |
| `--openssl-legacy-provider` 제거 | ✅ | ✅ |

### 11.3 권고: **vue-cli 5 우선 (Vue 2 유지 상태로 교체) → 이후 Vue 3 → 말미에 Vite 재평가**
근거: 본 앱은 레거시 의존(jQuery 225곳, DHTMLX 전역, element-ui/buefy, `runtimeCompiler`, CJS config)이 많아 **Vite + Vue 3 동시 전환은 동시 리스크가 과대**. vue-cli 5로 **빌드 툴만 먼저(여전히 Vue 2.6)** 교체하면: ① `--openssl-legacy-provider` 제거 ② webpack5/Node20+ 확보 ③ `vue.config.js` 정리(죽은 ts-loader·중복 키) ④ Vue 3 점프 전 빌드 안정화 — 가 저위험으로 달성됨. Vite는 element-plus/ag-grid-vue3까지 끝난 뒤 마지막에 재평가.

### 11.4 vue-cli 5 PoC 단계 (선정 시)
1. `@vue/cli-service` 3 → 5, `@vue/cli-plugin-*` 5, eslint parser `babel-eslint`→`@babel/eslint-parser`
2. `vue.config.js` 재작성(중복 `configureWebpack` 제거, 죽은 `ts-loader` 제거, alias/sass 규칙 정리)
3. `package.json` scripts에서 `--openssl-legacy-provider` 제거
4. `npm run build` 통과 + `/` 런타임 스모크(로그인·전표목록·ag-grid 1화면) 확인
5. CI(`.gitlab-ci.yml`) 빌드스텝 점검(`dist` 동일 → 최소 변경)

### 11.5 vue-cli 5 교체 사양 (확정 2026-06-17 · 셸 복구 후 실행 대기)
> 사용자 결정: **vue-cli 5 우선**. 셸 명령 분류기 일시 장애로 `npm install`/`build` 실행 대기 중.
> 교체용 config 초안 = `vue.config.cli5.js` (검증 후 `vue.config.js`로 교체).

**(a) `package.json` devDependencies 변경**
```
@vue/cli-service        ^3.12.1 → ^5.0.8
@vue/cli-plugin-babel   ^3.12.1 → ^5.0.8
@vue/cli-plugin-eslint  ^3.12.1 → ^5.0.8
@vue/cli-plugin-unit-mocha ^3.12.1 → ^5.0.8
sass-loader             ^10.2.1 → ^12.6.0   (webpack5 호환)
webpack                 ^4.46.0 → 제거       (cli5가 webpack5 제공)
webpack-cli             ^3.3.9  → 제거       (충돌 방지)
html-webpack-plugin     ^4.4.0  → 제거       (cli5 내장)
+ @babel/eslint-parser  추가 ^7.23.0         (babel-eslint 대체)
```
**(b) eslint parser 교체**: `eslintConfig.parserOptions.parser`: `"babel-eslint"` → `"@babel/eslint-parser"`
**(c) scripts에서 `set NODE_OPTIONS=--openssl-legacy-provider && ` 5곳 제거**
**(d) `vue.config.js` ← `vue.config.cli5.js` 내용으로 교체**

**⚠️ 알려진 리스크:** `@vue/eslint-config-airbnb@^3.0.5`가 eslint 7/8(cli5 동반)과 비호환 가능 → lint 단계에서 충돌 시 `^6`/`^7`로 상향 또는 lint 일시 완화 필요. babel-polyfill(6)·@babel/polyfill(7) 중복도 정리 후보.

**실행 순서(셸 복구 시):**
```
git switch -c migration/vue-cli5
# package.json 위 변경 적용
npm install --legacy-peer-deps
copy vue.config.cli5.js vue.config.js   # 또는 내용 교체
npm run build        # openssl 플래그 없이 통과 확인
# 런타임 스모크: / 로그인 → 전표목록(ag-grid 1화면) 정상 확인
```

### 11.6 ✅ vue-cli 5 교체 실행 결과 (2026-06-17 완료, 브랜치 `migration/vue-cli5`)

**`npm run build` / `npm run serve` 모두 `--openssl-legacy-provider` 없이 webpack5에서 통과.**
(build: `DONE Build complete`, modern/legacy 듀얼 번들·해시 파일명·`dist/dhtmlx` 정적자산 복사 정상 / serve: `App running at http://localhost:19050/`)

**§11.5 확정 사양 대비 실제 적용/추가 변경 4건:**

| # | 사양/이슈 | 조치 |
|---|-----------|------|
| 1 | eslint 미설치로 cli-plugin-eslint가 `eslint.version` 읽기 실패 + airbnb@3.0.5가 eslint7/8 비호환(§11.5 ⚠️ 예견) | `@vue/eslint-config-airbnb` **3.0.5 → ^6.0.0**(eslint ^7.32‖^8.2·cli-service ^5 지원), **eslint ^7.32.0 / eslint-plugin-import ^2.25.3 / eslint-plugin-vue ^8 / eslint-plugin-vuejs-accessibility ^1.1.0** 명시 추가 |
| 2 | webpack5 css-loader가 SCSS 루트절대 `url(/img/*.png)`(public 참조)을 모듈 해석하려다 실패(`DhxGrid`/`MyMain` 등) | `vue.config.js`에 `css.loaderOptions.css.url.filter = (u)=>!u.startsWith('/')` 추가 |
| 3 | airbnb v6 스타일 규칙(max-len/indent/quotes)이 기존 277파일 코드를 빌드에서 에러 처리 | `vue.config.js` **`lintOnSave: false`** (빌드 차단 해제, `npm run lint` 수동 실행은 유지). 전체 재포맷은 별도 단계로 보류 |
| 4 | webpack5 node core 폴리필 제거 → `DhxGrid.vue`의 `import { setInterval } from 'timers'` 모듈 해석 실패 | 불필요 import 제거(`setInterval`은 브라우저 전역) |

- `vue.config.cli5.js`(초안)는 적용 완료되어 삭제. 최종본 = `vue.config.js`.
- **CI(`.gitlab-ci.yml`) 변경 불필요:** openssl 플래그는 `package.json` scripts에만 존재(제거 완료), `dist` 산출물 구조·`npm install --legacy-peer-deps`·`build:staging`·sass-migrator 스텝 모두 그대로 유효.
- ⚠️ **남은 검증:** 실제 브라우저 런타임 스모크(로그인 → 전표목록 ag-grid 1화면·DHTMLX 잔존 화면)는 dev 기동 후 별도 확인 권장. **lint 재베이스라인**(airbnb v6 스타일로 전체 정리 or 규칙 완화)은 후속 과제.

---

## 12. ✅ Vue 2.7 브리지 (2단계 도약판, 2026-06-17 완료 · 브랜치 `migration/vue2.7`)

> **결정(사용자 승인):** "Vue 3 코어만" 단독 전환은 불가 — Vue 3로 올리는 순간 **Vue 2 전용 UI 3종이 전부 빌드 실패**(`element-ui` 99파일 / `buefy` 52파일 / `ag-grid-vue` v25 166파일). 따라서 Vue 3는 UI 교체와 함께 빅뱅이어야 함(§5·§7 경고). 그 전에 **안전·즉시 배포 가능한 도약판으로 Vue 2.6 → 2.7** 을 먼저 적용.

### 12.1 왜 2.7인가
- Vue 2.6 → **2.7**(마지막 Vue 2): 근사 드롭인. **현 라이브러리(element-ui/buefy/ag-grid-vue v25/router3/vuex3/i18n8) 전부 그대로 동작** → 즉시 빌드·배포 가능.
- Composition API / `<script setup>` 백포트 → Vue 3 스타일 코드를 **2.7 위에서 미리** 작성 가능(이후 3 점프 시 diff 축소).
- 2.7은 `vue/compiler-sfc` 내장 → 빌드 도구가 Vue 3 방식 컴파일러 경로를 쓸 수 있어 이후 전환 정합.

### 12.2 적용 변경 (최소)
- `vue` `^2.6.12` → **`^2.7.16`** (실제 2.7.16)
- **`vue-template-compiler` devDependency 제거** — 2.7은 컴파일러 내장(`vue/compiler-sfc`). cli5가 설치한 **vue-loader 17.4.2**가 이 경로를 사용하므로 별도 컴파일러 불필요.
- 그 외 무변경: `vuex@3.5.1`/`vue-router@3.3.4`/`vue-i18n@8.22.2`/`@vue/test-utils@1.0.3` 모두 2.7과 호환되어 그대로 유지(브리지 단계 최소 변경 원칙).

### 12.3 검증
- ✅ `npm run build` 통과(첫 시도). Vue 관련 에러 0. 경고 2건은 **번들 크기 성능 힌트**(vendor 번들 대형)일 뿐 2.7 무관·비차단.
- ✅ `npm run serve:local` 정상 컴파일·부팅(`App running at http://localhost:19050/`).
- ⚠️ 브라우저 런타임 스모크(로그인·전표·그리드)는 dev 기동 후 별도 확인 권장.

### 12.4 다음 (실제 2단계 = Vue 3)
2.7 위에서 안정화 후, Vue 3 점프는 **UI 라이브러리 교체와 묶어** 진행:
1. `@vue/compat` 마이그레이션 빌드로 Vue 3 + router4/vuex4/i18n9 + `createApp` 부트스트랩 도입
2. **동시에** element-ui→element-plus / ag-grid-vue→ag-grid-vue3 / buefy 흡수(3단계, 최대 공수)
3. `$bus`(new Vue()) 23파일 → mitt, `Vue.filter`/`.native`/lifecycle 등 API 패턴 정리(4단계)
> 그리드 DHTMLX→ag-grid 통합은 사실상 완료(§10) → ag-grid는 `ag-grid-vue3` 업그레이드만 남음.

---

## 13. element-ui → element-plus 치환 PoC (2026-06-17 · 브랜치 `poc/element-plus`)

> **전제:** element-plus는 **Vue 3 전용** → 현재 Vue 2.7 스택에선 실행/빌드 검증 불가. 본 PoC는 "동작 확인"이 아니라 **치환 공수·자동화율 실측 + 전략 수립**이 목적. 실제 치환은 Vue 3 점프와 동시에(3단계).

### 13.1 사용 인벤토리 (src 전수, 99파일)
**컴포넌트 태그 (등장 횟수):** `el-button` 298 · `el-input` 170 · **`el-date-picker` 103** · `el-form-item` 40 · `el-col` 39 · `el-checkbox` 29 · `el-radio` 27 · `el-radio-group` 15 · `el-option` 13 · `el-select` 12 · `el-row` 7 · `el-form` 6 · `el-link` 5 · `el-divider` 3 · `el-radio-button` 2 · `el-input-number` 1

**JS API:** `this.$message` 196 · `this.$confirm` 43 · `this.$alert` 40 (전부 `main.js`의 `Vue.prototype.$*` 전역; **컴포넌트 외 직접 `from 'element-ui'` import는 0건** → 치환면이 main.js+템플릿+전역호출로 한정되어 유리)

**등록 방식:** `main.js`에서 **개별 컴포넌트 `Vue.use()` 27종** + `el_locale.use(ko)` + `$ELEMENT={size:'small'}` + `$loading/$msgbox/$alert/$confirm/$prompt/$notify/$message` 프로토타입 7종.

### 13.2 gogocode 코드모드 실측 (`gogocode-plugin-element`, 대표 3파일)
| 항목 | 결과 |
|------|------|
| ✅ **아이콘 변환** | `icon="el-icon-search"`(문자열) → `:icon="ElIconSearch"`(컴포넌트) + `import {Search as ElIconSearch}` 및 components 등록 **자동 추가**. 가장 손 많이 가는 부분을 처리 |
| ⚠️ **date-picker 포맷 미변환** | `format="yyyyMMdd"` 그대로 둠. element-plus는 **dayjs 토큰**(`yyyy`→`YYYY`, `dd`→`DD`) 필요 → **별도 처리 필수** |
| ⚠️ **아이콘 import 경로 구버전** | gogocode가 `@element-plus/icons` 출력. element-plus 2.x는 **`@element-plus/icons-vue`** → 일괄 치환 필요 |
| ❌ **파일 전체 prettier 재포맷** | 들여쓰기/속성 줄바꿈/`{{x}}`→`{{ x }}`/hex 소문자화/주석 reflow를 **모든 파일에 강제** → 99파일 적용 시 **git blame 파괴 + 의미/포맷 변경 분리 불가 → 리뷰 불가능**. 통짜 적용 비권장 |

### 13.3 수작업 핫스팟 (코드모드가 못 잡는 breaking change)
1. **date-picker 포맷 토큰 ~90곳**: `yyyy`→`YYYY`, `dd`→`DD` (`value-format="yyyyMMdd"` ×22, `format="yyyy-MM"` ×19, `format="yyyyMMdd"` ×14 …). format/value-format 속성에 한정한 **스코프 정규식**으로 기계적 처리 가능하나 코드모드 밖.
2. **size 시각 회귀**: `size="large"`가 **el-button 107곳**. element-ui v2 Button은 large 미지원→전역 small로 무시되어 작게 렌더 중. **element-plus는 large 유효 → 버튼이 커짐**(시각 회귀). + `size="medium"` 29(→`default`)·`mini` 2(→`small`) 매핑 필요.
3. **`type="text"` 버튼**: el-button `type="text"` → element-plus `text` prop(deprecated 경로). (`type="text"` 포함 파일 151개, el-button 외 혼재라 컨텍스트 확인 필요)
4. **main.js 재작성**(Vue 3 동시): 개별 `Vue.use` 27종 → `app.use(ElementPlus,{locale})` 또는 선별 `app.use`; `$message/$alert/$confirm` → `app.config.globalProperties` 또는 `import {ElMessage,ElMessageBox}`; `$ELEMENT` size 기본값 → element-plus는 `<el-config-provider>` 또는 app 옵션.
5. radio/checkbox: 선택값 `label` → `value`(element-plus 2.6 deprecation) 검토.

### 13.4 공수 추정 / 권장 전략
- **자동화 가능(~60%)**: 아이콘 변환(gogocode 또는 자체 스코프 코드모드) + date 토큰 정규식 + size/button 스코프 치환.
- **수작업(~40%)**: main.js, edge 컴포넌트, 슬롯/스코프드슬롯 API, **그리고 전 화면 Vue 3 런타임 회귀 테스트**(현재 불가, Vue 3 점프 후).
- **권장:** gogocode **통짜 적용 금지**(재포맷 노이즈). 대신 **변환 종류별 스코프 코드모드/정규식**으로 나눠 리뷰 가능한 단위로:
  1. date-picker 토큰(yyyy→YYYY, dd→DD) — 독립 PR 가능(단 element-plus 전엔 런타임 무의미하므로 Vue3 브랜치에서)
  2. 아이콘(`el-icon-*`→컴포넌트, import 경로 `-vue`)
  3. main.js 등록/전역 재작성
  4. size/type 시각속성 정리
- **element-ui는 buefy(52파일)와 함께 Vue 3 빅뱅에 포함** → 이 PoC는 그 브랜치 작업의 사전 설계도. 별도 단독 배포 불가.

> **PoC 산출물:** 본 문서 §13(인벤토리+자동화율+전략). 코드 변경 없음(gogocode 트라이얼은 `/tmp` 폐기). 다음: buefy 흡수 PoC 또는 `$bus`(23파일)→mitt 결정 후 Vue 3 빅뱅 브랜치 착수.

---

## 14. buefy 흡수 PoC (2026-06-17 · 브랜치 `poc/buefy`)

> **핵심 결론(중요):** buefy는 §2에서 "54파일/126곳·직접 대체 없음(최대 난관)"으로 분류했으나, **전수 재조사 결과 사용면이 매우 얕고 — 컴포넌트 2종뿐·프로그래매틱 API 0 — 둘 다 Vue 버전 비의존 치환이 가능해 빅뱅 없이 현재 Vue 2.7에서 제거 가능**하다. element-ui(Vue3 필수)와 달리 **buefy는 단독 선제거 가능한 블로커.**

### 14.1 사용 인벤토리 (src 전수)
- **컴포넌트 2종뿐:** `<b-select>` **108곳** + `<b-modal>` **14곳(활성 9 / 주석 5)**. 그 외 b-* 태그 0.
- 등록: `main.js`의 `Vue.use(Buefy, {defaultModalCanCancel})` 한 곳 + `buefy/dist/buefy.css`(이미 주석).
- `<b-modal>` 활성: **멀티라인 주석 보정 후 6곳/4파일** — `SlipCrdLstModal`(3, **죽은 경로**)·`ApprDtlQryPop`(1)·`ApprMndSet`(1)·`ApprRuleSet`(1). (`CardInfo`의 3개는 주석블록 내부 = 비활성)

> ### 14.1.1 ⚠️ 중대 정정 (2026-06-17) — buefy는 "얕지 않음", `$modal`이 buefy다
> 최초 §14는 `this.$buefy.*`만 grep해 "프로그래매틱 API 0"으로 **오판**했다. 실제로 **buefy 0.7.x는 `this.$modal`/`$dialog`/`$toast` 등을 prototype에 직접 등록**(레거시 형태, `$buefy.modal`이 아님)한다.
> - **`this.$modal.open({component, props, events, parent})` = buefy 모달 서비스이며 src 전체에서 469곳 / 111파일 사용** — **신규 `accrualSlip` 서브시스템 전체 포함.**
> - 즉 `Vue.use(Buefy)` 제거 시 **469곳 모달 호출이 전부 깨짐.** buefy는 **앱 최심부 의존성** → "Vue 2.7에서 단독 즉시 제거" 결론은 **철회.**
> - 사용 중인 buefy 프로그래매틱 API는 `$modal`뿐(`$dialog/$toast/$snackbar/$notification` 0). `$loading`은 element-ui(`Loading.service`)라 buefy 아님.
> - **올바른 제거 전략:** `$modal.open({component, props, events, parent, width, fullScreen})` API를 그대로 재현하는 **커스텀 모달 플러그인을 작성**(컴포넌트 동적 마운트 + `events` 리스너 + `$parent.close()`로 닫기) → `Vue.use(Buefy)`를 이 플러그인으로 교체하면 **469개 호출부 무수정**으로 buefy 제거 가능. b-select(완료)·b-modal(6)·bulma CSS는 별도. **이 플러그인이 buefy 제거의 핵심 선결물**이며 Vue 3 호환(`h()`/`createApp` 마운트)으로 설계하면 빅뱅도 단순화.

### 14.2 대체 매핑
| buefy | 대체 | 난이도 | 비고 |
|-------|------|--------|------|
| `<b-select>` 108 | **native `<select>`** | 하(기계적) | 내부가 이미 native `<option v-for>`. `<b-select>`→`<select>` + `@change.native`→`@change`(1곳). ✅ **51파일 일괄 변환 → `npm run build` 통과**(브랜치 `refactor/remove-buefy`) |
| `<b-modal>` 활성 6 | **`$modal.open(...)`** | 중 | `:active.sync`+`@receive` → 트리거(`showX=true`)를 `$modal.open` 호출로 재배선. **단 `$modal` 자체가 buefy라 커스텀 플러그인 선행 필요**(§14.1.1) |
| **`$modal.open` 469곳** | **커스텀 모달 플러그인(드롭인)** | **상(핵심)** | buefy `$modal` API 동등 재현 → `Vue.use` 교체로 호출부 무수정 |
| `Vue.use(Buefy)` | 커스텀 플러그인으로 교체 | — | bulma CSS는 유지(레이아웃 의존), buefy npm 의존만 제거 |

### 14.3 공수 / 권장 (정정 후)
- **b-select(완료)** 는 buefy 의존이 아니어도 동작하는 순수 cleanup → 별도 머지 가능.
- **buefy 패키지 제거의 핵심 = `$modal` 드롭인 플러그인**(469곳 무수정 목표). 이게 되면 b-modal 6곳 재배선 + `Vue.use(Buefy)` 교체 + bulma 유지로 마무리.
- **⚠️ 단독 즉시 제거 불가** — `$modal`이 신규 accrualSlip 포함 앱 전반의 핵심이라, 플러그인 작성·전 화면 모달 런타임 회귀가 필요. Vue 3 호환 마운트로 설계하면 빅뱅과 시너지.

> **PoC 산출물:** §14(인벤토리+§14.1.1 정정+매핑). b-select 변환은 `refactor/remove-buefy`에 실재(빌드통과). 다음: (a) b-select cleanup만 단독 머지 (b) `$modal` 드롭인 플러그인 설계·구현 (c) `$bus`(23)→mitt.

---

## 15. `$bus`(new Vue() 이벤트버스) → mitt 전환 (2026-06-17 · 브랜치 `refactor/bus-to-mitt`)

> Vue 3는 `new Vue()`로 인스턴스를 못 만들어 이벤트버스 패턴이 깨진다(§3). 프레임워크 비의존 **mitt**(^3.0.1)로 교체. mitt는 Vue 버전 무관이라 **현 Vue 2.7에서 빌드 검증 완료**.

### 15.1 전환 범위 (2종)
- **전역 `Vue.prototype.$bus`** (`main.js`): `new Vue()` → `mitt()`. 사용처 = **accrualSlip 서브시스템** 내부 통신(`script.js`/`HD.vue` emit, `views/accrualSlip/index.vue`/`AssistantVAT.vue` on) — `this.$bus.$on/$emit` → `.on/.emit`.
- **로컬 `new Vue()` 버스 18선언/17파일**: `bus`/`eventBus`/`$bus` (BdgReq·GridED·AuthMng·PgmMng·ExpendMoneyMng·HrExpendStatus·oilPcePop·Project{Cons,ExcBud,PlanBud,Super}Lst·QuickSetting·SlipLst·SlipMng·TaxMng·AcctSubMng·CertificateMng·MailSendMng). 각 파일에 `import mitt`, `new Vue()`→`mitt()`, `.$on/.$emit`→`.on/.emit`.

### 15.2 API 매핑 / 안전성
- Vue 버스 `$on/$emit/$off` → mitt `on/emit/off`. `$once`는 **사용 0건**(mitt에 once 없음 → 무영향).
- ⚠️ mitt `emit(type, payload)`는 **단일 payload**만 — 전 호출부가 단일/무인자 emit임을 확인(다중인자 0) → 호환.
- 컴포넌트 자체 `this.$emit`/`this.$on`은 **미변경**(버스 변수 `bus/eventBus/$bus`에 한정한 코드모드).
- 코드모드는 일회성 Node 스크립트로 수행 후 제거(커밋 미포함).

### 15.3 검증 / 잔여
- ✅ `npm run build` 통과. 라이브 `new Vue()` 버스 잔존 0(주석/데드 제외).
- ⚠️ **런타임 확인 필요**: accrualSlip 발생전표(그리드 컬럼 갱신·행추가·세액라인 — `$bus` 이벤트), PgmMng(트리 재구성), 기타 로컬버스 화면.
- 참고: `EstimateList`/`ExpPriceReg`/`EstimateReg`/`ExpPriceList`/`ExchangeRateMng`는 `const bus` 선언이 **주석인데 `bus.$on`만 살아있는 기존 데드/잠재버그**(버스 미정의) → 본 작업 범위 외, 미변경.

---

## 16. Vue 3 빅뱅 착수 — @vue/compat foundation (2026-06-17 · 브랜치 `migration/vue3-compat`)

> **전략(사용자 승인):** Vue 3는 element-ui/buefy/ag-grid와 동시 전환(빅뱅)이 불가피(§5·§7) → **@vue/compat 마이그레이션 빌드(MODE 2)** 로 기존 Vue2 코드를 경고와 함께 동작시키며 점진 전환. **격리 브랜치, master(2.7)는 무영향.**

### 16.1 이번 증분 = foundation (코어 부트스트랩 전환 + 에러 지형 파악)
- **의존성:** `vue` 2.7 → **3.5.38**, **`@vue/compat`** 3.5.38·**`@vue/compiler-sfc`** 추가, `vue-router` 3 → **4.6**, `vuex` 3 → **4.1**, `vue-i18n` 8 → **9.14**.
- **`vue.config.js`:** `vue`/`vue$` → `@vue/compat` alias + vue-loader `compilerOptions.compatConfig = { MODE: 2 }`.
- **`src/i18n.js`:** `new VueI18n()` → `createI18n({ legacy: true, globalInjection: true })` (v8 `$t` 호환).
- **`src/store.js`:** `new Vuex.Store()` → `createStore()`, `Vue.use(Vuex)`·store 내 `Vue.use(VueMomentJS)` 제거.
- **`src/router.js`:** `new Router({mode:'history'})` → `createRouter({history: createWebHistory(base)})`, `Vue.use(Router)`·중복내비 가드 제거(v4는 reject 안 함).
- **`src/main.js`:** `new Vue({router,store,i18n,render,created}).$mount()` → `createApp(App).use(store).use(router).use(i18n).mount()`. HTTP 인터셉터(구 root `created`)는 `store`/`router`/`axios`/`VueCookie`/`swal` 직접 참조로 이관. **전역 `Vue.use`/`Vue.prototype`/`Vue.component`/`Vue.filter`/`Vue.directive`는 compat MODE 2가 전역 동작시키므로 그대로 유지.**

### 16.2 첫 빌드 결과 — ✅ 라이브러리 캐스케이드 없음, 템플릿 컴파일 에러 24건만
- **핵심:** 우려했던 element-ui(99)/buefy/ag-grid-vue v25 **전역 API·플러그인 에러가 0건.** compat MODE 2가 `Vue.use`/프로토타입/필터/디렉티브/`$on·$emit`(버스) 등을 모두 흡수.
- 남은 빌드 차단 = **Vue 3 템플릿 컴파일러 strict 에러 24건 / 14파일**(compat로 안 풀리는 진짜 수정):

| 유형 | 건수 | 내용 / 대응 |
|------|------|-------------|
| `v-model cannot be used on a prop` | **19** | 받은 prop을 자식에 `v-model` 양방향 바인딩(Vue3 금지). 컴포넌트별 로컬 data 복사 또는 computed(get/set+emit)로 수정 — **판단 필요** |
| `Element is missing end tag` / `Invalid end tag` | 3 | 닫는 태그 누락/오류(Vue2 관대, Vue3 엄격) — 템플릿 수정 |
| `v-model` 표현식 | 1 | `v-model="form.a - b"` → `:value`로 (TaxInvoiceAmtModifyPop) |
| v-if/else 동일 key | 1 | 고유 key 부여 (MonthlyPicker) |

**대상 14파일:** `HrExcelUploadPop`·`MonthlyPicker`·`Prepay`·`TaxInvoiceAmtModifyPop`·`accrualSlip/Approval/Top`·`accrualSlip/Print/Top`·`costBudget/{AcctTypeSumPop,PerformanceCheck,PerformanceCheckDetail,YearPlanPop}`·`slip/SlipTable`·`ConfirmPop`·`SlipGlDetailModal`·`views/CardInfoMng`.

### 16.3 다음 증분
1. **[다음] 템플릿 컴파일 에러 24건 수정** → 빌드 GREEN 달성(최우선).
2. 빌드 통과 후 **dev 런타임 기동** → compat **deprecation 경고** 수집(element-ui/buefy/ag-grid의 Vue3 비호환 런타임 지점 파악).
3. 경고 기반으로 **element-ui→element-plus / ag-grid-vue3 / buefy($modal) 순차 교체** + `compatConfig` MODE를 파일별 3으로 좁히기.
> ⚠️ 현 브랜치는 **빌드 미통과 WIP**. master(Vue 2.7)는 정상.

### 16.4 ✅ 템플릿 컴파일 에러 수정 완료 — 빌드 GREEN (2026-06-17)
- 24건(+연쇄 1건 `CardInfo.vue`) 전부 수정 → **`npm run build` 통과**(Vue3+compat).
- **v-model on prop 19건:**
  - 표시용 disabled/readonly 11건 → `v-model` → `:value`(단방향): Prepay(totAmt)·Approval/Print Top(refUserId)·costBudget 4팝업의 cctrCd/cctrNm.
  - 편집형 6건 → **로컬 data 복사(`<prop>M`)** + 참조(템플릿/`this.x`/watch키) 일괄 rename: HrExcelUploadPop(periodYM)·AcctTypeSumPop/PerformanceCheck/PerformanceCheckDetail(periodYm)·PerformanceCheckDetail(acctCd)·YearPlanPop(periodYear).
  - SlipTable 2건(`<component v-model="value">`, value=prop) → `:value` + `@input="$emit('input',$event)"` passthrough.
- **태그/표현식/key 5건:** ConfirmPop·SlipGlDetailModal·CardInfo의 누락 `</tr>`/중복 `<tr>` 보정, CardInfoMng 잉여 `</div>` 제거, TaxInvoiceAmtModifyPop `v-model` 표현식 → `:value`, MonthlyPicker v-if/else 고유 key.
- 경고는 번들 크기 2건뿐(compat 컴파일 에러 0).
- **다음:** dev 런타임 기동 → compat **deprecation 경고**(런타임) 수집 → element-plus/ag-grid-vue3/buefy 순차 교체.

---

## 17. element-ui → element-plus 교체 (2026-06-17 · `migration/vue3-compat`)

> 런타임 검증서 element-ui가 Vue3 compat에서 mount 시 `$el` 접근 실패 → 달력·메뉴·레이아웃 연쇄 차단 확인(§16 후속). element-plus로 교체.

### 17.1 1차 증분: 등록 전환 (main.js)
- `element-plus` 2.14 + `@element-plus/icons-vue` 설치. **element-ui는 deps 유지**(`element-variables.scss`가 참조 → 빌드 깨짐 방지, 추후 정리).
- `main.js`: element-ui 개별 `Vue.use(컴포넌트)` 27종 제거 → **`app.use(ElementPlus, {locale: ko, size:'small', zIndex:3000})`** + 아이콘 전역 등록. `$message/$msgbox/$alert/$confirm/$prompt/$notify/$loading` → `ElMessage/ElMessageBox/ElNotification/ElLoading`로 재매핑.
- `<el-*>` 태그는 element-plus에 동명 컴포넌트 존재 → 대부분 그대로 resolve. `npm run build` 통과.

### 17.2 알려진 후속(이번 증분 범위 밖)
1. **날짜 포맷 토큰**: el-date-picker `value-format="yyyyMMdd"` → element-plus는 dayjs 토큰(`yyyy→YYYY`, `dd→DD`) 필요. ~90곳 스코프 치환(§13.3).
2. **아이콘**: `icon="el-icon-search"` 문자열 → element-plus는 컴포넌트(`:icon="Search"`). 아이콘 전역등록은 했으나 문자열 구문은 미동작 → 코드모드 필요(§13.2).
3. **size 시각차**: medium/mini 제거, large 유효(§13.3).
4. **CSS 충돌 가능**: element-ui 테마(element-variables.scss) + element-plus CSS 동시 로드 → 정리 필요.
5. **ag-grid 별개**: 그리드는 ag-grid-vue3 전환 전까지 여전히 미렌더.

### 17.3 런타임 검증 포인트
- 메뉴/레이아웃/달력 **렌더 복구** 여부(= `$el` 크래시 해소).
- `$message/$confirm/$alert` 동작(196/43/40곳).
- ⚠️ 그리드는 아직 안 보이는 게 정상(ag-grid epic 대기).

### 17.4 날짜 토큰 수정 + 팝업(buefy $modal) 블로커 발견 (2026-06-17)
- **el-date-picker 날짜 토큰**: 런타임서 "날짜 선택해도 input에 안 들어감" = element-plus(dayjs)가 `yyyy/dd` 토큰 미인식. **`format`/`value-format` 속성의 `yyyy→YYYY`, `dd→DD` 일괄 변환(92곳/26파일, 템플릿 한정)** → 빌드 GREEN. (MM/HH/mm/ss 동일 유지, JS moment 문자열 미변경)
- **⚠️ 팝업 안 뜸 = buefy `$modal` 블로커 확인**: accrualSlip `trxOpenModal` 등은 `this.$modal.open({component})`(=buefy) 사용. (1) 전제조건 `postingDt`(날짜) 미설정으로 early-return(날짜 수정으로 해소) + (2) **buefy `$modal` 자체가 Vue3 compat에서 모달 마운트 실패 추정** → §14의 `$modal` 469곳 블로커가 런타임서 현실화. **커스텀 $modal 플러그인이 다음 핵심 epic.**

### 17.5 Vue3 런타임 남은 블로커 (현재)
| 블로커 | 상태 | 다음 작업 |
|--------|------|----------|
| element-plus 컴포넌트/날짜 | ✅ 동작(레이아웃·달력 복구) | 아이콘 `el-icon-*`·`slot=` 구문·CSS 정리(소) |
| **buefy `$modal` 469곳** | ❌ 팝업 안 열림 | **커스텀 $modal 드롭인 플러그인(§14.1.1)** |
| **ag-grid-vue v25** | ❌ 그리드 미렌더 | **ag-grid-vue3 전환(166파일)** |
