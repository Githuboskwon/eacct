package com.iljin.apiServer.ijeas.es.erpViews.fund;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ErpTrTrxHeadersDto implements Serializable {

    private static final long serialVersionUID = 5281003613811924734L;

    String xtrSlipType;

    Integer orgId;

    Integer batchId;

    String dealNumber;

    String transactionNumber;

    String createdBy;

    String dealType;

    String productType;

    String originTrxNumber;

    String dealSubtype;

    BigDecimal dailyRate;

    LocalDateTime journalDate;

    BigDecimal transactionRate;

    String transactionType;

    String description;

    String currencyCodeHeader;

    BigDecimal debitAmount;

    BigDecimal creditAmount;

    BigDecimal accountedDr;

    BigDecimal accountedCr;

    String amountType;

    BigDecimal trxUniqueId;

    String trxUniqueNumber;

    BigDecimal buyAmount;

    BigDecimal sellAmount;

    BigDecimal profitAmount;

    BigDecimal slipAmount;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    @QueryProjection
    public ErpTrTrxHeadersDto(String xtrSlipType,Integer orgId,Integer batchId,String dealNumber,
                              String transactionNumber,String createdBy,String dealType,String productType,
                              String originTrxNumber,String dealSubtype,BigDecimal dailyRate,LocalDateTime journalDate,
                              BigDecimal transactionRate,String transactionType,String description,String currencyCodeHeader,
                              BigDecimal debitAmount,BigDecimal creditAmount,BigDecimal accountedDr,BigDecimal accountedCr,
                              String amountType,BigDecimal trxUniqueId,String trxUniqueNumber,BigDecimal buyAmount,
                              BigDecimal sellAmount,BigDecimal profitAmount,BigDecimal slipAmount,String attribute1,
                              String attribute2,String attribute3,String attribute4,String attribute5){
        this.xtrSlipType = xtrSlipType;
        this.orgId = orgId;
        this.batchId = batchId;
        this.dealNumber = dealNumber;
        this.transactionNumber = transactionNumber;
        this.createdBy = createdBy;
        this.dealType = dealType;
        this.productType = productType;
        this.originTrxNumber = originTrxNumber;
        this.dealSubtype = dealSubtype;
        this.dailyRate = dailyRate;
        this.journalDate = journalDate;
        this.transactionRate = transactionRate;
        this.transactionType = transactionType;
        this.description = description;
        this.currencyCodeHeader = currencyCodeHeader;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.accountedDr = accountedDr;
        this.accountedCr = accountedCr;
        this.amountType = amountType;
        this.trxUniqueId = trxUniqueId;
        this.trxUniqueNumber = trxUniqueNumber;
        this.buyAmount = buyAmount;
        this.sellAmount = sellAmount;
        this.profitAmount = profitAmount;
        this.slipAmount = slipAmount;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

}
