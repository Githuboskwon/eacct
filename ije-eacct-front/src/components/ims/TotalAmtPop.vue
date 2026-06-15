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
          </div>


          <div>
            <ag-grid-vue
                ref="grid"
                style="width: 100%; height: 600px;"
                class="ag-theme-alpine"
                rowSelection="multiple"
                :columnDefs="columnDefs"
                :defaultColDef="defaultColDef"
                :frameworkComponents="frameworkComponents"
                :rowData="data"
                :gridOptions="gridOptions"
                :suppressRowClickSelection="false"
                :enableRangeSelection="false"
                @cell-clicked="onCellClicked"
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

//['docMngNo', 'value', 'readonly']
export default {
  props: {
    projectNumber: {
      type: String,
      required: false
    },
    degree: {
      type: String,
      required: false
    },
  },
  mixins: [],
  components: {
    AgGridVue,
    Layout
  },
  data() {
    return {
      title: 'PJT실적분석-총비용',
      columnDefs: [],
      defaultColDef: {
        resizable: true, filter:false, sortable: false
      },
      frameworkComponents: {},
      data: [],
      gridOptions: {},
      periodYear: '',
      periodMonth: '',
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
    },
    makeColDef(){
      const that = this;

      this.columnDefs = [
        {
          headerName: '프로젝트번호',
          field: 'projectNumber',
          width: 150,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '프로젝트명',
          field: 'projectName',
          width: 250,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '년도',
          field: 'periodYear',
          width: 150,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '월',
          field: 'periodNum',
          width: 150,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '합계금액',
          field: 'totalAmount',
          width: 250,
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          },
          cellStyle:{textAlign: 'right'}
        },
      ]

    },
    goSearch(){

      this.periodYear = this.$moment(this.periodYm).format('YYYY');
      this.periodMonth = this.$moment(this.periodYm).format('MM');

      this.makeColDef();

      this.$store.commit('loading');

      const params = {
        projectNumber : this.projectNumber,
        degree : this.degree
      }

      this.$http.post(`/api/pjt/perfAnalysis/totalAmt/pop/list`,params)
          .then(response => {
            if (response.data) {
              this.data = response.data;
            }
          }).catch(response => {
        // TODO::Error Handling
        return response
      }).finally(() => {
        this.$store.commit('finish');
      });

    },
    onCellClicked(params) {
      //const idx = params.rowIndex;
      const field = params.colDef.field;
      if(field === 'ptdActualAmt') {

      }
    },
    getNumberFormat(value){

      value = this.isEmptyNum(value);

      if(value){
        if(typeof value === "string"){
          if(val.substr(0,1) !== '-') value = value.replace(/[^0-9]/g, "")
          if(val.substr(0,1) === '-') value = value.replace( /^\[-\]?\\d\*$/g, "")
        }
        return Math.floor(value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      }
    },
    isEmptyNum(value){
      if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
        return 0
      }else{
        return value
      }
    },
  },
  created() {
  },
  mounted() {
    this.goSearch();
  },
  watch: {
    'periodYm'() {
      this.goSearch();
    },
  }

}
</script>

<style lang="scss" scoped>

.modal-card {
  width: 1400px;
}

</style>
