package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SalesTaxInvoiceKey implements Serializable {
    private static final long serialVersionUID = -6650037649004251534L;

    BigDecimal customerTrxId;
}
