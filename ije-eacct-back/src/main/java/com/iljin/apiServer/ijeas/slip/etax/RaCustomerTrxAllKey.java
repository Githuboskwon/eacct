package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RaCustomerTrxAllKey implements Serializable {
    private static final long serialVersionUID = 6296718159500764027L;

    BigDecimal customerTrxId;
}
