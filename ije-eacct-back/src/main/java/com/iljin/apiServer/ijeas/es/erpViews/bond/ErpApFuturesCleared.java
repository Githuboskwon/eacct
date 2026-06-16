package com.iljin.apiServer.ijeas.es.erpViews.bond;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpApFuturesClearedKey.class)
@Table(name = "CBO_SP_AP_FUTURE_CLEARED_V")
@Entity
public class ErpApFuturesCleared {

    @Id
    @Column(name = "ORG_ID")
    Integer orgId;

    @Id
    @Column(name = "CHECK_ID", nullable = false)
    Integer checkId;

    @Column(name = "ESLIP_TRANSFER_BATCH_ID")
    String eslipTransferBatchId;

    @Column(name = "ESLIP_TRANSFER_ID")
    String eslipTransferId;

    @Column(name = "ESLIP_TRANSFER")
    String eslipTransfer;

    @Column(name = "TRANSFER_TYPE")
    String transferType;

    @Column(name = "TRANSFER_DATE")
    String transferDate;

    @Column(name = "TRANSFERRED_BY")
    String transferredBy;

    @Column(name = "ESLIP_STATUS")
    String eslipStatus;

    @Column(name = "BANK_ACCOUNT_ID")
    Integer bankAccountId;

    @Column(name = "BANK_NAME", nullable = false)
    String bankName;

    @Column(name = "BANK_BRANCH_NAME", nullable = false)
    String bankBranchName;

    @Column(name = "BANK_ACCOUNT_NAME", nullable = false)
    String bankAccountName;

    @Column(name = "BANK_ACCOUNT_NUM")
    String bankAccountNum;

    @Column(name = "PAYMENT_METHOD_LOOKUP_CODE")
    String paymentMethodLookupCode;

    @Column(name = "CHECK_NUMBER")
    BigDecimal checkNumber;

    @Column(name = "PAYMENT_FORMAT")
    String paymentFormat;

    @Column(name = "CHECK_DATE")
    LocalDateTime checkDate;

    @Column(name = "FUTURE_PAY_DUE_DATE")
    LocalDateTime futurePayDueDate;

    @Column(name = "VENDOR_NAME")
    String vendorName;

    @Column(name = "EXTERNAL_ACCOUNT_NAME")
    String externalAccountName;

    @Column(name = "EXTERNAL_ACCOUNT_NUM")
    String externalAccountNum;

    @Column(name = "AMOUNT")
    String amount;

    @Column(name = "BASE_AMOUNT")
    BigDecimal baseAmount;

    @Column(name = "CURRENCY_CODE")
    String currencyCode;

    @Column(name = "CURRENCY_CONVERSION_RATE")
    BigDecimal currencyConversionRate;

    @Column(name = "CURRENCY_CONVERSION_DATE")
    LocalDateTime currencyConversionDate;

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
}
