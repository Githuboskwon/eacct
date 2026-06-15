package com.iljin.apiServer.ijeas.slip.budget;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(BudgetAmountsKey.class)
@Table(name = "CBO_SP_BUDGET_AMOUNT_V")
@Entity
public class BudgetAmounts {

    @Id
    @Column(name = "LEDGER_ID", nullable = false)
    Integer ledgerId;

    @Id
    @Column(name = "PERIOD_NAME", nullable = false)
    String periodName;

    @Column(name = "AMOUNT_TYPE")
    String amountType;

    @Column(name = "BOUNDARY_CODE")
    String boundaryCode;

    @Id
    @Column(name = "SUMMARY_CODE_COMBINATION_ID", nullable = false)
    BigDecimal summaryCodeCombinationId;

    @Column(name = "BUDGET_DEPT_CODE")
    String budgetDeptCode;

    @Column(name = "BUDGET_DEPT_DESC")
    String budgetDeptDesc;

    @Column(name = "BUDGET_ACCT_CODE")
    String budgetAcctCode;

    @Column(name = "BUDGET_ACCT_DESC")
    String budgetAcctDesc;

    @Column(name = "BUDGET_AMOUNT")
    BigDecimal budgetAmount;

    @Column(name = "EXT_ENCUMBRANCE_AMOUNT")
    BigDecimal extEncumbranceAmount;

    @Column(name = "ENCUMBRANCE_AMOUNT")
    BigDecimal encumbranceAmount;

    @Column(name = "ACTUAL_AMOUNT")
    BigDecimal actualAmount;

    @Column(name = "AVAILABLE_AMOUNT")
    BigDecimal availableAmount;

}
