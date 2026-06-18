<template>
    <div class="table-area">
        <div class="table-a fixed-th">
            <div class="table-name">
                <h3 class="ico_table_name">기본정보</h3>
            </div>
            <table class="table">
                <tbody>
                    <tr>
                        <th>그룹번호</th>
                        <td><el-input type="text" v-model="value.slipGroupNumber" disabled ></el-input></td>
                        <th>전표번호</th>
                        <td><el-input type="text" v-model="value.slipNo" disabled ></el-input></td>
                        <th>진행단계</th>
                        <td>
                            <el-select v-model="value.slipStatus" disabled style="width: 100%;">
                                <el-option v-for="(item, idx) in slipStatusList" :key="idx" :label="item.value" :value="item.key">
                                </el-option>
                            </el-select>
                            <!-- <el-input v-model="value.slipStatus" type="text" disabled >
                            </el-input> -->
                        </td>
                    </tr>
                    <tr>
                        <th class="tp-a">회계일자</th>
                        <td>
                            <el-date-picker
                                v-model="value.postingDt"
                                type="date"
                                format="YYYY-MM-DD"
                                value-format="YYYYMMDD"
                                :readonly="true"
                                style="width: 100%;">
                            </el-date-picker>
                        </td>
                        <th class="tp-a">직원</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.empNo" style="width: 30%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(70% - 3px);">
                                <el-input type="text" v-model="value.empNm" :readonly="true">
                                </el-input>
                            </div>
                        </td>
                        <th class="tp-a">부서</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.deptCd" style="width: 30%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(70% - 3px);">
                                <el-input type="text" v-model="value.deptNm" disabled></el-input>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th class="tp-a">거래유형</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.trxTypeCode" style="width: 30%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(70% - 3px);">
                                <el-input type="text" v-model="value.trxTypeName" :readonly="true"></el-input>
                            </div>
                        </td>
                        <th class="tp-a" width="12%">세무증빙유형</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.taxEvidenceType" style="width: 30%" disabled></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(70% - 3px);">
                                <el-input type="text" v-model="value.taxEvidenceTypeName" :readonly="true"></el-input>
                            </div>
                        </td>
                        <th>세금계산서</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.taxbillSuId" style="width: 30%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(60% - 3px);">
                                <el-input type="text" v-model="value.taxSmartbillNo" clearable :readonly="true"></el-input>
                            </div>
                            <el-link type="info" v-if="value.taxSmartbillNo" style="width:10%;" @click="taxbillViewer(value.taxSmartbillNo)">
                                <i class="el-icon-search el-icon-document"></i>
                            </el-link>
                        </td>
                    </tr>
                    <tr>
                        <th>사업장</th>
                        <td>
                            <el-input type="text" v-model="value.taxLocationName" clearable :readonly="true"></el-input>
                        </td>
                        <th class="tp-a">통화</th>
                        <td>
                            <el-select 
                                v-model="value.curCd"
                                style="width: 100%;"
                                disabled>
                                <el-option 
                                    v-for="(item, idx) in options['FRGN_CUR_CD']"
                                    :key="idx"
                                    :label="item.currencyCode"
                                    :value="item.currencyCode">
                                </el-option>
                            </el-select>
                        </td>
                        <th class="tp-a">환율일자/환율</th>
                        <td>
                            <el-date-picker
                                class="mr3"
                                type="date"
                                format="YYYY-MM-DD"
                                value-format="YYYYMMDD"
                                style="width: 60%"
                                v-model="value.exchangeDate"
                                :readonly="true">
                            </el-date-picker>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2 mr3" style="width:calc(30% - 3px)">
                                <el-input v-model="value.excRt" :readonly="true"></el-input>
                            </div>
                            <div class="ip-box ip-box-w02 mb2 rs-mt2" style="text-align: center;">
                                <el-checkbox v-model="value.exchangeRateType" :readonly="true" size="medium" true-label="Y" false-label="N" style="width:calc(10% - 3px); text-align: center;"></el-checkbox>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>헤더계정</th>
                        <td>
                            <el-input type="text" v-model="value.acctName" class="mr3" disabled></el-input> 
                        </td>
                        <th>전표금액</th>
                        <td>
                            <el-input type="text" v-model="value.totAmt" class="mr3" disabled></el-input>
                        </td>
                        <th width="12%">전표금액(KRW)</th>
                        <td>
                            <el-input type="text" v-model="value.totAmtKrw" class="mr3" disabled></el-input>
                        </td>
                    </tr>
                    <tr>
                        <th>정산구분</th>
                        <td colspan="1">
                            <el-radio-group v-model="value.prepaymentApplyFlag" disabled>
                                <el-radio label="N">미정산</el-radio>
                                <el-radio label="K">선급금정산</el-radio>
                            </el-radio-group>
                        </td>
                        <th class="tp-a">선급금정산</th>
                        <td>
                            <el-input class="input-amt" type="text" v-model.number="value.amountToApply" :readonly="true"></el-input>
                            <!-- <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(30% - 71px)">
                                <el-button icon="el-icon-delete" @click="clearPrepayment" :disabled="value.prepaymentApplyFlag != 'K'"></el-button>
                            </div> -->
                        </td>
                        <th>실지급금액</th>
                        <td colspan="2">
                            <el-input class="input-amt mr3" type="text" v-model="value.curCd" style="width: 40%;" disabled></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(60% - 3px)">
                                <el-input type="text" class="input-amt" v-model.number="value.realAmt" :readonly="true"></el-input>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th class="tp-a">적요</th>
                        <td colspan="6">
                            <el-input v-model="value.description" minlength="1" maxlength="240" show-word-limit type="text" class="mr3" :readonly="true"></el-input> 
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="table-name">
                <h3 class="ico_table_name">공급자 & 결제조건</h3>
            </div>
            <table class="table">
                <tbody>
                    <tr>
                        <th class="tp-a">거래처</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.vatRegistrationNum" style="width: 40%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(60% - 3px)">
                                <el-input type="text" v-model="value.integrationVendorName" :readonly="true"></el-input>
                            </div>
                        </td>
                        <th class="tp-a">결제조건</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.termName" style="width: 40%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(60% - 3px)">
                                <el-input type="text" v-model="value.termDescription" :readonly="true"></el-input>
                            </div>
                        </td>
                        <th class="tp-a">결제예정일</th>
                        <td>
                            <el-date-picker
                                v-model="value.termDueDate"
                                type="date"
                                format="YYYY-MM-DD"
                                value-format="YYYYMMDD"
                                :readonly="true">
                            </el-date-picker>
                        </td>
                    </tr>
                    <tr>
                        <th class="tp-a">계좌정보</th>
                        <td>
                            <el-input class="mr3" v-model="value.bankAcct" type="text" style="width: 50%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(50% - 3px)">
                                <el-input type="text" v-model="value.bankAccountName" :readonly="true"></el-input>
                            </div>
                        </td>
                        <th width="11%">상이계좌여부</th>
                        <td>
                            <el-radio-group v-model="value.globalAttribute8" disabled>
                                <el-radio label="N">N</el-radio>
                                <el-radio label="Y">Y</el-radio>
                            </el-radio-group>
                        </td>
                        <th width="11%">어음만기예정일</th>
                        <td v-if="value.noteFlag === 'Y'">
                            <el-date-picker
                                v-model="value.maturityDate"
                                type="date"
                                format="YYYY-MM-DD"
                                value-format="YYYYMMDD"
                                disabled>
                            </el-date-picker>
                        </td>
                        <td v-else></td>
                    </tr>
                    <tr>
                        <th>비고</th>
                        <td>
                            <el-input v-model="value.remark" minlength="1" maxlength="120" show-word-limit class="mr3" type="text" :readonly="true"></el-input>
                        </td>
                        <th width="12%">선급금확인여부</th>
                        <td>
                            <el-radio-group v-model="value.prepaymentYn" disabled>
                                <el-radio label="N">N</el-radio>
                                <el-radio label="Y">Y</el-radio>
                            </el-radio-group>
                        </td>
                        <th width="12%">반제예정일</th>
                        <td>
                            <el-date-picker
                                v-model="value.repaymentDueDate"
                                type="date"
                                format="YYYY-MM-DD"
                                value-format="YYYYMMDD"
                                disabled>
                            </el-date-picker>
                        </td>
                    </tr>
                </tbody>
            </table>
            
            <component ref="sub" :is="subHeaderComponent" :status="status" @input="$forceUpdate()" v-model="value" :readOnly="true" v-show="status.loaded" />
            
            <component :is="assistantSubHeaderComponent" :slipTypeCd="value.slipTypeCd" :status="status" @input="$forceUpdate()" v-model="value" :readOnly="true" v-show="status.assistantloaded"/>
        </div>
    </div>
