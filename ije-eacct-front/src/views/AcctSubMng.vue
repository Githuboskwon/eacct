<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <button class="btn-size btn-gray fl_left" @click="btnSearchClick">
          <span class="btn-icon">
            <i class="fas fa-search"></i>
          </span> 조회
        </button>
        <!-- <button class="btn-size btn-blue fl_left" @click="save">
          <span class="btn-icon">
            <i class="fas fa-save"></i>
          </span> 저장
        </button> -->
      </div>
    </div>
    
    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
          <label class="control-label">부서 코드/명</label>
            <div class="form-input">
              <input class="input Rt-M tal" type="text" id="dept_cd" v-model="params.deptCd" />
            </div>
      </div>
      <div class="form-group">
          <label class="control-label">계정 코드/명</label>
            <div class="form-input">
              <input class="input Rt-M tal" type="text" id="account_cd" v-model="params.acctCd" />
            </div>
      </div>
      <div class="form-group">
          <label class="control-label">보조계정 코드/명</label>
            <div class="form-input">
              <input class="input Rt-M tal" type="text" id="account_cd" v-model="params.subAcctCd" />
            </div>
      </div>               
      <div class="form-group">
          <label class="control-label">사용유무</label>
          <div class="form-input">
            <select id="bselect" class="select is-fullwidth" v-model="params.useYn">
              <option
                v-for="item in useYnList"
                :key="item.key"
                :value="item.key"
                v-text="item.value"
              />
            </select>
          </div>
      </div>
         
    </div>

    <!-- Grid영역 -->
    <div class="table-area mt20">
      <div class="table-b" style="margin-bottom:0 !important">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">보조계정 내역</h3>
          </div>
          <!-- <div class="btn-wrap btn-type2 clearfix">
            <button class="btn-size btn-w-gray" @click="buttonClickEventAddRow">
              <span class="btn-icon">
                <i class="fas fa-plus"></i>
              </span>행추가
            </button>
            <button class="btn-size btn-w-gray" @click="buttonClickEventRemoveRow">
              <span class="btn-icon">
                <i class="fas fa-trash-alt"></i>
              </span>행삭제
            </button>
          </div> -->
        </div>
      </div>
    </div>
    <div class="table-b">
        <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height" rowSelection="multiple"  
                          :columnDefs="columnDefs" 
                          :rowData="data" 
                          :gridOptions="gridOptions"
                          :frameworkComponents="frameworkComponents"
                          :defaultColDef="defaultColDef"

                          @grid-ready="onReady"
                          />
        </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import mitt from 'mitt';

import GridSelect from "@/components/grid/GridSelect.vue";
import mixin from "@/mixin/slip-common";
import mixinSlip from '@/mixin/slip';
import GridCheckbox from '@/components/grid/GridCheckbox.vue'
import _ from 'lodash'

import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn.vue";

import ErpAccountNew from '@/components/ErpAccount_Ag.vue';

import common from '@/mixin/common';

var eventBus = mitt();

const options = {};

