<template>
  <div class="inner-box" style="padding: 20px;">
    <h2 class="dp-ib">GridED → ag-grid PoC (전표 편집형 그리드 4대 난관 실증)</h2>
    <p style="margin: 8px 0; color:#555;">
      운영 <code>GridED.vue</code>는 건드리지 않는 독립 실증용. 슬립 기본유형(config_def)의 핵심 패턴을 ag-grid로 재현.
    </p>
    <ul style="margin: 0 0 12px 16px; color:#555; font-size:13px;">
      <li>① 인-셀 컴포넌트: 부서/계정/제품 <b>검색버튼</b>(cellRenderer) · 차변금액 <b>숫자편집</b> · 유형 <b>셀렉트</b></li>
      <li>② 셀 잠금: 잠긴 행(대변/세액)은 편집 불가 + 음영 (DHTMLX <code>lockRow</code> → <code>editable(fn)</code>+<code>cellStyle(fn)</code>)</li>
      <li>③ 키보드: Tab이 <b>편집 가능한 셀로만</b> 이동 (<code>tabToNextCell</code>)</li>
      <li>④ 편집/포맷: 적요 인라인편집, 금액 천단위 콤마 + 차/대변 합계 자동계산(<code>onCellValueChanged</code>)</li>
    </ul>

    <div class="btn-wrap btn-type2" style="margin-bottom: 8px;">
      <button class="btn-size btn-w-gray" @click="addRow"><i class="fas fa-plus"></i> 행추가</button>
      <button class="btn-size btn-w-gray" @click="removeRow"><i class="fas fa-trash-alt"></i> 행삭제</button>
      <span style="margin-left:12px; color:#888;">차변합계: {{ comma(totalDebit) }} / 대변합계: {{ comma(totalCredit) }}</span>
    </div>

    <ag-grid-vue ref="grid"
                 style="width: 100%; height: 360px;"
                 class="ag-theme-alpine"
                 rowSelection="single"
                 :columnDefs="columnDefs"
                 :rowData="data"
                 :gridOptions="gridOptions"
                 :defaultColDef="defaultColDef"
                 :frameworkComponents="frameworkComponents"
                 @grid-ready="onGridReady"
                 @cell-value-changed="onCellValueChanged"/>
  </div>
</template>

<script>
import Vue from 'vue';
import { AgGridVue } from 'ag-grid-vue';

// ① 인-셀 검색버튼 렌더러 (DhxGrid의 component:{ template:'<button @click>...' } 대체)
const SearchButtonRenderer = Vue.extend({
  template: `<button type="button" class="icon" @click="onClick"><i class="fas fa-search"></i></button>`,
  methods: {
    onClick() {
      // 실제로는 $modal.open({component: Cctr_Ag/Account_Ag/Product_Ag ...}) 호출.
      // PoC에서는 모의 선택값을 setDataValue로 주입(브릿지 동작 증명).
      const field = this.params.colDef.cellRendererParams.targetField;
      const mock = this.params.colDef.cellRendererParams.mockValue;
      this.params.node.setDataValue(field, mock);
    }
  }
});

