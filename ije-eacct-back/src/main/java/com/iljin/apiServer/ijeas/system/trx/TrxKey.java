package com.iljin.apiServer.ijeas.system.trx;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TrxKey implements Serializable {
    private static final long serialVersionUID = -7702828458589672544L;

    String compCd;
    String trxTypeCd;
}
