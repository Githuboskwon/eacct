package com.iljin.apiServer.ijeas.es.item;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.bond.ErpApFuturesCleared;
import com.iljin.apiServer.ijeas.es.erpViews.item.ErpApPaymentsHeader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@IdClass(ApPaymentsDtKey.class)
@Table(name = "TB_AP_PAYMENTS_DT")
@Entity
public class ApPaymentsDt extends BaseEntity {

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "SLIP_NO", nullable = false)
    String slipNo;

    @Id
    @Column(name = "SLIP_TYPE", nullable = false)
    String slipType;

    @Id
    @Column(name = "CHECK_ID", nullable = false)
    BigDecimal checkId;

    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Column(name = "ESLIP_TRANSFER_BATCH_ID")
    String eslipTransferBatchId;

    @Column(name = "ESLIP_TRANSFER_ID")
    BigDecimal eslipTransferId;

    @Column(name = "ESLIP_TRANSFER")
    String eslipTransfer;

    @Column(name = "ESLIP_STATUS")
    String eslipStatus;

    @Column(name = "TRANSFER_TYPE")
    String transferType;

    @Column(name = "TRANSFER_DT")
    String transferDt;

    @Column(name = "TRANSFERRED_BY")
    String transferredBy;

    @Column(name = "BANK_NM")
    String bankNm;

    @Column(name = "BANK_BRANCH_NM")
    String bankBranchNm;

    @Column(name = "BANK_ACCT_NM")
    String bankAcctNm;

    @Column(name = "BANK_ACCT_NUM")
    String bankAcctNum;

    @Column(name = "CHECK_NO")
    BigDecimal checkNo;

    @Column(name = "PAYMENT_FORMAT")
    String paymentFormat;

    @Column(name = "CHECK_DT")
    LocalDateTime checkDt;

    @Column(name = "FUTURE_PAY_DUE_DT")
    LocalDateTime futurePayDueDt;

    @Column(name = "VENDOR_NM")
    String vendorNm;

    @Column(name = "EXTERNAL_BANK_NM")
    String externalBankNm;

    @Column(name = "EXTERNAL_ACCT_NM")
    String externalAcctNm;

    @Column(name = "EXTERNAL_ACCT_NUM")
    String externalAcctNum;

    @Column(name = "ACCT_HOLDER_NM")
    String acctHolderNm;

    @Column(name = "PAYMENT_METHOD_CD")
    String paymentMethodCd;

    @Column(name = "PAYMENT_METHOD")
    String paymentMethod;

    @Column(name = "PAYMENT_AMT")
    String paymentAmt;

    @Column(name = "BASE_AMT")
    BigDecimal baseAmt;

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Column(name = "CURRENCY_CONVERSION_RATE")
    BigDecimal currencyConversionRate;

    @Column(name = "CURRENCY_CONVERSION_DT")
    LocalDateTime currencyConversionDt;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "ATTRIBUTE1")
    String attribute1;

    @Column(name = "ATTRIBUTE2")
    String attribute2;

    @Column(name = "ATTRIBUTE3")
    String attribute3;

    @Column(name = "ATTRIBUTE4")
    String attribute4;

    @Column(name = "ATTRIBUTE5")
    String attribute5;

    @Builder
    public ApPaymentsDt(String compCd, String slipNo, String slipType, BigDecimal checkId,
        BigDecimal slipHeaderId,
        String eslipTransferBatchId, BigDecimal eslipTransferId, String eslipTransfer, String eslipStatus,
        String transferType, String transferDt, String transferredBy, String bankNm,
        String bankBranchNm, String bankAcctNm, String bankAcctNum, BigDecimal checkNo,
        String paymentFormat,
        LocalDateTime checkDt, LocalDateTime futurePayDueDt, String vendorNm, String externalBankNm,
        String externalAcctNm, String externalAcctNum, String acctHolderNm, String paymentMethodCd,
        String paymentMethod, String paymentAmt, BigDecimal baseAmt, String currencyCd,
        BigDecimal currencyConversionRate, LocalDateTime currencyConversionDt, String remark,
        String attribute1, String attribute2, String attribute3, String attribute4,
        String attribute5) {
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

    public static ApPaymentsDt from(ErpApPaymentsHeader erpHeader, String slipNo, BigDecimal slipHeaderId) {
        return ApPaymentsDt.builder()
            .slipNo(slipNo)
            .slipType(SlipType.ITEM.getCode())
            .slipHeaderId(slipHeaderId)
            .compCd(String.valueOf(erpHeader.getOrgId()))
            .checkId(erpHeader.getCheckId())
            .eslipTransferBatchId(String.valueOf(erpHeader.getEslipTransferBatchId()))
            .eslipTransferId(erpHeader.getEslipTransferId())
            .eslipTransfer(erpHeader.getEslipTransfer())
            .eslipStatus(erpHeader.getEslipStatus())
            .transferType(erpHeader.getTransferType())
            .transferDt(erpHeader.getTransferDate())
            .transferredBy(erpHeader.getTransferredBy())
            .bankNm(erpHeader.getBankName())
            .bankBranchNm(erpHeader.getBankBranchName())
            .bankAcctNm(erpHeader.getBankAccountName())
            .bankAcctNum(erpHeader.getBankAccountNum())
            .checkNo(erpHeader.getCheckNumber())
            .paymentFormat(erpHeader.getPaymentFormat())
            .paymentMethod(erpHeader.getPaymentMethod())
            .paymentMethodCd(erpHeader.getPaymentMethodCode())
            .checkDt(erpHeader.getCheckDate())
            .futurePayDueDt(erpHeader.getFuturePayDueDate())
            .vendorNm(erpHeader.getVendorName())
            .externalBankNm(erpHeader.getExternalBankName())
            .externalAcctNm(erpHeader.getExternalAccountName())
            .externalAcctNum(erpHeader.getExternalAccountNum())
            .acctHolderNm(erpHeader.getAccountHolderName())
            .paymentAmt(erpHeader.getAmount())
            .baseAmt(erpHeader.getBaseAmount())
            .currencyCd(erpHeader.getCurrencyCode())
            .currencyConversionRate(erpHeader.getCurrencyConversionRate())
            .currencyConversionDt(erpHeader.getCurrencyConvesionDate())
            .remark(erpHeader.getRemark())
            .attribute1(erpHeader.getAttribute1())
            .attribute2(erpHeader.getAttribute2())
            .attribute3(erpHeader.getAttribute3())
            .attribute4(erpHeader.getAttribute4())
            .attribute5(erpHeader.getAttribute5())
            .build();
    }

    /* 대량지급 dt 생성 */
    public static ApPaymentsDt from(ErpApPaymentsHeader erpHeader, String slipNo, BigDecimal slipHeaderId , String slipType) {
        return ApPaymentsDt.builder()
                .slipNo(slipNo)
                .slipType(slipType)
                .slipHeaderId(slipHeaderId)
                .compCd(String.valueOf(erpHeader.getOrgId()))
                .checkId(erpHeader.getCheckId())
                .eslipTransferBatchId(String.valueOf(erpHeader.getEslipTransferBatchId()))
                .eslipTransferId(erpHeader.getEslipTransferId())
                .eslipTransfer(erpHeader.getEslipTransfer())
                .eslipStatus(erpHeader.getEslipStatus())
                .transferType(erpHeader.getTransferType())
                .transferDt(erpHeader.getTransferDate())
                .transferredBy(erpHeader.getTransferredBy())
                .bankNm(erpHeader.getBankName())
                .bankBranchNm(erpHeader.getBankBranchName())
                .bankAcctNm(erpHeader.getBankAccountName())
                .bankAcctNum(erpHeader.getBankAccountNum())
                .checkNo(erpHeader.getCheckNumber())
                .paymentFormat(erpHeader.getPaymentFormat())
                .paymentMethod(erpHeader.getPaymentMethod())
                .paymentMethodCd(erpHeader.getPaymentMethodCode())
                .checkDt(erpHeader.getCheckDate())
                .futurePayDueDt(erpHeader.getFuturePayDueDate())
                .vendorNm(erpHeader.getVendorName())
                .externalBankNm(erpHeader.getExternalBankName())
                .externalAcctNm(erpHeader.getExternalAccountName())
                .externalAcctNum(erpHeader.getExternalAccountNum())
                .acctHolderNm(erpHeader.getAccountHolderName())
                .paymentAmt(erpHeader.getAmount())
                .baseAmt(erpHeader.getBaseAmount())
                .currencyCd(erpHeader.getCurrencyCode())
                .currencyConversionRate(erpHeader.getCurrencyConversionRate())
                .currencyConversionDt(erpHeader.getCurrencyConvesionDate())
                .remark(erpHeader.getRemark())
                .attribute1(erpHeader.getAttribute1())
                .attribute2(erpHeader.getAttribute2())
                .attribute3(erpHeader.getAttribute3())
                .attribute4(erpHeader.getAttribute4())
                .attribute5(erpHeader.getAttribute5())
                .build();
    }

    public static ApPaymentsDt from(ErpApFuturesCleared erpCleared, String slipNo, BigDecimal slipHeaderId) {
        return ApPaymentsDt.builder()
                .slipNo(slipNo)
                .slipType(SlipType.BULK.getCode())
                .slipHeaderId(slipHeaderId)
                .compCd(String.valueOf(erpCleared.getOrgId()))
                .checkId(new BigDecimal(erpCleared.getCheckId()))
                .eslipTransferBatchId(String.valueOf(erpCleared.getEslipTransferBatchId()))
                .eslipTransfer(erpCleared.getEslipTransfer())
                .eslipStatus(erpCleared.getEslipStatus())
                .transferType(erpCleared.getTransferType())
                .transferDt(erpCleared.getTransferDate())
                .transferredBy(erpCleared.getTransferredBy())
                .bankNm(erpCleared.getBankName())
                .bankBranchNm(erpCleared.getBankBranchName())
                .bankAcctNm(erpCleared.getBankAccountName())
                .bankAcctNum(erpCleared.getBankAccountNum())
                .checkNo(erpCleared.getCheckNumber())
                .paymentFormat(erpCleared.getPaymentFormat())
                .paymentMethodCd(erpCleared.getPaymentMethodLookupCode())
                .checkDt(erpCleared.getCheckDate())
                .futurePayDueDt(erpCleared.getFuturePayDueDate())
                .vendorNm(erpCleared.getVendorName())
                .externalAcctNm(erpCleared.getExternalAccountName())
                .externalAcctNum(erpCleared.getExternalAccountNum())
                .paymentAmt(erpCleared.getAmount())
                .baseAmt(erpCleared.getBaseAmount())
                .currencyCd(erpCleared.getCurrencyCode())
                .currencyConversionRate(erpCleared.getCurrencyConversionRate())
                .currencyConversionDt(erpCleared.getCurrencyConversionDate())
                .attribute1(erpCleared.getAttribute1())
                .attribute2(erpCleared.getAttribute2())
                .attribute3(erpCleared.getAttribute3())
                .attribute4(erpCleared.getAttribute4())
                .attribute5(erpCleared.getAttribute5())
                .build();
    }

}