</template>

<script>

/** 
 * * 서브헤더의 화면 정의 시 거래유형에서의 전표유형코드로 작명해서 사용.
 * * * * * * * * * * * HD.vue 컴포넌트의 computed subHeaderComponent 리턴에서 사용.
 * * EXPEND : 경조금
 * * TRAFFIC: 여비교통비
 * * TRIP : 출장비
 * * BOND : BOND
 * * 원천세 : AWT
 * * 잡급비 : ETCAWT
 * * 법인카드(임직원-삼성카드) : CARD
 * * 구매물대(미지급금) : PO
 * * 수입물대 : IM
 * * 인사전표 : HR
 */
// import COMMON from '@/components/accrualSlip/SubHD/Common.vue'; /** 디폴트 */
import EXPEND from '@/components/accrualSlip/SubHD/Expend.vue'; /** 경조금 */
import TRAFFIC from '@/components/accrualSlip/SubHD/Traffic.vue'; /** 여비교통비 */
import TRIP from '@/components/accrualSlip/SubHD/Trip.vue'; /** 출장비 */
import BOND from '@/components/accrualSlip/SubHD/Bond.vue'; /** BOND */
import AWT from '@/components/accrualSlip/SubHD/Awt.vue'; /** 원천세 */
import ETCAWT from '@/components/accrualSlip/SubHD/EtcAwt.vue'; /** 잡급비 */
import CARD from '@/components/accrualSlip/SubHD/Card.vue'; /** 법인카드(임직원-삼성카드) */
import PO from '@/components/accrualSlip/SubHD/Po.vue'; /** 구매물대(미지급금) */
import IM from '@/components/accrualSlip/SubHD/Im.vue'; /** 수입물대 */
import HR from '@/components/accrualSlip/SubHD/Hr.vue'; /** 인사전표 */

