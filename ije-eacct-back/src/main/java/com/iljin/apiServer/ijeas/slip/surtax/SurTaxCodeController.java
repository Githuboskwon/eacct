package com.iljin.apiServer.ijeas.slip.surtax;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/surTaxCode")
@RequiredArgsConstructor
public class SurTaxCodeController {
    private final SurTaxCodeQRepository surTaxCodeQRepository;

    @PostMapping("/list/{taxEvidenceType}/{returnType}")
    public ResponseEntity<List<SurTaxCodeDto>> getSurTaxCodeList(@RequestBody ErpSlipRequestDto erpSlipRequestDto,
                                                                 @PathVariable String taxEvidenceType,
                                                                 @PathVariable String returnType){
        List<SurTaxCodeDto> list = surTaxCodeQRepository.getSurTaxCodeList(erpSlipRequestDto,taxEvidenceType,returnType);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
