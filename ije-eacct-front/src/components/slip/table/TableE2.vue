<template>
<table class="table">
  <tbody>
    <table-header v-model="value" :readonly="readonly" v-if="showHeader" />
    <tr>
      <table-input title="신청번호" v-model="value" field="grSlipNo" :disabled="true" :colspan="2" :readonly="readonly" />
      <!--      <table-calendar title="회계일자" v-model="value" field="postDt" :required="true" :readonly="readonly" @input="postEvdDtChangeEvent"/>-->
      <table-calendar title="회계일자" v-model="value" field="postDt" :required="true" :readonly="isGrSlipNo" @input="postEvdDtChangeEvent"/>
      <table-input title="진행상태" v-model="value" field="slipStatNm" :disabled="true" :readonly="readonly" />
    </tr>

    <tr>
      <wrt-dept title="부서" v-model="value" :required="true" :readonly="readonly" :disabled="true" :colspan="2"/>
      <base-emp title="직원" v-model="value" :required="true" :readonly="readonly" :disabled="true"/>
      <!-- 2020.04.22 증빙일자 이벤트 교체 -->
      <table-calendar title="증빙일자" v-model="value" field="evdDt" :required="true" :readonly="readonly" @input="evdDtChangeEvent"/>
    </tr>
    <tr>
      <table-select title="부채계정" v-model="value" field="lbltAcctCd" code="acctCd" text="acctNm" :options="options['LBLT_ACCOUNT']" :colspan="2" :readonly="readonly" @input="lbltAcctCdChangeEvent" />
      <table-select title="부채보조계정" v-model="value" field="lbltSubAcctCd" code="subAcctCd" :options="options['LBLT_SUB_ACCOUNT']" text="subAcctNm" :readonly="readonly" @input="lbltSubAcctCdChangeEvent"/>
      <!--      <table-select title="부채보조계정" v-model="value" field="lbltSubAcctCd" code="subAcctCd" :options="options['LBLT_SUB_ACCOUNT']" text="subAcctNm" :readonly="readonly" @input="lbltSubAcctCdChangeEvent" />-->
      <table-exchg-calendar title="환율일자/환율" v-model="value" field="excDt" :required="true" :readonly="readonly" />
    </tr>

    <tr>
      <th>총금액</th>
      <td :disabled="true" colspan="2">
        <tr class="input2" v-if="readonly===false">{{totAmtFormat}}</tr>
        <tr class= "input3" v-else>{{totAmtFormat}}</tr>
      </td>
      <th>총금액(KRW)</th>
      <td :disabled="true">
        <tr class="input2" v-if="readonly===false">{{totAmtKrwFormat}}</tr>
        <tr class= "input3" v-else>{{totAmtKrwFormat}}</tr>
      </td>
<!--      <table-radio title="선급금신청" v-model="value" field="aplFgCd" :options="options['APL_FG_CD']" :readonly="readonly" :colspan="2" @input="aplFgCdChangeEvent"/>-->
      <!-- <table-input title="통화" v-model="value" field="curCd" :disabled="true" :readonly="readonly" /> -->
      <table-select title="통화" v-model="value" field="curCd" code="key" text="value" :options="options['FRGN_CUR_CD']" :readonly="readonly" @input="frgnChangEvent(value)"/>
    </tr>
    <tr>
      <table-radio title="선급금신청" v-model="value" field="aplFgCd" :options="options['APL_FG_CD']" :readonly="readonly" :colspan="2" @input="aplFgCdChangeEvent"/>
      <table-radio title="정산구분" v-model="value" field="stlFgCd" :options="options['STL_FG_CD']" :readonly="readonly" @input="stlFgCdChangeEvent"/>
      <table-radio title="고정자산여부" v-model="value" field="fixAssetYn" :options="options['FIX_ASSET_YN']" :readonly="readonly" />
<!--      <adv-req title="선급금정산" v-model="value" :readonly="readonly" :colspan="2" @change="gridAcctEvent"/>-->
<!--      <td style="border-left: none;"></td>-->
    </tr>
    <tr>
<!--    <tr>-->
<!--      <table-select title="부채계정" v-model="value" field="lbltAcctCd" code="key" text="value" :options="options['LBLT_ACCOUNT']" :colspan="2" :readonly="readonly" @input="lbltAcctCdChangeEvent" />-->
<!--      &lt;!&ndash;<table-number-input title="총금액" v-model="value" field="totAmt" :readonly="readonly" :disabled="true"/>&ndash;&gt;-->
<!--      &lt;!&ndash;<table-number-input title="총금액(KRW)" v-model="value" field="totAmtKrw" :readonly="readonly" :disabled="true"/>&ndash;&gt;-->
<!--      &lt;!&ndash;2020.08.14 기본정보 소수점 변경&ndash;&gt;-->
<!--      <th>총금액</th>-->
<!--      <td :disabled="true">-->
<!--        <tr class="input2" v-if="readonly===false">{{totAmtFormat}}</tr>-->
<!--        <tr class= "input3" v-else>{{totAmtFormat}}</tr>-->
<!--      </td>-->
<!--      <th>총금액(KRW)</th>-->
<!--      <td :disabled="true">-->
<!--        <tr class="input2" v-if="readonly===false">{{totAmtKrwFormat}}</tr>-->
<!--        <tr class= "input3" v-else>{{totAmtKrwFormat}}</tr>-->
<!--      </td>-->
<!--    </tr>-->