/**
 * * 보조 컴포넌트
 */
import AssistantVAT from '@/components/accrualSlip/Assistant/AssistantVAT.vue'; /** 부가세 */

const numFormats = (curCd, decimal) => {
    let format = `0,000`;
    if(decimal > 0 && curCd != 'KRW') {
        format = `${format}.` + '#'.repeat(decimal);
    }
    return format;
}

export default {
  compatConfig: { MODE: 2 },
    components: {
        EXPEND, TRAFFIC, TRIP, BOND, AWT, ETCAWT, CARD, PO, IM, AssistantVAT, HR
    },
    props: {
        values: {
            Type: Object,
            required: true,
        },
        slipNo: {
            type: String,
            required: false,
        }
    },
    data() {
        return {
            slipStatusList: [
                {key: 'SV', value: '저장됨'},
                {key: 'VE', value: '검증오류'},
                {key: 'VC', value: '검증됨'},
                {key: 'AP', value: '결재중'},
                {key: 'AR', value: '결재반려'},
                {key: 'CP', value: '검인중'},
                {key: 'CR', value: '검인반려'},
                {key: 'CC', value: '검인됨'},
                {key: 'IE', value: '이관오류'},
                {key: 'IC', value: '이관완료'},
                {key: 'SD', value: '전표삭제'},
                {key: 'FP', value: '검인중(ERP)'},
                {key: 'FH', value: '검인보류(ERP)'},
                {key: 'FC', value: '검인됨(ERP)'},
                {key: 'FR', value: '검인반려(ERP)'},
                {key: 'SC', value: '전표취소'},
            ], //진행단계 프론트 코드화 하드코딩
            options: { //화면 options 리스트 객체 담는 object
                FRGN_CUR_CD: [], //통화목록
            },
            status: { //컴포넌트 활성화 상태 object
                loaded: true,
                assistantloaded: false,
            },
            value: {},
            rowCount: 0
        }
    },
    computed: {
        subHeaderComponent() {
            return this.value.slipTypeCd == 'COMMON' ? '': ((this.value.slipTypeCd === 'ACARD' || this.value.slipTypeCd === 'MCARD') ? 'CARD' : this.value.slipTypeCd) ;
        },
        assistantSubHeaderComponent() {
            let compnt = '';
            /**
             * * 세무증빙유형에서 부가세라면 세금계산서를 반드시 선택해야함.
             * * 세무증빙유형에서의 부가세로 세금계산서 활성/비활성
             * * 세금계산서가 존재할 경우에만 부가세 내역 컴포넌트 추가
             */
             if(this.value.lineAttribute1 === 'V' && !['MCARD', 'ACARD', 'CARD', 'TRIP'].includes(this.value.slipTypeCd) ) { /** 부가세인 경우 컴포넌트 세팅 */
                compnt = 'AssistantVAT'
            }
            return compnt
        }
    },
    created() {
        
        if(this.slipNo) {
            
            this.getHeaderData()
            .then(data => {
                
                this.$parent.$parent.data = Object.assign({}, this.value, this.values, data);
                
                const {integrationVendorNum, bankAccountId, slipCurrencyCode, exchangeRate, enteredAmount, accountedAmount} = data;
                if(integrationVendorNum && bankAccountId) {
                    // 공급자 & 계좌정보
                    const params = {
                        integrationVendorNum,
                        bankAccountId,
                        currencyCode: slipCurrencyCode
                    };
                    this.$http.post(`/api/vendor/bankList`, params)
                    .then(res => res.data)
                    .then(data => {
                        if(!!data && data.length > 0) {
                            const { bankAccountId, bankAccountName, bankAccountNumber, bankName } = data.filter(item => item.primaryFlag == 'Y')[0];
                            this.value.bankAccountId = bankAccountId;
                            this.value.bankAccountName = bankAccountName;
                            this.value.bankAccountNumber = bankAccountNumber;
                            this.value.bankName = bankName;
                            
                            this.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                        }
                    });
                }
            });

        } else {
            this.value = this.values;
            
            const {exchangeRate, slipCurrencyCode, enteredAmount, accountedAmount} = this.value;
            this.value.excRt = exchangeRate || 1;
            const AMT_LENGTH = this.options['FRGN_CUR_CD'].filter(f => f.currencyCode === slipCurrencyCode )[0]?.precision || 0;
            this.value.totAmt = this.$numeral(enteredAmount).format(numFormats(slipCurrencyCode,AMT_LENGTH));
            this.value.totAmtKrw = this.$numeral(accountedAmount).format(numFormats(slipCurrencyCode,AMT_LENGTH));
            
            // this.value.realAmt = this.$numeral(this.$numeral(this.value.totAmtKrw).value() - this.$numeral(this.value.amountToApply || 0).value()).format(numFormats(slipCurrencyCode,AMT_LENGTH));
            
            if(this.value.slipTypeCd === 'EXPEND') {
                this.value.expend = this.value.slipExpendDto;
            } else if(this.value.slipTypeCd === 'TRAFFIC') {
                
                this.value.traffic = this.value.slipTrafficHdDto;
                
            } else if(this.value.slipTypeCd === 'TRIP') {
                this.value[`trip`] = this.value.slipBusinessTripDtoList.map(x =>{
                    x.slipHeaderId = x.detailSlipHeaderId;
                    x.strPostingDate = x.detailGlDt;
                    x.slipNo = x.detailSlipNo;
                    x.slipType = x.detailTrxType;
                    x.integrationVendorNum = x.detailVendorId;
                    x.erpVendorName = x.detailVendorNm;
                    x.usedCur = x.detailUsedCur;
                    x.usedAmt = x.detailUsedAmt;
                    return x;
                });
            } else if(this.value.slipTypeCd === 'BOND') {
                this.value[`bond`] = this.value.bondHeaderDto;
            }
        }
        
    },
    mounted() {
        
    },
    methods: {
        getHeaderData() {
            this.$http.get(`/api/exchange/currency/list`)
            .then(res => {
                this.options['FRGN_CUR_CD'] = res.data.map(x => {
                    x.gubun = x.gubun || 99; //우선순위 Null 값에 대한 후순위로 변경
                    return x;
                }).map(x => x).sort((a, b) => {
                    return a.gubun - b.gubun; 
                });
            });
            return new Promise((resolve, reject) => {
                this.$http.get(`/api/slip/header/${this.slipNo}`)
                .then(res => res.data)
                .then(data => {
                    const { headerAcctCode, headerAcctName, enteredAmount, accountedAmount, slipCurrencyCode,
                            exchangeRate, exchangeRateType, exchangeDate,
                            evidenceVatRegistrationNum, taxbillSupplyAmount, taxbillTaxAmount, taxbillTotalAmount, 
                            bankName, vatRegistrationNum, bankAccountId, integrationVendorNum, repaymentDueDate, termDueDate, maturityDate,
                            lineAttribute1, lineAttribute3, ttypeInterfaceModule,
                            taxCode, vatTaxId , slipTypeCd} = data;

                    // Object.assign(this.value, data);
                    this.value = Object.assign({}, data, {expend: {}, traffic: {}, trip: [], bond: {} , awt: [], etcawt: [], bankAcct: ''});
                    
                    // this.value = data.map(x => {
                    //     x.awt = [];
                    //     x.etcawt = [];
                    //     x.bankAcct = '';
                    //     return x;
                    // });
                    this.value.slipTypeCd = slipTypeCd;
                    this.value.ttypeInterfaceModule = ttypeInterfaceModule;
                    // 기본 헤더
                    this.value.acctCode = headerAcctCode;
                    this.value.acctName = headerAcctName;
                    this.value.curCd = slipCurrencyCode;
                    this.value.exchangeRateType = exchangeRateType || 'Y';
                    this.value.exchangeDate = this.$moment(exchangeDate).format("YYYYMMDD") || this.$moment().format("YYYYMMDD");
                    // this.value.repaymentDueDate = this.$moment(repaymentDueDate).format("YYYYMMDD") || this.$moment().format("YYYYMMDD");
                    this.value.termDueDate = this.$moment(termDueDate).format("YYYYMMDD") || this.$moment().format("YYYYMMDD");
                    this.value.maturityDate = this.$moment(maturityDate).format("YYYYMMDD") || this.$moment().format("YYYYMMDD");

                    this.value.excRt = exchangeRate || 1;
                    const AMT_LENGTH = this.options['FRGN_CUR_CD'].filter(f => f.currencyCode === slipCurrencyCode )[0]?.precision || 0;
                    this.value.totAmt = this.$numeral(enteredAmount).format(numFormats(slipCurrencyCode,AMT_LENGTH));
                    this.value.totAmtKrw = this.$numeral(accountedAmount).format(numFormats(slipCurrencyCode,0));
                    
                    if(slipCurrencyCode === 'KRW') {
                        this.value.realAmt = this.$numeral(this.$numeral(this.value.totAmtKrw).value() - this.$numeral(this.value.amountToApply || 0).value()).format(numFormats(slipCurrencyCode,AMT_LENGTH));
                    } else {
                        this.value.realAmt = this.$numeral(this.$numeral(this.value.totAmt).value() - this.$numeral(this.value.amountToApply || 0).value()).format(numFormats(slipCurrencyCode,AMT_LENGTH));
                    }
                    // 부가세 헤더
                    this.value.evidenceVendorVat = evidenceVatRegistrationNum;
                    this.value.taxbillSupplyAmt = taxbillSupplyAmount;
                    this.value.taxbillTaxAmt = taxbillTaxAmount;
                    this.value.taxbillTotalAmt = taxbillTotalAmount;
                    this.value.taxRateCode = taxCode;
                    this.value.taxRateId = vatTaxId;
                    // this.value.supplyAmount = this.value.supplyAmount || 0; //공급가액
                    // this.value.taxAmount = this.value.taxAmount || 0; //세액
                    // this.value.totalAmount = this.value.totalAmount || 0; //합계

                    this.value.supplyAmount = this.value.taxbillSupplyAmt;
                    this.value.taxAmount = (parseInt(this.value.supplyAmount) * (this.value.percentageRate / 100)); //실 세금
                    this.value.totalAmount = parseInt(this.value.supplyAmount) + parseInt(this.value.supplyAmount * (this.value.percentageRate / 100)); // 실 금액 합계

                    //재계산 필요 없이 세금계산서 금액 그대로 표기 (View 참조용)
                    this.value.vSupplyAmount = this.value.taxbillSupplyAmt; //(View 용)
                    this.value.vTaxAmount = this.value.taxbillTaxAmt; //(View 용)
                    this.value.vTotalAmount = this.value.taxbillTotalAmt; //(View 용)
                    // this.value.vSupplyAmount = this.value.taxbillSupplyAmt; //(View 용)
                    // this.value.vTaxAmount = Math.round((parseInt(this.value.supplyAmount) * (this.value.percentageRate / 100))); //(View 용)
                    // this.value.vTotalAmount = parseInt(this.value.vSupplyAmount) + parseInt(this.value.vTaxAmount); //(View 용)
                    // if(integrationVendorNum && bankAccountId) {
                    //     // 공급자 & 계좌정보
                        
                    //     const params = {
                    //         integrationVendorNum,
                    //         bankAccountId,
                    //         currencyCode: slipCurrencyCode
                    //     };
                    //     this.$http.post(`/api/vendor/bankList`, params)
                    //     .then(res => res.data)
                    //     .then(data => {
                    //         if(!!data && data.length > 0) {
                    //             const { bankAccountId, bankAccountName, bankAccountNumber, bankName } = data.filter(item => item.primaryFlag == 'Y')[0];
                    //             this.value.bankAccountId = bankAccountId;
                    //             this.value.bankAccountName = bankAccountName;
                    //             this.value.bankAccountNumber = bankAccountNumber;
                    //             this.value.bankName = bankName;
                    //             this.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                    //         }
                    //     });
                    // }

                    // 서브 컴포넌트에 대한 정의값
                    if(this.value.slipTypeCd === 'EXPEND') {
                        //경조금 객체 초기화를 위한 상세 API호출
                        this.$http.get(`/api/expend/slip/${this.slipNo}`)
                        .then(res => res.data)
                        .then(data => {
                            if(data) {
                                const {expendDt} = data;
                                this.value['expend'] = data;
                                this.value['expend'].expendDt = this.$moment(expendDt).format("YYYYMMDD") || this.$moment().format("YYYYMMDD");
                            } else {
                                this.value['expend'].expendDt = ''
                            }
                        });
                    } else if(this.value.slipTypeCd === 'TRAFFIC') {
                        //this.value[`traffic`] = data;
                        // const { tripObj, tripCd, temp2 } = data;
                        // this.value[`traffic`].tripObj = tripObj;
                        // this.value[`traffic`].tripCd = tripCd;
                        // this.value[`traffic`].temp2 = temp2;
                        this.value[`traffic`] = data;
                    } else if(this.value.slipTypeCd === 'TRIP') {
                        this.value[`trip`] = data.slipBusinessTripList.map(x =>{
                            x.slipHeaderId = x.detailSlipHeaderId;
                            x.strPostingDate = x.detailGlDt;
                            x.slipNo = x.detailSlipNo;
                            x.slipType = x.detailTrxType;
                            x.integrationVendorNum = x.detailVendorId;
                            x.erpVendorName = x.detailVendorNm;
                            x.usedCur = x.detailUsedCur;
                            x.usedAmt = x.detailUsedAmt;
                            return x;
                        });
                    } else if(this.value.slipTypeCd === 'BOND') {
                        this.value[`bond`] = this.value[`bond`];
                    }

                    resolve(this.value)
                })
                .catch(err => reject(err))
            });
        },
        taxbillViewer(issueId) {
            
            const url = `http://197.200.1.19:10002/addservice/view/XXSB_DTI_PRINT_VIEW_NTS.asp?issue_id=${issueId}&SBTYPE=AP`;
            //800 x 500
            window.open(url, '_blank'); 
        },
    },
    watch: {
        /**
         * * 세금계산서 데이터 들어오면 부가세 컴포넌트 활성
         * @param {*} val 
         */
         'value.lineAttribute1'(val) {
            /** 세금계산서가 존재할 경우 서브헤더의 보조 컴포넌트 활성화 (세무증빙유형에서의 부가세 체크 추가하여야함.)*/
            if(val === 'V' && !['MCARD', 'ACARD', 'CARD', 'TRIP'].includes(this.value.slipTypeCd)) {
                this.status.assistantloaded = true;
            } else {
                this.status.assistantloaded = false;
            }
        },
    }
}
</script>