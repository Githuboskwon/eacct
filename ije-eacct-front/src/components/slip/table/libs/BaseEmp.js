import Emp from '@/components/Emp_new.vue'

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
  <td :colspan="colspan" v-if="readonly">{{ value.empNo }} / {{ value.empNm }}</td>
  <td :colspan="colspan" v-else>
    <div class="td-s-thumb search-area" v-if="value.slipTypeCd === 'E2' || value.slipTypeCd === 'E5'">
      <input class="input input-bg mr5 Rt-M rs-mt2" type="text" v-model="value.empNo" style="width:66px;" disabled="disabled" />
      <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(100% - 71px)">
        <input type="text" class="input" v-model="value.empNm"  disabled="disabled"/>
      </div>
    </div>
    <div class="td-s-thumb search-area" v-else>
      <input class="input input-bg mr5 Rt-M rs-mt2" type="text" v-model="value.empNo" style="width:66px;" disabled="disabled" />
      <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(100% - 100px)">
        <input type="text" class="input" v-model="value.empNm" @keypress.enter="query(value.empNm)" />
        <button class="icon is-right" @click="modal(value.empNm)"><i class="fas fa-search"></i></button>
      </div>
      <button class="remove_text trash-input rs-mb2" @click="apply()">
        <i class="far fa-trash-alt"></i>
      </button>
    </div>
  </td>
  `,
  methods: {
    apply(data) {
      data = data || {}
      this.value.empNo = data.empNo
      this.value.empNm = data.empNm
      this.$emit('input', this.value)
    },
    failure() {
      this.apply({})
    },
    modal(searchStr) {
      let vm = this
      this.$modal.open({
        component: Emp,
        props: {
          searchStr
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
        this.$http.get(`/api/emp/${searchStr}`)
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
    }
  }, created(){
    if(!this.value.empNo) {
      this.value.empNo = this.$store.state.loginInfo.loginId;
      this.value.empNm = this.$store.state.loginInfo.userName;
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
