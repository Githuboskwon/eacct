package com.iljin.apiServer.ijeas.slipCommon.exchange;


import com.iljin.apiServer.ijeas.es.erpViews.ErpExchangeRtDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSpCurrencies;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;


    @PostMapping(value={"/rate/{excDt}" , "/rate/{excDt}/{curCd}" })
    public ResponseEntity<List<ErpExchangeRtDto>> getErpExchangeRate(@PathVariable(required = false) String curCd,
                                                                     @PathVariable(required = false) String excDt) {
        List<ErpExchangeRtDto> list = exchangeService.getErpExchangeRate(curCd, excDt);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/currency/list")
    public List<ErpSpCurrencies> getSpCurrentcieCode(){
        return exchangeService.getSpCurrentcieCode();
    }
}
