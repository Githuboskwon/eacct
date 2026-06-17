<template>
  <div class="inner-box">
    <div class="table-b">
      <div class="table-header">
        <div class="table-name">
          <h3 v-if="slipType === 'E1'" class="ico_table_name">법인카드지급</h3>
          <h3 v-else class="ico_table_name">세부항목</h3>
        </div>
        <div class="btn-wrap btn-type2">
          <button v-if="slipType === 'E1'" class="btn-size btn-w-gray" @click="buttenClickEventCrdList()">
          <span class="btn-icon">
            <i class="far fa-file-alt"></i>
          </span>
            법인카드내역
          </button>
          <button v-if="slipType === 'E2' || slipType === 'E5'" class="btn-size btn-w-gray" @click="buttenClickEventCopy()">
          <span class="btn-icon">
            <i class="far fa-file-alt"></i>
          </span>
            행복사
          </button>
          <button v-else class="btn-size btn-w-gray" @click="buttenClickEventReset()">
          <span class="btn-icon">
            <i class="fas fa-redo"></i>
          </span>
            초기화
          </button>
          <button v-if="slipType !== 'E1'" class="btn-size btn-w-gray" @click="buttonClickEventAdd()">
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
          <button class="btn-size btn-excel"  @click="saveExcel">엑셀 저장</button>
        </div>
        <!-- DhxGrid component -->
        <dhx-grid ref="grid" class="slip-grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" />
        <!-- //DhxGrid component -->
      </div>
    </div>

    <div class="table-b">
      <div class="table-header" v-if="slipType === 'E1'">
        <div class="table-name">
          <h3 class="ico_table_name">현금지급</h3>
        </div>
        <div class="btn-wrap btn-type2">
          <button class="btn-size btn-w-gray" @click="buttenClickEventCopy2()">
          <span class="btn-icon">
            <i class="far fa-window-restore"></i>
          </span>
            행복사
          </button>
          <button class="btn-size btn-w-gray" @click="buttonClickEventAdd2()">
          <span class="btn-icon">
            <i class="fas fa-plus"></i>
          </span>
            행추가
          </button>
          <button class="btn-size btn-w-gray" @click="buttonClickEventRemove2()">
          <span class="btn-icon">
            <i class="fas fa-trash-alt"></i>
          </span>
            행삭제
          </button>
        </div>
        <!-- DhxGrid component -->
        <dhx-grid ref="grid2" class="slip-grid" v-if="slipType === 'E1'" v-model="datad" :config="config_E1_2" @constructGridSuccessful="constructGridSuccessful2" />
        <!-- //DhxGrid component -->
      </div>
    </div>
  </div>
</template>

<script>
// For global event bus
import Vue from 'vue';
import createBus from '@/libs/eventBus';
import _ from 'lodash';
import Join from '@/libs/join';
// Import DhxGrid
import DhxGrid from '@/components/DhxGrid.vue';
// import CctrE1 from '@/components/AccountCtrl.vue'
import Cctr from '@/components/Cctr_Ag.vue';
import ErpCctr from '@/components/ErpCctr_Ag.vue';
import Account from '@/components/Account_Ag.vue';
import AccountSub from '@/components/ErpAccountSub_Ag.vue';
import ErpAccount from '@/components/ErpAccount.vue';
import IO from '@/components/IO_new.vue';
import SlipMngItemPop from '@/components/SlipMngItemPop.vue';
import mixin from '@/mixin/slip-common';

import DhxCalendar from '@/components/DhxCalendar.vue';
import NumberInput from '@/components/NumberInput.vue';
import GridInput from '@/components/grid/GridInput.vue';
import GridNumberInput from '@/components/grid/GridNumberInput.vue';
import GridSelect from '@/components/grid/GridSelect.vue';
import Emp from '@/components/Emp.vue';
import Product from '@/components/Product_Ag.vue';
import CardInfoDetailPop from '@/components/CardInfoDetailPop.vue';

import SlipCrdLstModal from '@/components/SlipCrdLstModal.vue';
import EvidAtchPop from '@/components/EvidAtchPop.vue';

import mixin2 from '@/mixin';
import mixinSlip from '@/mixin/slip';

const eventBus = createBus();
const _url = Join.url;

Vue.config.errorHandler = (err, vm, info) => {
  // err: error trace
  // vm: component in which error occured
  // info: Vue specific error information such as lifecycle hooks, events etc.
  try {
    if(vm === undefined){
      console.log('vue config errorHandler ', err, vm, info)
      return false
    }
  }catch(e) {
    console.warn('errorHandler message ',e)
  }

};
// For grid events handling
const modalStatus = {
  Cctr: false,
  Account: false,
  Product: false,
  ErpCctr: false
}

const options = {
  'USE_SELECT_CD': [{"detailCd": "Y", "detailNm": "선택함"}, {"detailCd": "N", "detailNm": "선택안함"}]
}

var queryTpsTypeCd_run = false;
var queryOilKinCd_run = false;
var queryVatAcct_run = false;
var queryTaxes_run = false;
var queryOilPrice_run = {};

function queryTpsTypeCd() {
  if ((options['E6_tpsTypeCd'] === undefined || options['E1_tpsTypeCd'] === undefined) && !queryTpsTypeCd_run) {
    queryTpsTypeCd_run = true
    this.$http.get('/api/code/detail', {
      params: {
        groupCd: 'TPS_TYPE_CD'
      }
    }).then(response => {
      options['E6_tpsTypeCd'] = response.data;
      options['E1_tpsTypeCd'] = response.data;
    }).catch(response => {
      options['E6_tpsTypeCd'] = undefined;
      options['E1_tpsTypeCd'] = undefined;
      return response
    }).finally(() => {
      eventBus.emit('E6_tpsTypeCd.updated');
      eventBus.emit('E1_tpsTypeCd.updated');
      queryTpsTypeCd_run = false;
    })
  }
}

function queryVatAcct(){
  if(options['E1_vatAcctCd'] === undefined && !queryVatAcct_run){
    queryVatAcct_run = true;

    this.$http.get('/api/code/detail', {
      params: {
        groupCd: 'CRD_DDCT_CD'//'VAT_TYPE_CD'
      }
    }).then(response => {
      options['E1_vatAcctCd'] = response.data;
    }).catch(response => {
      options['E1_vatAcctCd'] = undefined;
      return response;
    }).finally(() => {
      eventBus.emit('E1_vatAcctCd.updated');
      queryVatAcct_run = false;
    })
  }
}

/**
 * 법인카드 부가세 코드 조회
 */
function queryTaxes() {
  if(options['E1_taxesCd'] === undefined && !queryTaxes_run){
    queryTaxes_run = true;

    this.$http.get('/api/taxes', {
      params: {
        evdTypeCd: '10'
      }
    }).then(response => {
      options['E1_taxesCd'] = response.data;
    }).catch(response => {
      options['E1_taxesCd'] = undefined;
      return response
    }).finally(() => {
      eventBus.emit('E1_taxesCd.updated');
      queryTaxes_run = false;
    })
  }

}

function queryOilKinCd() {
  if ((options['E6_oilKindCd'] === undefined || options['E1_oilKindCd'] === undefined) && !queryOilKinCd_run) {
    queryOilKinCd_run = true;
    this.$http.get('/api/code/detail', {
      params: {
        groupCd: 'OIL_KIND_CD'
      }
    }).then(response => {
      options['E6_oilKindCd'] = response.data;
      options['E1_oilKindCd'] = response.data;
    }).catch(response => {
      options['E6_oilKindCd'] = undefined;
      options['E1_oilKindCd'] = undefined;
      return response
    }).finally(() => {
      eventBus.emit('E6_oilKindCd.updated');
      eventBus.emit('E1_oilKindCd.updated');
      queryOilKinCd_run = false;
    })
  }
}

function queryOilPrice(baseYm) {
  options['E6_oilPrice'] = options['E6_oilPrice'] || {}
  options['E1_oilPrice'] = options['E1_oilPrice'] || {}
  if ((options['E6_oilPrice'][baseYm] === undefined || options['E1_oilPrice'][baseYm] === undefined) && !queryOilPrice_run[baseYm]) {
    queryOilPrice_run[baseYm] = true;
    this.$http.post('/api/oilPrice/list', {
      baseYm: baseYm
    }).then(response => {
      options['E6_oilPrice'][baseYm] = response.data;
      options['E1_oilPrice'][baseYm] = response.data;
      return response;
    }).catch(response => {
      options['E6_oilPrice'][baseYm] = undefined;
      options['E1_oilPrice'][baseYm] = undefined;
      return response;
    }).finally(() => {
      eventBus.emit('E6_oilPrice.updated', baseYm);
      eventBus.emit('E1_oilPrice.updated', baseYm);
      queryOilPrice_run[baseYm] = undefined;
    })
  }
}

/**
 * 계정 관련 추가 정보
 */
const account = {};
const account_lock = {};

function queryAccountAddon(acctCd) {
  return new Promise((resolve, reject) => {
    if (!acctCd || account[acctCd] !== undefined || account_lock[acctCd]) {
      resolve(0);
    } else {
      account_lock[acctCd] = true
      this.$http.post('/api/slip/managementItem', {
        acctCd: acctCd,
        useYn: 'Y'
      }).then(response => {
        let filter = response.data.filter(x => x.acctCd === acctCd);
        account[acctCd] = filter.length;
        eventBus.emit(['account', acctCd].join('_'), filter.length);
      }).finally(() => {
        delete account_lock[acctCd];
      })
    }
  })
}

const $bus = createBus()
const budget = {};
const bdg_mutex = {};

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

/*
const budget = {};
const budget_mutex = {};

function queryRemainBudget(postDt, cctrCd, acctCd) {
  return new Promise((resolve, reject) => {
    let id = [postDt, cctrCd, acctCd].join('_');
    let valid = postDt && cctrCd && acctCd;
    if (valid && !budget_mutex[id] && budget[id] === undefined) {
      budget_mutex[id] = true;
      this.$http.get('/api/budget/remain', {
        params: {
          postDt,
          cctrCd,
          acctCd
        }
      }).then(response => {
        budget[id] = this.$numeral(response.data).value();
        eventBus.emit('budget', budget[id]);
        return resolve(budget[id]);
      }).catch(response => {
        return reject(budget[id]);
      }).finally(() => {
        delete budget_mutex[id];
      })
    } else {
      resolve(budget[id]);
    }
  })
}
*/

