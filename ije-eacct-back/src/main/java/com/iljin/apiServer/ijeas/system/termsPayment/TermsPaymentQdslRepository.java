package com.iljin.apiServer.ijeas.system.termsPayment;


import java.util.List;

public interface TermsPaymentQdslRepository {

    List<TermsPaymentDto> getTermsPaymentList(TermsPaymentDto termsPaymentDto);

}
