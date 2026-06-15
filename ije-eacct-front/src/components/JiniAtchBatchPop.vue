<template>
<layout>
  <span slot="header">
    {{ title }}
    <button class="btn-pop-close delete" aria-label="close" @click="closeModal()"></button>
  </span>
  <div slot="content">
    <div class="btn-wrap btn-type2 fl_right" :style="{display: readonly ? 'none' : ''}">
      <button class="btn-size btn-w-gray" @click="addJiniRow()">
        <span class="btn-icon">
          <i class="fas fa-plus"></i>
        </span>
        행추가
      </button>
      <button class="btn-size btn-w-gray" @click="removeRow()">
        <span class="btn-icon">
          <i class="fas fa-trash-alt"></i>
        </span>
        행삭제
      </button>
      <button class="btn-size btn-w-gray" @click="goSubmit()">
        <span class="btn-icon">
          <i class="fas fa-save"></i>
        </span>
        저장
      </button>
    </div>
    <div class="table-b">
      <div class="table-header">
        <dhx-grid ref="grid" v-model="slipDetails" class="slip-grid" :config="config" @constructGridSuccessful="constructGridSuccessful" />
      </div>
    </div>
  </div>
</layout>
</template>

<script>
import _ from 'lodash'

import Layout from '@/components/ModalSlot.vue'
import DhxGrid from '@/components/DhxGrid.vue'
import Vendor from '@/components/Vendor_Ag.vue'
import Emp from '@/components/Emp_Ag.vue'
import assert from '@/libs/assert'

//['docMngNo', 'value', 'readonly']
export default {
  props: {
    slipNo: {
      type: Array,
      required: true
    },
    readonly: {
      type: Boolean,
      required: false,
      default: false
    },
  },
  mixins: [],
  components: {
    Layout,
    DhxGrid
  },
  data() {
    return {
      title: '그룹웨어 일괄적용',
      slipDetails: [],
      config:{
        columns: [
          {
          id: 'itemSeq',
          align: 'center',
          value: 'No.',
          type: 'cntr',
          width: 10
        }, {
          id: 'jiniNm',
          align: 'center',
          value: '문서명',
          type: 'ed',
          width: 50
        }, {
          id: 'jiniId',
          align: 'center',
          value: '문서번호',
          type: 'ed',
          width: 60
        }, {
          id: 'jiniUrl',
          align: 'center',
          value: '문서보기',
          width: 30,
          component: {
            props: ['index', 'value', 'field'],
            template: `<span style="color: #0065b3;" @click="goLink()"> <i class="far fa-file-alt"></i></span>`,
            methods: {
              goLink(){
                if(this.value.jiniUrl !== undefined) window.open(this.value.jiniUrl, "_blank");
                else{
                  let mainUrl = "https://jini.iljin.co.kr/ekp/service/openapi/IF_CUS_EAP_002_goView?APP_ID=";
                  let jiniId = this.value.jiniId.split('_')[1];
                  let jiniUrl = mainUrl + jiniId;
                  window.open(jiniUrl, "_blank");
                }
              }
            }
          }
        }],
        events: {
          onEditCell(stage, rId, cInd) {
            if (cInd === 3) {
              if(!this.data[rId - 1].new){
                return false
              }
            }
            return true
          }
        },
      },
      chkYn : false
    }
  },
  methods: {
    addJiniRow(){
      this.slipDetails.push({
        jiniId:'',
        jiniNm:'',
        jiniUrl:undefined,
      })
    },
    removeRow() {
      let index = this.$refs.grid.instance.getSelectedRowId() - 1
      this.slipDetails.splice(index,1)
    },
    closeModal() {
      this.$emit('close')
    },
    constructGridSuccessful(grid) {
      grid.attachHeader([],[])
    },
    goSubmit() {
      let jiniList = this.slipDetails;
      if(jiniList.length === 0) {
        this.$swal({ type: 'info', text: '적용할 기안서를 추가해 주세요.' });
        return false;
      }

      for(let i=0; i < jiniList.length; i++){
        let row = i + 1
        if(jiniList[i].jiniNm === undefined || jiniList[i].jiniNm === '') throw row+'행 문서명을 입력해주세요.'
        if(jiniList[i].jiniId === undefined || jiniList[i].jiniId === '') throw row+'행 문서번호를 입력해주세요.'
        if(jiniList[i].jiniNm.indexOf(',') > -1) throw row+'행 문서명에 "," 입력할 수 없습니다.'
        if(jiniList[i].jiniId.indexOf('http') > -1) throw row+'행 문서번호가 잘못되었습니다.'
      }

      const that = this;
      this.$swal({
        type: 'question',
        text: '기안서 일괄 적용 하시겠습니까?',
        showCancelButton: true
      }).then(r => {
        if (r.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/jini/bundle`, {
            slipNoList : that.slipNo,
            jiniDtoList : jiniList,
          })
          .then((response) => {
            this.$swal({
              type: 'success',
              text: '기안서가 일괄 적용되었습니다.'
            })
            this.$emit('close', jiniList)
          })
          .finally(() => {
            this.$store.commit('finish')
          });
        }})
    },
  },
}

function _url(...args) {
  args = args.map(x => String(x || '').trim().replace(/^\/*|\/*$/g, ''))
  args = args.filter(x => x)
  return args.join('/')
}
</script>

<style lang="scss" scoped>
.modal-card {
  width: 1200px;
}

.display-area {
  :global(img) {
    width: 100%;
  }
}
</style>
