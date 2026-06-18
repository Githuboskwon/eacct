<template>
  <layout>
    <span slot="header">{{title}} 상세<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>

    <div slot="content">

      <div  class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
        <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px;">
          <button class="btn-size btn-blue" v-if="data.status === 'SV'" @click="requestApproval()">
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
<!--      <div v-if="docType === 'appr' && status === 'CP'" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">-->
<!--        <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px;">-->
<!--          <button class="btn-size btn-blue" @click="verify()"><span class="btn-icon"><i-->
<!--              class="fas fa-pen-alt"></i></span> 검인-->
<!--          </button>-->
<!--        </div>-->
<!--      </div>-->

<!--      <div v-if="isReuse" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">-->
<!--        <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px; margin-right: 5px">-->
<!--          <button class="btn-size btn-orange" @click="reuse()"><span class="btn-icon"><i-->
<!--              class="fas fa-pen-alt"></i></span> 전표 재사용-->
<!--          </button>-->
<!--        </div>-->
<!--      </div>-->
      <div v-if="isCancel" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
        <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px; margin-right: 5px">
          <button class="btn-size btn-orange" @click="cancelAppr()"><span class="btn-icon"><i
              class="fas fa-pen-alt"></i></span> 상신취소
          </button>
        </div>
      </div>

      <div class="table-area">
        <div class="table-header">
          <div class="content-name">
              <div class="table-name">
                  <h3 class="ico_table_name">{{title}} 상세</h3>
              </div>
          </div>
          <div class="table-a fixed">
            <div class="content">
              <div class="btn-wrap" >
                <table class="table">
                  <tbody>
                    <tr>
                      <th style="width:130px">작성번호</th>
                      <td colspan="3" >{{data.slipNo}}</td>
                      <th style="width:130px">회계일자</th>
                      <td colspan="1">{{getDateFormat(data.postingDt)}}</td>
                    </tr>
                    <tr>
                      <th style="width:130px">작성자</th>
                      <td colspan="3">{{data.regNm}}</td>
                      <th style="width:130px">소속부서</th>
                      <td colspan="1">{{data.deptNm}}</td>
                    </tr>
                    <tr>
                      <th style="width:130px">헤더적요</th>
                      <td colspan="5">{{data.headerRemark}}</td>
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
                        <td style="text-align: center;">{{item.apprDtm ? $moment(item.apprDtm).format('YYYY-MM-DD HH:mm:ss') : null}}</td>
                        <td style="border-right: 1px solid #adadad">{{item.apprDesc}}</td>
                    </tr>
                </tbody>
            </table>
          </div>

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
                      <!-- <th rowspan="3" style="width: 50px;">전표</th> -->
                      <th>분개명</th>
                      <td colspan="3">{{data.headerNm}}</td>
                      <th>GL일자</th>
                      <td colspan="3">{{getDateFormat(data.glDt)}}</td>
                    </tr>
                    <tr>
                      <th>배치명</th>
                      <td colspan="3">{{data.batchNm}}</td>
                      <th>작성자</th>
                      <td colspan="3">{{data.regNm}}</td>
                    </tr>
                    <tr>
                      <th>원천</th>
                      <td>{{data.sourceNm}}</td>
                      <th>출처</th>
                      <td>{{data.categoryNm}}</td>
                      <th>생성일시</th>
                      <td colspan="3">{{getTimeFormat(data.regDtm)}}</td>
                    </tr>
                    <tr>
                      <th>통화</th>
                      <td>{{data.currencyCd}}</td>
                      <th>환율</th>
                      <td>{{data.currencyRate}}</td>
                      <th>환율일자</th>
                      <td colspan="3">{{getDateFormat(data.currencyDt)}}</td>
                    </tr>
                  </tbody>
                </table>
                <div class="table-b">
                  <div class="grid-wrap">
                    <ag-grid-vue ref="grid" style="width: 100%; height:300px;" class="ag-theme-alpine grid_search_height_350"
                                rowSelection="single" 
                                :columnDefs="columnDefs"
                                :rowData="data.detailList"
                                :gridOptions="gridOptions"
                                :defaultColDef="defaultColDef"
                                @row-selected="onRowSelected"
                                @grid-ready="onReady"
                                />
                  </div>
                </div>

                <table class="table">
                  <tbody>
                    <tr>
                      <th rowspan="4" style="width: 140px;">계정정보</th>
                      <th style="width: 140px;">계정</th>
                      <td colspan="2">{{coaCd}}</td>
                    </tr>
                    <tr>
                      <th style="width: 140px;">계정명</th>
                      <td colspan="2">{{coaDesc}}</td>
                    </tr>
                  </tbody>
                </table>

                <table class="table">
                  <tr>
                    <th rowspan="3">합계</th>
                    <th colspan="2">외화금액</th>
                    <th colspan="2">원화금액</th>
                  </tr>
                  <tr>
                    <th>차변</th>
                    <th style="height:30px;">대변</th>
                    <th>차변</th>
                    <th>대변</th>
                  </tr>
                  <tr>
                    <td class="rightDt" style="height: 30px;">{{getDoubleNumberFormat(sumValues.enteredDr)}}</td>
                    <td class="rightDt">{{getDoubleNumberFormat(sumValues.enteredCr)}}</td>
                    <td class="rightDt">{{getNumberFormat(sumValues.accountedDr)}}</td>
                    <td class="rightDt">{{getNumberFormat(sumValues.accountedCr)}}</td>
                  </tr>
                </table>
                <div class="table-area" style="height: 60px;">
                  <div class="file has-name" style="width: 1000px;">
                    <div class="file" style="margin-right: 10px;">
                      <div class="file-label" @click="openUploadEvidencePopup()">
                          <span class="file-cta">
                            <span class="file-label"> 증빙첨부</span>
                            <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                          </span>
                          <span class="file-name">{{ this.$numeral(data.ufileCnt).format('00') }}<i class="far fa-file-alt"></i></span>
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
import JiniAtchPop from '@/components/JiniAtchPop.vue'
import ApprActPop from '@/components/ApprActPop.vue'
import ApprLineSet from '@/components/ApprLineSet.vue'
import ApprBundlePopDrafter from "@/components/ApprBundlePopDrafter.vue"

