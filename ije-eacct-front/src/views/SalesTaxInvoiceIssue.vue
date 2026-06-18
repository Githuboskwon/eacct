<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <!-- <button class="btn-size btn-blue fl_left" @click="goFirst"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span> 조회</button> -->
        <el-button size="large" type="primary" icon="el-icon-search" @click="goFirst">조회</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="search_box" style="width: 35%">
        <div class="search_title">
          <span class="search_tit" style="color: #CC3D3D;">- 회계일자</span>
        </div>
        <div class="search_con">
<!--          <div class="datepicker w-type-ymd02">-->
<!--            <dhx-calendar class="input ddate sDate" v-model="form.postDtFrom" />-->
<!--          </div>-->
<!--          <span class="wave"> ~ </span>-->
<!--          <div class="datepicker w-type-ymd02">-->
<!--            <dhx-calendar class="input ddate sDate" v-model="form.postDtTo" />-->
<!--          </div>-->
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
      </div>

      <div class="item desc-center">
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
                  <span class="search_tit">- 회계일자</span>
                </div>
                <div class="search_con search-area">
<!--                  <div class="datepicker w-type-ymd02 w30p">-->
<!--                    <dhx-calendar class="input ddate sDate" v-model="form.postDtFrom" />-->
<!--                  </div>-->
<!--                  <span class="datepicker w10p dp-ib tac"> ~ </span>-->
<!--                  <div class="datepicker w-type-ymd02 w30p">-->
<!--                    <dhx-calendar class="input ddate sDate" v-model="form.postDtTo" />-->
<!--                  </div>-->
                  <el-date-picker
                      v-model="postDt"
                      type="daterange"
                      align="right"
                      unlink-panels
                      style="width: 70%"
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
                  <span class="search_tit">- 거래처</span>
                </div>
                <div class="search_con search-area">
                  <input class="input dp-ib input-bg w40p_5r" type="text" v-model="form.integrationVendorNum" disabled>
                  <div class="dp-ib w60p">
                    <input class="input" type="text" v-model="form.integrationVendorName" @input="initVendor" @keypress.enter="popVendor">
                    <button class="icon is-right" @click="popVendor"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 처리상태</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth" v-model="form.confirmFlag">
                    <option value="X">미발행</option>
                    <option value="ALL">전체</option>
                    <option value="M">공급받는자 발행취소요청</option>
                    <option value="N">공급자 발행취소요청</option>
                    <option value="R">수신거부</option>
                    <option value="C">수신승인</option>
                    <option value="T">역발행 거부</option>
                    <option value="V">역발행 요청</option>
                    <option value="W">역발행 요청 취소</option>
                    <option value="S">저장</option>
                    <option value="O">취소완료</option>
                    <option value="I">수신미승인</option>
                  </select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 발행일자</span>
                </div>
                <div class="search_con search-area">
<!--                  <div class="datepicker w-type-ymd02 w30p">-->
<!--                    <dhx-calendar class="input ddate sDate" v-model="form.postDtFrom" />-->
<!--                  </div>-->
<!--                  <span class="datepicker w10p dp-ib tac"> ~ </span>-->
<!--                  <div class="datepicker w-type-ymd02 w30p">-->
<!--                    <dhx-calendar class="input ddate sDate" v-model="form.postDtTo" />-->
<!--                  </div>-->
                  <el-date-picker
                      v-model="issueDt"
                      type="daterange"
                      align="right"
                      unlink-panels
                      style="width: 70%"
                      range-separator="~"
                      start-placeholder="시작일"
                      end-placeholder="종료일">
                  </el-date-picker>
                  <button @click="dateSetting('toDay','issueDt')" class="search_bt_white_s">당일</button>
                  <button @click="dateSetting('crrntMnth','issueDt')" class="search_bt_white_s">당월</button>
                  <button @click="dateSetting('PrvsMnth','issueDt')" class="search_bt_white_s">전월</button>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 부서</span>
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
            <h3 class="ico_table_name">거래처 목록</h3>
          </div>
          <div class="btn-type1 clearfix">
            <el-button type="primary" icon="el-icon-takeaway-box" @click="etaxIssue">세금계산서 발행</el-button>
            <el-button type="primary" icon="el-icon-takeaway-box" @click="etaxModify">수정세금계산서 발행</el-button>
            <el-button type="success" icon="el-icon-folder-checked" @click="reverseSave">역발행저장</el-button>
            <el-button type="danger" icon="el-icon-close" @click="reverseCancel">역발행취소</el-button>
            <el-button type="info" icon="el-icon-refresh" @click="etaxReIssue">발행재시도</el-button>
            <el-button type="danger" icon="el-icon-close" @click="cancelIssue">발행취소</el-button>
            <el-button type="primary" icon="el-icon-refresh" @click="emailReSend">메일 재발송</el-button>
            <el-button type="info" icon="el-icon-tickets" @click="preview('SUP')">세금계산서 보기(공급자용)</el-button>
            <el-button type="info" icon="el-icon-tickets" @click="preview('BYR')">세금계산서 보기(공급받는자용)</el-button>
          </div>
        </div>


        <div class="grid-tbl-box">

          <ag-grid-vue
            ref="grid"
            style="width: 100%; height: 400px;"
            class="ag-theme-alpine"
            rowSelection="multiple"

            :columnDefs="columnDefs"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponent"
            :rowData="invoiceList"
            :gridOptions="gridOptions"
            :suppressRowClickSelection="true"
            :enableRangeSelection="false"
            @grid-ready="onReady"
            @row-selected="onRowSelected"
            @selection-changed="onSelectionChanged"
            @cell-clicked="onCellClicked"
            @cell-value-changed="cellValueChange"
          />
        </div>

