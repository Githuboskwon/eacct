package com.iljin.apiServer.ijeas.system.pjt;

import com.iljin.apiServer.core.util.Util;
import java.util.List;

import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.PjtRegistInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectQdslRepository projectQdslRepository;

    private final Util util;

    @Override
    public List<ProjectDto> getProjectList(String value) {
        String compCd = util.getLoginUser().getCompCd();
        return projectQdslRepository.getProjectList(compCd, value);
    }

    @Override
    public List<ProjectDto> getSlipProjectList(ProjectDto projectDto){

        return projectQdslRepository.getSlipProjectList(projectDto);
    }

    @Override
    public List<PjtRegistInfoDto> getRegistInfoProjectList(PjtRegistInfoDto pjtRegistInfoDto){

        return projectQdslRepository.getRegistInfoProjectList(pjtRegistInfoDto);
    }

}
