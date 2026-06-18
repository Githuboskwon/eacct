<template>
  <div class="inner-box">
    <form @submit.prevent="query">
      <div class="content-name">
        <h2 class="dp-ib">계정과목 관리</h2>
        <div class="btn-wrap btn-type1 clearfix">
          <button type="submit" class="btn-size btn-gray fl_left">
            <span class="btn-icon"><i class="fas fa-search"></i></span>
            조회
          </button>
          <button type="button" class="btn-size btn-blue fl_left" @click="buttonClickEventSave()">
            <span class="btn-icon"><i class="fas fa-save"></i></span>
            저장
          </button>
        </div>
      </div>

      <!-- 검색영역 -->
      <div class="search-form">
        <search-comp v-model="params" :options="{ type: '02', compCd: 'compCd', authorize: true }" />
        <div class="form-group">
          <label class="control-label">부서 코드/명</label>
          <div class="form-input">
            <input type="text" class="input" v-model="params.deptCd" />
          </div>
        </div>
        <div class="form-group">
          <label class="control-label">계정 코드/명</label>
          <div class="form-input">
            <input type="text" class="input" v-model="params.acctCd" />
          </div>
        </div>
        <dl class="search_acc">
          <table-check-box title="사용여부" v-model="params" field="useYn" :readonly="false" />
          <table-check-box title="ERP_사용여부" v-model="params" field="erpUseYn" :readonly="false" />
          <table-check-box title="EA_사용여부" v-model="params" field="eaUseYn" :readonly="false" />
          <table-check-box title="미지급" v-model="params" field="apYn" :readonly="false" />
          <table-check-box title="개인비용" v-model="params" field="crdYn" :readonly="false" />
          <table-check-box title="개인비용외" v-model="params" field="evdYn" :readonly="false" />
          <table-check-box title="세금계산서" v-model="params" field="bllYn" :readonly="false" />
          <table-check-box title="선급금" v-model="params" field="ppdYn" :readonly="false" />
          <table-check-box title="불공제" v-model="params" field="rcpYn" :readonly="false" />
          <table-check-box title="부가세" v-model="params" field="vatYn" :readonly="false" />
          <table-check-box title="통제계정" v-model="params" field="ctrlYn" :readonly="false" />
        </dl>
      </div>

      <!-- //검색영역 -->
    </form>

    <div class="table-area mt20">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">계정과목 내역</h3>
          </div>
          <div class="btn-wrap btn-type2 clearfix">
            <button class="btn-size btn-excel" @click="buttonClickEventToExcel()">엑셀 저장</button>
          </div>
        </div>

        <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height_350"
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponent"
                         :defaultColDef="defaultColDef"

                         @grid-ready="onReady"
                         @cell-value-changed="cellValueChange"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import _ from 'lodash';
import assert from '@/libs/assert'
import debug from '@/libs/debug'
import TableCheckBox from '@/components/slip/table/libs/TableCheckBox'

import { AgGridVue } from 'ag-grid-vue'
import AgGridCheckbox from "@/components/agGrid/AgGridCheckbox.vue"

const _debug = debug('AcctMng')

