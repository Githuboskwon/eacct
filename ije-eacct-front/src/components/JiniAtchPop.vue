<template>
<layout>
  <span slot="header">
    {{ title }}
    <button class="btn-pop-close delete" aria-label="close" @click="closeModal()"></button>
  </span>
  <div slot="content">
    <div class="btn-wrap btn-type2 fl_right" :style="{display: readOnly ? 'none' : ''}">
      <button class="btn-size btn-w-gray" @click="buttonClickEventAdd()">
        <span class="btn-icon">
          <i class="fas fa-plus"></i>
        </span>
        행추가
      </button>
      <button class="btn-size btn-w-gray" @click="buttonClickEventRemove()">
        <span class="btn-icon">
          <i class="fas fa-trash-alt"></i>
        </span>
        행삭제
      </button>
      <button class="btn-size btn-w-gray" @click="buttenClickEventSave()">
        <span class="btn-icon">
          <i class="fas fa-save"></i>
        </span>
        저장
      </button>
    </div>
    <div class="table-b">
      <div class="table-header">
    <!-- DhxGrid component -->
      <dhx-grid ref="grid" v-model="slipDetails" class="slip-grid" :config="config" @constructGridSuccessful="constructGridSuccessful" />
    <!-- //DhxGrid component -->
      </div>
    </div>
  </div>
</layout>
</template>

<script>
import _ from 'lodash'

