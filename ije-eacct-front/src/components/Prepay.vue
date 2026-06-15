<template>
<layout>
  <span slot="header">
    {{ title }} 조회
    <button class="btn-pop-close delete" aria-label="close" @click="$dismiss"></button>
  </span>
  <div slot="content">
    <div class="btn-type1">
      <button class="btn-size btn-gray" @click="apply()">
        <span class="btn-icon"><i class="far fa-window-restore"></i></span> 적용
      </button>
      <button class="btn-size btn-gray" @click="$dismiss" style="margin-left: 10px;">
        <span class="btn-icon"><i class="far fa-window-restore"></i></span> 닫기
      </button>
    </div>

    <!-- 검색영역 -->
    <div class="pop-content-search">
      <div class="field has-addons ">
        <div class="mr20 " style="width:20%">
          <span class="pop-c-name">- 전표금액</span>
        </div>
        <div class="control is-expanded" style="width:50%; margin-right: 15px;">
          <number-input class="input input-bg" v-model="totAmt" disabled="disabled" />
        </div>

        <div class="mr20 " style="width:20%">
          <span class="pop-c-name">- 반제금액</span>
        </div>
        <div class="control is-expanded" style="width:50%">
          <number-input class="input " v-model="value.mrAmt" />
        </div>
      </div>
    </div>
    <!-- //검색영역 -->

    <!-- DHX 그리드 영역 -->
    <dhx-grid ref="grid" class="gridbox" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful"/>
    <!-- //DHX 그리드 영역 -->
  </div>
</layout>
</template>

<script>
import { url as _url } from '@/libs/join'

import Layout from '@/components/ModalSlot.vue'
import DhxGrid from '@/components/DhxGrid.vue'
import NumberInput from '@/components/NumberInput.vue'
import assert from '@/libs/assert'

export default {
  props: {
    totAmt: {
      type: Number,
      required: true,
    },
    payCustCd: {
      type: String,
      required: true
    },
    title: {
      type: String,
      required: false,
      default: '선급금'
    }
  },
  components: {
    Layout,
    DhxGrid,
    NumberInput
  },
  data() {
    return {
      value: {
        mrAmt : 0
      },
      config: {
        columns: [{
          id: 'no',
          type: 'cntr',
          align: 'center',
          sort: 'int',
          value: 'No.',
          width: 30
        }, {
          id: 'preInvoiceNum',
          type: 'ro',
          align: 'center',
          sort: 'str',
          value: '선급금전표번호',
          width: 100
        }, {
          id: 'hdRef7',
          type: 'ro',
          align: 'center',
          sort: 'str',
          value: '라인',
          width: 30
        }, {
          id: 'preInvoiceDt',
          type: 'ron',
          align: 'center',
          value: '선급금전표일자',
          width: 90
        }, {
          id: 'preAmt',
          type: 'ron',
          align: 'right',
          value: '선급금',
          width: 120
        }, {
          id: 'preAmtRm',
          type: 'ron',
          align: 'right',
          value: '선급금 잔액',
          width: 120
        }, {
          id: 'preDesc',
          type: 'ro',
          align: 'center',
          sort: 'str',
          value: '적요',
          width: 180
        }],
        events: {
          // onRowDblClicked(rid) {
          //   var index = rid - 1
          //   var vm = this.$parent.$parent
          //   let mrAmt = vm.data[index].mrAmt = this.$parent.$parent.value.mrAmt
          //   let preAmtRm = vm.data[index].preAmtRm

          //   if(vm.$numeral(mrAmt).value() > vm.$numeral(preAmtRm).value()){
          //     this.$swal({type: 'warning', text: '반제금액이 선급금 잔액보다 큽니다.'});
          //   } else if (vm.$numeral(mrAmt).value() > vm.$numeral(vm.totAmt).value()) {
          //     this.$swal({type: 'warning', text: '반제금액이 전표금액 잔액보다 큽니다.'});
          //   } else {
          //     vm.$emit('close', vm.data[index])
          //   }
            
          // }
        },
        height: 300
      },
      data: []
    }
  },
  methods: {
    constructGridSuccessful(grid) {
      grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
      grid.setNumberFormat('0,000', 4, '.', ',')
      grid.setNumberFormat('0,000', 5, '.', ',')
    },
    apply() {
      
      try{
        if(this.$refs.grid.instance.getSelectedRowId() == null)
        throw '선택된 행이 없습니다.'
        let index = this.$refs.grid.instance.getSelectedRowId() - 1

        let totAmt = this.$numeral(this.totAmt).value()
        let mrAmt = this.data[index].mrAmt = this.value.mrAmt
        let preAmtRm = this.data[index].preAmtRm
        
        if(this.$numeral(mrAmt).value() > this.$numeral(preAmtRm).value()){
          this.$swal({type: 'warning', text: '반제금액이 선급금 잔액보다 큽니다.'});
        } else if (this.$numeral(mrAmt).value() > this.$numeral(totAmt).value()) {
          this.$swal({type: 'warning', text: '반제금액이 전표금액 잔액보다 큽니다.'});
        } else {
          this.$emit('close', this.data[index])
        }
      }catch(e){
        this.$swal({
          type: 'warning',
          text: e
        })
      }
      
      // this.value[this.field] = this.$numeral(this.number).value()
      // if (this.onSubmit !== undefined && typeof this.onSubmit === 'function') {
      //   this.onSubmit.apply(this, [])
      // }
    },
    $dismiss() {
      this.$emit('dismiss')
      this.$parent.close()
    },
    query() {
      this.$store.commit('loading')
      this.data = []
      this.$http.get(`/api/erp/expense/prepay/${this.payCustCd}`)
        .then(response => {
          // console.log(response)
          this.data = response.data
          if (!slow && this.data.length === 1)
            this.$emit('close', this.data[0])
          return response
        })
        .catch(response => {
          return response
        })
        .finally(() => {
          this.$store.commit('finish')
        })
    }
  },
  created() {
    this.query()
  }
}
</script>
