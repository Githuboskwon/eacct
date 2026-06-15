package com.iljin.apiServer.ijeas.es.erpViews.item;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErpApPaymentsLinesKey implements Serializable {

    private static final long serialVersionUID = -2314003469016582080L;

    BigDecimal checkId;

    Long aeHeaderId;

    Long aeLineNumber;
}
