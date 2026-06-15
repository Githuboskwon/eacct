package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ErpGlCoaSegmentKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039749L;

    String valueCode;

    BigDecimal coaId;

    BigDecimal segmentNum;
}
