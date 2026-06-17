<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch()">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save()">저장</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="item search_wrap">
        <div class="search_box">
          <div class="search_title">
            <span class="search_tit" style="color: #CC3D3D;">- 사용일자</span>
          </div>
          <div class="search_con">
            <el-date-picker
                v-model="searchDt"
                type="daterange"
                align="right"
                unlink-panels
                style="width: 260px;"
                range-separator="~"
                start-placeholder="시작일"
                end-placeholder="종료일">
            </el-date-picker>
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
                  <span class="search_tit">- 사용일자</span>
                </div>
                <div class="search_con search-area">
<!--                  <div class="datepicker w-type-ymd02 w45p">-->
<!--                    <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />-->
<!--                  </div>-->
<!--                  <span class="datepicker w10p dp-ib tac"> ~ </span>-->
<!--                  <div class="datepicker w-type-ymd02 w45p">-->
<!--                    <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />-->
<!--                  </div>-->
                  <el-date-picker
                      v-model="searchDt"
                      type="daterange"
                      align="right"
                      unlink-panels
                      style="width: 100%;"
                      range-separator="~"
                      start-placeholder="시작일"
                      end-placeholder="종료일">
                  </el-date-picker>
                </div>
              </div>


              <div v-if="cardUseFlag || (airCardUser && form.cardType === 'F')" style="margin-bottom: 20px;">

                <div class="search_box_pop">
                  <div class="search_title">
                    <span class="search_tit">- 카드소유자</span>
                  </div>
                  <div class="search_con search-area">
                    <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.crdPssrDut" disabled>
                    <input class="input dp-ib input-bg w30p_5r" type="text" v-model="form.crdPssrDept" disabled>
                    <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.userId" disabled>
                    <div class="dp-ib w30p">
                      <input class="input" type="text" v-model="form.userNm" @input="initEmp" @keypress.enter="popEmp">
                      <button class="icon is-right" @click="popEmp"><i class="fas fa-search"></i>
                      </button>
                    </div>
                  </div>
                </div>

                <div class="search_box_pop">
                  <div class="search_title">
                    <span class="search_tit">- 예산부서</span>
                  </div>
                  <div class="search_con search-area">
                    <input class="input input-bg w30p_5r" type="text" v-model="form.cctrCd" disabled>
                    <div class="dp-ib w70p">
                      <input class="input" type="text" v-model="form.cctrNm" @input="initErpCctr" @keypress.enter="popCctr('c')">
                      <button class="icon is-right" @click="popCctr('c')"><i class="fas fa-search"></i>
                      </button>
                    </div>
                  </div>
                </div>

                <div class="search_box_pop">
                  <div class="search_title">
                    <span class="search_tit">- 귀속부서</span>
                  </div>
                  <div class="search_con search-area">
                    <input class="input input-bg w30p_5r" type="text" v-model="form.blgDeptCd" disabled>
                    <div class="dp-ib w70p">
                      <input class="input" type="text" v-model="form.blgDeptNm" @input="initBlgCctr" @keypress.enter="popCctr('b')">
                      <button class="icon is-right" @click="popCctr('b')"><i class="fas fa-search"></i>
                      </button>
                    </div>
                  </div>
                </div>

              </div>

              <!-- 권한 유저가 아닌경우(일반사용자) -->
              <div v-else style="margin-bottom: 20px;">

                <div class="search_box_pop">
                  <div class="search_title">
                    <span class="search_tit">- 카드소유자</span>
                  </div>
                  <div class="search_con search-area">
                    <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.crdPssrDut" disabled>
                    <input class="input dp-ib input-bg w30p_5r" type="text" v-model="form.crdPssrDept" disabled>
                    <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.userId" disabled>
                    <div class="dp-ib w30p">
                      <input class="input" type="text" v-model="form.userNm" @input="initEmp" @keypress.enter="popEmp">
                      <button class="icon is-right" @click="popEmp"><i class="fas fa-search"></i>
                      </button>
                    </div>
                  </div>
                </div>

                <div class="search_box_pop" >
                  <div class="search_title">
                    <span class="search_tit">- 예산부서</span>
                  </div>
                  <div class="search_con search-area">
                    <input class="input input-bg w30p_5r" type="text" v-model="form.cctrCd" disabled>
                    <div class="dp-ib w70p">
                      <input class="input" type="text" v-model="form.cctrNm" @input="initErpCctr" @keypress.enter="popCctr('a')" disabled>
                      <button class="icon is-right" @click="popCctr('a')"><i class="fas fa-search"></i>
                      </button>
                    </div>
                  </div>
                </div>

              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 진행상태</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth w100p" v-model="form.status">
                    <option value=''>==전체==</option>
                    <option
                        v-for="item in progressStatusCds"
                        :key="item.key"
                        :value="item.key"
                        v-text="item.value"/>
                  </select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 항공권내역여부</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth w100p" v-model="form.cardType">
                    <option value=''>==전체==</option>
                    <option value='A'>항공권 내역 제외</option>
                    <option value='F'>항공권 내역</option>
                  </select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 카드번호</span>
                </div>
                <div class="search_con search-area">
                  <input class="input Rt-M tal w100p" type="text" v-model="form.crdNo">
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
                  <button @click="resetSearch()" class="bt_gray_s"><i class="fas fa-undo"></i></button><!-- 검색 초기화 버튼 -->
                </li>
              </ul>


            </div>

          </div>
        </div>
        <!-- //상세검색 내용 -->
      </div>
    </div>
    <!-- //검색조건 영역 -->

    <!-- 테이블 -->
    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">법인카드 사용내역</h3>
          </div>
          <div class="btn-type1 clearfix">
            <div style="float:left; padding-bottom:10px; margin-right:10px;"> 총 금액 : {{$numeral(totalAmt).format('0,0')}} </div>
            <el-button type="info" icon="el-icon-check" @click="changeUsed">개인용도 변경</el-button>
            <el-button v-if="cardUseFlag" type="danger" icon="el-icon-delete" @click="cancelUsed()">개인용도 취소</el-button><!-- 권한을 가진 유저만 취소 가능하게 -->
            <el-button type="success" icon="el-icon-document-checked" @click="saveExcel()">엑셀 저장</el-button>
          </div>
        </div>

        <div class="grid-wrap">
          <div id="gridbox" class="grid-tbl-box"></div>
          <div id="pagingbox" class="pagingbox" style="display:none"></div>
        </div>

        <div class="grid-wrap">
          <!-- <dhx-grid class="slip-grid" ref="grid" v-model="useDetails" :config="config" @constructGridSuccessful="constructGridSuccessful"/> -->
          <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height3"
                       :columnDefs="columnDefs"
                       :rowData="useDetails"
                       :gridOptions="gridOptions"
                       :defaultColDef="defaultColDef"
                       :frameworkComponents="frameworkComponents"
                       :suppressRowClickSelection="true"
                       rowSelection="multiple"
                       @grid-ready="onGridReady"
                       @cell-value-changed="cellValueChange"
                       @row-selected="onRowSelected"/>
        </div>
      </div>
    </div>
    <!-- //테이블 -->

    <!--팝업-->
    <!-- <b-modal :active.sync="showEmpModal" has-modal-card @receive="receiveEmp">
        <emp :param="form.crdPssrNm"></emp>
    </b-modal> -->
    <!--//팝업 -->
  </div>
