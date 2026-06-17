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
                                unlink-panels
                                :pickerOptions="posdtPickerOptions"
                                type="date"
                                format="YYYYMMDD"
                                value-format="YYYYMMDD"
                                :readonly="editabledState"
                                @change="exchangeRateEvent"
                                style="width: 100%;">
                            </el-date-picker>
                        </td>
                        <th class="tp-a">직원</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.empNo" style="width: 30%" disabled></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(70% - 3px);">
                                <el-input type="text" v-model="value.empNm" clearable @keypress.native.enter="empOpenEnterModal(value.empNm)" :readonly="value.slipStatus || value.slipStatus !== ''">
                                    <i v-if="!value.slipStatus" class="el-icon-search el-input__icon" slot="suffix" @click="empOpenModal(value.empNm)"></i>
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
                                <el-input type="text" v-model="value.trxTypeName" clearable @keypress.native.enter="trxOpenEnterModal(value.trxTypeName)" :readonly="editabledState">
                                    <i v-if="!editabledState" class="el-icon-search el-input__icon" slot="suffix" @click="trxOpenModal(value.trxTypeName)"></i>
                                </el-input>
                            </div>
                        </td>
                        <th class="tp-a" width="12%">세무증빙유형</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.taxEvidenceType" style="width: 30%" disabled></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(70% - 3px);">
                                <el-input type="text" v-model="value.taxEvidenceTypeName" clearable @keypress.native.enter="taxEvidenceCodeEnterPop(value.taxEvidenceTypeName)" :readonly="editabledState">
                                    <i v-if="!editabledState" class="el-icon-search el-input__icon" slot="suffix" @click="taxEvidenceCodePop(value)"></i>
                                </el-input>
                            </div>
                        </td>
                        <th :class="isTaxReadOnly ? '': 'tp-a'">세금계산서</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.taxbillSuId" style="width: 30%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(60% - 3px);">
