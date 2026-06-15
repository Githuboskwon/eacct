package com.iljin.apiServer.ijeas.system.scheduler;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SchedulerService {

    @Transactional(readOnly = true)
    List<SchedulerDto> getSchedulerList(SchedulerDto schedulerDto);

    @Modifying
    @Transactional
    String saveSchedulerList(List<SchedulerDto> schedulerDtos);

    @Modifying
    @Transactional
    String deleteScheduler(SchedulerDto schedulerDto);

    @Modifying
    @Transactional
    String executeScheduler(SchedulerDto schedulerDto);
}
