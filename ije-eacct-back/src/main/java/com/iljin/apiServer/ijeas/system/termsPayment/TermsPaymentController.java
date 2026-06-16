package com.iljin.apiServer.ijeas.system.termsPayment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/termsPayment")
public class TermsPaymentController {

    private final TermsPaymentService termsPaymentService;

    @PostMapping("/list")
    public ResponseEntity<List<TermsPaymentDto>> getTermsPaymentList(@RequestBody TermsPaymentDto termsPaymentDto) {
        List<TermsPaymentDto> list = termsPaymentService.getTermsPaymentList(termsPaymentDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<String> saveTermsPaymentList(@RequestBody List<TermsPaymentDto> termsPaymentDtoList) {
        return new ResponseEntity<>(termsPaymentService.saveTermsPaymentList(termsPaymentDtoList), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/delete")
    public ResponseEntity<String> deleteTermsPaymentList(@RequestBody List<TermsPaymentDto> termsPaymentDtoList) {
        return new ResponseEntity<>(termsPaymentService.deleteTermsPaymentList(termsPaymentDtoList), HttpStatus.OK);
    }
//
//    @Transactional
//    @PostMapping("/confirm/update")
//    public ResponseEntity<String> updateConfirmSeq(@RequestBody ConfirmDto confirmDto) {
//        return confirmService.updateConfirmSeq(confirmDto);
//    }

}
