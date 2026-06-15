<template>
  <div>
    <div style = "float: left; max-width: 0px">
      <input type="text" class="schinput" v-model="val" :readonly="readonly" :disabled="disabled" @keypress.enter="searchVal" @keydown="keyDown"/>
    </div>
    <div>
      <button style = "float: right;margin-top: 10px;" :readonly="readonly" v-show="!disabled" type="button" class="icon" @click="openPopup"><i class="fas fa-search"></i></button>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'

export default Vue.extend({
  data() {
    return {
      val: '',
      readonly: false,
      disabled : false,
    }
  },
  created() {
    if(this.params.disable) {
      this.disabled = this.params.disable;
    }
    if(this.params.data.new){
      this.readonly = false
    }else{
      this.readonly = true
    }
  },
  methods: {
    openPopup(){
      var funcNm = this.params.funcNm;
      if(funcNm !== undefined){
        this.$parent.$parent.rowIdx = this.params.node.rowIndex;
        this.$parent.$parent.rowId = this.params.node.id;
        this.$parent.$parent[funcNm]();
      }
    },
    searchVal(){
      console.log("Searching")
      var funcNm = this.params.valFuncNm;
      if(funcNm !== undefined){
        this.$parent.$parent.rowIdx = this.params.node.rowIndex;
        this.$parent.$parent.rowId = this.params.node.id;
        this.$parent.$parent[funcNm](this.val);
      }
    },
    keyDown(e) {
      //좌우측 방향키 눌림시 다음 컬럼 넘어감 방지
      if(e.code === "F2" || e.code === "End" || e.code === "Home" || e.code === "ArrowLeft" || e.code === "ArrowRight"){
        e.stopPropagation()
      } 
      // else if(e.keyCode === 13){
      //   console.log(1)
      //   var funcNm = this.params.valFuncNm;
      //   if(funcNm !== undefined){
      //     this.$parent.$parent.rowIdx = this.params.node.rowIndex;
      //     this.$parent.$parent.rowId = this.params.node.id;
      //     this.$parent.$parent[funcNm](this.val);
      //   }
      // }
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
<style>
.schinput {
  font-size: inherit;
  font-family: inherit;
  line-height: inherit;
  color: inherit;
  border-width: 0px;
  border-style: none;
  min-height: 40px;
  height: 100%;
  min-width: 300px;
  background-color: rgba(255,255,255,0.0);
}

.schinput:read-only{
  font-size: inherit;
  font-family: inherit;
  line-height: inherit;
  color: inherit;
  border-width: 0px;
  border-style: none;
  min-height: 40px;
  height: 100%;
  background-color: rgba(255,255,255,0.0);
}

.schinput:focus{
  outline: none;
}

</style>