</template>

<script>
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import _ from 'lodash'
import DhxCalendar from '@/components/DhxCalendar.vue'
import Emp from '@/components/Emp_Ag.vue';
import DeptEmp from '@/components/DeptEmp.vue';
import { AgGridVue } from 'ag-grid-vue'
import CheckboxCellRenderer from "@/components/agGrid/checkbox-cell-renderer";
import Cctr from "@/components/Cctr_Ag";
import Cctr2 from "@/components/Cctr2";
import CctrDel from "@/components/CctrDelegate";
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn";

export default {
  name: 'CardUseLst',
  mixins: [mixin, mixinSlip],
  components: {Emp, DhxCalendar, AgGridVue},
  data() {
    return {
      columnDefs : [],
      gridOptions : {
        statusBar: {
          statusPanels: [
            {
              statusPanel: 'agTotalAndFilteredRowCountComponent',
              align: 'left',
            },
          ]
        },
      },
      defaultColDef:{},
      frameworkComponents: {
        checkboxRenderer: CheckboxCellRenderer,
        schBtn : AgGridSearchBtn,
      },
      title: '법인카드 사용현황',
      totalAmt:0, // 전체금액 변수
      showUsedTimeUsers: [],
      compCds: [],
      crdCompCds: [],
      taxTypeCds: [],
      useDtlsStatCds: [],
      progressStatusCds: [],
      slipStatusCds: [],
      crdFgCds: [],
      useDetails: [],
      cardUseRole: [],
      cardUseFlag: false,
      airCardUser: false,
      rowNode: '',
      showEmpModal: false,
      showUsedTime: false,
      authority: '',
      searchDt: [this.$moment().add(-3, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')],
      form: {
        compCd: '',
        crdCompCd: '',
        useDtlsStatCd: '',
        crdFgCd: '',
        crdNo: '',
        userId: '',
        userNm: '',
        crdPssrTitle: '',
        crdPssrDut: '',
        crdPssrDept: '',
        cctrCd: "",
        cctrNm: "",
        blgDeptCd: "",
        blgDeptNm: "",
        cardType: "A",
        status: '',
        searchDtmFr: this.$moment().add(-3, 'month').startOf('month').format('YYYY-MM-DD'),
        searchDtmTo: this.$moment().format('YYYY-MM-DD'),
      },
      loginDeptCd: ''
    };
  },
  methods: {
    onGridReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    makeColDef(){
      const that = this;
      this.columnDefs = [
        {
          headerName: '',
          headerCheckboxSelection: true,
          checkboxSelection: true,
          field:'regYn',
          width : 80,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: 'No.',
          field: 'rn',
          width: 80,
          colSpan:(params) => {
            if(params.data.rn === '합계'){
              return 9;
            }else{
              return 1;
            }
          },
          cellStyle:(params) => {
            if(params.data.rn === '합계'){
              return {textAlign: 'center'}
            }else{
              return {textAlign: 'center'}
            }
          },
          valueFormatter: (params) => {
            if(params.data.rn === '합계'){
              return '합계'
            }else{
              return params.node.rowIndex+1;
            }
          }
        },
        {
          headerName: '사용일자',
          field: 'usedDt',
          width: 120,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(params.value);
          },
          editable:false
        },
        {
          headerName: '사용시간',
          field: 'usedTime',
          width: 100,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getTimeFormat(params.value);
          },
          editable:false,
          hide:!this.showUsedTime,
        },
        {
          headerName: '매입일자',
          field: 'buyDt',
          width: 120,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(params.value);
          },
          editable:false
        },
        {
          headerName: '카드번호',
          field: 'cardNo',
          width: 190,
          cellStyle:{textAlign: 'left'},
          valueFormatter: (params) => {
            return that.getFormattedPureCardNumber(params.value);
          },
          editable:false
        },
        {
          headerName: '가맹점명',
          field: 'storeNm',
          width: 260,
          cellStyle:{textAlign: 'left'}
          ,editable:false
        },
        {
          headerName: '업종명',
          field: 'mccNm',
          width: 120,
          cellStyle:{textAlign: 'left'}
          ,editable:false
        },
        {
          headerName: '사업자번호',
          field: 'irsNo',
          width: 120,
          cellStyle:{textAlign: 'center'},
          editable:false
        },
        {
          headerName: '가맹점주소',
          field: 'storeAddr',
          width: 280,
          cellStyle:{textAlign: 'left'}
          ,editable:false
        },
        {
          headerName: '사용금액',
          field: 'usedAmt',
          width: 120,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          },
          comparator: function (valueA, valueB) {
            return Number(valueA) - Number(valueB);
          },
          editable:false
        },
        {
          headerName: '공급가액',
          field: 'originAmt',
          width: 120,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          },
          comparator: function (valueA, valueB) {
            return Number(valueA) - Number(valueB);
          },
          editable:false
        },
        {
          headerName: '부가세',
          field: 'surtax',
          width: 120,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          },
          comparator: function (valueA, valueB) {
            return Number(valueA) - Number(valueB);
          },
          editable:false
        },
        {
          headerName: '봉사료',
          field: 'serviceCharge',
          width: 120,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          },
          comparator: function (valueA, valueB) {
            return Number(valueA) - Number(valueB);
          },
          editable:false
        },
        {
          headerName: '진행상태',
          field: 'status',
          width: 90,
          cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return that.chkStatCd(params.value)
          },
          editable:false
        },
        {
          headerName: '취소구분',
          field: 'cancelFlag',
          width: 120,
          cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return that.chkCancel(params.value)
          }
          ,editable:false
        },
        {
          headerName: '승인번호',
          field: 'apprNo',
          width: 120,
          cellStyle:{textAlign: 'center'}
          ,editable:false
        },
        {
          headerName: '사원번호',
          field: 'userId',
          width: 90,
          cellStyle:{textAlign: 'center'},
          editable:false
        },
        {
          headerName: '직급',
          field: 'jobGradeNm',
          width: 100,
          cellStyle:{textAlign: 'center'},
          editable:false
        },
        {
          headerName: '카드사용자',
          field: 'userNm',
          width: 110,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'schBtn',
          cellRendererParams(params) {
            return {
              funcNm: 'updateEmpItem',
              valFuncNm: 'updateEmpItemVal',
              disable : that.chkStatus(params.data.status)
            }
          },
          editable: function(params){
            if(that.chkStatus(params.data.status)){
              return false
            }else{
              return true
            }
          },
        },
        {
          headerName: '국내/해외',
          field: 'abroadFlag',
          width: 140,
          cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return that.chkAbroad(params.value)
          }
          ,editable:false
        },
        {
          headerName: '항공권발권자',
          field: 'temp1',
          width: 140,
          cellStyle:{textAlign: 'center'}
          ,editable:false
        },
        {
          headerName: '항공권발권번호',
          field: 'temp3',
          width: 160,
          cellStyle:{textAlign: 'center'}
          ,editable:false
        },
        {
          headerName: '제품군',
          field: 'productNm',
          width: 100,
          cellStyle:{textAlign: 'left'}
          ,editable:false
        },
        {
          headerName: '실행예산 부서',
          field: 'cctrNm',
          width: 150,
          cellStyle:{textAlign: 'left'}
          ,editable:false
        },
        {
          headerName: '귀속 부서',
          field: 'deptNm',
          width: 160,
          cellStyle:{textAlign: 'left'},
          editable:false
        },
        {
          headerName: '계정명',
          field: 'trAcctNm',
          width: 100,
          cellStyle:{textAlign: 'center'}
          ,editable:false
        },
        {
          headerName: '세금코드',
          field: 'taxCode',
          width: 120,
          cellStyle:{textAlign: 'center'}
          ,valueFormatter: function(params) {
            return that.chkTax(params.value)
          }
          ,editable:false
        },
        {
          headerName: '과세구분',
          field: 'taxType',
          width: 120,
          cellStyle:{textAlign: 'center'}
          ,editable:false
        },
        {
          headerName: '프로젝트명',
          field: 'pjtNm',
          width: 140,
          cellStyle:{textAlign: 'center'}
          ,editable:false
        },
        {
          headerName: '태스크',
          field: 'taskName',
          width: 100,
          cellStyle:{textAlign: 'left'}
          ,editable:false
        },
        {
          headerName: '적요',
          field: 'ttext',
          width: 100,
          cellStyle:{textAlign: 'left'}
          ,editable:false
        },
        {
          headerName: '전표번호',
          field: 'slipNo',
          width: 120,
          cellStyle:{textAlign: 'center'}
          ,editable:false
        },
        {
          headerName: '전표상태',
          field: 'slipStatus',
          width: 120,
          valueFormatter: function(params) {
            return that.chkSlipStatCd(params.value)
          },
          cellStyle:{textAlign: 'center'}
          ,editable:false
        },
      ];
    },
    makeSumData(){
      const initValue = 0;

      let sum = [{
        rn: '합계',
        usedAmt: this.useDetails.reduce((prev, next) => {return Number(prev) + Number(next.usedAmt)}, initValue),
        originAmt: this.useDetails.reduce((prev, next) => {return Number(prev) + Number(next.originAmt)}, initValue),
        surtax: this.useDetails.reduce((prev, next) => {return Number(prev) + Number(next.surtax)}, initValue),
        serviceCharge: this.useDetails.reduce((prev, next) => {return Number(prev) + Number(next.serviceCharge)}, initValue)
      }]

      this.gridOptions.api.setPinnedTopRowData(sum)
    },
    onCellClicked(params) {

      this.rowNode = params.node;
    },
    getShowUsedTime(userId) {
      return this.$http.get(`/api/code/combo`, { params: { groupCd: "CARD_USED_TIME_SHOW_USER" } })
          .then(response => {
            this.showUsedTime = response.data.some(role => role.key === userId);
          });
    },
    getCompCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "COMP_CD"}})
          .then(response => {
            this.compCds = response.data;
          });
    },
    getCardCompCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "CRD_COMP_CD"}})
          .then(response => {
            this.crdCompCds = response.data;
          });
    },
    getUseStatCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "USE_DTLS_STAT_CD"}})
          .then(response => {
            this.useDtlsStatCds = response.data;
          });
    },
    getCardFgCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "CRD_FG_CD"}})
          .then(response => {
            this.crdFgCds = response.data;
          });
    },
    getTaxTypeCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "TAX_TYPE_CD"}})
          .then(response => {
            this.taxTypeCds = response.data;
          });
    },
    getProgressStatusCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "PROGRESS_STATUS_CD"}})
          .then(response => {
            this.progressStatusCds = response.data;
          });
    },
    getSlipStatusCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "SLIP_STAT_CD"}})
          .then(response => {
            this.slipStatusCds = response.data;
          });
    },
    getCardUseRole() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "CARD_USE_ROLE_CD"}})
          .then(response => {
            // this.cardUseRole = response.data;
            response.data.forEach(x=>{
              if(this.authority ===  x.key){
                this.cardUseFlag = true;
              }
            })

            if(!this.cardUseFlag) {
              this.form.cctrCd = this.$store.state.loginInfo.loginCctrCd;
              this.form.cctrNm = this.$store.state.loginInfo.loginCctrNm;
              this.form.blgDeptCd = '';
              this.form.blgDeptNm = '';
              this.form.userId = this.$store.state.loginInfo.loginId;
              this.form.userNm = this.$store.state.loginInfo.userName;
              this.form.crdPssrDut = this.$store.state.loginInfo.loginJobDutNm;
              this.form.crdPssrDept = this.$store.state.loginInfo.loginDeptNm;
            }

          });
    },
    getAirCardUseRole() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "AIR_CARD_USE_ROLE_USER"}})
          .then(response => {
            response.data.forEach(x=>{
              if(this.$store.state.loginInfo.loginId ===  x.key){
                this.airCardUser = true;
              }
            })
          });
    },
    getNumberFormat(value){
      if(value) {
        return this.$numeral(value).format('-0,0');
      }
    },
    mainGridRefresh(idx) {
      var rows = [];
      var rowNode = this.gridApi.getDisplayedRowAtIndex(idx);
      rows.push(rowNode);
      this.gridApi.redrawRows({ rowNodes: rows });
    },
    cancelUsed(){

      let vm = this;
      const list = this.gridApi.getSelectedRows();
      let flag = false;

      list.forEach(x=>{
        if(x.status !== '07') flag = true;
      })

      if(flag){
        this.$swal({ type: 'warning', text: '진행 상태가 개인용도인 경우만 취소 가능합니다.' });
        return;
      }

      vm.result = [];

      if(list.length > 0){
        this.$swal({
          type: 'question',
          html: '선택한 항목을 개인용도 취소 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            vm.result = list;
            this.$http.post(`/api/card/useList/cancel`, vm.result)
                .then((response) => {
                  this.$swal({ type: 'success', text: '개인용도를 취소 하였습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.goSearch();
                        }
                      });
                })
                .catch((e) => {
                  this.$swal({ type: 'warning', text: '개인용도 취소를 실패 하였습니다.' })
                })
                .finally(() => {
                  this.$store.commit('finish')
                });
          }
        })
      } else {
        this.$swal({ type: 'info', text: '취소할 항목을 선택하세요.' });
      }

    },
    changeUsed(){

      const vm = this;
      // let list = this.useDetails.filter(v => v.regYn);
      const list = this.gridApi.getSelectedRows();
      let flag = false;

      list.forEach(x=>{
        if(x.status !== '01') flag = true;
      })

      if(flag){
        this.$swal({ type: 'warning', text: '진행 상태가 미처리인 경우만 변경 가능합니다.' });
        return;
      }

      vm.result = [];

      if(list.length > 0){
        this.$swal({
          type: 'question',
          html: '선택한 항목을 개인용도로 변경 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            vm.result = list;
            this.$http.post(`/api/card/useList/change`, vm.result)
                .then((response) => {
                  this.$swal({ type: 'success', text: '개인용도로 변경 하였습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.goSearch();
                        }
                      });
                })
                .catch((e) => {
                  this.$swal({ type: 'warning', text: '개인용도 변경을 실패 하였습니다.' })
                })
                .finally(() => {
                  this.$store.commit('finish')
                });
          }
        })
      } else {
        this.$swal({ type: 'info', text: '변경할 항목을 선택하세요.' });
      }

    },
    save(){

      const vm = this;
      const list = this.gridApi.getSelectedRows();
      let flag = false;

      list.forEach(x=>{
        if(x.status == '01' || x.status == 'CR' || x.status == 'SD' || x.status == 'FR' || x.status == 'SC'){
          flag = true;
        }else if(vm.isEmpty(x.userId) == ""){
          flag = true;
        }
      })

      if(!flag){
        this.$swal({ type: 'warning', html: '변경이 불가능합니다<br/>진행 상태를 확인해주세요.' });
        return;
      }

      vm.result = [];

      if(list.length > 0){
        this.$swal({
          type: 'question',
          html: '선택한 항목을 저장 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            vm.result = list;
            this.$http.post(`/api/card/useList/emp/update`, vm.result)
                .then((response) => {
                  this.$swal({ type: 'success', text: '저장 하였습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.goSearch();
                        }
                      });
                })
                .catch((e) => {
                  this.$swal({ type: 'warning', text: '저장을 실패 하였습니다.' })
                })
                .finally(() => {
                  this.$store.commit('finish')
                });
          }
        })
      } else {
        this.$swal({ type: 'info', text: '저장할 항목을 선택하세요.' });
      }

    },
    saveExcel() {
      this.gridOptions.api.exportDataAsExcel({fileName: '법인카드 사용현황 수신내역'});
    },
    compareDate(value){
      if(!value){
        this.$swal({ type: 'info', text: '사용일자를 입력해주세요.' });
        return false;
      }
    },
    goSearch() {


      if(!this.cardUseFlag && !this.airCardUser && this.form.cctrCd === "") {
        this.$swal({type: 'error', text: '부서를 선택하여주세요.'});
        return false;
      }

      // if(this.airCardUser && this.form.cardType ==='F'){
      //   // pass
      // }else
      // if(!this.cardUseFlag && this.form.blgDeptCd !== this.$store.state.loginInfo.loginDeptCd){
      //   this.$swal({ type: 'error', text: '소속된 부서의 현황만 조회 가능합니다.' });
      //   return false;
      // }

      this.compareDate(this.searchDt);
      this.form.searchDtmFr = this.$moment(this.searchDt[0]).format("YYYYMMDD");
      this.form.searchDtmTo = this.$moment(this.searchDt[1]).format("YYYYMMDD");

      const param = JSON.parse(JSON.stringify(this.form));

      // const stripFields = ['searchDtmFr', 'searchDtmTo','crdNo'];
      const stripFields = ['crdNo'];
      _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

      this.$store.commit('loading');
      this.$http.post(`/api/card/useList`, {
            compCd: param.compCd,
            searchDtmFr: param.searchDtmFr,
            searchDtmTo: param.searchDtmTo,
            useDtlsStatCd: param.useDtlsStatCd,
            cctrCd : param.cctrCd,
            blgDeptCd : param.blgDeptCd,
            status : param.status,
            cardType: param.cardType,
            cardNo: param.crdNo,
            userId: param.userId
          }
      )
          .then(response => {
            if (response.data) {
              this.useDetails = response.data;
              this.makeSumData();
            }
          }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish');
      });
      document.getElementById("open-moda").style.opacity = "0";
      document.getElementById("open-moda").style.pointerEvents = "none";
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
      this.searchDt = [this.$moment().add(-3, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')];
      this.form.compCd = '';
      this.form.crdCompCd = '';
      this.form.useDtlsStatCd = '';
      this.form.crdFgCd = '';
      this.form.crdNo = '';
      this.form.userId = '';
      this.form.userNm = '';
      this.form.crdPssrTitle = '';
      this.form.crdPssrDut = '';
      this.form.crdPssrDept = '';
      this.form.cctrCd = "";
      this.form.cctrNm = "";
      this.form.blgDeptCd = "";
      this.form.blgDeptNm = "";
      this.form.cardType = "A";
      this.form.status = '';
      this.form.searchDtmFr = this.$moment().add(-3, 'month').startOf('month').format('YYYY-MM-DD');
      this.form.searchDtmTo = this.$moment().format('YYYY-MM-DD');

      if(!this.cardUseFlag) {
        this.form.cctrCd = this.$store.state.loginInfo.loginCctrCd;
        this.form.cctrNm = this.$store.state.loginInfo.loginCctrNm;
        this.form.blgDeptCd = '';
        this.form.blgDeptNm = '';
        this.form.userId = this.$store.state.loginInfo.loginId;
        this.form.userNm = this.$store.state.loginInfo.userName;
        this.form.crdPssrDut = this.$store.state.loginInfo.loginJobDutNm;
        this.form.crdPssrDept = this.$store.state.loginInfo.loginDeptNm;
      }

    },
    popEmp() {

      const that = this;

      this.$modal.open({
        component: Emp,
        parent: this,
        props: {
          param: that.form.userNm
        },
        width: 800,
        events: {
          close(object) {
            that.form.userId = object.empNo;
            that.form.userNm = object.empNm;
            that.form.crdPssrDut = object.jobDutNm;
            that.form.crdPssrDept = object.deptNm;
          }
        }
      });

    },
    popDeptEmp() {

      const that = this;

      this.$modal.open({
        component: DeptEmp,
        parent: this,
        props: {
          param: that.form.userNm,
          type : that.form.blgDeptCd
        },
        width: 800,
        events: {
          close(object) {
            that.form.userId = object.empNo;
            that.form.userNm = object.empNm;
            that.form.crdPssrDut = object.jobDutNm;
            that.form.crdPssrDept = object.deptNm;
          }
        }
      });

    },
    isEmpty(value){
      if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
        return ""
      }else{
        return value
      }
    },
    initEmp(force) {
      if (force === true) this.form.userNm = '';
      if (this.form.userNm === '') {
        this.form.userId = '';
        this.form.crdPssrDut = '';
        this.form.crdPssrDept = '';
      }
    },
    chkTax(params) {
      this.taxTypeCds.forEach(x =>{
        if(params === x.key) params = x.value;
      })
      return params;
    },
    chkAbroad(params) {
      if(params === 'N'){
        params = "국내";
      }else if(params === 'Y'){
        params = "해외";
      }
      return params;
    },
    chkCancel(params) {
      if(params === 'Y'){
        params = "취소";
      }else if(params === 'N'){
        params = "승인";
      }
      return params;
    },
    chkStatCd(params) {
      this.progressStatusCds.forEach(x =>{
        if(params === x.key) params = x.value;
      })
      return params;
    },
    chkStatus(params) {
      let flag = true;
      if(this.form.cardType == "F" && (this.cardUseFlag || this.airCardUser)){
        if(params == '01' || params == 'CR' || params == 'SD' || params == 'FR' || params == 'SC'){
          flag = false;
        }
      }
      return flag;
    },
    chkSlipStatCd(params) {
      this.slipStatusCds.forEach(x =>{
        if(params === x.key) params = x.value;
      })
      return params;
    },
    cellValueChange(params){
      if(params.data.regYn){
        this.totalAmt = Number(this.totalAmt) + Number(params.data.usedAmt);
      }else{
        this.totalAmt = Number(this.totalAmt) - Number(params.data.usedAmt);
      }
    },
    updateEmpItem() {
      const vm = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      this.$modal.open({
        component: Emp,
        parent: this,
        props: {
          param: rowNode.data.userNm
        },
        width: 800,
        events: {
          close(object) {
            rowNode.setDataValue('userId', object.empNo);
            rowNode.setDataValue('userNm', object.empNm);
            vm.gridApi.getRowNode(vm.rowId).setSelected(true);
          }
        }
      });
    },
    updateEmpItemVal() {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);

      const pVal = rowNode.data.userNm;

      if(pVal) {
        this.$http.get(`/api/emp/pop/list/${pVal}`)
            .then((response) => {
              if(response.data.length === 1) {
                vm.data[rowId].userNm = response.data[0].empNm;
                vm.data[rowId].userId = response.data[0].empNo;
                vm.gridApi.getRowNode(vm.rowId).setSelected(true);
              } else {
                this.$modal.open({
                  component: Emp,
                  parent: this,
                  width: 700,
                  props: {
                    param: pVal,
                  },
                  events: {
                    close(object) {
                      rowNode.setDataValue('userId', object.empNo);
                      rowNode.setDataValue('userNm', object.empNm);
                      vm.gridApi.getRowNode(vm.rowId).setSelected(true);
                    }
                  }
                })
              }
            }).catch(response => {
          this.$store.commit('finish');
          console.error("goSearch Error" + response);
        });
      }

    },
    popCctr(flag) {
      let vm = this
      let deptNm;
      let component;
      if(flag === 'a'){
        deptNm = this.form.cctrNm;
        component = CctrDel;
      }else if(flag === 'b'){
        deptNm = this.form.blgDeptNm;
        component = Cctr;
      }else{
        deptNm = this.form.cctrNm;
        component = Cctr2;
      }

      this.$modal.open({
        component: component,
        props: {
          param: flag === 'a' ? '' : deptNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveCctr(obj,flag)
          }
        }
      });
    },
    receiveCctr(obj,flag) {
      if(flag === 'a'){
        this.form.crdPssrDut = "";
        this.form.crdPssrDept = "";
        this.form.userId = "";
        this.form.userNm = "";
        this.form.cctrCd = obj.deptCd;
        this.form.cctrNm = obj.deptNm;
      }else
      if(flag === 'b'){
        this.form.blgDeptCd = obj.deptCd;
        this.form.blgDeptNm = obj.deptNm;
      }else{
        this.form.cctrCd = obj.deptCd;
        this.form.cctrNm = obj.deptNm;
      }
    },
    initErpCctr(force) {
      if (force === true) this.form.cctrNm = '';
      if (this.form.cctrNm === '') this.form.cctrCd = '';
    },
    initBlgCctr(force) {
      if (force === true) this.form.blgDeptNm = '';
      if (this.form.blgDeptNm === '') this.form.blgDeptCd = '';
    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.useDetails[rowIdx].regYn = params.node.isSelected();

      const list = this.gridApi.getSelectedRows();

      this.totalAmt = 0;

      list.forEach(x=>{
        this.totalAmt = Number(this.totalAmt) + Number(x.usedAmt);
      });

    }
  },
  async beforeMount(){
    await this.getShowUsedTime(this.$store.state.loginInfo.loginId);
    this.makeColDef();
    this.defaultColDef = { resizable: true, filter:true, sortable: true };
    this.gridOptions.getRowStyle  = function(params){
      if(params.data.rn === '합계'){
        return {background: '#f9f9f3'};
      }
    }
  },
  mounted() {
    this.form.compCd = this.$store.state.loginInfo.compCd;
    this.authority = this.$store.state.loginInfo.authorities[0].roleCd;
    this.getCompCdCombo();
    this.getCardCompCdCombo();
    this.getUseStatCdCombo();
    this.getCardFgCdCombo();
    this.getTaxTypeCombo();
    this.getProgressStatusCombo();
    this.getSlipStatusCombo();
    this.getCardUseRole();
    this.getAirCardUseRole();
    this.makeSumData();
  },
  watch: {
    'form.cardType': {
      handler(value) {
        if(value === 'F' && this.airCardUser){
          this.form.cctrCd = '';
          this.form.cctrNm = '';
          this.form.blgDeptCd = '';
          this.form.blgDeptNm = '';
          this.form.userId = '';
          this.form.userNm = '';
          this.form.crdPssrDut = '';
          this.form.crdPssrDept = '';
        }else if(value !== 'F' && this.airCardUser){
          this.form.cctrCd = this.$store.state.loginInfo.loginCctrCd;
          this.form.cctrNm = this.$store.state.loginInfo.loginCctrNm;
          this.form.blgDeptCd = '';
          this.form.blgDeptNm = '';
          this.form.userId = this.$store.state.loginInfo.loginId;
          this.form.userNm = this.$store.state.loginInfo.userName;
          this.form.crdPssrDut = this.$store.state.loginInfo.loginJobDutNm;
          this.form.crdPssrDept = this.$store.state.loginInfo.loginDeptNm;
        }
      }
    },
  },
  created(){
    document.title = this.title + ' - IJEAS';
  }
};
</script>

