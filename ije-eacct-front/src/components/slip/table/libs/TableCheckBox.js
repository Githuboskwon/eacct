import uuid from '@/libs/uuid'

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
  template: `
  <dd>
    <input type="checkbox" :id="field" :checked="data" @click="apply" />
    <label :for="field">{{title}}</label>
  </dd>
  `,
  data() {
    return {
      id: undefined,
      data: undefined
    }
  },
  methods: {
    apply() {
      this.value[this.field] = !this.data ? 'Y' : undefined
      
      this.$emit('input', this.value)
    }
  },
  created() {
    this.id = String(uuid()).replace(/\W/g, '')
  },
  watch: {
    'value': {
      immediate: true,
      deep: true,
      handler(value) {
        this.data = value[this.field]
        
        // this.data = value[this.field]
      }
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

    const tde = createElement(td, {
      props: context.props,
      on: on
    })
    return [tde]
  }
}
