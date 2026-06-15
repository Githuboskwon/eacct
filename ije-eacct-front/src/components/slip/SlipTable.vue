<template>
  <div class="table-area">
    <div class="table-a fixed-th">
      <div class="table-name">
        <h3 class="ico_table_name">기본정보</h3>
      </div>
      <component :is="tableId" v-model="value" :status="status" />
      <div v-if="slipType === 'E2'" class="table-name">
        <h3 class="ico_table_name">공급자 & 결제조건</h3>
      </div>
      <component v-if="slipType === 'E2'" :is="tableId+'_BTM'" v-model="value" :status="status" />
    </div>
  </div>
</template>

<script>
// import TableE1 from '@/components/slip/E1/TableRO.vue'
//E1 - 개인비용
import TableE1 from '@/components/slip/table/TableE1.vue'
import TableE2 from '@/components/slip/table/TableE2.vue'
import TableE2_BTM from '@/components/slip/table/TableE2_BTM.vue'
// import TableE2 from '@/components/slip/E2/TableRO.vue'
import TableE3 from '@/components/slip/E3/TableRO.vue'
// E4 - 기타증빙
import TableE4 from '@/components/slip/table/TableE4.vue'
// E5 - 법인카드 분할전표
import TableE5 from '@/components/slip/table/TableE5.vue'
import TableE5_BTM from '@/components/slip/table/TableE5_BTM.vue'


export default {
  props: ['slipType', 'value', 'status'],
  components: {
    TableE1,
    TableE2,
    TableE2_BTM,
    TableE3,
    TableE4,
    TableE5,
    TableE5_BTM,
    //TableE6,
    // TableE7
  },
  data() {
    return {
      options: {}
    }
  },
  computed: {
    tableId() {
      return 'Table' + this.slipType
    }
  }
}
</script>
