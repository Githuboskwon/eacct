<template>
  <layout>
  <span slot="header">
    세금계산서 품목수정
    <button class="btn-pop-close delete" aria-label="close" @click="$parent.close()"></button>
  </span>
    <div slot="content" class="inner-box">
      <div class="table-area">
        <div class="table-b">
          <div class="table-a fixed-th">
            <div class="table-header">
              <div class="table-name">
                <h3 class="ico_table_name">품목정보</h3>
              </div>
              <div class="btn-wrap btn-type2 clearfix fl_right">
                <button class="btn-size btn-w-gray" @click="submit">
                  <span class="btn-icon"><i class="fas fa-check"></i></span> 수정
                </button>
                <button class="btn-size btn-w-gray" @click="$parent.close()">
                  <span class="btn-icon"><i class="fas fa-times"></i></span> 취소
                </button>
              </div>
            </div>

            <table class="table">
              <colgroup>
                <col width="15%"><col width="35%"><col width="15%"><col width="35%">
              </colgroup>
              <tbody>
              <tr>
                <th>거래처명</th>
                <td>
                  <input class="input" type="text" v-model="form.customerName" readonly/>
                </td>

                <th>GL 일자</th>
                <td>
                  <input class="input" type="text" v-model="form.glDate" readonly/>
                </td>
              </tr>

              <tr>
                <th>품목명</th>
                <td>
                  <input class="input" type="text" v-model="form.itemName" />
                </td>

                <th>비고</th>
                <td>
                  <input class="input" type="text" v-model="form.remark" />
                </td>
              </tr>
              <tr>
                <th>수량</th>
                <td>
                  <input class="input" type="text" v-model="form.itemQty" />
                </td>

                <th>단가</th>
                <td>
                  <input class="input" type="text" v-model="form.unitPrice" />
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>
  </layout>
</template>

<script>
import _ from 'lodash'

import Layout from '@/components/ModalSlot.vue'
import {AgGridVue} from "ag-grid-vue";


export default {
  compatConfig: { MODE: 2 },
  props: {
    invoiceData: {
      type: Object,
      required: true
    },
    itemData: {
      type: Object,
      required: true
    },
    personId: {
      type: String,
      required: true
    }
  },
  components: {
    AgGridVue,
    Layout
  },
  data() {
    return {
      form: {
        customerName: '',
        glDate: '',
        itemName: '',
        remark: '',
        itemQty: '',
        unitPrice: '',
      }
    }
  },
  methods: {
    submit(){
      if(!this.form.itemName){
        this.$swal({ type: 'warning', text: 'test' });
        return
      }

      this.$swal({
        type: 'question',
        text: '수정 하시겠습니까?',
        showCancelButton: true
      }).then(response => {
        if (response.value) {
          //발생대상 제외
          this.$store.commit('loading')
          this.$http.post(`/api/salesTax/modifyItem`, {
            etaxIssueId: this.invoiceData.etaxIssueId,
            itemName: this.form.itemName,
            remark: this.form.remark,
            itemQty: this.form.itemQty,
            unitPrice: this.form.unitPrice,
            createdPersonId: this.personId
          })
            .then(response => {
              this.$swal({ type: 'success', text: response.data.Message });
              if(response.data.Message == "수정 성공"){
                this.$emit('close');
              }
            })
            .catch(response => {

            })
            .finally(() => {
              this.$store.commit('finish')
            })
        }
      })



    },
    getNumberFormat(value){
      if(value){
        if(typeof value === "string"){
          if(val.substr(0,1) !== '-') value = value.replace(/[^0-9]/g, "")
          if(val.substr(0,1) === '-') value = value.replace( /^\[-\]?\\d\*$/g, "")
        }
        return Math.floor(value).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      }
    },
  },
  watch: {
  },
  created() {
    this.form.customerName = this.invoiceData.customerName
    this.form.glDate = this.invoiceData.glDate
    this.form.remark = this.invoiceData.remark
    this.form.itemName = this.itemData.itemName
    this.form.itemQty = this.getNumberFormat(this.itemData.itemQty)
    this.form.unitPrice = this.getNumberFormat(this.itemData.unitPrice)
  },
  beforeMount(){

  },
}
</script>

<style scoped>


.modal-card {
  width: 1200px;
}

</style>
