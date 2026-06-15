<template>
<div class="table-b">
  <div class="table-header">
    <div class="table-name">
      <h3 v-if="value.slipTypeCd === 'E1'" class="ico_table_name">법인카드지급</h3>
      <h3 v-else class="ico_table_name">세부항목</h3>
    </div>
  </div>

  <!-- DhxGrid component -->
  <dhx-grid ref="grid" class="slip-grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" />
  <div class="table-header" v-if="value.slipTypeCd === 'E1'">
    <div class="table-name">
      <h3 class="ico_table_name">현금지급</h3>
    </div>
    <dhx-grid ref="grid2" class="slip-grid"  v-model="datad" :config="config_E1_2" @constructGridSuccessful="constructGridSuccessful2" />
  </div>
  <!-- //DhxGrid component -->
</div>
</template>

<script>
import Vue from 'vue'

// Import DhxGrid
import DhxGrid from '@/components/DhxGrid.vue'
import Cctr from '@/components/Cctr.vue'
import Account from '@/components/Account.vue'
import ErpAccount from '@/components/ErpAccount.vue'
import Product from '@/components/Product.vue'
import SlipMngItemPop from '@/components/SlipMngItemPop.vue'
import GridSelect from '@/components/grid/GridSelect.vue'
import EvidAtchPop from '@/components/EvidAtchPop.vue'
import CardInfoDetailPop from '@/components/CardInfoDetailPop.vue'

const eventBus = new Vue()

/**
 * 계정 관련 추가 정보
 */
const account = {}
const account_lock = {}

const options = {
  'USE_SELECT_CD': [{"detailCd": "Y", "detailNm": "선택함"}, {"detailCd": "N", "detailNm": "선택안함"}]
}

function queryAccountAddon(acctCd) {
  return new Promise((resolve, reject) => {
    if (!acctCd || account[acctCd] !== undefined || account_lock[acctCd]) {
      resolve(0)
    } else {
      account_lock[acctCd] = true
      this.$http.post('/api/slip/managementItem', {
        acctCd: acctCd,
        useYn: 'Y'
      }).then(response => {
        let filter = response.data.filter(x => x.acctCd === acctCd)
        account[acctCd] = filter.length
        eventBus.$emit(['account', acctCd].join('_'), filter.length)
      }).finally(() => {
        delete account_lock[acctCd]
      })
    }
  })
}

