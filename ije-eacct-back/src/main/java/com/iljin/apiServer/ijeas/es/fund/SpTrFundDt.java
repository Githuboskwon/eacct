package com.iljin.apiServer.ijeas.es.fund;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.fund.ErpTrTransactionsDto;
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
@IdClass(SpTrFundDtKey.class)
@Table(name = "TB_TR_FUND_DT")
@Entity
public class SpTrFundDt extends BaseEntity {

    @Id
    @Column(name = "COMP_CD" , nullable = false)
    String compCd;

    @Column(name = "SLIP_NO" , nullable = false)
    String slipNo;

    @Column(name = "SLIP_TYPE" , nullable = false)
    String slipType;

    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Column(name = "TRX_UNIQUE_ID")
    BigDecimal trxUniqueId;

    @Id
    @Column(name = "TRX_UNIQUE_NUM")
    String trxUniqueNum;

    @Id
    @Column(name = "SLIP_SEQ" , nullable = false)
    BigDecimal slipSeq;

    @Column(name = "XTR_SLIP_TYPE")
    String xtrSlipType;

    @Column(name = "BATCH_ID" , nullable = false)
    Integer batchId;

    @Column(name = "DEAL_NUM")
    String dealNum;

    @Column(name = "TRANSACTION_NUM")
    String transactionNum;

    @Column(name = "ORIGIN_TRX_NUM")
    String originTrxNum;

    @Column(name = "DEAL_TYPE")
    String dealType;

    @Column(name = "DEAL_SUBTYPE")
    String dealSubtype;

    @Column(name = "PRODUCT_TYPE")
    String productType;

    @Column(name = "AMOUNT_TYPE")
    String amountType;

    @Column(name = "TRANSACTION_TYPE")
    String transactionType;

    @Column(name = "CURRENCY_CD_HEADER")
    String currencyCdHeader;

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Column(name = "DAILY_RATE")
    BigDecimal dailyRate;

    @Column(name = "TRANSACTION_RATE")
    BigDecimal transactionRate;

    @Column(name = "JOURNAL_DT")
    LocalDateTime journalDt;

    @Column(name = "SEGMENT1_2")
    String segment1_2;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "DEBIT_AMT")
    BigDecimal debitAmt;

    @Column(name = "CREDIT_AMT")
    BigDecimal creditAmt;

    @Column(name = "ACCOUNTED_DR")
    BigDecimal accountedDr;

    @Column(name = "ACCOUNTED_CR")
    BigDecimal accountedCr;

    @Column(name = "BANK_NM")
    String bankNm;

    @Column(name = "ACCOUNT_NO")
    String accountNo;

    @Column(name = "CURRENCY_CD_LINE")
    String currencyCdLine;

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
    public SpTrFundDt(String compCd,String slipNo,String slipType, BigDecimal slipHeaderId,
                      BigDecimal trxUniqueId,String trxUniqueNum, BigDecimal slipSeq,
                      String xtrSlipType,Integer batchId,String dealNum,String transactionNum,
                      String originTrxNum,String dealType,String dealSubtype,String productType,
                      String amountType,String transactionType,String currencyCdHeader,String currencyCd,
                      BigDecimal dailyRate,BigDecimal transactionRate,LocalDateTime journalDt,String segment1_2,
                      String description,BigDecimal debitAmt,BigDecimal creditAmt,BigDecimal accountedDr,
                      BigDecimal accountedCr,String bankNm,String accountNo,String currencyCdLine,
                      String attribute1,String attribute2,String attribute3,String attribute4,String attribute5){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.trxUniqueId = trxUniqueId;
        this.trxUniqueNum = trxUniqueNum;
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
        this.currencyCdHeader = currencyCdHeader;
        this.currencyCd = currencyCd;
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
        this.currencyCdLine = currencyCdLine;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

    public static SpTrFundDt from(ErpTrTransactionsDto erpTrTransactions, String slipNo, BigDecimal slipHeaderId , BigDecimal slipSeq){
        return SpTrFundDt.builder()
                .compCd(String.valueOf(erpTrTransactions.getOrgId()))
                .slipNo(slipNo)
                .slipType(SlipType.FUND.getCode())
                .slipHeaderId(slipHeaderId)
                .slipSeq(slipSeq)
                .trxUniqueId(erpTrTransactions.getTrxUniqueId())
                .trxUniqueNum(erpTrTransactions.getTrxUniqueNumber())
                .xtrSlipType(erpTrTransactions.getXtrSlipType())
                .batchId(erpTrTransactions.getBatchId())
                .dealNum(erpTrTransactions.getDealNumber())
                .transactionNum(erpTrTransactions.getTransactionNumber())
                .originTrxNum(erpTrTransactions.getOriginTrxNumber())
                .dealType(erpTrTransactions.getDealType())
                .dealSubtype(erpTrTransactions.getDealSubtype())
                .productType(erpTrTransactions.getProductType())
                .amountType(erpTrTransactions.getAmountType())
                .transactionType(erpTrTransactions.getTransactionType())
                .currencyCdHeader(erpTrTransactions.getCurrencyCodeHeader())
                .currencyCd(erpTrTransactions.getCurrencyCode())
                .dailyRate(erpTrTransactions.getDailyRate())
                .transactionRate(erpTrTransactions.getTransactionRate())
                .journalDt(erpTrTransactions.getJournalDate())
                .segment1_2(erpTrTransactions.getSegment1_2())
                .description(erpTrTransactions.getDescription())
                .debitAmt(erpTrTransactions.getDebitAmount())
                .creditAmt(erpTrTransactions.getCreditAmount())
                .accountedDr(erpTrTransactions.getAccountedDr())
                .accountedCr(erpTrTransactions.getAccountedCr())
                .bankNm(erpTrTransactions.getBankName())
                .accountNo(erpTrTransactions.getAccountNo())
                .currencyCdLine(erpTrTransactions.getCurrencyCodeLine())
                .attribute1(erpTrTransactions.getAttribute1())
                .attribute2(erpTrTransactions.getAttribute2())
                .attribute3(erpTrTransactions.getAttribute3())
                .attribute4(erpTrTransactions.getAttribute4())
                .attribute5(erpTrTransactions.getAttribute5())
                .build();
    }

}
