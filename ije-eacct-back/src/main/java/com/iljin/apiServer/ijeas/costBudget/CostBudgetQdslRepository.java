package com.iljin.apiServer.ijeas.costBudget;

import com.iljin.apiServer.ijeas.system.item.ItemDto;
import com.iljin.apiServer.ijeas.system.pjt.ProjectDto;

import java.util.List;

public interface CostBudgetQdslRepository {

    List<ItemDto> getBudgetItemList(CostBudgetDto costBudgetDto);

    List<ProjectDto> getBudgetProjectList(CostBudgetDto costBudgetDto);

}
