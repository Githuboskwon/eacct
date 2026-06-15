package com.iljin.apiServer.ijeas.es.item;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApPaymentsItemKey implements Serializable {

    private static final long serialVersionUID = -8145710152259229420L;

    String compCd;

    String slipNo;

    String slipType;

    BigDecimal checkId;

    Long aeLineId;

    Long aeLineNo;

    @Builder
    public ApPaymentsItemKey(String compCd, String slipNo, String slipType, BigDecimal checkId,
        Long aeLineId,
        Long aeLineNo) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.checkId = checkId;
        this.aeLineId = aeLineId;
        this.aeLineNo = aeLineNo;
    }
}
