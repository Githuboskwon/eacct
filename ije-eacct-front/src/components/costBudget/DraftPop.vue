<template>
  <layout>
  <span slot="header">
    {{ title }}
    <button class="btn-pop-close delete" aria-label="close" @click="closeModal()"></button>
  </span>
    <div slot="content">



      <div  class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
        <div class="btn-type1 clearfix" style="float:right; width: 310px;">
          <el-button v-if="flag !== 'new'" size="large" type="primary" icon="el-icon-folder-opened" @click="openVendorPopup()">상신</el-button>
          <el-button v-if="flag !== 'new'" size="large" type="danger" icon="el-icon-delete" @click="deleteSlip()">삭제</el-button>
          <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save()">저장</el-button>
        </div>
      </div>

      <div class="table-area">
        <div class="table-header">
          <div class="table-a fixed">
            <div class="content">
              <div class="btn-wrap" >
                <table class="table">
                  <tbody>
                  <tr>
                    <th style="width: 140px;">작성번호</th>
                    <td colspan="3" >{{form.slipNo}}</td>
                    <th style="width: 140px;">회계일자</th>
                    <td colspan="3">{{form.postDt}}</td>
                  </tr>
                  <tr>
                    <th>작성자</th>
                    <td colspan="3">{{form.userCd}} / {{form.userNm}}</td>
                    <th>소속부서</th>
                    <td colspan="3">{{form.deptCd}} / {{form.deptNm}}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <div class="table-a fixed">
            <div class="content">
              <div class="btn-wrap" >
                <table class="table">
                  <tbody>
                  <tr>
                    <th style="width: 140px;">예산부서</th>
                    <td colspan="3" >{{form.cctrNm}}</td>
                    <th style="width: 140px;">예산년월</th>
                    <td colspan="3">{{form.periodYear}} - {{form.periodMonth}}</td>
                  </tr>
                  <tr>
                    <th>제목</th>
                    <td colspan="3"><input class="input" type="text" v-model="form.title" /></td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>


          <div class="table-b" v-if="flag !== 'new'">
            <div class="table-name">
              <h3 class="ico_table_name">결재선</h3>
            </div>
            <table class="table">
              <colgroup>
                <col width="3%">
                <col width="7%">
                <col width="20%">
                <col width="20%">
                <col width="10%">
                <col width="10%">
                <col width="30%">
              </colgroup>
              <thead>
              <tr>
                <th class="no-radius">NO</th>
                <th>결재유형</th>
                <th>결재자</th>
                <th>실제결재자</th>
                <th>결재상태</th>
                <th>결재일시</th>
                <th style="border-right: 1px solid rgb(173, 173, 173);">의견</th>
              </tr>
              </thead>
              <tbody id="tbody">
              <tr v-for="(item, index) in apprLine" :key="index">
                <td style="text-align: center;">{{index+1}}</td>
                <td style="text-align: center;">{{item.apprType}}</td>
                <td>{{item.aprverUser}}</td>
                <td>{{item.aaprverUser}}</td>
                <td style="text-align: center;">{{item.apprStatus}}</td>
                <td style="text-align: center;">{{item.apprDtm === null ? null : $moment(item.apprDtm).format('YYYY-MM-DD HH:mm:ss')}}</td>
                <td style="border-right: 1px solid #adadad">{{item.apprDesc}}</td>
              </tr>
              </tbody>
            </table>
          </div>

        </div>
      </div>

      <div class="table-area">
        <div class="table-b">
          <div class="table-header">
            <div class="table-name">
              <h3 class="ico_table_name">총괄실적표</h3>
            </div>

          </div>


          <div>
            <ag-grid-vue
                ref="grid"
                style="width: 100%; height: 400px;"
                class="ag-theme-alpine"
                rowSelection="single"
                :columnDefs="columnDefs"
                :defaultColDef="defaultColDef"
                :frameworkComponents="frameworkComponents"
                :rowData="data"
                :gridOptions="gridOptions"
                :suppressRowClickSelection="false"
                :enableRangeSelection="false"
                @cell-clicked="onCellClicked"
                @grid-ready="onReady"
            />
          </div>

        </div>
      </div>

      <div class="table-area">
        <div class="table-b">
          <div class="table-header">
            <div class="table-name">
              <h3 class="ico_table_name">예산초과 상세내역</h3>
            </div>
            <div class="btn-type2 clearfix">
              <el-button type="info" icon="el-icon-plus" @click="addRow">행추가</el-button>
              <el-button type="danger" icon="el-icon-delete" @click="deleteRow()">행삭제</el-button>
            </div>
          </div>


          <div>
            <ag-grid-vue
                ref="grid"
                style="width: 100%; height: 400px;"
                class="ag-theme-alpine"
                rowSelection="single"
                :columnDefs="columnDefsSub"
                :defaultColDef="defaultColDefSub"
                :frameworkComponents="frameworkComponents"
                :rowData="subData"
                :gridOptions="gridOptionsSub"
                :suppressRowClickSelection="false"
                :enableRangeSelection="false"
                @cell-clicked="onCellClicked"
            />
          </div>

        </div>
      </div>

      <div class="table-area">
        <div class="table-b">
          <div class="table-header">
            <div class="table-name">
              <h3 class="ico_table_name">내용</h3>
            </div>
          </div>


          <div>
            <el-input
                type="textarea"
                placeholder="내용을 입력해주세요."
                v-model="form.remark"
                :autosize="{ minRows: 5, maxRows: 8}"
                maxlength="4000"
                show-word-limit
            >
            </el-input>
          </div>

        </div>
      </div>

      <div class="table-area" style="height: 60px;">
        <div class="file has-name" style="width: 1000px;">
          <div class="file" style="margin-right: 10px;">
            <div class="file-label" @click="openUploadEvidencePopup()">
                          <span class="file-cta">
                            <span class="file-label"> 증빙첨부</span>
                            <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                          </span>
              <span class="file-name">{{ this.$numeral(getEvidFileSize).format('00') }}<i class="far fa-file-alt"></i></span>
            </div>
          </div>
          <div class="file">
            <div class="file-label" @click="openUploadWingsPopup()">
                          <span class="file-cta">
                            <span class="file-label"> 그룹웨어 문서</span>
                            <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                          </span>
              <span class="file-name">{{ this.$numeral(getJiniSize).format('00') }}<i class="far fa-file-alt"></i></span>
            </div>
          </div>
        </div>
      </div>

    </div>
  </layout>
