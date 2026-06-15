package com.iljin.apiServer.ijeas.system.termsPayment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TermsPaymentKey implements Serializable {

    private static final long serialVersionUID = 3973090930745991200L;

    String compCd;

    Integer termId;

    String deptCd;

}
