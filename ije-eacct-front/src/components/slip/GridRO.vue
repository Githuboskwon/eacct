<template>
<div class="table-b">
  <div class="table-header">
    <div class="table-name">
      <h3 v-if="value.slipTypeCd === 'E1'" class="ico_table_name">법인카드지급</h3>
      <h3 v-else class="ico_table_name">세부항목</h3>
    </div>
  </div>

  <!-- ag-grid (read-only) -->
  <ag-grid-vue ref="grid" class="slip-grid ag-theme-alpine" style="width: 100%;" :style="{ height: gridHeight + 'px' }"
               :columnDefs="columnDefs"
               :rowData="data"
               :gridOptions="gridOptions"
               :defaultColDef="defaultColDef"
               :frameworkComponents="frameworkComponents"
               :pinnedBottomRowData="pinnedBottomRowData"
               @grid-ready="onGridReady"/>

  <div class="table-header" v-if="value.slipTypeCd === 'E1'">
    <div class="table-name">
      <h3 class="ico_table_name">현금지급</h3>
    </div>
    <ag-grid-vue ref="grid2" class="slip-grid ag-theme-alpine" style="width: 100%; height: 260px;"
                 :columnDefs="columnDefs2"
                 :rowData="datad"
                 :gridOptions="gridOptions2"
                 :defaultColDef="defaultColDef"
                 :frameworkComponents="frameworkComponents"
                 @grid-ready="onGridReady2"/>
  </div>
</div>
</template>

<script>
import Vue from 'vue'

import { AgGridVue } from 'ag-grid-vue'
import CardInfoDetailPop from '@/components/CardInfoDetailPop.vue'

// 카드정보 버튼 셀 렌더러 (DHTMLX component:{template:'<button @click=crdInfoPop>'} 대체)
const CrdInfoRenderer = Vue.extend({
  template: `<button type="button" class="icon far fa-file" @click="open"></button>`,
  methods: {
    open() {
      const v = this.params.data
      if (v && v.apprNo) this.params.context.parent.openCrdInfo(v.crdNo, v.apprNo)
    }
  }
})

// 스캔증빙 셀 렌더러 (count 표시 + 클릭 시 증빙첨부 팝업)
const ScanCtRenderer = Vue.extend({
  template: `<span style="color:#0065b3;cursor:pointer;" @click="open">{{ display }} <i class="far fa-file-alt"></i></span>`,
  data() {
    return { display: '0' }
  },
  created() {
    this.refresh()
  },
  watch: {
    'params': { deep: true, handler() { this.refresh() } }
  },
  methods: {
    refresh() {
      const v = this.params.data
      if (!v || !v.eaSlipNo) { this.display = this.$numeral(v && v.scanCt).format('0'); return }
      this.params.context.parent.fetchScanCt(v).then(cnt => {
        if (cnt !== undefined) { v.scanCt = cnt }
        this.display = this.$numeral(v.scanCt).format('0')
      })
    },
    open() {
      this.params.context.parent.openEvidence(this.params.node.rowIndex, this.params.data, this.params.colDef.__sub === true)
    }
  }
})

