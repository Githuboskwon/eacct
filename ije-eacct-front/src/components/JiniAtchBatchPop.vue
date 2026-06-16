<template>
<layout>
  <span slot="header">
    {{ title }}
    <button class="btn-pop-close delete" aria-label="close" @click="closeModal()"></button>
  </span>
  <div slot="content">
    <div class="btn-wrap btn-type2 fl_right" :style="{display: readonly ? 'none' : ''}">
      <button class="btn-size btn-w-gray" @click="addJiniRow()">
        <span class="btn-icon">
          <i class="fas fa-plus"></i>
        </span>
        행추가
      </button>
      <button class="btn-size btn-w-gray" @click="removeRow()">
        <span class="btn-icon">
          <i class="fas fa-trash-alt"></i>
        </span>
        행삭제
      </button>
      <button class="btn-size btn-w-gray" @click="goSubmit()">
        <span class="btn-icon">
          <i class="fas fa-save"></i>
        </span>
        저장
      </button>
    </div>
    <div class="table-b">
      <div class="table-header">
        <ag-grid-vue ref="grid" class="slip-grid ag-theme-alpine" style="width:100%; height:300px;"
                     rowSelection="single"
                     :columnDefs="columnDefs"
                     :rowData="slipDetails"
                     :gridOptions="gridOptions"
                     :defaultColDef="defaultColDef"
                     :frameworkComponents="frameworkComponents"
                     @grid-ready="onGridReady" />
      </div>
    </div>
  </div>
</layout>
</template>

<script>
import Vue from 'vue'
import _ from 'lodash'

import Layout from '@/components/ModalSlot.vue'
import { AgGridVue } from 'ag-grid-vue'
import Vendor from '@/components/Vendor_Ag.vue'
import Emp from '@/components/Emp_Ag.vue'
import assert from '@/libs/assert'

// 문서보기 링크 셀 렌더러 — DHTMLX component: 대체
const JiniLinkRenderer = Vue.extend({
  template: `<span style="color:#0065b3;cursor:pointer;" @click="goLink"><i class="far fa-file-alt"></i></span>`,
  methods: {
    goLink() {
      const v = this.params.data
      if (!v) return
      if (v.jiniUrl !== undefined) {
        window.open(v.jiniUrl, '_blank')
      } else if (v.jiniId) {
        const mainUrl = 'https://jini.iljin.co.kr/ekp/service/openapi/IF_CUS_EAP_002_goView?APP_ID='
        window.open(mainUrl + String(v.jiniId).split('_')[1], '_blank')
      }
    }
  }
})

//['docMngNo', 'value', 'readonly']
export default {
  props: {
    slipNo: {
      type: Array,
      required: true
    },
    readonly: {
      type: Boolean,
      required: false,
      default: false
    },
  },
  mixins: [],
  components: {
    Layout,
    AgGridVue
  },
  data() {
    return {
      title: '그룹웨어 일괄적용',
      slipDetails: [],
      gridApi: null,
      gridOptions: {},
      defaultColDef: { resizable: true, sortable: false, filter: false },
      frameworkComponents: { jiniLink: JiniLinkRenderer },
      columnDefs: [
        { headerName: 'No.', width: 50, cellStyle: { textAlign: 'center' }, valueGetter: p => p.node.rowIndex + 1 },
        { headerName: '문서명', field: 'jiniNm', flex: 1, minWidth: 200, editable: true, cellStyle: { textAlign: 'left' } },
        { headerName: '문서번호', field: 'jiniId', flex: 1, minWidth: 200, editable: true, cellStyle: { textAlign: 'left' } },
        { headerName: '문서보기', field: 'jiniUrl', width: 80, cellStyle: { textAlign: 'center' }, cellRenderer: 'jiniLink' }
      ],
      chkYn : false
    }
  },
  methods: {
    onGridReady(params) {
      this.gridApi = params.api
    },
    addJiniRow(){
      this.slipDetails.push({
        jiniId:'',
        jiniNm:'',
        jiniUrl:undefined,
      })
    },
    removeRow() {
      const nodes = this.gridApi ? this.gridApi.getSelectedNodes() : []
      if (!nodes.length) { this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' }); return }
      this.slipDetails.splice(nodes[0].rowIndex, 1)
    },
    closeModal() {
      this.$emit('close')
    },
    goSubmit() {
      let jiniList = this.slipDetails;
      if(jiniList.length === 0) {
        this.$swal({ type: 'info', text: '적용할 기안서를 추가해 주세요.' });
        return false;
      }

      for(let i=0; i < jiniList.length; i++){
        let row = i + 1
        if(jiniList[i].jiniNm === undefined || jiniList[i].jiniNm === '') throw row+'행 문서명을 입력해주세요.'
        if(jiniList[i].jiniId === undefined || jiniList[i].jiniId === '') throw row+'행 문서번호를 입력해주세요.'
        if(jiniList[i].jiniNm.indexOf(',') > -1) throw row+'행 문서명에 "," 입력할 수 없습니다.'
        if(jiniList[i].jiniId.indexOf('http') > -1) throw row+'행 문서번호가 잘못되었습니다.'
      }

      const that = this;
      this.$swal({
        type: 'question',
        text: '기안서 일괄 적용 하시겠습니까?',
        showCancelButton: true
      }).then(r => {
        if (r.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/jini/bundle`, {
            slipNoList : that.slipNo,
            jiniDtoList : jiniList,
          })
          .then((response) => {
            this.$swal({
              type: 'success',
              text: '기안서가 일괄 적용되었습니다.'
            })
            this.$emit('close', jiniList)
          })
          .finally(() => {
            this.$store.commit('finish')
          });
        }})
    },
  },
}

function _url(...args) {
  args = args.map(x => String(x || '').trim().replace(/^\/*|\/*$/g, ''))
  args = args.filter(x => x)
  return args.join('/')
}
</script>

<style lang="scss" scoped>
.modal-card {
  width: 1200px;
}

.display-area {
  :global(img) {
    width: 100%;
  }
}
</style>
