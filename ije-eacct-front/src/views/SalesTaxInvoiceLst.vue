<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goFirst">조회</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="search_box" style="width: 35%">
        <div class="search_title">
          <span class="search_tit" style="color: #CC3D3D;">- GL일자</span>
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
                  <span class="search_tit">- GL일자</span>
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
                  <span class="search_tit">- 확정여부</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth" v-model="form.confirmFlag">
                    <option value="">전체</option>
                    <option value="Y">확정</option>
                    <option value="N">미확정</option>
                  </select>
                </div>
              </div>


              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 부서</span>
                </div>
                <div class="search_con search-area">
                    <input class="input dp-ib input-bg w40p_5r" type="text" v-model="form.wrtDeptCd" disabled>
                    <div class="dp-ib w60p">
                      <input class="input" type="text" v-model="form.wrtDeptNm" @input="initCctr" @keypress.enter="popCctr">
                      <button class="icon is-right" @click="popCctr"><i class="fas fa-search"></i>
                      </button>
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

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- Accounting여부</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth" v-model="form.accountingFlag">
                    <option value="">전체</option>
                    <option value="Y">Y</option>
                    <option value="N">N</option>
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
          <el-form>
            <el-row class="btn-type1 clearfix">
              <div style="float:left; margin-right:10px;"> 선택 공급가액 : {{supplyAmt | amt}} </div>
              <div style="float:left; margin-right:10px;"> 부가세액 : {{taxAmt | amt}} </div>
              <div style="float:left; margin-right:10px;"> 합계 : {{totalAmt | amt}} </div>
              <el-button type="success" icon="el-icon-check" @click="confirm('SUM')">월합확정</el-button>
              <el-button type="success" icon="el-icon-check" @click="confirm('ONE')">개별확정</el-button>
              <el-button type="danger" icon="el-icon-minus" @click="confirm('NOT')">발행대상자제외</el-button>
              <el-button type="warning" icon="el-icon-document-checked" @click="modiAmt">금액수정</el-button>
              <el-button type="warning" icon="el-icon-document-checked" @click="accounting">채권조정</el-button>
              <el-button type="danger" icon="el-icon-close" @click="cancel">제외/확정 취소</el-button>
            </el-row>
          </el-form>
        </div>


        <div class="grid-tbl-box">

          <ag-grid-vue
            ref="grid"
            style="width: 100%; height: 450px;"
            class="ag-theme-alpine"
            rowSelection="multiple"

            :columnDefs="columnDefs"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponent"
            :rowData="slipList"
            :gridOptions="gridOptions"
            :suppressRowClickSelection="true"
            :enableRangeSelection="false"
            :getRowStyle="getRowStyle"
            @grid-ready="onReady"
            @row-selected="onRowSelected"
            @selection-changed="onSelectionChanged"
            @cell-clicked="onCellClicked"
          />
        </div>

        <!-- :statusBar="statusBar" -->

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


        <!-- 상세정보 -->
        <div class="mt20">
          <div class="table-header">
            <div class="table-name">
              <h3 class="ico_table_name">지급정보</h3>
            </div>
          </div>

          <div class="grid-tbl-box">
            <ag-grid-vue
              ref="lineGrid"
              style="width: 100%; height: 150px;"
              class="ag-theme-alpine"
              rowSelection="multiple"

              :columnDefs="columnDefsLine"
              :defaultColDef="defaultColDefLine"
              :frameworkComponents="frameworkComponent"
              :rowData="lineList"
              :gridOptions="gridOptionsLine"
              :suppressRowClickSelection="true"
              :enableRangeSelection="false"
              @grid-ready="onReadyLine"
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
import TaxInvoiceAmtModifyPop from "@/components/TaxInvoiceAmtModifyPop";

