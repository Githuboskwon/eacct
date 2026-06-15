package com.iljin.apiServer.ijeas.ims.pjtPersonnelPlan;

import java.util.List;
import java.util.Map;

public interface PjtPersonnelPlanRepositoryCustom {

    List<Map<String,Object>> getPersonnelList(PjtPersonnelPlanDto pjtPersonnelPlanDto);

    String savePersonnelList(PersonnelResult personnelResult) throws Exception;

}