export default {
  compatConfig: { MODE: 2 },
  components: {
    TableCheckBox, AgGridVue
  },
  data() {
    return {
      columnDefs : [],
      gridOptions : {},
      frameworkComponent : {
        'checkBox' : AgGridCheckbox
      },
      defaultColDef: {
        resizable: true,
        filter: false,
        sortable: false,
      },
      gridApi : null ,    //gridAip
      columnApi : null ,  //columApi
      data: [],
      params: {
        compCd: undefined,
        deptCd: undefined, //부서 코드/명
        acctCd: undefined, //계정 코드/명
        useYn: 'Y',  //사용여부
        erpUseYn: 'Y',  //ERP_사용여부
        eaUseYn: 'Y',   //EA_사용여부
        apYn: undefined, //미지급
        crdYn: undefined, //개인비용
        evdYn: undefined, //개인비용외
        bllYn: undefined, //세금계산서
        ppdYn: undefined, //선급금
        rcpYn: undefined, //접대비
        vatYn: undefined, //부가세
        ctrlYn: undefined, //통제계정
      },
      memento: []
    }
  },
  methods: {
    //그리드 Ready
    onReady(params) {

      //그리드용 api 정의
      //this.gridApi = params.api;
      //this.columnApi = params.columnApi; //params대신 gridOptions 사용가능
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    query() {
      this.$store.commit('loading')
      return this.$http.post('/api/account/list', this.params)
          .then(response => {
            const st = (new Date()).valueOf()
            this.data = response.data.map(x => {
              const chgDtm = this.$moment(x.chgDtm)
              return {
                deptCd: x.deptCd,
                deptNm: x.deptNm,
                acctCd: x.acctCd,
                acctNm: x.acctNm,
                // acctShortNm: x.acctShortNm,
                acctGrCd: x.acctGrCd,
                acctGrNm: x.acctGrNm,
                erpUseYn: x.erpUseYn === 'Y' ? true : false,
                eaUseYn: x.eaUseYn === 'Y' ? true : false,
                crdYn: x.crdYn === 'Y' ? true : false,
                bllYn: x.bllYn === 'Y' ? true : false,
                evdYn: x.evdYn === 'Y' ? true : false,
                trYn: x.trYn === 'Y' ? true : false,
                ppdYn: x.ppdYn === 'Y' ? true : false,
                rcpYn: x.rcpYn === 'Y' ? true : false,
                apYn: x.apYn === 'Y' ? true : false,
                vatYn: x.vatYn === 'Y' ? true : false,
                ctrlYn: x.ctrlYn === 'Y' ? true : false,
                ppdOrderSeq: x.ppdOrderSeq,
                apOrderSeq: x.apOrderSeq,
                ref1: x.ref1,
                ref2: x.ref2,
                ref3: x.ref3,
                chgId: x.chgId,
                chgNm: x.chgNm || x.chgId,
                useYn: x.useYn,
                chgDtm: chgDtm.isValid() ? chgDtm.format('YYYY-MM-DD HH:mm:ss') : '',
                compCd: x.compCd,
                chg: false
              }
            })
          }).finally(() => {
            this.$store.commit('finish')
          })
    },
    buttonClickEventSave() {
      try {
        const data = this.data.filter(x => x.chg || x.new).map(x => {
          return {
            deptCd: x.deptCd,
            deptNm: x.deptNm,
            acctCd: x.acctCd,
            acctNm: x.acctNm,
            // acctShortNm: x.acctShortNm,
            acctGrCd: x.acctGrCd,
            acctGrNm: x.acctGrNm,
            erpUseYn: x.erpUseYn ? 'Y' : 'N',
            eaUseYn: x.eaUseYn ? 'Y' : 'N',
            crdYn: x.crdYn ? 'Y' : 'N',
            bllYn: x.bllYn ? 'Y' : 'N',
            evdYn: x.evdYn ? 'Y' : 'N',
            trYn: x.trYn ? 'Y' : 'N',
            ppdYn: x.ppdYn ? 'Y' : 'N',
            rcpYn: x.rcpYn ? 'Y' : 'N',
            apYn: x.apYn ? 'Y' : 'N',
            vatYn: x.vatYn ? 'Y' : 'N',
            ctrlYn: x.ctrlYn ? 'Y' : 'N',
            ppdOrderSeq: x.ppdOrderSeq,
            apOrderSeq: x.apOrderSeq,
            ref1: x.ref1,
            ref2: x.ref2,
            ref3: x.ref3,
            useYn: x.useYn,
            compCd: x.compCd
          }
        })
        assert.apply(this, [data.length > 0, '변경된 행이 없습니다.'])
        let emptyDeptCd = data.filter(x => !x.deptCd)
        assert.apply(this, [emptyDeptCd.length === 0, '부서코드는 빈 값일 수 없습니다.'])

        let emptyAcctCd = data.filter(x => !x.acctCd)
        assert.apply(this, [emptyAcctCd.length === 0, '계정코드는 빈 값일 수 없습니다.'])

        let emptyAcctNm = data.filter(x => !x.acctNm)
        assert.apply(this, [emptyAcctNm.length === 0, '계정명칭은 빈 값일 수 없습니다.'])

        // let emptyAcctGrCd = data.filter(x => !x.acctGrCd)
        // assert.apply(this, [emptyAcctGrCd.length === 0, '계정그룹코드는 빈 값일 수 없습니다.'])

        this.$http.post('/api/account/save', data)
            .then(response => {
              this.$swal({
                type: 'success',
                text: '저장되었습니다.'
              })
            }).then(this.query)
      } catch (e) {
        this.$swal({
          type: 'error',
          text: e
        })
      }
    },
    buttonClickEventAddRow() {
      this.data.unshift({
        deptCd: undefined,
        deptNm: undefined,
        acctCd: undefined,
        acctNm: undefined,
        // acctShortNm: undefined,
        acctGrCd: undefined,
        acctGrNm: undefined,
        erpUseYn: true,
        eaUseYn: true,
        crdYn: true,
        bllYn: true,
        evdYn: true,
        trYn: true,
        ppdYn: true,
        rcpYn: true,
        apYn: true,
        vatYn: true,
        ctrlYn: true,
        ppdOrderSeq: undefined,
        apOrderSeq: undefined,
        ref1: undefined,
        ref2: undefined,
        ref3: undefined,
        chgId: undefined,
        chgDtm: undefined,
        useYn: 'Y',
        compCd: this.params.compCd,
        new: true,
        chg: false
      })
      this.$refs.grid.firstPage()
    },
    buttonClickEventRemoveRow() {
      const rId = this.$refs.grid.instance.getSelectedRowId()
      if (rId === null) {
        this.$swal({
          type: 'error',
          text: '삭제할 행을 선택하여주세요.'
        })
      } else {
        const rowIndex = this.$refs.grid.instance.getRowIndex(rId)
        const isNew = this.data[rowIndex].new
        const compCd = this.data[rowIndex].compCd
        const deptCd = this.data[rowIndex].deptCd
        const acctCd = this.data[rowIndex].acctCd

        this.data.splice(rowIndex, 1)

        if (!isNew) {
          this.$http.put('/api/account/delete', {
            compCd,
            deptCd,
            acctCd
          }).then(response => {
            // Do nothing!
          })
        }
      }
    },
    buttonClickEventToExcel() {
      var params = {
        fileName : "계정과목 내역"
      };
      this.gridApi.exportDataAsCsv(params);
    },
    makeColDef(){
      const that = this;

      this.columnDefs = [
        {
          headerName: 'No.',
          width: 60,
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          },
        },
        {
          headerName: '부서코드',
          field: 'deptCd',
          width: 85,
          cellStyle:()=>{
            return {textAlign: 'left', background:'#ededed'}
          }
        },
        {
          headerName: '부서명',
          field: 'deptNm',
          width: 200,
          cellStyle:()=>{
            return {textAlign: 'left', background:'#ededed'}
          }
        },
        {
          headerName: '계정코드',
          field: 'acctCd',
          width: 85,
          cellStyle:()=>{
            return {textAlign: 'left', background:'#ededed'}
          }
        },
        {
          headerName: '계정명칭',
          field: 'acctNm',
          width: 200,
          cellStyle:()=>{
            return {textAlign: 'left', background:'#ededed'}
          }
        },
        // {
        //     headerName: '계정약어명칭',
        //     field: 'acctShortNm',
        //     width: 200,
        //     cellStyle:()=>{
        //       return {textAlign: 'left', background:'#ededed'}
        //     }
        // },
        {
          headerName: '그룹코드',
          field: 'acctGrCd',
          width: 100,
          cellStyle:()=>{
            return {textAlign: 'left', background:'#ededed'}
          }
        },
        {
          headerName: '그룹코드명',
          field: 'acctGrNm',
          width: 100,
          cellStyle:()=>{
            return {textAlign: 'left', background:'#ededed'}
          }
        },
        {
          headerName: 'ERP_사용여부',
          field: 'erpUseYn',
          width: 120,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: 'EA_사용여부',
          field: 'eaUseYn',
          width: 110,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '개인비용',
          field: 'crdYn',
          width: 100,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '세금계산서',
          field: 'bllYn',
          width: 100,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '개인비용외',
          field: 'evdYn',
          width: 100,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '교통비',
          field: 'trYn',
          width: 75,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '선급금',
          field: 'ppdYn',
          width: 75,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '불공제',
          field: 'rcpYn',
          width: 75,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '미지급',
          field: 'apYn',
          width: 75,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '부가세',
          field: 'vatYn',
          width: 75,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '통제계정',
          field: 'ctrlYn',
          width: 90,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'checkBox'
        },
        {
          headerName: '선급금 계정 정렬순서',
          field: 'ppdOrderSeq',
          width: 200,
          cellStyle:{textAlign: 'center'},
          editable:true,
          valueFormatter:(params)=>{
            return that.chkNumber(params)
          }
        },
        {
          headerName: '미지급 계정 정렬순서',
          field: 'apOrderSeq',
          width: 200,
          cellStyle:{textAlign: 'center'},
          editable:true,
          valueFormatter:(params)=>{
            return that.chkNumber(params)
          }
        },
        {
          headerName: '비고1',
          field: 'ref1',
          width: 200,
          cellStyle:{textAlign: 'left'},
          editable:true
        },
        {
          headerName: '비고2',
          field: 'ref2',
          width: 200,
          cellStyle:{textAlign: 'left'},
          editable:true
        },
        {
          headerName: '비고3',
          field: 'ref3',
          width: 200,
          cellStyle:{textAlign: 'left'},
          editable:true
        },
        {
          headerName: '수정자',
          field: 'chgNm',
          width: 100,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '수정 일시',
          field: 'chgDtm',
          width: 180,
          cellStyle:{textAlign: 'left'}
        },
        {headerName:'변경사항', field:'chg', width:120, cellStyle:{textAlign:'left'}, hide:true},
      ]
    },
    cellValueChange(params){
      const rowNode = this.gridOptions.api.getRowNode(params.node.rowIndex);

      rowNode.setDataValue('chg', true);
    },
    chkNumber(params){

      var result = '';
      var val = params.value;

      if(!_.isEmpty(val) || _.isNumber(val)){
        val = val.toString();
        result = this.$numeral(val).format('0,0');
        this.data[params.node.rowIndex][params.column.colId] = this.$numeral(val).value();
      }

      return result
    }
  },
  beforeMount(){
    this.makeColDef();
  },
  created() {
    document.title = '계정과목관리 - IJEAS';
    try {
      this.params.compCd = this.$store.state.loginInfo.compCd
    } catch (e) {
      this.params.compCd = undefined
    }

    this.query()
  }
}
</script>