export default {
  name: 'SalesTaxInvoiceLst',
  mixins: [mixin, mixinSlip],
  components: {
    AgGridVue,
    Vendor,
    BenCountryPop,
    DhxCalendar,
    TaxInvoiceAmtModifyPop
  },
  data() {
    return {
      title: '매출전표 조회 및 확정',
      compCds: [],
      empDetail: [],
      slipList: [],
      lineList: [],
      columnDefs: [],
      columnDefsLine: [],
      defaultColDef: {},
      defaultColDefLine: {},
      gridOptions: {},
      gridOptionsLine:{},
      frameworkComponent: {},
      getRowStyle: '',
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
      form: {
        postDtFrom: this.$moment().startOf('month').format('YYYY-MM-DD'),
        postDtTo: this.$moment().format('YYYY-MM-DD'),
        integrationVendorNum: '',
        integrationVendorName: '',
        wrtDeptNm: '',
        wrtDeptCd: '',
        wrtId: '',
        wrtNm: '',
        accountingFlag: '',
        confirmFlag: ''
      },
      openDt:'',
      supplyAmt: 0,
      taxAmt: 0,
      totalAmt: 0,
      statusBar: null
    };
  },
  methods: {
    makeColDef(){
      const that = this
      this.columnDefs = [
        {headerName: 'back', field: 'back', width: 150, hide: true},
        { headerName: '',
          headerCheckboxSelection: true,
          checkboxSelection: true,
          field: 'rn',
          width: 70,
          editable: false
        },
        {headerName: '거래처명', field: 'customerName', width: 160},
        {headerName: '사업자번호', field: 'taxReference', width: 130},
        {headerName: '전표번호', field: 'trxNumber', width: 180},
        {headerName: '세금코드', field: 'taxCode', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName: '사업장', field: 'taxLocation', width: 110},
        {headerName: 'GL 일자', field: 'glDate', width: 110},
        {headerName: '부서코드', field: 'createDeptCode', width: 150, hide: true},
        {headerName: '부서명', field: 'createDeptName', width: 160},
        {headerName: '작성자사번', field: 'createEmpNo', width: 110, cellStyle:{textAlign: 'center'}},
        {headerName: '작성자명', field: 'createEmpName', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName: '통화', field: 'currencyCode', width: 80},
        { headerName: '공급가액(원화)',
          field: 'supplyAmount',
          width: 130,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName: '부가세액(원화)',
          field: 'taxAmount',
          width: 130,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName: '합계(원화)',
          field: 'totalAmount',
          width: 130,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        {headerName: '세무증빙유형', field: 'taxEvidenceType', width: 140, hide: true},
        {headerName: '세무증빙유형', field: 'taxEvidenceTypeName', width: 180},
        {headerName: '처리상태', field: 'dtiStatusText', width: 110, cellStyle:{textAlign: 'center'}},
        {headerName: '확정여부', field: 'etaxIssueIdYn', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName: '확정여부', field: 'etaxIssueId', width: 150, hide: true},
        {headerName: '적요', field: 'comments', width: 160},
        {headerName: '신고 단위', field: 'orgId', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName: 'Accounting Flag', field: 'completeFlag', width: 120, cellStyle:{textAlign: 'center'}},
        {headerName: '발행제외여부', field: 'etaxExcludeFlag', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName: '통합거래처번호', field: 'integrationVendorNum', width: 150, cellStyle:{textAlign: 'center'}},
        {headerName: '공급받는자 대표자명', field: 'representativeName', width: 110, cellStyle:{textAlign: 'center'}},
        {headerName: '공급받는자 업태', field: 'custBusinessCondition', width: 150},
        {headerName: '공급받는자 업종', field: 'custBusinessType', width: 150},
        {headerName: '공급받는자 주소', field: 'customerAddress', width: 400},
        {headerName: 'trxId', field: 'customerTrxId', width: 150, hide:true},
        {headerName: 'dtiStatus', field: 'dtiStatus', width: 150, hide:true},
        {headerName: 'direction', field: 'direction', width: 150, hide:true},
        {headerName: 'etaxIssueIdCnt', field: 'etaxIssueIdCnt', width: 150, hide:true},

      ]

      this.columnDefsLine = [
        { headerName: 'Line No',
          field: 'rn',
          width: 130,
          cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          }
        },
        {headerName: '적요', field: 'description', width: 250},
        {headerName: '부가세코드', field: 'taxClassificationCode', width: 150},
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
        { headerName: '수량',
          field: 'quantityInvoiced',
          width: 100,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        { headerName: '단가',
          field: 'unitSellingPrice',
          width: 100,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        {headerName: 'UOM', field: 'uomCode', width: 100, cellStyle:{textAlign: 'center'}},
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
        props: {
          param: this.form.integrationVendorName
        },
        parent: this,
        width: '1200px',
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
      this.form.confirmFlag = ''
      this.form.accountingFlag = ''
      this.postDt = [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')];
    },
    getCount(){
      this.$http.post(`/api/vendor/count`, {
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
      })

    },
    compareDate(value){
      if(!value){
        this.$swal({ type: 'info', text: 'GL일자를 입력해주세요.' });
        return false;
      }
    },
    goSearch(init) {

      this.compareDate(this.postDt);
      this.form.postDtFrom = this.$moment(this.postDt[0]).format("YYYYMMDD");
      this.form.postDtTo = this.$moment(this.postDt[1]).format("YYYYMMDD");

      if(this.initPage){
        this.initPage = false
        this.page.page = 0
      }

      if(init == 'init'){
        this.form.postDtFrom = this.$moment('29990101').format("YYYYMMDD");
        this.form.postDtTo = this.$moment('29990101').format("YYYYMMDD");
      }else{
        this.closeModal()
      }

      //this.getCount()
      this.$store.commit('loading');
      this.$http.post(`/api/salesTax/list`, {
          orgId: Number(this.$store.state.loginInfo.compCd),
          fromDate: this.form.postDtFrom,
          toDate: this.form.postDtTo,
          etaxIssueIdYn: this.form.confirmFlag,
          completeFlag: this.form.accountingFlag,
          integrationVendorNum: this.form.integrationVendorNum,
          createDeptCode: this.form.wrtDeptCd,
          createEmpNo: this.form.wrtId,
        }
      ).then(response => {
        this.slipList = response.data

        var togle = true
        var beforeEtaxIssueId = ''

        for(var i=0; i<this.slipList.length; i++){
          if(this.slipList[i].etaxIssueId != beforeEtaxIssueId){
            togle = !togle
          }
          this.slipList[i].back = togle
          beforeEtaxIssueId = this.slipList[i].etaxIssueId
        }

        let promise = new Promise(async (resolve, reject) => {
          this.getRowStyle  = params => {
            if (params.data.etaxIssueId) {
              if(params.data.back){
                return { backgroundColor: '#E0D016'}
              }else{
                return { backgroundColor: '#00ccbf'}
              }
            }
          }
          resolve()
        })

        promise.then(() => {
          this.gridOptions.api.refreshCells()
        }).catch(() => {
          console.log("bg error")
        })

      }).finally(() => {
        this.gridOptions.api.refreshCells()
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

      //화면 width에 맞게 컬럼 size 조정
      //this.gridApi.sizeColumnsToFit();
    },
    onReadyLine(params) {
      //라인그리드 api 정의
      this.gridApiLine = this.gridOptionsLine.api;
      this.columnApiLine = this.gridOptionsLine.columnApi;

      //화면 width에 맞게 컬럼 size 조정
      this.gridApiLine.sizeColumnsToFit();
    },
    onSelectionChanged(){
      const Row = this.gridApi.getSelectedRows();

      this.supplyAmt = 0
      this.taxAmt = 0
      this.totalAmt = 0

      //합계 금액 계산
      for(var i=0; i<Row.length; i++){
        this.supplyAmt += Row[i].supplyAmount
        this.taxAmt += Row[i].taxAmount
        this.totalAmt += Row[i].totalAmount
      }
    },
    onCellClicked(params){
      if(params.data.customerTrxId){
        this.$store.commit('loading');
        this.$http.post(`/api/salesTax/getLine`, params.data.customerTrxId
        ).then(response => {
          this.lineList = response.data;
        }).finally(() => {
          this.$store.commit('finish')
        });
      }
    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      //this.slipList[rowIdx].rn = params.node.isSelected();
    },
    confirm(method){
      const Rows = this.gridApi.getSelectedRows();

      var insertData = []

      var beforeVendorNum = ''
      var beforeCurrency = ''
      var beforTaxCode = ''

      //validation check
      for(var i=0; i<Rows.length; i++){
        if(Rows[i].etaxIssueIdYn == 'Y'){
          this.$swal({ type: 'warning', text: '이미 확정된 건이 선택되었습니다.' });
          return
        }

        if(!Rows[i].taxEvidenceType){
          this.$swal({ type: 'warning', text: '세무증빙유형이 없습니다.' });
          return
        }

        if(method == 'SUM') {
          //1번째 건
          if (beforeVendorNum === '' && beforeCurrency === '' && beforTaxCode === '') {
            beforeVendorNum = Rows[i].integrationVendorNum
            beforeCurrency = Rows[i].currencyCode
            beforTaxCode = Rows[i].taxCode
          } else {
            if (Rows[i].integrationVendorNum != beforeVendorNum || Rows[i].currencyCode != beforeCurrency || Rows[i].taxCode != beforTaxCode) {
              this.$swal({type: 'warning', text: '동일한 거래처, 통화, 세금코드만 월합확정 할 수 있습니다.'})
              return
            } else {
              beforeVendorNum = Rows[i].integrationVendorNum
              beforeCurrency = Rows[i].currencyCode
              beforTaxCode = Rows[i].taxCode
            }
          }
        }

        insertData.push({
          customerTrxId: Rows[i].customerTrxId,
          createdPersonId: this.$store.state.loginInfo.loginId,
          taxEvidenceType: Rows[i].taxEvidenceType,
          deptCd: this.empDetail.deptCd
        })
      }

      const supplySum  = Rows.reduce( (prev, next) => {
        return prev + next.supplyAmount;
      }, 0);

      const taxSum  = Rows.reduce( (prev, next) => {
        return prev + next.taxAmount;
      }, 0);

      const totalSum  = Rows.reduce( (prev, next) => {
        return prev + next.totalAmount;
      }, 0);

      if(method == 'SUM' && supplySum == 0 && taxSum == 0 && totalSum == 0){
        this.$swal({ type: 'warning', text: '합계가 0이면 확정할 수 없습니다.' })
        return
      }



      if(method == 'SUM'){
        this.$swal({
          type: 'question',
          text: '월합 확정 하시겠습니까?',
          showCancelButton: true
        }).then(response => {
          if (response.value) {
            //월합 확정
            this.$store.commit('loading')
            this.$http.post(`/api/salesTax/confirm/sum`, insertData)
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
      }else if(method == 'ONE'){
        this.$swal({
          type: 'question',
          text: '개별 확정 하시겠습니까?',
          showCancelButton: true
        }).then(response => {
          if (response.value) {
            //개별 확정
            this.$store.commit('loading')
            this.$http.post(`/api/salesTax/confirm/one`, insertData)
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
      }else if(method == 'NOT'){
        this.$swal({
          type: 'question',
          text: '발행대상 제외 하시겠습니까?',
          showCancelButton: true
        }).then(response => {
          if (response.value) {
            //발생대상 제외
            this.$store.commit('loading')
            this.$http.post(`/api/salesTax/confirm/not`, insertData)
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
      }

    },
    modiAmt(){
      const that = this
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }else if(Rows.length > 1){
        this.$swal({ type: 'warning', text: '여러행이 선택되었습니다.' });
        return
      }

      if(Rows[0].etaxIssueIdYn == 'Y'){
        this.$swal({ type: 'warning', text: '이미 확정된 건이 선택되었습니다.' });
        return
      }

      if(Rows[0].completeFlag == 'Y'){
        this.$swal({ type: 'warning', text: '금액수정을 할 수 없는 상태입니다.' });
        return
      }

      // 모달
      this.$modal.open({
        component: TaxInvoiceAmtModifyPop,
        props: {
          invoiceData: Rows[0]
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
    accounting(){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }else if(Rows.length > 1){
        this.$swal({ type: 'warning', text: '여러행이 선택되었습니다.' });
        return
      }

      if(Rows[0].etaxIssueIdYn == 'Y'){
        this.$swal({ type: 'warning', text: '이미 확정된 건이 선택되었습니다.' });
        return
      }

      if(Rows[0].completeFlag == 'Y'){
        this.$swal({ type: 'warning', text: '채권조정을 할 수 없는 상태입니다.' });
        return
      }

      this.$swal({
        type: 'question',
        text: '채권조정을 위해 Accounting 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading')
          this.$http.get(`/api/salesTax/accounting/${Rows[0].customerTrxId}`)
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
    cancel(){
      const Rows = this.gridApi.getSelectedRows();

      if(Rows.length == 0){
        this.$swal({ type: 'warning', text: '선택된 행이 없습니다.' });
        return
      }

      for(var i=0; i<Rows.length; i++){
        if(!(Rows[i].dtiStatus == 'O' || Rows[i].dtiStatus == '' || Rows[i].dtiStatus == 'S' || Rows[i].dtiStatus == 'R' || Rows[i].dtiStatus == null)){
          this.$swal({ type: 'question', text: '확정 취소 할수 있는 상태가 아닙니다. 처리상태가 공백이거나 취소완료, 수신거부 건만 가능합니다.' });
          return
        }


        if(Rows[i].etaxIssueIdYn != 'Y'){
          this.$swal({ type: 'warning', text: '선택하신 데이터 중 확정이 안된 건이 있습니다.' });
          return
        }

        if(Rows[i].completeFlag == 'Y' && Rows[i].direction == '1'){
          this.$swal({ type: 'warning', text: '선택하신 데이터 중 역발행 취소를 하지 않은 건이 있습니다.' });
          return
        }


        if(Rows[i].etaxIssueIdCnt > 1) {
          this.$swal({
            type: 'warning',
            html: '선택하신 데이터 중 월합확정 건이 있습니다. <br/>합쳐진 모든 건들은 확정이 해제됩니다. <br/>확정해제 하시겠습니까?',
            showCancelButton: true})
            .then(response => {
              if (!response.value) {
                return
              }
            })
        }
      }

      this.$swal({
        type: 'question',
        text: '확정 취소 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/cancel`, Rows)
            .then(response => {
              if(response.data.Message){
                this.$swal({ type: 'warning', text: response.data.Message });
              }else{
                this.$swal({ type: 'success', text: "확정 취소 되었습니다." });
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


    }

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
    this.goSearch('init')
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
        //{ statusPanel: 'sumStatusBarComponent'},
      ],
    }
    this.gridOptions = {enableRangeSelection: true, statusBar}
    this.gridOptionsLine = {enableRangeSelection: true, statusBar}

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

