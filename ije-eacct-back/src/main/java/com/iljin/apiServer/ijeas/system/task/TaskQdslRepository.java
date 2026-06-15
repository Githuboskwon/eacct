package com.iljin.apiServer.ijeas.system.task;

import java.util.List;

public interface TaskQdslRepository {
    List<TaskDto> getSlipTaskList(TaskDto taskDto);
}
