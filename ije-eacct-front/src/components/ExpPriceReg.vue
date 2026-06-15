<template>
  <div class="inner-box">
  <form @submit.prevent="save">
    <div class="content-name">
      <h2 class="dp-ib">예가등록</h2>
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
            <h3 class="ico_table_name">예가그룹코드</h3>
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
            style="width: 100%; height: 30vh; min-height: 25px;"
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
            @selection-changed="onSelectionChangedMain"
            @cell-value-changed="cellValueChange"
        />
      </div>
    </div>
    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">예가상세코드</h3>
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
            :class= this.theme
            rowSelection="multiple"

            :columnDefs="columnDefsSub"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponents"
            :rowData="subData"
            :gridOptions="gridOptionsSub"
            :suppressRowClickSelection="false"
            :tooltipShowDelay="tooltipShowDelay"
            :tooltipHideDelay="tooltipHideDelay"
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
import DatepickerCellRenderer from "@/components/agGrid/datepicker-cell-renderer";
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer";
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
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
        orderYn : ''
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
      tooltipShowDelay: null,
      tooltipHideDelay: null,
      frameworkComponents : {//그리드에서 사용할 외부 comp 등록
        checkboxRenderer: CheckboxCellRenderer,
        datePicker: DatepickerCellRenderer,
        selectCellRenderer: SelectCellRenderer,
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
            {headerName:'SFA_PRJ_NO', field:'sfaPrjNo', width:150, editable:true, cellStyle : {'textAlign': 'center'}},
            {headerName: '사업유형', field:'divisionCd', headerClass: 'require-column', width:110, cellRenderer: 'selectCellRenderer', cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["DIVISION_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName: '입찰형태', field:'estimateCd', width:110, cellRenderer: 'selectCellRenderer', cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["ESTIMATE_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName: '동시운송구분', field:'onceShipCd', width:130, cellRenderer: 'selectCellRenderer', cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["ONCE_SHIP_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName: '오더번호', field:'orderNo', width:110, editable:true},
            {headerName: '프로젝트명', field:'projectNm', headerClass: 'require-column', width:300, editable:true, cellEditor: 'agLargeTextCellEditor',
              cellEditorPopup: true, tooltipField: 'projectNm'},
            {headerName: '운송조건', field:'shipCd', width:110, cellRenderer: 'selectCellRenderer', cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["SHIP_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName:'도착지', field:'arrivalArea', width:200, editable:true, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'arrivalArea'},
            {headerName:'제품상세', field:'productDesc', width:300, editable:true, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'productDesc'},
            {headerName:'수량', field:'expAmount', width:80, editable:true, cellStyle : {'textAlign': 'right'},},
            {headerName:'본품중량(KG/대)', field:'mainWeight', width:160, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }},
            {headerName:'본품CBM(대)', field:'mainCbm', width:140, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }},
            {headerName:'부품중량(KG/대)', field:'partWeight', width:160, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }},
            {headerName:'부품CBM(대)', field:'partCbm', width:140, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, '', 'excRt');
              }},
            {headerName:'등록일자', field:'regDtm', width:110, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'center'},
              valueFormatter: (params) => {
                return this.$moment(params.value).format('YYYY-MM-DD');
              }},
            {headerName:'마감일자', cellRenderer: 'datePicker', field:'closeDt', width:110, editable:true, cellStyle : {'textAlign': 'center'}},
            {headerName:'등록자', field:'regNm', width:100, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'center'}},
            {headerName:'수주여부', field:'orderYn', width: 110, cellRenderer: 'checkboxRenderer', cellStyle : {'textAlign': 'center'}, cellClass: 'bg-grey', editable:false},
            {headerName:'비고', field:'remark', width:300, editable:true, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'remark'},
            {headerName:'', field:'headerId', hide : true},
            {headerName:'', field:'comCd', hide : true},
        ];

        this.columnDefsSub = [
            {headerName: 'No.', width : 80, valueGetter: (params) => {return params.node.rowIndex + 1 }},
            {headerName: '예가업체', field:'expBusinessCd',headerClass: 'require-column',  width:130, cellRenderer: 'selectCellRenderer', cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["EXP_BUSINESS_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName: '선적모드', field:'shipModeCd',headerClass: 'require-column',  width:110, cellRenderer: 'selectCellRenderer', cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["SHIP_MODE_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName:'제출일자', cellRenderer: 'datePicker', headerClass: 'require-column', field:'submitDt', width:120, editable:true, cellStyle : {'textAlign': 'center'}},
            {headerName:'국내운송료', field:'localShipAmt', width:150, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
            }},
            {headerName:'국내기타부대비용', field:'localEtcAmt', width:160, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
            }},
            {headerName:'해상항공금액', field:'seaAirAmt', width:150, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }},
            {headerName: '해상항공통화', field:'seaAirCurCd',  width:130, cellRenderer: 'selectCellRenderer', cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["CUR_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName:'해상항공환율', field:'seaAirExcRt', width:130, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }},
            {headerName:'현지운송료', field:'fieldShipAmt', width:150, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
            }},
            {headerName:'현지기타부대비용', field:'fieldEtcAmt', width:160, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
            }},
            {headerName:'현지하차비', field:'fieldOffAmt', width:150, editable:true, cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
            }},
            {headerName: '현지통화', field:'fieldCurCd',  width:110, cellRenderer: 'selectCellRenderer', cellStyle : {'textAlign': 'center'},
              cellRendererParams: {
                options : that.options["CUR_CD"],
                detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
              }
            },
            {headerName:'현지환율', field:'fieldExcRt', width:110, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }},
            {headerName:'합계(KRW)', field:'korAmt', width:150, editable:false, cellClass: 'bg-grey', cellStyle : {'textAlign': 'right'},
              valueFormatter: (params) => {
                return that.chkNumber(params, 'sub', 'excRt');
              }},
            {headerName:'비고', field:'remark', width:300, editable:true, cellEditor: 'agLargeTextCellEditor', cellEditorPopup: true, tooltipField: 'remark'},
            {headerName:'', field:'headerId', hide : true},
            {headerName:'', field:'detailId', hide : true},
            {headerName:'', field:'comCd', hide : true},
        ];
    },
    //Main 그리드 SelctionChange 발생
    onSelectionChangedMain() {
        const voRow = this.gridApi.getSelectedRows() //선택된 Row
        this.$store.commit('loading')
        this.$http
          .post("/api/expect/detail", {
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
      this.$http.post(`/api/expect/list`, param)
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

      if(!this.gridRequireCheck([this.$refs.gridMain, this.$refs.gridSub ])) return;

      let custCnt = 0;
      for(var i=0; i<this.subData.length; i++){
          if(this.subData[i].expBusinessCd === '99') custCnt = custCnt+1;
      }
      if(custCnt > 1) {
        this.$swal({ type: "error", text: "물류팀은 한건만 저장이 가능합니다." });
        return;
      }

      //저장 시작
      this.$http
          .put('/api/expect/save',{expectHeader : this.data, expectDetail : this.subData})
          .then(response => {
            this.$swal({ type: 'success', text: '저장되었습니다.' });
            this.goSearch()
          })
          .catch(({ message }) => {
            if (message === "Request failed with status code 500") {
              this.$swal({ type: "error", text: "저장 중 오류가 발생하였습니다." });
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
              this.$http.delete('/api/expect/delete/' + headerId
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

      //운송조건
      let aShipOptions = this.options['SHIP_CD']
      let sShipDetailCd = null
      if (Array.isArray(aShipOptions) && aShipOptions.length > 0) {
        sShipDetailCd = aShipOptions[0].key
      }

      /*
      this.$http.get(`/api/expect/headerId`)
      .then(response => {
        let newHeaderId ;
        newHeaderId = response.data
*/
        this.$http.get(`/api/expect/orderNo`,{params : {headerId : '', divisionCd :  sDivisionDetailCd}})
            .then(response => {
              let orderNo = response.data

              this.data.push({
                divisionCd : sDivisionDetailCd,
                orderNo : orderNo,
                projectNm : "",
                shipCd : sShipDetailCd,
                arrivalArea : "",
                productDesc : "",
                closeDt : "",
                regNm : this.$store.state.loginInfo.userName,
                orderYn: "N",
                headerId : "",
                compCd : this.$store.state.loginInfo.compCd,
              });

            }).catch(response => {
          console.error(response)
        })
//      })
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
          text: '선택된 정보를 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            const rowId = row[0].id;
            const isNew = vm.subData[rowId].new
            const headerId = vm.subData[rowId].headerId
            const detailId = vm.subData[rowId].detailId
            this.subData.splice(rowId, 1)
            if (!isNew && detailId != undefined) {
              vm.$http.delete('/api/expect/delete/' + headerId + '/' + detailId
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

        if(row.length < 1)
         throw '그룹코드가 선택되지 않았습니다.'

        const rowId = row[0].id

        if(this.data[rowId].headerId === "")
          throw '그룹코드 저장 후 입력해주세요.'

        //선적모드코드
        let aShipModeOptions = this.options['SHIP_MODE_CD']
        let sShipModeDetailCd = null
        if (Array.isArray(aShipModeOptions) && aShipModeOptions.length > 0) {
          sShipModeDetailCd = aShipModeOptions[1].key
        }

        let aCurOptions = this.options['CUR_CD']
        let sCurDetailCd = null
        if (Array.isArray(aCurOptions) && aCurOptions.length > 0) {
          sCurDetailCd = aCurOptions[1].key
        }


        this.subData.push({
          expBusinessCd : "",
          shipModeCd : sShipModeDetailCd,
          submitDt : "",
          localShipAmt : 0,
          localEtcAmt : 0,
          seaAirAmt : 0,
          seaAirCurCd : sCurDetailCd,
          seaAirExcRt : "",
          fieldShipAmt : 0,
          fieldEtcAmt : 0,
          fieldOffAmt : 0,
          fieldCurCd : sCurDetailCd,
          fieldExcRt : "",
          korAmt : "",
          remark : "",
          compCd: this.data[rowId].compCd,
          headerId: this.data[rowId].headerId
        });
      }catch (e) {
        this.$swal({
          type: 'warning',
          text: e
        })
      }
    },
    cellValueChange(params){
      if(params.colDef.field === 'divisionCd'){
        const rowNode = params.api.getRowNode(params.node.id);
        this.getOrderNo(params.node.id, rowNode.data.headerId, rowNode.data.divisionCd);
      }
    },
    cellValueChangeSub(params){

      if(params.colDef.field === 'submitDt'
      || params.colDef.field === 'seaAirCurCd'
      ||params.colDef.field === 'seaAirAmt'){
        const rowNode = params.api.getRowNode(params.node.id);
        this.frgnChangEvent(params.node.id, rowNode.data.submitDt, rowNode.data.seaAirCurCd, rowNode.data.seaAirAmt, 'SEA_AIR_CUR_CD');
      }
      if(params.colDef.field === 'submitDt'
          || params.colDef.field === 'fieldCurCd'
          ||params.colDef.field === 'fieldShipAmt'
          ||params.colDef.field === 'fieldEtcAmt'
          ||params.colDef.field === 'fieldOffAmt'){
        const rowNode = params.api.getRowNode(params.node.id);
        let fieldAmt = rowNode.data.fieldShipAmt + rowNode.data.fieldEtcAmt + rowNode.data.fieldOffAmt
        this.frgnChangEvent(params.node.id, rowNode.data.submitDt, rowNode.data.fieldCurCd, fieldAmt, 'FIELD_CUR_CD');
      }
      if (params.colDef.field === 'localShipAmt'
          || params.colDef.field === 'localEtcAmt'){
        this.calcTotAmt(params.node.id);
      }

    },
    frgnChangEvent(rId, submitDt, curCd, amt, type){

      let f = this.options[type].filter(x => x.key === curCd)[0]
      if (curCd === undefined || f === undefined) {
        f = this.options[type][0] || {}
      }

      let excDt = submitDt.replace(/-/gi,'')
      let tmp_excRt;
      let tmp_krwTotAmt;

      this.$http.get('/api/exchange/rate/'+curCd+'/'+excDt)
          .then(response => {
            let data = response.data[0]
            tmp_excRt = this.$numeral(data.convRate).value();
            tmp_krwTotAmt = amt * tmp_excRt
            tmp_krwTotAmt = Math.round(tmp_krwTotAmt)
            this.setExchangeValue(rId, tmp_krwTotAmt, tmp_excRt, type);
          }).catch(response => {
            if(curCd !== 'KRW' ){
              this.$swal({type: 'warning', text: '환율일자에 대한 환율값이 없습니다.'});
            }
            tmp_excRt = 1
            tmp_krwTotAmt = Math.floor(amt * tmp_excRt)
            this.setExchangeValue(rId, tmp_krwTotAmt, tmp_excRt, type);
      })
    },
    setExchangeValue(rId, totalAmt, excRt, type){
      const rowNode = this.gridOptionsSub.api.getRowNode(rId);

      if(type === 'SEA_AIR_CUR_CD'){
        rowNode.setDataValue('seaAirExcRt', excRt);
        this.subData[rId].seaAirExcRt = excRt;
      }
      if(type === 'FIELD_CUR_CD'){
        rowNode.setDataValue('fieldExcRt', excRt);
        this.subData[rId].fieldExcRt = excRt;
      }
      this.calcTotAmt(rId);
    },
    calcTotAmt(rId){

      const rowNode = this.gridOptionsSub.api.getRowNode(rId);

      let seaAirTotalAmt;
      let fieldTotalAmt;
      let localTotalAmt;
      let korAmt;

      seaAirTotalAmt = this.subData[rId].seaAirExcRt * (this.subData[rId].seaAirAmt || 0);
      fieldTotalAmt = this.subData[rId].fieldExcRt * ((this.subData[rId].fieldShipAmt || 0) + (this.subData[rId].fieldEtcAmt || 0) + (this.subData[rId].fieldOffAmt || 0));
      localTotalAmt = (this.subData[rId].localShipAmt || 0)  + (this.subData[rId].localEtcAmt|| 0);
      korAmt = Math.round( localTotalAmt +  seaAirTotalAmt + fieldTotalAmt);

      rowNode.setDataValue('korAmt', korAmt);
      this.subData[rId].korAmt = korAmt;
    },
    getOrderNo(rId, headerId, divisionCd){
      const rowNode = this.gridOptions.api.getRowNode(rId);
      this.$http.get(`/api/expect/orderNo`,{params : {headerId : headerId, divisionCd :  divisionCd}})
          .then(response => {
            let orderNo = response.data
            rowNode.setDataValue('orderNo', orderNo)
            this.data[rId].orderNo = orderNo;
          }).catch(response => {
        console.error(response)
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
    //운송코드 공통코드 조회(운송조건)
    queryShipCd() {
      this.$store.commit('loading')
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'SHIP_CD'
        }
      }).then(response => {
        this.options['SHIP_CD'] = response.data
        //bus.$emit('selectBox.updated')
      }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    //예가업체코드 공통코드 조회(업체코드)
    queryExpBusinessCd() {
      this.$store.commit('loading')
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'EXP_BUSINESS_CD'
        }
      }).then(response => {
        this.options['EXP_BUSINESS_CD'] = response.data
        //bus.$emit('selectBox.updated')
      }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    //선적모드코드 공통코드 조회(선적모드)
    queryShipModeCd() {
      this.$store.commit('loading')
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'SHIP_MODE_CD'
        }
      }).then(response => {
        this.options['SHIP_MODE_CD'] = response.data
        //bus.$emit('selectBox.updated')
      }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    queryCurCd(){
      this.$store.commit('loading')
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'CUR_CD'
        }
      }).then(response => {
        this.options['CUR_CD'] = response.data
        //bus.$emit('selectBox.updated')
      }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    //입찰형태 공통코드 조회(입찰형태)
    queryEstimateCd() {
      this.$store.commit('loading')
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'ESTIMATE_CD'
        }
      }).then(response => {
        this.options['ESTIMATE_CD'] = response.data
        //bus.$emit('selectBox.updated')
      }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    //동시운송구분 공통코드 조회(동시운송)
    queryOnceShipCd() {
      this.$store.commit('loading')
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'ONCE_SHIP_CD'
        }
      }).then(response => {
        let selectOptions = response.data
        selectOptions.unshift({
          key: undefined,
          value: ' '
        })
        this.options['ONCE_SHIP_CD'] = selectOptions
        //bus.$emit('selectBox.updated')
      }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    changeTheme(){
      this.theme = "ag-theme-balham";
      this.$router.push({path : '/expPriceReg', name:'expPriceReg', query: {theme: this.theme}}).catch(()=>{
        this.$router.push('expPriceReg');
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
    this.queryShipCd();
    this.queryExpBusinessCd();
    this.queryShipModeCd();
    this.queryCurCd();
    this.queryEstimateCd();
    this.queryOnceShipCd();
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