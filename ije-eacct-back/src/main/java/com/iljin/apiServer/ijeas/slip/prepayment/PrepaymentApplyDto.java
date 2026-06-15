package com.iljin.apiServer.ijeas.slip.prepayment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PrepaymentApplyDto implements Serializable {

    private static final long serialVersionUID = 7243103578505430972L;

    BigDecimal prepaymentApplyId;

    BigDecimal slipHeaderId;

    BigDecimal childSlipHeaderId;

    Integer orgId;

    BigDecimal ledgerId;

    BigDecimal prepayInvoiceId;

    Integer prepayLineNumber;

    BigDecimal prepayAmountRemaining;

    BigDecimal prepayAmount;

    BigDecimal amountToApply;

    LocalDateTime applyGlDate;

    BigDecimal vendorId;

    BigDecimal vendorSiteId;

    String invoiceCurrencyCode;

    String prepayCancelledFlag;

    BigDecimal prepayCancelledAmount;

    LocalDateTime prepayCancelledDate;

    BigDecimal prepayCancelledPersonId;

    String applyProcessFlag;

    LocalDateTime applyProcessDate;

    String applyProcessError;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    String attribute6;

    String attribute7;

    String attribute8;

    String attribute9;

    String attribute10;

    BigDecimal createdPersonId;

    LocalDateTime creationDate;

    BigDecimal lastUpdatedPersonId;

    LocalDateTime lastUpdateDate;

    BigDecimal lastUpdateLogin;

    String acctCode;

    String acctDesc;

    BigDecimal prepayAmountRemainingErp;

    BigDecimal lineAmount;

    LocalDateTime accountingDate;

    String periodName;

    Integer setOfBooksId;

    String description;

    BigDecimal poLineLocationId;

    BigDecimal poDistributionId;

    BigDecimal rcvTransactionId;

    String prepayInvoiceNumber;

    LocalDateTime invoiceDate;

    BigDecimal invoiceAmount;

    String integrationVendorNum;

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

    BigDecimal afterAmt;


    @QueryProjection
    public PrepaymentApplyDto(BigDecimal slipHeaderId,BigDecimal prepaymentApplyId,Integer orgId,BigDecimal ledgerId,BigDecimal prepayInvoiceId,
                              Integer prepayLineNumber,BigDecimal prepayAmountRemaining,BigDecimal prepayAmount,BigDecimal afterAmt,
                              BigDecimal amountToApply,LocalDateTime applyGlDate,BigDecimal vendorId,BigDecimal vendorSiteId,String invoiceCurrencyCode,
                              String prepayCancelledFlag,BigDecimal prepayCancelledAmount,LocalDateTime prepayCancelledDate,BigDecimal prepayCancelledPersonId,
                              String applyProcessFlag,LocalDateTime applyProcessDate,String applyProcessError,String attribute1,String attribute2,
                              String attribute3,String attribute4,String attribute5,String attribute6,String attribute7,String attribute8,
                              String attribute9,String attribute10,BigDecimal createdPersonId,LocalDateTime creationDate,BigDecimal lastUpdatedPersonId,
                              LocalDateTime lastUpdateDate,BigDecimal lastUpdateLogin, String prepayInvoiceNumber, String description){
        this.slipHeaderId = slipHeaderId;
        this.prepaymentApplyId = prepaymentApplyId;
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.prepayInvoiceId = prepayInvoiceId;
        this.prepayLineNumber = prepayLineNumber;
        this.prepayAmountRemaining = prepayAmountRemaining;
        this.prepayAmount = prepayAmount;
        this.afterAmt = afterAmt;
        this.amountToApply = amountToApply;
        this.applyGlDate = applyGlDate;
        this.vendorId = vendorId;
        this.vendorSiteId = vendorSiteId;
        this.invoiceCurrencyCode = invoiceCurrencyCode;
        this.prepayCancelledFlag = prepayCancelledFlag;
        this.prepayCancelledAmount = prepayCancelledAmount;
        this.prepayCancelledDate = prepayCancelledDate;
        this.prepayCancelledPersonId = prepayCancelledPersonId;
        this.applyProcessFlag = applyProcessFlag;
        this.applyProcessDate = applyProcessDate;
        this.applyProcessError = applyProcessError;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.attribute6 = attribute6;
        this.attribute7 = attribute7;
        this.attribute8 = attribute8;
        this.attribute9 = attribute9;
        this.attribute10 = attribute10;
        this.createdPersonId = createdPersonId;
        this.creationDate = creationDate;
        this.lastUpdatedPersonId = lastUpdatedPersonId;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdateLogin = lastUpdateLogin;
        this.prepayInvoiceNumber = prepayInvoiceNumber;
        this.description = description;
    }

}
