package com.iljin.apiServer.ijeas.es.fund;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SpTrFundDtKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039740L;

    String compCd;

    String trxUniqueNum;

    BigDecimal slipSeq;

}