<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">

        <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch()">조회</el-button>
        <el-button size="large" type="info" icon="el-icon-tickets" @click="openPerformerChkPop">실적조회</el-button>
        <el-button size="large" type="info" icon="el-icon-tickets" @click="openYearPlan">연간사업계획</el-button>
        <el-button size="large" type="info" icon="el-icon-tickets" @click="openAcctTypeSum">요약</el-button>
        <el-button size="large" type="success" icon="el-icon-check" @click="saveApproval()">예산확정</el-button>
        <el-button size="large" type="danger" icon="el-icon-close" @click="clearApproval()">예산확정해제</el-button>
        <el-button size="large" type="primary" icon="el-icon-takeaway-box" @click="openDraft()">기안</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save()">저장</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="item search_wrap">


        <div class="search_box">
          <div class="search_title">
            <span class="search_tit">회계연월</span>
          </div>
          <div class="search_con">
            <div class="td-s-thumb search-area" style="width: 200px">
              <div class="form-input">
                <el-date-picker
                    v-model="form.periodYm"
                    type="month"
                    format="YYYY-MM"
                    value-format="YYYYMM"
                    style="width: 80%;">
                </el-date-picker>
              </div>
            </div>
          </div>
        </div>


        <div class="search_box">
          <div class="search_title">
            <span class="search_tit">예산부서</span>
          </div>
          <div class="search_con">
            <div class="td-s-thumb search-area" style="width: 300px">
              <input class="input input-bg" type="text" style="width:100px; text-align: center" v-model="form.deptCd" disabled>
              <input class="input input-bg" type="text" style="width:180px; margin-left: 5px;" v-model="form.deptNm" disabled>
            </div>
          </div>
        </div>



        <div class="search_box">
          <div class="search_title">
            <span class="search_tit">위임자</span>
          </div>
          <div class="search_con">
            <div class="td-s-thumb search-area" style="width: 300px">
              <div class="form-input">
                <select class="select is-fullwidth" v-model="form.delegateNo">

                  <option v-if="this.$store.state.loginInfo.authorities[0].roleCd ==='ADMIN'" value='all'>==전체==</option>

                  <option :value='this.$store.state.loginInfo.loginId'>{{this.$store.state.loginInfo.userName}}</option>
                  <option
                      v-for="item in delegateList"
                      :key="item.giveUserId"
                      :value="item.giveUserId"
                      v-text="item.giveUserNm"/>
                  />
                </select>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
    <!-- //검색조건 영역 -->

    <el-form size="mini" :model="form"  class="components">

      <el-row>
        <el-form-item style="color: red; padding: 15px 1% 0px 1%; border: 2px solid #9db6c9;">

          <el-row>
            <el-col>
              <el-col :span="1">
                <el-form-item label="구분" style="text-align: center;">
                  <el-input
                      size="medium"
                      placeholder="실적저장"
                      readonly>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="1월" style="text-align: center;">
                  <el-checkbox v-model="savePerformance.budget01ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="2월">
                  <el-checkbox v-model="savePerformance.budget02ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="3월">
                  <el-checkbox v-model="savePerformance.budget03ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="4월">
                  <el-checkbox v-model="savePerformance.budget04ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="5월">
                  <el-checkbox v-model="savePerformance.budget05ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="6월">
                  <el-checkbox v-model="savePerformance.budget06ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="7월">
                  <el-checkbox v-model="savePerformance.budget07ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="8월">
                  <el-checkbox v-model="savePerformance.budget08ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="9월">
                  <el-checkbox v-model="savePerformance.budget09ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="10월">
                  <el-checkbox v-model="savePerformance.budget10ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="11월">
                  <el-checkbox v-model="savePerformance.budget11ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item label="12월">
                  <el-checkbox v-model="savePerformance.budget12ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
            </el-col>
          </el-row>

          <el-row style="top: -19px;">
            <el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-input
                      size="medium"
                      placeholder="예산확정"
                      readonly>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual01ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual02ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual03ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual04ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual05ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual06ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual07ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual08ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual09ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual10ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual11ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item>
                  <el-checkbox v-model="planConfirm.actual12ApprYn" true-label="Y" false-label="N" size="medium" style="width: 100%; text-align: center;" disabled border/>
                </el-form-item>
              </el-col>
            </el-col>
          </el-row>

        </el-form-item>
      </el-row>

    </el-form>


    <!-- 테이블 -->
    <div class="table-area">

      <div class="table-b">
        <div class="table-header">
          <div class="table-name">

          </div>
          <div class="btn-type2 clearfix" style="margin-top: 30px;">
            <el-button type="info" icon="el-icon-plus" @click="addRow">행추가</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteRow()">행삭제</el-button>
            <el-button type="success" icon="el-icon-document-checked" @click="saveExcel()">엑셀 저장</el-button>
          </div>
        </div>

        <div class="grid-wrap mt10">
          <ag-grid-vue ref="grid"
                       style="width: 100%; height: 50vh;"
                       class="ag-theme-alpine grid_search_height"

                       :columnDefs="columnDefs"
                       :rowData="data"
                       :gridOptions="gridOptions"
                       :defaultColDef="defaultColDef"
                       :frameworkComponents="frameworkComponents"
                       :enableRangeSelection="true"
                       :suppressRowClickSelection="true"

                       rowSelection="multiple"

                       @row-selected="onRowSelected"
                       @grid-ready="onGridReady"
                       @rowDoubleClicked="rowDoubleClick"
                       @cell-clicked="onCellClicked"
          />


        </div>
      </div>

    </div>
    <!-- //테이블 -->

  </div>
