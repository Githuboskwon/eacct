<template>
<div class="modal-card">
  <header class="modal-card-head pop-header">
    <p class="modal-card-title tit">
      권한별 사용자 내역
      <button class="btn-pop-close delete" aria-label="close" @click="$parent.close()"></button>
    </p>
  </header>
  <section class="modal-card-body pop-content">
    <div class="inner-box">
      <div class="content-name">
        <div class="btn-wrap btn-type1 clearfix" style="float: right; margin-top: -25px;">
          <button class="btn-size btn-blue fl_left" @click="buttonClickEventSave()">
            <span class="btn-icon"><i class="fas fa-save"></i></span>
            저장
          </button>
        </div>
      </div>

      <div class="table-area">
        <div class="table-b">
          <ag-grid-vue ref="grid"
                       style="width: 100%; height: 400px;"
                       class="ag-theme-alpine"
                       :columnDefs="columnDefs"
                       :rowData="data"
                       :gridOptions="gridOptions"
                       :defaultColDef="defaultColDef"
                       :frameworkComponents="frameworkComponents" />
        </div>
      </div>
    </div>
  </section>
</div>
</template>

<script>
import { AgGridVue } from 'ag-grid-vue'
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'

export default {
  props: {
    'roleCd': {
      type: String,
      required: true
    },
    'compCd': {
      type: String,
      required: true
    }
  },
  components: {
    AgGridVue
  },
  data() {
    return {
      data: [],
      columnDefs: [
        {headerName: 'No.', width: 60, cellStyle: {textAlign: 'center'}, valueGetter: params => params.node.rowIndex + 1},
        {
          headerName: '권한부여', field: 'roleChk', width: 90, cellStyle: {textAlign: 'center'},
          cellRenderer: 'checkboxRenderer',
          cellRendererParams: {trueValue: true, falseValue: false}
        },
        {headerName: '현재권한', field: 'crtRole', width: 110, cellStyle: {textAlign: 'center'}},
        {headerName: '사번', field: 'empNo', width: 120, cellStyle: {textAlign: 'center'}},
        {headerName: '성명', field: 'empNm', width: 120, cellStyle: {textAlign: 'center'}},
        {headerName: '직급', field: 'jobGradeNm', width: 110, cellStyle: {textAlign: 'center'}},
        {headerName: '부서', field: 'deptNm', width: 180, cellStyle: {textAlign: 'left'}},
      ],
      gridOptions: {},
      defaultColDef: {resizable: true, sortable: true, filter: true},
      frameworkComponents: {checkboxRenderer: CheckboxCellRenderer},
    }
  },
  methods: {
    query() {
      this.$store.commit('loading')
      this.$http.get('/api/auth/user', {
        params: {
          roleCd: this.roleCd,
          compCd: this.compCd
        }
      }).then(response => {
        this.data = response.data.map(x => {
          x.roleChk = x.roleChk === '1' ? true : false
          x.compCd = this.compCd
          return x
        })
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    buttonClickEventSave() {
      let data = _.cloneDeep(this.data).map(x => {
        delete x.children
        x.roleChk = x.roleChk ? '1' : '0'
        x.roleCd = this.roleCd
        x.compCd = this.compCd
        return x
      })
      this.$http.put(`/api/auth/user/${this.roleCd}/${this.compCd}`, data)
        .then(response => {
          this.$swal({ type: 'success', text: '저장되었습니다' });
          this.$emit('close')
        }).catch(response => {
          console.error(response)
        })
    }
  },
  created() {
    this.query()
  }
}
</script>
