<template xmlns="http://www.w3.org/1999/html">
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="search">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save">저장</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="search_box">
      </div>


      <div class="search_box">
        <div class="search_title">
          <span class="search_tit">착공일 ~ 준공일</span>
        </div>
        <div class="search_con">
          <div class="td-s-thumb search-area" style="width: 300px; display: flex;">

            <div class="form-input">
              <el-date-picker
                  v-model="form.startDate"
                  type="month"
                  format="YYYY-MM"
                  value-format="YYYY-MM"
                  style="width: 80%;">
              </el-date-picker>
            </div>
            <span class="wave" style="position: absolute;left: 120px;"> ~ </span>
            <div class="form-input">
              <el-date-picker
                  v-model="form.endDate"
                  type="month"
                  format="YYYY-MM"
                  value-format="YYYY-MM"
                  style="width: 80%;">
              </el-date-picker>
            </div>

          </div>
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
                <span class="search_tit">착공일 ~ 준공일</span>
              </div>
              <div class="search_con search-area">
                <div class=" dp-ib w100p" style="display: flex;">
                  <div class="form-input">
                    <el-date-picker
                        v-model="form.startDate"
                        type="month"
                        format="YYYY-MM"
                        value-format="YYYY-MM"
                        style="width: 100%;">
                    </el-date-picker>
                  </div>
                  <span class="wave"> ~ </span>
                  <div class="form-input">
                    <el-date-picker
                        v-model="form.endDate"
                        type="month"
                        format="YYYY-MM"
                        value-format="YYYY-MM"
                        style="width: 100%;">
                    </el-date-picker>
                  </div>
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">&nbsp</span>
              </div>
              <div class="search_con search-a rea">
                <div class=" dp-ib w70p">
                  <input type="checkbox" id="inAmtPerFlag" v-model="form.inAmtPerFlag"/>
                  <label for="delegateChk" class="font">매출누계100%제외</label>
                </div>
              </div>
            </div>

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
                <span class="search_tit">PJT 구분</span>
              </div>
              <div class="search_con search-area">
                <select class="select is-fullwidth" id="use_yn" v-model="form.projectGubun">
                  <option value="">전체</option>
                  <option value="A">관납</option>
                  <option value="E">로컬</option>
                  <option value="B">민수</option>
                  <option value="C">해외</option>
                  <option value="D">지사</option>
                </select>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">PJT 명</span>
              </div>
              <div class="search_con search-area">
                <input class="input" type="text" v-model="form.PjtNm" >
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">검색시</span>
              </div>
              <span class="are">* PJT명 클릭 시 실행예산기안 화면으로 이동합니다.</span>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">&nbsp</span>
              </div>
              <span class="are">* 실행예산/실행률 클릭 시 예산계획 화면으로 이동합니다.</span>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">&nbsp</span>
              </div>
              <span class="are">* 수주금액 클릭 시 수주보고서 화면으로 이동합니다.</span>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">&nbsp</span>
              </div>
              <span class="are">* 매출금액 클릭 시 상세조회 화면으로 이동합니다.</span>
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
            <h3 class="ico_table_name">PJT진행현황표 내역</h3>
          </div>

          <div class="btn-wrap btn-type2 clearfix">
            <span>[단위 : 억원, %]</span>
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
                         :suppressRowTransform="true"
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
import { AgGridVue } from 'ag-grid-vue'
import SalesPop from "@/components/ims/ProfitSalesPop";

function rowSpan2(params) {
  return params.node.data.LINE === 1 || params.node.data.LINE === 3 ? 2 : 1;
}

function rowSpan3(params) {
  return params.node.data.LINE === 1 ? 3 :  1;
}

function rowSpan4(params) {
  return params.node.data.LINE === 1 ? 4 :  1;
}

function cellStyle(params){

  const stripLeftAligns = ['PROJECT_GUBUN', 'ISSUE' , 'ISSUE_MEASURE' , 'ISSUE_CHANGE_DATE' , 'ISSUE_CHANGE_USER_ID' , 'REMARK'];

  const stripCenterAligns = ['CT_BD', 'PLAN_ACT'];

  const stripLinked = ['PJT_LINE'];

  if(stripLeftAligns.includes(params.colDef.field)){
    return params.node.data.LINE === 1 ? {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'left'} :''
  } else if(stripCenterAligns.includes(params.colDef.field)){
    return params.node.data.LINE === 1 || params.node.data.LINE === 3? {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'center'} :''
  } else if(stripLinked.includes(params.colDef.field)){
    return params.node.data.LINE === 1 || params.node.data.LINE === 3? {backgroundColor: '#FFFFFF', 'text-decoration': 'underline' , 'text-underline-position': 'under' , color:'blue' , cursor:'pointer', borderBottomColor: '#DDE2EB', textAlign : 'center'} :''
  }else {
    return {backgroundColor: '#FFFFFF', borderBottomColor: '#DDE2EB', textAlign : 'right'}
  }
}

