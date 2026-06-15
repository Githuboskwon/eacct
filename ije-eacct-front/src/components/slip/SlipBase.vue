<template>
  <div v-if="isLoaded">
    <!-- Table area -->
    <slip-table :slipType="data.slipTypeCd" v-model="data" :status="status" @input="$forceUpdate()" v-if="status.loaded" />
    <!-- //Table area -->

    <!-- Grid area -->
    <component :is="gridComponentName" :slipType="data.slipTypeCd" v-model="data" :status="status" @input="$forceUpdate()" v-if="status.loaded" />
    <!-- //Grid area -->

    <!--파일업로드-->
    <div class="file-upload" v-if="showWingsAtchPop && data.slipTypeCd === 'E1'">
      <div class="table-name">
        <h3 class="ico_table_name">그룹웨어 문서</h3>
      </div>
      <div class="file has-name">
        <div class="file-label" @click="openUploadWingsPopup()">
        <span class="file-cta">
          <span class="file-label"> 그룹웨어 문서</span>
          <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
        </span>
          <span class="file-name">{{ this.$numeral(wingsFileSize).format('00') }}<i class="far fa-file-alt"></i></span>
        </div>
      </div>
    </div>
    <div class="file-upload" v-else-if="showWingsAtchPop && (data.slipTypeCd === 'E2' || data.slipTypeCd === 'E3' || data.slipTypeCd === 'E4'|| data.slipTypeCd === 'E5')">
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
  <div v-else></div>
</template>

<script>
import _ from 'lodash'
import assert from '@/libs/assert'
import {
  url as _url
} from '@/libs/join'
import debug from '@/libs/debug'

import SlipTable from '@/components/slip/SlipTable.vue'
import GridED from '@/components/slip/GridED.vue'
import GridRO from '@/components/slip/GridRO.vue'

import EvidAtchPop from '@/components/EvidAtchPop.vue'
import WingsAtchPop from '@/components/JiniAtchPop.vue'

const _debug = debug('SlipBase')

/**
 * Query options
 */
function queryOption(url, type, params) {
  return new Promise((resolve, reject) => {
    // Validate requirement
    assert(url !== undefined, 'URL is required')
    assert(type !== undefined, 'Type is required')

    if (!url.match(/^\/?api.*/g)) {
      url = _url('api', url)
    }

    this.$store.commit('loading')
    this.$http.get(url, {
        params
      })
      .then(response => {
        this.options[type] = response.data
        return resolve(response)
      }).catch(response => {
        return reject(response)
      }).finally(() => {
        this.$store.commit('finish')
      })
  })
}