import {
  url as _url
} from '@/libs/join'
import EvidAtchPopModeless from "@/components/EvidAtchPopModeless.vue";
import WingsAtchPop from "@/components/JiniAtchPop";

export default {
  compatConfig: { MODE: 2 },
  name: 'SlipDetailModal',
  props: {
    slipNo: {
      Type: String,
      required: false
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
      slipType: '27',
      objectPopup: null,
      apprLine : [],
      apprRemark: '',
      apprDesc: '',
      apprTitle: 'GL전표',
      isAprver: false,
      isCancel: false,
      isReuse: false,
      coaCd: '',
      coaDesc: '',
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
          headerName: "No.",
          width: 65,
          valueFormatter: (params) => {
            return params.node.rowIndex + 1;
          },
        },
        {
          headerName: '계정명',
          field: 'coaDesc',
          width: 250,
          cellStyle:{textAlign: 'left'},
          editable: false,
        },
        {
          headerName: '적요',
          field: 'remark',
          width: 300,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '외화금액',
          field: '',
          width: 260,
          children:[
            {
              headerName: '차변',
              field: 'enteredDr',
              width: 130,
              valueFormatter: (params) => {
                return vm.getDoubleNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '대변',
              field: 'enteredCr',
              width: 130,
              valueFormatter: (params) => {
                return vm.getDoubleNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
          ],
          editable: false,
        },
        {
          headerName: '원화금액',
          field: '',
          width: 260,
          children:[
            {
              headerName: '차변',
              field: 'accountedDr',
              width: 130,
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '대변',
              field: 'accountedCr',
              width: 130,
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
    getSlip() {
      this.$http.post('/api/erp/slip/gl/detail', {
        compCd: this.$store.state.loginInfo.compCd,
        slipNo: this.slipNo,
        slipType: this.slipType,
      })
      .then((response) => {
        this.data = response.data;
        this.coaCd = this.data.detailList[0].coaCd;
        this.coaDesc = this.data.detailList[0].coaDesc;
        this.sumValues.enteredDr = this.data.detailList.filter(x => x.enteredDr).map(x => x.enteredDr).reduce((a,x) => a+x, 0);
        this.sumValues.enteredCr = this.data.detailList.filter(x => x.enteredCr).map(x => x.enteredCr).reduce((a,x) => a+x, 0);
        this.sumValues.accountedDr = this.data.detailList.filter(x => x.accountedDr).map(x => x.accountedDr).reduce((a,x) => a+x, 0);
        this.sumValues.accountedCr = this.data.detailList.filter(x => x.accountedDr).map(x => x.accountedDr).reduce((a,x) => a+x, 0);
        if(this.data.status === 'SV') {
          this.data.regId = this.$store.state.loginInfo.loginId;
          this.data.regNm = this.$store.state.loginInfo.userName;
          this.data.deptNm = this.$store.state.loginInfo.loginDeptNm;
          this.getMainApprLine();
        }else {
          this.getApprInfo();
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
    getTimeFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
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
    openUploadEvidencePopup() { //증빙첨부 팝업
      let rdoCtrl = true
      let disabled = true

      if(this.data.status !== 'SV') {
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
      }
    },
    requestApproval() {
      const that = this;

      if (this.apprLine.length < 1) {
          this.$swal({type: 'warning', text: '결재선을 지정해주세요.'});
          return false;
      }
      let docTitleNm = this.data.headerRemark + ' / ' + that.$numeral(that.sumValues.accountedDr).format('0,0');
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
                apprGroupId : that.data.slipHeaderId,
                slipType: that.data.slipType,
                slipNo : that.data.slipNo,
                slipHeaderId: that.data.slipHeaderId,
                docTitleNm : that.data.headerRemark + ' / ' + that.$numeral(that.sumValues.accountedDr).format('0,0'),
                approvalDetails: that.apprLine,
                docContents : that.apprRemark,
                loginId : that.$store.state.loginInfo.loginId,
                glDate: this.$moment(that.data.glDt).format("YYYYMMDD"),
                draftId : that.$store.state.loginInfo.loginId,
            })
            .then((response) => {
                that.$swal({type: 'info', text: '상신이 완료되었습니다.'})
                this.$parent.$parent.goSearch();
                this.$emit('close');
            })
            .catch((e) => {
                that.$store.commit('finish');
                that.$swal({ type: 'error', text: e.message })
            }).finally(_ => {
              this.$store.commit('finish');
            });
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
            this.$parent.$parent.goSearch();
            this.$emit('close');
          }).catch(e => {
            this.$swal({ type: 'error', text: e.message })
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
    addApprDesc(title, docType){
      return new Promise((resolve,reject) => {
        const vm = this
        this.$modal.open({
          component:ApprBundlePopDrafter,
          props:{
            docTitleNm: title,
            docType: docType,
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
    /**
     * 검인
     */
    verify() {
      this.addApprDesc('검인', 'verify')
      .then(() => {
        this.$confirm(`검인을 계속 하시겠습니까?`, `${this.docTitleNm}`, {
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
          this.$http.post(`/api/appr/detail/verify`, params)
          .then(res => {
            this.$swal({type: 'info', text: '검인이 완료되었습니다.'})
            this.$parent.$parent.goSearch();
            this.$emit('close');
          }).catch(e => {
            this.$swal({ type: 'error', text: e.message })
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
    checkButtonUser() {
      if(this.data.status === 'AP' && this.apprHeader.nextAppUserId === this.$store.state.loginInfo.loginId) {
        this.isAprver = true;
      }
      if(this.data.status === 'AP' && this.apprHeader.draftId === this.$store.state.loginInfo.loginId) {
        this.isCancel = true;
      }
      if(this.status !== 'SV' && this.apprHeader.draftId === this.$store.state.loginInfo.loginId) {
        this.isReuse = true;
      }
    },
    cancelAppr(){
      const vm = this;

      this.$swal({
        type: 'question',
        html: '상신 취소하시겠습니까?',
        showCancelButton: true
      }).then(r => {
        if (r.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/appr/detail/cancel`, {
            slipNo : this.data.slipNo,
            apprGroupId : this.data.slipHeaderId,
            draftId : this.apprHeader.draftId
          })
          .then((response) => {
            this.$swal({ type: 'success', text: '상신을 취소하였습니다.' })
                .then((result) => {
                  if (result.value) {
                    this.$parent.$parent.goSearch();
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
      if(this.status === 'CP') {
        docType = 'verifyReject'
      }
      this.addApprDesc('반려', docType)
      .then(() => {
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
    reuse() {
      this.$confirm(`전표를 재사용하시겠습니까?`, `전표 재사용`, {
        confirmButtonText: '예',
        cancelButtonText: '아니오',
        type: 'success'
      }).then(() => {
        const params = {
          compCd: this.$store.state.loginInfo.compCd,
          slipNo: this.slipNo, //전표번호
          slipReusePossibleYn : 'Y',
        };
        this.$store.commit('loading');
        this.$http.post(`/api/slip/reuse`, params)
        .then(res => {
          this.$message({type: 'success', message: '전표가 재사용되었습니다.'});
          this.$parent.$parent.goSearch();
          this.$emit('close');
        }).catch(error => {
          this.$message.error({type: 'error', message: error.message});
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
    },
    onRowSelected(params) {
      const rowIndex = params.rowIndex;
      this.coaCd = this.data.detailList[rowIndex].coaCd;
      this.coaDesc = this.data.detailList[rowIndex].coaDesc;
    },
  },
  created(){

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
    this.getSlip();
    this.createColumnDefs();
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
