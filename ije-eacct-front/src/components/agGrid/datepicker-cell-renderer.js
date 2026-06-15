import Vue from 'vue';
import DhxCalendar from '@/components/DhxCalendar.vue'

export default Vue.extend({
  template: `
        <dhx-calendar class="calendar" v-model="date" :config="config" @input="apply" />
    `,
    components: {
      DhxCalendar
    },
    data: function () {
    return {
      date: undefined,
      config: {}
    };
  },
  created() {
    if(this.params.colDef.cellRendererParams !== undefined){
      this.config = this.params.colDef.cellRendererParams.config
    }
  },  
  methods: {
    apply() {      
      let colId = this.params.column.colId

      this.params.node.setDataValue(colId, this.date);
    }
  },
  watch: {
    'value': {
      immediate: true,
      deep: true,
      handler(value) {
        this.date = this.params.value
      }
    }
  }  
});