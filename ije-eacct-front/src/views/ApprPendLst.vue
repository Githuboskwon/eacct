<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{ title }}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch()">조회</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="item search_wrap">
        <div class="search_box">
          <div class="search_title">
            <span class="search_tit" style="color: #CC3D3D;">- 상신일자</span>
          </div>
          <div class="search_con">
            <!--                        <div class="datepicker w-type-ymd02">-->
            <!--                            <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />-->
            <!--                        </div>-->
            <!--                        <span class="wave"> ~ </span>-->
            <!--                        <div class="datepicker w-type-ymd02">-->
            <!--                            <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />-->
            <!--                        </div>-->
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
                  <span class="search_tit">- 상신일자</span>
                </div>
                <div class="search_con search-area">
                  <!--                                    <div class="datepicker w-type-ymd02 w30p">-->
                  <!--                                        <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />-->
                  <!--                                    </div>-->
                  <!--                                    <span class="datepicker w10p dp-ib tac"> ~ </span>-->
                  <!--                                    <div class="datepicker w-type-ymd02 w30p">-->
                  <!--                                        <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />-->
                  <!--                                    </div>-->
                  <el-date-picker
                      v-model="searchDt"
                      type="daterange"
                      align="right"
                      unlink-panels
                      style="width: 70%;"
                      range-separator="~"
                      start-placeholder="시작일"
                      end-placeholder="종료일">
                  </el-date-picker>
                  <button @click="dateSetting('toDay')" class="search_bt_white_s">당일</button>
                  <button @click="dateSetting('crrntMnth')" class="search_bt_white_s">당월</button>
                  <button @click="dateSetting('PrvsMnth')" class="search_bt_white_s">전월</button>
                </div>
              </div>

              <!--                            <div class="search_box_pop">-->
              <!--                                <div class="search_title">-->
              <!--                                    <span class="search_tit">- 문서코드</span>-->
              <!--                                </div>-->
              <!--                                <div class="search_con search-area">-->
              <!--                                    <select class="select is-fullwidth w100p" v-model="form.docTypeCd">-->
              <!--                                      <option value="">==전체==</option>-->
              <!--                                      <option-->
              <!--                                        v-for="item in docTypes"-->
              <!--                                        :key="item.key"-->
              <!--                                        :value="item.key"-->
              <!--                                        v-text="item.value"-->
              <!--                                      />-->
              <!--                                    </select>-->
              <!--                                </div>-->
              <!--                            </div>-->

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 부서</span>
                </div>
                <div class="search_con search-area">
                  <input class="input dp-ib input-bg w30p_5r" type="text" v-model="form.cctrCd" disabled />
                  <input class="input dp-ib w70p" type="text" v-model="form.cctrNm" @input="initCctr" @keypress.enter="popCctr" />
                  <button class="icon is-right" @click="popCctr()"><i class="fas fa-search"></i></button>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 상신자</span>
                </div>
                <div class="search_con search-area">
                  <input class="input input-bg dp-ib w20p_5r" type="text" v-model="form.draftUserDut" disabled />
                  <input class="input input-bg dp-ib w30p_5r" type="text" v-model="form.draftUserDept" disabled />
                  <input class="input input-bg dp-ib w20p_5r" type="text" v-model="form.draftId" disabled />
                  <div class=" w30p ps_rl dp-ib">
                    <input class="input" type="text" v-model="form.draftNm" @input="initEmp('draft')" @keypress.enter="popEmp('draft')" />
                    <button class="icon is-right" @click="popEmp('draft')"><i class="fas fa-search"></i>
                    </button>
                  </div>
                  <!--                                    <div class=" dp-ib w20p">-->
                  <!--                                    <input type="checkbox" id="delegateChk" v-model="checked" /><label for="delegateChk" class="s_font">위임포함</label>-->
                  <!--                                    </div>-->
                </div>
              </div>
              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 결재자</span>
                </div>
                <div class="search_con search-area">
                  <input class="input input-bg dp-ib w20p_5r" type="text" v-model="form.aprverDut" disabled />
                  <input class="input input-bg dp-ib w30p_5r" type="text" v-model="form.aprverDept" disabled />
                  <input class="input input-bg dp-ib w20p_5r" type="text" v-model="form.aprverId" disabled />
                  <div class=" w30p ps_rl dp-ib">
                    <input class="input" type="text" v-model="form.aprverNm" @input="initEmp('aprver')" @keypress.enter="popEmp('aprver')" disabled/>
                    <button class="icon is-right"><i class="fas fa-search"></i>
                    </button>
                  </div>
                  <!--                                    <div class=" dp-ib w20p">-->
                  <!--                                    <input type="checkbox" id="delegateChk" v-model="checked" /><label for="delegateChk" class="s_font">위임포함</label>-->
                  <!--                                    </div>-->
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
                  <span class="search_tit">- 제목</span>
                </div>
                <div class="search_con search-area">
                  <input class="input Rt-M tal w100p" type="text" v-model="form.docTitleNm" />
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
            <h3 class="ico_table_name">결재할 목록</h3>
          </div>

          <div class="btn-type1 clearfix">
