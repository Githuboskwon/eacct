<template>
  <layout>
  <span slot="header">
    {{ title }} 상세
    <button class="btn-pop-close delete" aria-label="close" @click="closeModal()"></button>
  </span>
    <div slot="content">

        <div v-if="isModify" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
          <div class="btn-type1 clearfix" style="float:right; margin-bottom: 5px;">
            <el-button size="large" type="primary" icon="el-icon-folder-opened" @click="openVendorPopup()">상신</el-button>
            <el-button size="large" type="danger" icon="el-icon-delete" @click="deleteSlip()">삭제</el-button>
            <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save()">저장</el-button>
          </div>
        </div>

        <div v-if="isAprver" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
          <div class="btn-type1 clearfix" style="float:right; margin-bottom: 5px;">
            <el-button size="large" type="danger" icon="el-icon-refresh" @click="reject()">반려</el-button>
            <el-button size="large" type="success" icon="el-icon-check" @click="approve()">결재</el-button>
          </div>
        </div>

        <div v-if="isCancel" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
          <div class="btn-type1 clearfix" style="float:right; margin-bottom: 5px; margin-right: 5px">
            <el-button size="large" type="danger" icon="el-icon-close" @click="cancelAppr()">상신취소</el-button>
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
                    <td colspan="3"  v-if="isModify"><input class="input" type="text" v-model="form.title" /></td>
                    <td colspan="3"  v-else>{{form.title}}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>


          <div class="table-b">
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
            <div v-if="isModify" class="btn-type2 clearfix">
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


          <div v-if="isModify">
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
          <div v-else>
            <el-input
                type="textarea"
                placeholder=""
                v-model="form.remark"
                :autosize="{ minRows: 5, maxRows: 8}"
                maxlength="4000"
                disabled
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
import ApprActPop from "@/components/ApprActPop";
import ApprBundlePopDrafter from "@/components/ApprBundlePopDrafter";
import EvidAtchPopModeless from "@/components/EvidAtchPopModeless";
import WingsAtchPop from "@/components/JiniAtchPop";

