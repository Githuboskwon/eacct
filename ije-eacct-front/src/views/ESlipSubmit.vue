<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">전자전표 ERP전송</h2>
      <div class="btn-type1 clearfix">
        <el-button type="primary" icon="el-icon-search" size="large" @click="search">조회</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
          <label class="control-label-req" id="standardMonth">회계년월</label>
          <div class="form-input">
<!--            <div class="datepicker w-type-ymd">-->
<!--              <monthly-picker v-model="form.searchMonth">-->
<!--              </monthly-picker>-->
<!--            </div>-->
            <el-date-picker
                v-model="form.searchMonth"
                type="month"
                format="yyyy-MM"
                value-format="yyyyMM"
                style="width: 260px;">
            </el-date-picker>
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
                <span class="search_tit">월구분</span>
              </div>
<!--              <div class="search_con search-area">-->
<!--                <div class="form-input">-->
<!--                  <div class="dp-ib w100p">-->
<!--                    <monthly-picker v-model="form.searchMonth">-->
<!--                    </monthly-picker>-->
<!--                  </div>-->
<!--                </div>-->
<!--              </div>-->
              <el-date-picker
                  v-model="form.searchMonth"
                  type="month"
                  format="yyyy-MM"
                  value-format="yyyyMM"
                  style="width: 40%">
              </el-date-picker>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">거래유형</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.slipType" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.slipTypeText" @input="initslipType" @keypress.enter="popslipType">
                  <button class="icon is-right" @click="popslipType(true)"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">전표번호</span>
              </div>
              <div class="search_con search-area">
                <div class="dp-ib w100p">
                  <input class="input" type="text" v-model="form.slipNo">
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">상신여부</span>
              </div>
              <div class="search_con search-area">
                <b-select class="select is-fullwidth w100p" v-model="form.reportArraign">
                  <option value="">==전체==</option>
                  <option value="Y">Y</option>
                  <option value="N">N</option>
                </b-select>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">결제여부</span>
              </div>
              <div class="search_con search-area">
                <b-select class="select is-fullwidth w100p" v-model="form.approvalArraign">
                  <option value="">==전체==</option>
                  <option value="Y">Y</option>
                  <option value="N">N</option>
                </b-select>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">검인여부</span>
              </div>
              <div class="search_con search-area">
                <b-select class="select is-fullwidth w100p" v-model="form.sealArraign">
                  <option value="">==전체==</option>
                  <option value="Y">Y</option>
                  <option value="N">N</option>
                </b-select>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">ERP반영여부</span>
              </div>
              <div class="search_con search-area">
                <b-select class="select is-fullwidth w100p" v-model="form.slipIfFlag">
                  <option value="">==전체==</option>
                  <option value="Y">Y</option>
                  <option value="N">N</option>
                  <option value="E">E</option>
                  <option value="ING">ING</option>
                </b-select>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">ERP중간전송여부</span>
              </div>
              <div class="search_con search-area">
                <b-select class="select is-fullwidth w100p" v-model="form.transferType">
                  <option value="">==전체==</option>
                  <option value="Y">Y</option>
                  <option value="N">N</option>
                </b-select>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">거래처 코드/명</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.integrationVendorNum" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.integrationVendorName" @input="initVendor" @keypress.enter="popVendor">
                  <button class="icon is-right" @click="popVendor(true)"><i class="fas fa-search"></i>
                  </button>
                </div>
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
            <h3 class="ico_table_name">전자전표 ERP전송</h3>
          </div>
          <div class="btn-type2 clearfix">
            <el-button type="success" icon="el-icon-upload2" @click="slipTransferErp">ERP일괄전송</el-button>
            <el-button type="primary" icon="el-icon-thumb" @click="chk10">10개 선택</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="removeRow">전표삭제</el-button>
            <!-- <button class="btn-size btn-w-gray" @click="slipTransferErp()">
                    <span class="btn-icon">
                      <i class="fas fa-plus"></i>
                    </span>
              ERP일괄전송
            </button>
            <button class="btn-size btn-w-gray" @click="chk10()">
              10개 선택
            </button>
            <button class="btn-size btn-w-gray" @click="removeRow()">
                    <span class="btn-icon">
                      <i class="fas fa-trash-alt"></i>
                    </span>
              전표삭제
            </button> -->
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
              @cell-value-changed="cellValueChange"
              @rowDoubleClicked="rowDoubleClick"
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
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
import NumberInputCellRenderer from '@/components/agGrid/numberinput-cell-renderer'
import DatepickerCellRenderer from '@/components/agGrid/datepicker-cell-renderer'
import AgGridSearchBtn from '@/components/agGrid/AgGridSearchBtn';
import VendorPop from '@/components/Vendor_Ag.vue';
import { AgGridVue } from 'ag-grid-vue';
import SlipBulkDetailModal from '@/components/SlipBulkDetailModal.vue';
import EmpPop from '../components/Emp_Ag.vue';
import PayBankPop from '@/components/PayBankPop.vue';
import CurrencyPop from '@/components/CurrencyPop.vue';
import AgGridScanAttach from "@/components/agGrid/AgGridScanAttach";
import EvidAtchBatchPop from "@/components/EvidAtchBatchPop";
import JiniAtchBatchPop from "@/components/JiniAtchBatchPop";
import MonthlyPicker from '@/components/MonthlyPicker.vue'
import MonthlyPickerCellRenderer from '@/components/grid/GridMonthlyPicker.vue'
import dealTypePoP from "@/components/dealTypePoP";
import ApprovalModal from "@/components/accrualSlip/Approval/Main";

