<template>
    <table class="table_tax">
        <table v-if="etaxScreen=='T2' || etaxScreen =='T4'" class="table_tax">
          <tr class="h20">
            <td rowspan="3" class="w350 border_none_r tax_tit tax_color">{{etaxData.taxTypeNm}}</td>
            <td rowspan="3" class="w100 border_none_l tax_color">공급받는자<br>(보관용)</td>
            <td class="w100 border_none_r tax_color">승인번호</td>
            <td class="w350 border_none_l tax_color">{{etaxData.issueId}}</td>
          </tr>
          <tr class="h20">
            <td class="w100_t border_none_r tax_color">당초승인번호</td>
            <td class="w350_t border_none_l tax_color">{{etaxData.taxInvDocOriID}}</td>
          </tr>
          <tr class="h20">
            <td class="w100_t border_none_r tax_color">관리번호</td>
            <td class="w350_t border_none_l tax_color">{{etaxData.exchDocId}}</td>
          </tr>
        </table>
        <table v-else class="table_tax">
          <tr class="h30">
            <td rowspan="2" class="w350 border_none_r tax_tit tax_color">{{etaxData.taxTypeNm}}</td>
            <td rowspan="2" class="w100 border_none_l tax_color">공급받는자<br>(보관용)</td>
            <td class="w100 border_none_r tax_color">승인번호</td>
            <td class="w350 border_none_l tax_color">{{etaxData.issueId}}</td>
          </tr>
          <tr class="h30">
            <td class="w100_t border_none_r tax_color">관리번호</td>
            <td class="w350_t border_none_l tax_color">{{etaxData.exchDocId}}</td>
          </tr>
        </table>
        <table class="table_tax">
          <tr class="h30">
            <td rowspan="4" class="w30 tax_color">공<br>급<br>자</td>
            <td class="w70 border_none_l tax_color">등록번호</td>
            <td colspan="3" class="w140 border_none_l">{{etaxData.invoicerId}}</td>
            <td rowspan="4" class="w30 tax_color">공<br>급<br>받<br>는<br>자</td>
            <td class="w70 border_none_l tax_color">등록번호</td>
            <td colspan="3" class="w140 border_none_l">{{etaxData.invoiceeId}}</td>
          </tr>
          <tr class="h45">
            <td class="w70_t border_none_l tax_color">상호<br>(법인명)</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoicerName}}</td>
            <td class="w70_t border_none_l tax_color">성명</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoicerSpecifiedPerson}}</td>
            <td class="w70_t border_none_l tax_color">상호<br>(법인명)</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoiceeName}}</td>
            <td class="w70_t border_none_l tax_color">성명</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoiceeSpecifiedPerson}}</td> 
          </tr>
          <tr class="h60">
            <td class="w70_t border_none_l tax_color">사업장<br>주소</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoicerSpecifiedAddr}}</td>
            <td class="w70_t border_none_l tax_color">종사업장<br>번호</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoicerTaxRegId}}</td>
            <td class="w70_t border_none_l tax_color">사업장<br>주소</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoiceeSpecifiedAddr}}</td>
            <td class="w70_t border_none_l tax_color">종사업장<br>번호</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoiceeTaxRegId}}</td>
          </tr>
          <tr class="h30">
            <td class="w70_t border_none_l tax_color">업태</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoicerTypeCode}}</td>
            <td class="w70_t border_none_l tax_color">종목</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoicerClassificationCode}}</td>
            <td class="w70_t border_none_l tax_color">업태</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoiceeTypeCode}}</td>
            <td class="w70_t border_none_l tax_color">종목</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxData.invoiceeClassificationCode}}</td>
          </tr>
        </table>
        <table v-if="broker==true" class="table_tax">
          <tr class="h30">
            <td class="w100 tax_color">수탁자</td>
            <td colspan="3" class="w800_t border_none_l tax_textAlign_l">
              ({{etaxData.brokerName}} ({{etaxData.brokerId}}, 종사업장:{{etaxData.brokerTaxRegId}}, {{etaxData.brokerSpecifiedPerson}},{{etaxData.brokerSpecifiedAddr}}), {{etaxData.brokerTypeCode}},{{etaxData.brokerClassificationCode}})
            </td>
          </tr>
        </table>
        <table v-if="etaxScreen =='T3' || etaxScreen =='T4'" class="table_tax">
          <tr class="h30">
            <td class="w100 tax_color">작성일자</td>
            <td class="w350 border_none_l tax_color">공급가액</td>
            <td class="w450 border_none_l tax_color">수정사유</td>
          </tr>
          <tr class="h30">
            <td class="w100_t tax_textAlign_c">{{$moment(etaxData.taxInvDocIssueDateTime).format('YYYY/MM/DD')}}</td>
            <td class="w350_t border_none_l tax_textAlign_r">{{$numeral(etaxData.chargeTotalAmt).format('0,0')}}</td>
            <td class="w450_t border_none_l">{{etaxData.amendCode}}</td>
          </tr>
          <tr class="h20">
            <td class="w100_t tax_color">비고 1</td>
            <td colspan="3" class="w800_t border_none_l tax_textAlign_l">{{etaxData.taxInvDocDesc}}</td>
          </tr>
        </table>
        <table v-else class="table_tax">
          <tr class="h30">
            <td class="w100 tax_color">작성일자</td>
            <td class="w140 border_none_l tax_color">공급가액</td>
            <td class="w210 border_none_l tax_color">세액</td>
            <td class="w450 border_none_l tax_color">수정사유</td>
          </tr>
          <tr class="h30">
            <td class="w100_t tax_textAlign_c">{{$moment(etaxData.taxInvDocIssueDateTime).format('YYYY/MM/DD')}}</td>
            <td class="w140_t border_none_l tax_textAlign_r">{{$numeral(etaxData.chargeTotalAmt).format('0,0')}}</td>
            <td class="w210_t border_none_l tax_textAlign_r">{{$numeral(etaxData.taxTotalAmt).format('0,0')}}</td>
            <td class="w450_t border_none_l">{{etaxData.amendCode}}</td>
          </tr>
          <tr class="h20">
            <td class="w100_t tax_color">비고 1</td>
            <td colspan="3" class="w800_t border_none_l tax_textAlign_l">{{etaxData.taxInvDocDesc}}</td>
          </tr>
        </table>
        <table class="table_tax">
          <tr class="h20">
            <td class="w50 tax_color">월</td>
            <td class="w50 border_none_l tax_color">일</td>
            <td class="w140 border_none_l tax_color">품목</td>
            <td class="w70 border_none_l tax_color">규격</td>
            <td class="w70 border_none_l tax_color">수량</td>
            <td class="w150 border_none_l tax_color">단가</td>
            <td class="w150 border_none_l tax_color">공급가액</td>
            <td class="w150 border_none_l tax_color">세액</td>
            <td class="w70 border_none_l tax_color">비고</td>
          </tr>
          <tr class="h20" v-for="(etaxItem,i) in etaxItems" :key="i"><!--for문 -->
            <td class="w50_t tax_textAlign_c">{{$moment(etaxItem.itemPruchExpDateTime).format('MM')}}</td>
            <td class="w50_t border_none_l tax_textAlign_c">{{$moment(etaxItem.itemPruchExpDateTime).format('DD')}}</td>
            <td class="w140_t border_none_l tax_textAlign_l">{{etaxItem.itemName}}</td>
            <td class="w70_t border_none_l tax_textAlign_c">{{etaxItem.itemInform}}</td>
            <td class="w70_t border_none_l tax_textAlign_c">{{etaxItem.itemChargeUnitQuan == 0 ? '' : etaxItem.itemChargeUnitQuan}}</td>
            <td class="w150_t border_none_l tax_textAlign_r">{{etaxItem.itemUnitAmt == 0 ? '' : $numeral(etaxItem.itemUnitAmt).format('0,0')}}</td>
            <td class="w150_t border_none_l tax_textAlign_r">{{$numeral(etaxItem.itemAmt).format('0,0')}}</td>
            <td class="w150_t border_none_l tax_textAlign_r">{{$numeral(etaxItem.itemCalcAmt).format('0,0')}}</td>
            <td class="w70_t border_none_l">{{etaxItem.itemDesc}}</td>
          </tr>
        </table>
        <table class="table_tax">
          <tr class="h20">
            <td class="w150 tax_color">합계금액</td>
            <td class="w150 border_none_l tax_color">현금</td>
            <td class="w150 border_none_l tax_color">수표</td>
            <td class="w150 border_none_l tax_color">어음</td>
            <td class="w150 border_none_l tax_color">외상미수금</td>
            <td rowspan="2" class="w150 border_none_l tax_color">이 금액을 &nbsp;&nbsp;&nbsp; {{!etaxData.purposeNm ? '':etaxData.purposeNm}}&nbsp;&nbsp;함</td>
          </tr>
          <tr class="h20">
            <td class="w150_t tax_textAlign_r">{{$numeral(etaxData.grandTotalAmt).format('0,0')}}</td>
            <td class="w150_t border_none_l tax_textAlign_r">{{etaxData.payTypeCode == '10' ? $numeral(etaxData.payAmt).format('0,0') : ''}}</td>
            <td class="w150_t border_none_l tax_textAlign_r">{{etaxData.payTypeCode == '20' ? $numeral(etaxData.payAmt).format('0,0') : ''}}</td>
            <td class="w150_t border_none_l tax_textAlign_r">{{etaxData.payTypeCode == '30' ? $numeral(etaxData.payAmt).format('0,0') : ''}}</td>
            <td class="w150_t border_none_l tax_textAlign_r">{{etaxData.payTypeCode == '40' ? $numeral(etaxData.payAmt).format('0,0') : ''}}</td>
          </tr>
        </table>
    </table>
</template>
<script>
export default {
  compatConfig: { MODE: 2 },
    name : 'EtaxScreen',
    props:{
        etaxData:{
            type: Object,
            required: true,
        },
        etaxItems:{
          type:Array,
          required:false,
        },
        etaxScreen:{
          type:String,
          required:true
        },
        broker:{
          type:Boolean,
          required:true
        }
    }
}
</script>