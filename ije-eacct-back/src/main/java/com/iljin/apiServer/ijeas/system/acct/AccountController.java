package com.iljin.apiServer.ijeas.system.acct;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/list")
    public ResponseEntity<List<AccountDto>> getAccountList(@RequestBody AccountDto accountDto){
        List<AccountDto> list = accountService.getAccountList(accountDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/slip/list")
    public ResponseEntity<List<AccountDto>> getSlipAccountList(@RequestBody AccountDto accountDto){
        List<AccountDto> list = accountService.getSlipAccountList(accountDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/slip/subList")
    public ResponseEntity<List<AccountDto>> getSlipAccountSubList(@RequestBody AccountDto accountDto){
        List<AccountDto> list = accountService.getSlipAccountSubList(accountDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/pop/list")
    public ResponseEntity<List<AccountDto>> getAccountPopList(@RequestBody AccountDto accountDto){
        List<AccountDto> list = accountService.getAccountPopList(accountDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
