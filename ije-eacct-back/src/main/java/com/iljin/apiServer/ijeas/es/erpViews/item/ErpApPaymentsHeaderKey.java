package com.iljin.apiServer.ijeas.es.erpViews.item;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErpApPaymentsHeaderKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039749L;

    Integer orgId;

    BigDecimal checkId;
}
