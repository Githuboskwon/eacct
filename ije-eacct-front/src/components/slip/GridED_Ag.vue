<template>
  <div class="inner-box">
    <div class="table-b">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">{{ slipType === 'E1' ? '법인카드지급' : '세부항목' }}</h3>
        </div>
        <div class="btn-wrap btn-type2">
          <button v-if="slipType === 'E1'" class="btn-size btn-w-gray" @click="buttenClickEventCrdList()"><i class="far fa-file-alt"></i> 법인카드내역</button>
          <button v-else class="btn-size btn-w-gray" @click="buttenClickEventReset()"><i class="fas fa-redo"></i> 초기화</button>
          <button v-if="slipType !== 'E1'" class="btn-size btn-w-gray" @click="buttonClickEventAdd()"><i class="fas fa-plus"></i> 행추가</button>
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

    <!-- E1: 현금지급 (2번째 그리드) -->
    <div class="table-b" v-if="slipType === 'E1'">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">현금지급</h3>
        </div>
        <div class="btn-wrap btn-type2">
          <button class="btn-size btn-w-gray" @click="buttonClickEventAdd2()"><i class="fas fa-plus"></i> 행추가</button>
          <button class="btn-size btn-w-gray" @click="buttonClickEventRemove2()"><i class="fas fa-trash-alt"></i> 행삭제</button>
        </div>
        <ag-grid-vue ref="grid2" class="slip-grid ag-theme-alpine" style="width: 100%; height: 260px;"
                     rowSelection="single"
                     :columnDefs="columnDefs2"
                     :rowData="datad"
                     :gridOptions="gridOptions2"
                     :defaultColDef="defaultColDef"
                     :frameworkComponents="frameworkComponents"
                     @grid-ready="onGridReady2"
                     @cell-editing-stopped="onCellEditingStopped2"
                     @cell-value-changed="onCellValueChanged2"/>
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
import AccountSub from '@/components/ErpAccountSub_Ag.vue'
import ErpAccount from '@/components/ErpAccount.vue'
import Product from '@/components/Product_Ag.vue'
import SlipMngItemPop from '@/components/SlipMngItemPop.vue'
import SlipCrdLstModal from '@/components/SlipCrdLstModal.vue'
import EvidAtchPop from '@/components/EvidAtchPop.vue'
import CardInfoDetailPop from '@/components/CardInfoDetailPop.vue'
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
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
      const p = this.params.colDef.cellRendererParams
      if (p.grid === 2) this.params.context.parent.openSearch2(p.kind, this.params.node.rowIndex)
      else this.params.context.parent.openSearch(p.kind, this.params.node.rowIndex)
    }
  }
})

// 카드정보 버튼 셀 렌더러 (E1)
const CrdInfoRenderer = Vue.extend({
  template: `<button type="button" class="icon far fa-file" @click="open"></button>`,
  methods: {
    open() {
      const v = this.params.data
      if (v && v.apprNo) this.params.context.parent.openCrdInfo(v.crdNo, v.apprNo)
    }
  }
})

// 스캔증빙 셀 렌더러 (E1/E1_2): 파일수 표시 + 클릭 시 증빙 팝업
const ScanCtRenderer = Vue.extend({
  template: `<span style="color:#0065b3;cursor:pointer;" @click="open">{{ display }} <i class="far fa-file-alt"></i></span>`,
  data() { return { display: '0' } },
  created() { this.refresh() },
  watch: { 'params': { deep: true, handler() { this.refresh() } } },
  methods: {
    refresh() {
      const v = this.params.data
      this.display = this.$numeral(v && v.scanCt).format('0')
      if (v && v.eaSlipNo) {
        this.params.context.parent.fetchScanCt(v).then(cnt => {
          if (cnt !== undefined) { v.scanCt = cnt; this.display = this.$numeral(v.scanCt).format('0') }
        })
      }
    },
    open() { this.params.context.parent.openEvidence(this.params.data) }
  }
})