<!--                                <el-input type="text" v-model="value.taxSmartbillNo" clearable :readonly="!['SV', 'VC', 'VE', ''].includes(value.slipStatus) || (!['PO', 'IM'].includes(value.slipTypeCd) && (editabledState || isTaxReadOnly))">-->
                                <el-input class="input-bg-white" type="text" v-model="value.taxSmartbillNo" clearable readonly>
                                    <i v-if="['SV', 'VC', 'VE', ''].includes(value.slipStatus) && (['PO', 'IM'].includes(value.slipTypeCd) || (!editabledState && !isTaxReadOnly))" class="el-icon-delete el-input__icon" slot="suffix" @click="clearTaxSmartbillNo()" style="cursor: pointer;"/>
                                    <i v-if="['SV', 'VC', 'VE', ''].includes(value.slipStatus) && (['PO', 'IM'].includes(value.slipTypeCd) || (!editabledState && !isTaxReadOnly))" class="el-icon-search el-input__icon" slot="suffix" @click="taxMainOpenModal()"/>
                                </el-input>
                            </div>
                            <el-link type="info" v-if="value.taxSmartbillNo" style="width:10%;" @click="taxbillViewer(value.taxSmartbillNo)">
                                <i class="el-icon-search el-icon-document"></i>
                            </el-link>
                        </td>
                    </tr>
                    <tr>
                        <th>사업장</th>
                        <td>
                            <el-input class="input-bg-white" type="text" v-model="value.taxLocationName" clearable readonly style="width: 100%;">
                                <i v-if="!editabledState" class="el-icon-search el-input__icon" slot="suffix" @click="openBizPop"></i>
                            </el-input>
                        </td>
                        <th class="tp-a">통화</th>
                        <td>
                            <el-select 
                                v-model="value.curCd"
                                @change="exchangeUpdate(value, true)"
                                :disabled="editabledState"
                                style="width: 100%;">
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
                                format="YYYYMMDD"
                                value-format="YYYYMMDD"
                                style="width: 60%"
                                v-model="value.exchangeDate"
                                @change="exchangeUpdate(value, false)"
                                :disabled="value.exchangeRateType == 'Y'">
                            </el-date-picker>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2 mr3" style="width:calc(30% - 3px)">
                                <el-input v-model="value.excRt" :disabled="value.exchangeRateType == 'N'" :readonly="editabledState" @input="(val) => {
                                    value.totAmtKrw = Math.floor((value.totAmt * value.excRt));
                                }"></el-input>
                            </div>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="text-align: center;">
                                <el-checkbox v-model="value.exchangeRateType" size="medium" true-label="Y" false-label="N" @change="exchangeRateEvent" style="width:calc(10% - 3px); text-align: center;" :disabled="editabledState"></el-checkbox>
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
                            <el-input type="text" v-model="value.totAmtKrw" class="mr3" @change="calcRealAmt" disabled></el-input>
                        </td>
                    </tr>
                    <tr>
                        <th>정산구분</th>
                        <td colspan="1">
                            <el-radio-group v-model="value.prepaymentApplyFlag" @change="prepaymentApplyChanges" :disabled="!['SV', 'VC', 'VE', ''].includes(value.slipStatus) || (!['PO', 'IM'].includes(value.slipTypeCd) && (value.slipStatus == '' || editabledState))">
                                <el-radio label="N">미정산</el-radio>
                                <el-radio label="K">선급금정산</el-radio>
                            </el-radio-group>
                        </td>
                        <th :class="value.prepaymentApplyFlag === 'K' ? 'tp-a' : ''">선급금정산</th>
                        <td>
                            <el-input type="text" v-model.number="value.amountToApply" @input="calcRealAmt" @change="calcRealAmt" clearable :disabled="value.slipHeaderId && value.prepaymentApplyFlag != 'K'" readonly>
                                <i v-if="['SV', 'VC', 'VE', ''].includes(value.slipStatus) || (!['PO', 'IM'].includes(value.slipTypeCd) &&  !editabledState)" class="el-icon-search el-input__icon" slot="suffix" @click="() => { 
                                    if(value.prepaymentApplyFlag == 'K') { 
                                        advancedPop(value)
                                    } }"></i>
                            </el-input>
                            <!-- <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(30% - 71px)">
                                <el-button icon="el-icon-delete" @click="clearPrepayment" :disabled="value.prepaymentApplyFlag != 'K'"></el-button>
                            </div> -->
                        </td>
                        <th>실지급금액</th>
                        <!-- <td colspan="2" class="bd-l-none"> -->
                        <td colspan="2">
                            <el-input class="input-amt mr3" type="text" v-model="value.curCd" style="width: 40%;" clearable disabled></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(60% - 3px)">
                                <el-input type="text" class="input-amt" v-model.number="value.realAmt" clearable readonly></el-input>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th class="tp-a">적요</th>
                        <td colspan="6">
                            <el-input v-model="value.description" minlength="1" maxlength="240" show-word-limit type="text" class="mr3" :readonly="editabledState"></el-input> 
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
                                <el-input type="text" v-model="value.integrationVendorName" @keypress.native.enter="vendorOpenModal(value.integrationVendorName)" clearable :readonly="editabledState">
                                    <i v-if="!editabledState" class="el-icon-search el-input__icon" slot="suffix" @click="vendorOpenModal(value.integrationVendorName)"></i>
                                </el-input>
                            </div>
                        </td>
                        <th class="tp-a">결제조건</th>
                        <td>
                            <el-input class="mr3" type="text" v-model="value.termName" style="width: 40%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(60% - 3px)">
                                <el-input type="text" v-model="value.termDescription" @keypress.native.enter="termOpenEnterModal(value.termDescription)" clearable :readonly="editabledState">
                                    <i v-if="!editabledState" class="el-icon-search el-input__icon" slot="suffix" @click="termOpenModal(value.termDescription)"></i>
                                </el-input>
                            </div>
                        </td>
                        <th class="tp-a">결제예정일</th>
                        <td>
                            <el-date-picker
                                v-model="value.termDueDate"
                                type="date"
                                format="YYYYMMDD"
                                value-format="YYYYMMDD"
                                :readonly="(value.termDueDateChangeFlag == 'N' || editabledState) && !(value.termId == '10295') && !(value.slipTypeCd == 'TRIP' && ['33011', '22041'].includes(value.deptCd))"
                                style="width: 100%;">
                              <!-- 결재예정일 예외사항
                                    1. 결재조건 IJE_원천세(10295)일 경우
                                    2. 출장비이면서 전선시공팀, 변압기시공팀일 경우
                               -->
                            </el-date-picker>
                        </td>
                    </tr>
                    <tr>
                        <th :class="value.vendorAcctCheck == 'Y' ? 'tp-a' : ''">계좌정보</th>
                        <td>
                            <el-input class="mr3" v-model="value.bankAcct" type="text" style="width: 50%" disabled ></el-input>
                            <div class="ip-box ip-box-w02 rs-mt2 mb2" style="width:calc(50% - 3px)">
                                <el-input type="text" v-model="value.bankAccountName" @keypress.native.enter="vendorBanksOpenModal(value.bankAccountName)" clearable :readonly="editabledState">
                                    <i v-if="!editabledState" class="el-icon-search el-input__icon" slot="suffix" @click="vendorBanksOpenModal(value.bankAccountName)"></i>
                                </el-input>
                            </div>
                        </td>
                        <th width="11%">상이계좌여부</th>
                        <td>
                            <el-radio-group v-model="value.globalAttribute8" :disabled="editabledState">
                                <el-radio label="N">N</el-radio>
                                <el-radio label="Y">Y</el-radio>
                            </el-radio-group>
                        </td>
                        <th width="11%" :class="value.noteFlag === 'Y' ? 'tp-a': ''">{{value.noteFlag === 'Y' ? '어음만기예정일' : ''}}</th>
                        <td v-if="value.noteFlag === 'Y'">
                            <el-date-picker
                                v-model="value.maturityDate"
                                type="date"
                                format="YYYYMMDD"
                                :readonly="editabledState"
                                value-format="YYYYMMDD"
                                style="width: 100%;">
                            </el-date-picker>
                        </td>
                        <td v-else></td>
                    </tr>
                    <tr>
                        <!-- <th>비고</th>
                        <td>
                            <el-input v-model="value.remark" minlength="1" maxlength="120" show-word-limit class="mr3" type="text" :readonly="editabledState"></el-input>
                        </td> -->
                        <th width="12%">선급금확인여부</th>
                        <td> 
                            <el-radio-group v-model="value.prepaymentYn" :disabled="!['SV', 'VC', 'VE', ''].includes(value.slipStatus) || (!['PO', 'IM'].includes(value.slipTypeCd) && editabledState)">
                                <el-radio label="N">N</el-radio>
                                <el-radio label="Y">Y</el-radio>
                            </el-radio-group>
                        </td>
                        <th width="11%">반제예정일</th>
                        <td>
                            <el-date-picker
                                v-model="value.repaymentDueDate"
                                type="date"
                                format="YYYYMMDD"
                                :readonly="editabledState"
                                value-format="YYYYMMDD"
                                style="width: 100%;">
                            </el-date-picker>
                        </td>
                    </tr>
                </tbody>
            </table>
            <!-- 거래유형에 따른 추가 헤더 컴포넌트 -->
            <component ref="subHD" :is="subHeaderComponent" v-model="value" :status="status" @input="$forceUpdate()" :readOnly="editabledState" v-show="status.loaded" />
            <!-- 세금계산서 속성에 따른 보조 서브헤더 컴포넌트 -->
            <component :is="assistantSubHeaderComponent" v-model="value" :status="status" @input="$forceUpdate()" :readOnly="editabledState" v-if="status.assistantloaded" />
        </div>
    </div>
