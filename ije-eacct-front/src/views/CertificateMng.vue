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
        <button class="btn-size btn-blue fl_left" @click="save">
          <span class="btn-icon">
            <i class="fas fa-save"></i>
          </span> 저장
        </button>
      </div>
    </div>
    
    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
        <div class="search_title">
            <span class="search_tit">- 조회일자</span>
        </div>
        <div class="search_con">
            <div class="datepicker w-type-ymd02">
                <dhx-calendar class="input date sDate" v-model="form.searchDate" />
            </div>
        </div>        
      </div>
    </div>

    <!-- Grid영역 -->
    <div class="table-area mt20">
      <div class="table-b" style="margin-bottom:0 !important">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">공인인증서 관리</h3>
          </div>
          <div class="btn-wrap btn-type2 clearfix">
            <button id="editfunc" class="btn-size btn-w-gray" @click="addRow">
              <span id="add" class="btn-icon">
                <i class="fas fa-plus"></i>
              </span>행추가
            </button>
            <button class="btn-size btn-w-gray" @click="deleteRow">
              <span class="btn-icon">
                <i class="fas fa-trash-alt"></i>
              </span>행삭제
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="table-b">
        <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height" 
                          rowSelection="multiple" 
                          :columnDefs="columnDefs" 
                          :rowData="data" 
                          :gridOptions="gridOptions"
                          :frameworkComponents="frameworkComponents"
                          :defaultColDef="defaultColDef"
                          :suppressRowClickSelection="false"

                          @grid-ready="onReady"
                          @cell-clicked="onCellClicked"
                          />
        </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import createBus from '@/libs/eventBus';

import mixin from "@/mixin/slip-common";
import mixinSlip from '@/mixin/slip';

import _ from 'lodash'

import DhxCalendar from '@/components/DhxCalendar.vue'
import AgGridFileAttach from "@/components/agGrid/AgGridScanAttach.vue"
import DatepickerCellRenderer from '@/components/agGrid/datepicker-cell-renderer'

import common from '@/mixin/common';

import CertAtchPop from '@/components/CertAtchPop.vue'

var eventBus = createBus();

const options = {};

