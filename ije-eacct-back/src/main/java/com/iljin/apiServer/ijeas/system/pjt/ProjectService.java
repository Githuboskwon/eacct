package com.iljin.apiServer.ijeas.system.pjt;

import java.util.List;

import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.PjtRegistInfoDto;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectService {

    @Transactional(readOnly = true)
    List<ProjectDto> getProjectList(String value);

    List<ProjectDto> getSlipProjectList(ProjectDto projectDto);

    List<PjtRegistInfoDto> getRegistInfoProjectList(PjtRegistInfoDto pjtRegistInfoDto);

}
