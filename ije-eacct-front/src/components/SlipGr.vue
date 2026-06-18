<template>
<div class="inner-box">
  <div class="table-area">
    <div class="table-a">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">전표정보</h3>
        </div>
      </div>
      <table class="table">
        <tbody>
          <tr>
            <th>그룹전표번호</th>
            <td>{{ data.grSlipNo }}</td>
            <th>전표유형</th>
            <td>{{ data.slipTypeNm }}</td>
            <th>통화</th>
            <td>{{ data.currCode }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <div class="table-area">
    <div class="table-b">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">전표내역</h3>
        </div>
      </div>
      <ag-grid-vue ref="grid" class="ag-theme-alpine" style="width: 100%; height: 200px;"
                   :columnDefs="columnDefs"
                   :rowData="data.slipGroupDetails"
                   :gridOptions="gridOptions"
                   :defaultColDef="defaultColDef"/>
    </div>
  </div>
</div>
</template>

<script>
import { AgGridVue } from 'ag-grid-vue'

export default {
  compatConfig: { MODE: 2 },
  props: {
    value: {
      type: Object,
      required: false
    }
  },
  components: {
    AgGridVue
  },
  data() {
    return {
      data: {
        slipGroupDetails: []
      },
      gridOptions: {},
      defaultColDef: { resizable: true, sortable: false, filter: false },
      columnDefs: [
        { headerName: 'No.', width: 50, cellStyle: { textAlign: 'center' }, valueGetter: p => p.node.rowIndex + 1 },
        { headerName: '전표금액', field: 'totAmt', width: 150, cellStyle: { textAlign: 'right' }, valueFormatter: p => (p.value == null || p.value === '') ? '' : this.$numeral(p.value).format('0,0') },
        { headerName: '계정명', field: 'stnAcctNm', width: 180, cellStyle: { textAlign: 'left' } },
        { headerName: '부서', field: 'wrtDeptNm', width: 180, cellStyle: { textAlign: 'left' } },
        { headerName: '지급처', field: 'payCustNm', width: 180, cellStyle: { textAlign: 'center' } },
        { headerName: '전표내용', field: 'hdSgtxt', width: 250, cellStyle: { textAlign: 'left' } },
        { headerName: '가맹점명', field: 'merchNm', width: 180, cellStyle: { textAlign: 'left' } },
        { headerName: '업종명', field: 'mccName', width: 150, cellStyle: { textAlign: 'left' } },
        { headerName: '승인일자', field: 'apprDate', width: 110, cellStyle: { textAlign: 'center' }, valueFormatter: p => this.fmtDate8(p.value) },
        { headerName: '승인시각', field: 'apprTime', width: 110, cellStyle: { textAlign: 'center' }, valueFormatter: p => this.fmtTime6(p.value) }
      ]
    }
  },
  methods: {
    fmtDate8(v) {
      return (v !== undefined && v !== null && String(v).match(/^\d{8}/)) ? String(v).replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3') : ''
    },
    fmtTime6(v) {
      return (v !== undefined && v !== null && String(v).match(/^\d{6}/)) ? String(v).replace(/(\d{2})(\d{2})(\d{2})/, '$1:$2:$3') : ''
    }
  },
  watch: {
    'value': {
      immediate: false,
      deep: true,
      handler(value) {
        this.data = value
      }
    }
  }
}
</script>

<style lang="scss" scoped>
table tbody tr th {
    width: 100px;
}

table tbody tr td {
    width: 180px;
}
</style>
