package com.iljin.apiServer.ijeas.slip.prepayment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "CBO_SP_PREPAYMENT_APPLY")
@IdClass(PrepaymentApplyKey.class)
public class PrepaymentApply {

	@Id
	@Column(name = "PREPAYMENT_APPLY_ID" , nullable=false)
	BigDecimal prepaymentApplyId;

	@Id
	@Column(name = "SLIP_HEADER_ID" , nullable=false)
	BigDecimal slipHeaderId;

	@Column(name = "CHILD_SLIP_HEADER_ID")
	BigDecimal childSlipHeaderId;

	@Id
	@Column(name = "ORG_ID" , nullable=false)
	Integer orgId;

	@Column(name = "LEDGER_ID" , nullable=false)
	BigDecimal ledgerId;

	@Column(name = "PREPAY_INVOICE_ID" , nullable=false)
	BigDecimal prepayInvoiceId;

	@Column(name = "PREPAY_LINE_NUMBER" , nullable=false)
	Integer prepayLineNumber;

	@Column(name = "PREPAY_AMOUNT_REMAINING" , nullable=false)
	BigDecimal prepayAmountRemaining;

	@Column(name = "PREPAY_AMOUNT" , nullable=false)
	BigDecimal prepayAmount;

	@Column(name = "AMOUNT_TO_APPLY" , nullable=false)
	BigDecimal amountToApply;

	@Column(name = "APPLY_GL_DATE" , nullable=false)
	LocalDateTime applyGlDate;

	@Column(name = "VENDOR_ID" , nullable=false)
	BigDecimal vendorId;

	@Column(name = "VENDOR_SITE_ID" , nullable=false)
	BigDecimal vendorSiteId;

	@Column(name = "INVOICE_CURRENCY_CODE" , nullable=false)
	String invoiceCurrencyCode;

	@Column(name = "PREPAY_CANCELLED_FLAG" , nullable=false)
	String prepayCancelledFlag;

	@Column(name = "PREPAY_CANCELLED_AMOUNT")
	BigDecimal prepayCancelledAmount;

	@Column(name = "PREPAY_CANCELLED_DATE")
	LocalDateTime prepayCancelledDate;

	@Column(name = "PREPAY_CANCELLED_PERSON_ID")
	BigDecimal prepayCancelledPersonId;

	@Column(name = "APPLY_PROCESS_FLAG" , nullable=false)
	String applyProcessFlag;

	@Column(name = "APPLY_PROCESS_DATE")
	LocalDateTime applyProcessDate;

	@Column(name = "APPLY_PROCESS_ERROR")
	String applyProcessError;

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

	@Column(name = "ATTRIBUTE6")
	String attribute6;

	@Column(name = "ATTRIBUTE7")
	String attribute7;

	@Column(name = "ATTRIBUTE8")
	String attribute8;

	@Column(name = "ATTRIBUTE9")
	String attribute9;

	@Column(name = "ATTRIBUTE10")
	String attribute10;

	@Column(name = "CREATED_PERSON_ID" , nullable=false)
	BigDecimal createdPersonId;

	@Column(name = "CREATION_DATE" , nullable=false)
	LocalDateTime creationDate;

	@Column(name = "LAST_UPDATED_PERSON_ID" , nullable=false)
	BigDecimal lastUpdatedPersonId;

	@Column(name = "LAST_UPDATE_DATE" , nullable=false)
	LocalDateTime lastUpdateDate;

	@Column(name = "LAST_UPDATE_LOGIN")
	BigDecimal lastUpdateLogin;

	@Builder
	public PrepaymentApply(BigDecimal prepaymentApplyId,BigDecimal slipHeaderId,Integer orgId,BigDecimal ledgerId,
						   BigDecimal prepayInvoiceId,Integer prepayLineNumber,BigDecimal prepayAmountRemaining,
						   BigDecimal prepayAmount,BigDecimal amountToApply,LocalDateTime applyGlDate,BigDecimal vendorId,
						   BigDecimal vendorSiteId,String invoiceCurrencyCode,String prepayCancelledFlag,String applyProcessFlag,
						   BigDecimal createdPersonId,LocalDateTime creationDate,BigDecimal lastUpdatedPersonId,LocalDateTime lastUpdateDate) {
		this.prepaymentApplyId = prepaymentApplyId;
		this.slipHeaderId = slipHeaderId;
		this.orgId = orgId;
		this.ledgerId = ledgerId;
		this.prepayInvoiceId = prepayInvoiceId;
		this.prepayLineNumber = prepayLineNumber;
		this.prepayAmountRemaining = prepayAmountRemaining;
		this.prepayAmount = prepayAmount;
		this.amountToApply = amountToApply;
		this.applyGlDate = applyGlDate;
		this.vendorId = vendorId;
		this.vendorSiteId = vendorSiteId;
		this.invoiceCurrencyCode = invoiceCurrencyCode;
		this.prepayCancelledFlag = prepayCancelledFlag;
		this.applyProcessFlag = applyProcessFlag;
		this.createdPersonId = createdPersonId;
		this.creationDate = creationDate;
		this.lastUpdatedPersonId = lastUpdatedPersonId;
		this.lastUpdateDate = lastUpdateDate;
	}

	public void updateFlag(BigDecimal amountToApply,
						   Integer personId){
		this.prepayCancelledFlag = "Y";
		this.prepayCancelledAmount = amountToApply;
		this.prepayCancelledDate = LocalDateTime.now();
		this.prepayCancelledPersonId = BigDecimal.valueOf(personId);
		this.lastUpdatedPersonId = BigDecimal.valueOf(personId);
		this.lastUpdateDate = LocalDateTime.now();
	}



}
