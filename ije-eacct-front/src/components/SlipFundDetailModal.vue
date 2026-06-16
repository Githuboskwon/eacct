<template>
  <layout>
    <span slot="header">{{title}} 상세<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>

    <div slot="content">
      <div class="table-area">
        <div class="table-header">
          <div class="content-name">
              <div class="table-name">
                  <h3 class="ico_table_name">{{title}} 상세</h3>
              </div>
          </div>
          <div class="table-a fixed">

            <div  class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
              <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px;">
                <button class="btn-size btn-blue" v-if="data.status === 'SV'" @click="approval()">
                  <span class="btn-icon"><i class="fas fa-pen-alt"></i></span> 상신
                </button>
                <button class="btn-size btn-blue" v-if="data.status === 'SV'" @click="openVendorPopup()">
                  <span class="btn-icon"><i class="fas fa-user-check"></i></span> 결재자 지정
                </button>
              </div>
            </div>
            <div v-if="isAprver" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
              <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px;">
                <button class="btn-size btn-blue" @click="reject()"><span class="btn-icon"><i
                    class="fas fa-pen-alt"></i></span> 반려
                </button>
              </div>
            </div>
            <div v-if="isAprver" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
              <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px; margin-right: 5px">
                <button class="btn-size btn-blue" @click="approve()"><span class="btn-icon"><i
                    class="fas fa-pen-alt"></i></span> 결재
                </button>
              </div>
            </div>

            <div v-if="isCancel" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
              <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px; margin-right: 5px">
                <button class="btn-size btn-orange" @click="cancelAppr()"><span class="btn-icon"><i
                    class="fas fa-pen-alt"></i></span> 상신취소
                </button>
              </div>
            </div>

            <div class="content">
              <div class="btn-wrap" >
                <table class="table">
                  <tbody>
                    <tr>
                      <th style="width: 140px;">작성번호</th>
                      <td colspan="3" >{{data.slipNo}}</td>
                      <th style="width: 140px;">회계일자</th>
                      <td colspan="3">{{getDateFormat(data.journalDt)}}</td>
                    </tr>
                    <tr>
                      <th>작성자</th>
                      <td colspan="3">{{data.chgNm}}({{data.chgId}})</td>
                      <th>소속부서</th>
                      <td colspan="3">{{data.deptNm}}({{data.deptCd}})</td>
                    </tr>
<!--                    <tr>-->
<!--                      <th>헤더적요</th>-->
<!--                      <td>{{data.headerRemark}}</td>-->
<!--                    </tr>-->
                  </tbody>
                </table>
              </div>
              </div>
          </div>
        </div>
      </div>

      <div class="table-area">
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

      <div class="table-area">
        <div class="table-header">
          <div class="table-a fixed">
            <div class="content">
              <div class="btn-wrap" >

                <table class="table">
                  <tbody>
                  <tr>
                    <th style="width: 140px;">배치ID</th>
                    <td colspan="3" >{{data.batchId}}</td>
                    <th style="width: 140px;">거래유형</th>
                    <td colspan="3">{{data.dealType}}</td>
                  </tr>
                  <tr>
                    <th>거래번호(DEAL)</th>
                    <td colspan="3">{{data.dealNum}}</td>
                    <th>상품유형</th>
                    <td colspan="3">{{data.productType}}</td>
                  </tr>
                  <tr>
                    <th>거래번호(EXP)</th>
                    <td colspan="3">{{data.transactionNum}}</td>
                    <th>처리유형</th>
                    <td colspan="3">{{data.transactionType}}</td>
                  </tr>
                  <tr>
                    <th>ERP작성자</th>
                    <td colspan="3">{{data.erpRegId}}</td>
                    <th>최초고시환율</th>
                    <td colspan="3">{{data.dailyRate}}</td>
                  </tr>
                  <tr>
                    <th>거래하위유형</th>
                    <td colspan="3">{{data.dealSubtype}}</td>
                    <th>체결환율</th>
                    <td colspan="3">{{data.transactionRate}}</td>
                  </tr>
                  <tr>
                    <th>전표일자</th>
                    <td colspan="3">{{getDateFormat(data.journalDt)}}</td>
                    <th>지급일자 <br/>원거래번호</th>
                    <td colspan="3">{{data.originTrxNum}}</td>
                  </tr>
                  </tbody>
                </table>

                <div class="table-b">
                  <div class="grid-wrap">
                    <ag-grid-vue ref="grid" style="width: 100%; height:350px;" class="ag-theme-alpine grid_search_height_350"
                                rowSelection="single" 
                                :columnDefs="columnDefs"
                                :rowData="data.dtList"
                                :gridOptions="gridOptions"
                                :defaultColDef="defaultColDef"
                                @grid-ready="onReady"
                                />
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
            </div>
          </div>
        </div>
      </div>
    </div>
  </layout>
</template>

<script>
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import _ from 'lodash'
import Layout from '@/components/ModalSlot2.vue'
import DhxCalendar from '@/components/DhxCalendar.vue'
import ApprActPop from '@/components/ApprActPop.vue'
import ApprLineSet from '@/components/ApprLineSet.vue'
import ApprBundlePopDrafter from "@/components/ApprBundlePopDrafter.vue"

