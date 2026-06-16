<template>
  <div class="inner-box">
  <form @submit.prevent="save">
    <div class="content-name">
      <h2 class="dp-ib">입찰내역조회</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <button type="button" class="btn-size btn-gray fl_left" @click="goSearch">
          <span class="btn-icon">
            <i class="fas fa-search"></i>
          </span>
          <!-- {{$t('search')}} -->
          {{this.$i18n.messages[this.$store.state.locale].search}}
        </button>
      </div>
    </div>
  </form>
    <!-- 검색 Form -->
    <div class="search-form">
      <div class="form-group">
        <label class="control-label-req">등록일자</label>
        <div class="search_con">
          <div class="datepicker w-type-ymd02">
            <dhx-calendar class="input ddate sDate" v-model="form.regDtFrom" />
          </div>
          <span class="wave"> ~ </span>
          <div class="datepicker w-type-ymd02">
            <dhx-calendar class="input ddate sDate" v-model="form.regDtTo" />
          </div>
        </div>
      </div>
      <div class="search_box">
        <button class="item-list icon is-right btn_s_w" @click="openModal()" type="button">상세검색<i class="fas fa-plus"></i></button>
      </div>

      <!-- 상세검색 내용 -->
      <div id="open-moda" class="modal-window">
        <div class="modal-window-wrap">
          <div class="modal-window-top">
            <h4>상세검색</h4>
            <button title="Close" @click="closeModal()" type="button" class="bt-modal-close mt5"><img src="../../public/img/bt_close_w.png" /></button>
          </div>

          <div class="search_box_wrap" >
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 입찰일자</span>
              </div>
              <div class="search_con search-area">
                <div class="datepicker w-type-ymd02 w30p">
                  <dhx-calendar class="input ddate sDate" v-model="form.bidDtFrom" />
                </div>
                <span class="datepicker w10p dp-ib tac"> ~ </span>
                <div class="datepicker w-type-ymd02 w30p">
                  <dhx-calendar class="input ddate sDate" v-model="form.bidDtTo" />
                </div>
                <button @click="dateSetting('toDay','bid')" class="search_bt_white_s">당일</button>
                <button @click="dateSetting('crrntMnth','bid')" class="search_bt_white_s">당월</button>
                <button @click="dateSetting('PrvsMnth','bid')" class="search_bt_white_s">전월</button>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 오더번호</span>
              </div>
              <div class="search_con search-area">
                <input class="input Rt-M tal w100p" type="text" v-model="form.orderNo">
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 프로젝트</span>
              </div>
              <div class="search_con search-area">
                <input class="input Rt-M tal w100p" type="text" v-model="form.projectNm">
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 사업유형</span>
              </div>
              <div class="search_con search-area">
                <b-select class="select is-fullwidth" v-model="form.divisionCd">
                  <option value=''>==전체==</option>
                  <option
                      v-for="item in divisionTypes"
                      :key="item.key"
                      :value="item.key"
                      v-text="item.value"/>
                </b-select>
              </div>
            </div>
            <!--
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 수주여부</span>
              </div>
              <div class="search_con search-area">
                <b-select class="select is-fullwidth" v-model="form.orderYn">
                  <option value="">==전체==</option>
                  <option value="Y">예</option>
                  <option value="N">아니오</option>
                </b-select>
              </div>
            </div>
            -->
            <!--
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 작성자</span>
              </div>
              <div class="search_con search-area">
                <input class="input dp-ib w30p_5r" type="text" v-model="form.wrtUserDept" disabled>
                <input class="input dp-ib w20p_5r" type="text" v-model="form.wrtId" disabled>
                <div class="dp-ib w50p">
                  <input class="input" type="text" v-model="form.wrtNm" @input="initEmp" @keypress.enter="popEmp">
                  <button class="icon is-right" @click="popEmp"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            -->
          </div>
          <div class="modal-window-bottom">
            <ul>
              <li>
                <button class="bt_blue_s" @click="goSearch">검색</button>
              </li>
              <li>
                <button @click="closeModal()" title="Close" class="bt_white_s">닫기</button>
              </li>
              <li>
                <button @click="resetSearch" class="bt_gray_s"><i class="fas fa-undo"></i></button>
              </li>
            </ul>
          </div>

        </div>
      </div>
    </div>
    <!-- //검색 Form -->
    <!-- 테이블 -->
    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">입찰그룹코드</h3>
          </div>
          <div class="btn-wrap btn-type2 clearfix">
            <button class="btn-size btn-w-gray" @click="changeTheme">테마 변경</button>
            <button class="btn-size btn-excel" @click="saveExcel">엑셀 저장</button>
          </div>
        </div>
        <!-- <dhx-grid ref="grid1" v-model="data" :config="config" /> -->
        <ag-grid-vue
            ref="gridMain"
            style="width: 100%; height: 70vh; min-height: 30px;"
            :class= this.theme
            rowSelection="multiple"

            :readOnlyEdit="true"
            :columnDefs="columnDefs"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponents"
            :rowData="data"
            :gridOptions="gridOptions"
            :suppressRowClickSelection="false"
            :tooltipShowDelay="tooltipShowDelay"
            :tooltipHideDelay="tooltipHideDelay"
            :suppressRowTransform="suppressRowTransform"
            :excelStyles="excelStyles"
            @grid-ready="onReadyMain"
        />
      </div>
    </div>
  </div>
