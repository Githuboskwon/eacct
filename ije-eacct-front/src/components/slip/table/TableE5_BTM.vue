<template>
<table class="table">
  <tbody>
    <tr>
      <evd-cust title="공급자" v-model="value" :required="true" :readonly="readonly" :colspan="2" @input="evdCusChangeEvent"/>
      <table-select title="지급조건" v-model="value" field="payTermCd" :options="options['PAY_TERM_CD']" :readonly="readonly"  @input="evdCusChangeEvent" />
      <table-input title="Site 명" v-model="value" field="siteNm" :disabled="true" :readonly="readonly" :colspan="2"/>
    </tr>

    <tr>
      <table-select title="계좌번호" v-model="value" field="custAccount" code="key" text="value" :options="options['VENT_ACCTS']" :colspan="2" :readonly="readonly" @input="evdCusChangeEvent" />
      <table-calendar title="지급예정일" v-model="value" field="payDueDt" :required="true" :readonly="readonly" :colspan="4" />
    </tr>
  </tbody>
</table>
</template>

<script>
import slip_table from '@/mixin/slip-table-new'

export default {
  mixins: [slip_table],
  created() {
    this.evdTypeChangeEvent()

    if(this.value.evdCustCd) {
      setTimeout(() => {
        this.evdCusChangeEvent()
      }, 200)
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

.ip-box.ip-box-w02{
    width: 25%;
}

</style>
