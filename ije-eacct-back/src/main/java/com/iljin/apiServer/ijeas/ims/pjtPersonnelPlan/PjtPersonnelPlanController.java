package com.iljin.apiServer.ijeas.ims.pjtPersonnelPlan;

import com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan.PjtProcessRatePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/pjt")
public class PjtPersonnelPlanController {

    private final PjtPersonnelPlanService pjtPersonnelPlanService;

    /**
     * PJT 인원계획및실적 조회
     * @param pjtPersonnelPlanDto
     */
    @PostMapping("/personnel/list")
    public ResponseEntity<PersonnelResult> getProcessList(@RequestBody PjtPersonnelPlanDto pjtPersonnelPlanDto){
        return new ResponseEntity<>(pjtPersonnelPlanService.getPersonnelList(pjtPersonnelPlanDto), HttpStatus.OK);
    }

    /**
     * PJT 인원계획및실적 저장
     */
    @PostMapping("/personnel/save")
    public ResponseEntity<String> saveProcessList(@RequestBody PersonnelResult personnelResult) throws Exception {
        return new ResponseEntity<>(pjtPersonnelPlanService.savePersonnelList(personnelResult), HttpStatus.OK);
    }

}