<!--            <div style="float: left; padding-top: 10px; margin-right: 10px">-->
<!--              총 금액 : {{ $numeral(totalAmt).format("0,0") }}-->
<!--            </div>-->
            <!--            <button class="btn-size btn-gray" @click="allChk">-->
            <!--              <span class="btn-icon"><i class="fas fa-check"></i></span>-->
            <!--              전체선택-->
            <!--            </button>-->
            <el-button type="primary" icon="el-icon-takeaway-box" @click="approvalBundle()">일괄결재</el-button>
            <el-button type="success" icon="el-icon-document-checked" @click="saveExcel()">엑셀 저장</el-button>
          </div>
        </div>
        <div class="grid-wrap">
          <ag-grid-vue
              ref="grid"
              style="width: 100%;"
              class="ag-theme-alpine grid_search_height"
              rowSelection="multiple"
              :columnDefs="columnDefs"
              :gridOptions="gridOptions"
              :defaultColDef="defaultColDef"
              :rowData="pendList"
              :frameworkComponents="frameworkComponents"

              :enableRangeSelection="true"
              :suppressMaxRenderedRowRestriction="true"
              :suppressColumnVirtualisation="true"
              :suppressRowVirtualisation="true"
              :suppressRowClickSelection="true"
              @grid-ready="onReady"

              @rowDoubleClicked="rowDoubleClick"
              @cell-value-changed="cellValueChange"
              @row-selected="onRowSelected"
          />
          <!-- <dhx-grid ref="grid" v-model="pendList" :config="config" @constructGridSuccessful="constructGridSuccessful"/> -->
        </div>
      </div>
    </div>
    <!-- //테이블 -->
  </div>
</template>

<script>
import mixin from "@/mixin";
import mixinSlip from "@/mixin/slip";
import _ from "lodash";


import Emp_Ag from "@/components/Emp_Ag.vue";
import Cctr_Ag from "@/components/Cctr_Ag.vue";
import { AgGridVue } from "ag-grid-vue";
import CheckboxCellRenderer from "@/components/agGrid/checkbox-cell-renderer";
import SlipDetailModal from "@/components/SlipDetailModal.vue";
import ApprovalModal from '@/components/accrualSlip/Approval/Main.vue';
import ApprBundlePop from "@/components/ApprBundlePop.vue";
import SlipBulkDetailModal from "@/components/SlipBulkDetailModal.vue";
import SlipGlDetailModal from "@/components/SlipGlDetailModal.vue";
import SlipFundDetailModal from "@/components/SlipFundDetailModal.vue";
import SlipCollectionDetailModal from "@/components/SlipCollectionDetailModal.vue";
import DraftPop from "@/components/costBudget/SlipBudgetDetailModal.vue";
import SlipBondDetailModal from "@/components/SlipBondDetailModal.vue";


