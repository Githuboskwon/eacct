package com.iljin.apiServer.ijeas.system.pjt;

import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.PjtRegistInfoDto;

import java.util.List;

public interface ProjectQdslRepository {

    List<ProjectDto> getProjectList(String compCd, String value);

    List<ProjectDto> getSlipProjectList(ProjectDto projectDto);

    List<PjtRegistInfoDto> getRegistInfoProjectList(PjtRegistInfoDto pjtRegistInfoDto);

}
