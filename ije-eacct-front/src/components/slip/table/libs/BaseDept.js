import Cctr from '@/components/Cctr_new.vue'

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
    'title': {
      type: String,
      required: true
    },
    'value': {
      type: Object,
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
  template: `
  <td :colspan="colspan" v-if="readonly">{{ value.baseDeptCd }} / {{ value.baseDeptNm }}</td>
  <td :colspan="colspan" v-else>  
    <div class="td-s-thumb search-area" v-if="value.slipTypeCd === 'E2' || value.slipTypeCd === 'E5'" >
      <input class="input input-bg mr5 Rt-M rs-mt2" type="text" v-model="value.baseDeptCd" style="width:66px;" disabled="disabled" />
      <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(100% - 71px)">
        <input type="text" class="input input-bg mr5" v-model="value.baseDeptNm" disabled="disabled"/>
      </div>
    </div>
    <div class="td-s-thumb search-area" v-else>
      <input class="input input-bg mr5 Rt-M rs-mt2" type="text" v-model="value.baseDeptCd" style="width:66px;" disabled="disabled" />
      <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(100% - 100px)">
        <input type="text" class="input" v-model="value.baseDeptNm" @keypress.enter="query(value.baseDeptNm)" />
        <button class="icon is-right" @click="modal(value.baseDeptNm)"><i class="fas fa-search"></i></button>
      </div>
      <button class="remove_text trash-input rs-mb2 actual" @click="apply()" disabled>
        <i class="far fa-trash-alt"></i>
      </button>
    </div>
  </td>
  `,
  methods: {
    apply(data) {
      data = data || {}
      this.value.baseDeptCd = data.deptCd
      this.value.baseDeptNm = data.deptNm
      this.$emit('input', this.value)
    },
    failure() {
      this.apply({})
    },
    modal(searchStr) {
      let vm = this
      this.$modal.open({
        component: Cctr,
        props: {
          searchStr,
          slipTypeCd: this.value.slipTypeCd
        },
        parent: this,
        events: {
          close(data) {
            vm.apply.apply(vm, [data])
          }
        }
      })
    },
    query(searchStr) {
      if (searchStr) {
        this.$store.commit('loading')
        this.$http.get(`/api/cctr/erp/${searchStr}`)
          .then(response => {
            if (response.data.length === 1) {
              this.apply(response.data[0] || {})
            } else {
              this.modal(searchStr)
            }
          })
          .catch(() => {
            this.failure()
          })
          .finally(() => {
            this.$store.commit('finish')
          })
      } else {
        this.failure()
      }
    },
  },
  created(){
    if(!this.value.baseDeptCd) {
      this.value.baseDeptCd = this.$store.state.loginCctr.cctrCd;
      this.value.baseDeptNm = this.$store.state.loginCctr.cctrNm;
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

    const the = createElement(th, {
      props: context.props
    })
    const tde = createElement(td, {
      props: context.props,
      on: on
    })
    return [the, tde]
  }
}
