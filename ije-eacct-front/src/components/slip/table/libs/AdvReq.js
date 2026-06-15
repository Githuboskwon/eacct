import Prepay from '@/components/Prepay.vue'
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
      required: false
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
  components: {
    NumberInput
  },
  template: `
    <td :colspan="colspan" class="clearfix">
    <div class="control is-expanded search-area">
      <div class="ip-box rs-mt2" style="width:56%;">
        <input class="input input-bg" v-model="value.preInvoiceNum" disabled="disabled" style="padding-right:35px;" />
        <button class="icon is-right" @click="modal(value.totAmt)" v-if="!readonly">
          <i class="fas fa-search"></i>
        </button>
      </div>
      <button class="remove_text rs-mt2 rs-mb2" @click="apply()" style="width:8%;" v-if="!readonly">
        <i class="far fa-trash-alt"></i>
      </button>
      <div class="ip-box rs-mt2" style="width:30%;">
        <number-input class="input input-bg" v-model="value.mrAmt" disabled="disabled" />
      </div>
    </div>
    </td>
  `
  ,
  methods: {
    apply(data) {
      data = data || {}
      this.value.preInvoiceNum = data.preInvoiceNum
      this.value.preInvoiceDt = data.preInvoiceDt
      this.value.preAmt = data.preAmt
      this.value.preAmtRm = data.preAmtRm
      this.value.preDesc = data.preDesc
      this.value.mrAmt = data.mrAmt
      this.$emit('input', this.value)
      this.$forceUpdate()
    },
    failure() {
      this.$swal({
        type: 'error',
        text: `요청하신 선급금 정보를 찾을 수 없습니다.`
      }).then(() => {
        this.apply({})
      })
    },
    modal(totAmt) {
      if(this.$parent.value.evdCustCd !== undefined) {
        let payCustCd = this.$parent.value.evdCustCd
        let modalReadOnly = this.readonly
        if(this.$parent.value.stlFgCd === "Y") {
          if(totAmt > 0) {
            let vm = this
            this.$modal.open({
              component: Prepay,
              props: {
                totAmt,
                payCustCd,
                readOnly: modalReadOnly
              },
              parent: this,
              events: {
                close(data) {
                  vm.apply.apply(vm, [data])
                  vm.$forceUpdate()
                }
              }
            })
          } else {
            this.$swal({type: 'warning', text: '총금액이 0보다 작습니다.'});
          }
        }else {
          this.$swal({type: 'warning', text: '미정산 구분 입니다.'});
        }
      } else {
        this.$swal({type: 'warning', text: '공급자를 선택하여 주세요.'});
      }
    },
    query() {
      this.$store.commit('loading')
      this.$http.get('/api/erp/expense/prepay')
          .then(response => {
            if (response.data.length > 0) {
              this.apply(response.data[0] || {})
            } else {
              this.failure()
            }
          })
          .catch(() => {
            this.failure()
          })
          .finally(() => {
            this.$store.commit('finish')
          })
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
      required: false
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