export default {
  name: "acctSubMng",
  mixins: [mixin, mixinSlip,common],
  data() {
    return {
      title: "보조계정관리",
      compCds: [],
      deleteid: [],
      useYnList: [{'key': '', 'value' : '=전체='}, {'key': 'Y', 'value' : '사용'}, {'key': 'N', 'value' : '미사용'}],
      params: {
        deptCd: undefined,
        acctCd: undefined,
        subAcctCd: undefined,
        useYn: 'Y',  //사용여부
      },
      gridOptions: {},      
      defaultColDef: {         
          resizable: true,
          filter: true,
          sortable: true,
          editable: false
      },       
      gridApi : null ,    //gridAip
      columnApi : null ,  //columApi 
      columnDefs: [] ,    //comulum Defs
      frameworkComponents: {
        checkbox: CheckboxCellRenderer,
        select: SelectCellRenderer,
        schBtn : AgGridSearchBtn
      },       
      data: []
    };
  },
  methods: {
    //그리드 Ready
    onReady(params) {
      
      //그리드용 api 정의
      //this.gridApi = params.api;
      //this.columnApi = params.columnApi; //params대신 gridOptions 사용가능
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
      
      this.createColumnDefs();
    },
    //컬럼정의 함수
    createColumnDefs() { 
        const vm = this

        this.columnDefs = [
          {
              headerName: 'No.', 
              width: 80,
              valueFormatter: function(params) {               
                return params.node.rowIndex+1;
              },
              filter: false,
              sortable: false,
              editable: false
          },  
          {
            headerName: '부서코드'
          , width : 150
          , field: 'deptCd'
          , cellRenderer: 'schBtn'
          , cellRendererParams:{
            slipStatCd : '50',
            funcNm : 'acctPopup'
            }
          },
          {
            headerName: '부서명'
          , width : 235
          , field: 'deptNm'
          , cellClass : 'bg-grey'
          },         
          {
            headerName: '계정코드'
          , width : 150
          , field: 'acctCd'
          , cellRenderer: 'schBtn'
          , cellRendererParams:{
            slipStatCd : '50',
            funcNm : 'acctPopup'
            }
          },
          {
            headerName: '계정명칭'
          , width : 235
          , field: 'acctNm'
          , cellClass : 'bg-grey'
          }, 
          {
            headerName: '보조계정코드'
          , width : 150
          , field: 'subAcctCd'
          },
          {
            headerName: '보조계정명칭'
          , width : 235
          , field: 'subAcctNm'
          },          
          {
            headerName: '사용여부'
          , width : 95 
          , field: 'useYn'
          , cellRenderer: 'checkbox'
          , cellRendererParams: {
              disable: true
            }
          , cellStyle : {'justify-content': 'center','align-items': 'center', 'display': 'flex','height': '100%'}
          },         
          {
            headerName: '비고1'
          , width : 230
          , field: 'ref1'
          },
          {
            headerName: '비고2'
          , width : 230 
          , field: 'ref2'
          },
          {
            headerName: '비고3'
          , width : 215
          , field: 'ref3'
          }
        ];
    },       
    save() {
      //그리드 필수입력 체크          
      if(!this.gridRequireCheck([this.$refs.grid])) return;
     

      for(var i = 0;i<this.data.length;i++){
        for(var j = i+1; j<this.data.length;j++){
          if(this.data[i].subAcctCd === this.data[j].subAcctCd){
            this.$swal({type: 'warning', text: '해당 보조계정 코드가 이미 존재합니다. 중복코드 : ' + this.data[i].subAcctCd});
            return false;
          }
        }
      }

      this.$store.commit('loading')
      
      this.$http.post('/api/acct/save',  this.data)
      .then(response =>{
        this.$swal({ type: 'success', text: '저장되었습니다' });
        this.goOpen()
      }).finally(() => {
        this.$store.commit('finish')
      });

    },
    btnSearchClick(){
      if(!this.searchRequireCheck()) return;

      this.goOpen();
    },
    goOpen() {

      this.$store.commit('loading')
      this.$http
        .post(`/api/account/sub/list`, { 
          deptCd: this.params.deptCd,
          acctCd: this.params.acctCd,
          subAcctCd: this.params.subAcctCd,
          useYn: this.params.useYn
        })
        .then(response => {
          this.data = response.data;
        }).finally(() => {
        this.$store.commit('finish')
      });
    },
    buttonClickEventAddRow() {
      
      //Selected Index
      const row = this.gridApi.getSelectedNodes()     
      const rowIndx = row.length < 1 ? 0 : row[0].rowIndex + 1
      
      //Selected Row
      this.data.splice(rowIndx, 0, {
          compCd: this.$store.state.loginInfo.compCd
        , useYn: 'Y'
        , new: 'Y'
      })

    },
    buttonClickEventRemoveRow() {
      const row = this.gridApi.getSelectedNodes()
      
      if (row.length < 1) {
        this.$swal({
          type: 'error',
          text: '삭제할 행을 선택하여주세요.'
        })
      } else {
        const rowIndex = row[0].rowIndex
        const isNew = this.data[rowIndex].new
        const groupCd = this.data[rowIndex].groupCd

        this.data.splice(rowIndex, 1)

        if (!isNew) {
          this.$store.commit('loading')

          this.$http.delete('/api/account/sub', {
              params: row[0].data
          }).then(response => {
            this.$swal({ type: 'success', text: '삭제 되었습니다.' });
            this.goOpen()
            // Do nothing!
          }).finally(() => {
            this.$store.commit('finish')
          })
        }
      }
    },
    acctPopup(){
      const that = this;
      const rowNode = this.gridApi.getRowNode(this.rowIdx);
      
      this.$modal.open({
        component: ErpAccountNew,
        parent: this,
        props: {
          postSearch : true
        },
        width: 700,
        events: {
          close(object) {
            rowNode.setDataValue('acctCd', object.acctCd);  
            rowNode.setDataValue('acctNm', object.acctNm);
          }
        },
      })
    },        

  },
  mounted() {
    document.title = this.title + ' - IJEAS';
    this.goOpen();
  }
};
</script>

