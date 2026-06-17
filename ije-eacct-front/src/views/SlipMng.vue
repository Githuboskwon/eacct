<template>
    <div class="inner-box">
        <div class="content-name">
            <h2 class="dp-ib">{{title}}</h2>
            <div class="btn-type1 clearfix">
                <el-button type="success" icon="el-icon-user" size="large" v-if="!isApFlag" @click="hideAndShow">검인자 변경</el-button>
                <el-button type="success" icon="el-icon-document-checked" size="large" v-if="!isApFlag" @click="verifyBundle">검인</el-button>
<!--                <button class="btn-size btn-blue" @click="rejectBundle">반려</button>-->
                <el-button type="primary" icon="el-icon-search" size="large" @click="goSearch">조회</el-button>
            </div>
        </div>

        <!-- 검색조건 영역 -->
        <div class="desc-content search-border">
            <div class="item search_wrap">
                <div class="search_box" style="width: 35%">
                    <div class="search_title">
                        <span class="search_tit" style="color: #CC3D3D;">- 회계일자</span>
                    </div>
                  <el-date-picker
                      v-model="postDt"
                      type="daterange"
                      align="right"
                      unlink-panels
                      style="width: 260px;"
                      range-separator="~"
                      start-placeholder="시작일"
                      end-placeholder="종료일">
                  </el-date-picker>
                </div>
                <div class="search_box" style="width: 30%">
                    <button class="item-list icon is-right btn_s_w" @click="openModal()" type="button">상세검색<i class="fas fa-plus"></i></button>
                </div>
                <div style="display: flex; justify-content: flex-end;">
                  <button :class="buttonColor"  style="border-right:none;"  @click="ingApproval">결재중</button>
                  <button :class="buttonColor"  style="border-right:none;"  @click="nowApproval">검인중</button>
                  <button :class="buttonColor1"  @click="searchAll">전체</button>
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
                                    <span class="search_tit">- 회계일자</span>
                                </div>
                                <div class="search_con search-area">
                                  <el-date-picker
                                      v-model="postDt"
                                      type="daterange"
                                      align="right"
                                      style="width: 70%"
                                      unlink-panels
                                      range-separator="~"
                                      start-placeholder="시작일"
                                      end-placeholder="종료일">
                                  </el-date-picker>
                                  <button @click="dateSetting('toDay','postDt')" class="search_bt_white_s">당일</button>
                                  <button @click="dateSetting('crrntMnth','postDt')" class="search_bt_white_s">당월</button>
                                  <button @click="dateSetting('PrvsMnth','postDt')" class="search_bt_white_s">전월</button>
                                </div>
                            </div>

                            <div class="search_box_pop">
                              <div class="search_title">
                                <span class="search_tit">- 거래유형</span>
                              </div>
                              <div class="search_con search-area">
                                <input class="input dp-ib w40p_5r" type="text" v-model="form.trxTypeCd" disabled>
                                <input class="input dp-ib w60p" type="text" v-model="form.trxTypeNm" @input="initTrx" @keypress.enter="popTrx">
                                <button class="icon is-right" @click="popTrx"><i class="fas fa-search"></i></button>
                              </div>
                            </div>

                            <div class="search_box_pop">
                              <div class="search_title">
                                <span class="search_tit">- 전표상태</span>
                              </div>
                              <div class="search_con search-area">
                                <select class="select is-fullwidth w100p" v-model="form.slipStatCd">
                                  <option value=''>==전체==</option>
                                  <option
                                    v-for="item in slipStats"
                                    :key="item.key"
                                    :value="item.key"
                                    v-text="item.value"/>
                                </select>
                              </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 작성부서</span>
                                </div>
                                <div class="search_con search-area">
                                    <div class="desc">
                                        <input class="input dp-ib input-bg w40p_5r" type="text" v-model="form.wrtDeptCd" disabled>
                                        <div class="dp-ib w60p">
                                            <input class="input" type="text" v-model="form.wrtDeptNm" @input="initCctr" @keypress.enter="popCctr">
                                            <button class="icon is-right" @click="popCctr"><i class="fas fa-search"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 작성자</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input dp-ib w40p_5r" type="text" v-model="form.wrtId" disabled>
                                    <div class="dp-ib w60p">
                                        <input class="input" type="text" v-model="form.wrtNm" @input="initEmp('writer')" @keypress.enter="popEmp('writer')">
                                        <button class="icon is-right" @click="popEmp('writer')"><i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="search_box_pop">
                              <div class="search_title">
                                <span class="search_tit">- 검인대기자</span>
                              </div>
                              <div class="search_con search-area">
                                <input class="input dp-ib w40p_5r" type="text" v-model="form.nextApprNo" disabled>
                                <div class="dp-ib w60p">
                                  <input class="input" type="text" v-model="form.nextApprNm" @input="initEmp('nextAppr')" @keypress.enter="popEmp('nextAppr')">
                                  <button class="icon is-right" @click="popEmp('nextAppr')"><i class="fas fa-search"></i>
                                  </button>
                                </div>
                              </div>
                            </div>

                            <div class="search_box_pop">
                              <div class="search_title">
                                <span class="search_tit">- 작성일자</span>
                              </div>
                              <div class="search_con search-area">
                                <el-date-picker
                                    v-model="regDt"
                                    type="daterange"
                                    align="right"
                                    style="width: 70%"
                                    unlink-panels
                                    range-separator="~"
                                    start-placeholder="시작일"
                                    end-placeholder="종료일">
                                </el-date-picker>
                                <button @click="dateSetting('toDay','regDt')" class="search_bt_white_s">당일</button>
                                <button @click="dateSetting('crrntMnth','regDt')" class="search_bt_white_s">당월</button>
                                <button @click="dateSetting('PrvsMnth','regDt')" class="search_bt_white_s">전월</button>
                              </div>
                            </div>

                            <div class="search_box_pop">
                              <div class="search_title">
                                <span class="search_tit">- 거래처</span>
                              </div>
                              <div class="search_con search-area">
                                <input class="input Rt-M tal w100p" type="text" v-model="form.evdCustNm">
                              </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 전표번호</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input Rt-M tal w100p" type="text" v-model="form.slipNo">
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
        </div>
        <!-- //검색조건 영역 -->

        <div class="table-area mt20" v-if="enableChangeArea">
          <div class="table-b">
            <div class="table-header">
              <div class="table-name">
                <h3 class="ico_table_name">검인자 변경</h3>
              </div>
            </div>
            <div class="change-area">
              <div class="input-area">
                <input type="text" class="input" style="width: 49%;" v-model="changeNo" readonly="readonly"/>
                <input type="text" class="input" style="width: 49%; margin-left: 5px" v-model="changeNm" readonly="readonly"/>
              </div>
              <div class="btn-wrap btn-type1">
                <button type="button" class="btn-size btn-gray fl_left" @click="popEmp('change')">
                  검색
                </button>
                <button type="button" class="btn-size btn-blue fl_left" @click="changeAppr">
                  변경
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 테이블 -->
        <div class="table-area">

            <div class="table-b">
                <div class="table-header">
                    <div class="table-name">
                        <h3 class="ico_table_name">전표내역</h3>
                    </div>
                    <div class="btn-type1 clearfix">
                        <div style="float:left; margin-right:10px;"> 총 금액 : {{totalAmt | amt}} </div>
                        <el-button type="success" icon="el-icon-document-checked" size="small" @click="saveExcel">엑셀 저장</el-button>
                    </div>
                </div>

                <div class="grid-wrap">
                    <ag-grid-vue
                        ref="grid"
                        style="width: 100%;"
                        class="ag-theme-alpine grid_search_height_350"
                        
                        :columnDefs="columnDefs"     
                        :gridOptions="gridOptions"
                        :rowData="slipList"
                        :defaultColDef="defaultColDef"
                        :frameworkComponents="frameworkComponent"
                        :enableRangeSelection="true"
                        :suppressRowClickSelection="true"
                        rowSelection="multiple"

                        @grid-ready="onReady"
                        @row-selected="onRowSelected"
                        @cell-clicked="onCellClicked"
                        @rowDoubleClicked="rowDoubleClick"
                        @selection-changed="onSelectionChanged"/>
                </div>

              <div class="pagingbox">
                <button class="btn-page page-first" @click="goFirst" :disabled="page.page==0 || page.count==0"></button>
                <button class="btn-page page-prev" @click="goPre" :disabled="page.page==0 || page.count==0"></button>

                <button :class="page.page==item.value-1 ? 'page-num active' : 'page-num'"
                        v-for="item in page.pageList"
                        :value="item.value"
                        @click="goPageNum(item.value)">{{item.value}}</button>

                <button class="btn-page page-next" @click="goNext" :disabled="page.page==page.lastPage || page.count==0"></button>
                <button class="btn-page page-last" @click="goEnd" :disabled="page.page==page.lastPage || page.count==0"></button>
              </div>
            </div>

        </div>
        <!-- //테이블 -->

    </div>