</template>


<script>
import main from '@/components/accrualSlip/script.js';
/**
 * * (임시) 자리수 표기
 * @param {*} curCd 
 * @param {*} decimal 
 */
 const numFormats = (curCd, decimal) => {
    let format = `0,000`;
    if(decimal > 0 && curCd != 'KRW') {
        format = `${format}.` + '#'.repeat(decimal);
    }
    return format;
}
export default {
    // mixins: [main],
    extends: main,
    computed: {
        subHeaderComponent() {
            return this.value.slipTypeCd == 'COMMON' ? '': ((this.value.slipTypeCd === 'ACARD' || this.value.slipTypeCd === 'MCARD') ? 'CARD' : this.value.slipTypeCd) ;
            // return `BOND`;
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
        },
        /**
         * 오픈되어진 회계일자 범위내에서만 표시되도록 flag
         */
        posdtPickerOptions() {
            return {
                disabledDate: this.disabledDate
            }
        },
        /**
         * readOnly : UI 활성여부
         * @returns false (활성) / true (비활성)
         * * 구입/수입 물대의 경우 수정불가
         */
         editabledState() {
            if(!this.value.slipStatus) return false;

            if(['PO', 'IM'].includes(this.value.slipTypeCd)) return true;

            return !['SV', 'VC', 'VE', ''].includes(this.value.slipStatus);
        }
    },
    created() {
        // this.setDefaultTaxSlip({
        //     empNo: '410451',
        //     trxTypeCd: 'SPAP001',
        //     evidenceCode: '110',
        //     evidenceName: '매입전자세금계산서'
        // })
        this.init();
    },
    mounted() {
        const { slipHeaderId } = this.$route.params;
        if(slipHeaderId) {
            this.initialize('all')
            .then(() => {
                this.$http.get(`/api/slip/header/${slipHeaderId}`)
                .then(res => res.data)
                .then(data => {

                    const { headerAcctCode, headerAcctName, enteredAmount, accountedAmount, slipCurrencyCode,
                        exchangeRate, exchangeRateType, exchangeDate,
                        evidenceVatRegistrationNum, taxbillSupplyAmount, taxbillTaxAmount, taxbillTotalAmount, 
                        bankName, vatRegistrationNum, bankAccountId, integrationVendorNum, repaymentDueDate, termDueDate, maturityDate,
                        lineAttribute1, lineAttribute3, ttypeInterfaceModule,
                        taxCode, vatTaxId, deptCd, slipTypeCd, fileCnt, remark, amountToApply, evidenceDate } = data;

                    this.$bus.emit('setGridColDef', slipTypeCd);
                    
                    // this.$parent.evidFileSize = fileCnt;
                    
                    /**
                     * ! *** 가장 먼저 실행되어야함. (데이터 초기화 이후 재데이터 세팅.)
                     */
                    this.value.ttypeInterfaceModule = ttypeInterfaceModule;
                    this.taxReadOnly(lineAttribute1, lineAttribute3);

                    Object.assign(this.value, data);
                    //변수가 맞지 않는 부분 세팅
                    // 기본 헤더
                    this.value.actualDeptCode = deptCd;
                    this.value.acctCode = headerAcctCode;
                    this.value.acctName = headerAcctName;
                    this.value.curCd = slipCurrencyCode;
                    this.value.exchangeRateType = exchangeRateType || 'Y';
                    this.value.exchangeDate = this.$moment(exchangeDate).format("YYYYMMDD") || this.$moment().format("YYYYMMDD");
                    // this.value.repaymentDueDate = this.$moment(repaymentDueDate).format("YYYYMMDD") || this.$moment().format("YYYYMMDD");
                    this.value.termDueDate = this.$moment(termDueDate).format("YYYYMMDD") || this.$moment().format("YYYYMMDD");
                    this.value.maturityDate = this.$moment(maturityDate).format("YYYYMMDD") || this.$moment().format("YYYYMMDD");

                    if(evidenceDate) { //부가세 작성년월일
                        this.value.evidenceDate = this.$moment(evidenceDate).format("YYYYMMDD")
                    }

                    // this.value.amountToApply = this.$numeral(amountToApply).format('#,#');
                    this.value.excRt = exchangeRate || 1;
                    const AMT_LENGTH = this.options['FRGN_CUR_CD'].filter(f => f.currencyCode === slipCurrencyCode )[0]?.precision || 0;
                    this.value.amountToApply = this.$numeral(amountToApply || 0 ).format(numFormats(slipCurrencyCode,AMT_LENGTH));
                    this.value.totAmt = this.$numeral(enteredAmount).format(numFormats(slipCurrencyCode,AMT_LENGTH));
                    this.value.totAmtKrw = this.$numeral(accountedAmount).format(numFormats(slipCurrencyCode,AMT_LENGTH));
                    
                    // 부가세 헤더
                    this.value.evidenceVendorVat = evidenceVatRegistrationNum;
                    this.value.taxbillSupplyAmt = taxbillSupplyAmount;
                    this.value.taxbillTaxAmt = taxbillTaxAmount;
                    this.value.taxbillTotalAmt = taxbillTotalAmount;
                    this.value.taxRateCode = taxCode;
                    this.value.taxRateId = vatTaxId;
                    this.value.supplyAmount = this.value.supplyAmount || 0; //공급가액
                    this.value.taxAmount = this.value.taxAmount || 0; //세액
                    this.value.totalAmount = this.value.totalAmount || 0; //합계

                    //부모 비고란 입력
                    this.$parent.data.remark = remark;
                    
                    this.taxCalulator(this.value.percentageRate || 0);
                    
                    this.getTaxIssueTypeName(this.value.taxIssueTypeCode)
                    .then(data => {
                        this.value.taxIssueTypeName = data.filter(f => f.taxIssueTypeCode == this.value.taxIssueTypeCode)?.[0]?.taxIssueTypeName
                    });
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
                                const { bankAccountId, bankAccountName, bankAccountNumber, bankName } = data[0];
                                this.value.bankAccountId = bankAccountId;
                                this.value.bankAccountName = bankAccountName;
                                this.value.bankAccountNumber = bankAccountNumber;
                                this.value.bankName = bankName;
                                this.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                            }
                        });
                    }
                    
                    // 서브 컴포넌트에 대한 정의값
                    if(this.value.slipTypeCd === 'EXPEND') {
                        //경조금 객체 초기화를 위한 상세 API호출
                        this.$http.get(`/api/expend/slip/${slipHeaderId}`)
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
                        const { tripObj, tripCd, temp2 } = data;
                        this.value[`traffic`].tripObj = tripObj;
                        this.value[`traffic`].tripCd = tripCd;
                        this.value[`traffic`].temp2 = temp2;
                    } else if(this.value.slipTypeCd === 'TRIP') {
                        const { slipBusinessTripList } = data;
                        this.value[`trip`] = slipBusinessTripList.map(x =>{
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
                    
                })
                .catch(err => {
                    this.$swal({ type: 'error', text: err })
                    // .then(_ => {
                    //     this.$router.push({path: `/accrualSlip`}).catch(_ => window.location.reload())
                    // });
                })
                .finally(_ => {
                    
                    this.setDisabledState(this.value)
                    
                    /**
                     * * 해당 주기가 watch 에서 slipTypeCd 의 전표 유형변화로 전표라인 재세팅 초기화되어 다시 전표유형을 셋업해줌.
                     */ 
                    this.$parent.reloadData(slipHeaderId);
                    //증빙파일 첨부 
                    this.$store.commit('setEvidFileSize', this.value.fileCnt)
                    //그룹웨어 문서
                    this.$store.commit('setJiniSize', this.value.jiniCnt)
                    //그룹웨어 첨부
                    this.$store.commit('setJiniFileSize', this.value.jiniFileCnt)
                    // this.slipGridUpdate(this.value.slipTypeCd);

                    this.calcRealAmt();
                });
            });
            
        } else {
            this.setDisabledState(this.value);
            this.$store.commit('setEvidFileSize', 0)
            this.$store.commit('setJiniSize', 0)
            this.$store.commit('setJiniFileSize', 0)
        }
        
    },
    watch: {
        /**
         * * 전표유형코드 데이터
         * @param {*} val 
         */
        'value.slipTypeCd'(val) {
            this.slipGridUpdate(val);
        },
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
        /**
         * * 직원명 데이터 변경 시 데이터 초기화 진행
         */
         'value.empNm': {
            immediate: true, //컴포넌트 발생시 즉시 실행 여부
            deep: false, //nested: true
            handler(nVal, oVal) {
                if(oVal && this.value.empNo && oVal.length > nVal.length) {

                    this.$confirm('직원 변경 시 일부 데이터가 초기화 됩니다. 계속하시겠습니까?', 'Warning',
                    {
                        distinguishCancelAndClose: true,
                        confirmButtonText: '예',
                        cancelButtonText: '아니오',
                        type: 'warning'
                    })
                    .then((val) => {
                        if(val) {
                            this.initialize(`all`)
                            .then(_ => {
                                this.$message({ type: '초기화', message: '초기화 되었습니다.' });
                            });

                        }
                    })
                    .catch(_ => {
                        this.value.empNm = oVal;
                        this.$message({ type: '취소', message: '취소되었습니다.' });
                    });
                }
            }
        },
        /**
         * * 거래유형 값이 변경 될 때 초기화 작업 진행 ..
         */
        'value.trxTypeName': {
            immediate: true,
            deep: false,
            handler(nVal, oVal) {
                if(oVal && this.value.trxTypeCode && oVal.length > nVal.length) {
                    const self = this;
                    this.$confirm('거래유형 변경 시 일부 데이터가 초기화 됩니다. 계속하시겠습니까?', 'warning',
                    {
                        distinguishCancelAndClose: true,
                        confirmButtonText: '예',
                        cancelButtonText: '아니오',
                        type: 'warning'
                    })
                    .then((val) => {
                        if(val) {
                            this.initialize(`initTrxType`)
                            .then(_ => {
                                this.initialize(`initSubDatas`);
                                this.$message({ type: '초기화', message: '초기화 되었습니다.' });
                            });
                        }
                    })
                    .catch(_ => {
                        this.value.trxTypeName = oVal;
                        this.$message({ type: '취소', message: '취소되었습니다.' });
                    });
                }
            }
        },
        /**
         * * 세무유형 변경 시 데이터 초기화 진행
         */
        'value.taxEvidenceTypeName': {
            immediate: true,
            deep: false,
            handler(nVal, oVal) {
                if(oVal && this.value.taxEvidenceType && oVal.length > nVal.length) {

                    this.$confirm('세무증빙유형을 초기화 하시겠습니까?', 'Warning',
                    {
                        distinguishCancelAndClose: true,
                        confirmButtonText: '예',
                        cancelButtonText: '아니오',
                        type: 'warning'
                    })
                    .then((val) => {
                        if(val) {
                            this.initialize(`initVendor`);
                            this.initialize(`initTaxBill`);
                            this.initialize(`initEvidenceVendor`);
                            this.$bus.emit('setGridColDef', this.value.slipTypeCd)
                            this.initialize(`initEvidence`)
                            .then(_ => {
                                this.$message({ type: 'success', message: '초기화 되었습니다.' });
                            });
                        }
                    })
                    .catch(_ => {
                        this.value.taxEvidenceTypeName = oVal;
                        this.$message({ type: 'info', message: '취소되었습니다.' });
                    });
                }
            }
        },
        'value.taxSmartbillNo': {
            immediate: true,
            deep: false,
            handler(nVal, oVal) {
                if(oVal && this.value.taxbillSuId && oVal.length > nVal.length) {

                    this.$confirm('세금계산서를 초기화 하시겠습니까?', 'Warning',
                    {
                        distinguishCancelAndClose: true,
                        confirmButtonText: '예',
                        cancelButtonText: '아니오',
                        type: 'warning'
                    })
                    .then((val) => {
                        if(val) {
                            //taxbillSuId
                            this.initialize(`initTaxBill`)
                            .then(_ => {
                                this.$message({ type: 'success', message: '초기화 되었습니다.' });
                            });
                        }
                    })
                    .catch(_ => {
                        this.value.taxSmartbillNo = oVal;
                        this.$message({ type: 'info', message: '취소되었습니다.' });
                    });
                }
            }
        },
        /**
         * 결재조건 변경 시 데이터 초기화 진행
         */
        'value.termDescription': {
            immediate: true,
            deep: false,
            handler(val) {
                if(!val) {
                    this.initialize(`initTerm`);
                    this.initialize(`initBank`);
                    this.value.termDueDateChangeFlag = 'N';
                }
            }
        },
        /**
         * 거래처 변경 시 데이터 초기화 진행
         */
        'value.integrationVendorName': {
            immediate: true,
            deep: false,
            handler(val) {
                if(!val) {
                    this.initialize(`initEvidenceVendor`)
                    this.initialize(`initVendor`)
                    this.initialize(`initBank`)
                }
            }
        },
        /**
         * 계좌정보 변경 시 데이터 초기화 진행
         */
         'value.bankAccountName': {
            immediate: true,
            deep: false,
            handler(val) {
                if(!val) {
                    this.initialize(`initBank`)
                }
            }
        },
        /**
         * 반제신청금액 변경 시점에서 숫자만 입력 되도록...
         * @param {*} val 
         * TODO: 반제신청 금액 변수가 달라질 수 있음. 그때 다시 변경...
         */
        'value.amount_to_apply'(val) {
            this.amountNumberic(val, 'amount_to_apply');
        },
        /** 
         * TODO: 원화가 실지급액 일까? 
         * */
        // 'value.totAmtKrw'(val) {
        //     this.value.realAmt = val;
        // }
    }
}
</script>

<style>
  .input-amt.el-input > .el-input__inner{
    text-align: right;
  }
</style>