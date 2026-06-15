package com.iljin.apiServer.ijeas.slip.budget;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BudgetAmountsDto implements Serializable {
    private static final long serialVersionUID = 5729842227739508470L;

    Integer ledgerId;

    String periodName;

    String amountType;

    String boundaryCode;

    BigDecimal summaryCodeCombinationId;

    String budgetDeptCode;

    String budgetDeptDesc;

    String budgetAcctCode;

    String budgetAcctDesc;

    BigDecimal budgetAmount;

    BigDecimal extEncumbranceAmount;

    BigDecimal encumbranceAmount;

    BigDecimal actualAmount;

    BigDecimal availableAmount;

    @QueryProjection
    public BudgetAmountsDto(Integer ledgerId, String periodName, String amountType, String boundaryCode,
                            BigDecimal summaryCodeCombinationId, String budgetDeptCode, String budgetDeptDesc,
                            String budgetAcctCode, String budgetAcctDesc, BigDecimal budgetAmount, BigDecimal extEncumbranceAmount,
                            BigDecimal encumbranceAmount, BigDecimal actualAmount, BigDecimal availableAmount) {

        this.ledgerId = ledgerId;
        this.periodName = periodName;
        this.amountType = amountType;
        this.boundaryCode = boundaryCode;
        this.summaryCodeCombinationId = summaryCodeCombinationId;
        this.budgetDeptCode = budgetDeptCode;
        this.budgetDeptDesc = budgetDeptDesc;
        this.budgetAcctCode = budgetAcctCode;
        this.budgetAcctDesc = budgetAcctDesc;
        this.budgetAmount = budgetAmount;
        this.extEncumbranceAmount = extEncumbranceAmount;
        this.encumbranceAmount = encumbranceAmount;
        this.actualAmount = actualAmount;
        this.availableAmount = availableAmount;
    }

}
