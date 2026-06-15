package com.iljin.apiServer.ijeas.costBudget;

import com.iljin.apiServer.ijeas.system.acct.AccountDto;

import java.util.List;

public interface CostBudgetRepositoryCustom {

    List<CostBudgetDto> getBudgetAmountListCnt(CostBudgetDto costBudgetDto) throws Exception;

    CostBudgetResult getBudgetAmountList(CostBudgetDto costBudgetDto) throws Exception;

    List<CostBudgetDto> getPlanTotal(CostBudgetDto costBudgetDto) throws Exception;

    List<CostBudgetDto> getSumDetail(CostBudgetDto costBudgetDto) throws Exception;

    List<CostBudgetDto> getPerformanceCheck(CostBudgetDto costBudgetDto) throws Exception;

    AccountDetailResult getPerformanceCheckDetail(CostBudgetDto costBudgetDto) throws Exception;

    String doApprovalSave(CostBudgetDto costBudgetDto) throws Exception;

    String doApprovalClear(CostBudgetDto costBudgetDto) throws Exception;

    String doSave(CostBudgetSaveList costBudgetSaveList) throws Exception;

    String doDelete(CostBudgetSaveList costBudgetSaveList) throws Exception;

    DraftListResult getDraftList(CostBudgetDto costBudgetDto) throws Exception;

    CostBudgetDto doDraftSave(DraftResult draftResult) throws Exception;

    List<AccountDto> getAccountPopList(CostBudgetDto costBudgetDto);
}
