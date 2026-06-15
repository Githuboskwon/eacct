package com.iljin.apiServer.ijeas.ims.pjtPersonnelPlan;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface PjtPersonnelPlanService {

    PersonnelResult getPersonnelList(PjtPersonnelPlanDto pjtPersonnelPlanDto);

    @Modifying
    @Transactional
    String savePersonnelList(PersonnelResult personnelResult) throws Exception;

}
