package com.iljin.apiServer.ijeas.system.payBank;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayBankKey implements Serializable {

    private static final long serialVersionUID = -8020828042774096258L;

    Long operatingUnitId;

    Long bankAccountId;

    public PayBankKey(Long operatingUnitId, Long bankAccountId) {
        this.operatingUnitId = operatingUnitId;
        this.bankAccountId = bankAccountId;
    }
}
