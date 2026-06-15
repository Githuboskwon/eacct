package com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan;


import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.PjtRegistInfoDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PjtProcessRatePlanService {

    ProcessResult getProcessList(PjtProcessRatePlanDto pjtProcessRatePlanDto);

    @Modifying
    @Transactional
    String saveProcessList(ProcessResult pjtProcessRatePlanDto);

}
