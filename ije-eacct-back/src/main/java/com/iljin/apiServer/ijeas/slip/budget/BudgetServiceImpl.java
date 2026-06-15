package com.iljin.apiServer.ijeas.slip.budget;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetAmountsQRepository budgetAmountsQRepository;

    public List<BudgetAmountsDto> getBudgetAmountList(String periodName, String budgetDeptCode){
        return budgetAmountsQRepository.getBudgetAmountList(periodName,budgetDeptCode);
    }

}
