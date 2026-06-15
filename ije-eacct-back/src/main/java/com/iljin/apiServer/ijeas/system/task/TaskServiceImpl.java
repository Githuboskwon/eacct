package com.iljin.apiServer.ijeas.system.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskQdslRepository taskQdslRepository;

    @Override
    public List<TaskDto> getSlipTaskList(TaskDto taskDto){

        return taskQdslRepository.getSlipTaskList(taskDto);
    }
}
