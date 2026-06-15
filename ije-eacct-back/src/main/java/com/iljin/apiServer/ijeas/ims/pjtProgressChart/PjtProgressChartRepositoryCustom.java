package com.iljin.apiServer.ijeas.ims.pjtProgressChart;

import java.util.List;
import java.util.Map;

public interface PjtProgressChartRepositoryCustom {

    List<Map<String,Object>> getProgressChartList(PjtProgressChartDto pjtProgressChartDto);

}