</template>

<script>
import _ from 'lodash'

import Layout from '@/components/ModalSlot.vue'
import {AgGridVue} from "ag-grid-vue";
import PerformerChkDetail from "@/components/costBudget/PerformanceCheckDetail";
import ApprLineSet from "@/components/ApprLineSet";
import NumberInputCellRenderer from "@/components/agGrid/numberinput-cell-renderer";
import EvidAtchPopModeless from "@/components/EvidAtchPopModeless";
import WingsAtchPop from "@/components/JiniAtchPop";

//['docMngNo', 'value', 'readonly']
export default {
  props: {
    periodYm: {
      type: String,
      required: false
    },
    cctrCd: {
      type: String,
      required: false
    },
    cctrNm: {
      type: String,
      required: false
    },
    flag: {
      type: String,
      required: false
    },
    slipNo: {
      type: String,
      required: false
    },
    slipHeaderId: {
      type: String,
      required: false
    },
    title: {
      type: String,
      required: false,
      default: '비용예산 기안서'
    }
  },
  mixins: [],
  components: {
    AgGridVue,
    Layout
  },
  data() {
    return {
      columnDefs: [],
      columnDefsSub: [],
      defaultColDef: {
        resizable: true, filter:false, sortable: false
      },
      defaultColDefSub: {
        resizable: true, filter:false, sortable: false
      },
      frameworkComponents: {numberInput: NumberInputCellRenderer,},
      data: [],
      subData: [],
      form:{
        postDt : "",
        userCd : "",
        userNm : "",
        deptCd : "",
        deptNm : "",
        cctrCd : "",
        cctrNm : "",
        periodYear : "",
        periodMonth : "",
        title : "",
        slipNo : this.slipNo,
        slipHeaderId : this.slipHeaderId,
        slipType : "90",
        remark : "",
      },
      gridOptions: {},
      gridOptionsSub: {},
      periodYear: '',
      periodMonth: '',
      apprLine : [],
    }
  },
  methods: {
    closeModal() {
      this.$emit('close')
      //this.$parent.close();
    },
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.gridApiSub = this.gridOptionsSub.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    makeColDef(){
      const that = this;

      let month = Number(this.form.periodMonth);

      let yearMonth = Number(this.$moment(this.periodYm).format('YYYYMM'));

      if(month == 11){

        this.columnDefs = [
          {
            headerName: '구분',
            field: 'acctDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: (month-1)+' 월 (전월)',
            children:[
              {
                headerName: '사업계획',
                field: 'prevPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ptdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                },
              },
              {
                headerName: '차이',
                field: 'prevPtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month-1)+' 월 (전월 누계)',
            children:[
              {
                headerName: '사업계획',
                field: 'prevYtdPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevYtdBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ytdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '차이',
                field: 'prevYtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: month+' 월 (당월:예산월)',
            children:[
              {
                headerName: '사업계획',
                field: 'planAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'bdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+1)+' 월 (차월)',
            children:[
              {
                headerName: '사업계획',
                field: 'nextPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'nextBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
        ]

      }else if(month == 12){

        this.columnDefs = [
          {
            headerName: '구분',
            field: 'acctDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: (month-1)+' 월 (전월)',
            children:[
              {
                headerName: '사업계획',
                field: 'prevPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ptdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                },
              },
              {
                headerName: '차이',
                field: 'prevPtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month-1)+' 월 (전월 누계)',
            children:[
              {
                headerName: '사업계획',
                field: 'prevYtdPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevYtdBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ytdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '차이',
                field: 'prevYtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: month+' 월 (당월:예산월)',
            children:[
              {
                headerName: '사업계획',
                field: 'planAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'bdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
        ]

      } else if(month == 1){

        this.columnDefs = [
          {
            headerName: '구분',
            field: 'acctDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: '12 월 (전월)',
            children:[
              {
                headerName: '사업계획',
                field: 'prevPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ptdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                },
              },
              {
                headerName: '차이',
                field: 'prevPtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: '12 월 (전월 누계)',
            children:[
              {
                headerName: '사업계획',
                field: 'prevYtdPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevYtdBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ytdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '차이',
                field: 'prevYtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: month+' 월 (당월:예산월)',
            children:[
              {
                headerName: '사업계획',
                field: 'planAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'bdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+1)+' 월 (차월)',
            children:[
              {
                headerName: '사업계획',
                field: 'nextPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'nextBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+2)+' 월 (차월)',
            children:[
              {
                headerName: '사업계획',
                field: 'next2PlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'next2BdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
        ]

      }
      else{

        this.columnDefs = [
          {
            headerName: '구분',
            field: 'acctDivNm',
            width: 80,
            cellStyle:{textAlign: 'center'}
          },
          {
            headerName: '계정명',
            field: 'acctNm',
            width: 250,
            cellStyle:{textAlign: 'left'}
          },
          {
            headerName: (month-1)+' 월 (전월)',
            children:[
              {
                headerName: '사업계획',
                field: 'prevPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ptdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                },
              },
              {
                headerName: '차이',
                field: 'prevPtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month-1)+' 월 (전월 누계)',
            children:[
              {
                headerName: '사업계획',
                field: 'prevYtdPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'prevYtdBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '실적',
                field: 'ytdActualAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '차이',
                field: 'prevYtdGapAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: month+' 월 (당월:예산월)',
            children:[
              {
                headerName: '사업계획',
                field: 'planAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'bdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+1)+' 월 (차월)',
            children:[
              {
                headerName: '사업계획',
                field: 'nextPlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'nextBdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
          {
            headerName: (month+2)+' 월 (차월)',
            children:[
              {
                headerName: '사업계획',
                field: 'next2PlanAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
              {
                headerName: '비용예산',
                field: 'next2BdAmt',
                width: 130,
                cellStyle:{textAlign: 'right'},
                valueFormatter: (params) => {
                  return that.getNumberFormat(params.value);
                }
              },
            ]
          },
        ]

      }


      this.columnDefsSub = [
        {
          headerName: '선택'
          , checkboxSelection: true
          , field:'regYn'
          , width : 80
          , cellStyle:{textAlign: 'center'}
          , editable: false
        },
        {
          headerName: '계정명',
          field: 'acctNm',
          width: 130,
          cellStyle:{textAlign: 'left'},
          editable : true
        },
        {
          headerName: '전월(비용예산 대비 실적)',
          children:[
            {
              headerName: '초과금액',
              field: 'pastOverAmt',
              width: 200,
              editable: true,
              cellRenderer: 'numberInput',
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '초과사유',
              field: 'pastOverReason',
              width: 550,
              cellStyle:{textAlign: 'left'},
              editable : true
            },
          ]
        },
        {
          headerName: '당월(사업계획 대비 비용예산)',
          children:[
            {
              headerName: '초과금액',
              field: 'curOverAmt',
              width: 200,
              editable: true,
              cellRenderer: 'numberInput',
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '초과사유',
              field: 'curOverReason',
              width: 550,
              cellStyle:{textAlign: 'left'},
              editable : true
            },
          ]
        },

      ];


    },
    goSearch(){
      const vm = this;

      if(this.flag === 'new'){
        this.form.periodYear = this.$moment(this.periodYm).format('YYYY');
        this.form.periodMonth = this.$moment(this.periodYm).format('MM');
      }

      this.makeColDef();

      this.$store.commit('loading');

      const params = {
        periodYear : this.form.periodYear,
        periodMonth : this.form.periodMonth,
        searchDeptCd : this.form.cctrCd,
        searchDegree : 1,
        slipNo : this.isEmpty(this.form.slipNo),
        slipHeaderId : this.isEmpty(this.form.slipHeaderId),
        slipType : this.form.slipType
      }

      this.$http.post(`/api/cost/budget/pop/draft/list`,params)
          .then(response => {
            if (response.data) {
              vm.data = response.data.list;
              if(vm.flag != "new"){
                vm.setSlipData(response.data.data[0]);
                vm.subData = response.data.dtList;
              }
            }
          }).catch(response => {
        // TODO::Error Handling
        return response;
      }).finally(() => {
        this.$store.commit('finish');
      });

    },
    setSlipData(data){
      this.form.slipNo = data.slipNo;
      this.form.postDt = data.postDt;
      this.form.userCd = data.empNo;
      this.form.userNm = data.empNm;
      this.form.deptCd = data.deptCd;
      this.form.deptNm = data.deptNm;
      this.cctrCd = data.cctrCd;
      this.cctrNm = data.cctrNm;
      this.form.title = data.headerRemark;
      this.form.remark = data.remark;
    },
    onCellClicked(params) {
      //const idx = params.rowIndex;
      const field = params.colDef.field;
      if(field === 'ptdActualAmt') {

        var that = this
        this.$modal.open({
          component: PerformerChkDetail,
          props: {
            periodYm: this.$moment(this.periodYm).add(-1, 'month').startOf('month').format('YYYYMM'),
            cctrCd : this.cctrCd,
            cctrNm : this.cctrNm,
            acctCd : params.data.acctCd
          },
          parent: this,
          width: 1400,
          events: {
            close() {

            }
          }
        })

      }
    },
    getNumberFormat(value){
      if(value){
        if(typeof value === "string"){
          if(val.substr(0,1) !== '-') value = value.replace(/[^0-9]/g, "")
          if(val.substr(0,1) === '-') value = value.replace( /^\[-\]?\\d\*$/g, "")
        }
        return Math.floor(value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      }
    },
    getDateFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD')
      }
    },
    save() {
      const that = this;

      this.$swal({
        type: 'info',
        text: `결재내역을 저장합니다. 계속 하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오',
      }).then((result) => {
        if (result.value) {
          this.$store.commit('loading');

          _.forEach(that.apprLine, (v, index) => {
            v.apprSeq = index + 1
          })

          const pData = {
            periodYear : that.form.periodYear,
            periodMonth : that.form.periodMonth,
            cctrCd : that.form.cctrCd,
            cctrNm : that.form.cctrNm,
            slipNo : that.isEmpty(that.form.slipNo),
            slipHeaderId : that.isEmpty(that.form.slipHeaderId),
            approvalGroupId : that.isEmpty(that.form.slipHeaderId),
            slipType : that.form.slipType,
            deptCd : that.form.deptCd,
            deptNm : that.form.deptNm,
            headerRemark : that.form.title,
            remark : that.form.remark,
            userId : that.form.userCd,
            postDt : that.form.postDt
          }

          const param = {
            data : pData,
            subData : that.subData,
          }

          this.$http.post(`/api/cost/budget/pop/draft/save`, param)
              .then((response) => {

                const retrunData = response.data;
                that.form.slipNo = retrunData.slipNo;
                that.form.slipHeaderId = retrunData.slipHeaderId;
                that.periodYm = retrunData.periodYear+retrunData.periodMonth;
                // that.form.title = retrunData.headerRemark;
                that.flag = "def";

                that.$store.commit('finish');
                that.$swal({type: 'info', text: '저장이 완료되었습니다.'})
                    .then((result)=>{
                      if(result.value){
                        that.goSearch();
                      }
                    });
              })
              .catch((e) => {
                that.$store.commit('finish');
                // that.$emit('close');
              });
        }
      })
    },
    deleteSlip(){
      const that = this;

      this.$swal({
        type: 'info',
        text: `결재내역을 삭제합니다. 계속 하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오',
      }).then((result) => {
        if (result.value) {
          this.$store.commit('loading');

          _.forEach(that.apprLine, (v, index) => {
            v.apprSeq = index + 1
          })

          this.$http.post(`/api/cost/budget/pop/draft/delete`, {
            slipHeaderId: that.isEmpty(that.form.slipHeaderId),
            slipNo: that.isEmpty(that.form.slipNo),
          })
          .then((response) => {
            that.$store.commit('finish');
            that.$swal({type: 'info', text: '삭제되었습니다.'})
                .then((result) => {
                  if (result.value) {
                    that.$emit('close', 'Y');
                  }
                });
          })
          .catch((e) => {
            that.$store.commit('finish');
            that.$emit('close');
          });
        }
      });

    },
    approval() {
      const that = this;

      if (this.apprLine.length < 1) {
        // this.$swal({type: 'warning', text: '결재선을 지정해주세요.'});
        return false;
      }
      this.$swal({
        type: 'info',
        text: `결재내역을 상신합니다. 계속 하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오',
      }).then((result) => {
        if (result.value) {
          this.$store.commit('loading');




          _.forEach(that.apprLine, (v, index) => {
            v.apprSeq = index + 1
          })


          const pData = {
            periodYear : that.form.periodYear,
            periodMonth : that.form.periodMonth,
            cctrCd : that.form.cctrCd,
            cctrNm : that.form.cctrNm,
            slipNo : that.isEmpty(that.form.slipNo),
            slipHeaderId : that.isEmpty(that.form.slipHeaderId),
            approvalGroupId : that.isEmpty(that.form.slipHeaderId),
            slipType : that.form.slipType,
            deptCd : that.form.deptCd,
            deptNm : that.form.deptNm,
            headerRemark : that.form.title,
            remark : that.form.remark,
            userId : that.form.userCd,
            postDt : that.form.postDt
          }

          const param = {
            data : pData,
            subData : that.subData,
          }

          this.$http.post(`/api/cost/budget/pop/draft/save`, param)
          .then((response) => {

              this.$http.put(`/api/appr/detail/req`, {
                apprGroupId : that.isEmpty(that.form.slipHeaderId),
                slipHeaderId : that.isEmpty(that.form.slipHeaderId),
                slipNo : that.isEmpty(that.form.slipNo),
                slipType : that.form.slipType,
                docTypeCd: that.form.docType,
                docMngNo: that.form.slipNo,
                draftId : that.$store.state.loginInfo.loginId,
                docTitleNm : that.form.title ,
                approvalDetails: that.apprLine,
                apprRemark : that.apprRemark,
                totalAmt : 0,
              })
              .then((response) => {
                that.$store.commit('finish');
                that.$swal({type: 'info', text: '상신이 완료되었습니다.'})
                    .then((result)=>{
                      if(result.value){
                        that.$emit('close','Y');
                      }
                    });
                that.search();
              })
              .catch((e) => {
                that.$store.commit('finish');
                this.$swal({ type: 'error', text: e.response.data.message })
                // that.$emit('close');
              });

          })
          .catch((e) => {
            this.$swal({ type: 'error', text: e.response.data.message })
            that.$store.commit('finish');
            // that.$emit('close');
          });


        }else{
          that.apprLine =  []
        }
      })
    },
    addRow() {
      let index = this.data.length;
      this.subData.push({
        acctNm : '',
        curOverAmt : '',
        curOverReason : '',
        pastOverAmt : '',
        pastOverReason : '',
      });
    },
    deleteRow() {
      let vm = this;
      // let list = this.data.filter(v => v.regYn)
      const list = this.gridApiSub.getSelectedRows();

      vm.result = [];

      if(list.length > 0){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
              this.subData.splice(list);
          }
        })
      } else {
        this.$swal({ type: 'info', text: '삭제할 항목을 선택하세요.' });
      }
    },
    openVendorPopup() {
      $(".animation-content").addClass('pop_min2');
      $(".animation-content").draggable();

      let vm = this
      this.$modal.open({
        component: ApprLineSet,
        props: {
          lineList: this.apprLine,
          setUserId : this.$store.state.loginInfo.loginId,
        },
        parent: this,
        events: {
          close(data) {
            // Close event handler
            if(data.lineList) {
              vm.apprLine = data.lineList || []
            }
            vm.approval();
          }
        }
      })
    },
    loginInfoSet(){
      this.form.postDt = this.$moment().format("YYYYMMDD");
      this.form.userCd = this.$store.state.loginInfo.loginId;
      this.form.userNm = this.$store.state.loginInfo.userName;
      this.form.deptCd = this.$store.state.loginInfo.loginDeptCd;
      this.form.deptNm = this.$store.state.loginInfo.loginDeptNm;
      this.form.title = this.$moment(this.periodYm).format('YYYY')+"_"+this.$moment(this.periodYm).format('MM')+"_"+this.cctrNm;
      this.form.cctrCd = this.cctrCd;
      this.form.cctrNm = this.cctrNm;
    },
    isEmpty(value){
      if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
        return ""
      }else{
        return value
      }
    },
    openUploadEvidencePopup() {

      let rdoCtrl = true
      let disabled = true

      const slipNo = this.form.slipNo;

      if(slipNo) {

        this.$modal.open({
          component: EvidAtchPopModeless,
          props: {
            slipNo,
            disabled,
            rdoCtrl
          },
          parent: this,
          width: 1200
        });

      } else {
        this.$alert(`저장해야 증빙첨부가 가능합니다.`, '확인', {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '확인',
          type: 'error',
          center: true,
          callback: () => {}
        });
      }

    },
    openUploadWingsPopup(){

      let readOnly = false;

      const slipNo = this.form.slipNo;

      if(slipNo){
        this.$modal.open({
          component: WingsAtchPop,
          props: {
            slipNo,
            readOnly
          },
          parent: this,
          width: 1200,
          events: {
            close(value) {

            }
          }
        })
      } else {
        this.$alert(`저장해야 증빙첨부가 가능합니다.`, '확인', {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '확인',
          type: 'error',
          center: true,
          callback: () => {}
        });
      }
    },
  },
  created() {
  },
  computed: {
    getEvidFileSize() {
      return 0;
    },
    getJiniSize() {
      return 0;
    },
  },
  mounted() {
    if(this.flag === 'new') this.loginInfoSet();
    this.goSearch();
  },
}
</script>

<style lang="scss" scoped>

.modal-card {
  width: 1800px;
  height : 1500px;
}

</style>