</template>

<script>
import Vue from 'vue';
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import slip from '@/mixin/slip-basic'
import _ from 'lodash'

import DhxCalendar from '@/components/DhxCalendar.vue'
import Cctr from '@/components/Cctr_Ag.vue'
import CctrDeptRole from '@/components/CctrDeptRole'
import Emp from '@/components/Emp_Ag.vue'
import {AgGridVue} from 'ag-grid-vue';
import AgGridCheckbox from "@/components/agGrid/AgGridCheckbox.vue"
import CheckboxCellRenderer from "@/components/agGrid/checkbox-cell-renderer";
import Dff from "@/components/SlipMngItemPop"
import Trx from "@/components/TrxSearchPop"
import ApprovalModal from '@/components/accrualSlip/Approval/Main.vue';
import ApprBundlePop from "@/components/ApprBundlePop.vue";
import ApprBundlePopDrafter from "@/components/ApprBundlePopDrafter.vue";

const bus = new Vue()
export default {
    name: 'SlipLst',
    mixins: [mixin, mixinSlip, slip],
    components: {Cctr, Emp, DhxCalendar, AgGridVue,AgGridCheckbox, Dff},
    props: {
        params: {
            type: Object,
            required: false
        },
    },
    data() {
        return {
            title: '전표관리',
            slipTypes: [],
            slipStats: [],
            slipList: [],
            trxList: [],
            authority: '',
            openDt:'',
            postDt: [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')],
            regDt: ['' , ''],
            form: {
                postDtFrom: this.$moment().startOf('month').format('YYYY-MM-DD'),
                postDtTo: this.$moment().format('YYYY-MM-DD'),
                slipNo: '',
                slipTypeCd: '',
                evdCustNm: '',
                wrtId: '',
                wrtNm: '',
                wrtUserDut: '',
                wrtUserDept: '',
                wrtDeptCd: '',
                wrtDeptNm: '',
                regDtFrom: '',
                regDtTo: '',
                slipStatCd: '',
                hdSgtxt:'',
                delegateNo: '',
                trxTypeCd: '',
                trxTypeNm: '',
                nextApprNm: '',
                nextApprNo: '',
            },
            changeNo: '',
            changeNm: '',
            syncGoPage: false,
            gridOptions: {},
            columnDefs:[],
            defaultColDef: { 
                resizable: true, 
                filter:true, 
                sortable: true
            },
            chkYn: 'N',
            totalAmt:0, // 전체금액 변수
            frameworkComponent : {
              'checkBox' : AgGridCheckbox,
              checkboxRenderer: CheckboxCellRenderer,
            },
            isApFlag : false,
            appCBtn : false,
            appExctpExpenseCBtn : false,
            appEbillCBtn : false,
            appBillCBtn : false,
            buttonColor:'item-list1 btn_s_o_w',
            buttonColor1:'item-list1 btn_s_o_w',
            buttonColor2:'item-list1 btn_s_o_w',
            submitChk : false,
            initPage: false,
            page:{
              page: 0,
              limit: 100,
              count: 0,
              lastPage: 0,
              pageList: []
            },
            enableChangeArea: false,
        };
    },
    methods: {
        makeColDef(){
            const that = this;
            this.columnDefs =[
                {
                  headerName: '',
                  field:'regYn',
                  width : 60,
                  cellStyle:{textAlign: 'center'},
                  headerCheckboxSelection: true,
                  checkboxSelection: true,
                },
                {
                  headerName:'No.',
                  field:'no',
                  width:80,
                  cellStyle:{textAlign: 'center'},
                  valueFormatter: function(params) {
                      return that.page.page*100 + params.node.rowIndex + 1;
                  }
                },
                {
                  headerName:'전표번호',
                  field:'slipNo',
                  width:185
                },
                {
                  headerName:'회계일자',
                  field:'postingDt',
                  width:110,
                  cellStyle:{textAlign: 'center'},
                  valueFormatter: (params) => {
                    return that.$moment(params.value).format('YYYY-MM-DD');
                  }
                },
                {
                  headerName:'결제예정일',
                  field:'termDueDate',
                  width:110,
                  cellStyle:{textAlign: 'center'},
                  valueFormatter: (params) => {
                    return params.value ? that.$moment(params.value).format('YYYY-MM-DD') : null;
                  }
                },
                {
                  headerName:'거래유형',
                  field:'slipType',
                  width:150,
                  valueFormatter: (params) => {
                    for(var i=0; i<that.trxList.length; i++){
                      if(that.trxList[i].trxTypeCd === params.value){
                        return that.trxList[i].trxTypeNm
                      }
                    }
                  }
                },
                {headerName:'전표상태', field:'status', width:110, hide: true},
                {headerName:'전표상태', field:'statusNm', width:110, cellStyle:{textAlign: 'center'},},
                /*{
                  headerName:'전표상태',
                  field:'statusText',
                  width:120,
                  valueFormatter: (params) => {
                    for(var i=0; i<that.slipStats.length; i++){
                      if(that.slipStats[i].key === params.data.status){
                        return that.slipStats[i].value
                      }
                    }
                  }
                },*/
                {
                  headerName:'작성부서명',
                  field:'deptNm',
                  width:150
                },
                {
                  headerName:'작성자',
                  field:'empNm',
                  width:100,
                  cellStyle:{textAlign: 'center'},
                },
                {
                  headerName:'적요',
                  field:'headerRemark',
                  width:300
                },
                {
                  headerName:'공급가',
                  field:'supplyAmount',
                  width:150,
                  cellStyle:{textAlign: 'right'},
                  valueFormatter: (params) => {
                    let val = params.value
                    if(val){
                      return String(this.$numeral(val).value()).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                    }
                  },
                  comparator: function (valueA, valueB) {
                    return Number(valueA) - Number(valueB);
                  },
                },
                {
                  headerName:'부가세액',
                  field:'taxAmount',
                  width:150,
                  cellStyle:{textAlign: 'right'},
                  valueFormatter: (params) => {
                    let val = params.value
                    if(val){
                      return String(this.$numeral(val).value()).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                    }
                  },
                  comparator: function (valueA, valueB) {
                    return Number(valueA) - Number(valueB);
                  },
                },
                {
                  headerName:'총금액(KRW)',
                  field:'usedAmt',
                  width:150,
                  cellStyle:{textAlign: 'right'},
                  valueFormatter: (params) => {
                    let val = params.value
                    if(val){
                      return String(this.$numeral(val).value()).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                    }
                  },
                  comparator: function (valueA, valueB) {
                    return Number(valueA) - Number(valueB);
                  },
                },
                {
                  headerName:'거래처코드',
                  field:'integrationVendorNum',
                  width:110,
                  cellStyle:{textAlign: 'center'},
                  valueFormatter: (params) => {
                    return params.value === '-' ? '' : params.value;
                  }
                },
                {
                  headerName:'거래처명',
                  field:'integrationVendorName',
                  width:200
                },
                {
                  headerName:'결제조건',
                  field:'termName',
                  width:200
                },
                {
                  headerName:'지급여부',
                  field:'intStatus',
                  width:110,
                  cellStyle:{textAlign: 'center'},
                },
                {
                  headerName:'통화',
                  field:'usedCur',
                  width:90,
                  cellStyle:{textAlign: 'center'},
                },
                {
                  headerName:'총금액',
                  field:'usedForAmt',
                  width:130,
                  cellStyle:{textAlign: 'right'},
                  valueFormatter: (params) => {
                    let val = params.value
                    if(val){
                      return String(this.$numeral(val).value()).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                    }
                  },
                  comparator: function (valueA, valueB) {
                    return Number(valueA) - Number(valueB);
                  },
                },
                {
                    headerName:'재경의견',
                    field:'apprDesc',
                    width:200
                },
                {
                  headerName:'검인대기자',
                  field:'nextAppUserNm',
                  width:110,
                  cellStyle:{textAlign: 'center'},
                },
                {
                    headerName:'',
                    field:'slipStatCd',
                    width:140,
                    hide:true
                },
                {
                    headerName:'',
                    field:'slipTypeCd',
                    width:140,
                    hide:true
                },
                {
                    headerName:'ERP전송상태',
                    field:'slipIfFlag',
                    width:120,
                    cellStyle:{textAlign: 'center'},
                    /*cellRenderer: (params) => {
                        if(params.data.erpInvoiceId !== ''){
                            return '<div style="text-decoration: underline; text-underline-position: under; color:blue;">'+params.data.erpStatus+'</div>'
                        }
                    }*/
                },
                {
                  headerName:'작성일자',
                  field:'regDtm',
                  width:120,
                  cellStyle:{textAlign: 'center'},
                  valueFormatter: (params) => {
                    return that.$moment(params.value).format('YYYY-MM-DD');
                  }
                },
                {
                  headerName:'정산일',
                  field:'calcuDt',
                  width:110,
                  valueFormatter: (params) => {
                    if(!_.isEmpty(params.value)){
                    return that.$moment(params.value).format('YYYY-MM-DD');
                    }else{
                      return null
                    }
                  },
                  hide:true
                },
                {headerName:'검인라인수정', field:'changeNo', hide:true}
            ]
        },
        onReady(){
          this.gridApi = this.gridOptions.api;
          this.columnApi = this.gridOptions.columnApi;
        },
        onCellClicked(params) {
            
            var field = params.colDef.field;

            if(field === "erpStatus"){
                
                  /*this.$modal.open({
                    component: ErpItfErrModal,
                    parent: this,
                    props: {
                        grSlipNo: params.data.grSlipNo,
                    },
                    width: 600
                })*/
                
            }
        },
        rowDoubleClick(params){
          this.$cookie.set('searchForm', JSON.stringify(this.form));
          this.$store.commit('searchForm', JSON.parse(this.$cookie.get('searchForm')));

          // this.$router.push({path: `/accrualSlip/${params.data.slipNo}`});
          let slipType = params.data.slipType;
          let docMngNo = params.data.apprNo;
          let slipNo = params.data.slipNo;

          this.$cookie.set('searchForm', JSON.stringify(this.form));
          this.$store.commit('searchForm', JSON.parse(this.$cookie.get('searchForm')));

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
                    // value: this.data
                  },
                  parent: this,
                  width: 1200,
                  events: {
                    close(data) {
                      // if (callback !== undefined && typeof callback === 'function') {
                      //   callback.apply(this, [data])
                      // }
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

        },
        showDetailPop(slipNo, slipType, docMngNo) {
          const vm = this
          let title = "";
          let setModal = undefined;
          switch (slipType) {
            /*case 'E11' :
              title = '자금지급전표'
              setModal = SlipPayDetailModal
              break;*/
            default:
              title = "전표";
              setModal = ApprovalModal;
              break;
          }
          if(slipType.length != 2){
            this.$modal.open({
              component: setModal,
              parent: this,
              props: {
                slipNo : slipNo,
                docType: 'verify',
                title: title,
              },
              width: 1200,
              events: {
                close(data) {
                  vm.goSearch()
                }
              }
            });
          }
        },
        getSlipTypeCombo() {
            this.$http.get(`/api/code/combo`, {params: {groupCd: "SLIP_TYPE_CD"}})
                .then(response => {
                    this.slipTypes = response.data;
                });
        },
        getSlipStatCombo() {
            this.$http.get(`/api/code/combo`, {params: {groupCd: "PROGRESS_STATUS_CD"}})
                .then(response => {
                    this.slipStats = response.data.filter(x => (x.key != 'SV' && x.key != 'VE' && x.key != 'VC' && x.key != 'AP' && x.key != 'AR' &&
                                                                x.key != '01' && x.key != '07'));
                });
        },
        getTrxList() {
          this.$http.post(`/api/trx/list`, {params: {compCd: this.$store.state.loginInfo.compCd}})
            .then(response => {
              this.trxList = response.data;
            });
        },
        saveExcel() {
          var params = {
            fileName : "전표내역"
          };
          this.gridOptions.api.exportDataAsCsv(params)
        },
        onSelectionChanged(){
          const Row = this.gridApi.getSelectedRows();

          this.totalAmt = 0

          //합계 금액 계산
          for(var i=0; i<Row.length; i++){
            this.totalAmt += this.$numeral(Row[i].usedAmt).value()
          }
        },
        onRowSelected(params) {
          const rowIdx = params.rowIndex;
          //this.data[rowIdx].regYn = params.node.isSelected();
        },
        rejectBundle() {
          const vm = this;
          const rows = this.gridApi.getSelectedRows();

          if(rows.length == 0){
            this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
            return
          }

          for(let i=0; i<rows.length; i++){
            if(rows[i].status != 'AP' && rows[i].status != 'CP'){
              this.$swal({ type: 'warning', text: '반려 가능한 전표를 선택해주세요.' });
              return
            }
          }
          this.addApprDesc(rows.length).then(() => {
            this.$swal({
              type: 'info',
              text: `결재내역을 반려합니다. 계속 하시겠습니까?`,
              showCancelButton: true,
              confirmButtonText: '예',
              cancelButtonText: '아니오',
            }).then((result) => {
              if (result.value) {
                this.$store.commit('loading');

                this.$http.post(`/api/appr/bundle/rej`, {
                  apprDesc: this.apprDesc,
                  approvalHeaderDtos: rows
                })
                .then((response) => {
                  vm.$store.commit('finish');
                  vm.$swal({type: 'info', text: '반려가 완료되었습니다.'})
                  .then((result)=>{
                    if(result.value){
                      this.$emit('close');
                    }
                  });
                  this.goSearch();
                })
                .catch((e) => {
                  vm.$store.commit('finish');
                  vm.$emit('close');
                });
              }
            })
          })
          this.$store.commit('loading');
          this.$http.post(`/api/appr/bundle/rej`, rows)
          .then(response => {
            this.$store.commit('finish');
            this.$swal({type: 'info', text: '반려가 완료되었습니다.'})
            this.goSearch();
          }).finally(() => {
            this.$store.commit('finish')
          });

        },
        addApprDesc(){
          const rows = this.gridApi.getSelectedRows();
          let title = '';
          if(rows.length > 1) {
            title = rows[0].slipNo + ' 외 ' + (this.$numeral(rows.length).value() - 1) + '건';
          } else {
            title = rows[0].slipNo;
          }
          return new Promise((resolve,reject) => {
            const vm = this;
            this.$modal.open({
              component:ApprBundlePopDrafter,
              props:{
                docType:'reject',
                docTitleNm: title,
              },
              events: {
                close(drafterData) {
                  // Close event handler
                  if(!_.isEmpty(drafterData)){
                    vm.apprDesc = drafterData
                    return resolve()
                  }else{
                    return reject()
                  }
                }
              }
            })
          })
        },
        compareDate(value){
          if(!value){
            this.$swal({ type: 'info', text: '회계일자를 입력해주세요.' });
            return false;
          }
        },
        getCount(){

          this.$http.post(`/api/slip/history/count`, {
              compCd: this.$store.state.loginInfo.compCd,
              postDtFrom: this.$moment(this.postDt[0]).format('YYYYMMDD'),
              postDtTo: this.$moment(this.postDt[1]).format('YYYYMMDD'),
              wrtDeptCd: this.form.wrtDeptCd,
              evdCustNm: this.form.evdCustNm,
              slipTypeCd: this.form.trxTypeCd,
              wrtId: this.form.wrtId,
              regDtFrom: this.regDt[0] ? this.$moment(this.regDt[0]).format('YYYYMMDD') : undefined,
              regDtTo: this.regDt[1] ? this.$moment(this.regDt[1]).format('YYYYMMDD') : undefined,
              slipStatCd: this.form.slipStatCd ? this.form.slipStatCd : 'CC,CP,CR,FC,FH,FP,FR,IC,IE,SC,SD',
              slipNo: this.form.slipNo.trim(),
              hdSgtxt: this.form.hdSgtxt,
              nextAppUserId: this.form.nextApprNo
            }
          ).then(response => {
            this.page.count = response.data;
            this.page.lastPage = Math.ceil(response.data / 100) - 1

            this.page.pageList = []

            if(this.page.lastPage > -1){
              var count = 0;
              for(var i=Math.floor(this.page.page/10)*10; i<=this.page.lastPage; i++){
                count++;
                if(count > 10) break

                this.page.pageList.push({value: i+1})
              }
            }
          })
        },
        goFirst(){
          this.page.page = 0
          this.goSearch()
        },
        goPre(){
          if(this.page.page > 0) {
            this.page.page = this.page.page - 1
          }
          this.goSearch()
        },
        goNext(){
          //마지막 구하기
          if(this.page.page < this.page.lastPage) {
            this.page.page = this.page.page + 1
          }
          this.goSearch()
        },
        goEnd(){
          this.page.page = this.page.lastPage
          this.goSearch()
        },
        goPageNum(param){
          this.page.page = param - 1
          this.goSearch()
        },
        ingApproval(){
          this.form.slipStatCd = 'AP'
          this.isApFlag = true;
          this.goSearch()
        },
        nowApproval(){
          this.form.slipStatCd = 'CP'
          this.isApFlag = false;
          this.goSearch()
        },
        searchAll(){
          this.form.slipStatCd = ''
          this.isApFlag = false;
          this.goSearch()
        },
        goSearch() {
            if(this.initPage){
              this.initPage = false
              this.page.page = 0
            }

            this.getCount()
            this.compareDate(this.postDt);
            const param = {
              compCd: this.$store.state.loginInfo.compCd,
              postDtFrom: this.$moment(this.postDt[0]).format('YYYYMMDD'),
              postDtTo: this.$moment(this.postDt[1]).format('YYYYMMDD'),
              wrtDeptCd: this.form.wrtDeptCd,
              evdCustNm: this.form.evdCustNm,
              slipTypeCd: this.form.trxTypeCd,
              wrtId: this.form.wrtId,
              regDtFrom: this.regDt[0] ? this.$moment(this.regDt[0]).format('YYYYMMDD') : undefined,
              regDtTo: this.regDt[1] ? this.$moment(this.regDt[1]).format('YYYYMMDD') : undefined,
              slipStatCd: this.form.slipStatCd ? this.form.slipStatCd : 'CC,CP,CR,FC,FH,FP,FR,IC,IE,SC,SD',
              slipNo: this.form.slipNo.trim(),
              hdSgtxt: this.form.hdSgtxt,
              nextAppUserId: this.form.nextApprNo,
              page: this.page.page,
              pageSize: this.page.limit,
            }

            // if (!this.validation(param)) return;

            this.$store.commit('loading');
            this.$http.post(`/api/slip/history`, param)
                .then(response => {
                    if (response.data) {
                        this.slipList = response.data;
                    }
                  this.totalAmt = 0
                  this.chkYn = false;
                }).finally(() => {
                    this.$store.commit('finish')
                });
                document.getElementById("open-moda").style.opacity = "0";
                document.getElementById("open-moda").style.pointerEvents = "none";
        },
        getStartDate(){
          this.$store.commit('loading');
          this.$http.get(`/api/acctPeriod/openDate`)
          .then(response => {
            this.openDt = response.data
            this.form.postDtFrom = this.$moment(response.data,'YYYYMMDD').format('YYYY-MM-DD')
            this.form.postDtTo = this.$moment(this.form.postDtFrom).endOf('month').format('YYYY-MM-DD')

          }).catch(response => {
          })
          .finally(() => {
            this.$store.commit('finish');
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
        validation(param) {
            if (!param.postDtFrom || !param.postDtTo) {
                this.$swal({type: 'warning', text: '신청일자를 입력해 주세요.'});
                return false;
            }
            return true;
        },
        popTrx() {
          const that = this;

          this.$modal.open({
            component: Trx,
            parent: this,
            props: {
              schTxt: this.form.trxTypeNm
            },
            width: 700,
            events: {
              close(obj) {
                that.form.trxTypeCd = obj.trxTypeCd;
                that.form.trxTypeNm = obj.trxTypeNm;
              }
            }
          });
        },
        initTrx(force) {
          if (force === true) this.form.trxTypeNm = '';
          if (this.form.trxTypeNm === '') {
            this.form.trxTypeCd = '';
          }
        },
        popCctr() {
            const that = this;

            this.$modal.open({
              component: Cctr,
              parent: this,
              props: {
                param: this.form.wrtDeptNm
              },
              width: 700,
              events: {
                close(obj) {
                  that.form.wrtDeptCd = obj.deptCd;
                  that.form.wrtDeptNm = obj.deptNm;
                }
              }
            });

        },
        initCctr(force) {
            if (force === true) this.form.wrtDeptNm = '';
            if (this.form.wrtDeptNm === '') this.form.wrtDeptCd = '';
        },
        popEmp(method) {
            const that = this;

            let search = ''

            if(method == 'writer'){
              search = this.form.wrtNm
            }else if(method == 'nextAppr'){
              search = this.form.nextApprNm
            }

            this.$modal.open({
              component: Emp,
              parent: this,
              props: {
                param: search
              },
              width: 700,
              events: {
                close(obj) {
                  if(method == 'writer'){
                    that.form.wrtId = obj.empNo;
                    that.form.wrtNm = obj.empNm;
                  }else if(method == 'nextAppr'){
                    that.form.nextApprNo = obj.empNo;
                    that.form.nextApprNm = obj.empNm;
                  }else if(method == 'change'){
                    that.changeNo = obj.empNo;
                    that.changeNm = obj.empNm;
                  }
                }
              }
            });
        },
        initEmp(method, force) {
            if(method == 'writer'){
              if (force === true) this.form.wrtNm = '';
              if (this.form.wrtNm === '') {
                this.form.wrtId = '';
              }
            }else if(method == 'nextAppr'){
              if (force === true) this.form.nextApprNm = '';
              if (this.form.nextApprNm === '') {
                this.form.nextApprNo = '';
              }
            }

        },
        changeAppr(){
            const Rows = this.gridApi.getSelectedRows();

            if(!this.changeNm || !this.changeNo){
              this.$swal({ type: 'warning', text: '변경할 검인자를 선택하세요.' });
             return
            }

            if(Rows.length == 0){
              this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
              return
            }

            for(let i=0; i<Rows.length; i++){
              if(Rows[i].status != 'CP'){
                this.$swal({ type: 'warning', text: '검인중인 전표만 선택해주세요.' });
                return
              }
              Rows[i].changeNo = this.changeNo
            }

            this.$http.post(`/api/slip/changeAppr`, Rows)
            .then(response => {
              this.$swal({ type: 'success', text: response.data.message });
              this.changeNo = ''
              this.changeNm = ''
              this.goSearch()
            });
        },
        setCallPageByParam(){
            if (this.$route.params) {
                for (let member in this.form) {
                    if (this.$route.params[member] !== undefined) {
                        this.form[member] = this.$route.params[member]
                    }
                }
            }
        },
        dateSetting(str,type){
          var typeFrom = type.concat('From')
          var typeTo = type.concat('To')

          switch (str) {
            case 'toDay':
              this.form[typeFrom] = this.$moment().format('YYYYMMDD')
              this.form[typeTo] = this.$moment().format('YYYYMMDD')
              this[type] =  [this.$moment().format('YYYY-MM-DD HH:mm:ss'),this.$moment().format('YYYY-MM-DD HH:mm:ss')]
              break;
            case 'crrntMnth':
              this.form[typeFrom] = this.$moment().startOf('month').format('YYYYMMDD')
              this.form[typeTo] = this.$moment().endOf('month').format('YYYYMMDD')
              this[type] =  [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'),this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')]
              break;
            case 'PrvsMnth':
              this.form[typeFrom] = this.$moment().add(-1, 'month').startOf('month').format('YYYYMMDD')
              this.form[typeTo] = this.$moment().add(-1, 'month').endOf('month').format('YYYYMMDD')
              this[type] =  [this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss'),this.$moment().add(-1, 'month').endOf('month').format('YYYY-MM-DD HH:mm:ss')]
              break;
          }
          // this.goSearch()
        },
      resetSearch(){
        this.postDt = [this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')];
        this.regDt = ['' , ''];
        this.form.postDtFrom = this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD')
        this.form.postDtTo = this.$moment().format('YYYY-MM-DD')
        this.form.evdCustNm = ''
        this.form.trxTypeCd = ''
        this.form.trxTypeNm = ''
        this.form.slipStatCd = 'CP'
        this.form.slipNo = ''
        this.form.hdSgtxt = ''
        this.form.regDtFrom = ''
        this.form.regDtTo = ''
        this.form.nextApprNo = this.$store.state.loginInfo.loginId
        this.form.nextApprNm = this.$store.state.loginInfo.userName
      },
      hideAndShow(){
        this.enableChangeArea = this.enableChangeArea ? false : true
      },
      verifyBundle() {
        let that = this;
        let verifyList = this.gridApi.getSelectedRows().filter((x) => x.apprGroupId = x.approvalGroupId);
        let loginId = this.$store.state.loginInfo.loginId;
        if(verifyList.length == 0){
          this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
          return
        }

        for(let i = 0; i < verifyList.length; i++){
            if(verifyList[i].status != 'CP' || verifyList[i].nextAppUserId != loginId){
              this.$swal({ type: 'warning', text: '검인 가능한 전표를 선택해주세요.' });
              return
            }
          }

        this.$modal.open({
          component: ApprBundlePop,
          props: {
            apprList : verifyList,
            docType: 'verify',
          },
          parent: this,
          events: {
            close(data) {
              // Close event handler
              console.log("Appr Popup Close Event!!", data);
              that.goSearch();
            },
          },
        });

      }

    },
    created() {
        let vm = this
        if(vm.$route.query.slipStatCd || _.isEmpty(this.params)){
          this.form.slipStatCd = vm.$route.query.slipStatCd
          this.getStartDate()
          this.getSlipStatCombo()
          this.getTrxList()
          this.resetSearch()
          this.goSearch();
        }
    },
    beforeMount() {
        const that = this;
        this.makeColDef();

        this.gridOptions = {
          enableRangeSelection: true,
          /*statusBar: {
            statusPanels: [
              {
                statusPanel: 'agTotalAndFilteredRowCountComponent',
                align: 'left',
              },
            ]
          },*/

          onFilterChanged: () => {
            that.chkYn = 'N';
            that.totalAmt = 0;

            for(var j=0; j<that.data.length; j++){
              that.data[j].useYn = that.chkYn
            }

            var size = that.$refs.grid.$children.length;
            for(var k=0; k<size; k++){
              that.$refs.grid.$children[k].select = that.chkYn;
            }

          }
        }
    },
    mounted() {
      document.title = this.title + ' - IJEAS';
        if(this.params.slipTypeCd) this.form.slipTypeCd = this.params.slipTypeCd;

        this.authority = this.$store.state.loginInfo.authorities[0].roleSelectCd;
        if((!this.form.wrtDeptCd || this.form.wrtDeptCd === '') && this.authority !== '10') {
            this.form.wrtDeptCd = this.$store.state.loginInfo.loginDeptCd;
            this.form.wrtDeptNm = this.$store.state.loginInfo.loginDeptNm;
        }
        if((!this.form.wrtId || this.form.wrtId === '') && this.authority !== '10') {
            this.form.wrtId = this.$store.state.loginInfo.loginId;
            this.form.wrtNm = this.$store.state.loginInfo.userName;
        }

        setTimeout(() => {
          //this.goSearch();
        },500);

    },
    watch: {
      'params': {
          immediate: true,
          deep: true,
          handler(value) {
              if(value && value.postDtFrom) {
                  this.form = value
                  if(this.form.page > 0) {
                      this.syncGoPage = true
                  }
              }
          }
      },
      'form': {
        immediate: false,
        deep: true,
        handler(value) {
          this.initPage = true
        }
      },
    }
};
</script>

<style scoped>
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
    float:left
}

.desc-content .item .desc-item:last-child {
    margin-bottom: 0;
    height: 25px;
    float:left
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
    padding-left: 90px;
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
    width: 40%;
    padding-right: 20px;
}

.desc-content .item.desc-center {
    width: 35%;
    padding-right: 40px;
}

.desc-content .item.desc-right {
    width: 25%;
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

.search-border .td-s-thumb.search-area .ip-box {
    vertical-align: top;
}

@media (max-width: 1580px) {
    .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
        width: 50%;
    }
}

.change-area {
  position: relative;

  input[type="file"] {
    display: none;
  }

  * {
    float: left;
  }

  .input-area {
    min-width: 400px;
    margin-right: 15px;

    .input {
      height: 34px;
    }
  }
}

.change-area:after {
  content: " ";
  display: table;
  clear: both;
}

</style>
