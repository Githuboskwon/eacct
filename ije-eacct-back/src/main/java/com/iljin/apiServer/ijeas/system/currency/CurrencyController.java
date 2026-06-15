package com.iljin.apiServer.ijeas.system.currency;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping("/list")
    public ResponseEntity<List<CurrencyDto>> getCurrencyList(@RequestBody CurrencyDto currencyDto) {
        return new ResponseEntity<>(currencyService.getCurrencyList(currencyDto), HttpStatus.OK);
    }

}