<style lang="scss" scoped>x
.tax-mng-grid {
  :global(.gridbox) {
    height: 50vh !important;
  }
}

.gridbox {
  height: 200px !important;
}
.gridbox .objbox {
  height: 350px !important;
}

.desc-content:after {
  clear: both;
  content: "";
  display: block;
}
.btn-wrap {
  margin-bottom: 10px;
}
.desc-content {
  border: 2px solid #9db6c9;
  background: #f9fafc;
  margin: 0 0 20px 0;
  border-radius: 4px;
  padding: 15px 2%;
  clear: both;
}
.desc-content .item {
  float: left;
}
.desc-content .item .desc-item {
  position: relative;
  padding-left: 82px;
  margin-bottom: 8px;
}
.desc-content .item .desc-item:last-child {
  margin-bottom: 0;
  height: 25px;
}
.desc-content .item .desc-item .tit {
  position: absolute;
  left: 0;
}
.desc-content .item .desc-item .label-tit {
  font-family: "NotoM";
  color: #222;
  font-size: 15px;
}
.desc-content .item.desc-left .desc-item {
  padding-left: 90px;
}
.desc-content .item.desc-left .desc-item .desc:after {
  clear: both;
  content: "";
  display: block;
}
.desc-content .item.desc-left .desc-item .desc .datepicker {
  float: left;
}
.desc-content .item.desc-left .desc-item .desc span.wave {
  float: left;
  padding: 0 6px;
}
.desc-content .item.desc-left .desc-item .td-s-thumb.search-area:after {
  clear: both;
  content: "";
  display: block;
}
.desc-content .item.desc-left .desc-item .td-s-thumb.search-area input,
.desc-content .item.desc-left .desc-item .td-s-thumb.search-area .ip-box {
  float: left;
}
.desc-content .item.desc-left .desc-item .desc.select select {
  width: 70%;
}
.desc-content .item.desc-left {
  width: 35%;
  padding-right: 40px;
}
.desc-content .item.desc-center {
  width: 35%;
  padding-right: 40px;
}
.desc-content .item.desc-right {
  width: 30%;
}
.search-area input {
  position: relative;
}
.search-area .icon {
  position: absolute;
  right: 8px;
  top: 1px;
  z-index: 100;
  cursor: pointer;
  font-size: 16px;
  color: #555;
}
.search-border .td-s-thumb {
  position: relative;
  display: inline-block;
}
.search-border .td-s-thumb.search-area > input,
.search-border
  .td-s-thumb.search-area
  > .ip-box
  .search-border
  .td-s-thumb.search-area
  > button {
  float: left;
}
.search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
  width: 60%;
  margin-left: 6px;
}
.remove_text {
  margin-left: 0;
}
.contents div.gridbox_material.gridbox .xhdr {
  border-bottom: 1px solid #dfdfdf;
}

@media (max-width: 1580px) {
  .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
    width: 50%;
  }
}
</style>