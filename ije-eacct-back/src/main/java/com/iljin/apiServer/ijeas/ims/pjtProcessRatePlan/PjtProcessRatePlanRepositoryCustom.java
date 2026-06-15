package com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan;

import java.util.List;
import java.util.Map;

public interface PjtProcessRatePlanRepositoryCustom {

    List<Map<String,Object>> getProcessList(PjtProcessRatePlanDto pjtProcessRatePlanDto);

    String saveProcessList(ProcessResult pjtProcessRatePlanDto);

}
