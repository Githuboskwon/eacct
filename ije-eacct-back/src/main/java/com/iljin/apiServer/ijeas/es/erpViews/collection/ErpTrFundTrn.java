package com.iljin.apiServer.ijeas.es.erpViews.collection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpTrFundTrnKey.class)
@Table(name = "CBO_SP_TR_FUND_TRN_V")
@Entity
public class ErpTrFundTrn {

    @Column(name = "XTR_SLIP_TYPE")
    String xtrSlipType;

    @Id
    @Column(name = "ORG_ID" , nullable = false)
    Integer orgId;

    @Column(name = "TRANSACTION_NUMBER")
    BigDecimal transactionNumber;

    @Column(name = "CREATED_BY")
    String createdBy;

    @Column(name = "TRANSACTION_TYPE")
    String transactionType;

    @Column(name = "ORIGIN_TRX_NUMBER")
    String originTrxNumber;

    @Column(name = "DEAL_TYPE")
    String dealType;

    @Column(name = "GL_DATE")
    LocalDateTime glDate;

    @Id
    @Column(name = "SEGMENT2_3")
    String segment2_3;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "AMOUNT_DR")
    BigDecimal amountDr;

    @Column(name = "AMOUNT_CR")
    BigDecimal amountCr;

    @Id
    @Column(name = "FUNCTIONAL_AMOUNT_DR")
    BigDecimal functionalAmountDr;


    @Id
    @Column(name = "FUNCTIONAL_AMOUNT_CR")
    BigDecimal functionalAmountCr;

    @Column(name = "BANK_SHORT_CODE")
    String bankShortCode;

    @Id
    @Column(name = "ACCOUNT_NUMBER")
    String accountNumber;

    @Column(name = "CURRENCY_CODE")
    String currencyCode;

    @Id
    @Column(name = "REMIT_ID")
    BigDecimal remitId;

    @Column(name = "REF_TRANSACTION_NUMBER")
    BigDecimal refTransactionNumber;

    @Builder
    public ErpTrFundTrn(String xtrSlipType, Integer orgId, BigDecimal transactionNumber,
                        String createdBy, String transactionType, String originTrxNumber,
                        String dealType, LocalDateTime glDate, String segment2_3,
                        String remark, BigDecimal amountDr, BigDecimal amountCr,
                        BigDecimal functionalAmountDr, BigDecimal functionalAmountCr,
                        String bankShortCode, String accountNumber, String currencyCode,
                        BigDecimal remitId, BigDecimal refTransactionNumber){
        this.xtrSlipType = xtrSlipType;
        this.orgId = orgId;
        this.transactionNumber = transactionNumber;
        this.createdBy = createdBy;
        this.transactionType = transactionType;
        this.originTrxNumber = originTrxNumber;
        this.dealType = dealType;
        this.glDate = glDate;
        this.segment2_3 = segment2_3;
        this.remark = remark;
        this.amountDr = amountDr;
        this.amountCr = amountCr;
        this.functionalAmountDr = functionalAmountDr;
        this.functionalAmountCr = functionalAmountCr;
        this.bankShortCode = bankShortCode;
        this.accountNumber = accountNumber;
        this.currencyCode = currencyCode;
        this.remitId = remitId;
        this.refTransactionNumber = refTransactionNumber;
    }

}