export default {
  props: ['slipType', 'value', 'status', 'lnk'],
  mixins: [mixin, mixin2, mixinSlip],
  components: {
    DhxGrid,
    Cctr,
    Account,
    DhxCalendar,
    Emp,
    Product
  },
  computed: {
    config() {
      switch (this.slipType) {
        case 'E6':
          return this.config_E6
        case 'E1':
          return this.config_E1
        case 'E2':
        case 'E5':
          return this.config_E2
        default:
          return this.config_def
      }
    }
  },
  data() {
    return {
      id: 'ED',
      data: [],
      datad: [],
      evidFileSize: 0,
      vatAmt: [],
      supAmt: [],
      data1:'', //2020.05.14 vatCd 데이터 전역변수로 선언
      baseDeptGrCd:'',
      //sgaProductCd: '9999999',
      //sgaProductCd: this.$store.getters.CONSTANTS.DefaultProductCd,
      isExpenseFlag : false,
      rcpYn: 'N',
      config_def: {
        columns: [{
          id: 'itemSeq',
          align: 'center',
          value: 'No.',
          type: 'cntr',
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
        },
          {
            id: 'cctrCd',
            align: 'center',
            value: '비용부서코드',
            width: 100,
            hidden: true
          }, {
            id: 'cctrNm',
            align: 'left',
            value: '부서',
            width: 120,
            type: 'ron'
          }, {
            id: 'cctrSrch',
            align: 'center',
            value: '#cspan',
            width: 40,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateCctr()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateCctr() {
                  const value = this.$parent.value[this.index]
                  const vm = this;
                  this.$modal.open({
                    component: ErpCctr,
                    parent: this,
                    width: 700,
                    events: {
                      close(object) {
                        value.cctrCd = object.deptCd
                        value.cctrNm = object.deptNm
                      }
                    }
                  })
                }
              }
            }
          },
          {
            id: 'acctCd',
            align: 'center',
            value: '계정코드',
            width: 80
          }, {
            id: 'acctNm',
            align: 'left',
            value: '계정명',
            width: 140,
            type: 'ed'
          }, {
            id: 'acctSrch',
            align: 'center',
            value: '#cspan',
            width: 40,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateAccount()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateAccount() {
                  const vm = this
                  const value = this.$parent.value[this.index]
                  this.$modal.open({
                    component: Account,
                    parent: this,
                    props: {
                      deptCd: value.cctrCd,
                      slipTypeCd: vm.$parent.$parent.value.slipTypeCd
                    },
                    width: 700,
                    events: {
                      close(object) {
                        vm.value.acctCd = object.acctCd
                        vm.value.acctNm = object.acctNm
                        vm.value.ctrlYn = object.ctrlYn
                      }
                    }
                  })
                }
              }
            }
          },
          {
            id: 'productCd',
            align: 'left',
            value: '',
            type: 'ron',
            hidden: true
          },{
            id: 'productNm',
            align: 'left',
            value: '제품',
            width: 80,
            type: 'ed'
          }, {
            id: 'prodSrch',
            align: 'center',
            value: '#cspan',
            width: 20,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateProd()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateProd() {
                  const value = this.$parent.value[this.index]
                  const self = this.$parent
                  this.$modal.open({
                    component: Product,
                    parent: this,
                    props: {
                      slipTypeCd: self.$parent.value.slipTypeCd
                    },
                    width: 700,
                    events: {
                      close(object) {
                        value.productCd = object.detailCd
                        value.productNm = object.detailNm
                        modalStatus.Product = false;
                      }
                    }
                  })
                }
              }
            }
          },
          {
            id: 'acctAddon',
            value: '#cspan',
            width: 40,
            component: {
              props: ['index', 'value', 'field'],
              template: `
              <button type="button" class="icon" v-if="visible" @click="openAddon()"><i class="fas fa-search"></i></button>
            `,
              data() {
                return {
                  visible: false
                }
              },
              methods: {
                openAddon() {
                  let vm = this
                  this.$modal.open({
                    component: SlipMngItemPop,
                    props: {
                      acctCd: this.value.acctCd,
                      data: this.value
                    },
                    parent: this,
                    events: {
                      close(data) {
                        for (var i = 1; i <= 10; i++) {
                          let id = ['ref', i].join('')
                          vm.value[id] = data[id]
                        }
                      }
                    }
                  })
                }
              },
              created() {
                let length = account[this.value.acctCd]
                if (length === undefined) {
                  queryAccountAddon.apply(this, [this.value.acctCd])
                } else {
                  this.visible = length > 0
                  this.value.hasAddon = this.visible
                }

                eventBus.on(['account', this.value.acctCd].join('_'), () => {
                  this.visible = account[this.value.acctCd] > 0
                  this.value.hasAddon = this.visible
                })
              },
              watch: {
                value: {
                  immediate: true,
                  deep: true,
                  handler() {}
                }
              }
            }
          },
          {
            id: 'debitAmt',
            value: '차변금액',
            width: 90,
            type: 'edn',
            align: 'right'
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
            type: 'ed',
            width: 480
          }],
        height: 260,
        events: {
          onEditCell(stage, rId, cInd, nValue, oValue) {
            let row = this.data[rId - 1]
            let colDef = this.options.columns[cInd]
            // Edit event handler
            if (stage === 2 && nValue !== oValue) {
              switch (colDef.id) {
                case 'cctrNm': // 비용부서
                  // Delay 200ms
                  setTimeout(() => {
                    if (row.cctrNm) {
                      _findCctr.apply(this, [row.cctrNm])
                    } else {
                      row.cctrNm = String(oValue)
                    }
                  }, 200)
                  break
                case 'acctNm': // 계정
                  // Delay 200ms
                  setTimeout(() => {
                    if (row.acctNm) {
                      _findAccount.apply(this, [row.acctNm])
                    } else {
                      row.acctNm = String(oValue)
                    }
                  }, 200)
                  break
                case 'productNm':
                  // Delay 200ms
                  setTimeout(() => {
                    if (row.productNm) {
                      _findProduct.apply(this, [row.productNm])
                    } else {
                      row.productNm = String(oValue)
                    }
                  }, 200)
                  break
                case 'debitAmt':
                  row.acctAmt = row.debitAmt = this.$numeral(nValue || 0).value()
                  break
              }
            }

            return true

            function _findCctr(searchStr) {
              this.$store.commit('loading')
              this.$http.get(_url('/api/cctr/erp', searchStr))
                  .then(response => {
                    if (Array.isArray(response.data)) {
                      if (response.data.length === 1) {
                        row.cctrCd = response.data[0].deptCd
                        row.cctrNm = response.data[0].deptNm
                      } else {
                        _openCctrModal.apply(this, [searchStr])
                      }
                    } else {
                      _openCctrModal.apply(this, [searchStr])
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              })
            }

            function _openCctrModal(searchStr) {
              if (!modalStatus.ErpCctr) {
                modalStatus.ErpCctr = true
                this.$modal.open({
                  component: ErpCctr,
                  props: {
                    title: '비용부서',
                    searchStr
                  },
                  parent: this,
                  events: {
                    close(data) {
                      row.cctrCd = data.deptCd
                      row.cctrNm = data.deptNm
                      modalStatus.ErpCctr = false
                    },
                    dismiss() {
                      row.cctrNm = String(oValue)
                      modalStatus.ErpCctr = false
                    }
                  }
                })
              }
            }

            function _findAccount(searchStr) {
              this.$store.commit('loading')
              this.$http.get(_url('/api/account', searchStr))
                  .then(response => {
                    if (Array.isArray(response.data)) {
                      if (response.data.length === 1) {
                        row.acctCd = response.data[0].acctCd
                        row.acctNm = response.data[0].acctNm
                        row.ctrlYn = response.data[0].ctrlYn
                      } else {
                        _openAccountModal.apply(this, [searchStr])
                      }
                    } else {
                      _openAccountModal.apply(this, [searchStr])
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              })
            }

            function _openAccountModal(searchStr) {
              if (!modalStatus.Account) {
                modalStatus.Account = true
                this.$modal.open({
                  component: Account,
                  props: {
                    title: '귀속부서',
                    searchStr
                  },
                  parent: this,
                  events: {
                    close(data) {
                      row.acctCd = data.acctCd
                      row.acctNm = data.acctNm
                      row.ctrlYn = data.ctrlYn
                      modalStatus.Account = false
                    },
                    dismiss() {
                      row.acctNm = String(oValue)
                      modalStatus.Account = false
                    }
                  }
                })
              }
            }

            function _findProduct(searchStr) {
              this.$store.commit('loading')
              this.$http.get(`/api/slip/product/${searchStr}`)
                  .then(response => {
                    if (Array.isArray(response.data)) {
                      if (response.data.length === 1) {
                        row.productCd = response.data[0].detailCd
                        row.productNm = response.data[0].detailNm
                      } else {
                        _openProductModal.apply(this, [searchStr])
                      }
                    } else {
                      _openProductModal.apply(this, [searchStr])
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              })
            }

            function _openProductModal(searchStr) {
              if (!modalStatus.Product) {
                modalStatus.Product = true
                this.$modal.open({
                  component: Product,
                  props: {
                    title: '제품',
                    searchStr
                  },
                  parent: this,
                  events: {
                    close(data) {
                      row.productCd = data.detailCd
                      row.productNm = data.detailNm
                      modalStatus.Product = false
                    },
                    dismiss() {
                      row.productNm = String(oValue)
                      modalStatus.Product = false
                    }
                  }
                })
              }
            }
          }
        },
        afterRefreshData(grid, data) {
          if (grid !== undefined && data !== undefined && Array.isArray(data)) {
            this.$nextTick(() => {
              _.forEach(data, (item, index) => {
                var rowIndex = index + 1
                var val = [item.dcCd, item.lnTypeCd].join('_')

                switch (val) {
                  case 'C_ITEM':
                    grid.lockRow(rowIndex, true)
                    break
                  case 'D_TAX':
                    grid.lockRow(rowIndex, true)
                    break
                  case 'D_ITEM':
                    this.setColumnClassName(rowIndex, [4, 5, 7, 8, 13, 15], 'bg-lightpink')
                    break
                }
              })
            })
          }
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
            hidden: true
          }, {
            id: 'deptNm',
            align: 'left',
            value: '부서',
            width: 80,
            type: 'ed'
          }, {
            id: 'cctrSrch',
            align: 'center',
            value: '#cspan',
            width: 20,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateCctr()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateCctr() {
                  const value = this.$parent.value[this.index]
                  const vm = this;
                  this.$modal.open({
                    component: ErpCctr,
                    parent: this,
                    width: 700,
                    events: {
                      close(object) {
                        
                        value.deptCd = object.deptCd
                        value.deptNm = object.deptNm

                        //연구소부서 개발 프로젝트 세팅
                        if(object.expenseFlag === 'Y'){
                          //value.detailCd = vm.$parent.$parent.sgaProductCd
                          //value.detailNm = vm.$parent.$parent.sgaProductCd
                          //연구소부서 필수여부 설정
                          value.isExpenseFlag = true;
                        }else{
                          //연구소부서 필수여부 설정
                          value.isExpenseFlag = false;
                          value.detailCd = ''
                          value.detailNm = ''
                        }
                        
                        //계정, 보조계정, 관리항목 초기화 [START]
                        value.acctCd = '';
                        value.acctNm = '';
                        value.acctCdSub = '';
                        value.acctNmSub = '';

                        for(var i = 1; i <= 15; i++){
                          value['attribute'+i] = '';
                        }
                        //계정, 보조계정, 관리항목 초기화 [END]
                      }
                    }
                  })
                }
              }
            }
          },
          {
            id: 'acctCd',
            align: 'center',
            value: '계정코드',
            width: 80
          }, {
            id: 'acctNm',
            align: 'left',
            value: '계정명',
            width: 140,
            type: 'ed'
          }, {
            id: 'acctSrch',
            align: 'center',
            value: '#cspan',
            width: 40,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateAccount()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateAccount() {
                  const value = this.$parent.value[this.index]
                  const vm = this
                  this.$modal.open({
                    component: Account,
                    parent: this,
                    props: {
                      deptCd: value.deptCd,
                      slipTypeCd: vm.$parent.$parent.value.slipTypeCd
                    },
                    width: 700,
                    events: {
                      close(object) {
                        vm.value.acctCd = object.acctCd
                        vm.value.acctNm = object.acctNm
                        vm.value.ctrlYn = object.ctrlYn

                        vm.$http.get('/api/account/sub/' + value.deptCd + '/' + object.acctCd)
                            .then(response => {
                              var list = response.data;

                              if(list.length === 1){
                                vm.value.acctCdSub = list[0].subAcctCd
                                vm.value.acctNmSub = list[0].subAcctNm
                                vm.value.reqSubAcct = true
                              }else if(list.length > 1){
                                vm.value.reqSubAcct = true
                                if(vm.value.reqSubAcct && !vm.value.acctCdSub){
                                  //보조계정 팝업 호출
                                // vm.$parent.$parent.accountSubPop(value, object)
                                  vm.$modal.open({
                                    component: AccountSub,
                                    parent: vm,
                                    props: {
                                      deptCd: value.deptCd,
                                      slipTypeCd: vm.value.slipTypeCd,
                                      acctCd: object.acctCd,
                                      year: vm.$parent.$parent.value.postDt
                                    },
                                    width: 700,
                                    events: {
                                      close(object) {
                                        value.acctCdSub = object.subAcctCd
                                        value.acctNmSub = object.subAcctNm
                                      }
                                    }
                                  })
                                }
                              }else{
                                vm.value.acctCdSub = ''
                                vm.value.acctNmSub = ''
                                vm.value.reqSubAcct = false
                              }
                            });

                      }
                    }
                  })
                }
              },
            }
          },
          {
            id: 'dff',
            value: '#cspan',
            width: 40,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" v-if="visible" @click="updateDff()"><i class="fas fa-search"></i></button>`,
              data() {
                return {
                  visible: false
                }
              },
              methods: {
                updateDff() {
                  const vm = this
                  this.$modal.open({
                    component: SlipMngItemPop,
                    parent: this,
                    props: {
                      acctCd: this.value.acctCd,
                      data: this.value
                    },
                    events: {
                      close(data) {
                        for(var i = 1; i <= 15; i++){
                          vm.value['attribute'+i] = data['attribute'+i];
                        }
                      }
                    },
                  })
                }
              },
              created() {
                let length = account[this.value.acctCd]
                if (length === undefined) {
                  queryAccountAddon.apply(this, [this.value.acctCd])
                } else {
                  this.visible = length > 0
                  this.value.hasAddon = this.visible
                }

                eventBus.on(['account', this.value.acctCd].join('_'), () => {
                  this.visible = account[this.value.acctCd] > 0
                  this.value.hasAddon = this.visible
                })
              },
              watch: {
                value: {
                  immediate: true,
                  deep: true,
                  handler() {}
                }
              }
            }
          },
          {
            id: 'acctCdSub',
            align: 'center',
            value: '보조계정코드',
            width: 80
          },{
            id: 'acctNmSub',
            align: 'left',
            value: '보조계정',
            width: 140,
            type: 'ed'
          }, {
            id: 'acctSubSrch',
            align: 'center',
            value: '#cspan',
            width: 40,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateAccountSub()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateAccountSub() {
                  const value = this.$parent.value[this.index]
                  const vm = this

                  if(_.isEmpty(value.acctCd)){
                    this.$swal({
                      type: 'warning',
                      text: '계정을 먼저 선택하기 바랍니다.'
                    });
                    return;
                  }

                  this.$modal.open({
                    component: AccountSub,
                    parent: this,
                    props: {
                      deptCd: value.deptCd,
                      slipTypeCd: vm.$parent.$parent.value.slipTypeCd,
                      acctCd: value.acctCd,
                      year: vm.$parent.$parent.value.postDt
                    },
                    width: 700,
                    events: {
                      close(object) {
                        vm.value.acctCdSub = object.subAcctCd
                        vm.value.acctNmSub = object.subAcctNm
                      }
                    }
                  })
                }
              },
            }
          },
          {
            id: 'detailCd',
            align: 'left',
            value: '',
            type: 'ron',
            hidden: true
          },{
            id: 'detailNm',
            align: 'left',
            value: '개발 프로젝트',
            width: 80,
            type: 'ed'
          }, {
            id: 'prodSrch',
            align: 'center',
            value: '#cspan',
            width: 20,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateProd()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateProd() {
                  const value = this.$parent.value[this.index]
                  const self = this.$parent

                  if(!value.isExpenseFlag && value.expenseFlag !== 'Y'){
                    this.$swal({
                      type: 'warning',
                      text: '연구부서만 등록 가능합니다.'
                    });
                    return;
                  }

                  this.$modal.open({
                    component: Product,
                    parent: this,
                    props: {
                      slipTypeCd: self.$parent.value.slipTypeCd
                    },
                    width: 700,
                    events: {
                      close(object) {
                        value.detailCd = object.detailCd
                        value.detailNm = object.detailNm
                        modalStatus.Product = false;
                      }
                    }
                  })
                }
              }
            }
          },
          {
            id: 'useAmt',
            value: '금액',
            width: 80,
            type: 'ed',
            align: 'right',
          }, {
            id: 'rmdAmt',
            value: '잔여예산',
            width: 80,
            type: 'edn',
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
                let id = [this.$parent.$parent.value.postDt, this.value.deptCd, this.value.acctCd, this.value.acctCdSub].join('_')
                queryRemainBudget.apply(this, [this.$parent.$parent.value.postDt, this.value.deptCd, this.value.acctCd, this.value.acctCdSub])

                $bus.on('BUDGET_CMPL', () => {
                  this.data = this.value.rmdAmt = budget[id]
                })

              },
              watch: {
                immediate: true,
                deep: true,
                handler() {
                  queryRemainBudget.apply(this, [this.$parent.$parent.value.postDt, this.value.deptCd, this.value.acctCd, this.value.acctCdSub])
                }
              }
            }
          }, {
            id: 'taxCd',
            width: 80,
            type: 'ro',
            hidden: true
          }, {
            id: 'taxNm',
            value: '세금코드',
            width: 80,
            type: 'ro',
            align: 'right',
          }, {
            id: 'lnSgtxt',
            value: '적요',
            width: 320,
            align: 'left',
            type: 'ed'
          },{id: 'reqSubAcct', type: 'ro', value: '', width: 0},
        ],
        events: {
          onEditCell(stage, rId, cInd, nValue, oValue) {
            let row = this.data[rId - 1]
            let colDef = this.options.columns[cInd]
            // Edit event handler
            if (stage === 2 && nValue !== oValue) {
              switch (colDef.id) {
                case 'useAmt':
                  setTimeout(() => {
                    let topAmt = this.$parent.value.slipDetails.map(x => x.useAmt).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value())
                    this.$parent.value.totAmt = topAmt;
                    this.$parent.value.totAmtKrw = topAmt * this.$parent.value.excRt
                    if(this.$parent.value.curCd === 'JPY'){
                      this.$parent.value.totAmtKrw = topAmt * this.$parent.value.excRt * 0.01
                      this.$parent.value.totAmtKrw = Math.floor(this.$parent.value.totAmtKrw)
                    }else {
                      this.$parent.value.totAmtKrw = topAmt * this.$parent.value.excRt
                      this.$parent.value.totAmtKrw = Math.floor(this.$parent.value.totAmtKrw)
                    }
                    this.$forceUpdate()
                  }, 200)
                  break
              }
            }
            return true
          },
          onEnter(stage, rId, cInd, nValue, oValue) {
            let row = this.data[rId - 1]
            let colDef = this.options.columns[cInd]
            // Edit event handler
            if (stage === 2 && nValue !== oValue) {
              switch (colDef.id) {
                case 'deptNm': this.$parent.cctrValueChange(rId-1,cInd,nValue); break
                case 'acctNm': this.$parent.acctValueChange(rId-1,cInd,nValue); break
                case 'acctNmSub': this.$parent.subAcctValueChange(rId-1,cInd,nValue); break
                case 'detailNm': this.$parent.productValueChange(rId-1,cInd,nValue); break
              }
            }
            return true
          },
        },
        //2020.08.14 그리드(세부항목) 소수점 변경
        afterRefreshData(grid, data) {
          const that = this;
          this.$nextTick(() => {
            _.forEach(data, (item, index) => {
              let rId = index + 1
              if(that.$parent.value.slipTypeCd ==='E2'){
                if(that.$parent.value.curCd ==='KRW'){
                  grid.cells(rId, 13).setValue(that.$numeral(grid.cells(rId, 13).getValue()).format('0,0'));
                }else{
                  grid.cells(rId, 13).setValue(that.$numeral(grid.cells(rId, 13).getValue()).format('0,0'));
                }
              }
            })
          })
        }

      }, config_E6: {
        columns: [{
          id: 'itemSeq',
          value: 'No.',
          width: 35
        }, {
          id: 'useDt',
          value: '사용일자',
          width: 60,
          align: 'left',
          type: 'dhxCalendarA'
        }, {
          id: 'stptPlc',
          width: 60,
          value: '출발지',
          align: 'left',
          type: 'ed'
        }, {
          id: 'dstnPlc',
          width: 60,
          value: '도착지',
          align: 'left',
          type: 'ed'
        }, {
          id: 'biztrpObj',
          width: 60,
          value: '출장목적',
          align: 'left',
          type: 'ed'
        }, {
          id: 'tpsTypeCd',
          width: 60,
          value: '교통비유형',
          component: GridSelect,
          mixin: {
            data() {
              return {
                options: []
              }
            },
            created() {
              this.options = options['E6_tpsTypeCd']

              eventBus.on('E6_tpsTypeCd.updated', () => {
                this.options = options['E6_tpsTypeCd']
              })
            },
            methods: {
              onSubmit() {
                if (this.value.tpsTypeCd === '10') {
                  this.value.acctAmt = 0
                  this.value.tpsDst = 0
                  this.value.oilKindCd = 'GSL'
                } else {
                  this.value.tpsDst = undefined
                  this.value.oilUpc = undefined
                  this.value.oilEff = undefined
                  this.value.acctAmt = 0
                  this.value.oliKindCd = undefined
                }
              }
            }
          }
        }, {
          id: 'tpsDst',
          value: '거리',
          width: 60,
          align: 'right',
          type: 'edn'
        }, {
          id: 'oilKindCd',
          value: '유종',
          width: 60,
          component: GridSelect,
          mixin: {
            data() {
              return {
                options: []
              }
            },
            created() {
              if (this.value.tpsTypeCd !== '10') {
                this.showIf = false
              }
              this.options = options['E6_oilKindCd']
              this.onSubmit()
              eventBus.on('E6_oilKindCd.updated', () => {
                this.options = options['E6_oilKindCd']
                this.onSubmit()
              })
            },
            methods: {
              onSubmit() {
                if (this.value.tpsTypeCd === '10') {
                  if (options['E6_oilPrice']) {
                    var search = (options['E6_oilPrice'][this.$moment(this.value.useDt).format('YYYYMM')] || []).filter(x => x.oilKindCd === this.value.oilKindCd)
                    // found
                    if (search.length > 0) {
                      this.value.oilUpc = search[0].oilUpce
                      this.value.oilEff = search[0].oilEff
                      if (this.value.tpsTypeCd === '10') {
                        var tpsDst = this.$numeral(this.value.tpsDst).value()
                        var oilUpc = this.$numeral(this.value.oilUpc).value()
                        var oilEff = this.$numeral(this.value.oilEff).value()
                        try {
                          this.value.supAmt = this.value.acctAmt = Math.floor(tpsDst / oilEff * oilUpc)
                        } catch (e) {
                          this.value.supAmt = this.value.acctAmt = 0
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }, {
          id: 'oilUpc',
          value: '유류단가/연비',
          width: 80,
          component: {
            props: ['index', 'value', 'field'],
            template: `<span v-if="value.oilUpc && value.oilEff">{{ this.$numeral(value.oilUpc).format('0,0') }} / {{ this.$numeral(value.oilEff).format('0,0') }}</span>`,
            data() {
              return {
                options: []
              }
            },
            created() {
              this.options = options['E6_oilPrice'] ? options['E6_oilPrice'][this.$moment(this.value.useDt).format('YYYYMM')] : []
              this.updateOilPriceInfo()

              eventBus.on('E6_oilPrice.updated', baseYm => {
                this.options = options['E6_oilPrice'][baseYm]
                this.updateOilPriceInfo()
              })
            },
            methods: {
              updateOilPriceInfo() {
                if (options['E6_oilPrice'] && this.value.tpsTypeCd === '10') {
                  var search = (options['E6_oilPrice'][this.$moment(this.value.useDt).format('YYYYMM')] || []).filter(x => x.oilKindCd === this.value.oilKindCd)
                  // found
                  if (search.length > 0) {
                    this.value.oilUpc = search[0].oilUpce
                    this.value.oilEff = search[0].oilEff
                    if (this.value.tpsTypeCd === '10') {
                      var tpsDst = this.$numeral(this.value.tpsDst).value()
                      var oilUpc = this.$numeral(this.value.oilUpc).value()
                      var oilEff = this.$numeral(this.value.oilEff).value()
                      try {
                        this.value.supAmt = this.value.acctAmt = Math.floor(tpsDst / oilEff * oilUpc)
                      } catch (e) {
                        this.value.supAmt = this.value.acctAmt = 0
                      }
                    }
                  }
                }
              }
            }
          }
        }, {
          id: 'acctAmt',
          value: '사용금액',
          width: 60,
          align: 'right',
          type: 'edn'
        }, {
          id: 'acctCd',
          align: 'center',
          value: '계정코드',
          width: 50
        }, {
          id: 'acctNm',
          align: 'left',
          value: '계정명',
          width: 80,
          type: 'ed'
        }, {
          id: 'acctSrch',
          align: 'center',
          value: '#cspan',
          width: 20,
          component: {
            props: ['index', 'value', 'field'],
            template: `<button type="button" class="icon" @click="updateAccount()"><i class="fas fa-search"></i></button>`,
            methods: {
              updateAccount() {
                const value = this.$parent.value[this.index]
                const self = this.$parent
                this.$modal.open({
                  component: Account,
                  parent: this,
                  props: {
                    slipTypeCd: self.$parent.value.slipTypeCd
                  },
                  width: 700,
                  events: {
                    close(object) {
                      value.acctCd = object.acctCd
                      value.acctNm = object.acctNm
                      value.ctrlYn = object.ctrlYn
                    }
                  }
                })
              }
            }
          }
        }],
        height: 260,
        events: {
          onEditCell(stage, rId, cInd, nValue, oValue) {
            let row = this.data[rId - 1]
            let colDef = this.options.columns[cInd]

            // Before start
            if (stage === 0) {

              // 교통비유형이 10인 경우, 금액 수정 불가
              if (colDef.id === 'acctAmt' && row.tpsTypeCd === '10') {
                return false
              }
              // 교통비유형이 10이 아닌 경우, 거리 수정 불가
              else if (colDef.id === 'tpsDst' && row.tpsTypeCd !== '10') {
                return false
              }
            }
            // Edit event handler
            else if (stage === 2 && nValue !== oValue && colDef.id === 'acctNm') {
              setTimeout(() => {
                if (row.acctNm) {
                  _findAccount.apply(this, [row.acctNm])
                } else {
                  row.acctNm = String(oValue)
                }
              }, 200)
            }

            return true

            function _findAccount(searchStr) {
              this.$store.commit('loading')
              this.$http.get(_url('/api/account', searchStr))
                  .then(response => {
                    if (Array.isArray(response.data)) {
                      if (response.data.length === 1) {
                        row.acctCd = response.data[0].acctCd
                        row.acctNm = response.data[0].acctNm
                        row.ctrlYn = response.data[0].ctrlYn
                      } else {
                        _openAccountModal.apply(this, [searchStr])
                      }
                    } else {
                      _openAccountModal.apply(this, [searchStr])
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              })
            }

            function _openAccountModal(searchStr) {
              if (!modalStatus.Account) {
                modalStatus.Account = true
                this.$modal.open({
                  component: Account,
                  props: {
                    title: '귀속부서',
                    searchStr
                  },
                  parent: this,
                  events: {
                    close(data) {
                      row.acctCd = data.acctCd
                      row.acctNm = data.acctNm
                      row.ctrlYn = data.ctrlYn
                      modalStatus.Account = false
                    },
                    dismiss() {
                      row.acctNm = String(oValue)
                      modalStatus.Account = false
                    }
                  }
                })
              }
            }
          },
          onCellChanged(rId, cInd, nValue) {
            let row = this.data[this.instance.getRowIndex(rId)]
            let colDef = this.options.columns[cInd]

            if (colDef.id === 'useDt') {
              let max = String(Math.max.apply(this, this.value.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').map(x => x.useDt)))
              this.$parent.value.payDueDt = max
              _.forEach(this.value.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'C_ITEM'), x => {
                x.useDt = max
              })
            } else if (colDef.id === 'tpsDst') {

              if (row.tpsTypeCd === '10') {
                var tpsDst = this.$numeral(row.tpsDst).value()
                var oilUpc = this.$numeral(row.oilUpc).value()
                var oilEff = this.$numeral(row.oilEff).value()
                try {
                  row.supAmt = row.acctAmt = Math.floor(tpsDst / oilEff * oilUpc)
                } catch (e) {
                  // this.value[rId - 1].supAmt = this.value[rId - 1].acctAmt = 0
                }
              }
            } else if (colDef.id === 'acctAmt') {
              let id = [row.dcCd, row.lnTypeCd].join('_')
              if (id === 'D_ITEM') {
                let totAmt = this.value.filter(x => [x.dcCd, row.lnTypeCd].join('_') === 'D_ITEM').map(x => x.acctAmt).reduce((a, v) => a + v)
                let taxRt = this.$parent.value.taxRt / 100.0
                let taxAmt = Math.floor(taxRt * totAmt / (1 + taxRt))
                let supAmt = totAmt - taxAmt

                this.$parent.value.totAmt = totAmt
                this.$parent.value.supAmt = supAmt
                this.$parent.value.taxAmt = taxAmt

              }
            }
          }
        },
        afterRefreshData(grid, data) {
          if (grid !== undefined && data !== undefined && Array.isArray(data)) {
            this.$nextTick(() => {
              _.forEach(data, (item, index) => {
                var rowIndex = index + 1
                var val = [item.dcCd, item.lnTypeCd].join('_')

                switch (val) {
                  case 'C_ITEM':
                    grid.lockRow(rowIndex, true)
                    break
                  case 'D_TAX':
                    grid.lockRow(rowIndex, true)
                    break
                  case 'D_ITEM':
                    this.setColumnClassName(rowIndex, [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], 'bg-lightpink', true)
                    if (item.tpsTypeCd === '10')
                      this.setColumnClassName(rowIndex, [1, 2, 3, 4, 5, 6, 7, 11, 12], 'bg-lightpink')
                    else
                      this.setColumnClassName(rowIndex, [1, 2, 3, 4, 5, 9, 11, 12], 'bg-lightpink')
                    break
                }
              })
            })
          }
        }
      },
      config_E1: {
        columns: [{
          id: 'itemSeq',
          value: 'No.',
          width: 35
        },
          {
            id: 'eaSlipDt',
            value: '증빙일자',
            width: 100,
            align: 'left',
            type: 'ron',
          },{
            id: 'deptCd',
            align: 'center',
            value: '부서',
            hidden: true
          }, {
            id: 'deptNm',
            align: 'left',
            value: '부서',
            width: 120,
            type: 'ed'
          }, {
            id: 'cctrSrch',
            align: 'center',
            value: '#cspan',
            width: 40,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateCctr()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateCctr() {
                  const value = this.$parent.value[this.index]
                  const vm = this;
                  this.$modal.open({
                    component: ErpCctr,
                    parent: this,
                    width: 700,
                    events: {
                      close(object) {
                        value.deptCd = object.deptCd
                        value.deptNm = object.deptNm
                        //check (항목 계정비용구분 : value.expenseFlag)
                        if(object.expenseFlag !== value.expenseFlag) {
                          value.expenseId = undefined
                          value.expenseAcctName = undefined
                          value.expenseName = undefined
                          value.expenseAcctCode = undefined
                          value.expenseFlag = undefined
                        }
                      }
                    }
                  })
                }
              }
            }
          },
          {
            id: 'expenseId',
            value: '',
            type: 'ron'
          }, {
            id: 'expenseAcctCode',
            value: '',
            type: 'ron'
          }, {
            // id: 'ctrlNm',
            id: 'expenseName',
            align: 'left',
            value: '항목',
            width: 120,
            type: 'ron'
          }, {
            id: 'expenseSrch',
            align: 'center',
            value: '#cspan',
            width: 20,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateCtrl()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateCtrl() {
                  const value = this.$parent.value[this.index]
                  const self = this.$parent
                  const vm = this
                  this.$modal.open({
                    component: ErpAccount,
                    parent: this,
                    props: {
                      // deptCd: vm.$parent.$parent.value.wrtDeptCd,
                      deptCd: value.deptCd,
                      slipTypeCd: vm.$parent.$parent.value.slipTypeCd
                    },
                    width: 700,
                    events: {
                      close(object) {
                        vm.value.expenseId = object.expenseId
                        vm.value.expenseAcctName = object.expenseAcctName;
                        vm.value.expenseName = object.expenseName;
                        vm.value.expenseAcctCode = object.expenseAcctCode;
                        vm.value.expenseFlag = object.expenseFlag;

                        vm.$http.get(`/api/account/rcp/${value.deptCd}`)
                            .then(response => {
                              let data = response.data.filter(x => x.acctCd === object.expenseAcctCode && x.rcpYn === 'Y')[0]

                             if(data) value.vatCd = '10006'
                              else value.vatCd = '10004'

                            });

                      }
                    },
                  })
                }
              }
            }
          }, {
            id: 'expenseAcctName',
            align: 'left',
            value: '계정',
            width: 120,
            type: 'ron'
          }, {
            id: 'detailNm',
            align: 'left',
            value: '제품',
            width: 120,
            type: 'ron'
          }, {
            id: 'prodSrch',
            align: 'center',
            value: '#cspan',
            width: 20,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateProd()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateProd() {
                  const value = this.$parent.value[this.index]
                  const self = this.$parent
                  const vm = this
                  this.$modal.open({
                    component: Product,
                    parent: this,
                    props: {
                      slipTypeCd: self.$parent.value.slipTypeCd
                    },
                    width: 700,
                    events: {
                      close(object) {
                        value.detailCd = object.detailCd;
                        value.detailNm = object.detailNm;
                        modalStatus.Product = false;
                      }
                    }
                  })
                }
              }
            }
          },
          {
            id: 'vatCd',
            width: 120,
            value: '세금코드',
            component: GridSelect,
            mixin: {
              data() {
                return {
                  options: []
                }
              },
              created(){
                this.options = options['E1_taxesCd'];
                this.onSubmit();
                eventBus.on('E1_taxesCd.updated', () => {
                  this.options = options['E1_taxesCd'];
                  this.onSubmit();
                })
              },
              methods: {
                onSubmit() {
                  if(options['E1_taxesCd'] === undefined) {
                    this.$http.get('/api/taxes', {
                      params: {
                        evdTypeCd: '10'
                      }
                    }).then(response => {
                      options['E1_taxesCd'] = response.data;
                      let data = _.cloneDeep(options['E1_taxesCd']).map(x => {return x}).filter(x => x.taxCd !== undefined)
                      for(let i = 0; i < data.length; i++){
                        this.options[i].detailCd = data[i].taxCd;
                        this.options[i].detailNm = data[i].taxNm;
                      }
                      if (Array.isArray(this.options) && this.options.length > 0) {
                        let found = this.options.filter(x => x[this.detailCd] === this.data)[0]

                        if (found === undefined) {
                          this.data = this.options[0][this.detailCd];
                          this.value[this.field] = this.data;
                          this.$emit('input', this.value);
                        }
                      }
                    }).catch(response => {
                      options['E1_taxesCd'] = undefined;
                      return response
                    }).finally(() => {
                      eventBus.emit('E1_taxesCd.updated');
                      this.queryTaxes_run = false;
                    })
                  }else {
                    let data = _.cloneDeep(options['E1_taxesCd']).map(x => {return x}).filter(x => x.taxCd !== undefined)
                    for(let i = 0; i < data.length; i++){
                      this.options[i].detailCd = data[i].taxCd;
                      this.options[i].detailNm = data[i].taxNm;
                    }
                    if (Array.isArray(this.options) && this.options.length > 0) {
                      let found = this.options.filter(x => x[this.detailCd] === this.data)[0]

                      if (found === undefined) {
                        this.data = this.options[0][this.detailCd];
                        this.value[this.field] = this.data;
                        this.$emit('input', this.value);
                      }
                    }
                  }
                }

              }

            }
          }, {
            id: 'supAmt',
            value: '공급가액',
            width: 100,
            align: 'right',
            type: 'ron'
          }, {
            id: 'vatAmt',
            value: '세액',
            width: 100,
            align: 'right',
            type: 'ron',
            mixin: {
              created() {
                if (this.value.vatYn !== 'Y') {
                  this.showIf = false;
                }
              }
            }
          }, {
            id: 'useAmt',
            value: '금액',
            width: 100,
            align: 'right',
            type: 'ron'
          }, {
            id: 'lnSgtxt',
            value: '적요',
            width: 200,
            align: 'left',
            type: 'ed'
          },
          // {
          //   id: 'crdNo',
          //   value: '카드번호',
          //   width: 200,
          //   align: 'right',
          //   type: 'ed'
          // },
          {
            id: 'crdMerchNm',
            value: '가맹점명',
            width: 100,
            align: 'center',
            type: 'ro'
          },
          {
            id: 'crdInfo',
            value: '카드정보',
            width: 100,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon far fa-file" @click="crdInfoPop"></button>`,
              methods: {
                crdInfoPop(r) {
                  if (this.value.apprNo) {
                    this.open(this.value.crdNo, this.value.apprNo);
                  }
                },
                open(crdNo, apprNo){

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
          }, {
            id: 'scanCt',
            value: '스캔증빙',
            width: 100,
            component: {
              props: ['index', 'value', 'field'],
              template: `<span style="color: #0065b3;" @click="openUploadEvidencePopup()">{{ this.$numeral(value.scanCt).format('0') }} <i class="far fa-file-alt"></i></span>`,
              created(){
                this.scanFileCount();
              },
              methods: {
                scanFileCount() {
                  const val = this.$parent.value[this.index]
                  this.$http.get(`/api/evid/fileList/${val.eaSlipNo}`)
                      .then(response => {
                        val.scanCt = ((response.data || {}).aFiles || []).length
                      })
                },
                openUploadEvidencePopup(){
                  let vm = this
                  const val = this.$parent.value[this.index]

                  this.$modal.open({
                    component: EvidAtchPop,
                    props: {
                      docMngNo: vm.value.eaSlipNo,
                      readonly: vm.$parent.$parent.status.readonly // Check readonly this.form.slipStatCd !== '10'
                    },
                    parent: this,
                    width: 1200,
                    events: {
                      close(value) {
                        val.scanCt = value.length
                        //스캔증빙 카운팅 벨류 강제 업데이트
                        vm.$forceUpdate();
                      }
                    }
                  })
                }
              },
            }
          }, {
            id: 'stptPlc',
            width: 100,
            value: '출발지',
            align: 'left',
            type: 'ed'
          }, {
            id: 'dstnPlc',
            width: 100,
            value: '도착지',
            align: 'left',
            type: 'ed'
          }, {
            id: 'biztrpObj',
            width: 120,
            value: '출장목적',
            align: 'left',
            type: 'ed'
          }, {
            id: 'tpsTypeCd',
            width: 100,
            value: '교통비유형',
            component: GridSelect,
            mixin: {
              data() {
                return {
                  options: []
                }
              },
              created() {
                this.options = options['E1_tpsTypeCd']
                eventBus.on('E1_tpsTypeCd.updated', () => {
                  this.options = options['E1_tpsTypeCd']
                })

              },
              methods: {
                onSubmit() {
                  if (this.value.tpsTypeCd === '10') {
                    this.value.acctAmt = 0
                    this.value.tpsDst = 0
                    this.value.oilKindCd = 'GSL'
                  } else {
                    this.value.tpsDst = undefined
                    this.value.oilUpc = undefined
                    this.value.oilEff = undefined
                    this.value.acctAmt = 0
                    this.value.oliKindCd = undefined
                  }
                }
              }
            }
          }, {
            id: 'tpsDst',
            value: '거리',
            width: 100,
            align: 'right',
            type: 'edn'
          }, {
            id: 'oilKindCd',
            value: '유종',
            width: 100,
            component: GridSelect,
            props: ['index', 'value', 'field'],
            mixin: {
              data() {
                return {
                  options: []
                }
              },
              created() {
                if (this.value.tpsTypeCd !== '10') {
                  this.showIf = false
                }
                this.options = options['E1_oilKindCd']
                this.onSubmit()

                eventBus.on('E1_oilKindCd.updated', () => {
                  this.options = options['E1_oilKindCd']
                  this.onSubmit()
                })

              }
              ,methods: {
                onSubmit() {
                  if (this.value.tpsTypeCd === '10') {
                    if (options['E1_oilKindCd']) {
                      var search = (options['E1_oilKindCd'][this.$moment(this.value.useDt).format('YYYYMM')] || []).filter(x => x.oilKindCd === this.value.oilKindCd)
                      // found
                      if(search !== undefined) {
                        if (search.length > 0) {
                          try {
                            this.value.oilUpc = search[0].oilUpce
                            this.value.oilEff = search[0].oilEff

                            var tpsDst = this.$numeral(this.value.tpsDst || 0).value()
                            var oilUpc = this.$numeral(this.value.oilUpc || 0).value()
                            var oilEff = this.$numeral(this.value.oilEff || 0).value()
                            this.value.supAmt = this.value.useAmt = this.value.acctAmt = Math.floor(tpsDst / oilEff * oilUpc)
                            if(oilUpc > 0 && oilEff > 0 && tpsDst > 0){
                              setTimeout(() => {
                                let topAmt = this.$parent.$parent.data.map(x => x.useAmt).reduce((a, v) => a + v);
                                if(this.$parent.$parent.datad.length === 0) {
                                  this.$parent.$parent.value.totAmt = topAmt ;
                                } else {
                                  let botAmt = this.$parent.$parent.datad.map(x => x.useAmt2).reduce((a, v) => vm.$numeral(a).value() + vm.$numeral(v).value());
                                  this.$parent.$parent.value.totAmt = topAmt + botAmt;
                                }
                              }, 200)
                            }
                          } catch (e) {

                            this.value.supAmt = this.value.useAmt  = this.value.acctAmt = 0
                          }
                        }
                      }

                    }
                  }
                }
              }
            }
          }, {
            id: 'oilUpc',
            value: '유류단가/연비',
            width: 100,
            component: {
              props: ['index', 'value', 'field'],
              // template: `<span v-if="value.oilUpc && value.oilEff">{{ this.$numeral(value.oilUpc).format('0,0') }} / {{ this.$numeral(value.oilEff).format('0,0') }}</span>`,
              template: `<span v-if="value.cal">{{ value.cal }}</span>`,
              data() {
                return {
                  options: []
                }
              },
              created() {
                queryOilPrice.apply(this, [this.$moment(this.value.eaSlipDt).format('YYYYMM')])

                this.options = options['E1_oilPrice'] ? options['E1_oilPrice'][this.$moment(this.value.eaSlipDt).format('YYYYMM')] : []

                var search = (options['E1_oilPrice'][this.$moment(this.value.eaSlipDt).format('YYYYMM')] || []).filter(x => x.oilKindCd === this.value.oilKindCd)
                this.value.cal = this.$numeral(search[0].oilUpce).format('0,0') + ' / ' + this.$numeral(search[0].oilEff).format('0,0')

                //
                this.value.oilUpc = search[0].oilUpce
                this.value.oilEff = search[0].oilEff
                //
                var tpsDst = this.$numeral(this.value.tpsDst || 0).value()
                var oilUpc = this.$numeral(search[0].oilUpce || 0).value()
                var oilEff = this.$numeral(search[0].oilEff || 0).value()
                if(tpsDst) {
                  this.value.supAmt = this.value.useAmt  = this.value.acctAmt = Math.floor(tpsDst / oilEff * oilUpc)
                  setTimeout(() => {
                    let topAmt = this.$parent.$parent.data.map(x => x.useAmt).reduce((a, v) => a + v);
                    if(this.$parent.$parent.datad.length === 0) {
                      this.$parent.$parent.value.totAmt = topAmt ;
                    } else {
                      let botAmt = this.$parent.$parent.datad.map(x => x.useAmt2).reduce((a, v) => vm.$numeral(a).value() + vm.$numeral(v).value());
                      this.$parent.$parent.value.totAmt = topAmt + botAmt;
                    }
                  })
                }
              },
            }
          }, {
            id: 'acctAmt',
            value: '경비금액',
            width: 100,
            align: 'right',
            type: 'edn'
          }, {
            id: 'evdTypeCd',
            value: '',
            hidden: true
          },
          {
            id: 'eaSlipNo',
            value: '전표번호',
            width: 100,
            hidden: true
          },
        ],
        height: 260,
        events: {
          onEditCell(stage, rId, cInd, nValue, oValue) {
            let row = this.data[rId - 1]
            let colDef = this.options.columns[cInd]

            // Before start
            if (stage === 0) {
              // 교통비유형이 10인 경우, 금액 수정 불가
              if (colDef.id === 'acctAmt' && row.tpsTypeCd === '10') {
                return false
              }
              // 교통비유형이 10이 아닌 경우, 거리 수정 불가
              else if (colDef.id === 'tpsDst' && row.tpsTypeCd !== '10') {
                return false
              }
            }
                // Edit event handler
            // 항목/계정 팝업
            else if (stage === 2 && nValue !== oValue && colDef.id === 'acctNm') {
              setTimeout(() => {
                if (row.acctNm) {
                  _findAccount.apply(this, [row.acctNm])
                } else {
                  row.acctNm = String(oValue)
                }
              }, 200)
            }
            // 제품 팝업
            else if(stage === 2 && nValue !== oValue && colDef.id === 'detailNm') {
              setTimeout(() => {
                if (row.detailNm) {
                  _findProduct.apply(this, [row.detailNm])
                } else {
                  row.detailNm = String(oValue)
                }
              }, 200)
            }

            return true

            function _findAccount(searchStr) {
              this.$store.commit('loading')
              this.$http.get(_url('/api/account', searchStr))
                  .then(response => {
                    if (Array.isArray(response.data)) {
                      if (response.data.length === 1) {
                        row.acctCd = response.data[0].acctCd
                        row.acctNm = response.data[0].acctNm
                        row.ctrlYn = response.data[0].ctrlYn
                      } else {
                        _openAccountModal.apply(this, [searchStr])
                      }
                    } else {
                      _openAccountModal.apply(this, [searchStr])
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              })
            }

            function _openAccountModal(searchStr) {
              if (!modalStatus.Account) {
                modalStatus.Account = true
                this.$modal.open({
                  component: Account,
                  props: {
                    title: '귀속부서',
                    searchStr
                  },
                  parent: this,
                  events: {
                    close(data) {
                      row.acctCd = data.acctCd
                      row.acctNm = data.acctNm
                      row.ctrlYn = data.ctrlYn
                      modalStatus.Account = false
                    },
                    dismiss() {
                      row.acctNm = String(oValue)
                      modalStatus.Account = false
                    }
                  }
                })
              }
            }

            function _findProduct(searchStr) {
              this.$store.commit('loading')
              this.$http.get(_url('/api/code/detail'),{
                params: {
                  groupCd: "PRODUCT_CD"
                }
              })
                  .then(response => {
                    if (Array.isArray(response.data)) {
                      if (response.data.length === 1) {
                        row.detailCd = response.data[0].detailCd
                        row.detailNm = response.data[0].detailNm
                      } else {
                        _openProductModal.apply(this, [searchStr])
                      }
                    } else {
                      _openProductModal.apply(this, [searchStr])
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              })
            }

            function _openProductModal(searchStr) {
              if (!modalStatus.Product) {
                modalStatus.Product = true
                this.$modal.open({
                  component: Product,
                  props: {
                    title: '제품',
                    searchStr
                  },
                  parent: this,
                  events: {
                    close(data) {
                      row.detailCd = data.detailCd
                      row.detailNm = data.detailNm
                      modalStatus.Product = false
                    },
                    dismiss() {
                      row.acctNm = String(oValue)
                      modalStatus.Product = false
                    }
                  }
                })
              }
            }
          },
          onCellChanged(rId, cInd, nValue) {
            let row = this.data[this.instance.getRowIndex(rId)]
            let colDef = this.options.columns[cInd]

            if (colDef.id === 'tpsDst') {
              if(row) {
                if (row.tpsTypeCd === '10') {
                  var tpsDst = this.$numeral(row.tpsDst).value()
                  var oilUpc = this.$numeral(row.oilUpc).value()
                  var oilEff = this.$numeral(row.oilEff).value()
                  try {
                    if(oilUpc > 0 && oilEff > 0 && tpsDst > 0){
                      row.useAmt = Math.floor(tpsDst / oilEff * oilUpc)
                      row.vatAmt = 0;
                      row.supAmt = Math.floor(tpsDst / oilEff * oilUpc)
                      row.acctAmt = Math.floor(tpsDst / oilEff * oilUpc)
                    }
                  } catch (e) {

                    this.value[rId - 1].useAmt = this.value[rId - 1].acctAmt = 0;
                    this.value[rId - 1].vatAmt = 0;
                    this.value[rId - 1].supAmt = this.value[rId - 1].acctAmt = 0;
                  }
                }
              }
            } else if (colDef.id === 'acctAmt') {
              if(row) {//2020.06.29 법인카드내역 27번째 적요 입력안되는 현상(row가 undefind로 나옴)
                let id = [row.dcCd, row.lnTypeCd].join('_')
                if (id === 'D_ITEM') {
                  let totAmt = this.value.filter(x => [x.dcCd, row.lnTypeCd].join('_') === 'D_ITEM').map(x => x.acctAmt).reduce((a, v) => a + v)
                  let taxRt = this.$parent.value.taxRt / 100.0
                  let taxAmt = Math.floor(taxRt * totAmt / (1 + taxRt))
                  let supAmt = totAmt - taxAmt
                  try{
                    if(row.oilUpc > 0 && row.oilEff > 0 && row.tpsDst > 0){
                      this.$parent.value.totAmt = totAmt
                      this.$parent.value.taxAmt = taxAmt
                      var tpsDst = this.$numeral(row.tpsDst).value()
                      var oilUpc = this.$numeral(row.oilUpc).value()
                      var oilEff = this.$numeral(row.oilEff).value()

                      if(row.tpsTypeCd !== '10'){
                        row.useAmt = supAmt;
                        row.vatAmt = 0;
                        row.supAmt = supAmt;
                      }
                    }
                  }catch(e){}

                  // _.forEach(this.value.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'C_ITEM'), x => {
                  //   x.acctAmt = totAmt
                  // })
                }
              }
            }
          }
        },
        afterRefreshData(grid, data) {
          // grid = this.$refs.grid;
          if (grid !== undefined && data !== undefined && Array.isArray(data)) {
            this.$nextTick(() => {
              _.forEach(data, (item, index) => {
                var rowIndex = index + 1
                var val = [item.dcCd, item.lnTypeCd].join('_')

                if (grid.cells(rowIndex, 1).getValue()) grid.cells(rowIndex, 1).setValue(this.$moment(grid.cells(rowIndex, 1).getValue()).format('YYYY-MM-DD'));

                switch (val) {
                  case 'C_ITEM':
                    grid.lockRow(rowIndex, true)
                    break
                  case 'D_TAX':
                    grid.lockRow(rowIndex, true)
                    break
                  case 'D_ITEM':
                    this.setColumnClassName(rowIndex, [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], 'bg-lightpink', true)
                    if (item.tpsTypeCd === '10')
                      this.setColumnClassName(rowIndex, [2, 3, 4, 5, 6, 7, 11, 12], 'bg-lightpink')
                    else
                      this.setColumnClassName(rowIndex, [2, 3, 4, 5, 9, 11, 12], 'bg-lightpink')
                    break
                }
              })
            })
          }
          grid.attachEvent("onCellChanged", (rId,cInd,nValue) => {
            let row = this.data[this.instance.getRowIndex(rId)]
            let colDef = this.options.columns[cInd]

            if (colDef.id === 'tpsDst') {
              if(row) {
                if (row.tpsTypeCd === '10') {
                  try {
                    var tpsDst = this.$numeral(row.tpsDst || 0).value()
                    var oilUpc = this.$numeral(row.oilUpc || 0).value()
                    var oilEff = this.$numeral(row.oilEff || 0).value()
                    if(oilUpc > 0 && oilEff > 0 && tpsDst > 0){
                      row.useAmt = Math.floor(tpsDst / oilEff * oilUpc)
                      row.vatAmt = 0;
                      row.supAmt = Math.floor(tpsDst / oilEff * oilUpc)
                      row.acctAmt = Math.floor(tpsDst / oilEff * oilUpc)
                    }
                  } catch (e) {

                    this.value[rId - 1].useAmt = this.value[rId - 1].acctAmt = 0;
                    this.value[rId - 1].vatAmt = 0;
                    this.value[rId - 1].supAmt = this.value[rId - 1].acctAmt = 0;
                  }
                }
              }
            }
            else if (colDef.id === 'acctAmt') {
              let id = [row.dcCd, row.lnTypeCd].join('_')
              if (id === 'D_ITEM') {
                try{
                  let totAmt = this.value.filter(x => [x.dcCd, row.lnTypeCd].join('_') === 'D_ITEM').map(x => x.acctAmt).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value())
                  let taxRt = this.$parent.value.taxRt / 100.0
                  let taxAmt = Math.floor(taxRt * totAmt / (1 + taxRt))
                  let supAmt = totAmt - taxAmt
                  let botAmt = this.$parent.value.slipDetails2.filter(x => [x.dcCd, row.lnTypeCd].join('_') === 'D_ITEM').map(x => x.useAmt2).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value())

                  this.$parent.value.totAmt = totAmt + botAmt
                  this.$parent.value.taxAmt = taxAmt
                  var tpsDst = this.$numeral(row.tpsDst || 0).value()
                  var oilUpc = this.$numeral(row.oilUpc || 0).value()
                  var oilEff = this.$numeral(row.oilEff || 0).value()

                  if(row.tpsTypeCd !== '10'){
                    row.useAmt = supAmt;
                    row.vatAmt = 0;
                    row.supAmt = supAmt;
                  }
                  this.$forceUpdate()
                }catch(e){
                  _.forEach(this.value.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'C_ITEM'), x => {
                    x.acctAmt = totAmt
                  })
                }
              }
            }

          });
        }
      },
      config_E1_2: {
        columns: [{
          id: 'itemSeq',
          align: 'center',
          value: 'No.',
          type: 'cntr',
          width: 35
        }, {
          id: 'eaSlipDt',
          value: '증빙일자',
          width: 100,
          align: 'left',
          type: 'dhxCalendarA'
        }, {
          id: 'deptCd',
          align: 'center',
          value: '부서',
          hidden: true
        }, {
          id: 'deptNm',
          align: 'left',
          value: '부서',
          width: 120,
          type: 'ed'
        }, {
          id: 'cctrSrch',
          align: 'center',
          value: '#cspan',
          width: 40,
          component: {
            props: ['index', 'value', 'field'],
            template: `<button type="button" class="icon" @click="updateCctr()"><i class="fas fa-search"></i></button>`,
            methods: {
              updateCctr() {
                const value = this.$parent.value[this.index]
                const vm = this
                this.$modal.open({
                  component: ErpCctr,
                  parent: this,
                  width: 700,
                  events: {
                    close(object) {
                      value.deptCd = object.deptCd
                      value.deptNm = object.deptNm
                      //check (항목 계정비용구분 : value.expenseFlag)
                      if(object.expenseFlag !== value.expenseFlag2) {
                        value.expenseId2 = undefined
                        value.expenseAcctName2 = undefined
                        value.expenseName2 = undefined
                        value.expenseAcctCode2 = undefined
                        value.expenseFlag2 = undefined
                      }
                    }
                  }
                })
              }
            }
          }
        },
          {
            id: 'expenseId2',
            value: '',
            type: 'ron'
          }, {
            id: 'expenseAcctCode2',
            value: '',
            type: 'ron'
          }, {
            id: 'expenseName2',
            align: 'center',
            value: '항목',
            type: 'ron',
            width: 80
          }, {
            id: 'acctSrch',
            align: 'center',
            value: '#cspan',
            width: 40,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateAccount()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateAccount() {
                  const value = this.$parent.value[this.index]
                  const vm = this
                  this.$modal.open({
                    component: ErpAccount,
                    parent: this,
                    props: {
                      deptCd: value.deptCd,
                      slipTypeCd: vm.$parent.$parent.value.slipTypeCd
                    },
                    width: 700,
                    events: {
                      close(object) {
                        // console.log(vm.value)
                        value.expenseId2 = object.expenseId
                        value.expenseAcctName2 = object.expenseAcctName
                        value.expenseName2 = object.expenseName
                        value.expenseAcctCode2 = object.expenseAcctCode
                        value.expenseFlag2 = object.expenseFlag;
                        modalStatus.Account = false
                      }
                    }
                  })
                }
              }
            }
          }, {
            id: 'expenseAcctName2',
            align: 'left',
            value: '계정명',
            width: 140,
            type: 'ron'
          }, {
            id: 'detailNm2',
            align: 'left',
            value: '제품',
            width: 120,
            type: 'ron'
          }, {
            id: 'prodSrch',
            align: 'center',
            value: '#cspan',
            width: 20,
            component: {
              props: ['index', 'value', 'field'],
              template: `<button type="button" class="icon" @click="updateCtrl()"><i class="fas fa-search"></i></button>`,
              methods: {
                updateCtrl() {
                  const value = this.$parent.value[this.index]
                  const self = this.$parent
                  this.$modal.open({
                    component: Product,
                    parent: this,
                    props: {
                      slipTypeCd: self.$parent.value.slipTypeCd
                    },
                    width: 700,
                    events: {
                      close(object) {
                        value.detailCd2 = object.detailCd2
                        value.detailNm2 = object.detailNm2
                        modalStatus.Product = false;
                      }
                    }
                  })
                }
              }
            }
          }, {
            id: 'useAmt2',
            value: '금액',
            width: 100,
            align: 'right',
            type: 'edn'
          }
          , {
            id: 'lnSgtxt',
            align: 'left',
            value: '적요',
            type: 'ed',
            width: 200
          }, {
            id: 'scanCt',
            value: '스캔증빙',
            width: 100,
            component: {
              props: ['index', 'value', 'field'],
              template: `<span style="color: #0065b3;" @click="openUploadEvidencePopup()">{{ this.$numeral(value.scanCt).format('0') }} <i class="far fa-file-alt"></i></span>`,
              created(){
                this.scanFileCount();
              },
              methods: {
                scanFileCount() {
                  const val = this.$parent.value[this.index]
                  this.$http.get(`/api/evid/fileList/${this.value.eaSlipNo}`)
                      .then(response => {
                        val.scanCt = ((response.data || {}).aFiles || []).length
                      })
                },
                openUploadEvidencePopup(){
                  let vm = this
                  const val = this.$parent.value[this.index]

                  this.$modal.open({
                    component: EvidAtchPop,
                    props: {
                      docMngNo: vm.value.eaSlipNo,
                      readonly: vm.$parent.$parent.status.readonly // Check readonly this.form.slipStatCd !== '10'
                    },
                    parent: this,
                    width: 1200,
                    events: {
                      close(value) {
                        val.scanCt = value.length
                        //스캔증빙 카운팅 벨류 강제 업데이트
                        vm.$forceUpdate();
                      }
                    }
                  })
                }
              },
            }
          }, {
            id: 'storeNm',
            align: 'left',
            value: '가맹점',
            type: 'ed',
            width: 200
          }, {
            id: 'address',
            align: 'left',
            value: '주소',
            type: 'ed',
            width: 200
          }, {
            id: 'evdTypeCd',
            value: '',
            hidden: true
          },
        ],
        //////////////////////////////////
        height: 260,
        events: {
          onEditCell(stage, rId, cInd, nValue, oValue) {
            let row = this.data[rId - 1]
            let colDef = this.options.columns[cInd]

            // Edit event handler
            if (stage === 2 && nValue !== oValue) {
              switch (colDef.id) {
                case 'cctrNm': // 귀속부서
                  // Delay 200ms
                  setTimeout(() => {
                    if (row.cctrNm) {
                      _findCctr.apply(this, [row.cctrNm])
                    } else {
                      row.cctrNm = String(oValue)
                    }
                  }, 200)
                  break
                case 'expenseName2': // 항목
                case 'expenseAcctName2': // 계정
                  // Delay 200ms
                  setTimeout(() => {
                    if (row.expenseAcctName2) {
                      _findAccount.apply(this, [row.expenseAcctName2])
                    } else {
                      row.expenseAcctName2 = String(oValue)
                    }
                  }, 200)
                  break
                case 'detailNm2':
                  // Delay 200ms
                  setTimeout(() => {
                    if (row.detailNm2) {
                      _findProduct.apply(this, [row.detailNm2])
                    } else {
                      row.detailNm2 = String(oValue)
                    }
                  }, 200)
                  break
                case 'useAmt2':
                  setTimeout(() => {
                    let botAmt = this.data.map(x => x.useAmt2).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
                    if(this.$parent.value.slipDetails.length === 0) {
                      let totAmt = 0
                      this.$parent.value.totAmt = totAmt + botAmt
                    } else {
                      let topAmt = this.$parent.value.slipDetails.map(x => x.useAmt).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value())
                      this.$parent.value.totAmt = topAmt + botAmt;
                    }

                    this.$forceUpdate()
                  }, 200)

                  break
              }
            }

            return true

            function _findCctr(searchStr) {
              this.$store.commit('loading')
              this.$http.get(_url('/api/cctr', searchStr))
                  .then(response => {
                    if (Array.isArray(response.data)) {
                      if (response.data.length === 1) {
                        row.cctrCd = response.data[0].cctrCd
                        row.cctrNm = response.data[0].cctrNm
                      } else {
                        _openCctrModal.apply(this, [searchStr])
                      }
                    } else {
                      _openCctrModal.apply(this, [searchStr])
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              })
            }

            function _openCctrModal(searchStr) {
              if (!modalStatus.Cctr) {
                modalStatus.Cctr = true
                this.$modal.open({
                  component: Cctr,
                  props: {
                    title: '귀속부서',
                    searchStr
                  },
                  parent: this,
                  events: {
                    close(data) {
                      row.cctrCd = data.cctrCd
                      row.cctrNm = data.cctrNm
                      modalStatus.Cctr = false
                    },
                    dismiss() {
                      row.cctrNm = String(oValue)
                      modalStatus.Cctr = false
                    }
                  }
                })
              }
            }

            function _findAccount(searchStr) {
              this.$store.commit('loading')
              this.$http.get(_url('/api/account', searchStr))
                  .then(response => {
                    if (Array.isArray(response.data)) {
                      if (response.data.length === 1) {
                        row.acctCd = response.data[0].acctCd
                        row.acctNm = response.data[0].acctNm
                        row.ctrlYn = response.data[0].ctrlYn
                      } else {
                        _openAccountModal.apply(this, [searchStr])
                      }
                    } else {
                      _openAccountModal.apply(this, [searchStr])
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              })
            }

            function _openAccountModal(searchStr) {
              if (!modalStatus.Account) {
                modalStatus.Account = true
                this.$modal.open({
                  component: Account,
                  props: {
                    title: '귀속부서',
                    searchStr
                  },
                  parent: this,
                  events: {
                    close(data) {
                      row.acctCd = data.acctCd
                      row.acctNm = data.acctNm
                      row.ctrlYn = data.ctrlYn
                      modalStatus.Account = false
                    },
                    dismiss() {
                      row.acctNm = String(oValue)
                      modalStatus.Account = false
                    }
                  }
                })
              }
            }

            function _findProduct(searchStr) {
              this.$store.commit('loading')
              this.$http.get(_url('/api/code/detail'),{
                params: {
                  groupCd: "PRODUCT_CD"
                }
              })
                  .then(response => {
                    if (Array.isArray(response.data)) {
                      if (response.data.length === 1) {
                        row.detailCd2 = response.data[0].detailCd
                        row.detailNm2 = response.data[0].detailNm
                      } else {
                        _openProductModal.apply(this, [searchStr])
                      }
                    } else {
                      _openProductModal.apply(this, [searchStr])
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              })
            }

            function _openProductModal(searchStr) {
              if (!modalStatus.Product) {
                modalStatus.Product = true
                this.$modal.open({
                  component: Product,
                  props: {
                    title: '제품',
                    searchStr
                  },
                  parent: this,
                  events: {
                    close(data) {
                      row.detailCd2 = data.detailCd
                      row.detailNm2 = data.detailNm
                      modalStatus.Product = false
                    },
                    dismiss() {
                      row.acctNm = String(oValue)
                      modalStatus.Product = false
                    }
                  }
                })
              }
            }
          },
        },

        afterRefreshData(grid, data) {
          if (grid !== undefined && data !== undefined && Array.isArray(data)) {
            this.$nextTick(() => {
              _.forEach(data, (item, index) => {
                var rowIndex = index + 1
                var val = [item.dcCd, item.lnTypeCd].join('_')

                switch (val) {
                  case 'C_ITEM':
                    grid.lockRow(rowIndex, true)
                    break
                  case 'D_TAX':
                    grid.lockRow(rowIndex, true)
                    break
                  case 'D_ITEM':
                    this.setColumnClassName(rowIndex, [6, 7, 9, 10, 13, 15], 'bg-lightpink')
                    break
                }
              })
            })
          }
        }
      },
    }
  },
  created() { 
    if (this.slipType === 'E6' || this.slipType === 'E1' || this.slipType === 'E2' || this.slipType === 'E5') {
      queryTpsTypeCd.apply(this, [])
      queryOilKinCd.apply(this, [])
      queryVatAcct.apply(this, [])
      if(this.slipType === 'E1') queryTaxes.apply(this, [])
      queryOilPrice.apply(this, [this.$moment(this.value.evdDt).format('YYYYMM')])
    }      
  },
  mounted() {
     this.$store.commit('loading')
     this.$http.get(`/api/emp/${this.$store.state.loginInfo.loginId}`)
       .then(response => {
         let data = response.data[0]
  //       this.value.wrtDeptCd = data.liabilityDeptCode
  //       this.value.wrtDeptNm = data.liabilityDeptName
         this.$http
             .get('/api/cctr/erp/' + data.liabilityDeptCode)
             .then(response => {
               this.baseDeptGrCd = response.data[0].expenseFlag;
             })
       })
       .catch((e) => {
         this.$swal({
           type: 'error',
           text: e
         })
       })
       .finally(() => {
  //       setTimeout(() => {
  //         if(this.$route.query.lnk === 'main') {
  //           this.buttenClickEventCrdList()
  //         }
  //       }, 200)
         this.$store.commit('finish')
       })
    setTimeout(() => {
      if(this.$route.query.lnk === 'main') {
        this.buttenClickEventCrdList()
      }
    }, 200) 
  },
  methods: {
    accountSubPop(value, object){

      this.$modal.open({
        component: AccountSub,
        parent: this,
        props: {
          deptCd: value.deptCd,
          slipTypeCd: this.value.slipTypeCd,
          acctCd: object.acctCd,
          year: this.$parent.$parent.value.postDt
        },
        width: 700,
        events: {
          close(object) {
            value.acctCdSub = object.subAcctCd
            value.acctNmSub = object.subAcctNm
          }
        }
      })
    },
    buttenClickEventReset() {
      // TODO::물어봐요
      // 데이터 리셋
      this.reset_rows()
      this.$refs.grid.instance.callEvent('onGridReconstructed', [])
    },
    buttenClickEventCrdList() {
      const vm = this

      this.$modal.open({
        component: SlipCrdLstModal,
        parent: this,
        props: {
          deptCd: this.value.baseDeptCd,
          slipTypeCd: this.value.slipTypeCd,
          copyUseDetails: vm.data
        },
        width: 1800,
        events: {
          close(object) {
            if(object === undefined) {
              return false
            } else {

              //2020.05.14 배열 순서 바뀌는 현상 수정(api호출할때 순서 변경되어서 forEach문 밖으로 뺌)
              vm.$http.get(`/api/account/rcp/${this.value.baseDeptCD}`).then(response => {
                vm.data1 = response.data.filter(v => v.acctCd === object[0].expenseAcctCode && v.rcpYn === 'Y')[0];

                object.forEach(async(x, i) => {
                  vm.$http.get(`/api/tmp/documentMngNo`)
                      .then(response => {x.eaSlipNo = response.data})

                  //2020.04.23 E1 법인카드 금액 합산 안되는 현상 수정
                  //.finally(() => {
                  vm.add_row({
                    dcCd: 'D',
                    lnTypeCd: 'ITEM',
                    useDt: vm.slipType === 'E1' ? vm.value.evdDt : undefined,
                    tpsTypeCd: vm.slipType === 'E1' ? '10' : undefined,
                    oilKindCd: vm.slipType === 'E1' ? 'GSL' : undefined,
                    acctAmt: 0,
                    crdNo: x.cardNo,
                    crdMerchBizNo: x.merchBizNo,
                    crdMerchNm: x.merchNm,
                    deptCd: (vm.slipType === 'E1') ? vm.value.baseDeptCd : undefined,
                    deptNm: (vm.slipType === 'E1') ? vm.value.baseDeptNm : undefined,
                    eaSlipDt: vm.$moment(x.apprDate).format('YYYYMMDD'),
                    supAmt: x.apprAmt,
                    useAmt: x.purchTot,
                    vatAmt: x.vat,
                    useDtlsNo: x.useDtlsNo,
                    crdInfo: x.apprNo,
                    apprNo: x.apprNo,
                    eaSlipNo: x.eaSlipNo,
                    detailCd: x.detailCd ? x.detailCd : '',
                    detailNm: x.detailNm ? x.detailNm : '',
                    expenseId: x.expenseId,
                    expenseName: x.expenseName,
                    expenseAcctName: x.expenseAcctName,
                    expenseAcctCode: x.expenseAcctCode,
                    lnSgtxt: x.crdOln,
                    vatCd: vm.data1 ? '10006' : '10004',
                    stptPlc : x.stptPlcPop,
                    dstnPlc : x.dstnPlcPop,
                    biztrpObj : x.biztrpObjPop,
                  });

                  if((object.length-1) === i) {  //계산 로직 싱크 맞추기 위해서 간단히 체크후 사용
                    vm.setTotAmt();
                  }
                  //}) //finally end
                })// for end
              });


              /* 원본소스
               object.forEach(async(x, i) => {
                 vm.$http.get(`/api/account/rcp`).then(response => {
                  let data = response.data.filter(v => v.acctCd === object[0].expenseAcctCode && v.rcpYn === 'Y')[0];
                  vm.$http.get(`/api/tmp/documentMngNo`)
                   .then(response => {x.eaSlipNo = response.data})
                 //2020.04.23 E1 법인카드 금액 합산 안되는 현상 수정
                 //.finally(() => {
                 vm.add_row({
                     dcCd: 'D',
                     lnTypeCd: 'ITEM',
                     useDt: vm.slipType === 'E1' ? vm.value.evdDt : undefined,
                     tpsTypeCd: vm.slipType === 'E1' ? '10' : undefined,
                     oilKindCd: vm.slipType === 'E1' ? 'GSL' : undefined,
                     acctAmt: 0,
                     crdNo: x.cardNo,
                     crdMerchBizNo: x.merchBizNo,
                     crdMerchNm: x.merchNm,
                     deptCd: (vm.slipType === 'E1') ? vm.value.wrtDeptCd : undefined,
                     deptNm: (vm.slipType === 'E1') ? vm.value.wrtDeptNm : undefined,
                     eaSlipDt: vm.$moment(x.apprDate).format('YYYYMMDD'),
                     supAmt: x.apprAmt,
                     useAmt: x.purchTot,
                     vatAmt: x.vat,
                     useDtlsNo: x.useDtlsNo,
                     crdInfo: x.apprNo,
                     apprNo: x.apprNo,
                     eaSlipNo: x.eaSlipNo,
                     detailCd: x.detailCd ? x.detailCd : '0000',
                     detailNm: x.detailNm ? x.detailNm : 'N/A',
                     expenseId: x.expenseId,
                     expenseName: x.expenseName,
                     expenseAcctName: x.expenseAcctName,
                     expenseAcctCode: x.expenseAcctCode,
                     lnSgtxt: x.crdOln,
                     vatCd: data ? '10006' : '10004',
                     stptPlc : x.stptPlcPop,
                     dstnPlc : x.dstnPlcPop,
                     biztrpObj : x.biztrpObjPop,
                   });
                   //}) //finally end
                   if((object.length-1) === i) {  //계산 로직 싱크 맞추기 위해서 간단히 체크후 사용
                     vm.setTotAmt();
                   }
                 }); //api(rcp)end
               })// for end
               */
            }// else end
          }
        }
      });
    },
    setTotAmt(){
      const vm = this
      return new Promise(resolve => {
        let topAmt = this.$parent.value.slipDetails.map(x => x.useAmt).reduce((a, v) => a + v);
        if(this.$parent.value.slipDetails2.length === 0) {
          this.$parent.value.totAmt = topAmt ;
        } else {
          let botAmt = this.$parent.value.slipDetails2.map(x => x.useAmt2).reduce((a, v) => vm.$numeral(a).value() + vm.$numeral(v).value());
          this.$parent.value.totAmt = topAmt + botAmt;
        }
      })
    },
    buttenClickEventCopy(){
      try {
        if (this.$refs.grid.instance.getSelectedRowId() == null)
          throw '선택된 행이 없습니다.'
        let index = this.$refs.grid.instance.getSelectedRowId() - 1
        this.add_row(this.data[index])
        this.data[this.data.length-1].itemSeq = this.$numeral(this.$refs.grid.instance.getSelectedRowId()).value() + 1
        // this.data.push(_.clone(this.data[index]));
      } catch (e) {
        this.$swal({
          type: 'warning',
          text: e
        })
      }
    },
    buttenClickEventCopy2(){
      try {
        if (this.$refs.grid2.instance.getSelectedRowId() == null)
          throw '선택된 행이 없습니다.'
        let index = this.$refs.grid2.instance.getSelectedRowId() - 1

        this.$http.get('/api/tmp/documentMngNo')
            .then(response => {
              let eaSlipNo = response.data
              this.add_row_sub(this.datad[index]);
              this.datad[this.datad.length-1].eaSlipNo = eaSlipNo
            })


      } catch (e) {
        this.$swal({
          type: 'warning',
          text: e
        })
      }
    },
    buttonClickEventAdd() {
      let dupv = 0
      if (this.data.length > 0 && (this.slipTypeCd !== 'E6' && this.slipTypeCd !== 'E1' && this.slipType !== 'E2' && this.slipType !== 'E5')) {
        var sum = this.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').map(x => x.acctAmt).reduce((a, v) => a + v);
        dupv = this.value.supAmt - sum;
        dupv = dupv < 0 ? 0 : dupv;
      }
      var index = this.$refs.grid.instance.getSelectedRowId();
      if(index){
        this.add_row({
          dcCd: 'D',
          lnTypeCd: 'ITEM',
          postDt: (this.slipType === 'E2' || this.slipType === 'E5') ? this.value.postDt : undefined,
          deptCd: this.value.baseDeptCd,
          deptNm: this.value.baseDeptNm,
          useDt: (this.slipType === 'E6' || this.slipType === 'E1' || this.slipType === 'E2' || this.slipType === 'E5') ? this.value.evdDt : undefined,
          tpsTypeCd: (this.slipType === 'E6' || this.slipType === 'E1')  ? '10' : undefined,
          oilKindCd: (this.slipType === 'E6' || this.slipType === 'E1')  ? 'GSL' : undefined,
          acctAmt: (this.slipType !== 'E6' || this.slipType !== 'E1' || this.slipType !== 'E2' || this.slipType !== 'E5') ? dupv : undefined,
          //detailCd: (this.slipType === 'E2'&& this.baseDeptGrCd === "Y") ? this.sgaProductCd:'',
          //detailNm: (this.slipType === 'E2'&& this.baseDeptGrCd === "Y") ? this.sgaProductCd:'',
          isExpenseFlag: (this.slipType === 'E2'&& this.baseDeptGrCd === "Y") ? true:false,
          acctCd: this.slipType === 'E2' || this.slipType === 'E5' ? this.value.acctCd : undefined,
          acctNm: this.slipType === 'E2' || this.slipType === 'E5' ? this.value.acctNm : undefined,
          acctCdSub: this.slipType === 'E2' || this.slipType === 'E5' ? this.value.acctCdSub : undefined,
          acctNmSub: this.slipType === 'E2' || this.slipType === 'E5' ? this.value.acctNmSub : undefined,
          itemIndex: index,
          rmdAmt : this.value.rmdAmt
        });
      }else{
      this.add_row({
        dcCd: 'D',
        lnTypeCd: 'ITEM',
        postDt: (this.slipType === 'E2' || this.slipType === 'E5') ? this.value.postDt : undefined,
        deptCd: this.value.baseDeptCd,
        deptNm: this.value.baseDeptNm,
        useDt: (this.slipType === 'E6' || this.slipType === 'E1' || this.slipType === 'E2' || this.slipType === 'E5') ? this.value.evdDt : undefined,
        tpsTypeCd: (this.slipType === 'E6' || this.slipType === 'E1')  ? '10' : undefined,
        oilKindCd: (this.slipType === 'E6' || this.slipType === 'E1')  ? 'GSL' : undefined,
        acctAmt: (this.slipType !== 'E6' || this.slipType !== 'E1' || this.slipType !== 'E2' || this.slipType !== 'E5') ? dupv : undefined,
        //detailCd: (this.slipType === 'E2'&& this.baseDeptGrCd === "Y") ? this.sgaProductCd:'',
        //detailNm: (this.slipType === 'E2'&& this.baseDeptGrCd === "Y") ? this.sgaProductCd:'',
        isExpenseFlag: (this.slipType === 'E2'&& this.baseDeptGrCd === "Y") ? true:false,
        acctCd: this.slipType === 'E2' || this.slipType === 'E5' ? this.value.acctCd : undefined,
        acctNm: this.slipType === 'E2' || this.slipType === 'E5' ? this.value.acctNm : undefined,
        acctCdSub: this.slipType === 'E2' || this.slipType === 'E5' ? this.value.acctCdSub : undefined,
        acctNmSub: this.slipType === 'E2' || this.slipType === 'E5' ? this.value.acctNmSub : undefined,
        rmdAmt : this.value.rmdAmt
      });
      }
      this.$refs.grid.instance.callEvent('onGridReconstructed', []);
    },
    buttonClickEventAdd2() {
      this.$http.get('/api/tmp/documentMngNo')
          .then(response => {
            let eaSlipNo = response.data
            this.add_row_sub({
              dcCd: 'D',
              lnTypeCd: 'ITEM',
              eaSlipNo: eaSlipNo,
              readonly: false,
              scanCt: this.evidFileSize,
              eaSlipDt: this.value.evdDt,
              useDt: this.value.evdDt,
              deptCd: (this.slipType === 'E1') ? this.value.baseDeptCd : undefined,
              deptNm: (this.slipType === 'E1') ? this.value.baseDeptNm : undefined,
              detailCd2: '',
              detailNm2: ''
            });
          })
      // this.$refs.grid2.instance.callEvent('onGridReconstructed', []);
    },
    buttonClickEventRemove() {
      try {
        if (this.$refs.grid.instance.getSelectedRowId() == null)
          throw '선택된 행이 없습니다.'
        let index = this.$refs.grid.instance.getSelectedRowId() - 1
        let row = this.value.slipDetails[index]
        let id = [row.dcCd, row.lnTypeCd].join('_')
        if (id !== 'D_ITEM') {
          this.$swal({
            type: 'warning',
            text: '선택하신 행은 삭제할 수 없습니다.'
          })
        } else {
          var size = this.value.slipDetails.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').length
          this.value.slipDetails.splice(index, 1)

          let topAmt = this.value.slipDetails.map(x => x.useAmt).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
          if(this.value.slipDetails2.length === 0) {
            this.$parent.value.totAmt = topAmt;
          } else {
            let botAmt = this.value.slipDetails2.map(x => x.useAmt2).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
            this.$parent.value.totAmt = topAmt + botAmt;
          }


          if (size === 1 && this.value.slipTypeCd !== 'E1' && this.value.slipTypeCd !== 'E2' && this.slipType !== 'E5') {
            this.add_row({
              dcCd: 'D',
              lnTypeCd: 'ITEM',
              useDt: (this.value.slipTypeCd === 'E6') ? this.value.evdDt : undefined,
              tpsTypeCd: (this.value.slipTypeCd === 'E6') ? '10' : undefined,
              oilKindCd: (this.value.slipTypeCd === 'E6') ? 'GSL' : undefined,
              acctAmt: (this.value.slipTypeCd !== 'E6') ? this.value.supAmt : undefined
            })
          }

          this.$refs.grid.instance.callEvent('onGridReconstructed', [])
        }
      } catch (e) {
        if(this.value.slipTypeCd === 'E1') {
          if(this.value.slipDetails.length === 0) {
            if(this.value.slipDetails2.length === 0) {
              this.$parent.value.totAmt = 0
            } else {
              let topAmt = 0;
              let botAmt = this.value.slipDetails2.map(x => x.useAmt2).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
              this.$parent.value.totAmt = topAmt + botAmt;
            }
          } else {
            let topAmt = 0;
            let botAmt = this.value.slipDetails2.map(x => x.useAmt2).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
            this.$parent.value.totAmt = topAmt + botAmt;
          }
        } else {
          if(this.value.slipDetails.length === 0) {
            this.$parent.value.totAmt = 0;
            this.$parent.value.totAmtKrw = 0;
          } else {
            let totAmt = this.value.slipDetails.map(x => x.useAmt).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
            this.$parent.value.totAmt = totAmt;
            this.$parent.value.totAmtKrw = totAmt * this.$parent.value.excRt;
          }
        }
      }
    },
    buttonClickEventRemove2() {
      try {
        const rId = this.$refs.grid2.instance.getSelectedRowId()
        if (rId === null) {
          this.$swal({
            type: 'error',
            text: '삭제할 행을 선택하여주세요.'
          })
        }else{
          // this.$refs.grid2.instance.callEvent('onGridReconstructed', [])
          const rowIndex = this.$refs.grid2.instance.getRowIndex(rId)
          this.datad.splice(rowIndex, 1)
          let topAmt = this.value.slipDetails.map(x => x.useAmt).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
          let botAmt = this.value.slipDetails2.map(x => x.useAmt2).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());

          this.$parent.value.totAmt = topAmt + botAmt;
        }
      } catch (e) {
        if(this.value.slipDetails2.length === 0) {
          if(this.value.slipDetails.length === 0) {
            this.$parent.value.totAmt = 0
          }else {
            let topAmt = this.value.slipDetails.map(x => x.useAmt).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
            let botAmt = 0
            this.$parent.value.totAmt = topAmt + botAmt;
          }
        }else {
          if(this.value.slipDetails.length === 0) {
            let botAmt = this.value.slipDetails2.map(x => x.useAmt2).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
            this.$parent.value.totAmt = botAmt
          }else {
            let topAmt = this.value.slipDetails.map(x => x.useAmt).reduce((a, v) => this.$numeral(a).value() + this.$numeral(v).value());
            let botAmt = 0
            this.$parent.value.totAmt = topAmt + botAmt;
          }
        }
      }
    },
    constructGridSuccessful(grid) {
      var header_style = 'color:#222;text-align:center;'
      var stat_total_text = 'color:red;text-align:right;padding-right:10px !important;'
      let vm = this
      if (this.slipType === 'E6') {
        /**
         * Custom summary function
         */
        grid._in_header_stat_total = function(tag, index, data) {
          let calc = function() {
            let sum = 0
            try {
              sum = vm.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').map(x => x.acctAmt).reduce((a, x) => a + x)
            } catch (e) {
              sum = 0
            }
            return vm.$numeral(sum).format('0,0')
          }
          this._stat_in_header(tag, calc, index, data, data)
        }

        grid.attachHeader(
            ['합계', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#stat_total', '', '', ''],
            [header_style, '', '', '', '', '', '', '', '', stat_total_text, '', '', '']
        )

        grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
        grid.setNumberFormat('0,000', 6, '.', ',')
        grid.setNumberFormat('0,000', 9, '.', ',')
      } else if(this.slipType === 'E1'){
        grid.attachHeader([],[])

        grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
        grid.setNumberFormat('0,000', 13, '.', ',')
        grid.setNumberFormat('0,000', 14, '.', ',')
        grid.setNumberFormat('0,000', 15, '.', ',')
        grid.setNumberFormat('0,000', 24, '.', ',')
        grid.setNumberFormat('0,000', 27, '.', ',')
        grid.setColumnHidden(2, true) //부서코드 히든
        grid.setColumnHidden(5, true) //항목Id 히든
        grid.setColumnHidden(6, true) //항목코드 히든
        grid.setColumnHidden(28, true)
        grid.setColumnHidden(29, true)
        // grid.enableAutoWidth(false)

      } else if(this.slipType === 'E2' || this.slipType === 'E5') {
        //개인비용외 css
        grid.attachHeader([],[])
        grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
        grid.setNumberFormat('0,000', 14, '.', ',')
        grid.setNumberFormat('0,000', 15, '.', ',')
        grid.setColumnHidden(1,true)
        grid.setColumnHidden(4,true)
        grid.setColumnHidden(8,true)
        grid.setColumnHidden(11,true)
        grid.setColumnHidden(16,true)
        grid.setColumnHidden(17,true)
      }else if(this.slipType === 'E5'){
        grid.attachHeader([],[])
        grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
        //grid.setNumberFormat('0,000', 13, '.', ',')
        grid.setColumnHidden(1,true)
        grid.setColumnHidden(4,true)
        grid.setColumnHidden(7,true)
        grid.setColumnHidden(10,true)
        grid.setColumnHidden(14,true)
        grid.setNumberFormat('0,000.000', 13, '.', ',') //2020.08.14 그리드(세부항목) 소수점 변경(E2,E5 분리)
      } else {
        grid._in_header_stat_total = function(tag, index, data) {
          let calc = function() {
            let c = vm.config.columns[index]
            let sum = 0
            if (c !== undefined) {
              try {
                switch (c.id) {
                  case 'debitAmt':
                    sum = vm.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') !== 'C_ITEM').map(x => x.acctAmt).reduce((a, x) => a + x)
                    break
                  case 'creditAmt':
                    sum = vm.data.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'C_ITEM').map(x => x.acctAmt).reduce((a, x) => a + x)
                    break
                }
              } catch (e) {
                sum = 0
              }
            }
            return vm.$numeral(sum).format('0,0')
          }
          this._stat_in_header(tag, calc, index, data, data)
        }

        grid.attachHeader(
            ['합계', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#cspan', '#stat_total', '#stat_total', ''],
            [header_style, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', stat_total_text, stat_total_text, '']
        )
        grid.setNumberFormat('0,000', 12, '.', ',')
        grid.setNumberFormat('0,000', 13, '.', ',')
        grid.setNumberFormat('0,000', 14, '.', ',')
      }
    },
    constructGridSuccessful2(grid) {
      grid.attachHeader([])
      grid.setDateFormat('%Y-%m-%d', '%Y%m%d')
      grid.setNumberFormat('0,000', 12, '.', ',')
      grid.setColumnHidden(2, true)
      grid.setColumnHidden(5, true)
      grid.setColumnHidden(6, true)
      grid.setColumnHidden(17, true)
    },
    cctrValueChange(rId,cInd,nValue){
      const vm = this;
      //계정, 보조계정, 관리항목 초기화 [START]
      this.$refs.grid.data[rId].acctCd = '';
      this.$refs.grid.data[rId].acctNm = '';
      this.$refs.grid.data[rId].acctCdSub = '';
      this.$refs.grid.data[rId].acctNmSub = '';

      for(var i = 1; i <= 15; i++){
        this.$refs.grid.data[rId]['attribute'+i] = '';
      }
      //계정, 보조계정, 관리항목 초기화 [END]
      var schCode = nValue;
      if(schCode){
        this.$http
            .get('/api/cctr/erp/' +  schCode)
            .then(response => {
              if(response.data.length === 1){
                this.$refs.grid.data[rId].deptCd = response.data[0].deptCd
                this.$refs.grid.data[rId].deptNm = response.data[0].deptNm
                //연구소부서 개발 프로젝트 세팅
                if(response.data[0].expenseFlag === "Y"){
                  //this.$refs.grid.data[rId].detailCd= this.sgaProductCd;
                  //this.$refs.grid.data[rId].detailNm= this.sgaProductCd;
                //연구소부서 필수여부 설정
                  this.$refs.grid.data[rId].isExpenseFlag= true;
                }else{
                //연구소부서 필수여부 설정
                  this.$refs.grid.data[rId].isExpenseFlag= false;
                  this.$refs.grid.data[rId].detailCd= '';
                  this.$refs.grid.data[rId].detailNm= '';
                }
              }else{
                  this.$modal.open({
                    component: ErpCctr,
                    props:{searchStr: schCode},
                    parent: this,
                    width: 700,
                    events: {
                      close(object) {
                        vm.data[rId].deptCd = object.deptCd
                        vm.data[rId].deptNm = object.deptNm
                      }
                    }
                  })
              }
            })
      }else{
        this.$refs.grid.cells(rId,3).setValue("");
        this.$refs.grid.cells(rId,4).setValue("");
      }
    },
    acctValueChange(rId,cInd,nValue){
      const vm = this
      //보조계정, 관리항목 초기화 [START]
      this.$refs.grid.data[rId].acctCdSub = '';
      this.$refs.grid.data[rId].acctNmSub = '';

      for(var i = 1; i <= 15; i++){
        this.$refs.grid.data[rId]['attribute'+i] = '';
      }
      //보조계정, 관리항목 초기화 [END]
      var schCode = nValue;

      if(schCode){
        const deptCd =  this.$refs.grid.data[rId].deptCd;
        this.$http.get('/api/account/' + deptCd + '/' + this.slipType + '/' + this.rcpYn +'/' + schCode)
            .then((response) => {
              if(response.data.length === 1){
                this.$refs.grid.data[rId].acctCd = response.data[0].acctCd;
                this.$refs.grid.data[rId].acctNm = response.data[0].acctNm;
                //보조계정 조회
                this.$http.get('/api/account/sub/' + response.data[0].deptCd + '/' + response.data[0].acctCd)
                    .then(responseSub => {
                      var object = responseSub.data;
                      if(object.length === 1){
                        vm.$refs.grid.data[rId].acctCdSub = object[0].subAcctCd;
                        vm.$refs.grid.data[rId].acctNmSub = object[0].subAcctNm;
                        vm.$refs.grid.data[rId].reqSubAcct = true;
                      }else if(object.length == 0){
                        vm.$refs.grid.data[rId].acctCdSub = '';
                        vm.$refs.grid.data[rId].acctNmSub = '';
                        vm.$refs.grid.data[rId].reqSubAcct = false;
                      }else{
                        vm.$refs.grid.data[rId].acctCdSub = '';
                        vm.$refs.grid.data[rId].acctNmSub = '';
                        vm.$modal.open({
                          component: AccountSub,
                          parent: this,
                          props: {
                            deptCd: deptCd,
                            slipTypeCd: vm.slipType,
                            acctCd: response.data[0].acctCd,
                            year: vm.$parent.$parent.value.postDt
                          },
                          width: 700,
                          events: {
                            close(object) {
                              vm.$refs.grid.data[rId].acctCdSub = object.subAcctCd
                              vm.$refs.grid.data[rId].acctNmSub = object.subAcctNm
                              vm.$refs.grid.data[rId].reqSubAcct = true;
                            }
                          }
                        })
                      }
                    });

              }else{
                vm.$modal.open({
                  component: Account,
                  parent: this,
                  props: {
                    deptCd: vm.$refs.grid.data[rId].deptCd,
                    slipTypeCd: vm.slipType,
                    param: schCode,
                  },
                  width: 700,
                  events: {
                    close(object) {
                      vm.$refs.grid.data[rId].acctCd = object.acctCd;
                      vm.$refs.grid.data[rId].acctNm = object.acctNm;
                      vm.$http.get('/api/account/sub/'+ deptCd + '/' + object.acctCd)
                       .then(response => {
                         if(response.data.length === 1){
                           vm.$refs.grid.data[rId].acctCdSub = response.data[0].subAcctCd;
                           vm.$refs.grid.data[rId].acctNmSub = response.data[0].subAcctNm;
                         }else if(response.data.length == 0){
                           // this.$refs.grid.cells(rId,37).setValue(false);
                         }else{
                           vm.$modal.open({
                             component: AccountSub,
                             parent: this,
                             props: {
                               deptCd: deptCd,
                               slipTypeCd: vm.slipType,
                               acctCd: object.acctCd,
                               year: vm.$parent.$parent.data.postDt
                             },
                             width: 700,
                             events: {
                               close(object) {
                                 vm.$refs.grid.data[rId].acctCdSub = object.subAcctCd
                                 vm.$refs.grid.data[rId].acctNmSub = object.subAcctNm
                               }
                             }
                           })
                         }
                       });
                    }
                  }
                })
              }
            })
      }else{
        this.$refs.grid.data[rId].acctCd = '';
        this.$refs.grid.data[rId].acctNm = '';
      }

    },
    subAcctValueChange(rId,cInd,nValue){
      const vm = this;
      const deptCd =  this.$refs.grid.data[rId].deptCd;
      var schCode = nValue;

      if(schCode){

        const acctCd =  this.$refs.grid.data[rId].acctCd;

        if(_.isEmpty(acctCd)){
          this.$swal({
            type: 'warning',
            text: '계정을 먼저 선택하기 바랍니다.'
          });
          return;
        }
        //보조계정 조회
        this.$http.get('/api/account/sub/' + deptCd + '/' + acctCd + '/' + schCode)
            .then(response => {
              var object = response.data;

              if(object.length === 1){
                this.$refs.grid.data[rId].acctCdSub = object[0].subAcctCd;
                this.$refs.grid.data[rId].acctNmSub = object[0].subAcctNm;
              }else{
                vm.$modal.open({
                  component: AccountSub,
                  parent: this,
                  props: {
                    deptCd: deptCd,
                    slipTypeCd: vm.slipType,
                    acctCd: acctCd,
                    searchStr: this.$refs.grid.data[rId].acctNmSub,
                    year: vm.$parent.$parent.data.postDt
                  },
                  width: 700,
                  events: {
                    close(object) {
                      vm.$refs.grid.data[rId].acctCdSub = object.subAcctCd
                      vm.$refs.grid.data[rId].acctNmSub = object.subAcctNm
                    }
                  }
                })
              }
            });

      }else{
        this.$refs.grid.data[rId].acctCdSub = '';
        this.$refs.grid.data[rId].acctNmSub = '';
      }

    },
    productValueChange(rId,cInd,nValue){
      const vm = this;
      const deptCd =  this.$refs.grid.data[rId].deptCd;
      var schCode = nValue;

      if(!this.$refs.grid.data[rId].isExpenseFlag && this.$refs.grid.data[rId].expenseFlag !== 'Y'){
        this.$swal({
          type: 'warning',
          text: '연구부서만 등록 가능합니다.'
        });
        return;
      }

      if(schCode){

        this.$http.get(`/api/slip/product/` + schCode)
            .then(response => {

              if(response.data.length === 1){
                this.$refs.grid.data[rId].detailCd = response.data[0].detailCd;
                this.$refs.grid.data[rId].detailNm = response.data[0].detailNm;
              }else{
                //TODO --> popProduct 어떻게 할건지...?
                // this.$refs.pop.popProduct(this.popup.rId, this.popup.cInd, schCode)
                this.$modal.open({
                  component: Product,
                  parent: this,
                  props: {
                    slipTypeCd: vm.slipType,
                  },
                  width: 700,
                  events: {
                    close(object) {
                      vm.$refs.grid.data[rId].detailCd = object.detailCd
                      vm.$refs.grid.data[rId].detailNm = object.detailNm
                      modalStatus.Product = false;
                    }
                  }
                })

              }
            })

      }else{
        this.$refs.grid.cells(rId,13).setValue("");
        this.$refs.grid.cells(rId,14).setValue("");
      }
    },
    saveExcel() {
      this.downloadExcel(this.$refs.grid);
    },
  },
  watch: {
    'value': {
      immediate: true,
      deep: true,
      handler(value) {
        this.data = value.slipDetails
        this.datad = value.slipDetails2
      }
    }
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
