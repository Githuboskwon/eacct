package com.iljin.apiServer.ijeas.slip.budget;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BudgetAmountsKey implements Serializable {

	private static final long serialVersionUID = 2903347160331141789L;

	Integer ledgerId;

	String periodName;

	BigDecimal summaryCodeCombinationId;
}
