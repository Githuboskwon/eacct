package com.iljin.apiServer.ijeas.system.termsPayment;


import java.util.List;

public interface TermsPaymentService {

    List<TermsPaymentDto> getTermsPaymentList(TermsPaymentDto termsPaymentDto);

    String saveTermsPaymentList(List<TermsPaymentDto> termsPaymentDtoList);

    String deleteTermsPaymentList(List<TermsPaymentDto> termsPaymentDtoList);
//
//    ResponseEntity<String> updateConfirmSeq(ConfirmDto confirm);

}
