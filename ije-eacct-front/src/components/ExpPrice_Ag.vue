<template>
<layout>
  <span slot="header">
    {{ title }} 조회
    <button class="btn-pop-close delete" aria-label="close" @click="$dismiss"></button>
  </span>
  <div slot="content">
    <div class="desc-content search-border" >
      <div class="item desc-left" >
        <div class="desc-item">
          <div class="tit">
            <span class="label-tit">등록일자</span>
          </div>
          <div class="desc">
            <div class="datepicker w-type-ymd02">
              <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />
            </div>
            <span class="wave"> ~ </span>
            <div class="datepicker w-type-ymd02">
              <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />
            </div>
          </div>
        </div>
      </div>

      <div class="item desc-center" style="width: 200px; margin-right:40px;">
        <div class="desc-item" >
          <div class="tit">
            <span class="label-tit"> 사업유형</span>
          </div>
          <div class="desc">
            <select class="select is-fullwidth" v-model="form.divisionCd">
              <option value=''>==전체==</option>
              <option
                  v-for="item in divisionTypes"
                  :key="item.key"
                  :value="item.key"
                  v-text="item.value"/>
            </select>
          </div>
        </div>
      </div>

      <div class="item desc-center" style="width: 200px; margin-right:40px;">
        <div class="desc-item" >
          <div class="tit">
            <span class="label-tit"> 수주여부</span>
          </div>
          <div class="desc">
            <select class="select is-fullwidth" v-model="form.orderYn">
              <option value="">==전체==</option>
              <option value="Y">예</option>
              <option value="N">아니오</option>
            </select>
          </div>
        </div>
      </div>

      <div class="item desc-right" style="width: 200px;">
        <div class="desc-item" >
          <div class="tit">
            <span class="label-tit"> 오더번호</span>
          </div>
          <div class="desc">
            <div class="search_con search-area">
              <input class="input Rt-M tal w100p" type="text" v-model="search">
            </div>
          </div>
        </div>
      </div>

      <div class="item desc-right" style="width: 110px">
        <div class="desc-item">
          <div class="tit">
            <div class="btn-wrap btn-type1 clearfix">
              <button class="btn-size btn-gray fl_left" style="height: 26px;" @click="goSearch">
                <span class="btn-icon"><i class="fas fa-search"></i></span> 조회
              </button>
            </div>
          </div>
        </div>
      </div>

    </div>
    <!-- //검색조건 영역 -->
    <div class="grid-tbl-box">
      <ag-grid-vue 
                  style="width: 100%; height: 720px;"
                  class="ag-theme-alpine"
                  rowSelection="single"

                  :columnDefs="columnDefs"     
                  :gridOptions="gridOptions"
                  :tooltipShowDelay="tooltipShowDelay"
                  :tooltipHideDelay="tooltipHideDelay"
                  :rowData="rowData"
                  :defaultColDef="defaultColDef"
                  :getRowStyle="getRowStyle"
                  @rowDoubleClicked="rowDoubleClick"
                  @grid-ready="onGridReady"/>
    </div>
  </div>
</layout>
</template>

<script>
import common from '@/mixin/common';
import Layout from '@/components/ModalSlot4.vue'
import DhxCalendar from '@/components/DhxCalendar.vue'
import {AgGridVue} from 'ag-grid-vue';
import _ from "lodash";

