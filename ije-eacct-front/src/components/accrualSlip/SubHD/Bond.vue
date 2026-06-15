<template>
    <div class="table-header">
        <div class="table-name">
            <h3 class="ico_table_name">BOND 내역</h3>
        </div>
        <div class="btn-wrap btn-type2 clearfix mr3" v-if="!readOnly">
            <el-button type="danger" icon="el-icon-refresh" @click="bondInit">초기화</el-button>
        </div>
        <table class="table">
            <colgroup>
                <col width="20%">
                <col width="30%">
                <col width="20%">
                <col width="30%">
            </colgroup>
            <tbody>
                <tr>
                    <th>분할 및 기타비용</th>
                    <td>
                        <el-checkbox v-model="value.bond.splitEtcYn" true-label="Y" false-label="N" :disabled="readOnly" @change="bondTypeChk">
                            <el-link type="danger">* 분납 및 기타비용 지출이면 체크하세요.</el-link>
                        </el-checkbox>
                    </td>
                    <th>BOND 종류</th>
                    <td>
                        <el-input v-model="value.bond.bondGubun" readonly></el-input>
                    </td>
                </tr>
                <tr>
                    <th>수수료 유형</th>
                    <td>
                        <el-radio-group v-model="value.bond.type" :disabled="readOnly" @change="bondTypeChk">
                            <el-radio label="LOCAL">국내수수료</el-radio>
                            <el-radio label="OVERSEA">해외수수료</el-radio>
                        </el-radio-group>
                    </td>
                    <th>수익자 국가</th>
                    <td>
                        <el-input class="mr3" type="text" v-model="value.bond.benCountryCd" readonly style="width: 40%;"></el-input>
                        <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(60% - 5px)">
                            <el-input type="text" v-model="value.bond.benCountry" clearable readonly></el-input>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th>REF NO. (신규/AMEND)</th>
                    <td>
                        <el-input class="mr3" type="text" v-model="value.bond.refNo" disabled style="width: 40%;"></el-input>
                        <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(60% - 5px)">
                            <el-input type="text" class="input-bg-white" v-model="value.bond.amendText" clearable readonly>
                                <i class="el-icon-search el-input__icon" slot="suffix" @click="refNoOpenModal"></i>
                            </el-input>
                        </div>
                    </td>
                    <th>PJT 명</th>
                    <td>
                        <el-input type="text" v-model="value.bond.projectNm" readonly></el-input>
                    </td>
                </tr>
                <tr>
                    <th>고객명</th>
                    <td>
                        <el-input type="text" v-model="value.bond.customerNm" readonly></el-input>
                    </td>
                    <th>개설일(YYYYMMDD)</th>
                    <td>
                        <el-input type="text" v-model="value.bond.openingDt" readonly></el-input>
                    </td>
                </tr>
                <tr>
                    <th>통화</th>
                    <td>
                        <el-input class="mr3" type="text" v-model="value.bond.currencyCd" readonly style="width: 40%;"></el-input>
                        <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(60% - 5px)">
                            <el-input type="text" v-model="value.bond.currencyNm" readonly>
                                <!-- <i class="el-icon-search el-input__icon" slot="suffix"></i> -->
                            </el-input>
                        </div>
                    </td>
                    <th>보증금액</th>
                    <td>
                        <el-input type="number" :step="0.01" v-model="value.bond.guaranteeAmt" :readonly="readOnly"></el-input>
                    </td>
                </tr>
                <tr>
                    <th>{{area}}만기(YYYYMMDD)</th>
                    <td>
                        <el-date-picker
                            v-model="value.bond.maturityDt"
                            type="date"
                            format="yyyyMMdd"
                            value-format="yyyyMMdd"
                            :readonly="readOnly">
                        </el-date-picker>
                    </td>
                    <th>{{area}}요율(%)</th>
                    <td>
                        <el-input-number v-model="value.bond.rate" :precision="2" :step="0.01" :max="100" :min="0" :disabled="readOnly"></el-input-number>
                    </td>
                </tr>
                <tr>
                    <th>{{area}}은행</th>
                    <td>
                        <el-input type="text" v-if="value.bond.type === 'LOCAL'" v-model="value.bond.localBankNm" readonly></el-input>
                        <el-input type="text" v-else v-model="value.bond.intBankNm" readonly></el-input>
                    </td>
                    <th>예산(단위:원)</th>
                    <td>
                        <!-- <el-input type="text" v-model="value.bond.budget" readonly></el-input> -->
                        {{ value.bond.budget | amt }}
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>

import BondRefNoModal from '@/components/accrualSlip/Modals/BondRefNoModal.vue';

