<template>
<table class="table">
  <tbody>
    <table-header v-model="value" :readonly="readonly" v-if="showHeader" />

    <tr>
      <table-input title="신청번호" v-model="value" field="grSlipNo" :disabled="true"  :readonly="readonly"/>
      <table-calendar title="작성일자" v-model="value" field="regDt" :required="true" :readonly="readonly" :disabled="true" :colspan="3" />
<!--      <table-calendar title="회계년월" v-model="value" field="postDt" :required="true" :readonly="readonly" :disabled="true"/>-->
      <table-input title="진행상태" v-model="value" field="slipStatNm" :disabled="true" :colspan="3" :readonly="readonly"  />
    </tr>
    <tr>
      <wrt-dept title="작성부서" v-model="value" :required="true" :readonly="true" />
      <base-emp title="직원" v-model="value" :required="true" :colspan="3" :readonly="true" />
      <table-select title="지급조건" v-model="value" field="payTermCd" :options="options['PAY_TERM_CD']" :readonly="readonly"  @input="payTermCdChangeEvent" :disabled="true"/>
      <table-calendar title="지급예정일" v-model="value" field="payDueDt" :required="true" :readonly="readonly" :disabled="true"/>
    </tr>
    <tr>
      <table-input title="부채계정" v-model="value" field="liabilityAcctNm" :disabled="true"  :readonly="readonly"/>
      <table-number-input title="총 금액" v-model="value" field="totAmt" :disabled="true" :readonly="readonly" />
      <table-input title="통화" v-model="value" field="curCd" :disabled="true" :readonly="readonly" />
      <table-input title="지급비고란" v-model="value" field="payText" :required="true" :colspan="3" :readonly="readonly" :disabled="true"/>
    </tr>
    <tr>
      <table-input title="적요" v-model="value" field="hdSgtxt" :required="true" :readonly="readonly" :colspan="9" :disabled="true"/>
    </tr>

  </tbody>
</table>
</template>

<script>
import slip_table from '@/mixin/slip-table-new'

export default {
  mixins: [slip_table]
}
</script>

<style lang="scss" scoped>
table {
  table-layout: fixed;

  ::v-deep th {
    width: 88px;
  }

  ::v-deep td {
    width: 180px;
  }
}
</style>
