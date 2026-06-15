package com.iljin.apiServer.ijeas.slip.prepayment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ApplyPrepaysDto implements Serializable {

    private static final long serialVersionUID = 7243103578505930972L;

    String rowId;

    BigDecimal invoiceId;

    Integer prepayLineNumber;

    String acctCode;

    String acctDesc;

    BigDecimal prepayAmountRemainingErp;

    BigDecimal prepayAmountRemaining;

    BigDecimal lineAmount;

    LocalDateTime accountingDate;

    String periodName;

    Integer setOfBooksId;

    String description;

    BigDecimal poLineLocationId;

    BigDecimal poDistributionId;

    BigDecimal rcvTransactionId;

    Integer orgId;

    String prepayInvoiceNumber;

    LocalDateTime invoiceDate;

    BigDecimal invoiceAmount;

    String integrationVendorNum;

    BigDecimal vendorId;

    BigDecimal vendorSiteId;

    String invoiceCurrencyCode;

    String paymentCurrencyCode;

    BigDecimal paymentCrossRate;

    BigDecimal poHeaderId;

    BigDecimal poNumber;

    String vendorName;

    String vendorNumber;

    String vendorSiteCode;

    String receiptNumber;

    LocalDateTime earliestSettlementDate;

    String source;

    String taxRateCode;

    @QueryProjection
    public ApplyPrepaysDto(String rowId,BigDecimal invoiceId,Integer prepayLineNumber,String acctCode,
                           String acctDesc,BigDecimal prepayAmountRemainingErp,BigDecimal prepayAmountRemaining,
                           BigDecimal lineAmount,LocalDateTime accountingDate,String periodName,Integer setOfBooksId,
                           String description,BigDecimal poLineLocationId,BigDecimal poDistributionId,BigDecimal rcvTransactionId,
                           Integer orgId,String prepayInvoiceNumber,LocalDateTime invoiceDate,BigDecimal invoiceAmount,
                           String integrationVendorNum,BigDecimal vendorId,BigDecimal vendorSiteId,String invoiceCurrencyCode,
                           String paymentCurrencyCode,BigDecimal paymentCrossRate,BigDecimal poHeaderId,BigDecimal poNumber,
                           String vendorName,String vendorNumber,String vendorSiteCode,String receiptNumber,
                           LocalDateTime earliestSettlementDate,String source,String taxRateCode){
        this.rowId = rowId;
        this.invoiceId = invoiceId;
        this.prepayLineNumber = prepayLineNumber;
        this.acctCode = acctCode;
        this.acctDesc = acctDesc;
        this.prepayAmountRemainingErp = prepayAmountRemainingErp;
        this.prepayAmountRemaining = prepayAmountRemaining;
        this.lineAmount = lineAmount;
        this.accountingDate = accountingDate;
        this.periodName = periodName;
        this.setOfBooksId = setOfBooksId;
        this.description = description;
        this.poLineLocationId = poLineLocationId;
        this.poDistributionId = poDistributionId;
        this.rcvTransactionId = rcvTransactionId;
        this.orgId = orgId;
        this.prepayInvoiceNumber = prepayInvoiceNumber;
        this.invoiceDate = invoiceDate;
        this.invoiceAmount = invoiceAmount;
        this.integrationVendorNum = integrationVendorNum;
        this.vendorId = vendorId;
        this.vendorSiteId = vendorSiteId;
        this.invoiceCurrencyCode = invoiceCurrencyCode;
        this.paymentCurrencyCode = paymentCurrencyCode;
        this.paymentCrossRate = paymentCrossRate;
        this.poHeaderId = poHeaderId;
        this.poNumber = poNumber;
        this.vendorName = vendorName;
        this.vendorNumber = vendorNumber;
        this.vendorSiteCode = vendorSiteCode;
        this.receiptNumber = receiptNumber;
        this.earliestSettlementDate = earliestSettlementDate;
        this.source = source;
        this.taxRateCode = taxRateCode;
    };

}