export default {
  name: 'ProjectStaTabRep',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, CctrAg, ProjectPop
  },
  data() {
    return {
      title: "PJT진행현황표",
      columnDefs : [],
      gridOptions : {},
      defaultColDef: {
        resizable: true,
        filter: false,
        sortable: false,
      },
      gridApi : null ,    //gridApi
      columnApi : null ,  //columApi
      num : 0,
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
        projectManageNm: '',
        startDate: this.$moment().startOf('year').format('YYYY-MM'),
        endDate: this.$moment().endOf('year').format('YYYY-MM'),
        countryCd: '',
        countryNm: '',
        inAmtPerFlag : true,
      },
      frameworkComponents: {
        checkboxRenderer: CheckboxCellRenderer,
        selectRenderer: SelectCellRenderer,
        numberInputRenderer: NumberInputCellRenderer,
        searchButtonRenderer: AgGridSearchBtn,
        datePicker: DatepickerCellRenderer,
      },
      combos: [],
      initialColumnDefs : [
        {headerName: '구분', field: 'PROJECT_GUBUN', width: 150, rowSpan: rowSpan4 , cellStyle: cellStyle},
        {headerName: 'PJT 명/선종', field: 'PJT_LINE', width: 150, rowSpan: rowSpan3 , cellStyle: cellStyle},
        {headerName: 'PJT 정보', field: 'PJT_INFO', width: 150,},
        {
          headerName: '수주/매출금액',
          field: 'PJT_REV',
          width: 150,
          cellStyle: (params) => {
            let value = {textAlign: 'right' , 'text-decoration': 'underline' , 'text-underline-position': 'under' , color:'blue' , cursor:'pointer'};
            if(params.data.LINE == 1 || params.data.LINE == 3){
              value = {textAlign: 'left', color : '#a6a6a6'};
            }
            return value;
          }
        },
        {headerName: '수주원가/실행예산',field: 'PJT_REV2',width: 150, cellStyle: (params) => {
            let value = {textAlign: 'right'};
            if(params.data.LINE == 1){
              value = {textAlign: 'right' , 'text-decoration': 'underline' , 'text-underline-position': 'under' , color:'blue' , cursor:'pointer'};
            }
            return value;
          }},
        {headerName: '공정&예산',field: 'CT_BD',width: 150,rowSpan: rowSpan2 , cellStyle: cellStyle},
        {headerName: '계획&실적',field: 'PLAN_ACT',width: 150,cellStyle:{textAlign: 'center'}}
      ],
      monthListColumns: [] , // 여기서 초기화
      extraColumns : [
        {headerName: '이슈사항', field: 'ISSUE', width: 150, rowSpan: rowSpan4 , cellStyle: cellStyle , editable: true},
        {headerName: '대책', field: 'ISSUE_MEASURE', width: 150, rowSpan: rowSpan4 , cellStyle: cellStyle, editable: true},
        {headerName: '최종수정일', field: 'ISSUE_CHANGE_DATE', width: 150, rowSpan: rowSpan4 , cellStyle: cellStyle },
        {headerName: '최종수정자', field: 'ISSUE_CHANGE_USER_ID', width: 150, rowSpan: rowSpan4 , cellStyle: cellStyle},
        {headerName: '비고', field: 'REMARK', width: 150, rowSpan: rowSpan4 , cellStyle: cellStyle}
      ],
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
    getDateRange(startDate, endDate){
      const start = new Date(startDate);
      const end = new Date(endDate);

      const result = [];

      while (start <= end) {
        result.push(this.$moment(start.toISOString().split('T')[0]).format('YYYYMM'));
        start.setMonth(start.getMonth() + 1);
      }

      return result
    },
    createColumnDefs() {


      const s_yyyy = (this.$moment(this.form.startDate).format('YYYYMM') + "").substring(0,4);
      const e_yyyy = (this.$moment(this.form.endDate).format('YYYYMM') + "").substring(0,4);
      const s_month = (this.$moment(this.form.startDate).format('YYYYMM') + "").substring(4,6);
      const e_month = (this.$moment(this.form.endDate).format('YYYYMM') + "").substring(4,6);
      const c_month = (e_yyyy - s_yyyy) * 12 + (e_month - s_month) + 1;
      const total_month = c_month;
      let lineAamt = null;

      this.columnDefs = [];

      const vm = this;

      this.form.monthList = this.getDateRange(this.form.startDate, this.form.endDate);

      //1...this.initialColumnDefs,  //1번째 세팅
      const oneColumn = new Promise((resolve, reject) => {
        if(this.initialColumnDefs) {
          resolve(this.initialColumnDefs)
        } else {
          reject([])
        }
      });
      //2...this.monthListColumns,   //1번재 세팅 끝난 이후 2번째 세팅
      const twoColumn = new Promise((resolve, reject) => {
        const result = this.form.monthList;
        if(result) {
          resolve(result)
        } else {
          reject([])
        }
      });

      //3...this.extraColumns        //2번째 세팅이 끝난 이후 3번째 세팅
      const threeColumn = new Promise((resolve, reject) => {
        if(this.extraColumns) {
          resolve(this.extraColumns);
        } else {
          reject([])
        }
      });

      oneColumn.then(x => {
        this.columnDefs.push(...x);
      }).finally(_ => {
        twoColumn.then(x => {
          x.map( data => {
            const column = {
              headerName: data,
              field: data + "_CT_PLAN",
              width: 100,
              cellStyle: (params) => {
                let value = {textAlign: 'right'};
                if(params.data.LINE === 2){
                  const vb1 =  params.data[data+"_CT_ACTUAL"]; // 실적
                  const vb2 =  params.data[data+"_CT_PLAN"]; // 계획

                  if((vb1 - vb2) < 0 || (vb2 - vb1) > 10 ){
                    value = {textAlign: 'right', color : 'red'};
                  }
                }else
                if(params.data.LINE === 4){
                  const vd1 = params.data[data+"_BUDGET_ACTUAL"]; // 실적
                  const vd2 = params.data[data+"_BUDGET_PLAN"]; // 계획

                  if((vd1 - vd2) > 5){
                    value = {textAlign: 'right', color : 'red'};
                  }
                }
                return value;
              },
              valueFormatter: (params) => vm.getNumberFormat(vm.getMonthInfo(params, data))
            };
            this.columnDefs.push(column);
          });
        }).finally(_ => {
          threeColumn.then(x => {
            this.columnDefs.push(...x);
          });
        })
      });
    },
    search() {

      if(this.isEmpty(this.form.startDate) === ""){
        this.$swal({type: "error",text: "착공일을 설정해 주세요."});
        return false;
      }else
      if(this.isEmpty(this.form.endDate) === ""){
        this.$swal({type: "error",text: "준공일을 설정해 주세요."});
        return false;
      }

      this.createColumnDefs();

      const param = {
        projectName : this.form.projectNm,
        projectGubun : this.form.projectGubun,
        countryCode : this.form.countryCd,
        countryName : this.form.countryNm,
        startDate : this.form.startDate,
        endDate : this.form.endDate,
        inAmtPerFlag : this.form.inAmtPerFlag ? 'Y' : 'N',
        param : this.form.monthList
      };

      this.$store.commit('loading')
      this.$http.post('/api/pjt/progress/list', param)
          .then(response => {
            this.data = response.data;
            if(response.data.length == 0){
              this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
            }
          })
          .finally(() => {
            this.$store.commit('finish')
          })
    },
    save() {
      const params = [];
      this.data.forEach(x=>{
        if(x.LINE == 1) {
          params.push({
            projectManageNo : x.PROJECT_MANAGE_NO,
            issue : x.ISSUE,
            issueMeasure: x.ISSUE_MEASURE,
            weeklyIssue: x.WEEKLY_ISSUE
          });
        }
      });

      this.$swal({
        type: "info",
        text: `변경 내역을 저장 하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: "예",
        cancelButtonText: "아니오"
      }).then(result => {
        if (result.value) {
          this.$store.commit('loading');
          this.$http.post('/api/pjt/progress/save', params)
              .then(() => {
                this.$swal({type: 'success', text: '저장되었습니다.'})
                this.search();
              })
              .catch(error => {
                this.$swal({
                  type:'error', text: error.data.message,
                })
              })
              .finally(() => {
                this.$store.commit('finish');
              })
        }
      });

    },
    newPage() {
      this.$swal({
        type: "info",
        text: `변경된 데이터가 있습니다. 계속 하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: "예",
        cancelButtonText: "아니오"
      }).then(result => {
        if (result.value) {
          this.goOpen();
        }
      });
    },
    getNumberFormat(val) {
      if(val) {
        return this.$numeral(val).format('0.0');
      }else if(this.isEmpty(val) === ""){
        return '0.0';
      }
    },
    isEmpty(value){
      if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
        return ""
      }else{
        return value
      }
    },
    getMonthInfo(params,fieldNm){

      let value = params.value

      if(params.data.LINE === 1){
        value = params.data[fieldNm+"_CT_PLAN"];
      }else
      if(params.data.LINE === 2){
        value = params.data[fieldNm+"_CT_ACTUAL"];
      }else
      if(params.data.LINE === 3){
        value = params.data[fieldNm+"_BUDGET_PLAN"];
      }else
      if(params.data.LINE === 4){
        value = params.data[fieldNm+"_BUDGET_ACTUAL"];
      }

      return value;
    },
    onCellClicked(params) {
      const field = params.colDef.field;
      const data = params.data;
      const that = this;

      if(field === 'PJT_LINE' && data.LINE !== 4) {

        this.$swal({ type: 'warning', text: '예산 기안 기능은 개발중입니다.' })
        return false;

      }else
      if(field === 'PJT_REV' && data.LINE == 4) {

        this.$modal.open({
          component: SalesPop,
          props: {
            projectNumber: data.PROJECT_CODE
          },
          parent: this,
          width: 1400,
          events: {
            close() {

            }
          }
        });

      }else
      if(field === 'PJT_REV' && data.LINE == 2) {

        const url = 'http://sfa.iljin.co.kr/poaacct/sfa/contract/contract_success_report_mgt.jsp?em_no=' + data.PROJECT_CODE + '&seq=2'+ '&doc_status=app_end' + '&acctFlag=' + true;
        this.objectPopup = window.open(url, '_blank', 'scrollbars=yes,toolbar=0,location=0,menubar=0,resizable=yes');

      }else
      if(field === 'PJT_REV2' && data.LINE == 1) {

        let param = "";
        param += "?project_number=" + data.PROJECT_CODE;
        param += "&version="+ data.VERSION;
        param += "&user_id=" + data.ADD_USER_ID;
        param += "&emrq_item_code=H1";
        param += "&doc_title=H1_C";
        param += "&cost_type=CONT";
        param += "&doc_status=app_end";
        param += "&acctFlag=true";

        const url = "http://sfa.iljin.co.kr/poaacct/sfa/contract/calc_page/calculation_ce_hv_mgt.jsp" + param;
        this.objectPopup = window.open(url,"calculation_ce_hv_mgt","scrollbars=yes,toolbar=0,location=0,menubar=0,resizable=yes");

      }

    },
    addRow() {
      this.data.push({
        regNo:'N',
        compCd: this.$store.state.loginInfo.compCd,
        refNo: '',
        openingDt: '',
        projectId: '',
        projectNm: '',
        releaseDt:'',
        new: true,
      })
    },
    removeRow() {
      let vm = this;
      let list = this.data.filter(v => v.regYn)
      vm.result = [];
      if(list.length > 0){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            vm.result = list;
            this.$http.post(`/api/Project/delete`, vm.result)
                .then((response) => {
                  this.$swal({ type: 'success', text: '삭제되었습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.search();
                        }
                      });
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
        this.$swal({ type: 'info', text: '삭제할 항목을 선택하세요.' });
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
    resetSearch() {
      this.form.compCd = '';
      this.form.projectCd = '';
      this.form.projectNm = '';
      this.form.projectGubun = '';
      this.form.projectManageNm = '';
      this.form.startDate = this.$moment().startOf('year').format('YYYY-MM');
      this.form.endDate = this.$moment().endOf('year').format('YYYY-MM');
      this.form.countryCd = '';
      this.form.countryNm = '';
      this.form.inAmtPerFlag  = true;
    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    popCctrAg() {
      let vm = this
      this.$modal.open({
        component: CctrAg,
        props: {
          param: this.form.CctrAg
        },
        parent: this,
        events: {
          close(obj) {
            vm.getCctrAg(obj);
          }
        }
      })
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
