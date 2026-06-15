package com.iljin.apiServer.ijeas.bond;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BondMstKey implements Serializable {
    private static final long serialVersionUID = 4308241169310964175L;

    String compCd;

    String refNo;

    public BondMstKey(String compCd, String refNo) {
        this.compCd = compCd;
        this.refNo = refNo;
    }
}
