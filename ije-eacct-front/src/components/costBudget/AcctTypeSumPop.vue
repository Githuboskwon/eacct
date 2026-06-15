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
              <div class="item search_wrap">


                <div class="search_box">
                  <div class="search_title">
                    <span class="search_tit">회계연도</span>
                  </div>
                  <div class="search_con">
                    <div class="td-s-thumb search-area" style="width: 200px">
                      <div class="form-input">
                        <el-date-picker
                            v-model="periodYm"
                            type="month"
                            format="yyyy-MM"
                            value-format="yyyyMM"
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
                      <input class="input input-bg" type="text" style="width:100px;" v-model="cctrCd" disabled>
                      <div class="ip-box ip-box-w02" style="width:120px;">
                        <input class="input input-bg" type="text" style="width:150px;" v-model="cctrNm" disabled>
                      </div>
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
    periodYm: {
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
      title: '비용예산관리 계정별 요약',
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

      let month = Number(this.periodMonth);

      let yearMonth = Number(this.$moment(this.periodYm).format('YYYYMM'));

      if(month == 11){

        this.columnDefs = [
          {
            headerName: '회계연도',
            field: 'periodYear',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '구분',
            field: 'cctrDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '예산부서명',
            field: 'cctrNm',
            width: 160,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 230,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '['+yearMonth+'] 사업계획',
            field: 'm01PlanAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+yearMonth+'] 예산',
            field: 'm01BudgetAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+1)+'] 사업계획',
            field: 'm02PlanAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+1)+'] 예산',
            field: 'm02BudgetAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
        ]

      }else if(month == 12){

        this.columnDefs = [
          {
            headerName: '회계연도',
            field: 'periodYear',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '구분',
            field: 'cctrDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '예산부서명',
            field: 'cctrNm',
            width: 160,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 230,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '['+yearMonth+'] 사업계획',
            field: 'm01PlanAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+yearMonth+'] 예산',
            field: 'm01BudgetAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
        ]

      }else{

        this.columnDefs = [
          {
            headerName: '회계연도',
            field: 'periodYear',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '구분',
            field: 'cctrDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '예산부서명',
            field: 'cctrNm',
            width: 160,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 230,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '['+yearMonth+'] 사업계획',
            field: 'm01PlanAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+yearMonth+'] 예산',
            field: 'm01BudgetAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+1)+'] 사업계획',
            field: 'm02PlanAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+1)+'] 예산',
            field: 'm02BudgetAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+2)+'] 사업계획',
            field: 'm03PlanAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+2)+'] 예산',
            field: 'm03BudgetAmt',
            width: 180,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
        ]

      }


    },
    goSearch(){

      this.periodYear = this.$moment(this.periodYm).format('YYYY');
      this.periodMonth = this.$moment(this.periodYm).format('MM');

      this.makeColDef();

      this.$store.commit('loading');

      const params = {
        periodYear : this.periodYear,
        periodMonth : this.periodMonth,
        searchDeptCd : this.cctrCd,
        searchDegree : 1
      }

      this.$http.post(`/api/cost/budget/pop/acctTypeSum`,params)
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
  width: 1200px;
}

</style>
