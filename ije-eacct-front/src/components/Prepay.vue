<template>
<layout>
  <span slot="header">
    {{ title }} 조회
    <button class="btn-pop-close delete" aria-label="close" @click="$dismiss"></button>
  </span>
  <div slot="content">
    <div class="btn-type1">
      <button class="btn-size btn-gray" @click="apply()">
        <span class="btn-icon"><i class="far fa-window-restore"></i></span> 적용
      </button>
      <button class="btn-size btn-gray" @click="$dismiss" style="margin-left: 10px;">
        <span class="btn-icon"><i class="far fa-window-restore"></i></span> 닫기
      </button>
    </div>

    <!-- 검색영역 -->
    <div class="pop-content-search">
      <div class="field has-addons ">
        <div class="mr20 " style="width:20%">
          <span class="pop-c-name">- 전표금액</span>
        </div>
        <div class="control is-expanded" style="width:50%; margin-right: 15px;">
          <number-input class="input input-bg" :value="totAmt" disabled="disabled" />
        </div>

        <div class="mr20 " style="width:20%">
          <span class="pop-c-name">- 반제금액</span>
        </div>
        <div class="control is-expanded" style="width:50%">
          <number-input class="input " v-model="value.mrAmt" />
        </div>
      </div>
    </div>
    <!-- //검색영역 -->

    <!-- 그리드 영역 (ag-grid) -->
    <ag-grid-vue ref="grid" class="gridbox ag-theme-alpine" style="width:100%; height:300px;"
                 rowSelection="single"
                 :columnDefs="columnDefs"
                 :rowData="data"
                 :gridOptions="gridOptions"
                 :defaultColDef="defaultColDef"
                 @grid-ready="onGridReady"/>
    <!-- //그리드 영역 -->
  </div>
</layout>
</template>

<script>
import { url as _url } from '@/libs/join'

import Layout from '@/components/ModalSlot.vue'
import { AgGridVue } from 'ag-grid-vue'
import NumberInput from '@/components/NumberInput.vue'
import assert from '@/libs/assert'

export default {
  compatConfig: { MODE: 2 },
  props: {
    totAmt: {
      type: Number,
      required: true,
    },
    payCustCd: {
      type: String,
      required: true
    },
    title: {
      type: String,
      required: false,
      default: '선급금'
    }
  },
  components: {
    Layout,
    AgGridVue,
    NumberInput
  },
  data() {
    const fmtAmt = (p) => (p.value == null || p.value === '') ? '' : this.$numeral(p.value).format('0,0')
    const fmtDate = (p) => (p.value !== undefined && p.value !== null && String(p.value).match(/^\d{8}/)) ? String(p.value).replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3') : ''
    return {
      value: {
        mrAmt : 0
      },
      gridApi: null,
      gridOptions: {},
      defaultColDef: { resizable: true, sortable: false, filter: false },
      columnDefs: [
        { headerName: 'No.', width: 50, cellStyle: { textAlign: 'center' }, valueGetter: p => p.node.rowIndex + 1 },
        { headerName: '선급금전표번호', field: 'preInvoiceNum', width: 130, cellStyle: { textAlign: 'center' } },
        { headerName: '라인', field: 'hdRef7', width: 50, cellStyle: { textAlign: 'center' } },
        { headerName: '선급금전표일자', field: 'preInvoiceDt', width: 110, cellStyle: { textAlign: 'center' }, valueFormatter: fmtDate },
        { headerName: '선급금', field: 'preAmt', width: 120, cellStyle: { textAlign: 'right' }, valueFormatter: fmtAmt },
        { headerName: '선급금 잔액', field: 'preAmtRm', width: 120, cellStyle: { textAlign: 'right' }, valueFormatter: fmtAmt },
        { headerName: '적요', field: 'preDesc', flex: 1, minWidth: 180, cellStyle: { textAlign: 'left' } }
      ],
      data: []
    }
  },
  methods: {
    onGridReady(params) {
      this.gridApi = params.api
    },
    apply() {
      try {
        const nodes = this.gridApi ? this.gridApi.getSelectedNodes() : []
        if (!nodes.length) throw '선택된 행이 없습니다.'
        const row = nodes[0].data

        const totAmt = this.$numeral(this.totAmt).value()
        const mrAmt = row.mrAmt = this.value.mrAmt
        const preAmtRm = row.preAmtRm

        if (this.$numeral(mrAmt).value() > this.$numeral(preAmtRm).value()) {
          this.$swal({ type: 'warning', text: '반제금액이 선급금 잔액보다 큽니다.' })
        } else if (this.$numeral(mrAmt).value() > this.$numeral(totAmt).value()) {
          this.$swal({ type: 'warning', text: '반제금액이 전표금액 잔액보다 큽니다.' })
        } else {
          this.$emit('close', row)
        }
      } catch (e) {
        this.$swal({
          type: 'warning',
          text: e
        })
      }
    },
    $dismiss() {
      this.$emit('dismiss')
      this.$parent.close()
    },
    query() {
      this.$store.commit('loading')
      this.data = []
      this.$http.get(`/api/erp/expense/prepay/${this.payCustCd}`)
        .then(response => {
          // console.log(response)
          this.data = response.data
          if (this.data.length === 1)
            this.$emit('close', this.data[0])
          return response
        })
        .catch(response => {
          return response
        })
        .finally(() => {
          this.$store.commit('finish')
        })
    }
  },
  created() {
    this.query()
  }
}
</script>
