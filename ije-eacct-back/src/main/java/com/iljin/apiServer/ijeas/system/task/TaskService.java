package com.iljin.apiServer.ijeas.system.task;

import java.util.List;

public interface TaskService {

    List<TaskDto> getSlipTaskList(TaskDto taskDto);
}
