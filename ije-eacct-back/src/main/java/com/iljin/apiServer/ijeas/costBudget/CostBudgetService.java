package com.iljin.apiServer.ijeas.costBudget;


import com.iljin.apiServer.ijeas.system.acct.AccountDto;
import com.iljin.apiServer.ijeas.system.item.ItemDto;
import com.iljin.apiServer.ijeas.system.pjt.ProjectDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CostBudgetService {

    List<CostBudgetDto> getBudgetAmountListCnt(CostBudgetDto costBudgetDto) throws Exception;

    CostBudgetResult getBudgetAmountList(CostBudgetDto costBudgetDto) throws Exception;

    List<CostBudgetDto> getPlanTotal(CostBudgetDto costBudgetDto) throws Exception;

    List<CostBudgetDto> getSumDetail(CostBudgetDto costBudgetDto) throws Exception;

    List<CostBudgetDto> getPerformanceCheck(CostBudgetDto costBudgetDto) throws Exception;

    AccountDetailResult getPerformanceCheckDetail(CostBudgetDto costBudgetDto) throws Exception;

    @Modifying
    @Transactional
    String doApprovalSave(CostBudgetDto costBudgetDto) throws Exception;

    @Modifying
    @Transactional
    String doApprovalClear(CostBudgetDto costBudgetDto) throws Exception;

    @Modifying
    @Transactional
    String doSave(CostBudgetSaveList costBudgetSaveList) throws Exception;

    @Modifying
    @Transactional
    String doDelete(CostBudgetSaveList costBudgetSaveList) throws Exception;

    DraftListResult getDraftList(CostBudgetDto costBudgetDto) throws Exception;

    @Modifying
    @Transactional
    CostBudgetDto doDraftSave(DraftResult draftResult) throws Exception;

    @Modifying
    @Transactional
    String doDraftDelete(CostBudgetDto costBudgetDto) throws Exception;

    List<ItemDto> getBudgetItemList(CostBudgetDto costBudgetDto);

    List<ProjectDto> getBudgetProjectList(CostBudgetDto costBudgetDto);

    List<AccountDto> getAccountPopList(CostBudgetDto costBudgetDto);

}
