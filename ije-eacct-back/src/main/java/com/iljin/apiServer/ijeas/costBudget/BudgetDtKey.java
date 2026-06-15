package com.iljin.apiServer.ijeas.costBudget;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BudgetDtKey implements Serializable {

	private static final long serialVersionUID = 2903347160331141789L;

	String compCd;

	BigDecimal ledgerId;

	String slipNo;

	BigDecimal slipHeaderId;

	String acctNm;

	String pastOverAmt;

	String pastOverReason;

	String curOverAmt;

	String curOverReason;

	public BudgetDtKey(String compCd, BigDecimal ledgerId, String slipNo, BigDecimal slipHeaderId,
					   String acctNm, String pastOverAmt, String pastOverReason, String curOverAmt, String curOverReason) {
		this.compCd = compCd;
		this.ledgerId = ledgerId;
		this.slipNo = slipNo;
		this.slipHeaderId = slipHeaderId;
		this.acctNm = acctNm;
		this.pastOverAmt = pastOverAmt;
		this.pastOverReason = pastOverReason;
		this.curOverAmt = curOverAmt;
		this.curOverReason = curOverReason;
	}
}
