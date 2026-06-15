package com.iljin.apiServer.ijeas.ims.pjtRegistInfo;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PjtRegistInfoService {

    List<PjtRegistInfoDto> getRegistInfoList(PjtRegistInfoDto pjtRegistInfoDto);

    @Modifying
    @Transactional
    String saveRegistInfoList(List<PjtRegistInfoDto> pjtRegistInfoDtos);

}
