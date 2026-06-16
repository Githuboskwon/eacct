<template>
  <div class="inner-box">
    <div class="table-b">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">세부항목</h3>
        </div>
        <div class="btn-wrap btn-type2">
          <button class="btn-size btn-w-gray" @click="buttenClickEventReset()"><i class="fas fa-redo"></i> 초기화</button>
          <button class="btn-size btn-w-gray" @click="buttonClickEventAdd()"><i class="fas fa-plus"></i> 행추가</button>
          <button class="btn-size btn-w-gray" @click="buttonClickEventRemove()"><i class="fas fa-trash-alt"></i> 행삭제</button>
          <button class="btn-size btn-excel" @click="saveExcel">엑셀 저장</button>
        </div>
        <ag-grid-vue ref="grid" class="slip-grid ag-theme-alpine" style="width: 100%; height: 300px;"
                     rowSelection="single"
                     :columnDefs="columnDefs"
                     :rowData="data"
                     :gridOptions="gridOptions"
                     :defaultColDef="defaultColDef"
                     :frameworkComponents="frameworkComponents"
                     :pinnedBottomRowData="pinnedBottomRowData"
                     @grid-ready="onGridReady"
                     @cell-editing-stopped="onCellEditingStopped"
                     @cell-value-changed="onCellValueChanged"/>
      </div>
    </div>
  </div>
</template>

<script>
/*
 * GridED_Ag — GridED(편집형 전표)의 ag-grid 전환본 (증분 작업).
 * [완료] config_def(일반전표/기타) 슬립유형: 자동검색·셀잠금·합계푸터·행추가삭제·Tab.
 * [TODO] config_E6(출장/교통비: 유종/유가/거리), config_E2/E5(보조계정·예산잔액),
 *        config_E1(법인카드 2그리드: 카드정보/스캔증빙) — 슬립유형별로 순차 이관 + 런타임 검증.
 * 완성·검증 전까지 slip-basic.js 의 gridType 스위치에 연결하지 않음(운영 무영향).
 */
import Vue from 'vue'
import { AgGridVue } from 'ag-grid-vue'
import ErpCctr from '@/components/ErpCctr_Ag.vue'
import Account from '@/components/Account_Ag.vue'
import Product from '@/components/Product_Ag.vue'
import SlipMngItemPop from '@/components/SlipMngItemPop.vue'
import mixin from '@/mixin/slip-common'
import mixin2 from '@/mixin'
import mixinSlip from '@/mixin/slip'

