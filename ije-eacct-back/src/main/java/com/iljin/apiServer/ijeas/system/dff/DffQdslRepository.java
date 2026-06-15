package com.iljin.apiServer.ijeas.system.dff;

import java.util.List;

public interface DffQdslRepository {
    List<DffDto> getDffFromAccount(DffDto dffDto);

    int getDffDetailCount(String beforeValue, String search);

    List<DffDto> getDffDetail(String beforeValue, Integer page, String search);
}
