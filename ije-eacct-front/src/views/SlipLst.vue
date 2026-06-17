<template>
    <div class="inner-box">
        <div class="content-name">
            <h2 class="dp-ib">{{title}}</h2>
            <div class="btn-type1 clearfix">
              <el-button type="danger" icon="el-icon-search" size="large" @click="goSearch(true)">반려조회</el-button>
              <el-button type="danger" icon="el-icon-close" size="large" @click="cancelAppr">상신취소</el-button>
              <el-button type="primary" icon="el-icon-folder-opened" size="large" @click="goSubmit">일괄상신</el-button>
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
                <div class="search_box" style="width: 35%">
                  <div class="search_title">
                    <span class="search_tit">- 위임자</span>
                  </div>
                  <div class="search_con" style="width: 30%">
                    <select class="select is-fullwidth" v-model="form.delegateNo">

                      <option v-if="this.$store.state.loginInfo.authorities[0].roleCd ==='ADMIN'
                                    || this.$store.state.loginInfo.authorities[0].roleCd ==='FINANCE'
                                    || this.$store.state.loginInfo.authorities[0].roleCd ==='ACCOUNT'
                                    || this.$store.state.loginInfo.authorities[0].roleCd ==='HR'
                                    || this.deptAuth.length > 0" value='all'>==전체==</option>

                      <option :value='this.$store.state.loginInfo.loginId'>{{this.$store.state.loginInfo.userName}}</option>
                      <option
                        v-for="item in delegateList"
                        :key="item.giveUserId"
                        :value="item.giveUserId"
                        v-text="item.giveUserNm"/>
                    </select>
                  </div>
                </div>
                <div class="search_box" style="width: 30%">
                    <button class="item-list icon is-right btn_s_w" @click="openModal()" type="button">상세검색<i class="fas fa-plus"></i></button>
                </div>
<!--                <div style="display: flex; justify-content: flex-end;"  v-if="appCBtn">
                  <button :class="buttonColor"  style="border-right:none;"  @click="appWriting('exctpExpense')">개인비용외</button>
                  <button :class="buttonColor1" style="border-right:none;"  @click="appWriting('ebill')">전자세금계산서</button>
                  <button :class="buttonColor2"  @click="appWriting('bill')">수기세금계산서</button>
                </div>-->

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
                                    <span class="search_tit">- 작성부서</span>
                                </div>
                                <div class="search_con search-area">
                                    <div v-if="this.$store.state.loginInfo.authorities[0].roleCd ==='ADMIN'
                                               || this.$store.state.loginInfo.authorities[0].roleCd ==='FINANCE'
                                               || this.$store.state.loginInfo.authorities[0].roleCd ==='ACCOUNT'
                                               || this.$store.state.loginInfo.authorities[0].roleCd ==='HR'
                                               || this.deptAuth.length > 0" class="desc">
                                        <input class="input dp-ib input-bg w40p_5r" type="text" v-model="form.wrtDeptCd" disabled>
                                        <div class="dp-ib w60p">
                                            <input class="input" type="text" v-model="form.wrtDeptNm" @input="initCctr" @keypress.enter="popCctr">
                                            <button class="icon is-right" @click="popCctr"><i class="fas fa-search"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div v-else class="desc">
                                        <input class="input dp-ib input-bg w40p_5r" type="text" style="width:100px;" v-model="form.wrtDeptCd" disabled>
                                        <div class="dp-ib w60p">
                                            <input class="input" type="text" v-model="form.wrtDeptNm" @input="initCctr" @keypress.enter="popCctr" disabled>
                                        </div>
                                    </div>
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
                                    <span class="search_tit">- 작성자</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input dp-ib w40p_5r" type="text" v-model="form.wrtId" disabled>
                                    <div  class="dp-ib w60p">
                                        <input class="input" type="text" v-model="form.wrtNm" @input="initEmp" @keypress.enter="popEmp">
                                        <button class="icon is-right" @click="popEmp"><i class="fas fa-search"></i>
                                        </button>
                                    </div>
