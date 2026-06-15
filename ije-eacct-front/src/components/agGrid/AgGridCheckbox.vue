<template>
  <div class="bd-l-none checkbox-wrapper">
    <input ref="chkBox" :id="id" type="checkbox" v-model="select" @click="apply($event)" :disabled="chk"/>
    <label :for="id">&nbsp;</label>
  </div>
</template>

<script>
import Vue from 'vue'
import uuid from '@/libs/uuid'

export default Vue.extend({
  data() {
    return {
      id: undefined,
      select: false,
      chk: false
    }
  },
  created() {
    this.id = uuid();
    this.chkVal();
  },
  methods: {
    apply(e) {
      const chk = this.params.value;

      if(typeof chk === 'string'){
        this.select = (e.target.checked ? 'Y' : 'N');  
      }else{
        this.select = e.target.checked;  
      }

      this.params.node.setDataValue(this.params.column.colId, this.select);
    },
    chkVal(){
      if(this.params.data.cStateYn){
        this.chk = true;
      }
    }
  },
  watch: {
    'value': { 
      immediate: true,
      deep: true,
      handler() {
        if(typeof this.params.value === 'string'){
          this.select = (this.params.value === 'Y' ? true : false);
        }else{
          this.select = (this.params.value ? true : false);
        }
      }
    }
  }
});
</script>
