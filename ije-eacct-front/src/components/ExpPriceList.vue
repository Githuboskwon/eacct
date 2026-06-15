<template>
  <div class="inner-box">
  <form>
    <div class="content-name">
      <h2 class="dp-ib">예가내역조회</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <button type="button" class="btn-size btn-gray fl_left" @click="goSearch('search')">
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

          <div class="search_box_wrap">
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
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 거래처 사용여부</span>
              </div> 
              <div class="search_con search-area">
                <!-- <input class="input" type="checkbox" v-model="form.cust" checked>
                  <label>
                  </label>
                </input> -->
                <div class="bd-l-none checkbox-wrapper" @click="changeChk()">
                  <input class="custChk" type="checkbox" v-model="form.cust" checked value="true"/>
                  <label>&nbsp;</label>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-window-bottom">
            <ul>
              <li>
                <button class="bt_blue_s" @click="goSearch('search')">검색</button>
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

import GridCheckbox from '@/components/grid/GridCheckbox.vue'
import Emp from '@/components/Emp_Ag.vue'
import DhxCalendar from '@/components/DhxCalendar.vue'
import DhxGrid from '@/components/DhxGrid.vue'
import DatepickerCellRenderer from "@/components/agGrid/datepicker-cell-renderer";
import CheckboxCellRenderer from "@/components/agGrid/checkbox-cell-renderer";
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer";
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn";

import {AgGridVue} from 'ag-grid-vue';
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

function rowSpan(params) {
  return params.node.data.shipModeNum === 1 ? 5 :  1;
}

function cellStyle(params){
  const stripCenterAligns = ['sfaPrjNo','divisionNm','estimateNm','orderNo','shipCd','No.'];
  const stripLeftAligns = ['projectNm','arrivalArea','productDesc'];

  if(stripLeftAligns.includes(params.colDef.field)){
    return  {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'left'}
  } else if(stripCenterAligns.includes(params.colDef.field)){
    return {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'center'}
  } else {
    return {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'right'}
  }
}

