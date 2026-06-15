<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib"> {{ title }}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="search">조회</el-button>
        <!-- <button type="button" class="btn-size btn-gray fl_left" @click="search()">
          <span class="btn-icon"><i class="fas fa-search"></i></span>
          조회
        </button> -->
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
        <label class="control-label-req" id="standardMonth">REF NO.</label>
        <div class="form-input">
          <div class="search_con search-area">
              <div class="dp-ib w100p">
                <input class="input" type="text" v-model="form.refNo" @input="initRefNo" @keypress.enter="popRefNo()">
                <button class="icon is-right" @click="popRefNo()"><i class="fas fa-search"></i>
                </button>
              </div>
            </div>
<!--          <div class="datepicker w-type-ymd">-->
<!--            <dhx-calendar class="input" v-model="form.searchFrom" :config="config"/>-->
<!--          </div>-->
<!--          <span> ~ </span>-->
<!--          <div class="datepicker w-type-ymd">-->
<!--            <dhx-calendar class="input" v-model="form.searchTo" :config="config"/>-->
<!--          </div>-->
          <!-- <el-date-picker
              v-model="searchDt"
              type="daterange"
              align="right"
              unlink-panels
              style="width: 260px;"
              range-separator="~"
              start-placeholder="시작일"
              end-placeholder="종료일">
          </el-date-picker> -->
        </div>
      </div>

      <div class="form-group">
        <button class="item-list icon is-right btn_s_w" @click="openModal()" type="button">상세검색<i class="fas fa-plus"></i></button>
      </div>


      <!-- 상세검색 내용 -->
      <div id="open-modal" class="modal-window">
        <div class="modal-window-wrap">
          <div class="modal-window-top">
            <h4>상세검색</h4>
            <button title="Close" @click="closeModal()" type="button" class="bt-modal-close mt5"><img src="../../public/img/bt_close_w.png" /></button>
          </div>

          <div class="search_box_wrap">

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">REF NO</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.refNo" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.refNm" @input="initRefNo()" @keypress.enter="popRefNo()">
                  <button class="icon is-right" @click="popRefNo()"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">부서</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.deptCd" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.deptNm" @input="initDeptNm()" @keypress.enter="popDeptNm()">
                  <button class="icon is-right" @click="popDeptNm()"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">수익자국가</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.benCountryCd" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.benCountry" @input="initBenCountry()" @keypress.enter="popBenCountry()">
                  <button class="icon is-right" @click="popBenCountry()"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">PJT명</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.projectId" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.projectNm" @input="initProjectNm()" @keypress.enter="popProjectNm()">
                  <button class="icon is-right" @click="popProjectNm()"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">고객명</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.customerId" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.customerNm" @input="initCustomerNm()" @keypress.enter="popCustomerNm()">
                  <button class="icon is-right" @click="popCustomerNm()"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">보증금액</span>
              </div>
              <div class="search_con search-area">
                <input class="input" type="text" v-model="form.guaranteeAmt">
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">개설일자</span>
              </div>
              <div class="search_con search-area">
                <el-date-picker
                  v-model="form.openingDt"
                  unlink-panels
                  type="date"
                  format="yyyyMMdd"
                  value-format="yyyyMMdd">
                </el-date-picker>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">국내만기일</span>
              </div>
              <div class="search_con search-area">
                <el-date-picker
                  v-model="form.localMaturityDt"
                  unlink-panels
                  type="date"
                  format="yyyyMMdd"
                  value-format="yyyyMMdd">
                </el-date-picker>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">해외만기일</span>
              </div>
              <div class="search_con search-area">
                <el-date-picker
                  v-model="form.overseasMaturityDt"
                  unlink-panels
                  type="date"
                  format="yyyyMMdd"
                  value-format="yyyyMMdd">
                </el-date-picker>
              </div>
            </div>

          </div>

          <div class="modal-window-bottom">
            <ul>
              <li>
                <button class="bt_blue_s" @click="search()">검색</button>
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
    <!-- //검색조건 영역 -->


    <div class="table-area mt20">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">전표내역</h3>
          </div>
        </div>

        <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height"
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef"
                         :suppressRowClickSelection="true"
                         rowSelection="multiple"
                         @grid-ready="onReady"
                         @row-selected="onRowSelected"
                         @rowDoubleClicked="rowDoubleClick"
                         @cell-value-changed="cellValueChange"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import _ from 'lodash';
