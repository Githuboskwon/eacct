<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{ title }}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="search">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save">저장</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group" style="width: 25%">
        <label class="control-label">PJT관리번호</label>
        <div class="search_con search-area">
          <input class="input" type="text" v-model="form.projectNm" @input="initPro" @keypress.enter="popProject">
          <button class="icon is-right" @click="popProject(true)"><i class="fas fa-search"></i>
          </button>
        </div>
      </div>
      <div class="form-group" style="width: 10%">
      </div>
      <div class="form-group" style="width: 25%">
        <label class="control-label">팀명</label>
        <div class="search_con search-area">
          <input class="input input-bg w30p_5r" type="text" v-model="form.deptCd" disabled>
          <div class="dp-ib w70p">
            <input class="input" type="text" v-model="form.deptNm" @input="initCctr" @keypress.enter="popCctr">
            <button class="icon is-right" @click="popCctr()"><i class="fas fa-search"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="form-group" style="width: 10%">
      </div>
      <div class="form-group" style="width: 25%">
        <label class="control-label">등록자</label>
        <div class="search_con search-area">
          <input class="input input-bg w30p_5r" type="text" v-model="form.empNo" disabled>
          <div class="dp-ib w70p">
            <input class="input" type="text" v-model="form.empNm" @input="initEmp" @keypress.enter="popEmp">
            <button class="icon is-right" @click="popEmp(true)"><i class="fas fa-search"></i>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- //검색조건 영역 -->


    <div class="table-area mt20">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">PJT등록정보 변경 내역</h3>
          </div>
        </div>

        <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height_350"
                         rowSelection="multiple"
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef"
                         @row-selected="onRowSelected"
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
import DatepickerCellRenderer from '@/components/agGrid/datepicker-cell-renderer'
import AgGridSearchBtn from '@/components/agGrid/AgGridSearchBtn';
import CctrPop from '@/components/Cctr_Ag.vue';
import EmpPop from '@/components/Emp_Ag.vue'
import ProjectPop from '@/components/ProjectPop.vue';
import { AgGridVue } from 'ag-grid-vue'