<!--        <div class="pagingbox">
          <button class="btn-page page-first" @click="goFirst" :disabled="page.page==0 || page.count==0"></button>
          <button class="btn-page page-prev" @click="goPre" :disabled="page.page==0 || page.count==0"></button>

          <button :class="page.page==item.value-1 ? 'page-num active' : 'page-num'"
                  v-for="item in page.pageList"
                  :value="item.value"
                  @click="goPage(item.value)">{{item.value}}</button>

          <button class="btn-page page-next" @click="goNext" :disabled="page.page==page.lastPage || page.count==0"></button>
          <button class="btn-page page-last" @click="goEnd" :disabled="page.page==page.lastPage || page.count==0"></button>
        </div>-->

<br/>
        <!-- 상세정보 -->
        <div>
          <div class="table-header">
            <div class="table-name">
              <h3 class="ico_table_name">품목정보</h3>
            </div>
            <div class="btn-wrap btn-type1 clearfix">
              <el-button type="success" icon="el-icon-document-checked" @click="modifyItem">품목수정</el-button>
            </div>
          </div>


          <div class="grid-tbl-box" style="float: left; width: 65%;">
            <ag-grid-vue
              ref="itemGrid"
              style="height: 150px;"
              class="ag-theme-alpine"
              rowSelection="single"

              :columnDefs="columnDefsItem"
              :defaultColDef="defaultColDefItem"
              :frameworkComponents="frameworkComponent"
              :rowData="itemList"
              :gridOptions="gridOptionsItem"
              :suppressRowClickSelection="true"
              :enableRangeSelection="false"
              @grid-ready="onReadyItem"
            />
          </div>

          <div class="grid-tbl-box" style="float: left; width: 35%; padding-left: 10px;">
            <ag-grid-vue
              ref="slipGrid"
              style="height: 150px;"
              class="ag-theme-alpine"
              rowSelection="single"

              :columnDefs="columnDefsSlip"
              :defaultColDef="defaultColDefSlip"
              :frameworkComponents="frameworkComponent"
              :rowData="slipList"
              :gridOptions="gridOptionsSlip"
              :suppressRowClickSelection="false"
              :enableRangeSelection="false"
              @grid-ready="onReadySlip"
            />
          </div>
        </div>

      </div>
    </div>
    <!-- //테이블 -->

  </div>
</template>

<script>
import {AgGridVue} from 'ag-grid-vue'
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import _ from 'lodash'
import Vendor from "@/components/Vendor_Ag";
import BenCountryPop from '@/components/BenCountryPop.vue';
import DhxCalendar from '@/components/DhxCalendar.vue'
import Cctr from "@/components/Cctr_Ag";
import Emp from "@/components/Emp_Ag";
import TaxInvoiceItemModifyPop from "@/components/TaxInvoiceItemModifyPop";
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer";
import DatepickerCellRenderer from "@/components/agGrid/datepicker-cell-renderer";
import EtaxModify from "@/components/EtaxInvoiceModifyPop";

