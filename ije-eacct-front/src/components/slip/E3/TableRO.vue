<template>
<table class="table">
  <tbody>
    <!-- 기준정보  -->
    <tr v-if="!isNew">
      <th width="7%">신청번호</th>
      <td width="16%">{{ value.grSlipNo }}</td>
      <td width="7%"></td>

      <th width="8%">전표유형</th>
      <td width="20%">{{ value.slipTypeNm }}</td>

      <th width="8%">전표상태</th>
      <td width="12%">
        <span class="color_table_org" v-if="value.slipStatCd !== '40'">{{ value.slipStatNm }}</span>
        <a class="color_table_org" v-else>{{ value.slipStatNm }}</a>
      </td>
      <td width="8%" v-if="value.fnlApprDtm">{{ this.$moment(value.fnlApprDtm).format('YYYY-MM-DD') }}</td>
      <td width="8%" v-else></td>
      <td colspan="2"></td>
    </tr>

    <tr v-if="!isNew">
      <th>작성부서</th>
      <td>{{ value.wrtDeptCd }} / {{ value.wrtDeptNm }}</td>
      <td>{{ value.bpNm }}</td>

      <th>작성자</th>
      <td>{{ value.wrtNm }}</td>

      <th>작성일자</th>
      <td class="Rt-M" colspan="4">{{ this.$moment(value.wrtDtm).format('YYYY-MM-DD') }}</td>
    </tr>
    <!-- //기준정보  -->

    <tr>
      <th>증빙유형</th>
      <td colspan="2">{{ value.evdTypeNm }}</td>

      <th>선급금신청</th>
      <td>
        <div v-if="value.aplFgCd === 'K'">선급금신청</div>
        <div v-else>없음</div>
      </td>      


      <th>증빙일자</th>
      <td>{{ this.$moment(value.evdDt).format('YYYY-MM-DD') }}</td>
      
      <th>회계일자</th>
      <td colspan="2">{{ this.$moment(value.postDt).format('YYYY-MM-DD') }}</td>
    </tr>

    <tr>
      <th>세금코드</th>
      <td colspan="2">[{{ value.taxCd }}] {{ value.taxNm }}</td>

     <!-- <th>정산구분</th>
      <td>
        <div v-if="value.stlFgCd === 'K'">선급금정산</div>
        <div v-else>미정산</div>
      </td>
      -->
      <th>고정자산여부</th>
      <td>{{ value.fixAssetYn }}</td>

      <th>지급조건</th>
      <td colspan="4">{{ value.payTermNm }}</td>
      <!-- TODO::선급금정산, 사용안함 -->
<!--      <th>선급금정산</th>-->
<!--      <td class="clearfix" colspan="4">{{value.advReqSlipNo}} / {{this.$numeral(value.advReqSlipAmt).format('0,0')}}</td>-->
      
    </tr>

    <tr>
      <th>거래처</th>
      <td colspan="2">{{ value.evdCustCd }} / {{ value.evdCustNm }} / {{value.evdCustBizNo}}</td>

      <td class="bd-l-none">
        <input type="checkbox" id="check02" v-model="actualEvdPay" disabled="disabled" />
        <label for="check02">실지급처</label>
      </td>
      <td class="bd-l-none" v-if="actualEvdPay">{{ value.payCustCd }} / {{ value.payCustNm }}</td>
      <td class="bd-l-none" v-else></td>

      <th>지급예정일</th>
      <td>{{ this.$moment(value.payDueDt, 'YYYYMMDD').format('YYYY-MM-DD') }}</td>

      <th>지급비고란</th>
      <td colspan="2">{{value.payText}}</td>
    </tr>

    <tr>
      <th>공급가액</th>
      <td class="Rt-M tar" colspan="2">{{ this.$numeral(value.supAmt).format('0,0') }}</td>

      <th>기준부서</th>
      <td>{{ value.baseDeptCd }} / {{ value.baseDeptNm }}</td>

      <th>지급계좌</th>
      <td colspan="4">{{ value.custAcctNo }}</td>
    </tr>

    <tr>
      <th>부가세액</th>
      <td class="Rt-M tar" colspan="2">{{ this.$numeral(value.vatAmt).format('0,0') }}</td>

      <th>대변계정</th>
      <td>{{ value.apAcctCd }} / {{ value.apAcctNm }}</td>

      <th>대변 보조계정</th>
      <td colspan="4">{{ value.apSubAcctCd }} / {{ value.apSubAcctNm }}</td>
    </tr>

    <tr>
      <th>기타금액</th>
      <td class="Rt-M tar" colspan="2">{{ this.$numeral(value.etcAmt).format('0,0') }}</td>

      <!-- <th>통화</th>
      <td>{{ value.curCd }}</td> -->
      <th>{{columnTitle}}</th>
      <td v-if="value.slipTypeCd === 'E3'"> 
        <span class="NotoM">{{value.etaxNo}}</span>
        <span class="btn-icon" style="width:10%; margin: 0 0 0 8px; cursor:pointer" @click="goEtaxPage(value.etaxNo)">
            <i class="fas fa-external-link-alt"></i>
        </span>
      </td>
      <td v-else> <span class="NotoM">{{value.curCd}}</span></td>

      <th>사업장</th>
      <td colspan="4">{{ value.bpCd }} / {{ value.bpNm }}</td>
    </tr>

    <tr>
      <th>총 금액</th>
      <td class="Rt-M tar" colspan="2">{{ this.$numeral(value.totAmt).format('0,0') }}</td>

      <th>적요</th>
      <td colspan="6">{{ value.hdSgtxt }}</td>
    </tr>
    <tr v-if="value.taxCd === '10003'">
        <th>지급여부</th>
        <td colspan="2">
            <div class="control">
                <div class="dp-ib fl_left">
                    <input type="checkbox" id="check01" v-model="value.hdRef8" disabled="false"/>
                    <label for="check01" class="NotoM">지급안함</label>
                </div>
            </div>
        </td>
        <td colspan="7"></td>
    </tr>
  </tbody>
</table>
</template>

<script>
import _ from 'lodash'
import mixin from '@/mixin/slip-table'
import DhxCalendar from '@/components/DhxCalendar.vue'
import NumberInput from '@/components/NumberInput.vue'
import ElecTaxPopup from '@/components/ElecTaxPopup.vue';

export default {
  compatConfig: { MODE: 2 },
  props: ['value', 'eventBus', 'isNew', 'readonly'],
  mixins: [mixin],
  components: {
    DhxCalendar,
    NumberInput
  },
  data() {
    return {
      id: 'RO'
    }
  },
  computed: {
    actualEvdPay(data) {
      return !!data.value.payCustCd
    },
    columnTitle() {
      return this.value.slipTypeCd === 'E3' ? '전자승인번호' : '통화';
    }
  },
  methods: {
    goEtaxPage(eTaxNo) {
        //window.open(`http://197.200.1.19:10004/addservice/view/XXSB_DTI_PRINT_VIEW_NTS.asp?issue_id=${eTaxNo}&SBTYPE=AP`, "_blank");

        this.$modal.open({
            component: ElecTaxPopup,
            parent: this,
            props: {
                eTaxNo: eTaxNo
            }
        });
    }
  },
  created(){
    let custAcctNo = this.value.custAcctNo
    let custBnkCd = this.value.custBnkCd
    if(!_.isEmpty(custAcctNo)){
      this.value.custAcctNo = custAcctNo + ' ('+ custBnkCd + ')'         
     }
  }
}
</script>