export default {
    props: ['value', 'readOnly'],
    components: {
        
    },
    data() {
        return {
        }
    },
    computed: {
        area() {
            return this.value.bond.type === 'LOCAL' ? '국내' : '해외';
        }
    },
    created() {
        this.bondTypeChk();
    },
    methods: {
        //REF No
        getRefNoList(params) {
            return new Promise((resolve, reject) => {
                this.$http.post(`/api/bondMst/refNo/list`, params).then(res => resolve.data)
                .then(data => resolve(data))
                .catch(err => reject(err));
            })
        },
        // REF No 조회 팝업
        refNoOpenModal() {
            const self = this;
            this.$modal.open({
                component: BondRefNoModal,
                parent: this,
                width: 800,
                events: {
                    close(val) {
                        const { 
                            amendSeq, count, bondCd, currencyCd, benCountryCd, benCountry,
                            refNo, projectNm, customerNm, openingDt,
                            localBankNm, intBankNm, budget, currencyAmt
                        } = val;
                        self.value.bond.amendSeq = self.$numeral(amendSeq || 0).value();
                        self.value.bond.amendCount = self.$numeral(count || 0).value();
                        self.value.bond.bondGubun = bondCd;
                        self.value.bond.benCountryCd = benCountryCd;
                        self.value.bond.benCountry = benCountry;
                        self.value.bond.refNo = refNo;
                        self.value.bond.projectNm = projectNm;
                        self.value.bond.customerNm = customerNm;
                        self.value.bond.openingDt = openingDt ? self.$moment(openingDt).format('YYYYMMDD') : null;
                        self.value.bond.currencyCd = currencyCd;
                        self.value.bond.currencyNm = currencyAmt;
                        self.value.bond.localBankNm = localBankNm;
                        self.value.bond.intBankNm = intBankNm;
                        self.value.bond.budget = budget || 0;

                        self.bondTypeChk();
                    }
                }
            });
        },
        bondTypeChk() {

            this.setArea(this.value.bond.type);

            if(this.value.bond.type === 'LOCAL') {
                if(this.value.bond.amendSeq == 0 && this.value.bond.amendCount == 0) {
                    this.value.bond.amendText = '신규';
                    if(this.value.bond.splitEtcYn){
                        this.$message({ type: 'warning', message: '신규건은 [분납 및 기타비용] 을 선택 할 수 없습니다.' });
                        this.value.bond.splitEtcYn = 'N';
                    }
                } else {
                    if(this.value.bond.splitEtcYn){
                        if(this.value.bond.amendSeq === 0){
                            this.value.bond.amendText = '신규';
                        }else{
                            this.value.bond.amendText = `AMEND(${this.value.bond.amendSeq})`;
                        }
                    } else {
                        if(['CC', 'IC', 'IE'].includes(this.value.slipStatus)){
                            //결재완료인 전표는 amend_seq 그대로 표현
                            if(this.value.bond.amendSeq == 0){
                                this.value.bond.amendText = '신규';
                            }else{
                                this.value.bond.amendText = `AMEND(${this.value.bond.amendSeq})`;
                            }
                        }else{
                            this.value.bond.amendText = `AMEND(${this.value.bond.amendSeq+ 1})`;
                        }
                    }
                }
            } else {
                if(this.value.bond.amendCount == 0){
                    this.$message({ type: 'warning', message: '해외수수료건은 결재완료된 국내수수료 전표 없이는 작성 할 수 없습니다.' });
                    this.setArea('LOCAL');
                }else{
                    if(this.value.bond.amendSeq == 0){
                        this.value.bond.amendText = '신규';
                    }else{
                        this.value.bond.amendText = `AMEND(${this.value.bond.amendSeq+ 1})`;
                    }
                }
            }
            if(!this.value.bond.refNo) {
                this.value.bond.amendText = '';
            }
        },
        /**
         * TODO : 함수로 뺄 것. 국/해외에 따른 은행 지정 추가해야함.
         * @param {*} area 
         */
        setArea(area) {
            this.value.bond.type = area || 'LOCAL';
        },
        bondInit() {
            this.$confirm(`초기화 하시겠습니까`, `확인`, {
                    confirmButtonText: '예',
                    cancelButtonText: '아니오',
                    type: 'warning',
                }).then(() => {
                    this.value.bond = {
                        splitEtcYn: 'N', //분할 및 기타비용
                        bondGubun: '', //BOND 종류
                        type: 'LOCAL', //수수료 유형 (국내 or 해외)
                        benCountryCd: '', //수익자 국가 코드
                        benCountry: '', //수익자 국가
                        refNo: '', //REF NO. (신규/AMEND)
                        amendText: '신규',//REF NO. (신규/AMEND)
                        projectNm: '',//PJT 명
                        customerNm: '', //고객명
                        openingDt: '', //개설일
                        currencyCd: '',//통화
                        currencyNm: '',//통화
                        guaranteeAmt: 0, //보증금액
                        maturityDt: '', //만기일
                        rate: 0.0, //요율
                        localBankNm: '', //은행
                        intBankNm: '', //은행
                        budget: 0, //예산
                        amendSeq: 0,
                        amendCount: 0
                    }
                    this.$message({
                        type: 'success',
                        message: '초기화 하였습니다.'
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '취소하였습니다.'
                    });
            });
        }
    },
    watch: {
        'value.bond.rate'(input) {
            const reg = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z]/;
            // 한글, 영문 체크
            if(reg.exec(input)!==null) {
                // /[\-0-9]+\.[0-9]+/g
                this.value['bond']['rate'] = parseFloat(input.replace(/[^0-9]+\.?[0-9]/g, 0));
            }
            // .... 만 입력하게 될 경우 체크
            if(isNaN(parseFloat(input))) {
                this.value['bond']['rate'] = 0;
            }
            
        },
        'value.bond.guaranteeAmt'(input) {
            const reg = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z]/;
            // 한글, 영문 체크
            if(reg.exec(input)!==null) {
                this.value['bond']['guaranteeAmt'] = input.replace(/[^0-9]/g, 0);
            }
            // .... 만 입력하게 될 경우 체크
            // if(isNaN(parseFloat(input))) {
            //     this.value['bond']['guaranteeAmt'] = 0;
            // }
            
        },
        
    }
}
</script>