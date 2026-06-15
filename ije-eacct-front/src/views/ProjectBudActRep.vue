<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="search">조회</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">

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

<!--            <div class="search_box_pop">-->
<!--              <div class="search_title">-->
<!--                <span class="search_tit">- 착공일 ~ 준공일</span>-->
<!--              </div>-->
<!--              <div class="search_con search-area">-->
<!--                <div class="form-input">-->
<!--                  <el-date-picker-->
<!--                      v-model="openingDt"-->
<!--                      type="daterange"-->
<!--                      align="right"-->
<!--                      unlink-panels-->
<!--                      range-separator="~"-->
<!--                      start-placeholder="착공일"-->
<!--                      end-placeholder="준공일">-->
<!--                  </el-date-picker>-->
<!--                </div>-->
<!--              </div>-->
<!--            </div>-->

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">국가코드</span>
              </div>
              <div class="search_con search-area">
                <input class="input" type="text" v-model="form.countryCd" >
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">국가</span>
              </div>
              <div class="search_con search-area">
                <input class="input" type="text" v-model="form.countryNm" >
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">공정률100%제외</span>
              </div>
              <div class="search_con search-a rea">
                <div class=" dp-ib w70p">
                  <input type="checkbox" id="inAmtPerFlag" v-model="form.inAmtPerFlag"/>
                  <label for="delegateChk" class="font">&nbsp;</label>
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">PJT 구분</span>
              </div>
              <div class="search_con search-area">
                <b-select class="select is-fullwidth" id="use_yn" v-model="form.projectGubun">
                  <option value="">전체</option>
                  <option value="A">관납</option>
                  <option value="E">로컬</option>
                  <option value="B">민수</option>
                  <option value="C">해외</option>
                  <option value="D">지사</option>
                </b-select>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">PJT 명</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.projectCd" disabled>
                <input class="input input-bg w30p_5r" type="text" v-show="false" v-model="form.projectNm" >
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.projectNm" @input="initPro" @keypress.enter="popProject">
                  <button class="icon is-right" @click="popProject(true)"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">PJT 관리번호</span>
              </div>
              <div class="search_con search-area">
                <div class="dp-ib w100p">
                  <input class="input" type="text" v-model="form.projectManageNo" @keypress.enter="popProjectMng">
                  <button class="icon is-right" @click="popProjectMng(true)"><i class="fas fa-search"></i>
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
            <h3 class="ico_table_name">PJT실적분석 내역</h3>
          </div>
        </div>

        <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height_350"
                         rowSelection="multiple"
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef"
                         :enableRangeSelection="true"
                         :suppressRowClickSelection="true"
                         @cell-clicked="onCellClicked"
                         @grid-ready="onReady"
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
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
import NumberInputCellRenderer from '@/components/agGrid/numberinput-cell-renderer'
import DatepickerCellRenderer from '@/components/agGrid/datepicker-cell-renderer'
import AgGridSearchBtn from '@/components/agGrid/AgGridSearchBtn';
import CctrAg from '@/components/Cctr_Ag.vue';
import ProjectPop from '@/components/ProjectPop.vue';
import ProjectMngPop from '@/components/ims/ProjectMngPop.vue';
import { AgGridVue } from 'ag-grid-vue'
import SalesPop from "@/components/ims/ProfitSalesPop";
import ClaimPop from "@/components/ims/ClaimPop";
import CollectionPop from "@/components/ims/CollectionPop";
import TotalAmtPop from "@/components/ims/TotalAmtPop";

