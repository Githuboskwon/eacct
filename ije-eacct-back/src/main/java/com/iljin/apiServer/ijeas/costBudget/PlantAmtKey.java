package com.iljin.apiServer.ijeas.costBudget;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PlantAmtKey implements Serializable {

	private static final long serialVersionUID = 2903347160331141789L;

	String compCd;

	BigDecimal ledgerId;

	String periodYear;

	String periodMonth;

	String cctrCd;

	String acctCd;

	String pjtCd;
}
