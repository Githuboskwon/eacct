import DhxCalendar from '@/components/DhxCalendar'
import NumberInput from '@/components/NumberInput.vue'

const th = {
  props: {
    'title': {
      type: String,
      required: true
    },
    'readonly': {
      type: Boolean,
      required: false,
      default: false
    },
    'required': {
      type: Boolean,
      required: false,
      default: false
    }
  },
  template: `
  <th :class="{ 'tp-a': required && !readonly }">{{ title }}</th>
  `
}

const td = {
  props: {
    'value': {
      type: Object,
      required: true
    },
    'field': {
      type: String,
      required: true
    },
    'colspan': {
      type: Number,
      required: false,
      default: 1
    },
    'disabled': {
      type: Boolean,
      required: false,
      default: false
    },
    'readonly': {
      type: Boolean,
      required: false,
      default: false
    }
  },
  components: { DhxCalendar, NumberInput },
  template: `
  <td :colspan="colspan" v-if="readonly">{{ this.$moment(value[field]).format('YYYY-MM-DD') }} / {{value.excRt}}</td>
  <td :colspan="colspan" v-else>
    <div td-s-thumb search-area>
      <div>
        <div class="datepicker ip-box ip-box-w02 rs-mt2">
          <dhx-calendar class="input" v-model="value[field]" @input="dateInputChange(value)" :disabled="disabled" style="width:120px"/>
        </div>
        <input type="text" class="input input-bg mr5 Rt-M rs-mt2" v-model="value.excRt" format="0,0" style="width:calc(100% - 130px);display:inline-block" disabled="disabled"/>
      </div>
    </div>
  </td>
  `,
  methods: {
    dateInputChange(value) {
      this.$http.get(`/api/erp/exchange/rate/${value.curCd}/${value.excDt}`)
      .then(response => {
        let data = response.data[0]
        value.curCd = data.frCurrency
        value.excRt = this.$numeral(data.convRt).value()
        if(data.frCurrency === 'JPY'){
          value.totAmtKrw = value.totAmt * value.excRt * 0.01
          value.totAmtKrw = Math.floor(value.totAmtKrw)
        }else {
          value.totAmtKrw = value.totAmt * value.excRt
          value.totAmtKrw = Math.floor(value.totAmtKrw)
        }
        
      }).catch(response => {
        value.curCd = "KRW"
        value.excRt = 1
        value.totAmtKrw = value.totAmt * value.excRt
      }).finally(() => {
        this.$emit('input', value)
        this.$store.commit('finish')
      })
      
    }
  },
  data() {
    return {
    }
  }
}

export default {
  functional: true,
  props: {
    'title': {
      type: String,
      required: true
    },
    'required': {
      type: Boolean,
      required: false,
      default: false
    },
    'value': {
      type: Object,
      required: true
    },
    'field': {
      type: String,
      required: true
    },
    'colspan': {
      type: Number,
      required: false,
      default: 1
    },
    'disabled': {
      type: Boolean,
      required: false,
      default: false
    },
    'readonly': {
      type: Boolean,
      required: false,
      default: false
    }
  },
  render(createElement, context) {
    // construct emit
    let on = {}
    for (var event in context.listeners) {
      on[event] = e => {
        if (Array.isArray(context.listeners[event]) && context.listeners[event].length > 0) {
          _.forEach(context.listeners[event], f => {
            if (typeof f === 'function')
              f.apply(this, [e])
          })
        } else if (typeof context.listeners[event] === 'function') {
          context.listeners[event].apply(this, [e])
        }
      }
    }

    const the = createElement(th, { props: context.props })
    const tde = createElement(td, {
      props: context.props,
      on: on
    })
    return [the, tde]
  }
}