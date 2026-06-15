package com.iljin.apiServer.ijeas.system.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerService schedulerService;

    @ExceptionHandler(SchedulerException.class)
    public ResponseEntity<Error> schedulerExceptionNotFound(SchedulerException e) {
        Error error = new Error("2001"+e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 스케줄러관리
     * 조회
     * @param schedulerDto has two search variables.
     *                     schedulerNm, useYn
     * */
    @PostMapping("/scheduler/list")
    public ResponseEntity<List<SchedulerDto>> getSchedulerList(@RequestBody SchedulerDto schedulerDto) {
        List<SchedulerDto> result = schedulerService.getSchedulerList(schedulerDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 스케줄러관리
     * 저장
     * @param schedulerDtos is scheduler list on grid
     * */
    @PostMapping("/scheduler/save")
    public ResponseEntity<String> saveSchedulerList(@RequestBody List<SchedulerDto> schedulerDtos) {
        return new ResponseEntity<>(schedulerService.saveSchedulerList(schedulerDtos), HttpStatus.OK);
    }

    /**
     * 스케줄러관리
     * 행 삭제
     * */
    @PostMapping("/scheduler/delete")
    public ResponseEntity<String> deleteScheduler(@RequestBody SchedulerDto schedulerDto) {
        return new ResponseEntity<>(schedulerService.deleteScheduler(schedulerDto), HttpStatus.OK);
    }

    /**
     * 스케줄러관리
     * 처리
     * @param schedulerDto
     * */
    @PostMapping("/scheduler/execute")
    public ResponseEntity<String> executeScheduler(@RequestBody SchedulerDto schedulerDto) {
        return new ResponseEntity<>(schedulerService.executeScheduler(schedulerDto), HttpStatus.OK);
    }


}
