import Vue from 'vue';

export default Vue.extend({
  template: `
      <select class="select" v-model="data" @change="onInput()" :disabled="disable" v-show="showIf" style="width: 132%;">
        <option v-for="option in options" :key="option[detailCd]" :value="option[detailCd]" v-text="option[detailNm]"/>
      </select>
    `,
    data: function () {
    return {
      data: undefined,
      disable: false,
      showIf: true,
      options: [
      ],
      detailCd: 'detailCd',
      detailNm: 'detailNm'
    };
  },
  mounted() {
    this.data = this.params.value
    
    if(this.params.colDef.cellRendererParams.detailCd !== undefined){
      this.detailCd = this.params.colDef.cellRendererParams.detailCd
    }
    
    if(this.params.colDef.cellRendererParams.detailNm !== undefined){
      this.detailNm = this.params.colDef.cellRendererParams.detailNm
    }
    
    if(this.params.colDef.editable !== undefined) {
      if(typeof this.params.colDef.editable === 'function'){
        this.disable = !this.params.colDef.editable(this.params)
      }else{
        this.disable = !this.params.colDef.editable
      }
    }
  },  
  methods: {
    onInput() {
      this.$nextTick(() => {
        let colId = this.params.column.colId
        this.params.node.setDataValue(colId, this.data);
      })
    }
  },
  watch: {
    'value': {
      immediate: true,
      deep: true,
      handler() { 
        this.data = this.params.value
        this.$forceUpdate()
      }
    },
    'params.colDef.options': {
      immediate: true,
      deep: true,
      handler() {
        this.options = this.params.colDef.cellRendererParams.options
        this.$forceUpdate()
      }
    },
  }  
});