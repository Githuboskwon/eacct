package com.iljin.apiServer.ijeas.bond;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BondHeaderKey implements Serializable {

    private static final long serialVersionUID = -4117878736082884889L;

    String compCd;

    String slipNo;

    String refNo;

    String type;

    @Builder
    public BondHeaderKey(String compCd, String slipNo, String refNo, String type) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.refNo = refNo;
        this.type = type;
    }
}
