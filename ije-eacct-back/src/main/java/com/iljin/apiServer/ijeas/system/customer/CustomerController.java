package com.iljin.apiServer.ijeas.system.customer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 고객 List 조회
     * 전체 조회 고객 코드, 고객명, 사업자번호로 조회
     * */
    @GetMapping(value={"/customer/list", "/customer/list/{value}"})
    public ResponseEntity<CustomerPageResponse> getCustomerList(@PathVariable(required = false) String value, @PageableDefault(size = 100)
        Pageable pageable) {
        return new ResponseEntity<>(customerService.getCustomerList(value, pageable), HttpStatus.OK);
    }

    /**
     * 국내은행 조회
     * */
    @GetMapping(value={"/localBank/list", "/localBank/list/{value}"})
    public ResponseEntity<List<LocalBankDto>> getLocalBankList(@PathVariable(required = false) String value) {
        return new ResponseEntity<>(customerService.getLocalBankList(value), HttpStatus.OK);
    }

}
