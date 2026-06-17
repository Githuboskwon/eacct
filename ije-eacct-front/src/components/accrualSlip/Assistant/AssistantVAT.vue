<template>
    <div>
        <div class="table-name">
            <h3 class="ico_table_name">부가세정보</h3>
        </div>
        <table class="table">
            <tbody>
                <tr>
                    <th>증빙거래처</th>
                    <td>
                        <!-- <el-input class="mr3" type="text" v-model="value.evidencdVendorNum" style="width: 40%" disabled ></el-input>
                        <div class="ip-box ip-box-w02 rs-mt2" style="width:calc(70% - 55px)">
                            <el-input type="text" v-model="value.evidenceVendorName" @keypress.native.enter="evidVendorOpenModal(value.evidenceVendorName)" clearable>
                                <i class="el-icon-search el-input__icon" slot="suffix" @click="evidVendorOpenModal(value.evidenceVendorName)"></i>
                            </el-input>
                        </div> -->
                        <el-input type="primary" class="input-bg-white" v-model="value.evidenceVendorName" readonly></el-input>
                    </td>
                    <th>사업자번호</th>
                    <td>
                        <el-input class="input-bg-white" type="text" v-model="value.evidenceVendorVat" clearable readonly>
                            <i v-if="!readOnly" class="el-icon-search el-input__icon" slot="suffix" @click="evidVendorOpenModal(value.evidenceVendorVat)"></i>
                        </el-input>
                    </td>
                    <th>발행구분</th>
                    <td>
                        <el-input class="mr3" type="text" v-model="value.taxIssueTypeCode" style="width: 30%" disabled ></el-input>
                        <div class="ip-box" style="width:68%;">
                            <el-input type="text" class="input-bg-white" v-model="value.taxIssueTypeName" clearable @keypress.native.enter="hdTaxIssueModal" readonly>
                                <i v-if="!readOnly" class="el-icon-search el-input__icon" slot="suffix" @click="hdTaxIssueModal"></i>
                            </el-input>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th class="tp-a">작성년월일</th>
                    <td>
                        <el-date-picker
                            v-model="value.evidenceDate"
                            type="date"
                            format="YYYYMMDD"
                            value-format="YYYYMMDD"
                            :disabled="value.isTaxReadOnly || readOnly">
                        </el-date-picker>
                    </td>
                    <th>헤더부가세코드</th>
                    <td colspan="3">
                        <el-input class="mr3" type="text" v-model="value.percentageRate" style="width: 15%; text-align-last: end;" disabled>
                            <i slot="suffix" class="el-input__icon">%</i>
                        </el-input>
                        <div class="ip-box" style="width:25%;">
                            <el-input class="input-bg-white" type="text" v-model="value.taxAcctName" readonly>
                                <i v-if="!readOnly" class="el-icon-search el-input__icon" slot="suffix" @click="hdTaxOpenModal"></i>
                            </el-input>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th width="15%">세금계산서 공급가액</th>
                    <td>
                        {{ value.taxbillSupplyAmt | amt }}
                    </td>
                    <th width="15%">세금계산서 세액</th>
                    <td>
                        {{ value.taxbillTaxAmt | amt }}
                    </td>
                    <th width="15%">세금계산서 합계</th>
                    <td>
                        {{ value.taxbillTotalAmt | amt }}
                    </td>
                </tr>
                <tr>
                    <th width="15%">공급가액</th>
                    <td>
                        {{ value.vSupplyAmount | amt }}
                    </td>
                    <th width="15%">세액</th>
                    <td>
                        {{ value.vTaxAmount | amt }}
                    </td>
                    <th width="15%">합계</th>
                    <td>
                        {{ value.vTotalAmount | amt }}
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>

import VendorModal from '@/components/Vendor_Ag'; /** 거래처 */
import VatModal from '@/components/accrualSlip/GridModal/VatModal'; /** 부가세 코드 조회 */
import TaxIssueModal from '@/components/accrualSlip/Modals/TaxIssueModal.vue'; /** 발행구분 */

