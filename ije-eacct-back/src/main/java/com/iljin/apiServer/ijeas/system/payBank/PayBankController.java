package com.iljin.apiServer.ijeas.system.payBank;

import com.iljin.apiServer.core.util.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/payBank")
public class PayBankController {

    private final PayBankService payBankService;

    @ExceptionHandler(PayBankException.class)
    public ResponseEntity<Error> PayBankNotFound(PayBankException e) {
        Error error = new Error(2001, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/list")
    public ResponseEntity<List<PayBankDto>> getPayBankList(@RequestBody PayBankDto payBankDto) {
        return new ResponseEntity<>(payBankService.getPayBankList(payBankDto), HttpStatus.OK);
    }

}
