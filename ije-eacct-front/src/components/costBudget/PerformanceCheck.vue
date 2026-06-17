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
                            v-model="periodYmM"
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
                      <input class="input input-bg" type="text" style="width:100px; text-align: center;" :value="cctrCd" disabled>
                      <input class="input input-bg" type="text" style="width:180px; margin-left: 5px;" :value="cctrNm" disabled>
                    </div>
                  </div>
                </div>

                <span class="search_tit" style="position: absolute; font-size: 13px; width: 300px; color: red">* 전월실적 클릭시 상세내역으로 이동합니다. </span>

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
import PerformerChkDetail from "@/components/costBudget/PerformanceCheckDetail";

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
    Layout
  },
  data() {
    return {
      periodYmM: this.periodYm, // (Vue3) prop v-model 금지 → 로컬 복사
      title: '비용예산관리 실적조회',
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

      let yearMonth = Number(this.$moment(this.periodYmM).format('YYYYMM'));

      if(month == 11){

        this.columnDefs = [
          {
            headerName: '구분',
            field: 'acctDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정코드',
            field: 'acctCd',
            width: 100,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '프로젝트코드',
            field: 'pjtCd',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '프로젝트명',
            field: 'pjtNm',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '제품군코드',
            field: 'itemGroupCd',
            width: 110,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '제품군명',
            field: 'itemGroupNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: (month-1)+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'prevPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ptdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                },
                cellRenderer: (params) => {
                  return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${params.value}</div>`;
                }
              },
              {
                headerName: '차이',
                field: 'prevPtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month-1)+' 월 누계',
            children:[
              {
                headerName: '사업계획',
                field: 'prevYtdPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevYtdBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ytdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '차이',
                field: 'prevYtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: month+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'planAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'bdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+1)+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'nextPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'nextBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
        ]

      }else if(month == 12){

        this.columnDefs = [
          {
            headerName: '구분',
            field: 'acctDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정코드',
            field: 'acctCd',
            width: 100,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '프로젝트코드',
            field: 'pjtCd',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '프로젝트명',
            field: 'pjtNm',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '제품군코드',
            field: 'itemGroupCd',
            width: 110,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '제품군명',
            field: 'itemGroupNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: (month-1)+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'prevPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ptdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                // valueFormatter: (params) => {
                //   return that.getNumberFormat(params.value);
                // },
                cellRenderer: (params) => {
                  const value = Math.floor(params.value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                  return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${value}</div>`;
                }
              },
              {
                headerName: '차이',
                field: 'prevPtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month-1)+' 월 누계',
            children:[
              {
                headerName: '사업계획',
                field: 'prevYtdPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevYtdBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ytdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '차이',
                field: 'prevYtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: month+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'planAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'bdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
        ]

      } else if(month == 1){

        this.columnDefs = [
          {
            headerName: '구분',
            field: 'acctDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정코드',
            field: 'acctCd',
            width: 100,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '프로젝트코드',
            field: 'pjtCd',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '프로젝트명',
            field: 'pjtNm',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '제품군코드',
            field: 'itemGroupCd',
            width: 110,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '제품군명',
            field: 'itemGroupNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '12 월',
            children:[
              {
                headerName: '사업계획',
                field: 'prevPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ptdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                },
                cellRenderer: (params) => {
                  const value = Math.floor(params.value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                  return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${value}</div>`;
                }
              },
              {
                headerName: '차이',
                field: 'prevPtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: '12 월 누계',
            children:[
              {
                headerName: '사업계획',
                field: 'prevYtdPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevYtdBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ytdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '차이',
                field: 'prevYtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: month+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'planAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'bdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+1)+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'nextPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'nextBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+2)+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'next2PlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'next2BdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
        ]

      }
      else{

        this.columnDefs = [
          {
            headerName: '구분',
            field: 'acctDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정코드',
            field: 'acctCd',
            width: 150,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '프로젝트코드',
            field: 'pjtCd',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '프로젝트명',
            field: 'pjtNm',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '제품군코드',
            field: 'itemGroupCd',
            width: 110,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '제품군명',
            field: 'itemGroupNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: (month-1)+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'prevPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ptdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                },
                cellRenderer: (params) => {
                  const value = Math.floor(params.value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                  return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${value}</div>`;
                }
              },
              {
                headerName: '차이',
                field: 'prevPtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month-1)+' 월 누계',
            children:[
              {
                headerName: '사업계획',
                field: 'prevYtdPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevYtdBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ytdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '차이',
                field: 'prevYtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: month+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'planAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'bdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+1)+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'nextPlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'nextBdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+2)+' 월',
            children:[
              {
                headerName: '사업계획',
                field: 'next2PlanAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'next2BdAmt',
                width: 180,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
        ]

      }


    },
    goSearch(){

      this.periodYear = this.$moment(this.periodYmM).format('YYYY');
      this.periodMonth = this.$moment(this.periodYmM).format('MM');

      this.makeColDef();

      this.$store.commit('loading');

      const params = {
        periodYear : this.periodYear,
        periodMonth : this.periodMonth,
        searchDeptCd : this.cctrCd,
        searchDegree : 1
      }

      this.$http.post(`/api/cost/budget/pop/performanceCheck`,params)
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

        var that = this
        this.$modal.open({
          component: PerformerChkDetail,
          props: {
            periodYm: this.$moment(this.periodYmM).add(-1, 'month').startOf('month').format('YYYYMM'),
            cctrCd : this.cctrCd,
            cctrNm : this.cctrNm,
            acctCd : params.data.acctCd
          },
          parent: this,
          width: 1400,
          events: {
            close() {

            }
          }
        })

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
    'periodYmM'() {
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
