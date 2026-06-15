package com.iljin.apiServer.ijeas.ims.pjtLaborCost;

import java.util.List;

public interface PjtLaborCostQdslRepository {

    List<PjtLaborCostDto> getLaborCostList(PjtLaborCostDto pjtLaborCostDto);
}
