package com.iljin.apiServer.ijeas.es.erpViews.collection;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ErpTrFundTrnKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039740L;

    Integer orgId;

    String segment2_3;

    BigDecimal functionalAmountDr;

    BigDecimal functionalAmountCr;

    String accountNumber;

    BigDecimal remitId;
}
