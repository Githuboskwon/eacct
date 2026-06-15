<template>
    <layout style="width:100%;">
        <span slot="header">
            {{ title }}
            <button class="btn-pop-close delete" aria-label="close" @click="close"></button>
        </span>
        <div slot="content">
            <div class="btn-wrap btn-type1 clearfix" v-if="(values.slipNo || slipNo) && readOnly !== true " style="float:right;">
                <el-button size="midium" type="primary" v-if="isAprver && slipNo" @click="approve" icon="el-icon-folder-checked">결재</el-button>
                <el-button size="midium" type="primary" v-if="isVerifier" @click="verify" icon="el-icon-folder-checked">검인</el-button>
                <el-button size="midium" type="success" v-if="isAprver || isVerifier || isAccount || $route.name==='slipMng'" @click="reject" icon="el-icon-user">반려</el-button>
<!--                <el-button type="success" v-if="isReuse" @click="reuse" icon="el-icon-user">전표 재사용</el-button>-->
                <el-button size="midium" type="success" v-if="isCancel" @click="cancelAppr" icon="el-icon-user">상신취소</el-button>
            </div>
            <div class="btn-wrap btn-type1 clearfix" v-if="(values.slipNo || slipNo) && status === 'SV' && readOnly !== true " style="float:right;">
                <el-button size="midium" type="primary" @click="review" icon="el-icon-folder-checked">상신</el-button>
                <el-button size="midium" type="success" @click="designateApprover" icon="el-icon-user">결재자 지정</el-button>
            </div>
            <div class="table-area" style="margin-top: 0px;">
              <!-- 결재선 -->
              <top :apprHeader="apprHeader" :slipNo="slipNo" :appr-line="apprLine" :ref-user-id="refUserId" :ref-list="refList" v-model="data" @approvalLine="designateApproverLineSetCallback"></top>
              <!-- 헤더 -->
              <mid ref="mid" :values="data" :slipNo="slipNo"></mid>
              <!-- 상세내역 -->
              <bottom ref="bottom" :slipTypeCd="data.slipTypeCd" :slipNo="slipNo" :gridData="data.rowData"></bottom>
              <!-- 증빙파일 등 -->
              <!-- 파일 업로드 -->
              <div class="file-upload mt20" v-if="showCardException">
                  <div class="table-name">
                    <h3 class="ico_table_name">파일보기</h3>
                  </div>
                  <div class="file has-name" style="width: 1200px;">
                      <div class="file" style="margin-right: 10px;">
                          <div class="file-label" @click="openUploadWingsPopup()">
                            <span class="file-cta">
                                <span class="file-label"> 그룹웨어 문서(URL)</span>
                                <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                            </span>
                              <span class="file-name" style="border-top-right-radius:0px; border-bottom-right-radius:0px;">
                                  {{ this.$numeral(data.jiniCnt).format('00') }}<i class="far fa-file-alt"></i></span>
                          </div>
                          <div class="file-label" @click="openUploadGroupwarePopup()">
                            <span class="file-cta" style="border-left:0px; border-top-left-radius:0px; border-bottom-left-radius:0px;">
                                <span class="file-label"> 그룹웨어 문서(PDF)</span>
                                <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                            </span>
                              <span class="file-name">{{ this.$numeral(data.jiniFileCnt).format('00') }}<i class="far fa-file-alt"></i></span>
                          </div>
                      </div>

                      <div class="file">
                          <div class="file-label" @click="openUploadEvidencePopup()">
                            <span class="file-cta">
                                <span class="file-label"> 증빙첨부</span>
                                <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                            </span>
                            <span class="file-name">{{ this.$numeral(data.fileCnt).format('00') }}<i class="far fa-file-alt"></i></span>
                          </div>
                      </div>
                  </div>
              </div>
              <div class="file-upload mt20" v-else-if="showWingsAtchPop">
                  <div class="table-name">
                      <h3 class="ico_table_name">파일보기</h3>
                  </div>
                  <div class="file has-name" style="width: 1200px;">
                      <div class="file" style="margin-right: 10px;">
                          <div class="file-label" @click="openUploadWingsPopup()">
                              <span class="file-cta">
                                  <span class="file-label"> 그룹웨어 문서</span>
                                  <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                              </span>
                              <span class="file-name">{{ this.$numeral(data.jiniCnt).format('00') }}<i class="far fa-file-alt"></i></span>
                          </div>
                      </div>

                      <div class="file">
                          <div class="file-label" @click="openUploadEvidencePopup()">
                              <span class="file-cta">
                                  <span class="file-label"> 증빙첨부</span>
                                  <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                              </span>
                              <span class="file-name">{{ this.$numeral(data.fileCnt).format('00') }}<i class="far fa-file-alt"></i></span>
                          </div>
                      </div>
                  </div>
              </div>
              <div class="file-upload mt20" v-else>
                  <div class="table-name"><h3 class="ico_table_name">파일보기</h3></div>
                  <div class="file has-name">
                      <div class="file-label" @click="openUploadEvidencePopup()">
                          <span class="file-cta">
                              <span class="file-label">증빙첨부</span>
                              <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                          </span>
                          <span class="file-name">{{ this.$numeral(data.fileCnt).format('00') }}<i class="far fa-file-alt"></i></span>
                      </div>
                  </div>
              </div>
              <!-- 비고 -->
              <div class="table-b">
                  <div class="table-header">
                      <div class="table-name">
                          <h3 class="ico_table_name">비고</h3>
                      </div>
                  </div>
                  <div class="grid-tbl-box">
                      <el-input
                          type="textarea"
                          class="mb40"
                          :rows="5"
                          maxlength="4000"
                          show-word-limit
                          v-model="data.remark"
                          :readonly="true"
                          style="font-size: 0.9em;">
                      </el-input>
                  </div>
              </div>
            </div>
        </div>
    </layout>
