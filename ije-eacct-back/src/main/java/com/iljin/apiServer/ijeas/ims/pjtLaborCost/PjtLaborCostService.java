package com.iljin.apiServer.ijeas.ims.pjtLaborCost;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PjtLaborCostService {

    List<PjtLaborCostDto> getLaborCostList(PjtLaborCostDto pjtLaborCostDto);

    @Modifying
    @Transactional
    String saveLaborCostList(List<PjtLaborCostDto> pjtLaborCostDtos);


    @Modifying
    @Transactional
    String deleteLaborCostList(PjtLaborCostDto pjtLaborCostDto);
}
