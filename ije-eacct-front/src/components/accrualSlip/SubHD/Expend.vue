<template>
    <div>
        <div class="table-name">
            <h3 class="ico_table_name">내부경조내역</h3>
        </div>
        <table class="table">
            <tbody>
                <tr>
                    <th class="tp-a">경조일시</th>
                    <td>
                        <el-date-picker
                            v-model="value.expend.expendDt"
                            type="date"
                            format="yyyyMMdd"
                            value-format="yyyyMMdd"
                            @change="expendInit"
                            :disabled="readOnly">
                        </el-date-picker>
                    </td>
                    <th class="tp-a">경조구분</th>
                    <td>
                        <el-input :class="readOnly ? '': 'input-bg-white'" type="primary" v-model="value.expend.expendNm" @keypress.native.enter="expendGubunOpenPop(value.expend.expendDt)" :readonly="true" clearable>
                            <i v-if="!readOnly" class="el-icon-search el-input__icon" slot="suffix" @click="expendGubunOpenPop(value.expend.expendDt)"></i>
                        </el-input>
                    </td>
                    <th class="tp-a">신청인과의 관계</th>
                    <td>
                        <el-input type="primary" v-model="value.expend.expendRelation" :readonly="true" clearable></el-input>
                    </td>
                </tr>
                <tr>
                    <th class="tp-a">당사자 성명</th>
                    <td>
                        <el-input type="primary" v-model="value.expend.expendReceiveNm" :readonly="readOnly" clearable></el-input>
                    </td>
                    <th>회사지급액</th>
                    <td>
                        <el-input class="input-amt" type="primary" v-model="value.expend.expendAmt" :readonly="true" clearable></el-input>
                    </td>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                    <th>경조화환</th>
                    <td>
                        <el-radio-group v-model="value.expend.wreathYn" :disabled="true">
                            <el-radio label="Y">유</el-radio>
                            <el-radio label="N">무</el-radio>
                        </el-radio-group>
                    </td>
                    <th>상조용품</th>
                    <td>
                        <el-radio-group v-model="value.expend.mutualYn" :disabled="true">
                            <el-radio label="Y">유</el-radio>
                            <el-radio label="N">무</el-radio>
                        </el-radio-group>
                    </td>
                    <th>-</th>
                    <td>-</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>

/**
 * * Modal
 */
import ExpendGubunModal from '@/components/accrualSlip/Modals/ExpendGubunModal.vue'; /** 경조금 구분 */

export default {
    props: ['value', 'readOnly'],
    data() {
        return {
        }
    },
    methods: {
        expendInit() {
            this.value.expend.expendCd = '';
            this.value.expend.expendNm = '';
            this.value.expend.expendRelation = '';
            this.value.expend.expendReceiveNm = '';
            this.value.expend.expendAmt = 0;
            this.value.expend.wreathYn = 'N';
            this.value.expend.holiday = '';
            this.value.expend.mutualYn = '';
        },
        /**
         * * 경조구분 팝업
         */
        expendGubunOpenPop(fameveDate) {
            if(!fameveDate) {
                this.value.expend.expendCd = '';
                this.$message.error({ type: '알림', message: '경조일시를 지정해주세요.' });
                return false;
            }
            const self = this;
            this.$modal.open({
                component: ExpendGubunModal,
                props: {
                    fameveDate
                },
                parent: this,
                width: 1200,
                events: {
                    close(obj) {

                        const {
                            expendCd, remark2, remark3, expendAmt, wreathYn, mutualYn, holiday
                        } = obj;
                        
                        self.value.expend.expendCd = expendCd;
                        self.value.expend.expendNm = `${remark2}/${remark3}`;
                        self.value.expend.expendRelation = remark3;
                        self.value.expend.expendAmt = self.$numeral(expendAmt).format('#,#');
                        self.value.expend.wreathYn = wreathYn;
                        self.value.expend.mutualYn = mutualYn;
                        self.value.expend.holiday = holiday;
                        
                        self.$parent.$parent.rowData.forEach((item, idx) => {
                            const rowNode = self.$parent.$parent.gridApi.getRowNode(idx);
                            self.$parent.$parent.setGridDataValue(idx, 'supplyAmount', self.$numeral(expendAmt).value());
                            // self.$parent.$parent.setGridDataValue(idx, 'vatAmount', 0);
                            self.$parent.$parent.setGridDataValue(idx, 'usedAmt', self.$numeral(expendAmt).value());
                            rowNode.setDataValue('supplyAmount', self.$numeral(expendAmt).value());
                            rowNode.setDataValue('usedAmt', self.$numeral(expendAmt).value());
                        });
                    },
                }
            });
        }
    },
    watch: {
        'value.expend.expendNm': {
            immediate: true, //컴포넌트 발생시 즉시 실행 여부
            deep: false, //nested: true
            handler(nVal, oVal) {
                // if(this.value.expend.expendNm && oVal.length > nVal.length) {
                    if(oVal && !nVal) {

                    this.$confirm('경조구분 변경 시 경조 데이터가 초기화 됩니다. 계속하시겠습니까?', 'Warning',
                    {
                        distinguishCancelAndClose: true,
                        confirmButtonText: '예',
                        cancelButtonText: '아니오',
                        type: 'warning'
                    })
                    .then((val) => {
                        if(val) {
                            this.expendInit();
                            this.$message({ type: '초기화', message: '초기화 되었습니다.' });

                        }
                    })
                    .catch(err => {
                        console.log(err)
                        this.value.expend.expendNm = oVal;
                        this.$message({ type: '취소', message: '취소되었습니다.' });
                    });
                }
            }
        },
    }
}
</script>