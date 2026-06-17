<template>
  <div>
    <div class="table-area">
      <div class="table-a fixed-th">
        <div class="table-name">
          <h3 class="ico_table_name">신청구분</h3>
        </div>
        <table class="table">
          <tbody>
          <!-- 헤더 -->
          <tr v-if="isSaved">
            <th>신청서번호</th>
            <td>{{ budReqNo }}</td>

            <th>회사</th>
            <td>{{ data.compNm }}</td>

            <th>신청서상태</th>
            <td>{{ data.budStatNm }}</td>
          </tr>

          <tr v-if="isSaved">
            <th>신청자</th>
            <td>{{ data.wrtNm }}</td>

            <th>소속부서</th>
            <td>{{ data.deptNm }}</td>

            <th>신청일자</th>
            <td>{{ this.$moment(data.wrtDt).format('YYYY-MM-DD') }}</td>
          </tr>
          <!-- //헤더 -->

          <tr>
            <th>예산구분</th>
            <td colspan="5">{{ data.budFgNm }}</td>
          </tr>

          <tr>
            <table-radio title="신청유형" v-model="data" field="brwTypeCd" :options="options['BRW_TYPE_CD']" :colspan="5" :required="!isReadonly" :disabled="!isNew" @input="brwTypeCdChangeEvent" />
          </tr>

          <tr>
            <th :class="{ 'tp-a': !isReadonly }">
              신청사유<br>
              <sup>(<span>{{nowByte}}</span>/{{maxBytes}}bytes)</sup>
            </th>
            <td colspan="5">
              <textarea v-model="data.reqRsn" rows="20" @input="checkReqRsnBytes()" @keydown="onKeyDown"></textarea>
            </td>
          </tr>

          <tr v-if="(manage && ['40', '50', '60', '70'].indexOf(data.budStatCd) >= 0) || (!manage && ['40', '70'].indexOf(data.budStatCd) >= 0)">
            <th>반려사유</th>
            <td colspan="5">
              <textarea v-model="data.rejRsn" :disabled="!manage || (manage && ['40', '70'].indexOf(data.budStatCd) >= 0)"></textarea>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">신청항목</h3>
          </div>
          <div class="btn-wrap btn-type2" v-if="!isReadonly">
            <button class="btn-size btn-w-gray" @click="buttonClickEvent_ResetRow">
              <span class="btn-icon"><i class="fas fa-redo"></i></span>
              초기화
            </button>
            <button class="btn-size btn-w-gray" @click="buttonClickEvent_AddRow">
              <span class="btn-icon"><i class="fas fa-plus"></i></span>
              행추가
            </button>
            <button class="btn-size btn-w-gray" @click="buttonClickEvent_RemoveRow">
              <span class="btn-icon"><i class="fas fa-trash-alt"></i></span>
              행삭제
            </button>
          </div>
        </div>
        <dhx-grid ref="grid" class="slip-grid" v-model="data.budgetDetails" :config="config" @constructGridSuccessful="constructGridSuccessful" v-if="Array.isArray(data.budgetDetails)" />
      </div>
    </div>

    <!--파일업로드-->
    <div class="file-upload" v-if="showWingsAtchPop">
      <div class="table-name">
        <h3 class="ico_table_name">파일보기</h3>
      </div>
      <div class="file has-name" style="width: 1200px;">
        <div class="file" style="margin-right: 10px;">
          <div class="file-label" @click="openUploadWingsPopup()">
            <span class="file-cta">
              <span class="file-label"> 그룹웨어 문서</span>
              <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
            </span>
            <span class="file-name">{{ this.$numeral(wingsFileSize).format('00') }}<i class="far fa-file-alt"></i></span>
          </div>
        </div>

        <div class="file">
          <div class="file-label" @click="openUploadEvidencePopup()">
            <span class="file-cta">
              <span class="file-label"> 증빙첨부</span>
              <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
            </span>
            <span class="file-name">{{ this.$numeral(evidFileSize).format('00') }}<i class="far fa-file-alt"></i></span>
          </div>
        </div>
      </div>
    </div>
    <div class="file-upload" v-else>
      <div class="table-name">
        <h3 class="ico_table_name">파일보기</h3>
      </div>
      <!-- 20190520 퍼블수정 -->
      <div class="file has-name">
        <div class="file-label" @click="openUploadEvidencePopup()">
        <span class="file-cta">
          <span class="file-label">증빙첨부</span>
          <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
        </span>
          <span class="file-name">{{ this.$numeral(evidFileSize).format('00') }}<i class="far fa-file-alt"></i></span>
        </div>
      </div>
      <!-- //20190520 퍼블수정 -->
    </div>
    <!--//파일업로드-->
  </div>
</template>

<script>
import Vue from 'vue'
import createBus from '@/libs/eventBus';
import assert from '@/libs/assert'
import {
  url as _url
} from '@/libs/join'
import _ from 'lodash'
import common from '@/mixin/common';

import DhxGrid from '@/components/DhxGrid.vue'
import TableRadio from '@/components/slip/table/libs/TableRadio'

