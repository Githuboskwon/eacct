<template>
  <monthly-picker v-model="date" @input="apply" v-show="showIf"/>
</template>

<script>
import MonthlyPicker from '@/components/MonthlyPicker.vue'

export default {
  props: ['index', 'value', 'field'],
  components: {
    MonthlyPicker
  },
  data() {
    return {
      date: undefined,
      showIf: true,
      config: {}
    }
  },
  methods: {
    apply() {
      const value = this.$parent.value[this.index] ;
      value[this.field] = this.date
      if (this.onSubmit !== undefined && typeof this.onSubmit === 'function') {
        this.onSubmit.apply(this, [])
      }
    }
  },
  watch: {
    'value': {
      immediate: true,
      deep: true,
      handler(value) {
        const pValue = this.$parent.value[this.index] ;
        this.date = pValue[this.field]
      }
    }
  }
}
</script>
