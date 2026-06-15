package com.iljin.apiServer.ijeas.approval;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ApprovalHeaderKey implements Serializable {

    private static final long serialVersionUID = 2439238269399221463L;

    String compCd;

    BigDecimal apprGroupId;

    String slipNo;

    public ApprovalHeaderKey(String compCd, BigDecimal apprGroupId, String slipNo) {
        this.compCd = compCd;
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
    }
}
