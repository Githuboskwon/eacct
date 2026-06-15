package com.iljin.apiServer.ijeas.costBudget;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CostBudgetKey implements Serializable {

	private static final long serialVersionUID = 2903347160331141789L;

	String compCd;

	BigDecimal ledgerId;

	String periodYear;

	String periodMonth;

	String cctrCd;

	String acctCd;

	String pjtCd;

	String itemGroupCd;

	String degree;

	public CostBudgetKey (String compCd, BigDecimal ledgerId, String periodYear, String periodMonth,
						  String cctrCd, String acctCd, String pjtCd, String itemGroupCd, String degree){
		this.compCd = compCd;
		this.ledgerId = ledgerId;
		this.periodYear = periodYear;
		this.periodMonth = periodMonth;
		this.cctrCd = cctrCd;
		this.acctCd = acctCd;
		this.pjtCd = pjtCd;
		this.itemGroupCd = itemGroupCd;
		this.degree = degree;
	}

}
