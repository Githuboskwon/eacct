package com.iljin.apiServer.ijeas.es.fund;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SpTrFundHdKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039740L;

    String compCd;

    String slipNo;

    String slipType;
}