//['docMngNo', 'value', 'readonly']
export default {
  props: {
    docType: {
      Type: String,
      required: false,
      default: 'slip',
    },
    slipNo: {
      type: String,
      required: false
    },
    slipHeaderId: {
      type: Number,
      required: false
    },
    title: {
      type: String,
      required: false,
      default: '비용예산 상세'
    },
    slipTypeCd: {
      Type:String,
      required:false,
    },
    status:{
      Type:String,
      required:false,
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
      apprDesc:'',
      apprLine : [],
      isAprver: false,
      isCancel: false,
      isModify: false,
    }
  },
  methods: {
    closeModal() {
      this.$parent.close()
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
          , checkboxSelection: that.isModify
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
          editable : that.isModify
        },
        {
          headerName: '당월(사업계획 대비 비용예산)',
          children:[
            {
              headerName: '초과금액',
              field: 'curOverAmt',
              width: 200,
              editable: that.isModify,
              cellRenderer: 'numberInput',
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '초과사유',
              field: 'curOverReason',
              width: 550,
              cellStyle:{textAlign: 'left'},
              editable : that.isModify
            },
          ]
        },
        {
          headerName: '전월(비용예산 대비 실적)',
          children:[
            {
              headerName: '초과금액',
              field: 'pastOverAmt',
              width: 200,
              editable: that.isModify,
              cellRenderer: 'numberInput',
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '초과사유',
              field: 'pastOverReason',
              width: 550,
              cellStyle:{textAlign: 'left'},
              editable : that.isModify
            },
          ]
        },
      ];


    },
    goSearch(){
      const vm = this;

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
              vm.setSlipData(response.data.data[0]); // 문서 데이터
              vm.data = response.data.list; // 리스트
              vm.subData = response.data.dtList; // 추가 리스트
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
      this.form.cctrCd = data.cctrCd;
      this.form.cctrNm = data.cctrNm;
      this.form.title = data.headerRemark;
      this.form.remark = data.remark;
      this.form.periodYear = data.periodYear;
      this.form.periodMonth = data.periodMonth;

      if(this.status === 'SV') {
        this.checkButtonUser2();
      } else {
        this.getApprInfo();
      }


      // 증빙 첨부/문서 갯수 동적으로 체크하도록 변경
      this.$store.commit('setEvidFileSize', data.ufileCnt);
      this.$store.commit('setJiniSize', data.jiniCnt);

      this.makeColDef();
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
                // that.search();
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
    openApprPopup() {
      let vm = this
      this.$modal.open({
        component: ApprActPop,
        props: {
          docTitleNm: this.docTitleNm,
          apprNo: this.apprNo,
          apprSeq: this.apprSeq
        },
        parent: this,
        events: {
          close(data) {
            // Close event handler
            console.log('Appr Popup Close Event!!'+ data)
            vm.getAppr(data)
          }
        }
      })
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
    addApprDesc(docTitleNm, docType){
      return new Promise((resolve,reject) => {
        const vm = this
        this.$modal.open({
          component:ApprBundlePopDrafter,
          props:{
            docTitleNm,
            docType,
          },
          events: {
            close(data) {
              // Close event handler
              if(!_.isEmpty(data.apprDesc) || !_.isEmpty(data.slipReusePossibleYn)){
                //기안자 결재의견 set
                vm.apprRemark = data.apprDesc;
                vm.apprDesc = data.apprDesc;
                vm.slipReusePossibleYn = data.slipReusePossibleYn;

                return resolve()
              }else{
                return reject()
              }
            }
          }
        })
      })
    },
    approve() {
      const vm = this;
      this.addApprDesc('결재', 'appr')
          .then(() => {
            this.$confirm(`결재를 계속 하시겠습니까?`, `${this.docTitleNm}`, {
              confirmButtonText: '예',
              cancelButtonText: '아니오',
              type: 'success'
            }).then(() => {
              const params = {
                slipNo: this.slipNo, //전표번호
                apprGroupId: this.apprHeader.apprGroupId, //전표 헤더 그룹 아이디
                aaprverId: this.$store.state.loginInfo.loginId,
                slipType: this.apprHeader.slipType, //거래유형 코드
                apprDesc: this.apprDesc,
              };
              this.$store.commit('loading');
              this.$http.post(`/api/appr/detail/doApproval`, params)
                  .then(res => {
                    this.$swal({type: 'info', text: '결재가 완료되었습니다.'})
                    vm.$parent.search();
                    vm.$parent.goSearch();
                  }).catch(e => {
                this.$swal({ type: 'error', text: e.response.data.message })
              }).finally(_ => {
                this.$store.commit('finish');
                this.$emit('close');
              });
            }).catch((e) => {
              console.log(e)
              // this.$message({
              //     type: 'info',
              //     message: '취소하였습니다.'
              // });
            });
          });
    },
    getMainApprLine() {
      this.$http.post(`/api/apprLine/main`, {
        compCd: this.$store.state.loginInfo.compCd,
        userId : this.$store.state.loginInfo.loginId,
        mainApprYn: 'Y',
        useYn: 'Y',
      })
      .then(res => {
        let lineList = []
        res.data.forEach(e => {
          let apprTypeNm = '';
          if(e.apprTypeCd==='20') apprTypeNm = '결재'
          else if(e.apprTypeCd==='30') apprTypeNm = '합의'
          lineList.push(
              {
                aprverId: e.apprUserId,
                aprverUser : e.apprUserNm,
                jobCd: e.jobCd,
                jobNm : e.jobNm,
                cctrCd: e.cctrCd,
                cctrNm : e.cctrNm,
                apprTypeCd: e.apprTypeCd,
                apprType: apprTypeNm,
                serveCd: e.serveCd,
              }
          )
        })
        this.apprLine = lineList || [];
      })
    },
    getApprInfo() {
      this.$http.get(`/api/appr/detail/${this.slipNo}`)
          .then(res => res.data)
          .then(data => {
            this.apprLine = data.apprDetails;
            this.apprHeader = data.apprHeader[0];
            this.docTitleNm = this.apprHeader.docTitleNm;
            this.checkButtonUser();
          })
    },
    checkButtonUser() {

      if(this.form.status === 'AP' && this.apprHeader.nextAppUserId === this.$store.state.loginInfo.loginId) {
        this.isAprver = true;
        this.isCancel = false;
      }
      if(this.form.status === 'AP' && this.apprHeader.draftId === this.$store.state.loginInfo.loginId) {
        this.isCancel = true;
      }

    },
    checkButtonUser2() {

      if(this.form.status === 'SV' && this.form.userCd === this.$store.state.loginInfo.loginId) {
        this.isModify = true;
      }

    },
    cancelAppr(){

      const vm = this;

      const rows = [{
        slipNo : this.slipNo,
        apprGroupId : this.slipHeaderId,
        slipHeaderId : this.slipHeaderId,
        draftId : this.apprHeader.draftId
      }];

      this.$swal({
        type: 'question',
        html: '상신 취소하시겠습니까?',
        showCancelButton: true
      }).then(r => {
        if (r.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/appr/bundle/cancel`, rows)
              .then((response) => {
                vm.$store.commit('finish');
                vm.$swal({type: 'info', text: '상신을 취소하였습니다.'})
                    .then((result)=>{
                      if(result.value){
                        vm.$emit('close','Y');
                      }
                    });
              })
              .catch((e) => {
                this.$swal({ type: 'warning', text: e.data.message });
              })
              .finally(() => {
                this.$store.commit('finish')
              });
        }
      })


    },
    reject() {

      const that = this;

      let docType = 'reject';
      if(this.data.status === 'CP') {
        docType = 'verifyReject'
      }
      this.addApprDesc('반려', docType).then(() => {
        this.$confirm(`반려를 계속 하시겠습니까?`, `${this.docTitleNm}`, {
          confirmButtonText: '예',
          cancelButtonText: '아니오',
          type: 'success'
        }).then(() => {
          const params = {
            slipNo: this.slipNo, //전표번호
            approvalGroupId: this.apprHeader.apprGroupId, //전표 헤더 그룹 아이디
            aaprverId: this.$store.state.loginInfo.loginId,
            apprDesc: this.apprDesc,
            slipReusePossibleYn: this.slipReusePossibleYn,
          };

          this.$store.commit('loading');
          this.$http.post(`/api/appr/detail/rej`, params)
              .then((response) => {
                that.$store.commit('finish');
                that.$swal({type: 'info', text: '상신을 취소하였습니다.'})
                    .then((result)=>{
                      if(result.value){
                        that.$emit('close','Y');
                      }
                    });
              })
              .catch(error => {
            console.log("err ", error)
            this.$message.error({type: 'error', message: `${error.message}`});
          }).finally(_ => {
            this.$store.commit('finish');
          });
        }).catch((e) => {
          console.log(e)
          // this.$message({
          //     type: 'info',
          //     message: '취소하였습니다.'
          // });
        });
      });
    },
    isInit(){
      this.form.slipNo = this.slipNo;
      this.form.slipHeaderId = this.slipHeaderId;
      this.form.status = this.status;
    },
    openUploadEvidencePopup() {

      let rdoCtrl = true
      let disabled = true

      if(this.status !== 'SV' ) {
        disabled = true
        rdoCtrl = true
      } else {
        disabled = false
        rdoCtrl = false
      }

      const slipNo = this.slipNo;

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

      let readOnly = true;

      if(this.status !== 'SV' ) {
        readOnly = true
      } else {
        readOnly = false
      }

      const slipNo = this.slipNo;

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
      return this.$store.state.evidFileSize;
    },
    getJiniSize() {
      return this.$store.state.jiniSize;
    },
  },
  mounted() {
    this.isInit();
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
