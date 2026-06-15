package com.iljin.apiServer.ijeas.approval;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ApprovalDetailKey implements Serializable {

    private static final long serialVersionUID = -2185409943864987873L;

    String compCd;
    BigDecimal apprGroupId;
    String slipNo;
    String apprNo;

    public ApprovalDetailKey(String compCd, BigDecimal apprGroupId, String slipNo, String apprNo) {
        this.compCd = compCd;
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.apprNo = apprNo;
    }
}
