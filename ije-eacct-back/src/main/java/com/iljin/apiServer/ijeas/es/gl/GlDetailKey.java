package com.iljin.apiServer.ijeas.es.gl;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GlDetailKey implements Serializable {

    private static final long serialVersionUID = 1797133887695761483L;

    String compCd;

    String slipNo;

    String slipType;

    Integer slipSeq;

    public GlDetailKey(String compCd, String slipNo, String slipType, Integer slipSeq) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipSeq = slipSeq;
    }
}
