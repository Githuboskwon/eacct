import Vue from 'vue';

export default Vue.extend({
    data: function () {
    return {
      date: undefined,
      type: 'month',
      format: 'yyyy-MM',
      valueFormat: 'yyyyMM',
      disable: false,
      defaultValue: undefined,
      datePickerOptions: {
        disabledDate (date) {
          return date < new Date('1971-01-01')
        }
      },
      config: {}
    };
  },
  watch: {
    'value': {
      immediate: true,
      deep: true,
      handler(value) {
        this.$nextTick(() => {

          this.date = this.params.value

          if(this.params.colDef.cellRendererParams !== undefined){
            if(typeof this.params.colDef.cellRendererParams=== 'function'){
              const { disable, type, format, valueFormat, options, defaultValue } = this.params.colDef.cellRendererParams(this.params);
              this.disable = disable ;
              this.type = type;
              this.format = format;
              this.valueFormat = valueFormat;
              this.datePickerOptions = options;
              this.defaultValue = defaultValue;
            } else {
              const { disable, type, format, valueFormat, options, defaultValue } = this.params.colDef.cellRendererParams;
              this.disable = disable;
              this.type = type;
              this.format = format;
              this.valueFormat = valueFormat;
              this.datePickerOptions = options;
              this.defaultValue = defaultValue;
            }
          }
        })        
      }
    }
  },
  methods: {
    apply() {      
      let colId = this.params.column.colId

      this.params.node.setDataValue(colId, this.date);
    }
  },
  // <dhx-calendar class="calendar" v-model="date" :config="config" @input="apply" :disabled="disable"/>
  template: `
  <el-date-picker style="width: 135px;" :picker-options="datePickerOptions" :default-value="defaultValue" v-model="date" :type="type" :format="format" :value-format="valueFormat" :disabled="disable" @input="apply" />
    `,
});