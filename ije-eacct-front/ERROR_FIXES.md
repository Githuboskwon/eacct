# 화면 점검 에러 정리 (Vue3 본전환 회귀)

Vue3 본전환 작업 이후 화면을 클릭하며 발견한 버그와 수정 내용을 누적 기록한다.
실제 스택: Vue 2.7.16 + vue-router 3.6.5 + element-ui 2.15.14.

---

## #1. accrualSlip — 신규버튼 클릭 후 "예" 눌러도 화면이 초기화되지 않음

- **상태**: ✅ 수정완료
- **재현 경로**: `http://localhost:19050/accrualSlip`
- **증상**: 전표 작성 중 **신규** 버튼 클릭 → "신규버튼 클릭 시 모든 데이터가 초기화 되어집니다. 초기화 하시겠습니까?" confirm 표시 → **예** 클릭 시 화면이 초기화되어야 하나 그대로 남아있음.

### 근본 원인

두 가지가 맞물려 발생.

1. `src/App.vue:8` — `<router-view :key="$route.fullPath"/>` : `fullPath`가 바뀔 때만 컴포넌트가 재마운트되고, 그때 `HD.vue`의 `mounted()` → `initialize('all')`이 다시 돌아 초기화된다.
2. `src/router.js:18-27` — 마이그레이션 때 "redundant navigation" dev 오버레이를 없애려고 추가한 `push`/`replace` 전역 패치가, 중복 네비게이션 reject를 삼켜 **resolve로 바꿔버린다.**

신규버튼 로직(`src/views/accrualSlip/index.vue:4036`)은
```js
this.$router.push({path: `/accrualSlip`}).catch(_ => window.location.reload())
```
처럼 "같은 경로 push → reject → `.catch`에서 reload"를 의도했으나, 전역 패치가 reject를 resolve로 바꿔 `.catch`가 실행되지 않는다(reload 안 됨). 동시에 경로가 `/accrualSlip` → `/accrualSlip`로 동일해 `:key`도 안 바뀌어 재마운트도 안 일어난다 → 아무 일도 일어나지 않음.

> 같은 패턴이 `index.vue:3729`(저장 후 `/accrualSlip/${slipNo}` 이동)에도 있으나, 그쪽은 다른 경로로 가서 fullPath가 바뀌므로 정상 동작. 수정 대상은 신규버튼뿐.

### 수정 내용

`src/views/accrualSlip/index.vue` `newPageLoad()` — 전역 패치에 의존하지 않고 confirm `.then()`에서 직접 분기:

```js
}).then(() => {
  if (this.$route.path === '/accrualSlip') {
    window.location.reload()          // 동일 경로 → 강제 새로고침으로 초기화
  } else {
    this.$router.push({path: `/accrualSlip`})  // 파라미터 보유 → 라우팅(키 변경)으로 재마운트
  }
}).catch(() => { ... })
```

### 검증

1. `/accrualSlip` 진입 → 임의 입력 → **신규** → **예** → 화면 초기화 확인.
2. **아니오** → "취소하였습니다." 메시지만 뜨고 데이터 유지 확인.
3. (회귀) 저장 후 `/accrualSlip/${slipNo}` 이동 흐름 정상 동작 확인.

---

## #2. 라우터 전역 패치가 `.catch(reload/폴백)` 부류 전체를 무력화 (#1의 근본 원인)

- **상태**: ✅ 수정완료
- **증상**: `this.$router.push(...).catch(() => window.location.reload())` 또는 `.catch(() => this.$router.push(폴백))` 형태로 **중복 네비게이션 reject에 의존**하는 호출부가 동작하지 않음. #1(accrualSlip 신규버튼)이 대표 증상이고, 같은 부류가 여러 곳에 존재.

### 근본 원인

`src/router.js`의 전역 `Router.prototype.push/replace` 패치가 "redundant navigation" reject를 **resolve로 바꿔 삼켜서**, 호출부의 `.catch`가 발화하지 않는다. 이 패치의 도입 사유("dev 런타임 오버레이에 노출")는 커밋 `cf5602d`(dev 런타임 에러 오버레이 비활성화)로 **이미 무효**.

이 패치로 무력화되던 호출부:

| 파일 | 위치 | `.catch` 의도 |
|---|---|---|
| `src/mixin/slip-basic.js` | `reroute`(1215), `grReroute`(1239), `create`(1249) | 중복 시 `window.location.reload()` |
| `src/components/EstimateReg.vue` | 1471 | theme 쿼리 push 실패 시 name-only push 폴백 |
| `src/components/ExpPriceReg.vue` | 1026 | 동일 |
| `src/components/EstimateList.vue` | 633 | 동일 |
| `src/components/ExpPriceList.vue` | 526 | 동일 |
| `src/views/accrualSlip/index.vue` | 3411, 3729 | 중복 시 reload(3729는 다른 경로라 기존에도 정상) |

### 수정 내용

`src/router.js` — 프로토타입 패치를 **제거**해 네이티브 reject 동작 복원. 대신 `.catch` 없는 호출부의 unhandled rejection 중 **중복 네비게이션만** 전역에서 골라 억제:

```js
if (typeof window !== 'undefined') {
  window.addEventListener('unhandledrejection', (event) => {
    if (isDuplicated(event.reason)) {
      event.preventDefault();   // 중복 네비게이션 미처리 reject만 조용히 무시
    }
  });
}
```

- `.catch` 있는 호출부 → 네이티브 reject → `.catch` 발화 → reload/폴백 정상.
- `.catch` 없는 호출부 → unhandled rejection을 전역 필터가 억제 → 콘솔/오버레이 노이즈 없음.
- 중복 네비게이션 외 진짜 네비게이션 에러는 그대로 전파.

> `accrualSlip newPageLoad()`(#1 우회 수정)는 reject에 의존하지 않고 이미 동작하므로 그대로 둔다. 위 표의 나머지 호출부는 이 한 곳 수정으로 별도 편집 없이 함께 정상화된다.

### 검증

1. (#1 회귀 없음) `/accrualSlip` 신규 → 예 → 초기화, 저장 후 `/accrualSlip/:id` 이동 정상.
2. slip 계열: 저장 후 `reroute`(중복 케이스)·`create`(신규)에서 화면 reload·초기화 확인.
3. theme 폴백: EstimateReg/ExpPriceReg/EstimateList/ExpPriceList 동일 theme 재진입 시 폴백 발화·화면 정상.
4. 콘솔에 "redundant navigation" unhandled rejection·dev 오버레이가 뜨지 않는지 확인.
5. 존재하지 않는 라우트 push 시 reject가 삼켜지지 않고 정상 전파되는지 확인.
