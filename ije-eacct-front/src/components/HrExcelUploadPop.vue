<template>
  <layout>
  <span slot="header">
    {{ title }}
    <button class="btn-pop-close delete" aria-label="close" @click="closeModal()"></button>
  </span>
    <div slot="content">
      <div class="table-area">
        <div class="table-b">
          <div class="table-header">
            <div style="width:700px;" class="fl_left">
              <div>
                <monthly-picker style="width:100px;" v-model="periodYM" />
                <span>업로드 작업구분명 : </span><input class="input" style="width:300px; background-color:#FFF5F5DC" type="text" v-model="uploadTitle">
              </div>
            </div>
            <div class="btn-wrap btn-type2 clearfix fl_right">
              <input type="file" ref="file" @change="fileChangeEvent()" accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" style="display: none;"/>
              <button class="btn-size btn-w-gray" @click="fileSearch()">
                <span class="btn-icon"><i class="fas fa-plus"></i></span>파일업로드
              </button>
              <button class="btn-size btn-w-gray" @click="clear()">
                <span class="btn-icon"><i class="fas fa-trash-alt"></i></span>초기화
              </button>
              <button class="btn-size btn-w-gray" @click="excelUpload()">
                <span class="btn-icon"><i class="fas fa-save"></i></span>업로드
              </button>
            </div>

          </div>


          <div>
            <ag-grid-vue
              ref="grid"
              style="width: 100%; height: 600px;"
              class="ag-theme-alpine"
              rowSelection="single"
              :columnDefs="columnDefs"
              :defaultColDef="defaultColDef"
              :frameworkComponents="frameworkComponents"
              :rowData="data"
              :gridOptions="gridOptions"
              :suppressRowClickSelection="true"
              :enableRangeSelection="false"
              @grid-ready="onReady"
            />
          </div>

        </div>
      </div>

    </div>
  </layout>
</template>

<script>
import _ from 'lodash'

import Layout from '@/components/ModalSlot.vue'
import {AgGridVue} from "ag-grid-vue";
import XLSX from 'xlsx';
import MonthlyPicker from "@/components/MonthlyPicker";

//['docMngNo', 'value', 'readonly']
export default {
  props: {
    periodYM: {
      type: String,
      required: false
    }
  },
  mixins: [],
  components: {
    AgGridVue,
    Layout,
    MonthlyPicker,
  },
  data() {
    return {
      title: 'HR 엑셀 업로드',
      columnDefs: [],
      defaultColDef: {
        resizable: true, filter:false, sortable: false
      },
      frameworkComponents: {},
      compCds: [],
      data: [],
      gridOptions: {},
      uploadTitle: '',
    }
  },
  methods: {
    closeModal() {
      this.$emit('close')
      //this.$parent.close();
    },
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;

      //this.gridApi.sizeColumnsToFit();
    },
    getCompCdCombo() {
      this.$http.get(`/api/code/detail`, {params: {groupCd: "COMP_CD"}})
        .then(response => {
          this.compCds = response.data;
        });
    },
    makeColDef() {
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
        {headerName : '전표일자', field: 'slipDate', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '인건비유형', field: 'payrollTypeName', width: 120, cellStyle:{textAlign: 'left'}},
        {headerName : '업로드명', field: 'uploadTitle', width: 200, cellStyle:{textAlign: 'left'}},
        {headerName : '사번', field: 'retireEmpNo', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '귀속부서코드', field: 'coaSegment3', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '계정과목코드', field: 'coaSegment4', width: 130, cellStyle:{textAlign: 'center'}},
        {headerName : '제품군코드', field: 'coaSegment5', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '라인코드', field: 'payrollTypeCode', width: 120, cellStyle:{textAlign: 'center'}},
        { headerName : '금액',
          field: 'payrollAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value)
          }
        },
        {headerName : '라인적요', field: 'lineDescription', width: 200, cellStyle:{textAlign: 'left'}},
        {headerName : '결재조건', field: 'termName', width: 150, cellStyle:{textAlign: 'left'}},
        {headerName : '계좌번호', field: 'bankAccountName', width: 150, cellStyle:{textAlign: 'left'}},
        {headerName : '결재예정일', field: 'dueDate', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '작성년월', field: 'batchPeriod', width: 100, hide: true},
        {headerName : '업로드작업구분명', field: 'payrollBatchName', width: 100, hide: true},
        {headerName : '회사', field: 'orgId', width: 100, hide: true},
        {headerName : '작성자', field: 'uploadEmpNo', width: 100, hide: true},
        {headerName : '', field: 'ledgerId', width: 100, hide: true},

      ]

    },
    clear(){
      this.data = []
      this.uploadTitle = ''
      this.$refs.file.value = null
    },
    fileSearch(event){
      this.$refs.file.click()
    },
    fileChangeEvent(event){
      if(!this.$refs.file.files[0]) return

      this.data = []
      const that = this
      const XLSX = require("xlsx")
      let files = this.$refs.file.files[0]

      let reader = new FileReader();

      reader.onload = function () {
        let fileData = reader.result;
        let wb = XLSX.read(fileData, {type: 'binary', cellDates: true});

        const toJson = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[1]], { header: 1 });

        that.importDataToGrid(toJson)
      };

      reader.readAsBinaryString(files);
    },
    importDataToGrid(json){
      const headers = json[2];
      const gridData = json.slice(3)

      let offset = new Date().getTimezoneOffset() * 60000 ; //대한민국의 offset 을 수동으로 추가

      for(var i=0; i<gridData.length; i++){
        if(gridData[i][0]){
          this.data.push({
            slipDate: gridData[i][0] === undefined ? '' : this.$moment(new Date(gridData[i][0] - offset)).format('YYYY-MM-DD'),
            payrollTypeName: gridData[i][1],
            uploadTitle: gridData[i][2],
            retireEmpNo: gridData[i][3],
            coaSegment3: gridData[i][4],
            coaSegment4: gridData[i][5],
            coaSegment5: gridData[i][6],
            payrollTypeCode: gridData[i][7],
            payrollAmount: gridData[i][8],
            lineDescription: gridData[i][9],
            termName: gridData[i][10],
            bankAccountName: gridData[i][11],
            dueDate: gridData[i][12] === undefined ? '' : this.$moment(new Date(gridData[i][12] - offset)).format('YYYY-MM-DD')
          })
        }
      }

    },
    excelUpload() {
      if(!this.uploadTitle){
        this.$swal({ type: "error", text: "업로드 작업구분명을 입력해주세요." })
        return
      }

      if(!this.periodYM){
        this.$swal({ type: "error", text: "지급년월을 선택해주세요." })
        return
      }

      if(this.data.length == 0){
        this.$swal({ type: "error", text: "업로드된 데이터가 없습니다." })
        return
      }

      for(var i=0; i<this.data.length; i++){
        this.data[i].batchPeriod = this.periodYM
        this.data[i].payrollBatchName = this.uploadTitle
        this.data[i].orgId = this.$store.state.loginInfo.compCd
        this.data[i].uploadEmpNo = this.$store.state.loginInfo.loginId
        this.data[i].ledgerId = this.compCds[0].remark2
      }

      this.$store.commit('loading');
      this.$http
        .post("/api/slip/hr/uploadPayroll", this.data)
        .then(response => {
          this.$swal({type: 'success', text: response.data.Message});
        }).finally(() => {
          this.$store.commit('finish')
          this.closeModal()
      });

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
  created() {
  },
  mounted() {
    this.makeColDef()
    this.getCompCdCombo()
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
</script>

<style lang="scss" scoped>

.modal-card {
  width: 1200px;
}

</style>
