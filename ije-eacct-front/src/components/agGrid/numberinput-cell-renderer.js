import Vue from 'vue';

export default Vue.extend({
    template: `
    <div>
    <input ref="box" type="text" class="inputnumber" v-model="number" @input="number = $numeral(number).format('0,0')" @blur="apply" @keyup.enter="apply" :disabled="disable" v-show="showIf" @keydown="keyDown" :maxlength="maxlength"/>
    </div>                              
`,
  data() {
    return {
      number: '0',
      disable: false,
      showIf: true,
      maxlength: 20
    }
  },  
  created() {

    if(typeof this.params.colDef.cellRendererParams === 'function') {
      const { disable } = this.params.colDef.cellRendererParams(this.params);
      this.disable = disable;
    } else {
      if(this.params.colDef.cellRendererParams !== undefined){
        
        const param = this.params.colDef.cellRendererParams
        
        if(param.disable !== undefined) this.disable = param.disable
  
        if(param.maxlength !== undefined) this.maxlength = param.maxlength 
  
      }
    }
    
    
    //그리드 셀 focusing 방지
    this.params.colDef.editable = false;
  },
  mounted() {
    $(this.$refs.box).on('focus', () => {
      $(this.$refs.box).trigger('select')
    })
  },  
  methods: {
    apply(e) {
      this.params.node.setDataValue(this.params.column.colId, this.$numeral(this.number).value());      
    },
    keyDown(e) {
      //좌우측 방향키 눌림시 다음 컬럼 넘어감 방지
      if(e.key === "ArrowLeft" || e.key === "ArrowRight" || e.key === "Home" || e.key === "End"){
        e.stopPropagation()
      }
    }    
  }, watch: {
    'value': {
      immediate: true,
      deep: true,
      handler() {
        this.number = this.$numeral(this.params.value).format('0,0')
      }
    }
  }
});