</template>
<script>
import common from '@/mixin/common';
//import Vue from "vue";
import _ from "lodash";

import Emp from '@/components/Emp_Ag.vue'
import DhxCalendar from '@/components/DhxCalendar.vue'
import DatepickerCellRenderer from "@/components/agGrid/datepicker-cell-renderer"
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer"
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn.vue";

import {AgGridVue} from 'ag-grid-vue';

function rowSpan(params) {
  return params.node.data.detailNum === 1 ? 4 :  1;
}

function cellStyle(params){

  const stripLeftAligns = ['projectNm', 'productDesc', 'arrivalArea',
    'bidProductDesc', 'bidArrivalArea', 'bidBusiness', 'bidRemark',
    'accStandard', 'accArrivalArea', 'accShipment', 'accRemark', 'remark'];

  const stripCenterAligns = ['divisionNm', 'orderNo', 'shipModeCd', 'bidDt', 'bidShipModeCd', 'bidCurCd', 'accPayment', 'accShipModeCd', 'accCurCd'];

  if(stripLeftAligns.includes(params.colDef.field)){
    return params.node.data.detailNum === 1 ? {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'left'} :''
  } else if(stripCenterAligns.includes(params.colDef.field)){
    return params.node.data.detailNum === 1 ? {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'center'} :''
  } else {
    return {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'right'}
  }
}

//const bus = new Vue()
/**
 * Url JOIN
 */
