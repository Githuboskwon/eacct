# ije-eacct-front : Vue 2 → Vue 3 마이그레이션 계획

> 작성일: 2026-06-16
> 목표: 프론트엔드 스택 최신화 (Vue 3 / 빌드 툴체인 현대화)
> 참고: 백엔드는 `ije-eacct-back/MIGRATION_PLAN_JAVA21.md`로 Java 21 / Boot 3 전환 완료

---

## 1. 현재 상태 (As-Is)

| 항목 | 현재 버전 | 비고 |
|------|-----------|------|
| Vue | 2.6.12 | EOL (2023-12 지원 종료) |
| @vue/cli-service | 3.12.1 (2019) | EOL, Vue 3 미지원 |
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
`ag-charts-vue`, `vue-class-component`, `vue-property-decorator`, `vue-pdf`,
`vuejs-datepicker`, `vue-upload-component`, `vue-owl-carousel`, `vue-swiper`,
`vue-loading-overlay`, `vue-the-mask`(전역 등록만 됨, 템플릿 사용 0)

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
| `components/PopupGrid.vue` | `<account>`/`<cctr>`(DHTMLX) 사용하나 **어디서도 import 안 되는 완전 죽은 파일** | ⏸ 보류 — import 정리가 아닌 **파일 삭제 후보**(별도 판단) |
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

---

## 9. 다음 액션
1. [x] **(완료 2026-06-16)** 1차 PoC — `ApprMndPop.vue` DHTMLX 사원조회 → `Emp_Ag` 전환 + 검색팝업 죽은 import 정리(4파일) (§8.6)
2. [x] **(완료 2026-06-16)** B그룹 활성 3개 전환 — `ApprRuleSet`/`ApprMndSet`/`ApprLineSet` (§8.7)
3. [x] **(완료 2026-06-16)** 죽은 구버전 `AuthMngUser.vue`/`AuthMngMenu.vue` 삭제 + 활성본 `AuthMngMenu2.vue` 죽은 import 정리 (권한관리 활성본은 기전환됨) (§8.7)
4. [x] **(완료 2026-06-16)** 런타임 확인 — `/apprRuleSet`·`/apprMndSet`·`/apprLineMng` 정상 동작 확인 (※ `ApprMndPop` 사원검색 팝업은 `/apprMndSet` 신규/수정 흐름에서 추가 확인 권장)
5. [ ] **(선결)** §8.5 — 편집형 전표 `GridED` 1화면 ag-grid PoC로 4대 난관(셀잠금/키보드/페이징/인라인 컴포넌트) 실현성·공수 실측
6. [ ] `PopupGrid.vue`(완전 죽은 파일) 삭제 여부 판단 + DHTMLX `_new` 변형 잔존 정리
7. [ ] 0단계: 미사용 의존성 9종 제거 + **C그룹 16개 파일의 dead `DhxGrid` import 제거** + `.env*` SSO 잔존 정리(백엔드 SSO 제거와 연계)
6. [ ] 1단계: vue-cli 5 vs Vite PoC 브랜치로 빌드 환경 결정
7. [ ] element-plus 자동 치환 도구(gogocode 등) 검증으로 element-ui 전환 공수 실측
8. [ ] `$bus` 대체 방식(mitt vs Pinia) 결정
