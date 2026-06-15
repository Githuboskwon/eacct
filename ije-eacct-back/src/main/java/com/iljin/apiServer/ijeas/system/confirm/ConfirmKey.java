package com.iljin.apiServer.ijeas.system.confirm;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ConfirmKey implements Serializable {

    private static final long serialVersionUID = 3973090930745991200L;

    String compCd;

    String deptCd;

    String confirmUserId;

    String confirmSeq;

    @Builder
    public ConfirmKey(String compCd, String deptCd, String confirmUserId, String confirmSeq) {
        this.compCd = compCd;
        this.deptCd = deptCd;
        this.confirmUserId = confirmUserId;
        this.confirmSeq = confirmSeq;
    }

}
