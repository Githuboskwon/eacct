<template>
  <layout>
  <span slot="header">
    매출전표 금액수정
    <button class="btn-pop-close delete" aria-label="close" @click="$parent.close()"></button>
  </span>
    <div slot="content" class="inner-box">

      <div class="table-b">
        <div class="table-header">
          <div class="btn-wrap btn-type2 clearfix fl_right">
            <button class="btn-size btn-w-gray" @click="submit">
              <span class="btn-icon"><i class="fas fa-check"></i></span> 금액수정
            </button>
            <button class="btn-size btn-w-gray" @click="$parent.close()">
              <span class="btn-icon"><i class="fas fa-times"></i></span> 취소
            </button>
          </div>
        </div>

        <div class="grid-tbl-box">
          <ag-grid-vue
            ref="grid"
            style="width: 100%; height: 110px;"
            class="ag-theme-alpine"
            rowSelection="single"

            :columnDefs="columnDefs"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponents"
            :rowData="rowData"
            :gridOptions="gridOptions"
            :suppressRowClickSelection="false"
            :enableRangeSelection="false"
            @grid-ready="onReady"
          />
        </div>
      </div>


      <div class="table-area">
        <div class="table-b">
          <div class="table-a fixed-th">
            <div class="table-name">
              <h3 class="ico_table_name">금액수정</h3>
            </div>
            <table class="table">
              <colgroup>
                <col width="30%"><col width="20%"><col width="30%"><col width="20%">
              </colgroup>
              <tbody>
              <tr>
                <th>공급가액</th>
                <td>
                  <input class="input" type="text" v-model="form.oriSupplyAmt" readonly/>
                </td>

                <th>부가세액</th>
                <td>
                  <input class="input" type="text" v-model="form.oriTaxAmt" readonly/>
                </td>
              </tr>

              <tr>
                <th>수정공급가액</th>
                <td>
                  <input class="input" type="text" v-model="form.modiSupplyAmt" />
                </td>

                <th>수정부가세액</th>
                <td>
                  <input class="input" type="text" v-model="form.modiTaxAmt" />
                </td>
              </tr>

              <tr>
                <th>공급가액 차액(수정공급가액 - 공급가액)</th>
                <td>
                  <input class="input" type="text" v-model="form.subSupplyAmt" readonly/>
                </td>

                <th>부가세액 차액(수정부가세액 - 부가세액)</th>
                <td>
                  <input class="input" type="text" v-model="form.modiTaxAmt - rowData[0].taxAmount" readonly/>
                </td>
              </tr>
              </tbody>
            </table>
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


