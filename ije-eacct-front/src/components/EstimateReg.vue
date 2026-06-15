<template>
  <div class="inner-box">
  <form @submit.prevent="save">
    <div class="content-name">
      <h2 class="dp-ib">입찰등록</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <button type="button" class="btn-size btn-gray fl_left" @click="goSearch">
          <span class="btn-icon">
            <i class="fas fa-search"></i>
          </span>
          <!-- {{$t('search')}} -->
          {{this.$i18n.messages[this.$store.state.locale].search}}
        </button>
        <button type="submit" class="btn-size btn-blue fl_left">
          <span class="btn-icon">
            <i class="fas fa-save"></i>
          </span>
          <!-- {{$t('save')}} -->
          {{this.$i18n.messages[this.$store.state.locale].save}}
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

          <div class="search_box_wrap">
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 마감일자</span>
              </div>
              <div class="search_con search-area">
                <div class="datepicker w-type-ymd02 w30p">
                  <dhx-calendar class="input ddate sDate" v-model="form.closeDtFrom" />
                </div>
                <span class="datepicker w10p dp-ib tac"> ~ </span>
                <div class="datepicker w-type-ymd02 w30p">
                  <dhx-calendar class="input ddate sDate" v-model="form.closeDtTo" />
                </div>
                <button @click="dateSetting('toDay','close')" class="search_bt_white_s">당일</button>
                <button @click="dateSetting('crrntMnth','close')" class="search_bt_white_s">당월</button>
                <button @click="dateSetting('PrvsMnth','close')" class="search_bt_white_s">전월</button>
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
      <!-- //상세검색 내용 -->
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
            <button class="btn-size btn-w-gray" @click="addRow()">
              <span class="btn-icon">
                <i class="fas fa-plus"></i>
              </span>{{this.$i18n.messages[this.$store.state.locale].addRow}}
            </button>
            <button class="btn-size btn-w-gray" @click="deleteRow">
              <span class="btn-icon">
                <i class="fas fa-trash-alt"></i>
              </span>{{this.$i18n.messages[this.$store.state.locale].delRow}}
            </button>
          </div>
        </div>
        <!-- <dhx-grid ref="grid1" v-model="data" :config="config" /> -->
        <ag-grid-vue
            ref="gridMain"
            style="width: 100%; height: 35vh; min-height: 30px;"
            :class= this.theme
            rowSelection="multiple"

            :columnDefs="columnDefs"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponents"
            :rowData="data"
            :gridOptions="gridOptions"
            :suppressRowClickSelection="false"
            :tooltipShowDelay="tooltipShowDelay"
            :tooltipHideDelay="tooltipHideDelay"
            @grid-ready="onReadyMain"
            @cell-value-changed="cellValueChange"
            @selection-changed="onSelectionChangedMain"

        />
      </div>
    </div>
    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">입찰상세코드</h3>
          </div>
          <div class="btn-wrap btn-type2 clearfix">
            <button class="btn-size btn-w-gray" @click="addRowd">
              <span class="btn-icon">
                <i class="fas fa-plus"></i>
              </span>{{this.$i18n.messages[this.$store.state.locale].addRow}}
            </button>
            <button class="btn-size btn-w-gray" @click="deleteRowd">
              <span class="btn-icon">
                <i class="fas fa-trash-alt"></i>
              </span>{{this.$i18n.messages[this.$store.state.locale].delRow}}
            </button>
          </div>
        </div>
        <!-- <dhx-grid ref="grid2" v-model="subData" :config="config_child" /> -->
        <ag-grid-vue
            ref="gridSub"
            style="width: 100%; height: 30vh; min-height: 25px;"
            class="ag-theme-alpine"
            rowSelection="multiple"

            :columnDefs="columnDefsSub"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponents"
            :rowData="subData"
            :gridOptions="gridOptionsSub"
            :tooltipShowDelay="tooltipShowDelay"
            :tooltipHideDelay="tooltipHideDelay"
            :suppressRowClickSelection="false"
            :suppressRowTransform="suppressRowTransform"
            @grid-ready="onReadySub"
            @cell-value-changed="cellValueChangeSub"
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
import DhxGrid from '@/components/DhxGrid.vue'
import DatepickerCellRenderer from "@/components/agGrid/datepicker-cell-renderer"
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer"
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn.vue";
import ExpPrice from '@/components/ExpPrice_Ag.vue';

import {AgGridVue} from 'ag-grid-vue';

function gridEditable(params){
    return params.node.data.orderNo === null || params.node.data.orderNo === "" ;
}

function gridCellStyle(params){
  if(params.node.data.orderNo === null || params.node.data.orderNo === ""){
    return ['productDesc','arrivalArea'].includes(params.colDef.field) ? {backgroundColor: '#FFFFFF'} : {backgroundColor: '#FFFFFF', textAlign : 'right'}
  } else {
    return ['productDesc','arrivalArea'].includes(params.colDef.field) ? {backgroundColor: '#F6F6F6'} : {backgroundColor: '#F6F6F6', textAlign : 'right'}
  }
}

function detailRowSpan(params) {
  return params.node.id === "0" ? 4 :  1;
}

function detailEditable(params) {
  if(['bidCurCd', 'bidAmt', 'accCurCd', 'accAmt', 'accExcRt'].includes(params.colDef.field)){
    return params.node.id !== "3"
  } else {
    return params.node.id === "0"
  }
}

