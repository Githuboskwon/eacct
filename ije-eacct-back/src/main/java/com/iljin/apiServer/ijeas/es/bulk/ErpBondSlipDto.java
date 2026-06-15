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
public class ErpBondSlipDto implements Serializable {

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
    public ErpBondSlipDto(String compCd, String eslipTransferBatchId,  String eslipTransfer, String transferType, String transferDt, String transferredBy,
                          String bankNm, String bankBranchNm, String bankAcctNm, String bankAcctNum, String segment3, String segment4, String segment3Desc,
                          String segment4Desc, LocalDateTime futurePayDueDt, BigDecimal sumAmt, String remark, String paymentMethodCd,
                          String slipNo , Integer slipHeaderId, String slipType, Long uFileCnt, String docTitle) {
        this.compCd = compCd;
        this.eslipTransferBatchId = eslipTransferBatchId;
        this.eslipTransfer = eslipTransfer;
        this.transferType = transferType;
        this.transferDt = transferDt;
        this.transferredBy = transferredBy;
        this.bankNm = bankNm;
        this.bankBranchNm = bankBranchNm;
        this.bankAcctNm = bankAcctNm;
        this.bankAcctNum = bankAcctNum;
        this.segment3 = segment3;
        this.segment4 = segment4;
        this.segment3Desc = segment3Desc;
        this.segment4Desc = segment4Desc;
        this.futurePayDueDt = futurePayDueDt;
        this.sumAmt = sumAmt;
        this.remark = remark;
        this.paymentMethodCd = paymentMethodCd;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.slipType = slipType;
        this.uFileCnt = uFileCnt;
        this.docTitle = docTitle;
    }

}