export default {
  props: {
    invoiceData: {
      type: Object,
      required: true
    },
  },
  components: {
    AgGridVue,
    Layout
  },
  data() {
    return {
      columnDefs: [],
      defaultColDef: {resizable: true, filter:false, sortable: false},
      frameworkComponents: {},
      gridOptions: {},
      rowData: [],
      modiSupplyAmount: 0,
      modiTaxAmount: 0,
      form: {
        oriSupplyAmt: 0,
        oriTaxAmt: 0,
        modiSupplyAmt: 0,
        modiTaxAmt: 0,
        subSupplyAmt: 0,
        subTaxAmt: 0
      },
    }
  },
  methods: {
    makeColDef() {
      const that = this
      this.columnDefs = [
        { headerName: '공급가액(원화)',
          field: 'supplyAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName: '부가세액(원화)',
          field: 'taxAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName: '합계(원화)',
          field: 'totalAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        {headerName: '거래처명', field: 'customerName', width: 200, cellStyle:{textAlign: 'left'}},
        {headerName: '전표번호', field: 'trxNumber', width: 185, cellStyle:{textAlign: 'left'}},
        {headerName: '세금코드', field: 'taxCode', width: 120},
        {headerName: 'GL 일자', field: 'glDate', width: 120},
        {headerName: '통화', field: 'currencyCode', width: 80},
        {headerName: '사업자번호', field: 'taxReference', width: 120, cellStyle:{textAlign: 'left'}},
        {headerName: '부서코드', field: 'createDeptCode', width: 150, hide: true},
        {headerName: '부서명', field: 'createDeptName', width: 150, cellStyle:{textAlign: 'left'}},
        {headerName: '작성자사번', field: 'createEmpNo', width: 110},
        {headerName: '작성자명', field: 'createEmpName', width: 100},
        {headerName: '세무증빙유형', field: 'taxEvidenceType', width: 150, hide: true},
        {headerName: '세무증빙유형', field: 'taxEvidenceTypeName', width: 180, cellStyle:{textAlign: 'left'}},
        {headerName: '적요', field: 'comments', width: 250, cellStyle:{textAlign: 'left'}},
        {headerName: '처리상태', field: 'dtiStatusText', width: 150, cellStyle:{textAlign: 'left'}},
        {headerName: '확정여부', field: 'etaxIssueIdYn', width: 100},
        {headerName: '확정여부', field: 'etaxIssueId', width: 150, hide: true},
        {headerName: '신고 단위', field: 'orgId', width: 100},
        {headerName: '통합거래처번호', field: 'integrationVendorNum', width: 150},
        {headerName: 'trxId', field: 'customerTrxId', width: 150, hide:true},
        {headerName: 'dtiStatus', field: 'dtiStatus', width: 150, hide:true},
        {headerName: 'direction', field: 'direction', width: 150, hide:true},
        {headerName: 'etaxIssueIdCnt', field: 'etaxIssueIdCnt', width: 150, hide:true},
      ]


      this.rowData.push({
        supplyAmount: this.invoiceData.supplyAmount,
        taxAmount: this.invoiceData.taxAmount,
        totalAmount: this.invoiceData.totalAmount,
        customerName: this.invoiceData.customerName,
        trxNumber: this.invoiceData.trxNumber,
        taxCode: this.invoiceData.taxCode,
        glDate: this.invoiceData.glDate,
        currencyCode: this.invoiceData.currencyCode,
        taxReference: this.invoiceData.taxReference,
        createDeptCode: this.invoiceData.createDeptCode,
        createDeptName: this.invoiceData.createDeptName,
        createEmpNo: this.invoiceData.createEmpNo,
        createEmpName: this.invoiceData.createEmpName,
        taxEvidenceType: this.invoiceData.taxEvidenceType,
        taxEvidenceTypeName: this.invoiceData.taxEvidenceTypeName,
        comments: this.invoiceData.comments,
        dtiStatusText: this.invoiceData.dtiStatusText,
        etaxIssueIdYn: this.invoiceData.etaxIssueIdYn,
        etaxIssueId: this.invoiceData.etaxIssueId,
        orgId: this.invoiceData.orgId,
        integrationVendorNum: this.invoiceData.integrationVendorNum,
      })

      this.form.oriSupplyAmt = this.invoiceData.supplyAmount ? that.getNumberFormat( this.invoiceData.supplyAmount) : 0
      this.form.oriTaxAmt = this.invoiceData.taxAmount ? that.getNumberFormat(this.invoiceData.taxAmount) : 0
    },
    submit(){
      if((this.form.modiSupplyAmt == '' || this.form.modiSupplyAmt == 0) &&
        (this.form.modiTaxAmt == '' || this.form.modiTaxAmt == 0)){
        console.log(this.form.modiSupplyAmt)
        console.log(this.form.modiTaxAmt)
        this.$swal({ type: 'warning', text: '수정 금액이 없습니다.' });
        return
      }

      this.$swal({
        type: 'question',
        text: '금액 수정 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          //발생대상 제외
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/modifyAmt`, {
            modifySupplyAmount: this.form.modiSupplyAmt ,
            modifyTaxAmount: this.form.modiTaxAmt ,
            customerTrxId: this.invoiceData.customerTrxId ,
          })
            .then(response => {
              this.$swal({ type: 'success', text: response.data.Message });
              if(response.data.Message == "금액 수정 성공"){
                this.$parent.close()
              }
            })
            .catch(response => {

            })
            .finally(() => {
              this.$store.commit('finish')
            })
        }
      })



    },
    onReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    getNumberFormat(value){
      if(value){
        if(typeof value === "string"){
          if(val.substr(0,1) !== '-') value = value.replace(/[^0-9]/g, "")
          if(val.substr(0,1) === '-') value = value.replace( /^\[-\]?\\d\*$/g, "")
        }
        return Math.floor(value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      }
    },
    changeSupply(value){
      //this.form.modiSupplyAmt = this.getNumberFormat(value)
      this.form.subSupplyAmt = this.getNumberFormat(value - this.rowData[0].supplyAmount)
    },
    changeTax(value){
      //this.form.modiTaxAmt = this.getNumberFormat(value)
      this.form.subTaxAmt = this.getNumberFormat(value - this.rowData[0].taxAmount)
    },
  },
  watch: {
    'form.modiSupplyAmt': {
      immediate: false,
      deep: true,
      handler(value) {
        this.changeSupply(value)
      }
    },
    'form.modiTaxAmt': {
      immediate: false,
      deep: true,
      handler(value) {
        this.changeTax(value)
      }
    }
  },
  created() {

  },
  beforeMount(){
    this.makeColDef()
  },
}
</script>

<style scoped>


.modal-card {
  width: 1200px;
}

</style>
