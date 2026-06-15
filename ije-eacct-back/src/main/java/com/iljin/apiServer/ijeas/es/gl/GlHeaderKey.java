package com.iljin.apiServer.ijeas.es.gl;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GlHeaderKey implements Serializable {

    private static final long serialVersionUID = -581640757440468869L;

    String compCd;

    String slipNo;

    String slipType;

    public GlHeaderKey(String compCd, String slipNo, String slipType) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
    }
}