import Cctr from '@/components/ErpCctr_Ag.vue'
import Account from '@/components/ErpAccount_Ag.vue'
import ErpAccountSub from '@/components/ErpAccountSub_Ag.vue';

import WingsAtchPop from '@/components/JiniAtchPop.vue'

const CctrSearchButton = {
  name: 'CctrSearchButton',
  props: ['index', 'value', 'field'],
  template: `
    <button type="button" class="icon" @click="openModal()"><i class="fas fa-search"></i></button>
  `,
  methods: {
    openModal() {
      let vm = this
      if(this.$parent.$parent.data.brwTypeCd == '10' && this.field == 'sndCctrSrch') return
      // if(this.$parent.$parent.data.brwTypeCd == '10' && this.field == 'rcvCctrSrch') return
      if(this.$parent.$parent.data.brwTypeCd == '20' && this.field == 'sndCctrSrch' &&!this.$parent.$parent.isUnlockUser) return
      if(this.$parent.$parent.data.brwTypeCd == '20' && this.field == 'rcvCctrSrch' &&!this.$parent.$parent.isUnlockUser) return
      if(this.$parent.$parent.data.brwTypeCd == '30' && this.field == 'rcvCctrSrch' &&!this.$parent.$parent.isUnlockUser) return
      if(this.$parent.$parent.data.brwTypeCd == '40' && this.field == 'sndCctrSrch') return
      this.$modal.open({
        component: Cctr,
        parent: this,
        events: {
          close(data) {
            if (vm.onSubmit !== undefined && typeof vm.onSubmit === 'function') {
              vm.onSubmit.apply(vm, [data])
            }
          }
        }
      })
    },
  }
}

const AcctSearchButton = {
  name: 'AcctSearchButton',
  props: ['index', 'value', 'field'],
  template: `
    <button type="button" class="icon" @click="openModal()"><i class="fas fa-search"></i></button>
  `,
  methods: {
    openModal() {
      let vm = this
      let deptCd = undefined
      let budYm = undefined

      if(this.$parent.$parent.data.brwTypeCd == '10' && this.field == 'sndAcctSrch') return

      if(this.field == 'sndAcctSrch') {
        budYm = vm.value.sndYm
        deptCd = vm.value.sndCctrCd
      }else if(this.field == 'rcvAcctSrch'){
        budYm = vm.value.rcvYm
        deptCd = vm.value.rcvCctrCd
      }

      if(_.isEmpty(deptCd)){
        this.$swal({
          type:'warning',
          text:'부서를 먼저 선택하기 바랍니다.'
        })
        return
      }

      this.$modal.open({
        component: Account,
        props: {
          deptCd: deptCd,
          ctrlYn: 'Y'
        },
        parent: this,
        events: {
          close(data) {
            if (vm.onSubmit !== undefined && typeof vm.onSubmit === 'function') {
              vm.onSubmit.apply(vm, [data])
              vm.$modal.open({
                component: ErpAccountSub,
                props: {
                  deptCd: deptCd,
                  acctCd: data.acctCd,
                  year: budYm,
                  ctrlYn: 'Y',
                },
                parent: this,
                events: {
                  close(object) {
                    if (vm.onSubmit !== undefined && typeof vm.onSubmit === 'function') {

                      data.subAcctCd = object.subAcctCd
                      data.subAcctNm = object.subAcctNm

                      vm.onSubmit.apply(vm, [data])
                    }
                  }
                }
              })
            }
          }
        }
      })
    }
  }
}

const SubAcctSearchButton = {
  name: 'SubAcctSearchButton',
  props: ['index', 'value', 'field'],
  template: `
    <button type="button" class="icon" @click="openModal()"><i class="fas fa-search"></i></button>
  `,
  methods: {
    openModal() {
      let vm = this
      let budYm = undefined
      let deptCd = undefined;
      let acctCd = undefined;

      if(this.$parent.$parent.data.brwTypeCd == '10' && this.field == 'sndSubAcctSrch') return

      if(this.field == 'sndSubAcctSrch') {
        budYm = vm.value.sndYm
        deptCd = vm.value.sndCctrCd
        acctCd = vm.value.sndAcctCd
      }else if(this.field == 'rcvSubAcctSrch'){
        budYm = vm.value.rcvYm
        deptCd = vm.value.rcvCctrCd
        acctCd = vm.value.rcvAcctCd
      }

      this.$modal.open({
        component: ErpAccountSub,
        props: {
          deptCd: deptCd,
          acctCd: acctCd,
          year:budYm,
          ctrlYn: 'Y',
        },
        parent: this,
        events: {
          close(data) {
            if (vm.onSubmit !== undefined && typeof vm.onSubmit === 'function') {
              vm.onSubmit.apply(vm, [data])
            }
          }
        }
      })
    },
    mounted(){
     $bus.emit('openModal')
    }
  }
}

const DateRenderer = {
  name: 'DateRenderer',
  props: ['index', 'value', 'field'],
   template: `
    <span v-if="value[field]">{{bdgDate}}</span>
    <span v-else></span>
  `,
  data() {
    return {
      bdgDate: this.$moment(this.value[this.field], 'YYYYMM').format('YYYY-MM')
    }
  },
}

