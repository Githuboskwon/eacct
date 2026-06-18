<template>
  <div class="inner-box">
      <div class="content-name">
        <h2 class="dp-ib">스케줄러 관리</h2>
        <div class="btn-type1 clearfix">
          <el-button size="large" type="primary" icon="el-icon-search" @click="search()">조회</el-button>
          <el-button size="large" type="success" icon="el-icon-check" @click="process()">처리</el-button>
          <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save()">저장</el-button>
        </div>
      </div>

      <!-- 검색영역 -->
      <div class="search-form">
        <div class="form-group">
          <label class="control-label" id="schedulerNm">스케줄러명</label>
          <div class="form-input">
            <input type="text" class="input" v-model="form.schedulerNm"/>
          </div>
        </div>
        <div class="form-group">
          <label class="control-label">사용여부</label>
            <div class="form-input">
              <select class="input" v-model="form.useYn">
                <option value=''>==선택==</option>
                <option value='Y'>사용</option>
                <option value='N'>미사용</option>
              </select>
            </div>
          </div>
      </div>

      <!-- //검색영역 -->

    <div class="table-area mt20">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">스케줄러 내역</h3>
          </div>
          <div class="btn-type2 clearfix">
            <el-button type="info" icon="el-icon-plus" @click="addRow">행추가</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="removeRow()">행삭제</el-button>
          </div>
        </div>

        <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height"
                         rowSelection="multiple" 
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef"
                         @grid-ready="onReady"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import _ from 'lodash';
import mixin from '@/mixin'
import mixinSlip from '@/mixin/slip'
import common from '@/mixin/common';
import DhxCalendar from "@/components/DhxCalendar.vue";
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'

import { AgGridVue } from 'ag-grid-vue'

export default {
  compatConfig: { MODE: 2 },
  name: 'schedulerMng',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue
  },
  data() {
    return {
      columnDefs : [],
      gridOptions : {
        statusBar: {
          statusPanels: [
            {
              statusPanel: 'agTotalAndFilteredRowCountComponent',
              align: 'left',
            },
          ]
        },
      },
      defaultColDef: {
        resizable: true,
        filter: false,
        sortable: false,
      },
      gridApi : null ,    //gridApi
      columnApi : null ,  //columApi
      data: [],
      config: {
        hideTime: true,
        format: "YYYY-MM",
        outputFormat: "YYYYMM",
        dateFormat: '%Y-%m'
      },
      form: {
        compCd: '',
        schedulerCd: '',
        schedulerNm: '',
        useYn: '',  //사용여부
        remark: '', // 비고
      },
      frameworkComponents: {
        checkboxRenderer: CheckboxCellRenderer,
      },
    }
  },
  created() {
    this.createColumnDefs();
    this.search();
  },
  methods: {
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    createColumnDefs(){
      const vm = this;

      this.columnDefs = [
        {
          headerName: 'No.',
          width: 80,
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          },
          editable:false
        },
        {
          headerName: '스케줄러코드',
          field: 'schedulerCd',
          width: 250,
          headerClass: 'require-column',
          cellStyle: {textAlign: 'left'},
          cellClass: function(params) {
            if(params.node.data.new) {
              return 'bg-lightpink';
            } else {
              return 'bg-grey';
            }
          },
          editable: function(params) {
            if(params.node.data.new) {
              return true;
            } else {
              return false;
            }
          },
        },
        {
          headerName: '스케줄러명',
          field: 'schedulerNm',
          width: 200,
          headerClass: 'require-column',
          editable: true,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '비고',
          field: 'remark',
          width: 520,
          editable: true,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '사용여부',
          field: 'useYn',
          width: 100,
          cellStyle: {textAlign: 'center'},
          cellRenderer: 'checkboxRenderer',
          cellRendererParams: {
            trueValue: "Y"    //true값  지정 defalut-"Y" ex) "Y", true, "1"
            , falseValue: "N"  //false값 지정 defalut-"N" ex) "N", false, "0"
          },
        },
        {
          headerName: '실행시간',
          field: 'procDtm',
          width: 180,
          cellClass: () => {
            return 'bg-grey'
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        },
        {
          headerName: '최종수정일',
          field: 'chgDtm',
          width: 140,
          cellClass: () => {
            return 'bg-grey'
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
          valueFormatter: (params) => {
            return vm.getDateFormat(params.value)
          }
        },
        {
          headerName: '수정자',
          field: 'chgNm',
          width: 100,
          cellClass: () => {
            return 'bg-grey'
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          }
        },
      ]
    },
    search() {
      this.$store.commit('loading')
      this.$http.post('/api/scheduler/list', {
        schedulerNm : this.form.schedulerNm,
        useYn : this.form.useYn
      })
      .then(response => {
        this.data = response.data;
        if(response.data.length == 0){
            this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
          }  
      })
      .finally(() => {
        this.$store.commit('finish')
      })
    },
    save() {
      if(!this.gridRequireCheck([this.$refs.grid])) return;
      for (var i = 0; i < this.data.length; i++) {
        for (var j = i + 1; j < this.data.length; j++) {
          
          if (this.data[i].schedulerCd === this.data[j].schedulerCd) {
            this.$swal({
              type: "error",
              text: "해당 스케줄러가 이미 존재합니다."
            });
            return false;
          }
        }
      }
      this.$store.commit('loading');
      this.$http.post('/api/scheduler/save', this.data)
        .then(() => {
          this.$swal({type: 'success', text: '저장되었습니다.'})
          this.search();
        })
        .catch(error => {
          this.$swal({
            type:'error', text: error.data.message,
          })
        })
        .finally(() => {
          this.$store.commit('finish');
        })
    },
    process() {

      let row = this.gridApi.getSelectedRows()[0];

      if(!row) {
        this.$swal({type:'error', text: '처리할 행을 선택해주세요.'})
        return false;
      }

      const isNew = row.new;
      const compCd = row.compCd;
      const schedulerCd = row.schedulerCd;

      if(isNew){
        this.$swal({type:'error', text: '스케줄러를 먼저 저장해주세요.'})
        return false;
      }

      this.$store.commit('loading')
      this.$http.post('/api/scheduler/execute', {compCd, schedulerCd})
        .then(()=> {
          this.$swal({type: 'info', text: '스케줄러가 실행되었습니다.'})
        })
        .catch((err) => {
          console.log(err);
        }).finally(() => {
        this.$store.commit('finish')
      })

    },
    addRow() {
      this.data.push({
        schedulerCd : '',
        schedulerNm: '',
        useYn: 'Y',
        remark: '',
        chgNm: '',
        chgDtm: '',
        compCd: this.$store.state.loginInfo.compCd,
        new: true,
      })
    },
    removeRow() {
      let row = this.gridApi.getSelectedRows()[0];
      if(!row) {
        this.$swal({type:'error', text: '삭제할 행을 선택해주세요.'})
        return false;
      }
      const rowIndex = this.gridApi.getSelectedNodes()[0].childIndex;
      const isNew = row.new;
      const compCd = row.compCd;
      const schedulerCd = row.schedulerCd;

      if(!isNew){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then((r) => {
          if(r.value) {
            this.$http.post('/api/scheduler/delete', {compCd, schedulerCd})
            .then(() => {
              this.data.splice(rowIndex, 1);
              this.$swal({type: 'info', text: '삭제되었습니다.'})
            })
            .catch((err) => {
              console.error(err);
            })
          }
        })
      } else {
        this.data.splice(rowIndex, 1);
      }
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
}
</script>
