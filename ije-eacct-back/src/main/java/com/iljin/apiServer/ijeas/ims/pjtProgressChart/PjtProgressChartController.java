package com.iljin.apiServer.ijeas.ims.pjtProgressChart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/pjt")
public class PjtProgressChartController {

    private final PjtProgressChartService pjtProgressChartService;

    /**
     * PJT 진행현황표 조회
     * @param pjtProgressChartDto
     */

    @PostMapping("/progress/list")
    public ResponseEntity<List<Map<String,Object>>> getProgressChartList(@RequestBody PjtProgressChartDto pjtProgressChartDto){
        return new ResponseEntity<>(pjtProgressChartService.getProgressChartList(pjtProgressChartDto), HttpStatus.OK);
    }

    /**
     * PJT 진행현황표 저장
     */
    @PostMapping("/progress/save")
    public ResponseEntity<String> saveProgressChartList(@RequestBody List<PjtProgressChartDto> pjtProgressChartDtos){
        return new ResponseEntity<>(pjtProgressChartService.saveProgressChartList(pjtProgressChartDtos), HttpStatus.OK);
    }

}