const _url = (...args) => args.map(x => String(x || '').trim().replace(/^\/*|\/*$/g, '')).filter(x => x).join('/')

// 검색버튼 셀 렌더러 (DHTMLX component:{template:'<button @click>'} 대체). kind 로 어떤 팝업인지 구분
const SearchBtnRenderer = Vue.extend({
  template: `<button type="button" class="icon" v-if="show" @click="onClick"><i class="fas fa-search"></i></button>`,
  computed: {
    show() {
      const kind = this.params.colDef.cellRendererParams.kind
      if (kind === 'addon') return !!(this.params.data && this.params.data.hasAddon)
      return true
    }
  },
  methods: {
    onClick() {
      this.params.context.parent.openSearch(this.params.colDef.cellRendererParams.kind, this.params.node.rowIndex)
    }
  }
})

export default {
  props: ['slipType', 'value', 'status', 'lnk'],
  mixins: [mixin, mixin2, mixinSlip],
  components: { AgGridVue },
  data() {
    return {
      id: 'ED',
      data: [],
      datad: [],
      rcpYn: 'N',
      gridApi: null,
      columnApi: null,
      gridOptions: {
        context: { parent: this },
        tabToNextCell: (params) => this.nextEditableCell(params)
      },
      defaultColDef: { resizable: true, sortable: false, filter: false },
      frameworkComponents: { searchBtn: SearchBtnRenderer }
    }
  },
  computed: {
    columnDefs() {
      // 증분 1: config_def 만 구현. (E6/E2/E5/E1 은 추후)
      switch (this.slipType) {
        case 'E6':
        case 'E1':
        case 'E2':
        case 'E5':
          // TODO: 슬립유형별 컬럼/로직 이관 전까지는 기본 컬럼으로 표시
          return this.makeDefCols()
        default:
          return this.makeDefCols()
      }
    },
    // 합계 푸터 (DHTMLX attachHeader #stat_total 대체): 차변=비대변 합, 대변=대변 합 (acctAmt 기준)
    pinnedBottomRowData() {
      const debit = this.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') !== 'C_ITEM')
        .reduce((a, x) => a + this.$numeral(x.acctAmt).value(), 0)
      const credit = this.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'C_ITEM')
        .reduce((a, x) => a + this.$numeral(x.acctAmt).value(), 0)
      return [{ dcNm: '합계', debitAmt: debit, creditAmt: credit, __total: true }]
    }
  },
  watch: {
    'value': {
      immediate: true,
      deep: true,
      handler(value) {
        this.data = value.slipDetails || []
        this.datad = value.slipDetails2 || []
      }
    }
  },
  methods: {
    onGridReady(params) {
      this.gridApi = params.api
      this.columnApi = params.columnApi
    },
    isLocked(row) {
      if (!row) return false
      const id = [row.dcCd, row.lnTypeCd].join('_')
      return id === 'C_ITEM' || id === 'D_TAX'
    },
    L(align) { return { textAlign: align || 'left' } },
    fmtAmt(p) {
      return (p.value == null || p.value === '') ? '' : this.$numeral(p.value).format('0,0')
    },
    // config_def 컬럼 정의
    makeDefCols() {
      const that = this
      const editableIf = (p) => !that.isLocked(p.data) && !(p.node && p.node.rowPinned)
      const lockStyle = (p) => {
        if (p.node && p.node.rowPinned) return { textAlign: 'right', fontWeight: 'bold', color: '#c00' }
        return that.isLocked(p.data) ? { backgroundColor: '#f5f5f5', color: '#999' } : null
      }
      const cell = (align) => (p) => Object.assign({ textAlign: align || 'left' }, lockStyle(p) || {})
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowPinned ? '' : p.node.rowIndex + 1, width: 45, cellStyle: cell('center') },
        { headerName: '유형', field: 'dcNm', width: 50, cellStyle: cell('center') },
        { headerName: '세금코드', field: 'taxCd', width: 60, cellStyle: cell('center') },
        { headerName: '비용부서코드', field: 'cctrCd', hide: true },
        { headerName: '부서', field: 'cctrNm', width: 120, cellStyle: cell('left') }, // ron: 검색버튼으로만 설정
        { headerName: '', field: 'cctrSrch', width: 40, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'cctr' } },
        { headerName: '계정코드', field: 'acctCd', width: 80, cellStyle: cell('center') },
        { headerName: '계정명', field: 'acctNm', width: 140, editable: editableIf, cellStyle: cell('left') },
        { headerName: '', field: 'acctSrch', width: 40, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'account' } },
        { headerName: '제품코드', field: 'productCd', hide: true },
        { headerName: '제품', field: 'productNm', width: 90, editable: editableIf, cellStyle: cell('left') },
        { headerName: '', field: 'prodSrch', width: 30, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'product' } },
        { headerName: '', field: 'acctAddon', width: 40, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'addon' } },
        { headerName: '차변금액', field: 'debitAmt', width: 90, editable: editableIf, cellStyle: cell('right'), valueFormatter: that.fmtAmt, valueParser: p => that.$numeral(p.newValue).value() },
        { headerName: '대변금액', field: 'creditAmt', width: 90, cellStyle: cell('right'), valueFormatter: that.fmtAmt },
        { headerName: '적요', field: 'lnSgtxt', flex: 1, minWidth: 200, editable: editableIf, cellStyle: cell('left') }
      ]
    },
    // ===== 편집 종료 시 자동검색 (DHTMLX onEditCell stage2 대체) =====
    onCellEditingStopped(params) {
      if (params.node && params.node.rowPinned) return
      const field = params.colDef.field
      const row = params.data
      if (field === 'acctNm') {
        if (row.acctNm) this.findAccount(row, row.acctNm)
      } else if (field === 'productNm') {
        if (row.productNm) this.findProduct(row, row.productNm)
      }
    },
    onCellValueChanged(params) {
      if (params.node && params.node.rowPinned) return
      if (params.colDef.field === 'debitAmt') {
        params.data.acctAmt = params.data.debitAmt = this.$numeral(params.newValue || 0).value()
        if (this.gridApi) this.gridApi.refreshCells({ force: true })
      }
    },
    // ===== 검색 팝업 (셀 검색버튼 클릭) =====
    openSearch(kind, rowIndex) {
      const row = this.data[rowIndex]
      if (!row) return
      if (kind === 'cctr') this.openCctr(row)
      else if (kind === 'account') this.openAccount(row)
      else if (kind === 'product') this.openProduct(row)
      else if (kind === 'addon') this.openAddon(row)
    },
    openCctr(row) {
      this.$modal.open({
        component: ErpCctr, parent: this, width: 700,
        events: { close(o) { row.cctrCd = o.deptCd; row.cctrNm = o.deptNm } }
      })
    },
    openAccount(row) {
      const vm = this
      this.$modal.open({
        component: Account, parent: this, width: 700,
        props: { deptCd: row.cctrCd, slipTypeCd: vm.value.slipTypeCd },
        events: { close(o) { row.acctCd = o.acctCd; row.acctNm = o.acctNm; row.ctrlYn = o.ctrlYn; vm.refresh() } }
      })
    },
    openProduct(row) {
      const vm = this
      this.$modal.open({
        component: Product, parent: this, width: 700,
        props: { slipTypeCd: vm.value.slipTypeCd },
        events: { close(o) { row.productCd = o.detailCd; row.productNm = o.detailNm; vm.refresh() } }
      })
    },
    openAddon(row) {
      this.$modal.open({
        component: SlipMngItemPop, parent: this,
        props: { acctCd: row.acctCd, data: row },
        events: { close(data) { for (let i = 1; i <= 10; i++) { row['ref' + i] = data['ref' + i] } } }
      })
    },
    // ===== 이름 입력 → 코드 자동조회 (단건이면 채우고, 다건이면 팝업) =====
    findCctr(row, searchStr) {
      const vm = this
      this.$store.commit('loading')
      this.$http.get(_url('/api/cctr/erp', searchStr)).then(response => {
        if (Array.isArray(response.data) && response.data.length === 1) {
          row.cctrCd = response.data[0].deptCd; row.cctrNm = response.data[0].deptNm
        } else {
          vm.$modal.open({
            component: ErpCctr, parent: vm, props: { title: '비용부서', searchStr }, width: 700,
            events: { close(d) { row.cctrCd = d.deptCd; row.cctrNm = d.deptNm } }
          })
        }
      }).finally(() => { this.$store.commit('finish'); vm.refresh() })
    },
    findAccount(row, searchStr) {
      const vm = this
      this.$store.commit('loading')
      this.$http.get(_url('/api/account', searchStr)).then(response => {
        if (Array.isArray(response.data) && response.data.length === 1) {
          row.acctCd = response.data[0].acctCd; row.acctNm = response.data[0].acctNm; row.ctrlYn = response.data[0].ctrlYn
        } else {
          vm.$modal.open({
            component: Account, parent: vm, props: { title: '귀속부서', searchStr, deptCd: row.cctrCd, slipTypeCd: vm.value.slipTypeCd }, width: 700,
            events: { close(d) { row.acctCd = d.acctCd; row.acctNm = d.acctNm; row.ctrlYn = d.ctrlYn } }
          })
        }
      }).finally(() => { this.$store.commit('finish'); vm.refresh() })
    },
    findProduct(row, searchStr) {
      const vm = this
      this.$store.commit('loading')
      this.$http.get(`/api/slip/product/${searchStr}`).then(response => {
        if (Array.isArray(response.data) && response.data.length === 1) {
          row.productCd = response.data[0].detailCd; row.productNm = response.data[0].detailNm
        } else {
          vm.$modal.open({
            component: Product, parent: vm, props: { title: '제품', searchStr, slipTypeCd: vm.value.slipTypeCd }, width: 700,
            events: { close(d) { row.productCd = d.detailCd; row.productNm = d.detailNm } }
          })
        }
      }).finally(() => { this.$store.commit('finish'); vm.refresh() })
    },
    refresh() {
      if (this.gridApi) this.gridApi.refreshCells({ force: true })
    },
    // ===== 행 추가/삭제/초기화 (DHTMLX 인스턴스 API → ag-grid 선택 + mixin add_row) =====
    selectedIndex() {
      const nodes = this.gridApi ? this.gridApi.getSelectedNodes() : []
      return nodes.length ? nodes[0].rowIndex : null
    },
    buttonClickEventAdd() {
      let dupv = 0
      if (this.data.length > 0) {
        const sum = this.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').map(x => x.acctAmt).reduce((a, v) => a + v, 0)
        dupv = this.value.supAmt - sum
        dupv = dupv < 0 ? 0 : dupv
      }
      const idx = this.selectedIndex()
      this.add_row({
        dcCd: 'D', lnTypeCd: 'ITEM',
        deptCd: this.value.baseDeptCd, deptNm: this.value.baseDeptNm,
        acctAmt: dupv,
        itemIndex: (idx != null) ? idx + 1 : undefined
      })
    },
    buttonClickEventRemove() {
      const idx = this.selectedIndex()
      if (idx == null) { this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' }); return }
      const row = this.value.slipDetails[idx]
      if (!row || [row.dcCd, row.lnTypeCd].join('_') !== 'D_ITEM') {
        this.$swal({ type: 'warning', text: '선택하신 행은 삭제할 수 없습니다.' }); return
      }
      this.value.slipDetails.splice(idx, 1)
    },
    buttenClickEventReset() {
      this.reset_rows()
    },
    saveExcel() {
      if (this.gridApi) this.gridApi.exportDataAsExcel({ fileName: '전표세부항목' })
    },
    // ===== Tab: 편집 가능 셀로만 순환 (PoC 검증 패턴) =====
    nextEditableCell(params) {
      if (!this.columnApi || !this.gridApi) return params.nextCellPosition
      const cols = this.columnApi.getAllDisplayedColumns()
      const rowCount = this.gridApi.getDisplayedRowCount()
      if (!cols.length || !rowCount) return params.nextCellPosition
      const isEditable = (col, rowIndex) => {
        const colDef = col.getColDef()
        const ed = colDef.editable
        if (ed === true) return true
        if (typeof ed === 'function') {
          const node = this.gridApi.getDisplayedRowAtIndex(rowIndex)
          return !!ed({ data: node && node.data, node, colDef, column: col, api: this.gridApi })
        }
        return false
      }
      const backwards = params.backwards === true
      const cur = params.previousCellPosition || params.nextCellPosition
      if (!cur) return params.nextCellPosition
      let r = cur.rowIndex
      let c = cols.findIndex(x => x.getColId() === cur.column.getColId())
      const total = rowCount * cols.length
      for (let step = 0; step < total; step++) {
        if (backwards) { c--; if (c < 0) { c = cols.length - 1; r = (r - 1 + rowCount) % rowCount } }
        else { c++; if (c >= cols.length) { c = 0; r = (r + 1) % rowCount } }
        if (isEditable(cols[c], r)) return { rowIndex: r, column: cols[c], rowPinned: null }
      }
      return params.nextCellPosition
    }
  }
}
</script>

<style lang="scss" scoped>
.slip-grid { margin-bottom: 8px; }
.slip-grid :global(.ag-row-pinned) { background-color: #f9f9f3; font-weight: bold; }
</style>