export default {
  compatConfig: { MODE: 2 },
  name: 'ProjectWrtChaLst',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, ProjectPop, EmpPop, CctrPop
  },
  data() {
    return {
      title:"PJT등록정보 변경",
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
        outputFormat: 'YYYY-MM-DD HH:mm:ss',
        dateFormat: '%Y-%m-%d'
      },
      form: {
        orgId:'',
        projectMngNo: '',
        addUserId:'',
        regNo:'',
        empNm:'',
        empNo:'',
        deptNm:'',
        deptCd:'',
        projectNm:'',
        projectCd:'',
        compCd:'',
        cctrNm:'',
      },
      frameworkComponents: {
        checkboxRenderer: CheckboxCellRenderer,
        selectRenderer: SelectCellRenderer,
        numberInputRenderer: NumberInputCellRenderer,
        searchButtonRenderer: AgGridSearchBtn,
        datePicker: DatepickerCellRenderer,
      },
      combos: [],
    }
  },
  created() {
    this.getCombos();
    // this.search();
  },
  methods: {
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    async getCombos() {
      let [compCd] = await Promise.all([this.$http.get(`/api/code/combo`, {params: {groupCd: "COMP_CD"}})]);
      this.combos['COMP_CD'] = compCd.data;
      this.createColumnDefs();
    },
    createColumnDefs(){
      const vm = this;

      this.columnDefs = [
        {
          headerName: ''
          , field:'regYn'
          , width : 80
          , cellStyle:{textAlign: 'center'}
          , editable: false
          , headerCheckboxSelection: true
          , checkboxSelection: true
        },
        {headerName: 'PJT 관리번호', field: 'projectManageNo', width: 150},
        {headerName: '차수', field: 'degree', width: 150},
        {
          headerName: 'PJT명',
          field: 'projectName',
          width: 300,
        },
        {
          headerName: 'TASK 명',
          field: 'taskName',
          width: 150,
        },
        {headerName: 'PJT 시작일',
          field: 'startDate',
          width: 150,
          cellRenderer: 'datePicker',
          headerClass: 'require-column',
          cellRendererParams: {
            config: {
              hideTime: true,
              format: "YYYY-MM-DD",
              outputFormat: "YYYY-MM-DD HH:mm:ss",
              dateFormat: '%Y-%m-%d'
            },
          },
          cellClass: () => {
            return 'bg-grey'
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        },
        {headerName: 'PJT 종료일',
          field: 'endDate',
          width: 150,
          cellRenderer: 'datePicker',
          headerClass: 'require-column',
          cellRendererParams: {
            config: {
              hideTime: true,
              format: "YYYY-MM-DD",
              outputFormat: "YYYY-MM-DD HH:mm:ss",
              dateFormat: '%Y-%m-%d'
            },
          },
          cellClass: () => {
            return 'bg-grey'
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        },
        {headerName: '총개월수', field: 'totalMonth', width: 150, headerClass: 'require-column',},
        {headerName: 'PJT등록자', field: 'addUserId', width: 150, headerClass: 'require-column',

        },
        {headerName: 'PJT등록자', field: 'addUserNameUse', width: 150, headerClass: 'require-column',
          cellRenderer: 'searchButtonRenderer',
          cellRendererParams: {
            funcNm: 'updateAddUserId',
            valFuncNm: 'updateAddUserIdVal',
          },
        },
      ]
    },
    search() {
      this.$store.commit('loading')
      this.$http.post('/api/pjt/registInfo/list', {
        projectCode : this.form.projectCd,
        bdeptCode : this.form.deptCd,
        orgId : this.$store.state.loginInfo.compCd,
        regNo : this.form.regNo,
        addUserId : this.form.empNo,
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
      let vm = this;
      // let list = this.data.filter(v => v.regYn)
      const list = this.gridApi.getSelectedRows();

      this.$store.commit('loading');
      this.$http.post('/api/pjt/registInfo/save', list)
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
          });

    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    openModal() {
      document.getElementById("open-moda").style.opacity = "1";
      document.getElementById("open-moda").style.pointerEvents = "auto";
    },
    closeModal() {
      document.getElementById("open-moda").style.opacity = "0";
      document.getElementById("open-moda").style.pointerEvents = "none";
    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.data[rowIdx].regYn = params.node.isSelected();
    },
    popCctr(){
      const vm  = this;

      this.$modal.open({
        width: 800,
        component: CctrPop,
        parent: this,
        props: {
          param: this.form.cctrNm
        },
        events: {
          close(data) {
            vm.receiveCctr(data)
          },
        }
      })
    },
    popEmp() {
      const vm = this

      this.$modal.open({
        component: EmpPop,
        props: {
          param: this.form.empNm
        },
        parent: this,
        events: {
          close(data) {
            vm.receiveEmp(data)
          }
        }
      })
    },
    popProject(){
      const vm  = this;

      this.$modal.open({
        width: 800,
        component: ProjectPop,
        parent: this,
        props: {
          param: this.form.projectNm
        },
        events: {
          close(data) {
            vm.receivePro(data)
            vm.$forceUpdate();
          },
        }
      })
    },
    updateAddUserId() {
      const vm = this;
      let rowId = this.rowId;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      this.$modal.open({
        component: EmpPop,
        parent: this,
        props: {
          param: vm.data[rowId].addUserId,
        },
        width: 700,
        events: {
          close(obj) {
            vm.data[rowId].addUserId = obj.empNo;
            vm.data[rowId].addUserNameUse = obj.empNm;
            rowNode.setDataValue('addUserId', obj.empNo);
            rowNode.setDataValue('addUserNameUse', obj.empNm);
          }
        }
      })
    },
    updateAddUserIdVal(pVal) {
      const vm = this;
      let rowId = this.rowId;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      if(pVal) {
        this.$http.get(`/api/pjt/registInfo/list/${pVal}`)
            .then(response => {
              if(response.data.length === 1) {
                vm.data[rowId].addUserId = response.data[0].empNo;
                vm.data[rowId].addUserNameUse = response.data[0].empNm;
                rowNode.setDataValue('addUserId', response.data[0].addUserId);
                rowNode.setDataValue('addUserNameUse', response.data[0].addUserNameUse);
              } else {
                this.$modal.open({
                  component: EmpPop,
                  parent: this,
                  props: {
                    param: pVal,
                  },
                  width: 700,
                  events: {
                    close(obj) {
                      vm.data[rowId].addUserId = obj.empNo;
                      vm.data[rowId].addUserNameUse = obj.empNm;
                      rowNode.setDataValue('addUserId', obj.empNo);
                      rowNode.setDataValue('addUserNameUse', obj.empNm);
                    }
                  }
                })
              }
            })
      } else {
        vm.data[rowId].addUserId = '';
        vm.data[rowId].addUserNameUse = '';
        rowNode.setDataValue('addUserId', '');
        rowNode.setDataValue('addUserNameUse', '');
      }
    },
    receiveCctr(data) {
      this.form.deptCd = data.deptCd;
      this.form.deptNm = data.deptNm;
    },
    receiveEmp(data) {
      this.form.empNo = data.empNo;
      this.form.empNm = data.empNm;
    },
    receivePro(data) {
      this.form.projectCd = data.projectCd;
      this.form.projectNm = data.projectNm;
    },
    initCctr(force) {
      if (force === true) this.form.deptNm = '';
      if (this.form.deptNm === '') {
        this.form.deptCd = '';
        this.form.deptNm = '';
      }},
    initEmp(force) {
      if (force === true) this.form.empNm = '';
      if (this.form.empNm === '') {
        this.form.empNo = '';
        this.form.empNm = '';
      }
    },
    initPro(force) {
      if (force === true) this.form.projectNm = '';
      if (this.form.projectNm === '') {
        this.form.projectCd = '';
        this.form.projectNm = '';
      }
    },
    cellValueChange(params){
      const vm = this;
      const rowNode = params.gridOptions.api.getRowNode(params.node.rowIndex);
    },
  },

}
</script>
