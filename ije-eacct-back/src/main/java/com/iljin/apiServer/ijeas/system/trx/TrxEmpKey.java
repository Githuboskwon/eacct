package com.iljin.apiServer.ijeas.system.trx;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TrxEmpKey implements Serializable {
    private static final long serialVersionUID = 7990489675015170130L;

    String compCd;
    String trxTypeCd;
    Integer personId;
}