function _url(...args) {
  args = args.map(x =>
    String(x || "")
      .trim()
      .replace(/^\/*|\/*$/g, "")
  );
  return args.filter(x => x).join("/");
}

export default {
  mixins: [common],
  components: {Emp, DhxCalendar, AgGridVue},
  name: "codeMng",
  data() {
    return {
      title: "예가등록",
      deleteList: [],
      divisionTypes : [],
      form: {
        regDtFrom: this.$moment().startOf('month').format('YYYY-MM-DD'),
        regDtTo: this.$moment().format('YYYY-MM-DD'),
        closeDtFrom : '',
        closeDtTo : '',
        bidDtFrom : '',
        bidDtTo   : '',
        orderNo: '',
        projectNm : '',
        divisionCd : '',
        //wrtId: '',
        //wrtNm: '',
        //wrtUserDut: '',
        //wrtUserDept: '',
      },
      data: [],
      subData: [],
      options: {
        'DIVISION_CD': [],
        'CUR_CD' : [],
        'SEA_AIR_CUR_CD' : [],
        'FIELD_CUR_CD' : [],
        'SHIP_MODE_CD' : [],
      },
      gridOptions: {
        //enableColResize: true,
        //enableFilter: true,
        //animateRows: false,
        //enableSorting: true
        enableColResize: true,
        defaultColDef: {
          width: 100
        }
      },
      defaultColDef: {
          resizable: true,
          filter: true,
          sortable: true,
          cellClass: 'multiline',
      },
      gridApi: null,
      columnApi: null,
      columnDefs: [],
      gridApiSub: null,
      columnApiSub: null,
      columnDefsSub: [],
      suppressRowTransform: true,
      tooltipShowDelay: null,
      tooltipHideDelay: null,
      excelStyles: null,
      frameworkComponents : {//그리드에서 사용할 외부 comp 등록
        checkboxRenderer: CheckboxCellRenderer,
        datePicker: DatepickerCellRenderer,
        selectCellRenderer: SelectCellRenderer,
        schBtn : AgGridSearchBtn,
      },
      theme : null
    };
  },
  methods: {
    //메인그리드 Ready
    onReadyMain() {
        //메인그리드 api 정의
        this.gridApi = this.gridOptions.api;
        this.columnApi = this.gridOptions.columnApi;
        //그리드 너비에 맞게 컬럼 size 조정
        //this.gridApi.sizeColumnsToFit();
        setTimeout(() => {
          this.createColumnDefs();
        },600);
        this.goSearch();
    },
    //그리드컬럼 정의
    createColumnDefs() {
      const that = this

      this.columnDefs = [
        {headerName: 'No.', width : 80, rowSpan: rowSpan, cellStyle : cellStyle,
          valueGetter: (params) => {
            return Number.isInteger(params.node.rowIndex/4) === true ? Math.floor(params.node.rowIndex/4)  + 1 : null
          }
        },
        {headerName: '사업유형', width:110 , field:'divisionNm', rowSpan: rowSpan, cellStyle : cellStyle, cellClass:'codeType'},
        {headerName: '프로젝트명', field:'projectNm', width:200, rowSpan: rowSpan, tooltipField: 'projectNm', cellStyle : cellStyle},
        {
          headerName: '예가',
          children: [
            {headerName:'오더번호', field:'orderNo', width:110, rowSpan: rowSpan, cellStyle : cellStyle, cellClass:'codeType'},
            {headerName:'제품상세', field:'productDesc', width:200, rowSpan: rowSpan, tooltipField: 'productDesc', cellStyle : cellStyle},
            {headerName:'수량', field:'estAmount', width:80, rowSpan: rowSpan, cellStyle : cellStyle},
            {headerName:'본품중량(KG/대)', field:'mainWeight', width:160, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'본품CBM(대)', field:'mainCbm', width:140, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'부품중량(KG/대)', field:'partWeight', width:160, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'부품CBM(대)', field:'partCbm', width:140, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'도착지', field:'arrivalArea', width:200, rowSpan: rowSpan, tooltipField: 'arrivalArea', cellStyle : cellStyle},
            {headerName:'구분', field:'shipModeCd', width:80, cellStyle : {textAlign: 'center'}, cellClass:'codeType'},
            {headerName:'예가금액', field:'expAmt', width:150, cellStyle : cellStyle, tooltipField: 'expAmt', cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'영업예산금액', field:'saleBudgetAmt', width:150, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'예가대비%', field:'', width:120, cellStyle : cellStyle},
          ]
        },
        {
          headerName: '입찰',
          children:[
            {headerName:'입찰일자', field:'bidDt', width:110, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'dateFormat',
              valueFormatter: (params) => {
                return that.getDateFormat(params.value);
              }
            },
            {headerName:'제품상세', field:'bidProductDesc', width:300, rowSpan: rowSpan, tooltipField: 'bidProductDesc', cellStyle : cellStyle},
            {headerName:'수량', field:'bidAmount', width:80, rowSpan: rowSpan, cellStyle : cellStyle},
            {headerName:'본품중량(KG/대)', field:'bidMainWeight', width:160, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'본품CBM(대)', field:'bidMainCbm', width:140, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'부품중량(KG/대)', field:'bidPartWeight', width:160, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'부품CBM(대)', field:'bidPartCbm', width:140, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'도착지', field:'bidArrivalArea', width:200, rowSpan: rowSpan, tooltipField: 'bidArrivalArea', cellStyle : cellStyle},
            {headerName:'낙찰업체', field:'bidBusiness', rowSpan: rowSpan, cellStyle : cellStyle},
            {headerName:'선적모드', field:'bidShipModeCd', width:110, cellStyle : {textAlign: 'center'}, cellClass:'codeType'},
            {headerName:'낙찰금액', field:'bidAmt', width:150, cellClass: 'bg-green', tooltipField: 'bidAmt', cellStyle : cellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName: '입찰통화', field:'bidCurCd',  width:110, cellStyle : {textAlign: 'center'}, cellClass:'codeType'},
            {headerName:'입찰환율', field:'bidExcRt', width:110, cellStyle : cellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }},
            {headerName:'예산대비금액', field:'bidBudAmt', width:130, tooltipField: 'bidBudAmt', cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'예산대비%', field:'bidBudPer', width:120, cellStyle : cellStyle},
            {headerName:'예가대비금액', field:'bidExpAmt', width:140, cellClass: 'numberType', cellStyle : cellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'예가대비%', field:'bidExpPer', width:120, cellStyle : cellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '');
              }
            },
            {headerName:'차이내역', field:'bidRemark', width:200, rowSpan: rowSpan, tooltipField: 'bidRemark', cellStyle : cellStyle},
          ]
        },
        {
          headerName: '정산',
          children:[
            {headerName:'규격', field:'accStandard', width:200, rowSpan: rowSpan, tooltipField: 'accStandard', cellStyle : cellStyle},
            {headerName:'수량', field:'accAmount', width:80, rowSpan: rowSpan, cellStyle : cellStyle},
            {headerName:'본품중량(KG/대)', field:'accMainWeight', width:160, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'본품CBM(대)', field:'accMainCbm', width:140, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'부품중량(KG/대)', field:'accPartWeight', width:160, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'부품CBM(대)', field:'accPartCbm', width:140, rowSpan: rowSpan, cellStyle : cellStyle, cellClass: 'numberType',
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'도착지', field:'accArrivalArea', width:200, rowSpan: rowSpan, tooltipField: 'accArrivalArea', cellStyle : cellStyle},
            {headerName:'선적출항일', field:'accShipment', rowSpan: rowSpan, tooltipField: 'accShipment', cellStyle : cellStyle},
            {headerName:'납기', width:150, field:'accPayment', rowSpan: rowSpan, cellStyle : cellStyle},
            {headerName: '선적모드', field:'accShipModeCd', width:110, cellStyle : {textAlign: 'center'}, cellClass:'codeType'},
            {headerName:'최종금액', field:'accAmt', width:160, cellStyle : cellStyle, tooltipField: 'accAmt',
              cellClass: (params) => {
                return Number.isInteger((params.node.rowIndex+1)/4) === true ? 'bg-orange' : ''
              },
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName: '정산통화', field:'accCurCd',  width:110, cellStyle : {textAlign: 'center'}, cellClass:'codeType'},
            {headerName:'정산환율', field:'accExcRt', width:110, cellStyle : cellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }},
            {headerName:'예산대비금액', field:'accBudAmt', width:140, cellClass: 'numberType', tooltipField: 'accBudAmt', cellStyle : cellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'예산대비%', field:'accBudPer', width:120, cellStyle : cellStyle},
            {headerName:'입찰대비금액', field:'accBidAmt', width:140, cellClass: 'numberType', tooltipField: 'accBidAmt', cellStyle : cellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'입찰대비%', field:'accBidPer', width:120, cellStyle : cellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '');
              }
            },
            {headerName:'차이내역', field:'accRemark', width:250, rowSpan: rowSpan, tooltipField: 'accRemark', cellStyle : cellStyle},
            {headerName:'', field:'comCd', hide : true},
          ]
        },
        {headerName:'비고', field:'remark', width:300, rowSpan: rowSpan, tooltipField: 'remark', cellStyle : cellStyle},
        {headerName:'', field:'headerId', hide : true},
        {headerName:'', field:'detailNum', hide : true},
        {headerName:'', field:'comCd', hide : true},
      ];
    },
    goSearch() {
      const param = JSON.parse(JSON.stringify(this.form));
      const stripFields = ['regDtFrom', 'regDtTo', 'bidDtFrom', 'bidDtTo'];
      _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

      if (!this.validation(param)) return;

      this.$store.commit('loading')
      this.$http.post(`/api/estimate/history`, param)
        .then(response => {
          this.data = response.data;

          if(response.data.length == 0){
              this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
          }
        }).finally(() => {
          this.$store.commit('finish')
        });
    },
    //사업유형코드 공통코드 조회
    queryDivisionCd() {
      this.$store.commit('loading')
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'DIVISION_CD'
        }
      }).then(response => {
        this.options['DIVISION_CD'] = response.data
        this.divisionTypes = response.data
        //console.log(this.form.divisionTypes);
        //bus.$emit('selectBox.updated')
      }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    chkNumber(params, flag, excRt){

      var result = '';
      var val = params.value;
      var data = this.data;

      if(flag === 'sub'){
        data = this.subData;
      }

      if(!_.isEmpty(val) || _.isNumber(val)){
        val = val.toString();

        if (excRt === 'excRt'){
          result = this.$numeral(val).format('0,0.00');
        } else {
          result = this.$numeral(val).format('0,0');
        }

        data[params.node.id][params.column.colId] = this.$numeral(val).value();
      }

      return result
    },
    validation(param) {
      if (param.regDtFrom.length < 8 || param.regDtTo.length < 8) {
        this.$swal({type: 'warning', text: '등록일자를 입력해 주세요.'});
        return false;
      }
      return true;
    },
    /*
    initEmp(force) {
      if (force === true) this.form.wrtNm = '';
      if (this.form.wrtNm === '') {
        this.form.wrtId = '';
        this.form.wrtUserDept = '';
      }
    },
    popEmp() {
      const that = this;

      this.$modal.open({
        component: Emp,
        parent: this,
        props: {
          param: this.form.wrtNm
        },
        width: 700,
        events: {
          close(obj) {
            that.form.wrtId = obj.empNo;
            that.form.wrtNm = obj.empNm;
            that.form.wrtUserDept = obj.deptNm;
          }
        }
      });
    },
     */
    openModal() {
      document.getElementById("open-moda").style.opacity = "1";
      document.getElementById("open-moda").style.pointerEvents = "auto";
    },
    closeModal() {
      document.getElementById("open-moda").style.opacity = "0";
      document.getElementById("open-moda").style.pointerEvents = "none";
    },
    resetSearch(){
      this.form.regDtFrom = this.$moment().startOf('month').format('YYYY-MM-DD')
      this.form.regDtTo = this.$moment().format('YYYY-MM-DD')
      this.form.bidDtFrom = ''
      this.form.bidDtTo = ''
      this.form.orderNo = ''
      this.form.projectNm = ''
      this.form.divisionCd = ''
      //this.form.orderYn = ''
      //this.form.wrtId = ''
      //this.form.wrtNm = ''
      //this.form.wrtUserDept = ''
    },
    dateSetting(str,type) {
      var typeFrom = type.concat('DtFrom')
      var typeTo = type.concat('DtTo')

      switch (str) {
        case 'toDay':
          this.form[typeFrom] = this.$moment().format('YYYYMMDD')
          this.form[typeTo] = this.$moment().format('YYYYMMDD')
          break;
        case 'crrntMnth':
          this.form[typeFrom] = this.$moment().startOf('month').format('YYYYMMDD')
          this.form[typeTo] = this.$moment().endOf('month').format('YYYYMMDD')
          break;
        case 'PrvsMnth':
          this.form[typeFrom] = this.$moment().add(-1, 'month').startOf('month').format('YYYYMMDD')
          this.form[typeTo] = this.$moment().add(-1, 'month').endOf('month').format('YYYYMMDD')
          break;
      }
    },
    saveExcel() {
      var params = {
        fileName : "입찰내역"
      };
      this.gridOptions.api.exportDataAsExcel(params)
    },
    changeTheme(){
      this.theme = "ag-theme-balham";
      this.$router.push({path : '/estimateList', name:'estimateList', query: {theme: this.theme}}).catch(()=>{
        this.$router.push('estimateList');
      });
    }
  },
  beforeMount() {
    //this.createColumnDefs(); //그리드 컬럼정의 함수 호출
  },
  created() {
    this.tooltipShowDelay = 0;
    this.tooltipHideDelay = 2000;
    this.queryDivisionCd();

    if(this.$route.query.theme === undefined){
      this.theme = "ag-theme-alpine";
    } else {
      this.theme = this.$route.query.theme;
    }

    this.excelStyles = [
      {
        id: 'cell',
        alignment: {
          vertical: 'Center',
        },
      },
      {
        id: 'numberType',
        numberFormat: {
          format: '#,##0.00',
        }
      },
      {
        id: "codeType",
        alignment: {
          horizontal: "Center",
        }
      },
      {
        id: 'dateFormat',
        dataType: 'DateTime',
        numberFormat: {
          format: 'yyyy-mm-dd;',
        },
        alignment: {
          horizontal: "Center",
        }
      },
      {
        id: 'multiline',
        alignment: {
          wrapText: true,
        },
      },
      {
        id: 'bg-green',
        interior: {
          color: '#B5E6B5',
          pattern: 'Solid',
        },
        numberFormat: {
          format: '#,##0.00',
        },
      },
      {
        id: 'bg-orange',
        interior: {
          color: '#FFBF00',
          pattern: 'Solid',
        },
        numberFormat: {
          format: '#,##0.00',
        },
      },
      {
        id: 'header',
        font: {
          bold: true,
        },
        alignment: {
          vertical: 'Center',
        },
        interior: {
          color: '#f8f8f8',
          pattern: 'Solid',
          patternColor: undefined,
        },
        borders: {
          borderBottom: {
            color: '#000000',
            lineStyle: 'Continuous',
            weight: 1,
          },
          borderRight:{
            color: '#000000',
            lineStyle: 'Continuous',
            weight: 1,
          }
        },
      },
    ];
    /*
    bus.$on('selectBox.updated', () => {
      //셀렉트 박스 안에 맵핑될 데이터가 그리드에 반영되기 위해 비동기 처리
      //this.createColumnDefs();
    })
     */
  },
  mounted() {
  },
};
</script>


<style scoped>
.gridbox {
  height: 200px !important;
}
.gridbox .objbox {
  height: 350px !important;
}

.desc-content:after {
  clear: both;
  content: "";
  display: block;
}
.btn-wrap {
  margin-bottom: 20px;
}
.desc-content {
  border: 2px solid #9db6c9;
  background: #f9fafc;
  margin: 0 0 20px 0;
  border-radius: 4px;
  padding: 15px 2%;
  clear: both;
}
.desc-content .item {
  float: left;
}
.desc-content .item .desc-item {
  position: relative;
  padding-left: 82px;
  margin-bottom: 8px;
}
.desc-content .item .desc-item:last-child {
  margin-bottom: 0;
  height: 25px;
}
.desc-content .item .desc-item .tit {
  position: absolute;
  left: 0;
}
.desc-content .item .desc-item .label-tit {
  font-family: "NotoM";
  color: #222;
  font-size: 15px;
}
.desc-content .item.desc-left .desc-item {
  padding-left: 90px;
}
.desc-content .item.desc-left .desc-item .desc:after {
  clear: both;
  content: "";
  display: block;
}
.desc-content .item.desc-left .desc-item .desc .datepicker {
  float: left;
}
.desc-content .item.desc-left .desc-item .desc span.wave {
  float: left;
  padding: 0 6px;
}
.desc-content .item.desc-left .desc-item .td-s-thumb.search-area:after {
  clear: both;
  content: "";
  display: block;
}
.desc-content .item.desc-left .desc-item .td-s-thumb.search-area input,
.desc-content .item.desc-left .desc-item .td-s-thumb.search-area .ip-box {
  float: left;
}
.desc-content .item.desc-left .desc-item .desc.select select {
  width: 70%;
}
.desc-content .item.desc-left {
  width: 35%;
  padding-right: 40px;
}
.desc-content .item.desc-center {
  width: 35%;
  padding-right: 40px;
}
.desc-content .item.desc-right {
  width: 30%;
}
.search-area input {
  position: relative;
}
.search-area .icon {
  position: absolute;
  right: 8px;
  top: 1px;
  z-index: 100;
  cursor: pointer;
  font-size: 16px;
  color: #555;
}
.search-border .td-s-thumb {
  position: relative;
  display: inline-block;
}
.search-border .td-s-thumb.search-area > input,
.search-border
  .td-s-thumb.search-area
  > .ip-box
  .search-border
  .td-s-thumb.search-area
  > button {
  float: left;
}
.search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
  width: 60%;
  margin-left: 6px;
}
.remove_text {
  margin-left: 0;
}
.contents div.gridbox_material.gridbox .xhdr {
  border-bottom: 1px solid #dfdfdf;
}

@media (max-width: 1580px) {
  .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
    width: 50%;
  }
}

</style>