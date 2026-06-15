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
<!--              <div>-->
<!--                <span>회계연도 : </span>-->
<!--                <monthly-picker style="width:100px;" v-model="periodYM" />-->
<!--                <span>업로드 작업구분명 : </span><input class="input input-bg" style="width:200px;" type="text" v-model="uploadTitle">-->
<!--              </div>-->
              <div class="item search_wrap">


                <div class="search_box">
                  <div class="search_title">
                    <span class="search_tit">회계연도</span>
                  </div>
                  <div class="search_con">
                    <div class="td-s-thumb search-area" style="width: 200px">
                      <div class="form-input">
                        <el-date-picker
                            v-model="periodYear"
                            type="year"
                            format="yyyy"
                            value-format="yyyy"
                            style="width: 80%;">
                        </el-date-picker>
<!--                        v-on:change.native="changeYm()"-->
                      </div>
                    </div>
                  </div>
                </div>

                <div class="search_box">
                  <div class="search_title">
                    <span class="search_tit">예산부서</span>
                  </div>
                  <div class="search_con">
                    <div class="td-s-thumb search-area" style="width: 300px">
                      <input class="input input-bg" type="text" style="width:100px; text-align: center;" v-model="cctrCd" disabled>
                      <input class="input input-bg" type="text" style="width:180px; margin-left: 5px;" v-model="cctrNm" disabled>
                    </div>
                  </div>
                </div>

              </div>
            </div>
            <div class="btn-wrap btn-type1 clearfix fl_right">
              <button class="btn-size btn-gray" @click="goSearch(true)"><span class="btn-icon"><i class="fas fa-search"></i></span> 조회</button>
            </div>

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
              :suppressRowClickSelection="true"
              :enableRangeSelection="true"
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
    periodYear: {
      type: String,
      required: false
    },
    cctrCd: {
      type: String,
      required: false
    },
    cctrNm: {
      type: String,
      required: false
    },
  },
  mixins: [],
  components: {
    AgGridVue,
    Layout,
    MonthlyPicker,
  },
  data() {
    return {
      title: '비용예산관리 사업계획 조회',
      columnDefs: [],
      defaultColDef: {
        resizable: true, filter:false, sortable: false
      },
      frameworkComponents: {},
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
    makeColDef() {
      const that = this
      this.columnDefs = [
        // { headerName: 'No.',
        //   field: 'rn',
        //   width: 80,
        //   valueFormatter: function(params) {
        //     return params.node.rowIndex + 1;
        //   },
        //   cellStyle:{textAlign: 'center'}
        // },
        {headerName : '회계연도', field: 'periodYear', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName : '예산부서코드', field: 'cctrCd', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '예산부서명', field: 'cctrNm', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '계정코드', field: 'acctCd', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '계정명', field: 'acctNm', width: 180, cellStyle:{textAlign: 'left'}},
        {headerName : '프로젝트코드', field: 'pjtCd', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '프로젝트명', field: 'pjtNm', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName : '제품군코드', field: 'itemGroupCd', width: 110, cellStyle:{textAlign: 'center'}},
        {headerName : '제품군명', field: 'itemGroupNm', width: 200, cellStyle:{textAlign: 'left'}},
        {headerName : '1월 사업계획', field: 'm01PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '2월 사업계획', field: 'm02PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '3월 사업계획', field: 'm03PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '4월 사업계획', field: 'm04PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '5월 사업계획', field: 'm05PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '6월 사업계획', field: 'm06PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '7월 사업계획', field: 'm07PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '8월 사업계획', field: 'm08PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '9월 사업계획', field: 'm09PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '10월 사업계획', field: 'm10PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '11월 사업계획', field: 'm11PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '12월 사업계획', field: 'm12PlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},
        {headerName : '사업계획 합계', field: 'totalPlanAmt', width: 130, cellStyle:{textAlign: 'right'},valueFormatter: (params) => {return that.getNumberFormat(params.value);}},


      ]

    },
    goSearch(){

      this.$store.commit('loading');

      const params = {
        periodYear: this.periodYear,
        searchDeptCd: this.cctrCd
      }

      this.$http.post(`/api/cost/budget/pop/yearplan`,params)
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
    this.goSearch();
  },
  watch: {
    'periodYear'() {
      this.goSearch();
    },
  }

}
</script>

<style lang="scss" scoped>

.modal-card {
  width: 1200px;
}

</style>
