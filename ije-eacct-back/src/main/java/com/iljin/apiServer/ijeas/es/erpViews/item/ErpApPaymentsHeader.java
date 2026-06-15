package com.iljin.apiServer.ijeas.es.erpViews.item;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpApPaymentsHeaderKey.class)
@Table(name = "CBO_SP_AP_PAYMENTS_HEADER_V")
@Entity
public class ErpApPaymentsHeader {

    @Id
    @Column(name = "ORG_ID")
    Integer orgId;

    @Id
    @Column(name = "CHECK_ID", nullable = false)
    BigDecimal checkId;

    @Column(name = "ESLIP_TRANSFER_BATCH_ID")
    Long eslipTransferBatchId;

    @Column(name = "ESLIP_TRANSFER_ID")
    BigDecimal eslipTransferId;

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

    @Column(name = "BANK_NAME", nullable = false)
    String bankName;

    @Column(name = "BANK_BRANCH_NAME", nullable = false)
    String bankBranchName;

    @Column(name = "BANK_ACCOUNT_NAME", nullable = false)
    String bankAccountName;

    @Column(name = "BANK_ACCOUNT_NUM")
    String bankAccountNum;

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

    @Column(name = "EXTERNAL_BANK_NAME")
    String externalBankName;

    @Column(name = "EXTERNAL_ACCOUNT_NAME")
    String externalAccountName;

    @Column(name = "EXTERNAL_ACCOUNT_NUM")
    String externalAccountNum;

    @Column(name = "ACCOUNT_HOLDER_NAME")
    String accountHolderName;

    @Column(name = "PAYMENT_METHOD_CODE")
    String paymentMethodCode;

    @Column(name = "PAYMENT_METHOD")
    String paymentMethod;

    @Column(name = "CURRENCY_CODE", nullable = false)
    String currencyCode;

    @Column(name = "AMOUNT")
    String amount;

    @Column(name = "BASE_AMOUNT")
    BigDecimal baseAmount;

    @Column(name = "CURRENCY_CONVERSION_RATE")
    BigDecimal currencyConversionRate;

    @Column(name = "CURRENCY_CONVERSION_DATE")
    LocalDateTime currencyConvesionDate;

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

}