export default {
  name: "certificateMng",
  mixins: [mixin, mixinSlip, common],
  components: {DhxCalendar},
  data() {
    return {
      title: "공인인증서 관리",
      compCds: [],
      deleteList: [],
      form: {
        //searchDate: this.$moment().startOf('month').format('YYYY-MM-DD')
        searchDate: ""
      },
      gridOptions: {},
      frameworkComponents: {
          fileAttach : AgGridFileAttach,
          datePicker: DatepickerCellRenderer
      },             
      defaultColDef: {         
          resizable: true,
          filter: true,
          sortable: true,
          editable: true
      },       
      gridApi : null ,    //gridAip
      columnApi : null ,  //columApi 
      columnDefs: [] ,    //comulum Defs
      selected: [],
      data: [{}]
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
      
      setTimeout(() => {
        this.createColumnDefs();
      },500);      
    },
    //컬럼정의 함수
    createColumnDefs() { 
        const vm = this

        this.columnDefs = [
          {
              headerName: 'No.', 
              width: 80,
              valueFormatter: function(params) {               
                return params.node.rowIndex + 1;
              },
              editable: false
          },          
          {
            headerName: '유효기간(From)'
          , width : 120
          , field: 'effectStDt'          
          , headerClass: 'require-column' //필수입력 컬럼 css
          , cellRenderer: 'datePicker'
          },
          {
            headerName: '유효기간(To)'
          , width : 120
          , field: 'effectEndDt'          
          , headerClass: 'require-column' //필수입력 컬럼 css
          , cellRenderer: 'datePicker'
          },
          {
            headerName: '비밀번호'
          , width : 200
          , field: 'pwFake'          
          , headerClass: 'require-column' //필수입력 컬럼 css
          },
          {
            headerName: '비밀번호 확인'
          , width : 200
          , field: 'pwConfirmFake'          
          , headerClass: 'require-column' //필수입력 컬럼 css
          },
          {
              headerName: '첨부', 
              field: 'fileCnt', 
              width: 80,
              cellStyle:{textAlign: 'center'},
              cellRenderer: 'fileAttach',
          },          
          {
            headerName: '인증서명'
          , width : 430
          , field: 'certName'
          },
          {
            headerName: '비고'
          , width : 390
          , field: 'remark'
          }
        ];
    }, 
    addRow() {
      this.data.push({
        new: true,
        effectStDt: "",
        effectEndDt: "",
        fileCnt : 0
      });
    }, 
    deleteRow() {
      try{
        const row = this.gridApi.getSelectedNodes()

        if(row.length < 1)
          throw '선택된 행이 없습니다.'
        
        if (!row[0].data.new) {
          this.deleteList.push(row[0].data.fileId)
        }

        let index = row[0].rowIndex
        this.data.splice(index, 1)

      }catch (e) {
        this.$swal({
          type: 'warning',
          text: e
        })
      }
    },   
    save(callback) {

      //그리드 필수입력 체크          
      if(!this.gridRequireCheck([this.$refs.grid])) return;

      // 삭제할 것이 있음
      if (this.deleteList.length > 0) {
        this.$store.commit('loading')
        let work = []
        
        _.forEach(this.deleteList, fileId => {
          work.push(this.$http.delete('/api/cert/del', {
              params: {
                fileId
              }            
          }))
        })
        Promise.all(work)
          .then(response => {
            _proceed.apply(this, [callback])
          }).catch(response => {
            console.error('Failed to remove authority')
          })
        this.$store.commit('finish')  
      } else {
        _proceed.apply(this, [callback])
      }

      function _proceed(callback) {

        this.$store.commit('loading')
        
        let row = _.clone(this.data)
        
        let errIdx = "";

        row.forEach((x, index) => {
          x.effectStDt = this.$moment(x.effectStDt).format('YYYYMMDD')
          x.effectEndDt = this.$moment(x.effectEndDt).format('YYYYMMDD')

          if(x.pwFake != x.pwConfirmFake ) errIdx = index + 1         
        })
        
        if(errIdx){
          this.$swal({ type: 'info', text: errIdx + '번째 데이터 비밀번호 확인 바랍니다.' });
          this.$store.commit('finish')  
        }else{
          this.$http.post('/api/cert/save', row)
                    .then(response => {
                      if (callback !== undefined && typeof callback === 'function') {
                        callback.apply(this, [])
                      } else {
                        this.$swal({
                          type: 'success',
                          text: '저장되었습니다.'
                        }).then(this.goOpen)
                      }
                    }).finally(() => {
                      this.$store.commit('finish')
                    })
        }
      }
    },
    onCellClicked(event){
        
        const field = event.colDef.field

        if(field === "fileCnt"){
            
          const that = this;
          
          let i = this.gridOptions.api.getSelectedNodes()[0].childIndex
          let row = this.gridOptions.api.getSelectedRows()[0]

          if(!row.fileId){
            this.$swal({
              type: 'error',
              text: '인증서 저장후에 파일첨부 가능합니다.'
            })
          }else{
            this.$modal.open({
              component: CertAtchPop,
              props: {
                fileId: row.fileId,
                readonly: false, // Check readonly
                readonlyCtrl: false
              },
              parent: this,
              width: 1200,
              events: {
                close() {
                  that.goOpen();
                }
              }
            })
          }  
        }
    },
    btnSearchClick(){
      if(!this.searchRequireCheck()) return;

      this.goOpen();
    },
    goOpen() {

      let schDate = "";

      if(this.form.searchDate) schDate = this.form.searchDate

      this.$store.commit('loading');
      this.$http
        .get(`/api/cert/list/` + schDate)
        .then(response => {
          this.data = response.data;
          this.deleteList = [];
        }).finally(() => {
            this.$store.commit('finish');
        });
    },
  },
  created(){
    document.title = this.title + ' - IJEAS';
  },
  mounted() {
    this.goOpen();
  }
};
</script>

<style lang="scss" scoped>
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