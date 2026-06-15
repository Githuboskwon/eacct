package com.iljin.apiServer.ijeas.ims.pjtProgressChart;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface PjtProgressChartService {

    List<Map<String,Object>> getProgressChartList(PjtProgressChartDto pjtProgressChartDto);


    @Modifying
    @Transactional
    String saveProgressChartList(List<PjtProgressChartDto> pjtProgressChartDtos);

}
