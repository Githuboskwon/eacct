package com.iljin.apiServer.ijeas.system.dff;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DffService {
    List<DffDto> getDffFromAccount(DffDto dffDto);

    @Transactional
    List<DffDto> getDffDetail(DffDto dffDto);
}
