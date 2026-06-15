import Vue from 'vue';
import uuid from '@/libs/uuid'

export default Vue.extend({
  data() {
    return {
      id: undefined,
      data: false,
      disable: false, // 체크박스가 보이지 않아야 할 때 true 처리
      trueValue: "Y",
      falseValue: "N",
      name: '',
      readonly: false, // 변경이 되지 않아야 하는 체크박스를 표시 할때 true 처리
      field: 'check',
      headerId: undefined,
      allChkColumn : false, // 전체 체크 컬럼의 경우 true 처리
      class : '' // 예전모양의 체크박스를 사용할때 사용.  'girdChk' 넣을것.
    }
  }, watch: {
    'value': {
      immediate: true,
      deep: true,
      handler() {
        this.$nextTick(() => {

          let tVal;

          if(this.params.displayName) { //헤더 부분에서 받아오는 파라미터.
            this.field = `${this.params.displayName}`.toLocaleLowerCase();
            this.id = 'all';
          } else {
            // if(typeof this.params.colDef.cellRendererParams === 'function') {
            if (this.field === '선택') {
              let data = this.params.colDef.cellRendererParams(this.params);
              this.disable = data.disable;
              this.name = data.name;
              tVal = data.trueValue;
            } else {
              tVal = this.params.colDef.cellRendererParams.trueValue;
              this.disable = this.params.colDef.cellRendererParams.disable;
              this.name = this.params.colDef.cellRendererParams.name;
              this.readonly = this.params.colDef.cellRendererParams.readonly;
              this.class = this.params.colDef.cellRendererParams.class;
            }

            this.data = this.params.value === (tVal || this.trueValue) ? true : false;

            if(this.readonly){
              this.id = undefined;
            }

          }

          const castChecked = (params) => {
            if(typeof params === 'boolean') {
              return params
            } else if(typeof params === 'string') {
              return params === 'Y' ? true : false;
            } else {
              return params ? true : false;
            }
          }

          try {
            if(!this.disable &&  this.noAllChkColumn && this.params.context.headerAllCheckEvent) {
              document.getElementById('all').checked = castChecked(this.params.value);
            }
          } catch(e) {
          }

        })
      }
    }
  },
  created() {
    this.id = uuid()
  },
  methods: {
    checkedHandler(event) {
      let checked = null

      if(this.params.displayName) { //헤더 부분에서 받아오는 파라미터.
        this.params.context.headerAllCheckEvent(this.field, event.target.checked)
      } else {
        if(typeof this.params.colDef.cellRendererParams === 'function') {
          let types = typeof this.params.colDef.cellRendererParams.trueValue === 'boolean';
          if(types) {
            checked = event.target.checked
          } else {
            checked = event.target.checked ? this.trueValue : this.falseValue
          }
          // checked = event.target.checked ? this.trueValue === 'Y' ? true: false : this.falseValue;
        } else {
          if(this.params.colDef.cellRendererParams === undefined){
            checked = event.target.checked ? this.trueValue : this.falseValue
          }else{
            let fValue = typeof this.params.colDef.cellRendererParams.falseValue === 'boolean' ? false : this.params.colDef.cellRendererParams.falseValue
            let tValue = typeof this.params.colDef.cellRendererParams.trueValue === 'boolean' ? true : this.params.colDef.cellRendererParams.trueValue
            checked = event.target.checked ? tValue : fValue
          }
        }

        let colId = this.params.column.colId

        this.$nextTick(() => {
          this.params.node.setDataValue(colId, checked);
        });
      }
    },
  },
  template:
      `<div>
      {{name !== '' ? name : '&nbsp;'}}
      <input 
        type="checkbox"
        :class="this.class"
        :id="id"
        :checked="data"
        :disabled="disable"
        @click="checkedHandler($event)"
      />
      <label :for="id">{{disable ? '' : '&nbsp;'}}</label>
    </div>`,
});