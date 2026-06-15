package com.iljin.apiServer.ijeas.es.item;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ApPaymentsDtDto implements Serializable {

    String compCd;

    String slipNo;

    String slipType;

    BigDecimal checkId;

    BigDecimal slipHeaderId;

    String eslipTransferBatchId;

    BigDecimal eslipTransferId;

    String eslipTransfer;

    String eslipStatus;

    String transferType;

    String transferDt;

    String transferredBy;

    String bankNm;

    String bankBranchNm;

    String bankAcctNm;

    String bankAcctNum;

    BigDecimal checkNo;

    String paymentFormat;

    LocalDateTime checkDt;

    LocalDateTime futurePayDueDt;

    String vendorNm;

    String externalBankNm;

    String externalAcctNm;

    String externalAcctNum;

    String acctHolderNm;

    String paymentMethodCd;

    String paymentMethod;

    String paymentAmt;

    BigDecimal baseAmt;

    String currencyCd;

    BigDecimal currencyConversionRate;

    LocalDateTime currencyConversionDt;

    String remark;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    String usedForAmt;

    String usedAmt;


    @Builder
    @QueryProjection
    public ApPaymentsDtDto(String compCd,String slipNo,String slipType, BigDecimal checkId,
        BigDecimal slipHeaderId,String eslipTransferBatchId, BigDecimal eslipTransferId,
                           String eslipTransfer,String eslipStatus,String transferType,String transferDt,String transferredBy,String bankNm,String bankBranchNm,
                           String bankAcctNm,String bankAcctNum,BigDecimal checkNo,String paymentFormat,LocalDateTime checkDt,LocalDateTime futurePayDueDt,
                           String vendorNm,String externalBankNm,String externalAcctNm,String externalAcctNum,String acctHolderNm,String paymentMethodCd,
                           String paymentMethod,String paymentAmt,BigDecimal baseAmt,String currencyCd,BigDecimal currencyConversionRate,LocalDateTime currencyConversionDt,
                           String remark,String attribute1,String attribute2,String attribute3,String attribute4,String attribute5) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.checkId = checkId;
        this.slipHeaderId = slipHeaderId;
        this.eslipTransferBatchId = eslipTransferBatchId;
        this.eslipTransferId = eslipTransferId;
        this.eslipTransfer = eslipTransfer;
        this.eslipStatus = eslipStatus;
        this.transferType = transferType;
        this.transferDt = transferDt;
        this.transferredBy = transferredBy;
        this.bankNm = bankNm;
        this.bankBranchNm = bankBranchNm;
        this.bankAcctNm = bankAcctNm;
        this.bankAcctNum = bankAcctNum;
        this.checkNo = checkNo;
        this.paymentFormat = paymentFormat;
        this.checkDt = checkDt;
        this.futurePayDueDt = futurePayDueDt;
        this.vendorNm = vendorNm;
        this.externalBankNm = externalBankNm;
        this.externalAcctNm = externalAcctNm;
        this.externalAcctNum = externalAcctNum;
        this.acctHolderNm = acctHolderNm;
        this.paymentMethodCd = paymentMethodCd;
        this.paymentMethod = paymentMethod;
        this.paymentAmt = paymentAmt;
        this.baseAmt = baseAmt;
        this.currencyCd = currencyCd;
        this.currencyConversionRate = currencyConversionRate;
        this.currencyConversionDt = currencyConversionDt;
        this.remark = remark;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

    @Builder
    @QueryProjection
    public ApPaymentsDtDto(String compCd,String slipNo,String slipType,BigDecimal checkId,BigDecimal slipHeaderId,String eslipTransferBatchId,
                           String eslipTransfer,String eslipStatus,String transferType,String transferDt,String transferredBy,String bankNm,String bankBranchNm,
                           String bankAcctNm,String bankAcctNum,BigDecimal checkNo,String paymentFormat,LocalDateTime checkDt,LocalDateTime futurePayDueDt,
                           String vendorNm,String externalBankNm,String externalAcctNm,String externalAcctNum,String acctHolderNm,String paymentMethodCd,
                           String paymentMethod,String paymentAmt,BigDecimal baseAmt,String currencyCd,BigDecimal currencyConversionRate,LocalDateTime currencyConversionDt,
                           String remark,String attribute1,String attribute2,String attribute3,String attribute4,String attribute5,
                           String usedAmt , String usedForAmt) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.checkId = checkId;
        this.slipHeaderId = slipHeaderId;
        this.eslipTransferBatchId = eslipTransferBatchId;
        this.eslipTransfer = eslipTransfer;
        this.eslipStatus = eslipStatus;
        this.transferType = transferType;
        this.transferDt = transferDt;
        this.transferredBy = transferredBy;
        this.bankNm = bankNm;
        this.bankBranchNm = bankBranchNm;
        this.bankAcctNm = bankAcctNm;
        this.bankAcctNum = bankAcctNum;
        this.checkNo = checkNo;
        this.paymentFormat = paymentFormat;
        this.checkDt = checkDt;
        this.futurePayDueDt = futurePayDueDt;
        this.vendorNm = vendorNm;
        this.externalBankNm = externalBankNm;
        this.externalAcctNm = externalAcctNm;
        this.externalAcctNum = externalAcctNum;
        this.acctHolderNm = acctHolderNm;
        this.paymentMethodCd = paymentMethodCd;
        this.paymentMethod = paymentMethod;
        this.paymentAmt = paymentAmt;
        this.baseAmt = baseAmt;
        this.currencyCd = currencyCd;
        this.currencyConversionRate = currencyConversionRate;
        this.currencyConversionDt = currencyConversionDt;
        this.remark = remark;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.usedAmt = usedAmt;
        this.usedForAmt = usedForAmt;
    }



    public static ApPaymentsDtDto from(ApPaymentsDt apPaymentsDt) {
        return ApPaymentsDtDto.builder()
                .compCd(apPaymentsDt.getCompCd())
                .slipNo(apPaymentsDt.getSlipNo())
                .slipType(apPaymentsDt.getSlipType())
                .checkId(apPaymentsDt.getCheckId())
                .slipHeaderId(apPaymentsDt.getSlipHeaderId())
                .eslipTransferBatchId(apPaymentsDt.getEslipTransferBatchId())
                .eslipTransferId(apPaymentsDt.getEslipTransferId())
                .eslipTransfer(apPaymentsDt.getEslipTransfer())
                .eslipStatus(apPaymentsDt.getEslipStatus())
                .transferType(apPaymentsDt.getTransferType())
                .transferDt(apPaymentsDt.getTransferDt())
                .transferredBy(apPaymentsDt.getTransferredBy())
                .bankNm(apPaymentsDt.getBankNm())
                .bankBranchNm(apPaymentsDt.getBankBranchNm())
                .bankAcctNm(apPaymentsDt.getBankAcctNm())
                .bankAcctNum(apPaymentsDt.getBankAcctNum())
                .checkNo(apPaymentsDt.getCheckNo())
                .paymentFormat(apPaymentsDt.getPaymentFormat())
                .checkDt(apPaymentsDt.getCheckDt())
                .futurePayDueDt(apPaymentsDt.getFuturePayDueDt())
                .vendorNm(apPaymentsDt.getVendorNm())
                .externalBankNm(apPaymentsDt.getExternalBankNm())
                .externalAcctNm(apPaymentsDt.getExternalAcctNm())
                .externalAcctNum(apPaymentsDt.getExternalAcctNum())
                .acctHolderNm(apPaymentsDt.getAcctHolderNm())
                .paymentMethodCd(apPaymentsDt.getPaymentMethodCd())
                .paymentMethod(apPaymentsDt.getPaymentMethod())
                .paymentAmt(apPaymentsDt.getPaymentAmt())
                .baseAmt(apPaymentsDt.getBaseAmt())
                .currencyCd(apPaymentsDt.getCurrencyCd())
                .currencyConversionRate(apPaymentsDt.getCurrencyConversionRate())
                .currencyConversionDt(apPaymentsDt.getCurrencyConversionDt())
                .remark(apPaymentsDt.getRemark())
                .attribute1(apPaymentsDt.getAttribute1())
                .attribute2(apPaymentsDt.getAttribute2())
                .attribute3(apPaymentsDt.getAttribute3())
                .attribute4(apPaymentsDt.getAttribute4())
                .attribute5(apPaymentsDt.getAttribute5())
                .build();
    }
}