<!--                                    <div v-else class="dp-ib w60p">
                                        <input class="input" type="text" v-model="form.wrtNm" @input="initEmp" @keypress.enter="popEmp" disabled>
                                    </div>-->
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
                                    <span class="search_tit">- 전표번호</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input Rt-M tal w100p" type="text" v-model="form.slipNo">
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 적요</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input Rt-M tal w100p" type="text" v-model="form.hdSgtxt">
                                </div>
                            </div>

                            <div class="search_box_pop">
                              <div class="search_title">
                                <span class="search_tit">- 통화</span>
                              </div>
                              <div class="search_con search-area">
                                <select class="select is-fullwidth w100p" v-model="form.usedCur">
                                  <option value=''>==전체==</option>
                                  <option
                                    v-for="item in currencyList"
                                    :key="item.key"
                                    :value="item.key"
                                    v-text="item.key"/>
                                </select>
                              </div>
                            </div>

                            <div class="search_box_pop">
                              <div class="search_title">
                                <span class="search_tit">- 결제조건</span>
                              </div>
                              <div class="search_con search-area">
                                <input class="input dp-ib w40p_5r" type="text" v-model="form.termCd" disabled>
                                <input class="input dp-ib w60p" type="text" v-model="form.termNm" @input="initTerms" @keypress.enter="popTerms">
                                <button class="icon is-right" @click="popTerms"><i class="fas fa-search"></i></button>
                              </div>
                            </div>

                            <div class="search_box_pop">
                              <div class="search_title">
                                <span class="search_tit">- 지급여부</span>
                              </div>
                              <div class="search_con search-area">
                                <select class="select is-fullwidth w100p" v-model="form.intStatus">
                                  <option value=''>==전체==</option>
                                  <option value="Y">Y</option>
                                  <option value="N">N</option>
                                  <option value="C">C</option>
                                </select>
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

        <!-- 테이블 -->
        <div class="table-area">

            <div class="table-b">
                <div class="table-header">
                    <div class="table-name">
                        <h3 class="ico_table_name">전표내역</h3>
                    </div>
                    <div class="btn-wrap btn-type1 clearfix">
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
                        @selection-changed="onSelectionChanged"
                        />
                </div>

              <div class="pagingbox">
                <button class="btn-page page-first" @click="goFirst" :disabled="page.page==0 || page.count==0"></button>
                <button class="btn-page page-prev" @click="goPre" :disabled="page.page==0 || page.count==0"></button>

                <button :class="page.page==item.value-1 ? 'page-num active' : 'page-num'"
                  v-for="(item, idx) in page.pageList"
                  :key="idx"
                  :value="item.value"
                  @click="goPageNum(item.value)">{{item.value}}</button>

                <button class="btn-page page-next" @click="goNext" :disabled="page.page==page.lastPage || page.count==0"></button>
                <button class="btn-page page-last" @click="goEnd" :disabled="page.page==page.lastPage || page.count==0"></button>
              </div>
            </div>
            <div class="grid-wrap" v-show="false">
              <ag-grid-vue
                  ref="excel"
                  style="width: 100%;"
                  class="ag-theme-alpine grid_search_height_350"

                  :columnDefs="columnDefsExcel"
                  :gridOptions="gridOptionsExcel"
                  :rowData="slipListExcel"
              />
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
    import ApprBundleSubmTemp from "@/views/ApprBundleSubmTemp";
    import SlipGlDetailModal from "@/components/SlipGlDetailModal";
    import SlipDetailModal from "@/components/SlipDetailModal";
    import SlipBulkDetailModal from "@/components/SlipBulkDetailModal";
    import SlipBondDetailModal from "@/components/SlipBondDetailModal";
    import SlipFundDetailModal from "@/components/SlipFundDetailModal";
    import SlipCollectionDetailModal from "@/components/SlipCollectionDetailModal";
    import Draft from "@/components/costBudget/SlipBudgetDetailModal";
    import TermsPop from "@/components/TermsPop";

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
            dateSet: {
              type: Object,
              required: false
            }
        },
        data() {
            return {
                title: '전표내역조회',
                slipTypes: [],
                slipStats: [],
                slipList: [],
                slipListExcel: [],
                trxList: [],
                deptAuth: [],
                delegateList: [],
                currencyList: [],
                authority: '',
                openDt:'',
                postDt: [this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')],
                regDt: ['' , ''],
                form: {
                    postDtFrom: this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD'),
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
                    termCd: '',
                    termNm: '',
                    intStatus: '',
                    usedCur: '',
                },
                syncGoPage: false,
                gridOptions: {},
                gridOptionsExcel: {},
                columnDefs:[],
                columnDefsExcel:[],
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
                appCBtn : false,
                appExctpExpenseCBtn : false,
                appEbillCBtn : false,
                appBillCBtn : false,
                buttonColor:'item-list1 btn_s_o_w',
                buttonColor1:'item-list1 btn_s_o_w',
                buttonColor2:'item-list1 btn_s_o_w',
                returnFlag : false,
                submitChk : false,
                initPage: false,
                page:{
                  page: 0,
                  limit: 100,
                  count: 0,
                  lastPage: 0,
                  pageList: []
                },
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
                        return params.value ? that.$moment(params.value).format('YYYY-MM-DD') : null
                      }
                    },
                    {
                      headerName:'거래유형',
                      field:'slipType',
                      width:120,
                      valueFormatter: (params) => {
                        switch(params.value){
                          case '21':
                            return '건별지급'
                          case '22':
                            return '대량지급'
                          case '23':
                            return '전자채권만기'
                          case '24':
                            return '자금전표'
                          case '25':
                            return '집금전표'
                          case '27':
                            return 'GL전표'
                          case '90':
                            return '비용예산'
                          default:
                            for(var i=0; i<that.trxList.length; i++){
                              if(that.trxList[i].trxTypeCd === params.value){
                                return that.trxList[i].trxTypeNm
                              }
                            }
                        }

                      }
                    },
                    {headerName:'전표상태', field:'status', width:110, hide: true},
                    {headerName:'전표상태', field:'statusNm', width:110, cellStyle:{textAlign: 'center'}},
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
                      headerName:'작성부서명',
                      field:'deptNo',
                      width:150,
                      hide: true
                    },
                    {
                      headerName:'작성자',
                      field:'empNm',
                      cellStyle:{textAlign: 'center'},
                      width:100
                    },
                    {
                      headerName:'작성자',
                      field:'empNo',
                      width:100,
                      hide: true
                    },
                    {
                      headerName:'적요',
                      field:'headerRemark',
                      width:300
                    },
                    {
                      headerName:'PO번호',
                      field:'poNumber',
                      width:150
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
                      width:160,
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
                      cellStyle:{textAlign: 'center'},
                      width:100
                    },
                    {
                      headerName:'통화',
                      field:'usedCur',
                      cellStyle:{textAlign: 'center'},
                      width:90
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
                      headerName:'문서결제구분',
                      field:'apprTypeCd',
                      cellStyle:{textAlign: 'center'},
                      width:120,
                      valueFormatter: (params) => {
                        if(params.value == '01'){
                          return '결재'
                        }else if(params.value == '02'){
                          return '검인'
                        }
                        return that.getNumberFormat(params.value);
                      },
                    },
                    {
                      headerName:'문서결제대기자',
                      field:'nextAppUserNm',
                      cellStyle:{textAlign: 'center'},
                      width:140
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
                        cellStyle:{textAlign: 'center'},
                        width:130,
                        /*cellRenderer: (params) => {
                            if(params.data.erpInvoiceId !== ''){
                                return '<div style="text-decoration: underline; text-underline-position: under; color:blue;">'+params.data.erpStatus+'</div>'
                            }
                        }*/
                    },
                    {
                      headerName:'작성일자',
                      field:'regDtm',
                      width:110,
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
                ];

              this.columnDefsExcel =[
                {headerName:'전표번호', field:'slipNo', width:185},
                {headerName:'회계일자', field:'postingDt', width:110,},
                {headerName:'결제예정일', field:'termDueDate', width:110,},
                {headerName:'거래유형', field:'slipType', width:120,},
                {headerName:'전표상태', field:'statusNm', width:110,},
                {headerName:'작성부서명', field:'deptNm', width:150},
                {headerName:'작성자', field:'empNm', width:100},
                {headerName:'적요', field:'headerRemark', width:300},
                {headerName:'PO번호', field:'poNumber', width:150},
                {headerName:'공급가', field:'supplyAmount', width:150,},
                {headerName:'부가세액', field:'taxAmount', width:150,},
                {headerName:'총금액(KRW)', field:'usedAmt', width:160,},
                {headerName:'거래처코드', field:'integrationVendorNum', width:110,},
                {headerName:'거래처명', field:'integrationVendorName', width:200},
                {headerName:'결제조건', field:'termName', width:200},
                {headerName:'지급여부', field:'intStatus', width:100},
                {headerName:'통화', field:'usedCur', width:90},
                {headerName:'총금액', field:'usedForAmt', width:130,},
                {headerName:'재경의견', field:'apprDesc', width:200},
                {headerName:'문서결재구분', field:'apprTypeCd', width:120,},
                {headerName:'문서결제대기자', field:'nextAppUserNm', width:140},
                {headerName:'ERP전송상태', field:'slipIfFlag', width:130,},
                {headerName:'작성일자', field:'regDtm', width:110,},
              ];
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
            onSelectionChanged(){
              const Row = this.gridApi.getSelectedRows();

              this.totalAmt = 0

              //합계 금액 계산
              for(var i=0; i<Row.length; i++){
                this.totalAmt += this.$numeral(Row[i].usedAmt).value()
              }
            },
            rowDoubleClick(params){

              const searchForm = {
                compCd: this.$store.state.loginInfo.compCd,
                postDtFrom: this.$moment(this.postDt[0]).format('YYYYMMDD'),
                postDtTo: this.$moment(this.postDt[1]).format('YYYYMMDD'),
                wrtDeptCd: this.form.wrtDeptCd,
                wrtDeptNm: this.form.wrtDeptNm,
                evdCustNm: this.form.evdCustNm,
                slipTypeCd: this.form.trxTypeCd,
                wrtId: this.form.wrtId,
                wrtNm: this.form.wrtNm,
                regDtFrom: this.regDt[0] ? this.$moment(this.regDt[0]).format('YYYYMMDD') : undefined,
                regDtTo: this.regDt[1] ? this.$moment(this.regDt[1]).format('YYYYMMDD') : undefined,
                slipStatCd: this.form.slipStatCd,
                slipNo: this.form.slipNo,
                hdSgtxt: this.form.hdSgtxt,
                page: this.page.page,
                pageSize: this.page.limit,
                postDt: [this.$moment(this.postDt[0]).format('YYYY-MM-DD'), this.$moment(this.postDt[1]).format('YYYY-MM-DD')],
                regDt: [this.regDt[0] ? this.$moment(this.regDt[0]).format('YYYY-MM-DD') : '', this.regDt[1] ? this.$moment(this.regDt[1]).format('YYYY-MM-DD') : ''],
                // regDt: [this.$moment(this.regDt[0]).format('YYYY-MM-DD'), this.$moment(this.regDt[1]).format('YYYY-MM-DD')],
                delegateNo: this.form.delegateNo,
                trxTypeCd: this.form.trxTypeCd,
                trxTypeNm: this.form.trxTypeNm,
                usedCur: this.form.usedCur,
                termCd: this.form.termCd,
                termNm: this.form.termNm,
                intStatus: this.form.intStatus,
              }
              
              this.$cookie.set('searchForm', JSON.stringify(searchForm));
              this.$store.commit('searchForm', JSON.parse(this.$cookie.get('searchForm')));

              const vm = this;

              if(params.data.slipType == '21'){ //건별지급
                this.$modal.open({
                  component: SlipDetailModal,
                  parent: this,
                  props: {
                    slipNo : params.data.slipNo,
                    slipTypeCd: params.data.slipTypeCd,
                    title : '건별지급',
                  },
                  width: 1200,
                  events: {
                    close(obj){
                      vm.goSearch();
                    }
                  }
                })
              }else if(params.data.slipType == '22'){ //대량지급
                this.$modal.open({
                  component: SlipBulkDetailModal,
                  parent: this,
                  props: {
                    slipNo : params.data.slipNo,
                    slipType: params.data.slipType,
                    title : '대량지급',
                  },
                  width: 1200,
                  events: {
                    close(obj){
                      vm.goSearch();
                    }
                  }
                })
              }else if(params.data.slipType == '23'){ //전자채권만기
                this.$modal.open({
                  component: SlipBondDetailModal,
                  parent: this,
                  props: {
                    slipNo : params.data.slipNo,
                    slipType: params.data.slipType,
                    title : '전자채권만기',
                  },
                  width: 1200,
                  events: {
                    close(obj){
                      vm.goSearch();
                    }
                  }
                })
              }else if(params.data.slipType == '24'){ //자금전표
                this.$modal.open({
                  component: SlipFundDetailModal,
                  parent: this,
                  props: {
                    slipNo : params.data.slipNo,
                    slipType: params.data.slipType,
                    title : '자금전표',
                  },
                  width: 1200,
                  events: {
                    close(obj){
                      vm.goSearch();
                    }
                  }
                })
              }else if(params.data.slipType == '25'){ //집금전표
                this.$modal.open({
                  component: SlipCollectionDetailModal,
                  parent: this,
                  props: {
                    slipNo : params.data.slipNo,
                    slipType: params.data.slipType,
                    title : '집금전표',
                  },
                  width: 1200,
                  events: {
                    close(obj){
                      vm.goSearch();
                    }
                  }
                })
              }else if(params.data.slipType == '27'){ //GL전표
                this.$modal.open({
                  component: SlipGlDetailModal,
                  parent: this,
                  props: {
                    slipNo : params.data.slipNo,
                    slipType: params.data.slipType,
                    title : 'GL전표',
                  },
                  width: 1200,
                  events: {
                    close(obj){
                      vm.goSearch();
                    }
                  }
                })
              }else if(params.data.slipType == '90'){ //비용예산
                this.$modal.open({
                  component: Draft,
                  parent: this,
                  props: {
                    slipNo : params.data.slipNo,
                    slipType: params.data.slipType,
                    title: '비용예산',
                    slipHeaderId : params.data.slipHeaderId,
                    status : params.data.status,
                  },
                  width: 1800,
                  events: {
                    close(obj){
                      vm.goSearch();
                    }
                  }
                })
              }else {
                this.$router.push({path: `/accrualSlip/${params.data.slipNo}`});
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
                        this.slipStats = response.data.filter(x => (x.key != '01' && x.key != '07'))
                    });
            },
            getcurrencyList(){
              this.$http.get(`/api/code/combo`, {params: {groupCd: "CUR_CD"}})
                .then(response => {
                  this.currencyList = response.data;
                });
            },
            getTrxList() {
              this.$http.post(`/api/trx/list`, {params: {compCd: this.$store.state.loginInfo.compCd}})
                .then(response => {
                  this.trxList = response.data;
                });
            },
            getDeptAuth(){
              this.$http.post(`/api/dept/deptAuth/slipLst`, {
                compCd: this.$store.state.loginInfo.compCd,
                empNo: this.$store.state.loginInfo.loginId,
                remark: this.$store.state.loginInfo.authorities[0].roleCd
              })
                .then(response => {
                  this.deptAuth = response.data
                })
            },
            getDelegateList() {
              this.$http.post(`/api/delegate/personal`,
                {  compCd: this.$store.state.loginInfo.compCd,
                        receiveUserId : this.$store.state.loginInfo.loginId,
                        startDate: this.$moment().format('YYYYMMDD'),
                        endDate: this.$moment().format('YYYYMMDD'),
                        delegateStatCd: '1',
                })
                .then(response => {
                  this.delegateList = response.data;
                });
            },
            saveExcel() {

              let vm = this;
              let searchDept = this.$store.state.loginInfo.loginDeptCd
              //부서권한 조회 예외 처리
              if(this.$store.state.loginInfo.authorities[0].roleCd != 'ADMIN'
                  && this.$store.state.loginInfo.authorities[0].roleCd != 'ACCOUNT'
                  && this.$store.state.loginInfo.authorities[0].roleCd != 'FINANCE'
                  && this.$store.state.loginInfo.authorities[0].roleCd != 'HR'
                  && this.deptAuth.length>0 && !this.form.wrtDeptCd){
                for(let i=0; i<this.deptAuth.length; i++){
                  searchDept += ',' + this.deptAuth[i].deptCd
                }
              }else{
                searchDept = this.form.wrtDeptCd
              }

              this.compareDate(this.postDt);
              const param = {
                compCd: this.$store.state.loginInfo.compCd,
                postDtFrom: this.$moment(this.postDt[0]).format('YYYYMMDD'),
                postDtTo: this.$moment(this.postDt[1]).format('YYYYMMDD'),
                wrtDeptCd: searchDept,
                evdCustNm: this.form.evdCustNm,
                slipTypeCd: this.form.trxTypeCd,
                wrtId: this.form.wrtId,
                regDtFrom: this.regDt[0] ? this.$moment(this.regDt[0]).format('YYYYMMDD') : undefined,
                regDtTo: this.regDt[1] ? this.$moment(this.regDt[1]).format('YYYYMMDD') : undefined,
                slipStatCd: this.returnFlag ? '' : this.form.slipStatCd, // 반려 조회 일때는 상태 값 적용 제외
                slipNo: this.form.slipNo,
                hdSgtxt: this.form.hdSgtxt,
                page: this.page.page,
                pageSize: this.page.limit,
                returnFlag : this.returnFlag,
                currency: this.form.usedCur,
                termId: this.form.termCd,
                intStatus: this.form.intStatus,
              }


              this.$store.commit('loading');
              this.$http.post(`/api/slip/history/excel`, param)
              .then(response => {
                let excelListTemp = response.data;
                excelListTemp.map(x => {
                  x.slipType = this.getSlipType(x.slipType);
                  if(x.apprTypeCd === '01') {
                    x.apprTypeCd = '결재'
                  } else if(x.apprTypeCd === '02') {
                    x.apprTypeCd = '검인'
                  }
                })
                this.slipListExcel = excelListTemp;

              }).finally(() => {
                var params = {
                  fileName : "전표내역"
                };
                this.gridOptionsExcel.api.exportDataAsCsv(params)
                this.$store.commit('finish')
              });

            },
            onRowSelected(params) {
              const rowIdx = params.rowIndex;
              //this.data[rowIdx].regYn = params.node.isSelected();
            },
            goSubmit() {
              const Rows = this.gridApi.getSelectedRows();

              if(Rows.length == 0){
                this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
                return
              }

              let regEmp = Rows[0].empNo

              for(let i=0; i<Rows.length; i++){
                if(Rows[i].status != 'SV' && Rows[i].status != 'VC'){
                  this.$swal({ type: 'warning', text: '상신 가능한 전표를 선택해주세요.' });
                  return
                }

                if(Rows[i].empNo != regEmp){
                  this.$swal({ type: 'warning', text: '한 명의 작성자로만 상신가능합니다.' });
                  return
                }

                if(this.$store.state.loginInfo.authorities[0].roleCd != 'ADMIN'){
                  let check = false
                  if(Rows[i].empNo == this.$store.state.loginInfo.loginId){
                    check = true
                  }else{
                    for(let j=0; j<this.delegateList.length; j++){
                      if(this.delegateList[j].giveUserId == Rows[i].empNo){
                        check = true;
                        break;
                      }
                    }
                  }
                  if(!check){
                    this.$swal({ type: 'warning', text: '다른 사람의 전표는 상신할 수 없습니다.' });
                    return
                  }
                }
              }

              this.$modal.open({
                component: ApprBundleSubmTemp,
                props: {
                  value: Rows,
                  apprTitle : '전표',
                  slipType : 'SLIP'
                },
                parent: this,
                width: 1800,
                events: {
                  close() {

                  }
                }
              })
            },
            compareDate(value){
              if(!value){
                this.$swal({ type: 'info', text: '회계일자를 입력해주세요.' });
                return false;
              }
            },
            getCount(){


              let searchDept = this.$store.state.loginInfo.loginDeptCd
              //부서권한 조회 예외 처리
              if(this.$store.state.loginInfo.authorities[0].roleCd != 'ADMIN'
                    && this.$store.state.loginInfo.authorities[0].roleCd != 'ACCOUNT'
                    && this.$store.state.loginInfo.authorities[0].roleCd != 'FINANCE'
                  && this.$store.state.loginInfo.authorities[0].roleCd != 'HR'
                    && this.deptAuth.length>0 && !this.form.wrtDeptCd){
                for(let i=0; i<this.deptAuth.length; i++){
                  searchDept += ',' + this.deptAuth[i].deptCd
                }
              }else{
                searchDept = this.form.wrtDeptCd
              }


              this.$http.post(`/api/slip/history/count`, {
                  compCd: this.$store.state.loginInfo.compCd,
                  postDtFrom: this.$moment(this.postDt[0]).format('YYYYMMDD'),
                  postDtTo: this.$moment(this.postDt[1]).format('YYYYMMDD'),
                  wrtDeptCd: searchDept,
                  evdCustNm: this.form.evdCustNm,
                  slipTypeCd: this.form.trxTypeCd,
                  wrtId: this.form.wrtId,
                  regDtFrom: this.regDt[0] ? this.$moment(this.regDt[0]).format('YYYYMMDD') : undefined,
                  regDtTo: this.regDt[1] ? this.$moment(this.regDt[1]).format('YYYYMMDD') : undefined,
                  slipStatCd: this.returnFlag ? '' : this.form.slipStatCd, // 반려 조회 일때는 상태 값 적용 제외
                  slipNo: this.form.slipNo.trim(),
                  hdSgtxt: this.form.hdSgtxt,
                  returnFlag : this.returnFlag,
                  currency: this.form.usedCur,
                  termId: this.form.termCd,
                  intStatus: this.form.intStatus,
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
            goSearch(flag) {

                // 반려전표 조회 기능 추가
                this.returnFlag = flag === true ? true : false

                if(this.initPage || flag){
                  this.initPage = false
                  this.page.page = 0
                }

                let searchDept = this.$store.state.loginInfo.loginDeptCd
                //부서권한 조회 예외 처리
                if(this.$store.state.loginInfo.authorities[0].roleCd != 'ADMIN'
                      && this.$store.state.loginInfo.authorities[0].roleCd != 'ACCOUNT'
                      && this.$store.state.loginInfo.authorities[0].roleCd != 'FINANCE'
                      && this.$store.state.loginInfo.authorities[0].roleCd != 'HR'
                      && this.deptAuth.length>0 && !this.form.wrtDeptCd){
                  for(let i=0; i<this.deptAuth.length; i++){
                    searchDept += ',' + this.deptAuth[i].deptCd
                  }
                }else{
                  searchDept = this.form.wrtDeptCd
                }


                this.getCount()
                this.compareDate(this.postDt);
                const param = {
                  compCd: this.$store.state.loginInfo.compCd,
                  postDtFrom: this.$moment(this.postDt[0]).format('YYYYMMDD'),
                  postDtTo: this.$moment(this.postDt[1]).format('YYYYMMDD'),
                  wrtDeptCd: searchDept,
                  evdCustNm: this.form.evdCustNm,
                  slipTypeCd: this.form.trxTypeCd,
                  wrtId: this.form.wrtId,
                  regDtFrom: this.regDt[0] ? this.$moment(this.regDt[0]).format('YYYYMMDD') : undefined,
                  regDtTo: this.regDt[1] ? this.$moment(this.regDt[1]).format('YYYYMMDD') : undefined,
                  slipStatCd: this.returnFlag ? '' : this.form.slipStatCd, // 반려 조회 일때는 상태 값 적용 제외
                  slipNo: this.form.slipNo.trim(),
                  hdSgtxt: this.form.hdSgtxt,
                  page: this.page.page,
                  pageSize: this.page.limit,
                  returnFlag : this.returnFlag,
                  currency: this.form.usedCur,
                  termId: this.form.termCd,
                  intStatus: this.form.intStatus,
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
            cancelAppr(){
                let rows = this.gridApi.getSelectedRows();
                rows.filter((x) => x.apprGroupId = x.approvalGroupId);

                if(rows.length == 0){
                    this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
                    return
                }

                for(let i=0; i<rows.length; i++){
                    if(rows[i].status != 'AP'){
                      this.$swal({ type: 'warning', text: '상신 취소 가능한 전표를 선택해주세요.' });
                      return
                    }
                }

              this.$swal({
                type: 'question',
                html: '선택한 항목을 상신 취소하시겠습니까?',
                showCancelButton: true
              }).then(r => {
                if (r.value) {
                  this.$store.commit('loading')
                  this.$http.post(`/api/appr/bundle/cancel`, rows)
                  .then((response) => {
                    this.$swal({ type: 'success', text: '상신을 취소하였습니다.' })
                    .then((result) => {
                      if (result.value) {
                        this.goSearch();
                      }
                    });
                  })
                  .catch((e) => {
                    this.$swal({ type: 'warning', text: '상신 취소를 실패하였습니다.'});
                  })
                  .finally(() => {
                    this.$store.commit('finish')
                  });
                }
              })


            },
            getStartDate(){
              this.$store.commit('loading');
              this.$http.get(`/api/acctPeriod/openDate`)
              .then(response => {
                this.openDt = response.data
                //this.form.postDtFrom = this.$moment(response.data,'YYYYMMDD').format('YYYY-MM-DD')
                //this.form.postDtTo = this.$moment(this.form.postDtFrom).endOf('month').format('YYYY-MM-DD')

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
                    this.$swal({type: 'warning', text: '회계일자를 입력해 주세요.'});
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

                if(this.$store.state.loginInfo.authorities[0].roleCd != 'ADMIN'
                      && this.$store.state.loginInfo.authorities[0].roleCd != 'ACCOUNT'
                      && this.$store.state.loginInfo.authorities[0].roleCd != 'FINANCE'
                      && this.$store.state.loginInfo.authorities[0].roleCd != 'HR'
                      && this.deptAuth.length>0){
                  this.$modal.open({
                    component: CctrDeptRole,
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
                }else{
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
                }


            },
            initCctr(force) {
                if (force === true) this.form.wrtDeptNm = '';
                if (this.form.wrtDeptNm === '') this.form.wrtDeptCd = '';
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
                  }
                }
              });
            },
            initEmp(force) {
                if (force === true) this.form.wrtNm = '';
                if (this.form.wrtNm === '') {
                    this.form.wrtId = '';
                }
            },
            popTerms() {
              const that = this;

              this.$modal.open({
                component: TermsPop,
                props: {
                  schTxt: that.form.termNm
                },
                parent: this,
                width: 700,
                events: {
                  close(obj) {
                    that.form.termCd = obj.termId;
                    that.form.termNm = obj.description;
                  }
                }
              });
            },
            initTerms(force) {
              if (force === true) this.form.termNm = '';
              if (this.form.termNm === '') {
                this.form.termCd = '';
              }
            },
            setCallPageByParam(){
              if (this.$route.params) {
                    for (const member in this.form) {
                        if (this.$route.params[member]) {
                          if(member === 'postDt') {
                            this.postDt = this.$route.params.postDt;
                          }
                          else if(member === 'regDt') {
                            this.regDt = this.$route.params.regDt;
                          }
                          else {
                            this.form[member] = this.$route.params[member]
                          }
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
            this.postDt = this.dateSet ? [this.$moment(this.dateSet.startDate).format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')] : [this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')];
            this.regDt = ['' , ''];
            this.form.postDtFrom = this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD')
            this.form.postDtTo = this.$moment().format('YYYY-MM-DD')
            this.form.evdCustNm = ''
            this.form.trxTypeCd = ''
            this.form.trxTypeNm = ''
            this.form.slipStatCd = this.$route.query.slipStatCd ? this.$route.query.slipStatCd : '';
            this.form.slipNo = ''
            this.form.hdSgtxt = ''
            this.form.regDtFrom = ''
            this.form.regDtTo = ''
            this.form.usedCur = ''
            this.form.intStatus = ''
            this.form.termNm = ''
            this.form.termCd = ''
            if(this.authority == '10'){
                this.form.wrtDeptCd = ''
                this.form.wrtDeptNm = ''
                this.form.wrtId = ''
                this.form.wrtNm =''
                this.form.delegateNo = this.$store.state.loginInfo.loginId
            }else{
                this.form.wrtId = this.$store.state.loginInfo.loginId
                this.form.wrtNm = this.$store.state.loginInfo.userName
                this.form.delegateNo = this.$store.state.loginInfo.loginId
            }
          },
          getSlipType(param){
            switch(param){
              case '21':
                return '건별지급'
              case '22':
                return '대량지급'
              case '23':
                return '전자채권만기'
              case '24':
                return '자금전표'
              case '25':
                return '집금전표'
              case '27':
                return 'GL전표'
              case '90':
                return '비용예산'
              default:
                for(var i=0; i<this.trxList.length; i++){
                  if(this.trxList[i].trxTypeCd === param){
                    return this.trxList[i].trxTypeNm
                  }
                }
              }
            }
        },
        created() {
            let vm = this
            if(vm.$route.query.slipStatCd || _.isEmpty(this.params)){
              this.getStartDate()
              this.getSlipStatCombo()
              this.getTrxList()
              this.getcurrencyList()
              this.getDelegateList()
              this.getDeptAuth()
              this.resetSearch()

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

          if(this.$route.params) {
            this.getTrxList()
            this.getSlipStatCombo();
            this.getDelegateList();
            this.setCallPageByParam();
            this.getcurrencyList();
            // 메인에서 전표 반려 버튼을 눌렀을때, 반려 조회를 기본으로
            if(this.$route.query.slipStatCd === 'AR'){
              console.log("반려조회")
              this.goSearch(true);
            }else{
              this.goSearch();
              console.log("반려조회아님")
            }
          }
          // this.getSlipTypeCombo()


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


          'form.delegateNo': {
            handler(value) {
              if(value == 'all'){
                this.form.wrtDeptCd = ''
                this.form.wrtDeptNm = ''
                this.form.wrtId = ''
                this.form.wrtNm = ''
              }else{
                if(this.$store.state.loginInfo.loginId == value){
                  this.form.wrtDeptCd = this.$store.state.loginInfo.loginDeptCd
                  this.form.wrtDeptNm = this.$store.state.loginInfo.loginDeptNm
                  this.form.wrtId = this.$store.state.loginInfo.loginId
                  this.form.wrtNm = this.$store.state.loginInfo.userName
                }else{
                  for(var i=0; i<this.delegateList.length; i++){
                    if(this.delegateList[i].giveUserId == value){
                      this.form.wrtDeptCd = this.delegateList[i].deptCd
                      this.form.wrtDeptNm = this.delegateList[i].deptNm
                      this.form.wrtId = this.delegateList[i].giveUserId
                      this.form.wrtNm = this.delegateList[i].giveUserNm
                      break;
                    }
                  }
                }
              }
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
</style>