function detailCellStyle(params){
  const stripFields = ['bidProductDesc', 'bidArrivalArea', 'bidBusiness', 'bidRemark', 'accStandard', 'accArrivalArea', 'accShipment', 'accPayment', 'accRemark', 'remark'];
  const disableStripFields = ['bidExcRt', 'bidBudAmt', 'bidBudPer', 'bidExpAmt', 'bidExpPer', 'accBudAmt', 'accBudPer', 'accBidAmt', 'accBidPer'];

  if(disableStripFields.includes(params.colDef.field)){
    return {backgroundColor: '#F6F6F6', textAlign : 'right'}
  } else if (params.node.id === "0" ){
    return stripFields.includes(params.colDef.field) ? {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB'} : {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'right'}
  } else if (params.node.id === "3" )  {
    return stripFields.includes(params.colDef.field) ? {backgroundColor: '#F6F6F6'} : {backgroundColor: '#F6F6F6', textAlign : 'right'}
  } else {
    return stripFields.includes(params.colDef.field) ? {backgroundColor: '#FFFFFF'} : {backgroundColor: '#FFFFFF', textAlign : 'right'}
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
  components: {Emp, DhxCalendar, DhxGrid, AgGridVue},
  name: "codeMng",
  data() {
    return {
      title: "예가등록",
      deleteList: [],
      divisionTypes : [],
      form: {
        regDtFrom: this.$moment().subtract(1, 'months').startOf('month').format('YYYYMMDD'),
        regDtTo: this.$moment().format('YYYY-MM-DD'),
        closeDtFrom : '',
        closeDtTo : '',
        orderNo: '',
        projectNm : '',
        divisionCd : '',
        wrtId: '',
        wrtNm: '',
        wrtUserDut: '',
        wrtUserDept: '',
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
      gridOptionsSub: {
        //enableColResize: true,
        //enableFilter: true,
        //animateRows: false,
        //enableSorting: true
      },
      defaultColDef: {
          resizable: true,
          filter: true,
          sortable: true,
          editable: true
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
    //서브그리드 Ready
    onReadySub() {
      //서브그리드 api 정의
      this.gridApiSub = this.gridOptionsSub.api;
      this.columnApiSub = this.gridOptionsSub.columnApi;
    },
    //그리드컬럼 정의
    createColumnDefs() {
        const that = this

        this.columnDefs = [
            {headerName: 'No.', width : 80, valueGetter: (params) => {return params.node.rowIndex + 1 }},
            {headerName: '사업유형', field:'divisionCd', headerClass: 'require-column', width:110, cellRenderer: 'selectCellRenderer', cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["DIVISION_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName: '프로젝트명', field:'projectNm', headerClass: 'require-column', width:240, editable:true, cellEditor: 'agLargeTextCellEditor', tooltipField: 'projectNm',
              cellEditorPopup: true},
            {headerName:'오더번호', field:'orderNo', width:130, cellRenderer: 'schBtn', editable:false,
              cellRendererParams:{
                funcNm : 'updateOrderNo',
                //valFuncNm : 'updateOrderNoVal'
              }
            },
            {headerName:'제품상세', field:'productDesc', width:250, cellEditor: 'agLargeTextCellEditor', tooltipField: 'productDesc', cellEditorPopup: true, editable : gridEditable, cellStyle: gridCellStyle},
            {headerName:'수량', field:'estAmount', width:80, editable : gridEditable, cellStyle: gridCellStyle},
            {headerName:'본품중량(KG/대)', field:'mainWeight', width:160, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'본품CBM(대)', field:'mainCbm', width:140, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'부품중량(KG/대)', field:'partWeight', width:160, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'부품CBM(대)', field:'partCbm', width:140, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'마감일자', field:'closeDt', width:110, editable : gridEditable, cellStyle : gridCellStyle,
              valueFormatter: (params) => {
                return this.getDateFormat(params.value);
              }
            },
            {headerName:'도착지', field:'arrivalArea', width:200, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'arrivalArea', editable : gridEditable, cellStyle: gridCellStyle},
            {headerName:'영업예산', field:'saleBudgetAmt', width:140, editable:true, cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'국내운송료', field:'localShipAmt', width:140, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'국내기타부대비용', field:'localEtcAmt', width:160, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'해상금액', field:'seaAirAmt', width:120, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName: '해상항공통화', field:'seaAirCurCd',  width:130, cellRenderer: 'selectCellRenderer', editable : gridEditable, cellStyle: gridCellStyle,
              cellRendererParams: {
                options : that.options["CUR_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName:'해상항공환율', field:'seaAirExcRt', width:150, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'현지운송료', field:'fieldShipAmt', width:120, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'현지기타부대비용', field:'fieldEtcAmt', width:160, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'현지하차비', field:'fieldOffAmt', width:120, editable : gridEditable, cellStyle: gridCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName: '현지통화', field:'fieldCurCd',  width:110, cellRenderer: 'selectCellRenderer', editable : gridEditable, cellStyle: gridCellStyle,
              cellRendererParams: {
                options : that.options["CUR_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName:'현지환율', field:'fieldExcRt', width:110, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'합계(KRW)', field:'korAmt', width:150, editable:false, cellClass: 'bg-grey', cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }
            },
            {headerName:'비고', field:'remark', width:300, editable:true, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'remark'},
            {headerName:'작성자', field:'wrtNm', width:110, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'center'}},
            {headerName:'', field:'expHeaderId', hide : true},
            {headerName:'', field:'estimateCd', hide : true},
            {headerName:'', field:'shipCd', hide : true},
            {headerName:'', field:'headerId', hide : true},
            {headerName:'', field:'comCd', hide : true},
        ];

      this.columnDefsSub = [
        {
          headerName: '입찰',
          children:[
            {headerName:'입찰일자', cellRenderer: 'datePicker', field:'bidDt', width:110, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'제품상세', field:'bidProductDesc', width:300, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'bidProductDesc', rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'수량', field:'bidAmount', width:80, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'본품중량(KG/대)', field:'bidMainWeight', width:160, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'본품CBM(대)', field:'bidMainCbm', width:140, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'부품중량(KG/대)', field:'bidPartWeight', width:160, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'부품CBM(대)', field:'bidPartCbm', width:140, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'도착지', field:'bidArrivalArea', width:200, cellEditor: 'agLargeTextCellEditor', tooltipField: 'bidArrivalArea', cellEditorPopup: true, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'낙찰업체', field:'bidBusiness', tooltipField: 'bidBusiness', width:120, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'선적모드', field:'bidShipModeCd', width:110, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'center'}},
            {headerName:'낙찰금액', field:'bidAmt', width:150, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName: '입찰통화', field:'bidCurCd',  width:110, cellRenderer: 'selectCellRenderer', editable : detailEditable, cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["CUR_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName:'입찰환율', field:'bidExcRt', width:110, editable:false, cellStyle : detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }},
            {headerName:'예산대비금액', field:'bidBudAmt', width:130, editable : false, cellStyle : detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'예산대비%', field:'bidBudPer', width:130, editable : false, cellStyle : detailCellStyle},
            {headerName:'예가대비금액', field:'bidExpAmt', width:130, editable : false, cellStyle : detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'예가대비%', field:'bidExpPer', width:130, editable : false, cellStyle : detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub');
              }
            },
            {headerName:'차이내역', field:'bidRemark', width:200, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'bidRemark', rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
          ]
        },
        {
          headerName: '정산',
          children: [
            {headerName:'규격', field:'accStandard', cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'accStandard', width:200, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'수량', field:'accAmount', width:80, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'본품중량(KG/대)', field:'accMainWeight', width:160, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'본품CBM(대)', field:'accMainCbm', width:140, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'부품중량(KG/대)', field:'accPartWeight', width:160, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'부품CBM(대)', field:'accPartCbm', width:140, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'도착지', field:'accArrivalArea', width:200, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'accArrivalArea', rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'선적출항일', field:'accShipment', cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'accShipment', width:180, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'납기', field:'accPayment', cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'accPayment', width:180, rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'선적모드', field:'accShipModeCd', width:110, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'center'}},
            {headerName:'최종금액', field:'accAmt', width:150, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName: '정산통화', field:'accCurCd',  width:130, cellRenderer: 'selectCellRenderer', editable : detailEditable, cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["CUR_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName:'정산환율', field:'accExcRt', width:110, editable : detailEditable, cellStyle: detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }},
            {headerName:'예산대비금액', field:'accBudAmt', width:150, editable : false, cellStyle : detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'예산대비%', field:'accBudPer', width:130, editable : false, cellStyle : detailCellStyle},
            {headerName:'입찰대비금액', field:'accBidAmt', width:150, editable : false, cellStyle : detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }
            },
            {headerName:'입찰대비%', field:'accBidPer', width:130, editable : false, cellStyle : detailCellStyle,
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub');
              }
            },
            {headerName:'차이내역', field:'accRemark', width:150, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'accRemark', rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle},
            {headerName:'', field:'headerId', hide : true},
            {headerName:'', field:'detailId', hide : true},
            {headerName:'', field:'comCd', hide : true},
          ]
        },
        {headerName:'비고', field:'remark', width:300, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'remark', rowSpan: detailRowSpan, editable : detailEditable, cellStyle: detailCellStyle}
      ];
    },
    //Main 그리드 SelctionChange 발생
    onSelectionChangedMain() {
      const voRow = this.gridApi.getSelectedRows() //선택된 Row
      this.$store.commit('loading')
      this.$http
          .post("/api/estimate/detail", {
            headerId: voRow[0].headerId
          })
          .then(response => {
            this.subData = response.data
          }).finally(() => {
        this.$store.commit('finish')
      });
    },
    goSearch() {
      const param = JSON.parse(JSON.stringify(this.form));
      const stripFields = ['regDtFrom', 'regDtTo', 'closeDtFrom', 'closeDtTo'];
      _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

      if (!this.validation(param)) return;

      this.$store.commit('loading')
      this.$http.post(`/api/estimate/list`, param)
        .then(response => {
          this.data = response.data;

          if(response.data.length == 0){
              this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
          }
        }).finally(() => {
          this.$store.commit('finish')
        });
    },
    save() {

      //if(!this.gridRequireCheck([this.$refs.gridMain, this.$refs.gridSub ])) return;
      if(!this.gridRequireCheck([this.$refs.gridMain])) return;

      //저장 시작
      this.$http
          .put('/api/estimate/save',{estimateHeader : this.data, estimateDetail : this.subData})
          .then(response => {
            this.$swal({ type: 'success', text: '저장되었습니다.' });
            this.goSearch()
            this.onSelectionChangedMain();
          })
          .catch(({ message }) => {
            if (message === "Request failed with status code 500") {
              this.$swal({ type: "error", text: "오류가 발생하였습니다." });
            }
          });
    },
    deleteRow() {

      if(this.subData.length > 0){
        this.$swal({type: 'error' , text:'예가그룹코드의 상세코드가 존재합니다. 상세코드 삭제 후 예가그룹코드 삭제가 가능합니다.'})
        return
      }

      const row = this.gridApi.getSelectedNodes()

      if (row.length < 1) {
        this.$swal({
          type: 'error',
          text: '삭제할 행을 선택하여주세요.'
        })
      } else {
        let vm = this
        this.$swal({
          type: 'question',
          text: '선택된 예가정보를 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            const rowId = row[0].id
            const isNew = this.data[rowId].new
            const headerId = this.data[rowId].headerId
            this.data.splice(rowId, 1)
            if (!isNew) {
              this.$http.delete('/api/estimate/delete/' + headerId
              ).then(response => {
                this.$swal({ type: 'success', text: '삭제 되었습니다.' });
                this.goSearch()
                // Do nothing!
              })
            }
          }
        })
      }
    },
    addRow() {
      //조회권한 첫번째 Select 박스 포커싱
      //사업유형
      let aDivisionOptions = this.options['DIVISION_CD']
      let sDivisionDetailCd = null
      if (Array.isArray(aDivisionOptions) && aDivisionOptions.length > 0) {
        sDivisionDetailCd = aDivisionOptions[0].key
      }

      //현지통화코드
      let aCurOptions = this.options['CUR_CD']
      let sCurDetailCd = null
      if (Array.isArray(aCurOptions) && aCurOptions.length > 0) {
        sCurDetailCd = aCurOptions[1].key
      }

/*
      this.$http.get(`/api/estimate/headerId`)
      .then(response => {
        let newHeaderId ;
        newHeaderId = response.data
*/
      this.data.push({
        divisionCd : sDivisionDetailCd,
        projectNm : "",
        orderNo : "",
        arrivalArea : "",
        mainWeight : "",
        saleBudgetAmt : 0,
        localShipAmt : 0,
        localEtcAmt : 0,
        seaAirCurCd : sCurDetailCd,
        seaAirAmt : 0,
        fieldCurCd : sCurDetailCd,
        fieldShipAmt : 0,
        fieldEtcAmt : 0,
        fieldOffAmt : 0,
        korAmt : 0,
        regNm : this.$store.state.loginInfo.userName,
        orderYn: "N",
        headerId : "",
        compCd : this.$store.state.loginInfo.compCd,
        new : true,
      });
  //    })
    },
    deleteRowd() {

      const row = this.gridApiSub.getSelectedNodes()

      if (row.length < 1) {
        this.$swal({
          type: 'error',
          text: '삭제할 행을 선택하여주세요.'
        })
      } else {
        let vm = this
        this.$swal({
          type: 'question',
          text: '선택된 그룹의 입찰상세코드가 모두 삭제됩니다.',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            const rowId = row[0].id
            const isNew = vm.subData[rowId].new
            const headerId = vm.subData[rowId].headerId
            const detailId = vm.subData[rowId].detailId
            this.subData.splice(0, 4)
            if (!isNew && detailId != undefined) {
              vm.$http.delete('/api/estimate/delete/' + headerId + '/' + detailId
              ).then(response => {
                vm.$swal({ type: 'success', text: '삭제 되었습니다.' });
                vm.goSearch()
                // Do nothing!
              })
            }
          }
        })
      }
    },
    addRowd() {
      try{

        const row = this.gridApi.getSelectedNodes()
        const rowSub = this.subData.length

        if(row.length < 1)
          throw '그룹코드가 선택되지 않았습니다.'

        if(rowSub >= 4)
          throw '더이상 추가할 수 없습니다.'

        const rowId = row[0].id

        if(this.data[rowId].headerId === "")
          throw '그룹코드 저장 후 입력해주세요.'

        let sCurDetailCd;
        let sBidDt;
        let sBidExcRt;
        let sACCExcRt;
        const sShipModeCd = ['국내', '해상', '현지', '합계'];

        for(var i=0; i<4; i++){

          sBidDt = this.$moment().startOf('month').format('YYYYMMDD');

          if (i === 0) {
            sCurDetailCd = "KRW";
          }

          if (i === 0 || i === 3){
            sCurDetailCd = "KRW";
            sBidExcRt = "1";
            sACCExcRt = "1";
          } else {
            //현지통화코드
            let aCurOptions = this.options['CUR_CD']
            sCurDetailCd = null
            if (Array.isArray(aCurOptions) && aCurOptions.length > 0) {
              sCurDetailCd = aCurOptions[1].key
            }
            sBidExcRt = "";
            sACCExcRt = "";
          }

          this.subData.push({
            bidDt : sBidDt,
            bidShipModeCd : sShipModeCd[i],
            bidAmt : 0,
            bidCurCd : sCurDetailCd,
            bidExcRt : sBidExcRt,
            accCurCd : sCurDetailCd,
            accExcRt : sACCExcRt,
            accShipModeCd : sShipModeCd[i],
            compCd: this.data[rowId].compCd,
            headerId: this.data[rowId].headerId
          });
        }
      }catch (e) {
        this.$swal({
          type: 'warning',
          text: e
        })
      }
    },

    cellValueChange(params){

      const today = this.$moment().startOf('month').format('YYYYMMDD')

      if(params.colDef.field === 'seaAirCurCd'
          ||params.colDef.field === 'seaAirAmt'){
        const rowNode = params.api.getRowNode(params.node.id);
        this.frgnChangEvent(params.node.id, today, rowNode.data.seaAirCurCd, rowNode.data.seaAirAmt, 'SEA_AIR_CUR_CD');
      }
      if(params.colDef.field === 'fieldCurCd'
          ||params.colDef.field === 'fieldShipAmt'
          ||params.colDef.field === 'fieldEtcAmt'
          ||params.colDef.field === 'fieldOffAmt'){
        const rowNode = params.api.getRowNode(params.node.id);
        let fieldAmt = rowNode.data.fieldShipAmt + rowNode.data.fieldEtcAmt + rowNode.data.fieldOffAmt;
        this.frgnChangEvent(params.node.id, today, rowNode.data.fieldCurCd, fieldAmt, 'FIELD_CUR_CD');
      }
      if (params.colDef.field === 'localShipAmt'
          || params.colDef.field === 'localEtcAmt'){
        this.calcTotAmt(params.node.id);
      }
    },
    cellValueChangeSub(params){

      if(params.node.id !== "3"){

        const rowNode = params.api.getRowNode(params.node.id);
        const firstRowNode = params.api.getRowNode(0);

        if(params.colDef.field === 'bidDt'
            || params.colDef.field === 'bidCurCd'
            || params.colDef.field === 'bidAmt'){
          this.frgnChangEvent(params.node.id, firstRowNode.data.bidDt, rowNode.data.bidCurCd, rowNode.data.bidAmt, 'BID_CUR_CD');
        } else if(params.colDef.field === 'bidDt'
            || params.colDef.field === 'accAmt'
            || params.colDef.field === 'accExcRt'){
          this.frgnChangEvent(params.node.id, firstRowNode.data.bidDt, rowNode.data.accCurCd, rowNode.data.accAmt, 'ACC_CUR_CD');
        }
      }
    },
    frgnChangEvent(rId, submitDt, curCd, amt, type){

      let f = this.options['CUR_CD'].filter(x => x.key === curCd)[0]
      if (curCd === undefined || f === undefined) {
        f = this.options['CUR_CD'][0] || {}
      }
      let excDt = submitDt.replace(/-/gi,'')
      let tmp_excRt;
      let tmp_krwTotAmt;

      this.$http.get('/api/exchange/rate/'+curCd+'/'+excDt)
          .then(response => {
            let data = response.data[0]
            tmp_excRt = this.$numeral(data.convRate).value();
            tmp_krwTotAmt = amt
            //tmp_krwTotAmt = amt * tmp_excRt
            //tmp_krwTotAmt = Math.round(tmp_krwTotAmt)
            this.setExchangeValue(rId, tmp_krwTotAmt, tmp_excRt, type);
          }).catch(response => {
            if(curCd !== 'KRW' && type !== 'ACC_CUR_CD'){
              this.$swal({type: 'warning', text: '환율일자에 대한 환율값이 없습니다.'});
            }
            tmp_excRt = 1
            tmp_krwTotAmt = Math.floor(amt * tmp_excRt)
            this.setExchangeValue(rId, tmp_krwTotAmt, tmp_excRt, type);
      })
    },
    setExchangeValue(rId, totalAmt, excRt, type){
      const rowNode = this.gridOptions.api.getRowNode(rId);
      const subRowNode = this.gridOptionsSub.api.getRowNode(rId);

      const headerRow = this.gridApi.getSelectedNodes();
      const rowIndex = headerRow[0].id

      if(type === 'SEA_AIR_CUR_CD'){
        rowNode.setDataValue('seaAirExcRt', excRt);
        this.data[rId].seaAirExcRt = excRt;
        this.calcTotAmt(rId);
      } else  if(type === 'FIELD_CUR_CD'){
        rowNode.setDataValue('fieldExcRt', excRt);
        this.data[rId].fieldExcRt = excRt;
        this.calcTotAmt(rId);
      } else if (type === 'BID_CUR_CD'){

        let array = [];
        let totalArray;
        let tmp_bidExpAmt; //입찰예가대비금액
        let tmp_bidExpPer; //입찰예가대비%

        if(rId === "0"){
          array.push(this.data[rowIndex].localShipAmt);
          array.push(this.data[rowIndex].localEtcAmt);
        } else if (rId === "1"){
          array.push(this.data[rowIndex].seaAirAmt);
        } else if (rId === "2"){
          array.push(this.data[rowIndex].fieldShipAmt);
          array.push(this.data[rowIndex].fieldEtcAmt);
          array.push(this.data[rowIndex].fieldOffAmt);
        }

        totalArray = array.reduce((a,b) => (a+b), 0);
        tmp_bidExpAmt = totalAmt - (totalArray);
        tmp_bidExpPer = Math.round((totalAmt / totalArray) * 100);

        subRowNode.setDataValue('bidExcRt', excRt);
        subRowNode.setDataValue('bidExpAmt', tmp_bidExpAmt);
        subRowNode.setDataValue('bidExpPer', tmp_bidExpPer);
        this.subData[rId].bidExcRt = excRt;
        this.subData[rId].bidExpAmt = tmp_bidExpAmt;
        this.subData[rId].bidExpPer = tmp_bidExpPer;

        this.subCalcTotAmt(type);

      } else if (type === 'ACC_CUR_CD'){

        let tmp_accBidAmt;
        let tmp_accBidPer;

        tmp_accBidAmt = this.subData[rId].accAmt - this.subData[rId].bidAmt;
        tmp_accBidPer = Math.round((this.subData[0].accBudPer / this.subData[rId].bidExpAmt) * 100);



        //subRowNode.setDataValue('accExcRt', excRt);
        subRowNode.setDataValue('accBudAmt', tmp_accBidAmt);
        subRowNode.setDataValue('accBidPer', tmp_accBidPer);

        //this.subData[rId].accExcRt = excRt;
        this.subData[rId].accBidAmt = tmp_accBidAmt;
        this.subData[rId].accBidPer = tmp_accBidPer;

        this.subCalcTotAmt(type);
      }
    },
    calcTotAmt(rId){

      const rowNode = this.gridOptions.api.getRowNode(rId);

      let seaAirTotalAmt;
      let fieldTotalAmt;
      let localTotalAmt;
      let korAmt;

      seaAirTotalAmt = this.data[rId].seaAirExcRt * (this.data[rId].seaAirAmt || 0);
      fieldTotalAmt = this.data[rId].fieldExcRt * ((this.data[rId].fieldShipAmt || 0) + (this.data[rId].fieldEtcAmt || 0) + (this.data[rId].fieldOffAmt || 0));
      localTotalAmt = (this.data[rId].localShipAmt || 0)  + (this.data[rId].localEtcAmt|| 0);
      korAmt = Math.round( localTotalAmt +  seaAirTotalAmt + fieldTotalAmt);
/*
      array.push(this.data[rId].seaAirExcRt * (this.data[rId].seaAirAmt || 0));
      array.push(this.data[rId].fieldExcRt * ((this.data[rId].fieldShipAmt || 0) + (this.data[rId].fieldEtcAmt || 0) + (this.data[rId].fieldOffAmt || 0)));
      array.push((this.data[rId].localShipAmt || 0)  + (this.data[rId].localEtcAmt|| 0));
      korAmt = array.reduce((a,b) => (a+b));
*/
      rowNode.setDataValue('korAmt', korAmt);
      this.data[rId].korAmt = korAmt;
    },
    subCalcTotAmt(type){

      const headerRow = this.gridApi.getSelectedNodes();
      const rowIndex = headerRow[0].id
      const bidDt = this.subData[0].bidDt; // 입찰일자

      for(var i=0; i<this.subData.length; i++){
        this.subData[i].bidDt = bidDt;
      }

      if(type === "BID_CUR_CD"){
        const rowNodeSubSum = this.gridOptionsSub.api.getRowNode(3);
        //const rowNodeSub = this.gridOptionsSub.api.getRowNode(0);

        let totalBidAmt;   //낙찰금액 합계
        let bidArray = []; //낙찰금액
        let totalBidExpAmt;   //예가대비금액 합계
        let bidExpArray = []; //예가대비금액 합계
        let totalSaleBudgetAmt; //영업예산
        let totalBidBudAmt; //입찰예산대비금액
        let totalBidBudPer; //입찰예산대비%
        let korAmt; //예가 합계(입찰그룹코드)
        let totalBidExpPer; //입찰예가대비%

        for(var i=0; i<3; i++) {
          bidArray.push(this.subData[i].bidExcRt * (this.subData[i].bidAmt || 0));
          bidExpArray.push(this.subData[i].bidExcRt * (this.subData[i].bidExpAmt || 0));
        }

        totalBidAmt = bidArray.reduce((a,b) => (a+b), 0);
        totalBidExpAmt = bidExpArray.reduce((a,b) => (a+b), 0);

        totalSaleBudgetAmt = this.data[rowIndex].saleBudgetAmt;
        totalBidBudAmt = totalBidAmt - totalSaleBudgetAmt;
        totalBidBudPer = Math.round((totalBidAmt/totalSaleBudgetAmt) * 100);
        if(totalBidBudPer === Infinity) totalBidBudPer = 0

        korAmt = this.data[rowIndex].korAmt;
        totalBidExpPer = Math.round((totalBidAmt / korAmt) * 100);

        rowNodeSubSum.setDataValue('bidAmt', totalBidAmt);
        rowNodeSubSum.setDataValue('bidBudAmt', totalBidBudAmt);
        rowNodeSubSum.setDataValue('bidBudPer', totalBidBudPer);
        rowNodeSubSum.setDataValue('bidExpAmt', totalBidExpAmt);
        rowNodeSubSum.setDataValue('bidExpPer', totalBidExpPer);
        this.subData[3].bidAmt = totalBidAmt;
        this.subData[3].bidBudAmt = totalBidBudAmt;
        this.subData[3].bidBudPer = totalBidBudPer;
        this.subData[3].bidExpAmt = totalBidExpAmt;
        this.subData[3].bidExpPer = totalBidExpPer;


      } else if(type === "ACC_CUR_CD"){

        const rowNodeSubSum = this.gridOptionsSub.api.getRowNode(3);
        //const rowNodeSub = this.gridOptionsSub.api.getRowNode(0);

        let totalAccAmt; //최종금액합계
        let accArray = []; //최종금액
        let totalSaleBudgetAmt; //영업예산
        let totalAccBudAmt; //정산예산대비금액
        let totalAccBudPer; //정산예산대비%
        let totalAccBidAmt; //(정산)입찰정산대비금액

        for(var k=0; k<3; k++) {
          accArray.push(this.subData[k].accExcRt * (this.subData[k].accAmt || 0));
        }

        totalAccAmt = accArray.reduce((a,b) => (a+b));

        totalSaleBudgetAmt = this.data[rowIndex].saleBudgetAmt;
        totalAccBudAmt = totalAccAmt - totalSaleBudgetAmt;

        totalAccBudPer = Math.round((totalAccAmt / totalSaleBudgetAmt) * 100);
        if(totalAccBudPer === Infinity) totalAccBudPer = 0

        totalAccBidAmt = totalAccAmt - this.subData[3].bidAmt;

        rowNodeSubSum.setDataValue('accAmt', totalAccAmt);
        rowNodeSubSum.setDataValue('accBudAmt', totalAccBudAmt);
        rowNodeSubSum.setDataValue('accBudPer', totalAccBudPer);
        rowNodeSubSum.setDataValue('accBidAmt', totalAccBidAmt);

        this.subData[3].accAmt = totalAccAmt;
        this.subData[3].accBudAmt = totalAccBudAmt;
        this.subData[3].accBudPer = totalAccBudPer;
        this.subData[3].accBidAmt = totalAccBidAmt;
      }
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
      this.form.closeDtFrom = ''
      this.form.closeDtTo = ''
      this.form.orderNo = ''
      this.form.projectNm = ''
      this.form.divisionCd = ''
      this.form.orderYn = ''
      this.form.wrtId = ''
      this.form.wrtNm = ''
      this.form.wrtUserDept = ''
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
    updateOrderNo(pVal){
      //Selected Index
      // const row = this.gridOptions.api.getSelectedNodes()
      // const rowIdx = row[0].rowIndex
      const rowIdx = this.rowId
      const rowNode = this.gridOptions.api.getRowNode(rowIdx);

      const that = this;

      this.$modal.open({
        component: ExpPrice,
        props: {
          searchStr: pVal
        },
        parent: this,
        width: 1100,
        events: {
          close(object) {

            if(object !== ''){
              rowNode.setDataValue('orderNo', object.orderNo);
              rowNode.setDataValue('productDesc', object.productDesc);
              rowNode.setDataValue('estAmount', object.expAmount);
              rowNode.setDataValue('mainWeight', object.mainWeight);
              rowNode.setDataValue('mainCbm', object.mainCbm);
              rowNode.setDataValue('partWeight', object.partWeight);
              rowNode.setDataValue('partCbm', object.partCbm);
              rowNode.setDataValue('closeDt', object.closeDt);
              rowNode.setDataValue('arrivalArea', object.arrivalArea);
              rowNode.setDataValue('localShipAmt', object.localShipAmt);
              rowNode.setDataValue('localEtcAmt', object.localEtcAmt);
              rowNode.setDataValue('seaAirCurCd', object.seaAirCurCd);
              rowNode.setDataValue('seaAirExcRt', object.seaAirExcRt);
              rowNode.setDataValue('seaAirAmt', object.seaAirAmt);
              rowNode.setDataValue('fieldCurCd', object.fieldCurCd);
              rowNode.setDataValue('fieldExcRt', object.fieldExcRt);
              rowNode.setDataValue('fieldShipAmt', object.fieldShipAmt);
              rowNode.setDataValue('fieldEtcAmt', object.fieldEtcAmt);
              rowNode.setDataValue('fieldOffAmt', object.fieldOffAmt);
              rowNode.setDataValue('korAmt', object.korAmt);
              rowNode.setDataValue('expHeaderId', object.expHeaderId);
              rowNode.setDataValue('estimateCd', object.estimateCd);
              rowNode.setDataValue('shipCd', object.shipCd);

              that.data[rowIdx].orderNo = object.orderNo;
              that.data[rowIdx].productDesc = object.productDesc;
              that.data[rowIdx].estAmount = object.expAmount;
              that.data[rowIdx].mainWeight = object.mainWeight;
              that.data[rowIdx].mainCbm = object.mainCbm;
              that.data[rowIdx].partWeight = object.partWeight;
              that.data[rowIdx].partCbm = object.partCbm;
              that.data[rowIdx].closeDt = object.closeDt;
              that.data[rowIdx].arrivalArea = object.arrivalArea;
              that.data[rowIdx].localShipAmt = object.localShipAmt;
              that.data[rowIdx].localEtcAmt = object.localEtcAmt;
              that.data[rowIdx].seaAirCurCd = object.seaAirCurCd;
              that.data[rowIdx].seaAirExcRt = object.seaAirExcRt;
              that.data[rowIdx].seaAirAmt = object.seaAirAmt;
              that.data[rowIdx].fieldCurCd = object.fieldCurCd;
              that.data[rowIdx].fieldExcRt = object.fieldExcRt;
              that.data[rowIdx].fieldShipAmt = object.fieldShipAmt;
              that.data[rowIdx].fieldEtcAmt = object.fieldEtcAmt;
              that.data[rowIdx].fieldOffAmt = object.fieldOffAmt;
              that.data[rowIdx].korAmt = object.korAmt;
              that.data[rowIdx].expHeaderId = object.expHeaderId;
              that.data[rowIdx].estimateCd = object.estimateCd;
              that.data[rowIdx].shipCd = object.shipCd;

            } else {

              rowNode.setDataValue('orderNo', "");
              rowNode.setDataValue('productDesc', "");
              rowNode.setDataValue('estAmount', "");
              rowNode.setDataValue('mainWeight', "");
              rowNode.setDataValue('mainCbm', "");
              rowNode.setDataValue('partWeight', "");
              rowNode.setDataValue('partCbm', "");
              rowNode.setDataValue('closeDt', "");
              rowNode.setDataValue('arrivalArea', "");
              rowNode.setDataValue('localShipAmt', "");
              rowNode.setDataValue('localEtcAmt', "");
              rowNode.setDataValue('seaAirCurCd', "");
              rowNode.setDataValue('seaAirExcRt', "");
              rowNode.setDataValue('seaAirAmt', "");
              rowNode.setDataValue('fieldCurCd', "");
              rowNode.setDataValue('fieldExcRt', "");
              rowNode.setDataValue('fieldShipAmt', "");
              rowNode.setDataValue('fieldEtcAmt', "");
              rowNode.setDataValue('fieldOffAmt', "");
              rowNode.setDataValue('korAmt', "");
              rowNode.setDataValue('expHeaderId', "");
              rowNode.setDataValue('estimateCd', "");
              rowNode.setDataValue('shipCd', "");

              that.data[rowIdx].orderNo = "";
              that.data[rowIdx].productDesc = "";
              that.data[rowIdx].estAmount = "";
              that.data[rowIdx].mainWeight = "";
              that.data[rowIdx].mainCbm = "";
              that.data[rowIdx].partWeight = "";
              that.data[rowIdx].partCbm = "";
              that.data[rowIdx].closeDt = "";
              that.data[rowIdx].arrivalArea = "";
              that.data[rowIdx].localShipAmt = "";
              that.data[rowIdx].localEtcAmt = "";
              that.data[rowIdx].seaAirCurCd = "";
              that.data[rowIdx].seaAirExcRt = "";
              that.data[rowIdx].seaAirAmt = "";
              that.data[rowIdx].fieldCurCd = "";
              that.data[rowIdx].fieldExcRt = "";
              that.data[rowIdx].fieldShipAmt = "";
              that.data[rowIdx].fieldEtcAmt = "";
              that.data[rowIdx].fieldOffAmt = "";
              that.data[rowIdx].korAmt = "";
              that.data[rowIdx].expHeaderId = "";
              that.data[rowIdx].estimateCd = "";
              that.data[rowIdx].shipCd = "";
            }

          }
        }
      });
    },
    /*
    updateOrderNoVal(pVal){
      //Selected Index
      //const row = this.gridOptions.api.getSelectedNodes()
      //const rowIdx = row[0].rowIndex

      const rowIdx = this.rowId;

      const that = this;
      const rowNode = this.gridOptions.api.getRowNode(rowIdx);

      if(pVal){
        this.$http.post(`/api/expect/regList/`, {
          // compCd: param.compCd,
          searchDtmFr: this.$moment().startOf('month').format('YYYY-MM-DD'),
          searchDtmTo: this.$moment().endOf('month').format('YYYY-MM-DD'),
          orderNo: pVal
        }).then(response => {
          //console.log(response.data);

              if(response.data.length == 1){
                rowNode.setDataValue('orderNo', response.data[0].orderNo);
                rowNode.setDataValue('productDesc', response.data[0].productDesc);
                rowNode.setDataValue('mainWeight', response.data[0].mainWeight);
                rowNode.setDataValue('mainCbm', response.data[0].mainCbm);
                rowNode.setDataValue('partWeight', response.data[0].partWeight);
                rowNode.setDataValue('partCbm', response.data[0].partCbm);
                rowNode.setDataValue('arrivalArea', response.data[0].arrivalArea);
                rowNode.setDataValue('localShipAmt', response.data[0].localShipAmt);
                rowNode.setDataValue('localEtcAmt', response.data[0].localEtcAmt);
                rowNode.setDataValue('seaAirAmt', response.data[0].seaAirAmt);
                rowNode.setDataValue('fieldShipAmt', response.data[0].fieldShipAmt);
                rowNode.setDataValue('fieldEtcAmt', response.data[0].fieldEtcAmt);
                rowNode.setDataValue('fieldOffAmt', response.data[0].fieldOffAmt);
                rowNode.setDataValue('korAmt', response.data[0].korAmt);
                rowNode.setDataValue('expHeaderId', response.data[0].expHeaderId);


                that.data[rowIdx].orderNo = response.data[0].orderNo;
                that.data[rowIdx].productDesc = response.data[0].productDesc;
                that.data[rowIdx].mainWeight = response.data[0].mainWeight;
                that.data[rowIdx].mainCbm = response.data[0].mainCbm;
                that.data[rowIdx].partWeight = response.data[0].partWeight;
                that.data[rowIdx].partCbm = response.data[0].partCbm;
                that.data[rowIdx].arrivalArea = response.data[0].arrivalArea;
                that.data[rowIdx].localShipAmt = response.data[0].localShipAmt;
                that.data[rowIdx].localEtcAmt = response.data[0].localEtcAmt;
                that.data[rowIdx].seaAirAmt = response.data[0].seaAirAmt;
                that.data[rowIdx].fieldShipAmt = response.data[0].fieldShipAmt;
                that.data[rowIdx].fieldEtcAmt = response.data[0].fieldEtcAmt;
                that.data[rowIdx].fieldOffAmt = response.data[0].fieldOffAmt;
                that.data[rowIdx].korAmt = response.data[0].korAmt;
                that.data[rowIdx].expHeaderId = response.data[0].expHeaderId;

              }else{
                rowNode.setDataValue('orderNo', pVal);
                this.updateOrderNo(pVal);
              }
            })

      }else{
        rowNode.setDataValue('orderNo', "");
        rowNode.setDataValue('productDesc', "");
        rowNode.setDataValue('mainWeight', "");
        rowNode.setDataValue('mainCbm', "");
        rowNode.setDataValue('partWeight', "");
        rowNode.setDataValue('partCbm', "");
        rowNode.setDataValue('arrivalArea', "");
        rowNode.setDataValue('localShipAmt', "");
        rowNode.setDataValue('localEtcAmt', "");
        rowNode.setDataValue('seaAirAmt', "");
        rowNode.setDataValue('fieldShipAmt', "");
        rowNode.setDataValue('fieldEtcAmt', "");
        rowNode.setDataValue('fieldOffAmt', "");
        rowNode.setDataValue('korAmt', "");
        rowNode.setDataValue('expHeaderId', "");

        that.data[rowIdx].budCctrCd = "";
        that.data[rowIdx].budCctrNm = "";
        that.data[rowIdx].orderNo = "";
        that.data[rowIdx].productDesc = "";
        that.data[rowIdx].mainWeight = "";
        that.data[rowIdx].mainCbm = "";
        that.data[rowIdx].partWeight = "";
        that.data[rowIdx].partCbm = "";
        that.data[rowIdx].arrivalArea = "";
        that.data[rowIdx].localShipAmt = "";
        that.data[rowIdx].localEtcAmt = "";
        that.data[rowIdx].seaAirAmt = "";
        that.data[rowIdx].fieldShipAmt = "";
        that.data[rowIdx].fieldEtcAmt = "";
        that.data[rowIdx].fieldOffAmt = "";
        that.data[rowIdx].korAmt = "";
        that.data[rowIdx].expHeaderId = "";
      }
    },
     */

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
    queryCurCd() {
      this.$store.commit('loading')
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'CUR_CD'
        }
      }).then(response => {
        /*
        this.options['SEA_AIR_CUR_CD'] = response.data
        this.options['FIELD_CUR_CD'] = response.data
         */
        this.options['CUR_CD'] = response.data
        //bus.$emit('selectBox.updated')
      }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    changeTheme(){
      this.theme = "ag-theme-balham";
      this.$router.push({path : '/estimateReg', name:'estimateReg', query: {theme: this.theme}}).catch(()=>{
        this.$router.push('estimateReg');
      });
    }
  },
  beforeMount() {
    //this.createColumnDefs(); //그리드 컬럼정의 함수 호출
  },
  created() {
    this.tooltipShowDelay = 0;
    this.tooltipHideDelay = 2000;
    /*
    bus.$on('selectBox.updated', () => {
      //셀렉트 박스 안에 맵핑될 데이터가 그리드에 반영되기 위해 비동기 처리
      //this.createColumnDefs();
    })
     */

    if(this.$route.query.theme === undefined){
      this.theme = "ag-theme-alpine";
    } else {
      this.theme = this.$route.query.theme;
    }

    this.queryDivisionCd();
    this.queryCurCd();
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