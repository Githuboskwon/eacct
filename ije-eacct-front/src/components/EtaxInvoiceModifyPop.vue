<template>
  <layout>
    <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
    <div slot="content">

      <div class="grid-wrap">
          <ag-grid-vue 
                  style="width: 100%; height: 320px;"
                  class="ag-theme-alpine"    
                  rowSelection="single"
                  
                  :columnDefs="columnDefs"     
                  :gridOptions="gridOptions"
                  :rowData="rowData"
                  :defaultColDef="defaultColDef"
                  @rowDoubleClicked="rowDoubleClick"
                  @grid-ready="onGridReady"/>
      </div>
    </div>
  </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'
import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

export default {
  compatConfig: { MODE: 2 },
  name: 'CctrDeptRole',
  props: {
    amendCode: '',
    orgId: '',
    integrationVendorNum: '',
    currencyCode: '',
    taxCode: '',
  },
  mixins: [ mixin ],
  components: {
    Layout,
    AgGridVue
  },
  data() {
    return {
      //부서권한 조회
      title: '원본 세금계산서 조회',
      search: '',
      gridOptions: {},
      columnDefs:[],
      defaultColDef: { 
        resizable: true, 
        filter:true, 
        sortable: true 
      },
      rowData: []
    }
  },
  methods: {
    makeColDef(){
      const that = this
      this.columnDefs = [
        {headerName:'거래처명', field:'customerName', width:200, cellStyle:{textAlign:'left'} },
        {headerName:'부가세코드', field:'taxCode', width:120 },
        {headerName:'통화', field:'currencyCode', width:80, cellStyle:{textAlign:'center'} },
        { headerName:'공급가액',
          field:'supplyAmount',
          width:150,
          cellStyle:{textAlign:'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName:'부가세액',
          field:'taxAmount',
          width:150,
          cellStyle:{textAlign:'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        {headerName:'처리상태', field:'dtiStatusText', width:150 },
        {headerName:'메시지', field:'returnDescription', width:200 },
        {headerName:'세금계산서 종류', field:'dtiTypeText', width:110, cellStyle:{textAlign:'center'} },
        {headerName:'매출/매입 구분', field:'supbuyTypeText', width:100, cellStyle:{textAlign:'center'} },
        { headerName:'정/역 구분',
          field:'direction',
          width:100,
          cellStyle:{textAlign:'center'},
          valueFormatter: (params) => {
            if(params.value == '1') {
              return '역발행'
            }else if(params.value == '2') {
              return '정발행'
            }else{
              return params.value
            }
          }
        },
        { headerName:'영수구분',
          field:'taxDemand',
          width:100,
          cellStyle:{textAlign:'center'},
          valueFormatter: (params) => {
            if(params.value == '01') {
              return '영수'
            }else if(params.value == '18') {
              return '청구'
            }else{
              return params.value
            }
          }
        },
        {headerName:'세금계산서 발행일자', field:'dtiWdate', width:160, cellStyle:{textAlign:'center'} },
        {headerName:'E-Mail', field:'byrEmail', width:220, cellStyle:{textAlign:'left'}, },
        {headerName:'품목명', field:'itemName', width:250, cellStyle:{textAlign:'left'}, },
        {headerName:'비고', field:'remark', width:250, cellStyle:{textAlign:'left'}, },
        {headerName:'사업자번호', field:'byrComRegno', width:130, cellStyle:{textAlign:'left'}, },
        { headerName:'합계',
          field:'totalAmount',
          width:150,
          cellStyle:{textAlign:'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        {headerName:'승인번호', field:'approveId', width:250, cellStyle:{textAlign:'left'}, },
        {headerName:'확정ID', field:'etaxIssueId', width:50, hide: true },
      ]
    },
    getNumberFormat(val) {
      if(val){

        if(typeof val === "string"){
          val = val.replace(/[^0-9]/g, "")
        }

        return Math.floor(val).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      }
    },
    goSearch() {
      this.$store.commit('loading');

      this.$http.post(`/api/salesTax/modifyList`, {
        orgId: this.orgId,
        integrationVendorNum: this.integrationVendorNum,
        currencyCode: this.currencyCode,
        taxCode: this.taxCode,
        amendCode: this.amendCode,
      })
       .then(response => {
          this.rowData = response.data;

        })
        .catch(response => {
          console.error("Error");
        })
        .finally(() => {
          this.$store.commit('finish')
        })
    },
    rowDoubleClick(params){
      this.$emit('close', params.data);
    },
    closeModal() {
      this.$parent.close();
    },
    onGridReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    }
  },
  beforeMount() {
    this.makeColDef();
  },
  mounted() {
    this.goSearch();
  }
};
</script>

<style scoped>
div#gridbox {
  width: 100%;
  height: 100%;
  /* min-height: 300px; */
}

.modal-card{
  width: 1200px;
}

</style>
