package com.iljin.apiServer.ijeas.es.item;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ApPaymentsDtKey implements Serializable {

    private static final long serialVersionUID = -3054150952809551275L;

    String compCd;

    String slipNo;

    String slipType;

    BigDecimal checkId;

    @Builder
    public ApPaymentsDtKey(String compCd, String slipNo, String slipType, BigDecimal checkId) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.checkId = checkId;
    }
}
