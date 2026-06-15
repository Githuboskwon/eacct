package com.iljin.apiServer.ijeas.es.collection;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SpTrCollectionDtKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039740L;

    String compCd;

    BigDecimal slipSeq;

    String segment2_3;

    BigDecimal functionalAmtDr;

    BigDecimal functionalAmtCr;

    String accountNum;

    BigDecimal remitId;
}
