package com.iljin.apiServer.ijeas.es.erpViews.sale;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ErpArTrxHeadersKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039749L;

    String slipType;

    BigDecimal slipId;
}
