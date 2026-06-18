<template>
  <layout>
      <span slot="header">
    {{ title }}
    <button class="btn-pop-close delete" aria-label="close" @click="$parent.close()"></button>
  </span>
    <div slot="content" style="width:1150px;">
      <div class="btn-wrap btn-type1 clearfix" v-if="isNew" style="float:right;">
        <button class="btn-size btn-blue" @click="approval">
        <span class="btn-icon">
          <i class="fas fa-pen-alt"></i>
        </span>
          상신
        </button>
        <button class="btn-size btn-blue" @click="openVendorPopup">
        <span class="btn-icon">
          <i class="fas fa-user-check"></i>
        </span>
          결재자 지정
        </button>
      </div>
      <div slot="content" v-if="!isNew" style="width:1150px;">
        <div class="btn-wrap btn-type1 clearfix" style="float:right;">
          <button
              v-if="this.$store.state.loginInfo.loginId === this.apprInfo.thisApprUser || this.apprInfo.deleYn ==='Y'"
              class="btn-size btn-blue" @click="openApprPopup()"><span class="btn-icon"><i
              class="fas fa-pen-alt"></i></span> 결재
          </button>
          <!-- 상신된 문서 취소 기능은 전자결재 - 상신한문서 - 각 유형의 상세 정보창 에서 상신 취소 가능 & 다른 화면에서는 취소불가능에 따른 주석 처리 -->
          <!-- <button v-if="this.$store.state.loginInfo.loginId == this.apprInfo.draftUserId && this.apprInfo.docStatus === 'REQ'"
                  class="btn-size btn-yellow" @click="cancel(docMngNo)"><span class="btn-icon"><i class="far fa-file"></i></span> 상신취소
          </button> -->
        </div>
      </div>
      <appr-dtl ref="apprDtl" v-if="isLoaded" v-model="data" :docType="docType" :approval-line.sync="apprLine"
                :docMngNo="docMngNo" :budReqNo="budReqNo" @input="$forceUpdate()"
                @approvalLine="changeApprovalLineEvent" @apprInfo="apprInfoCheck"/>
      <!-- Appr Header area -->
      <!--                <div class="table-area" style="margin-top: 0px;">
                          <appr-header :doc-type="docType" :apprHeader="apprHeader" :appr-line="apprLine" v-model="data"/>
                      </div>-->
      <!-- //Appr Header area -->
      <!--            <component :is="docComponent" v-model="data" :readonly="true" @input="queryApprLineRule"/>-->
    </div>
  </layout>
</template>

<script>
import _ from 'lodash'
import mixin from '@/mixin';

import ApprLineSet from '@/components/ApprLineSet.vue'
import ApprActPop from '@/components/ApprActPop.vue'
import ApprDtl from '@/components/ApprDtl.vue'
import ApprBundlePopDrafter from "@/components/ApprBundlePopDrafter.vue";
import Layout from '@/components/ModalSlot.vue'

