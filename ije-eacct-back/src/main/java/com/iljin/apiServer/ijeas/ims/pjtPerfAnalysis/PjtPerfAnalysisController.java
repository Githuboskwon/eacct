package com.iljin.apiServer.ijeas.ims.pjtPerfAnalysis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/pjt")
public class PjtPerfAnalysisController {

    private final PjtPerfAnalysisService pjtPerfAnalysisService;

    /**
     * PJT 실적분석 조회
     * @param pjtPerfAnalysisDto
     */
    @PostMapping("/perfAnalysis/list")
    public ResponseEntity<List<PjtPerfAnalysisDto>> getPerfAnalysisList(@RequestBody PjtPerfAnalysisDto pjtPerfAnalysisDto){
        return new ResponseEntity<>(pjtPerfAnalysisService.getPerfAnalysisList(pjtPerfAnalysisDto), HttpStatus.OK);
    }

    /**
     * PJT 매출 상세 조회 팝업
     * @param pjtPerfAnalysisDto
     */
    @PostMapping("/perfAnalysis/profitSales/pop/list")
    public ResponseEntity<List<PjtPerfAnalysisDto>> getProfitSalesInfo(@RequestBody PjtPerfAnalysisDto pjtPerfAnalysisDto){
        return new ResponseEntity<>(pjtPerfAnalysisService.getProfitSalesInfo(pjtPerfAnalysisDto), HttpStatus.OK);
    }

    /**
     * PJT 청구 상세 조회 팝업
     * @param pjtPerfAnalysisDto
     */
    @PostMapping("/perfAnalysis/claim/pop/list")
    public ResponseEntity<List<PjtPerfAnalysisDto>> getClaimInfo(@RequestBody PjtPerfAnalysisDto pjtPerfAnalysisDto){
        return new ResponseEntity<>(pjtPerfAnalysisService.getClaimInfo(pjtPerfAnalysisDto), HttpStatus.OK);
    }


    /**
     * PJT 수금 상세 조회 팝업
     * @param pjtPerfAnalysisDto
     */
    @PostMapping("/perfAnalysis/collection/pop/list")
    public ResponseEntity<List<PjtPerfAnalysisDto>> getCollectionInfo(@RequestBody PjtPerfAnalysisDto pjtPerfAnalysisDto){
        return new ResponseEntity<>(pjtPerfAnalysisService.getCollectionInfo(pjtPerfAnalysisDto), HttpStatus.OK);
    }


    /**
     * PJT 총비용 조회 팝업
     * @param pjtPerfAnalysisDto
     */
    @PostMapping("/perfAnalysis/totalAmt/pop/list")
    public ResponseEntity<List<PjtPerfAnalysisDto>> getTotalAmt(@RequestBody PjtPerfAnalysisDto pjtPerfAnalysisDto){
        return new ResponseEntity<>(pjtPerfAnalysisService.getTotalAmt(pjtPerfAnalysisDto), HttpStatus.OK);
    }

}
