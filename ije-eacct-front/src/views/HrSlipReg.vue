<template>
<div class="inner-box">
  <form @submit.prevent="goSearch">
    <!-- 헤더 -->
    <div class="content-name">
      <h2 class="dp-ib">HR 인건비전표 관리</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch">조회</el-button>
      </div>
    </div>
    <!-- //헤더 -->

    <!-- 검색폼 -->
    <div class="search-form">
      <div class="form-group" style="width: 20%">
        <label class="control-label-req">지급년월</label>
<!--        <monthly-picker v-model="form.searchDtmFr" />-->
        <el-date-picker
            v-model="form.searchDtmFr"
            type="month"
            format="YYYY-MM"
            value-format="YYYYMM"
            style="width: 60%;">
        </el-date-picker>
      </div>
      <div class="form-group" style="width: 10%">
      </div>
      <div class="form-group" style="width: 40%">
        <label class="control-label">작업구분</label>
        <div class="form-input">
          <select class="input" v-model="form.batchSelect">
            <option value="" selected="selected">== 작업구분 선택 ==</option>
            <option
              v-for="item in payrollBatch"
              :value="item.payrollBatchId"
              v-text="item.payrollBatchName"/>
          </select>
        </div>
      </div>
      <div class="form-group" style="width: 10%">
      </div>
      <div class="form-group" style="width: 20%">
        <label class="control-label">검증여부</label>
        <div class="form-input">
          <select class="input" v-model="form.validationFlag">
            <option value="" selected="selected">전체</option>
            <option value="E" selected="selected">에러(E)</option>
            <option value="Y" selected="selected">성공(Y)</option>
          </select>
        </div>
      </div>
    </div>
    <!-- //검색폼 -->
  </form>

  <div class="table-area mt20">
    <div class="table-b">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">신청항목</h3>
        </div>
        <div class="btn-type1 clearfix">
          <el-button type="success" icon="el-icon-zoom-in" size="small" @click="validatePayroll">검증</el-button>
          <el-button type="primary" icon="el-icon-edit" size="small" @click="createPayroll">전표작성</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" @click="uploadDelete">업로드 삭제</el-button>
          <el-button type="success" icon="el-icon-upload" size="small" @click="excelUpload">엑셀 업로드</el-button>
          <el-button type="success" icon="el-icon-download" size="small" onclick="location.href='./documents/salary_upload_template.xlsx'">양식 download</el-button>
        </div>
      </div>


      <ag-grid-vue
        ref="grid"
        style="width: 100%;"
        class="ag-theme-alpine grid_search_height"
        rowSelection="single"
        :columnDefs="columnDefs"
        :defaultColDef="defaultColDef"
        :frameworkComponents="frameworkComponents"
        :rowData="payrollUpload"
        :gridOptions="gridOptions"
        :suppressRowClickSelection="true"
        :enableRangeSelection="false"
        :context="context"
        @grid-ready="onReady"
      />
    </div>
  </div>
</div>
</template>

<script>
/**
 * EA-03-03
 * 예산 신청 내역 - 개인
 */

import {AgGridVue} from 'ag-grid-vue'
import CheckboxCellRenderer from "@/components/agGrid/checkbox-cell-renderer";
import MonthlyPicker from "@/components/MonthlyPicker.vue";
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer";
import HrExcelUploadPop from "@/components/HrExcelUploadPop.vue"

