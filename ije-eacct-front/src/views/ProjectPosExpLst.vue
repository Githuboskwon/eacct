<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="search">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save">저장</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
      </div>
    </div>
    <!-- //검색조건 영역 -->


    <div class="table-area mt20">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">PJT인건비관리 내역</h3>
          </div>
          <div class="btn-type2 clearfix">
            <el-button type="info" icon="el-icon-plus" @click="addRow">행추가</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteRow()">행삭제</el-button>
          </div>
        </div>

        <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height_350"
                         rowSelection="single"
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef"
                         @grid-ready="onReady"
                         @cell-value-changed="cellValueChange"
            />
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
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
import NumberInputCellRenderer from '@/components/agGrid/numberinput-cell-renderer'
import AgGridSearchBtn from '@/components/agGrid/AgGridSearchBtn';
import AccountPop from '@/components/AccountPop.vue';
import { AgGridVue } from 'ag-grid-vue'

export default {
  compatConfig: { MODE: 2 },
  name: 'ProjectPosExpLst',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, AccountPop
  },
  data() {
    return {
      title: "PJT인건비관리",
      columnDefs : [],
      gridOptions : {},
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
        format: 'YYYY-MM-DD',
        outputFormat: 'YYYYMMDD',
      },
      form: {
        orgId: '',
        projectMngNo:'',
        positionNm:'',
        positionCd: '',
        amt:'',
        amtType:'',
        acctCode:'',
        acctName:'' ,
        remark:'',
        addDate:this.$moment().format("YYYYMM"),
        addTime:this.$moment().format("HHMMSS"),
        changeDate:'',
        changeTime:'',
        changeUserId:'',
        addUserId:'',
        deptCd:this.$store.state.loginInfo.loginDeptCd,
      },
      frameworkComponents: {
        checkboxRenderer: CheckboxCellRenderer,
        selectRenderer: SelectCellRenderer,
        numberInputRenderer: NumberInputCellRenderer,
        searchButtonRenderer: AgGridSearchBtn,
      },
      combos: [],
    }
  },
  created() {
    this.getSpjCombos();
    this.search();
  },
  methods: {
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    async getSpjCombos() {
      let [spjAmtTypeDc] = await Promise.all([this.$http.get(`/api/code/combo`, {params: {groupCd: "SPJ_AMT_TYPE_CD"}})]);
      this.combos['SPJ_AMT_TYPE_CD'] = spjAmtTypeDc.data;
      let [spjEmCd] = await Promise.all([this.$http.get(`/api/code/combo`, {params: {groupCd: "SPJ_EM_CD"}})]);
      this.combos['SPJ_EM_CD'] = spjEmCd.data;
      this.createColumnDefs();
    },
    createColumnDefs(){
      const vm = this;

      this.columnDefs = [
        {
          headerName: '직책코드',
          field: 'positionCd',
          width: 150,
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
          cellRenderer: 'selectRenderer',
          cellRendererParams: {
            options : vm.combos['SPJ_EM_CD'],
            detailCd: 'key',
            detailNm: 'key'
          },
          cellStyle: {textAlign: 'center'},
        },
        {
          headerName: '직책명',
          field: 'positionCd',
          width: 220,
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
          cellRenderer: 'selectRenderer',
          cellRendererParams: {
            options : vm.combos['SPJ_EM_CD'],
            detailCd: 'key',
            detailNm: 'value'
          },
          cellStyle:()=>{
            return {textAlign: 'left'}
          },
        },
        {
          headerName: '인건비',
          field: 'amt',
          width: 150,
          cellRenderer: 'numberInputRenderer',
          callStyle:{textAlign: 'right'}
        },
        {
          headerName: '비용구분',
          field: 'amtType',
          width: 150,
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
          cellRenderer: 'selectRenderer',
          cellRendererParams: {
            options : vm.combos['SPJ_AMT_TYPE_CD'],
            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
            detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
          },
          cellStyle: {textAlign: 'center'},

        },
        {
          headerName: '계정코드',
          field: 'acctCode',
          width: 120,
          headerClass: 'require-column',
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        },
        {
          headerName: '계정명',
          field: 'acctName',
          headerClass: 'require-column',
          width: 160,
          cellRenderer: 'searchButtonRenderer',
          cellRendererParams: {
            funcNm: 'updateAcctName',
            valFuncNm: 'updateAcctNameVal'
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        },
        {
          headerName: '비고',
          field: 'remark',
          width: 300,
          editable: true,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '최종수정일',
          field: 'changeDate',
          width: 130,
          cellClass: function(){
            return 'bg-grey'
          },
          valueFormatter: (params) => {
            return vm.getDtmFormat(params.value)
          }

        },
        {
          headerName: '최종수정자',
          field: 'changeUserId',
          width: 130,
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        },
      ]
    },
    search() {
      this.$http.post('/api/pjt/laborCost/list', {
        orgID : this.$store.state.loginInfo.compCd,
        positionCd : this.form.positionCd,
        remark : this.form.remark,

      })
          .then(response => {
            this.data = response.data;
            if(response.data.length == 0){
              this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
            }
          })
    },
    save() {
      for (var i = 0; i < this.data.length; i++) {
        for (var j = i + 1; j < this.data.length; j++) {
          if (this.data[i].positionCd === this.data[j].positionCd ) {
            this.$swal({
              type: "error",
              text: "해당 직책코드가 이미 존재합니다."
            });
            return false;
          }
        }
      }

      this.$http.post('/api/pjt/laborCost/save', this.data).then(() => {
        this.$swal({type: 'success', text: '저장되었습니다' })
        this.search();
      }).catch(error => {
        this.$swal({
          type:'error', text: error.data.message,
        })
      })
          .finally(() => {
            this.$store.commit('finish');
          })
    },
    addRow() {
      this.data.push({
        orgId:this.$store.state.loginInfo.compCd,
        projectMngNo:'JET19821018',
        positionCd:'',
        positionNm:'',
        amt:'',
        remark:'',
        changeDate:this.$moment().format("YYYYMMDD"),
        changeTime:this.$moment().format("HHMMSS"),
        changeUserId:this.$store.state.loginInfo.loginId,
        amtType:'',
        acctCode:'',
        acctName:'',
        new:true,
      })
    },
    deleteRow() {

      let row = this.gridApi.getSelectedRows()[0];
      if(!row) {
        this.$swal({type:'error', text: '삭제할 행을 선택해주세요.'})
        return false;
      }
      const rowIndex = this.gridApi.getSelectedNodes()[0].childIndex;
      const isNew = row.new;
      const positionCd = row.positionCd;
      const orgId = row.orgId;
      const projectMngNo = row.projectMngNo;

      if(!isNew){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then((r) => {
          if(r.value) {
            this.$http.post('/api/pjt/laborCost/delete', {positionCd, orgId, projectMngNo})
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
    chkNumber(params){

      var result = '';
      var val = params.value;

      if(!_.isEmpty(val) || _.isNumber(val)){
        val = val.toString();
        result = this.$numeral(val).format('0,0');
        this.data[params.node.rowIndex][params.column.colId] = this.$numeral(val).value();
      }

      return result
    },
    openModal() {
      document.getElementById("open-modal").style.opacity = "1";
      document.getElementById("open-modal").style.pointerEvents = "auto";
    },
    closeModal() {
      document.getElementById("open-modal").style.opacity = "0";
      document.getElementById("open-modal").style.pointerEvents = "none";
    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD')
      }
    },
    updateAcctName() {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);

      this.$modal.open({
        component: AccountPop,
        parent: this,
        width: 700,
        props: {
          // param: vm.data[rowId].acctNm,
          postSearch : true,
          deptCd: this.form.deptCd
        },
        events: {
          close(obj) {
            vm.data[rowId].acctCode = obj.acctCd;
            vm.data[rowId].acctName = obj.acctNm;
            vm.data[rowId].amtType = obj.assetsTrackingFlag == 'Y' ? 'B' : 'A';
            rowNode.setDataValue('amtType', obj.assetsTrackingFlag == 'Y' ? 'B' : 'A');
            rowNode.setDataValue('acctCode', obj.acctCd);
            rowNode.setDataValue('acctName', obj.acctNm);
          }
        }
      })
    },
    updateAcctNameVal(pVal) {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);
      if(pVal) {
        this.$http.get(`/api/account/list/${pVal}`)
            .then(response => {
              if(response.data.length === 1) {
                vm.data[rowId].acctCode = response.data[0].acctCd;
                vm.data[rowId].acctName = response.data[0].acctNm;
                rowNode.setDataValue('acctCode', response.data[0].acctCd);
                rowNode.setDataValue('acctName', response.data[0].acctNm);
              } else {
                this.$modal.open({
                  component: AccountPop,
                  parent: this,
                  width: 700,
                  props: {
                    param: pVal,
                  },
                  events: {
                    close(obj) {
                      vm.data[rowId].acctCode = obj.acctCd;
                      vm.data[rowId].acctName = obj.acctNm;
                      rowNode.setDataValue('acctCode', obj.acctCd);
                      rowNode.setDataValue('acctName', obj.acctNm);
                    }
                  }
                })
              }
            })
      } else {
        vm.data[rowId].acctCode = '';
        vm.data[rowId].acctName = '';
        rowNode.setDataValue('acctCode', '');
      }
    },

    initAccount(force) {
      if (force === true) this.form.acctName = '';
      if (this.form.acctName === '') {
        this.form.acctCode = '';
        this.form.acctName = '';
      }
    },
    cellValueChange(params){
      const vm = this;
      const rowNode = params.api.getRowNode(params.node.rowIndex);
    },

  },
}
</script>