export default {
  name: 'GridEDPoc',
  components: { AgGridVue },
  data() {
    return {
      gridApi: null,
      columnApi: null,
      columnDefs: [],
      gridOptions: {
        // ③ Tab: 편집 가능한 다음 셀로만 이동(비편집/잠금 셀 건너뜀)
        tabToNextCell: (params) => this.nextEditableCell(params)
      },
      defaultColDef: { resizable: true, sortable: false, filter: false },
      frameworkComponents: { searchBtn: SearchButtonRenderer },
      data: []
    };
  },
  computed: {
    totalDebit() {
      return this.data.reduce((a, r) => a + (Number(r.debitAmt) || 0), 0);
    },
    totalCredit() {
      return this.data.reduce((a, r) => a + (Number(r.creditAmt) || 0), 0);
    }
  },
  methods: {
    comma(v) {
      return (v == null || v === '') ? '' : Number(v).toLocaleString(undefined, { maximumFractionDigits: 0 });
    },
    // 잠금 행 여부 (DHTMLX afterRefreshData 의 lockRow 조건 재현: 대변/세액 행 잠금)
    isLocked(row) {
      return row && (row.dcCd === 'C' || row.lnTypeCd === 'TAX');
    },
    onGridReady(params) {
      this.gridApi = params.api;
      this.columnApi = params.columnApi;
      this.makeColDef();
    },
    makeColDef() {
      const that = this;
      // 편집 가능 조건: 잠긴 행이 아니고, 컬럼이 편집형으로 표시된 경우
      const editableIf = (params) => !that.isLocked(params.data);
      // 잠긴 행 음영 (setColumnClassName/bg-lightpink 대체)
      const lockStyle = (params) => that.isLocked(params.data)
        ? { textAlign: 'left', backgroundColor: '#f5f5f5', color: '#999' }
        : { textAlign: 'left' };
      const rightLock = (params) => Object.assign(lockStyle(params), { textAlign: 'right' });

      this.columnDefs = [
        { headerName: 'No.', width: 55, cellStyle: { textAlign: 'center' }, valueGetter: p => p.node.rowIndex + 1 },
        { headerName: '유형', field: 'dcNm', width: 70, cellStyle: lockStyle },
        // 부서: 텍스트 편집 + 검색버튼
        { headerName: '부서', field: 'cctrNm', width: 150, editable: editableIf, cellStyle: lockStyle },
        {
          headerName: '', field: 'cctrSrch', width: 44, cellStyle: { textAlign: 'center' },
          cellRenderer: 'searchBtn',
          cellRendererParams: { targetField: 'cctrNm', mockValue: '경영지원팀(모의선택)' }
        },
        // 계정: 텍스트 편집 + 검색버튼
        { headerName: '계정명', field: 'acctNm', width: 150, editable: editableIf, cellStyle: lockStyle },
        {
          headerName: '', field: 'acctSrch', width: 44, cellStyle: { textAlign: 'center' },
          cellRenderer: 'searchBtn',
          cellRendererParams: { targetField: 'acctNm', mockValue: '여비교통비(모의선택)' }
        },
        // 제품: 텍스트 편집 + 검색버튼
        { headerName: '제품', field: 'productNm', width: 120, editable: editableIf, cellStyle: lockStyle },
        {
          headerName: '', field: 'prodSrch', width: 44, cellStyle: { textAlign: 'center' },
          cellRenderer: 'searchBtn',
          cellRendererParams: { targetField: 'productNm', mockValue: '공통(모의선택)' }
        },
        // 차변금액: 숫자 편집 + 천단위 콤마
        {
          headerName: '차변금액', field: 'debitAmt', width: 110, editable: editableIf, cellStyle: rightLock,
          valueFormatter: p => that.comma(p.value),
          valueParser: p => that.parseNum(p.newValue)
        },
        // 대변금액: 읽기전용(계산결과)
        {
          headerName: '대변금액', field: 'creditAmt', width: 110, editable: false, cellStyle: rightLock,
          valueFormatter: p => that.comma(p.value)
        },
        // 유형(셀렉트) 예시: 세금코드
        {
          headerName: '세금코드', field: 'taxCd', width: 90, editable: editableIf, cellStyle: lockStyle,
          cellEditor: 'agSelectCellEditor',
          cellEditorParams: { values: ['', 'V1', 'V2', 'V3'] }
        },
        // 적요: 인라인 텍스트 편집(넓게)
        { headerName: '적요', field: 'lnSgtxt', flex: 1, minWidth: 200, editable: editableIf, cellStyle: lockStyle }
      ];
    },
    parseNum(v) {
      if (v == null || v === '') return 0;
      const n = Number(String(v).replace(/,/g, ''));
      return isNaN(n) ? 0 : n;
    },
    // ③ Tab 이동: 편집 가능한 다음 셀로만 이동(비편집/잠금 skip), 끝에서 처음으로 순환(그리드 밖으로 안 나감)
    // 주의: getAllDisplayedColumns()는 columnApi 의 메서드 (gridApi 아님)
    nextEditableCell(params) {
      if (!this.columnApi || !this.gridApi) return params.nextCellPosition;
      const cols = this.columnApi.getAllDisplayedColumns();
      const rowCount = this.gridApi.getDisplayedRowCount();
      if (!cols.length || !rowCount) return params.nextCellPosition;

      const isEditable = (col, rowIndex) => {
        const colDef = col.getColDef();
        const ed = colDef.editable;
        if (ed === true) return true;
        if (typeof ed === 'function') {
          const node = this.gridApi.getDisplayedRowAtIndex(rowIndex);
          return !!ed({ data: node && node.data, node, colDef, column: col, api: this.gridApi });
        }
        return false;
      };

      const backwards = params.backwards === true;
      // 현재(이동 전) 위치
      const cur = params.previousCellPosition || params.nextCellPosition;
      if (!cur) return params.nextCellPosition;
      let r = cur.rowIndex;
      let c = cols.findIndex(x => x.getColId() === cur.column.getColId());

      // 한 칸씩 이동하며(row-major, 순환) 편집 가능한 첫 셀 탐색
      const total = rowCount * cols.length;
      for (let step = 0; step < total; step++) {
        if (backwards) {
          c--;
          if (c < 0) { c = cols.length - 1; r = (r - 1 + rowCount) % rowCount; }
        } else {
          c++;
          if (c >= cols.length) { c = 0; r = (r + 1) % rowCount; }
        }
        if (isEditable(cols[c], r)) {
          return { rowIndex: r, column: cols[c], rowPinned: null };
        }
      }
      return params.nextCellPosition;
    },
    // ④ 금액 편집 시 차/대변 합계 자동계산 (onEditCell 'debitAmt' 로직 재현)
    onCellValueChanged(params) {
      if (params.colDef.field === 'debitAmt') {
        // 마지막 대변(C) 행에 차변합계를 반영하는 단순 예시
        const creditRow = this.data.find(r => r.dcCd === 'C');
        if (creditRow) {
          creditRow.creditAmt = this.totalDebit;
          if (this.gridApi) this.gridApi.refreshCells({ force: true });
        }
      }
    },
    addRow() {
      this.data.push({ dcCd: 'D', dcNm: '차변', lnTypeCd: 'ITEM', cctrNm: '', acctNm: '', productNm: '', debitAmt: 0, creditAmt: 0, taxCd: '', lnSgtxt: '' });
    },
    removeRow() {
      const sel = this.gridApi ? this.gridApi.getSelectedNodes() : [];
      if (!sel.length) {
        this.$swal ? this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' }) : alert('선택된 행이 없습니다.');
        return;
      }
      this.data.splice(sel[0].rowIndex, 1);
    },
    seed() {
      this.data = [
        { dcCd: 'D', dcNm: '차변', lnTypeCd: 'ITEM', cctrNm: '영업1팀', acctNm: '소모품비', productNm: '공통', debitAmt: 50000, creditAmt: 0, taxCd: 'V1', lnSgtxt: '사무용품 구입' },
        { dcCd: 'D', dcNm: '차변', lnTypeCd: 'ITEM', cctrNm: '영업1팀', acctNm: '여비교통비', productNm: '공통', debitAmt: 30000, creditAmt: 0, taxCd: '', lnSgtxt: '택시비' },
        { dcCd: 'D', dcNm: '세액', lnTypeCd: 'TAX', cctrNm: '', acctNm: '부가세대급금', productNm: '', debitAmt: 8000, creditAmt: 0, taxCd: '', lnSgtxt: '(잠금) 세액 행' },
        { dcCd: 'C', dcNm: '대변', lnTypeCd: 'ITEM', cctrNm: '', acctNm: '미지급금', productNm: '', debitAmt: 0, creditAmt: 88000, taxCd: '', lnSgtxt: '(잠금) 대변 행' }
      ];
    }
  },
  created() {
    this.seed();
  }
};
</script>

<style scoped>
.inner-box code { background:#eef; padding:1px 4px; border-radius:3px; }
</style>
