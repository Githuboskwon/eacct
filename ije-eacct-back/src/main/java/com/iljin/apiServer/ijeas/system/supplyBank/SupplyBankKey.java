package com.iljin.apiServer.ijeas.system.supplyBank;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SupplyBankKey implements Serializable {

    private static final long serialVersionUID = -8020828042774097258L;

    BigDecimal vendorId;

    String integrationVendorNum;

    BigDecimal bankAccountId;

    String bankNumber;

}
