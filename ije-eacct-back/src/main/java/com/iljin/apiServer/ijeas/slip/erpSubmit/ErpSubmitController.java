package com.iljin.apiServer.ijeas.slip.erpSubmit;

import com.iljin.apiServer.ijeas.es.ErpSlipSubmitDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTrxTypeHdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/erp/submit")
public class ErpSubmitController {
    private final ErpSubmitService erpSubmitService;


    /**
     * 전자전표 ERP전송 목록 조회
     * @param erpSlipSubmitDto has searchConditions
     * */
    @PostMapping("/list")
    public ResponseEntity<List<ErpSlipSubmitDto>> getErpSlipSubmitList(@RequestBody ErpSlipSubmitDto erpSlipSubmitDto) {
        return new ResponseEntity<>(erpSubmitService.getErpSlipSubmitList(erpSlipSubmitDto), HttpStatus.OK);
    }

    /**
     * 전자전표 ERP전송 전표 유형 조회
     * @param search has search
     * */
    @PostMapping(value={"/dealType/list","/dealType/list/{search}"})
    public ResponseEntity<List<ErpGlTrxTypeHdDto>> getErpSlipSubmitDealTypeList(@PathVariable(required = false) String search) {
        return new ResponseEntity<>(erpSubmitService.getErpSlipSubmitDealTypeList(search), HttpStatus.OK);
    }

    /**
     * 전자전표 ERP전송 전표 일괄 전송
     * @param erpSlipSubmitDtos has submit
     * */
    @PostMapping("/transfer")
    public ResponseEntity<String> slipTransferErp(@RequestBody List<ErpSlipSubmitDto> erpSlipSubmitDtos){
        return erpSubmitService.slipTransferErp(erpSlipSubmitDtos);
    }

}