import {
  url as _url
} from '@/libs/join'
import JiniAtchPop from "@/components/JiniAtchPop";
import EvidAtchPopModeless from "@/components/EvidAtchPopModeless";
import WingsAtchPop from "@/components/JiniAtchPop";

export default {
  name: 'SlipDetailModal',
  props: {
    slipNo: {
      Type: String,
      required: false
    },
    docType: {
      Type: String,
      required: false,
      default: 'slip',
    },
    title: {
      Type: String,
      default: '결재상신',
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
  components: {DhxCalendar, Layout},
  mixins: [ mixin, mixinSlip,],
  data() {
    return {
      columnDefs : [],
      gridOptions : {},
      defaultColDef: {
        resizable: true,
        filter: true,
        sortable: true,
      },
      gridApi : null ,    //gridApi
      columnApi : null ,  //columApi
      data: {},
      etaxXmlHd:[],
      sumValues : {
        enteredDr : 0,
        enteredCr : 0,
        accountedDr : 0,
        accountedCr : 0,
      },
      objectPopup: null,
      apprLine : [],
      apprRemark: '',
      apprDesc: '',
      apprTitle: '자금전표',
      isAprver: false,
      isCancel: false,
    }
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
          headerName: 'No.',
          field: 'rn',
          width: 100,
          colSpan:(params) => {
            if(params.data.rn === 'TOTAL'){
              return 5;
            }else{
              return 1;
            }
          },
          cellStyle:(params) => {
            if(params.data.rn === '합계'){
              return {textAlign: 'center'}
            }else{
              return {textAlign: 'left'}
            }
          },
          valueFormatter: (params) => {
            if(params.data.rn === 'TOTAL'){
              return 'TOTAL'
            }else{
              return params.node.rowIndex+1;
            }
          }
        },
        {
          headerName: '부서-계정',
          field: 'segment1_2',
          width: 180,
          cellStyle:{textAlign: 'left'},
          editable: false,
        },
        {
          headerName: '적요',
          field: 'description',
          width: 180,
          cellStyle:{textAlign: 'left'},
          editable: false,
        },
        {
          headerName: '은행명',
          field: 'bankNm',
          width: 180,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '계좌번호',
          field: 'accountNo',
          width: 180,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '외화',
          field: '',
          width: 300,
          children:[
            {
              headerName: '차변',
              field: 'debitAmt',
              width: 150,
              valueFormatter: (params) => {
                return vm.getDoubleNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '대변',
              field: 'creditAmt',
              width: 150,
              valueFormatter: (params) => {
                return vm.getDoubleNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
          ],
          editable: false,
        },
        {
          headerName: '원화',
          field: '',
          width: 300,
          children:[
            {
              headerName: '차변',
              field: 'accountedDr',
              width: 150,
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '대변',
              field: 'accountedCr',
              width: 150,
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
          ],
          editable: false,
        },
      ]
    },
    getAppr() {
      this.$http.post('/api/erp/slip/fund/detail', {
        compCd: this.$store.state.loginInfo.compCd,
        slipNo: this.slipNo,
      })
      .then((response) => {
        this.data = response.data;
        this.data.dtList.map((e) => {
          e.deptAcctProductCd = e.segment3 + '-' + e.segment4 + '-' + e.segment5;
          e.deptAcctProductNm = e.segment3Desc + '-' + e.segment4Desc + '-' + e.segment5Desc;
        })
        this.sumValues.enteredDr = this.data.dtList.filter(x => x.enteredDr).map(x => x.enteredDr).reduce((a,x) => a+x, 0);
        this.sumValues.enteredCr = this.data.dtList.filter(x => x.enteredCr).map(x => x.enteredCr).reduce((a,x) => a+x, 0);
        this.sumValues.accountedDr = this.data.dtList.filter(x => x.accountedDr).map(x => x.accountedDr).reduce((a,x) => a+x, 0);
        this.sumValues.accountedCr = this.data.dtList.filter(x => x.accountedDr).map(x => x.accountedDr).reduce((a,x) => a+x, 0);
        if(this.data.status === 'SV') {
          this.getMainApprLine();
        } else {
          this.getApprInfo();
        }

        this.makeSumData();
        this.gridOptions.getRowStyle  = function(params){
          if(params.data.rn === 'TOTAL'){
            return {background: '#f9f9f3'};
          }
        }

        // 증빙 첨부/문서 갯수 동적으로 체크하도록 변경
        this.$store.commit('setEvidFileSize', this.data.ufileCnt);
        this.$store.commit('setJiniSize', this.data.jiniCnt);

      })
      .catch((e) => {
        console.log(e);
      })
    },
    getDateFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD')
      }
    },
    getDoubleNumberFormat(val) {
      if(val) {
        return this.$numeral(val).format('0,0.00');
      }
    },
    getSumItemList(params) {
      return
    },
    closeModal(){
      this.$parent.close()
    },
    makeSumData(){
      const initValue = 0;

      console.log(this.data.dtList);

      let sum = [{
        rn: 'TOTAL',
        debitAmt: this.data.dtList.reduce((prev, next) => {return Number(prev) + Number(next.debitAmt)}, initValue),
        creditAmt: this.data.dtList.reduce((prev, next) => {return Number(prev) + Number(next.creditAmt)}, initValue),
        accountedDr: this.data.dtList.reduce((prev, next) => {return Number(prev) + Number(next.accountedDr)}, initValue),
        accountedCr: this.data.dtList.reduce((prev, next) => {return Number(prev) + Number(next.accountedCr)}, initValue)
      }]

      this.gridOptions.api.setPinnedBottomRowData(sum);
    },
    openUploadEvidencePopup() {

      let rdoCtrl = true
      let disabled = true

      if(this.data.status !== 'SV' ) {
        disabled = true
        rdoCtrl = true
      } else {
        disabled = false
        rdoCtrl = false
      }

      const slipNo = this.data.slipNo;

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

      }

    },
    openUploadWingsPopup(){

      let readOnly = true;

      if(this.data.status !== 'SV' ) {
        readOnly = true
      } else {
        readOnly = false
      }

      const slipNo = this.data.slipNo;

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
    approval() {
      const that = this;
      if (this.apprLine.length < 1) {
        this.$swal({type: 'warning', text: '결재선을 지정해주세요.'});
        return false;
      }

      let docTitleNm = that.isEmpty(that.data.remark) === "" ? this.$numeral(that.data.slipAmt).format('0,0') : this.isEmpty(that.data.remark) + ' / ' + this.$numeral(that.data.slipAmt).format('0,0');
      this.addApprDesc(docTitleNm, 'submit').then(() => {
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

            this.$http.put(`/api/appr/detail/req`, {
              compCd : that.$store.state.loginInfo.compCd,
              apprGroupId : that.data.slipHeaderId,
              slipHeaderId: that.data.slipHeaderId,
              slipType: that.data.slipType,
              slipNo : that.data.slipNo,
              docTitleNm : docTitleNm,
              approvalDetails: that.apprLine,
              docContents : that.data.apprRemark,
              loginId : that.$store.state.loginInfo.loginId,
              draftId : that.$store.state.loginInfo.loginId,
            })
            .then((response) => {
              that.$store.commit('finish');
              that.$swal({type: 'info', text: '상신이 완료되었습니다.'})
                  .then((result)=>{
                    if(result.value){
                      that.$emit('close','Y');
                      this.$parent.$parent.goSearch();
                    }
                  });
              this.$parent.$parent.search();
            })
            .catch((e) => {
              that.$store.commit('finish');
              this.$swal({ type: 'error', text: e.data.message })
            });
          }
        })
      })
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
          }
        }
      })
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
    isEmpty(value){
      if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
        return ""
      }else{
        return value
      }
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
                    this.$parent.$parent.goSearch();
                    this.$emit('close');
                  }).catch(e => {
                this.$swal({ type: 'error', text: e.response.data.message })
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
      if(this.data.status === 'AP' && this.apprHeader.nextAppUserId === this.$store.state.loginInfo.loginId) {
        this.isAprver = true;
      }
      if(this.data.status === 'AP' && this.apprHeader.draftId === this.$store.state.loginInfo.loginId) {
        this.isCancel = true;
      }
    },
    cancelAppr(){

      const vm = this;

      const rows = [{
        slipNo : this.data.slipNo,
        apprGroupId : this.data.slipHeaderId,
        slipHeaderId : this.data.slipHeaderId,
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
                this.$swal({ type: 'success', text: '상신을 취소하였습니다.' })
                    .then((result) => {
                      if (result.value) {
                        vm.$parent.search();
                        vm.$parent.goSearch();
                        this.$emit('close');
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
              .then(res => {
                this.$message({type: 'success', message: `반려가 완료되었습니다.`});
                this.$parent.$parent.goSearch();
                this.$emit('close');
              }).catch(error => {
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
  },
  created(){
      this.getAppr();
      this.createColumnDefs();
      // if(this.docType === 'slip') {
      //   this.getMainApprLine();
      // } else {
      //   this.getApprInfo();
      // }
  },
  mounted() {
  },
  computed: {
    getEvidFileSize() {
      return this.$store.state.evidFileSize;
    },
    getJiniSize() {
      return this.$store.state.jiniSize;
    },
  },
  watch: {
    'title': {
      immediate: true,
      deep: true,
      handler(value) {
          if(value !== undefined) {
              this.title = value
          }
      }
    },
  },
};
</script>

<style scoped>
div#gridbox {
  width: 100%;
  height: 100%;
  min-height: 300px;
}
div.modal-card {
  width: 100%;
}
table tr th {
    text-align: center
}

.rightDt {
    text-align: right
}
.modal .pop-content .btn-type1 {
    float: right;
    text-align: right;
    /* margin-bottom: 16px; */
}
</style>