export default {
  compatConfig: { MODE: 2 },
  props: ['value', 'status'],
  components: {
    AgGridVue
  },
  data() {
    return {
      id: 'RO',
      data: [],
      datad: [],
      gridApi: null,
      gridApi2: null,
      options: {
        'OIL_KIND_CD': [],
        'TPS_TYPE_CD': []
      },
      objectPopup: [],
      objectPopupSub: [],
      attachRoles: [],
      gridOptions: { context: { parent: this } },
      gridOptions2: { context: { parent: this } },
      defaultColDef: { resizable: true, sortable: false, filter: false },
      frameworkComponents: { crdInfoBtn: CrdInfoRenderer, scanCtCell: ScanCtRenderer }
    }
  },
  computed: {
    gridHeight() {
      switch (this.value.slipTypeCd) {
        case 'E6': return 220
        case 'E1':
        case 'E2': return 260
        default: return 300
      }
    },
    columnDefs() {
      switch (this.value.slipTypeCd) {
        case 'E6': return this.makeE6Cols()
        case 'E1': return this.makeE1Cols()
        case 'E2': return this.makeE2Cols()
        case 'E5': return this.makeDefCols()
        default: return this.makeDefCols()
      }
    },
    columnDefs2() {
      return this.makeE1_2Cols()
    },
    // 합계 푸터 (DHTMLX attachHeader #stat_total 대체) — def/E5/E6 만
    pinnedBottomRowData() {
      const st = this.value.slipTypeCd
      if (st === 'E6') {
        return [{ itemSeq: '합계', acctAmt: this.sum('acctAmt') }]
      } else if (st === undefined || st === '' || st === 'E5' || ['E3', 'E4', 'E7'].indexOf(st) >= 0) {
        return [{ dcNm: '합계', debitAmt: this.sum('debitAmt'), creditAmt: this.sum('creditAmt') }]
      }
      return null
    }
  },
  created() {
    this.$watch(() => this.value.slipDetails, (nValue) => {
      this.data = (nValue !== undefined && Array.isArray(nValue)) ? this.value.slipDetails : []
      this.mapAttribute10()
    }, { immediate: true, deep: true })

    this.$watch(() => this.value.slipDetails2, (nValue) => {
      this.datad = (nValue !== undefined && Array.isArray(nValue)) ? this.value.slipDetails2 : []
    }, { immediate: true, deep: true })

    this.setAttachRoles()

    this.$http.get('/api/code/detail', { params: { groupCd: 'TPS_TYPE_CD' } })
      .then(response => { this.options['TPS_TYPE_CD'] = response.data })

    this.$http.get('/api/code/detail', { params: { groupCd: 'OIL_KIND_CD' } })
      .then(response => { this.options['OIL_KIND_CD'] = response.data })
  },
  methods: {
    onGridReady(params) { this.gridApi = params.api },
    onGridReady2(params) { this.gridApi2 = params.api },
    sum(field) {
      return this.data.reduce((a, r) => a + this.$numeral(r && r[field]).value(), 0)
    },
    // ===== 공통 포맷터 =====
    fmtAmt(p) {
      if (p.value == null || p.value === '') return ''
      return this.$numeral(p.value).format('0,0')
    },
    fmtAmt3(p) {
      if (p.value == null || p.value === '') return ''
      return this.$numeral(p.value).format('0,0.000')
    },
    fmtDate(p) {
      return p.value ? this.$moment(p.value).format('YYYY-MM-DD') : ''
    },
    L(align) { return { textAlign: align || 'left' } },
    // ===== columnDefs 빌더 (DHTMLX config.columns 매핑) =====
    makeDefCols() {
      const that = this
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowIndex + 1, width: 45, cellStyle: that.L('center') },
        { headerName: '유형', field: 'dcNm', width: 50, cellStyle: that.L('center') },
        { headerName: '세금코드', field: 'taxCd', width: 70, cellStyle: that.L('center') },
        { headerName: '비용부서', field: 'cctrCd', width: 100, cellStyle: that.L('center') },
        { headerName: '비용부서명', field: 'cctrNm', width: 120, cellStyle: that.L('left') },
        { headerName: '계정코드', field: 'acctCd', width: 80, cellStyle: that.L('center') },
        { headerName: '계정명', field: 'acctNm', width: 140, cellStyle: that.L('left') },
        { headerName: '보조계정코드', field: 'subAcctCd', width: 80, cellStyle: that.L('center') },
        { headerName: '보조계정명', field: 'subAcctNm', width: 140, cellStyle: that.L('left') },
        { headerName: '개발 프로젝트', field: 'productNm', width: 140, cellStyle: that.L('left') },
        { headerName: '차변금액', field: 'debitAmt', width: 100, cellStyle: that.L('right'), valueFormatter: that.fmtAmt },
        { headerName: '대변금액', field: 'creditAmt', width: 100, cellStyle: that.L('right'), valueFormatter: that.fmtAmt },
        { headerName: '적요', field: 'lnSgtxt', flex: 1, minWidth: 150, cellStyle: that.L('left') },
        { headerName: '선납부가세 증빙유형', field: 'attribute10', width: 90, cellStyle: that.L('center') }
      ]
    },
    makeE5Cols() { return this.makeDefCols() },
    makeE6Cols() {
      const that = this
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowIndex + 1, width: 45, cellStyle: that.L('center') },
        { headerName: '사용일자', field: 'useDt', width: 100, cellStyle: that.L('center'), valueFormatter: that.fmtDate },
        { headerName: '출발지', field: 'stptPlc', width: 80, cellStyle: that.L('left') },
        { headerName: '도착지', field: 'dstnPlc', width: 80, cellStyle: that.L('left') },
        { headerName: '출장목적', field: 'biztrpObj', width: 120, cellStyle: that.L('left') },
        { headerName: '교통비유형', field: 'tpsTypeNm', width: 100, cellStyle: that.L('center') },
        { headerName: '거리', field: 'tpsDst', width: 80, cellStyle: that.L('right'), valueFormatter: that.fmtAmt },
        {
          headerName: '유종', field: 'oilKindCd', width: 100, cellStyle: that.L('center'),
          valueGetter: p => {
            const r = (that.options['OIL_KIND_CD'] || []).filter(x => x.detailCd === (p.data && p.data.oilKindCd))[0]
            return r ? r.detailNm : ''
          }
        },
        {
          headerName: '유류단가/연비', field: 'oilUpc', width: 100, cellStyle: that.L('center'),
          valueGetter: p => {
            const v = p.data || {}
            return (v.oilUpc && v.oilEff) ? `${that.$numeral(v.oilUpc).format('0,0')} / ${that.$numeral(v.oilEff).format('0,0')}` : ''
          }
        },
        { headerName: '사용금액', field: 'acctAmt', width: 100, cellStyle: that.L('right'), valueFormatter: that.fmtAmt },
        { headerName: '계정코드', field: 'acctCd', width: 60, cellStyle: that.L('center') },
        { headerName: '계정명', field: 'acctNm', flex: 1, minWidth: 100, cellStyle: that.L('left') }
      ]
    },
    makeE2Cols() {
      const that = this
      const krw = this.$parent && this.$parent.value && this.$parent.value.curCd === 'KRW'
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowIndex + 1, width: 45, cellStyle: that.L('center') },
        { headerName: '비용부서코드', field: 'deptCd', hide: true },
        { headerName: '부서', field: 'deptNm', width: 100, cellStyle: that.L('left') },
        { headerName: '계정코드', field: 'acctCd', hide: true },
        { headerName: '계정명', field: 'acctNm', width: 140, cellStyle: that.L('left') },
        { headerName: '보조계정코드', field: 'acctCdSub', hide: true },
        { headerName: '보조계정명', field: 'acctNmSub', width: 140, cellStyle: that.L('left') },
        { headerName: '개발 프로젝트', field: 'detailNm', width: 140, cellStyle: that.L('center') },
        { headerName: '금액', field: 'useAmt', width: 110, cellStyle: that.L('right'), valueFormatter: krw ? that.fmtAmt : that.fmtAmt3 },
        { headerName: '적요', field: 'lnSgtxt', flex: 1, minWidth: 200, cellStyle: that.L('left') }
      ]
    },
    makeE1Cols() {
      const that = this
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowIndex + 1, width: 45, cellStyle: that.L('center') },
        { headerName: '회계일자', field: 'postDt', width: 100, cellStyle: that.L('left'), valueFormatter: that.fmtDate },
        { headerName: '부서', field: 'deptCd', hide: true },
        { headerName: '부서', field: 'deptNm', width: 120, cellStyle: that.L('left') },
        { headerName: '', field: 'expenseAcctCode', hide: true },
        { headerName: '계정명', field: 'expenseAcctName', width: 120, cellStyle: that.L('left') },
        { headerName: '보조계정명', field: 'expenseAcctNameSub', width: 180, cellStyle: that.L('left') },
        { headerName: '공급가액', field: 'supAmt', width: 100, cellStyle: that.L('right'), valueFormatter: that.fmtAmt },
        { headerName: '세액', field: 'vatAmt', width: 100, cellStyle: that.L('right'), valueFormatter: that.fmtAmt },
        { headerName: '금액', field: 'useAmt', width: 100, cellStyle: that.L('right'), valueFormatter: that.fmtAmt },
        { headerName: '가맹점명', field: 'crdMerchNm', width: 150, cellStyle: that.L('left') },
        { headerName: '적요', field: 'lnSgtxt', width: 150, cellStyle: that.L('left') },
        { headerName: '개발 프로젝트', field: 'detailNm', width: 120, cellStyle: that.L('left') },
        { headerName: '세금코드', field: 'vatNm', width: 120, cellStyle: that.L('center') },
        { headerName: '카드소유주', field: 'crdPssrNm', width: 120, cellStyle: that.L('center') },
        { headerName: '카드정보', field: 'crdInfo', width: 90, cellStyle: that.L('center'), cellRenderer: 'crdInfoBtn' },
        { headerName: '스캔증빙', field: 'scanCt', width: 100, cellStyle: that.L('center'), cellRenderer: 'scanCtCell' }
      ]
    },
    makeE1_2Cols() {
      const that = this
      return [
        { headerName: 'No.', valueGetter: p => p.node.rowIndex + 1, width: 45, cellStyle: that.L('center') },
        { headerName: '회계일자', field: 'eaSlipDt', width: 100, cellStyle: that.L('left'), valueFormatter: that.fmtDate },
        { headerName: '부서', field: 'deptCd', hide: true },
        { headerName: '부서', field: 'deptNm', width: 100, cellStyle: that.L('left') },
        { headerName: '계정명', field: 'expenseAcctName2', width: 120, cellStyle: that.L('left') },
        { headerName: '보조계정명', field: 'expenseAcctNameSub2', width: 180, cellStyle: that.L('left') },
        { headerName: '금액', field: 'useAmt2', width: 120, cellStyle: that.L('right'), valueFormatter: that.fmtAmt },
        { headerName: '통화', field: 'curCd', width: 80, cellStyle: that.L('center') },
        { headerName: '환율', field: 'excRt', width: 100, cellStyle: that.L('right'), valueFormatter: that.fmtAmt3 },
        { headerName: '원화금액', field: 'krwTotAmt', width: 130, cellStyle: that.L('right'), valueFormatter: that.fmtAmt },
        { headerName: '가맹점', field: 'evdCustNm', width: 200, cellStyle: that.L('left') },
        { headerName: '지급처', field: 'payCustNm', width: 200, cellStyle: that.L('left') },
        { headerName: '적요', field: 'lnSgtxt', width: 200, cellStyle: that.L('left') },
        { headerName: '개발 프로젝트', field: 'detailNm2', width: 120, cellStyle: that.L('left') },
        { headerName: '스캔증빙', field: 'scanCt', width: 100, cellStyle: that.L('center'), cellRenderer: 'scanCtCell', __sub: true }
      ]
    },
    // ===== 셀 렌더러가 호출하는 부모 메서드 =====
    openCrdInfo(crdNo, apprNo) {
      this.$modal.open({
        component: CardInfoDetailPop,
        parent: this,
        props: { apprNo, crdNo },
        width: 700
      })
    },
    fetchScanCt(row) {
      return this.$http.get(`/api/evid/fileList/${row.eaSlipNo}`)
        .then(response => ((response.data || {}).aFiles || []).length)
        .catch(() => undefined)
    },
    openEvidence(index, val, isSub) {
      const vm = this
      const popups = isSub ? this.objectPopupSub : this.objectPopup
      let rdoCtrl = this.status.readonly
      const memento = this.status.memento[0]
      const loginId = this.$store.state.loginInfo.loginId
      const roleCd = this.$store.state.loginInfo.authorities[0].roleCd

      if (memento.slipStatCd === '20' || memento.slipStatCd === '30' || memento.slipStatCd === '50') {
        if (memento.wrtId === loginId) rdoCtrl = false
        if (this.attachRoles.filter(x => x.detailCd === roleCd).length > 0) rdoCtrl = false
      }

      const url = '/evidAtchPopModeless?docMngNo=' + val.eaSlipNo + '&readonly=' + this.status.readonly + '&readonlyCtrl=' + rdoCtrl

      if (!(!popups[index] || popups[index].closed)) {
        popups[index].focus()
      } else {
        popups[index] = window.open(url, '_blank', 'toolbar=0,location=0,menubar=0,resizable=yes')
      }

      const agent = navigator.userAgent.toLowerCase()
      const onClose = () => {
        val.scanCt = localStorage.getItem("popFileCnt")
        vm.$forceUpdate()
        if (isSub ? vm.gridApi : vm.gridApi) {
          const api = isSub ? vm.gridApi2 : vm.gridApi
          if (api) api.refreshCells({ force: true })
        }
      }
      if ((navigator.appName === 'Netscape' && navigator.userAgent.search('Trident') !== -1) || (agent.indexOf("msie") !== -1)) {
        popups[index].attachEvent("onunload", onClose)
      } else {
        popups[index].onbeforeunload = onClose
      }
    },
    // DHTMLX else 분기의 attribute10(증빙유형) 매핑 재현 (def/E5 등)
    mapAttribute10() {
      const st = this.value.slipTypeCd
      if (st === 'E6' || st === 'E1' || st === 'E2') return
      this.data.forEach(x => {
        if (x.dcCd === 'D' && x.lnTypeCd === 'TAX') {
          if (x.attribute10 === '0') x.attribute10 = '기타'
          else if (x.attribute10 === '1') x.attribute10 = '종이'
          else if (x.attribute10 === '2') x.attribute10 = '전자'
          else x.attribute10 = (st === 'E4') ? '종이' : '전자'
        }
      })
    },
    setAttachRoles() {
      this.$store.commit('loading')
      this.$http.get('/api/code/detail', { params: { groupCd: "ATTACH_ROLE" } })
        .then(response => { this.attachRoles = response.data })
        .catch(response => { console.error("getAttchRole Error " + response.message) })
        .finally(() => { this.$store.commit('finish') })
    }
  }
}
</script>

<style lang="scss" scoped>
.slip-grid {
  margin-bottom: 8px;
}
/* 합계 푸터(pinned bottom) 강조 */
.slip-grid :global(.ag-row-pinned) {
  background-color: #f9f9f3;
  font-weight: bold;
}
.slip-grid :global(.ag-row-pinned .ag-cell) {
  color: #c00;
}
</style>