export default {
  compatConfig: { MODE: 2 },
  name: 'SalesTaxInvoiceLst',
  mixins: [mixin, mixinSlip],
  components: {
    AgGridVue,
    Vendor,
    BenCountryPop,
    DhxCalendar,
    TaxInvoiceItemModifyPop
  },
  data() {
    return {
      title: '매출세금계산서 발행',
      compCds: [],
      empDetail: [],
      invoiceList: [],
      itemList: [],
      slipList: [],
      columnDefs: [],
      columnDefsItem: [],
      columnDefsSlip: [],
      defaultColDef: {},
      defaultColDefItem: {},
      defaultColDefSlip: {},
      gridOptions: {},
      gridOptionsItem:{},
      gridOptionsSlip: {},
      frameworkComponent: {
        select: SelectCellRenderer,
        datePicker: DatepickerCellRenderer
      },
      backgColor: '',
      page:{
        page: 0,
        limit: 100,
        count: 0,
        lastPage: 0,
        pageList: []
      },
      authority: '',
      initPage: false,
      postDt: [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')],
      issueDt: '',
      form: {
        postDtFrom: this.$moment().startOf('month').format('YYYY-MM-DD'),
        postDtTo: this.$moment().format('YYYY-MM-DD'),
        issueDtFrom: '',
        issueDtTo: '',
        integrationVendorNum: '',
        integrationVendorName: '',
        wrtDeptNm: '',
        wrtDeptCd: '',
        wrtId: '',
        wrtNm: '',
        confirmFlag: 'X'
      },
      openDt:'',
      supplyAmt: 0,
      taxAmt: 0,
      totalAmt: 0,
      objectPopup: null
    };
  },
  methods: {
    makeColDef(){
      const that = this
      this.columnDefs = [
        { headerName: '',
          headerCheckboxSelection: true,
          checkboxSelection: true,
          field: 'rn',
          width: 80,
          editable: false
        },
        {headerName: '거래처명', field: 'customerName', width: 160},
        {headerName: '부가세코드', field: 'taxCode', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName: 'GL 일자', field: 'glDate', width: 110, cellStyle:{textAlign: 'center'}},
        {headerName: '부서코드', field: 'createDeptCode', width: 150, hide: true},
        {headerName: '부서명', field: 'createDeptName', width: 160},
        {headerName: '작성자사번', field: 'createEmpNo', width: 110, cellStyle:{textAlign: 'center'}},
        {headerName: '작성자명', field: 'createEmpName', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName: '통화', field: 'currencyCode', width: 100, cellStyle:{textAlign: 'center'}},
        { headerName: '공급가액(원화)',
          field: 'supplyAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName: '부가세액(원화)',
          field: 'taxAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        {headerName: '메세지', field: 'returnDescription', width: 200},
        {headerName: '처리상태 코드', field: 'dtiStatus', width: 150, hide: true},
        {headerName: '처리상태', field: 'dtiStatusText', width: 110, cellStyle:{textAlign: 'center'}},
        {headerName: '국세청전송상태', field: 'sendRequest', width: 150},
        {headerName: '세금계산서 종류 코드', field: 'dtiType', width: 150, hide: true},
        {headerName: '세금계산서 종류', field: 'dtiTypeText', width: 130, cellStyle: {'background-color':'#FFF5F5DC', textAlign: 'center'}},
        {headerName: '매입/매출 구분 코드', field: 'supbuyType', width: 150, hide: true},
        {headerName: '매입/매출 구분', field: 'supbuyTypeText', width: 125, cellStyle: {'background-color':'#FFF5F5DC', textAlign: 'center'}},
        { headerName: '정/역 구분',
          field: 'direction',
          width: 120,
          cellRenderer: 'select',
          cellRendererParams: {
            options : [
              {detailCd: '2', detailNm: '정발행'},
              {detailCd: '1', detailNm: '역발행'}
            ]
          },
          cellStyle: {'background-color':'#FFF5F5DC'},
        },
        { headerName: '영수구분',
          field: 'taxDemand',
          width: 120,
          cellRenderer: 'select',
          cellRendererParams: {
            options : [
              {detailCd: '01', detailNm: '영수'},
              {detailCd: '18', detailNm: '청구'}
            ]
          },
          cellStyle: {'background-color':'#FFF5F5DC', textAlign: 'center'},
        },
        { headerName: '세금계산서 발행일자',
          field: 'dtiWdate',
          width: 160,
          cellRenderer: 'datePicker',
          cellRendererParams: {
            config: {
              hideTime: true,
              format: "YYYYMMDD",
              outputFormat: "YYYYMMDD",
              dateFormat: '%Y%m%d'
            },
          },
          cellStyle: {'background-color':'#FFF5F5DC', textAlign: 'center'},
        },
        {headerName: 'E-mail', field: 'byrEmail', width: 200, editable: true, cellStyle: {'background-color':'#FFF5F5DC'}},
        {headerName: '품목명', field: 'itemName', width: 200, editable: true, cellStyle: {'background-color':'#FFF5F5DC'}},
        {headerName: '승인번호', field: 'approveId', width: 220},
        {headerName: '비고', field: 'remark', width: 200},
        { headerName: '수정코드',
          field: 'amendCode',
          width: 150,
          cellRenderer: 'select',
          cellRendererParams: {
            options : [
              {detailCd: '', detailNm: ''},
              {detailCd: '01', detailNm: '기재사항의 착오-정정'},
              {detailCd: '02', detailNm: '공급가액 변동'},
              {detailCd: '03', detailNm: '환입'},
              {detailCd: '04', detailNm: '계약의 해제'},
              {detailCd: '05', detailNm: '내국신용장사후 개설'},
              {detailCd: '06', detailNm: '착오에 의한 이중발급'}
            ]
          },
          cellStyle: {'background-color':'#FFF5F5DC'},
        },
        {headerName: '원승인번호', field: 'oriIssueId', width: 150, cellStyle: {'background-color':'#FFF5F5DC'}},
        {headerName: '원확정ID', field: 'oriEtaxIssueId', width: 150, cellStyle: {'background-color':'#FFF5F5DC'}},
        {headerName: '원본 발행일자', field: 'oriDtiWdate', width: 150, cellStyle: {'background-color':'#FFF5F5DC'}},
        { headerName: '내국신용장 사후개설일자',
          field: 'localLcOpenDate',
          width: 150,
          cellRenderer: 'datePicker',
          cellRendererParams: {
            config: {
              hideTime: true,
              format: "YYYYMMDD",
              outputFormat: "YYYYMMDD",
              dateFormat: '%Y%m%d'
            },
          },
          cellStyle: {'background-color':'#FFF5F5DC', textAlign: 'center'},
        },
        {headerName: '전자세금계산서ID', field: 'conversationId', width: 270},
        {headerName: '사업자번호', field: 'byrComRegno', width: 120},
        {headerName: '공급자 종사업장번호', field: 'supBizplaceCode', width: 120, cellStyle: {textAlign: 'center'}},
        {headerName: '공급받는자 종사업장번호', field: 'byrBizplaceCode', width: 155, editable: true, cellStyle: {'background-color':'#FFF5F5DC'}},
        { headerName: '합계(원화)',
          field: 'totalAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        {headerName: '공급받는자 번호', field: 'byrTelNum', width: 170},
        {headerName: '공급받는자 대표자성명', field: 'byrRepName', width: 150, cellStyle: {textAlign: 'center'}},
        {headerName: '공급받는자 업태', field: 'byrComType', width: 200},
        {headerName: '공급받는자 업종', field: 'byrComClassify', width: 200},
        {headerName: '공급받는자 주소', field: 'byrComAddr', width: 320},

        {headerName: '확정 ID', field: 'etaxIssueId', width: 150, hide: true},
        {headerName: 'BATCH ID', field: 'interfaceBatchId', width: 150, hide: true},
        {headerName: '거래처No', field: 'integrationVendorNum', width: 150, hide: true},
        {headerName: 'returnCode', field: 'returnCode', width: 50, hide: true},
      ]

      this.columnDefsItem = [
        { headerName: '번호',
          field: 'rn',
          width: 80,
          cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          }
        },
        {headerName: '품목코드', field: 'description', width: 250},
        {headerName: '품목명', field: 'itemName', width: 150},
        {headerName: '구입일자', field: 'itemMd', width: 150},
        { headerName: '수량',
          field: 'itemQty',
          width: 100,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName: '단가',
          field: 'unitPrice',
          width: 100,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName: '공급가액',
          field: 'supplyAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName: '부가세액',
          field: 'taxAmount',
          width: 150,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
      ]


      this.columnDefsSlip = [
        {headerName: '전표번호', field: 'trxNumber', width: 200},
      ]

    },
    getEmpDetail(){
      this.$http.get(`/api/emp/${this.$store.state.loginInfo.loginId}`)
        .then((response) => {
          this.empDetail = response.data
        })
    },
    getCompCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "COMP_CD"}})
        .then(response => {
          this.compCds = response.data;
        });
    },
    getStartDate(){
      this.$store.commit('loading');
      this.$http.get(`/api/acctPeriod/openDate`)
        .then(response => {
          this.openDt = response.data
          this.form.postDtFrom = this.$moment(response.data,'YYYYMMDD').format('YYYY-MM-DD')
          this.form.postDtTo = this.$moment(this.form.postDtFrom).endOf('month').format('YYYY-MM-DD')

        })
        .catch(response => {
        })
        .finally(() => {
          this.$store.commit('finish');
        });
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
    },
    initVendor(force) {
      if (force === true) this.form.integrationVendorName = '';
      if (this.form.integrationVendorName === '') {
        this.form.integrationVendorNum = '';
      }
    },
    popVendor(clear) {
      let vm = this
      this.$modal.open({
        component: Vendor,
        width: '1200px',
        props: {
          param: this.form.integrationVendorName
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.integrationVendorNum = obj.integrationVendorNum;
            vm.form.integrationVendorName = obj.integrationVendorName;
          }
        }
      })
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
    closeModal(){
      document.getElementById("open-moda").style.opacity = "0";
      document.getElementById("open-moda").style.pointerEvents = "none";
    },
    openModal(){
      document.getElementById("open-moda").style.opacity = "1";
      document.getElementById("open-moda").style.pointerEvents = "auto";
    },
    resetSearch(){
      this.initVendor(true)
      this.initCctr(true)
      this.initEmp(true)
      this.form.confirmFlag = 'X'
      this.postDt = [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')];
      this.issueDt = ''
    },
    getCount(){
      /*this.$http.post(`/api/vendor/count`, {
          compCd: this.$store.state.loginInfo.compCd,
          integrationVendorNum: this.form.integrationVendorNum,
          integrationVendorName:this.form.integrationVendorName,
          arFlag: null,
          apFlag: null,
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
      })*/
    },
    compareDate(value){
      if(!value){
        this.$swal({ type: 'info', text: '회계일자를 입력해주세요.' });
        return false;
      }
    },
    goSearch() {

      this.compareDate(this.postDt);
      this.form.postDtFrom = this.$moment(this.postDt[0]).format("YYYYMMDD");
      this.form.postDtTo = this.$moment(this.postDt[1]).format("YYYYMMDD");

      if (this.issueDt) {
        this.form.issueDtFrom = this.$moment(this.issueDt[0]).format("YYYYMMDD");
        this.form.issueDtTo = this.$moment(this.issueDt[1]).format("YYYYMMDD");
      }

      if(this.initPage){
        this.initPage = false
        this.page.page = 0
      }
      this.closeModal()
      //this.getCount()
      this.$store.commit('loading');
      this.$http.post(`/api/salesTax/issueList`, {
          orgId: Number(this.$store.state.loginInfo.compCd),
          postDtFrom: this.form.postDtFrom.replaceAll('-', ''),
          postDtTo: this.form.postDtTo.replaceAll('-', ''),
          integrationVendorNum: this.form.integrationVendorNum,
          dtiStatus: this.form.confirmFlag,
          issueDtFrom: this.form.issueDtFrom.replaceAll('-', ''),
          issueDtTo: this.form.issueDtTo.replaceAll('-', ''),
          wrtDeptCd: this.form.wrtDeptCd,
          wrtId: this.form.wrtId,
          empNo: this.$store.state.loginInfo.loginId
        }
      ).then(response => {
        this.invoiceList = response.data
        this.itemList = []
        this.slipList = []
      }).finally(() => {
        //this.gridOptions.api.refreshCells()
        this.$store.commit('finish')
      });

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
    goPage(param){
      this.page.page = param - 1
      this.goSearch()
    },
    onReady(params) {
      //메인그리드 api 정의
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    onReadyItem(params) {
      //라인그리드 api 정의
      this.gridApiItem = this.gridOptionsItem.api;
      this.columnApiItem = this.gridOptionsItem.columnApi;

      //화면 width에 맞게 컬럼 size 조정
      this.gridApiItem.sizeColumnsToFit();
    },
    onReadySlip(params) {
      //라인그리드 api 정의
      this.gridApiSlip = this.gridOptionsSlip.api;
      this.columnApiSlip = this.gridOptionsSlip.columnApi;

      //화면 width에 맞게 컬럼 size 조정
      this.gridApiSlip.sizeColumnsToFit();
    },
    onSelectionChanged(){
      const Row = this.gridApi.getSelectedRows();

      if(Row.length == 1){
        this.$store.commit('loading');
        this.$http.post(`/api/salesTax/getItem`, Row[0].etaxIssueId
        ).then(response => {
          this.itemList = response.data;
        }).finally(() => {
          this.$store.commit('finish')
        });

        this.$store.commit('loading');
        this.$http.post(`/api/salesTax/getSlipNo`, Row[0].etaxIssueId
        ).then(response => {
          this.slipList = response.data;
        }).finally(() => {
          this.$store.commit('finish')
        });
      }
    },
    onCellClicked(params){
      const that = this
      var field = params.colDef.field;

      if(field == 'oriIssueId' || field == 'oriEtaxIssueId' || field == 'oriDtiWdate'){
        if(!params.data.amendCode){
          this.$swal({ type: 'warning', text: '수정코드를 먼저 선택하세요.' });
          return
        }
        if(params.data.conversationId){
          return
        }

        //modal 원본 세금계산서 선택 모달
        this.$modal.open({
          component: EtaxModify,
          parent: this,
          props: {
            amendCode: params.data.amendCode,
            orgId: params.data.orgId,
            integrationVendorNum: params.data.integrationVendorNum,
            currencyCode: params.data.currencyCode,
            taxCode: params.data.taxCode,
          },
          width: 1200,
          events: {
            close(obj) {
              params.data.oriIssueId = obj.approveId
              params.data.oriEtaxIssueId = obj.etaxIssueId
              params.data.oriDtiWdate = obj.dtiWdate

              that.gridOptions.api.refreshCells()
            }
          }
        });


      }
    },
    cellValueChange(params){
      const rowIdx = params.rowIndex;

      if(params.value == ''){
        this.invoiceList[rowIdx].oriIssueId = ''
        this.invoiceList[rowIdx].oriEtaxIssueId = ''
        this.invoiceList[rowIdx].oriDtiWdate = ''
        this.invoiceList[rowIdx].localLcOpenDate = ''

        this.gridOptions.api.refreshCells()
      }
    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      //this.invoiceList[rowIdx].rn = params.node.isSelected();
    },

    etaxIssue(){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }

      //validation check
      for(let i=0; i<Rows.length; i++){
        if(isNaN(Rows[i].byrBizplaceCode)){
          this.$swal({ type: 'warning', text: '공급받는자의 종사업장번호는 숫자만 입력하세요.' });
          return
        }
        if(!Rows[i].dtiType){
          this.$swal({ type: 'warning', text: '세금계산서 종류를 선택하세요.' });
          return
        }
        if(!Rows[i].supbuyType){
          this.$swal({ type: 'warning', text: '매출/매입구분을 선택하세요.' });
          return
        }
        if(!Rows[i].direction){
          this.$swal({ type: 'warning', text: '정/역 구분을 선택하세요.' });
          return
        }
        if(!Rows[i].taxDemand){
          this.$swal({ type: 'warning', text: '영수구분이 없습니다.' });
          return
        }
        if(!Rows[i].dtiWdate){
          this.$swal({ type: 'warning', text: '세금계산서 발행일자를 입력하세요.' });
          return
        }
        if((Rows[i].dtiWdate * 1) > (this.$moment().format('YYYYMMDD') * 1)){
          this.$swal({ type: 'warning', text: '세금계산서 발행일자는 미래 날짜를 입력 할 수 없습니다.' });
          return
        }
        if(!Rows[i].byrEmail){
          this.$swal({ type: 'warning', text: '이메일주소를 입력해주세요.' });
          return
        }
        if(!Rows[i].itemName){
          this.$swal({ type: 'warning', text: '품목명을 입력하세요.' });
          return
        }
        if(!Rows[i].byrComRegno){
          this.$swal({ type: 'warning', text: '사업자 번호가 없습니다.' });
          return
        }
        if(!Rows[i].byrRepName){
          this.$swal({ type: 'warning', text: '공급받는자 대표자 성명이 없습니다.' });
          return
        }
        if(Rows[i].conversationId || Rows[i].dtiStatus){
          this.$swal({ type: 'warning', text: '매출발행 혹은 역발행 처리된 행을 선택하였습니다.' });
          return
        }
        if(Rows[i].amendCode){
          this.$swal({ type: 'warning', html: '수정세금계산서 관련 데이터가 있습니다.<br/>수정세금계산서 발행은 수정세금계산서발행 버튼을 눌러주세요.' });
          return
        }


        Rows[i].createdPersonId = this.empDetail.personId
      }

      this.$swal({
        type: 'question',
        text: '세금계산서 발행 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/issue`, Rows)
            .then(response => {
              this.$swal({ type: 'success', text: response.data.Message });
            })
            .catch(response => {

            })
            .finally(() => {
              this.goSearch()
              this.$store.commit('finish')
            })
        }
      })

    },
    etaxModify(){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }

      //validation Check
      for(var i=0; i<Rows.length; i++){
        if(!Rows[i].dtiType){
          this.$swal({ type: 'warning', text: '세금계산서 종류를 선택하세요.' });
          return
        }
        if(!Rows[i].supbuyType){
          this.$swal({ type: 'warning', text: '매출/매입구분을 선택하세요.' });
          return
        }
        if(!Rows[i].direction){
          this.$swal({ type: 'warning', text: '정/역 구분을 선택하세요.' });
          return
        }
        if(!Rows[i].taxDemand){
          this.$swal({ type: 'warning', text: '영수구분이 없습니다.' });
          return
        }
        if(!Rows[i].dtiWdate){
          this.$swal({ type: 'warning', text: '세금계산서 발행일자를 입력하세요.' });
          return
        }
        if((Rows[i].dtiWdate * 1) > (this.$moment().format('YYYYMMDD') * 1)){
          this.$swal({ type: 'warning', text: '세금계산서 발행일자는 미래 날짜를 입력 할 수 없습니다.' });
          return
        }
        if(!Rows[i].byrEmail){
          this.$swal({ type: 'warning', text: '이메일주소를 입력해주세요.' });
          return
        }
        if(!Rows[i].itemName){
          this.$swal({ type: 'warning', text: '품목명을 입력하세요.' });
          return
        }
        if(!Rows[i].byrComRegno){
          this.$swal({ type: 'warning', text: '사업자 번호가 없습니다.' });
          return
        }
        if(!Rows[i].byrRepName){
          this.$swal({ type: 'warning', text: '공급받는자 대표자 성명이 없습니다.' });
          return
        }
        if(Rows[i].conversationId || Rows[i].dtiStatus){
          this.$swal({ type: 'warning', text: '매출발행 혹은 역발행 처리된 행을 선택하였습니다.' });
          return
        }
        if(!Rows[i].amendCode){
          this.$swal({ type: 'warning', text: '수정코드를 선택하세요.' });
          return
        }
        if(!Rows[i].oriIssueId || !Rows[i].oriEtaxIssueId || !Rows[i].oriDtiWdate){
          this.$swal({ type: 'warning', text: '원본 세금계산서 정보가 없습니다.' });
          return
        }
        if(Rows[i].amendCode == '05' && !Rows[i].localLcOpenDate){
          this.$swal({ type: 'warning', text: '내국신용장 사후개설일자가 없습니다.' });
          return
        }

        Rows[i].createdPersonId = this.empDetail.personId
      }

      this.$swal({
        type: 'question',
        text: '수정세금계산서를 발행 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/etaxModify`, Rows)
            .then(response => {
              this.$swal({ type: 'success', text: response.data.Message });
            })
            .catch(response => {

            })
            .finally(() => {
              this.goSearch()
              this.$store.commit('finish')
            })
        }
      })

    },
    etaxReIssue(){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }

      for(var i=0; i<Rows.length; i++){
        if(!(Rows[i].dtiStatus =="S" && (Rows[i].returnCode == "37010" || Rows[i].returnCode == "31079" || Rows[i].returnCode == "" || Rows[i].returnCode == null))){
          this.$swal({ type: 'warning', text: '발행재시도 상태가 아닙니다.' });
          return
        }

        Rows[i].createdPersonId = this.empDetail.personId
      }

      this.$swal({
        type: 'question',
        text: '재발행 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/reIssue`, Rows)
            .then(response => {
              this.$swal({ type: 'success', text: response.data.Message });
            })
            .catch(response => {

            })
            .finally(() => {
              this.goSearch()
              this.$store.commit('finish')
            })
        }
      })

    },
    reverseSave(){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }

      //validation check
      for(var i=0; i<Rows.length; i++){
        if(!Rows[i].dtiType){
          this.$swal({ type: 'warning', text: '세금계산서 종류를 선택하세요.' });
          return
        }
        if(!Rows[i].supbuyType){
          this.$swal({ type: 'warning', text: '매출/매입구분을 선택하세요.' });
          return
        }
        if(!Rows[i].direction){
          this.$swal({ type: 'warning', text: '정/역 구분을 선택하세요.' });
          return
        }
        if(!Rows[i].taxDemand){
          this.$swal({ type: 'warning', text: '영수구분이 없습니다.' });
          return
        }
        if(!Rows[i].dtiWdate){
          this.$swal({ type: 'warning', text: '세금계산서 발행일자를 입력하세요.' });
          return
        }
        if((Rows[i].dtiWdate * 1) > (this.$moment().format('YYYYMMDD') * 1)){
          this.$swal({ type: 'warning', text: '세금계산서 발행일자는 미래 날짜를 입력 할 수 없습니다.' });
          return
        }
        if(!Rows[i].itemName){
          this.$swal({ type: 'warning', text: '품목명을 입력하세요.' });
          return
        }
        if(!Rows[i].byrComRegno){
          this.$swal({ type: 'warning', text: '사업자 번호가 없습니다.' });
          return
        }
        if(!Rows[i].byrRepName){
          this.$swal({ type: 'warning', text: '공급받는자 대표자 성명이 없습니다.' });
          return
        }
        if(Rows[i].conversationId || Rows[i].dtiStatus){
          this.$swal({ type: 'warning', text: '매출발행 혹은 역발행 처리된 행을 선택하였습니다.' });
          return
        }
        if(Rows[i].direction != '1'){
          this.$swal({ type: 'warning', text: '역발행만 선택하세요.' });
          return
        }

        Rows[i].createdPersonId = this.empDetail.personId
      }

      this.$swal({
        type: 'question',
        text: '역발행 저장 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/reverseSave`, Rows)
            .then(response => {
              if(response.data.Message){
                this.$swal({ type: 'warning', text: response.data.Message });
              }else{
                this.$swal({ type: 'success', text: "역발행 저장 되었습니다." });
              }

            })
            .catch(response => {

            })
            .finally(() => {
              this.goSearch()
              this.$store.commit('finish')
            })
        }
      })


    },
    reverseCancel(){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }

      //validation check
      for(var i=0; i<Rows.length; i++){
        if(Rows[i].direction != '1') {
          this.$swal({ type: 'warning', text: '역발행 저장된 건을 선택하시기 바랍니다.' });
          return
        }

      }

      this.$swal({
        type: 'question',
        text: '역발행 취소 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/reverseCancel`, Rows)
            .then(response => {
              if(response.data.Message){
                this.$swal({ type: 'warning', text: response.data.Message });
              }else{
                this.$swal({ type: 'success', text: "역발행 취소 되었습니다." });
              }
            })
            .catch(response => {

            })
            .finally(() => {
              this.goSearch()
              this.$store.commit('finish')
            })
        }
      })


    },
    cancelIssue(){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }

      for(var i=0; i<Rows.length; i++){
        if(Rows[i].dtiStatus != 'I'){
          this.$swal({ type: 'question', text: '발행 취소 할 수 있는 상태가 아닙니다.' });
          return
        }

        Rows[i].createdPersonId = this.empDetail.personId
      }

      this.$swal({
        type: 'question',
        text: '발행 취소 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/cancelIssue`, Rows)
            .then(response => {
              if(response.data.Message){
                this.$swal({ type: 'warning', text: response.data.Message });
              }else{
                this.$swal({ type: 'success', text: "발행 취소 되었습니다." });
              }
            })
            .catch(response => {

            })
            .finally(() => {
              this.goSearch()
              this.$store.commit('finish')
            })
        }
      })
    },
    emailReSend(){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }

      for(var i=0; i<Rows.length; i++){
        if(!Rows[i].byrEmail){
          this.$swal({ type: 'question', text: 'E-Mail 주소가 올바르지 않습니다.' });
          return
        }

        Rows[i].createdPersonId = this.empDetail.personId
      }

      this.$swal({
        type: 'question',
        text: 'Email 재전송 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/emailReSend`, Rows)
            .then(response => {
              if(response.data.Message){
                this.$swal({ type: 'warning', text: response.data.Message });
              }else{
                this.$swal({ type: 'success', text: "Email 재전송 되었습니다." });
              }
            })
            .catch(response => {

            })
            .finally(() => {
              this.goSearch()
              this.$store.commit('finish')
            })
        }
      })
    },
    preview(method){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length != 1){
        this.$swal({ type: 'warning', text: '하나의 행만 선택해 주세요.' });
        return
      }

      if(Rows[0].direction != '2'){
        this.$swal({ type: 'warning', text: '정발행만 선택 가능합니다.' });
        return
      }

      Rows[0].createdPersonId = this.empDetail.personId

      this.$store.commit('loading')
      this.$http.post(`/api/salesTax/preview`, Rows[0])
        .then(response => {
          this.$store.commit('finish')

          let url = 'http://197.200.1.19:10002/callSB_V3/XXSB_DTI_PRINT'
                    + (method == 'SUP' ? '.asp' : '_RVS.asp')
                    + '?BATCH_ID=' + response.data.newInterfaceId
                    + '&SORTFIELD=A&SORTORDER=1';

          if(!(!this.objectPopup || this.objectPopup.closed)){
            this.objectPopup.focus();
          }else{
            this.objectPopup = window.open(url, '_blank', 'toolbar=0,location=0,menubar=0,resizable=yes');
          }
        })
        .catch(response => {

        })

    },
    modifyItem(){
      const that = this
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }else if(Rows.length > 1){
        this.$swal({ type: 'warning', text: '여러행이 선택되었습니다.' });
        return
      }

      /*if(Rows[0].conversationId || Rows[0].dtiStatus){
        this.$swal({ type: 'warning', text: '매출발행 혹은 역발행 처리된 행을 선택하였습니다.' });
        return
      }*/

      this.$modal.open({
        component: TaxInvoiceItemModifyPop,
        props: {
          invoiceData: Rows[0],
          itemData: this.itemList[0],
          personId: this.empDetail.personId
        },
        parent: this,
        width: 1200,
        events: {
          close() {
            that.goSearch()
          }
        }
      })

    },

  },
  watch: {
    'form': {
      immediate: false,
      deep: true,
      handler() {
        this.initPage = true
      }
    },
  },
  created() {
    this.getStartDate();
    this.resetSearch()
  },
  mounted() {
    document.title = this.title + ' - IJEAS';
    this.authority = this.$store.state.loginInfo.authorities[0].roleCd;
    //this.getCompCdCombo();
    this.getEmpDetail();
  },
  beforeMount() {
    this.makeColDef();
    this.defaultColDef = { resizable: true, filter:false, sortable: false };
    const statusBar = {
      statusPanels: [
        { statusPanel: 'agTotalAndFilteredRowCountComponent', align: 'left' },
      ],
    }
    this.gridOptions = {enableRangeSelection: true, statusBar}
    this.gridOptionsItem = {enableRangeSelection: true, statusBar}
    this.gridOptionsSlip = {enableRangeSelection: true, statusBar}
  },
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
}

.desc-content .item .desc-item:last-child {
  margin-bottom: 0;
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

@media (max-width: 1580px) {
  .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
    width: 50%;
  }
}


</style>

