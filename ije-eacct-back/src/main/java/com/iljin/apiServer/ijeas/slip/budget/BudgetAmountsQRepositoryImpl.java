package com.iljin.apiServer.ijeas.slip.budget;

import com.iljin.apiServer.core.util.Util;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.slip.budget.QBudgetAmounts.budgetAmounts;

@RequiredArgsConstructor
@Repository
public class BudgetAmountsQRepositoryImpl implements BudgetAmountsQRepository {

    private final Util util;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BudgetAmountsDto> getBudgetAmountList(String periodName, String budgetDeptCode) {
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();


        return queryFactory
                .select(new QBudgetAmountsDto(
                        budgetAmounts.ledgerId,
                        budgetAmounts.periodName,
                        budgetAmounts.amountType,
                        budgetAmounts.boundaryCode,
                        budgetAmounts.summaryCodeCombinationId,
                        budgetAmounts.budgetDeptCode,
                        budgetAmounts.budgetDeptDesc,
                        budgetAmounts.budgetAcctCode,
                        budgetAmounts.budgetAcctDesc,
                        budgetAmounts.budgetAmount,
                        budgetAmounts.extEncumbranceAmount,
                        budgetAmounts.encumbranceAmount,
                        budgetAmounts.actualAmount,
                        budgetAmounts.availableAmount
                ))
                .from(budgetAmounts)
                .where(budgetAmounts.ledgerId.eq(Integer.valueOf(ledgerId)),
                        budgetAmounts.periodName.eq(periodName),
                        budgetAmounts.budgetDeptCode.eq(budgetDeptCode)
                )
                .fetch();
    }

}
