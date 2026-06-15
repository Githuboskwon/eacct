package com.iljin.apiServer.ijeas.slipCommon.tax;


import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpTaxEvidenceCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/tax")
public class TaxEvidenceController {

    private final TaxEvidenceService taxEvidenceService;

    @PostMapping("/evidence/code/{trxTypeCode}")
    public ResponseEntity<List<ErpTaxEvidenceCodeDto>> getErpTaxEvidenceCode(@RequestBody ErpSlipRequestDto erpSlipRequestDto,
                                                                             @PathVariable String trxTypeCode) {
        List<ErpTaxEvidenceCodeDto> list = taxEvidenceService.getErpTaxEvidenceCode(erpSlipRequestDto, trxTypeCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
