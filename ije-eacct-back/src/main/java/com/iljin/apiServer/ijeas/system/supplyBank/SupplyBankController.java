package com.iljin.apiServer.ijeas.system.supplyBank;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/supply")
public class SupplyBankController {
    private final SupplyBankService supplyBankService;

    /**
     * 공급 계좌 목록 조회
     * */
    @GetMapping("/bank/list/{integrationVendorNum}/{termId}")
    public ResponseEntity<List<SupplyBankDto>> getSupplyBankList(@PathVariable String integrationVendorNum,
                                                                 @PathVariable String termId) {
        return new ResponseEntity<>(supplyBankService.getSupplyBankList(integrationVendorNum,termId), HttpStatus.OK);
    }


}
