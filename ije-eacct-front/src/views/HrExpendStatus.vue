<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <!-- <button class="btn-size btn-blue fl_left" @click="goOpen">
            <span class="btn-icon">
              <i class="fas fa-pen-alt"></i>
            </span>
          조회
        </button> -->
        <el-button size="large" type="primary" icon="el-icon-search" @click="goOpen">조회</el-button>
      </div>
    </div>
    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
        <label class="control-label-req" id="standardMonth">회계일자</label>
        <div class="form-input">
<!--          <div class="datepicker w-type-ymd">-->
<!--            <dhx-calendar class="input" v-model="form.startDate"/>-->
<!--          </div>-->
<!--          <span> ~ </span>-->
<!--          <div class="datepicker w-type-ymd">-->
<!--            <dhx-calendar class="input" v-model="form.endDate"/>-->
<!--          </div>-->
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

      <div class="form-group">
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
                <span class="search_tit">회계일자</span>
              </div>
              <div class="search_con search-area">
                <div class="form-input">
<!--                  <div class="datepicker w-type-ymd">-->
<!--                    <dhx-calendar class="input" v-model="form.startDate"/>-->
<!--                  </div>-->
<!--                  <div class="datepicker w-type-ymd">-->
<!--                    <dhx-calendar class="input" v-model="form.endDate"/>-->
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
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">작성자</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.empNo" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.empNm" @input="initEmp" @keypress.enter="popEmp">
                  <button class="icon is-right" @click="popEmp()"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">거래처 코드/명</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.integrationVendorNum" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.integrationVendorName" @input="initVendor1" @keypress.enter="popVendor1">
                  <button class="icon is-right" @click="popVendor1(true)"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>

          </div>

          <div class="modal-window-bottom">
            <ul>
              <li>
                <button class="bt_blue_s" @click="goOpen()">검색</button>
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

    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">경조금 내역</h3>
          </div>
          <div class="btn-wrap btn-type2 clearfix">
            <el-button type="success" icon="el-icon-document-checked" size="small" @click="saveExcel">엑셀 저장</el-button>
          </div>
        </div>


        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height"
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef"

                         :enableRangeSelection="true"
                         :suppressMaxRenderedRowRestriction="true"
                         :suppressColumnVirtualisation="true"
                         :suppressRowVirtualisation="true"
                         :suppress-row-click-selection="false"
                         :context="context"

                         @grid-ready="onGridReady"
                         @rowDoubleClicked="rowDoubleClick"
                         @cell-clicked="onCellClicked"/>
            <ag-grid-vue  v-show="false"
                          ref="grid" style="width: 100%;" class="ag-theme-alpine"
                         :columnDefs="columnDefs"
                         :rowData="excelData"
                         :gridOptions="gridOptionsExcel"
                         :defaultColDef="defaultColDefExcel"/>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import createBus from '@/libs/eventBus';
import DhxCalendar from "@/components/DhxCalendar.vue";

import mixin from "@/mixin";
import mixinSlip from "@/mixin/slip";
import MonthlyPicker from '@/components/MonthlyPicker.vue'
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer";
import NumberInputCellRenderer from "@/components/agGrid/numberinput-cell-renderer";
import DatepickerCellRenderer from "@/components/agGrid/datepicker-cell-renderer";
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer';
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn";
import Expend from "@/components/Expend_Ag";
import Emp from "@/components/Emp_Ag";
import HrExpendPop from "@/components/HrExpendPop";
import Vendor from "@/components/Vendor_Ag";
import ApprovalModal from "@/components/accrualSlip/Approval/Main";
import SlipDetailModal from "@/components/SlipDetailModal";

const bus = createBus();

const options = {};
const lock = {};