export default {
  /**
   * Properties include
   */
  props: {
    id: {
      type: String,
      required: false
    },
    eaSlipNo: {
      type: String,
      required: false
    },
    grSlipNo: {
      type: String,
      required: false
    },
    value: {
      type: Object,
      required: false
    },
    status: {
      type: Object,
      required: false,
      default () {
        return {
          new: false,
          loaded: false,
          readonly: false,
          change: false,
          tmpBack: false, //임시 플래그(테스트시만 사용)
          memento: []
        }
      }
    },
    readonly: {
      type: Boolean,
      required: false,
      default: false
    },
    showEvidAtchPop: {
      type: Boolean,
      required: false,
      default: true
    },
    showWingsAtchPop: {
      type: Boolean,
      required: false,
      default: true
    }
  },
  components: {
    SlipTable,
    GridED,
    GridRO
  },
  data() {
    return {
      pData:[],
      data: {},
      tempData: [],
      options: {
        'SLIP_TYPE_CD': [],
        'VENDOR': [],
        'EMP': [],
        'TAXES_CD' : [],
        'TPS_TYPE_CD' : [],
        'VENT_ACCTS' : []
      },
      attachRoles : [],
      evidFileSize: 0,
      wingsFileSize: 0,
      objectPopup: null
    }
  },
  computed: {
    //수정1
    isNew() {
      return this.getId === undefined && this.data.eaSlipNo === undefined && this.data.grSlipNo === undefined
    },
    isLoaded() {
      return this.status.loaded
    },
    gridComponentName() {
      return 'Grid' + (this.isReadonly ? 'RO' : 'ED')
    },
    getId() {
      return this.grSlipNo || this.data.grSlipNo || this.id || this.eaSlipNo || this.data.eaSlipNo
    },
    isReadonly() {
      return this.status.readonly || this.readonly
    }

    // 원본
    // isNew() {
    //   return this.getId === undefined && this.data.eaSlipNo === undefined
    // },
    // isLoaded() {
    //   return this.status.loaded
    // },
    // gridComponentName() {
    //   return 'Grid' + (this.isReadonly ? 'RO' : 'ED')
    // },
    // getId() {
    //   return this.eaSlipNo || this.id
    // },
    // isReadonly() {
    //   return this.status.readonly || this.readonly
    // }
  },
  methods: {
    //세금계산서 조회
    querySlipData() {
      return new Promise((resolve, reject) => {
        if (this.getId === undefined) {
          this.status.loaded = true
          if(this.status.tmpBack === undefined || !this.status.tmpBack){
            this.status.readonly = true
          }else{
            this.status.readonly = false
          }
          return resolve({})
        }
        this.status.loaded = false
        this.$store.commit('loading')
        let slipNo = this.data.grSlipNo ? this.data.grSlipNo : this.getId

        this.$http.get(`/api/slip/${slipNo}`)
          .then(response => {

              let data = response.data.slipHeader
              data.slipDetails = response.data.slipDetails
              data.title = response.data.slipHeader.slipTypeNm + ' 전표'

              if(_.isEmpty(this.pData)){
                data.custAccount = [data.custBnkCd, data.custAcctNo].join('_')
              }else{
                data.custAccount = this.pData.custAcctNo === undefined ? data.custAcctNo : this.pData.custAcctNo
                data.custBnkCd = this.pData.custBnkCd === undefined ? data.custBnkCd : this.pData.custBnkCd
                data.custAcctNo = this.pData.custAcctNo === undefined ? data.custAcctNo : this.pData.custAcctNo
              }

              // 로직 연달아 2번 호출되서 계좌은행명이 2번 붙는 문제
              // if(!_.isEmpty(data.custAcctNo)){ //data.custAcctNo가 null값으로 response 되었을때 아래 로직을 타지 않도록 처리
              //   if(data.custAcctNo.indexOf(")") == -1) data.custAcctNo = data.custAcctNo + ' ('+ data.custBnkCd + ')'
              // }

              _.forEach(data.slipDetails, item => {
                switch (item.dcCd) {
                  case 'C':
                    item.creditAmt = item.acctAmt
                    break
                  case 'D':
                    item.debitAmt = item.acctAmt
                    break
                }
              })

              // 교통비 전표인 경우, 미지급 항목을 제외한다
              // if (data.slipTypeCd === 'E6' || data.slipTypeCd === 'E7') {
              if (data.slipTypeCd === 'E6') {
                data.slipDetails = data.slipDetails.filter(x => [x.dcCd, x.lnTypeCd].join('_') !== 'C_ITEM')
              }

              this.data = data

              let fileEaSlipNo = this.eaSlipNo || this.data.eaSlipNo
              let fileGrSlipNo = this.grSlipNo || this.data.grSlipNo

              this.queryEvidFile(fileEaSlipNo)
              this.queryWingsFile(fileGrSlipNo)
              this.$emit('input', this.data)

              //this.status.readonly = this.data.slipStatCd !== "10"
              if(this.readonly){
                this.status.readonly = this.readonly
              }else if(this.data.slipStatCd !== "10"){
                this.status.readonly = true;
              }

              this.status.change = false
              this.status.loaded = true

              setTimeout(() => {
                this.status.memento = [_.clone(this.data)]
              }, 1500)

              return resolve(this.data)
            })
            .catch(response => {
              return reject(response)
            })
            .finally(() => {
              this.$emit('slipTypeSend', this.data.slipTypeCd)
              this.$store.commit('finish')
            })
      })
    },
    querygrSlipData() {
      return new Promise((resolve, reject) => {
        if (this.getId === undefined) {
          this.status.loaded = true
          if(this.value.slipStatCd === '20'){
            // this.grSlipNo = this.$router.currentRoute.params.grSlipNo
            // this.eaSlipNo = this.$router.currentRoute.params.grSlipNo
            this.data.eaSlipNo = this.$route.params.grSlipNo
            this.status.readonly = true
          }else{
            this.status.readonly = false
            if(!this.$route.params.grSlipNo) return resolve({})
          }
        }
        this.status.loaded = false
        this.$store.commit('loading')
        if(this.data.slipTypeCd === 'E1') {
          let slipNo = this.$route.params.grSlipNo || this.data.grSlipNo
          if(slipNo !== undefined) {

            this.queryTpsTypeCd()
            this.queryTaxes()

            this.$http.get(`/api/slip/p/${slipNo}`)
                .then(response => {

                  let data_gr = response.data.slipGroup;
                  let data_hd_top = response.data.slipHeaders.filter(x => x.evdTypeCd === '10')
                  let data_hd_botm = response.data.slipHeaders.filter(x => x.evdTypeCd === '11')
                  data_gr.empNo = data_gr.wrtId
                  data_gr.empNm = data_gr.wrtNm

                  // data_gr.baseDeptCd = data_gr.wrtDeptCd
                  // data_gr.baseDeptNm = data_gr.wrtDeptNm
                  data_gr.baseDeptCd = response.data.slipHeaders[0].baseDeptCd
                  data_gr.baseDeptNm = response.data.slipHeaders[0].baseDeptNm
                  data_gr.evdTypeCd = '10'
                  if(data_gr.payTermCd === '-') data_gr.payTermCd = '10195'
                  data_gr.payDueDt = response.data.slipHeaders[0].payDueDt
                  data_gr.payText = response.data.slipHeaders[0].payText
                  data_gr.lbltSubAcctCd = response.data.slipHeaders[0].apSubAcctCd

                  this.data.grSlipNo = this.$route.params.grSlipNo ? this.$route.params.grSlipNo : response.data.slipGroup.grSlipNo //얘 넣어주라
                  // this.data = data_gr
                  data_hd_top.forEach((x, index) => {
                    x.itemSeq = index + 1
                    x.eaSlipDt = x.evdDt
                    x.useDt = x.evdDt
                    x.cctrCd = x.wrtDeptCd
                    x.cctrNm = x.wrtDeptNm
                    x.deptCd = x.baseDeptCd
                    x.deptNm = x.baseDeptNm
                    x.expenseId = x.expsId
                    x.expenseAcctCode = x.expsAcctCd
                    x.expenseName = x.expsNm
                    x.expenseAcctName = x.expsAcctNm
                    x.expenseAcctNameSub = x.subAcctNm
                    x.detailNm = x.productNm
                    x.detailCd = x.productCd
                    x.useAmt = x.totAmt
                    x.lnSgtxt = x.hdSgtxt
                    x.scanCt = x.ufileCnt
                    x.crdApprNo = x.apprNo  //카드승인번호
                    x.crdNo = x.crdNo   //카드번호
                    x.crdMerchBizNo = x.merchBizNo  //가맹점 사업자번호
                    x.stptPlc = x.slipDetails[0].stptPlc  //출발지
                    x.dstnPlc = x.slipDetails[0].dstnPlc  //도착지
                    x.biztrpObj = x.slipDetails[0].biztrpObj
                    x.tpsDst = x.slipDetails[0].tpsDst  //거리-교통비
                    x.oilKindCd = x.slipDetails[0].oilKindCd //유종코드-교통비
                    x.oilUpc = x.slipDetails[0].oilUpc  //유류단가-교통비
                    x.oilEff = x.slipDetails[0].oilEff  //연비-교통비
                    x.acctAmt = x.slipDetails[0].acctAmt  //경비금액
                    x.dcCd = 'D'
                    x.dcNm = '차변'
                    x.lnTypeCd = 'ITEM'
                    let tax = this.options['TAXES_CD'].filter(v => v.taxCd === x.taxCd)[0]

                    if(tax !== undefined) {
                      x.vatCd = tax.taxCd
                      x.vatNm = tax.taxNm
                    }

                    let tps = this.options['TPS_TYPE_CD'].filter(v => v.detailCd === x.slipDetails[0].tpsTypeCd)[0]
                    if(tps !== undefined) {
                      x.tpsTypeCd = tps.detailCd  //교통비유형코드-교통비
                    } else {
                      x.tpsTypeCd = '10'
                      x.acctAmt = 0
                      x.tpsDst = 0
                      x.oilKindCd = 'GSL'
                    }
                  })


                  data_hd_botm.forEach((x, index) => {
                    x.itemSeq = index + 1
                    x.evdDt = x.evdDt
                    x.eaSlipDt = x.evdDt
                    x.eaSlipDt2 = x.evdDt
                    x.cctrCd = x.wrtDeptCd
                    x.cctrNm = x.wrtDeptNm
                    x.deptCd = x.baseDeptCd
                    x.deptNm = x.baseDeptNm
                    x.expenseId2 = x.expsId
                    x.expenseAcctCode2 = x.expsAcctCd
                    x.expenseName2 = x.expsNm
                    x.expenseAcctName2 = x.expsAcctNm
                    x.expenseAcctNameSub2 = x.subAcctNm
                    x.detailNm2 = x.productNm
                    x.detailCd2 = x.productCd
                    x.useAmt2 = x.totAmt
                    x.lnSgtxt = x.hdSgtxt
                    x.scanCt = x.scanCt
                    //x.storeNm = x.hdRef5
                    x.evdCustCd2 = x.evdCustCd
                    x.evdCustNm2 = x.evdCustNm
                    x.payCustCd2 = x.payCustCd
                    x.payCustNm2 = x.payCustNm
                    x.address = x.hdRef3
                    x.scanCt = x.ufileCnt
                    x.dcCd = 'D'
                    x.dcNm = '차변'
                    x.lnTypeCd = 'ITEM'
                  })

                  this.data = data_gr
                  this.data.slipDetails = []
                  this.data.slipDetails2 = []

                  data_hd_top.forEach((x) => this.data.slipDetails.push(x))
                  data_hd_botm.forEach((x) => this.data.slipDetails2.push(x))

                  this.queryWingsFile(this.data.grSlipNo);

                  this.$emit('input', this.data)

                  this.status.readonly = this.data.slipStatCd !== "10"
                  this.status.change = false
                  this.status.loaded = true

                  setTimeout(() => {
                    this.status.memento = [_.clone(this.data)]
                  }, 1500)
                  return resolve(this.data)
                })
                .catch(response => {
                  return reject(response)
                })
                .finally(() => {
                  this.$emit('slipTypeSend', this.data.slipTypeCd || 'E1')
                  this.$store.commit('finish')
                })
          }
        } else if(this.data.slipTypeCd === 'E2') {
          let grSlipNo = this.$route.params.grSlipNo ? this.$route.params.grSlipNo : this.value.grSlipNo || this.grSlipNo

          this.$http.get(`/api/slip/np/${grSlipNo}`)
              .then(response => {

                let data_hd = response.data.slip
                let data_dt = response.data.slipDetails

                data_hd.evdTypeCd = '40'

                data_hd.lbltAcctCd = data_hd.liabilityAcctCd
                data_hd.lbltAcctNm = data_hd.liabilityAcctNm
                data_hd.lbltSubAcctCd = data_hd.apSubAcctCd
                data_hd.lbltSubAcctNm = data_hd.apSubAcctNm
                //data_hd.baseDeptCd = data_hd.wrtDeptCd
                //data_hd.baseDeptNm = data_hd.wrtDeptNm
                data_hd.empNo = data_hd.wrtId
                data_hd.empNm = data_hd.wrtNm
                data_hd.preInvoiceNum = data_hd.advReqSlipNo
                data_hd.mrAmt = data_hd.advReqSlipAmt
                data_hd.reUsedSlip = data_hd.reUsedSlip

                let custCdGubun;
                if(!_.isEmpty(data_hd.evdCustCd) && _.isEmpty(data_hd.payCustCd) ) {
                  custCdGubun = data_hd.evdCustCd;
                } else if(!_.isEmpty(data_hd.evdCustCd) && !_.isEmpty(data_hd.payCustCd)) {
                  custCdGubun = data_hd.payCustCd;
                }

                //if(!data_hd.payTermCd && data_hd.evdCustCd) {
                if(!data_hd.payTermCd && custCdGubun) {
                  this.$http.get(`/api/vendor/${this.data.slipTypeCd}/${custCdGubun}`)
                      .then((result) => {
                        data_hd.payTermCd = result.data[0].termsId
                      })
                }

                queryOption.apply(this, [`/vendor/account/${custCdGubun}`, 'VENT_ACCTS'])
                    .then(() => {
                      if (this.options['VENT_ACCTS'].length === 0) {
                        data_hd.custAccount = '_'
                        data_hd.custAcctNo = '== 지급계좌 없음 =='
                      } else {
                        this.options['VENT_ACCTS'] = this.options['VENT_ACCTS'].map(x => {
                          // data_hd.custAccount = x.key = [x.bnkCd, x.acctNo].join('_')
                          // data_hd.custAcctNo = x.value = x.acctNo
                          if(x.acctNo == data_hd.custAcctNo){
                            data_hd.custAccount = [x.bnkCd, x.acctNo].join('_')
                          }
                          // data_hd.custAccount = [data_hd.bnkCd, data_hd.acctNo].join('_')
                          x.key = [x.bnkCd, x.acctNo].join('_')
                          x.value = x.acctNo
                        })
                      }
                    })

                data_dt.forEach(x => {
                  x.itemSeq = x.itemSeq
                  x.dcCd = 'D'
                  x.dcNm = '차변'
                  x.lnTypeCd = 'ITEM'
                  x.deptCd = x.cctrCd
                  x.deptNm = x.cctrNm
                  //비용항목계정명 셋팅해야함.
                  x.detailCd = x.productCd
                  x.detailNm = x.productNm
                  x.vatYn = x.vatYn ? 'Y' : 'N'
                  x.useAmt = x.acctAmt
                  x.lnSgtxt = x.lnSgtxt
                  x.acctCdSub = x.subAcctCd
                  x.acctNmSub = x.subAcctNm

                  delete x.regId
                  delete x.regDtm
                  delete x.chgId
                  delete x.chgDtm
                })

                this.data = data_hd
                this.data.slipDetails = []
                data_dt.forEach(x => this.data.slipDetails.push(x))

                this.queryWingsFile(this.data.grSlipNo)
                this.queryEvidFile(this.data.eaSlipNo)
                this.$emit('input', this.data)

                //this.status.readonly = this.data.slipStatCd !== "10"
                if(this.readonly){
                  this.status.readonly = this.readonly
                }else if(this.data.slipStatCd !== "10"){
                  this.status.readonly = true;
                }

                this.status.change = false
                this.status.loaded = true

                setTimeout(() => {
                  this.status.memento = [_.clone(this.data)]
                  // console.log(this.status.memento)
                }, 1500)

                return resolve(this.data)
              })
              .catch(response => {
                return reject(response)
              })
              .finally(() => {
                this.$emit('slipTypeSend', this.data.slipTypeCd)
                this.$store.commit('finish')
              })

        }else if(this.data.slipTypeCd === 'E5'){
          let grSlipNo = this.$route.params.grSlipNo ? this.$route.params.grSlipNo : this.value.grSlipNo || this.grSlipNo

          this.$http.get(`/api/slip/${grSlipNo}`)
            .then(response => {
              
             _.forEach(response.data.slipDetails, item => {
                  switch (item.dcCd) {
                    case 'C':
                      item.creditAmt = item.acctAmt
                      break
                    case 'D':
                      item.debitAmt = item.acctAmt
                      break
                  }
              })

              let data_hd = response.data.slipHeader
              data_hd.title = response.data.slipHeader.slipTypeNm
              let data_dt = response.data.slipDetails
              this.data = data_hd
              this.data.slipDetails = []
              data_dt.forEach(x => this.data.slipDetails.push(x))
              
              this.queryWingsFile(this.data.grSlipNo)
              this.queryEvidFile(this.data.eaSlipNo)
              this.$emit('input', this.data)

              //this.status.readonly = this.data.slipStatCd !== "10"
              if(this.readonly){
                this.status.readonly = this.readonly
              }else if(this.data.slipStatCd !== "10"){
                this.status.readonly = true;
              }
              
              this.status.change = false
              this.status.loaded = true

              setTimeout(() => {
                this.status.memento = [_.clone(this.data)]
                // console.log(this.status.memento)
              }, 1500)

              return resolve(this.data)
            })
            .catch(response => {
              return reject(response)
            })
            .finally(() => {
              this.$emit('slipTypeSend', this.data.slipTypeCd)
              this.$store.commit('finish')
            })
        }
      })
    },
    queryVentAccts() {
      return queryOption.apply(this, [`/vendor/account/${this.data.evdCustCd}`, 'VENT_ACCTS'])
          .then(() => {
            if (this.options['VENT_ACCTS'].length === 0) {
              this.options['VENT_ACCTS'].push({
                key: '_',
                value: '== 지급계좌 없음 =='
              })
            } else {
              this.options['VENT_ACCTS'] = this.options['VENT_ACCTS'].map(x => {
                x.key = [x.bnkCd, x.acctNo].join('_')
                x.value = x.acctNo
                return x
              })
            }
          })
    },
    queryTaxes() {
      return queryOption.apply(this, ['/taxes', 'TAXES_CD', {
        evdTypeCd: '10'
      }])
    },
    queryTpsTypeCd() {
      return queryOption.apply(this, ['/code/detail', 'TPS_TYPE_CD', {
        groupCd: 'TPS_TYPE_CD'
      }])
    },
    querySlipTypeCd() {
      return queryOption.apply(this, ['/code/detail', 'SLIP_TYPE_CD', {
        groupCd: 'SLIP_TYPE_CD'
      }])
    },
    queryVendor(searchStr) {
      return queryOption.apply(this, [_url(`/vendor/${this.data.slipTypeCd}`, searchStr), 'VENDOR'])
    },
    queryEmp() {
      return queryOption.apply(this, ['/emp', 'EMP'])
    },
    queryEvidFile() {
      /**
       * 증빙파일이 얼마나 첨부되었는지 확인해보자
       */
      if(this.getId) {
        this.$http.get(`/api/evid/fileList/${this.data.eaSlipNo}`)
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
    openUploadEvidencePopup() {
      let vm = this

      let rdoCtrl = true
      let readonly = this.isReadonly
      const authority = this.$store.state.loginInfo.authorities[0].roleCd

      // 전표작성단계가 아니면 수정불가
      if(this.value.slipStatCd !== '10') {
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
      if(this.value.slipStatCd === '20' || this.value.slipStatCd === '30' || this.value.slipStatCd === '50'){
        if(this.data.wrtId === vm.$parent.$store.state.loginInfo.loginId){
          rdoCtrl = false
        }
        let authorityRoleChecked = this.attachRoles.filter(x=>x.detailCd === authority);
        if(authorityRoleChecked.length > 0){
          rdoCtrl = false
        }
        // if(authority === 'ADMIN' || authority === 'F_USER'){
        //   rdoCtrl = false
        // }
      }
      
      let url = '/evidAtchPopModeless?docMngNo=' + this.data.eaSlipNo + '&readonly='+ readonly + '&readonlyCtrl='+ rdoCtrl;

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

      // this.$modal.open({
      //   component: EvidAtchPop,
      //   props: {
      //     docMngNo: this.data.eaSlipNo,
      //     readonly: readonly, // Check readonly
      //     readonlyCtrl: rdoCtrl
      //   },
      //   parent: this,
      //   width: 1200,
      //   events: {
      //     close(value) {
      //       vm.evidFileSize = value.length
      //     }
      //   }
      // })
    },
    openUploadWingsPopup(){

      let vm = this

      let chkReadOnly = this.isReadonly

      const authority = this.$store.state.loginInfo.authorities[0].roleCd
      /*
        - 작성자 & 재무팀 후첨 -
        결재완료 상태까지 작성자와 재무팀/경영관리팀은 후첨 가능
      */
      if(this.value.slipStatCd === '20' || this.value.slipStatCd === '30' || this.value.slipStatCd === '50'){
        if(this.data.wrtId === vm.$parent.$store.state.loginInfo.loginId){
          chkReadOnly = false
        }

        let authorityRoleChecked = this.attachRoles.filter(x=>x.detailCd === authority);
        if(authorityRoleChecked.length > 0){
          chkReadOnly = false
        }
      }

      var propGrslipNo = this.getId;
      if(this.$parent.copyEaSlipNo !== undefined) propGrslipNo = this.data.grSlipNo

      if(this.$parent.copyEaSlipNo !== undefined && this.data.grSlipNo !== undefined && this.data.grSlipNo.substring(0,3) === 'TMP'){
        this.$store.commit('loading');
        this.$http.get(`/api/wings/document/${this.$route.params.copyEaSlipNo}`)
            .then(response => {
              if(response.data) {
                // this.tempData = response.data
                this.tempData = response.data.map(x => {
                  x.grSlipNo = this.data.grSlipNo
                  delete x.documentId
                  return x
                })
              }

            }).catch(response => {

        }).finally(() => {
          this.$store.commit('finish');
          this.$modal.open({
            component: WingsAtchPop,
            props: {
              grSlipNo: propGrslipNo,
              slipDetail: this.tempData,
              readonly: chkReadOnly // Check readonly
            },
            parent: this,
            width: 1200,
            events: {
              close(value) {
                //신규작성 - 임시번호 전달
                if(!vm.data.grSlipNo){
                  vm.data.grSlipNo = value[0].grSlipNo
                }
                vm.wingsFileSize = value.length
                vm.tempData = value
              }
            }
          })

        })
      }
      else{
        this.$modal.open({
          component: WingsAtchPop,
          props: {
            grSlipNo: propGrslipNo,
            slipDetail: this.tempData,
            readonly: chkReadOnly // Check readonly
          },
          parent: this,
          width: 1200,
          events: {
            close(value) {
              //신규작성 - 임시번호 전달
              if(!vm.data.grSlipNo){
                vm.data.grSlipNo = value[0].grSlipNo
              }
              vm.wingsFileSize = value.length
              vm.tempData = value
            }
          }
        })
      }
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
  },
  destroyed() {
    if(this.objectPopup !== null){
      this.objectPopup.close()
    }
  },
  mounted() {
    // Get login info
    let l = {}
    try {
      l = this.$store.state.loginInfo
    } catch (e) {
      l = {}
    }
    // Check is new
    if (this.getId || this.data.eaSlipNo !== undefined || this.data.grSlipNo !== undefined) {
      this.status.new = false
    } else {
      this.status.new = true
    }
    // if has copy code
    if (this.$route.params.copyEaSlipNo !== undefined) {
      console.log("전표복사 버튼 클릭 이후 :", this.data.slipTypeCd, 'copy : ',this.$route.params.copyEaSlipNo)
      switch(this.data.slipTypeCd) {
        case 'E1':
          this.$http.get(_url('/api/slip/p/copy', this.$route.params.copyEaSlipNo))
              .then(response => {
                let data_gr = response.data.slipGroup;
                let data_hd_top = response.data.slipHeaders.filter(x => x.evdTypeCd === '10')
                let data_hd_botm = response.data.slipHeaders.filter(x => x.evdTypeCd === '11')
                data_gr.eaSlipDt = data_gr.postDt
                data_gr.baseDeptCd = response.data.slipHeaders[0].baseDeptCd
                data_gr.baseDeptNm = response.data.slipHeaders[0].baseDeptNm
                data_gr.evdTypeCd = '10'
                if(data_gr.payTermCd === '-') data_gr.payTermCd = '10195'
                data_hd_top.forEach((x, index) => {
                  x.itemSeq = index + 1
                  x.eaSlipDt = x.evdDt
                  x.useDt = x.evdDt
                  x.cctrCd = x.wrtDeptCd
                  x.cctrNm = x.wrtDeptNm
                  x.deptCd = x.baseDeptCd
                  x.deptNm = x.baseDeptNm
                  x.expenseId = x.expsId
                  x.expenseAcctCode = x.expsAcctCd
                  x.expenseName = x.expsNm
                  x.expenseAcctName = x.expsAcctNm
                  x.detailNm = x.productNm
                  x.detailCd = x.productCd
                  x.useAmt = x.totAmt
                  x.lnSgtxt = x.hdSgtxt
                  // x.vatCd = x.vatTaxCd
                  x.crdNo = x.cardNo
                  x.stptPlc = x.stptPlc  //출발지
                  x.dstnPlc = x.dstnPlc  //도착지
                  x.biztrpObj = x.biztrpObj

                  x.tpsDst = x.tpsDst  //거리-교통비
                  x.oilKindCd = x.oilKindCd //유종코드-교통비
                  x.oilUpc = x.oilUpc  //유류단가-교통비
                  x.oilEff = x.oilEff  //연비-교통비
                  x.acctAmt = x.acctAmt  //경비금액
                  x.dcCd = 'D'
                  x.lnTypeCd = 'ITEM'
                  let tax = this.options['TAXES_CD'].filter(v => v.taxCd === x.vatTaxCd)[0]
                  if(tax !== undefined) {
                    x.vatCd = tax.taxCd
                    x.vatNm = tax.taxNm
                  }
                  let tps = this.options['TPS_TYPE_CD'].filter(v => v.detailCd === x.tpsTypeCd)[0]
                  if(tps !== undefined) {
                    x.tpsTypeCd = tps.detailCd  //교통비유형코드-교통비
                  } else {
                    x.tpsTypeCd = '10'
                    x.acctAmt = 0
                    x.tpsDst = 0
                    x.oilKindCd = 'GSL'
                  }
                })


                data_hd_botm.forEach((x, index) => {
                  x.itemSeq = index + 1
                  x.evdDt = x.evdDt
                  x.eaSlipDt = x.evdDt
                  x.eaSlipDt2 = x.evdDt
                  x.cctrCd = x.wrtDeptCd
                  x.cctrNm = x.wrtDeptNm
                  x.deptCd = x.baseDeptCd
                  x.deptNm = x.baseDeptNm
                  x.expenseId2 = x.expsId
                  x.expenseAcctCode2 = x.expsAcctCd
                  x.expenseName2 = x.expsNm
                  x.expenseAcctName2 = x.expsAcctNm
                  x.detailNm2 = x.productNm
                  x.detailCd2 = x.productCd
                  x.useAmt2 = x.totAmt
                  x.lnSgtxt = x.hdSgtxt
                  x.scanCt = x.scanCt
                  //x.storeNm = x.hdRef5
                  x.evdCustCd2 = x.evdCustCd
                  x.evdCustNm2 = x.evdCustNm
                  x.payCustCd2 = x.payCustCd
                  x.payCustNm2 = x.payCustNm
                  x.address = x.hdRef3
                  x.dcCd = 'D'
                  x.lnTypeCd = 'ITEM'
                })

                this.data = data_gr
                this.data.slipDetails = []
                this.data.slipDetails2 = []

                data_hd_top.forEach((x) => {
                  //스캔증빙이 라인에 추가되면서 같이 임시번호 채번해줘야함
                  this.$http.get('/api/tmp/documentMngNo')
                      .then(response => {
                        x.eaSlipNo = response.data
                      }).finally(() => {
                    this.data.slipDetails.push(x)
                  })
                })
                data_hd_botm.forEach((x) => {
                  // 임시번호 채번
                  this.$http.get('/api/tmp/documentMngNo')
                      .then(response => {
                        x.eaSlipNo = response.data
                      }).finally(() => {
                    this.data.slipDetails2.push(x)
                  })
                })

                this.$emit('input', this.data)

                this.status.change = true
                this.status.loaded = true
                this.status.readonly = false

                setTimeout(() => {
                  this.status.memento = [_.clone(this.data)]
                }, 1500)

              })
          break
        case 'E2':
          this.$http.get(_url('/api/slip/np/copy', this.$route.params.copyEaSlipNo))
              .then(response => {
                let data_hd = response.data.slip
                let data_dt = response.data.slipDetails

                data_hd.evdTypeCd = '40'
                data_hd.evdCustNm = data_hd.evdCustNm
                data_hd.evdCustCd = data_hd.evdCustCd
                data_hd.payCustNm = data_hd.payCustNm
                data_hd.payCustCd = data_hd.payCustCd
                data_hd.lbltAcctCd = data_hd.liabilityAcctCd
                data_hd.lbltAcctNm = data_hd.liabilityAcctNm
                //data_hd.baseDeptCd = data_hd.wrtDeptCd
                //data_hd.baseDeptNm = data_hd.wrtDeptNm
                data_hd.empNo = data_hd.wrtId
                data_hd.empNm = data_hd.wrtNm
                data_hd.preInvoiceNum = data_hd.advReqSlipNo
                data_hd.mrAmt = data_hd.advReqSlipAmt
                queryOption.apply(this, [`/vendor/account/${data_hd.evdCustCd}`, 'VENT_ACCTS'])
                    .then(() => {
                      if (this.options['VENT_ACCTS'].length === 0) {
                        data_hd.custAccount = '_'
                        data_hd.custAcctNo = '== 지급계좌 없음 =='
                      } else {
                        this.options['VENT_ACCTS'] = this.options['VENT_ACCTS'].map(x => {
                          data_hd.custAccount = x.key = [x.bnkCd, x.acctNo].join('_')
                          data_hd.custAcctNo = x.value = x.acctNo
                        })
                      }
                    })

                data_dt.forEach(x => {
                  x.itemSeq = x.itemSeq
                  x.dcCd = 'D'
                  x.dcNm = '차변'
                  x.lnTypeCd = 'ITEM'
                  x.deptCd = x.cctrCd
                  x.deptNm = x.cctrNm
                  //비용항목계정명 셋팅해야함.
                  x.detailCd = x.productCd
                  x.detailNm = x.productNm
                  x.vatYn = x.vatYn ? 'Y' : 'N'
                  x.useAmt = x.acctAmt
                  x.lnSgtxt = x.lnSgtxt

                  delete x.regId
                  delete x.regDtm
                  delete x.chgId
                  delete x.chgDtm
                })
                this.data.grSlipNo = data_hd.grSlipNo
                data_hd.eaSlipNo = data_hd.grSlipNo
                this.data = data_hd
                this.data.slipDetails = []
                data_dt.forEach(x => this.data.slipDetails.push(x))

                this.$emit('input', this.data)

                this.status.readonly = this.data.slipStatCd !== "10"

                this.status.change = false
                this.status.loaded = true

                setTimeout(() => {
                  this.status.memento = [_.clone(this.data)]
                }, 1500)

              })
          break
        default:
          this.$http.get(_url('/api/slip/copy', this.$route.params.copyEaSlipNo))
              .then(response => {
                let data = response.data.slipHeader
                data.slipDetails = response.data.slipDetails
                data.title = response.data.slipHeader.slipTypeNm + ' 전표'

                data.custAccount = [data.custBnkCd, data.custAcctNo].join('_')

                _.forEach(data.slipDetails, item => {
                  switch (item.dcCd) {
                    case 'C':
                      item.creditAmt = item.acctAmt
                      break
                    case 'D':
                      item.debitAmt = item.acctAmt
                      break
                  }
                })

                // 교통비 전표인 경우, 미지급 항목을 제외한다
                // if (data.slipTypeCd === 'E6' || data.slipTypeCd === 'E7') {
                if (data.slipTypeCd === 'E6') {
                  data.slipDetails = data.slipDetails.filter(x => [x.dcCd, x.lnTypeCd].join('_') !== 'C_ITEM')
                }

                // 임시번호 채번
                this.$http.get('/api/tmp/documentMngNo')
                    .then(response => {
                      data.eaSlipNo = response.data

                      this.data = data

                      this.$emit('input', this.data)
                      this.queryEvidFile(this.data.eaSlipNo)

                      this.status.change = false
                      this.status.loaded = true
                      this.status.readonly = false

                      setTimeout(() => {
                        this.status.memento = [_.clone(this.data)]
                      }, 1500)
                    })
              })
          break
      }

    }
    // if is new
    else if (this.isNew) {
      this.status.readonly = this.readonly || false

      // 임시번호 채번
      this.$http.get(`/api/tmp/documentMngNo`)
          .then(response => {
            this.data.eaSlipNo = response.data
            this.queryEvidFile(response.data)
          })

      // 기타전표 또는 교통비전표일때
      // if (this.data.slipTypeCd === 'E4' || this.data.slipTypeCd === 'E6' || this.data.slipTypeCd === 'E7') {
      if (this.data.slipTypeCd === 'E6') {
        Promise.all([this.querySlipTypeCd(), this.queryVendor(l.loginId), this.queryEmp()])
            .then(() => {
              let slip = this.options['SLIP_TYPE_CD'].filter(x => x.detailCd === this.data.slipTypeCd)[0]
              // Has found
              if (slip !== undefined) {
                this.data.bpCd = slip.remark1
              }

              // has found vendor info
              if (this.options['VENDOR'].length === 1) {
                let vendor = this.options['VENDOR'][0]

                this.data.evdCustCd = vendor.vendCd
                this.data.evdCustNm = vendor.vendNm
                this.data.evdCustBizNo = vendor.bizNo

                let emp = this.options['EMP'].filter(x => x.empNo === vendor.bizNo)[0]
                if (emp !== undefined) {
                  this.data.baseDeptCd = emp.cctrCd
                  this.data.baseDeptNm = emp.cctrNm
                }
              }
              this.status.loaded = true
              setTimeout(() => {
                this.status.memento = [_.clone(this.data)]
              }, 1500)
            })
      }
      //개인경비 신청/ 외신청/ 일괄비용 일 때
      else if(this.data.slipTypeCd === 'E1' || this.data.slipTypeCd === 'E2' || this.data.slipTypeCd === 'E5'){
        if(this.$route.params.grSlipNo !== undefined && !this.isReadonly) {
          Promise.all([this.queryTaxes(), this.queryTpsTypeCd()])
              .then(() => {
                this.status.change = true
                this.status.loaded = true
                this.status.readonly = false
              })
        } else {
          Promise.all([this.querySlipTypeCd(), this.queryVendor(l.loginId)])
              .then(() => {
                let slip = this.options['SLIP_TYPE_CD'].filter(x => x.detailCd === this.data.slipTypeCd)[0]
                // Has found
                if (slip !== undefined) {
                  const valueArr = slip.remark1.split(",");
                  if (valueArr.length === 2) {
                    this.data.bpCd = valueArr[0]
                  }
                }
                // has found vendor info
                if (this.options['VENDOR'].length === 1) {
                  let vendor = this.options['VENDOR'][0]

                  this.data.evdCustCd = vendor.vendCd
                  this.data.evdCustNm = vendor.vendNm
                  this.data.evdCustBizNo = vendor.bizNo

                  let emp = this.options['EMP'].filter(x => x.empNo === vendor.bizNo)[0]
                  if (emp !== undefined) {
                    this.data.baseDeptCd = emp.cctrCd
                    this.data.baseDeptNm = emp.cctrNm
                  }
                }
                this.status.loaded = true
                setTimeout(() => {
                  this.status.memento = [_.clone(this.data)]
                }, 1500)
              })
        }
      }
    }
    // if isEditable and data is not set
    else if (this.data.eaSlipNo === undefined) {
      if(this.data.slipTypeCd === 'E1' || this.data.slipTypeCd === 'E2' || this.data.slipTypeCd === 'E5') {
        this.querygrSlipData()
      }
      else {
        this.querySlipData()
      }
      // if(this.$router.currentRoute.params.grSlipNo === 'GR') {
      // }else{
      // this.querySlipData()
      // this.queryEvidFile(this.getId)
      // }

    }
    // else
    else {
      if(this.data.slipTypeCd === 'E1' || this.data.slipTypeCd === 'E2' || this.data.slipTypeCd === 'E5') {
        this.querygrSlipData(this.data.grSlipNo)
      } else {
        this.querySlipData()
      }
      this.queryEvidFile(this.data.eaSlipNo)
    }
  },
  created(){
    this.setAttachRoles();
  },
  watch: {
    'value': {
      immediate: true,
      deep: true,
      handler(value) {
        this.pData = value
        this.data = value || {}
      }
    },
    'data': {
      deep: true,
      handler(value) {
        if (this.status.memento.length === 0) {
          this.status.memento.unshift(_.clone(value))
        } else {
          let c = JSON.stringify(this.status.memento[0])
          let v = JSON.stringify(v)
          if (c !== v) {
            this.status.memento.unshift(_.clone(value))
          }
        }
      }
    },
    'status.readonly'() {
      switch(this.data.slipTypeCd) {
        case 'E1':
        case 'E2':
        case 'E5':
          this.querygrSlipData()
          break
        default:
          this.querySlipData()
          break
      }
    },
  }
}
</script>

<style lang="scss" scoped>

</style>
