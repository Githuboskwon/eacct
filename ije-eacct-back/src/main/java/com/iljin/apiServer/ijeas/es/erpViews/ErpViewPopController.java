package com.iljin.apiServer.ijeas.es.erpViews;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/erp")
public class ErpViewPopController {

    private final ErpViewPopService erpViewPopService;

    public ErpViewPopController(ErpViewPopService erpViewPopService) {
        this.erpViewPopService = erpViewPopService;
    }


    /**
     * ERP 자금 전표 거래 유형
     * */
    @PostMapping(value={"/slip/fund/pop/dealType","/slip/fund/pop/dealType/{searchDate}"})
    public ResponseEntity<List<ErpViewPopFundSlipDto>> getPopDealTypeList(@PathVariable(required = false) String searchDate) {
        List<ErpViewPopFundSlipDto> list = erpViewPopService.getPopDealType(searchDate);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * ERP 자금 전표 상품 유형
     * */
    @PostMapping(value={"/slip/fund/pop/productType","/slip/fund/pop/productType/{searchDate}"})
    public ResponseEntity<List<ErpViewPopFundSlipDto>> getPopProductTypeList(@PathVariable(required = false) String searchDate) {
        List<ErpViewPopFundSlipDto> list = erpViewPopService.getPopProductType(searchDate);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
