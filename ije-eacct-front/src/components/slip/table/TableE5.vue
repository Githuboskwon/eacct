<template>
<table class="table">
  <tbody>
    <table-header v-model="value" :readonly="true"/>
    <tr>
      <table-input title="카드번호" v-model="value" field="crdNo" :disabled="true" :colspan="2" :readonly="true" />
      <table-input title="승인번호" v-model="value" field="crdApprNo" :disabled="true" :readonly="true" />
      <table-calendar title="증빙일자" v-model="value" field="evdDt" :required="true" :readonly="true" :colspan="3"/>
    </tr>
    <tr>
      <th>카드소유자</th><td colspan="2">{{this.value.crdPssrNm}} {{this.value.crdPssrTitle}}</td>
      <table-input title="카드구분" v-model="value" field="crdFgNm" :disabled="true" :readonly="true" />
      <table-calendar title="회계일자" v-model="value" field="postDt" :required="true" :readonly="true" :colspan="3"/>
    </tr>
    <tr>
      <table-number-input title="승인금액" v-model="value" field="totAmt" :readonly="true" :disabled="true" :colspan="2"/>
      <table-input title="가맹점명" v-model="value" field="crdMerchNm" :disabled="true" :readonly="true" />
      <th>승인시간</th><td colspan="3">{{apprDateTime}}</td>
    </tr>
    <tr>
      <table-number-input title="공급가액" v-model="value" field="supAmt" :readonly="true" :disabled="true" :colspan="2"/>
      <table-input title="가맹점주소" v-model="value" field="merchAddr" :disabled="true" :readonly="true" :colspan="5" />
    </tr>
    <tr>
      <table-number-input title="부가세액" v-model="value" field="vatAmt" :readonly="true" :disabled="true" :colspan="2"/>
      <th>과세유형/업종</th><td>{{this.value.taxTypeNm}}/{{this.value.mccName}}</td>
      <table-calendar title="지급예정일" v-model="value" field="payDueDt" :required="true" :readonly="true"/>
      <table-input title="지급비고란" v-model="value" field="payText" :disabled="true" :readonly="true"/>
    </tr>
    <tr>
      <table-number-input title="봉사료" v-model="value" field="tips" :readonly="true" :disabled="true" :colspan="2"/>
      <table-input title="세금코드" v-model="value" field="taxNm" :disabled="true" :readonly="true" />
      <base-dept title="기준부서" v-model="value" :required="true" :readonly="true" :colspan="3"/>
    </tr>    
    <tr>
      <table-input title="적요" v-model="value" field="hdSgtxt" :required="true" :readonly="true" :colspan="8" @input="hdSgtxtChangeEvent" />
    </tr>
  </tbody>
</table>
</template>

<script>
import slip_table from '@/mixin/slip-table-new'
import slip from '@/mixin/slip';

export default {
  mixins: [slip_table,slip],
  created() {    
    if(!this.value.aplFgCd) this.value.aplFgCd = 'N'
    if(!this.value.stlFgCd) this.value.stlFgCd = 'N'
    if(!this.value.excDt) this.value.excDt = this.$moment().format('YYYYMMDD')
    if(!this.value.excRt) this.value.excRt = 1
    if(!this.value.taxNm) this.value.taxNm = '불공제'

    if(this.value.crdNo) this.value.crdNo = this.getFormattedCardNumber(this.value.crdNo);
    
  }
  ,computed: {
    apprDateTime() {
        const dt = _.trim(this.value.apprDate + this.value.apprTime);
        return (dt.length===14) ? this.$moment(dt,'YYYYMMDDHHmiss').format("YYYY-MM-DD HH:mm:ss") :
               (dt.length===8) ? this.$moment(dt,'YYYYMMDD').format("YYYY-MM-DD") : "";
    },
  },  
}
</script>

<style lang="scss" scoped>
table {
  table-layout: fixed;

  :global(th) {
    width: 88px;
  }

  :global(td) {
    width: 180px;
  }
}

.ip-box.ip-box-w02{
    width: 25%;
}

</style>
