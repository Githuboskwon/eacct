package com.iljin.apiServer.ijeas.es.item;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErpItemSlipDto implements Serializable {

    private static final long serialVersionUID = 5281003613811924734L;

    String compCd;

    String slipNo;

    String slipType;

    BigDecimal slipHeaderId;

    BigDecimal checkId;

    LocalDateTime checkDt;

    String bankAcctNum;

    String currencyCd;

    String paymentAmt;

    BigDecimal baseAmt;

    String vendorNm;

    LocalDateTime futurePayDueDt;

    BigDecimal currencyConversionRate;

    LocalDateTime currencyConversionDt;

    BigDecimal checkNo;

    String paymentFormat;

    String paymentMethodCd;

    String paymentMethod;

    String remark;

    String regId;

    String regNm;

    String deptNo;

    String deptNm;

    String postingDt;

    String headerRemark;

    String bankNm;

    String bankAcctNm;

    String bankBranchNm;

    String acctHolderNm;

    String externalBankNm;

    String externalAcctNum;

    String status;

    Long uFileCnt;

    Long jiniCnt;

    List<ApPaymentsItemDto> itemList;

    String docTitle;

    // list
    @QueryProjection
    public ErpItemSlipDto(String compCd, String slipNo, String slipType, BigDecimal slipHeaderId, BigDecimal checkId,
        LocalDateTime checkDt, String bankAcctNum, String currencyCd, String paymentAmt,
        BigDecimal baseAmt, String vendorNm, LocalDateTime futurePayDueDt,
        BigDecimal currencyConversionRate, LocalDateTime currencyConversionDt, BigDecimal checkNo,
        String paymentFormat, String paymentMethodCd, String paymentMethod, String remark,
        String regId, String regNm, Long uFileCnt, String docTitle) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.checkId = checkId;
        this.checkDt = checkDt;
        this.bankAcctNum = bankAcctNum;
        this.currencyCd = currencyCd;
        this.paymentAmt = paymentAmt;
        this.baseAmt = baseAmt;
        this.vendorNm = vendorNm;
        this.futurePayDueDt = futurePayDueDt;
        this.currencyConversionRate = currencyConversionRate;
        this.currencyConversionDt = currencyConversionDt;
        this.checkNo = checkNo;
        this.paymentFormat = paymentFormat;
        this.paymentMethodCd = paymentMethodCd;
        this.paymentMethod = paymentMethod;
        this.remark = remark;
        this.regId = regId;
        this.regNm = regNm;
        this.uFileCnt = uFileCnt;
        this.docTitle = docTitle;
    }

    // detail
    @QueryProjection
    public ErpItemSlipDto(String compCd, String slipNo, String slipType, BigDecimal slipHeaderId, BigDecimal checkId,
        LocalDateTime checkDt, String bankAcctNum, String currencyCd, String paymentAmt,
        BigDecimal baseAmt, String vendorNm, LocalDateTime futurePayDueDt,
        BigDecimal currencyConversionRate, LocalDateTime currencyConversionDt, BigDecimal checkNo,
        String paymentFormat, String paymentMethodCd, String paymentMethod, String remark,
        String regId, String regNm, String deptNo, String deptNm, String postingDt, String headerRemark,
        String bankNm, String bankAcctNm, String bankBranchNm, String acctHolderNm,
        String externalBankNm, String externalAcctNum, String status, Long uFileCnt, Long jiniCnt) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.checkId = checkId;
        this.checkDt = checkDt;
        this.bankAcctNum = bankAcctNum;
        this.currencyCd = currencyCd;
        this.paymentAmt = paymentAmt;
        this.baseAmt = baseAmt;
        this.vendorNm = vendorNm;
        this.futurePayDueDt = futurePayDueDt;
        this.currencyConversionRate = currencyConversionRate;
        this.currencyConversionDt = currencyConversionDt;
        this.checkNo = checkNo;
        this.paymentFormat = paymentFormat;
        this.paymentMethodCd = paymentMethodCd;
        this.paymentMethod = paymentMethod;
        this.remark = remark;
        this.regId = regId;
        this.regNm = regNm;
        this.deptNo = deptNo;
        this.deptNm = deptNm;
        this.postingDt = postingDt;
        this.headerRemark = headerRemark;
        this.bankNm = bankNm;
        this.bankAcctNm = bankAcctNm;
        this.bankBranchNm = bankBranchNm;
        this.acctHolderNm = acctHolderNm;
        this.externalBankNm = externalBankNm;
        this.externalAcctNum = externalAcctNum;
        this.status = status;
        this.uFileCnt = uFileCnt;
        this.jiniCnt = jiniCnt;
    }
}