export default {
  compatConfig: { MODE: 2 },
  name: 'HrSlipReg',
  components: {
    AgGridVue,
    MonthlyPicker,
    HrExcelUploadPop,
  },
  data() {
    return {
      form: {
        searchDtmFr: this.$moment().startOf('month').format('YYYYMM'),
        batchSelect: '',
        validationFlag: ''
      },
      columnDefs: [],
      defaultColDef: {
        resizable: true, filter:false, sortable: true
      },
      frameworkComponents: {
        select: SelectCellRenderer,
        checkboxRenderer: CheckboxCellRenderer,
      },
      context: {
        headerAllCheckEvent: this.allChk
      },
      gridOptions: {
        statusBar: {
          statusPanels: [
            {
              statusPanel: 'agTotalAndFilteredRowCountComponent',
              align: 'left',
            },
          ]
        },
      },
      payrollUpload: [],
      payrollBatch: [],
    }
  },
  methods: {
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    makeColDef(){
      const that = this
      this.columnDefs = [
        { headerName: 'No.',
          field: 'rn',
          width: 80,
          valueFormatter: function(params) {
            return params.node.rowIndex + 1;
          },
          cellStyle:{textAlign: 'center'}
        },
        {headerName: '검증여부', field: 'validationFlag', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName: '검증오류내역', field: 'errorMsg', width: 150, cellStyle:{textAlign: 'right'}},
        { headerName: '전표일자',
          field: 'slipDate',
          width: 120,
          valueFormatter: (params) => {
            return params.value === null ? '' : this.$moment(params.value).format('YYYY-MM-DD')
          },
          cellStyle:{textAlign: 'center'}
        },
        {headerName: '라인코드', field: 'payrollTypeCode', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName: '인건비유형', field: 'payrollTypeName', width: 130, cellStyle:{textAlign: 'left'}},
        {headerName: '업로드명', field: 'uploadTitle', width: 220, cellStyle:{textAlign: 'left'}},
        {headerName: '사번', field: 'retireEmpNo', width: 110, cellStyle:{textAlign: 'center'}},
        {headerName: '업로드 사번', field: 'uploadEmpNo', width: 110, cellStyle:{textAlign: 'center'}},
        {headerName: '귀속부서코드', field: 'coaSegment3', width: 130, cellStyle:{textAlign: 'center'}},
        {headerName: '계정과목코드', field: 'coaSegment4', width: 130, cellStyle:{textAlign: 'center'}},
        {headerName: '제품군', field: 'coaSegment5', width: 100, cellStyle:{textAlign: 'center'}},
        { headerName: '금액',
          field: 'payrollAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value)
          }
        },
        {headerName: '라인적요', field: 'lineDescription', width: 220, cellStyle:{textAlign: 'left'}},
        {headerName: '결재조건', field: 'termName', width: 200, cellStyle:{textAlign: 'left'}},
        {headerName: '계좌번호', field: 'bankAccountName', width: 150, cellStyle:{textAlign: 'right'}},
        { headerName: '결재예정일',
          field: 'dueDate',
          width: 130,
          valueFormatter: (params) => {
            return params.value === null ? '' : this.$moment(params.value).format('YYYY-MM-DD')
          },
          cellStyle:{textAlign: 'center'}
        },
        {headerName: '작성자사번', field: 'createdEmpNo', width: 110, cellStyle:{textAlign: 'center'}},
        { headerName: '작성일',
          field: 'creationDate',
          width: 110,
          valueFormatter: (params) => {
            return params.value === null ? '' : this.$moment(params.value).format('YYYY-MM-DD')
          },
          cellStyle:{textAlign: 'center'}
        },
        {headerName: '수정자사번', field: 'lastUpdatedEmpNo', width: 110, cellStyle:{textAlign: 'center'}},
        { headerName: '수정일',
          field: 'lastUpdatedDate',
          width: 110,
          valueFormatter: (params) => {
            return params.value === null ? '' : this.$moment(params.value).format('YYYY-MM-DD')
          },
          cellStyle:{textAlign: 'center'}
        },
      ]
    },
    monthChange(value){
      this.$store.commit('loading');
      this.$http
        .post("/api/slip/hr/getPayrollBatchList", {
          orgId: this.$store.state.loginInfo.compCd,
          batchPeriod: this.form.searchDtmFr,
          uploadEmpNo: ''
        })
        .then(response => {
          this.payrollBatch = response.data
          this.form.batchSelect = ''
          this.form.validationFlag = ''
        }).finally(() => {
        this.$store.commit('finish')
      });
    },
    allChk(){
      const grid = this.$refs.grid
      const chkDatas = grid.rowData.filter((x,i) => {
        return x.regYn === "Y"
      })

      if(chkDatas.length === this.payrollUpload.length){
        this.gridOptions.api.forEachNodeAfterFilter((v, i) => {
          v.data.regYn = "N"
        })
      }else{
        this.gridOptions.api.forEachNodeAfterFilter((v, i) => {
          v.data.regYn = "Y"
        })
      }

      this.gridOptions.api.refreshCells()
    },
    goSearch() {
      //validation check
      if(!this.form.batchSelect){
        this.$swal({ type: "error", text: "작업구분을 선택해주세요." })
        return
      }

      this.$store.commit('loading');
      this.$http
        .post("/api/slip/hr/getPayrollUploadList", {
          orgId: this.$store.state.loginInfo.compCd,
          uploadEmpNo: '',
          payrollBatchId: this.form.batchSelect,
          validationFlag: '%' + this.form.validationFlag + '%',
        })
        .then(response => {
           this.payrollUpload = response.data
        }).finally(() => {
        this.$store.commit('finish')
      });

    },
    createPayroll(){
      if(!this.form.batchSelect){
        this.$swal({ type: "error", text: "작업구분을 선택해주세요." })
        return
      }

      this.$store.commit('loading');
      this.$http.post("/api/slip/hr/createPayroll", {
        orgId: this.$store.state.loginInfo.compCd,
        payrollBatchId: this.form.batchSelect,
        uploadEmpNo: this.$store.state.loginInfo.loginId
      }).then(response => {
        this.$swal({type: 'success', html: response.data.Code + '<br/>' + response.data.Message});
      }).finally(() => {
        this.goSearch()
        this.$store.commit('finish')
      })
    },
    validatePayroll(){
      if(!this.form.batchSelect){
        this.$swal({ type: "error", text: "작업구분을 선택해주세요." })
        return
      }

      this.$store.commit('loading');
      this.$http.post("/api/slip/hr/validationPayroll", {
        orgId: this.$store.state.loginInfo.compCd,
        payrollBatchId: this.form.batchSelect,
        uploadEmpNo: this.$store.state.loginInfo.loginId
      }).then(response => {
        this.$swal({type: 'success', html: response.data.Code + '<br/>' + response.data.Message});
      }).finally(() => {
        this.goSearch()
        this.$store.commit('finish')
      })
    },
    uploadDelete(){
      const that = this

      if(!this.form.batchSelect){
        this.$swal({ type: "error", text: "작업구분을 선택해주세요." })
        return
      }

      this.$swal({
        type: 'warning',
        text: '삭제 하시겠습니까?',
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니요'
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading');
          this.$http.post("/api/slip/hr/deletePayroll", {
            orgId: this.$store.state.loginInfo.compCd,
            payrollBatchId: this.form.batchSelect,
            uploadEmpNo: this.$store.state.loginInfo.loginId
          }).then(response => {
            this.$swal({type: 'success', html: response.data.Code + '<br/>' + response.data.Message});
          }).finally(() => {
            this.$store.commit('finish')
            this.monthChange(that.form.searchDtmFr)
            this.initSelect()
          })
        }else{
          return
        }
      })

    },
    excelUpload(){
      var that = this
      this.$modal.open({
        component: HrExcelUploadPop,
        props: {
          periodYM: this.form.searchDtmFr
        },
        parent: this,
        width: 1200,
        events: {
          close() {
            that.monthChange(that.form.searchDtmFr)
            that.initSelect();
          }
        }
      })
    },
    initSelect(){
      this.form.batchSelect = ''
      this.form.validationFlag = ''
      this.payrollUpload = []
    },
    getNumberFormat(value){
      if(value){
        if(typeof value === "string"){
          if(val.substr(0,1) !== '-') value = value.replace(/[^0-9]/g, "")
          if(val.substr(0,1) === '-') value = value.replace( /^\[-\]?\\d\*$/g, "")
        }
        return Math.floor(value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      }
    }
  },
  watch: {
    'form.searchDtmFr': {
      immediate: true,
      deep: true,
      handler(value) {
        this.monthChange(value)
      }
    },
    'form.batchSelect': {
      immediate: true,
      deep: true,
      handler(value) {
        if(value)
          this.goSearch()
        else
          this.payrollUpload = []
      }
    }
  },
  created() {

  },
  beforeMount() {
    const statusBar = {
      statusPanels: [
        { statusPanel: 'agTotalAndFilteredRowCountComponent', align: 'left' },
      ],
    }
    this.gridOptions = {
      enableRangeSelection: true,
      statusBar
    }
  },
  mounted() {
    document.title = 'HR 인건비전표 관리 - IJEAS';
    this.makeColDef()
  }
}
</script>