export default {
  compatConfig: { MODE: 2 },
  props: {
    title: {
      type: String,
      required: false,
      default: '예가리스트'
    },
    searchStr: {
      type: String,
      required: false
    }
  },
  components: {Layout, AgGridVue, DhxCalendar},
  mixins: [common],
  data() {
    return {
      search: undefined,
      gridOptions: {},
      tooltipShowDelay: null,
      tooltipHideDelay: null,
      divisionTypes : [],
      getRowStyle  : '',
      columnDefs:[
        {headerName:'No.', field:'no', width:80,
          valueFormatter: function(params) {               
            return params.node.rowIndex+1;
          }
        },
        {headerName:'SFA_PRJ_NO', field:'sfaPrjNo', width:140},
        {headerName:'사업유형', field:'divisionNm', width:110},
        {headerName:'입찰형태', field:'estimateNm', width:110},
        {headerName:'동시운송', field:'onceShipNm', width:110},
        {headerName:'오더번호', field:'orderNo', width:110},
        {headerName:'프로젝트명', field:'projectNm', tooltipField: 'projectNm', width:250, cellStyle : {'textAlign': 'left'}},
        {headerName:'운송조건', field:'shipNm', width:160},
        {headerName:'도착지', field:'arrivalArea', tooltipField: 'arrivalArea', width:250, cellStyle : {'textAlign': 'left'}},
        {headerName:'제품상세', field:'productDesc', tooltipField: 'productDesc', width:250, cellStyle : {'textAlign': 'left'}},
        {headerName:'수량', field:'expAmount', width:80,},
        {headerName:'본품중량(KG/대)', field:'mainWeight', width:160, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }
        },
        {headerName:'본품CBM(대)', field:'mainCbm', width:140, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }
        },
        {headerName:'부품중량(KG/대)', field:'partWeight', width:160, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }
        },
        {headerName:'부품CBM(대)', field:'partCbm', width:140, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }
        },
        {headerName:'마감일자', field:'closeDt', width:120,
          valueFormatter: (params) => {
            return params.value === null ? "" : this.$moment(params.value).format('YYYY-MM-DD');
          }},
        {headerName:'선적모드', field:'shipModeNm',  width:140},
        {headerName:'제출일자', field:'submitDt', width:120,
          valueFormatter: (params) => {
            return params.value === null ? "" : this.$moment(params.value).format('YYYY-MM-DD');
          }},
        {headerName:'국내운송료', field:'localShipAmt', width:120, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }
        },
        {headerName:'국내기타부대비용', field:'localEtcAmt', width:160, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }},
        {headerName:'해상항공금액', field:'seaAirAmt', width:140, cellStyle : {'textAlign': 'right'},
                  valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }},
        {headerName:'해상항공통화', field:'seaAirCurCd', width:140},
        {headerName:'해상항공환율', field:'seaAirExcRt', width:140, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }},
        {headerName:'현지운송료', field:'fieldShipAmt', width:140, cellStyle : {'textAlign': 'right'},
                  valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }},
        {headerName:'현지기타부대비용', field:'fieldEtcAmt', width:160, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }
        },
        {headerName:'현지하차비', field:'fieldOffAmt', width:140, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }},
        {headerName:'현지통화', field:'fieldCurCd', width:120},
        {headerName:'현지환율', field:'fieldExcRt', width:140, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }},
        {headerName:'합계금액', field:'korAmt', width:140, cellStyle : {'textAlign': 'right'},
          valueFormatter: (params) => {
            return this.chkNumber(params, '', 'excRt');
          }},
        {headerName:'비고', field:'remark', width:240, cellStyle : {'textAlign': 'left'}},
        {headerName:'', field:'estimateCd', hide : true},
        {headerName:'', field:'shipCd', hide : true},
        {headerName:'', field:'orderYn', hide : true},
        {headerName:'', field:'expHeaderId', hide : true},
      ],
      defaultColDef: { 
        resizable: true, 
        filter:true, 
        sortable: true 
      },
      rowData: [],
      form: {
        searchDtmFr: this.$moment().startOf('month').format('YYYY-MM-DD'),
        searchDtmTo: this.$moment().endOf('month').format('YYYY-MM-DD'),
        divisionCd : '',
        orderYn : 'N'
      },
      sendData : []
    }
  },
  methods: {
    $dismiss() {
      this.$emit('dismiss')
      this.$parent.close()
    },
    goSearch() {
      const param = JSON.parse(JSON.stringify(this.form));
      const stripFields = ['searchDtmFr', 'searchDtmTo'];
      _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

      this.$store.commit('loading')

      this.data = []
      // console.log("erp cctr value ", this)
      this.$http.post(`/api/expect/regList/`, {
            // compCd: param.compCd,
            searchDtmFr: param.searchDtmFr,
            searchDtmTo: param.searchDtmTo,
            divisionCd: param.divisionCd,
            orderNo: this.search,
            orderYn: param.orderYn
        })
        .then(response => {
          this.rowData = response.data;
        })
        .catch(response => {
          console.error("ErpCctr_Ag Error");
        })
        .finally(() => {
          this.$store.commit('finish')
        })
    },
    rowDoubleClick(params){
      this.$emit('close', params.data);
    },
    onGridReady(){
      //this.gridOptions.api.sizeColumnsToFit();
    },
    chkNumber(params, flag, excRt){

      var result = '';
      var val = params.value;

      if(!_.isEmpty(val) || _.isNumber(val)){
        val = val.toString();

        if (excRt === 'excRt'){
          result = this.$numeral(val).format('0,0.00');
        } else {
          result = this.$numeral(val).format('0,0');
        }
        //data[params.node.id][params.column.colId] = this.$numeral(val).value();
      }

      return result
    },
    //사업유형코드 공통코드 조회
    queryDivisionCd() {
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'DIVISION_CD'
        }
      }).then(response => {
        this.divisionTypes = response.data
      }).catch(response => {
        return response
      }).finally(() => {
      })
    },
  },
  mounted(){
  },
  created() {
    this.tooltipShowDelay = 0;
    this.tooltipHideDelay = 2000;
    this.queryDivisionCd();

    this.search = this.searchStr
    if (this.search) {
      this.goSearch()
    }

    this.getRowStyle  = params => {
      if (params.data.orderYn === "Y") {
        return { backgroundColor: '#81BEF7' };
      }
    }

  }
}

function _url(...args) {
  args = args.map(x => String(x || '').trim().replace(/^\/*|\/*$/g, ''))
  args = args.filter(x => x)
  return args.join('/')
}
</script>


<style scoped>
div#gridbox {
  width: 100%;
  height: 100%;
  min-height: 300px;
}
div.modal-card {
  width: 100%;
}

</style>