export default {
    props: ['value', 'readOnly'],
    created() {
        /**
         * 모달 등 뷰어에서 공급가액/세액/합계 등이 보이지 않을 경우가 존재하여 해당 버스 이벤트를 명시해놓음.
         * 사용은 accualSlip/Approval/Bottom.vue 에서 전달해주고 있음.
         */
        this.$bus.on('setViewTaxAmount', (val) => {
            this.value = Object.assign({}, this.value, val);
        });
    },
    mounted() {
        // console.group("================================================");
        // console.group("AssistantVat dtiType", this.value.dtiType);
        // console.group("AssistantVat taxbillTaxAmt", this.value.taxbillTaxAmt);
        // console.group("AssistantVat taxEvidenceType", this.value.taxEvidenceType);
        // console.groupEnd("================================================");

        this.$store.commit('loading');
        const params = {
            searchCd: '',
            searchNm: '',
            taxRateCd: ''
        }
        this.$http.post(`/api/surTaxCode/list/${this.value.taxEvidenceType}/form`, params)
        .then(res => res.data)
        .then(data => {
            if(!!data && data.length === 1) {
                const {
                    taxStatusCode, /** 부가세명 */ percentageRate, /** 세율 */ taxRateId, /** 부가세계정ID (이게 key 인듯) */
                    taxRateCode, /** 부가세계정 */ taxAcctCode /** 부가세계정코드 */
                } = data[0];
                this.value.taxStatusCode = taxStatusCode;
                this.value.percentageRate = percentageRate;
                this.value.taxRateId = taxRateId;
                this.value.taxRateCode = taxRateCode;
                this.value.taxAcctCode = taxAcctCode;
                this.value.taxAcctName = taxRateCode; //저장 시 필요한 듯
            }
        })
        .finally(() => {
            this.$store.commit('finish');
        });

    },
    methods: {
        /**
         * * 증빙 거래처 팝업(부가세인 경우 사용함)
         */
         evidVendorOpenModal(search) {
            
            const self = this;
            this.$modal.open({
                component: VendorModal,
                props: {
                    slipTypeCd: this.value.slipTypeCd,
                    param: search
                    
                },
                parent: this,
                width: 1200,
                events: {
                    close(obj) {
                        const { integrationVendorNum, integrationVendorName, vatRegistrationNum, 
                            vendorId, venTermsId, venTermsName, 
                            noteAccountFlag, prepayCnt, vendorSiteId, venPaymentCurrencyCd, venPaymentDescription } = obj;
                        if(self.value.taxbillSuId) {
                            if(self.value.taxbillSuId !== vatRegistrationNum) {
                                self.$alert(`세금계산서의 공급자사업자 번호와 증빙거래처의 사업자 번호가 다릅니다.`, '확인', {
                                    dangerouslyUseHTMLString: true,
                                    confirmButtonText: '확인',
                                    type: 'error',
                                    center: true
                                });
                                return false;
                            }
                        }
                        self.value.evidenceVendorNum = integrationVendorNum;
                        self.value.evidenceVendorVat = vatRegistrationNum;
                        self.value.evidenceVendorName = integrationVendorName;
                        self.value.evidenceVendorCustSiteId = vendorSiteId; // 거래처 ID
                    },
                    
                }
            });
        },
        /**
         * (세금)부가세 모달
         */
        hdTaxOpenModal(taxRateCd) {
            const { taxEvidenceType } = this.value;

            const self = this;
            this.$modal.open({
                component: VatModal,
                parent: this,
                props: {
                    taxEvidenceType,
                    returnType: 'form',
                },
                width: 800,
                events: {
                    close(object) {
                        const { 
                            taxStatusCode,  //부가세명
                            percentageRate, //세율
                            taxRateId,      //부가세계정ID (이게 key 인듯)
                            taxRateCode,    //부가세계정
                            taxAcctCode     //부가세계정코드
                        } = object;
                        self.value.taxStatusCode = taxStatusCode;
                        self.value.percentageRate = percentageRate;
                        self.value.taxRateId = taxRateId;
                        self.value.taxRateCode = taxRateCode;
                        self.value.taxAcctCode = taxAcctCode;
                        self.value.taxAcctName = taxRateCode; //저장 시 필요한 듯
                        /** grid 세금코드 세팅 */
                        const { getRowData } = self.$parent.$parent.$refs.grid;
                        const rowDataSize = getRowData().length || 0;
                        if(rowDataSize > 0) {
                            getRowData().forEach( (row, idx) => {
                                self.$parent.$parent.setGridDataValue(idx, 'taxId', taxRateId);
                                self.$parent.$parent.setGridDataValue(idx, 'taxCd', taxRateCode);
                                self.$parent.$parent.setGridDataValue(idx, 'taxNm', taxRateCode);
                                self.$parent.$parent.setGridDataValue(idx, 'taxAcctCode', taxAcctCode);
                                self.$parent.$parent.setGridDataValue(idx, 'taxAcctName', taxRateCode);
                                self.$parent.$parent.setGridDataValue(idx, 'percentageRate', percentageRate);
                                if(taxRateCode == '매입불공제') {
                                    self.$parent.$parent.setGridDataValue(idx, 'taxFlag', 'N');
                                    self.$parent.$parent.setGridDataValue(idx, 'lineAttribute1', 'Y');
                                } else if(taxRateCode != '' && taxRateCode != '매입불공제') {
                                    self.$parent.$parent.setGridDataValue(idx, 'taxFlag', 'Y');
                                    self.$parent.$parent.setGridDataValue(idx, 'lineAttribute1', '');
                                } else {
                                    self.$parent.$parent.setGridDataValue(idx, 'lineAttribute1', '');
                                }
                            })
                        }
                    }
                },
            });
            
        },
        /**
         * 발행구분 모달
         */
        hdTaxIssueModal() {
            const { taxEvidenceType } = this.value;
            const self = this;
            this.$modal.open({
                component: TaxIssueModal,
                parent: this,
                width: 800,
                events: {
                    close(object) {
                        const { 
                            taxIssueTypeName,
                            taxIssueTypeCode,
                        } = object;
                        self.value.taxIssueTypeName = taxIssueTypeName;
                        self.value.taxIssueTypeCode = taxIssueTypeCode;
                    }
                },
            });
        }
    },
    watch: {
        /**
         * * 증빙거래처의 초기화
         */
        'value.evidenceVendorName': {
            immediate: true,
            deep: false,
            handler(val) {
                
            }
        },
        'value.percentageRate'(val) {
            this.$parent.taxCalulator(val);
            this.$parent.$parent.amountCalcurate();
            this.$parent.$parent.$refs.grid.getRowData().map((x, i) => {
                const vatAmount = Math.round(this.$numeral(x.supplyAmount * (val / 100)).value());
                this.$parent.$parent.setGridDataValue(i, 'vatAmount', vatAmount);
                const rowNode = this.$parent.$parent.gridApi.getRowNode(i);
                rowNode.setDataValue('vatAmount', vatAmount);
            })
        }
    }
}
</script>