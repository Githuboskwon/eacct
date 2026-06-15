package com.iljin.apiServer.ijeas.es.erpViews.fund;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ErpTrTransactionsDto implements Serializable {

    private static final long serialVersionUID = 5281003613811924754L;

    String xtrSlipType;

    Integer orgId;

    Integer batchId;

    String dealNumber;

    String transactionNumber;

    String createdBy;

    String dealType;

    String originTrxNumber;

    String dealSubtype;

    String productType;

    String amountType;

    String transactionType;

    String currencyCodeHeader;

    String currencyCode;

    BigDecimal dailyRate;

    BigDecimal transactionRate;

    LocalDateTime journalDate;

    String segment1_2;

    String description;

    BigDecimal debitAmount;

    BigDecimal creditAmount;

    BigDecimal accountedDr;

    BigDecimal accountedCr;

    String bankName;

    String accountNo;

    String currencyCodeLine;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    BigDecimal trxUniqueId;

    String trxUniqueNumber;



    @QueryProjection
    public ErpTrTransactionsDto(String xtrSlipType,Integer orgId,Integer batchId,String dealNumber,
                                String transactionNumber,String createdBy,String dealType,String originTrxNumber,
                                String dealSubtype,String productType,String amountType,String transactionType,
                                String currencyCodeHeader,String currencyCode,BigDecimal dailyRate,BigDecimal transactionRate,
                                LocalDateTime journalDate,String segment1_2,String description,BigDecimal debitAmount,
                                BigDecimal creditAmount,BigDecimal accountedDr,BigDecimal accountedCr,String bankName,
                                String accountNo,String currencyCodeLine,String attribute1,String attribute2,String attribute3,
                                String attribute4,String attribute5, BigDecimal trxUniqueId, String trxUniqueNumber){
        this.xtrSlipType = xtrSlipType;
        this.orgId = orgId;
        this.batchId = batchId;
        this.dealNumber = dealNumber;
        this.transactionNumber = transactionNumber;
        this.createdBy = createdBy;
        this.dealType = dealType;
        this.originTrxNumber = originTrxNumber;
        this.dealSubtype = dealSubtype;
        this.productType = productType;
        this.amountType = amountType;
        this.transactionType = transactionType;
        this.currencyCodeHeader = currencyCodeHeader;
        this.currencyCode = currencyCode;
        this.dailyRate = dailyRate;
        this.transactionRate = transactionRate;
        this.journalDate = journalDate;
        this.segment1_2 = segment1_2;
        this.description = description;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.accountedDr = accountedDr;
        this.accountedCr = accountedCr;
        this.bankName = bankName;
        this.accountNo = accountNo;
        this.currencyCodeLine = currencyCodeLine;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.trxUniqueId = trxUniqueId;
        this.trxUniqueNumber = trxUniqueNumber;
    }

}
