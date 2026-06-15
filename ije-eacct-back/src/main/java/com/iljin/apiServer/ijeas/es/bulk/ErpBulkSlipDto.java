package com.iljin.apiServer.ijeas.es.bulk;

import com.iljin.apiServer.ijeas.es.item.ApPaymentsDtDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class ErpBulkSlipDto implements Serializable {

    private static final long serialVersionUID = 5281003613811924734L;

    String compCd;

    String slipType;

    String eslipTransferBatchId;

    String eslipTransfer;

    String slipNo;

    Integer slipHeaderId;

    String transferType;

    String transferDt;

    String transferredBy;

    String bankNm;

    String bankBranchNm;

    String bankAcctNm;

    String bankAcctNum;

    Integer bankAcctId;

    String segment3;

    String segment4;

    String segment3Desc;

    String segment4Desc;

    LocalDateTime futurePayDueDt;

    BigDecimal sumAmt;

    LocalDateTime checkDt;

    String remark;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    String usedCur;

    String usedForAmt;

    String usedAmt;

    String paymentMethodLookupCode;

    String currencyCd;

    String paymentAmt;

    BigDecimal baseAmt;

    String vendorNm;

    BigDecimal currencyConversionRate;

    LocalDateTime currencyConversionDt;

    BigDecimal checkNo;

    String paymentFormat;

    String paymentMethodCd;

    String paymentMethod;

    String regId;

    String regNm;

    String deptNo;

    String deptNm;

    String postingDt;

    String headerRemark;

    String acctHolderNm;

    String externalBankNm;

    String externalAcctNum;

    Long uFileCnt;

    Long jiniCnt;

    List<ApPaymentsDtDto> dtList;

    String docTitle;

    // list

    @QueryProjection
    public ErpBulkSlipDto(String compCd,String eslipTransferBatchId,String bankNm,String bankAcctNum,
                          LocalDateTime checkDt,String usedCur,String usedForAmt,String usedAmt,String paymentMethodLookupCode,
                          String slipNo, String slipType, Integer slipHeaderId, Long uFileCnt, String docTitle) {
        this.compCd = compCd;
        this.eslipTransferBatchId = eslipTransferBatchId;
        this.bankNm = bankNm;
        this.bankAcctNum = bankAcctNum;
        this.checkDt = checkDt;
        this.usedCur = usedCur;
        this.usedForAmt = usedForAmt;
        this.usedAmt = usedAmt;
        this.paymentMethodLookupCode = paymentMethodLookupCode;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.uFileCnt = uFileCnt;
        this.docTitle = docTitle;
    }

    String status;

    @QueryProjection
    public ErpBulkSlipDto(String compCd, String slipNo, Integer slipHeaderId ,String slipType,
                          LocalDateTime checkDt, String bankAcctNum, String usedCur,
                          String usedAmt, String usedForAmt, LocalDateTime futurePayDueDt,String remark,
                          String regId, String regNm, String deptNo, String deptNm, String postingDt, String headerRemark,
                          String bankNm, String bankAcctNm, String bankBranchNm, String eslipTransferBatchId,
                          BigDecimal sumAmt, String status, Long uFileCnt, Long jiniCnt) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.slipType = slipType;
        this.checkDt = checkDt;
        this.bankAcctNum = bankAcctNum;
        this.usedCur = usedCur;
        this.usedForAmt = usedForAmt;
        this.usedAmt = usedAmt;
        this.futurePayDueDt = futurePayDueDt;
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
        this.eslipTransferBatchId = eslipTransferBatchId;
        this.sumAmt = sumAmt;
        this.status = status;
        this.uFileCnt = uFileCnt;
        this.jiniCnt = jiniCnt;
    }
}