<!--    <tr>-->
<!--      <table-radio title="선급금신청" v-model="value" field="aplFgCd" :options="options['APL_FG_CD']" :readonly="readonly" :colspan="2" @input="aplFgCdChangeEvent"/>-->
<!--      &lt;!&ndash; <table-input title="통화" v-model="value" field="curCd" :disabled="true" :readonly="readonly" /> &ndash;&gt;-->
<!--      <table-select title="통화" v-model="value" field="curCd" code="key" text="value" :options="options['FRGN_CUR_CD']" :readonly="readonly" @input="frgnChangEvent(value)"/>-->
<!--      <table-exchg-calendar title="환율일자/환율" v-model="value" field="excDt" :required="true" :readonly="readonly" />-->
<!--    </tr>-->
<!--    <tr>-->
<!--      <table-radio title="정산구분" v-model="value" field="stlFgCd" :options="options['STL_FG']" :readonly="readonly" :colspan="2" @input="stlFgCdChangeEvent"/>-->
<!--      <adv-req title="선급금정산" v-model="value" :readonly="readonly" :colspan="2" @change="gridAcctEvent"/>-->
<!--       <td style="border-left: none;"></td>-->
<!--    </tr>-->
<!--    <tr>-->
      <!-- 2020.04.22 E2에 세부항목 적요 덮어씌우기 원복 -->
      <table-input title="적요" v-model="value" field="hdSgtxt" :required="true" :readonly="readonly" :colspan="6" @input="hdSgtxtChangeEvent"/>
    </tr>
  </tbody>
</table>
</template>

<script>
import slip_table from '@/mixin/slip-table-new'

export default {
  mixins: [slip_table],
  //2020.08.14 기본정보 소수점 변경
  data(){
    return{
      isGrSlipNo: false
    }
  },
  computed:{
    totAmtFormat: function(){
      if(this.value.curCd==="KRW"){
        return this.$numeral(this.value.totAmt).format("0,0");
      }else{
        return this.$numeral(this.value.totAmt).format("0,0.00");
      }

    },
    totAmtKrwFormat: function(){
      if(this.value.curCd==="KRW"){
        return this.$numeral(this.value.totAmtKrw).format("0,0");
      }else{
        return this.$numeral(this.value.totAmtKrw).format("0,0");
      }

    },

  },
  watch:{
    'value.grSlipNo': {
      deep:true,
      immediate: true,
      handler(value){
        if(value){
          if(value.substr(0,2) === 'GR'){
            this.isGrSlipNo = true
          }
        }
      }
    }
  }

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
//2020.08.14 2020.08.14 기본정보 소수점 변경
.input2{
  background-color: inherit;
  border-color: #dadada;
  box-shadow: inherit;
  color: #555;
  background: #f0f3f7;
  font-family: 'RobotoM';
  text-align: right !important;
  line-height: 1;
  font-size: 14px;
  height: 25px;
  cursor: not-allowed;
  border-radius: 2px;
  max-width: 100%;
  width: 100%;
  align-items: center;
  border: 1px solid transparent;
  display: inline-flex;
  justify-content: flex-end;
  padding-bottom: calc(0.375em - 1px);
  padding-left: calc(0.625em - 1px);
  padding-right: calc(0.625em - 1px);
  padding-top: calc(0.375em - 1px);
  position: relative;
  vertical-align: top;
  box-sizing: border-box;
  text-rendering: auto;
  letter-spacing: normal;
  word-spacing: normal;
  text-transform: none;
  text-indent: 0px;
  text-shadow: none;
}

.input3{
  text-align: right !important;
  line-height: 1;
  font-size: 14px;
  height: 25px;
  cursor: not-allowed;
  border-radius: 2px;
  max-width: 100%;
  width: 100%;
  align-items: center;
  border: 1px solid transparent;
  display: inline-flex;
  justify-content: flex-end;
  padding-bottom: calc(0.375em - 1px);
  padding-left: calc(0.625em - 1px);
  padding-right: calc(0.625em - 1px);
  padding-top: calc(0.375em - 1px);
  position: relative;
  vertical-align: top;
  box-sizing: border-box;
  text-rendering: auto;
  letter-spacing: normal;
  word-spacing: normal;
  text-transform: none;
  text-indent: 0px;
  text-shadow: none;
}

.ip-box.ip-box-w02{
    width: 25%;
}

</style>