import mixin from '@/mixin'
import mixinSlip from '@/mixin/slip'
import common from '@/mixin/common';
import DhxCalendar from "@/components/DhxCalendar.vue";
import NumberInputCellRenderer from '@/components/agGrid/numberinput-cell-renderer'
import DatepickerCellRenderer from '@/components/agGrid/datepicker-cell-renderer'
import AgGridSearchBtn from '@/components/agGrid/AgGridSearchBtn';
import VendorPop from '@/components/Vendor_Ag.vue';
import { AgGridVue } from 'ag-grid-vue';
import EmpPop from '../components/Emp_Ag.vue';
import RefNoPop from "@/components/RefNoPop.vue";
import AgGridScanAttach from "@/components/agGrid/AgGridScanAttach.vue";
import CctrPop from '@/components/Cctr_Ag.vue';
import BenCountryPop from "@/components/BenCountryPop.vue";
import CustomerPop from '@/components/CustomerPop.vue';
import BondProjectPop from "@/components/BondProjectPop.vue";
import ApprovalModal from "@/components/accrualSlip/Approval/Main.vue";

export default {
  name: 'BondExpendList',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, VendorPop, EmpPop
  },
  data() {
    return {
      title: 'BOND 경비조회',
      columnDefs : [],
      gridOptions : {
        enableRangeSelection: true,
        statusBar: {
          statusPanels: [
            {
              statusPanel: 'agTotalAndFilteredRowCountComponent',
              align: 'left',
            },
          ]
        },
      },
      defaultColDef: {
        resizable: true,
        filter: true,
        sortable: true,
      },
      gridApi : null ,    //gridApi
      columnApi : null ,  //columApi
      data: [],
      config: {
        hideTime: true,
        format: 'YYYY-MM-DD',
        outputFormat: 'YYYY-MM-DD HH:mm:ss',
        dateFormat: '%Y-%m-%d'
      },
      searchDt: [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss')],
      form: {
        compCd : this.$store.state.loginInfo.compCd,
        refNo : '',
        refNoCustomerNm : '',
        deptCd : '',
        deptNm : '',
        benCountryCd : '',
        benCountry : '',
        projectId : '',
        projectNm : '',
        customerId : '',
        customerNm : '',
        guaranteeAmt: '',
        openingDt: '',
        localMaturityDt: '',
        overseasMaturityDt: '',
      },
      frameworkComponents: {
        numberInputRenderer: NumberInputCellRenderer,
        searchButtonRenderer: AgGridSearchBtn,

      },
    }
  },
  created() {
    this.createColumnDefs();
    this.search();
  },
  methods: {
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    createColumnDefs(){
      const vm = this;
      this.columnDefs = [
        {
          headerName:'No.',
          field:'no',
          width:70,
          cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          }
        },
        {
          headerName: '담당부서',
          field: 'deptNm',
          width: 140,
          cellStyle:{textAlign: 'left'},
          editable: false,
        },
        {
          headerName: '수익자국가',
          field: 'benCountry',
          width: 120,
          editable: false,
        },
        {
          headerName: '고객명',
          field: 'customerNm',
          width: 200,
          editable: false,
        },
        {
          headerName: 'PJT명',
          field: 'projectNm',
          width: 200,
          editable: false,
        },
        {
          headerName: 'REF NO.',
          field: 'refNo',
          width: 160,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '국내은행',
          field: 'localBankNm',
          width: 150,
          editable: false,
        },
        {
          headerName: '해외은행',
          field: 'intBankNm',
          width: 160,
          cellStyle:{textAlign: 'left'},
          editable: false,
        },
        {
          headerName: 'BOND종류',
          field: 'bondCd',
          width: 120,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '통화(보증금)',
          field: 'bondCurrencyCd',
          width: 120,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '보증금', 
          field: 'guaranteeAmt',
          width: 120,
          valueFormatter: (params) => {
            return vm.getDoubleNumberFormat(params.value);
          },
          cellStyle:{textAlign: 'right'},
        },
        {
          headerName: '수수료유형',
          field: 'type',
          width: 110,
          cellStyle:{textAlign: 'center'},

        },
        {
          headerName: '개설일',
          field: 'openingDt',
          width: 130,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return vm.getDateFormat(params.value);
          },
          editable: false,
        },
        {
          headerName: '국내만기',
          field: 'maturityDtI',
          width: 110,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return vm.getDateFormat(params.value);
          },
          editable: false,
        },
        {
          headerName: '해외만기',
          field: 'maturityDtF',
          width: 110,
          valueFormatter: (params) => {
            return vm.getDateFormat(params.value);
          },
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '신규/AMEND',
          field: 'amentSeq',
          width: 120,
          editable: false,
        },
        {
          headerName: '분납 및 기타비용',
          field: 'splitEtcYn',
          width: 90,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '전표번호',
          field: 'slipNo',
          width: 170,
          editable: false,
        },
        {
          headerName: '적요',
          field: 'headerText',
          width: 220,
          editable: false,
        },
        {
          headerName: '통화(수수료)',
          field: 'currencyCd',
          width: 120,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '국내요율',
          field: 'rateI',
          width: 100,
          valueFormatter: (params) => {
            return vm.getDoubleNumberFormat(params.value);
          },
          editable: false,
          cellStyle:{textAlign: 'right'},
        },
        {
          headerName: '국내수수료',
          field: 'usedAmtI',
          width: 150,
          valueFormatter: (params) => {
            return vm.getNumberFormat(params.value);
          },
          editable: false,
          cellStyle:{textAlign: 'right'},
        },
        {
          headerName: '해외요율',
          field: 'rateF',
          width: 150,
          valueFormatter: (params) => {
            return vm.getDoubleNumberFormat(params.value);
          },
          editable: false,
          cellStyle:{textAlign: 'right'},
        },
        {
          headerName: '해외수수료',
          field: 'usedAmtF',
          width: 120,
          valueFormatter: (params) => {
            return vm.getDoubleNumberFormat(params.value);
          },
          editable: false,
          cellStyle:{textAlign: 'right'},
        },
        {
          headerName: '예산',
          field: 'budget',
          width: 150,
          valueFormatter: (params) => {
            return vm.getNumberFormat(params.value);
          },
          editable: false,
          cellStyle:{textAlign: 'right'},
        },
      ]
    },
    search(){
      this.$store.commit('loading')
      this.$http.post('/api/bond/expend/list', this.form)
          .then(response => {
            console.log(response)
            this.data = response.data;
            // if(response.data.length == 0){
            //     this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
            //   }
          })
          .finally(() => {
            this.$store.commit('finish')
          })
    },
    chkNumber(params){

      var result = '';
      var val = params.value;

      if(!_.isEmpty(val) || _.isNumber(val)){
        val = val.toString();
        result = this.$numeral(val).format('0,0');
        this.data[params.node.rowIndex][params.column.colId] = this.$numeral(val).value();
      }

      return result
    },
    openModal() {
      document.getElementById("open-modal").style.opacity = "1";
      document.getElementById("open-modal").style.pointerEvents = "auto";
    },
    closeModal() {
      document.getElementById("open-modal").style.opacity = "0";
      document.getElementById("open-modal").style.pointerEvents = "none";
    },
    resetSearch(){
      this.searchDt = [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss')];
      this.form.batchNm = '';
      this.form.headerNm = '';
      this.form.sourceNm = '';
      this.form.fileYn = '';
      this.form.jiniYn = '';
      this.form.trxTypeCd = '';
      this.form.trxTypeNm = '';
      this.form.transferredBy = '';
      this.form.transferredByNm = '';
    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    getDoubleNumberFormat(val) {
      if(val) {
        return this.$numeral(val).format('0,0.00');
      }
    },
    popTransfferedBy() {
      let vm = this
      this.$modal.open({
        component: EmpPop,
        props: {
          param: this.form.transferredByNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.transferredBy = obj.empNo;
            vm.form.transferredByNm = obj.empNm;
          }
        }
      })
    },
    initTransfferedBy(force) {
      if (force === true) this.form.transferredByNm = '';
      if (this.form.transferredByNm === '') {
        this.form.transferredBy = '';
        this.form.transferredByNm = '';
      }
    },
    popRefNo() {
      let vm = this;
      this.$modal.open({
        component: RefNoPop,
        props: {
          param : this.form.refNo,
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.refNo = obj.refNo;
            vm.form.refNm = obj.customerNm;
          }
        }
      })
    },
    initRefNo(force) {
      if (force === true) this.form.refNo = '';
      if (this.form.refNo === '') {
        this.form.refNo = '';
        this.form.refNoCustomerNm = '';
      }
    },
    popDeptNm() {
      let vm = this;
      this.$modal.open({
        component: CctrPop,
        props: {
          param : this.form.deptNm,
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.deptCd = obj.deptCd;
            vm.form.deptNm = obj.deptNm;
          }
        }
      })
    },
    initDeptNm(force) {
      if (force === true) this.form.deptNm = '';
      if (this.form.deptNm === '') {
        this.form.deptCd = '';
        this.form.deptNm = '';
      }
    },
    popBenCountry() {
      let vm = this;
      this.$modal.open({
        component: BenCountryPop,
        props: {
          param : this.form.benCountry,
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.benCountryCd = obj.benCountryCd;
            vm.form.benCountry = obj.benCountryNm;
          }
        }
      })
    },
    initBenCountry(force) {
      if (force === true) this.form.benCountry = '';
      if (this.form.benCountry === '') {
        this.form.benCountryCd = '';
        this.form.benCountry = '';
      }
    },
    popProjectNm() {
      let vm = this;
      this.$modal.open({
        component: BondProjectPop,
        props: {
          param : this.form.projectNm,
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.projectId = obj.projectId;
            vm.form.projectNm = obj.name;
          }
        }
      })
    },
    initProjectNm(force) {
      if (force === true) this.form.projectNm = '';
      if (this.form.projectNm === '') {
        this.form.projectId = '';
        this.form.projectNm = '';
      }
    },
    popCustomerNm() {
      let vm = this;
      this.$modal.open({
        component: CustomerPop,
        props: {
          param : this.form.customerNm,
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.customerId = obj.customerNum;
            vm.form.customerNm = obj.customerName;
          }
        }
      })
    },
    initCustomerNm(force) {
      if (force === true) this.form.customerNm = '';
      if (this.form.customerNm === '') {
        this.form.customerId = '';
        this.form.customerNm = '';
      }
    },
    rowDoubleClick(params) {
      const vm = this
      this.$modal.open({
        component: ApprovalModal,
        parent: this,
        props: {
          slipNo : params.data.slipNo,
          title : 'BOND 전표'
        },
        width: 1200,
        events: {
          close(data) {
            vm.goSearch()
          }
        }
      });
    },
    mainGridRefresh(idx) {
      var rows = [];
      var rowNode = this.gridApi.getDisplayedRowAtIndex(idx);
      rows.push(rowNode);
      this.gridApi.redrawRows({ rowNodes: rows });
    },
    cellValueChange(params){
      const vm = this;
      const rowNode = params.api.getRowNode(params.node.rowIndex);
    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.data[rowIdx].regYn = params.node.isSelected();
    }
  },
}
</script>
