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
@IdClass(ErpApFuturesBatchKey.class)
@Table(name = "CBO_SP_AP_FUTURE_BATCH_V")
@Entity
public class ErpApFuturesBatch {

    @Id
    @Column(name = "ORG_ID")
    Integer orgId;

    @Id
    @Column(name = "ESLIP_TRANSFER_BATCH_ID", nullable = false)
    String eslipTransferBatchId;

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

    @Column(name = "SEGMENT3")
    String segment3;

    @Column(name = "SEGMENT3_DESC")
    String segment3Desc;

    @Column(name = "SEGMENT4")
    String segment4;

    @Column(name = "SEGMENT4_DESC")
    String segment4Desc;

    @Column(name = "FUTURE_PAY_DUE_DATE")
    LocalDateTime futurePayDueDate;

    @Column(name = "SUM_AMOUNT")
    BigDecimal sumAmount;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "PAYMENT_METHOD_CODE")
    String paymentMethodCode;

    @Column(name = "PAYMENT_METHOD")
    String paymentMethod;
}