function queryMngItemCd() {
  return new Promise((resolve, reject) => {
    if (options["EXPEND_MONEY_CD"] !== undefined || lock["EXPEND_MONEY_CD"]) {
      reject(false);
    } else {
      lock["EXPEND_MONEY_CD"] = true;
      this.$store.commit("loading");
      this.$http
          .get("/api/code/detail", {
            params: {
              groupCd: "EXPEND_MONEY_CD"
            }
          })
          .then(response => {
            options["EXPEND_MONEY_CD"] = response.data;
            delete lock["EXPEND_MONEY_CD"];
            bus.emit("EXPEND_MONEY_CD", response.data);
            return resolve(response);
          })
          .catch(response => {
            return reject(response);
          })
          .finally(() => {
            this.$store.commit("finish");
          });
    }
  });
}

export default {
  name: "HrExpendStatus",
  mixins: [mixin, mixinSlip],
  components: {
    DhxCalendar,
    MonthlyPicker,
    Expend
  },
  data() {
    return {
      title: "HR 경조금신청 현황",
      compCds: [],
      data: [],
      excelData: [],
      rowNode: "",
      deletedata: "",

      columnDefs : [],
      gridOptions : {
        //enableColResize: true,
        //enableFilter: true,
        //animateRows: false,
        //enableSorting: true
        statusBar: {
            statusPanels: [
                {
                statusPanel: 'agTotalAndFilteredRowCountComponent',
                align: 'left',
                },
            ]
        },
      },
      gridOptionsExcel : {
        //enableColResize: true,
        //enableFilter: true,
        //animateRows: false,
        //enableSorting: true
      },
      defaultColDef: {
        resizable: true,
        filter: true,
        sortable: true,
      },
      defaultColDefExcel: {
        resizable: true,
        filter: false,
        sortable: false,
      },
      frameworkComponents: {
        select: SelectCellRenderer,
        numberInput: NumberInputCellRenderer,
        datePicker: DatepickerCellRenderer,
        checkboxRenderer: CheckboxCellRenderer,
        schBtn : AgGridSearchBtn,
      },
      context: {
        headerAllCheckEvent: this.allChk
      },
      gridApi: null,
      columnApi: null,

      config: {
        hideTime: false,
        format: "YYYY-MM",
        outputFormat: "YYYYMM"
      },
      searchDt: [this.$moment().add(-1, 'month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')],
      form: {
        compCd: "",
        empNm: "",
        empNo: "",
        startDate: this.$moment().add(-1, 'month').format('YYYYMMDD'),
        endDate: this.$moment().format('YYYYMMDD'),
        expendCd: "",
      },
      combos: {
        'EXPEND_MONEY_CD' : [],
      },
    };
  },
  methods: {
    constructGridSuccessful(grid) {
      grid.setDateFormat("%Y-%m", "%Y%m");
    },
    onGridReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    createColumnDefs(){
      const vm = this;

      this.columnDefs = [
        {
          headerName: '전표번호',
          field: 'slipNo',
          width: 180,
          editable: false,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '회계일자',
          field: 'postingDt',
          width: 150,
          valueFormatter: function(params) {
            return vm.getDateFormat(params.value);//숫자 포맷<common.js>
          },
          cellStyle:{textAlign: 'center'},
          editable : false
        },
        {
          headerName: '등록자',
          field: 'chgNm',
          width: 100,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '경조사원',
          field: 'chgNm',
          width: 130,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '당사자성명',
          field: 'expendReceiveNm',
          width: 150,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '경조금',
          field: 'expendAmt',
          width: 100,
          editable: false,
          cellRenderer: 'numberInput',
          cellStyle:{textAlign: 'right'}
        },
        {
          headerName: '경조구분',
          field: 'expendCd',
          width: 150,
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
          valueFormatter: function(params) {
            return vm.chkType(params.value)
          },
        },
        {
          headerName: '경조일',
          field: 'expendDt',
          width: 130,
          valueFormatter: function(params) {
            return vm.getDateFormat(params.value);//숫자 포맷<common.js>
          },
          editable : false
        },
        {
          headerName: '화환'
          , field:'wreathYn'
          , width : 100
          , editable: false
          , cellStyle:{textAlign: 'center'}
          , cellRenderer: 'checkboxRenderer'
          , cellRendererParams: {
            trueValue: "Y"    //true값  지정 defalut-"Y" ex) "Y", true, "1"
            , falseValue: "N"  //false값 지정 defalut-"N" ex) "N", false, "0"
            , readonly: true
            , class : 'girdChk'
          },
        },
        {
          headerName: '상조용품'
          , field:'mutualYn'
          , width : 120
          , editable: false
          , cellStyle:{textAlign: 'center'}
          , cellRenderer: 'checkboxRenderer'
          , cellRendererParams: {
            trueValue: "Y"    //true값  지정 defalut-"Y" ex) "Y", true, "1"
            , falseValue: "N"  //false값 지정 defalut-"N" ex) "N", false, "0"
            , readonly: true
            , class : 'girdChk'
          },
        },
        // {
        //   headerName: '휴일',
        //   field: 'holiday',
        //   width: 80,
        //   editable: false,
        //   cellStyle:{textAlign: 'right'}
        // },
        {
          headerName: '경조금지급처',
          field: 'integrationVendorName',
          width: 200,
          editable: false,
          cellStyle:{textAlign: 'center'}
        },
      ]
    },
    onCellClicked(params){
      this.rowNode = params;
    },
    rowDoubleClick(params){
      let slipType = params.data.slipType;
      let docMngNo = params.data.apprNo;
      let slipNo = params.data.slipNo;

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
              },
              parent: this,
              width: 1200,
              events: {
                close(data) {
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
    chkType(params) {
      this.combos['EXPEND_MONEY_CD'].forEach(x =>{
        if(params === x.key) params = x.value;
      })
      return params;
    },
    allChk(){

      const grid = this.$refs.grid
      const chkDatas = grid.rowData.filter((x,i) => {
        return x.regYn === "Y"
      })

      if(chkDatas.length === this.data.length){
        this.gridOptions.api.forEachNodeAfterFilter((v, i) => {
          v.data.regYn = "N"
          // this.subData.regYn = "0"
        })
      }else{
        this.gridOptions.api.forEachNodeAfterFilter((v, i) => {
          v.data.regYn = "Y"
          // this.subData.regYn = "1"
        })
      }


      // this.$forceUpdate()
      // this.forceRerender();
      grid.rowData.forEach((x,i)=>{
        this.mainGridRefresh(i);
      })

    },
    mainGridRefresh(idx) {
      var rows = [];
      var rowNode = this.gridApi.getDisplayedRowAtIndex(idx);
      rows.push(rowNode);
      this.gridApi.redrawRows({ rowNodes: rows });
    },
    getCompCdCombo() {
      this.$http
          .get(`/api/code/combo`, { params: { groupCd: "COMP_CD" } })
          .then(response => {
            // eslint-disable-next-line
            this.compCds = response.data;
          });

      this.form.compCd = this.$store.state.loginInfo.compCd;
      if (this.$store.state.loginInfo.authorities[0].authority === "ADMIN") {
        //ADMIN일경우 disabled 해제
        document.getElementById("bselect").removeAttribute("disabled");
      }
    },
    async getCombos() {
      let [bizTripArea] = await Promise.all([this.$http.get(`/api/code/combo`, {params: {groupCd: "EXPEND_MONEY_CD"}})]);
      this.combos['EXPEND_MONEY_CD'] = bizTripArea.data;
      this.createColumnDefs();
    },
    compareDate(value){
      if(!value){
        this.$swal({ type: 'info', text: '회계일자를 입력해주세요.' });
        return false;
      }
    },
    goOpen() {

      this.compareDate(this.searchDt);
      this.form.startDate = this.$moment(this.searchDt[0]).format("YYYYMMDD");
      this.form.endDate = this.$moment(this.searchDt[1]).format("YYYYMMDD");

      this.$store.commit('loading')
      this.$http
          .post("/api/expend/hr/list",this.form)
          .then(response => {
            this.data = response.data;
            this.excelData = response.data.forEach(x=>{
              x.expendCd = this.chkType(x.expendCd)
              x.expendAmt = this.checkNumber(x.expendAmt) ? x.expendAmt : 0;
            });
            this.closeModal();
          })
          .catch((err) => {
            console.log(err);
          }).finally(() => {
            this.$store.commit('finish')
          })
    },
    checkNumber(inputVal) {
      const inputValue = inputVal;
      let isNumeric = false;

      if (inputValue === '') {
        isNumeric = false;
      } else {
        const numericValue = parseInt(inputValue, 10);

        if (!isNaN(numericValue)) {
          isNumeric = true;
        } else {
          isNumeric = false;
        }
      }

      return isNumeric;
    },
    getDateFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD')
      }
    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    showDetailPop(slipNo, slipType, docMngNo) {
      let title = "";
      let setModal = undefined;
      switch (slipType) {
        case 'E12' :
          title = '구매전표'
          setModal = SlipDetailModal
          break;
        case 'E13' :
          title = '공사비전표'
          setModal = SlipDetailModal
          break;
        default:
          title = "전표";
          setModal = ApprovalModal;
          break;
      }
      this.$modal.open({
        component: setModal,
        parent: this,
        props: {
          slipNo : slipNo,
          docType: 'appr',
          title: title,
          readOnly : true,
        },
        width: 1200,
      });
    },
    popExpend(flag) {
      let vm = this
      this.$modal.open({
        component: Expend,
        props: {
          param: this.form.customerNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveExpend(obj)
          }
        }
      })
    },
    receiveExpend(obj) {
      this.form.expendCd = obj.detailCd;
      this.form.expendNm = obj.detailNm;
    },
    initExpend(force) {
      if (force === true) this.form.expendNm = '';
      if (this.form.expendNm === '') this.form.expendCd = '';
    },
    receiveEmp(obj) {
      this.form.empNo = obj.empNo;
      this.form.empNm = obj.empNm;
      this.$forceUpdate();
    },
    initEmp(force) {
      if (force === true) this.form.empNm = '';
      if (this.form.empNm === '') this.form.empNo = '';
    },
    popEmp(clear) {
      let vm = this
      this.$modal.open({
        component: Emp,
        props: {
          param: this.form.empNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveEmp(obj)
          }
        }
      })
    },
    popVendor1(clear) {
      let vm = this
      this.$modal.open({
        component: Vendor,
        props: {
          param: this.form.vendNm,
          slipTypeCd: this.slipTypeCd
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveVendor1(obj)
          }
        }
      })
    },
    initVendor1(force) {
      if(force===true) this.form.integrationVendorName = '';
      if(this.form.integrationVendorName === '') {
        this.form.integrationVendorNum = '';
      }
    },
    receiveVendor1(obj) {
      this.form.integrationVendorNum = obj.integrationVendorNum;
      this.form.integrationVendorName = obj.integrationVendorName;
      this.$forceUpdate();
    },
    updateExpendItem(pVal){

      const vm = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);

      this.$modal.open({
        component: Expend,
        parent: this,
        width: 700,
        props: {
          data: {
          }
        },
        events: {
          close(object) {
            rowNode.setDataValue('expendCd', object.detailCd);
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
    openModal() {
      document.getElementById("open-moda").style.opacity = "1";
      document.getElementById("open-moda").style.pointerEvents = "auto";
    },
    closeModal() {
      document.getElementById("open-moda").style.opacity = "0";
      document.getElementById("open-moda").style.pointerEvents = "none";
    },
    saveExcel() {
      this.gridOptions.api.exportDataAsExcel({fileName: 'HR 경조금신청 현황'});
    },
    resetSearch() {
      this.searchDt = [this.$moment().add(-1, 'month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')];
      this.form.giveUserId = "";
      this.form.giveUserNm = "";
      this.form.receiveUserId = "";
      this.form.receiveUserNm = "";
      this.form.delegateStatCd = "";
      this.form.startDate = this.$moment().startOf('month').format('YYYYMMDD');
      this.form.endDate = this.$moment().endOf('month').format('YYYYMMDD');
    },
  },
  created() {
    this.getCombos();
    queryMngItemCd.apply(this, []);
  },
  mounted() {
    this.getCompCdCombo();
    this.goOpen();
  }
};
</script>