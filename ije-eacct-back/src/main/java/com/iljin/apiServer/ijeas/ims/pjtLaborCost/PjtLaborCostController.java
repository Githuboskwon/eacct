package com.iljin.apiServer.ijeas.ims.pjtLaborCost;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/pjt")
@RequiredArgsConstructor
public class PjtLaborCostController {

    private final PjtLaborCostService pjtLaborCostService;

    /**
     * PJT 인건비 조회
     * @param pjtLaborCostDto
     */
    @PostMapping("/laborCost/list")
    public ResponseEntity<List<PjtLaborCostDto>> getLaborCostList(@RequestBody PjtLaborCostDto pjtLaborCostDto){
        List<PjtLaborCostDto> list = pjtLaborCostService.getLaborCostList(pjtLaborCostDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * PJT 인건비 저장
     */
    @PostMapping("/laborCost/save")
    public ResponseEntity<String> saveLaborCostList(@RequestBody List<PjtLaborCostDto> pjtLaborCostDtos){
        return new ResponseEntity<>(pjtLaborCostService.saveLaborCostList(pjtLaborCostDtos), HttpStatus.OK);
    }

    /**
     * PJT 인건비 삭제
     * 행 삭제
     * */
    @PostMapping("/laborCost/delete")
    public ResponseEntity<String> deleteLaborCostList(@RequestBody PjtLaborCostDto pjtLaborCostDto) {
        return new ResponseEntity<>(pjtLaborCostService.deleteLaborCostList(pjtLaborCostDto), HttpStatus.OK);
    }
}
