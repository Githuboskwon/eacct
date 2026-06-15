package com.iljin.apiServer.ijeas.es.erpViews.bulk;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpApPaymentsBatchKey.class)
@Table(name = "CBO_SP_AP_PAYMENTS_BATCH_V")
@Entity
public class ErpApPaymentsBatch {

    @Id
    @Column(name = "ORG_ID")
    Integer orgId;

    @Id
    @Column(name = "ESLIP_TRANSFER_BATCH_ID", nullable = false)
    Integer eslipTransferBatchId;

    @Column(name = "ESLIP_TRANSFER")
    String eslipTransfer;

    @Column(name = "TRANSFER_TYPE")
    String transferType;

    @Column(name = "TRANSFER_DATE")
    String transferDate;

    @Column(name = "TRANSFERRED_BY")
    String transferredBy;

    @Column(name = "BANK_NAME", nullable = false)
    String bankName;

    @Column(name = "BANK_BRANCH_NAME", nullable = false)
    String bankBranchName;

    @Column(name = "BANK_ACCOUNT_NAME", nullable = false)
    String bankAccountName;

    @Column(name = "BANK_ACCOUNT_NUM")
    String bankAccountNum;

    @Column(name = "CHECK_DATE")
    LocalDateTime checkDate;

    @Column(name = "SUM_AMOUNT")
    BigDecimal sumAmount;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "PAYMENT_METHOD_LOOKUP_CODE")
    String paymentMethodLookupCode;

    @Column(name = "BANK_ACCOUNT_ID")
    Integer bankAccountId;

    @Column(name = "SUM_BASE_AMOUNT")
    BigDecimal sumBaseAmount;

    @Column(name = "CURRENCY_CODE")
    String currencyCode;

}
