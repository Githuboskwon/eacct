<template>
    <span style="color: #0065b3;" @click="openAttachPopup">{{ this.$numeral(val).format('0') }} <i class="far fa-file-alt"></i></span>
</template>

<script>
import Vue from 'vue'
import uuid from '@/libs/uuid'

export default Vue.extend({
  data() {
    return {
      val: ''
    }
  },
  created() {
  },
  methods: {
    openAttachPopup(){
      var funcNm = this.params.funcNm;

      if(funcNm !== undefined){
        this.$parent.$parent.rowIdx = this.params.node.rowIndex;
        this.$parent.$parent.rowId = this.params.node.id;
        this.$parent.$parent[funcNm]();
      }
    }
  },
  watch: {
    'value': {
      immediate: true,
      deep: true,
      handler() {
        this.val = this.params.value;
      }
    }
  }
});
</script>

