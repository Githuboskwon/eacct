package com.iljin.apiServer.ijeas.system.delegate;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DelegateKey implements Serializable {

    private static final long serialVersionUID = 3973090930745991200L;

    String compCd;

    String giveUserId;

    String receiveUserId;

    BigDecimal delegateSeq;

    @Builder
    public DelegateKey(String compCd, String giveUserId, String receiveUserId, BigDecimal delegateSeq) {
        this.compCd = compCd;
        this.giveUserId = giveUserId;
        this.receiveUserId = receiveUserId;
        this.delegateSeq = delegateSeq;
    }

}