const $bus = createBus()

const budget = {}
const bdg_mutex = {}

function queryRemainBudget(postDt, cctrCd, acctCd, subAcctCd) {
  return new Promise(resolve => {
    let id = [postDt, cctrCd, acctCd, subAcctCd].join('_')
    let valid = (postDt !== undefined && cctrCd !== undefined)
    if (bdg_mutex[id] || !valid) {
      resolve(id)
    } else {
      bdg_mutex[id] = true
      this.$http.post('/api/budget/remain', {
        budYm: postDt,
        budCctrCd: cctrCd,
        budAcctCd: acctCd,
        budSubAcctCd: subAcctCd
      }).then(response => {
        budget[id] = this.$numeral(response.data).value()
        $bus.emit('BUDGET_CMPL', id)
      }).finally(() => {
        delete bdg_mutex[id]
        resolve(budget[id])
      })
    }
  })
}

export default {
  props: {
    id: {
      type: String,
      required: false
    },
    budReqNo: {
      type: String,
      required: false
    },
    manage: {
      type: Boolean,
      required: false,
      default: false
    },
    value: {
      type: Object,
      required: false,
      default () {
        return {
          //예산신청번호
          budReqNo: undefined,
          budgetDetails: [],
          //회사코드
          compCd: undefined,
          //신청상태코드
          budStatCd: '10',
          //예산구분코드
          budFgCd: '10',
          budFgNm: undefined,
          //전용유형코드
          brwTypeCd: undefined,
          //신청총금액
          reqTotAmt: 0,
          //신청사유
          reqRsn: '',
          //ERP관리번호
          erpMngNo: undefined,
          //최종결재일시
          fnlApprDtm: undefined,
          //최종결재자ID
          fnlAprverId: undefined,
          //전자결재ID
          elecApprId: undefined,
          //전자결재기안일시
          elecApprDtm: undefined,
          //반려사유
          rejRsn: undefined,
          //작성자ID
          wrtId: undefined,
          //작성부서코드
          wrtDeptCd: undefined,
          //작성일자
          wrtDt: undefined,
          //증빙관리번호
          evdMngNo: undefined,
          //문서고유값
          docInVal: undefined
        }
      }
    },
    status: {
      type: Object,
      required: false,
      default () {
        return {
          memento: [],
          readonly: false
        }
      }
    },
    readonly: {
      type: Boolean,
      required: false,
      default: false
    },
    showWingsAtchPop: {
      type: Boolean,
      required: false,
      default: true
    }
  },
  mixins: [common],
  components: {
    DhxGrid,
    TableRadio
  },
  data() {
    return {
      data: {},
      tempData: [],
      evidFileSize: 0,
      wingsFileSize: 0,
      objectPopup: null,
      //월간전용 부서 컨트롤
      nowByte: 0,
      maxBytes: 2000,
      isUnlockUser: false,
      config_e: {
        columns: [{
          id: 'budSeq',
          value: 'No.',
          type: 'cntr',
          width: 50
        }, {
          id: 'brwTypeNm',
          value: '신청유형'
        }, {
          id: 'sndYm',
          value: '주는 대상',
          align: 'center',
          type: 'dhxCalendarA',
          component: DateRenderer,
        }, {
          id: 'sndCctrCd',
          value: '#cspan'
        }, {
          id: 'sndCctrNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        }, {
          id: 'sndCctrSrch',
          value: '#cspan',
          width: 50,
          component: CctrSearchButton,
          mixin: {
            methods: {
              onSubmit(data) {
                this.value.sndCctrCd = data.deptCd
                this.value.sndCctrNm = data.deptNm

                this.value.sndAcctCd = undefined
                this.value.sndAcctNm = undefined

                this.value.sndSubAcctCd = undefined
                this.value.sndSubAcctNm = undefined

                switch (this.value.brwTypeCd) {
                  case '20':
                  case '30':
                    this.value.rcvCctrCd = data.deptCd
                    this.value.rcvCctrNm = data.deptNm

                    this.value.rcvAcctCd = undefined
                    this.value.rcvAcctNm = undefined

                    this.value.rcvSubAcctCd = undefined
                    this.value.rcvSubAcctNm = undefined
                    break
                }
              }
            }
          }
        }, {
          id: 'sndAcctCd',
          value: '#cspan'
        }, {
          id: 'sndAcctNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        }, {
          id: 'sndAcctSrch',
          value: '#cspan',
          width: 50,
          component: AcctSearchButton,
          mixin: {
            methods: {
              onSubmit(data) {
                this.value.sndAcctCd = data.acctCd
                this.value.sndAcctNm = data.acctNm

                this.value.sndSubAcctCd = data.subAcctCd;
                this.value.sndSubAcctNm = data.subAcctNm;

                switch (this.value.brwTypeCd) {
                  case '20':
                  case '40':
                    this.value.rcvAcctCd = data.acctCd
                    this.value.rcvAcctNm = data.acctNm
                    break
                }
              }
            }
          }
        },
        {
          id: 'sndSubAcctCd',
          value: '#cspan'
        }, {
          id: 'sndSubAcctNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        }, {
          id: 'sndSubAcctSrch',
          ref: 'sndSubAcctSrch',
          value: '#cspan',
          width: 50,
          component: SubAcctSearchButton,
          mixin: {
            methods: {
              onSubmit(data) {
                this.value.sndSubAcctCd = data.subAcctCd
                this.value.sndSubAcctNm = data.subAcctNm
              }
            }
          }
        },
        {
          id: 'rmdAmt',
          value: '#cspan',
          align: 'right',
          component: {
            props: ['index', 'value', 'field'],
            template: `
            <span style="padding-right: 10px;" v-if="data !== undefined">{{ this.$numeral(data).format('0,0') }}</span>
            <span v-else></span>
            `,
            data() {
              return {
                data: undefined
              }
            },
            created() {
              let id = [this.value.sndYm, this.value.sndCctrCd, this.value.sndAcctCd, this.value.sndSubAcctCd].join('_')
              this.data = this.value.rmdAmt = budget[id]

              if (this.data === undefined && this.value.brwTypeCd != '10') {
                queryRemainBudget.apply(this, [this.value.sndYm, this.value.sndCctrCd, this.value.sndAcctCd, this.value.sndSubAcctCd])
              }

              $bus.on('BUDGET_CMPL', () => {
                this.data = this.value.rmdAmt = budget[id]
              })
            },
            watch: {
              immediate: true,
              deep: true,
              handler() {
                queryRemainBudget.apply(this, [this.value.sndYm, this.value.sndCctrCd, this.value.sndAcctCd, this.value.sndSubAcctCd])
              }
            }
          }
        }, {
          id: 'rcvYm',
          value: '받는 대상',
          align: 'center',
          type: 'dhxCalendarA',
          component: DateRenderer,
        }, {
          id: 'rcvCctrCd',
          value: '#cspan'
        }, {
          id: 'rcvCctrNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        }, {
          id: 'rcvCctrSrch',
          value: '#cspan',
          width: 50,
          component: CctrSearchButton,
          mixin: {
            methods: {
              onSubmit(data) {
                this.value.rcvCctrCd = data.deptCd
                this.value.rcvCctrNm = data.deptNm

                this.value.rcvAcctCd = undefined
                this.value.rcvAcctNm = undefined

                this.value.rcvSubAcctCd = undefined
                this.value.rcvSubAcctNm = undefined

                switch (this.value.brwTypeCd) {
                  case '20':
                    this.value.sndCctrCd = data.deptCd
                    this.value.sndCctrNm = data.deptNm
                    break
                }
              }
            }
          }
        },
        {
          id: 'rcvAcctCd',
          value: '#cspan'
        }, {
          id: 'rcvAcctNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        }, {
          id: 'rcvAcctSrch',
          value: '#cspan',
          width: 50,
          component: AcctSearchButton,
          mixin: {
            methods: {
              onSubmit(data) {
                this.value.rcvAcctCd = data.acctCd
                this.value.rcvAcctNm = data.acctNm

                this.value.rcvSubAcctCd = data.subAcctCd;
                this.value.rcvSubAcctNm = data.subAcctNm;
              }
            }
          }
        },
        {
          id: 'rcvSubAcctCd',
          value: '#cspan'
        }, {
          id: 'rcvSubAcctNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        }, {
          id: 'rcvSubAcctSrch',
          ref: 'rcvSubAcctSrch',
          value: '#cspan',
          width: 50,
          component: SubAcctSearchButton,
          mixin: {
            methods: {
              onSubmit(data) {
                this.value.rcvSubAcctCd = data.subAcctCd
                this.value.rcvSubAcctNm = data.subAcctNm
              }
            }
          }
        },
        {
          id: 'reqAmt',
          value: '신청금액',
          align: 'right',
          type: 'edn'
        }],
        events: {
          onEditCell(stage, rId, cInd, nValue, oValue) {
            if (stage === 2) {
              if (nValue !== oValue) {
                var index = this.instance.getRowIndex(rId)
                let row = this.value[index]
                let c = this.options.columns[cInd]

                if (c.id === 'sndYm' && (row.brwTypeCd === '30' || row.brwTypeCd === '40')) {
                  row.rcvYm = nValue
                } else if (c.id === 'reqAmt') {
                  row.reqAmt = this.$numeral(nValue).value()
                  let reqTotAmt = this.value.map(x => this.$numeral(x.reqAmt).value()).reduce((ac, v) => ac + v)
                  this.$parent.data.reqTotAmt = reqTotAmt
                }
              }
            }
          }
        },
        beforeRefreshData(grid, data) {
          if (grid && Array.isArray(data)) {
            _.forEach(data, (r, i) => {
              var rId = String(i + 1)
              this.unlockAllCells(rId)

              switch (r.brwTypeCd) {
                case '10':
                  this.lockCells(rId, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 17])
                  break;
                case '20':
                  this.lockCells(rId, [11, 12, 13, 14, 15, 16])
                  break;
                case '30':
                  this.lockCells(rId, [13, 14, 15, 16])
                  break;
                case '40':
                  this.lockCells(rId, [7])
                  break;
              }
            })
          }
        },
        afterRefreshData(grid, data) {
          if (grid && Array.isArray(data)) {
            this.$nextTick(() => {
              _.forEach(data, (r, i) => {
                var rId = grid.getRowId(i)
                this.setColumnClassName(rId, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17], 'bg-grey', true)
                // this.setColumnClassName(rId, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23], 'bg-lightpink', true)
                // this.setColumnClassName(rId, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23], 'bg-yellow', true)
                switch (r.brwTypeCd) {
                  case '10':
                    this.setColumnClassName(rId, [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], 'bg-grey')
                    this.setColumnClassName(rId, [13, 15, 16, 18, 19, 21, 22, 23], 'bg-lightpink')
                    this.lockCells(rId, [2])
                    break
                  case '20':
                    this.setColumnClassName(rId, [3,4,5,8,9,10], 'bg-grey')
                    this.setColumnClassName(rId, [2,7,11], 'bg-lightpink')
                    this.setColumnClassName(rId, 6, 'bg-yellow')
                    this.lockCells(rId, [8])
                    break
                  case '30':
                    this.setColumnClassName(rId, [13, 14, 15, 16], 'bg-grey')
                    this.setColumnClassName(rId, [2, 4, 5, 7, 8, 10, 11, 18, 19, 21, 22,23], 'bg-lightpink')
                    this.setColumnClassName(rId, 12, 'bg-yellow')
                    this.lockCells(rId, [13])
                    break
                  case '40':
                    this.setColumnClassName(rId, [3,4,5,7], 'bg-grey')
                    this.setColumnClassName(rId, [2,8,9,10,11], 'bg-lightpink')
                    this.setColumnClassName(rId, 6, 'bg-yellow')
                    this.lockCells(rId, [7])
                    break
                }
              })
              grid.callEvent('ongridreconstructed', [])
            })
          }
        }
      },
      config_r: {
        columns: [{
          id: 'budSeq',
          value: 'No.',
          width: 50
        }, {
          id: 'brwTypeNm',
          value: '신청유형'
        }, {
          id: 'sndYm',
          value: '주는 대상',
          component: DateRenderer
        }, {
          id: 'sndCctrCd',
          value: '#cspan'
        }, {
          id: 'sndCctrNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        },
        {
          id: 'sndAcctCd',
          value: '#cspan'
        }, {
          id: 'sndAcctNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        },
        {
          id: 'sndSubAcctCd',
          value: '#cspan'
        }, {
          id: 'sndSubAcctNm',
          value: '#cspan',
          width: 180,
          align: 'left'
        }, {
            id: 'rmdAmt',
            value: '#cspan',
            align: 'right',
            type: 'ron'
        },{
          id: 'rcvYm',
          value: '받는 대상',
          component: DateRenderer
        }, {
          id: 'rcvCctrCd',
          value: '#cspan'
        }, {
          id: 'rcvCctrNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        },
        {
          id: 'rcvAcctCd',
          value: '#cspan'
        }, {
          id: 'rcvAcctNm',
          value: '#cspan',
          width: 150,
          align: 'left'
        },
        {
          id: 'rcvSubAcctCd',
          value: '#cspan'
        }, {
          id: 'rcvSubAcctNm',
          value: '#cspan',
          width: 180,
          align: 'left'
        },
        {
          id: 'reqAmt',
          value: '신청금액',
          align: 'right',
          type: 'ron'
        }],
        afterRefreshData(grid, data) {
          if (grid && Array.isArray(data)) {
            this.$nextTick(() => {
              _.forEach(data, (r, i) => {
                var rId = grid.getRowId(i)
                this.setColumnClassName(rId, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17], 'bg-grey', true)
                switch (r.brwTypeCd) {
                  case '10':
                    this.setColumnClassName(rId, [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], 'bg-grey')
                    break
                  case '20':
                    this.setColumnClassName(rId, [8, 9, 10, 11], 'bg-grey')
                    break
                  case '30':
                    this.setColumnClassName(rId, [10, 11, 12], 'bg-grey')
                    break
                  case '40':
                    this.setColumnClassName(rId, [7, 10, 11], 'bg-grey')
                    break
                }
              })
              grid.callEvent('ongridreconstructed', [])
            })
          }
        }
      },
      options: {
        'BRW_TYPE_CD': [],
        'BUD_FG_CD': []
      },
      mutex: {}
    }
  },
  computed: {
    isNew() {
      return this.budReqNo === undefined && (this.data.budReqNo === undefined ||this.data.budReqNo.substring(0,3) === 'TMP')
    },
    isSaved() {
      return this.budReqNo !== undefined || (this.data.budReqNo === undefined ||this.data.budReqNo.substring(0,3) !== 'TMP')
    },
    config() {
      return this.isReadonly ? this.config_r : this.config_e
    },
    getId() {
      return this.budReqNo || this.data.budReqNo
    },
    isReadonly() {
      return this.status.readonly || this.readonly
    }
  },
  methods: {
    checkReqRsnBytes(){
      //입력한 문자수
      let textLenth = this.data.reqRsn.length
      let text = this.data.reqRsn
      let totalByte = 0
      for(let i = 0;i<textLenth;i++){
        let eachChar = text.charAt(i);
        totalByte += this.charByteSize(eachChar)
      }
      this.nowByte = totalByte;
       if(totalByte > this.maxBytes){
         alert('최대 ' + this.maxBytes + "Byte까지만 입력 가능합니다.")
         this.data.reqRsn = this.cutByteLength(this.data.reqRsn, this.maxBytes)
         this.checkReqRsnBytes();
      }
    },
    onKeyDown(event){
      if (this.nowByte >= this.maxBytes) {
        if (event.keyCode >= 48 && event.keyCode <= 90) {
          alert('최대 ' + this.maxBytes + "Byte까지만 입력 가능합니다.")
          event.preventDefault()
          return
        }
      }
    },
    queryBrwTypeCd() {
      return new Promise(resolve => {
        if (this.mutex['BRW_TYPE_CD']) {
          resolve()
        } else {
          this.mutex['BRW_TYPE_CD'] = true
          this.$store.commit('loading')
          this.$http.get('/api/code/combo', {
            params: {
              groupCd: 'BRW_TYPE_CD'
            }
          }).then(response => {
            this.options['BRW_TYPE_CD'] = response.data.map(x => {
              x.label = x.value
              x.value = x.key
              return x
            })
          }).finally(() => {
            this.$store.commit('finish')
            delete this.mutex['BRW_TYPE_CD']
            return resolve()
          })
        }
      })
    },
    queryBudFgCd() {
      return new Promise(resolve => {
        if (this.mutex['BUD_FG_CD']) {
          resolve()
        } else {
          this.mutex['BUD_FG_CD'] = true
          this.$store.commit('loading')
          this.$http.get('/api/code/combo', {
            params: {
              groupCd: 'BUD_FG_CD'
            }
          }).then(response => {
            this.options['BUD_FG_CD'] = response.data
          }).finally(() => {
            this.$store.commit('finish')
            delete this.mutex['BUD_FG_CD']
            return resolve()
          })
        }
      })
    },
    queryBdgtReq() {
      return new Promise((resolve, reject) => {
        if(this.getId) {
          this.$store.commit('loading')
          this.$http.get(`/api/budget/${this.getId}`)
              .then(response => {
                let data = response.data.budgetHeader
                data.budgetDetails = response.data.budgetDetails

                this.data = data
                this.$emit('input', this.data)

                if (this.manage) {
                  this.status.readonly = true
                } else {
                  this.status.readonly = (this.data.budStatCd !== '10')
                }

                this.checkReqRsnBytes()

                return resolve(response)
              })
              .catch(response => {
                return reject(response)
              })
              .finally(() => {
                this.$store.commit('finish')
              })
        }
      })
    },
    constructGridSuccessful(grid) {
      var header_style = 'color:#222;text-align:center;'
      var stat_total_text = 'color:red;text-align:right;padding-right:10px !important;'

      if (!this.isReadonly) {
        grid.attachHeader(
            ['#rspan', '#rspan', '연월', '부서', '#cspan', '#cspan', '계정', '#cspan', '#cspan', '보조계정', '#cspan', '#cspan', '잔액', '연월', '부서', '#cspan', '#cspan', '계정', '#cspan', '#cspan', '보조계정', '#cspan', '#cspan', '#rspan']
        )
        grid.attachHeader(
            ['합계', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#stat_total'],
            [header_style, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', stat_total_text]
        )

        grid.setDateFormat('%Y-%m', '%Y%m')
        grid.setNumberFormat('0,000', 12, '.', ',')
        grid.setNumberFormat('0,000', 23, '.', ',')
      } else {
        grid.attachHeader(
            ['#rspan', '#rspan', '연월', '부서', '계정', '#cspan', '#cspan', '보조계정', '#cspan', '잔액', '연월', '부서', '#cspan', '계정', '#cspan', '보조계정', '#cspan', '#rspan']
        )
        grid.attachHeader(
            ['합계', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#stat_total'],
            [header_style, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', stat_total_text]
        )

        grid.setNumberFormat('0,000', 9, '.', ',')
        grid.setNumberFormat('0,000', 17, '.', ',')
      }
    },
    reset_rows() {
      this.data.budgetDetails = []
      this.add_row_alter()
    },
    add_row(data) {
      data = data || {}
      this.data.budgetDetails.push({
        budReqNo: this.data.budReqNo,
        budSeq: undefined,
        compCd: this.data.compCd,
        brwTypeCd: this.data.brwTypeCd,
        brwTypeNm: this.data.brwTypeNm,
        sndYm: data.sndYm,
        sndCctrCd: data.sndCctrCd,
        sndCctrNm: data.sndCctrNm,
        sndAcctCd: data.sndAcctCd,
        sndAcctNm: data.sndAcctNm,
        sndSubAcctCd: data.sndSubcctCd,
        sndSubAcctNm: data.sndSubAcctNm,
        rmdAmt: data.rmdAmt,
        rcvYm: data.rcvYm,
        rcvCctrCd: data.rcvCctrCd,
        rcvCctrNm: data.rcvCctrNm,
        rcvAcctCd: data.rcvAcctCd,
        rcvAcctNm: data.rcvAcctNm,
        rcvSubAcctCd: data.rcvSubAcctCd,
        rcvSubAcctNm: data.rcvSubAcctNm,
        reqAmt: 0
      })
    },
    add_row_alter() {
      const openDt = this.$store.state.openDt
      switch (this.data.brwTypeCd) {
        case '10':
          this.add_row({
            rcvYm: this.$moment(openDt).format('YYYYMM'),
            rcvCctrCd: this.data.deptCd,
            rcvCctrNm: this.data.deptNm
          })
          break
        case '20':
          this.add_row({
            sndYm: this.$moment(openDt).format('YYYYMM'),
            sndCctrCd: this.data.deptCd,
            sndCctrNm: this.data.deptNm,
            rcvYm: this.$moment(openDt).format('YYYYMM'),
            rcvCctrCd: this.data.deptCd,
            rcvCctrNm: this.data.deptNm
          })
          break
        case '30':
          this.add_row({
            sndYm: this.$moment(openDt).format('YYYYMM'),
            sndCctrCd: this.data.deptCd,
            sndCctrNm: this.data.deptNm,
            rcvYm: this.$moment(openDt).format('YYYYMM'),
            rcvCctrCd: this.data.deptCd,
            rcvCctrNm: this.data.deptNm
          })
          break
        case '40':
          this.add_row({
            sndYm: this.$moment(openDt).format('YYYYMM'),
            sndCctrCd: this.data.deptCd,
            sndCctrNm: this.data.deptNm,
            rcvYm: this.$moment(openDt).format('YYYYMM'),
            rcvCctrCd: this.data.deptCd,
            rcvCctrNm: this.data.deptNm
          })
          break
      }
    },
    /**
     * 신청유형 변경 이벤트
     */
    brwTypeCdChangeEvent() {
      let f = this.options['BRW_TYPE_CD'].filter(x => x.value === this.data.brwTypeCd)[0]
      this.data.brwTypeNm = f !== undefined ? f.label : this.data.brwTypeCd
      this.reset_rows()
    },
    /**
     * 버튼 이벤트
     */
    buttonClickEvent_ResetRow() {
      this.reset_rows()
    },
    buttonClickEvent_AddRow() {
      this.add_row_alter()
    },
    buttonClickEvent_RemoveRow() {
      var rId = this.$refs.grid.instance.getSelectedRowId()
      if (rId === null) {
        this.$swal({
          type: 'warning',
          text: '삭제하려는 행을 선택해주십시오.'
        })
      } else {
        var index = this.$refs.grid.instance.getRowIndex(rId)
        this.data.budgetDetails.splice(index, 1)
      }
    },
    openUploadWingsPopup() {

      let vm = this

      let chkReadOnly = this.isReadonly

      const authority = this.$store.state.loginInfo.authorities[0].roleCd
      /*
        - 작성자 & 재무팀 후첨 -
        결재완료 상태까지 작성자와 재무팀/경영관리팀은 후첨 가능
      */
      if (this.value.budStatCd === '20' || this.value.budStatCd === '30' || this.value.budStatCd === '50') {
        if (this.data.wrtId === vm.$parent.$store.state.loginInfo.loginId) {
          chkReadOnly = false
        }
        if (authority === 'ADMIN' || authority === 'F_USER' || authority === 'P_USER') {
          chkReadOnly = false
        }
      }

      var propBudReqNo = this.data.budReqNo;

      if (vm.data.budReqNo !== undefined && vm.data.budReqNo.substring(0, 3) === 'TMP') {
        this.$store.commit('loading');

        this.$modal.open({
          component: WingsAtchPop,
          props: {
            grSlipNo: propBudReqNo,
            slipDetail: this.tempData,
            readonly: chkReadOnly // Check readonly
          },
          parent: this,
          width: 1200,
          events: {
            close(value) {
              //신규작성 - 임시번호 전달
              if (!vm.data.budReqNo) {
                vm.data.budReqNo = value[0].grSlipNo
              }
              vm.wingsFileSize = value.length
              vm.tempData = value
            }
          }
        })

      } else {
        this.$modal.open({
          component: WingsAtchPop,
          props: {
            grSlipNo: propBudReqNo,
            slipDetail: this.tempData,
            readonly: chkReadOnly // Check readonly
          },
          parent: this,
          width: 1200,
          events: {
            close(value) {
              //신규작성 - 임시번호 전달
              if (!vm.data.budReqNo) {
                vm.data.budReqNo = value[0].grSlipNo
              }
              vm.wingsFileSize = value.length
              vm.tempData = value
            }
          }
        })
      }
    },
    openUploadEvidencePopup() {
      let vm = this

      let rdoCtrl = true
      let readonly = this.isReadonly
      const authority = this.$store.state.loginInfo.authorities[0].roleCd

      // 작성단계가 아니면 수정불가
      if(_.trim(this.value.budStatCd) !== '' && this.value.budStatCd !== '10' ) {
        readonly = true
        rdoCtrl = true
      } else {
        readonly = false
        rdoCtrl = false
      }

      /*
        - 작성자 & 재무팀 후첨 -
        결재완료 상태까지 작성자와 재무팀은 후첨 가능
      */
      if(this.value.budStatCd === '20' || this.value.budStatCd === '30' || this.value.budStatCd === '50'){
        if(this.data.wrtId === vm.$parent.$store.state.loginInfo.loginId){
          rdoCtrl = false
        }
        if(authority === 'ADMIN' || authority === 'F_USER' || authority === 'P_USER'){
          rdoCtrl = false
        }
      }

      let url = '/evidAtchPopModeless?docMngNo=' + this.getId + '&readonly='+ readonly + '&readonlyCtrl='+ rdoCtrl;

      if(!(!this.objectPopup || this.objectPopup.closed)){
        this.objectPopup.focus();
      }else{
        this.objectPopup = window.open(url, '_blank', 'toolbar=0,location=0,menubar=0,resizable=yes');
      }
      //팝업 Close Callback
      //브라우저 체크(IE 체크)
      var agent = navigator.userAgent.toLowerCase();

      if ((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1)){
        this.objectPopup.attachEvent("onunload",function(){
          vm.evidFileSize = localStorage.getItem("popFileCnt");
        });
      }else{
        this.objectPopup.onbeforeunload = function(){
          vm.evidFileSize = localStorage.getItem("popFileCnt");
        };
      }
    },
    queryEvidFile() {
      /**
       * 증빙파일이 얼마나 첨부되었는지 확인해보자
       */
      if(this.getId) {
        this.$http.get(`/api/evid/fileList/${this.getId}`)
            .then(response => {
              this.evidFileSize = ((response.data || {}).aFiles || []).length
            })
      }
    },
    queryWingsFile() {
      /**
       * Wings파일이 얼마나 첨부되었는지 확인해보자
       */
      if(this.getId) {
        this.$http.get(`/api/wings/document/${this.getId}`)
            .then(response => {
              this.wingsFileSize = response.data.length
            })
      }
    },
  },
  created() {
    this.queryBrwTypeCd()
        .then(() => {
          if (this.data.brwTypeCd === undefined) {
            this.data.brwTypeCd = this.options['BRW_TYPE_CD'][0].value
            this.data.brwTypeNm = this.options['BRW_TYPE_CD'][0].label
            if (this.isNew) {
              this.reset_rows()
            }
            this.status.memento = [JSON.stringify(this.data)]
          }
        })

    if (this.isNew) {
      this.queryBudFgCd()
          .then(() => {
            let f = this.options['BUD_FG_CD'].filter(x => x.key === this.data.budFgCd)[0]
            this.data.budFgNm = f !== undefined ? f.value : this.data.budFgCd
            this.status.memento = [JSON.stringify(this.data)]
          })
    }

    if (this.isSaved) {
      this.queryBdgtReq()
          .then(() => {
            this.status.memento = [JSON.stringify(this.data)]
          })
    }

    // find logged in user
    let l = undefined
    try {
      l = this.$store.state.loginInfo.loginId
    } catch (e) {
      l = undefined
    }
    this.$http.get(_url('/api/emp', l))
        .then(response => {
          if (response.data.length > 0) {
            this.data.deptCd = response.data[0].cctrCd
            this.data.deptNm = response.data[0].cctrNm
            if (this.isNew) {
              this.reset_rows()
            }
          }
        })
        .finally(() => {
          this.status.memento = [JSON.stringify(this.data)]
        })
    if(this.$store.state.loginInfo.authorities[0].roleCd === 'P_USER' || this.$store.state.loginInfo.authorities[0].roleCd === 'ADMIN'){
      this.isUnlockUser = true
    }else{
      this.isUnlockUser = false
    }
  },
  beforeMount(){
    if(!this.budReqNo) {
      // 임시번호 채번
      this.$http.get('/api/tmp/documentMngNo')
          .then(response => {
            this.data.budReqNo = response.data

            this.queryEvidFile(response.data);

            this.status.change = false
            this.status.loaded = true
            this.status.readonly = false

            // setTimeout(() => {
            //   this.status.memento = [_.clone(this.data)]
            // }, 1500)
          })

    }else{
      this.queryEvidFile(this.budReqNo)
      this.queryWingsFile(this.budReqNo)
    }
  },
  watch: {
    'data': {
      deep: true,
      handler(value) {
        let v = JSON.stringify(value)
        var index = this.status.memento.indexOf(v)
        if (index >= 0) {
          this.status.memento.splice(0, index)
        } else {
          this.status.memento.unshift(v)
        }
      }
    },
    'value': {
      immediate: true,
      deep: true,
      handler(value) {
        this.data = value
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.table-area table {
  table-layout: fixed;
}

.table-area table th {
  width: 150px;
}

.table-area .table-a table tr td {
  width: calc(33.33% - 150px);
  vertical-align: middle;
}

.table-area {
  :global(table) {
    :global(th) {
      vertical-align: middle !important;
    }

    :global(td) {
      vertical-align: middle !important;
    }
  }
}

.table-area .table-a table th.tp-a:before {
  content: "*";
  position: absolute;
  left: 11px;
  top: calc(50% + 2px);
  transform: translateY(-50%);
}
</style>
