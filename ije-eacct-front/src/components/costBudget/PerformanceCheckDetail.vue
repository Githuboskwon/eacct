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
            <div style="width:1050px;" class="fl_left">
              <div class="item search_wrap">


                <div class="search_box" style="width: 25%">
                  <div class="search_title">
                    <span class="search_tit">회계연도</span>
                  </div>
                  <div class="search_con">
                    <div class="td-s-thumb search-area" style="width: 150px">
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

                <div class="search_box" style="margin-right: 50px;">
                  <div class="search_title">
                    <span class="search_tit">예산부서</span>
                  </div>
                  <div class="search_con">
                    <div class="td-s-thumb search-area" style="width: 300px">
                      <input class="input input-bg" type="text" style="width:100px; text-align: center" v-model="cctrCd" disabled>
                      <input class="input input-bg" type="text" style="width:180px; margin-left: 5px;" v-model="cctrNm" disabled>
                    </div>
                  </div>
                </div>

                <div class="search_box">
                  <div class="search_title">
                    <span class="search_tit">계정과목</span>
                  </div>
                  <div class="search_con">
                    <div class="td-s-thumb search-area" style="width: 150px">
                      <input class="input" type="text" style="width:100px;" v-model="acctCd"  @keypress.enter="popAcct">
                      <button class="icon is-right" @click="popAcct(true)"><i class="fas fa-search"></i>
                      </button>
                    </div>
                  </div>
                </div>

                <span class="search_tit" style="position: absolute; font-size: 13px; width: 245px; color: red">*전표번호 클릭 시 해당전표로 이동합니다.  </span>

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

          <div>
            <div style="display: flex">
              <span>합계 : </span>
              <el-input type="text" v-model="this.total" style="width: 200px;">
              </el-input>
            </div>
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
import AccountPop from "@/components/AccountPop_common";
import ApprovalModal from "@/components/accrualSlip/Approval/Main";
import SlipDetailModal from "@/components/SlipDetailModal";

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
    acctCd: {
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
      title: '비용실적 상세조회',
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
            headerName: '출처',
            field: 'type',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '연도',
            field: 'periodYear',
            width: 120,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '월',
            field: 'periodNum',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '예산부서코드',
            field: 'teamCd',
            width: 150,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '예산부서명',
            field: 'teamNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '전표번호',
            field: 'slipNo',
            width: 250,
            cellStyle:{textAlign: 'center'},
            cellRenderer: (params) => {
              return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${params.value}</div>`;
            }
          },
          {
            headerName: '라인번호',
            field: 'lineNo',
            width: 110,
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
            headerName: '프로젝트',
            field: 'projectNumber',
            width: 150,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '제품군',
            field: 'segment5',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '내용',
            field: 'description',
            width: 400,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '공급자번호',
            field: 'integrationVendorNum',
            width: 150,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '공급자',
            field: 'vendorName',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '통화',
            field: 'currencyCode',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '환율',
            field: 'exchangeRate',
            width: 150,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '분배금액',
            field: 'amount',
            width: 150,
            cellStyle:{textAlign: 'right'},
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: 'GL일자',
            field: 'glDate',
            width: 150,
            cellStyle:{textAlign: 'center'},
            valueFormatter: (params) => {
              return that.getDateFormat(params.value);
            }
          },
          {
            headerName: '귀속부서',
            field: 'deptCode',
            width: 150,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '귀속부서명',
            field: 'deptName',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '생성자사번',
            field: 'createdBy',
            width: 200,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '생성자명',
            field: 'createdName',
            width: 150,
            cellStyle:{textAlign: 'left'}
          },
        ]



    },
    goSearch(){

      this.periodYear = this.$moment(this.periodYm).format('YYYY');
      this.periodMonth = this.$moment(this.periodYm).format('MM');

      this.$store.commit('loading');

      const params = {
        periodYear : this.periodYear,
        periodMonth : this.periodMonth,
        searchDeptCd : this.cctrCd,
        acctCd : this.acctCd
      }

      this.$http.post(`/api/cost/budget/pop/performanceCheck/detail`,params)
          .then(response => {
            if (response.data) {
              this.data = response.data.list;
              this.total = this.getNumberFormat(response.data.total);
            }
          }).catch(response => {
        // TODO::Error Handling
        return response
      }).finally(() => {
        this.$store.commit('finish');
      });

    },
    onCellClicked(params) {
      const field = params.colDef.field;
      if(field === 'slipNo') {

        let slipType = params.data.slipType;
        let docMngNo = params.data.apprNo;
        let slipNo = params.data.slipNo;

        switch (slipType) {
          case "E1": //개인비용
          default:
            if(params.data.docTypeCd === 'BDGT'){
              var vm = this
              this.$modal.open({
                component: ApprovalModal,
                props: {
                  docType: params.data.docTypeCd,
                  budReqNo: params.data.docMngNo,
                  docMngNo : params.data.apprNo,
                },
                parent: this,
                width: 1200,
                events: {
                  close(data) {
                    vm.goSearch();
                  }
                }
              })
            }
            else{
              this.showDetailPop(slipNo, slipType, docMngNo);
            }
            break;
        }

      }
    },
    showDetailPop(slipNo, slipType, docMngNo) {
      let title = "";
      let setModal = undefined;
      switch (slipType) {
        case 'E12' :
          title = '구매전표'
          setModal = SlipDetailModal
          break;
        case 'E13' :
          title = '공사비전표'
          setModal = SlipDetailModal
          break;
        default:
          title = "전표";
          setModal = ApprovalModal;
          break;
      }
      this.$modal.open({
        component: setModal,
        parent: this,
        props: {
          slipNo : slipNo,
          docType: 'appr',
          title: title,
          readOnly : true,
        },
        width: 1200,
      });
    },
    popAcct() {
      const vm = this
      this.$modal.open({
        component: AccountPop,
        parent: this,
        props: {
          postSearch : true,
          deptCd : this.cctrCd
        },
        width: 700,
        events: {
          close(object) {
            vm.acctCd = object.acctCd;
            vm.acctNm = object.acctNm;
          }
        }
      })
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
    getDateFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD')
      }
    },
  },
  created() {
    this.makeColDef();
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
.search_box {
  width: 30%;
}

</style>
