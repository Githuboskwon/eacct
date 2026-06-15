package com.iljin.apiServer.ijeas.card;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CardKey implements Serializable {

    private static final long serialVersionUID = 3973090930745991200L;

    String compCd;

    String crdNo;

    public CardKey(String compCd, String crdNo) {
        this.compCd = compCd;
        this.crdNo = crdNo;
    }
}