export default {
  props: ['value', 'status'],
  components: {
    DhxGrid,
    Cctr,
    Account,
    ErpAccount,
    Product
  },
  data() {
    return {
      id: 'RO',
      data: [],
      datad: [],
      options: {
        'OIL_KIND_CD': [],
        'TPS_TYPE_CD': []
      },
      objectPopup:[],
      objectPopupSub:[],
      attachRoles:[],
      config_def: {
        columns: [{
          id: 'itemSeq',
          align: 'center',
          value: 'No.',
          width: 35
        }, {
          id: 'dcNm',
          align: 'center',
          value: '유형',
          width: 40
        }, {
          id: 'taxCd',
          align: 'center',
          value: '세금코드',
          width: 60
        }, {
          id: 'cctrCd',
          align: 'center',
          value: '비용부서',
          width: 100
        }, {
          id: 'cctrNm',
          align: 'left',
          value: '비용부서명',
          width: 120
        }, {
          id: 'acctCd',
          align: 'center',
          value: '계정코드',
          width: 80
        }, {
          id: 'acctNm',
          align: 'left',
          value: '계정명',
          width: 140
        }
        , {
          id: 'subAcctCd',
          align: 'center',
          value: '보조계정코드',
          width: 80
        }, {
          id: 'subAcctNm',
          align: 'left',
          value: '보조계정명',
          width: 140
        }, {
          id: 'productNm',
          align: '',
          value: '개발 프로젝트',
          width: 140
        }, {
          id: 'debitAmt',
          align: 'right',
          value: '차변금액',
          width: 90,
          type: 'ron'
        }, {
          id: 'creditAmt',
          align: 'right',
          value: '대변금액',
          width: 90,
          type: 'ron'
        }, {
          id: 'lnSgtxt',
          align: 'left',
          value: '적요',
          width: 150
        }, {
          id: 'attribute10',
          align: 'center',
          value: '선납부가세<br>증빙유형',
          width: 50
        }],
        height: 300
      },
      config_E6: {
        columns: [{
          id: 'itemSeq',
          value: 'No.',
          width: 35
        }, {
          id: 'useDt',
          value: '사용일자',
          width: 40,
          component: {
            props: ['index', 'value', 'field'],
            template: `
              <span>{{ $moment(value.useDt).format('YYYY-MM-DD') }}</span>
            `
          }
        }, {
          id: 'stptPlc',
          width: 60,
          value: '출발지',
          align: 'left'
        }, {
          id: 'dstnPlc',
          width: 60,
          value: '도착지',
          align: 'left'
        }, {
          id: 'biztrpObj',
          width: 100,
          value: '출장목적',
          align: 'left'
        }, {
          id: 'tpsTypeNm',
          width: 100,
          value: '교통비유형'
        }, {
          id: 'tpsDst',
          value: '거리',
          width: 80,
          align: 'right',
          type: 'ron'
        }, {
          id: 'oilKindCd',
          value: '유종',
          width: 100,
          component: {
            template: `<span>{{ text }}</span>`,
            props: ['index', 'value', 'field'],
            data() {
              return {
                text: undefined
              }
            },
            methods: {
              setText() {
                let vm = this.$parent.$parent
                if (vm.options['OIL_KIND_CD'].length > 0) {
                  let result = vm.options['OIL_KIND_CD'].filter(x => x.detailCd === this.value[this.field])[0]
                  if (result !== undefined) {
                    this.text = result.detailNm
                  }
                }
              }
            },
            created() {
              this.setText()
              this.$parent.$parent.$on('options.OIL_KIND_CD.updated', () => {
                this.setText()
              })
            }
          }
        }, {
          id: 'oilUpc',
          value: '유류단가/연비',
          width: 80,
          component: {
            props: ['index', 'value', 'field'],
            template: `<span v-if="value.oilUpc && value.oilEff">{{ this.$numeral(value.oilUpc).format('0,0') }} / {{ this.$numeral(value.oilEff).format('0,0') }}</span>`
          }
        }, {
          id: 'acctAmt',
          value: '사용금액',
          width: 90,
          align: 'right',
          type: 'ron'
        }, {
          id: 'acctCd',
          align: 'center',
          value: '계정코드',
          width: 50
        }, {
          id: 'acctNm',
          align: 'left',
          value: '계정명',
          width: 80
        }],
        height: 220
      },
      // config_E7: {
      config_E1: {
        columns: [
            { id: 'itemSeq', value: 'No.', width: 35}
          , { id: 'postDt', value: '회계일자', width: 100, align: 'left', type: 'ro'}
          , { id: 'deptCd', align: 'center', value: '부서', hidden: true}
          , { id: 'deptNm', align: 'left', value: '부서', width: 120, type: 'ron'}
          , { id: 'expenseAcctCode', value: '', type: 'ron', hidden: true}
          , { id: 'expenseAcctName', align: 'left', value: '계정명', width: 120, type: 'ron'}
          , { id: 'expenseAcctNameSub', align: 'left', value: '보조계정명', width: 180, type: 'ron'}
          , { id: 'supAmt', value: '공급가액', width: 100, align: 'right', type: 'ron'}
          , { id: 'vatAmt', value: '세액', width: 100, align: 'right', type: 'ron'}
          , { id: 'useAmt', value: '금액', width: 100, align: 'right', type: 'ron'}
          , { id: 'crdMerchNm', value: '가맹점명', width: 150, align: 'left', type: 'ron'}
          , { id: 'lnSgtxt', value: '적요', width: 150, align: 'left', type: 'ron'}
          , { id: 'detailNm', align: 'left', value: '개발 프로젝트', width: 120, type: 'ron'}
          , { id: 'vatYn', width: 90, value: '부가세포함', type: 'ron', component: GridSelect, hide: true,
            mixin: {
              data() {
                return {
                  options: [{
                    detailCd: 'N',
                    detailNm: '선택안함'
                  }, {
                    detailCd: 'Y',
                    detailNm: '선택'
                  }]
                }
              },
              created() {
                this.disable = true
                this.update()
                eventBus.$on('OPTIONS.USE_SELECT_CD', () => {
                  this.update()
                })
              },
              methods: {
                update() {
                  this.options = options['USE_SELECT_CD']

                  if (Array.isArray(this.options) && this.options.length > 0) {
                    let found = this.options.filter(x => x[this.detailCd] === this.data)[0]
                    if (found === undefined) {
                      this.data = this.options[0][this.detailCd]
                      this.value[this.field] = this.data
                      this.$emit('input', this.value)
                    }
                  }

                  return function() {}
                }
              },
              watch: {
                value: {
                  immediate: true,
                  deep: true,
                  handler() {

                  }
                }
              }

            }
          }
          , { id: 'vatNm', width: 120, value: '세금코드'}
          , { id: 'crdPssrNm', width: 120, value: '카드소유주'}
          , { id: 'crdInfo', value: '카드정보', width: 100,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon far fa-file" @click="crdInfoPop"></button>`,
              methods: {
                crdInfoPop(r) {
                  if (this.value.apprNo) {
                    this.open(this.value.crdNo, this.value.apprNo)
                  }
                },
                open(crdNo, apprNo) {
                  this.$modal.open({
                    component: CardInfoDetailPop,
                    parent: this,
                    props: {
                      apprNo: apprNo,
                      crdNo: crdNo
                    },
                    width: 700
                  })
                }
              }
            }
          }
          , { id: 'scanCt', value: '스캔증빙', width: 100,
            component: {
              props: ['index', 'value', 'field'],
              template: `<span style="color: #0065b3;" @click="openUploadEvidencePopup()">{{ this.$numeral(value.scanCt).format('0') }} <i class="far fa-file-alt"></i></span>`,
              created() {
                this.scanFileCount();
              },
              watch: {
                value: {
                  immediate: true,
                  deep: true,
                  handler() {
                    this.scanFileCount();
                  }
                }
              },
              methods: {
                scanFileCount() {
                  const val = this.$parent.value[this.index]
                  this.$http.get(`/api/evid/fileList/${this.value.eaSlipNo}`)
                      .then(response => {
                        val.scanCt = ((response.data || {}).aFiles || []).length
                      })
                },
                openUploadEvidencePopup() {
                  let vm = this
                  let rdoCtrl = vm.$parent.$parent.status.readonly
                  /*
                    - 작성자 후첨 -
                    결재요청, 결재진행, 결재완료 전표 중 작성자와 접속자가 동일한 경우, 후첨 가능하도록 변경
                    단, 기 첨부된 증빙은 삭제할 수 없음
                  */
                  if(vm.$parent.$parent.status.memento[0].slipStatCd === '20'
                      || vm.$parent.$parent.status.memento[0].slipStatCd === '30'
                      || vm.$parent.$parent.status.memento[0].slipStatCd === '50') {
                    if(vm.$parent.$parent.status.memento[0].wrtId === vm.$parent.$parent.$store.state.loginInfo.loginId) {
                      rdoCtrl = false
                    }
                  }

                  /*
                    - 재경팀 후첨 -
                    재경확정 전표는 재경팀에서 후첨 가능하도록 변경
                    단, 기 첨부된 증빙은 삭제할 수 없음
                  */
                  if(vm.$parent.$parent.status.memento[0].slipStatCd === '20'
                      || vm.$parent.$parent.status.memento[0].slipStatCd === '30'
                      || vm.$parent.$parent.status.memento[0].slipStatCd === '50') {
                    let authorityRoleChecked = vm.$parent.$parent.attachRoles
                        .filter(x=>x.detailCd === vm.$parent.$parent.$store.state.loginInfo.authorities[0].roleCd);
                    if(authorityRoleChecked.length > 0){
                      rdoCtrl = false
                    }
                    // if(vm.$parent.$parent.$store.state.loginInfo.authorities[0].roleCd === 'ADMIN' ||
                    //     vm.$parent.$parent.$store.state.loginInfo.authorities[0].roleCd === 'P_USER'||
                    //     vm.$parent.$parent.$store.state.loginInfo.authorities[0].roleCd === 'F_USER') {
                    //   rdoCtrl = false
                    // }
                  }

                  const val = this.$parent.value[this.index]

                  let url = '/evidAtchPopModeless?docMngNo=' + vm.value.eaSlipNo + '&readonly='+ vm.$parent.$parent.status.readonly + '&readonlyCtrl='+ rdoCtrl;

                  if(!(!vm.$parent.$parent.objectPopup[this.index] || vm.$parent.$parent.objectPopup[this.index].closed)){
                    vm.$parent.$parent.objectPopup[this.index].focus();
                  }else{
                    vm.$parent.$parent.objectPopup[this.index] = window.open(url, '_blank', 'toolbar=0,location=0,menubar=0,resizable=yes');
                  }
                  //팝업 Close Callback
                  //브라우저 체크(IE 체크)
                  var agent = navigator.userAgent.toLowerCase();

                  if ((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1)){
                    vm.$parent.$parent.objectPopup[this.index].attachEvent("onunload",function(){
                      val.scanCt = localStorage.getItem("popFileCnt")
                      vm.$forceUpdate();
                    });
                  }else{
                    vm.$parent.$parent.objectPopup[this.index].onbeforeunload = function(){
                      val.scanCt = localStorage.getItem("popFileCnt")
                      vm.$forceUpdate();
                    };
                  }

                  // this.$modal.open({
                  //     component: EvidAtchPop,
                  //     props: {
                  //         docMngNo: vm.value.eaSlipNo,
                  //         readonly: vm.$parent.$parent.status.readonly,
                  //         readonlyCtrl: rdoCtrl
                  //     },
                  //     parent: this,
                  //     width: 1200,
                  //     events: {
                  //         close(value) {
                  //           val.scanCt = value.length
                  //           //스캔증빙 카운팅 벨류 강제 업데이트
                  //           vm.$forceUpdate();
                  //         }
                  //     }
                  // })
                }
              },
            }
          },
        ],
        height: 260,
        afterRefreshData(grid, data) {
          this.$nextTick(() => {
            _.forEach(data, (item, index) => {
              let rId = index + 1
              if (grid.cells(rId, 1).getValue()) grid.cells(rId, 1).setValue(this.$moment(grid.cells(rId, 1).getValue()).format('YYYY-MM-DD'));
            })
          })
          grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
          grid.setColumnHidden(2,true)
          grid.setColumnHidden(4,true)
          grid.setColumnHidden(25,true)
          grid.setNumberFormat('0,000', 7, '.', ',')
          grid.setNumberFormat('0,000', 8, '.', ',')
          grid.setNumberFormat('0,000', 9, '.', ',')
          grid.setNumberFormat('0,000', 21, '.', ',')
          grid.setNumberFormat('0,000', 24, '.', ',')
        }
      },
      config_E1_2: {

        columns: [
            { id: 'itemSeq', align: 'center', value: 'No.', type: 'cntr', width: 35}
          , { id: 'eaSlipDt', value: '회계일자', width: 100, align: 'left', type: 'ro'}
          , { id: 'deptCd', align: 'center', type: 'ron', value: '부서', hidden: true}
          , { id: 'deptNm', align: 'left', value: '부서', type: 'ron', width: 100,}
          , { id: 'expenseAcctName2', align: 'left', value: '계정명', width: 120, type: 'ron'}
          , { id: 'expenseAcctNameSub2', align: 'left', value: '보조계정명', width: 180, type: 'ron'}
          , { id: 'useAmt2', value: '금액', width: 120, align: 'right', type: 'ron'}
          , { id: 'curCd', value: '통화', width: 100, align: 'center', type: 'ron'}
          , { id: 'excRt', value: '환율', width: 100, align: 'right', type: 'ron'}
          , { id: 'krwTotAmt', value: '원화금액', width: 130, align: 'right', type: 'ron'}
          , { id: 'evdCustNm', align: 'left', value: '가맹점', type: 'ron', width: 200}
          , { id: 'payCustNm', align: 'left', value: '지급처', type: 'ron', width: 200}
          , { id: 'lnSgtxt', align: 'left', value: '적요', type: 'ron', width: 200}
          , { id: 'detailNm2', align: 'left', value: '개발 프로젝트', width: 120, type: 'ron'}
          , {
            id: 'scanCt',
            value: '스캔증빙',
            width: 100,
            component: {
              props: ['index', 'value', 'field'],
              template: `<span style="color: #0065b3;" @click="openUploadEvidencePopup()">{{ this.$numeral(value.scanCt).format('0') }} <i class="far fa-file-alt"></i></span>`,
              created() {
                this.scanFileCount();
              },
              watch: {
                value: {
                  immediate: true,
                  deep: true,
                  handler() {
                    this.scanFileCount();
                  }
                }
              },
              methods: {
                scanFileCount() {
                  const val = this.$parent.value[this.index]
                  this.$http.get(`/api/evid/fileList/${this.value.eaSlipNo}`)
                      .then(response => {
                        val.scanCt = ((response.data || {}).aFiles || []).length
                      })
                },
                openUploadEvidencePopup() {
                  let vm = this
                  let rdoCtrl = vm.$parent.$parent.status.readonly

                  /*
                    - 작성자 후첨 -
                    결재요청, 결재진행, 결재완료 전표 중 작성자와 접속자가 동일한 경우, 후첨 가능하도록 변경
                    단, 기 첨부된 증빙은 삭제할 수 없음
                  */
                  if(vm.$parent.$parent.status.memento[0].slipStatCd === '20'
                      || vm.$parent.$parent.status.memento[0].slipStatCd === '30'
                      || vm.$parent.$parent.status.memento[0].slipStatCd === '50') {
                    if(vm.$parent.$parent.status.memento[0].wrtId === vm.$parent.$parent.$store.state.loginInfo.loginId) {
                      rdoCtrl = false
                    }
                  }
                  /*
                    - 재경팀 후첨 -
                    재경확정 전표는 재경팀에서 후첨 가능하도록 변경
                    단, 기 첨부된 증빙은 삭제할 수 없음
                  */
                  if(vm.$parent.$parent.status.memento[0].slipStatCd === '20'
                      || vm.$parent.$parent.status.memento[0].slipStatCd === '30'
                      || vm.$parent.$parent.status.memento[0].slipStatCd === '50') {
                    let authorityRoleChecked = vm.$parent.$parent.attachRoles
                        .filter(x=>x.detailCd === vm.$parent.$parent.$store.state.loginInfo.authorities[0].roleCd);
                    if(authorityRoleChecked.length > 0) {
                      rdoCtrl = false
                    }
                    // if(vm.$parent.$parent.$store.state.loginInfo.authorities[0].roleCd === 'ADMIN' ||
                    //     vm.$parent.$parent.$store.state.loginInfo.authorities[0].roleCd === 'P_USER'||
                    //     vm.$parent.$parent.$store.state.loginInfo.authorities[0].roleCd === 'F_USER') {
                    //   rdoCtrl = false
                    // }
                  }

                  const val = this.$parent.value[this.index]
                  let url = '/evidAtchPopModeless?docMngNo=' + vm.value.eaSlipNo + '&readonly='+ vm.$parent.$parent.status.readonly + '&readonlyCtrl='+ rdoCtrl;

                  if(!(!vm.$parent.$parent.objectPopupSub[this.index] || vm.$parent.$parent.objectPopupSub[this.index].closed)){
                    vm.$parent.$parent.objectPopupSub[this.index].focus();
                  }else{
                    vm.$parent.$parent.objectPopupSub[this.index] = window.open(url, '_blank', 'toolbar=0,location=0,menubar=0,resizable=yes');
                  }
                  //팝업 Close Callback
                  //브라우저 체크(IE 체크)
                  var agent = navigator.userAgent.toLowerCase();

                  if ((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1)){
                    vm.$parent.$parent.objectPopupSub[this.index].attachEvent("onunload",function(){
                      val.scanCt = localStorage.getItem("popFileCnt")
                      vm.$forceUpdate();
                    });
                  }else{
                    vm.$parent.$parent.objectPopupSub[this.index].onbeforeunload = function(){
                      val.scanCt = localStorage.getItem("popFileCnt")
                      vm.$forceUpdate();
                    };
                  }
                }
              },
            }
          }
          //, { id: 'address', align: 'left', value: '주소', type: 'ron', width: 200 }
        ],
        //////////////////////////////////
        height: 260,
        afterRefreshData(grid, data) {
          this.$nextTick(() => {
            _.forEach(data, (item, index) => {
              let rId = index + 1
              if (grid.cells(rId, 1).getValue()) grid.cells(rId, 1).setValue(this.$moment(grid.cells(rId, 1).getValue()).format('YYYY-MM-DD'));
            })
          })
        }
        
      },
      config_E2: {
          columns: [{
            id: 'itemSeq',
            value: 'No.',
            width: 35
          },
          {
            id: 'deptCd',
            align: 'center',
            value: '비용부서코드',
            width: 80,
            hide: true
          }, {
            id: 'deptNm',
            align: 'left',
            value: '부서',
            width: 80,
            type: 'ron'
          }, 
          {
            id: 'acctCd',
            align: 'center',
            value: '계정코드',
            width: 80,
            hide: true
          }, {
            id: 'acctNm',
            align: 'left',
            value: '계정명',
            width: 140,
            type: 'ron'
          },  
          {
            id: 'acctCdSub',
            align: 'center',
            value: '보조계정코드',
            width: 80,
            hide: true
          }, {
            id: 'acctNmSub',
            align: 'left',
            value: '보조계정명',
            width: 140,
            type: 'ron'
          },
          {
            id: 'detailCd',
            align: 'left',
            value: '',
            type: 'ron',
            hide: true
          },{
            id: 'detailNm',
            align: 'center',
            value: '개발 프로젝트',
            width: 140,
            type: 'ro'
          }, 
          {
            id: 'vatYn',
            width: 65,
            value: '부가세포함',
            type: 'ron',
            component: GridSelect,
            hide: true,
            mixin: {
              data() {
                return {
                  options: [{
                    detailCd: 'N',
                    detailNm: '선택안함'
                  }, {
                    detailCd: 'Y',
                    detailNm: '선택'
                  }]
                }
              },
              created() {
                this.disable = true
                this.update()
                eventBus.$on('OPTIONS.USE_SELECT_CD', () => {
                  this.update()
                })
              },
              methods: {
                update() {
                  this.options = options['USE_SELECT_CD']
                  
                  if (Array.isArray(this.options) && this.options.length > 0) {
                    let found = this.options.filter(x => x[this.detailCd] === this.data)[0]
                    if (found === undefined) {
                      this.data = this.options[0][this.detailCd]
                      this.value[this.field] = this.data
                      this.$emit('input', this.value)
                    }
                  }

                  return function() {}
                }
              },
              watch: {
                value: {
                  immediate: true,
                  deep: true,
                  handler() {

                  }
                }
              }
              
            }
          }, {
            id: 'useAmt',
            value: '금액',
            width: 80,
            type: 'ron',
            align: 'right',
          }, {
            id: 'taxCd',
            width: 80,
            type: 'ro',
            hide: true
          }, {
            id: 'taxNm',
            value: '세금코드',
            width: 80,
            type: 'ro',
            align: 'right',
            hide: true
          }, {
            id: 'lnSgtxt',
            value: '적요',
            width: 400,
            align: 'left',
            type: 'ron'
          }, 
        ],
      },
      config_E5: {
        columns: [{
          id: 'itemSeq',
          align: 'center',
          value: 'No.',
          width: 35
        }, {
          id: 'dcNm',
          align: 'center',
          value: '유형',
          width: 40
        }, {
          id: 'taxCd',
          align: 'center',
          value: '세금코드',
          width: 60
        }, {
          id: 'cctrCd',
          align: 'center',
          value: '비용부서',
          width: 100
        }, {
          id: 'cctrNm',
          align: 'left',
          value: '비용부서명',
          width: 120
        }, {
          id: 'acctCd',
          align: 'center',
          value: '계정코드',
          width: 80
        }, {
          id: 'acctNm',
          align: 'left',
          value: '계정명',
          width: 140
        }
        , {
          id: 'subAcctCd',
          align: 'center',
          value: '보조계정코드',
          width: 80
        }, {
          id: 'subAcctNm',
          align: 'left',
          value: '보조계정명',
          width: 140
        }, {
          id: 'productNm',
          align: 'left',
          value: '개발 프로젝트',
          width: 140
        }, {
          id: 'debitAmt',
          align: 'right',
          value: '차변금액',
          width: 90,
          type: 'ron'
        }, {
          id: 'creditAmt',
          align: 'right',
          value: '대변금액',
          width: 90,
          type: 'ron'
        }, {
          id: 'lnSgtxt',
          align: 'left',
          value: '적요',
          width: 150
        }],
        height: 300
      },      
    
    }
  },
  computed: {
    config() {
      switch (this.value.slipTypeCd) {
        case 'E6':
          return this.config_E6
        case 'E1':
          return this.config_E1
        case 'E2':
          return this.config_E2
        case 'E5':
          return this.config_E5
        default:
          return this.config_def
      }
    }
  },
  created() {
    this.$watch(() => this.value.slipDetails, (nValue, oValue) => {
      if (nValue === undefined) {
        this.data = []
      } else if (nValue !== undefined && Array.isArray(nValue)) {
        this.data = this.value.slipDetails
      }
    }, {
      immediate: true,
      deep: true
    })

    this.$watch(() => this.value.slipDetails2, (nValue, oValue) => {
      if (nValue === undefined) {
        this.datad = []
      } else if (nValue !== undefined && Array.isArray(nValue)) {
        this.datad = this.value.slipDetails2
      }
    }, {
      immediate: true,
      deep: true
    })

    this.setAttachRoles()

    this.$http.get('/api/code/detail', {
      params: {
        groupCd: 'TPS_TYPE_CD'
      }
    }).then(response => {
      this.options['TPS_TYPE_CD'] = response.data
      this.$emit('options.TPS_TYPE_CD.updated', response.data)
    })

    this.$http.get('/api/code/detail', {
      params: {
        groupCd: 'OIL_KIND_CD'
      }
    }).then(response => {
      this.options['OIL_KIND_CD'] = response.data
      this.$emit('options.OIL_KIND_CD.updated', response.data)
    })
  },
  methods: {
    constructGridSuccessful(grid) {
      var header_style = 'color:#222;text-align:center;'
      var stat_total_text = 'color:red;text-align:right;padding-right:10px !important;'
      if (this.value.slipTypeCd === 'E6') {
        grid.attachHeader(
          ['합계', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#stat_total', '', ''],
          [header_style, '', '', '', '', '', '', '', '', stat_total_text, '', '']
        )
        grid.setNumberFormat('0,000', 6, '.', ',')
        grid.setNumberFormat('0,000', 9, '.', ',')
      
      } else if(this.value.slipTypeCd === 'E1') {
        
        grid.attachHeader([],[])

        grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
        grid.setColumnHidden(2,true)
        grid.setColumnHidden(4,true)
        grid.setColumnHidden(25,true)
        grid.setNumberFormat('0,000', 7, '.', ',')
        grid.setNumberFormat('0,000', 8, '.', ',')
        grid.setNumberFormat('0,000', 9, '.', ',')
        grid.setNumberFormat('0,000', 21, '.', ',')
        grid.setNumberFormat('0,000', 24, '.', ',')
      } else if(this.value.slipTypeCd === 'E2') {
        grid.attachHeader([],[])
        grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
        
		    //2020.07.16 E2(read) 통화에 따른 소수점 표시
        if(this.value.slipTypeCd === 'E2') {
          // grid.setColumnHidden(10,true)
          if(this.$parent.value.curCd === 'KRW'){
            grid.setNumberFormat('0,000', 10, '.', ',')
          }else{
            grid.setNumberFormat('0,000.000', 10, '.', ',')
          }
        }else{
          grid.setNumberFormat('0,000.000', 10, '.', ',')
        }
      } else if(this.value.slipTypeCd === 'E5') {
        
        grid.attachHeader(
          ['합계', '#cspan', '#cspan', '#cspan',  '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#stat_total', '#stat_total', ''],
          [header_style, '', '', '', '', '', '', '', '', '',  stat_total_text, stat_total_text, '']
        )
        grid.setNumberFormat('0,000', 10, '.', ',')
        grid.setNumberFormat('0,000', 11, '.', ',')        
      
      }else {
        grid.attachHeader(
          ['합계', '#cspan', '#cspan', '#cspan',  '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#stat_total', '#stat_total', '',''],
          [header_style, '', '', '', '', '', '', '', '', '',  stat_total_text, stat_total_text, '']
        )
        grid.setNumberFormat('0,000', 10, '.', ',')
        grid.setNumberFormat('0,000', 11, '.', ',')

        this.data.forEach(x => {
          if(x.dcCd === 'D' && x.lnTypeCd === 'TAX') {
            if(x.attribute10 === '0') {
              x.attribute10 = '기타'
            } else if(x.attribute10 === '1') {
              x.attribute10 = '종이'
            } else if(x.attribute10 === '2') {
              x.attribute10 = '전자'
            } else {
              if(this.value.slipTypeCd === 'E4') {
                x.attribute10 = '종이'
              }else {
                x.attribute10 = '전자'
              }
            }
          }
        })
        
      }
      grid.callEvent('ongridreconstructed', [])
    },
    constructGridSuccessful2(grid) {
      grid.attachHeader([])
      grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
      grid.setNumberFormat('0,000', 6, '.', ',')
      grid.setNumberFormat('0,000.00', 8, '.', ',')
      grid.setNumberFormat('0,000', 9, '.', ',')
      grid.setColumnHidden(2, true)
    },
    setAttachRoles(){
      this.$store.commit('loading')

      this.$http.get('/api/code/detail', {params: {groupCd: "ATTACH_ROLE"}})
          .then(response => {
            this.attachRoles = response.data;

          }).catch(response => {
        console.error("getAttchRole Error " + response.message);

      }).finally(() => {
        this.$store.commit('finish')
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.slip-grid {
  :global(.xhdr table tbody tr:last-child) {
    background-color: #f9f9f3;
  }

  :global(.xhdr table tbody tr:last-child td) {
    background-color: transparent !important;
  }

  :global(.xhdr table tbody tr:last-child td div) {
    background-color: transparent !important;
    color: inherit !important;
    text-align: inherit !important;
  }
}
</style>
