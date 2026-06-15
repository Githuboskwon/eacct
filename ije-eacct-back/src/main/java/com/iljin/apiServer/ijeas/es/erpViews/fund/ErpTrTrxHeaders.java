package com.iljin.apiServer.ijeas.es.erpViews.fund;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpTrTrxHeadersKey.class)
@Table(name = "CBO_SP_TR_TRX_HEADERS_V")
@Entity
public class ErpTrTrxHeaders {

    @Id
    @Column(name = "XTR_SLIP_TYPE")
    String xtrSlipType;

    @Id
    @Column(name = "ORG_ID" , nullable = false)
    Integer orgId;

    @Id
    @Column(name = "BATCH_ID")
    Integer batchId;

    @Id
    @Column(name = "DEAL_NUMBER")
    String dealNumber;

    @Id
    @Column(name = "TRANSACTION_NUMBER")
    String transactionNumber;

    @Column(name = "CREATED_BY")
    String createdBy;

    @Column(name = "DEAL_TYPE")
    String dealType;

    @Column(name = "PRODUCT_TYPE")
    String productType;

    @Column(name = "ORIGIN_TRX_NUMBER")
    String originTrxNumber;

    @Column(name = "DEAL_SUBTYPE")
    String dealSubtype;

    @Column(name = "DAILY_RATE")
    BigDecimal dailyRate;

    @Column(name = "JOURNAL_DATE")
    LocalDateTime journalDate;

    @Column(name = "TRANSACTION_RATE")
    BigDecimal transactionRate;

    @Column(name = "TRANSACTION_TYPE")
    String transactionType;

    @Id
    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "CURRENCY_CODE_HEADER")
    String currencyCodeHeader;

    @Column(name = "DEBIT_AMOUNT")
    BigDecimal debitAmount;

    @Column(name = "CREDIT_AMOUNT")
    BigDecimal creditAmount;

    @Column(name = "ACCOUNTED_DR")
    BigDecimal accountedDr;

    @Column(name = "ACCOUNTED_CR")
    BigDecimal accountedCr;

    @Column(name = "AMOUNT_TYPE")
    String amountType;

    @Column(name = "TRX_UNIQUE_ID")
    BigDecimal trxUniqueId;

    @Column(name = "TRX_UNIQUE_NUMBER")
    String trxUniqueNumber;

    @Column(name = "BUY_AMOUNT")
    BigDecimal buyAmount;

    @Column(name = "SELL_AMOUNT")
    BigDecimal sellAmount;

    @Column(name = "PROFIT_AMOUNT")
    BigDecimal profitAmount;

    @Column(name = "SLIP_AMOUNT")
    BigDecimal slipAmount;

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
