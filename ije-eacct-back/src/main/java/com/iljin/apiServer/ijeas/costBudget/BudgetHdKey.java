package com.iljin.apiServer.ijeas.costBudget;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BudgetHdKey implements Serializable {

	private static final long serialVersionUID = 2903347160331141789L;

	String compCd;

	BigDecimal ledgerId;

	String slipNo;

	BigDecimal slipHeaderId;

	String acctCd;

	String pjtCd;

	String itemGroupCd;



	public BudgetHdKey(String compCd, BigDecimal ledgerId, String slipNo, BigDecimal slipHeaderId,
					   String acctCd,String pjtCd,String itemGroupCd) {
		this.compCd = compCd;
		this.ledgerId = ledgerId;
		this.slipNo = slipNo;
		this.slipHeaderId = slipHeaderId;
		this.acctCd = acctCd;
		this.pjtCd = pjtCd;
		this.itemGroupCd = itemGroupCd;
	}
}
