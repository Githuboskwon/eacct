package com.iljin.apiServer.ijeas.slip.budget;

import java.util.List;

public interface BudgetService {

    List<BudgetAmountsDto> getBudgetAmountList(String periodName, String budgetDeptCode);

}
