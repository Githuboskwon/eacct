package com.iljin.apiServer.ijeas.es.erpViews.frgn;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ErpSpOsSlipKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039749L;

    String slipType;

    BigDecimal slipId;
}