export default {
  compatConfig: { MODE: 2 },
  name: "ApprPendLst",
  mixins: [mixin, mixinSlip],
  components: { AgGridVue },
  props: {
    params: {
      type: Object,
      required: false,
    },
    dateSet: {
      type: Object,
      required: false
    }
  },
  data() {
    return {
      columnDefs: [],
      gridApi: null,
      columnApi: null,
      gridOptions: {
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
      defaultColDef: {},
      title: "결재할 문서",
      docTypes: [],
      pendList: [],
      checked: "checked",
      showEmpModal: false,
      searchDt: [this.$moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss')],
      form: {
        docTypeCd: "",
        searchDtmFr: '',
        searchDtmTo: '',
        draftId : '',
        draftNm : '',
        draftUserDut : '',
        draftUserDept : '',
        aprverId : this.$store.state.loginInfo.loginId,
        aprverNm : this.$store.state.loginInfo.userName,
        aprverDut : this.$store.state.loginInfo.loginJobGradeNm,
        aprverDept : this.$store.state.loginInfo.loginDeptNm,
        delegateChk: "",
        docTitleNm: "",
        page: 0,
        cctrCd: "",
        cctrNm: "",
        slipNo: "",
      },
      syncGoPage: false,
      totalAmt: 0, //2020.06.04 전체금액 변수
      chkYn: false,
      frameworkComponents: null,
    };
  },
  methods: {
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    rowDoubleClick(params) {
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
            this.showDetailPop(slipNo, slipType, docMngNo, params);
          }
          break;
      }
    },
    showDetailPop(slipNo, slipType, docMngNo, params) {
      let vm = this;
      let title = "";
      let setModal = undefined;
      switch (slipType) {
        case '21' :
          title = '건별지급'
          setModal = SlipDetailModal
          break;
        case '22' :
          title = '대량지급'
          setModal = SlipBulkDetailModal
          break;
        case '23' :
          title = '전자채권'
          setModal = SlipBondDetailModal
          break;
        case '24' :
          title = '자금전표'
          setModal = SlipFundDetailModal
          break;
        case '25' :
          title = '집금전표'
          setModal = SlipCollectionDetailModal
          break;
        case '27' :
          title = 'GL전표'
          setModal = SlipGlDetailModal
          break;
        case '90' :
          title = '비용예산'
          setModal = DraftPop
          break;
        default:
          title = '전표'
          setModal = ApprovalModal;
          break;
      }
      this.$modal.open({
        component: setModal,
        parent: this,
        props: {
          slipNo : slipNo,
          docType: 'appr',
          slipType: slipType,
          slipHeaderId : params.data.apprGroupId,
          title: title,
          status: params.data.slipStatus,
        },
        width: slipType === '90' ? 1800 : 1200,
        events: {
          close(data) {
            vm.goSearch();

          }
        }
      });
    },
    makeColDef() {
      this.columnDefs = [
        {
          headerName: ''
          , headerCheckboxSelection: true
          , checkboxSelection: true
          , field:'regYn'
          , width : 70
          , cellStyle:{textAlign: 'center'}
          , editable: false
        },
        {
          headerName: "No.",
          width: 65,
          cellStyle: { textAlign: "center" },
          valueFormatter: (params) => {
            return params.node.rowIndex + 1;
          },
        },
        {
          headerName: "제목",
          field: "docTitleNm",
          // valueFormatter: (params) => {
          //   if(params.value === '제목'){
          //     params.value = "제목 - 머릿말("+params.data.headerRemark+")";
          //   }
          //   return params.value;
          // },
          width: 270,
          cellStyle: { textAlign: "left" },
        },
        {
          headerName: "거래유형",
          field: "slipTypeNm",
          width: 150,
          cellStyle: { textAlign: "left" },
        },
        {
          headerName: "결재상태",
          field: "slipStatusNm",
          width: 90,
          cellStyle: { textAlign: "center" },
        },
        {
          headerName: "문서번호",
          field: "slipNo",
          width: 180,
          cellStyle: { textAlign: "left" },
        },
        {
          headerName: "결재번호",
          field: "apprNo",
          width: 180,
          cellStyle: { textAlign: "left" },
        },
        {
          headerName: "공급가",
          field: "supplyAmount",
          width: 180,
          cellStyle: { textAlign: "right" },
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
          headerName: "부가세액",
          field: "taxAmount",
          width: 180,
          cellStyle: { textAlign: "right" },
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
          headerName: "총금액(KRW)",
          field: "usedAmt",
          width: 180,
          cellStyle: { textAlign: "right" },
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
          headerName: "부서",
          field: "draftDeptNm",
          width: 130,
          cellStyle: { textAlign: "left" },
        },
        {
          headerName: "기안자사번",
          field: "draftId",
          width: 110,
          cellStyle: { textAlign: "center" },
        },
        {
          headerName: "기안자",
          field: "draftNm",
          width: 90,
          cellStyle: { textAlign: "center" },
        },
        {
          headerName: "직급",
          field: "draftUserJob",
          width: 100,
          cellStyle: { textAlign: "center" },
        },
        {
          headerName: "기안일자",
          field: "draftDtm",
          width: 120,
          valueFormatter: (params) => {
            return this.getDateFormat(params.value)
          },
          cellStyle: { textAlign: "left" },
        },
        // {
        //   headerName: "타입",
        //   field: "slipTypeCd",
        //   width: 0,
        //   cellStyle: { textAlign: "left" },
        //   hide: true,
        // },
        // {
        //   headerName: "apprStage",
        //   field: "apprStage",
        //   width: 0,
        //   cellStyle: { textAlign: "left" },
        //   hide: true,
        // },
        // {
        //   headerName: "금액",
        //   field: "totAmt",
        //   width: 0,
        //   cellStyle: { textAlign: "left" },
        //   hide: true,
        // },
      ];
    },
    onGridReady() {
      this.gridOptions.api.sizeColumnsToFit();
    },
    cellValueChange(params) {
      if (params.data.regYn) {
        this.totalAmt = Number(this.totalAmt) + Number(params.data.totAmt);
      } else {
        this.totalAmt = Number(this.totalAmt) - Number(params.data.totAmt);
      }
    },
    allChk() {
      var filterList = [];

      this.gridOptions.api.forEachNodeAfterFilter((v, i) => {
        filterList.push(v.data.docMngNo)
      })

      this.chkYn = this.chkYn ? false : true;

      for(var i=0; i<filterList.length; i++){
        for(var j=0; j<this.pendList.length; j++){
          if(filterList[i] === this.pendList[j].docMngNo){
            this.pendList[j].regYn = this.chkYn;
            if (this.chkYn) {
              this.cellValueChange({ data: this.pendList[j] });
            }else{
              this.totalAmt = 0;
            }
          }
        }
      }

      var size = this.$refs.grid.$children.length;
      for(var k=0; k<size; k++){
        this.$refs.grid.$children[k].select = this.chkYn;
      }

    },
    getStatusTypeCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "SLIP_STAT_CD"}})
          .then(response => {
              this.docStatuses = response.data;
      });
    },
    saveExcel() {
      var params = {
        fileName : "결재할 문서"
      };
      this.gridOptions.api.exportDataAsCsv(params)
    },
    compareDate(value){
      if(!value){
        this.$swal({ type: 'info', text: '상신일자를 입력해주세요.' });
        return false;
      }
    },
    goSearch() {
      this.compareDate(this.searchDt);
      this.form.searchDtmFr = this.$moment(this.searchDt[0]).format("YYYYMMDD");
      this.form.searchDtmTo = this.$moment(this.searchDt[1]).format("YYYYMMDD");

      const param = JSON.parse(JSON.stringify(this.form));
      // const stripFields = ["searchDtmFr", "searchDtmTo"];
      // _.forEach(
      //   stripFields,
      //   (name) => (param[name] = this.toPure(param[name]))
      // );

      // if (param.searchDtmFr > param.searchDtmTo) {
      //   this.$swal({
      //     type: "warning",
      //     text: "조회 시작일자와 종료일자 지정이 잘못되었습니다.",
      //   });
      //   return false;
      // }

      let deleChk = "";
      if (this.checked) deleChk = "Y";

      this.$store.commit("loading");
      this.$http
          .post(`/api/appr/todo/list`, {
            docTypeCd: param.docTypeCd,
            searchDtmFr: this.toPure(param.searchDtmFr) === '0' ? '' : this.toPure(param.searchDtmFr),
            searchDtmTo: this.toPure(param.searchDtmTo) === '0' ? '' : this.toPure(param.searchDtmTo),
            draftId: param.draftUserId,
            acctCd: param.acctCd,
            delegateChk: deleChk,
            docTitleNm: param.docTitleNm,
            wrtDeptCd: this.form.cctrCd,
            slipNo : param.slipNo
          })
          .then((response) => {
            if (response.data) {
              this.pendList = response.data;
              this.totalAmt = 0;
              this.chkYn = false;
            }
          })
          .finally(() => {
            this.$store.commit("finish");
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
      this.searchDt = [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss')];
      this.form.docTypeCd = "";
      this.form.searchDtmFr = '';
      this.form.searchDtmTo = '';
      this.form.draftId = '';
      this.form.draftNm = '';
      this.form.draftUserDut = '';
      this.form.draftUserDept = '';
      this.form.aprverId = this.$store.state.loginInfo.loginId;
      this.form.aprverNm = this.$store.state.loginInfo.userName;
      this.form.aprverDut = this.$store.state.loginInfo.loginJobGradeNm;
      this.form.aprverDept = this.$store.state.loginInfo.loginDeptNm;
      this.form.delegateChk = "";
      this.form.docTitleNm = "";
      this.form.page = 0;
      this.form.cctrCd = "";
      this.form.cctrNm = "";
    },
    approvalBundle() {
      let that = this;

      //일괄승인 대기중
      let apprList = this.pendList.filter((x) => x.regYn);
      if (apprList.length === 0) {
        this.$swal({ type: "warning", text: "선택하신 전표정보가 없습니다." });
        return false;
        }
      this.$modal.open({
        component: ApprBundlePop,
        props: {
          apprList: apprList,
          docType: 'appr',
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
    },
    popEmp(type) {
      const that = this;
      this.$modal.open({
        width: 800,
        component: Emp_Ag,
        parent: this,
        props: {
          param: this.form.draftNm,
        },
        events: {
          close(data) {
            if(type === 'draft') {
              that.form.draftId = data.empNo;
              that.form.draftNm = data.empNm;
              that.form.draftUserDut = data.jobGradeNm;
              that.form.draftUserDept = data.deptNm;

            } else if(type === 'aprver') {
              that.form.aprverId = data.empNo;
              that.form.aprverNm = data.empNm;
              that.form.aprverDut = data.jobGradeNm;
              that.form.aprverDept = data.deptNm;

            }
          },
        },
      });
    },
    initEmp(type, force) {
      if(type === 'draft') {
        if (force === true) this.form.draftNm = "";
        if (this.form.draftNm === "") {
          this.form.draftId = "";
          this.form.draftUserDut = "";
          this.form.draftUserDept = "";
        }
      } else if(type === 'aprver') {
        if (force === true) this.form.aprverNm = "";
        if (this.form.aprverNm === "") {
          this.form.aprverId = "";
          this.form.aprverDut = "";
          this.form.aprverDept = "";
        }

      }
    
    },
    popCctr() {
      const that = this;

      this.$modal.open({
        width: 800,
        component: Cctr_Ag,
        parent: this,
        props: {
          param: this.form.cctrNm,
        },
        events: {
          close(data) {
            that.form.cctrCd = data.deptCd;
            that.form.cctrNm = data.deptNm;
          },
        },
      });
    },
    initCctr(force) {
      if (force === true) this.form.cctrNm = "";
      if (this.form.cctrNm === "") this.form.cctrCd = "";
    },
    dateSetting(str){
      switch (str) {
        case 'toDay':
          this.form.searchDtmFr = this.$moment().format('YYYYMMDD')
          this.form.searchTo = this.$moment().format('YYYYMMDD')
          this.searchDt =  [this.$moment().format('YYYY-MM-DD HH:mm:ss'),this.$moment().format('YYYY-MM-DD HH:mm:ss')];
          break;
        case 'crrntMnth':
          this.form.searchDtmFr = this.$moment().startOf('month').format('YYYYMMDD')
          this.form.searchDtmTo = this.$moment().endOf('month').format('YYYYMMDD')
          this.searchDt =  [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'),this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')]
          break;
        case 'PrvsMnth':
          this.form.searchDtmFr = this.$moment().add(-1, 'month').startOf('month').format('YYYYMMDD')
          this.form.searchDtmTo = this.$moment().add(-1, 'month').endOf('month').format('YYYYMMDD')
          this.searchDt =  [this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss'),this.$moment().add(-1, 'month').endOf('month').format('YYYY-MM-DD HH:mm:ss')]
          break;
      }
      // this.goSearch()
    },
    close(params) {
      console.log(params, '실행됨2')
      this.goSearch();
    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.pendList[rowIdx].regYn = params.node.isSelected();
    }
  },
  beforeMount() {
    const that = this;

    this.makeColDef();
    this.defaultColDef = { resizable: true, filter: true, sortable: true };
    this.frameworkComponents = {//그리드에서 사용할 외부 comp 등록
      checkboxRenderer: CheckboxCellRenderer,
    };
    this.gridOptions = {
      enableRangeSelection: true,
      statusBar: {
        statusPanels: [
          {
            statusPanel: 'agTotalAndFilteredRowCountComponent',
            align: 'left',
          },
        ]
      },
      onFilterChanged: () => {
        that.chkYn = 'N';
        that.totalAmt = 0;

        // for(var j=0; j<that.pendList.length; j++){
        //   that.pendList[j].regYn = that.chkYn
        // }

        // var size = that.$refs.grid.$children.length;
        // for(var k=0; k<size; k++){
        //   that.$refs.grid.$children[k].select = that.chkYn;
        // }

      }
    }

  },
  mounted() {
    document.title = this.title + ' - IJEAS';
    if(this.dateSet) {
      this.searchDt = [this.$moment(this.dateSet.startDate).format('YYYY-MM-DD HH:mm:ss'), this.$moment(this.dateSet.endDate).format('YYYY-MM-DD HH:mm:ss')]
    }
    this.getStatusTypeCombo();
    this.goSearch();
  },
  watch: {
    params: {
      immediate: true,
      deep: true,
      handler(value) {
        if (value && value.searchDtmFr) {
          this.form = value;
          if (this.form.page > 0) {
            this.syncGoPage = true;
          }
        }
      },
    },
  },
};
</script>

<style scoped>
.desc-content:after {
  clear: both;
  content: "";
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
  padding-left: 80px;
}
.datepicker.w-type-ymd02 {
  width: 114px;
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

.search-border .td-s-thumb.search-area .ip-box {
  vertical-align: top;
}

@media (max-width: 1580px) {
  .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
    width: 50%;
  }
}
</style>

