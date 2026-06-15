<template>
<table class="table">
  <tbody>
    <table-header v-model="value" :readonly="readonly" v-if="showHeader" />
    <tr>
      <table-select title="증빙유형" v-model="value" field="evdTypeCd" code="key" text="value" :options="options['EVD_TYPE_CD']" :readonly="readonly" @input="evdTypeChangeEvent" :colspan="2"/>

      <table-radio title="선급금신청" v-model="value" field="aplFgCd" :options="options['APL_FG_CD']" :disabled="true" :readonly="readonly" />

      <table-calendar title="증빙일자" v-model="value" field="evdDt" :required="true" :readonly="readonly" @input="evdDtChangeEvent"/>

      <table-calendar title="회계일자" v-model="value" field="postDt" :required="true" :readonly="readonly"/>
    </tr>

    <tr>
      <th>세금코드</th><td :colspan="2">{{taxTxt}}</td>

      <!-- <table-radio title="정산구분" v-model="value" field="stlFgCd" :options="options['STL_FG_CD']" :disabled="true" :readonly="readonly" /> -->
      <th>고정자산여부</th><td>{{value.fixAssetYn}}</td>

<!--      <adv-req title="선급금정산" v-model="value" :readonly="readonly" :colspan="3"/>-->
      <table-select title="지급조건" v-model="value" field="payTermCd" :options="options['PAY_TERM_CD']" :readonly="readonly" @input="payTermCdChangeEvent" :colspan="3"/>
    </tr>

    <tr>
      <evd-cust title="거래처" v-model="value" :readonly="readonly" :required="true" @input="evdCustCdChangeEvent" :colspan="2"/>

      <th>실지급처</th><td>{{realAccount}}</td>

      <table-calendar title="지급예정일" v-model="value" field="payDueDt" :required="true" :readonly="readonly"/>

      <table-input title="지급비고란" v-model="value" field="payText" :disabled="true" :readonly="readonly"/>
    </tr>

    <!-- 현금영수증인 경우 사용 -->
    <tr v-if="value.evdTypeCd === '40'">
      <table-input title="상호명" v-model="value" field="crcptMerchNm" :required="true" :readonly="readonly" />

      <table-input title="사업자번호" v-model="value" field="crcptBizNo" :required="true" :readonly="readonly" />

      <table-input title="승인번호" v-model="value" field="crcptNo" :required="true" :readonly="readonly" />
    </tr>
    <!-- //현금영수증인 경우 사용 -->

    <!-- 사내증빙인 경우 사용 -->
    <tr v-if="value.evdTypeCd === '42'">
      <table-radio title="사용유형" v-model="value" field="useTypeCd" :options="options['USE_TYPE_CD']" :readonly="readonly" :required="true" :colspan="7" />
    </tr>
    <!-- //사내증빙인 경우 사용 -->

    <!-- 해외 사용인 경우 사용 -->
    <tr v-if="value.evdTypeCd === '43'">
      <table-select title="사용국가" v-model="value" field="frgnNatCd" code="key" text="value" :options="options['FRGN_NAT_CD']" :readonly="readonly" />

      <table-select title="사용국가" v-model="value" field="frgnCurCd" code="key" text="value" :options="options['FRGN_CUR_CD']" :readonly="readonly" />

      <table-number-input title="외화금액" v-model="value" field="frgnAmt" :readonly="readonly" />
    </tr>
    <!-- //해외 사용인 경우 사용 -->

    <tr>
      <table-number-input title="공급가액" v-model="value" field="supAmt" :readonly="readonly" :required="true" @input="amountChangeEvent('supAmt')" :colspan="2"/>

      <base-dept title="기준부서" v-model="value" :required="true" :readonly="readonly" @input="baseDeptCdChangeEvent"/>

      <table-select title="지급계좌" v-model="value" field="custAcctNo" code="key" text="value" :options="options['VENT_ACCTS']" :required="true" :readonly="readonly" :colspan="3"/>
    </tr>

    <tr>
      <table-number-input title="부가세액" v-model="value" field="vatAmt" :disabled="!value.taxRt" :readonly="readonly" @input="amountChangeEvent('vatAmt')" :colspan="2"/>

      <table-select title="대변계정" v-model="value" field="apAcctCd" code="acctCd" text="acctNm" :options="options['ACCOUNT']" :readonly="readonly" @input="apAcctCdChangeEvent"/>

      <table-select title="대변 보조계정" v-model="value" field="apSubAcctCd" code="subAcctCd" text="subAcctNm" :options="options['SUB_ACCOUNT']" :readonly="readonly" @input="apSubAcctCdChangeEvent" :colspan="3"/>

    </tr>

    <tr>
      
      <table-number-input title="기타금액" v-model="value" field="etcAmt" :disabled="value.evdTypeCd !== '40'" :readonly="readonly" @input="amountChangeEvent('etcAmt')" :colspan="2"/>

      <th>통화</th><td>{{value.curCd}}</td>

      <table-select title="사업장" v-model="value" field="bpCd" code="key" text="value" :options="options['BP_CD']" :readonly="readonly" :colspan="3"/>
    </tr>

    <tr>
      
      <table-number-input title="총 금액" v-model="value" field="totAmt" :disabled="true" :readonly="readonly" :colspan="2"/>

      <table-input title="적요" v-model="value" field="hdSgtxt" :required="true" :readonly="readonly" :colspan="5" @input="hdSgtxtChangeEvent" />

    </tr>
    <tr v-if="value.taxCd === '10004'">
        <table-input title="카드번호" v-model="value" field="crdNo" :required="true" :readonly="readonly" />
        <td colspan="5"></td>
    </tr>
    <tr v-else-if="value.taxCd === '10003'">
        <th>지급여부</th>
        <td colspan="2">
            <div class="control">
                <div class="dp-ib fl_left">
                    <input type="checkbox" id="check01" v-model="value.hdRef8" disabled="false"/>
                    <label for="check01" class="NotoM">지급안함</label>
                </div>
            </div>
        </td>
        <td colspan="5"></td>
    </tr>
  </tbody>
</table>
</template>

<script>
import slip_table from '@/mixin/slip-table-new'
import _ from 'lodash'

export default {
  mixins: [slip_table],
  data(){
    return{
      taxTxt : '',
      realAccount : ''
    }
  },
  created(){
    if(!_.isEmpty(this.value.taxCd)){
      this.taxTxt = '['+this.value.taxCd+'] '+this.value.taxNm;
    }
    if(!_.isEmpty(this.value.payCustCd)){
      this.value.custAccount = null
      this.realAccount = this.value.payCustCd+' / '+this.value.payCustNm+' / '+this.value.custAcctNo
    }
  }
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
</style>
