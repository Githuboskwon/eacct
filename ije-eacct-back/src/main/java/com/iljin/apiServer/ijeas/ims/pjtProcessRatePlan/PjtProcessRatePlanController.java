package com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/pjt")
public class PjtProcessRatePlanController {

    private final PjtProcessRatePlanService pjtProcessRatePlanService;

    /**
     * PJT 공정률계획및실적 조회
     * @param pjtProcessRatePlanDto
     */
    @PostMapping("/process/list")
    public ResponseEntity<ProcessResult> getProcessList(@RequestBody PjtProcessRatePlanDto pjtProcessRatePlanDto){
        return new ResponseEntity<>(pjtProcessRatePlanService.getProcessList(pjtProcessRatePlanDto), HttpStatus.OK);
    }

    /**
     * PJT 공정률계획및실적 저장
     */
    @PostMapping("/process/save")
    public ResponseEntity<String> saveProcessList(@RequestBody ProcessResult pjtRegistInfoDtos){
        return new ResponseEntity<>(pjtProcessRatePlanService.saveProcessList(pjtRegistInfoDtos), HttpStatus.OK);
    }

}