<style scoped>
.gridbox {
  height: 390px !important;
}

.gridbox .objbox {
  height: 350px !important;
}

.desc-content:after {
  clear: both;
  content: '';
  display: block;
}

.btn-wrap {
  margin-bottom: 10px;
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
  height: 50px;
}

.desc-content .item .desc-item .tit {
  position: absolute;
  left: 0;
}

.desc-content .item .desc-item .label-tit {
  font-family: 'NotoM';
  color: #222;
  font-size: 15px;
}

.desc-content .item.desc-left .desc-item {
  padding-left: 80px;
}

.desc-content .item.desc-left .desc-item .desc:after {
  clear: both;
  content: '';
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
  content: '';
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
  width: 38%;
  padding-right: 20px;
}

.desc-content .item.desc-center {
  width: 35%;
  padding-right: 40px;
}

.desc-content .item.desc-right {
  width: 27%;
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
.search-border .td-s-thumb.search-area > .ip-box
.search-border .td-s-thumb.search-area > button {
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

<style lang="scss" scoped>
.slip-grid {
  :global(.xhdr table tbody tr:last-child) {
    background-color: #f9f9f3;
  }

  :global(.xhdr table tbody tr:last-child td) {
    background-color: transparent !important;
  }

  :global(.xhdr table tbody tr:last-child td div) {
    background-color: transparent !important;
    color: inherit !important;
    text-align: inherit !important;
  }
}
</style>