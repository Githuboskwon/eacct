package com.iljin.apiServer.ijeas.system.scheduler;

import java.util.List;

public interface SchedulerQdslRepository {

    List<SchedulerDto> getSchedulerList(SchedulerDto schedulerDto);


}