export default {
  name: 'ProjectBudActRep',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, CctrAg, ProjectPop , ProjectMngPop
  },
  data() {
    return {
      title: "PJT실적분석",
      columnDefs : [],
      gridOptions : {},
      defaultColDef: {
        resizable: true,
        filter: false,
        sortable: false,
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
        compCd: '',
        projectCd: '',
        projectNm: '',
        projectGubun: '',
        projectManageNo: '',
        projectManageNm: '',
        openingDtFrom: this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'),
        openingDtTo: this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss'),
        countryCd: '',
        countryNm: '',
        inAmtPerFlag : true,
      },
      openingDt: [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')],
      frameworkComponents: {
        checkboxRenderer: CheckboxCellRenderer,
        selectRenderer: SelectCellRenderer,
        numberInputRenderer: NumberInputCellRenderer,
        searchButtonRenderer: AgGridSearchBtn,
        datePicker: DatepickerCellRenderer,
      },
      combos: [],
    }
  },
  created() {
    this.getCombos();
    // this.search();
  },
  methods: {
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    async getCombos() {
      let [curCd] = await Promise.all([this.$http.get(`/api/code/combo`, {params: {groupCd: "CUR_CD"}})]);
      this.combos['CUR_CD'] = curCd.data;
      this.createColumnDefs();
    },
    onCellClicked(params) {
      //const idx = params.rowIndex;
      const field = params.colDef.field;
      const that = this;

      if(field === 'salesPer' || field === 'revenueAmount') {

        this.$modal.open({
          component: SalesPop,
          props: {
            projectNumber: params.data.projectCode
          },
          parent: this,
          width: 1400,
          events: {
            close() {

            }
          }
        });
      }else
      if(field === 'claimPer') {

        this.$modal.open({
          component: ClaimPop,
          props: {
            projectNumber: params.data.projectCode
          },
          parent: this,
          width: 1400,
          events: {
            close() {

            }
          }
        });
      }else
      if(field === 'collectionPer') {

        this.$modal.open({
          component: CollectionPop,
          props: {
            projectNumber: params.data.projectCode
          },
          parent: this,
          width: 1400,
          events: {
            close() {

            }
          }
        });
      }else
      if(field === 'resultAmt') {

        this.$modal.open({
          component: TotalAmtPop,
          props: {
            projectNumber: params.data.projectCode,
            degree : params.data.degree
          },
          parent: this,
          width: 1400,
          events: {
            close() {

            }
          }
        });
      }else
      if(field === 'ctAmount') {

        const row = params.data;

        const url = 'http://sfa.iljin.co.kr/poaacct/sfa/contract/contract_success_report_mgt.jsp?em_no=' + row.projectCode + '&degree='+ row.degree + '&seq=2'+ '&doc_status=app_end' + '&acctFlag=' + true;
        this.objectPopup = window.open(url, '_blank', 'scrollbars=yes,toolbar=0,location=0,menubar=0,resizable=yes');

      }else
      if(field === 'ctAmountFor') {
        const row = params.data;

        let param = "";
        param += "?project_number=" + row.projectCode;
        param += "&version="+ row.version;
        param += "&user_id=" + row.addUserId;
        param += "&emrq_item_code=H1";
        param += "&doc_title=H1_C";
        param += "&cost_type=CONT";
        param += "&doc_status=app_end";
        param += "&acctFlag=true";

        const url = "http://sfa.iljin.co.kr/poaacct/sfa/contract/calc_page/calculation_ce_hv_mgt.jsp" + param;
        this.objectPopup = window.open(url,"calculation_ce_hv_mgt","scrollbars=yes,toolbar=0,location=0,menubar=0,resizable=yes");

      }else
      if(field === 'planAmt') {

        this.$swal({ type: 'warning', text: '예산 기안 기능은 개발중입니다.' })
        return false;

      }



    },
    createColumnDefs(){
      const vm = this;

      this.columnDefs = [
        {headerName: 'PJT 관리번호', field: 'projectManageNo', width: 150},
        {
          headerName: 'PJT명',
          field: 'projectName',
          width: 180,
        },
        {
          headerName: '진행률(%)',
          field: '',
          children:[
            {headerName: '매출', field: 'salesPer', width: 90,cellStyle:{textAlign: 'right'},
              cellRenderer: (params) => {
                return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${params.value}</div>`;
              }},
            {headerName: '공정', field: 'processPer', width: 90,cellStyle:{textAlign: 'right'}},
            {headerName: '청구', field: 'claimPer', width: 90,cellStyle:{textAlign: 'right'},
              cellRenderer: (params) => {
                return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${params.value}</div>`;
              }},
            {headerName: '수금', field: 'collectionPer', width: 90,cellStyle:{textAlign: 'right'},
              cellRenderer: (params) => {
                return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${params.value}</div>`;
              }}
          ]
        },
        {
          headerName: '계획(착공)',
          field: '',
          children:[
            {headerName: '수주금액', field: 'ctAmount', width: 140,
              cellStyle:{textAlign: 'right'},
              cellRenderer: (params) => {
                const value = Math.floor(params.value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${value}</div>`;
              }},
            {headerName: '수주원가', field: 'ctAmountFor', width: 140,
              cellStyle:{textAlign: 'right'},
              cellRenderer: (params) => {
                const value = Math.floor(params.value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${value}</div>`;
              }},
            {headerName: '실행예산', field: 'planAmt', width: 140,
              cellStyle:{textAlign: 'right'},
              cellRenderer: (params) => {
                const value = Math.floor(params.value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${value}</div>`;
              }},
            {headerName: '실행률', field: 'planAmtPer', width: 90,
              cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return params.value+"%";
              }},
            {headerName: '매출총이익', field: 'totalSalesProfitPlan', width: 140,
              cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              }},
          ]
        },
        {
          headerName: '실적(준공)',
          field: '',
          children:[
            {headerName: '매출', field: 'revenueAmount', width: 140,
              cellStyle:{textAlign: 'right'},
              cellRenderer: (params) => {
                const value = Math.floor(params.value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${value}</div>`;
              }},
            {headerName: '총비용', field: 'resultAmt', width: 140,
              cellStyle:{textAlign: 'right'},
              cellRenderer: (params) => {
                const value = Math.floor(params.value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                return `<div style="text-decoration: underline; text-underline-position: under; color:blue; cursor:pointer;">${value}</div>`;
              }},
            {headerName: '총비용검증', field: 'ftam', width: 140,
              cellStyle:{textAlign: 'right'},
              cellClass: function(params) {
                if(params.data.resultAmt === params.value) {
                  return '';
                } else {
                  return 'bg-pink';
                }
              },
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              }},
            {headerName: '실행률', field: 'resultAmtPer', width: 90,
              cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return params.value+"%";
              }},
            {headerName: '매출총이익', field: 'totalSalesProfitBudget', width: 140,
              cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              }},
          ]
        },
        {
          headerName: '차이',
          field: '',
          children:[
            {headerName: '매출', field: 'revenueAmount1', width: 140,
              cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              }},
            {headerName: '총비용', field: 'resultAmt1', width: 140,
              cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              }},
            {headerName: '실행률', field: 'resultAmtPer1', width: 90,
              cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return params.value+"%";
              }},
            {headerName: '매출총이익', field: 'totalSalesProfitBudget1', width: 140,
              cellStyle:{textAlign: 'right'},
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              }},
          ]
        },
        {
          headerName: '프로젝트코드',
          field: 'projectCode',
          hide:true
        },
        {
          headerName: '등록자',
          field: 'addUserId',
          hide:true
        },
      ]
    },
    search() {
      this.$store.commit('loading')

      const param = {
        projectName : this.form.projectNm,
        projectGubun : this.form.projectGubun,
        countryCode : this.form.countryCd,
        countryName : this.form.countryNm,
        projectManageNo : this.form.projectManageNo,
        inAmtPerFlag : this.form.inAmtPerFlag ? 'Y' : 'N'
      }

      this.$http.post('/api/pjt/perfAnalysis/list', param)
          .then(response => {
            this.data = response.data;
            this.closeModal();
            if(response.data.length == 0){
              this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
            }
          })
          .finally(() => {
            this.$store.commit('finish')
          })
    },
    reset() {
      this.search();
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
    resetSearch() {
      this.form.projectCd = '';
      this.form.projectNm = '';
      this.form.projectGubun = '';
      this.form.projectManageNo = '';
      this.form.projectManageNm = '';
      this.form.openingDtFrom = this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss');
      this.form.openingDtTo = this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss');
      this.form.countryCd = '';
      this.form.countryNm = '';
      this.form.inAmtPerFlag = true;
      this.openingDt = [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')];
    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    popProject(){
      const vm  = this;

      this.$modal.open({
        width: 800,
        component: ProjectPop,
        parent: this,
        props: {
          param: this.form.projectNm
        },
        events: {
          close(data) {
            vm.receivePro(data)
            vm.$forceUpdate();
          },
        }
      })
    },
    initPro(force) {
      if (force === true) this.form.projectNm = '';
      if (this.form.projectNm === '') {
        this.form.projectCd = '';
        this.form.projectNm = '';
      }
    },
    receivePro(data) {
      this.form.projectCd = data.projectCd;
      this.form.projectNm = data.projectNm;
    },
    popProjectMng(){
      const vm  = this;

      this.$modal.open({
        width: 800,
        component: ProjectMngPop,
        parent: this,
        props: {
          param: this.form.projectManageNo
        },
        events: {
          close(data) {
            vm.receiveProMng(data)
            vm.$forceUpdate();
          },
        }
      })
    },
    receiveProMng(data) {
      this.form.projectManageNo = data.projectManageNo;
    },
    getCctrAg(obj) {
      this.form.CctrAg = obj.deptNm;
      this.form.CctrAgCd = obj.deptCd;
    },
    updateCctrAg() {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);

      this.$modal.open({
        component: CctrAg,
        parent: this,
        width: 700,
        props: {
          param: this.form.CctrAg
        },
        events: {
          close(obj) {
            vm.data[rowId].CctrAg = obj.deptNm;
            vm.data[rowId].CctrAgCd = obj.deptCd;
            rowNode.setDataValue('CctrAg', obj.deptNm);
          }
        }
      })
    },
    updateCctrAgVal(pVal) {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);
      if(pVal) {
        this.$http.get(`/api/Cctr_Ag/list/${pVal}`)
            .then(response => {
              if(response.data.length === 1) {
                vm.data[rowId].CctrAg = response.data[0].deptNm;
                vm.data[rowId].CctrAgCd = response.data[0].deptCd;
                rowNode.setDataValue('CctrAg', response.data[0].deptNm);
              } else {
                this.$modal.open({
                  component: Cctr_Ag,
                  parent: this,
                  width: 700,
                  props: {
                    param: pVal,
                  },
                  events: {
                    close(obj) {
                      vm.data[rowId].CctrAg = obj.deptNm;
                      vm.data[rowId].CctrAgCd = obj.deptCd;
                      rowNode.setDataValue('CctrAg', obj.deptNm);
                    }
                  }
                })
              }
            })
      } else {
        vm.data[rowId].CctrAg = '';
        vm.data[rowId].CctrAgCd = '';
        rowNode.setDataValue('CctrAg', '');
      }
    },

    initCctrAg(force) {
      if (force === true) this.form.CctrAg = '';
      if (this.form.CctrAg === '') {
        this.form.CctrAg = '';
        this.form.CctrAgCd = '';
      }
    },
    cellValueChange(params){
      const vm = this;
      const rowNode = params.api.getRowNode(params.node.rowIndex);
    },
  },
}
</script>
