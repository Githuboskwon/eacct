package com.iljin.apiServer.ijeas.es.bulk;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class ApPaymentsHdKey implements Serializable {

    private static final long serialVersionUID = -3054150952809551275L;

    String compCd;

    String slipNo;

    String slipType;

    String eslipTransferBatchId;

    @Builder
    public ApPaymentsHdKey(String compCd, String slipNo, String slipType, String eslipTransferBatchId) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.eslipTransferBatchId = eslipTransferBatchId;
    }
}