export default {
  name: 'PurSlipList',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, VendorPop, EmpPop , MonthlyPicker
  },
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
      form: {
        compCd: this.$store.state.loginInfo.compCd,
        searchMonth: this.$moment().format("YYYYMM"),
        slipType: '',
        slipTypeText: '',
        reportArraign: '',
        approvalArraign: '',
        sealArraign: '',
        slipIfFlag: '',
        transferType: '',
        integrationVendorNum: '',
        integrationVendorName: '',
        vendorId: '',
        vendorNm: '',
        bankAcctNum: '',
        checkNo: '',
        futurePayDueDt: '',
        paymentMethod: '',
        currencyCd: '',
        transferredBy: '',
        transferredByNm: '',
        currencyNm: '',
      },
      frameworkComponents: {
        selectRenderer: SelectCellRenderer,
        numberInputRenderer: NumberInputCellRenderer,
        searchButtonRenderer: AgGridSearchBtn,
        datePicker: DatepickerCellRenderer,
        monthlyPicker: MonthlyPickerCellRenderer,
        scanAttach: AgGridScanAttach,
      },
      paymentMethodCombo : [],
      combos: [],
    }
  },
  created() {
    this.createColumnDefs();
    this.getPaymentMethodCombo();
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
          headerName: '선택'
          , checkboxSelection: true
          , field:'regYn'
          , width : 80
          , cellStyle:{textAlign: 'center'}
          , editable: false
        },
        {
          headerName: '회계일자',
          field: 'glDate',
          width: 120,
          valueFormatter: (params) => {
            return vm.getDateFormat(params.value)
          },
          editable: false,
        },
        {
          headerName: '거래유형',
          field: 'slipTypeText',
          width: 200,
          editable: false,
        },
        {
          headerName: '전자전표번호',
          field: 'slipNo',
          width: 180,
          editable: false,
        },
        {
          headerName: '결재그룹번호',
          field: 'approvalGroupId',
          cellStyle:{textAlign: 'center'},
          width: 120,
          editable: false,
        },
        {
          headerName: '작성자',
          field: 'createUser',
          width: 90,
          cellStyle:{textAlign: 'right'},
          editable: false,
        },
        {
          headerName: '상신',
          field: 'reportArraign',
          // cellRenderer: 'numberInputRenderer',
          width: 100,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '최종결재',
          field: 'approvalArraign',
          width: 100,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '최종검인',
          field: 'sealArraign',
          width: 100,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: 'ERP중간전송',
          field: 'transferType',
          width: 120,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: 'ERP반영여부',
          field: 'slipIfFlag',
          width: 120,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: 'ERP에러메세지',
          field: 'slipInterfaceErrorMsg',
          width: 150,
          editable: false,
        },
        {
          headerName: 'ERP상태',
          field: 'approvalStatus',
          width: 110,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        // {
        //   headerName: 'ERP지급승인',
        //   field: '',
        //   width: 120,
        //   editable: false,
        // },
        {
          headerName: '거래처',
          field: 'storeName',
          width: 200,
          editable: false,
        },
        {
          headerName: '통화',
          field: 'slipCurrencyCode',
          width: 90,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '외화합계금액',
          field: 'usedFAmt',
          valueFormatter: (params) => {
            return vm.getDoubleNumberFormat(params.value);
          },
          width: 120,
          editable: false,
          cellStyle:{textAlign: 'right'},
        },
        {
          headerName: '합계금액(KRW)',
          field: 'usedAmt',
          valueFormatter: (params) => {
            return vm.getNumberFormat(params.value);
          },
          width: 150,
          editable: false,
          cellStyle:{textAlign: 'right'},
        },
        {
          headerName: '전표번호',
          field: 'slipNo',
          hide: true
        },
        {
          headerName: '전표타입',
          field: 'slipType',
          hide: true
        },
      ]
    },
    getPaymentMethodCombo() {
      this.$http
          .get(`/api/code/combo`, { params: { groupCd: "PAYMENT_METHOD_CD" } })
          .then((response) => {
            this.paymentMethodCombo = response.data;
          });
    },
    search() {
      this.$store.commit('loading')
      this.$http.post('/api/erp/submit/list', this.form)
          .then(response => {
            this.data = response.data;
            // if(response.data.length == 0){
            //     this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
            //   }
            this.closeModal();
          })
          .finally(() => {
            this.$store.commit('finish')
          })
    },
    slipTransferErp(){
      let vm = this;
      // let list = this.data.filter(v => v.regYn)
      const list = this.gridApi.getSelectedRows();

      list.forEach(x=>{
        if(x.reportArraign !== 'Y'){
          this.$swal({ type: 'error', html: "상신되지 않은 전표 입니다. 전표번호 : " + x.slipNo})
          return false;
        }else
        if(x.slipIfFlag !== 'N'){
          this.$swal({ type: 'error', html: "이미 ERP 반영된 전표입니다. 전표번호 : " + x.slipNo})
          return false;
        }else
        if(x.slipIfFlag !== 'N'){
          this.$swal({ type: 'error', html: "이미 중간전송된 전표입니다. 전표번호 : " + x.slipNo})
          return false;
        }
      })

      vm.result = [];
      if(list.length > 0){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 전송하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            vm.result = list;
            this.$http.post(`/api/erp/submit/transfer`, vm.result)
                .then((response) => {
                  if(response.data.indexOf("상신") != -1 || response.data.indexOf("실패") != -1){
                    this.$swal({ type: 'error', html: response.data })
                        .then((result) => {
                          if (result.value) {
                            this.search();
                          }
                        });
                  }else{
                    this.$swal({ type: 'success', html: response.data })
                        .then((result) => {
                          if (result.value) {
                            this.search();
                          }
                        });
                  }
                })
                .catch((e) => {
                  this.$swal({ type: 'error', text: '전송 실패하였습니다.' })
                })
                .finally(() => {
                  this.$store.commit('finish')
                });
          }
        })
      } else {
        this.$swal({ type: 'info', text: '전송할 항목을 선택하세요.' });
      }
    },
    chk10(){
      this.gridApi.forEachNode(node => {
        if(node.rowIndex < 10) {
          node.setSelected(true);
        }
      })
    },
    removeRow() {
      let vm = this;
      // let list = this.data.filter(v => v.regYn)
      const list = this.gridApi.getSelectedRows();

      list.forEach(x=>{
        if(x.status === 'SV' || x.status === 'VC' || x.status === 'VE'){
          this.$swal({ type: 'error', html: "전표삭제는 저장됨, 검증됨, 검증오류 상태만 삭제 가능합니다."})
          return false;
        }else
        if(x.slipGroupYn === 'Y') {
          this.$swal({
            type: 'question',
            text: '선택한 항목을 삭제하시겠습니까?',
            showCancelButton: true
          }).then(r => {
            if (!r.value) {
              return false;
            }
          })
        }
      })

      vm.result = [];
      if(list.length > 0){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            vm.result = list;

            vm.result.forEach(x=>{
              x.glDate = '';
            })

            this.$http.post(`/api/slip/delete`, vm.result)
                .then((response) => {

                  this.$swal({ type: 'success', html: '삭제하였습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.search();
                        }
                      });

                  // if(response.data.indexOf("상신") != -1 || response.data.indexOf("실패") != -1){
                  //
                  //   this.$swal({ type: 'error', html: response.data })
                  //       .then((result) => {
                  //         if (result.value) {
                  //           // this.search();
                  //         }
                  //       });
                  //
                  // }else{
                  //
                  //   this.$swal({ type: 'success', html: response.data })
                  //       .then((result) => {
                  //         if (result.value) {
                  //           // this.search();
                  //         }
                  //       });
                  //
                  // }

                })
                .catch((e) => {
                  this.$swal({ type: 'error', text: '삭제 실패하였습니다.' })
                })
                .finally(() => {
                  this.$store.commit('finish')
                });
          }
        })
      } else {
        this.$swal({ type: 'info', text: '전송할 항목을 선택하세요.' });
      }
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
      this.form.compCd = this.$store.state.loginInfo.compCd;
      this.form.searchMonth = this.$moment().format("YYYYMM");
      this.form.slipNo = '';
      this.form.slipType = '';
      this.form.slipTypeText = '';
      this.form.reportArraign = '';
      this.form.approvalArraign = '';
      this.form.sealArraign = '';
      this.form.slipIfFlag = '';
      this.form.transferType = '';
      this.form.integrationVendorNum = '';
      this.form.integrationVendorName = '';
      this.form.vendorId = '';
      this.form.vendorNm = '';
      this.form.bankAcctNum = '';
      this.form.checkNo = '';
      this.form.futurePayDueDt = '';
      this.form.paymentMethod = '';
      this.form.currencyCd = '';
      this.form.transferredBy = '';
      this.form.transferredByNm = '';
      this.form.currencyNm = '';
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
    rowDoubleClick(params) {
      this.$cookie.set('searchForm', JSON.stringify(this.form));
      this.$store.commit('searchForm', JSON.parse(this.$cookie.get('searchForm')));

      // this.$router.push({path: `/accrualSlip/${params.data.slipNo}`});
      let slipType = params.data.slipType;
      let docMngNo = params.data.apprNo;
      let slipNo = params.data.slipNo;

      this.$cookie.set('searchForm', JSON.stringify(this.form));
      this.$store.commit('searchForm', JSON.parse(this.$cookie.get('searchForm')));

      this.showDetailPop(slipNo, slipType, docMngNo);
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
    popVendor() {
      let vm = this
      this.$modal.open({
        component: VendorPop,
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
    initVendor(force) {
      if (force === true) this.form.integrationVendorName = '';
      if (this.form.integrationVendorName === '') {
        this.form.integrationVendorNum = '';
        this.form.integrationVendorName = '';
      }
    },
    popslipType() {
      let vm = this;
      this.$modal.open({
        component: dealTypePoP,
        props: {
          param: this.form.slipTypeText,
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.slipType = obj.trxTypeCode;
            vm.form.slipTypeText = obj.trxTypeName;
          }
        }
      })
    },
    initslipType(force) {
      if (force === true) this.form.slipTypeText = '';
      if (this.form.slipTypeText === '') {
        this.form.slipType = '';
        this.form.slipTypeText = '';
      }
    },
    popCurrencyNm() {
      let vm = this;
      this.$modal.open({
        component: CurrencyPop,
        props: {
          param : this.form.currencyNm,
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.currencyCd = obj.currencyCode;
            vm.form.currencyNm = obj.currencyName;
          }
        }
      })
    },
    initCurrencyNm(force) {
      if (force === true) this.form.currencyNm = '';
      if (this.form.currencyNm === '') {
        this.form.currencyId = '';
        this.form.currencyNm = '';
      }
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
    /**
     * * 그리드 체크박스 선택 이벤트
     */
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.data[rowIdx].regYn = params.node.isSelected();
    }
  },
}
</script>
