<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">개인비용(경비)외 신청서</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <!-- <button class="btn-size btn-gray fl_left" @click="buttonEventPrint" v-if="isReadOnly">
          <span class="btn-icon"><i class="fas fa-print"></i></span>
          출력
        </button> -->
        <button class="btn-size btn-blue fl_left" @click="buttonEventViewSubmit" v-if="isReadOnly">
          <span class="btn-icon"><i class="fas fa-vote-yea"></i></span>
          결재보기
        </button>
        <button class="btn-size btn-orange fl_left" @click="buttonEventCopy" v-show="slipStatCd ==='40' || slipStatCd ==='60'">
          <span class="btn-icon"><i class="far fa-clone"></i></span>
          전표복사
        </button>
        <button class="btn-size btn-orange fl_left" @click="buttonEventCopy" v-show="slipStatCd ==='70' && (data.slipTypeCd == 'E2' || data.slipTypeCd == 'E4')">
          <span class="btn-icon"><i class="far fa-clone"></i></span>
          전표복사
        </button>
        <button class="btn-size btn-blue fl_left" @click="buttonEventSubmit" v-if="isSubmitable && data.grSlipNo && data.empNo == this.$store.state.loginInfo.loginId">
          <span class="btn-icon">
            <i class="fas fa-pen-alt"></i>
          </span>
          결재상신
        </button>
        <button class="btn-size btn-gray fl_left" @click="buttonEventCreate" v-if="isCreateable">
          <span class="btn-icon">
            <i class="far fa-file"></i>
          </span>
          신규
        </button>
        <button class="btn-size btn-gray fl_left" @click="buttonEventDelete" v-if="isDeleteable && data.grSlipNo && data.empNo == this.$store.state.loginInfo.loginId">
          <span class="btn-icon">
            <i class="far fa-trash-alt"></i>
          </span>
          삭제
        </button>
        <button class="btn-size btn-gray fl_left" @click="buttonEventSave" v-if="isNew || isSaveable && data.empNo == this.$store.state.loginInfo.loginId">
          <span class="btn-icon">
            <i class="fas fa-check"></i>
          </span>
          저장
        </button>
        <!-- <button class="btn-size btn-gray fl_left" @click="showTmpActivity" v-if="!isReadOnly">
          <span class="btn-icon">
            <i class="far fa-file"></i>
          </span>
          임시상세화면
        </button>
        <button class="btn-size btn-gray fl_left" @click="backTmpActivity" v-if="isReadOnly">
          <span class="btn-icon">
            <i class="far fa-file"></i>
          </span>
          임시뒤로가기
        </button> -->
      </div>
    </div>
    <slip-base :grSlipNo="grSlipNo" v-model="data" :slipStatCd="slipStatCd" :status="status" />
  </div>
  
</template>
  
<script>
import slip from '@/mixin/slip-basic'
import _ from 'lodash'
import assert from '@/libs/assert'

export default {
  props: {
    grSlipNo: {
      type: String,
      required: false
    },
    slipStatCd: {
      type: String,
      required: false
    },
  },
  mixins: [slip],
  data() {
    return {
      name: 'exctpExpense',
      data: {
        grSlipNo: undefined,
        eaSlipNo: undefined,
        compCd: undefined,
        slipTypeCd: 'E2',
        evdDt: this.$moment().format('YYYYMMDD'),
        eaSlipDt: this.$moment().format('YYYYMMDD'),
        postDt: this.$moment().format('YYYYMMDD'),
        slipStatCd: '10',
        evdTypeCd: '40',
        stlFgCd: 'N',
        aplFgCd: 'N',
        fixAssetYn: 'N',
        useTypeCd: undefined,
        sysFgCd: 'W',
        bpCd: undefined,
        taxCd: undefined,
        taxRt: undefined,
        evdCustCd: undefined,
        evdCustNm: undefined,
        evdCustBizNo: undefined,
        custUseYn: 'Y',
        custBnkCd: undefined,
        totAmt: 0,
        supAmt: 0,
        preInvoiceNum: undefined,
        mrAmt: 0,
        preAmtRm: 0,
        totAmtKrw: 0,
        curCd: 'KRW',
        payCustCd: undefined,
        payCustNm: undefined,
        payDueDt: undefined,
        payTermCd: undefined,
        payText:undefined,
        wrtDtm: undefined,
        wrtId: undefined,
        wrtDeptCd: undefined,
        wrtDeptNm: undefined,
        baseDeptCd: undefined,
        baseDeptNm: undefined,
        custAcctNo: undefined,
        custAccount: undefined,
        lbltAcctCd: undefined,
        lbltSubAcctCd: undefined,
        stnAcctCd: undefined,
        expItemCnt: undefined,
        hdSgtxt: undefined,
        fnlApprDtm: undefined,
        fnlAprverId: undefined,
        elecApprId: undefined,
        elecApprDtm: undefined,
        apprRsn: undefined,
        evdMngNo: undefined,
        regId: undefined,
        regDtm: undefined,
        chgId: undefined,
        chgDtm: undefined,
        excDt: this.$moment().format('YYYYMMDD'),
        excRt: 1,
        slipDetails: [],
        useYn: 'N',
        block:''//2020.05.15 E2 지급조건에따른 지급예정일 활성화
      }
    }
  },
  destroyed() {
    // this.$cookie.delete('searchForm')
    // this.$store.commit('searchForm', undefined);
  },
  created() {
    document.title = '개인비용(경비)외 신청서 - IJEAS';
  },
  watch: {
    // 'slipStatCd': {
    //   immediate: true,
    //   deep: true,
    //   handler(value) {
    //     if(!_.isEmpty(value)){
    //       this.data.slipStatCd = value
    //     }
    //   }
    // },
    // 'data.stlFgCd'(value) {
    // },
    // 'data.aplFgCd'(value) {
    //   if(value === 'K') {
    //     this.data.stlFgCd = 'N'
    //   }
    // }
  },
  // 2020.08.21 개인비용(경비)외 신청서(E2) 상태 '결재반려(40)', '재경반려(60)','재경확정(70)'일 경우 전표복사 버튼 활성화
  computed: {
    // isShowCopy(){
    //   return (this.data.slipTypeCd === 'E2' && (this.slipStatCd === '40' || this.slipStatCd === '60' || this.slipStatCd === '70'))
    // }
  }
}
</script>
