<template>
<layout>
  <span slot="header">
    {{ title }}
    <button class="btn-pop-close delete" aria-label="close" @click="closeModal()"></button>
  </span>
  <div slot="content">
    <div class="btn-wrap btn-type2 fl_right" :style="{display: readOnly ? 'none' : ''}">
      <button class="btn-size btn-w-gray" @click="buttonClickEventAdd()">
        <span class="btn-icon">
          <i class="fas fa-plus"></i>
        </span>
        행추가
      </button>
      <button class="btn-size btn-w-gray" @click="buttonClickEventRemove()">
        <span class="btn-icon">
          <i class="fas fa-trash-alt"></i>
        </span>
        행삭제
      </button>
      <button class="btn-size btn-w-gray" @click="buttenClickEventSave()">
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
                   @grid-ready="onGridReady"
                   @cell-editing-stopped="onCellEditingStopped" />
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
import assert from '@/libs/assert'

// 문서보기 링크 셀 렌더러 (그룹웨어 문서 열기) — DHTMLX component: 대체
const JiniLinkRenderer = Vue.extend({
  template: `<span style="color:#0065b3;cursor:pointer;" @click="goLink"><i class="far fa-file-alt"></i></span>`,
  methods: {
    goLink() {
      const v = this.params.data
      if (!v || v.jiniUrl === undefined) return
      if (v.jiniUrl.indexOf('appro.nsf') !== -1 || v.jiniUrl.indexOf('approcab') !== -1) {
        const mainUrl = 'https://jini.iljin.co.kr/ekp/service/openapi/IF_CUS_EAP_002_goView?APP_ID='
        let s = v.jiniUrl.substring(0, v.jiniUrl.indexOf('?'))
        s = s.substring(s.length - 32)
        window.open(mainUrl + s, '_blank')
      } else {
        window.open(v.jiniUrl, '_blank')
      }
    }
  }
})
//['docMngNo', 'value', 'readonly']
export default {
  props: {
    slipNo: {
      type: String,
      required: false
    }, readOnly: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  mixins: [],
  components: {
    Layout,
    AgGridVue
  },
  data() {
    return {
      title: '그룹웨어 문서',
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
      ]
    }
  },
  methods: {
    closeModal() {
      this.$emit('close', this.slipDetails)
      // this.$parent.close();
    },
    onGridReady(params) {
      this.gridApi = params.api
    },
    refresh() {
      if (this.gridApi) this.gridApi.refreshCells({ force: true })
    },
    // 문서번호 입력 → 문서보기 URL 도출 (DHTMLX onEditCell jiniId 대체)
    onCellEditingStopped(params) {
      if (params.colDef.field !== 'jiniId') return
      const row = params.data
      const nValue = row.jiniId
      if (!nValue || String(nValue).length < 1) {
        this.$swal({ type: 'warning', text: '문서번호를 입력해 주세요.' }).then(() => { row.jiniId = undefined; this.refresh() })
        return
      }
      if (String(nValue).indexOf('_AA')) {
        const mainUrl = 'https://jini.iljin.co.kr/ekp/service/openapi/IF_CUS_EAP_002_goView?APP_ID='
        row.jiniUrl = mainUrl + String(nValue).split('_')[1]
      } else {
        row.jiniUrl = nValue
      }
      this.refresh()
    },
    goSearch() {
      this.$store.commit('loading');
      this.$http.get(_url(`/api/jini/document/${this.slipNo}`))
            .then(response => {
              if(response.data) {
                this.slipDetails = response.data
                this.slipDetails.forEach(x => {
                  x.slipNo = x.documentId
                  delete x.documentId
                })
              }

            }).catch(response => {

            }).finally(() => {
              this.$store.commit('finish');
            })
    },
    buttonClickEventAdd() {
      this.add_row({
        dcCd: 'D',
        lnTypeCd: 'ITEM',
        slipNo: this.slipNo,
        jiniNm: undefined,
        jiniId: undefined,
        jiniUrl: undefined
      })

    },
    buttonClickEventRemove() {
      const nodes = this.gridApi ? this.gridApi.getSelectedNodes() : []
      if (!nodes.length) { this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' }); return }
      const index = nodes[0].rowIndex
      const rowdata = this.slipDetails[index]
      this.slipDetails.splice(index, 1)
      this.$store.commit('loading')
      this.$http.post(_url('/api/jini/document/delete'), [rowdata])
        .then(response => { if (response) this.$swal({ type: 'success', text: '삭제 되었습니다.' }) })
        .catch(() => { this.$swal({ type: 'warning', text: '그룹웨어 문서 삭제중 오류가 발생했습니다.' }); this.goSearch() })
        .finally(() => { this.$store.commit('finish') })
    },
    buttenClickEventSave(){
      try{
        let datas = this.slipDetails

        let deleteInit = false
        //if(datas.length == 0) throw '추가된 행이 존재하지 않습니다.'

        for(let i=0; i < datas.length; i++){
          datas[i].new = false
          let row = i + 1
          if(datas[i].jiniNm === undefined || datas[i].jiniNm === '') throw row+'행 문서명을 입력해주세요.'
          if(datas[i].jiniId === undefined || datas[i].jiniId === '') throw row+'행 문서번호를 입력해주세요.'
          if(datas[i].jiniNm.indexOf(',') > -1) throw row+'행 문서명에 "," 입력할 수 없습니다.'
          if(datas[i].jiniId.indexOf('http') > -1) throw row+'행 문서번호가 잘못되었습니다.'
        }

        if(datas.length == 0){
          this.slipDetails.push({
            dcCd: 'D',
            lnTypeCd: 'ITEM',
            slipNo: this.slipNo,
            jiniNm: '',
            jiniId: '',
            jiniUrl: undefined,
            new: true
          })

          deleteInit = true
        }

        this.$nextTick(() => {
          this.$store.commit('loading');
          this.$http.post(_url('/api/jini/document'), datas)
          .then(response => {
            datas.forEach((x, index) => {
              x.slipNo = response.data[index].documentId
            })
            this.$swal({ type: 'success', text: '저장되었습니다.' });
            // 기안서 연동 카운트 추가
            if(deleteInit){
              this.$store.commit('setJiniSize', 0)
            }else{
              this.$store.commit('setJiniSize', datas.length)
            }
            this.$emit('close', datas)
          }).catch(response => {
            this.$swal({
              type: 'error',
              text: '그룹웨어 문서 전송에 실패하였습니다.'
            })
            return response
          }).finally(() => {
            this.$store.commit('finish');
          })
        }, 200)

      }catch(e){
        this.$swal({
          type: 'warning',
          text: e
        })
      }
    },
    add_row(data) {
      data = data || {}
      this.slipDetails.push({
          dcCd: 'D',
          lnTypeCd: 'ITEM',
          slipNo: this.slipNo,
          jiniNm: undefined,
          jiniId: undefined,
          jiniUrl: undefined,
          new: true
      })

    },
  },
  created() {
    if(this.$store.state.loginInfo.authorities[0].roleCd === 'ADMIN' || this.$store.state.loginInfo.authorities[0].roleCd === 'ACCOUNT'){
      this.readOnly = false;
    }
  },
  mounted() {
    this.goSearch()
  },
  watch: {
    slipDetail: {
      immediate: true,
      deep: true,
      handler(value) {
        if(value !== undefined){
          this.slipDetails = value
        }
      }
    }
  }

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
  ::v-deep img {
    width: 100%;
  }
}
</style>
