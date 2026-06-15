package com.iljin.apiServer.ijeas.system.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;


    @PostMapping("/slip/list")
    public ResponseEntity<List<TaskDto>> getSlipTaskList(@RequestBody TaskDto taskDto){
        return new ResponseEntity<>(taskService.getSlipTaskList(taskDto), HttpStatus.OK);
    }
}