</template>

<script>
// import Vue from 'vue'
import mixin from '@/mixin'
import mixinSlip from '@/mixin/slip'
import _ from 'lodash'

import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import DhxCalendar from '@/components/DhxCalendar.vue'
//팝업
import Cctr from '@/components/Cctr_Ag.vue'
import Emp from '@/components/Emp_Ag.vue'


import { AgGridVue } from 'ag-grid-vue'
import YearPlan from "@/components/costBudget/YearPlanPop";
import AcctypeSum from "@/components/costBudget/AcctTypeSumPop";
import PerformerChk from "@/components/costBudget/PerformanceCheck";
import Draft from "@/components/costBudget/DraftPop";
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn";
import AccountPop from "@/components/costBudget/CostBudgetAccountModal";
import ProjectPop from '@/components/costBudget/CostBudgetProjectModal.vue';
import ProductModal from "@/components/costBudget/CostBudgetProductModal.vue";
// import AgDhxCalendar from "@/components/agGrid/AgDhxCalendar.vue";

export default {
  compatConfig: { MODE: 2 },
  name: 'payrollSlipLst',
  mixins: [mixin, mixinSlip],
  components: { Emp, DhxCalendar, AgGridVue},
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
        resizable: true, filter:true, sortable: true
      },
      frameworkComponents: null,
      gridApi: null,
      columnApi: null,
      title: '비용예산관리',
      slipTypes: [],
      slipStats: [],
      data: [],
      delegateList: [],
      planConfirm: {
        actual01ApprYn: "N",
        actual02ApprYn: "N",
        actual03ApprYn: "N",
        actual04ApprYn: "N",
        actual05ApprYn: "N",
        actual06ApprYn: "N",
        actual07ApprYn: "N",
        actual08ApprYn: "N",
        actual09ApprYn: "N",
        actual10ApprYn: "N",
        actual11ApprYn: "N",
        actual12ApprYn: "N",
      },
      savePerformance: [{
        budget01ApprYn: "N",
        budget02ApprYn: "N",
        budget03ApprYn: "N",
        budget04ApprYn: "N",
        budget05ApprYn: "N",
        budget06ApprYn: "N",
        budget07ApprYn: "N",
        budget08ApprYn: "N",
        budget09ApprYn: "N",
        budget10ApprYn: "N",
        budget11ApprYn: "N",
        budget12ApprYn: "N",
      }],
      evdYnList: [{'key': 'N', 'value' : '미첨부'}, {'key': 'Y', 'value' : '첨부'}],
      confirmSeqList: [{'key': '1', 'value' : '1'}, {'key': '2', 'value' : '2'}, {'key': '3', 'value' : '3'}, {'key': '4', 'value' : '4'}, {'key': '5', 'value' : '5'}, {'key': '6', 'value' : '6'}, {'key': '7', 'value' : '7'}, {'key': '8', 'value' : '8'}, {'key': '9', 'value' : '9'}, {'key': '10', 'value' : '10'}],
      tempData: [],
      authority: '',
      chkYn:false,
      form: {
        deptCd: "",
        deptNm: "",
        confirmUserId: "",
        confirmSeq: "",
        confirmStartAmt : "",
        confirmEndAmt : "",
        remark : "",
        periodYm : this.$moment().format('YYYY-MM'),
        periodYear : "",
        periodMonth : "",
        searchDeptCd : "",
        delegateNo : this.$store.state.loginInfo.loginId,
        // searchFr:this.$moment().startOf('month').format('YYYY-MM-DD'),
        // searchTo: this.$moment().endOf('month').format('YYYY-MM-DD'),
      },
      delegateInfo: [],
      showCctrModal: false,
      showEmpModal: false,
      showCustomerModal: false,
      result : [],
      delChkList: [],
      rowIdx: '',
      selTotAmt: '',
      objectPopup: []
    };
  },
  methods: {
    onGridReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;

      this.goSearch();
    },
    makeColDef(){
      const that = this;

      const month = Number(this.form.periodMonth);

      const yearMonth = Number(this.$moment(this.form.periodYm).format('YYYYMM'));

      if(month == 11){

        this.columnDefs = [
          {
            headerName: ''
            , headerCheckboxSelection: true
            , checkboxSelection : (params) => {
              return !that.chkStatus(params.data.itemGroupCd)
            }
            , field:'regYn'
            , width : 80
            , cellStyle:{textAlign: 'center' , color: 'transparent' ,'justify-content': 'center'}
            , editable: false
          },
          {
            headerName: '회계연도',
            field: 'periodYear',
            width: 120,
            cellStyle:{textAlign: 'center'},
            editable: false
          },
          {
            headerName: '예산부서코드',
            field: 'cctrCd',
            width: 100,
            cellStyle:{textAlign: 'center'},
            editable: false
          },
          {
            headerName: '예산부서명',
            field: 'cctrNm',
            width: 180,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '계정코드',
            field: 'acctCd',
            width: 150,
            cellStyle:{textAlign: 'center'},
            cellRenderer: 'schBtn',
            cellRendererParams(params) {

              return {
                funcNm: 'updateAcctInfo',
                // valFuncNm: 'updateEmpItemVal',
                disable : that.chkStatus(params.data.itemGroupCd)
              }
            },
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 200,
            cellStyle:{textAlign: 'left'}
            ,editable: false
          },
          {
            headerName: '프로젝트코드',
            field: 'pjtCd',
            width: 150,
            cellStyle:{textAlign: 'center'},
            cellRenderer: 'schBtn',
            cellRendererParams(params) {
              return {
                funcNm: 'updatePjtInfo',
                // valFuncNm: 'updateEmpItemVal',
                disable : that.chkStatus(params.data.itemGroupCd)
              }
            },
          },
          {
            headerName: '프로젝트명',
            field: 'pjtNm',
            width: 120,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '제품군코드',
            field: 'itemGroupCd',
            width: 120,
            cellStyle:{textAlign: 'center'},
            cellRenderer: 'schBtn',
            cellRendererParams(params) {
              return {
                funcNm: 'updateItemGroupInfo',
                // valFuncNm: 'updateEmpItemVal',
                disable : that.chkStatus(params.data.itemGroupCd)
              }
            },
          },
          {
            headerName: '제품군명',
            field: 'itemGroupNm',
            width: 200,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '['+yearMonth+'] 사업계획',
            field: 'm01PlanAmt',
            width: 150,
            cellStyle:{textAlign: 'right' , 'background-color' : '#e1e1e1'},
            editable: false,
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+yearMonth+'] 예산',
            field: 'm01BudgetAmt',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+yearMonth+'] 적요',
            field: 'm01Remark',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
          },
          {
            headerName: '['+(yearMonth+1)+'] 사업계획',
            field: 'm02PlanAmt',
            width: 150,
            cellStyle:{textAlign: 'right' , 'background-color' : '#e1e1e1'},
            editable: false,
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+1)+'] 예산',
            field: 'm02BudgetAmt',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month+1)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month+1)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+1)+'] 적요',
            field: 'm02Remark',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month+1)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month+1)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
          },
        ]

      }else if(month == 12){

        this.columnDefs = [
          {
            headerName: ''
            , headerCheckboxSelection: true
            , checkboxSelection : (params) => {
                return !that.chkStatus(params.data.itemGroupCd)
            }
            , field:'regYn'
            , width : 80
            , cellStyle:{textAlign: 'center', color: 'transparent'}
            , editable: false
          },
          {
            headerName: '회계연도',
            field: 'periodYear',
            width: 120,
            cellStyle:{textAlign: 'center'},
            editable: false
          },
          {
            headerName: '예산부서코드',
            field: 'cctrCd',
            width: 100,
            cellStyle:{textAlign: 'center'},
            editable: false
          },
          {
            headerName: '예산부서명',
            field: 'cctrNm',
            width: 180,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '계정코드',
            field: 'acctCd',
            width: 150,
            cellStyle: {textAlign: 'center'},
            cellRenderer: 'schBtn',
            cellRendererParams(params) {
              return {
                funcNm: 'updateAcctInfo',
                // valFuncNm: 'updateEmpItemVal',
                disable : that.chkStatus(params.data.itemGroupCd)
              }
            },
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 200,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '프로젝트코드',
            field: 'pjtCd',
            width: 150,
            cellStyle:{textAlign: 'center'},
            cellRenderer: 'schBtn',
            cellRendererParams(params) {
              return {
                funcNm: 'updatePjtInfo',
                // valFuncNm: 'updateEmpItemVal',
                disable : that.chkStatus(params.data.itemGroupCd)
              }
            },
          },
          {
            headerName: '프로젝트명',
            field: 'pjtNm',
            width: 120,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '제품군코드',
            field: 'itemGroupCd',
            width: 120,
            cellStyle:{textAlign: 'center'},
            cellRenderer: 'schBtn',
            cellRendererParams(params) {
              return {
                funcNm: 'updateItemGroupInfo',
                // valFuncNm: 'updateEmpItemVal',
                disable : that.chkStatus(params.data.itemGroupCd)
              }
            },
          },
          {
            headerName: '제품군명',
            field: 'itemGroupNm',
            width: 200,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '['+yearMonth+'] 사업계획',
            field: 'm01PlanAmt',
            width: 150,
            cellStyle:{textAlign: 'right' , 'background-color' : '#e1e1e1'},
            editable: false,
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+yearMonth+'] 예산',
            field: 'm01BudgetAmt',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month)){
                return true;
              }
              else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+yearMonth+'] 적요',
            field: 'm01Remark',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
          },
        ]

      }else{

        this.columnDefs = [
          {
            headerName: ''
            , headerCheckboxSelection: true
            , checkboxSelection : (params) => {
              return !that.chkStatus(params.data.itemGroupCd)
            }
            , field:'regYn'
            , width : 80
            , cellStyle:{textAlign: 'center', color: 'transparent'}
            , editable: false
          },
          {
            headerName: '회계연도',
            field: 'periodYear',
            width: 120,
            cellStyle:{textAlign: 'center'},
            editable: false
          },
          {
            headerName: '예산부서코드',
            field: 'cctrCd',
            width: 100,
            cellStyle:{textAlign: 'center'},
            editable: false
          },
          {
            headerName: '예산부서명',
            field: 'cctrNm',
            width: 180,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '계정코드',
            field: 'acctCd',
            width: 150,
            cellStyle:{textAlign: 'center'},
            cellRenderer: 'schBtn',
            cellRendererParams(params) {
              return {
                funcNm: 'updateAcctInfo',
                // valFuncNm: 'updateEmpItemVal',
                disable : that.chkStatus(params.data.itemGroupCd)
              }
            },
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 200,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '프로젝트코드',
            field: 'pjtCd',
            width: 150,
            cellStyle:{textAlign: 'center'},
            cellRenderer: 'schBtn',
            cellRendererParams(params) {
              return {
                funcNm: 'updatePjtInfo',
                // valFuncNm: 'updateEmpItemVal',
                disable : that.chkStatus(params.data.itemGroupCd)
              }
            },
          },
          {
            headerName: '프로젝트명',
            field: 'pjtNm',
            width: 120,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '제품군코드',
            field: 'itemGroupCd',
            width: 120,
            cellStyle:{textAlign: 'center'},
            cellRenderer: 'schBtn',
            cellRendererParams(params) {
              return {
                funcNm: 'updateItemGroupInfo',
                // valFuncNm: 'updateEmpItemVal',
                disable : that.chkStatus(params.data.itemGroupCd)
              }
            },
          },
          {
            headerName: '제품군명',
            field: 'itemGroupNm',
            width: 200,
            cellStyle:{textAlign: 'left'},
            editable: false
          },
          {
            headerName: '['+yearMonth+'] 사업계획',
            field: 'm01PlanAmt',
            width: 150,
            cellStyle:{textAlign: 'right' , 'background-color' : '#e1e1e1'},
            editable: false,
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+yearMonth+'] 예산',
            field: 'm01BudgetAmt',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+yearMonth+'] 적요',
            field: 'm01Remark',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
          },
          {
            headerName: '['+(yearMonth+1)+'] 사업계획',
            field: 'm02PlanAmt',
            width: 150,
            cellStyle:{textAlign: 'right' , 'background-color' : '#e1e1e1'},
            editable: false,
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+1)+'] 예산',
            field: 'm02BudgetAmt',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month+1)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month+1)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+1)+'] 적요',
            field: 'm02Remark',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month+1)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month+1)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
          },
          {
            headerName: '['+(yearMonth+2)+'] 사업계획',
            field: 'm03PlanAmt',
            width: 150,
            cellStyle:{textAlign: 'right' , 'background-color' : '#e1e1e1'},
            editable: false,
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+2)+'] 예산',
            field: 'm03BudgetAmt',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month+2)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month+2)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
            valueFormatter: (params) => {
              return that.getNumberFormat(params.value);
            }
          },
          {
            headerName: '['+(yearMonth+2)+'] 적요',
            field: 'm03Remark',
            width: 150,
            editable: function (params) {
              if (params.node.data.insertYn === 'Y') {
                return true;
              }else
              if (!that.actualApprYnChk(month+2)){
                return true;
              } else {
                return false;
              }
            },
            cellStyle: (params) => {
              if (params.node.data.insertYn === 'Y') {
                return {textAlign: 'right'}
              }else
              if (!that.actualApprYnChk(month+2)){
                return {textAlign: 'right'}
              } else {
                return {textAlign: 'right' , 'background-color' : '#e1e1e1'};
              }
            },
          },
        ]

      }


    },
    onCellClicked(params) {

    },
    rowDoubleClick(params){

    },
    openConfirmPop(flag,list){

    },
    openYearPlan(){

      var that = this
      this.$modal.open({
        component: YearPlan,
        props: {
          periodYear: this.$moment(this.form.periodYm).format('YYYY'),
          cctrCd : this.form.deptCd,
          cctrNm : this.form.deptNm
        },
        parent: this,
        width: 1200,
        events: {
          close() {

          }
        }
      })

    },
    openDraft(){

      var that = this
      this.$modal.open({
        component: Draft,
        props: {
          periodYm: this.form.periodYm,
          cctrCd : this.form.deptCd,
          cctrNm : this.form.deptNm,
          flag : 'new'
        },
        parent: this,
        width: 1800,
        height: 1500,
        events: {
          close() {

          }
        }
      })

    },
    openAcctTypeSum(){

      var that = this
      this.$modal.open({
        component: AcctypeSum,
        props: {
          periodYm: this.form.periodYm,
          cctrCd : this.form.deptCd,
          cctrNm : this.form.deptNm
        },
        parent: this,
        width: 1200,
        events: {
          close() {

          }
        }
      })

    },
    openPerformerChkPop(){

      var that = this
      this.$modal.open({
        component: PerformerChk,
        props: {
          periodYm: this.form.periodYm,
          cctrCd : this.form.deptCd,
          cctrNm : this.form.deptNm
        },
        parent: this,
        width: 1400,
        events: {
          close() {

          }
        }
      })

    },
    cellValueChange(params){
      if(params.data.chk){
        this.delChkList.push(params.data)
      }
    },
    mainGridRefresh(idx) {
      var rows = [];
      var rowNode = this.gridApi.getDisplayedRowAtIndex(idx);
      rows.push(rowNode);
      this.gridApi.redrawRows({ rowNodes: rows });
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
    getSlipTypeCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "SLIP_TYPE_CD"}})
          .then(response => {
            this.slipTypes = response.data;
          });
    },
    isEmpty(value){
      if( value === "" || value === null || value === undefined || (typeof value === "object" && !Object.keys(value).length === 0) ){
        return ""
      }else{
        return value
      }
    },
    goSearch() {

      this.form.periodYear = this.$moment(this.form.periodYm).format('YYYY');
      this.form.periodMonth = this.$moment(this.form.periodYm).format('MM');

      this.makeColDef();

      this.$store.commit('loading');

      this.$http.post(`/api/cost/budget/list`,this.form)
          .then(response => {
            if (response.data) {

              this.data = response.data.list;

              if (this.isEmpty(response.data.savePerformance[0]) !== ""){
                this.savePerformance = response.data.savePerformance[0];
              } else {
                this.savePerformance = [{
                  budget01ApprYn: "N",
                  budget02ApprYn: "N",
                  budget03ApprYn: "N",
                  budget04ApprYn: "N",
                  budget05ApprYn: "N",
                  budget06ApprYn: "N",
                  budget07ApprYn: "N",
                  budget08ApprYn: "N",
                  budget09ApprYn: "N",
                  budget10ApprYn: "N",
                  budget11ApprYn: "N",
                  budget12ApprYn: "N"
                }];
              }

              if (this.isEmpty(response.data.planConfirm[0]) !== "") {
                this.planConfirm = response.data.planConfirm[0];
              }else {
                  this.planConfirm = {
                      actual01ApprYn: "N",
                      actual02ApprYn: "N",
                      actual03ApprYn: "N",
                      actual04ApprYn: "N",
                      actual05ApprYn: "N",
                      actual06ApprYn: "N",
                      actual07ApprYn: "N",
                      actual08ApprYn: "N",
                      actual09ApprYn: "N",
                      actual10ApprYn: "N",
                      actual11ApprYn: "N",
                      actual12ApprYn: "N"
                };
              }

              this.closeModal();
            }
          }).catch(response => {
        // TODO::Error Handling
        return response
      }).finally(() => {
        this.$store.commit('finish');
      });

    },
    save(){

      let vm = this;
      const list = this.gridApi.getSelectedRows().filter(f => f.itemGroupCd !== 'Total');

      const param = {
        main : this.form,
        list : list,
        planConfirm : this.planConfirm
      };

      if(list.length > 0) {

        let month = Number(this.form.periodMonth);
        let yearMonth = Number(this.$moment(this.form.periodYm).format('YYYYMM'));

        for(var i = 0; i < list.length; i++){

          if(this.isEmpty(list[i].acctNm) === ""){
            this.$swal({type: 'warning', text: '계정코드를 입력해 주세요.'});
            return false;
          }
          if(this.isEmpty(list[i].pjtNm) === ""){
            this.$swal({type: 'warning', text: '프로젝트코드를 입력해 주세요.'});
            return false;
          }
          if(this.isEmpty(list[i].itemGroupNm) === ""){
            this.$swal({type: 'warning', text: '제품군을 입력해 주세요.'});
            return false;
          }

          if(month == 11){
            if(this.isEmpty(list[i].m01BudgetAmt) === ""){
              this.$swal({type: 'warning', text: yearMonth+' 예산을 입력해 주세요.'});
              return false;
            }
            if(this.isEmpty(list[i].m02BudgetAmt) === ""){
              this.$swal({type: 'warning', text: (yearMonth+1)+' 예산을 입력해 주세요.'});
              return false;
            }
            if(isNaN(this.isEmpty(list[i].m01BudgetAmt))){
              this.$swal({type: 'warning', text: yearMonth+' 예산에 숫자가 아닌 값이 입력되었습니다.'});
              return false;
            }
            if(isNaN(this.isEmpty(list[i].m02BudgetAmt))){
              this.$swal({type: 'warning', text: (yearMonth+1)+' 예산에 숫자가 아닌 값이 입력되었습니다.'});
              return false;
            }
          }else if(month == 12){
            if(this.isEmpty(list[i].m01BudgetAmt) === ""){
              this.$swal({type: 'warning', text: yearMonth+' 예산을 입력해 주세요.'});
              return false;
            }
            if(isNaN(this.isEmpty(list[i].m01BudgetAmt))){
              this.$swal({type: 'warning', text: yearMonth+' 예산에 숫자가 아닌 값이 입력되었습니다.'});
              return false;
            }
          }else{
            if(this.isEmpty(list[i].m01BudgetAmt) === ""){
              this.$swal({type: 'warning', text: yearMonth+' 예산을 입력해 주세요.'});
              return false;
            }
            if(this.isEmpty(list[i].m02BudgetAmt) === ""){
              this.$swal({type: 'warning', text: (yearMonth+1)+' 예산을 입력해 주세요.'});
              return false;
            }
            if(this.isEmpty(list[i].m03BudgetAmt) === ""){
              this.$swal({type: 'warning', text: (yearMonth+2)+' 예산을 입력해 주세요.'});
              return false;
            }
            if(isNaN(this.isEmpty(list[i].m01BudgetAmt))){
              this.$swal({type: 'warning', text: yearMonth+' 예산에 숫자가 아닌 값이 입력되었습니다.'});
              return false;
            }
            if(isNaN(this.isEmpty(list[i].m02BudgetAmt))){
              this.$swal({type: 'warning', text: (yearMonth+1)+' 예산에 숫자가 아닌 값이 입력되었습니다.'});
              return false;
            }
            if(isNaN(this.isEmpty(list[i].m03BudgetAmt))){
              this.$swal({type: 'warning', text: (yearMonth+2)+' 예산에 숫자가 아닌 값이 입력되었습니다.'});
              return false;
            }
          }

        }

        this.$swal({
          type: 'question',
          html: '저장 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading');

            this.$http.post(`/api/cost/budget/save`, param)
                .then(response => {
                  this.$swal({type: 'success', text: '저장 하였습니다.'})
                      .then((result) => {
                        if (result.value) {
                          this.goSearch();
                        }
                      });
                }).catch(e => {
              this.$swal({type: 'error', text: e.response.data.message})
            }).finally(() => {
              this.$store.commit('finish');
            });
          }
        });
      }else {
        this.$swal({ type: 'info', text: '저장할 항목을 선택하세요.' });
      }
    },
    saveApproval(){

      const month = Number(this.form.periodMonth);

      if(month == 11){

        const rowdata1 = eval("this.planConfirm.actual" + this.addLeadingZero(month) + "ApprYn");
        const rowdata2 = eval("this.planConfirm.actual" + this.addLeadingZero(month+1) + "ApprYn");

        if(rowdata1 === "Y"){
          this.$swal({ type: 'error', text: month+'월은 이미 [예산확정] 되었습니다.' })
          return;
        }else
        if(rowdata2 === "Y"){
          this.$swal({ type: 'error', text: (month+1)+'월은 이미 [예산확정] 되었습니다.' })
          return;
        }

      }else if(month == 12){

        const rowdata1 = eval("this.planConfirm.actual" + this.addLeadingZero(month) + "ApprYn");

        if(rowdata1 === "Y"){
          this.$swal({ type: 'error', text: month+'월은 이미 [예산확정] 되었습니다.' })
          return;
        }

      }else{

        const rowdata1 = eval("this.planConfirm.actual" + this.addLeadingZero(month) + "ApprYn");
        const rowdata2 = eval("this.planConfirm.actual" + this.addLeadingZero(month+1) + "ApprYn");
        const rowdata3 = eval("this.planConfirm.actual" + this.addLeadingZero(month+2) + "ApprYn");


        if(rowdata1 === "Y"){
          this.$swal({ type: 'error', text: month+'월은 이미 [예산확정] 되었습니다.' })
          return;
        }else
        if(rowdata2 === "Y"){
          this.$swal({ type: 'error', text: (month+1)+'월은 이미 [예산확정] 되었습니다.' })
          return;
        }else
        if(rowdata3 === "Y"){
          this.$swal({ type: 'error', text: (month+2)+'월은 이미 [예산확정] 되었습니다.' })
          return;
        }

      }

      this.$swal({
        type: 'question',
        html: this.form.periodYear+'년도 '+this.form.deptCd+' ('+this.form.deptNm+') 부서의 <br/>3개월 예산을 확정하시겠습니까?<br/>' +
            '<br/>' +
            ' (참고 : 조회 월 기준, M+2)' ,
        showCancelButton: true
      }).then(r => {
        if (r.value) {
          this.$store.commit('loading');
          this.$http.post(`/api/cost/budget/approval/save`,this.form)
              .then(response => {
                this.$swal({ type: 'success', text: '예산을 확정 하였습니다.' })
                    .then((result) => {
                      if (result.value) {
                        this.goSearch();
                      }
                    });
              }).catch(e => {
            this.$swal({ type: 'error', text: e.response.data.message })
          }).finally(() => {
            this.$store.commit('finish');
          });
        }
      });
    },
    clearApproval(){

      this.$swal({
        type: 'question',
        html: this.form.periodYear+'년도 '+this.form.periodMonth+'월 '+this.form.deptCd+' ('+this.form.deptNm+') 부서의 <br/>예산확정을 해제하시겠습니까?<br/>' ,
        showCancelButton: true
      }).then(r => {
        if (r.value) {
          this.$store.commit('loading');
          this.$http.post(`/api/cost/budget/approval/clear`,this.form)
              .then(response => {
                this.$swal({ type: 'success', text: '예산을 해제 하였습니다.' })
                    .then((result) => {
                      if (result.value) {
                        this.goSearch();
                      }
                    });
              }).catch(e => {
            this.$swal({ type: 'error', text: e.response.data.message })
          }).finally(() => {
            this.$store.commit('finish');
          });
        }
      });
    },
    addRow() {
      // let index = this.data.length;
      this.data.unshift({
        insertYn : 'Y',
        periodYear : this.form.periodYear,
        cctrCd : this.form.deptCd,
        cctrNm : this.form.deptNm,
        pjtCd  : this.delegateInfo.pjtCd,
        pjtNm  : this.delegateInfo.pjtNm,
        itemGroupCd : this.delegateInfo.productCd,
        itemGroupNm : this.delegateInfo.productNm,
        // index : index
      });
    },
    deleteRow() {
      let vm = this;
      // let list = this.data.filter(v => v.regYn)
      // const list = this.gridApi.getSelectedRows();

      const list = this.gridApi.getSelectedRows().filter(f => f.insertYn !== 'Y');
      const nList = this.gridApi.getSelectedRows().filter(f => f.insertYn === 'Y');

      nList.forEach((x)=>{
        let index = x.rowIdx;
        this.data.splice(index,1);
      })

      const param = {
        main : this.form,
        list : list
      };

      if(list.length === 0 && nList.length > 0){
        // 저장 하지 않은 값은 바로 삭제
      }else
      if(list.length > 0){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            this.$http.post(`/api/cost/budget/delete`, param)
                .then((response) => {
                  this.$swal({ type: 'success', text: '예산을 삭제 하였습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.goSearch();
                        }
                      });
                })
                .catch((e) => {
                  this.$swal({type: 'error', text: e.response.data.message})
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
    chkStatus(itemGroupCd) {
      return itemGroupCd == 'Total' ? true : false;
    },
    saveExcel() {
      // this.downloadExcel(this.$refs.grid);
      this.gridOptions.api.exportDataAsExcel({fileName: '비용예산'});
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
      if (!param.searchFr || !param.searchTo) {
        this.$swal({type: 'warning', text: 'GL일자를 입력해 주세요.'});
        return false;
      }
      return true;
    },
    addLeadingZero(number) {
      return number < 10 ? `0${number}` : number;
    },
    popCctr(flag) {
      let vm = this
      this.$modal.open({
        component: Cctr,
        props: {
          param: this.form.deptNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveCctr(obj)
          }
        }
      })
    },
    popEmp(clear) {
      let vm = this
      this.$modal.open({
        component: Emp,
        props: {
          param: this.form.confirmUserNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveEmp(obj)
            vm.$forceUpdate();
          }
        }
      })
    },
    receiveCctr(obj) {
      this.form.deptCd = obj.deptCd;
      this.form.deptNm = obj.deptNm;
    },
    receiveEmp(obj) {
      this.form.confirmUserId = obj.empNo;
      this.form.confirmUserNm = obj.empNm;
      this.$forceUpdate();
    },
    initCctr(force) {
      if (force === true) this.form.deptNm = '';
      if (this.form.deptNm === '') this.form.deptCd = '';
    },
    initEmp(force) {
      if (force === true) this.form.confirmUserNm = '';
      if (this.form.confirmUserNm === '') this.form.confirmUserId = '';
    },
    dateSetting(str){
      switch (str) {
        case 'toDay':
          this.form.searchFr = this.$moment().format('YYYYMMDD')
          this.form.searchTo = this.$moment().format('YYYYMMDD')
          break;
        case 'crrntMnth':
          this.form.searchFr = this.$moment().startOf('month').format('YYYYMMDD')
          this.form.searchTo = this.$moment().endOf('month').format('YYYYMMDD')
          break;
        case 'PrvsMnth':
          this.form.searchFr = this.$moment().add(-1, 'month').startOf('month').format('YYYYMMDD')
          this.form.searchTo = this.$moment().add(-1, 'month').endOf('month').format('YYYYMMDD')
          break;
      }
      // this.goSearch()
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
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.data[rowIdx].regYn = params.node.isSelected();
    },
    updateAcctInfo(){
      const vm = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      this.$modal.open({
        component: AccountPop,
        parent: this,
        props: {
          postSearch : true,
          deptCd: this.form.deptCd
        },
        width: 800,
        events: {
          close(object) {
            rowNode.setDataValue('acctCd', object.acctCd);
            rowNode.setDataValue('acctNm', object.acctNm);
            vm.gridApi.getRowNode(vm.rowId).setSelected(true);
          }
        }
      });
    },
    updatePjtInfo(){
      const vm = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      this.$modal.open({
        component: ProjectPop,
        parent: this,
        props: {
          param: rowNode.data.projectNm
        },
        width: 800,
        events: {
          close(object) {
            rowNode.setDataValue('pjtCd', object.projectCd);
            rowNode.setDataValue('pjtNm', object.projectNm);
            vm.gridApi.getRowNode(vm.rowId).setSelected(true);
          }
        }
      });
    },
    updateItemGroupInfo(){
      const vm = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      this.$modal.open({
        component: ProductModal,
        parent: this,
        props: {
          schTxt: rowNode.data.itemGroupCd
        },
        width: 800,
        events: {
          close(object) {
            rowNode.setDataValue('itemGroupCd', object.itemGroupCd);
            rowNode.setDataValue('itemGroupNm', object.itemGroupNm);
            vm.gridApi.getRowNode(vm.rowId).setSelected(true);
          }
        }
      });
    },
    getEmpDetail(empNo){
      this.$http.get(`/api/emp/${empNo}`)
          .then((response) => {
            this.delegateInfo = response.data
          })
    },
    actualApprYnChk(month){

      let val = false;

      switch (month) {
        case 1 :
        if(this.planConfirm.actual01ApprYn === 'Y'){
          val = true;
        }
        break;
        case 2 :
          if(this.planConfirm.actual02ApprYn === 'Y'){
            val = true;
          }
          break;
        case 3 :
          if(this.planConfirm.actual03ApprYn === 'Y'){
            val = true;
          }
          break;
        case 4 :
          if(this.planConfirm.actual04ApprYn === 'Y'){
            val = true;
          }
          break;
        case 5 :
          if(this.planConfirm.actual05ApprYn === 'Y'){
            val = true;
          }
          break;
        case 6 :
          if(this.planConfirm.actual06ApprYn === 'Y'){
            val = true;
          }
          break;
        case 7 :
          if(this.planConfirm.actual07ApprYn === 'Y'){
            val = true;
          }
          break;
        case 8 :
          if(this.planConfirm.actual08ApprYn === 'Y'){
            val = true;
          }
          break;
        case 9 :
          if(this.planConfirm.actual09ApprYn === 'Y'){
            val = true;
          }
          break;
        case 10 :
          if(this.planConfirm.actual10ApprYn === 'Y'){
            val = true;
          }
          break;
        case 11 :
          if(this.planConfirm.actual11ApprYn === 'Y'){
            val = true;
          }
          break;
        case 12 :
          if(this.planConfirm.actual12ApprYn === 'Y'){
            val = true;
          }
          break;
      }

      return val;
    },
  },
  beforeMount(){
    const that = this;
    this.getDelegateList();
    this.frameworkComponents = {//그리드에서 사용할 외부 comp 등록
      checkboxRenderer: CheckboxCellRenderer,
      schBtn : AgGridSearchBtn,
    };
  },
  mounted() {
    this.form.deptCd = this.$store.state.loginInfo.loginCctrCd;
    this.form.deptNm = this.$store.state.loginInfo.loginCctrNm
    this.form.searchDeptCd = this.form.deptCd;
    this.getEmpDetail(this.$store.state.loginInfo.loginId);
  },
  watch: {
    'form.delegateNo': {
      handler(value) {
        if (value == this.$store.state.loginInfo.loginId){
          this.form.deptCd = this.$store.state.loginInfo.loginCctrCd;
          this.form.deptNm = this.$store.state.loginInfo.loginCctrNm
          this.form.searchDeptCd = this.form.deptCd;
        }else{
          for (var i = 0; i < this.delegateList.length; i++) {
            if (this.delegateList[i].giveUserId === value) {
              this.form.deptCd = this.delegateList[i].deptCd;
              this.form.deptNm = this.delegateList[i].deptNm;
              this.form.searchDeptCd = this.form.deptCd;
              break;
            }
          }
        }
        this.$forceUpdate();
        this.goSearch();
        this.getEmpDetail(value);
      }
    },
    'form.periodYm'() {
      this.goSearch();
    },
  },
  destroyed() {
    if (this.objectPopup.length > 0) {
      for (const item of this.objectPopup) {
        item.close()
      }
    }
  },

};
</script>

<style scoped>
.control.select.is-fullwidth {
  width: 55%;
}
.el-col-24 {
  width: 180%;
  /*margin-left: 5%;*/
}
.el-row {
  height: 0;
}
.search_box {
  width: 30%;
}
.inner-box{
  overflow: hidden;
}
</style>