</template>

<script>
import _ from 'lodash';
/**
 * layout
 */
import Layout from '@/components/ModalSlot4.vue'

/**
 * Component
 */
import Top from './Top.vue'; /** 결재선 */
import Mid from './Mid.vue';  /** 헤더 */
import Bottom from './Bottom.vue'; /** 상세내역 */

/**
 * Modals
 */
import ApprLineSet from '@/components/ApprLineSet.vue' /** 결재자 지정 */
import ApprBundlePopDrafter from "@/components/ApprBundlePopDrafter.vue"; /** 결재 의견 */
import WingsAtchPop from '@/components/JiniAtchPop'; /** Wings 문서 팝업 */
import EvidAtchPopModeless from '@/components/EvidAtchPopModeless';
import EvidAtchPopGroupware from "@/components/EvidAtchPopGroupware"; /** 증빙첨부 파일 */

export default {
    components: {
        Layout,
        Top,
        Mid,
        Bottom
    },
    props: {
        values: {
            required: false,
            default() {
                return {}
            }
        },
        slipNo: {
            Type: String,
            required: false
        },
        title: {
            Type: String,
            default: '결재상신',
        },
        readOnly: {
          Type: Boolean,
          required: false
        },
    },
    data() {
        return {
            data: {},
            dataProp: {},
            apprHeader: {},
            apprLine: [],
            refUserId: '',
            refList: [],
            apprNo: '',
            apprSeq: '',
            docTitleNm: '',
            docStatus: '',
            draftUserId: '',
            thisApprUser: '',
            deleYn: 'N',
            apprInfo: [],
            apprDesc: '',
            isAccount: false,
            isAprver: false,
            isVerifier: false,
            status : '',
            isReuse: false,
            isCancel: false,
            slipReusePossibleYn: '',
            showWingsAtchPop: true,
            showCardException: false,
        }
    },
    computed: {},
    created() {
        if(this.slipNo) {
            this.getApprInfo();
        } else {
            Object.assign(this.data, this.values);
            Object.assign(this.apprHeader, this.values);
            this.docTitleNm = this.data.trxTypeName + ' / ' + this.$filters.number(this.data.totAmt) + ' / ' + this.data.description;
            this.status = this.data.slipStatus;
            this.getApprInfo();
            this.getMainApprLine();
        }
    },
    mounted() {
        // Close modal with 'esc' key
        document.addEventListener("keydown", (e) => {
            if (e.keyCode == 27) {
                this.close();
            }
        });
    },
    methods: {
        addApprDesc(docType){
            return new Promise((resolve,reject) => {
                const vm = this
                this.$modal.open({
                    component:ApprBundlePopDrafter,
                    props:{
                        docTitleNm:this.docTitleNm,
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
        /**
         * * 결재 상신(검토) 하기 시작.
         */
        review() {
            if (!!this.apprLine && this.apprLine.length < 1) {
                this.$message.error({ type: '알림', message: '결재선을 지정해주세요.' });
                return false;
            }

            this.addApprDesc('submit').then(() => {
                this.$confirm(`결재내역을 상신합니다. 계속 하시겠습니까?`, `${this.docTitleNm}`, {
                    confirmButtonText: '예',
                    cancelButtonText: '아니오',
                    type: 'success'
                }).then(() => {
                    const params = {
                        docTypeCd: 'SLIP',
                        slipNo: this.data.slipNo, //전표번호
                        apprGroupId: this.data.approvalGroupId, //전표 헤더 그룹 아이디
                        deptCd: this.data.deptCd, //귀속부서
                        totalAmt: this.$numeral(this.data.totAmtKrw).value(), //전표금액(KRW)
                        docTitleNm: this.docTitleNm, //결재 타이틀 
                        docContents: this.apprLine[0].apprDesc, //결재의견
                        approvalDetails: this.apprLine,
                        glDate: this.data.postingDt, //회계일자
                        slipType: this.data.trxTypeCode, //거래유형 코드
                        slipHeaderId: this.data.slipHeaderId, ////헤더전표ID
                        draftId : this.data.empNo, //위임자 No
                        refUserId: this.refUserId || ''
                    };
                    this.$store.commit('loading');
                    this.$http.put(`/api/appr/detail/req`, params)
                    .then(res => {
                        this.$message({type: res.data === '상신되었습니다.' ? 'success' : 'error', message: `${res.data}`});
                        if(res.data === '상신되었습니다.') {
                          this.close();
                        }
                    }).catch(error => {
                        this.$message.error({type: 'error', message: `${error}`});
                    }).finally(_ => {
                        this.$store.commit('finish');
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '취소하였습니다.'
                    });
                });
            });
        },
        /**
         * * 결재자 지정 팝업
         */
        designateApprover() {
            const self = this;
            this.$modal.open({
                component: ApprLineSet,
                props: {
                    lineList: this.apprLine,
                    setUserId: this.data.empNo,
                    refList: this.refList
                },
                parent: this,
                events: {
                    close(data) {
                        // Close event handler
                        if(data.lineList) {
                          self.apprLine = data.lineList || [];
                        }
                        if(data.refUserId) {
                          self.refUserId = data.refUserId || '';
                          self.refList = data.refList || [];
                        }
                    }
                }
            });
        },
        /**
         * * 결재자 지정 라인 셋업 콜백
         */
        designateApproverLineSetCallback(data) {
            this.apprLine = data
        },

        /**
         * 결재
         */
        approve() {
            this.addApprDesc('appr')
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
                        this.$message({type: 'success', message: `${res.data}`});
                        this.close();
                    }).catch(error => {
                        this.$message.error({type: 'error', message: error.response.data.message});  
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
        /**
         * 검인
         */
        verify() {
            this.addApprDesc('verify')
            .then(() => {
                this.$confirm(`검인을 계속 하시겠습니까?`, `${this.docTitleNm}`, {
                    confirmButtonText: '예',
                    cancelButtonText: '아니오',
                    type: 'success'
                }).then(() => {
                    const params = {
                        slipNo: this.slipNo || this.apprHeader.slipNo, //전표번호
                        apprGroupId: this.apprHeader.apprGroupId, //전표 헤더 그룹 아이디
                        aaprverId: this.$store.state.loginInfo.loginId,
                        slipType: this.apprHeader.slipType, //거래유형 코드
                        apprDesc: this.apprDesc,
                    };
                    this.$store.commit('loading');
                    this.$http.post(`/api/appr/detail/verify`, params)
                    .then(res => {
                        this.$message({type: 'success', message: `검인되었습니다.`});
                        this.close();
                    }).catch(error => {
                        this.$message.error({type: 'error', message: error.response.data.message});  
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
            userId : this.data.empNo,
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
        reject() {
          let docType = 'reject';
          if(this.status === 'CP') {
            docType = 'verifyReject'
          }
          this.addApprDesc(docType)
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
                slipReusePossibleYn: this.slipReusePossibleYn,
              };
              this.$store.commit('loading');
              this.$http.post(`/api/appr/detail/rej`, params)
              .then(res => {
                this.$message({type: 'success', message: `반려가 완료되었습니다.`});
                this.close();
              }).catch(error => {
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
        cancelAppr(){
            const slipNo = this.slipNo || this.values.slipNo;
            const vm = this;

            this.$swal({
              type: 'question',
              html: '상신 취소하시겠습니까?',
              showCancelButton: true
            }).then(r => {
              if (r.value) {
                this.$store.commit('loading')
                this.$http.post(`/api/appr/detail/cancel`, {
                  slipNo : slipNo,
                  apprGroupId : this.apprHeader.apprGroupId,
                  draftId : this.apprHeader.draftId
                })
                .then((response) => {
                  this.$swal({ type: 'success', text: '상신을 취소하였습니다.' })
                  this.close();
                })
                .catch((e) => {
                  this.$swal({ type: 'warning', text: e.response.data.message });
                })
                .finally(() => {
                  this.$store.commit('finish')
                });
              }
            })
        },
        getApprInfo() {
          const slipNo = this.slipNo || this.values.slipNo;
            this.$http.get(`/api/appr/detail/${slipNo}`)
            .then(res => res.data)
            .then(data => {
              this.apprLine = data.apprDetails;
              this.refUserId = data.apprHeader[0].refUserId || '';
            
              this.apprHeader = data.apprHeader[0];
              this.docTitleNm = this.apprHeader.docTitleNm;
              this.status = this.apprHeader.slipStatus;
              this.checkButtonUser();
            })
            .then(() => {
                if((this.values.trxTypeCode && this.values.trxTypeCode === 'SPAP004') || this.apprHeader.slipType === 'SPAP004'){
                    this.showCardException = true
                }
            })
        },
        checkButtonUser() {
          console.log(this.$store.state.loginInfo.authorities[0].roleCd);
          if(this.status === 'AP' && this.apprHeader.nextAppUserId === this.$store.state.loginInfo.loginId) {
            this.isAprver = true;
          }
          if(this.status === 'AP') {
            this.isCancel = true;
          }
          if(this.status === 'AP' && 'ACCOUNT' === this.$store.state.loginInfo.authorities[0].roleCd) {
            this.isAccount = true;
          }
          if (this.status === 'CP' && this.apprHeader.nextAppUserId === this.$store.state.loginInfo.loginId) {
            this.isVerifier = true;
          }
          if(this.status !== 'SV' && this.apprHeader.draftId === this.$store.state.loginInfo.loginId) {
            this.isReuse = true;
          }
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
                this.close();
              }).catch(error => {
                let errMsg = error.response.status === 400 ? error.response.data.message : '재사용 실패하였습니다.';
                this.$message.error({type: 'error', message: errMsg});
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
        close() {
            console.log('실행됨1')
            this.$emit('close', this.values.slipNo);
        },
        /**
         * * Wings 문서 팝업
         */
        openUploadWingsPopup(){
          const slipNo = this.slipNo || this.values.slipNo;
          if(slipNo){
              this.$modal.open({
                  component: WingsAtchPop,
                  props: {
                    slipNo,
                    readonly: true
                  },
                  parent: this,
                  width: 1200,
                  events: {
                    close(value) {

                    }
                  }
              })
          } else {
            this.$alert(`발생전표를 우선 저장해야 증빙첨부가 가능합니다.`, '확인', {
              dangerouslyUseHTMLString: true,
              confirmButtonText: '확인',
              type: 'error',
              center: true,
              callback: () => {}
            });
          }
        },
        /**
         * 그룹웨어 증빙파일 첨부 팝업
         */
        openUploadGroupwarePopup() {
            const slipNo = this.slipNo || this.values.slipNo;

            if(slipNo) {

                this.$modal.open({
                    component: EvidAtchPopGroupware,
                    props: {
                        slipNo,
                        readonly: true
                    },
                    parent: this,
                    width: 1200
                });

            } else {
                this.$alert(`발생전표를 우선 저장해야 그룹웨어 문서 첨부가 가능합니다.`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'error',
                    center: true,
                    callback: () => {}
                });
            }

        },
        /**
        * 증빙파일 첨부 팝업
        */
        openUploadEvidencePopup() {

          const disabled = true; //파일삭제 readOnloy flag
          const rdoCtrl = true; //파일업로드 readOnloy flag

          const slipNo = this.slipNo || this.values.slipNo;

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
              this.$alert(`발생전표를 우선 저장해야 증빙첨부가 가능합니다.`, '확인', {
                  dangerouslyUseHTMLString: true,
                  confirmButtonText: '확인',
                  type: 'error',
                  center: true,
                  callback: () => {}
              });
          }

        },
    }
}
</script>