export default {
  compatConfig: { MODE: 2 },
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
      gridApi2: null,
      columnApi: null,
      options: { TPS_TYPE_CD: [], OIL_KIND_CD: [], E1_taxesCd: [] }, // 교통비유형/유종/세금 코드
      oilPrice: {}, // 월(YYYYMM)별 유가표 캐시
      budget: {}, // 잔여예산 캐시 (postDt_deptCd_acctCd_acctCdSub)
      budgetLock: {}, // 예산 조회 중복방지
      gridOptions: {
        context: { parent: this },
        tabToNextCell: (params) => this.nextEditableCell(params)
      },
      gridOptions2: {
        context: { parent: this },
        tabToNextCell: (params) => this.nextEditableCell(params)
      },
      defaultColDef: { resizable: true, sortable: false, filter: false },
      frameworkComponents: { searchBtn: SearchBtnRenderer, select: SelectCellRenderer, crdInfoBtn: CrdInfoRenderer, scanCtCell: ScanCtRenderer }
    }
  },
  computed: {
    columnDefs() {
      // 증분: config_def(일반) + config_E6(출장/교통비) 구현. (E2/E5/E1 은 추후)
      switch (this.slipType) {
        case 'E6':
          return this.makeE6Cols()
        case 'E2':
        case 'E5':
          return this.makeE2Cols()
        case 'E1':
          return this.makeE1Cols()
        default:
          return this.makeDefCols()
      }
    },
    columnDefs2() {
      return this.makeE1_2Cols()
    },
    // 합계 푸터 (DHTMLX attachHeader #stat_total 대체)
    pinnedBottomRowData() {
      // E1/E2/E5: 합계 푸터 없음 (원본 attachHeader([],[]))
      if (this.slipType === 'E1' || this.slipType === 'E2' || this.slipType === 'E5') return null
      if (this.slipType === 'E6') {
        // E6: 사용금액(D_ITEM acctAmt) 합계
        const sum = this.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM')
          .reduce((a, x) => a + this.$numeral(x.acctAmt).value(), 0)
        return [{ stptPlc: '합계', acctAmt: sum, __total: true }]
      }
      // def/기타: 차변=비대변 합, 대변=대변 합 (acctAmt 기준)
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
  created() {
    if (this.slipType === 'E6') {
      this.$http.get('/api/code/detail', { params: { groupCd: 'TPS_TYPE_CD' } }).then(r => { this.options.TPS_TYPE_CD = r.data })
      this.$http.get('/api/code/detail', { params: { groupCd: 'OIL_KIND_CD' } }).then(r => { this.options.OIL_KIND_CD = r.data })
      // 현재 데이터에 등장하는 월의 유가표 선로드
      this.$nextTick(() => {
        const months = Array.from(new Set((this.data || []).filter(x => x.useDt).map(x => this.$moment(x.useDt).format('YYYYMM'))))
        months.forEach(m => this.loadOilPrice(m))
      })
    } else if (this.slipType === 'E2' || this.slipType === 'E5') {
      this.$nextTick(() => this.loadBudgetsForData())
    } else if (this.slipType === 'E1') {
      this.$http.get('/api/code/detail', { params: { groupCd: 'OIL_KIND_CD' } }).then(r => { this.options.OIL_KIND_CD = r.data })
      this.$http.get('/api/code/detail', { params: { groupCd: 'TPS_TYPE_CD' } }).then(r => { this.options.TPS_TYPE_CD = r.data })
      this.loadTaxes()
      // 증빙일자 월의 유가 + 데이터 월 유가 선로드
      this.loadOilPrice(this.value.evdDt ? this.$moment(this.value.evdDt).format('YYYYMM') : null)
      this.$nextTick(() => {
        const months = Array.from(new Set((this.data || []).filter(x => x.eaSlipDt).map(x => this.$moment(x.eaSlipDt).format('YYYYMM'))))
        months.forEach(m => this.loadOilPrice(m))
      })
    }
  },
  methods: {
    onGridReady(params) {
      this.gridApi = params.api
      this.columnApi = params.columnApi
    },
    onGridReady2(params) {
      this.gridApi2 = params.api
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
      const f = params.colDef.field
      const row = params.data
      if (this.slipType === 'E2' || this.slipType === 'E5') {
        if (f === 'deptNm') this.cctrValueChangeE2(row, row.deptNm)
        else if (f === 'acctNm') this.acctValueChangeE2(row, row.acctNm)
        else if (f === 'acctNmSub') this.subAcctValueChange(row, row.acctNmSub)
        else if (f === 'detailNm') this.productValueChangeE2(row, row.detailNm)
        return
      }
      if (this.slipType === 'E1') {
        if (f === 'expenseName' || f === 'expenseAcctName') { if (row.expenseAcctName) this.findExpenseAccount(row, row.expenseAcctName) }
        else if (f === 'detailNm') { if (row.detailNm) this.findProductE1(row, row.detailNm) }
        return
      }
      // def
      if (f === 'acctNm') { if (row.acctNm) this.findAccount(row, row.acctNm) }
      else if (f === 'productNm') { if (row.productNm) this.findProduct(row, row.productNm) }
    },
    // 현금지급(E1_2) 편집 종료
    onCellEditingStopped2(params) {
      const row = params.data
      const f = params.colDef.field
      if (f === 'expenseName2' || f === 'expenseAcctName2') { if (row.expenseAcctName2) this.findExpenseAccount2(row, row.expenseAcctName2) }
      else if (f === 'detailNm2') { if (row.detailNm2) this.findProductE1_2(row, row.detailNm2) }
    },
    onCellValueChanged(params) {
      if (params.node && params.node.rowPinned) return
      const row = params.data
      const f = params.colDef.field

      // ── config_E6 (출장/교통비) ──
      if (this.slipType === 'E6') {
        if (f === 'tpsTypeCd') {
          if (row.tpsTypeCd === '10') { row.acctAmt = 0; row.tpsDst = 0; row.oilKindCd = 'GSL' }
          else { row.tpsDst = undefined; row.oilUpc = undefined; row.oilEff = undefined; row.acctAmt = 0; row.oilKindCd = undefined }
        } else if (f === 'tpsDst' || f === 'oilKindCd') {
          this.computeOil(row)
        } else if (f === 'useDt') {
          const ym = row.useDt ? this.$moment(row.useDt).format('YYYYMM') : null
          this.loadOilPrice(ym).then(() => { this.computeOil(row); this.refresh() })
          this.syncUseDt()
        } else if (f === 'acctAmt') {
          this.recalcE6Totals()
        }
        this.refresh()
        return
      }

      // ── config_E2 / E5 (개인비용외) ──
      if (this.slipType === 'E2' || this.slipType === 'E5') {
        if (f === 'useAmt') this.recalcE2Totals()
        this.refresh()
        return
      }

      // ── config_E1 (법인카드: 카드+교통 하이브리드) ──
      if (this.slipType === 'E1') {
        if (f === 'tpsTypeCd') {
          if (row.tpsTypeCd === '10') { row.acctAmt = 0; row.tpsDst = 0; row.oilKindCd = 'GSL' }
          else { row.tpsDst = undefined; row.oilUpc = undefined; row.oilEff = undefined; row.acctAmt = 0; row.oilKindCd = undefined }
        } else if (f === 'tpsDst' || f === 'oilKindCd') {
          this.computeOilE1(row)
        } else if (f === 'acctAmt') {
          this.recalcE1Totals()
        }
        this.recalcE1Totals()
        this.refresh()
        return
      }

      // ── config_def (일반) ──
      if (f === 'debitAmt') {
        row.acctAmt = row.debitAmt = this.$numeral(params.newValue || 0).value()
        this.refresh()
      }
    },
    // 현금지급(E1_2) 값변경: 금액 → 총액 재계산
    onCellValueChanged2(params) {
      if (params.colDef.field === 'useAmt2') this.recalcE1Totals()
      this.refresh()
    },
    // ===== 검색 팝업 (셀 검색버튼 클릭) =====
    openSearch(kind, rowIndex) {
      const row = this.data[rowIndex]
      if (!row) return
      // def
      if (kind === 'cctr') this.openCctr(row)
      else if (kind === 'account') this.openAccount(row)
      else if (kind === 'product') this.openProduct(row)
      else if (kind === 'addon') this.openAddon(row)
      // E2/E5
      else if (kind === 'cctrE2') this.openCctrE2(row)
      else if (kind === 'accountE2') this.openAccountE2(row)
      else if (kind === 'acctSubE2') this.openAccountSubBtn(row)
      else if (kind === 'productE2') this.openProductE2(row)
      else if (kind === 'addonE2') this.openAddonE2(row)
      // E1 (메인)
      else if (kind === 'cctrE1') this.openCctrE1(row)
      else if (kind === 'expenseE1') this.openExpenseE1(row)
      else if (kind === 'productE1') this.openProductE1Btn(row)
    },
    // 현금지급(E1_2) 검색버튼
    openSearch2(kind, rowIndex) {
      const row = this.datad[rowIndex]
      if (!row) return
      if (kind === 'cctrE1_2') this.openCctrE1_2(row)
      else if (kind === 'expenseE1_2') this.openExpenseE1_2(row)
      else if (kind === 'productE1_2') this.openProductE1_2Btn(row)
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
      if (this.slipType === 'E1') this.recalcE1Totals()
      else if (this.slipType === 'E2' || this.slipType === 'E5') this.recalcE2Totals()
      else if (this.slipType === 'E6') this.recalcE6Totals()
    },
    buttenClickEventReset() {
      this.reset_rows()
    },
    saveExcel() {
      if (this.gridApi) this.gridApi.exportDataAsExcel({ fileName: '전표세부항목' })
    },
    // ===== config_E6 (출장/교통비) =====
    makeE6Cols() {
      const that = this
      const notLocked = (p) => !that.isLocked(p.data) && !(p.node && p.node.rowPinned)
      const editAmt = (p) => notLocked(p) && p.data && p.data.tpsTypeCd !== '10' // 유류대(10)면 거리로 자동계산
      const editDst = (p) => notLocked(p) && p.data && p.data.tpsTypeCd === '10'
      const lockStyle = (p) => {
        if (p.node && p.node.rowPinned) return { textAlign: 'right', fontWeight: 'bold', color: '#c00' }
        return that.isLocked(p.data) ? { backgroundColor: '#f5f5f5', color: '#999' } : null
      }
      const cell = (align) => (p) => Object.assign({ textAlign: align || 'left' }, lockStyle(p) || {})
      const selCell = (key) => ({
        cellRenderer: 'select', editable: notLocked,
        cellRendererParams: { options: that.options[key] || [], detailCd: 'detailCd', detailNm: 'detailNm' }
      })
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowPinned ? '' : p.node.rowIndex + 1, width: 45, cellStyle: cell('center') },
        { headerName: '사용일자', field: 'useDt', width: 100, editable: notLocked, cellStyle: cell('center'), valueFormatter: p => p.value ? that.$moment(p.value).format('YYYY-MM-DD') : '' },
        { headerName: '출발지', field: 'stptPlc', width: 70, editable: notLocked, cellStyle: cell('left') },
        { headerName: '도착지', field: 'dstnPlc', width: 70, editable: notLocked, cellStyle: cell('left') },
        { headerName: '출장목적', field: 'biztrpObj', width: 100, editable: notLocked, cellStyle: cell('left') },
        Object.assign({ headerName: '교통비유형', field: 'tpsTypeCd', width: 100, cellStyle: cell('center') }, selCell('TPS_TYPE_CD')),
        { headerName: '거리', field: 'tpsDst', width: 70, editable: editDst, cellStyle: cell('right'), valueFormatter: that.fmtAmt, valueParser: p => that.$numeral(p.newValue).value() },
        Object.assign({ headerName: '유종', field: 'oilKindCd', width: 90, cellStyle: cell('center') }, selCell('OIL_KIND_CD')),
        {
          headerName: '유류단가/연비', field: 'oilUpc', width: 100, cellStyle: cell('center'),
          valueGetter: p => {
            const v = p.data || {}
            return (v.oilUpc && v.oilEff) ? `${that.$numeral(v.oilUpc).format('0,0')} / ${that.$numeral(v.oilEff).format('0,0')}` : ''
          }
        },
        { headerName: '사용금액', field: 'acctAmt', width: 90, editable: editAmt, cellStyle: cell('right'), valueFormatter: that.fmtAmt, valueParser: p => that.$numeral(p.newValue).value() },
        { headerName: '계정코드', field: 'acctCd', width: 60, cellStyle: cell('center') },
        { headerName: '계정명', field: 'acctNm', flex: 1, minWidth: 100, editable: notLocked, cellStyle: cell('left') },
        { headerName: '', field: 'acctSrch', width: 40, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'account' } }
      ]
    },
    loadOilPrice(ym) {
      if (!ym || this.oilPrice[ym] !== undefined) return Promise.resolve()
      return this.$http.post('/api/oilPrice/list', { baseYm: ym })
        .then(r => { this.$set(this.oilPrice, ym, r.data) })
        .catch(() => { this.$set(this.oilPrice, ym, []) })
    },
    computeOil(row) {
      if (!row || row.tpsTypeCd !== '10') return
      const ym = row.useDt ? this.$moment(row.useDt).format('YYYYMM') : null
      const list = ym ? (this.oilPrice[ym] || []) : []
      const found = list.filter(x => x.oilKindCd === row.oilKindCd)[0]
      if (found) {
        row.oilUpc = found.oilUpce
        row.oilEff = found.oilEff
        const d = this.$numeral(row.tpsDst).value()
        const upc = this.$numeral(row.oilUpc).value()
        const eff = this.$numeral(row.oilEff).value()
        try { row.supAmt = row.acctAmt = Math.floor(d / eff * upc) } catch (e) { row.supAmt = row.acctAmt = 0 }
        if (!isFinite(row.acctAmt)) { row.supAmt = row.acctAmt = 0 }
      }
    },
    syncUseDt() {
      const ditems = this.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').filter(x => x.useDt)
      if (!ditems.length) return
      const max = String(Math.max.apply(null, ditems.map(x => Number(x.useDt))))
      this.$parent.value.payDueDt = max
      this.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'C_ITEM').forEach(x => { x.useDt = max })
    },
    recalcE6Totals() {
      const ditems = this.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM')
      if (!ditems.length) return
      const totAmt = ditems.map(x => this.$numeral(x.acctAmt).value()).reduce((a, v) => a + v, 0)
      const taxRt = (this.$parent.value.taxRt || 0) / 100.0
      const taxAmt = Math.floor(taxRt * totAmt / (1 + taxRt))
      this.$parent.value.totAmt = totAmt
      this.$parent.value.supAmt = totAmt - taxAmt
      this.$parent.value.taxAmt = taxAmt
    },
    // ===== config_E2 / E5 (개인비용외) =====
    makeE2Cols() {
      const that = this
      const notLocked = (p) => !that.isLocked(p.data) && !(p.node && p.node.rowPinned)
      const lockStyle = (p) => that.isLocked(p.data) ? { backgroundColor: '#f5f5f5', color: '#999' } : null
      const cell = (align) => (p) => Object.assign({ textAlign: align || 'left' }, lockStyle(p) || {})
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowIndex + 1, width: 45, cellStyle: cell('center') },
        { headerName: '비용부서코드', field: 'deptCd', hide: true },
        { headerName: '부서', field: 'deptNm', width: 100, editable: notLocked, cellStyle: cell('left') },
        { headerName: '', field: 'cctrSrch', width: 30, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'cctrE2' } },
        { headerName: '계정코드', field: 'acctCd', hide: true },
        { headerName: '계정명', field: 'acctNm', width: 140, editable: notLocked, cellStyle: cell('left') },
        { headerName: '', field: 'acctSrch', width: 40, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'accountE2' } },
        { headerName: '', field: 'dff', width: 40, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'addonE2' } },
        { headerName: '보조계정코드', field: 'acctCdSub', hide: true },
        { headerName: '보조계정', field: 'acctNmSub', width: 140, editable: notLocked, cellStyle: cell('left') },
        { headerName: '', field: 'acctSubSrch', width: 40, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'acctSubE2' } },
        { headerName: '개발 프로젝트코드', field: 'detailCd', hide: true },
        { headerName: '개발 프로젝트', field: 'detailNm', width: 90, editable: notLocked, cellStyle: cell('left') },
        { headerName: '', field: 'prodSrch', width: 30, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'productE2' } },
        { headerName: '금액', field: 'useAmt', width: 90, editable: notLocked, cellStyle: cell('right'), valueFormatter: that.fmtAmt, valueParser: p => that.$numeral(p.newValue).value() },
        {
          headerName: '잔여예산', field: 'rmdAmt', width: 90, cellStyle: cell('right'),
          valueGetter: p => { const k = that.budgetKey(p.data || {}); const v = that.budget[k]; return v !== undefined ? v : (p.data && p.data.rmdAmt) },
          valueFormatter: that.fmtAmt
        },
        { headerName: '세금코드코드', field: 'taxCd', hide: true },
        { headerName: '세금코드', field: 'taxNm', width: 80, cellStyle: cell('right') },
        { headerName: '적요', field: 'lnSgtxt', flex: 1, minWidth: 200, editable: notLocked, cellStyle: cell('left') }
      ]
    },
    // 이름 입력 → 자동검색 (DHTMLX onEnter → *ValueChange 대체). $refs.grid.data[rId] → row 직접
    cctrValueChangeE2(row, schCode) {
      const vm = this
      row.acctCd = ''; row.acctNm = ''; row.acctCdSub = ''; row.acctNmSub = ''
      for (let i = 1; i <= 15; i++) row['attribute' + i] = ''
      if (!schCode) { row.deptCd = ''; row.deptNm = ''; this.refresh(); return }
      this.$http.get('/api/cctr/erp/' + schCode).then(r => {
        if (r.data.length === 1) {
          row.deptCd = r.data[0].deptCd; row.deptNm = r.data[0].deptNm
          if (r.data[0].expenseFlag === 'Y') { row.isExpenseFlag = true } else { row.isExpenseFlag = false; row.detailCd = ''; row.detailNm = '' }
        } else {
          vm.$modal.open({
            component: ErpCctr, parent: vm, props: { searchStr: schCode }, width: 700,
            events: { close(o) { row.deptCd = o.deptCd; row.deptNm = o.deptNm; vm.refresh() } }
          })
        }
        vm.refresh()
      })
    },
    acctValueChangeE2(row, schCode) {
      const vm = this
      row.acctCdSub = ''; row.acctNmSub = ''
      for (let i = 1; i <= 15; i++) row['attribute' + i] = ''
      if (!schCode) { row.acctCd = ''; row.acctNm = ''; this.refresh(); return }
      const deptCd = row.deptCd
      this.$http.get('/api/account/' + deptCd + '/' + this.slipType + '/' + this.rcpYn + '/' + schCode).then(response => {
        if (response.data.length === 1) {
          row.acctCd = response.data[0].acctCd; row.acctNm = response.data[0].acctNm
          vm.$http.get('/api/account/sub/' + response.data[0].deptCd + '/' + response.data[0].acctCd).then(rs => {
            const obj = rs.data
            if (obj.length === 1) { row.acctCdSub = obj[0].subAcctCd; row.acctNmSub = obj[0].subAcctNm; row.reqSubAcct = true }
            else if (obj.length === 0) { row.acctCdSub = ''; row.acctNmSub = ''; row.reqSubAcct = false }
            else { row.acctCdSub = ''; row.acctNmSub = ''; vm.openAccountSub(row, response.data[0].acctCd) }
            vm.loadBudget(row); vm.refresh()
          })
        } else {
          vm.$modal.open({
            component: Account, parent: vm, props: { deptCd: deptCd, slipTypeCd: vm.slipType, param: schCode }, width: 700,
            events: {
              close(o) {
                row.acctCd = o.acctCd; row.acctNm = o.acctNm
                vm.$http.get('/api/account/sub/' + deptCd + '/' + o.acctCd).then(r2 => {
                  if (r2.data.length === 1) { row.acctCdSub = r2.data[0].subAcctCd; row.acctNmSub = r2.data[0].subAcctNm }
                  else if (r2.data.length > 1) { vm.openAccountSub(row, o.acctCd) }
                  vm.loadBudget(row); vm.refresh()
                })
              }
            }
          })
        }
        vm.refresh()
      })
    },
    subAcctValueChange(row, schCode) {
      const vm = this
      if (!schCode) { row.acctCdSub = ''; row.acctNmSub = ''; this.refresh(); return }
      if (!row.acctCd) { this.$swal({ type: 'warning', text: '계정을 먼저 선택하기 바랍니다.' }); return }
      this.$http.get('/api/account/sub/' + row.deptCd + '/' + row.acctCd + '/' + schCode).then(r => {
        const obj = r.data
        if (obj.length === 1) { row.acctCdSub = obj[0].subAcctCd; row.acctNmSub = obj[0].subAcctNm }
        else { vm.openAccountSub(row, row.acctCd) }
        vm.loadBudget(row); vm.refresh()
      })
    },
    productValueChangeE2(row, schCode) {
      const vm = this
      if (!row.isExpenseFlag && row.expenseFlag !== 'Y') { this.$swal({ type: 'warning', text: '연구부서만 등록 가능합니다.' }); return }
      if (!schCode) { row.detailCd = ''; row.detailNm = ''; this.refresh(); return }
      this.$http.get('/api/slip/product/' + schCode).then(r => {
        if (r.data.length === 1) { row.detailCd = r.data[0].detailCd; row.detailNm = r.data[0].detailNm }
        else {
          vm.$modal.open({
            component: Product, parent: vm, props: { slipTypeCd: vm.slipType }, width: 700,
            events: { close(o) { row.detailCd = o.detailCd; row.detailNm = o.detailNm; vm.refresh() } }
          })
        }
        vm.refresh()
      })
    },
    // E2 검색버튼(다건 직접 팝업)
    openCctrE2(row) {
      const vm = this
      this.$modal.open({
        component: ErpCctr, parent: this, width: 700,
        events: {
          close(o) {
            row.deptCd = o.deptCd; row.deptNm = o.deptNm
            row.isExpenseFlag = (o.expenseFlag === 'Y')
            if (o.expenseFlag !== 'Y') { row.detailCd = ''; row.detailNm = '' }
            row.acctCd = ''; row.acctNm = ''; row.acctCdSub = ''; row.acctNmSub = ''
            for (let i = 1; i <= 15; i++) row['attribute' + i] = ''
            vm.refresh()
          }
        }
      })
    },
    openAccountE2(row) {
      const vm = this
      this.$modal.open({
        component: Account, parent: this, props: { deptCd: row.deptCd, slipTypeCd: vm.slipType }, width: 700,
        events: {
          close(o) {
            row.acctCd = o.acctCd; row.acctNm = o.acctNm; row.ctrlYn = o.ctrlYn
            vm.$http.get('/api/account/sub/' + row.deptCd + '/' + o.acctCd).then(r => {
              if (r.data.length === 1) { row.acctCdSub = r.data[0].subAcctCd; row.acctNmSub = r.data[0].subAcctNm; row.reqSubAcct = true }
              else if (r.data.length > 1) { row.reqSubAcct = true; vm.openAccountSub(row, o.acctCd) }
              else { row.acctCdSub = ''; row.acctNmSub = ''; row.reqSubAcct = false }
              vm.loadBudget(row); vm.refresh()
            })
          }
        }
      })
    },
    openAccountSubBtn(row) {
      if (!row.acctCd) { this.$swal({ type: 'warning', text: '계정을 먼저 선택하기 바랍니다.' }); return }
      this.openAccountSub(row, row.acctCd)
    },
    openAccountSub(row, acctCd) {
      const vm = this
      this.$modal.open({
        component: AccountSub, parent: this,
        props: { deptCd: row.deptCd, slipTypeCd: this.slipType, acctCd: acctCd, searchStr: row.acctNmSub, year: this.$parent.value.postDt }, width: 700,
        events: { close(o) { row.acctCdSub = o.subAcctCd; row.acctNmSub = o.subAcctNm; vm.loadBudget(row); vm.refresh() } }
      })
    },
    openProductE2(row) {
      const vm = this
      if (!row.isExpenseFlag && row.expenseFlag !== 'Y') { this.$swal({ type: 'warning', text: '연구부서만 등록 가능합니다.' }); return }
      this.$modal.open({
        component: Product, parent: this, props: { slipTypeCd: vm.slipType }, width: 700,
        events: { close(o) { row.detailCd = o.detailCd; row.detailNm = o.detailNm; vm.refresh() } }
      })
    },
    openAddonE2(row) {
      const vm = this
      this.$modal.open({
        component: SlipMngItemPop, parent: this, props: { acctCd: row.acctCd, data: row },
        events: { close(data) { for (let i = 1; i <= 15; i++) { row['attribute' + i] = data['attribute' + i] } vm.refresh() } }
      })
    },
    recalcE2Totals() {
      const slip = this.$parent.value
      const topAmt = (slip.slipDetails || []).map(x => this.$numeral(x.useAmt).value()).reduce((a, v) => a + v, 0)
      slip.totAmt = topAmt
      if (slip.curCd === 'JPY') slip.totAmtKrw = Math.floor(topAmt * slip.excRt * 0.01)
      else slip.totAmtKrw = Math.floor(topAmt * slip.excRt)
    },
    // 잔여예산 조회 (DHTMLX rmdAmt 셀 컴포넌트 + queryRemainBudget 대체)
    budgetKey(row) {
      return [this.$parent.value.postDt, row.deptCd, row.acctCd, row.acctCdSub].join('_')
    },
    loadBudget(row) {
      if (!row || !row.deptCd) return
      const key = this.budgetKey(row)
      if (this.budget[key] !== undefined) { row.rmdAmt = this.budget[key]; return }
      if (this.budgetLock[key]) return
      this.budgetLock[key] = true
      this.$http.post('/api/budget/remain', { budYm: this.$parent.value.postDt, budCctrCd: row.deptCd, budAcctCd: row.acctCd, budSubAcctCd: row.acctCdSub })
        .then(r => { this.$set(this.budget, key, this.$numeral(r.data).value()); row.rmdAmt = this.budget[key]; this.refresh() })
        .finally(() => { delete this.budgetLock[key] })
    },
    loadBudgetsForData() {
      (this.data || []).forEach(row => this.loadBudget(row))
    },
    // ===== config_E1 (법인카드: 카드+교통 하이브리드) + config_E1_2 (현금지급) =====
    makeE1Cols() {
      const that = this
      const notLocked = (p) => !that.isLocked(p.data) && !(p.node && p.node.rowPinned)
      const editAmt = (p) => notLocked(p) && p.data && p.data.tpsTypeCd !== '10'
      const editDst = (p) => notLocked(p) && p.data && p.data.tpsTypeCd === '10'
      const lockStyle = (p) => that.isLocked(p.data) ? { backgroundColor: '#f5f5f5', color: '#999' } : null
      const cell = (a) => (p) => Object.assign({ textAlign: a || 'left' }, lockStyle(p) || {})
      const sel = (key) => ({ cellRenderer: 'select', editable: notLocked, cellRendererParams: { options: that.options[key] || [], detailCd: 'detailCd', detailNm: 'detailNm' } })
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowIndex + 1, width: 45, cellStyle: cell('center') },
        { headerName: '증빙일자', field: 'eaSlipDt', width: 100, cellStyle: cell('left'), valueFormatter: p => p.value ? that.$moment(p.value).format('YYYY-MM-DD') : '' },
        { headerName: '부서코드', field: 'deptCd', hide: true },
        { headerName: '부서', field: 'deptNm', width: 120, editable: notLocked, cellStyle: cell('left') },
        { headerName: '', field: 'cctrSrch', width: 30, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'cctrE1' } },
        { headerName: '항목', field: 'expenseName', width: 110, cellStyle: cell('left') },
        { headerName: '', field: 'expenseSrch', width: 20, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'expenseE1' } },
        { headerName: '계정', field: 'expenseAcctName', width: 110, cellStyle: cell('left') },
        { headerName: '제품', field: 'detailNm', width: 110, cellStyle: cell('left') },
        { headerName: '', field: 'prodSrch', width: 20, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'productE1' } },
        Object.assign({ headerName: '세금코드', field: 'vatCd', width: 110, cellStyle: cell('center') }, sel('E1_taxesCd')),
        { headerName: '공급가액', field: 'supAmt', width: 100, cellStyle: cell('right'), valueFormatter: that.fmtAmt },
        { headerName: '세액', field: 'vatAmt', width: 90, cellStyle: cell('right'), valueFormatter: that.fmtAmt },
        { headerName: '금액', field: 'useAmt', width: 100, cellStyle: cell('right'), valueFormatter: that.fmtAmt },
        { headerName: '적요', field: 'lnSgtxt', width: 180, editable: notLocked, cellStyle: cell('left') },
        { headerName: '가맹점명', field: 'crdMerchNm', width: 100, cellStyle: cell('center') },
        { headerName: '카드정보', field: 'crdInfo', width: 70, cellStyle: cell('center'), cellRenderer: 'crdInfoBtn' },
        { headerName: '스캔증빙', field: 'scanCt', width: 90, cellStyle: cell('center'), cellRenderer: 'scanCtCell' },
        { headerName: '출발지', field: 'stptPlc', width: 90, editable: notLocked, cellStyle: cell('left') },
        { headerName: '도착지', field: 'dstnPlc', width: 90, editable: notLocked, cellStyle: cell('left') },
        { headerName: '출장목적', field: 'biztrpObj', width: 100, editable: notLocked, cellStyle: cell('left') },
        Object.assign({ headerName: '교통비유형', field: 'tpsTypeCd', width: 100, cellStyle: cell('center') }, sel('TPS_TYPE_CD')),
        { headerName: '거리', field: 'tpsDst', width: 70, editable: editDst, cellStyle: cell('right'), valueFormatter: that.fmtAmt, valueParser: p => that.$numeral(p.newValue).value() },
        Object.assign({ headerName: '유종', field: 'oilKindCd', width: 90, cellStyle: cell('center') }, sel('OIL_KIND_CD')),
        { headerName: '유류단가/연비', field: 'oilUpc', width: 100, cellStyle: cell('center'), valueGetter: p => { const v = p.data || {}; return (v.oilUpc && v.oilEff) ? `${that.$numeral(v.oilUpc).format('0,0')} / ${that.$numeral(v.oilEff).format('0,0')}` : '' } },
        { headerName: '경비금액', field: 'acctAmt', width: 90, editable: editAmt, cellStyle: cell('right'), valueFormatter: that.fmtAmt, valueParser: p => that.$numeral(p.newValue).value() }
      ]
    },
    makeE1_2Cols() {
      const that = this
      const lockStyle = (p) => that.isLocked(p.data) ? { backgroundColor: '#f5f5f5', color: '#999' } : null
      const cell = (a) => (p) => Object.assign({ textAlign: a || 'left' }, lockStyle(p) || {})
      const ed = (p) => !that.isLocked(p.data)
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowIndex + 1, width: 45, cellStyle: cell('center') },
        { headerName: '증빙일자', field: 'eaSlipDt', width: 100, editable: ed, cellStyle: cell('left'), valueFormatter: p => p.value ? that.$moment(p.value).format('YYYY-MM-DD') : '' },
        { headerName: '부서코드', field: 'deptCd', hide: true },
        { headerName: '부서', field: 'deptNm', width: 120, editable: ed, cellStyle: cell('left') },
        { headerName: '', field: 'cctrSrch', width: 40, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'cctrE1_2', grid: 2 } },
        { headerName: '항목', field: 'expenseName2', width: 80, cellStyle: cell('center') },
        { headerName: '', field: 'acctSrch', width: 40, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'expenseE1_2', grid: 2 } },
        { headerName: '계정명', field: 'expenseAcctName2', width: 140, cellStyle: cell('left') },
        { headerName: '제품', field: 'detailNm2', width: 110, cellStyle: cell('left') },
        { headerName: '', field: 'prodSrch', width: 20, cellStyle: cell('center'), cellRenderer: 'searchBtn', cellRendererParams: { kind: 'productE1_2', grid: 2 } },
        { headerName: '금액', field: 'useAmt2', width: 100, editable: ed, cellStyle: cell('right'), valueFormatter: that.fmtAmt, valueParser: p => that.$numeral(p.newValue).value() },
        { headerName: '적요', field: 'lnSgtxt', width: 180, editable: ed, cellStyle: cell('left') },
        { headerName: '스캔증빙', field: 'scanCt', width: 90, cellStyle: cell('center'), cellRenderer: 'scanCtCell' },
        { headerName: '가맹점', field: 'storeNm', width: 150, editable: ed, cellStyle: cell('left') },
        { headerName: '주소', field: 'address', width: 150, editable: ed, cellStyle: cell('left') }
      ]
    },
    loadTaxes() {
      this.$http.get('/api/taxes', { params: { evdTypeCd: '10' } }).then(r => {
        this.options.E1_taxesCd = (r.data || []).filter(x => x.taxCd !== undefined).map(x => ({ detailCd: x.taxCd, detailNm: x.taxNm }))
      })
    },
    // 메인 검색버튼
    openCctrE1(row) {
      const vm = this
      this.$modal.open({
        component: ErpCctr, parent: this, width: 700,
        events: {
          close(o) {
            row.deptCd = o.deptCd; row.deptNm = o.deptNm
            if (o.expenseFlag !== row.expenseFlag) { row.expenseId = undefined; row.expenseAcctName = undefined; row.expenseName = undefined; row.expenseAcctCode = undefined; row.expenseFlag = undefined }
            vm.refresh()
          }
        }
      })
    },
    openExpenseE1(row) {
      const vm = this
      this.$modal.open({
        component: ErpAccount, parent: this, props: { deptCd: row.deptCd, slipTypeCd: vm.value.slipTypeCd }, width: 700,
        events: {
          close(o) {
            row.expenseId = o.expenseId; row.expenseAcctName = o.expenseAcctName; row.expenseName = o.expenseName; row.expenseAcctCode = o.expenseAcctCode; row.expenseFlag = o.expenseFlag
            vm.$http.get('/api/account/rcp/' + row.deptCd).then(r => { const d = (r.data || []).filter(x => x.acctCd === o.expenseAcctCode && x.rcpYn === 'Y')[0]; row.vatCd = d ? '10006' : '10004'; vm.refresh() })
          }
        }
      })
    },
    openProductE1Btn(row) {
      const vm = this
      this.$modal.open({
        component: Product, parent: this, props: { slipTypeCd: vm.value.slipTypeCd }, width: 700,
        events: { close(o) { row.detailCd = o.detailCd; row.detailNm = o.detailNm; vm.refresh() } }
      })
    },
    findExpenseAccount(row) { this.openExpenseE1(row) },
    findProductE1(row) { this.openProductE1Btn(row) },
    // 현금지급(E1_2) 검색버튼
    openCctrE1_2(row) {
      const vm = this
      this.$modal.open({
        component: ErpCctr, parent: this, width: 700,
        events: {
          close(o) {
            row.deptCd = o.deptCd; row.deptNm = o.deptNm
            if (o.expenseFlag !== row.expenseFlag2) { row.expenseId2 = undefined; row.expenseAcctName2 = undefined; row.expenseName2 = undefined; row.expenseAcctCode2 = undefined; row.expenseFlag2 = undefined }
            vm.refresh()
          }
        }
      })
    },
    openExpenseE1_2(row) {
      const vm = this
      this.$modal.open({
        component: ErpAccount, parent: this, props: { deptCd: row.deptCd, slipTypeCd: vm.value.slipTypeCd }, width: 700,
        events: { close(o) { row.expenseId2 = o.expenseId; row.expenseAcctName2 = o.expenseAcctName; row.expenseName2 = o.expenseName; row.expenseAcctCode2 = o.expenseAcctCode; row.expenseFlag2 = o.expenseFlag; vm.refresh() } }
      })
    },
    openProductE1_2Btn(row) {
      const vm = this
      this.$modal.open({
        component: Product, parent: this, props: { slipTypeCd: vm.value.slipTypeCd }, width: 700,
        events: { close(o) { row.detailCd2 = o.detailCd; row.detailNm2 = o.detailNm; vm.refresh() } }
      })
    },
    findExpenseAccount2(row) { this.openExpenseE1_2(row) },
    findProductE1_2(row) { this.openProductE1_2Btn(row) },
    // 카드정보/스캔증빙 팝업
    openCrdInfo(crdNo, apprNo) {
      this.$modal.open({ component: CardInfoDetailPop, parent: this, props: { apprNo, crdNo }, width: 700 })
    },
    fetchScanCt(row) {
      return this.$http.get(`/api/evid/fileList/${row.eaSlipNo}`)
        .then(r => ((r.data || {}).aFiles || []).length).catch(() => undefined)
    },
    openEvidence(row) {
      const vm = this
      this.$modal.open({
        component: EvidAtchPop, parent: this,
        props: { docMngNo: row.eaSlipNo, readonly: this.status && this.status.readonly }, width: 1200,
        events: { close(value) { row.scanCt = (value || []).length; vm.refresh() } }
      })
    },
    // 유가 자동계산 (E1) / 합계
    computeOilE1(row) {
      if (!row || row.tpsTypeCd !== '10') return
      const ym = row.eaSlipDt ? this.$moment(row.eaSlipDt).format('YYYYMM') : null
      const list = ym ? (this.oilPrice[ym] || []) : []
      const found = list.filter(x => x.oilKindCd === row.oilKindCd)[0]
      if (found) {
        row.oilUpc = found.oilUpce; row.oilEff = found.oilEff
        const d = this.$numeral(row.tpsDst).value(), upc = this.$numeral(row.oilUpc).value(), eff = this.$numeral(row.oilEff).value()
        let amt = (eff > 0) ? Math.floor(d / eff * upc) : 0
        if (!isFinite(amt)) amt = 0
        row.useAmt = row.supAmt = row.acctAmt = amt; row.vatAmt = 0
      }
    },
    recalcE1Totals() {
      const slip = this.$parent.value
      const top = (slip.slipDetails || []).map(x => this.$numeral(x.useAmt).value()).reduce((a, v) => a + v, 0)
      const bot = (slip.slipDetails2 || []).map(x => this.$numeral(x.useAmt2).value()).reduce((a, v) => a + v, 0)
      slip.totAmt = top + bot
    },
    // 법인카드내역 불러오기
    buttenClickEventCrdList() {
      const vm = this
      this.$modal.open({
        component: SlipCrdLstModal, parent: this,
        props: { deptCd: this.value.baseDeptCd, slipTypeCd: this.value.slipTypeCd, copyUseDetails: this.data }, width: 1800,
        events: {
          close(object) {
            if (!object) return
            vm.$http.get('/api/account/rcp/' + vm.value.baseDeptCd).then(response => {
              const data1 = (response.data || []).filter(v => v.acctCd === object[0].expenseAcctCode && v.rcpYn === 'Y')[0]
              object.forEach((x, i) => {
                vm.$http.get('/api/tmp/documentMngNo').then(r => { x.eaSlipNo = r.data })
                vm.add_row({
                  dcCd: 'D', lnTypeCd: 'ITEM', useDt: vm.value.evdDt, tpsTypeCd: '10', oilKindCd: 'GSL', acctAmt: 0,
                  crdNo: x.cardNo, crdMerchBizNo: x.merchBizNo, crdMerchNm: x.merchNm, deptCd: vm.value.baseDeptCd, deptNm: vm.value.baseDeptNm,
                  eaSlipDt: vm.$moment(x.apprDate).format('YYYYMMDD'), supAmt: x.apprAmt, useAmt: x.purchTot, vatAmt: x.vat, useDtlsNo: x.useDtlsNo,
                  crdInfo: x.apprNo, apprNo: x.apprNo, eaSlipNo: x.eaSlipNo, detailCd: x.detailCd || '', detailNm: x.detailNm || '',
                  expenseId: x.expenseId, expenseName: x.expenseName, expenseAcctName: x.expenseAcctName, expenseAcctCode: x.expenseAcctCode,
                  lnSgtxt: x.crdOln, vatCd: data1 ? '10006' : '10004', stptPlc: x.stptPlcPop, dstnPlc: x.dstnPlcPop, biztrpObj: x.biztrpObjPop
                })
                if ((object.length - 1) === i) vm.recalcE1Totals()
              })
            })
          }
        }
      })
    },
    buttonClickEventAdd2() {
      const vm = this
      this.$http.get('/api/tmp/documentMngNo').then(r => {
        vm.add_row_sub({ dcCd: 'D', lnTypeCd: 'ITEM', eaSlipNo: r.data, readonly: false, scanCt: 0, eaSlipDt: vm.value.evdDt, useDt: vm.value.evdDt, deptCd: vm.value.baseDeptCd, deptNm: vm.value.baseDeptNm, detailCd2: '', detailNm2: '' })
      })
    },
    buttonClickEventRemove2() {
      const nodes = this.gridApi2 ? this.gridApi2.getSelectedNodes() : []
      if (!nodes.length) { this.$swal({ type: 'error', text: '삭제할 행을 선택하여주세요.' }); return }
      this.datad.splice(nodes[0].rowIndex, 1)
      this.recalcE1Totals()
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
