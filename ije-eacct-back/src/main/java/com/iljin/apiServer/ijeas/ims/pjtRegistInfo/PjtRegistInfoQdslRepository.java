package com.iljin.apiServer.ijeas.ims.pjtRegistInfo;

import java.util.List;

public interface PjtRegistInfoQdslRepository {

    List<PjtRegistInfoDto> getRegistInfoList(PjtRegistInfoDto pjtRegistInfoDto);

}