import Layout from '@/components/ModalSlot.vue'
import DhxGrid from '@/components/DhxGrid.vue'
import assert from '@/libs/assert'
//['docMngNo', 'value', 'readonly']
export default {
  props: {
    slipNo: {
      type: String,
      required: false
    }, readOnly: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  mixins: [],
  components: {
    Layout,
    DhxGrid
  },
  data() {
    return {
      title: '그룹웨어 문서',
      slipDetails: [],
      config:{
        columns: [{
          id: 'itemSeq',
          align: 'center',
          value: 'No.',
          type: 'cntr',
          width: 20
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
                if(this.value.jiniUrl !== undefined){
                  if(this.value.jiniUrl.indexOf("appro.nsf") != -1 || this.value.jiniUrl.indexOf("approcab") != -1){
                    let mainUrl = "https://jini.iljin.co.kr/ekp/service/openapi/IF_CUS_EAP_002_goView?APP_ID=";
                    let substringUrl = this.value.jiniUrl.substring(0, this.value.jiniUrl.indexOf('?'))
                    substringUrl = substringUrl.substring(substringUrl.length - 32)
                    window.open(mainUrl + substringUrl, "_blank");
                  }else{
                    window.open(this.value.jiniUrl, "_blank");
                  }
                }
              }
            }
          }
        }],
        events: {
          onEditCell(stage, rId, cInd, nValue, oValue){
            let row = this.data[rId - 1]
            let colDef = this.options.columns[cInd]
            if (stage === 2 && nValue !== oValue) {
              switch (colDef.id) {
                case 'jiniId':
                  try {
                    if(nValue.length < 1) throw '문서번호를 입력해 주세요.'
                    // Delay 200ms
                    setTimeout(() => {
                      if(nValue.indexOf('_AA')){
                        let mainUrl = "https://jini.iljin.co.kr/ekp/service/openapi/IF_CUS_EAP_002_goView?APP_ID=";
                        row.jiniId = nValue
                        row.jiniUrl = mainUrl + nValue.split('_')[1]
                      }else{
                        row.jiniId = nValue
                        row.jiniUrl = nValue
                      }
                    }, 200)
                  }catch(e){
                    setTimeout(() => {
                      this.$swal({
                        type: 'warning',
                        text: e
                      }).then(()=> {
                        row.jiniId = undefined
                      })
                    }, 200)
                  }

                  break
              }
            }

            return true

          },

        },
      }
    }
  },
  methods: {
    closeModal() {
      this.$emit('close', this.slipDetails)
      // this.$parent.close();
    },
    constructGridSuccessful(grid) {
      grid.attachHeader([],[])
    },
    goSearch() {
      this.$store.commit('loading');
      this.$http.get(_url(`/api/jini/document/${this.slipNo}`))
            .then(response => {
              if(response.data) {
                this.slipDetails = response.data
                this.slipDetails.forEach(x => {
                  x.slipNo = x.documentId
                  delete x.documentId
                })
              }

            }).catch(response => {

            }).finally(() => {
              this.$store.commit('finish');
            })
    },
    buttonClickEventAdd() {
      this.add_row({
        dcCd: 'D',
        lnTypeCd: 'ITEM',
        slipNo: this.slipNo,
        jiniNm: undefined,
        jiniId: undefined,
        jiniUrl: undefined
      })

    },
    buttonClickEventRemove(){
      this.remove_row()
    },
    buttenClickEventSave(){
      try{
        let datas = this.slipDetails

        let deleteInit = false
        //if(datas.length == 0) throw '추가된 행이 존재하지 않습니다.'

        for(let i=0; i < datas.length; i++){
          datas[i].new = false
          let row = i + 1
          if(datas[i].jiniNm === undefined || datas[i].jiniNm === '') throw row+'행 문서명을 입력해주세요.'
          if(datas[i].jiniId === undefined || datas[i].jiniId === '') throw row+'행 문서번호를 입력해주세요.'
          if(datas[i].jiniNm.indexOf(',') > -1) throw row+'행 문서명에 "," 입력할 수 없습니다.'
          if(datas[i].jiniId.indexOf('http') > -1) throw row+'행 문서번호가 잘못되었습니다.'
        }

        if(datas.length == 0){
          this.slipDetails.push({
            dcCd: 'D',
            lnTypeCd: 'ITEM',
            slipNo: this.slipNo,
            jiniNm: '',
            jiniId: '',
            jiniUrl: undefined,
            new: true
          })

          deleteInit = true
        }

        this.$nextTick(() => {
          this.$store.commit('loading');
          this.$http.post(_url('/api/jini/document'), datas)
          .then(response => {
            datas.forEach((x, index) => {
              x.slipNo = response.data[index].documentId
            })
            this.$swal({ type: 'success', text: '저장되었습니다.' });
            // 기안서 연동 카운트 추가
            if(deleteInit){
              this.$store.commit('setJiniSize', 0)
            }else{
              this.$store.commit('setJiniSize', datas.length)
            }
            this.$emit('close', datas)
          }).catch(response => {
            this.$swal({
              type: 'error',
              text: '그룹웨어 문서 전송에 실패하였습니다.'
            })
            return response
          }).finally(() => {
            this.$store.commit('finish');
          })
        }, 200)

      }catch(e){
        this.$swal({
          type: 'warning',
          text: e
        })
      }
    },
    add_row(data) {
      data = data || {}
      this.slipDetails.push({
          dcCd: 'D',
          lnTypeCd: 'ITEM',
          slipNo: this.slipNo,
          jiniNm: undefined,
          jiniId: undefined,
          jiniUrl: undefined,
          new: true
      })

    },
    removeRowConfirm(){
      let index = this.$refs.grid.instance.getSelectedRowId() - 1
      var datas = this.slipDetails.splice(index,1)
      /*return new Promise((resolve,reject) =>{
        this.$swal({
          type: 'info',
          text: `그룹웨어 문서를 삭제합니다. 계속 하시겠습니까?`,
          showCancelButton: true,
          confirmButtonText: '예',
          cancelButtonText: '아니오',
        }).then(response => {
          if(response.value){
            resolve()
          }else {
            reject()
          }
        })
      })*/
    },
    remove_row(){
      this.removeRowConfirm().then(() => {
        //TODO 컨펌창
        let index = this.$refs.grid.instance.getSelectedRowId() - 1
        let deleteList = []
        let rowdata = this.$refs.grid.value[index]
        var datas = this.slipDetails.splice(index,1)
        deleteList.push(rowdata)
        try{
          if(this.$refs.grid.instance.getSelectedRowId() == null) throw '선택된 행이 없습니다.'
          this.$store.commit('loading');
          this.$http.post(_url('/api/jini/document/delete'), deleteList)
              .then(response => {
                if(response){
                  this.$swal({ type: 'success', text: '삭제 되었습니다.' });
                  //this.$emit('close', datas)
                }
              }).catch(response => {
            this.$swal({ type: 'warning', text: '그룹웨어 문서 삭제중 오류가 발생했습니다.' });
            console.log(response)
            this.goSearch()
          }).finally(() => {
            this.$store.commit('finish');
          })
          this.slipDetails.splice(index, 1)
          this.$refs.grid.instance.callEvent('onGridReconstructed', [])
        }catch(e){
          this.$swal({
            type: 'warning',
            text: e
          })
        }
      })
    }
  },
  created() {
    if(this.$store.state.loginInfo.authorities[0].roleCd === 'ADMIN' || this.$store.state.loginInfo.authorities[0].roleCd === 'ACCOUNT'){
      this.readOnly = false;
    }
  },
  mounted() {
    this.goSearch()
  },
  watch: {
    slipDetail: {
      immediate: true,
      deep: true,
      handler(value) {
        if(value !== undefined){
          this.slipDetails = value
        }
      }
    }
  }

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