export default {
  mixins: [common],
  components: {Emp, DhxCalendar, DhxGrid, AgGridVue},
  name: "codeMng",
  data() {
    return {
      title: "예가내역조회",
      deleteList: [],
      divisionTypes : [],
      form: {
        regDtFrom: this.$moment().startOf('month').format('YYYY-MM-DD'),
        regDtTo: this.$moment().format('YYYY-MM-DD'),
        closeDtFrom : '',
        closeDtTo : '',
        orderNo: '',
        projectNm : '',
        divisionCd : '',
        //wrtId: '',
        //wrtNm: '',
        //wrtUserDut: '',
        //wrtUserDept: '',
        orderYn : '',
      },
      data: [],
      subData: [],
      options: {
        'DIVISION_CD': [],
        'SHIP_CD': [],
        'CUR_CD': [],
        'EXP_BUSINESS_CD' : [],
        'SEA_AIR_CUR_CD' : [],
        'FIELD_CUR_CD' : [],
        'ESTIMATE_CD' : [],
        'ONCE_SHIP_CD' : [],
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
    //서브그리드 Ready
    onReadySub() {
        //서브그리드 api 정의
        this.gridApiSub = this.gridOptionsSub.api;
        this.columnApiSub = this.gridOptionsSub.columnApi;
    },
    //그리드컬럼 정의
    createColumnDefs() {
        const that = this
        const param = JSON.parse(JSON.stringify(this.form));
        this.$http.post(`/api/expect/custList`, param)
              .then(response => {
              const custList = response.data;
              

        this.columnDefs = [
            {headerName: 'No.', field:'No.', width : 80, rowSpan: rowSpan, cellClass: 'codeType', cellStyle : cellStyle,
              valueGetter: (params) => {
                return Number.isInteger(params.node.rowIndex/5) === true ? Math.floor(params.node.rowIndex/5)  + 1 : null
              }
            },
            {headerName:'SFA_PRJ_NO', field:'sfaPrjNo', width:140, rowSpan:rowSpan , cellStyle: cellStyle, cellClass: 'codeType'},
            {headerName: '사업유형', field:'divisionNm', width:110, rowSpan:rowSpan , cellStyle: cellStyle, cellClass: 'codeType'},
            {headerName: '입찰형태', field:'estimateNm', width:110, rowSpan:rowSpan , cellStyle: cellStyle, cellClass: 'codeType'},
            {headerName: '오더번호', field:'orderNo', width:110, rowSpan:rowSpan , cellStyle: cellStyle, cellClass: 'codeType'},
            {headerName: '프로젝트명', field:'projectNm', width:200, rowSpan:rowSpan ,cellStyle: cellStyle, tooltipField: 'projectNm'},
            {headerName: '운송조건', field:'shipCd', width:130, rowSpan:rowSpan ,cellStyle: cellStyle, cellClass: 'codeType'},
            {headerName:'도착지', field:'arrivalArea', width:300, rowSpan:rowSpan ,cellStyle: cellStyle, tooltipField: 'arrivalArea'},
            {headerName:'제품상세', field:'productDesc', width:300, rowSpan:rowSpan , cellStyle: cellStyle,tooltipField: 'productDesc'},
            {headerName:'구분', field:'shipModeNm', width:80, editable:false, cellClass : 'codeType', cellStyle : (params) => {
                  return Number.isInteger((params.node.rowIndex+1)/5) === true ? {'textAlign': 'center', 'backgroundColor' : '#F6F6F6'} : {'textAlign': 'center'}
              }},
            {headerName:'', field:'headerId', hide : true},
            {headerName:'', field:'shipModeNum', hide : true},
            {headerName:'', field:'comCd', hide : true},
        ];

            var columnDefs = this.columnDefs;

            if(document.getElementsByClassName("custChk")[0].checked){ // 거래처 포함이 체크된 경우
              for(var i = 0; i<custList.length; i++){
                
                if(custList[i].useYn === 'N') continue; // 사용중이 아니라면 그리드를 생성하지 않는다.

                if(custList[i].expBusinessCd != '99'){ // 물류팀 번호인 99번인 경우 입찰으로 그리드 생성
                  columnDefs.push({headerName:custList[i].expBusinessNm , field:'cust'+custList[i].expBusinessCd , tooltipField: 'cust'+custList[i].expBusinessCd, width:200, cellStyle : {'textAlign': 'right'},
                      valueFormatter: (params) => {
                        if(Number.isInteger((params.node.rowIndex+1)/5) != true ) return that.chkNumber(params, '', 'excRt');
                    },cellClass: (params) => {
                        return Number.isInteger((params.node.rowIndex+1)/5) === true ? 'bg-grey' : 'numberType'
                    }})
                }
              } 
            }else{
              for(var i = 0; i<custList.length; i++){
                if(custList[i].expBusinessCd != '99'){
                  columnDefs.push({headerName:custList[i].expBusinessNm , field:'cust'+custList[i].expBusinessCd , tooltipField: 'cust'+custList[i].expBusinessCd, width:200, cellStyle : {'textAlign': 'right'},
                      valueFormatter: (params) => {
                        if(Number.isInteger((params.node.rowIndex+1)/5) != true ) return that.chkNumber(params, '', 'excRt');
                    },cellClass: (params) => {
                        return Number.isInteger((params.node.rowIndex+1)/5) === true ? 'bg-grey' : 'numberType'
                    }}) 
                }
              } 
            }
            columnDefs.push(
              {headerName:'입찰', field:'cust99', width:200, cellClass: 'bg-orange', tooltipField: 'cust99', cellStyle : {'textAlign': 'right'}  ,
                valueFormatter: (params) => {
                  if(Number.isInteger((params.node.rowIndex+1)/5) != true ) return that.chkNumber(params, '', 'excRt');
              }})
            this.gridApi.setColumnDefs(this.columnDefs);
            this.gridOptions.columnApi.moveColumns(['cust99'], columnDefs.length-1);
        })
    },
    goSearch(params) {
      const param = JSON.parse(JSON.stringify(this.form));
      const stripFields = ['regDtFrom', 'regDtTo', 'closeDtFrom', 'closeDtTo'];
      _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

      if (!this.validation(param)) return;

      this.$store.commit('loading')
      this.$http.post(`/api/expect/history`, param)
        .then(response => {
          this.data = response.data;
          if(response.data.length == 0){
              this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
          }
          if(params === 'search'){
            setTimeout(() => {
              this.createColumnDefs();
            },600);
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
    changeChk() {
      const custChk = document.getElementsByClassName("custChk")[0];
      custChk.checked == true ? 
      custChk.checked = false : 
      custChk.checked = true;
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
        fileName : "예가내역"
      };
      this.gridOptions.api.exportDataAsExcel(params);
    },
    changeTheme(){
      this.theme = "ag-theme-balham";
      this.$router.push({path : '/expPriceList', name:'expPriceList', query: {theme: this.theme}}).catch(()=>{
        this.$router.push('expPriceList');
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
        id: 'bg-grey',
        interior: {
          color: '#F6F6F6',
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
  },
  mounted() {
  }
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