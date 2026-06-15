// Import components
import _ from 'lodash'
import {
  url as _url
} from '@/libs/join'
import assert from '@/libs/assert'
import debug from '@/libs/debug'

import SlipBase from '@/components/slip/SlipBase.vue'
import ApprSubm from '@/views/ApprSubm.vue'
import ApprDtlQryPop from '@/views/ApprDtlQryPop.vue'
import ApprBundleSubm from '@/views/ApprBundleSubm.vue'
import ApprBundleSubmSlip from '@/views/ApprBundleSubmSlip.vue'

const _debug = debug('slip-basic')

function queryBudget(postDt, cctrCd, acctCd, subAcctCd) {
  return this.$http.get('/api/budget/remain', {
    params: {
      postDt,
      cctrCd,
      acctCd,
      subAcctCd
    }
  })
}

export default {
  props: {
    grSlipNo: {
      type: String,
      required: false
    },
    eaSlipNo: {
      type: String,
      required: false
    },
    copyEaSlipNo: {
      type: String,
      required: false
    },
    fromSlipLst:{
      type: Boolean,
      required: false
    },
  },
  components: {
    SlipBase,
    ApprSubm,
    ApprDtlQryPop,
    ApprBundleSubm,
    ApprBundleSubmSlip
  },
  data() {
    return {
      status: {
        new: true,
        loaded: false,
        readonly: false,
        change: false,
        memento: []
      }
    }
  },
  computed: {
    /**
     * 신규 전표 여부
     */
    isNew() {
      if(this.grSlipNo || this.data.grSlipNo) {
        let grNo = this.grSlipNo || this.data.grSlipNo
        if(grNo.substr(0, 3) === 'TMP') {
          return true
        } else {
          // return grNo === undefined
          return false
        }
      } else {
        return this.eaSlipNo === undefined
      }

    },
    isReadOnly() {
      return this.status.readonly
    },
    /**
     * 결재상신 가능 여부
     */
    isSubmitable() {
      return !this.isNew && !this.status.readonly
    },
    /**
     * 신규작성 가능 여부
     */
    isCreateable() {
      return this.isNew || !this.status.readonly
    },
    /**
     * 삭제 가능 여부
     */
    isDeleteable() {
      return !this.isNew && !this.status.readonly
    },
    /**
     * 저장 가능 여부
     */
    isSaveable() {
      return this.isNew || !this.status.readonly
    }
  },
  methods: {
    /**
     * 데이터 검증
     */
    validate() {
      return new Promise(async (resolve, reject) => {

        // _debug.apply(this, ['validate', 'Begin validate'])

        var data = this.data
        var messages = {
          evdCustCd: {
            E4: '지급처를 선택해주세요.',
            E6: '지급처를 선택해주세요.'
          }
        }

        try {
          /**
           * 헤더에 대한 Validation
           */

          if(data.slipTypeCd !== 'E1' && data.slipTypeCd !== 'E2' && data.slipTypeCd !== 'E5'){
            assert.apply(this, [data.evdTypeCd, '증빙유형을 선택해 주세요.'])
            assert.apply(this, [data.stlFgCd, '정산구분을 선택해 주세요.'])
            assert.apply(this, [data.postDt, '회계일자를 선택해 주세요.'])

            assert.apply(this, [data.evdCustCd, messages['evdCustCd'][data.slipTypeCd] ? messages['evdCustCd'][data.slipTypeCd] : '거래처를 선택해 주세요.'])
            assert.apply(this, [data.payDueDt, '지급예정일을 선택해 주세요.'])
            assert.apply(this, [data.custAccount, '지급계좌를 선택해 주세요.'])
            assert.apply(this, [data.hdSgtxt, '적요를 입력해 주세요.'])
            assert.apply(this, [data.baseDeptCd, '기준부서를 선택해 주세요.'])
          }

          if (data.slipTypeCd !== 'E6' && data.slipTypeCd !== 'E1' && data.slipTypeCd !== 'E2' && data.slipTypeCd !== 'E5') {
            assert.apply(this, [this.$numeral(data.supAmt).value(), '공급가액을 입력해 주세요.'])
          }

          if(data.slipTypeCd !== 'E2' && data.slipTypeCd !== 'E5'){
            switch (data.evdTypeCd) {
              case '40':
                assert.apply(this, [data.crcptMerchNm, '상호명을 입력해 주세요.'])
                assert.apply(this, [data.crcptBizNo, '사업자번호를 입력해 주세요.'])
                assert.apply(this, [data.crcptNo, '승인번호를 입력해 주세요.'])
                break
            }
          }

          /**
           * Detail에 대한 Validation
           */
          // 교통비만 아니면 차변/대변 일치여부 검증
          // if (data.slipTypeCd !== 'E6' || data.slipTypeCd !== 'E7') {

          if (data.slipTypeCd !== 'E6' && data.slipTypeCd !== 'E1') {
            // 차변금액 & 대변금액 일치 여부 확인
            var creditAmt = data.slipDetails.map(x => x.creditAmt || 0).reduce((acc, cur) => acc + cur)
            var debitAmt = data.slipDetails.map(x => x.debitAmt || 0).reduce((acc, cur) => acc + cur)
            assert.apply(this, [creditAmt === debitAmt, '차변금액과 대변금액이 일치하지 않습니다.'])
          }

          //개인비용(경비)신청서
          if(data.slipTypeCd === 'E1') {
            if(_.isEmpty(data.hdSgtxt)){
              assert.apply(this, [data.hdSgtxt, '적요를 입력해 주세요.'])
            }

            if(_.isEmpty(data.payTermCd)){
              assert.apply(this, [data.payTermCd, '지급조건을 입력해 주세요.'])
            }
          }

          //개인비용(경비)외 신청서 또는 일괄비용
          if (data.slipTypeCd === 'E2' || data.slipTypeCd === 'E5') {
            if(data.stlFgCd === 'K') assert.apply(this, [data.mrAmt, '선급금정산 금액이 없습니다.'])
            assert.apply(this, [data.totAmt, '총 금액이 없습니다.'])
            assert.apply(this, [data.excDt, '환율일자를 선택해 주세요.'])
            assert.apply(this, [data.evdCustCd, '공급자를 입력하여 주세요.'])
            assert.apply(this, [data.hdSgtxt, '적요를 입력해주세요.'])
          }
          // 교통비인경우
          // if (data.slipTypeCd === 'E6' || data.slipTypeCd === 'E7') {
          if (data.slipTypeCd === 'E6') {
            var a = data.slipDetails.map(x => this.$moment(x.useDt).format('YYYYMM')).filter((x, i, arr) => arr.indexOf(x) === i)
            assert.apply(this, [a.length === 1, '사용일자가 다른 월에 있습니다.'])
          }

          if(data.slipTypeCd === 'E1') {
            // 디테일한 검증
            //2020.04.13 적요 필수 체크 추가
            assert.apply(this, [!(data.slipDetails.length < 1 && data.slipDetails2.length < 1), `저장할 데이터가 존재하지 않습니다.`])

            if(data.slipDetails.length !== undefined && data.slipDetails.length > 0) {

              data.slipDetails.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').forEach((v ,i) => {

                assert.apply(this, [v.acctCd, `법인카드지급 No.${i + 1}의 계정을 선택해 주세요.`])
                //연구소부서인 경우
                if (v.isExpenseFlag === true || v.expenseFlag === 'Y') {
                  assert.apply(this, [v.detailCd, `법인카드지급 No.${i + 1}의 개발 프로젝트를 입력해 주세요.`])
                }
                assert.apply(this, [v.lnSgtxt, `법인카드지급 No.${i + 1}의 적요를 입력해 주세요.`])

              })

              //보조계정 입력 체크
              data.slipDetails.forEach((v,i) =>{
                if(v.reqSubAcct && !v.acctCdSub){
                  assert.apply(this, [v.acctCdSub, `법인카드지급 No.${i + 1}의 보조계정을 입력해 주세요.`])
                }
              })

            }

            if(data.slipDetails2 !== undefined && data.slipDetails2.length > 0) {
              data.slipDetails2.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').forEach((v,i) => {

                assert.apply(this, [v.acctCd2, `현금지급 No.${i + 1}의 계정을 선택해 주세요.`])
                assert.apply(this, [v.useAmt2, `현금지급 No.${i + 1}의 금액을 선택해 주세요.`])
                //연구소부서인 경우
                if (v.isExpenseFlag === true || v.expenseFlag === 'Y') {
                  assert.apply(this, [v.detailCd2, `현금지급 No.${i + 1}의 개발 프로젝트를 선택해 주세요.`])
                }
                assert.apply(this, [v.eaSlipDt, `현금지급 No.${i + 1}의 증빙일자를 선택해 주세요.`])
                assert.apply(this, [v.lnSgtxt, `현금지급 No.${i + 1}의 적요를 입력해 주세요.`])
                assert.apply(this, [v.evdCustCd2, `현금지급 No.${i + 1}의 가맹점을 입력해 주세요.`])
                assert.apply(this, [v.payCustCd2, `현금지급 No.${i + 1}의 지급처를 입력해 주세요.`])

              })

              //보조계정 입력 체크
              data.slipDetails2.forEach((v,i) =>{
                if(v.reqSubAcct && !v.acctCdSub2){
                  assert.apply(this, [v.acctCdSub2, `현금지급 No.${i + 1}의 보조계정을 입력해 주세요.`])
                }
              })
            }
          } else {
            // 디테일한 검증
            data.slipDetails.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').forEach((v,i) => {
              // 공통 체크 부분
              switch (data.slipTypeCd) {
                case 'E1':  //기존 E7이였음 테스트용
                  assert.apply(this, [v.cctrCd, `No.${i + 1}의 부서를 입력해 주세요.`])
                  // assert.apply(this, [v.stptPlc, `No.${v.itemSeq}의 출발지를 입력해 주세요.`])
                  // assert.apply(this, [v.dstnPlc, `No.${v.itemSeq}의 도착지를 입력해 주세요.`])
                  // assert.apply(this, [v.biztrpObj, `No.${v.itemSeq}의 출장목적을 입력해 주세요.`])
                  if (v.tpsTypeCd === '10') {
                    // assert.apply(this, [v.tpsDst, `No.${v.itemSeq}의 거리를 입력해 주세요.`])
                    // assert.apply(this, [v.oilKindCd, `No.${v.itemSeq}의 유종을 입력해 주세요.`])
                    // assert.apply(this, [v.oilUpc && v.oilEff, `${this.$moment(v.useDt).format('YYYY-MM')}의 유류단가/연비가 없습니다.`])
                  }
                  // assert.apply(this, [v.acctAmt, `No.${v.itemSeq}의 사용금액이 0원입니다.`])
                  break;
                case 'E2':
                  assert.apply(this, [v.deptCd, `No.${v.itemSeq}의 비용부서를 선택해 주세요.`])
                  assert.apply(this, [v.acctCd, `No.${v.itemSeq}의 계정항목을 선택해 주세요.`])
                  //assert.apply(this, [v.detailCd, `No.${v.itemSeq}의 제품을 선택해 주세요.`])
                  //보조계정 입력 체크
                  var reqSubAcct = v.reqSubAcct;

                  if(reqSubAcct){
                    var strSubAcctCd = v.acctCdSub;
                    var strSubAcctNm = v.acctNmSub;

                    if(!strSubAcctCd || !strSubAcctNm){
                      assert.apply(this, [null, `No.${v.itemSeq}의 보조계정을 입력해 주세요.`])

                    }
                  }
                  var isExpenseFlag = v.isExpenseFlag;
                  if(isExpenseFlag){
                    assert.apply(this, [v.detailCd, `No.${v.itemSeq}의 개발 프로젝트를 입력해 주세요.`])
                  }
                  assert.apply(this, [v.useAmt, `No.${v.itemSeq}의 금액은 필수 값 입니다.`])
                  assert.apply(this, [v.lnSgtxt, `No.${v.itemSeq}의 적요를 작성해 주세요.`])
                  break;
                case 'E5':
                    assert.apply(this, [v.deptCd, `No.${v.itemSeq}의 비용부서를 선택해 주세요.`])
                    assert.apply(this, [v.acctCd, `No.${v.itemSeq}의 계정항목을 선택해 주세요.`])
                    //assert.apply(this, [v.detailCd, `No.${v.itemSeq}의 제품을 선택해 주세요.`])
                    assert.apply(this, [v.detailCd, `No.${v.itemSeq}의 개발 프로젝트를 입력해 주세요.`])
                    assert.apply(this, [v.useAmt, `No.${v.itemSeq}의 금액은 필수 값 입니다.`])
                    assert.apply(this, [v.lnSgtxt, `No.${v.itemSeq}의 적요를 작성해 주세요.`])
                  break;
                case 'E3':
                case 'E4':
                    assert.apply(this, [v.cctrCd, `No.${v.itemSeq}의 귀속부서를 선택해 주세요.`])
                  break
                case 'E6':
                  assert.apply(this, [v.acctCd, `No.${v.itemSeq}의 계정과목을 선택해 주세요.`])
                  assert.apply(this, [v.stptPlc, `No.${v.itemSeq}의 출발지를 입력해 주세요.`])
                  assert.apply(this, [v.dstnPlc, `No.${v.itemSeq}의 도착지를 입력해 주세요.`])
                  assert.apply(this, [v.biztrpObj, `No.${v.itemSeq}의 출장목적을 입력해 주세요.`])
                  if (v.tpsTypeCd === '10') {
                    assert.apply(this, [v.tpsDst, `No.${v.itemSeq}의 거리를 입력해 주세요.`])
                    assert.apply(this, [v.oilKindCd, `No.${v.itemSeq}의 유종을 입력해 주세요.`])
                    assert.apply(this, [v.oilUpc && v.oilEff, `${this.$moment(v.useDt).format('YYYY-MM')}의 유류단가/연비가 없습니다.`])
                  }
                  assert.apply(this, [v.acctAmt, `No.${v.itemSeq}의 사용금액이 0원입니다.`])
                  break
                default:
                  assert.apply(this, [v.cctrCd, `No.${v.itemSeq}의 귀속부서를 선택해 주세요.`])
                  break
              }
            })
          }

          /* 알피니언에서 제외처리
          if(data.slipDetails !== undefined) {
            // 예산 검증
            let budget = data.slipDetails.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM').map(x => {
              return {
                id: [data.postDt, x.deptCd, x.acctCd].join('_'),
                postDt: data.postDt,
                cctrCd: x.deptCd,
                cctrNm: x.deptNm,
                acctCd: x.acctCd,
                acctNm: x.acctNm,
                subAcctCd: x.acctCdSub,
                subAcctNm: x.acctNmSub,
                ctrlYn: x.ctrlYn
              }
            }).filter((x, i, arr) => x.ctrlYn === 'Y' && arr.findIndex(y => y.id === x.id) === i)

            await new Promise(resolve => {
              let cnt = budget.length
              if (cnt < 1) {
                resolve()
              } else {
                _.forEach(budget, x => {
                  queryBudget.apply(this, [x.postDt, x.cctrCd, x.acctCd, x.subAcctCd])
                    .then(response => {
                      x.bdgAmt = this.$numeral(response.data).value()
                      if (--cnt === 0) {
                        resolve()
                      }
                    })
                })
              }
            })

            _.forEach(budget, b => {
              let amount = data.slipDetails.filter(x => x.deptCd === b.cctrCd && x.acctCd === b.acctCd).map(x => x.acctAmt).reduce((ac, val) => ac + val)
              let fn = b.bdgAmt - amount
              assert.apply(this, [fn >= 0, `[${b.cctrCd}/${b.cctrNm}][${b.acctCd}/${b.acctNm}]의 예산이 부족합니다.`])
            })
          }
          */
          // _debug.apply(this, ['validate', 'Validate cmpl'])

          resolve()
        } catch (e) {
          if (e instanceof EvalError) {
            console.error(e.name + ": " + e.message);
          } else if (e instanceof RangeError) {
            console.error(e.name + ": " + e.message);
          } else if (e instanceof TypeError) {
            // console.log(e.name + ": " + e.message);
            this.$swal({
              type: 'warning',
              text: "저장할 데이터가 존재하지 않습니다."
            }).then(reject)
          } else {
            console.error(e.name + ": " + e.message);
            this.$swal({
              type: 'error',
              text: e
            }).then(reject)
          }
        }
      })
    },
    /**
     * ==========================================
     * 이벤트 처리 함수
     * ==========================================
     */
    /**
     * 출력 버튼 클릭 이벤트
     */
    buttonEventPrint() {
      // TODO::전표출력
    },
    /**
     * 결재보기 버튼 클릭 이벤트
     */
    buttonEventViewSubmit() {
      this.$modal.open({
        component: ApprSubm,
        props: {
          docType: 'SLIP',
          value: this.data,
          docMngNo: this.data.elecApprId
        },
        parent: this,
        width: 1600
      })
    },
    /**
     * 전표복사 버튼 클릭 이벤트
     */
    buttonEventCopy() {
      let name = undefined
      let url = undefined
      let grSlipNo = undefined
      const vm = this
      console.log("전표복사 버튼 클릭 :", this.data)
      switch (this.data.slipTypeCd) {
        case 'E1':
          name = 'pExpense'
          url = `/api/slip/p/copy/${this.grSlipNo}`
          break
        case 'E2':
          name = 'exctpExpenseEdit'
          url = `/api/slip/np/copy/${this.data.grSlipNo}`
          break
        case 'E3':
          name = 'ebillSlipReg'
          url = `/api/slip/copy/${this.data.grSlipNo}`
          break
        case 'E4':
          name = 'billSlipReg'
          url = `/api/slip/copy/${this.data.grSlipNo}`
          break
        case 'E5':
          name = 'cardSlipReg'
          url = `/api/slip/copy/${this.data.grSlipNo}`
          break
        case 'E6':
          name = 'whtSlipNew'
          break
        case 'E7':
          name = ''
          break
      }
      this.$swal({
        type: 'warning',
        text: `전표 복사시 기존 전표는 조회 목록에서 제외처리됩니다. 
        계속하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오',
      }).then((result) => {
        if (result.value) {
          this.$http.get(url)
              .then(responese => {
                var resGrslipNo = '';
                switch (vm.data.slipTypeCd) {
                  case 'E1':
                    resGrslipNo = responese.data.slipGroup.grSlipNo
                    break
                  case 'E2':
                    resGrslipNo = responese.data.slip.grSlipNo
                    break
                  case 'E3':
                    resGrslipNo = responese.data.slipHeader.grSlipNo
                    break
                  case 'E4':
                    resGrslipNo = responese.data.slipHeader.grSlipNo
                    break
                  case 'E5':
                    resGrslipNo = responese.data.slipHeader.grSlipNo
                    break
                }
                if (resGrslipNo !== '') {
                  vm.$router.push({
                    name: name,
                    params: {
                      grSlipNo: resGrslipNo,
                    }
                  })
                }
              })
        }
      });
    },
    /**
     * 전표복사 버튼 클릭 이벤트
     */
    buttonEventCopyNew() {
      let name = undefined
      let url = undefined
      const vm = this
      switch (this.slipTypeCd) {
        case 'E1':
          // name = 'cardSlipReg'
          name = 'pExpense'
          url = `/api/slip/p/copy/${this.grSlipNo}`
          break
        case 'E2':
          // name = 'ebillSlipReg'
          name = 'exctpExpenseEdit'
          url = `/api/slip/np/copy/${this.data.grSlipNo}`
          break
        case 'E3':
          name = 'ebillSlipReg'
          url = `/api/slip/copy/${this.data.grSlipNo}`
          break
        case 'E4':
          name = 'billSlipReg'
          url = `/api/slip/copy/${this.data.grSlipNo}`
          break
        case 'E5':
          name = 'cardSlipReg'
          url = `/api/slip/copy/${this.data.grSlipNo}`
          break
        case 'E6':
          name = 'whtSlipNew'
          break
        case 'E7':
          name = ''
          break
      }

      this.$swal({
        type: 'warning',
        text: `전표 복사시 기존 전표는 조회 목록에서 제외처리됩니다. 
        계속하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오',
      }).then((result) => {
        if (result.value) {
          this.$http.get(url)
            .then(responese => {
              if(responese.data) {
                var resGrslipNo = '';
                switch (vm.slipTypeCd) {
                  case 'E1':
                    resGrslipNo = responese.data.slipGroup.grSlipNo
                    break
                  case 'E2':
                    resGrslipNo = responese.data.slip.grSlipNo
                    break
                  case 'E3':
                    resGrslipNo = responese.data.slipHeader.grSlipNo
                    break
                  case 'E4':
                    resGrslipNo = responese.data.slipHeader.grSlipNo
                    break
                  case 'E5':
                    resGrslipNo = responese.data.slipHeader.grSlipNo
                    break
                }
                if (resGrslipNo !== '') {
                  vm.$router.push({
                    name: name,
                    params: {
                      grSlipNo: resGrslipNo,
                    }
                  })
                }
              }
            })
        }
      });
    },
    /**
     * 결재 상신 버튼 클릭 이벤트
     */
    buttonEventSubmit() {
      if(this.data.grSlipNo == undefined || this.data.grSlipNo.substring(0,3) === 'TMP'){
        this.$swal({type:'warning',text:'저장되지 않은 전표는 상신할 수 없습니다.'})
        return
      }
      //임시 방편(일괄비용시 세이브 안하고 바로 결재 상신 20-01-20(월) 적용)
      if(this.data.slipTypeCd === 'E5') {
        __proceed.apply(this, [this.data.grSlipNo])
      } else {
        this.buttonEventSave(__proceed)
      }

      function __proceed(grSlipNo) {
        this.data.grSlipNo = grSlipNo
        const vm = this
        this.$modal.open({
          component: ApprSubm,
          props: {
            docType: 'SLIP',
            value: this.data
          },
          parent: this,
          width: 1200,
          events: {
            close() {
              if (vm.$route.grSlipNo !== undefined) {
                location.reload()
              } else {
                vm.$router.push({
                  name: vm.name,
                  params: {
                    grSlipNo
                  }
                })
              }
            }
          }
        })
      }
    },
    /**
     * 전표 내역 일괄상신 버튼 클릭 이벤트
     */
    buttonEventBundleSubmit() {

      __proceed.apply(this, [this.result.grSlipNo])
      function __proceed(grSlipNo) {
        const vm = this
        this.$modal.open({
          component: ApprBundleSubmSlip,
          props: {
            docType: 'SLIP',
            value: this.result,
            config: this.config,
            apprTitle : this.title,
            slipTypeCd : this.result[0].slipTypeCd
          },
          parent: this,
          width: 1800,
          events: {
            close() {
              if (vm.$route.eaSlipNo !== undefined) {
                location.reload()
              } else {
                vm.$router.push({
                  name: vm.name,
                  params: this.result
                })
              }
            }
          }
        })
      }
    },
    /**
     * ERP전표 결재 상신 버튼 클릭 이벤트
     */
    buttonEventErpSubmit() {
      __proceed.apply(this, [this.result.grSlipNo])

      function __proceed(grSlipNo) {
        const vm = this
        this.$modal.open({
          component: ApprBundleSubm,
          props: {
            docType: 'ERP',
            value: this.result,
            config: this.config,
            apprTitle : this.title,
            slipTypeCd : this.form.slipTypeCd
          },
          parent: this,
          width: 1800,
          events: {
            close() {
              if (vm.$route.eaSlipNo !== undefined) {
                location.reload()
              } else {
                vm.$router.push({
                  name: vm.name,
                  params: this.result
                })
              }
            }
          }
        })
      }
    },
    buttonEventErpDelete(cnt) {
      let checkedRowIds = this.$refs.grid.instance.getCheckedRows(0);
      let targetList = [];
      let grSlipNo = ''; //삭제 키

      if(checkedRowIds) {
        _.forEach(checkedRowIds.split(","), (rId) => {
          //this.$refs.grid.instance.deleteRow(rId)
          grSlipNo = this.$refs.grid.instance.cells(rId,cnt).getValue()

          if(grSlipNo !== '') {
            targetList.push({
              grSlipNo : grSlipNo
            });
          }
        });

        this.$http.post(`/api/erp/delete/slip`, targetList)
            .then((response) => {
              this.$swal({ type: 'info', text: '삭제 되었습니다.' });
              this.goSearch();
            })
            .catch((e) => {
              console.error(e);
            })
            .finally(() => {
              this.$store.commit('finish')
            }); 
      } else {
        this.$swal({ type: 'info', text: '삭제 항목을 선택해 주세요.' });
      }
    },
    buttonEventErpDeleteNew() {
      var targetList = this.delChkList.filter(v => v.chk).map(v => v.grSlipNo);

      if(targetList.length > 0) {
        this.$swal({
          type: 'question',
          text: '선택한 전표 삭제 하시겠습니까?',
          showCancelButton: true
          }).then(r => {
            if (r.value) {

                this.$store.commit('loading');

                this.$http.post(`/api/erp/delete/slip`, targetList)
                .then((response) => {
                  this.$swal({ type: 'info', text: '삭제 되었습니다.' })
                  .then((result) => {
                    if (result.value) {
                      this.goSearch();
                    }
                  });
                })
                .catch((e) => {
                  console.error(e);
                })
                .finally(() => {
                  this.$store.commit('finish')
                });
            }
          })
      } else {
        this.$swal({ type: 'info', text: '삭제 항목을 선택해 주세요.' });
      }

    },
    /**
     * 상세화면 임시로 보기
     */
    showTmpActivity() {
      this.status.tmpBack = false;
      this.setGridStatus(!this.isReadOnly, false, !this.isReadOnly)
    },
    /**
     * 상세화면 임시 백버튼
     */
    backTmpActivity(){
      // this.status.tmpBack = true;
      // this.setGridStatus(false, false, false)
      // this.$router.go(-1)
      // this.props.history.goBack();
    },
    /**
     * 신규 작성 버튼 클릭 이벤트
     */
    buttonEventCreate() {

      if (this.status.memento.length > 1) {
        this.$swal({
          type: 'warning',
          text: '변경된 데이터가 있습니다. 계속 하시겠습니까?',
          showCancelButton: true
        }).then(val => {
          val.value ? this.create() : 0
        })
      } else {
        this.create()
      }
    },
    /**
     * 삭제 버튼 클릭 이벤트
     */
    buttonEventDelete() {
      const viewNmToRtr = this.name;
      this.$swal({
        type: 'question',
        text: '삭제하시겠습니까?',
        showCancelButton: true
      }).then(val => {
        if (val.value) {
          this.$http.delete(_url('/api/slip', this.data.grSlipNo))
            .then(response => {
              this.$swal({
                type: 'success',
                text: '저장 전표가 삭제되었습니다.'
              }).then(() => { //todo
                if(this.fromSlipLst){
                  this.$router.push({
                    name: 'slipLst'
                  })
                }else{
                  // this.$router.go(-1);
                  this.$router.push({
                    name: viewNmToRtr
                  })
                }
              })
              return response
            })
            .catch(response => {
              return response
            })
        }
      })
    },
    /**
     *
     */
    confirmPostDt(){
        return new Promise(async (resolve, reject) => {
          if(this.data.slipTypeCd === 'E2' || this.slipTypeCd === 'E3' || this.slipTypeCd === 'E4'){//E2~E4의 전표의 경우 회계일자 수정 불가에 대한 컨펌창 노출
          this.$swal({
            type: 'warning',
            text: `회계일자는 저장후 수정되지 않습니다. 계속해서 저장 하시겠습니까?`,
            showCancelButton: true,
            confirmButtonText: '예',
            cancelButtonText: '아니오',
          }).then((result) => {
            if (result.value) {
              resolve();//승인에 따른 resolve 처리
            }else{
              reject();//거부에 따른 reject 처리
            }
          });
          }else{
            resolve();//E1 외 전표에 대하여 컨펌창 노출 없이 저장 진행
          }
        })
    },
    /**
     * 저장 버튼 클릭 이벤트
     */
    buttonEventSave(callback) {
      this.confirmPostDt().then(() => {
      this.validate()
        .then(() => {

          switch(this.data.slipTypeCd){
            case 'E1':
              var slipGroup = _.clone(this.data)
              //데이터 컬럼명 맞추기
              // slipGroup.grSlipNo = slipGroup.grSlipNo || slipGroup.eaSlipNo //grSlipCode
              if(this.$route.params.copy){
                if(!_.isEmpty(this.tempData[0])){
                slipGroup.grSlipNo = this.tempData[0].grSlipNo //grSlipCode
                }else{
                slipGroup.grSlipNo = '' //grSlipCode
                }
              }else{
                if(!_.isEmpty(slipGroup.grSlipNo)){
                  slipGroup.grSlipNo = slipGroup.grSlipNo
                }else if(!_.isEmpty(slipGroup.eaSlipNo)){
                  slipGroup.grSlipNo = slipGroup.eaSlipNo
                }else if(!_.isEmpty(this.tempData)){
                  slipGroup.grSlipNo = this.tempData[0].grSlipNo
                }else if(this.tmpSlipNo){
                  slipGroup.grSlipNo = this.tmpSlipNo
                }
                // slipGroup.grSlipNo = slipGroup.grSlipNo || slipGroup.eaSlipNo //grSlipCode
              }

              slipGroup.postDt = this.$moment(slipGroup.postDt).endOf('month').format('YYYYMMDD')
              //slipGroup.postDt = slipGroup.eaSlipDt.replace(/-/gi,"") //전표일자를 회계일자에 대입해준다.(백엔드데이터에서 이것만 받고 있음.)

              slipGroup.wrtId = slipGroup.empNo     //직원 (작성자)
              //slipGroup.wrtDeptCd = slipGroup.baseDeptCd  //직원부서 (작성자)
              slipGroup.liabilityAccountCd = slipGroup.lbltAcctCd //부채계정코드
              slipGroup.slipHeaders = []

              //법인카드지급
              var sh1 = {}
              slipGroup.slipDetails.forEach((x, index) => {
                if(x.eaSlipNo === undefined) this.$http.get('/api/tmp/documentMngNo').then(response => {x.eaSlipNo = response.data})
                //details set
                var slipDetails = []
                slipDetails.push({
                  itemSeq: index+1,
                  slipTypeCd: 'E1',
                  dcCd: 'D',
                  itemSeq: x.itemSeq,   //sequence

                  //stptPlc : x.stptPlc,  //출발지
                  //dstnPlc : x.dstnPlc,  //도착지
                  //biztrpObj : x.biztrpObj,//출장목적
                  //tpsTypeCd : x.tpsTypeCd,  //교통비유형코드-교통비
                  //tpsDst : x.tpsDst,  //거리-교통비
                  //oilKindCd : x.oilKindCd, //유종코드-교통비
                  //oilUpc : x.oilUpc,  //유류단가-교통비
                  //oilEff : x.oilEff,  //연비-교통비
                  //acctAmt : x.acctAmt,  //경비금액
                  lnTypeCd: x.lnTypeCd,  //라인타입 보내줘야하네?

                  attribute1:x.attribute1,
                  attribute2:x.attribute2,
                  attribute3:x.attribute3,
                  attribute4:x.attribute4,
                  attribute5:x.attribute5,
                  attribute6:x.attribute6,
                  attribute7:x.attribute7,
                  attribute8:x.attribute8,
                  attribute9:x.attribute9,
                  attribute10:x.attribute10,
                  attribute11:x.attribute11,
                  attribute12:x.attribute12,
                  attribute13:x.attribute13,
                  attribute14:x.attribute14,
                  attribute15:x.attribute15,
                })

                x.slipDetails = slipDetails

                sh1 = {
                  itemSeq: index+1,
                  slipTypeCd: 'E1',
                  eaSlipNo: x.eaSlipNo,
                  evdTypeCd : '10',  //구분 법인카드
                  postDt : x.eaSlipDt, //회계일자
                  evdDt : x.evdDt,  //증빙일자
                  expsId : x.expenseId, //비용항목
                  expsAcctCd : x.acctCd,//비용계정
                  productCd : x.detailCd,  //제품
                  useDtlsNo: x.useDtlsNo,
                  crdApprNo : x.apprNo,  //카드승인번호
                  crdNo: x.crdNo,   //카드번호
                  crdMerchBizNo: x.merchBizNo,  //가맹점 사업자번호
                  crdMerchNm: x.merchNm,      //가맹점명
                  baseDeptCd : x.deptCd, //ERP부서 코드
                  baseDeptNm : x.deptNm, //ERP부서명
                  wrtDeptCd : slipGroup.wrtDeptCd,  //작성부서
                  wrtDeptNm : slipGroup.wrtDeptNm,  //작성부서
                  // apSubAcctCd : slipGroup.lbltSubAcctCd,//부채보조계정
                  // vatYn: x.vatYn,     //부가세포함
                  taxCd : x.vatCd, //부가세
                  supAmt : x.supAmt,  //금액 tot? etc?
                  vatAmt: x.vatAmt,
                  totAmt : x.useAmt,  //금액 tot? etc?
                  hdSgtxt : x.lnSgtxt, //적요
                  slipDetails: x.slipDetails,
                  acctCd : x.acctCd,
                  acctNm : x.acctNm,
                  subAcctCd : x.acctCdSub, //보조계정코드
                  subAcctNm : x.acctNmSub, //보조계정명
                  oriVatAmt: x.baseVatAmt,
                  oriSupAmt: x.baseSupAmt,
                  oriTotAmt: x.totAmt,
                  merchOwner: x.merchOwner,
                  payTermCd : slipGroup.payTermCd,//지급조건
                  payDueDt: this.$moment(slipGroup.payDueDt).format('YYYYMMDD'),//지급예정일
                  payText: slipGroup.payText,//지급비고
                }
                  slipGroup.slipHeaders.push(sh1)
              })

              //현금지급
              var sh2 = {}
              slipGroup.slipDetails2.forEach((x, index) => {
                sh2 = {
                  //header set
                  itemSeq: index+1,
                  eaSlipNo : x.eaSlipNo,
                  slipTypeCd: 'E1',
                  evdTypeCd : '11',  //구분 현금지급
                  evdDt : x.eaSlipDt,  //증빙일자
                  postDt : x.eaSlipDt, //회계일자
                  cctrCd: x.cctrCd,
                  cctrNm: x.cctrNm,
                  // baseDeptCd : slipGroup.baseDeptCd, //기준부서
                  // baseDeptNm : x.cctrNm2, //기준부서
                  baseDeptCd : x.deptCd, //ERP 부서코드
                  baseDeptNm : x.deptNm, //ERP 부서명
                  wrtDeptCd : slipGroup.wrtDeptCd,  //작성부서
                  wrtDeptNm : slipGroup.wrtDeptNm,  //작성부서
                  // apSubAcctCd : slipGroup.lbltSubAcctCd,//부채보조계정
                  expsId : x.expenseId2, //비용항목
                  expsAcctCd : x.acctCd2,//비용계정
                  productCd : x.detailCd2,  //제품
                  //supAmt: x.useAmt2,
                  supAmt: x.krwTotAmt,
                  totAmt : x.useAmt2,  //금액 tot? etc?
                  hdSgtxt : x.lnSgtxt, //적요
                  evdCustCd : x.evdCustCd2,  //가맹점명
                  payCustCd : x.payCustCd2,  //지급처
                  hdRef3 : x.address,  //가맹점 주소
                  slipDetails: [{
                    itemSeq: index+1,
                    slipTypeCd: 'E1',
                    compCd: '0',
                    lnTypeCd: 'ITEM',
                    dcCd: 'D',
                    attribute1:x.attribute1,
                    attribute2:x.attribute2,
                    attribute3:x.attribute3,
                    attribute4:x.attribute4,
                    attribute5:x.attribute5,
                    attribute6:x.attribute6,
                    attribute7:x.attribute7,
                    attribute8:x.attribute8,
                    attribute9:x.attribute9,
                    attribute10:x.attribute10,
                    attribute11:x.attribute11,
                    attribute12:x.attribute12,
                    attribute13:x.attribute13,
                    attribute14:x.attribute14,
                    attribute15:x.attribute15,
                  }],//slipDetail.isEmpty 조건 피하기 위한 하드코딩
                  acctCd : x.acctCd2,
                  acctNm : x.acctNm2,
                  subAcctCd : x.acctCdSub2, //보조계정코드
                  subAcctNm : x.acctNmSub2, //보조계정명

                  payTermCd : slipGroup.payTermCd,//지급조건
                  payDueDt: this.$moment(slipGroup.payDueDt).format('YYYYMMDD'),//지급예정일
                  payText: slipGroup.payText,//지급비고
                  curCd:  x.curCd,
                  excRt : x.excRt,
                  krwTotAmt : x.krwTotAmt,
                  krwSupAmt : x.krwTotAmt
                }

                slipGroup.slipHeaders.push(sh2)
              })

              //(slipGroup)
              //컬럼 맞추고 불필요하니 삭제
              delete slipGroup.slipDetails  //첫 그리드 리스트 삭제
              delete slipGroup.slipDetails2  //첫 그리드 리스트 삭제
              //1.
              //2.
              this.$store.commit('loading')
              this.$http.post('/api/slip/p', slipGroup)
                .then(response => {
                  if (callback !== undefined && typeof callback === 'function') {
                    callback.apply(this, [response.data.grSlipNo])
                  } else {
                    this.$swal({
                      type: 'success',
                      text: '저장되었습니다.'
                    }).then(() => {
                      this.grReroute(response.data.grSlipNo)
                    })
                  }
                  return response
                }).catch(response => {
                  //return reject(response)
                  console.error("buttonEventSave Error"+response)
                }).finally(() => {
                  this.$emit('slipTypeSend', this.data.slipTypeCd)
                  this.$store.commit('finish')
                })

              break
            case 'E2':
            case 'E5':
              var sg = _.clone(this.data)
              sg.slipDetails.forEach((x, index) => {
                x.dcCd = 'D'
                x.dcNm = "차변"
                x.compCd = '0'
                x.itemSeq = index + 1   //sequence
                x.cctrCd = x.deptCd
                x.cctrNm = x.deptNm
                x.productCd = x.detailCd
                x.productNm = x.detailNm
                x.acctAmt = this.$numeral(x.useAmt).value() //2020.08.14 그리드(세부항목) 소수점 변경
                x.lnSgtxt = x.lnSgtxt
                x.subAcctCd = x.acctCdSub
                x.subAcctNm = x.acctNmSub
              })

              var slipHeader = {
                grSlipNo : sg.grSlipNo,
                slipTypeCd : sg.slipTypeCd,
                postDt : this.$moment(sg.postDt).format('YYYYMMDD'),
                evdDt : this.$moment(sg.evdDt).format('YYYYMMDD'),
                slipStatCd : sg.slipStatCd,
                slipStatNm : sg.slipStatNm,
                wrtDeptCd : sg.wrtDeptCd,
                wrtDeptNm : sg.wrtDeptNm,
                wrtId : sg.empNo,
                wrtNm : sg.empNm,
                liabilityAccountCd : sg.lbltAcctCd,
                liabilityAccountNm : sg.lbltAcctNm,
                apSubAcctCd: sg.lbltSubAcctCd,
                curCd : sg.curCd,
                excDt : this.$moment(sg.excDt).format('YYYYMMDD'),
                excRt : sg.excRt,
                stlFgCd : sg.stlFgCd,
                aplFgCd : sg.aplFgCd,
                fixAssetYn : sg.fixAssetYn,
                totAmt : sg.totAmt,
                totAmtKrw : sg.totAmtKrw,
                advReqSlipNo : sg.preInvoiceNum,
                advReqSlipAmt: sg.mrAmt,
                hdSgtxt : sg.hdSgtxt,
                evdCustCd : sg.evdCustCd,
                evdCustNm : sg.evdCustNm,
                payCustCd : sg.payCustCd,
                payCustNm : sg.payCustNm,
                payTermCd : sg.payTermCd,
                payTermNm : sg.payTermNm,
                hdRef4 : sg.siteNm,
                custAcctNo : sg.custAcctNo,
                payDueDt : this.$moment(sg.payDueDt).format('YYYYMMDD'),
                payText: sg.payText,
                eaSlipNo : sg.eaSlipNo,
                slipDetails: sg.slipDetails,
                block:sg.block, //2020.05.15 E2 지급조건에따른 지급예정일 활성화
                reUsedSlip:sg.reUsedSlip
              }
              console.log(slipHeader)
              this.$store.commit('loading')
              this.$http.post('/api/slip/np', slipHeader)
              .then(response => {
                if (callback !== undefined && typeof callback === 'function') {
                  callback.apply(this, [response.data.grSlipNo])
                } else {
                  this.$swal({
                    type: 'success',
                    text: '저장되었습니다.'
                  }).then(() => {
                    this.grReroute(response.data.grSlipNo)
                  })
                }
                return response
              }).catch(response => {
                return reject(response)
              }).finally(() => {
                this.$emit('slipTypeSend', this.data.slipTypeCd)
                this.$store.commit('finish')
              })

              break
            default:
              // TODO::데이터 저장
              var slipHeader = _.clone(this.data)
              // 작성일자를 지운다
              delete slipHeader.wrtDtm
              delete slipHeader.regDtm
              delete slipHeader.fnlApprDtm
              delete slipHeader.fnlAprverId
              delete slipHeader.elecApprId
              delete slipHeader.elecApprDtm
              for (var member in slipHeader) {
                if (slipHeader[member] != null) {
                  if (member.match(/.*Amt$/g)) {
                    slipHeader[member] = this.$numeral(slipHeader[member]).value()
                  } else if (member.match(/.*(Dt)$/g)) {
                    slipHeader[member] = this.$moment(slipHeader[member]).format('YYYYMMDD')
                  }
                }
              }

              slipHeader.slipDetails = slipHeader.slipDetails.map(x => {
                delete x.regDtm
                delete x.chgDtm
                for (var member in x) {
                  if (x[member] != null) {
                    if (member.match(/^.*Amt$/g)) {
                      x[member] = this.$numeral(x[member]).value()
                    } else if (member.match(/^.*Dt$/g)) {
                      x[member] = this.$moment(x[member]).format('YYYYMMDD')
                    }
                  }
                }
                return x
              })

              if (slipHeader.slipTypeCd === 'E6' ) {
                slipHeader.supAmt = slipHeader.totAmt
              }
              // 대표계정코드
              slipHeader.stnAcctCd = slipHeader.slipDetails.filter(x => [x.dcCd, x.lnTypeCd].join('_') === 'D_ITEM')[0].acctCd
              this.$store.commit('loading')
              this.$http.post('/api/slip', slipHeader)
                .then(response => {
                  if (callback !== undefined && typeof callback === 'function') {
                    callback.apply(this, [response.data.eaSlipNo])
                  } else {
                    this.$swal({
                      type: 'success',
                      text: '저장되었습니다.'
                    }).then(() => {
                      if(response.data.slipTypeCd === 'E3' || response.data.slipTypeCd === 'E4') {
                        this.reroute(response.data.grSlipNo)
                      } else {
                        this.reroute(response.data.eaSlipNo)
                      }

                    })
                  }
                  return response
                })
                .catch(response => {
                  return response
                }).finally(() => {
                  this.$store.commit('finish')
                })
          }//end switch
        })//end then
      })
    },
    /**
     * 일반 이벤트 함수
     */
    reroute(no) {
      //    if (this.eaSlipNo === undefined || this.eaSlipNo !== eaSlipNo) {
      if(this.data.slipTypeCd === 'E3' || this.data.slipTypeCd === 'E4') {
        this.$router.push({
          name: this.name,
          params: {
            no
          }
        }).catch((err) => {
          window.location.reload()
        })
      } else {
        this.$router.push({
          name: this.name + 'Edit',
          params: {
            no
          }
        })
      }

      /*      } else {
              window.location.reload()
            }*/
    },
    grReroute(grSlipNo) {
      this.$router.push({
        name: this.name + 'Edit',
        params: {
          grSlipNo
        }
      }).catch((err) => {
        window.location.reload()
      })
    },
    create() {
      if (this.$route.params.eaSlipNo || this.$route.params.grSlipNo) {
        this.$router.push({
          name: this.name
        }).catch((err) => {
          window.location.reload()
        })
      } else {
        window.location.reload()
      }
    },
    setGridStatus(isLoaded, isNew, isReadonly) {
      this.status.loaded = isLoaded
      this.status.new = isNew
      this.status.readonly = isReadonly
      this.gridType = isReadonly ? 'GridRO' : 'GridED'
      this.$forceUpdate()
    },
    uploadFile() {
      this.$modal.open({
        component: EvidAtchPop,
        parent: this,
        width: 1200
      })
    }
  },
  created() {
    this.status.memento = []
  }
}
