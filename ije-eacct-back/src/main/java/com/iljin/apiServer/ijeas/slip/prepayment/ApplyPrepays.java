package com.iljin.apiServer.ijeas.slip.prepayment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "CBO_SP_APPLY_PREPAYS_V")
public class ApplyPrepays {

	@Id
	@Column(name = "ROW_ID")
	String rowId;

	@Column(name = "INVOICE_ID" , nullable=false)
	BigDecimal invoiceId;

	@Column(name = "PREPAY_LINE_NUMBER" , nullable=false)
	Integer prepayLineNumber;

	@Column(name = "ACCT_CODE")
	String acctCode;

	@Column(name = "ACCT_DESC")
	String acctDesc;

	@Column(name = "PREPAY_AMOUNT_REMAINING_ERP")
	BigDecimal prepayAmountRemainingErp;

	@Column(name = "PREPAY_AMOUNT_REMAINING")
	BigDecimal prepayAmountRemaining;

	@Column(name = "LINE_AMOUNT" , nullable=false)
	BigDecimal lineAmount;

	@Column(name = "ACCOUNTING_DATE" , nullable=false)
	LocalDateTime accountingDate;

	@Column(name = "PERIOD_NAME")
	String periodName;

	@Column(name = "SET_OF_BOOKS_ID" , nullable=false)
	Integer setOfBooksId;

	@Column(name = "DESCRIPTION")
	String description;

	@Column(name = "PO_LINE_LOCATION_ID")
	BigDecimal poLineLocationId;

	@Column(name = "PO_DISTRIBUTION_ID")
	BigDecimal poDistributionId;

	@Column(name = "RCV_TRANSACTION_ID")
	BigDecimal rcvTransactionId;

	@Column(name = "ORG_ID")
	Integer orgId;

	@Column(name = "PREPAY_INVOICE_NUMBER" , nullable=false)
	String prepayInvoiceNumber;

	@Column(name = "INVOICE_DATE")
	LocalDateTime invoiceDate;

	@Column(name = "INVOICE_AMOUNT")
	BigDecimal invoiceAmount;

	@Column(name = "INTEGRATION_VENDOR_NUM")
	String integrationVendorNum;

	@Column(name = "VENDOR_ID")
	BigDecimal vendorId;

	@Column(name = "VENDOR_SITE_ID")
	BigDecimal vendorSiteId;

	@Column(name = "INVOICE_CURRENCY_CODE" , nullable=false)
	String invoiceCurrencyCode;

	@Column(name = "PAYMENT_CURRENCY_CODE" , nullable=false)
	String paymentCurrencyCode;

	@Column(name = "PAYMENT_CROSS_RATE" , nullable=false)
	BigDecimal paymentCrossRate;

	@Column(name = "PO_HEADER_ID")
	BigDecimal poHeaderId;

	@Column(name = "PO_NUMBER")
	BigDecimal poNumber;

	@Column(name = "VENDOR_NAME")
	String vendorName;

	@Column(name = "VENDOR_NUMBER" , nullable=false)
	String vendorNumber;

	@Column(name = "VENDOR_SITE_CODE")
	String vendorSiteCode;

	@Column(name = "RECEIPT_NUMBER")
	String receiptNumber;

	@Column(name = "EARLIEST_SETTLEMENT_DATE")
	LocalDateTime earliestSettlementDate;

	@Column(name = "SOURCE")
	String source;

	@Column(name = "TAX_RATE_CODE")
	String taxRateCode;

	@Builder
	public ApplyPrepays(String rowId,BigDecimal invoiceId,Integer prepayLineNumber,String acctCode,
						String acctDesc,BigDecimal prepayAmountRemainingErp,BigDecimal prepayAmountRemaining,
						BigDecimal lineAmount,LocalDateTime accountingDate,String periodName,Integer setOfBooksId,
						String description,BigDecimal poLineLocationId,BigDecimal poDistributionId,BigDecimal rcvTransactionId,
						Integer orgId,String prepayInvoiceNumber,LocalDateTime invoiceDate,BigDecimal invoiceAmount,
						String integrationVendorNum,BigDecimal vendorId,BigDecimal vendorSiteId,String invoiceCurrencyCode,
						String paymentCurrencyCode,BigDecimal paymentCrossRate,BigDecimal poHeaderId,BigDecimal poNumber,
						String vendorName,String vendorNumber,String vendorSiteCode,String receiptNumber,
						LocalDateTime earliestSettlementDate,String source,String taxRateCode) {

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

	}
}
