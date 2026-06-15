package com.iljin.apiServer.ijeas.es.fund;

import com.iljin.apiServer.ijeas.es.fund.SpTrFundDt;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class ErpFundSlipDto implements Serializable {

    private static final long serialVersionUID = 5281003613811924734L;

    String compCd;

    String slipNo;

    BigDecimal slipHeaderId;

    String slipGroupNo;

    String deptNo;

    String slipType;

    String slipTypeCd;

    String slipForm;

    String status;

    String postingDt;

    String headerRemark;

    String usedCur;

    String usedAmt;

    String usedForAmt;

    String scanNo;

    String docNo;

    String evidenceYn;

    BigDecimal approvalGroupId;

    BigDecimal taxbillSupplyAmt;

    BigDecimal taxbillTaxAmt;

    BigDecimal taxbillTotalAmt;

    String taxSmartbillNo;

    Integer ledgerId;

    String validationFlag;

    String errorMsg;

    String docTitle;

    String docUrl;

    String erpInvoiceId;

    String erpAppUserId;

    String erpVendorNm;

    String erpXtrSlipType;

    String erpTransactionNo;

    String taxbillAmtModifyYn;

    String taxbillSuId;

    String transferType;

    String remark;

    BigDecimal debitAmount;

    BigDecimal creditAmount;

    BigDecimal accountedDr;

    BigDecimal accountedCr;

    String dealType;

    String productType;

    String xtrSlipType;

    Integer batchId;

    String dealNum;

    String transactionNum;

    String originTrxNum;

    String dealSubtype;

    LocalDateTime journalDt;

    BigDecimal transactionRate;

    String description;

    String currencyCodeHeader;

    BigDecimal debitAmt;

    BigDecimal creditAmt;

    String amountType;

    String transactionType;

    BigDecimal dailyRate;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    String chgNm;

    String slipSeq;

    String currencyCode;

    String segment1_2;

    String bankNm;

    String accountNo;

    String currencyCodeLine;

    String deptCd;

    String deptNm;

    String erpRegId;

    String chgId;

    Long uFileCnt;

    Long jiniCnt;

    BigDecimal buyAmt;

    BigDecimal sellAmt;

    BigDecimal profitAmt;

    BigDecimal slipAmt;

    List<SpTrFundDt> dtList;

    // list

    @QueryProjection
    public ErpFundSlipDto(String compCd,String slipNo,BigDecimal slipHeaderId,String slipGroupNo,String deptNo,
                          String slipType,String slipTypeCd,String slipForm,String status,String postingDt,
                          String headerRemark,String usedCur,String usedAmt,String usedForAmt,String scanNo,
                          String docNo,String evidenceYn,BigDecimal approvalGroupId,BigDecimal taxbillSupplyAmt,
                          BigDecimal taxbillTaxAmt,BigDecimal taxbillTotalAmt,String taxSmartbillNo,Integer ledgerId,
                          String validationFlag,String errorMsg,String docTitle,String docUrl,String erpInvoiceId,
                          String erpAppUserId,String erpVendorNm,String erpXtrSlipType,String erpTransactionNo,
                          String taxbillAmtModifyYn,String taxbillSuId,String transferType,String remark,
                          BigDecimal debitAmount, BigDecimal creditAmount, BigDecimal accountedDr,
                          BigDecimal accountedCr, String dealType, String productType
                          ) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.slipGroupNo = slipGroupNo;
        this.deptNo = deptNo;
        this.slipType = slipType;
        this.slipTypeCd = slipTypeCd;
        this.slipForm = slipForm;
        this.status = status;
        this.postingDt = postingDt;
        this.headerRemark = headerRemark;
        this.usedCur = usedCur;
        this.usedAmt = usedAmt;
        this.usedForAmt = usedForAmt;
        this.scanNo = scanNo;
        this.docNo = docNo;
        this.evidenceYn = evidenceYn;
        this.approvalGroupId = approvalGroupId;
        this.taxbillSupplyAmt = taxbillSupplyAmt;
        this.taxbillTaxAmt = taxbillTaxAmt;
        this.taxbillTotalAmt = taxbillTotalAmt;
        this.taxSmartbillNo = taxSmartbillNo;
        this.ledgerId = ledgerId;
        this.validationFlag = validationFlag;
        this.errorMsg = errorMsg;
        this.docTitle = docTitle;
        this.docUrl = docUrl;
        this.erpInvoiceId = erpInvoiceId;
        this.erpAppUserId = erpAppUserId;
        this.erpVendorNm = erpVendorNm;
        this.erpXtrSlipType = erpXtrSlipType;
        this.erpTransactionNo = erpTransactionNo;
        this.taxbillAmtModifyYn = taxbillAmtModifyYn;
        this.taxbillSuId = taxbillSuId;
        this.transferType = transferType;
        this.remark = remark;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.accountedDr = accountedDr;
        this.accountedCr = accountedCr;
        this.dealType = dealType;
        this.productType = productType;
    }

    @QueryProjection
    public ErpFundSlipDto(String compCd,String slipNo,String slipType,BigDecimal slipHeaderId,
                          String xtrSlipType,Integer batchId,String dealNum,String transactionNum,
                          String dealType,String productType,String originTrxNum,String dealSubtype,
                          LocalDateTime journalDt,BigDecimal transactionRate,String description,
                          String currencyCodeHeader,BigDecimal buyAmt,BigDecimal sellAmt,
                          BigDecimal profitAmt,BigDecimal slipAmt,String amountType,
                          String transactionType,BigDecimal dailyRate,String attribute1,
                          String attribute2,String attribute3,String attribute4,String attribute5,
                          String chgNm, Long uFileCnt, String docTitle){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.xtrSlipType = xtrSlipType;
        this.batchId = batchId;
        this.dealNum = dealNum;
        this.transactionNum = transactionNum;
        this.dealType = dealType;
        this.productType = productType;
        this.originTrxNum = originTrxNum;
        this.dealSubtype = dealSubtype;
        this.journalDt = journalDt;
        this.transactionRate = transactionRate;
        this.description = description;
        this.currencyCodeHeader = currencyCodeHeader;
        this.buyAmt = buyAmt;
        this.sellAmt = sellAmt;
        this.profitAmt = profitAmt;
        this.slipAmt = slipAmt;
        this.amountType = amountType;
        this.transactionType = transactionType;
        this.dailyRate = dailyRate;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.chgNm = chgNm;
        this.uFileCnt = uFileCnt;
        this.docTitle = docTitle;
    }


    @QueryProjection
    public ErpFundSlipDto(String compCd,String slipNo,String slipType,BigDecimal slipHeaderId,
                      String xtrSlipType,Integer batchId,String dealNum,String transactionNum,
                      String dealType,String productType,String originTrxNum,String dealSubtype,
                      LocalDateTime journalDt,BigDecimal transactionRate,String description,
                      String currencyCodeHeader,BigDecimal buyAmt,BigDecimal sellAmt,
                      BigDecimal profitAmt,BigDecimal slipAmt,String amountType,
                      String transactionType,BigDecimal dailyRate,String attribute1,
                      String attribute2,String attribute3,String attribute4,String attribute5,
                      String chgId ,String chgNm, String deptCd, String deptNm, String erpRegId,
                      String status, Long uFileCnt, Long jiniCnt){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.xtrSlipType = xtrSlipType;
        this.batchId = batchId;
        this.dealNum = dealNum;
        this.transactionNum = transactionNum;
        this.dealType = dealType;
        this.productType = productType;
        this.originTrxNum = originTrxNum;
        this.dealSubtype = dealSubtype;
        this.journalDt = journalDt;
        this.transactionRate = transactionRate;
        this.description = description;
        this.currencyCodeHeader = currencyCodeHeader;
        this.buyAmt = buyAmt;
        this.sellAmt = sellAmt;
        this.profitAmt = profitAmt;
        this.slipAmt = slipAmt;
        this.amountType = amountType;
        this.transactionType = transactionType;
        this.dailyRate = dailyRate;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.chgId = chgId;
        this.chgNm = chgNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.erpRegId = erpRegId;
        this.status = status;
        this.uFileCnt = uFileCnt;
        this.jiniCnt = jiniCnt;
    }



    @QueryProjection
    public ErpFundSlipDto(String compCd,String slipNo,String slipType, BigDecimal slipHeaderId, String slipSeq,
                      String xtrSlipType,Integer batchId,String dealNum,String transactionNum,
                      String originTrxNum,String dealType,String dealSubtype,String productType,
                      String amountType,String transactionType,String currencyCodeHeader,String currencyCode,
                      BigDecimal dailyRate,BigDecimal transactionRate,LocalDateTime journalDt,String segment1_2,
                      String description,BigDecimal debitAmt,BigDecimal creditAmt,BigDecimal accountedDr,
                      BigDecimal accountedCr,String bankNm,String accountNo,String currencyCodeLine,
                      String attribute1,String attribute2,String attribute3,String attribute4,String attribute5){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.slipSeq = slipSeq;
        this.xtrSlipType = xtrSlipType;
        this.batchId = batchId;
        this.dealNum = dealNum;
        this.transactionNum = transactionNum;
        this.originTrxNum = originTrxNum;
        this.dealType = dealType;
        this.dealSubtype = dealSubtype;
        this.productType = productType;
        this.amountType = amountType;
        this.transactionType = transactionType;
        this.currencyCodeHeader = currencyCodeHeader;
        this.currencyCode = currencyCode;
        this.dailyRate = dailyRate;
        this.transactionRate = transactionRate;
        this.journalDt = journalDt;
        this.segment1_2 = segment1_2;
        this.description = description;
        this.debitAmt = debitAmt;
        this.creditAmt = creditAmt;
        this.accountedDr = accountedDr;
        this.accountedCr = accountedCr;
        this.bankNm = bankNm;
        this.accountNo = accountNo;
        this.currencyCodeLine = currencyCodeLine;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

//    @QueryProjection
//    public ErpBulkSlipDto(String compCd,String slipType,String eslipTransferBatchId,String eslipTransfer,String slipNo,
//                          Integer slipHeaderId,String transferType,String transferDt,String transferredBy,String bankNm,
//                          String bankBranchNm,String bankAcctNm,String bankAcctNum,Integer bankAcctId,String segment3,
//                          String segment4,String segment3Desc,String segment4Desc,LocalDateTime futurePayDueDt,Integer sumAmt,LocalDateTime checkDt,
//                          String remark,String attribute1,String attribute2,String attribute3,String attribute4,String attribute5) {
//        this.compCd = compCd;
//        this.slipType = slipType;
//        this.eslipTransferBatchId = eslipTransferBatchId;
//        this.eslipTransfer = eslipTransfer;
//        this.slipNo = slipNo;
//        this.slipHeaderId = slipHeaderId;
//        this.transferType = transferType;
//        this.transferDt = transferDt;
//        this.transferredBy = transferredBy;
//        this.bankNm = bankNm;
//        this.bankBranchNm = bankBranchNm;
//        this.bankAcctNm = bankAcctNm;
//        this.bankAcctNum = bankAcctNum;
//        this.bankAcctId = bankAcctId;
//        this.segment3 = segment3;
//        this.segment4 = segment4;
//        this.segment3Desc = segment3Desc;
//        this.segment4Desc = segment4Desc;
//        this.futurePayDueDt = futurePayDueDt;
//        this.sumAmt = sumAmt;
//        this.checkDt = checkDt;
//        this.remark = remark;
//        this.attribute1 = attribute1;
//        this.attribute2 = attribute2;
//        this.attribute3 = attribute3;
//        this.attribute4 = attribute4;
//        this.attribute5 = attribute5;
//    }

    // detail
//    @QueryProjection
//    public ErpBulkSlipDto(String compCd, String slipNo, String slipType,
//                          LocalDateTime checkDt, String bankAcctNum, String currencyCd, String paymentAmt,
//                          BigDecimal baseAmt, String vendorNm, LocalDateTime futurePayDueDt,
//                          BigDecimal currencyConversionRate, LocalDateTime currencyConversionDt, BigDecimal checkNo,
//                          String paymentFormat, String paymentMethodCd, String paymentMethod, String remark,
//                          String regId, String regNm, String deptNo, String deptNm, String postingDt, String headerRemark,
//                          String bankNm, String bankAcctNm, String bankBranchNm, String acctHolderNm,
//                          String externalBankNm, String externalAcctNum) {
//        this.compCd = compCd;
//        this.slipNo = slipNo;
//        this.slipType = slipType;
//        this.checkDt = checkDt;
//        this.bankAcctNum = bankAcctNum;
//        this.currencyCd = currencyCd;
//        this.paymentAmt = paymentAmt;
//        this.baseAmt = baseAmt;
//        this.vendorNm = vendorNm;
//        this.futurePayDueDt = futurePayDueDt;
//        this.currencyConversionRate = currencyConversionRate;
//        this.currencyConversionDt = currencyConversionDt;
//        this.checkNo = checkNo;
//        this.paymentFormat = paymentFormat;
//        this.paymentMethodCd = paymentMethodCd;
//        this.paymentMethod = paymentMethod;
//        this.remark = remark;
//        this.regId = regId;
//        this.regNm = regNm;
//        this.deptNo = deptNo;
//        this.deptNm = deptNm;
//        this.postingDt = postingDt;
//        this.headerRemark = headerRemark;
//        this.bankNm = bankNm;
//        this.bankAcctNm = bankAcctNm;
//        this.bankBranchNm = bankBranchNm;
//        this.acctHolderNm = acctHolderNm;
//        this.externalBankNm = externalBankNm;
//        this.externalAcctNum = externalAcctNum;
//    }


//    @QueryProjection
//    public ErpBondSlipDto(String compCd, String slipNo, String slipType,
//                          LocalDateTime checkDt, String bankAcctNum, String usedCur, String usedAmt,
//                          LocalDateTime futurePayDueDt, String remark,
//                          String regId, String regNm, String deptNo, String deptNm, String postingDt, String headerRemark,
//                          String bankNm, String bankAcctNm, String bankBranchNm, String eslipTransferBatchId) {
//        this.compCd = compCd;
//        this.slipNo = slipNo;
//        this.slipType = slipType;
//        this.checkDt = checkDt;
//        this.bankAcctNum = bankAcctNum;
//        this.usedCur = usedCur;
//        this.usedAmt = usedAmt;
//        this.futurePayDueDt = futurePayDueDt;
//        this.remark = remark;
//        this.regId = regId;
//        this.regNm = regNm;
//        this.deptNo = deptNo;
//        this.deptNm = deptNm;
//        this.postingDt = postingDt;
//        this.headerRemark = headerRemark;
//        this.bankNm = bankNm;
//        this.bankAcctNm = bankAcctNm;
//        this.bankBranchNm = bankBranchNm;
//        this.eslipTransferBatchId = eslipTransferBatchId;
//    }
}