export default {
  compatConfig: { MODE: 2 },
  props: {
    docMngNo: {
      type: String,
      required: false
    },
    budReqNo: {
      type: String,
      required: false
    },
    docType: {
      type: String,
      required: false,
      default: 'SLIP'
    },
    value: {
      type: Object,
      required: false,
      default() {
        return {}
      }
    }
  },
  mixins: [mixin],
  components: {
    ApprDtl,
    ApprLineSet,
    Layout,
    ApprBundlePopDrafter,
  },
  data() {
    return {
      title: '결재상신',
      data: {},
      dataProp: {},
      apprHeader: {},
      apprLine: [],
      apprNo: '',
      apprSeq: '',
      docTitleNm: '',
      docStatus: '',
      draftUserId: '',
      thisApprUser: '',
      deleYn: 'N',
      apprInfo: [],
      loaded: false,
      isNew: false,
      isMxMn: true
    }
  },
  methods: {
    move() {
      if (this.isMxMn) {
        $(".modal-card").removeClass('pop_min2')
        $(".modal-card").addClass('pop_max');

        $(".animation-content").removeClass('pop_min2')
        $(".animation-content").addClass('pop_max');
        this.isMxMn = false
      } else {
        $(".modal-card").removeClass('pop_max')
        $(".modal-card").addClass('pop_min2');

        $(".animation-content").removeClass('pop_max')
        $(".animation-content").addClass('pop_min2');
        this.isMxMn = true
      }
    },
    addApprDesc(){
      return new Promise((resolve,reject) => {
        const vm = this
        this.$modal.open({
          component:ApprBundlePopDrafter,
          props:{
            docTitleNm:this.docTitleNm
          },
          events: {
            close(drafterData) {
              // Close event handler
              if(!_.isEmpty(drafterData)){
                //기안자 결재의견 set
                vm.apprLine[0].apprDesc = drafterData
                return resolve()
              }else{
                return reject()
              }
            }
          }
        })
      })
    },
    approval() {
      if (this.apprLine.length < 1) {
        this.$swal({type: 'warning', text: '결재선을 지정해주세요.'});
        return false;
      }

      this.addApprDesc().then(() => {
      this.$swal({
        type: 'info',
        text: `결재내역을 상신합니다. 계속 하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오',
      }).then((result) => {
        if (result.value) {
          this.$store.commit('loading');
          let docMngNo = undefined

          if(this.docType === 'SLIP'){
            docMngNo = this.data.grSlipNo
          }else if(this.docType === 'BDGT'){
            docMngNo = this.data.budReqNo
          } else if( this.docType === 'ACCURUAL' ) {
            docMngNo = this.data.slipNo
            // approvalGroupId = slipHeaderId
            // docMngNo = slipNo
            // "slipNo": "SPIE2023112400091",
            // "slipHeaderId": 256325,
          }
          _.forEach(this.apprLine, (v, index) => {
            v.apprSeq = index + 1
          })
          
          if (this.docType === 'SLIP' || this.docType === 'BDGT' || this.docType === 'ACCURUAL') {
            if(this.docType === 'ACCURUAL') {
              this.$http.post(`/api/slip/save`, this.data)
              .then(res => res.data)
              .then(result => {
                this.$http.put(`/api/appr/detail/req`, {
                  docTypeCd: 'SLIP',
                  slipNo: docMngNo, //전표번호
                  apprGroupId: this.data.slipHeaderId, //전표 헤더 그룹 아이디
                  deptCd: this.data.deptCd, //귀속부서
                  totalAmt: this.$numeral(this.data.totAmtKrw).value(), //전표금액(KRW)
                  docTitleNm: this.docTitleNm, //결재 타이틀 
                  docContents: this.apprLine[0].apprDesc, //결재의견
                  approvalDetails: this.apprLine,
                  glDate: this.data.postingDt, //회계일자
                  slipType: this.data.trxTypeCode //거래유형 코드
                }).then(res => {
                  this.$swal({type: 'success', text: res.data});
                }).catch(error => {
                  this.$swal({type: 'error', text: error.response.data.message});  
                })
              }).catch((e) => {
                this.$swal({type: 'error', text: e.response.data.message});
                this.$parent.close();
                // this.$parent.$parent.status.readonly = true
              }).finally(() => {
                this.$store.commit('finish');
              });
            } else {
              this.$http.put(`/api/appr/detail/req`, {
                docTypeCd: this.docType,
                docMngNo: docMngNo,
                docTitleNm: this.docTitleNm,
                approvalDetails: this.apprLine
              })
              .then((response) => {
                this.$parent.close();
                this.$swal({type: 'info', text: '상신이 완료되었습니다.'});
                //this.$parent.$parent.status.readonly = true
                //2020.09.02 새로고침
                if (this.data.slipTypeCd === 'E1' || this.data.slipTypeCd === 'E3' || this.data.slipTypeCd === 'E4' || this.data.slipTypeCd === 'E5') {
                  this.$router.push({
                    name: 'billSlipMng',
                    params: {grSlipNo: this.data.grSlipNo}
                  })
                } else {
                  this.$parent.$parent.status.readonly = true
                  if(this.data.slipTypeCd === 'E2'){
                      this.$parent.$parent.slipStatCd = this.data.slipStatCd
                  }else{
                      window.location.reload()
                  }
                }
              })
              .catch((e) => {
                this.$swal({type: 'error', text: e.response.data.message});
                this.$parent.close();
                // this.$parent.$parent.status.readonly = true
              }).finally(() => {
                this.$store.commit('finish');
              })

            }
          } else {
            console.log('그룹상신 !!')
            this.$http.post('/api/slip/groupAppr', {
              approvalHeader: {
                docTypeCd: 'SLIP',
                approvalDetails: this.apprLine
              },
              slipHistoryDtos: this.data.slipGroupDetails
            }).then(response => {
              this.$parent.close();
              this.$swal({type: 'info', text: '상신이 완료되었습니다.'});
              this.$parent.$parent.status.readonly = true
            }).catch((e) => {
              //   this.$swal({type: 'error', text: e.response.data.message});
              this.$parent.close();
              this.$parent.$parent.status.readonly = true
            }).finally(() => {
              this.$store.commit('finish');
            })
          }

        }
      })
      })

    },
    cancel(docMngNo) {
      this.$swal({
        type: 'info',
        text: `상신을 취소합니다. 계속 하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오',
      }).then((result) => {
        if (result.value) {

          if (docMngNo != undefined) {
            this.$http.get(`/api/appr/detail/cancel/${docMngNo}`)
            .then(response => {
              this.$parent.close();
              this.$swal({type: 'info', text: '상신 취소되었습니다.'});
              this.$parent.$parent.status.readonly = false

              if (this.value.slipTypeCd === 'E4') {
                var grSlipNo = this.value.grSlipNo
                var slipStatCd = "10"
                var slipTypeCd = this.value.slipTypeCd
                var params = {grSlipNo, slipStatCd, slipTypeCd}

                this.$router.push({
                  name: 'billSlipReg',
                  params
                })
              }
            })
            .catch(({message}) => {
              console.error(message);
            });
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
          lineList: this.apprLine
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
    openApprPopup() {
      let vm = this
      this.$modal.open({
        component: ApprActPop,
        props: {
          docTitleNm: this.apprInfo.docTitleNm,
          apprNo: this.apprInfo.apprNo,
          apprSeq: this.apprInfo.apprSeq
        },
        parent: this,
        events: {
          close(data) {
            // Close event handler
            console.log('Appr Popup Close Event!!')
            vm.$refs.apprDtl.getAppr(data)
            if (vm.$parent.$parent.goSearch() != undefined) {
              vm.$parent.$parent.goSearch()
            }
          }
        }
      })
    },
    changeApprovalLineEvent(data) {
      this.apprLine = data
    },
    apprInfoCheck(data) {
      console.log('Appr Info Check!!')
      this.apprInfo = data
    },
  },
  computed: {
    isLoaded() {
      return this.loaded
    }
  },
  created() {
    let docTitleNm = undefined

    if (this.docMngNo === undefined) {
      this.isNew = true
    } else {
      this.isNew = false
      this.title = '결재보기'
    }

    if (this.docType === 'SLIP') {
      if (this.data.grSlipNo.substr(0, 2) === 'GR' || this.data.grSlipNo.substr(0, 2) === 'EA') {
        docTitleNm = this.data.slipTypeNm + ' / ' + this.$filters.number(this.data.totAmt) + ' / ' + this.data.hdSgtxt
      }
    } else if (this.docType === 'BDGT') {
      if (this.data.budReqNo.substr(0, 2) === 'BD') {
        let title = this.getShortTitle(this.data.reqRsn);
        docTitleNm = this.data.budFgNm + ' / ' + this.$filters.number(this.data.reqTotAmt) + ' / ' + title
      }
    }
    this.docTitleNm = docTitleNm
  },
  mounted() {
    //pop slots modal move
    $(".animation-content").addClass('pop_min2');
    $(".animation-content").draggable();
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '4');
  },
  destroyed() {
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '7');
  },
  watch: {
    value: {
      immediate: true,
      deep: true,
      handler(value) {
        if (value !== undefined) {
          this.data = value
          this.loaded = true
        }
      }
    }
  }
}
</script>

<style scoped>
.modal-card {
  width: auto;
}
</style>
