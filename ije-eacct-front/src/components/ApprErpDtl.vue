<template>
    <div>
        <div class="table-area" style="margin-top: 0px;">
            <appr-erp-header :doc-type="docType" :apprHeader="apprHeader" :appr-line="apprLine" v-model="data"/>
        </div>
    </div>
</template>

<script>
    import ApprErpHeader from '@/components/ApprErpHeader.vue'
    import ApprLineSet from '@/components/ApprLineSet.vue'

    export default {
        props: {
            title: {
                type: String,
                required: false,
                default: '결재 상신'
            },
            docMngNo: {
                type: String,
                required: false
            },
            budReqNo: {
                type: String,
                required: false
            },
            value: {
                type: Array,
                required: false
            },
            docType: {
                type: String,
                required: false,
                default: 'SLIP'
            },
            approvalLine: {
                type: Array,
                required: true
            },
            slipTypeCd: {
                type: String,
                required: false
            },
        },
        components: {
            ApprErpHeader,
            ApprLineSet,
        },
        data() {
            return {
                apprHeader: {},
                apprLine: [],
                apprNo: '',
                apprSeq: '',
                docTitleNm: '',
                docTypeCd: '',
                docStatus: '',
                draftUserId: '',
                thisApprUser: '',
                deleYn: 'N',
                loaded: false,
                data: {},
                deleInfo: {},
            }
        },
        computed: {
            docComponent() {

            }
        },
        methods: {
            /**
             * Query Approval line rule
             */
            queryApprLineRule() {
                let docTypeCd = this.docType
                let dtlTypeCd = undefined
                let curCd = undefined
                let maxAmt = undefined

                switch (this.docType) {
                    case 'ERP':
                        docTypeCd = 'SLIP'
                        dtlTypeCd = this.slipTypeCd
                        curCd = 'KRW'
                        maxAmt = 0
                        break
                }

                this.$store.commit('loading')
                this.$http.post('/api/apprLine/group', {docTypeCd:docTypeCd})
                    .then(response => {
                      if (Array.isArray(response.data) && response.data.length > 0) {
                        this.apprLine = response.data.filter(x => x.aprverId)

                        this.apprLine = this.apprLine.map(x => {
                          x.apprDtm = x.apprDtm ? this.$moment(x.apprDtm).format('YYYY-MM-DD') : undefined
                          return x
                        })
                      } else {
                        this.$store.commit('loading')
                        this.$http.get('/api/appr/rule', {
                          params: {
                            docTypeCd,
                            dtlTypeCd,
                            curCd,
                            maxAmt
                          }
                        }).then(response => {
                          if (Array.isArray(response.data) && response.data.length > 0) {
                            this.apprLine = response.data.filter(x => x.aprverUser)
                          }
                        }).finally(() => {
                          this.apprLine.unshift(
                              {
                                aprverUser: this.$store.state.loginInfo.userName,
                                jobNm: this.$store.state.loginInfo.loginJobNm,
                                aprverId: this.$store.state.loginInfo.loginId,
                                cctrNm: this.$store.state.loginInfo.loginDeptNm,
                                apprTypeCd: '10',
                                apprType: '기안'
                              }
                          );
                          this.$emit('approvalLine', this.apprLine)
                          this.$store.commit('finish')
                        })
                      }
                      this.$emit('approvalLine', this.apprLine)
                    }).catch((e) => {
                  this.$swal({
                    type: 'error',
                    text: e
                  })
                }).finally(() => {
                  this.$emit('approvalLine', this.apprLine)
                  this.$store.commit('finish')
                })
            },
            approval() {
            },
            getAppr(docMngNo) {
                // console.log('Get Appr !!, docMngNo : '+docMngNo)
                this.$http.get(`/api/appr/detail/${docMngNo}`)
                    .then(response => {
                        if (response.data) {
                            this.apprSeq = ''
                            this.thisApprUser = ''
                            this.apprHeader = response.data.apprHeader[0];
                            this.apprLine = response.data.apprDetails;

                            // if (this.docType === 'GR') {
                            //     let data = response.data.slipGroupHeader
                            //     data.slipGroupDetails = response.data.slipGroupDetails
                            //     this.data = data
                            // } else {
                            //     let data = response.data.slipHeader
                            //     data.slipDetails = response.data.slipDetails
                            //     this.data = data
                            // }

                            //this.slipHeader = response.data.slipHeader;
                            //this.slipDetails = response.data.slipDetails;
                            if(response.data.slipGroup) {
                                let data_gr = response.data.slipGroup
                                let data_hd_top = response.data.slipHeaders.filter(x => x.evdTypeCd === '10')
                                let data_hd_botm = response.data.slipHeaders.filter(x => x.evdTypeCd === '42')

                                // this.data = data_gr
                                data_hd_top.forEach((x) => {
                                    x.itemSeq = x.itemSeq
                                    x.eaSlipDt = x.evdDt
                                    x.useDt = x.evdDt
                                    x.cctrCd = x.wrtDeptCd
                                    x.cctrNm = x.wrtDeptNm
                                    x.deptCd = x.baseDeptCd
                                    x.deptNm = x.baseDeptNms
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
                                    x.lnTypeCd = x.lnTypeCd  //라인타입 보내줘야하네?
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
                                data_hd_botm.forEach((x) => {
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
                                    x.storeNm = x.hdRef5
                                    x.address = x.hdRef3
                                })

                                this.data.slipDetails = []
                                this.data.slipDetails.push(data_hd_top)
                                this.data.slipDetails2 = []
                                this.data.slipDetails2.push(data_hd_botm)
                            } else if(response.data.slip) {
                                let data_hd = response.data.slip
                                let data_dt = response.data.slipDetails

                                data_hd.evdTypeCd = '40'
                                data_hd.evdCustNm = data_hd.payCustNm
                                data_hd.evdCustCd = data_hd.payCustCd
                                data_hd.custAccount = data_hd.custAcctNo
                                data_hd.lbltAcctCd = data_hd.liabilityAcctCd
                                data_hd.lbltAcctNm = data_hd.liabilityAcctNm
                                data_hd.baseDeptCd = data_hd.wrtDeptCd
                                data_hd.baseDeptNm = data_hd.wrtDeptNm
                                data_hd.empNo = data_hd.wrtId
                                data_hd.empNm = data_hd.wrtNm
                                data_dt.forEach(x => {
                                    x.itemSeq = x.itemSeq
                                    x.dcCd = 'D'
                                    x.dcNm = '차변'
                                    x.lnTypeCd = 'ITEM'
                                    x.deptCd = x.cctrCd
                                    x.deptNm = x.cctrNm
                                    x.expenseAcctCode = x.acctCd
                                    x.expenseAcctName = x.acctNm
                                    x.expenseName = x.expenseNm
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
                                this.data = data_hd

                                this.data.slipDetails = []
                                this.data.slipDetails.push(data_dt[0])
                            } else {
                                this.data = response.data.slipHeader
                                this.data.slipDetails = response.data.slipDetails
                                this.apprHeader = response.data.apprHeader;
                                this.apprLine = response.data.apprDetails;

                                this.docTypeCd = response.data.apprHeader[0].docTypeCd;
                                this.docStatus = response.data.apprHeader[0].docStatCd;
                                this.draftUserId = response.data.apprHeader[0].draftUserId;
                                this.docTitleNm = response.data.apprHeader[0].docTitleNm;
                                this.apprNo = response.data.apprHeader[0].apprNo;

                                this.data.custAccount = [this.data.custBnkCd, this.data.custAcctNo].join('_')

                                _.forEach(this.data.slipDetails, item => {
                                    switch (item.dcCd) {
                                        case 'C':
                                            item.creditAmt = item.acctAmt
                                            break
                                        case 'D':
                                            item.debitAmt = item.acctAmt
                                            break
                                    }
                                })
                            }


                            this.docTypeCd = response.data.apprHeader[0].docTypeCd;
                            this.docStatus = response.data.apprHeader[0].docStatCd;
                            this.draftUserId = response.data.apprHeader[0].draftUserId;
                            this.docTitleNm = response.data.apprHeader[0].docTitleNm;
                            this.apprNo = response.data.apprHeader[0].apprNo;

                            _.forEach(this.apprLine, v => {
                                if (v.apprFgCd === 'X') {
                                    this.apprSeq = v.apprSeq
                                    this.thisApprUser = v.aprverId
                                    return false
                                }
                            })

                            this.loaded = true

                            this.$emit('approvalLine', this.apprLine)
                        }
                    })
                    .catch(({message}) => {
                        console.error(message);
                    })
            },
            apprCheck() {
                this.$http.get(`/api/appr/delegatingCheck`, {
                    params: {
                        adlgId: this.thisApprUser,
                        actId: this.$store.state.loginInfo.loginId
                    }
                })
                    .then(response => {
                        console.log('ApprCheck!!')
                        this.deleYn = response.data;

                        this.apprInfo = {
                            "deleYn": this.deleYn,
                            "apprSeq": this.apprSeq,
                            "thisApprUser": this.thisApprUser,
                            "docStatus": this.docStatus,
                            "draftUserId": this.draftUserId,
                            "docTitleNm": this.docTitleNm,
                            "apprNo": this.apprNo
                        }
                        this.$emit('apprInfo', this.apprInfo)
                        this.loaded = false
                    });
            },
            cancel(docMngNo) {
                this.$swal({
                    type: 'info',
                    text: `상신을 취소합니다. 계속 하시겠습니까?`,
                    showCancelButton: true,
                    confirmButtonText: '예',
                    cancelButtonText: '아니오',
                }).then((result) => {
                    if (result.value) {
                        if (docMngNo != undefined) {
                            this.$store.commit('loading')
                            this.$http.get(`/api/appr/detail/cancel/${docMngNo}`)
                                .then(response => {
                                    if (response.data) {
                                        this.$swal({type: 'info', text: '상신 취소되었습니다.'});
                                        this.$router.push({path: `/apprReqLst`});
                                    }
                                    this.$store.commit('finish')
                                })
                                .catch(({message}) => {
                                    console.error(message);
                                    this.$store.commit('finish')
                                });
                        }
                    }
                })
            },
            openVendorPopup() {
                let vm = this
                this.$modal.open({
                    component: ApprLineSet,
                    props: {
                        lineList: this.apprLine
                    },
                    parent: this,
                    events: {
                        close(data) {
                            // Close event handler
                          if(data.lineList){
                            vm.apprLine = data.lineList || []
                          }
                        }
                    }
                })
            }
        },
        created() {
            if (this.docMngNo !== undefined) this.getAppr(this.docMngNo)
            if (this.docMngNo === undefined && this.budReqNo === undefined) this.queryApprLineRule()
        },
        mounted() {
            this.$store.commit('finish')
        },
        watch: {
            value: {
                immediate: true,
                deep: true,
                handler(value) {
                    this.data = value
                }
            },
            approvalLine: {
                immediate: true,
                deep: true,
                handler(value) {
                    this.apprLine = value
                }
            },
            'loaded'() {
                if(this.loaded) this.apprCheck()
            },
        }
    }
</script>

<style scoped>
    .modal-card {
        width: auto;
    }
</style>
